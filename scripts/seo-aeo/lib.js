// Helpers for the SEO / AEO check: an HTTP client that maps the site's
// canonical URLs onto the server under test, small HTML / XML / robots.txt
// readers, and the result collector. No dependencies beyond Node 22.

const crypto = require('crypto');

const DEFAULT_UA = 'seo-aeo-check/1.0 (node)';

// ---------------------------------------------------------------- client

function createClient({ base, siteUrl, timeoutMs = 20000, concurrency = 8 }) {
  const baseOrigin = new URL(base).origin;
  const siteOrigin = new URL(siteUrl).origin;
  const cache = new Map();
  let active = 0;
  const waiting = [];
  let requests = 0;

  // A path, or an absolute URL on the canonical origin, becomes a URL on
  // the server under test. Anything else (another host, another scheme) is
  // requested as it is.
  function toTarget(urlOrPath) {
    if (urlOrPath.startsWith('/')) return baseOrigin + urlOrPath;
    const u = new URL(urlOrPath);
    return u.origin === siteOrigin ? baseOrigin + u.pathname + u.search : urlOrPath;
  }

  async function slot(fn) {
    if (active >= concurrency) await new Promise((resolve) => waiting.push(resolve));
    active++;
    try {
      return await fn();
    } finally {
      active--;
      const next = waiting.shift();
      if (next) next();
    }
  }

  async function fetchOnce(url, ua) {
    let lastError;
    for (let attempt = 0; attempt < 2; attempt++) {
      try {
        requests++;
        const res = await fetch(url, {
          redirect: 'manual',
          headers: { 'user-agent': ua, accept: '*/*' },
          signal: AbortSignal.timeout(timeoutMs),
        });
        const buffer = Buffer.from(await res.arrayBuffer());
        return { status: res.status, headers: res.headers, buffer };
      } catch (e) {
        lastError = e;
        await new Promise((resolve) => setTimeout(resolve, 400));
      }
    }
    throw new Error(`${url}: ${lastError.cause ? lastError.cause.code || lastError.cause.message : lastError.message}`);
  }

  // GET with redirects followed by hand, so every hop is visible.
  //   status, headers, body, hash   of the final response
  //   hops                          [{ url, status, location }] before it
  // With follow: false the first response is returned as it is. Responses
  // are remembered for the run; fresh: true asks the server again.
  function get(urlOrPath, { ua = DEFAULT_UA, follow = true, fresh = false } = {}) {
    const key = `${ua}\n${follow}\n${urlOrPath}`;
    if (fresh) cache.delete(key);
    if (!cache.has(key)) {
      cache.set(key, slot(async () => {
        const hops = [];
        let url = toTarget(urlOrPath);
        for (;;) {
          const res = await fetchOnce(url, ua);
          const location = res.headers.get('location');
          if (follow && res.status >= 300 && res.status < 400 && location && hops.length < 6) {
            const next = new URL(location, url).href;
            hops.push({ url, status: res.status, location: next });
            url = toTarget(next);
            continue;
          }
          return {
            requested: urlOrPath,
            url,
            status: res.status,
            headers: res.headers,
            location: location ? new URL(location, url).href : null,
            body: res.buffer.toString('utf8'),
            bytes: res.buffer.length,
            hash: crypto.createHash('sha1').update(res.buffer).digest('hex'),
            hops,
          };
        }
      }));
    }
    return cache.get(key);
  }

  return { get, toTarget, requestCount: () => requests, base: baseOrigin };
}

// ------------------------------------------------------------------ html

function decodeEntities(s) {
  return String(s)
    .replace(/&#x([0-9a-f]+);/gi, (_, h) => String.fromCodePoint(parseInt(h, 16)))
    .replace(/&#(\d+);/g, (_, d) => String.fromCodePoint(Number(d)))
    .replace(/&quot;/g, '"').replace(/&#39;|&apos;/g, "'")
    .replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&nbsp;/g, ' ')
    .replace(/&amp;/g, '&');
}

// Attributes of one start tag, names in lower case. Handles quoted and
// unquoted values (the HTML minifier drops quotes where it can).
function parseAttrs(tag) {
  const attrs = {};
  const re = /([^\s"'<>/=]+)(?:\s*=\s*(?:"([^"]*)"|'([^']*)'|([^\s"'=<>`]+)))?/g;
  const inner = tag.replace(/^<\s*[a-zA-Z0-9:-]+/, '').replace(/\/?>$/, '');
  let m;
  while ((m = re.exec(inner))) {
    const value = m[2] !== undefined ? m[2] : m[3] !== undefined ? m[3] : m[4] !== undefined ? m[4] : '';
    attrs[m[1].toLowerCase()] = decodeEntities(value);
  }
  return attrs;
}

function tags(html, name) {
  const out = [];
  const re = new RegExp(`<${name}\\b[^>]*>`, 'gi');
  let m;
  while ((m = re.exec(html))) out.push(parseAttrs(m[0]));
  return out;
}

function metaAll(html, key) {
  return tags(html, 'meta')
    .filter((a) => a.name === key || a.property === key)
    .map((a) => a.content || '');
}
const meta = (html, key) => metaAll(html, key)[0];

function links(html, rel) {
  return tags(html, 'link').filter((a) => (a.rel || '').split(/\s+/).includes(rel));
}

function htmlLang(html) {
  const m = html.match(/<html\b[^>]*>/i);
  return m ? parseAttrs(m[0]).lang : undefined;
}

function title(html) {
  const m = html.match(/<title\b[^>]*>([\s\S]*?)<\/title>/i);
  return m ? decodeEntities(m[1]).trim() : '';
}

function stripTags(html) {
  return decodeEntities(
    html
      .replace(/<(script|style|svg|noscript|template)\b[\s\S]*?<\/\1>/gi, ' ')
      .replace(/<[^>]+>/g, ' '),
  ).replace(/\s+/g, ' ').trim();
}

function h1(html) {
  const m = html.match(/<h1\b[^>]*>([\s\S]*?)<\/h1>/i);
  return m ? stripTags(m[1]) : '';
}

// The readable text of the page as the server sent it (no JavaScript run),
// without code blocks: what a crawler that does not render gets to read.
function mainText(html) {
  const m = html.match(/<main\b[\s\S]*<\/main>/i) || html.match(/<article\b[\s\S]*<\/article>/i);
  const scope = m ? m[0] : html;
  return stripTags(scope.replace(/<(pre|code)\b[\s\S]*?<\/\1>/gi, ' '));
}

// Every JSON-LD node of the page (the members of @graph, or the object).
function jsonLd(html) {
  const nodes = [];
  const errors = [];
  const re = /<script\b[^>]*type=["']?application\/ld\+json["']?[^>]*>([\s\S]*?)<\/script>/gi;
  let m;
  while ((m = re.exec(html))) {
    try {
      const data = JSON.parse(m[1]);
      for (const item of Array.isArray(data) ? data : [data]) {
        nodes.push(...(Array.isArray(item['@graph']) ? item['@graph'] : [item]));
      }
    } catch (e) {
      errors.push(e.message);
    }
  }
  return { nodes, errors };
}

const typesOf = (node) => [].concat(node['@type'] || []);
const nodesOfType = (nodes, type) => nodes.filter((n) => typesOf(n).includes(type));

// --------------------------------------------------------- text and urls

function cjkCount(text) {
  const m = text.match(/[㐀-鿿]/g);
  return m ? m.length : 0;
}
function cjkRatio(text) {
  const visible = text.replace(/\s+/g, '');
  return visible.length === 0 ? 0 : cjkCount(visible) / visible.length;
}

// n items spread evenly over the list, the same ones on every run.
function sample(list, n) {
  if (n >= list.length) return list.slice();
  const out = [];
  for (let i = 0; i < n; i++) out.push(list[Math.floor((i * list.length) / n)]);
  return out;
}

// URL equality that ignores a trailing slash on anything but the root.
function normalizeUrl(url) {
  try {
    const u = new URL(url);
    const path = u.pathname.length > 1 ? u.pathname.replace(/\/+$/, '') : u.pathname;
    return u.origin + path + u.search;
  } catch (e) {
    return url;
  }
}
const sameUrl = (a, b) => normalizeUrl(a) === normalizeUrl(b);

// --------------------------------------------------------------- robots

function parseRobots(text) {
  const groups = [];
  const sitemaps = [];
  let current = null;
  let lastWasAgent = false;
  for (const raw of text.split(/\r?\n/)) {
    const line = raw.replace(/#.*$/, '').trim();
    const m = line.match(/^([A-Za-z-]+)\s*:\s*(.*)$/);
    if (!m) continue;
    const field = m[1].toLowerCase();
    const value = m[2].trim();
    if (field === 'user-agent') {
      if (!lastWasAgent) {
        current = { agents: [], rules: [] };
        groups.push(current);
      }
      current.agents.push(value.toLowerCase());
      lastWasAgent = true;
      continue;
    }
    lastWasAgent = false;
    if (field === 'sitemap') sitemaps.push(value);
    else if ((field === 'allow' || field === 'disallow') && current) current.rules.push({ type: field, path: value });
  }
  return { groups, sitemaps };
}

// Whether `token` may fetch `path`: the group naming the token, otherwise
// the `*` group; the longest matching rule decides, Allow winning a tie.
function robotsAllows(robots, token, path) {
  const name = token.toLowerCase();
  let groups = robots.groups.filter((g) => g.agents.includes(name));
  if (groups.length === 0) groups = robots.groups.filter((g) => g.agents.includes('*'));
  let best = null;
  for (const rule of groups.flatMap((g) => g.rules)) {
    if (rule.path === '') continue; // "Disallow:" with no path allows everything
    const pattern = new RegExp('^' + rule.path.replace(/[.+?^${}()|[\]\\]/g, '\\$&').replace(/\*/g, '.*').replace(/\\\$$/, '$'));
    if (!pattern.test(path)) continue;
    if (!best || rule.path.length > best.path.length || (rule.path.length === best.path.length && rule.type === 'allow')) best = rule;
  }
  return !best || best.type === 'allow';
}

// -------------------------------------------------------------- sitemap

function parseSitemap(xml) {
  const urls = [];
  const re = /<url>([\s\S]*?)<\/url>/g;
  let m;
  while ((m = re.exec(xml))) {
    const loc = (m[1].match(/<loc>([\s\S]*?)<\/loc>/) || [])[1];
    const lastmod = (m[1].match(/<lastmod>([\s\S]*?)<\/lastmod>/) || [])[1];
    if (loc) urls.push({ loc: decodeEntities(loc.trim()), lastmod: lastmod ? lastmod.trim() : null });
  }
  return urls;
}

// --------------------------------------------------------------- report

const LEVELS = ['fail', 'warn', 'info', 'skip', 'pass'];

function createReport() {
  const results = [];
  let section = '';
  const add = (level) => (message) => results.push({ section, level, message });
  return {
    results,
    setSection(name) { section = name; },
    pass: add('pass'),
    fail: add('fail'),
    warn: add('warn'),
    info: add('info'),
    skip: add('skip'),
    // pass when `ok`, otherwise fail (or warn) with the detail appended
    check(ok, message, detail, level = 'fail') {
      results.push({ section, level: ok ? 'pass' : level, message: ok || !detail ? message : `${message}: ${detail}` });
      return Boolean(ok);
    },
    count(level, name) {
      return results.filter((r) => r.level === level && (!name || r.section === name)).length;
    },
  };
}

module.exports = {
  DEFAULT_UA, LEVELS,
  createClient, createReport,
  parseAttrs, tags, meta, metaAll, links, htmlLang, title, h1, mainText, stripTags, jsonLd, typesOf, nodesOfType,
  cjkCount, cjkRatio, sample, normalizeUrl, sameUrl,
  parseRobots, robotsAllows, parseSitemap,
};
