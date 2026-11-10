#!/usr/bin/env node
// yarn i18n:status [--locale <l>] [--staged] [--json] [--check] [--hook]
//
// Which source pages need translating for each configured locale, from the
// working tree (default) or from the staged changes (--staged, used by the
// pre-commit hook). For every source file of the locale's source sets:
//
//   new         no translation file exists
//   changed     a translation exists but the English source's blob differs
//               from the one recorded in i18n/manifest.json
//   unverified  a translation exists with no manifest record (never passed
//               yarn i18n:verify and yarn i18n:mark)
//   current     the translation matches the recorded source
//   orphan      a translation whose source no longer exists (yarn i18n:prune)
//   deleted     a staged deletion (--staged) or a manifest record whose
//               source is gone
//
// --check exits 1 when anything is pending (for a pre-push check or a build
// log). --json prints the lists as JSON for other scripts.

const lib = require('./lib');
const { config } = lib;

function statusForLocale(locale, { staged = false } = {}) {
  const manifest = lib.loadManifest();
  const recorded = lib.manifestFiles(manifest, locale);
  const result = { locale, new: [], changed: [], unverified: [], current: [], orphan: [], deleted: [] };
  const sources = lib.localeSources(locale);

  const classify = (file, blob) => {
    const target = lib.targetPath(locale, file);
    const entry = recorded[file];
    if (!lib.exists(target)) result.new.push(file);
    else if (!entry) result.unverified.push(file);
    else if (entry.blob !== blob) result.changed.push(file);
    else result.current.push(file);
  };

  if (staged) {
    const dirs = sources.map((key) => config.sources[key].dir);
    const changes = lib.stagedChanges(dirs).filter(
      (c) => lib.isTranslatable(c.path) && sources.includes(lib.sourceKeyFor(c.path)),
    );
    const hashes = lib.indexHashes(changes.filter((c) => c.status !== 'D').map((c) => c.path));
    for (const change of changes) {
      if (change.status === 'D') result.deleted.push(change.path);
      else classify(change.path, hashes.get(change.path));
      if (change.status === 'R' && change.from && lib.sourceKeyFor(change.from)) {
        if (lib.exists(lib.targetPath(locale, change.from))) result.orphan.push(lib.targetPath(locale, change.from));
      }
    }
    return result;
  }

  for (const key of sources) {
    const files = lib.listSourceFiles(key);
    const hashes = lib.hashFiles(files);
    for (const file of files) classify(file, hashes.get(file));
    const known = new Set(files);
    for (const target of lib.listTargetFiles(locale, key)) {
      const source = lib.sourcePathFor(locale, target);
      if (!source || !known.has(source)) result.orphan.push(target);
    }
  }
  for (const file of Object.keys(recorded)) {
    if (!lib.exists(file)) result.deleted.push(file);
  }
  return result;
}

function pendingCount(status) {
  return status.new.length + status.changed.length + status.unverified.length + status.orphan.length + status.deleted.length;
}

function printStatus(status, { hook = false } = {}) {
  const pending = pendingCount(status);
  if (hook) {
    if (pending === 0) return;
    const parts = [];
    if (status.new.length) parts.push(`${status.new.length} new`);
    if (status.changed.length) parts.push(`${status.changed.length} changed`);
    if (status.unverified.length) parts.push(`${status.unverified.length} unverified`);
    if (status.deleted.length) parts.push(`${status.deleted.length} deleted`);
    if (status.orphan.length) parts.push(`${status.orphan.length} orphan`);
    console.log(`[i18n] ${status.locale}: ${parts.join(', ')}`);
    for (const f of [...status.new, ...status.changed, ...status.unverified]) console.log(`[i18n]   ${f}`);
    return;
  }
  console.log(`${status.locale}: ${status.current.length} current, ${pending} pending`);
  const section = (name, items) => {
    if (items.length === 0) return;
    console.log(`  ${name} (${items.length})`);
    for (const f of items) console.log(`    ${f}`);
  };
  section('new', status.new);
  section('changed', status.changed);
  section('unverified', status.unverified);
  section('deleted', status.deleted);
  section('orphan', status.orphan);
}

function main() {
  const { flags } = lib.parseArgs(process.argv.slice(2));
  const locales = lib.localesFromFlags(flags);
  const statuses = locales.map((locale) => statusForLocale(locale, { staged: Boolean(flags.staged) }));
  if (flags.json) {
    console.log(JSON.stringify(statuses, null, 2));
  } else {
    for (const status of statuses) printStatus(status, { hook: Boolean(flags.hook) });
    if (!flags.hook && statuses.some((s) => pendingCount(s) > 0)) {
      console.log('\nnext: translate the pending pages (see .claude/skills/i18n-translate), then yarn i18n:verify and yarn i18n:mark');
    }
  }
  if (flags.check && statuses.some((s) => pendingCount(s) > 0)) process.exit(1);
}

module.exports = { statusForLocale, pendingCount, printStatus };

if (require.main === module) {
  try {
    main();
  } catch (e) {
    console.error(`[i18n:status] ${e.message}`);
    process.exit(2);
  }
}
