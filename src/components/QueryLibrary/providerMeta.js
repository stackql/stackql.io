import {providers as providerConfigs} from '@site/src/configs/providers';

// Display metadata for a provider id. Falls back to a capitalized id and a
// generic blurb so a brand-new provider's first merged query renders without
// any manual step; add the provider to src/configs/providers.ts for a proper
// title and description.
export function providerMeta(id) {
  const cfg = providerConfigs.find((p) => p.name === id);
  const title = cfg ? cfg.title : id.charAt(0).toUpperCase() + id.slice(1).replace(/_/g, ' ');
  return {
    title,
    description: cfg ? cfg.description : `Curated StackQL queries for ${title}.`,
  };
}
