// Configuration of the SEO / AEO check (scripts/seo-aeo/check.js): what the
// site is expected to publish for search engines and answer engines, per
// locale, and which crawlers must be able to read it. Config, not code.
//
// What is translated per locale comes from scripts/i18n/config.js, so the
// expectations here follow the i18n setup: a page is expected to be
// indexable, listed and mirrored as markdown in a locale only when its
// source set is translated for that locale.

const i18n = require('../i18n/config.js');

module.exports = {
  // The canonical origin. Every URL the site prints (canonical, hreflang,
  // sitemap, llms.txt, JSON-LD) is absolute on this origin whatever the
  // server under test is, so the check maps it onto the target's base.
  siteUrl: 'https://stackql.io',
  siteName: 'StackQL',

  // --target <name>. `edge: true` turns on the checks that only the hosting
  // edge (Netlify) can satisfy: response headers from netlify.toml, redirect
  // rules, the proxied query library, per-locale 404 pages, and crawler
  // user-agent parity. `docusaurus serve` serves the files and nothing else.
  targets: {
    local: { base: 'http://localhost:3000', edge: false },
    prod: { base: 'https://stackql.io', edge: true },
  },

  defaultLocale: i18n.defaultLocale,

  // Keep in step with i18nConfig in docusaurus.config.js.
  //   prefix      URL path prefix of the locale ('' for the default)
  //   htmlLang    <html lang>, and the hreflang value
  //   inLanguage  JSON-LD inLanguage of the page-level nodes
  //   script      'cjk' when translated text must contain CJK characters
  locales: {
    en: { prefix: '', htmlLang: 'en', inLanguage: 'en-US', script: null },
    de: { prefix: '/de', htmlLang: 'de', inLanguage: 'de', script: null },
    fr: { prefix: '/fr', htmlLang: 'fr', inLanguage: 'fr', script: null },
    'zh-Hans': { prefix: '/zh-hans', htmlLang: 'zh-Hans', inLanguage: 'zh-Hans', script: 'cjk' },
  },

  // Source sets translated per locale (the default locale has all of them).
  sourcesFor(locale) {
    if (locale === i18n.defaultLocale) return Object.keys(i18n.sources);
    return (i18n.locales[locale] || {}).sources || [];
  },

  // Lower bounds, so a build that silently drops a section fails the check.
  minimums: {
    sitemapUrls: { en: 300, de: 150, fr: 150, 'zh-Hans': 200 },
    llmsEntries: { en: 300, de: 150, fr: 150, 'zh-Hans': 200 },
    llmsFullBytes: 300 * 1024,
    lastmodCoverage: 0.9,
    mainTextChars: 200,
    cjkRatio: 0.15,
  },

  // llms.txt sections, in order, and the source set each one is built from.
  llmsSections: [
    { title: 'AI Reference', source: 'ai-content' },
    { title: 'Documentation', source: 'docs' },
    { title: 'Blog - Product Announcements', source: 'blog-product' },
    { title: 'Blog - Provider Announcements', source: 'blog-providers' },
    { title: 'Blog - Tutorials', source: 'blog-tutorials' },
  ],

  // Route prefixes per source set, for "nothing untranslated is published
  // under a locale" checks on sitemaps, llms.txt and companions.
  sourceRoutes: {
    'ai-content': ['/ai'],
    'blog-product': ['/blog/product'],
    'blog-providers': ['/blog/providers'],
    'blog-tutorials': ['/blog/tutorials'],
    pages: ['/contact-us', '/privacy', '/terms', '/support'],
  },

  // Never in a sitemap, in any locale: list pages, search, and the alias
  // paths that are Netlify 301s (a redirect in a sitemap is reported by
  // Search Console as "Page with redirect"). Matched against the site path
  // with the locale prefix removed, so every pattern is anchored: there are
  // real pages at /blog/providers/..., /language-spec/registry and
  // /ai/tutorials.
  sitemapForbidden: [
    /^\/blog\/[^/]+\/tags(\/|$)/,
    /^\/blog\/[^/]+\/page\//,
    /^\/blog\/[^/]+\/(archive|authors)(\/|$)/,
    /^\/search$/,
    /^\/providers\/[^/]+$/,
    /^\/registry(\/|$)/,
    /^\/(install|downloads|stackqldocs|features|tutorials|cookbooks|stackql-deploy)$/,
    /^\/docs(\/|$)/,
  ],

  // Meta description length that draws a warning, in characters. Chinese
  // says as much in about half the characters.
  descriptionLength: { latin: [30, 320], cjk: [12, 160] },

  // Representative pages, checked in every locale.
  //   source     the source set the page belongs to (decides whether it is
  //              translated, hence indexable, in a locale)
  //   types      JSON-LD @types that must be present where it is indexable
  //   companion  false when the page has no markdown source to mirror
  //   noindex    true when the page is noindex in every locale
  //   h1         false when the theme renders no <h1> on that page
  //   article    true when the page has a body worth measuring
  pages: [
    { path: '/', source: 'docs', types: ['WebPage', 'WebSite', 'Organization', 'SoftwareApplication', 'SoftwareSourceCode', 'BreadcrumbList'], article: true },
    { path: '/installing-stackql', source: 'docs', types: ['TechArticle', 'WebPage', 'BreadcrumbList', 'Organization'], article: true },
    { path: '/language-spec/select', source: 'docs', types: ['TechArticle', 'WebPage', 'BreadcrumbList'], article: true },
    { path: '/command-line-usage/mcp', source: 'docs', types: ['TechArticle', 'WebPage'], article: true },
    { path: '/mcp', source: 'docs', types: ['TechArticle', 'WebPage'], article: true },
    { path: '/providers', source: 'docs', types: ['TechArticle', 'WebPage'] },
    { path: '/getting-started', source: 'docs', types: ['WebPage', 'BreadcrumbList'], companion: false },
    { path: '/ai', source: 'ai-content', types: ['WebPage', 'BreadcrumbList'], article: true },
    { path: '/ai/canonical-definitions/what-is-stackql', source: 'ai-content', types: ['TechArticle', 'FAQPage', 'WebPage', 'BreadcrumbList'], article: true },
    { path: '/ai/how-tos/use-stackql-with-ai-agents', source: 'ai-content', types: ['TechArticle', 'FAQPage', 'WebPage'], article: true },
    { path: '/blog/product/stackql-mcp-server-now-available', source: 'blog-product', types: ['Article', 'WebPage', 'BreadcrumbList'], article: true },
    { path: '/blog/product', source: 'blog-product', types: ['WebPage'], companion: false, h1: false },
    { path: '/blog', source: 'blog-product', types: ['WebPage'], companion: false },
    { path: '/contact-us', source: 'pages', types: ['WebPage', 'Organization'], companion: false },
    { path: '/blog/product/tags/mcp', source: 'blog-product', types: [], companion: false, noindex: true },
  ],

  // Crawlers that must be able to read the site. `ua` is a representative
  // user-agent string (the product token is what blocking rules match on);
  // an entry without one is a robots.txt token only. Review the list when a
  // lab publishes or renames a crawler.
  crawlers: [
    // Search engines, US and global
    { name: 'Googlebot', group: 'US search', ua: 'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)' },
    { name: 'Bingbot', group: 'US search', ua: 'Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)' },
    { name: 'DuckDuckBot', group: 'US search', ua: 'DuckDuckBot/1.1; (+http://duckduckgo.com/duckduckbot.html)' },
    { name: 'Applebot', group: 'US search', ua: 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15 (Applebot/0.1; +http://www.apple.com/go/applebot)' },
    // Model labs and answer engines, US
    { name: 'GPTBot', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; GPTBot/1.2; +https://openai.com/gptbot)' },
    { name: 'OAI-SearchBot', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; OAI-SearchBot/1.0; +https://openai.com/searchbot)' },
    { name: 'ChatGPT-User', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; ChatGPT-User/1.0; +https://openai.com/bot)' },
    { name: 'ClaudeBot', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; ClaudeBot/1.0; +claudebot@anthropic.com)' },
    { name: 'Claude-User', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; Claude-User/1.0; +Claude-User@anthropic.com)' },
    { name: 'Claude-SearchBot', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; Claude-SearchBot/1.0; +Claude-SearchBot@anthropic.com)' },
    { name: 'PerplexityBot', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; PerplexityBot/1.0; +https://perplexity.ai/perplexitybot)' },
    { name: 'Perplexity-User', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; Perplexity-User/1.0; +https://perplexity.ai/perplexity-user)' },
    { name: 'Google-Extended', group: 'US AI' },
    { name: 'Applebot-Extended', group: 'US AI' },
    { name: 'CCBot', group: 'US AI', ua: 'CCBot/2.0 (https://commoncrawl.org/faq/)' },
    { name: 'meta-externalagent', group: 'US AI', ua: 'meta-externalagent/1.1 (+https://developers.facebook.com/docs/sharing/webmasters/crawler)' },
    { name: 'Amazonbot', group: 'US AI', ua: 'Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; Amazonbot/0.1; +https://developer.amazon.com/support/amazonbot)' },
    // Search engines, China
    { name: 'Baiduspider', group: 'CN search', ua: 'Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)' },
    { name: 'Baiduspider-render', group: 'CN search', ua: 'Mozilla/5.0 (compatible; Baiduspider-render/2.0; +http://www.baidu.com/search/spider.html)' },
    { name: 'Sogou web spider', group: 'CN search', ua: 'Sogou web spider/4.0(+http://www.sogou.com/docs/help/webmasters.htm#07)' },
    { name: '360Spider', group: 'CN search', ua: 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36; 360Spider' },
    { name: 'YisouSpider', group: 'CN search', ua: 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 YisouSpider/5.0 Safari/537.36' },
    // Model labs and answer engines, China
    { name: 'Bytespider', group: 'CN AI', ua: 'Mozilla/5.0 (Linux; Android 5.0) AppleWebKit/537.36 (KHTML, like Gecko) Mobile Safari/537.36 (compatible; Bytespider; spider-feedback@bytedance.com)' },
    { name: 'ChatGLM-Spider', group: 'CN AI', ua: 'Mozilla/5.0 (compatible; ChatGLM-Spider/1.0; +https://chatglm.cn/)' },
    { name: 'PetalBot', group: 'CN AI', ua: 'Mozilla/5.0 (compatible;PetalBot;+https://webmaster.petalsearch.com/site/petalbot)' },
  ],

  // Paths requested once per crawler user-agent (edge targets only) and
  // compared with what a plain client gets: same status, same bytes. `{l}`
  // expands to each locale prefix.
  crawlerPaths: ['/robots.txt', '/sitemap.xml', '{l}/', '{l}/llms.txt', '{l}/installing-stackql', '{l}/installing-stackql.md'],

  // Hosts a page may load that are slow or unreachable from mainland China.
  // Listed as a warning for locales with script 'cjk': it cannot be measured
  // from outside China, only pointed at.
  mainlandUnreliableHosts: [
    'fonts.googleapis.com', 'fonts.gstatic.com', 'ajax.googleapis.com',
    'www.googletagmanager.com', 'www.google-analytics.com', 'www.google.com',
    'www.youtube.com', 'youtube.com', 'platform.twitter.com', 'connect.facebook.net',
  ],

  // Edge redirect cases that a literal rule in netlify.toml does not cover
  // (wildcard rules, host and scheme canonicalization).
  redirectCases: [
    { from: 'http://stackql.io/', to: 'https://stackql.io/' },
    { from: 'https://www.stackql.io/', to: 'https://stackql.io/' },
    { from: '/docs/installing-stackql', to: '/installing-stackql' },
    { from: '/docs/language-spec/select', to: '/language-spec/select' },
    { from: '/installing-stackql/', to: '/installing-stackql' },
  ],

  // How many literal rules to try from each generated block of netlify.toml
  // (every hand-written literal rule is tried).
  generatedRedirectSample: 8,

  // The regional variants of the English flag in the language switcher
  // (src/lib/regionalFlags.js), for the switcher section: the browser's
  // languages and time zone, and the variant expected (null: the default
  // flag). Add a case when a variant is added.
  flagCases: [
    { languages: 'en-US,en', timeZone: 'America/New_York', flag: null },
    { languages: 'en-AU,en', timeZone: 'America/New_York', flag: 'au' },
    { languages: 'en-US,en', timeZone: 'Australia/Sydney', flag: 'au' },
    // en-GB only counts when the time zone says nothing: a browser in
    // Melbourne that is set to British English is in Australia
    { languages: 'en-GB,en-US,en', timeZone: 'Australia/Melbourne', flag: 'au' },
    // and the other way around: en-AU decides wherever the machine is
    { languages: 'en-AU,en', timeZone: 'Europe/London', flag: 'au' },
    { languages: 'en-US,en', timeZone: 'Europe/London', flag: 'gb' },
    { languages: 'en-US,en', timeZone: 'Pacific/Auckland', flag: 'nz' },
    { languages: 'en-ZA,en', timeZone: 'UTC', flag: 'za' },
    { languages: 'en-GB,en', timeZone: 'Europe/Paris', flag: 'gb' },
    { languages: 'fr-FR,fr', timeZone: 'Europe/Paris', flag: null },
    { languages: 'en-CA,en', timeZone: 'America/Toronto', flag: null },
  ],
};
