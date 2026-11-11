// The one section of the SEO / AEO check that needs a browser: the language
// switcher. Its links are computed in the browser from the current path, so
// the served HTML can be right while the hydrated page is wrong. That is how
// it broke in November 2026: Netlify redirected /zh-Hans/ to /zh-hans/, the
// page hydrated with a baseUrl of /zh-Hans/, and the English link became
// //zh-hans/, which a browser reads as a host name.
//
// Drives headless Chrome over the DevTools protocol with what Node 22 ships
// (fetch, WebSocket): no dependency to install. Skipped when no Chrome or
// Chromium is found; set CHROME_PATH to point at one.

const fs = require('fs');
const os = require('os');
const net = require('net');
const path = require('path');
const { spawn } = require('child_process');

function findChrome() {
  const candidates = [
    process.env.CHROME_PATH,
    'C:/Program Files/Google/Chrome/Application/chrome.exe',
    'C:/Program Files (x86)/Google/Chrome/Application/chrome.exe',
    'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe',
    '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
    '/usr/bin/google-chrome',
    '/usr/bin/chromium',
    '/usr/bin/chromium-browser',
  ].filter(Boolean);
  return candidates.find((p) => fs.existsSync(p));
}

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

function freePort() {
  return new Promise((resolve, reject) => {
    const server = net.createServer();
    server.on('error', reject);
    server.listen(0, '127.0.0.1', () => {
      const { port } = server.address();
      server.close(() => resolve(port));
    });
  });
}

async function launch(chromePath) {
  const port = await freePort();
  const profile = fs.mkdtempSync(path.join(os.tmpdir(), 'seo-aeo-chrome-'));
  const child = spawn(chromePath, [
    '--headless=new', '--disable-gpu', '--no-first-run', '--no-default-browser-check',
    `--remote-debugging-port=${port}`, `--user-data-dir=${profile}`, '--window-size=1400,1000', 'about:blank',
  ], { stdio: 'ignore' });
  let wsUrl = null;
  for (let i = 0; i < 60 && !wsUrl; i++) {
    await sleep(250);
    try {
      wsUrl = (await (await fetch(`http://127.0.0.1:${port}/json/version`)).json()).webSocketDebuggerUrl;
    } catch (e) { /* not listening yet */ }
  }
  if (!wsUrl) {
    child.kill();
    throw new Error('Chrome did not open its DevTools port');
  }
  const ws = new WebSocket(wsUrl);
  await new Promise((resolve, reject) => {
    ws.addEventListener('open', resolve);
    ws.addEventListener('error', () => reject(new Error('cannot connect to Chrome')));
  });
  let nextId = 0;
  const pending = new Map();
  ws.addEventListener('message', (event) => {
    const msg = JSON.parse(event.data);
    if (!msg.id || !pending.has(msg.id)) return;
    const { resolve, reject } = pending.get(msg.id);
    pending.delete(msg.id);
    if (msg.error) reject(new Error(msg.error.message));
    else resolve(msg.result);
  });
  const send = (method, params = {}, sessionId) => new Promise((resolve, reject) => {
    const id = ++nextId;
    pending.set(id, { resolve, reject });
    ws.send(JSON.stringify({ id, method, params, sessionId }));
  });
  const { targetId } = await send('Target.createTarget', { url: 'about:blank' });
  const { sessionId } = await send('Target.attachToTarget', { targetId, flatten: true });
  await send('Page.enable', {}, sessionId);
  await send('Runtime.enable', {}, sessionId);

  async function evaluate(expression) {
    const { result, exceptionDetails } = await send('Runtime.evaluate', { expression, returnByValue: true }, sessionId);
    if (exceptionDetails) throw new Error(exceptionDetails.text);
    return result.value;
  }
  // Loads a URL and waits until Docusaurus has hydrated the page. Returns
  // the address the browser ended on, or an error text.
  async function open(url) {
    await send('Page.navigate', { url: 'about:blank' }, sessionId);
    const nav = await send('Page.navigate', { url }, sessionId);
    if (nav.errorText) return { error: nav.errorText };
    for (let i = 0; i < 120; i++) {
      await sleep(150);
      let ready = false;
      try {
        ready = await evaluate("document.readyState === 'complete' && document.documentElement.dataset.hasHydrated === 'true'");
      } catch (e) { /* the document is being replaced */ }
      if (ready) return { href: await evaluate('location.href') };
    }
    return { error: 'the page did not hydrate within 18 s', href: await evaluate('location.href').catch(() => null) };
  }
  async function close() {
    try { ws.close(); } catch (e) { /* already closed */ }
    child.kill();
    await sleep(300);
    try { fs.rmSync(profile, { recursive: true, force: true }); } catch (e) { /* Chrome still holds a file */ }
  }
  // What the next pages loaded will see as the browser's languages
  // ("en-AU,en") and time zone ("Australia/Sydney").
  async function emulate({ languages, timeZone }) {
    const userAgent = await evaluate('navigator.userAgent');
    await send('Emulation.setUserAgentOverride', { userAgent, acceptLanguage: languages }, sessionId);
    await send('Emulation.setTimezoneOverride', { timezoneId: timeZone }, sessionId);
  }
  return { open, evaluate, emulate, close, send: (method, params) => send(method, params, sessionId) };
}

// Every locale's page, loaded the way a visitor gets it (redirects
// included), must offer a working link to the same page in every locale.
async function switcher(ctx) {
  const { cfg, client, report: r, locales } = ctx;
  const chromePath = findChrome();
  if (!chromePath) {
    r.skip('language switcher: no Chrome or Chromium found (set CHROME_PATH)');
    return;
  }
  if (typeof WebSocket !== 'function') {
    r.skip('language switcher: this Node has no built-in WebSocket (Node 22 or later)');
    return;
  }
  const all = Object.keys(cfg.locales);
  const prefix = (l) => cfg.locales[l].prefix;
  const address = (l, p) => client.toTarget(cfg.siteUrl + prefix(l) + (p === '/' ? '/' : p));
  const same = (a, b) => a.replace(/\/+$/, '') === b.replace(/\/+$/, '');
  const browser = await launch(chromePath);
  try {
    for (const page of ['/', '/installing-stackql']) {
      for (const l of locales) {
        const tag = `[${l}] ${prefix(l)}${page}`;
        const opened = await browser.open(address(l, page));
        if (opened.error) {
          r.fail(`${tag} does not load in a browser: ${opened.error}`);
          continue;
        }
        const linksJson = await browser.evaluate(
          "JSON.stringify([...document.querySelectorAll('.navbar__items--right a.dropdown__link[lang]')].map((a) => ({ lang: a.lang, href: a.href })))",
        );
        const links = JSON.parse(linksJson);
        const wrong = [];
        for (const other of all) {
          const link = links.find((x) => x.lang === cfg.locales[other].htmlLang);
          const want = address(other, page);
          if (!link) wrong.push(`no ${other} link`);
          else if (!same(link.href, want)) wrong.push(`${other} link is ${link.href}, expected ${want}`);
        }
        const at = same(opened.href, address(l, page)) ? '' : ` (the browser ended on ${opened.href})`;
        r.check(wrong.length === 0, `${tag}: after hydration the language switcher links to this page in ${all.join(', ')}`, `${wrong.join('; ')}${at}`);
      }
    }
    // Walk through the switcher the way a visitor does: every ordered pair.
    const steps = [];
    for (const from of all) for (const to of all) if (from !== to) steps.push([from, to]);
    const broken = [];
    for (const [from, to] of steps) {
      const start = await browser.open(address(from, '/installing-stackql'));
      if (start.error) {
        broken.push(`${from} -> ${to}: ${from} page did not load (${start.error})`);
        continue;
      }
      const href = await browser.evaluate(
        `(document.querySelector('.navbar__items--right a.dropdown__link[lang="${cfg.locales[to].htmlLang}"]') || {}).href || ''`,
      );
      const landed = href ? await browser.open(href) : { error: 'no link' };
      if (landed.error) {
        broken.push(`${from} -> ${to}: ${href || 'no link'} (${landed.error})`);
        continue;
      }
      const lang = await browser.evaluate('document.documentElement.lang');
      if (lang !== cfg.locales[to].htmlLang || !same(landed.href, address(to, '/installing-stackql'))) {
        broken.push(`${from} -> ${to}: landed on ${landed.href} (lang ${lang})`);
      }
    }
    r.check(broken.length === 0, `switching language works for all ${steps.length} ordered pairs of ${all.join(', ')}`, broken.join('; '));

    // The English flag follows the visitor's region (src/lib/regionalFlags.js)
    // and stays the default when the region is not covered. Checked on the
    // English page, where it is the switcher's button, and on another
    // locale's page, where it is an entry of the menu.
    const wrong = [];
    for (const c of cfg.flagCases || []) {
      await browser.emulate(c);
      for (const l of [cfg.defaultLocale, all.find((x) => x !== cfg.defaultLocale)].filter(Boolean)) {
        const opened = await browser.open(address(l, '/installing-stackql'));
        const label = `${c.languages} in ${c.timeZone} on the ${l} page`;
        if (opened.error) {
          wrong.push(`${label}: ${opened.error}`);
          continue;
        }
        const seen = JSON.parse(await browser.evaluate(`JSON.stringify((() => {
          const el = document.querySelector('.navbar__items--right svg[data-flag-locale="${cfg.defaultLocale}"]');
          if (!el) return null;
          return {
            attr: document.documentElement.getAttribute('data-flag-${cfg.defaultLocale}'),
            background: getComputedStyle(el).backgroundImage,
            shapes: el.firstElementChild ? getComputedStyle(el.firstElementChild).display : null,
          };
        })())`));
        if (!seen) wrong.push(`${label}: no English flag in the switcher`);
        else if (c.flag) {
          const painted = seen.background.startsWith('url("data:image/svg+xml') && seen.shapes === 'none';
          if (seen.attr !== c.flag || !painted) wrong.push(`${label}: expected the ${c.flag} flag, got data-flag ${seen.attr}, background ${seen.background.slice(0, 40)}, default shapes ${seen.shapes}`);
        } else if (seen.attr || seen.background !== 'none' || seen.shapes === 'none') {
          wrong.push(`${label}: expected the default flag, got data-flag ${seen.attr}, background ${seen.background.slice(0, 40)}`);
        }
      }
    }
    if ((cfg.flagCases || []).length) {
      r.check(wrong.length === 0, `the English flag follows the browser's region in ${cfg.flagCases.length} cases (${cfg.flagCases.filter((c) => c.flag).map((c) => c.flag).join(', ')}) and is the default elsewhere, with no request for the artwork`, wrong.join('; '));
    }
  } finally {
    await browser.close();
  }
}

module.exports = { switcher, launch, findChrome };
