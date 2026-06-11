# CLAUDE.md - stackql.io

Project guide for Claude Code working in this repository. Tells you what this site is, how the AEO platform is wired together, where the dependencies live, and the conventions to follow.

## What this site is

stackql.io is the marketing and documentation site for StackQL, built on Docusaurus 3.10. Three audiences:

- **Humans** - default site nav, docs at `/docs/*`, blog at `/blog/*`, install/provider/tutorial landings at top-level React pages.
- **AI agents and answer engines** - a parallel content surface at `/ai/*` (canonical definitions, comparisons, how-tos, FAQs, troubleshooting, etc.) reachable by deep link or via `llms.txt`, but **not** linked from the human nav.
- **LLM crawlers** - `llms.txt` and `llms-full.txt` at site root; raw markdown twin (`/foo.md`) for every doc and blog page.

The two surfaces serve the same URLs to all visitors (no UA-based cloaking). The `/ai/*` pages just don't appear in the human-facing nav.

## Tech stack snapshot

- Docusaurus 3.10.1, classic preset
- React 18 (pinned - several deps require it)
- MUI 5/6 (already in deps - Material UI and emotion)
- Node 22 toolchain
- Deployed via Netlify, build = `npm run build`, publish = `build/`
- Search via Algolia DocSearch (`ALGOLIA_APP_ID`, `ALGOLIA_API_KEY`, `ALGOLIA_INDEX_NAME` env vars required for prod builds)

## AEO architecture

Two custom plugins published to npm and one secondary content-docs instance handle the AEO pipeline. Both plugins are in sibling repos and developed alongside this site.

### Plugin 1: `@stackql/docusaurus-plugin-structured-data`

Source: `../docusaurus-plugin-structured-data/` (sibling to this repo)
Published: npm registry, current version pinned in [package.json](package.json)
Lifecycle: `postBuild` + `allContentLoaded`

Emits JSON-LD `<script type="application/ld+json">` blocks into the `<head>` of every emitted HTML page. The shape:

- `WebPage` + `BreadcrumbList` + `WebSite` + `Organization` on every page
- `Article` + `ImageObject` + `Person` on `/blog/*` (real blog posts)
- `TechArticle` on `/docs/*` and `/ai/*` (configured via `techArticleRoutePrefixes`)
- `FAQPage`, `HowTo`, `SoftwareApplication` opt-in via frontmatter (`faq:`, `howTo:`, `softwareApplication:`)
- `SpeakableSpecification` on every WebPage with default selectors
- Connected `@graph` via `mainEntity` linking (TechArticle.mainEntity -> FAQPage when both present)

Config lives at `themeConfig.structuredData` in [docusaurus.config.js](docusaurus.config.js). Key settings:

- `techArticleRoutePrefixes: ['/docs/', '/ai/']` - controls which route prefixes get TechArticle
- `excludedRoutes: ['/providers']` - skip the providers page (custom React grid that doesn't conform)
- `authors:` - blog author identity graph
- `organization:` - StackQL Studios identity, contact, address
- `breadcrumbLabelMap:` - friendly names for URL path segments in breadcrumbs

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

- `llmsTxt.instanceSections` - section titles + ordering for the `llms.txt` index (AI Reference -> Documentation -> Blog)
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

### Netlify configuration

[netlify.toml](netlify.toml) sets MIME types and cache headers for the AEO files. Critical rules:

- `*.md` -> `text/markdown; charset=utf-8` (top-level and nested)
- `/llms.txt` and `/llms-full.txt` -> `text/plain; charset=utf-8`
- All three get `X-Robots-Tag: index, follow` and `max-age=300` cache

Without these rules Netlify serves `.md` as `application/octet-stream` (browsers download instead of display) and crawlers may skip it.

### Hand-rolled local components

[src/components/Gist/index.jsx](src/components/Gist/index.jsx) - local replacement for the unmaintained `react-gist` package (was blocking React 18 upgrade). Drop-in compatible: same `<Gist id="..." />` API. Used by two blog posts.

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
- Check that the build emits expected `.md` companions: `find build -name "*.md" -type f | wc -l` should be ~240+.
- Check that `build/llms.txt` and `build/llms-full.txt` exist and are non-empty.
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
npm install @stackql/docusaurus-plugin-structured-data@<version> --save
npm install @stackql/docusaurus-plugin-aeo@<version> --save
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

### `/install`, `/blog`, `/providers`, `/stackql-deploy`, `/tutorials`, `/mcp`, `/stackqldocs`

These top-level routes are React pages or auto-generated index landings with **no source markdown**. The AEO plugin correctly does not emit `.md` companions for them. They appear in the human nav but not in `llms.txt` or anywhere requiring a `.md` twin. This is by design - do not "fix" by trying to force `.md` emission.

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
grep -A1 'application/ld+json' build/docs/command-line-usage/exec.html | head -20

# Count .md companions
find build -name "*.md" -type f | wc -l

# Check llms.txt structure
grep -E '^## ' build/llms.txt
```

## Related repositories

- `../docusaurus-plugin-structured-data` - JSON-LD emission plugin source. Bug fixes for stackql.io-specific issues land here first, then ship to npm.
- `../docusaurus-plugin-aeo` - AEO plugin source. Same pattern.
- `../../stackql-registry` - StackQL provider registry. Houses the StackqlDeployDropdown component whose styling the Ask AI button was modeled on.
