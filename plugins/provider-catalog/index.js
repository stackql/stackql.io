// Local Docusaurus plugin: the published provider catalog.
//
// Writes <outDir>/providers.json at the end of every production build: the
// provider catalog (src/configs/providers.json, read through
// src/lib/providers.js) as a machine-readable document served at
// https://stackql.io/providers.json. It is a cross-repo contract: the shared
// chrome in ../docusaurus-config (vendored at build time by every provider
// microsite and by the query library) fetches it when those sites build and
// generates their two-level Providers menu from it, so they list whatever
// this site publishes and no copy of the catalog is kept anywhere else.
//
// Shape (version 1):
//   {
//     version: 1,
//     source: 'https://stackql.io/providers',      // the human catalog page
//     generated: '<ISO timestamp of the build>',
//     categories: [{
//       id, name,
//       url: 'https://stackql.io/providers#<id>',  // the category's section
//       providers: [{
//         name, slug,
//         href: 'https://<slug>-provider.stackql.io/',
//         icon: 'https://stackql.io/img/...',      // absolute
//         invertOnDark: true                        // only when set
//       }]
//     }]
//   }
//
// Adding a field is fine. Renaming or removing one, or moving the file,
// breaks the consumers' builds: bump `version` and change
// ../docusaurus-config first.
//
// postBuild only: `npm run start` does not emit the file (same as the AEO
// companions). Netlify serves it with CORS and a short cache (netlify.toml).
//
// Options:
//   filename   output file name under the build dir (default 'providers.json')

const fs = require('fs');
const path = require('path');
const { PROVIDER_CATEGORIES } = require('../../src/lib/providers');

module.exports = function providerCatalogPlugin(context, options) {
  const { filename = 'providers.json' } = options || {};
  const { url, baseUrl } = context.siteConfig;
  const site = `${url.replace(/\/+$/, '')}${(baseUrl || '/').replace(/\/+$/, '')}`;
  const absolute = (sitePath) => `${site}${sitePath.startsWith('/') ? '' : '/'}${sitePath}`;

  return {
    name: 'provider-catalog',

    async postBuild({ outDir }) {
      const catalog = {
        version: 1,
        source: `${site}/providers`,
        generated: new Date().toISOString(),
        categories: PROVIDER_CATEGORIES.map((category) => ({
          id: category.id,
          name: category.name,
          url: `${site}/providers#${category.id}`,
          providers: category.providers.map((provider) => ({
            name: provider.name,
            slug: provider.slug,
            href: provider.href,
            icon: absolute(provider.icon),
            ...(provider.invertOnDark ? { invertOnDark: true } : {}),
          })),
        })),
      };
      await fs.promises.writeFile(
        path.join(outDir, filename),
        `${JSON.stringify(catalog, null, 2)}\n`,
      );
      const providers = catalog.categories.reduce((n, c) => n + c.providers.length, 0);
      console.log(
        `[provider-catalog] wrote ${filename}: ${catalog.categories.length} categories, ${providers} providers`,
      );
    },
  };
};
