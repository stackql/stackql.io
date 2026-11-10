#!/usr/bin/env node
// yarn i18n:strings [--locale <l>] [--check]
//
// Refreshes the UI string files of each configured locale with
// `docusaurus write-translations --locale <l>` (navbar, footer, sidebar
// category labels, blog instance titles, and every <Translate> / translate()
// in src/ and in the plugins' theme components), then reports the keys that
// still carry their English text: keys the command just added, and keys
// whose message equals the English label embedded in the key (navbar,
// footer and sidebar keys are built from the English label). Those
// messages are what the translator edits in place. --check exits 1 when any
// remain.

const fs = require('fs');
const path = require('path');
const { execFileSync } = require('child_process');
const lib = require('./lib');
const { config } = lib;

const DOCUSAURUS = path.join(lib.ROOT, 'node_modules', '@docusaurus', 'core', 'bin', 'docusaurus.mjs');

function readJson(p) {
  return fs.existsSync(p) ? JSON.parse(fs.readFileSync(p, 'utf8')) : null;
}

// The English text a generated key embeds, if any: item.label.Install ->
// Install, sidebar.docs.category.Getting Started -> Getting Started,
// link.title.More -> More, version.label.current -> (none).
function englishFromKey(key) {
  const m = /^(?:item\.label|link\.title|link\.item\.label|sidebar\.[^.]+\.category|sidebar\.[^.]+\.doc|sidebar\.[^.]+\.link)\.(.+)$/.exec(key);
  return m ? m[1] : null;
}

function untranslatedKeys(file, json) {
  const out = [];
  for (const [key, entry] of Object.entries(json || {})) {
    const message = entry && typeof entry === 'object' ? entry.message : entry;
    if (typeof message !== 'string') continue;
    const english = englishFromKey(key);
    if (english !== null && english === message) out.push(key);
    // code.json without an explicit id: the key is the English message
    else if (file === 'code.json' && key === message) out.push(key);
  }
  return out;
}

// Renames a directory, retrying for a while: on Windows a rename of a large
// tree fails with EPERM while an indexer, scanner or editor still holds a
// file in it.
function renameWithRetry(from, to) {
  const attempts = 20;
  for (let i = 1; i <= attempts; i++) {
    try {
      fs.renameSync(from, to);
      return;
    } catch (e) {
      if (i === attempts) throw e;
      Atomics.wait(new Int32Array(new SharedArrayBuffer(4)), 0, 0, 1000);
    }
  }
}

// Moves a parked directory back, whatever happens; prints the manual
// command when even the retries fail.
function restoreDir(tmp, dir) {
  try {
    renameWithRetry(tmp, dir);
  } catch (e) {
    console.error(`[i18n:strings] could not move ${tmp} back to ${dir} (${e.code}); run: mv "${tmp}" "${dir}"`);
    throw e;
  }
}

function refreshLocale(locale) {
  const base = path.join(lib.ROOT, config.i18nDir, locale);
  const before = Object.fromEntries(config.stringFiles.map((f) => [f, readJson(path.join(base, f))]));
  // Loading the site config needs the Algolia variables, as a local build
  // does (CLAUDE.md); the values are irrelevant to string extraction.
  const env = {
    ALGOLIA_APP_ID: 'dummy',
    ALGOLIA_API_KEY: 'dummy',
    ALGOLIA_INDEX_NAME: 'dummy',
    ...process.env,
  };
  // Directories the extractor must not read (config.extractorExcludes) are
  // renamed aside while it runs and restored afterwards, whatever happens.
  // The parking spot must be outside src/ (the extractor globs all of it);
  // the repo root is on the same volume, so the move is a rename.
  const aside = [];
  (config.extractorExcludes || []).forEach((rel, i) => {
    const dir = path.join(lib.ROOT, rel);
    if (!fs.existsSync(dir)) return;
    const tmp = path.join(lib.ROOT, `.i18n-excluded-${i}`);
    renameWithRetry(dir, tmp);
    aside.push([tmp, dir]);
  });
  try {
    execFileSync(process.execPath, [DOCUSAURUS, 'write-translations', '--locale', locale], {
      cwd: lib.ROOT,
      env,
      stdio: ['ignore', 'pipe', 'inherit'],
    });
  } finally {
    for (const [tmp, dir] of aside) restoreDir(tmp, dir);
  }
  const report = [];
  for (const file of config.stringFiles) {
    const target = path.join(base, file);
    let after = readJson(target);
    if (!after) continue;
    // Keys that must keep the site default are dropped again (config).
    const useDefault = (config.stringKeysUseDefault || {})[file] || [];
    if (useDefault.some((k) => k in after)) {
      after = Object.fromEntries(Object.entries(after).filter(([k]) => !useDefault.includes(k)));
      fs.writeFileSync(target, `${JSON.stringify(after, null, 2)}\n`);
    }
    const accepted = (config.locales[locale] && config.locales[locale].sameAsEnglish) || [];
    const added = Object.keys(after).filter((k) => !(before[file] && k in before[file]));
    const english = untranslatedKeys(file, after).filter((k) => !added.includes(k) && !accepted.includes(k));
    if (added.length || english.length) report.push({ file: `${config.i18nDir}/${locale}/${file}`, added, english });
  }
  return report;
}

function main() {
  const { flags } = lib.parseArgs(process.argv.slice(2));
  let pending = 0;
  for (const locale of lib.localesFromFlags(flags)) {
    const report = refreshLocale(locale);
    if (report.length === 0) {
      console.log(`${locale}: string files are up to date`);
      continue;
    }
    for (const { file, added, english } of report) {
      console.log(file);
      for (const k of added) console.log(`  new (English): ${k}`);
      for (const k of english) console.log(`  still English: ${k}`);
      pending += added.length + english.length;
    }
  }
  if (pending > 0) {
    console.log(`\n${pending} string(s) to translate: edit the message values in place (keys stay as they are)`);
    if (flags.check) process.exit(1);
  }
}

module.exports = { refreshLocale, untranslatedKeys };

if (require.main === module) {
  try {
    main();
  } catch (e) {
    console.error(`[i18n:strings] ${e.message}`);
    process.exit(2);
  }
}
