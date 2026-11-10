// Shared helpers for the i18n scripts (scripts/i18n). Plain Node, no
// dependencies beyond git.
//
// What is tracked and how:
// - A translation is a file under i18n/<locale>/<target>/ mirroring a source
//   file under docs/, ai-content/, blog/<section>/ or src/pages/ (the source
//   sets in config.js).
// - i18n/manifest.json records, per locale and source file, the git blob SHA
//   of the English source the translation was made from. `git hash-object`
//   gives a working-tree file the same SHA a commit records for it, so the
//   comparison works on uncommitted edits and survives rebases and squash
//   merges (nothing depends on commit history).
// - A source whose current blob differs from the recorded one has a stale
//   translation; a source with no entry has no translation, or one that was
//   never verified and marked.

const fs = require('fs');
const path = require('path');
const { execFileSync } = require('child_process');
const config = require('./config');

const ROOT = path.resolve(__dirname, '..', '..');

function toPosix(p) {
  return p.split(path.sep).join('/');
}

function git(args, opts = {}) {
  return execFileSync('git', args, { cwd: ROOT, encoding: 'utf8', maxBuffer: 64 * 1024 * 1024, ...opts });
}

function isTranslatable(file) {
  return config.extensions.includes(path.extname(file).toLowerCase());
}

function walkFiles(absDir, predicate) {
  const out = [];
  if (!fs.existsSync(absDir)) return out;
  (function walk(d) {
    for (const entry of fs.readdirSync(d, { withFileTypes: true })) {
      const p = path.join(d, entry.name);
      if (entry.isDirectory()) walk(p);
      else if (predicate(entry.name)) out.push(toPosix(path.relative(ROOT, p)));
    }
  })(absDir);
  return out.sort();
}

// Source files of one source set, as repo-relative posix paths, sorted.
function listSourceFiles(sourceKey) {
  return walkFiles(path.join(ROOT, config.sources[sourceKey].dir), isTranslatable);
}

// Translation files present for one locale and source set.
function listTargetFiles(locale, sourceKey) {
  return walkFiles(path.join(ROOT, config.i18nDir, locale, config.sources[sourceKey].target), isTranslatable);
}

// The source set a repo-relative path belongs to (longest directory wins),
// or null.
function sourceKeyFor(file) {
  let best = null;
  for (const [key, { dir }] of Object.entries(config.sources)) {
    const inside = file === dir || file.startsWith(`${dir}/`);
    if (inside && (!best || dir.length > config.sources[best].dir.length)) best = key;
  }
  return best;
}

function configuredLocales() {
  return Object.keys(config.locales);
}

function localeSources(locale) {
  const entry = config.locales[locale];
  if (!entry) {
    throw new Error(`locale "${locale}" is not configured in scripts/i18n/config.js (configured: ${configuredLocales().join(', ') || 'none'})`);
  }
  return entry.sources;
}

// i18n/<locale>/<target>/<file relative to its source dir>
function targetPath(locale, file) {
  const key = sourceKeyFor(file);
  if (!key) throw new Error(`${file} is not under a configured source set`);
  const { dir, target } = config.sources[key];
  return toPosix(path.join(config.i18nDir, locale, target, path.relative(dir, file)));
}

// Inverse of targetPath: the source path a translation file mirrors, or
// null when the path is not under a known target.
function sourcePathFor(locale, target) {
  for (const { dir, target: t } of Object.values(config.sources)) {
    const prefix = `${config.i18nDir}/${locale}/${t}/`;
    if (target.startsWith(prefix)) return toPosix(path.join(dir, target.slice(prefix.length)));
  }
  return null;
}

// Blob SHAs of working-tree files (git filters applied, so equal to what a
// commit of the file would record). Map path -> sha.
function hashFiles(files) {
  const map = new Map();
  if (files.length === 0) return map;
  const out = git(['hash-object', '--stdin-paths'], { input: `${files.join('\n')}\n` });
  const shas = out.trim().split('\n');
  files.forEach((f, i) => map.set(f, shas[i]));
  return map;
}

// Blob SHAs as staged in the index. Map path -> sha.
function indexHashes(files) {
  const map = new Map();
  if (files.length === 0) return map;
  const out = git(['ls-files', '-s', '--', ...files]);
  for (const line of out.split('\n')) {
    if (!line) continue;
    const [meta, p] = line.split('\t');
    map.set(p, meta.split(' ')[1]);
  }
  return map;
}

// Staged changes under the given directories:
// [{ status: 'A' | 'M' | 'D' | 'R', path, from? }]
function stagedChanges(dirs) {
  if (dirs.length === 0) return [];
  const out = git(['diff', '--cached', '--name-status', '-M', '--', ...dirs]);
  const changes = [];
  for (const line of out.split('\n')) {
    if (!line) continue;
    const parts = line.split('\t');
    const status = parts[0][0];
    if (status === 'R') changes.push({ status, from: parts[1], path: parts[2] });
    else changes.push({ status, path: parts[1] });
  }
  return changes;
}

function manifestPath() {
  return path.join(ROOT, config.manifest);
}

function loadManifest() {
  if (!fs.existsSync(manifestPath())) return { version: 1, locales: {} };
  const parsed = JSON.parse(fs.readFileSync(manifestPath(), 'utf8'));
  if (!parsed.locales) parsed.locales = {};
  return parsed;
}

function sortedObject(obj) {
  return Object.fromEntries(Object.keys(obj).sort().map((k) => [k, obj[k]]));
}

function saveManifest(manifest) {
  const locales = {};
  for (const locale of Object.keys(manifest.locales).sort()) {
    const entry = manifest.locales[locale];
    locales[locale] = { files: sortedObject(entry.files || {}) };
  }
  const out = { version: manifest.version || 1, locales };
  fs.mkdirSync(path.dirname(manifestPath()), { recursive: true });
  fs.writeFileSync(manifestPath(), `${JSON.stringify(out, null, 2)}\n`);
}

// The per-file records of one locale (created on demand).
function manifestFiles(manifest, locale) {
  if (!manifest.locales[locale]) manifest.locales[locale] = { files: {} };
  if (!manifest.locales[locale].files) manifest.locales[locale].files = {};
  return manifest.locales[locale].files;
}

function normalizeText(s) {
  return s.replace(/\r\n?/g, '\n');
}

function exists(p) {
  return fs.existsSync(path.join(ROOT, p));
}

function readText(p) {
  return normalizeText(fs.readFileSync(path.join(ROOT, p), 'utf8'));
}

// Writes LF text ending in exactly one newline.
function writeText(p, text) {
  const abs = path.join(ROOT, p);
  fs.mkdirSync(path.dirname(abs), { recursive: true });
  fs.writeFileSync(abs, `${normalizeText(text).replace(/\n*$/, '')}\n`);
}

function today() {
  return new Date().toISOString().slice(0, 10);
}

// Minimal argv parser: --flag, --flag value (for names in `valued`), and
// positional paths (normalised to repo-relative posix).
function parseArgs(argv, valued = ['locale', 'concurrency']) {
  const flags = {};
  const files = [];
  for (let i = 0; i < argv.length; i++) {
    const a = argv[i];
    if (a.startsWith('--')) {
      const name = a.slice(2);
      const eq = name.indexOf('=');
      if (eq !== -1) flags[name.slice(0, eq)] = name.slice(eq + 1);
      else if (valued.includes(name) && i + 1 < argv.length && !argv[i + 1].startsWith('--')) flags[name] = argv[++i];
      else flags[name] = true;
    } else {
      files.push(toPosix(path.relative(ROOT, path.resolve(ROOT, a))));
    }
  }
  return { flags, files };
}

// The locales a command applies to: --locale <l>, else every configured one.
function localesFromFlags(flags) {
  if (flags.locale) {
    localeSources(flags.locale);
    return [flags.locale];
  }
  const all = configuredLocales();
  if (all.length === 0) throw new Error('no locale is configured in scripts/i18n/config.js');
  return all;
}

module.exports = {
  ROOT,
  config,
  git,
  toPosix,
  isTranslatable,
  listSourceFiles,
  listTargetFiles,
  sourceKeyFor,
  configuredLocales,
  localeSources,
  localesFromFlags,
  targetPath,
  sourcePathFor,
  hashFiles,
  indexHashes,
  stagedChanges,
  loadManifest,
  saveManifest,
  manifestFiles,
  normalizeText,
  exists,
  readText,
  writeText,
  today,
  parseArgs,
};
