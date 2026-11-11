// The checks of the SEO / AEO harness, one function per section. Each takes
// the run context ({ cfg, client, report, target, locales, opts }) and
// records its findings on the report. Expectations live in ./config.js.

const fs = require('fs');
const path = require('path');
const lib = require('./lib');

function helpers(ctx) {
  const { cfg } = ctx;
  const loc = (l) => cfg.locales[l];
  const translated = (l, source) => cfg.sourcesFor(l).includes(source);
  // The canonical URL of a site path in a locale. '/' is the locale root,
  // which Docusaurus prints with a trailing slash.
  const pageUrl = (l, p) => cfg.siteUrl + loc(l).prefix + (p === '/' ? '/' : p);
  // The site path of a URL with the locale prefix removed.
  const sitePath = (l, url) => new URL(url).pathname.slice(loc(l).prefix.length) || '/';
  const otherPrefixes = (l) => Object.entries(cfg.locales).filter(([k, v]) => k !== l && v.prefix).map(([, v]) => v.prefix);
  // Whether a URL belongs to the locale's own copy of the site.
  const inLocale = (l, url) => {
    const root = cfg.siteUrl + loc(l).prefix;
    if (url !== root && !url.startsWith(root + '/')) return false;
    if (loc(l).prefix) return true;
    const p = new URL(url).pathname;
    return !otherPrefixes(l).some((prefix) => p === prefix || p.startsWith(prefix + '/'));
  };
  // Routes of the source sets that are not translated for the locale.
  const untranslatedRoutes = (l) => Object.entries(cfg.sourceRoutes)
    .filter(([source]) => !translated(l, source))
    .flatMap(([, routes]) => routes);
  const underRoute = (p, route) => p === route || p.startsWith(route + '/');
  const list = (items, n = 5) => items.slice(0, n).join(', ') + (items.length > n ? ` (+${items.length - n} more)` : '');
  return { loc, translated, pageUrl, sitePath, inLocale, untranslatedRoutes, underRoute, list };
}

const contentType = (res) => (res.headers.get('content-type') || '').toLowerCase();

// ---------------------------------------------------------------- robots

async function robots(ctx) {
  const { cfg, client, report: r, target } = ctx;
  const res = await client.get('/robots.txt');
  if (!r.check(res.status === 200, 'robots.txt is served', `status ${res.status}`)) return;
  const rules = lib.parseRobots(res.body);

  const paths = ['/', '/llms.txt', '/sitemap.xml'];
  for (const l of Object.values(cfg.locales)) if (l.prefix) paths.push(`${l.prefix}/`, `${l.prefix}/llms.txt`, `${l.prefix}/sitemap.xml`);
  const blocked = [];
  for (const crawler of [{ name: '*' }, ...cfg.crawlers]) {
    for (const p of paths) if (!lib.robotsAllows(rules, crawler.name, p)) blocked.push(`${crawler.name} on ${p}`);
  }
  const groups = [...new Set(cfg.crawlers.map((c) => c.group))].join(', ');
  r.check(blocked.length === 0, `robots.txt allows all ${cfg.crawlers.length} crawler tokens (${groups}) on every locale`, blocked.slice(0, 8).join('; '));

  for (const [name, l] of Object.entries(cfg.locales)) {
    const expected = `${cfg.siteUrl}${l.prefix}/sitemap.xml`;
    r.check(rules.sitemaps.includes(expected), `[${name}] robots.txt lists ${expected}`, 'no such Sitemap line');
  }
  for (const url of rules.sitemaps) {
    if (!url.startsWith(cfg.siteUrl + '/')) {
      r.info(`robots.txt lists a sitemap on another host (not fetched): ${url}`);
      continue;
    }
    const own = Object.values(cfg.locales).some((l) => url === `${cfg.siteUrl}${l.prefix}/sitemap.xml`);
    if (!own && !target.edge) {
      r.skip(`sitemap listed in robots.txt is served by the edge only: ${url}`);
      continue;
    }
    const s = await client.get(url);
    r.check(s.status === 200 && s.hops.length === 0, `sitemap listed in robots.txt resolves: ${url}`, `status ${s.status}${s.hops.length ? ' after a redirect' : ''}`);
  }
}

// -------------------------------------------------------------- sitemaps

async function sitemaps(ctx) {
  const { cfg, client, report: r, target, locales, opts } = ctx;
  const h = helpers(ctx);
  for (const l of locales) {
    const tag = `[${l}]`;
    const prefix = h.loc(l).prefix;
    const res = await client.get(`${prefix}/sitemap.xml`);
    if (!r.check(res.status === 200, `${tag} sitemap.xml is served`, `status ${res.status}`)) continue;
    if (target.edge) r.check(contentType(res).includes('xml'), `${tag} sitemap.xml content type is XML`, contentType(res));
    const urls = lib.parseSitemap(res.body);
    const locs = urls.map((u) => u.loc);
    const min = cfg.minimums.sitemapUrls[l] || 1;
    r.check(locs.length >= min, `${tag} sitemap lists ${locs.length} URLs (at least ${min} expected)`);

    const foreign = locs.filter((u) => !h.inLocale(l, u));
    r.check(foreign.length === 0, `${tag} every sitemap URL is on ${cfg.siteUrl}${prefix}/`, h.list(foreign));
    // Netlify answers a page URL that has upper-case letters with a 301 to
    // the lower-case form. A locale built under /zh-Hans/ therefore has only
    // redirects in its sitemap, and its pages run at a path that no longer
    // matches their baseUrl (which broke the language switcher).
    const upper = locs.filter((u) => /[A-Z]/.test(new URL(u).pathname));
    r.check(upper.length === 0, `${tag} every sitemap URL path is lower case (the edge redirects anything else)`, h.list(upper, 3));
    const dupes = locs.filter((u, i) => locs.indexOf(u) !== i);
    r.check(dupes.length === 0, `${tag} no URL is listed twice`, h.list(dupes));
    const forbidden = locs.filter((u) => h.inLocale(l, u) && cfg.sitemapForbidden.some((re) => re.test(h.sitePath(l, u))));
    r.check(forbidden.length === 0, `${tag} no list page, search page or redirect alias in the sitemap`, h.list(forbidden));
    const routes = h.untranslatedRoutes(l);
    const untranslated = locs.filter((u) => h.inLocale(l, u) && routes.some((route) => h.underRoute(h.sitePath(l, u), route)));
    r.check(untranslated.length === 0, `${tag} no untranslated (English fallback) page in the sitemap`, h.list(untranslated));
    const dated = urls.filter((u) => u.lastmod).length / Math.max(1, urls.length);
    r.check(dated >= cfg.minimums.lastmodCoverage, `${tag} ${(dated * 100).toFixed(0)}% of sitemap URLs carry <lastmod>`, `under ${cfg.minimums.lastmodCoverage * 100}% (pages with no git history have no date)`, 'warn');

    // Sampled URLs: each must be the page it claims to be.
    const picked = opts.all ? locs : lib.sample(locs, opts.sample);
    const problems = [];
    const descriptions = new Map();
    await Promise.all(picked.map(async (u) => {
      const page = await client.get(u);
      if (page.status !== 200) return problems.push(`${u} -> status ${page.status}`);
      if (target.edge && page.hops.length) return problems.push(`${u} -> redirects to ${page.hops[0].location}`);
      const canonical = (lib.links(page.body, 'canonical')[0] || {}).href;
      if (canonical !== u) problems.push(`${u} -> canonical is ${canonical}`);
      if (/noindex/i.test(lib.meta(page.body, 'robots') || '')) problems.push(`${u} -> noindex`);
      if (lib.htmlLang(page.body) !== h.loc(l).htmlLang) problems.push(`${u} -> lang ${lib.htmlLang(page.body)}`);
      const description = lib.meta(page.body, 'description');
      if (description) descriptions.set(description, [...(descriptions.get(description) || []), u]);
      return null;
    }));
    r.check(problems.length === 0, `${tag} ${picked.length} sampled sitemap URLs answer 200 directly, are self-canonical, indexable and in the right language`, h.list(problems, 6));
    const repeated = [...descriptions.values()].filter((v) => v.length > 1);
    r.check(repeated.length === 0, `${tag} sampled pages have distinct meta descriptions`, h.list(repeated.map((v) => v.join(' = ')), 3), 'warn');
  }
}

// ------------------------------------------------------------------ llms

function parseLlmsTxt(text) {
  const sections = [];
  const entries = [];
  let section = null;
  for (const line of text.split(/\r?\n/)) {
    const heading = line.match(/^## (.+)$/);
    if (heading) {
      section = heading[1].trim();
      sections.push(section);
      continue;
    }
    const entry = line.match(/^- \[(.*)\]\((\S+?)\)(?::\s*(.*))?$/);
    if (entry) entries.push({ section, title: entry[1], url: entry[2], description: entry[3] || '' });
  }
  return { sections, entries };
}

async function llms(ctx) {
  const { cfg, client, report: r, target, locales, opts } = ctx;
  const h = helpers(ctx);
  const english = parseLlmsTxt((await client.get(`${h.loc(cfg.defaultLocale).prefix}/llms.txt`)).body);

  for (const l of locales) {
    const tag = `[${l}]`;
    const prefix = h.loc(l).prefix;
    const res = await client.get(`${prefix}/llms.txt`);
    if (!r.check(res.status === 200 && res.hops.length === 0, `${tag} llms.txt is served`, `status ${res.status}`)) continue;
    if (target.edge) r.check(/text\/plain/.test(contentType(res)) && /utf-8/.test(contentType(res)), `${tag} llms.txt is text/plain; charset=utf-8`, contentType(res));
    r.check(/^# \S/.test(res.body) && /^> \S/m.test(res.body), `${tag} llms.txt opens with a title and a summary line`);

    const doc = parseLlmsTxt(res.body);
    const expected = [...cfg.llmsSections.filter((s) => h.translated(l, s.source)).map((s) => s.title), 'Optional'];
    r.check(JSON.stringify(doc.sections) === JSON.stringify(expected), `${tag} llms.txt sections are: ${expected.join(' | ')}`, `found ${doc.sections.join(' | ')}`);

    const entries = doc.entries.filter((e) => e.section !== 'Optional');
    const min = cfg.minimums.llmsEntries[l] || 1;
    r.check(entries.length >= min, `${tag} llms.txt lists ${entries.length} pages (at least ${min} expected)`);
    const foreign = entries.filter((e) => !h.inLocale(l, e.url) || !e.url.endsWith('.md')).map((e) => e.url);
    r.check(foreign.length === 0, `${tag} every llms.txt entry is a markdown URL on ${cfg.siteUrl}${prefix}/`, h.list(foreign));
    const routes = h.untranslatedRoutes(l);
    const untranslated = entries.filter((e) => h.inLocale(l, e.url) && routes.some((route) => h.underRoute(h.sitePath(l, e.url).replace(/\.md$/, ''), route))).map((e) => e.url);
    r.check(untranslated.length === 0, `${tag} llms.txt lists no untranslated (English) page`, h.list(untranslated));
    if (h.loc(l).script === 'cjk') {
      const latin = entries.filter((e) => lib.cjkCount(e.title + e.description) === 0);
      r.check(latin.length <= entries.length * 0.05, `${tag} llms.txt titles and descriptions are in Chinese (${entries.length - latin.length} of ${entries.length})`, h.list(latin.map((e) => e.title)));
    }
    const full = doc.entries.find((e) => e.section === 'Optional');
    r.check(full && full.url === `${cfg.siteUrl}${prefix}/llms-full.txt`, `${tag} llms.txt points at ${prefix}/llms-full.txt`, full ? full.url : 'no Optional entry');

    // The markdown companions the index points at.
    const picked = lib.sample(entries, Math.min(opts.all ? entries.length : opts.sample, entries.length));
    const problems = [];
    await Promise.all(picked.map(async (e) => {
      const md = await client.get(e.url);
      if (md.status !== 200 || md.hops.length) return problems.push(`${e.url} -> status ${md.status}${md.hops.length ? ' after a redirect' : ''}`);
      if (!/^# \S/.test(md.body)) problems.push(`${e.url} -> does not start with a "# title"`);
      const source = (md.body.match(/^Source: (\S+)/m) || [])[1];
      if (!source || !h.inLocale(l, source)) problems.push(`${e.url} -> Source line is ${source || 'missing'}`);
      if (target.edge && !(contentType(md).includes('text/markdown') && contentType(md).includes('utf-8'))) problems.push(`${e.url} -> content type ${contentType(md)}`);
      if (target.edge && /noindex/i.test(md.headers.get('x-robots-tag') || '')) problems.push(`${e.url} -> X-Robots-Tag noindex`);
      return null;
    }));
    r.check(problems.length === 0, `${tag} ${picked.length} sampled markdown companions are served${target.edge ? ' as text/markdown; charset=utf-8, indexable' : ''} and name their source page`, h.list(problems, 6));

    const fullRes = await client.get(`${prefix}/llms-full.txt`);
    if (r.check(fullRes.status === 200, `${tag} llms-full.txt is served`, `status ${fullRes.status}`)) {
      r.check(fullRes.bytes >= cfg.minimums.llmsFullBytes, `${tag} llms-full.txt is ${(fullRes.bytes / 1024).toFixed(0)} KB (at least ${cfg.minimums.llmsFullBytes / 1024} KB expected)`);
      if (target.edge) r.check(/text\/plain/.test(contentType(fullRes)) && /utf-8/.test(contentType(fullRes)), `${tag} llms-full.txt is text/plain; charset=utf-8`, contentType(fullRes));
      if (h.loc(l).script === 'cjk') {
        const ratio = lib.cjkRatio(fullRes.body);
        r.check(ratio >= cfg.minimums.cjkRatio, `${tag} llms-full.txt is in Chinese (${(ratio * 100).toFixed(0)}% CJK characters, code included)`);
      }
    }

    // No markdown copy of an untranslated page under the locale.
    if (l !== cfg.defaultLocale) {
      const leaks = [];
      for (const route of routes) {
        const sampleEntry = english.entries.find((e) => h.underRoute(new URL(e.url).pathname.replace(/\.md$/, ''), route) && new URL(e.url).pathname !== `${route}.md`);
        if (!sampleEntry) continue;
        const copy = `${prefix}${new URL(sampleEntry.url).pathname}`;
        const md = await client.get(copy);
        // the edge answers a missing locale path with the locale's 404 page
        if (md.status === 200 && /^# \S/.test(md.body)) leaks.push(copy);
      }
      r.check(leaks.length === 0, `${tag} no markdown copy of an untranslated English page under ${prefix}/`, h.list(leaks));
      if (target.edge) {
        const doubled = await client.get(`${prefix}${prefix}/installing-stackql.md`);
        r.check(doubled.status !== 200, `${tag} no companions under the doubled path ${prefix}${prefix}/ (AEO plugin older than 0.5.1)`, `status ${doubled.status}`);
      }
    }
  }
}

// ----------------------------------------------------------------- pages

async function pages(ctx) {
  const { cfg, client, report: r, target, locales } = ctx;
  const h = helpers(ctx);
  const allLocales = Object.keys(cfg.locales);
  const social = { total: 0, generic: 0, sample: '' };

  for (const l of locales) {
    const L = h.loc(l);
    for (const page of cfg.pages) {
      const url = h.pageUrl(l, page.path);
      const tag = `[${l}] ${L.prefix}${page.path}`;
      const isTranslated = h.translated(l, page.source);
      const indexable = !page.noindex && isTranslated;
      const res = await client.get(url);
      if (!r.check(res.status === 200, `${tag} answers 200`, `status ${res.status}`)) continue;
      if (target.edge) r.check(res.hops.length === 0, `${tag} is served at its canonical URL without a redirect`, res.hops.length ? `redirects to ${res.hops[0].location}` : '');
      const html = res.body;
      const issues = [];
      const warnings = [];

      // language, title, description
      if (lib.htmlLang(html) !== L.htmlLang) issues.push(`<html lang> is ${lib.htmlLang(html)}, expected ${L.htmlLang}`);
      const title = lib.title(html);
      if (!title) issues.push('no <title>');
      else if (!title.includes(cfg.siteName)) warnings.push(`<title> does not name ${cfg.siteName}: ${title}`);
      const description = lib.meta(html, 'description') || '';
      if (!description) (indexable ? issues : warnings).push('no meta description');
      else {
        const [min, max] = cfg.descriptionLength[lib.cjkCount(description) > 0 ? 'cjk' : 'latin'];
        if (description.length < min || description.length > max) warnings.push(`meta description is ${description.length} characters`);
      }
      // (a title can be a name that stays Latin, such as SELECT; the
      // description is always prose)
      if (indexable && L.script === 'cjk' && description && lib.cjkCount(description) === 0) issues.push('meta description is not in Chinese');

      // canonical, robots, hreflang
      const canonical = (lib.links(html, 'canonical')[0] || {}).href;
      if (canonical !== url) issues.push(`canonical is ${canonical}, expected ${url}`);
      const robotsMeta = (lib.meta(html, 'robots') || '').toLowerCase();
      if (indexable && (/noindex/.test(robotsMeta) || !/index/.test(robotsMeta))) issues.push(`robots meta is "${robotsMeta}", expected index,follow`);
      if (!indexable && !/noindex/.test(robotsMeta)) issues.push(`robots meta is "${robotsMeta}" on ${page.noindex ? 'a list page' : 'an untranslated page'}, expected noindex`);
      const alternates = {};
      for (const a of lib.links(html, 'alternate')) if (a.hreflang) alternates[a.hreflang] = a.href;
      for (const other of allLocales) {
        const want = h.pageUrl(other, page.path);
        if (alternates[cfg.locales[other].htmlLang] !== want) issues.push(`hreflang ${cfg.locales[other].htmlLang} is ${alternates[cfg.locales[other].htmlLang]}, expected ${want}`);
      }
      if (alternates['x-default'] !== h.pageUrl(cfg.defaultLocale, page.path)) issues.push(`hreflang x-default is ${alternates['x-default']}`);

      // social cards
      for (const key of ['og:title', 'og:description', 'og:image', 'og:url', 'og:locale', 'twitter:card']) {
        if (!lib.meta(html, key)) warnings.push(`no ${key}`);
      }
      const ogUrl = lib.meta(html, 'og:url');
      if (ogUrl && ogUrl !== url) warnings.push(`og:url is ${ogUrl}`);
      const ogImage = lib.meta(html, 'og:image');
      if (ogImage) {
        const image = await client.get(ogImage);
        if (image.status !== 200 || !contentType(image).startsWith('image/')) issues.push(`og:image does not resolve to an image: ${ogImage} (status ${image.status})`);
      }
      if (indexable) {
        social.total++;
        const twitterTitle = lib.meta(html, 'twitter:title');
        if (twitterTitle && twitterTitle !== lib.meta(html, 'og:title')) {
          social.generic++;
          social.sample = twitterTitle;
        }
      }

      // structured data
      const { nodes, errors } = lib.jsonLd(html);
      if (errors.length) issues.push(`JSON-LD does not parse: ${errors[0]}`);
      if (indexable) {
        const present = new Set(nodes.flatMap(lib.typesOf));
        const missing = page.types.filter((t) => !present.has(t));
        if (missing.length) issues.push(`JSON-LD has no ${missing.join(', ')}`);
        for (const type of ['WebPage', 'TechArticle', 'Article']) {
          for (const node of lib.nodesOfType(nodes, type)) {
            if (node.inLanguage !== L.inLanguage) issues.push(`${type}.inLanguage is ${node.inLanguage}, expected ${L.inLanguage}`);
          }
        }
        const webPage = lib.nodesOfType(nodes, 'WebPage')[0];
        if (webPage) {
          if (!lib.sameUrl(webPage.url || '', url)) issues.push(`WebPage.url is ${webPage.url}`);
          if (!webPage.speakable) warnings.push('WebPage has no speakable');
          if (!webPage.dateModified) warnings.push('WebPage has no dateModified');
        }
        for (const node of [...lib.nodesOfType(nodes, 'TechArticle'), ...lib.nodesOfType(nodes, 'Article')]) {
          for (const field of ['headline', 'dateModified', 'publisher']) if (!node[field]) warnings.push(`${lib.typesOf(node).join('+')} has no ${field}`);
        }
        for (const faq of lib.nodesOfType(nodes, 'FAQPage')) {
          const questions = [].concat(faq.mainEntity || []);
          if (questions.length === 0) issues.push('FAQPage has no questions');
          if (questions.some((q) => !q.name || !q.acceptedAnswer || !q.acceptedAnswer.text)) issues.push('FAQPage has a question without text or answer');
          if (L.script === 'cjk' && questions.some((q) => lib.cjkCount(`${q.name}${q.acceptedAnswer && q.acceptedAnswer.text}`) === 0)) issues.push('FAQPage questions are not in Chinese');
        }
        for (const crumbs of lib.nodesOfType(nodes, 'BreadcrumbList')) {
          const bad = [].concat(crumbs.itemListElement || []).map((i) => (i.item && (i.item['@id'] || i.item)) || '').filter((u) => typeof u === 'string' && u && !h.inLocale(l, u));
          if (bad.length) issues.push(`breadcrumb leaves the locale: ${bad[0]}`);
        }
      }

      // markdown companion
      const markdown = lib.links(html, 'alternate').filter((a) => a.type === 'text/markdown');
      const wantsCompanion = indexable && page.companion !== false;
      if (wantsCompanion) {
        const want = page.path === '/' ? `${cfg.siteUrl}${L.prefix}/index.md` : `${url}.md`;
        if (markdown.length !== 1 || markdown[0].href !== want) issues.push(`markdown alternate link is ${markdown.map((a) => a.href).join(', ') || 'missing'}, expected ${want}`);
        else {
          const md = await client.get(want);
          if (md.status !== 200 || !/^# \S/.test(md.body)) issues.push(`markdown companion ${want} -> status ${md.status}`);
        }
      } else if (markdown.length) {
        issues.push(`advertises a markdown copy it should not have: ${markdown[0].href}`);
      }

      // what a crawler that runs no JavaScript reads
      if (indexable && page.article) {
        if (page.h1 !== false && !lib.h1(html)) issues.push('no <h1> in the served HTML');
        const text = lib.mainText(html);
        if (text.length < cfg.minimums.mainTextChars) issues.push(`only ${text.length} characters of text in the served HTML (content needs JavaScript?)`);
        else if (L.script === 'cjk' && lib.cjkRatio(text) < cfg.minimums.cjkRatio) issues.push(`body text is ${(lib.cjkRatio(text) * 100).toFixed(0)}% Chinese: not translated?`);
      }

      const kind = indexable ? 'indexable' : page.noindex ? 'noindex list page' : 'noindex English fallback';
      r.check(issues.length === 0, `${tag} (${kind}): language, canonical, robots, hreflang, structured data, markdown twin, served text`, issues.join('; '));
      for (const w of warnings) r.warn(`${tag}: ${w}`);
    }
  }
  if (social.total) {
    r.check(social.generic === 0, 'twitter:title follows the page', `${social.generic} of ${social.total} indexable pages carry the same site-wide twitter:title ("${social.sample}") while og:title is per page; X shows the generic one`, 'warn');
  }
}

// ---------------------------------------------------------------- assets

// Third-party hosts a locale's pages depend on. Informational, except that
// a host known to be slow or unreachable from mainland China is a warning
// for a Chinese locale when it sits on the rendering path.
async function assets(ctx) {
  const { cfg, client, report: r, locales } = ctx;
  const h = helpers(ctx);
  const siteHost = new URL(cfg.siteUrl).host;
  for (const l of locales) {
    const tag = `[${l}]`;
    const res = await client.get(h.pageUrl(l, '/installing-stackql'));
    if (res.status !== 200) continue;
    const html = res.body;
    const found = new Map(); // host -> Set of how it is used
    const note = (url, how) => {
      let host;
      try { host = new URL(url, cfg.siteUrl).host; } catch (e) { return; }
      if (host === siteHost || host === new URL(client.base).host) return;
      found.set(host, (found.get(host) || new Set()).add(how));
    };
    for (const a of lib.tags(html, 'script')) if (a.src) note(a.src, 'async' in a || 'defer' in a || a.type === 'module' ? 'async script' : 'blocking script');
    for (const a of lib.tags(html, 'link')) {
      const rel = (a.rel || '').split(/\s+/);
      if (!a.href) continue;
      if (rel.includes('stylesheet')) note(a.href, 'stylesheet');
      else if (rel.includes('preconnect') || rel.includes('dns-prefetch')) note(a.href, 'preconnect hint');
    }
    for (const a of lib.links(html, 'stylesheet')) {
      if (!a.href || /^https?:/.test(a.href)) continue;
      const css = await client.get(a.href);
      if (css.status !== 200) continue;
      for (const m of css.body.matchAll(/@import\s+(?:url\()?["']?(https?:\/\/[^"')\s;]+)/g)) note(m[1], 'CSS @import (render blocking)');
    }
    const hosts = [...found.entries()].map(([host, how]) => `${host} (${[...how].join(', ')})`);
    r.info(`${tag} third-party hosts on a page: ${hosts.join('; ') || 'none'}`);
    if (h.loc(l).script === 'cjk') {
      const risky = [...found.entries()].filter(([host, how]) => cfg.mainlandUnreliableHosts.includes(host) && [...how].some((x) => x !== 'preconnect hint'));
      r.check(risky.length === 0, `${tag} nothing on the rendering path is hosted where mainland China reaches it poorly`, `${risky.map(([host, how]) => `${host} (${[...how].filter((x) => x !== 'preconnect hint').join(', ')})`).join('; ')}: cannot be measured from outside China`, 'warn');
    }
  }
}

// -------------------------------------------------------------- crawlers

// Every crawler gets what everyone else gets: same status, same bytes. A
// different answer means the edge blocks or challenges that crawler, or
// serves it something else.
async function crawlers(ctx) {
  const { cfg, client, report: r, target, locales } = ctx;
  const h = helpers(ctx);
  if (!target.edge) {
    r.skip('crawler user-agent parity is a property of the hosting edge: run with --target prod');
    return;
  }
  const paths = [...new Set(cfg.crawlerPaths.flatMap((p) => (p.includes('{l}') ? locales.map((l) => p.replace('{l}', h.loc(l).prefix)) : [p])))];
  const baseline = new Map();
  for (const p of paths) baseline.set(p, await client.get(p));
  const unusable = paths.filter((p) => baseline.get(p).status !== 200);
  if (unusable.length) r.warn(`not compared across crawlers (not 200 for a plain client): ${unusable.join(', ')}`);
  const compared = paths.filter((p) => baseline.get(p).status === 200);

  // A crawler that is blocked stays blocked, so a different answer is asked
  // for once more before it counts: the edge now and then answers a single
  // request with a 403 (seen minutes after a deploy), and that is a warning,
  // not a failure.
  const byGroup = new Map();
  for (const crawler of cfg.crawlers.filter((c) => c.ua)) {
    const differences = [];
    const once = [];
    await Promise.all(compared.map(async (p) => {
      const base = baseline.get(p);
      const differs = (x) => x.status !== base.status || x.hash !== base.hash;
      let res = await client.get(p, { ua: crawler.ua });
      if (differs(res)) {
        const first = res.status;
        await new Promise((resolve) => setTimeout(resolve, 2000));
        res = await client.get(p, { ua: crawler.ua, fresh: true });
        if (!differs(res)) return once.push(`${p} (status ${first} once)`);
      }
      if (res.status !== base.status) differences.push(`${p} -> status ${res.status}`);
      else if (res.hash !== base.hash) differences.push(`${p} -> different content (${res.bytes} bytes, ${base.bytes} for a plain client)`);
      return null;
    }));
    const group = byGroup.get(crawler.group) || { ok: [], bad: [], flaky: [] };
    byGroup.set(crawler.group, group);
    if (differences.length) group.bad.push(`${crawler.name}: ${h.list(differences, 3)}`);
    else group.ok.push(crawler.name);
    if (once.length) group.flaky.push(`${crawler.name}: ${h.list(once, 3)}`);
  }
  for (const [group, { ok, bad, flaky }] of byGroup) {
    if (ok.length) r.pass(`${group}: ${ok.join(', ')} get the same ${compared.length} responses as a plain client (not blocked, not cloaked)`);
    for (const b of bad) r.fail(`${group}: ${b} (same answer on a second try)`);
    for (const f of flaky) r.warn(`${group}: ${f}, then the normal response on a second try: a passing edge error, not a block`);
  }
}

// ------------------------------------------------------------------ edge

function parseNetlifyRedirects(text) {
  const rules = [];
  let generated = null;
  let current = null;
  for (const line of text.split(/\r?\n/)) {
    const begin = line.match(/^#\s*BEGIN generated ([a-z ]+?) redirects/i);
    if (begin) generated = begin[1];
    if (/^#\s*END generated/i.test(line)) generated = null;
    if (/^\[\[redirects\]\]/.test(line)) {
      current = { generated };
      rules.push(current);
      continue;
    }
    if (/^\[/.test(line)) current = null;
    const kv = line.match(/^\s*(from|to|status|force)\s*=\s*(.+?)\s*$/);
    if (current && kv) current[kv[1]] = kv[2].replace(/^"(.*)"$/, '$1');
  }
  return rules.filter((rule) => rule.from && rule.to);
}

async function edge(ctx) {
  const { cfg, client, report: r, target, locales, opts } = ctx;
  const h = helpers(ctx);
  if (!target.edge) {
    r.skip('redirect rules, response headers, per-locale 404 pages and the query library proxy exist at the hosting edge only: run with --target prod');
    return;
  }
  const absolute = (to) => (to.startsWith('/') ? cfg.siteUrl + to : to);

  // host, scheme and wildcard cases from the config
  for (const c of cfg.redirectCases) {
    const res = await client.get(c.from, { follow: false });
    r.check([301, 308].includes(res.status) && lib.sameUrl(res.location || '', absolute(c.to)), `${c.from} is a permanent redirect to ${c.to}`, `status ${res.status}, Location ${res.location}`);
  }

  // literal rules of netlify.toml
  const file = path.join(__dirname, '..', '..', 'netlify.toml');
  const rules = parseNetlifyRedirects(fs.readFileSync(file, 'utf8'))
    .filter((rule) => /^30[1278]$/.test(rule.status || '301') && !/[*:]/.test(rule.from));
  const handWritten = rules.filter((rule) => !rule.generated);
  const generated = [...new Set(rules.map((rule) => rule.generated).filter(Boolean))];
  const picked = [...handWritten];
  for (const name of generated) {
    const block = rules.filter((rule) => rule.generated === name);
    picked.push(...(opts.all ? block : lib.sample(block, cfg.generatedRedirectSample)));
  }
  const problems = [];
  await Promise.all(picked.map(async (rule) => {
    const res = await client.get(rule.from, { follow: false });
    const want = absolute(rule.to);
    if (String(res.status) !== String(rule.status || 301) || !lib.sameUrl(res.location || '', want)) {
      return problems.push(`${rule.from} -> status ${res.status}, Location ${res.location} (rule says ${rule.status} ${rule.to})`);
    }
    if (rule.to.startsWith('/')) {
      const dest = await client.get(want);
      if (dest.status !== 200 || dest.hops.length) problems.push(`${rule.from} -> ${rule.to} is ${dest.hops.length ? 'another redirect' : `status ${dest.status}`}`);
    }
    return null;
  }));
  r.check(problems.length === 0, `${picked.length} redirect rules of netlify.toml answer as written, in one hop (${handWritten.length} hand-written, ${picked.length - handWritten.length} sampled from the generated ${generated.join(' and ')} blocks)`, h.list(problems, 6));

  // a missing page under a locale gets that locale's 404 page
  for (const l of locales) {
    const res = await client.get(`${h.loc(l).prefix}/seo-aeo-check-no-such-page`);
    r.check(res.status === 404 && lib.htmlLang(res.body) === h.loc(l).htmlLang, `[${l}] a missing page answers 404 with the ${h.loc(l).htmlLang} error page`, `status ${res.status}, lang ${lib.htmlLang(res.body)}`);
  }

  // provider catalog (cross-repo contract) and the proxied query library
  const catalog = await client.get('/providers.json');
  let parsed = null;
  try { parsed = JSON.parse(catalog.body); } catch (e) { /* reported below */ }
  r.check(catalog.status === 200 && parsed && parsed.version === 1 && Array.isArray(parsed.categories) && parsed.categories.length > 0,
    '/providers.json is served, version 1, with its categories', `status ${catalog.status}`);
  r.check(contentType(catalog).includes('application/json') && catalog.headers.get('access-control-allow-origin') === '*',
    '/providers.json is application/json with Access-Control-Allow-Origin: *', `${contentType(catalog)}, ACAO ${catalog.headers.get('access-control-allow-origin')}`);
  const library = await client.get('/docs/query-library/');
  r.check(library.status === 200, 'the query library proxy answers at /docs/query-library/', `status ${library.status}`);
}

module.exports = { robots, sitemaps, llms, pages, assets, crawlers, edge, switcher: require('./browser').switcher };
