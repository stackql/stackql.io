// Local Docusaurus plugin: provider redirect routes.
//
// Registers Docusaurus head redirects (meta refresh) from this site to the
// provider microsites at https://<slug>-provider.stackql.io/, driven by the
// catalog in src/data/providers.js:
//
//   /providers/<slug>   one route per catalog entry, no exceptions. This is
//                       the internal surface: the tiles on /docs/providers
//                       and the navbar dropdown link here.
//   /registry/<name>    the inbound surface for external links: one route
//                       per catalog entry plus each entry's registryAliases
//                       (family-level names such as /registry/databricks).
//   /providers, /registry   redirect to the catalog page.
//
// Because the routes are registered with Docusaurus, `to:` links to them
// pass the broken-link check and render without the external-link icon,
// and adding a catalog entry is all it takes for its routes to exist.
// Nothing lives under src/pages/providers or src/pages/registry any more;
// a file there would clash with these routes.
//
// Options:
//   landing    where the bare prefixes redirect (default '/docs/providers')
//   component  the redirect component (default
//              @site/src/components/ProviderRedirect/index.jsx); receives
//              `target`: { to, name }

const { providerRoutes, registryRoutes } = require('../../src/data/providers');

module.exports = function providerRedirectsPlugin(context, options) {
  const {
    landing = '/docs/providers',
    component = '@site/src/components/ProviderRedirect/index.jsx',
  } = options || {};

  return {
    name: 'provider-redirects',

    async contentLoaded({ actions }) {
      const { addRoute, createData } = actions;

      async function add(routePath, to, name) {
        const dataPath = await createData(
          `redirect${routePath.replace(/\//g, '_')}.json`,
          JSON.stringify({ to, name }),
        );
        addRoute({
          path: routePath,
          component,
          exact: true,
          modules: { target: dataPath },
        });
      }

      const surfaces = [
        ['/providers', providerRoutes()],
        ['/registry', registryRoutes()],
      ];
      for (const [prefix, routes] of surfaces) {
        await add(prefix, landing, 'the provider catalog');
        for (const { slug, href, name } of routes) {
          await add(`${prefix}/${slug}`, href, name);
        }
      }
    },
  };
};
