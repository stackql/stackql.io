// Provider catalog helpers. The catalog itself is config, not code:
// src/configs/providers.json. This module reads it, validates it, derives
// each provider's slug and route path, and exposes the views the site uses:
//
//   PROVIDER_CATEGORIES  the catalog with `slug` and `path` added to every
//                        provider (the tiles and TOC on /providers)
//   PROVIDERS            flat list in catalog order, with `category`
//   providerRoutes()     /providers/<slug>: one per entry, no exceptions
//   registryRoutes()     /registry/<name>: entries plus registryAliases
//   buildCatalog(json)   the same four views for any parsed catalog (the
//                        pre-commit hook uses it on the staged copy)
//
// providerRoutes() and registryRoutes() describe the two families of short
// URLs that 301 to the microsites. They are Netlify rules in netlify.toml,
// written by scripts/generate-provider-redirects.js, not Docusaurus routes:
// the tiles and the navbar link straight to `href`. The pre-commit hook
// rewrites those rules whenever this catalog is committed.
//
// Catalog entry fields (src/configs/providers.json):
//   name             display name: tile label, default menu label
//   href             microsite URL, must be https://<slug>-provider.stackql.io/
//                    (the slug is derived from it)
//   icon             tile icon, a path under static/
//   deployedBy       hosting platform of the microsite: "netlify" or
//                    "github" (GitHub Pages)
//   repo             GitHub repository of the provider and its microsite,
//                    as owner/name (stackql-registry/stackql-provider-<x>)
//   branch           the branch the microsite is deployed from (today
//                    always the repository's default branch)
//                    deployedBy, repo and branch are not rendered on this
//                    site; they are published in /providers.json so
//                    cross-repo automation can tell where each microsite
//                    lives and how to redeploy it
//   invertOnDark     optional, invert the icon in dark mode
//   registryAliases  optional, extra names that redirect to this microsite
//                    under /registry only (never under /providers), so a
//                    provider family can expose one canonical inbound link
//
// To add a provider, add one entry to the JSON and commit it: the pre-commit
// hook (scripts/precommit-provider-redirects.js) regenerates the redirect
// block in netlify.toml and stages it into the same commit, and
// scripts/generate-provider-redirects.js does the same by hand (a commit
// with --no-verify skips the hook). Nothing else changes: the tile,
// the navbar menu entry (src/theme/NavbarItem/ProvidersDropdownNavbarItem)
// and the catalog published at /providers.json (plugins/provider-catalog),
// which the provider microsites' shared chrome reads at build time, all
// follow.
//
// CommonJS on purpose: docusaurus.config.js and the redirect generator
// require() this; docs/providers.md imports it via webpack.

const catalog = require('../configs/providers.json');

// Every microsite is https://<slug>-provider.stackql.io/. The slug is the
// path segment used by the redirect routes.
const MICROSITE_RE = /^https:\/\/([a-z0-9-]+)-provider\.stackql\.io\/$/;

// Hosting platforms a microsite can be deployed by (the `deployedBy` field).
const DEPLOYED_BY = ['netlify', 'github'];

// `repo` is a GitHub owner/name, nothing more (no URL, no .git).
const REPO_RE = /^[A-Za-z0-9_.-]+\/[A-Za-z0-9_.-]+$/;

function slugFromHref(href) {
  const match = MICROSITE_RE.exec(href);
  if (!match) {
    throw new Error(
      `[providers] href "${href}" is not a https://<slug>-provider.stackql.io/ microsite URL`,
    );
  }
  return match[1];
}

function requireString(value, field, where) {
  if (typeof value !== 'string' || value.length === 0) {
    throw new Error(`[providers] ${where}: "${field}" must be a non-empty string`);
  }
}

// Validates a catalog (the parsed contents of providers.json) and returns
// its views. Throws on a missing or malformed field, a malformed href, or a
// duplicate slug or alias, so a bad entry fails the config, not a page
// render. The site uses the default catalog below; the pre-commit hook
// (scripts/precommit-provider-redirects.js) calls this on the staged copy.
function buildCatalog(catalog) {
  if (!Array.isArray(catalog)) {
    throw new Error('[providers] src/configs/providers.json must be an array of categories');
  }

  // Validated copy of the catalog with `slug` and `path` on every provider.
  // The JSON object passed in is left untouched.
  const PROVIDER_CATEGORIES = catalog.map((category, ci) => {
    requireString(category.id, 'id', `category #${ci}`);
    requireString(category.name, 'name', `category "${category.id}"`);
    if (!Array.isArray(category.providers)) {
      throw new Error(`[providers] category "${category.id}": "providers" must be an array`);
    }
    return {
      ...category,
      providers: category.providers.map((provider, pi) => {
        const where = `category "${category.id}" provider #${pi}`;
        requireString(provider.name, 'name', where);
        requireString(provider.href, 'href', `"${provider.name}"`);
        requireString(provider.icon, 'icon', `"${provider.name}"`);
        if (!DEPLOYED_BY.includes(provider.deployedBy)) {
          throw new Error(
            `[providers] "${provider.name}": "deployedBy" must be one of ${DEPLOYED_BY.join(', ')}`,
          );
        }
        requireString(provider.repo, 'repo', `"${provider.name}"`);
        if (!REPO_RE.test(provider.repo)) {
          throw new Error(`[providers] "${provider.name}": "repo" must be a GitHub owner/name`);
        }
        requireString(provider.branch, 'branch', `"${provider.name}"`);
        if (provider.registryAliases !== undefined && !Array.isArray(provider.registryAliases)) {
          throw new Error(`[providers] "${provider.name}": "registryAliases" must be an array`);
        }
        const slug = slugFromHref(provider.href);
        return { ...provider, slug, path: `/providers/${slug}` };
      }),
    };
  });

  // Flat list in catalog order, each entry with the id of its category.
  const PROVIDERS = PROVIDER_CATEGORIES.flatMap((category) =>
    category.providers.map((provider) => ({ ...provider, category: category.id })),
  );

  // /providers/<slug>: one { slug, href, name } per catalog entry, no
  // exceptions. Throws if two entries derive the same slug.
  function providerRoutes() {
    const owner = new Map();
    return PROVIDERS.map((provider) => {
      claim(owner, provider.slug, provider.name, '/providers');
      return { slug: provider.slug, href: provider.href, name: provider.name };
    });
  }

  // /registry/<name>: every catalog entry plus each entry's registryAliases.
  // Throws if an alias collides with another entry's slug or alias.
  function registryRoutes() {
    const owner = new Map();
    const routes = [];
    for (const provider of PROVIDERS) {
      for (const name of [provider.slug, ...(provider.registryAliases || [])]) {
        claim(owner, name, provider.name, '/registry');
        routes.push({ slug: name, href: provider.href, name: provider.name });
      }
    }
    return routes;
  }

  providerRoutes();
  registryRoutes();

  return { PROVIDER_CATEGORIES, PROVIDERS, providerRoutes, registryRoutes };
}

function claim(owner, name, ownerName, surface) {
  if (owner.has(name)) {
    throw new Error(
      `[providers] ${surface} name "${name}" is claimed by both "${owner.get(name)}" and "${ownerName}"`,
    );
  }
  owner.set(name, ownerName);
}

// The site's catalog, validated at load.
const { PROVIDER_CATEGORIES, PROVIDERS, providerRoutes, registryRoutes } = buildCatalog(catalog);

module.exports = {
  PROVIDER_CATEGORIES,
  PROVIDERS,
  providerRoutes,
  registryRoutes,
  buildCatalog,
  slugFromHref,
  MICROSITE_RE,
  DEPLOYED_BY,
};
