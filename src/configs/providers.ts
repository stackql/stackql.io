import siteConfig from '@generated/docusaurus.config';
import providersData from './providers-data.json';

// Provider display metadata lives in providers-data.json so non-webpack
// consumers (sidebars-query-library.js, query-library build scripts) can
// read the same source of truth.
export const providers = providersData;

export const getProviderSiteUrl = (name: string) =>{
    const registry = siteConfig.customFields?.registry
    return registry === name? `/providers/${name}`: `https://${name.replace('_', '-')}.stackql.io/providers/${name}`
}
