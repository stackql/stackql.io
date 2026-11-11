[![Netlify Status](https://api.netlify.com/api/v1/badges/ad26d902-9cb1-43be-90d9-284e8c7ac687/deploy-status)](https://app.netlify.com/sites/stackql-io/deploys)

# Website

This website is built using [Docusaurus 2](https://docusaurus.io/), a modern static website generator.

### Installation

```
yarn
```

### Local Development

```
yarn start
```

This command starts a local development server and opens up a browser window. Most changes are reflected live without having to restart the server.

### Build

```
export ALGOLIA_API_KEY="<your algolia api key>"
export ALGOLIA_APP_ID="<your algolia app id>"
export ALGOLIA_INDEX_NAME="<your algolia index name>"
export ALGOLIA_AGENTID="<your algolia agentid>"
export HUBSPOT_ACCT_ID="<your hubspot account id>"
export HUBSPOT_FORM_ID="<your hubspot form id>"
export HUBSPOT_REGION="<your hubspot region>"
export MAPS_API_KEY="<your google maps api key>"
export SMARTLOOK_PROJECT_KEY="<your smartlook project key>"
yarn build
```

or PowerShell:  

```
$env:ALGOLIA_API_KEY="<your algolia api key>"
$env:ALGOLIA_APP_ID="<your algolia app id>"
$env:ALGOLIA_INDEX_NAME="<your algolia index name>"
$env:ALGOLIA_AGENTID="<your algolia agent id>"
$env:HUBSPOT_ACCT_ID="<your hubspot account id>"
$env:HUBSPOT_FORM_ID="<your hubspot form id>"
$env:HUBSPOT_REGION="<your hubspot region>"
$env:MAPS_API_KEY="<your google maps api key>"
$env:SMARTLOOK_PROJECT_KEY="<your smartlook project key>"
yarn build
```

This command generates static content into the `build` directory and can be served using any static contents hosting service.

### Building the AI surface locally

The AI/machine surface (`.md` companions, `llms.txt`, JSON-LD) is only produced by a full production build - `yarn start` skips all of it because the AEO plugins run in `postBuild`. Run a full production build and serve it; Algolia values can be dummies for local testing:

```
ALGOLIA_APP_ID=dummy ALGOLIA_API_KEY=dummy ALGOLIA_INDEX_NAME=dummy yarn build
yarn serve
```

or PowerShell:

```
$env:ALGOLIA_APP_ID="dummy"; $env:ALGOLIA_API_KEY="dummy"; $env:ALGOLIA_INDEX_NAME="dummy"
yarn build
yarn serve
```

The served site at `http://localhost:3000` now includes `/llms.txt` and the `.md` twin for every doc and blog page.

The StackQL query library lives in its own repo (query-library.stackql.io) and is served under the canonical path `https://stackql.io/docs/query-library/*` via a Netlify 200 proxy rewrite (see `netlify.toml`). The path 404s when serving this site locally - that is expected, the rewrite only exists on Netlify.

### SEO and AEO checks

`yarn seo:check` verifies that the site publishes what search engines and answer engines need, in every locale (`en`, `de`, `zh-Hans`). It is run by hand, against a local build or against production:

```
# a local build: build it, serve it in one terminal, check it in another
ALGOLIA_APP_ID=dummy ALGOLIA_API_KEY=dummy ALGOLIA_INDEX_NAME=dummy yarn build
yarn serve
yarn seo:check                    # same as --target local (http://localhost:3000)

# production
yarn seo:check --target prod      # https://stackql.io
```

Run it locally before a deploy that touches locales, URLs, `netlify.toml`, `static/robots.txt`, the AEO or structured-data plugins or the theme's head tags, and against production once the deploy is live (Netlify takes a few minutes after the merge; until then production still answers with the previous build). It exits 1 when a check fails and prints each failure with the URL and what was found. Warnings do not fail the run.

| Section | What it checks | `local` | `prod` |
|---|---|---|---|
| `robots` | `robots.txt` allows every crawler token in the config and lists one sitemap per locale | yes | yes |
| `sitemaps` | per locale: URL count, only that locale's own indexable pages, lower-case paths, `lastmod`; sampled URLs answer 200, are self-canonical and in the right language | yes | yes, and no redirects |
| `llms` | per locale: `llms.txt` sections and entries, only translated pages listed, Chinese text for `zh-Hans`, sampled `.md` companions, `llms-full.txt` | yes | yes, and content types and `X-Robots-Tag` |
| `pages` | representative pages per locale: `lang`, title, description, canonical, robots meta, `hreflang`, social tags, JSON-LD types and language, the markdown twin, and that the text is in the served HTML (no JavaScript needed) | yes | yes, and no redirects |
| `assets` | third-party hosts on the rendering path; a warning for hosts that are slow or unreachable from mainland China | yes | yes |
| `switcher` | in headless Chrome: after the page hydrates, the language switcher links to the same page in every locale, for every pair of locales; the English flag follows the browser's region (AU, NZ, ZA, GB) and is the US flag otherwise | yes | yes |
| `crawlers` | each crawler user-agent (Googlebot, Bingbot, GPTBot, ClaudeBot, PerplexityBot, Baiduspider, Bytespider and the rest of the list) gets the same status and the same bytes as a plain client: not blocked, not cloaked | skipped | yes |
| `edge` | redirect rules of `netlify.toml` answer as written in one hop, a missing page gets its locale's 404 page, `/providers.json` headers, the query library proxy | skipped | yes |

The last two sections test the hosting edge, which `yarn serve` does not have. A page is expected to be indexable, listed and mirrored as markdown in a locale only when it is translated for that locale (`scripts/i18n/config.js`); an untranslated page must be `noindex` there and must not be listed anywhere.

Options:

```
yarn seo:check --target prod --locale zh-Hans        # one locale (comma-separated for several)
yarn seo:check --only sitemaps,llms                  # some sections
yarn seo:check --sample 50                           # sitemap URLs and llms.txt entries sampled per locale (default 20)
yarn seo:check --all                                 # every sitemap URL, every llms.txt entry, every redirect rule
yarn seo:check --verbose                             # list the checks that passed as well
yarn seo:check --json                                # machine-readable results
yarn seo:check --base http://localhost:3111          # a build served somewhere else (add --edge if it is a Netlify deploy)
```

The expectations (locales and their URL prefixes, the pages checked, the crawler list, minimum counts) are in `scripts/seo-aeo/config.js`; review the crawler list when a lab publishes or renames a crawler. The `switcher` section needs Chrome or Chromium (set `CHROME_PATH` if it is not in a standard location) and is skipped without one; nothing else needs more than Node 22.

Locale URL prefixes must be lower case. Netlify answers a page URL that has upper-case letters with a 301 to its lower-case form, which is why the Simplified Chinese locale (`zh-Hans`) is served at `/zh-hans/`.

What it cannot tell you: whether the site is reachable from mainland China, what Google Search Console, Bing Webmaster Tools or Baidu's webmaster platform report (coverage, sitemap submission, site verification), whether a crawler is blocked by IP rather than by user-agent, and page speed.

### Provider catalog

`src/configs/providers.json` is the list of providers behind the Providers menu, the tiles on `/providers`, the `/providers/<slug>` and `/registry/<name>` redirects, and the catalog published at `https://stackql.io/providers.json`, which the provider microsites and the query library read when they build their own Providers menu. It is an array of categories (`id`, `name`, `providers`), and each provider entry has:

| Field | Required | Meaning |
|---|---|---|
| `name` | yes | display name |
| `href` | yes | microsite URL, exactly `https://<slug>-provider.stackql.io/` (the slug is derived from it) |
| `icon` | yes | icon path under `static/` |
| `deployedBy` | yes | hosting platform of the microsite: `netlify` or `github` (GitHub Pages) |
| `repo` | yes | GitHub repository of the provider and its microsite, as `owner/name` |
| `branch` | yes | the branch the microsite is deployed from (today always the repository's default branch) |
| `invertOnDark` | no | invert the icon in dark mode |
| `registryAliases` | no | extra names that redirect to this microsite under `/registry` only |

`deployedBy`, `repo` and `branch` are not rendered anywhere on this site; they are published in `/providers.json` so cross-repo automation can tell where each microsite lives and how to redeploy it.

To add a provider, add one entry and run `node scripts/generate-provider-redirects.js` to regenerate the redirect block in `netlify.toml`. The config fails to load on a missing field, a malformed `href` or `repo`, an unknown `deployedBy` value, or a duplicate slug or alias. See CLAUDE.md, "Provider catalog", for the full picture.

### Deployment

```
$ GIT_USER=<Your GitHub username> USE_SSH=true yarn deploy
```

If you are using GitHub pages for hosting, this command is a convenient way to build the website and push to the `gh-pages` branch.
