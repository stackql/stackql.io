// Provider catalog helpers. The catalog itself is config, not code:
// src/configs/providers.json. This module reads it, validates it, derives
// each provider's slug and route path, and exposes the views the site uses:
//
//   PROVIDER_CATEGORIES  the catalog with `slug` and `path` added to every
//                        provider (the tiles and TOC on /docs/providers)
//   PROVIDERS            flat list in catalog order, with `category`
//   FEATURED_PROVIDERS   entries flagged `featured` (navbar dropdown)
//   providerRoutes()     /providers/<slug>: one per entry, no exceptions
//   registryRoutes()     /registry/<name>: entries plus registryAliases
//
// Catalog entry fields (src/configs/providers.json):
//   name             display name: tile label, default menu label
//   href             microsite URL, must be https://<slug>-provider.stackql.io/
//                    (the slug is derived from it)
//   icon             tile icon, a path under static/
//   invertOnDark     optional, invert the icon in dark mode
//   featured         optional, true to list it in the navbar dropdown
//   shortName        optional, menu label when `name` is too long for a menu
//   registryAliases  optional, extra names that redirect to this microsite
//                    under /registry only (never under /providers), so a
//                    provider family can expose one canonical inbound link
//
// To add a provider, add one entry to the JSON. Nothing else changes: the
// tile, both redirect routes and (if featured) the menu item all follow.
//
// CommonJS on purpose: docusaurus.config.js and plugins/provider-redirects
// require() this at config time; docs/providers.md imports it via webpack.

const catalog = require('../configs/providers.json');

// Every microsite is https://<slug>-provider.stackql.io/. The slug is the
// path segment used by the redirect routes.
const MICROSITE_RE = /^https:\/\/([a-z0-9-]+)-provider\.stackql\.io\/$/;

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

if (!Array.isArray(catalog)) {
  throw new Error('[providers] src/configs/providers.json must be an array of categories');
}

// Validated copy of the catalog with `slug` and `path` on every provider.
// The required JSON object is left untouched.
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

function claim(owner, name, ownerName, surface) {
  if (owner.has(name)) {
    throw new Error(
      `[providers] ${surface} name "${name}" is claimed by both "${owner.get(name)}" and "${ownerName}"`,
    );
  }
  owner.set(name, ownerName);
}

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

// Navbar dropdown entries, in catalog order.
const FEATURED_PROVIDERS = PROVIDERS.filter((provider) => provider.featured === true);

// Validate at load so a bad entry fails the config, not a page render.
providerRoutes();
registryRoutes();

module.exports = {
  PROVIDER_CATEGORIES,
  PROVIDERS,
  FEATURED_PROVIDERS,
  providerRoutes,
  registryRoutes,
  slugFromHref,
  MICROSITE_RE,
};
