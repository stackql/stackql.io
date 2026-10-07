# CLAUDE.md - stackql.io

Project guide for Claude Code working in this repository. Tells you what this site is, how the AEO platform is wired together, where the dependencies live, and the conventions to follow.

## What this site is

stackql.io is the marketing and documentation site for StackQL, built on Docusaurus 3.10. Three audiences:

- **Humans** - default site nav. This is a docs-only site: the docs tree is the site root (`/installing-stackql`, `/providers`, `/getting-started/*`, `/command-line-usage/*`, `/mcp/*`, `/language-spec/*`, `/developers/*`, `/quick-starts/*`) and [docs/index.md](docs/index.md) is the homepage. Blog at `/blog/<section>/*` (three sections, see "The blog" below). A few top-level React pages remain (`/features`, `/privacy`, `/contact-us`, `/stackql-deploy`). There are no meta-refresh stub pages any more: every alias URL is a Netlify 301 (see "Redirects are Netlify 301s, never pages" below).
- **AI agents and answer engines** - a parallel content surface at `/ai/*` (canonical definitions, comparisons, how-tos, FAQs, troubleshooting, etc.) reachable by deep link, via `llms.txt` and from one "AI Reference" link in the footer, but **not** from the header nav. The footer link exists so the tree has a crawl path: with no internal link at all Google left its 44 sitemap URLs as "Discovered - currently not indexed".
- **LLM crawlers** - `llms.txt` and `llms-full.txt` at site root; raw markdown twin (`/foo.md`) for every doc and blog page.

The two surfaces serve the same URLs to all visitors (no UA-based cloaking). The `/ai/*` pages just don't appear in the header nav.

## Redirects are Netlify 301s, never pages

Every alias or retired URL on this site is a `[[redirects]]` rule in [netlify.toml](netlify.toml), not a built page. Do not add a `src/pages` stub or a plugin route that renders `<meta http-equiv="refresh">`: a meta-refresh page is a 200 that the sitemap plugin lists and that Google reports as "Page with redirect" (Search Console flagged every one of them in October 2026), and it carries conflicting signals (`robots: index,follow`, a canonical, and a refresh). An edge 301 is neither built nor listed. The former stubs (`/install`, `/downloads`, `/stackqldocs`, `/tutorials`, `/cookbooks`, `/providers/<slug>`, `/registry/<name>`) are all rules now.

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
- Root-level routes that are not docs: `/blog/*`, `/ai/*`, `/features`, `/privacy`, `/search`, the provider routes (`/providers/<slug>`, `/registry/*`) and the stubs `/install`, `/stackqldocs`, `/downloads`, `/stackql-deploy`, `/contact-us`. A new doc whose slug collides with one of these would clash with it, so check before adding root-level docs.
- Getting Started and Command Line Usage are sidebar categories with generated index pages (`/getting-started`, `/command-line-usage`), so every major section has a landing page Google can surface as a sitelink: installation, providers, getting started, command line usage, MCP.

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
- `FAQPage`, `HowTo`, `SoftwareApplication` opt-in via frontmatter (`faq:`, `howTo:`, `softwareApplication:`)
- `SpeakableSpecification` on every WebPage with default selectors
- Connected `@graph` via `mainEntity` linking (TechArticle.mainEntity -> FAQPage when both present)

Config lives at `themeConfig.structuredData` in [docusaurus.config.js](docusaurus.config.js). Key settings:

- `techArticleDocsInstances` and `techArticleRoutePrefixes` - which pages get TechArticle (see above)
- `excludedRoutes: []` - nothing is excluded; `/providers` used to be a custom React grid and is a doc page today
- `authors:` - blog author identity graph
- `organization:` - StackQL Studios identity, contact, address, and a square 512px logo (Google wants a logo mark, not a cover image)
- `breadcrumbLabelMap:` - friendly names for breadcrumb segments. Keys are single segments (`'quick-starts'`) or full paths (`'/blog/providers'`), and a full path wins. Ancestor segments only become linked crumbs when a page exists at that path; otherwise they fold into the leaf name.

If a page has no `<meta name="description">`, the plugin falls back to `siteConfig.tagline`. The homepage explicitly sets a `description` meta in [docusaurus.config.js](docusaurus.config.js) `themeConfig.metadata` to avoid the fallback on the most-cited page.

### Plugin 2: `@stackql/docusaurus-plugin-aeo`

Source: `../docusaurus-plugin-aeo/` (sibling to this repo)
Published: npm registry, current version pinned in [package.json](package.json)
Lifecycle: `postBuild` + `allContentLoaded` + theme component injection

Four features:

1. **`.md` companion files** - for every doc and blog page, emits a sibling `.md` at the same path (e.g. `/docs/foo` -> `/docs/foo.md`). Mirrors the raw MDX source. Only emits when there is a source markdown file (React pages and auto-generated index routes are correctly skipped - they have no source to mirror).
2. **`llms.txt` + `llms-full.txt`** - at site root. `llms.txt` is the corpus index (Markdown bullet list with title and description per page). `llms-full.txt` is the concatenated body of every `.md` companion, separated by `\n\n---\n\n`.
3. **"Ask AI" dropdown** - swizzled into the breadcrumb row of every doc page and the header of every blog post. MUI outlined Button + Menu. Three providers: Claude, ChatGPT, Perplexity (Gemini was removed - it does not accept URL-encoded prompts). Brand icons are hand-rolled inline SVGs in `src/theme/AskAiButton/brand-icons/` to avoid React-version conflicts with icon libraries.
4. **`/ai/*` helpers** - exported from `@stackql/docusaurus-plugin-aeo/helpers`. Used to integrate the `/ai/*` content surface with the structured-data plugin.

Config lives at the plugin options object in the plugins array in [docusaurus.config.js](docusaurus.config.js):

- `llmsTxt.instanceSections` - section titles + ordering for the `llms.txt` index (AI Reference -> Documentation -> one "Blog - <Section>" section per blog instance, generated from `blogSections`)
- `askAi.providerOrder` - dropdown ordering (defaults to claude, chatgpt, perplexity)
- `askAi.promptTemplate` - the prefilled prompt sent to the AI surface. Default is self-contained ("Read {pageUrl}.md and help me understand it. Summarize the key points, then ask me one clarifying question to dig deeper."). The user can edit it before submitting.

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
- Sitemap `ignorePatterns` cover `/blog/*/tags/**` and `/blog/*/page/**`. Those same pages (about 300 tag pages and 20 paginated list pages across the three instances) are also marked `noindex, follow` by the wrappers in [src/theme/BlogTagsPostsPage](src/theme/BlogTagsPostsPage/index.js), [src/theme/BlogTagsListPage](src/theme/BlogTagsListPage/index.js) and [src/theme/BlogListPage](src/theme/BlogListPage/index.js) (page 2 onwards only; page 1 is the section landing), via [src/theme/NoIndex](src/theme/NoIndex/index.js). They are linked from every post, so Google crawls them regardless of the sitemap; the noindex stops them piling up under "Crawled - currently not indexed".
- The `breadcrumbLabelMap` entries for the section ids are generated from `blogSections`, so JSON-LD breadcrumbs read "Product Announcements" rather than "product".
- The shared nav used by the provider microsites lives in `../docusaurus-config` (vendored by those sites at build time). Its Blog/Tutorials links must be kept in step with the main site nav.

### Netlify configuration

[netlify.toml](netlify.toml) sets MIME types and cache headers for the AEO files. Critical rules:

- `*.md` -> `text/markdown; charset=utf-8` (top-level and nested)
- `/llms.txt` and `/llms-full.txt` -> `text/plain; charset=utf-8`
- All three get `X-Robots-Tag: index, follow` and `max-age=300` cache
- `/providers.json` (the published provider catalog, see "Provider catalog" below) -> `application/json; charset=utf-8`, the same 5-minute cache and `Access-Control-Allow-Origin: *`

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

[src/configs/providers.json](src/configs/providers.json) is the single source of truth for everything provider-related on this site: config, not code. It is an array of categories, each with `providers` of `{ name, href, icon, invertOnDark?, registryAliases? }`. The code that reads it is [src/lib/providers.js](src/lib/providers.js), which validates it, derives each provider's `slug` and `path` and exposes `PROVIDER_CATEGORIES`, `PROVIDERS`, `providerRoutes()` and `registryRoutes()`. Nothing else in the repo holds provider lists; the former `src/configs/providers-data.json`, `providers.ts` and the unused `ProviderCards` component were removed. The catalog drives:

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
- Check that the build emits expected `.md` companions: `find build -name "*.md" -type f | wc -l` should be ~300.
- Check that `build/llms.txt` and `build/llms-full.txt` exist and are non-empty.
- Check that `build/providers.json` exists and lists every catalog category: the provider microsites and the query library build their Providers menu from it.
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

### `/blog`, `/blog/<section>`, `/stackql-deploy`, `/features`, generated category indexes

These routes are React pages, the blog landing plugin route, blog list pages, or generated category index pages (`/getting-started`, `/command-line-usage`, `/quick-starts/*`) with **no source markdown**. The AEO plugin correctly does not emit `.md` companions for them. They appear in the human nav but not in `llms.txt` or anywhere requiring a `.md` twin. This is by design - do not "fix" by trying to force `.md` emission. The navbar and footer link to the canonical pages (`/`, `/installing-stackql`, `/providers`), so crawlers see real site structure; keep it that way when adding chrome links. `/install`, `/downloads`, `/stackqldocs`, `/tutorials`, `/cookbooks`, `/providers/<slug>` and `/registry/<name>` are Netlify 301s, so they 404 under `yarn serve`.

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
