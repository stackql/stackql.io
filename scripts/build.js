#!/usr/bin/env node
// build: `docusaurus build` once per locale, each in its own Node process,
// the default locale first (its build clears build/; every other locale
// builds into build/<baseUrl> and clears only that).
//
// Why not one `docusaurus build` for all locales: in one process the memory
// of every locale's build stays allocated (Docusaurus 3.10.2 with Rspack,
// about 1.4 GB more at the start of each locale), so 14 locales need more
// than the 11 GB of a Netlify build machine, which killed the build at the
// 9th locale (exit 137, November 2026). A process per locale returns the
// memory after each one; a single locale peaks around 4 GB. The output is
// the same, because every locale other than the default sets its baseUrl
// (docusaurus.config.js). See CLAUDE.md, "Build time".
//
// Usage: node scripts/build.js [locale ...]   (default: every locale)
// `yarn build` runs it, between the prebuild and postbuild scripts.

const path = require('path');
const { spawnSync } = require('child_process');

const root = path.resolve(__dirname, '..');
const docusaurus = require.resolve('@docusaurus/core/bin/docusaurus.mjs', { paths: [root] });
const { i18n } = require(path.join(root, 'docusaurus.config.js'))();

const requested = process.argv.slice(2);
const unknown = requested.filter((l) => !i18n.locales.includes(l));
if (unknown.length) {
  console.error(`build: unknown locale(s) ${unknown.join(', ')} (configured: ${i18n.locales.join(', ')})`);
  process.exit(1);
}
const selected = requested.length ? requested : i18n.locales;
const locales = [
  ...selected.filter((l) => l === i18n.defaultLocale),
  ...selected.filter((l) => l !== i18n.defaultLocale),
];

const started = Date.now();
for (const locale of locales) {
  const t = Date.now();
  const result = spawnSync(process.execPath, [docusaurus, 'build', '--locale', locale], {
    cwd: root,
    stdio: 'inherit',
    env: process.env,
  });
  if (result.status !== 0) {
    const how = result.signal ? `signal ${result.signal}` : `exit code ${result.status}`;
    console.error(`build: locale ${locale} failed (${how})`);
    process.exit(result.status || 1);
  }
  console.log(`build: ${locale} built in ${((Date.now() - t) / 1000).toFixed(1)} s`);
}
console.log(`build: ${locales.length} locale(s) in ${((Date.now() - started) / 1000).toFixed(1)} s`);
