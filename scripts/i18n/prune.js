#!/usr/bin/env node
// yarn i18n:prune [--locale <l>] [--dry-run]
//
// Deletes translations whose English source no longer exists and drops
// manifest records for missing sources. Lists what it would do with
// --dry-run.

const fs = require('fs');
const path = require('path');
const lib = require('./lib');
const { statusForLocale } = require('./status');

function prune(locale, { dryRun = false } = {}) {
  const status = statusForLocale(locale);
  const manifest = lib.loadManifest();
  const records = lib.manifestFiles(manifest, locale);
  for (const target of status.orphan) {
    console.log(`${dryRun ? 'would delete' : 'deleted'} ${target}`);
    if (!dryRun) fs.unlinkSync(path.join(lib.ROOT, target));
  }
  for (const file of status.deleted) {
    if (!records[file]) continue;
    console.log(`${dryRun ? 'would drop' : 'dropped'} manifest record ${file}`);
    if (!dryRun) delete records[file];
  }
  if (!dryRun) lib.saveManifest(manifest);
  return status.orphan.length + status.deleted.length;
}

function main() {
  const { flags } = lib.parseArgs(process.argv.slice(2));
  let n = 0;
  for (const locale of lib.localesFromFlags(flags)) n += prune(locale, { dryRun: Boolean(flags['dry-run']) });
  if (n === 0) console.log('nothing to prune');
}

module.exports = { prune };

if (require.main === module) {
  try {
    main();
  } catch (e) {
    console.error(`[i18n:prune] ${e.message}`);
    process.exit(2);
  }
}
