#!/usr/bin/env node
// Regenerates the "generated provider redirects" block in netlify.toml.
//
// Every provider has a microsite at https://<slug>-provider.stackql.io/.
// This site exposes two families of short URLs for them, both served as
// Netlify 301s rather than built pages:
//
//   /providers/<slug>   one rule per catalog entry, no exceptions
//   /registry           the catalog page (/providers)
//   /registry/<name>    one rule per catalog entry plus each entry's
//                       registryAliases (family-level names such as
//                       /registry/databricks), the inbound surface for
//                       external links
//
// These used to be Docusaurus routes rendering a meta-refresh page. Google
// treats a meta refresh as a redirect but the page itself is a 200 that the
// sitemap plugin lists, so Search Console reported every one of them as
// "Page with redirect". An edge 301 is neither built nor listed.
//
// The rules are derived from src/configs/providers.json through
// src/lib/providers.js, the same helpers the tiles and navbar use.
//
// Usage: node scripts/generate-provider-redirects.js
//
// Rerun after adding, removing or renaming a catalog entry or alias.

const fs = require('fs');
const path = require('path');

const { providerRoutes, registryRoutes } = require('../src/lib/providers');

const ROOT = path.resolve(__dirname, '..');
const NETLIFY_TOML = path.join(ROOT, 'netlify.toml');
const LANDING = '/providers';

const BEGIN =
  '# BEGIN generated provider redirects - do not edit by hand, run scripts/generate-provider-redirects.js';
const END = '# END generated provider redirects';

function rule(from, to) {
  return ['[[redirects]]', `  from = "${from}"`, `  to = "${to}"`, '  status = 301', ''].join('\n');
}

function generate() {
  const lines = [BEGIN, ''];
  for (const { slug, href } of providerRoutes()) {
    lines.push(rule(`/providers/${slug}`, href));
  }
  lines.push(rule('/registry', LANDING));
  for (const { slug, href } of registryRoutes()) {
    lines.push(rule(`/registry/${slug}`, href));
  }
  lines.push(END);
  // one rule per line group: providers + bare registry + registry names
  const count = providerRoutes().length + 1 + registryRoutes().length;
  return { block: lines.join('\n'), count };
}

function main() {
  const toml = fs.readFileSync(NETLIFY_TOML, 'utf8');
  const start = toml.indexOf(BEGIN);
  const end = toml.indexOf(END);
  if (start === -1 || end === -1 || end < start) {
    throw new Error(`netlify.toml must contain the markers:\n${BEGIN}\n${END}`);
  }
  const { block, count } = generate();
  const next = toml.slice(0, start) + block + toml.slice(end + END.length);
  if (next !== toml) {
    fs.writeFileSync(NETLIFY_TOML, next);
    console.log(`netlify.toml updated: ${count} provider redirect rules`);
  } else {
    console.log(`netlify.toml unchanged: ${count} provider redirect rules`);
  }
}

main();
