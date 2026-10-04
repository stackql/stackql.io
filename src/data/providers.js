// Provider catalog - the single source of truth for everything provider
// related on this site:
//
//   - the tiles and table of contents on /docs/providers (docs/providers.md
//     imports PROVIDER_CATEGORIES); tiles link to /providers/<slug>
//   - the navbar "Providers" dropdown (entries with `featured: true`, see
//     docusaurus.config.js)
//   - two families of Docusaurus head redirects to the provider microsites
//     at https://<slug>-provider.stackql.io/ (plugins/provider-redirects):
//
//       /providers/<slug>   explicit: exactly one route per catalog entry,
//                           no exceptions. Used internally (tiles, navbar).
//       /registry/<name>    the inbound surface for external links: one
//                           route per catalog entry plus `registryAliases`,
//                           so a provider family (databricks_account,
//                           databricks_workspace, ...) can expose a single
//                           canonical inbound link such as /registry/databricks.
//
// To add a provider, add one entry here. Nothing else needs to change: the
// tile, both redirect routes and (if featured) the menu item all follow.
//
// Entry shape:
//   name             display name: tile label, default menu label
//   href             the microsite URL, must be https://<slug>-provider.stackql.io/
//                    (the slug is derived from it)
//   icon             tile icon, a path under static/
//   invertOnDark     optional, invert the icon in dark mode
//   featured         optional, true to list it in the navbar dropdown
//   shortName        optional, menu label when `name` is too long for a menu
//   registryAliases  optional, extra names that redirect to this microsite
//                    under /registry only (never under /providers)
//
// Derived at load (do not set by hand): `slug` and `path` (/providers/<slug>).
//
// CommonJS on purpose: docusaurus.config.js and the local plugin require()
// this at config time, and MDX/React import it through webpack.

const PROVIDER_CATEGORIES = [
  {
    id: 'cloud-providers',
    name: 'Cloud Providers',
    providers: [
      {
        name: 'Amazon Web Services',
        shortName: 'AWS',
        featured: true,
        href: 'https://aws-provider.stackql.io/',
        icon: '/img/providers/aws/favicon.ico',
      },
      {
        name: 'Microsoft Azure',
        shortName: 'Azure',
        featured: true,
        href: 'https://azure-provider.stackql.io/',
        icon: '/img/providers/azure/favicon.ico',
      },
      {
        name: 'Google Cloud Platform',
        shortName: 'Google',
        featured: true,
        href: 'https://google-provider.stackql.io/',
        icon: '/img/providers/google/favicon.ico',
      },
      {
        name: 'Digital Ocean',
        href: 'https://digitalocean-provider.stackql.io/',
        icon: '/img/providers/digitalocean/favicon.png',
      },
      {
        name: 'Cloudflare',
        featured: true,
        href: 'https://cloudflare-provider.stackql.io/',
        icon: '/img/providers/cloudflare/favicon.ico',
      },
      {
        name: 'Linode',
        href: 'https://linode-provider.stackql.io/',
        icon: '/img/providers/linode/favicon.ico',
      },
      {
        name: 'AWS Cloud Control',
        href: 'https://awscc-provider.stackql.io/',
        icon: '/img/providers/aws/favicon.ico',
      },
      {
        name: 'Azure Stack',
        href: 'https://azure-stack-provider.stackql.io/',
        icon: '/img/providers/azure/favicon.ico',
      },
      {
        name: 'Azure ISV',
        href: 'https://azure-isv-provider.stackql.io/',
        icon: '/img/providers/azure/favicon.ico',
      },
    ],
  },
  {
    id: 'data-analytics',
    name: 'Data & Analytics',
    providers: [
      {
        name: 'Databricks Account',
        shortName: 'Databricks',
        featured: true,
        // the one canonical inbound link for the databricks family; the
        // pre-split /providers/databricks stub pointed here too
        registryAliases: ['databricks'],
        href: 'https://databricks-account-provider.stackql.io/',
        icon: '/img/providers/databricks/favicon.ico',
      },
      {
        name: 'Databricks Workspace',
        href: 'https://databricks-workspace-provider.stackql.io/',
        icon: '/img/providers/databricks/favicon.ico',
      },
      {
        name: 'Snowflake',
        featured: true,
        href: 'https://snowflake-provider.stackql.io/',
        icon: '/img/providers/snowflake/favicon.ico',
      },
      {
        name: 'ClickHouse',
        href: 'https://clickhouse-provider.stackql.io/',
        icon: '/img/providers/clickhouse/favicon.ico',
      },
      {
        name: 'Fivetran',
        href: 'https://fivetran-provider.stackql.io/',
        icon: '/img/providers/fivetran/favicon.png',
      },
      {
        name: 'Confluent',
        featured: true,
        href: 'https://confluent-provider.stackql.io/',
        icon: '/img/providers/confluent/favicon.ico',
      },
      {
        name: 'Kafka',
        href: 'https://kafka-provider.stackql.io/',
        icon: '/img/providers/kafka/kafka.png',
        invertOnDark: true,
      },
    ],
  },
  {
    id: 'identity-security',
    name: 'Identity & Security',
    providers: [
      {
        name: 'Okta',
        featured: true,
        href: 'https://okta-provider.stackql.io/',
        icon: '/img/providers/okta/favicon.png',
        invertOnDark: true,
      },
      {
        name: 'Google Admin',
        href: 'https://googleadmin-provider.stackql.io/',
        icon: '/img/providers/googleadmin/favicon.ico',
      },
      {
        name: 'Microsoft Entra ID',
        href: 'https://entra-id-provider.stackql.io/',
        icon: '/img/providers/entra_id/entra_id_icon.svg',
      },
    ],
  },
  {
    id: 'ai-machine-learning',
    name: 'AI & Machine Learning',
    providers: [
      {
        name: 'OpenAI',
        featured: true,
        href: 'https://openai-provider.stackql.io/',
        icon: '/img/providers/openai/favicon.ico',
        invertOnDark: true,
      },
      {
        name: 'OpenAI Admin',
        href: 'https://openai-admin-provider.stackql.io/',
        icon: '/img/providers/openai/favicon.ico',
        invertOnDark: true,
      },
      {
        name: 'Anthropic',
        href: 'https://anthropic-provider.stackql.io/',
        icon: '/img/providers/anthropic/favicon.png',
      },
      {
        name: 'Anthropic Admin',
        href: 'https://anthropic-admin-provider.stackql.io/',
        icon: '/img/providers/anthropic/favicon.png',
      },
      {
        name: 'Google Gemini',
        // "gemeni" is the live hostname; gemini-provider.stackql.io does not resolve
        href: 'https://gemeni-provider.stackql.io/',
        icon: '/img/providers/gemeni/favicon.png',
      },
      {
        name: 'TypeSafe AI',
        href: 'https://typesafe-provider.stackql.io/',
        icon: '/img/providers/typesafe/favicon.png',
      },
    ],
  },
  {
    id: 'devops-development',
    name: 'DevOps & Development',
    providers: [
      {
        name: 'GitHub',
        featured: true,
        href: 'https://github-provider.stackql.io/',
        icon: '/img/providers/github/favicon.ico',
        invertOnDark: true,
      },
      {
        name: 'Kubernetes',
        href: 'https://k8s-provider.stackql.io/',
        icon: '/img/providers/k8s/favicon.png',
      },
      {
        name: 'GitLab',
        href: 'https://gitlab-provider.stackql.io/',
        icon: '/img/providers/gitlab/favicon.ico',
      },
      {
        name: 'Firebase',
        href: 'https://firebase-provider.stackql.io/',
        icon: '/img/providers/firebase/favicon.png',
      },
      {
        name: 'Railway',
        href: 'https://railway-provider.stackql.io/',
        icon: '/img/providers/railway/favicon.ico',
        invertOnDark: true,
      },
      {
        name: 'Vercel',
        href: 'https://vercel-provider.stackql.io/',
        icon: '/img/providers/vercel/favicon.ico',
      },
      {
        name: 'Netlify',
        href: 'https://netlify-provider.stackql.io/',
        icon: '/img/providers/netlify/favicon.ico',
      },
      {
        name: 'Deno Deploy',
        href: 'https://deno-provider.stackql.io/',
        icon: '/img/providers/deno/favicon.ico',
      },
    ],
  },
  {
    id: 'monitoring-observability',
    name: 'Monitoring & Observability',
    providers: [
      {
        name: 'DataDog',
        href: 'https://datadog-provider.stackql.io/',
        icon: '/img/providers/datadog/favicon.ico',
      },
      {
        name: 'SumoLogic',
        href: 'https://sumologic-provider.stackql.io/',
        icon: '/img/providers/sumologic/favicon.png',
      },
      {
        name: 'PagerDuty',
        href: 'https://pagerduty-provider.stackql.io/',
        icon: '/img/providers/pagerduty/icon.svg',
      },
    ],
  },
  {
    id: 'other-providers',
    name: 'Other Providers',
    providers: [
      {
        name: 'Google Workspace',
        href: 'https://googleworkspace-provider.stackql.io/',
        icon: '/img/providers/googleworkspace/favicon.ico',
      },
      {
        name: 'GoDaddy',
        href: 'https://godaddy-provider.stackql.io/',
        icon: '/img/providers/godaddy/favicon.png',
      },
      {
        name: 'Azure Extras',
        href: 'https://azure-extras-provider.stackql.io/',
        icon: '/img/providers/azure/favicon.ico',
      },
      {
        name: 'Homebrew',
        href: 'https://homebrew-provider.stackql.io/',
        icon: '/img/providers/homebrew/favicon.ico',
      },
    ],
  },
];

// Every microsite is https://<slug>-provider.stackql.io/. The slug is the
// path segment used by the alias routes.
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

// Derive `slug` and `path` on every entry so consumers of
// PROVIDER_CATEGORIES (the tiles) get them without recomputing.
for (const category of PROVIDER_CATEGORIES) {
  for (const provider of category.providers) {
    provider.slug = slugFromHref(provider.href);
    provider.path = `/providers/${provider.slug}`;
  }
}

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
