/**
 * query-library-pages
 *
 * Generates the human browse surface for the query library from the built
 * catalogue (static/docs/query-library/index.json - single source of truth,
 * no hand-maintained provider list):
 *
 *   /docs/query-library              landing page, one card per provider
 *   /docs/query-library/<provider>   per-provider entry table
 *
 * Rendered query pages at /docs/query-library/queries/<id> stay on the
 * query-library docs-plugin instance. The machine artifacts at the same URL
 * prefix (manifest.json, index.json, queries/<id>.json|.md) are static files
 * and are not touched by this plugin.
 *
 * A provider appears on the landing page when its first query merges;
 * display names and blurbs come from src/configs/providers.ts client-side
 * with a capitalization fallback, and logos are resolved here at build time
 * with an _account/_workspace suffix fallback (databricks_account uses the
 * databricks logo).
 */

const fs = require('fs');
const path = require('path');

function resolveLogo(siteDir, providerId) {
  const candidates = [providerId, providerId.replace(/_(account|workspace)$/, '')];
  for (const c of candidates) {
    const rel = `img/providers/${c}/${c}.png`;
    if (fs.existsSync(path.join(siteDir, 'static', rel))) {
      return `/${rel}`;
    }
  }
  return null;
}

module.exports = function queryLibraryPagesPlugin(context) {
  return {
    name: 'query-library-pages',

    async contentLoaded({actions}) {
      const {siteDir} = context;
      const indexPath = path.join(
        siteDir, 'static', 'docs', 'query-library', 'index.json',
      );
      if (!fs.existsSync(indexPath)) {
        throw new Error(
          `[query-library-pages] ${indexPath} not found. ` +
          'Run "python query-library/scripts/build-artifacts.py" first.',
        );
      }
      const catalogue = JSON.parse(fs.readFileSync(indexPath, 'utf8'));
      const entries = catalogue.entries || [];

      // Group by primary provider (first id segment, equal to providers[0]).
      const byProvider = new Map();
      for (const entry of entries) {
        const providerId = entry.id.split('/')[0];
        if (!byProvider.has(providerId)) byProvider.set(providerId, []);
        byProvider.get(providerId).push(entry);
      }

      const providers = [...byProvider.keys()].sort().map((id) => ({
        id,
        count: byProvider.get(id).length,
        logo: resolveLogo(siteDir, id),
      }));

      // priority 1: these exact routes share the /docs/query-library prefix
      // with the query-library docs-plugin instance's non-exact parent route
      // and must be matched before it.
      await actions.addRoute({
        path: '/docs/query-library',
        component: '@site/src/components/QueryLibrary/LandingPage.jsx',
        exact: true,
        priority: 1,
        props: {providers, total: entries.length, buildId: catalogue.build_id || ''},
      });

      for (const provider of providers) {
        await actions.addRoute({
          path: `/docs/query-library/${provider.id}`,
          component: '@site/src/components/QueryLibrary/ProviderPage.jsx',
          exact: true,
          priority: 1,
          props: {provider, entries: byProvider.get(provider.id)},
        });
      }
    },
  };
};
