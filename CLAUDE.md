# CLAUDE.md - stackql.io

Project guide for Claude Code working in this repository. Tells you what this site is, how the AEO platform is wired together, where the dependencies live, and the conventions to follow.

## What this site is

stackql.io is the marketing and documentation site for StackQL, built on Docusaurus 3.10. Three audiences:

- **Humans** - default site nav. This is a docs-only site: the docs tree is the site root (`/installing-stackql`, `/providers`, `/getting-started/*`, `/command-line-usage/*`, `/mcp/*`, `/language-spec/*`, `/developers/*`, `/quick-starts/*`) and [docs/index.md](docs/index.md) is the homepage. Blog at `/blog/<section>/*` (three sections, see "The blog" below). Four top-level pages remain: `/privacy`, `/terms` and `/support` (MDX in `src/pages`) and `/contact-us` (a React page: community channels, email, phone and address). The three MDX pages are the privacy, terms and support URLs that the ChatGPT and Claude plugin directory listings point at (issue #305), so keep those paths stable. There are no meta-refresh stub pages: every alias URL is a Netlify 301 (see "Redirects are Netlify 301s, never pages" below).
- **AI agents and answer engines** - a parallel content surface at `/ai/*` (canonical definitions, comparisons, how-tos, FAQs, troubleshooting, etc.) reachable by deep link, via `llms.txt` and from one "AI Reference" link in the footer, but **not** from the header nav. The footer link exists so the tree has a crawl path: with no internal link at all Google left its 44 sitemap URLs as "Discovered - currently not indexed".
- **LLM crawlers** - `llms.txt` and `llms-full.txt` at site root; a plain-markdown twin (`/foo.md`) for every doc and blog page, advertised from each page by `<link rel="alternate" type="text/markdown">`.

The two surfaces serve the same URLs to all visitors (no UA-based cloaking). The `/ai/*` pages just don't appear in the header nav.

## Redirects are Netlify 301s, never pages

Every alias or retired URL on this site is a `[[redirects]]` rule in [netlify.toml](netlify.toml), not a built page. Do not add a `src/pages` stub or a plugin route that renders `<meta http-equiv="refresh">`: a meta-refresh page is a 200 that the sitemap plugin lists and that Google reports as "Page with redirect" (Search Console flagged every one of them in October 2026), and it carries conflicting signals (`robots: index,follow`, a canonical, and a refresh). An edge 301 is neither built nor listed. The former stubs (`/install`, `/downloads`, `/stackqldocs`, `/tutorials`, `/cookbooks`, `/stackql-deploy` -> stackql-deploy.io, `/features` -> `/`, `/providers/<slug>`, `/registry/<name>`) are all rules now; the navbar and footer link to stackql-deploy.io directly. `/contact-us` was a stub to GitHub Discussions and is a real page now ([src/pages/contact-us.js](src/pages/contact-us.js), data in [src/data/contact-us/index.js](src/data/contact-us/index.js)): agents landing on it read contact details as text rather than an empty page.

Two blocks of rules are generated and must not be edited by hand:

- `BEGIN/END generated provider redirects` - [scripts/generate-provider-redirects.js](scripts/generate-provider-redirects.js), from the provider catalog. Rerun after changing [src/configs/providers.json](src/configs/providers.json).
- `BEGIN/END generated blog redirects` - [scripts/generate-blog-redirects.js](scripts/generate-blog-redirects.js), from blog front matter slugs. See "The blog".

Netlify serves a matching static file in preference to a rule unless the rule has `force = true`, so a rule only works for a path that is not also built. If something must link to an alias path from inside the site (it should not need to), use `pathname://` so `onBrokenLinks: 'throw'` does not reject it.

## Tech stack snapshot

- Docusaurus 3.10.1, classic preset
- React 18 (pinned - several deps require it)
- MUI 5/6 (already in deps - Material UI and emotion)
- Node 22 toolchain, yarn is the package manager (yarn.lock is the only lockfile - package-lock.json is gitignored)
- Deployed via Netlify, build = `npm run build`, publish = `build/`
- Search via Algolia DocSearch (`ALGOLIA_APP_ID`, `ALGOLIA_API_KEY`, `ALGOLIA_INDEX_NAME` env vars required for prod builds)

## Docs at the site root

The classic preset's docs instance has `routeBasePath: '/'` (Docusaurus docs-only mode). [docs/index.md](docs/index.md) carries `slug: /` and is the homepage; there is no `src/pages/index.js`. The marketing homepage was collapsed into the docs site in August 2025, and until October 2026 the root was a meta-refresh stub to `/docs`, which is why Google indexed `/docs` as the home and showed no sitelinks.

- Inbound `/docs/*` links are Netlify 301s to the same path without the prefix (`/docs` -> `/`, `/docs.md` -> `/index.md`, `/docs/*` -> `/:splat`). Those rules sit below the `/docs/query-library/*` proxy and the specific legacy `/docs/...` rules in [netlify.toml](netlify.toml). Netlify applies the first matching rule, so keep that order.
- The query library stays proxied at `/docs/query-library/*`: it is a separate site whose `baseUrl` is a cross-repo contract (see "The query library" below). It is the only thing left under `/docs`.
- Internal links use root paths (`/installing-stackql`, never `/docs/installing-stackql`). `onBrokenLinks: 'throw'` catches relative mistakes; absolute `stackql.io/docs/...` links need a grep.
- Root-level routes that are not docs: `/blog/*`, `/ai/*`, `/privacy`, `/terms`, `/support`, `/contact-us`, `/search`, and the Netlify 301 aliases (`/install`, `/downloads`, `/stackqldocs`, `/stackql-deploy`, `/features`, `/tutorials`, `/cookbooks`, `/providers/<slug>`, `/registry/*`). A new doc whose slug collides with one of these would clash with it (a built file shadows a Netlify rule), so check before adding root-level docs.
- Getting Started and Command Line Usage are sidebar categories with generated index pages (`/getting-started`, `/command-line-usage`), so every major section has a landing page Google can surface as a sitelink: installation, providers, getting started, command line usage, MCP.

## Internationalization

The site is built once per locale from `i18nConfig` in [docusaurus.config.js](docusaurus.config.js): `en` at the root and every other locale under its own path: `de` at `/de/` and `zh-Hans` at `/zh-hans/` today; `ja`, `ko` and `pl` are planned, in that order. It is standard Docusaurus i18n (`i18n/<locale>/<plugin name>[-<instance id>]/...`, locale dropdown, `hreflang` and `og:locale` per page, one SPA and one sitemap per locale) with these site-specific pieces:

- **What is translated is per locale**, in [scripts/i18n/config.js](scripts/i18n/config.js) (`locales.<l>.sources`): `de` translates `docs` only; `zh-Hans` translates `docs` and `ai-content`. The AI Reference (`ai-content`) exists for answer engines, whose crawlers read English and Chinese, so it is translated for `zh-Hans` only; the blog and the three MDX pages under `src/pages` are not translated in any locale. Anything not translated is served in English at the localized URL (Docusaurus falls back to the default locale) and marked `noindex` by the wrappers in [src/theme/DocItem/Content](src/theme/DocItem/Content/index.js), [src/theme/BlogPostPage](src/theme/BlogPostPage/index.js), [src/theme/BlogListPage](src/theme/BlogListPage/index.js), [src/theme/BlogArchivePage](src/theme/BlogArchivePage/index.js) and the blog landing page. The fallback is detected from `metadata.source`: a translated page's source is under `@site/i18n/` ([src/lib/i18n.js](src/lib/i18n.js)). A translated doc shows a "Translated page" notice linking to the English page; `hide_translation_notice: true` in the front matter hides it.
- **What crawlers get per locale follows from what is translated**: every locale has its own `sitemap.xml`, `llms.txt`, `llms-full.txt` and `.md` companions, and each covers only the pages that exist in that language. English and Simplified Chinese are the languages of the search engines and model labs the site targets, so they carry the full surface including the AI Reference; a locale such as `de` costs nothing extra and publishes the same files for its translated docs only. The rule that matters is that no English copy of an untranslated page is published under `/<locale>/` where it would compete with the English original: before November 2026 `/de/llms.txt` listed 321 pages of which about 155 were English (the AI Reference and every blog post), each with an indexable `.md`. The sitemap needs no configuration (untranslated pages are noindex and the sitemap plugin leaves noindex pages out). For the answer engine files, `configForLocale()` at the end of [docusaurus.config.js](docusaurus.config.js) reads `locales.<l>.sources` from [scripts/i18n/config.js](scripts/i18n/config.js) and adds the routes of the untranslated source sets (`/ai/**`, `/blog/<section>/**`) to the AEO plugin's `companions.exclude`; `llms.txt` and `llms-full.txt` are built from the companions, so they follow, and the Ask AI button only shows on pages that have a companion. It also drops the footer "AI Reference" link for a locale the AI Reference is not translated for. The config is exported as a function for this: `docusaurus build` sets `DOCUSAURUS_CURRENT_LOCALE` before loading the config for each locale, but the module is evaluated once per process, so a top-level read of that variable keeps the first locale's value for every locale (the exported function is called on every load). [static/robots.txt](static/robots.txt) lists one sitemap per locale by hand, and [netlify.toml](netlify.toml) repeats the `/llms.txt` and `/llms-full.txt` header rules per locale.
- **Translations are generated by Claude Code**, interactively or headless, following [.claude/skills/i18n-translate/SKILL.md](.claude/skills/i18n-translate/SKILL.md) and [i18n/glossary.md](i18n/glossary.md) (terminology and style per locale). Text only: code fences (SQL, commands, mermaid diagrams), inline code, links, imports, JSX and images are never translated, and images are shared by every locale. Every heading in `docs/` and `ai-content/` carries an explicit `{#id}` (`yarn write-heading-ids . docs ai-content`, rerun for new pages) so anchors are identical in every locale; `onBrokenAnchors: 'throw'` enforces it. The exception is a heading indented inside a JSX block (the `## macOS` headings inside `<TabItem>` on the installation page): there MDX reads `{#id}` as an expression and the build fails, so those headings keep their generated anchor, which differs per locale; do not link to them across pages. Imports in a doc must use the `@site/...` alias, never a relative path, because the translated copy lives in another directory. Content files under `docs/`, `ai-content/` and `i18n/` are LF ([.gitattributes](.gitattributes)) so hashes and diffs agree across platforms.
- **Bookkeeping is `i18n/manifest.json`**, written by `yarn i18n:mark --locale <l>`: per locale and source file, the git blob SHA of the English source the translation was made from. `yarn i18n:status` compares it with the working tree (`git hash-object`, so it works on uncommitted edits and survives rebases and squashes) and lists new, changed, unverified, orphan and deleted pages; `--staged` looks at the index, `--check` exits 1 when anything is pending, `--json` is for scripts. `yarn i18n:diff --locale <l> <file>` prints what changed in the English source since the translation, so a changed page is patched rather than retranslated. `yarn i18n:verify --locale <l> [files]` checks a translation's structure against its source (front matter keys and protected values, imports, code fences byte for byte, link targets, heading levels and ids, JSX tags, admonitions) and `yarn i18n:mark` records only files that pass. `yarn i18n:prune` removes translations whose source is gone.
- **UI strings**: `yarn i18n:strings` runs `docusaurus write-translations` per locale (navbar, footer, sidebar categories and generated-index descriptions, blog instance titles, every `<Translate>` / `translate()` in `src/` and in the AEO plugin's theme), reports keys that are still English, and drops the keys in `stringKeysUseDefault` (footer copyright, logo alt, version label) so they keep the computed English value. Messages are translated in place in `i18n/<locale>/**/*.json`; keys whose English text is right for a locale (product and vendor names) go in `locales.<l>.sameAsEnglish`. The command parks `src/modules/react-animated-term/node_modules` outside `src/` while it runs (`extractorExcludes`): the extractor globs all of `src/` and that vendored tree (36k committed files, unused) does not parse.
- **Pre-commit hook**: [.githooks/pre-commit](.githooks/pre-commit), installed by `yarn install` through [scripts/i18n/install-hooks.js](scripts/i18n/install-hooks.js) (sets `core.hooksPath`), runs `scripts/i18n/precommit.js`. In `warn` mode (`precommit` in config.js, the default) it lists the staged pages whose translations will need updating; in `translate` mode (or `I18N_PRECOMMIT=translate`) it runs the headless translator on them and stages the results into the same commit. It never blocks a commit; `I18N_PRECOMMIT=off` or `--no-verify` skips it. `yarn i18n:translate --locale <l>` is the headless path: one `claude -p "/i18n-translate <file> <locale>"` per page with the subscription login Claude Code already has, then verify and mark. On this Windows machine the CLI is installed in WSL, so the driver runs it as `wsl -e claude` (`headless.command` in config.js; `-e` matters, a plain `wsl claude` hands the tool rules to bash, which chokes on the parentheses) and the WSL install must be logged in (`claude /login` once). The interactive path is a Claude Code session working through `yarn i18n:status` with the same skill, fanning pages out to parallel subagents.
- **Chrome**: the locale dropdown is a copy of theme-classic's `LocaleDropdownNavbarItem` in [src/theme/NavbarItem/LocaleDropdownNavbarItem](src/theme/NavbarItem/LocaleDropdownNavbarItem/index.js) showing a flag and short code per locale (add an entry to its `flags.js` per new locale). [plugins/blog-landing](plugins/blog-landing/index.js) prefixes its route with the locale base URL and takes the section titles from the (localized) blog instances; [plugins/provider-catalog](plugins/provider-catalog/index.js) writes `providers.json` on the default locale only.
- **AEO per locale**: `@stackql/docusaurus-plugin-aeo` >= 0.5.1 emits `/<locale>/foo.md`, `/<locale>/llms.txt` and `/<locale>/llms-full.txt` from the translated sources (older versions wrote them to `build/<locale>/<locale>/`; the published 0.5.0 did that on the live German site until the pins were bumped in November 2026), and the sitemap plugin writes `/<locale>/sitemap.xml`. `@stackql/docusaurus-plugin-structured-data` >= 1.7.1 sets `inLanguage` to the locale's `htmlLang`, and [netlify.toml](netlify.toml) has a `/<locale>/* -> /<locale>/404.html 404` rule per locale.
- **Search**: `algolia.contextualSearch` stays `false` until the DocSearch crawler config is confirmed to record the `lang` facet (with it on and no facet, every search returns nothing); with it off, results mix languages once the crawler has indexed `/de/` and `/zh-hans/`.
- **Locale URL paths are lower case**: Netlify answers a page URL that has upper-case letters with a 301 to its lower-case form (`/INSTALLING-STACKQL` -> `/installing-stackql`). The Docusaurus default path for `zh-Hans` is `/zh-Hans/`, and the first deploy (November 2026) therefore had every Chinese canonical, hreflang and sitemap URL answering with a redirect, and a broken language switcher: the page ran at `/zh-hans/` with a baseUrl of `/zh-Hans/`, theme-common strips the baseUrl from the path case-sensitively, and the English link came out as `//zh-hans/`, which a browser reads as a host name. The locale now sets `baseUrl: '/zh-hans/'` in `i18nConfig.localeConfigs` (the build goes to `build/zh-hans/`); the locale code, the `i18n/zh-Hans/` directory, `lang` and `hreflang` stay `zh-Hans`. [docusaurus.config.js](docusaurus.config.js) throws at load if a locale prefix has an upper-case letter, and `yarn seo:check` (see README, "SEO and AEO checks") checks the sitemap paths and drives the language switcher in headless Chrome. A locale whose code has upper-case letters needs the same `baseUrl`.
- **Simplified Chinese specifics**: the `zh-Hans` section of [i18n/glossary.md](i18n/glossary.md) fixes the terminology (a StackQL provider is "Provider", untranslated; a cloud vendor is 云服务商), full-width punctuation in prose, and a space between Chinese and Latin text. `yarn i18n:verify` warns about `length ratio` on most Chinese pages (Chinese is shorter); that warning is expected. Verify does not catch emphasis that fails to close: in CommonMark a closing `**` that follows full-width punctuation and is followed directly by a Chinese character stays literal, so punctuation goes outside the markers (`**适用场景**：`) or a space follows them. After a Chinese pass, compare the count of bold, italic, link and inline-code nodes per page with the English source (parse both with `mdast-util-from-markdown`, which is in `node_modules`). Link text that is another page's title must be that page's translated `title`.
- **Adding a locale**: add it to `i18nConfig` (label, `htmlLang`) and to `scripts/i18n/config.js` (`locales.<l>.sources`), a glossary section, a flag, the robots sitemap line and the Netlify entries (the 404 rule and the two `llms.txt` header rules); what the locale's `llms.txt` and companions cover needs no configuration, it follows `sources`; run `yarn i18n:strings --locale <l>` and translate the string files; run the full content pass (a session with subagents, or `yarn i18n:translate --locale <l> --all`); `yarn i18n:verify` and `yarn i18n:mark`; build, serve and run `yarn seo:check` (add the locale to [scripts/seo-aeo/config.js](scripts/seo-aeo/config.js) first), then run it again with `--target prod` once deployed. Enable a locale in `i18nConfig` only once its full pass is done.

## AEO architecture

Two custom plugins published to npm and one secondary content-docs instance handle the AEO pipeline. Both plugins are in sibling repos and developed alongside this site.

### Plugin 1: `@stackql/docusaurus-plugin-structured-data`

Source: `../docusaurus-plugin-structured-data/` (sibling to this repo)
Published: npm registry, current version pinned in [package.json](package.json)
Lifecycle: `postBuild` + `allContentLoaded`

Emits JSON-LD `<script type="application/ld+json">` blocks into the `<head>` of every emitted HTML page. The shape:

- `WebPage` + `BreadcrumbList` + `WebSite` + `Organization` on every page
- `Article` + `ImageObject` + `Person` on blog posts (`/blog/<section>/<slug>`). Breadcrumbs follow the blog instance base path (Home > Blog > Section > Post) and `Article.articleSection` is `['Blog', '<Section>']` - needs plugin >= 1.6.0
- `TechArticle` on every doc page of the default and `ai` docs instances (`techArticleDocsInstances: ['default', 'ai']`, plugin >= 1.6.0), except the instance roots `/` and `/ai`, which stay `WebPage`. `techArticleRoutePrefixes: ['/ai/']` is kept so older plugin versions still mark `/ai/*`.
- `FAQPage`, `HowTo`, `SoftwareApplication` opt-in via frontmatter (`faq:`, `howTo:`, `softwareApplication:`). The homepage sets `softwareApplication: true`, which emits a `SoftwareApplication` from the site defaults in `themeConfig.structuredData.softwareApplication` (name, `DeveloperApplication`, OS, MIT license URL, download URL, free `Offer`).
- `SoftwareSourceCode` (plugin >= 1.7.0) in every page's graph from `themeConfig.structuredData.softwareSourceCode` (repository, Go, MIT, platforms), referenced from `WebSite.about`.
- Dates (plugin >= 1.7.0): `dateModified` is the doc's git last-update time (`showLastUpdateTime: true` on the docs plugin) or the blog post's date; the build time only for pages with no date of their own (generated category indexes, React pages). `datePublished` is the post date for blog posts and `webpage.datePublished` (2021-07-01) for the rest.
- `SpeakableSpecification` on every WebPage with default selectors
- Connected `@graph` via `mainEntity` linking (TechArticle.mainEntity -> FAQPage when both present)

Config lives at `themeConfig.structuredData` in [docusaurus.config.js](docusaurus.config.js). Key settings:

- `techArticleDocsInstances` and `techArticleRoutePrefixes` - which pages get TechArticle (see above)
- `excludedRoutes: []` - nothing is excluded (exact routes or globs are accepted); `/providers` used to be a custom React grid and is a doc page today
- `authors:` - blog author identity graph
- `organization:` - StackQL Studios identity, contact point (`contactType: 'customer support'`, email, telephone, `url: /contact-us`; the plugin warns at build time if `contactType` is missing), address (postcode 3000, the same as [src/data/contact-us/index.js](src/data/contact-us/index.js)), and a square 512px logo (Google wants a logo mark, not a cover image)
- `softwareSourceCode:` and `softwareApplication:` - the product blocks described above. The plugin takes no plugin options; everything is under `themeConfig.structuredData`, and an option in the `plugins` array fails the build.
- `breadcrumbLabelMap:` - friendly names for breadcrumb segments. Keys are single segments (`'quick-starts'`) or full paths (`'/blog/providers'`), and a full path wins. Ancestor segments only become linked crumbs when a page exists at that path; otherwise they fold into the leaf name.

If a page has no `<meta name="description">`, the plugin falls back to `siteConfig.tagline`. The homepage explicitly sets a `description` meta in [docusaurus.config.js](docusaurus.config.js) `themeConfig.metadata` to avoid the fallback on the most-cited page.

### Plugin 2: `@stackql/docusaurus-plugin-aeo`

Source: `../docusaurus-plugin-aeo/` (sibling to this repo)
Published: npm registry, current version pinned in [package.json](package.json)
Lifecycle: `postBuild` + `allContentLoaded` + theme component injection

Four features:

1. **`.md` companion files** - for every doc and blog page, emits a sibling `.md` at the same path (e.g. `/foo` -> `/foo.md`, `/` -> `/index.md`). Since plugin 0.5.0 the file is plain markdown rendered from the MDX source (imports, exports, expressions and presentational JSX removed; `<Tabs>` become bold labels; download-link components become links; everything else unwrapped and dedented), with `# title`, `> description` and `Source: <page URL>` at the top. Only emits when there is a source markdown file (React pages and auto-generated index routes are correctly skipped - they have no source to mirror). Every page that has a companion carries `<link rel="alternate" type="text/markdown" href="...">` in its head, inserted in `postBuild`. Note that [docs/providers.md](docs/providers.md) is entirely JSX rendered from the catalog, so its companion is title, description and Source only.
2. **`llms.txt` + `llms-full.txt`** - at site root. `llms.txt` is the corpus index (Markdown bullet list with title and description per page, absolute links) and ends with an `## Optional` section linking `llms-full.txt`. `llms-full.txt` is the concatenated companions of the AI Reference and Documentation instances only (`llmsTxt.fullTxt.include`), separated by `\n\n---\n\n`; agents read a bounded prefix of it, so the blog posts stay in `llms.txt` but out of the full text.
3. **"Ask AI" dropdown** - swizzled into the breadcrumb row of every doc page and the header of every blog post, and rendered only on pages that have a companion (generated category index pages have none). The prompt links the companion by the same rule the file is written (`{companionUrl}`). MUI outlined Button + Menu. Three providers: Claude, ChatGPT, Perplexity (Gemini was removed - it does not accept URL-encoded prompts). Brand icons are hand-rolled inline SVGs in `src/theme/AskAiButton/brand-icons/` to avoid React-version conflicts with icon libraries.
4. **`/ai/*` helpers** - exported from `@stackql/docusaurus-plugin-aeo/helpers`. Used to integrate the `/ai/*` content surface with the structured-data plugin.

Config lives at the plugin options object in the plugins array in [docusaurus.config.js](docusaurus.config.js):

- `llmsTxt.instanceSections` - section titles + ordering for the `llms.txt` index (AI Reference -> Documentation -> one "Blog - <Section>" section per blog instance, generated from `blogSections`)
- `askAi.providerOrder` - dropdown ordering (defaults to claude, chatgpt, perplexity)
- `askAi.promptTemplate` - the prefilled prompt sent to the AI surface. Default is self-contained ("Read {companionUrl} and help me understand it. Summarize the key points, then ask me one clarifying question to dig deeper."). The user can edit it before submitting.
- `llmsTxt.fullTxt.include` - the content instances concatenated into `llms-full.txt` (AI Reference and Documentation).

### The `/ai/*` content surface

A second `@docusaurus/plugin-content-docs` instance with `id: 'ai'`, `routeBasePath: '/ai'`, `path: 'ai-content'`, and `sidebarPath: false`. Configured in [docusaurus.config.js](docusaurus.config.js).

Directory structure under [ai-content/](ai-content/):

```
ai-content/
├── index.md                    # /ai landing
├── canonical-definitions/      # "What is X?" pages
├── comparisons/                # "StackQL vs Y" pages
├── how-tos/                    # task guides
├── concepts/                   # design rationale + best practices
├── faqs/                       # topic-grouped Q&A
├── architecture/               # internals
├── troubleshooting/            # error -> resolution
├── industry-positioning/       # where StackQL fits
├── tutorials/                  # end-to-end walkthroughs
└── providers/                  # auto-generated per-provider reference (placeholder)
```

Each section has an `index.md` landing. Individual reference pages go directly inside each section dir.

`sidebarPath: false` is what keeps these out of the human nav. They're reachable via direct URL, the sitemap, and `llms.txt`.

### The blog: three content-blog instances

The preset blog is disabled (`blog: false`). Three `@docusaurus/plugin-content-blog` instances are generated from the `blogSections` array near the top of [docusaurus.config.js](docusaurus.config.js) (GitHub issue #288). A tag-based split was ruled out because the blog sidebar is built per instance and cannot be filtered by tag.

| Instance id | Content dir | Routes | Nav label |
|---|---|---|---|
| `product` | `blog/product/` | `/blog/product/*` | Product Announcements |
| `providers` | `blog/providers/` | `/blog/providers/*` | Provider Announcements |
| `tutorials` | `blog/tutorials/` | `/blog/tutorials/*` | Tutorials |

- Each instance has its own list page, sidebar (`blogSidebarCount: 'ALL'`), tags, pagination and feeds (`/blog/<id>/rss.xml`, `atom.xml`, `feed.json`). [blog/authors.yml](blog/authors.yml) is shared by all three via `authorsMapPath: '../authors.yml'`.
- New posts go straight into the section directory. The directory is the type - there is no marker tag. Slugs are set in front matter as before and must be unique across all three sections (the redirect generator checks this).
- `/blog` is a landing page built by the local plugin [plugins/blog-landing/index.js](plugins/blog-landing/index.js). It reads the three instances in `allContentLoaded` (the only hook that sees other plugins' content) and adds a route rendering [src/components/BlogLanding/index.jsx](src/components/BlogLanding/index.jsx) with the newest five posts per section.
- Every pre-split post URL (`/blog/<slug>` and its `.md` companion) has a 301 to its new home in [netlify.toml](netlify.toml). The per-post block between the `BEGIN/END generated blog redirects` markers is owned by [scripts/generate-blog-redirects.js](scripts/generate-blog-redirects.js), which derives rules from front matter slugs. Rerun it only if a pre-split post's slug or section changes; posts written after the split never had an old URL and need no rule, so add each new post's slug to `POST_SPLIT_SLUGS` in the script. Old `/blog/tags/*`, `/blog/page/*`, `/blog/archive` and `/blog/authors` go to `/blog`; the old feed URLs go to the product announcements feeds.
- A post file must live inside a section directory. A file at `blog/` root belongs to no instance, is silently not built, and gets no redirect (one tutorial sat there for two weeks after the split and its old URL 404'd).
- The navbar "More" dropdown and the footer list the three sections plus Quick Starts; both are derived from `blogSections`. The two announcement entries carry a bullhorn (`navLabel`) in the header only. The `/blog` landing page is deliberately not linked from the header or footer; it is reachable by URL and from the sitemap.
- "Tutorials" in the nav means the blog section. The docs walkthroughs formerly at `/docs/tutorials/*` are "Quick Starts" at `/quick-starts/*` (directory `docs/quick-starts/`, sidebar category in [sidebars.js](sidebars.js)). Old URLs are 301'd in netlify.toml, including `/tutorials` -> `/blog/tutorials` and `/cookbooks` -> `/quick-starts` (both were meta-refresh React stubs, now deleted).
- Sitemap: `lastmod: 'date'` per URL from the source file's last git commit (`changefreq` and `priority` are off; crawlers ignore them). `ignorePatterns` cover `/blog/*/tags/**`, `/blog/*/page/**`, `/blog/*/archive` and `/blog/*/authors` (list pages, not content). Those same pages (about 300 tag pages and 20 paginated list pages across the three instances) are also marked `noindex, follow` by the wrappers in [src/theme/BlogTagsPostsPage](src/theme/BlogTagsPostsPage/index.js), [src/theme/BlogTagsListPage](src/theme/BlogTagsListPage/index.js) and [src/theme/BlogListPage](src/theme/BlogListPage/index.js) (page 2 onwards only; page 1 is the section landing), via [src/theme/NoIndex](src/theme/NoIndex/index.js). They are linked from every post, so Google crawls them regardless of the sitemap; the noindex stops them piling up under "Crawled - currently not indexed".
- The `breadcrumbLabelMap` entries for the section ids are generated from `blogSections`, so JSON-LD breadcrumbs read "Product Announcements" rather than "product".
- The shared nav used by the provider microsites lives in `../docusaurus-config` (vendored by those sites at build time). Its Blog/Tutorials links must be kept in step with the main site nav.

### Netlify configuration

[netlify.toml](netlify.toml) sets MIME types and cache headers for the AEO files. Critical rules:

- `*.md` -> `text/markdown; charset=utf-8` (top-level and nested)
- `/llms.txt` and `/llms-full.txt` -> `text/plain; charset=utf-8`
- All three get `X-Robots-Tag: index, follow` and `max-age=300` cache
- `/providers.json` (the published provider catalog, see "Provider catalog" below) -> `application/json; charset=utf-8`, the same 5-minute cache and `Access-Control-Allow-Origin: *`
- `/.well-known/openai-apps-challenge` -> `text/plain; charset=utf-8`, no cache, `noindex`. The file is not in the repo: when the OpenAI plugin portal asks for domain verification, save the token it shows as `static/.well-known/openai-apps-challenge` (token only, no trailing newline). Docusaurus copies `static/` dotfiles into the build.

Without these rules Netlify serves `.md` as `application/octet-stream` (browsers download instead of display) and crawlers may skip it.

Redirect rules live in the same file and their order matters (first match wins): the query library proxy, then specific legacy `/docs/...` rules, then the `/docs/*` -> `/:splat` catch-all, then the hand-written top-level aliases (`/tutorials`, `/cookbooks`, `/install`, `/downloads`, `/stackqldocs`), then the generated provider block, then the remaining hand-written rules, then the generated per-post blog block. See "Redirects are Netlify 301s, never pages" above.

### The query library (proxied, not in this repo)

The StackQL query library lives in its own repo/site (query-library.stackql.io)
and is served under the canonical path `https://stackql.io/docs/query-library/*`
via a Netlify 200 proxy rewrite in [netlify.toml](netlify.toml). This repo
holds no library content, build tooling or CI - just the rewrite rule and a
navbar link to `/docs/query-library/` (an `href`, not `to`, so it is a full
page load rather than a client-side route). Response headers for the proxied
path (content types, CORS, cache) come from the origin site, not this repo's
netlify.toml. The path 404s under `yarn serve`/`yarn start` - expected,
Netlify-only behaviour. Do not add files under `static/docs/query-library/`:
Netlify serves matching static files in preference to redirect rules, so any
file there would shadow the proxied site.

### Provider catalog and the `/providers/<slug>` aliases

[src/configs/providers.json](src/configs/providers.json) is the single source of truth for everything provider-related on this site: config, not code. It is an array of categories, each with `providers` of `{ name, href, icon, deployedBy, repo, branch, invertOnDark?, registryAliases? }`. `deployedBy` is the microsite's hosting platform, `netlify` or `github` (GitHub Pages); `repo` is the GitHub repository of the provider and its microsite as `owner/name`, and `branch` the branch the microsite is deployed from (today always the repository's default branch). None of the three is rendered on this site; they are published in `/providers.json` so cross-repo automation can tell where each microsite lives and how to redeploy it. The code that reads it is [src/lib/providers.js](src/lib/providers.js), which validates it, derives each provider's `slug` and `path` and exposes `PROVIDER_CATEGORIES`, `PROVIDERS`, `providerRoutes()` and `registryRoutes()`. Nothing else in the repo holds provider lists; the former `src/configs/providers-data.json`, `providers.ts` and the unused `ProviderCards` component were removed. The catalog drives:

- the tiles and table of contents on [docs/providers.md](docs/providers.md), which imports from `src/lib/providers`. Tiles and TOC entries link straight to the microsite (`href`), in the same tab (`customProps.target: '_self'`, honoured by the swizzled [src/theme/DocCard](src/theme/DocCard/index.js)), with no redirect hop.
- the navbar "Providers" menu: a two-level dropdown (category -> provider, every entry, catalog order) rendered by the custom navbar item type `custom-providersDropdown` in [src/theme/NavbarItem/ProvidersDropdownNavbarItem/index.js](src/theme/NavbarItem/ProvidersDropdownNavbarItem/index.js) and configured as the "Providers" entry in [docusaurus.config.js](docusaurus.config.js). Category rows link to `/providers#<category id>`, provider rows straight to the microsite (`href` with `html` instead of `label` so there is no external-link icon, `target="_self"`, `rel="noopener"` so the microsite keeps the referrer). Desktop is a hover/focus flyout per category; the mobile sidebar gets nested collapsibles.
- the published catalog at `/providers.json`, written into the build by [plugins/provider-catalog/index.js](plugins/provider-catalog/index.js) (postBuild only, so not under `npm run start`). It is a cross-repo contract: the shared chrome in `../docusaurus-config`, vendored at build time by every provider microsite and by the query library, fetches it when those sites build and generates their own two-level Providers menu from it. The shape (version 1) is documented in the plugin. Adding a field is fine; renaming or removing one, or moving the file, breaks those builds, so bump the version and change the consumer first. Netlify serves it with CORS and a 5-minute cache.
- two families of Netlify 301 rules to `https://<slug>-provider.stackql.io/`, the `BEGIN/END generated provider redirects` block in [netlify.toml](netlify.toml) written by [scripts/generate-provider-redirects.js](scripts/generate-provider-redirects.js). They used to be Docusaurus routes rendering a meta-refresh page (`plugins/provider-redirects`, `src/components/ProviderRedirect`), which Search Console reported as "Page with redirect" for every entry; both were removed in October 2026.
  - `/providers/<slug>` is explicit: exactly one rule per catalog entry, no exceptions. Nothing inside the site links here any more (tiles and navbar go straight to the microsite); it is kept for inbound links and the old sitemap.
  - `/registry/<name>` is the inbound surface for external links: one rule per catalog entry plus each entry's `registryAliases`, so a provider family exposes one canonical inbound link (`/registry/databricks` -> the Databricks Account microsite).
  - `/providers` itself is the catalog doc page ([docs/providers.md](docs/providers.md)) now that docs live at the root; the bare `/registry` 301s to it.

The slug is derived from `href`, which must be exactly `https://<slug>-provider.stackql.io/`; the module throws at config load on a missing field, a malformed href or a duplicate slug or alias. To add a provider, add one entry to the JSON and run `node scripts/generate-provider-redirects.js`. Do not create pages under `src/pages/providers` or `src/pages/registry`: a built file there would shadow the Netlify rule for that path. The retired `/providers/databricks` URL is a hand-written 301 straight to the Databricks Account microsite (a single hop, not via `/registry/databricks`).

The provider microsites and the query library get their nav from `../docusaurus-config`, which builds its own Providers menu from this site's `/providers.json` at build time (see the bullet above), so there is no featured list to keep in step any more. A new catalog entry shows up on those sites on their next build after this site deploys.

### Hand-rolled local components

[src/components/Gist/index.jsx](src/components/Gist/index.jsx) - local replacement for the unmaintained `react-gist` package (was blocking React 18 upgrade). Drop-in compatible: same `<Gist id="..." />` API. Used by two blog posts.

[src/theme/DocItem/Footer/index.js](src/theme/DocItem/Footer/index.js) - copy of the theme-classic doc footer with one addition: a doc with `hide_last_update: true` in its front matter drops the "Last updated" row while keeping tags and the edit link. Only the homepage uses it, so search results do not show a modification date on a landing page. `showLastUpdateTime` stays on globally. Keep this file in step with theme-classic when Docusaurus is upgraded.

[src/theme/NavbarItem/ComponentTypes.js](src/theme/NavbarItem/ComponentTypes.js) - wraps the theme-classic navbar item registry and adds the site's custom types. One so far: `custom-providersDropdown` (see "Provider catalog" above). Its desktop and mobile markup copy theme-classic's `DropdownNavbarItem`; keep them in step when Docusaurus is upgraded.

[src/theme/DocCard/index.js](src/theme/DocCard/index.js) - copy of the theme-classic doc card with one addition: a sidebar item's `customProps` can replace the default emoji with `iconComponent` (a React node), `icon` (an image path under `static/`, with `invertOnDark` to invert it in dark mode) or `emoji`. Link items and category items both honour it. The tiles on [docs/providers.md](docs/providers.md) and the four Quick Starts provider categories in [sidebars.js](sidebars.js) use `icon`; the Quick Starts entries look the icon up in the provider catalog by slug, so the cards follow [src/configs/providers.json](src/configs/providers.json).

### Meta descriptions

Every doc has its own `description:`; the old boilerplate ("Query and Deploy Cloud Infrastructure and Resources using SQL") was shared by 89 pages and is gone. Google uses the description as the snippet under a sitelink and treats repetition as a reason to withhold sitelinks, so a new doc needs a one-sentence description written from its content, not a copied one. The homepage title is deliberately descriptive ("SQL for cloud infrastructure, SaaS APIs and AI agents") with `sidebar_label: Welcome to StackQL` keeping the sidebar entry short.

## Frontmatter conventions for AEO content

The structured-data plugin reads several frontmatter fields directly. Use these on `/ai/*` pages especially.

```yaml
---
title: What is StackQL?
description: One-sentence definition - this becomes the JSON-LD WebPage.description, the llms.txt entry's description, and the OG description.
keywords: [stackql, sql, cloud, api]
proficiencyLevel: Beginner          # Beginner | Intermediate | Expert - sets TechArticle.proficiencyLevel
dependencies: stackql >= 0.6        # optional string - sets TechArticle.dependencies
faq:
  - question: Is StackQL a database?
    answer: No. StackQL is a query runtime...
  - question: Does StackQL replace Terraform?
    answer: Not directly. Terraform's primary job is...
---
```

When `faq:` is present, the plugin emits a `FAQPage` JSON-LD node and links it to the `TechArticle` via `mainEntity`. The `.md` companion preserves the frontmatter verbatim, so an LLM ingesting the markdown gets the FAQ pairs as content too.

`howTo:` and `softwareApplication:` work the same way. See the structured-data plugin README for the full shapes.

## When you make changes

### Always

- Build with the AEO env vars set: `ALGOLIA_APP_ID=dummy ALGOLIA_API_KEY=dummy ALGOLIA_INDEX_NAME=dummy npm run build` (local builds only - production gets real values).
- Check that the build emits expected `.md` companions: `find build -name "*.md" -type f | wc -l` should be ~690 across the three locales (about 320 English, 164 German, 208 Chinese: one per page that exists in that language). `find build/de/ai build/de/blog build/zh-hans/blog -name "*.md" | wc -l` must be 0: an untranslated page gets no companion under a locale path.
- Check that `build/llms.txt` and `build/llms-full.txt` exist and are non-empty.
- Check that `build/providers.json` exists and lists every catalog category: the provider microsites and the query library build their Providers menu from it.
- After editing a doc, `yarn i18n:status` lists the translations that now need updating (see "Internationalization"); `yarn i18n:status --check` is clean when every configured locale is current. A new doc needs `yarn write-heading-ids . docs ai-content` before it is translated.
- For `/ai/*` pages, verify the JSON-LD by inspecting the rendered HTML for `TechArticle` + `FAQPage` types (script we wrote in earlier sessions can be reproduced if needed).

### When adding `/ai/*` content

- Pick the right directory by answer intent (definition, comparison, how-to, etc.).
- Write the page in the same voice as [ai-content/canonical-definitions/what-is-stackql.md](ai-content/canonical-definitions/what-is-stackql.md) - direct, declarative, technical-encyclopedia tone (think Wikipedia, not a vendor blog).
- Always include `description:` in frontmatter - this drives JSON-LD, llms.txt, and OG metadata.
- Use `faq:` frontmatter rather than the `<script type="application/json" data-aeo-faq>` MDX pattern. Both work; frontmatter is cleaner.
- Cross-link to related `/ai/*` pages even if they don't exist yet - mark them as "not yet written" so Docusaurus's `onBrokenLinks: 'throw'` doesn't fail the build. As pages are written, convert the plain-text references to real links.

### When editing existing docs

- The structured-data plugin doesn't care if you change content - it re-runs on every build. But if you add `faq:`, `howTo:`, `proficiencyLevel:`, or `dependencies:` to frontmatter, those will surface in the JSON-LD automatically.

### When bumping plugin versions

The two AEO plugins are developed in sibling repos. To bump:

```
yarn add @stackql/docusaurus-plugin-structured-data@<version>
yarn add @stackql/docusaurus-plugin-aeo@<version>
```

If the build breaks after a bump, the plugin's CHANGELOG.md is the first place to look. Both plugins maintain detailed changelogs noting breaking changes and config migrations.

### When using `dev` mode

`npm run start` does NOT run `postBuild`, so:
- No `.md` companions are emitted
- No `llms.txt` is emitted
- JSON-LD from the structured-data plugin is NOT injected (it runs in postBuild)

But the Ask AI button (theme component) DOES render in dev. To verify the full AEO pipeline locally, run `npm run build && npm run serve` instead.

After plugin changes, dev cache must be cleared: `rm -rf .docusaurus && npm run start`. Hard refresh the browser too (Ctrl+F5).

## Known issues and workarounds

### `react-gist` peer conflict (resolved)

`react-gist@1.2.4` declares `react: <=17` as a peer dep. Replaced with the local [src/components/Gist/index.jsx](src/components/Gist/index.jsx) component to drop the conflict. Do not reintroduce `react-gist`.

### Bot fetch caches

When the live site changes, AI fetch tools (Claude.ai's web_fetch, ChatGPT's browse, Perplexity) may serve cached responses for some hours. If a recently-deployed `.md` URL appears as 404 in an LLM response, check the URL directly with `curl` first - if curl returns 200, the bot is using a stale cache. Wait a few hours and retest.

### `/blog`, `/blog/<section>`, `/contact-us`, `/privacy`, generated category indexes

These routes are React pages, the blog landing plugin route, blog list pages, or generated category index pages (`/getting-started`, `/command-line-usage`, `/quick-starts/*`) with **no source markdown**. The AEO plugin correctly does not emit `.md` companions for them. They appear in the human nav but not in `llms.txt` or anywhere requiring a `.md` twin. This is by design - do not "fix" by trying to force `.md` emission. The navbar and footer link to the canonical pages (`/`, `/installing-stackql`, `/providers`), so crawlers see real site structure; keep it that way when adding chrome links. `/install`, `/downloads`, `/stackqldocs`, `/stackql-deploy`, `/features`, `/tutorials`, `/cookbooks`, `/providers/<slug>` and `/registry/<name>` are Netlify 301s, so they 404 under `yarn serve`. `/privacy`, `/terms` and `/support` are MDX pages under `src/pages`; the AEO plugin mirrors only the docs and blog instances, so they get no companion either (checked in the November 2026 build), and `/contact-us` is a React page and does not.

### Mobile breakpoint for Ask AI

The Ask AI button is hidden below 997px viewport width (the Docusaurus mobile breakpoint) to keep the breadcrumb row uncluttered. To verify the button on a desktop test, the browser window must be wider than 997px.

## Useful commands

```bash
# Local dev server (no AEO postBuild, no JSON-LD)
npm run start

# Full production build (AEO + JSON-LD + .md companions)
ALGOLIA_APP_ID=dummy ALGOLIA_API_KEY=dummy ALGOLIA_INDEX_NAME=dummy npm run build

# Serve the production build locally to verify
npm run serve

# Clear dev cache after plugin changes
rm -rf .docusaurus build

# Inspect emitted JSON-LD on a page
grep -A1 'application/ld+json' build/command-line-usage/exec.html | head -20
grep -A1 'application/ld+json' build/blog/product/stackql-mcp-server-now-available.html | head -20

# Count .md companions
find build -name "*.md" -type f | wc -l

# Check llms.txt structure
grep -E '^## ' build/llms.txt

# Regenerate the per-post blog redirect block in netlify.toml
node scripts/generate-blog-redirects.js

# Regenerate the provider redirect block in netlify.toml (after editing providers.json)
node scripts/generate-provider-redirects.js

# Translations (see "Internationalization"): what needs doing, check one, record it
yarn i18n:status
yarn i18n:verify --locale de docs/installing-stackql.md
yarn i18n:mark --locale de docs/installing-stackql.md
yarn i18n:strings            # refresh navbar/footer/sidebar/code strings per locale
ls build/de/*.md | wc -l; head -5 build/de/llms.txt   # German companions and llms.txt after a build
grep -E '^## ' build/de/llms.txt build/zh-hans/llms.txt   # de: Documentation only; zh-Hans: AI Reference and Documentation
find build/de/ai build/de/blog build/zh-hans/blog -name '*.md' | wc -l   # expect 0 (no English copies under a locale)

# Search Console hygiene: no redirect stubs or noindex pages in the sitemap
grep -c '<loc>' build/sitemap.xml
grep -E 'stackql.io/(providers/|registry|install$|downloads$|stackqldocs$)' build/sitemap.xml   # expect nothing
grep -l 'content="noindex, follow"' build/blog/*/tags/*.html | wc -l                              # expect every tag page
```

## Related repositories

- `../docusaurus-plugin-structured-data` - JSON-LD emission plugin source. Bug fixes for stackql.io-specific issues land here first, then ship to npm.
- `../docusaurus-plugin-aeo` - AEO plugin source. Same pattern.
- `../docusaurus-config` - the shared chrome vendored at build time by the provider microsites and the query library. It fetches this site's `/providers.json` to build its Providers menu, and its `theme/NavbarItem` is a copy of [src/theme/NavbarItem/ProvidersDropdownNavbarItem/index.js](src/theme/NavbarItem/ProvidersDropdownNavbarItem/index.js) adapted to that data source; change the two together.
- `../../stackql-registry` - StackQL provider registry. Houses the StackqlDeployDropdown component whose styling the Ask AI button was modeled on.
