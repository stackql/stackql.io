#!/usr/bin/env node
// yarn i18n:diff --locale <l> <source file>
//
// Prints what changed in the English source since its translation for the
// locale was marked: a unified diff between the blob recorded in
// i18n/manifest.json and the file as it is now. The translator applies these
// hunks to the existing translation instead of retranslating the page.
// Exits 3 when there is no recorded version (translate the whole file).

const lib = require('./lib');

function diffSince(locale, file) {
  const manifest = lib.loadManifest();
  const record = lib.manifestFiles(manifest, locale)[file];
  if (!record) return { recorded: false, diff: '' };
  if (!lib.exists(file)) return { recorded: true, deleted: true, diff: '' };
  // hash-object -w stores the working-tree version so git diff can read it;
  // a loose object that nothing references is harmless and gc'd later.
  const current = lib.git(['hash-object', '-w', '--', file]).trim();
  if (current === record.blob) return { recorded: true, diff: '' };
  let diff;
  try {
    diff = lib.git(['diff', '--no-color', record.blob, current]);
  } catch (e) {
    // git diff exits 1 when the blobs differ; the diff is on stdout
    diff = e.stdout || '';
  }
  return { recorded: true, diff: diff.replace(/^(---|\+\+\+) [ab]\/[0-9a-f]+$/gm, (m, p) => `${p} ${file}`) };
}

function main() {
  const { flags, files } = lib.parseArgs(process.argv.slice(2));
  if (!flags.locale) throw new Error('--locale <l> is required');
  if (files.length !== 1) throw new Error('exactly one source file is required');
  const locale = flags.locale;
  lib.localeSources(locale);
  const { recorded, deleted, diff } = diffSince(locale, files[0]);
  if (!recorded) {
    console.log(`no recorded translation of ${files[0]} for ${locale}: translate the whole file`);
    process.exit(3);
  }
  if (deleted) {
    console.log(`${files[0]} no longer exists: remove its translation (yarn i18n:prune)`);
    return;
  }
  if (!diff) {
    console.log(`${files[0]} is unchanged since its ${locale} translation was marked`);
    return;
  }
  process.stdout.write(diff);
}

module.exports = { diffSince };

if (require.main === module) {
  try {
    main();
  } catch (e) {
    console.error(`[i18n:diff] ${e.message}`);
    process.exit(2);
  }
}
