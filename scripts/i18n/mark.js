#!/usr/bin/env node
// yarn i18n:mark --locale <l> [source files...] [--force]
//
// Records in i18n/manifest.json which English source each translation was
// made from (the source file's current git blob SHA). Default: every source
// file of the locale's source sets that has a translation. A file is only
// recorded when it passes yarn i18n:verify; --force records it regardless.
// After marking, yarn i18n:status reports the file as current until the
// English source changes again.

const lib = require('./lib');
const { verifyFile, translatedSources } = require('./verify');

function markFiles(locale, files, { force = false } = {}) {
  const manifest = lib.loadManifest();
  const records = lib.manifestFiles(manifest, locale);
  const hashes = lib.hashFiles(files.filter((f) => lib.exists(f)));
  const marked = [];
  const rejected = [];
  for (const file of files) {
    const result = verifyFile(locale, file);
    if (result.errors.length > 0 && !force) {
      rejected.push(result);
      continue;
    }
    records[file] = { blob: hashes.get(file), at: lib.today() };
    marked.push(file);
  }
  lib.saveManifest(manifest);
  return { marked, rejected };
}

function main() {
  const { flags, files } = lib.parseArgs(process.argv.slice(2));
  if (!flags.locale) throw new Error('--locale <l> is required');
  const locale = flags.locale;
  lib.localeSources(locale);
  const list = files.length > 0 ? files : translatedSources(locale);
  const { marked, rejected } = markFiles(locale, list, { force: Boolean(flags.force) });
  for (const r of rejected) {
    console.log(`FAIL ${r.file}`);
    for (const e of r.errors) console.log(`       error: ${e}`);
  }
  console.log(`${locale}: marked ${marked.length} file(s)${rejected.length ? `, ${rejected.length} rejected by verify` : ''}`);
  if (rejected.length > 0) process.exit(1);
}

module.exports = { markFiles };

if (require.main === module) {
  try {
    main();
  } catch (e) {
    console.error(`[i18n:mark] ${e.message}`);
    process.exit(2);
  }
}
