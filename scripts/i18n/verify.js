#!/usr/bin/env node
// yarn i18n:verify --locale <l> [source files...] [--json] [--quiet]
//
// Structural checks of a translation against its English source: everything
// that is not prose must survive translation unchanged. Default: every
// source file of the locale's source sets that has a translation. Exit 1 on
// any failing file.
//
// Errors (the translation is rejected):
//   - missing translation, CR line endings, no trailing newline
//   - front matter: a key added or dropped; a protected key (id, slug,
//     sidebar_position, hide_*, date, image, tags, ...) with a different value
//   - import/export lines that differ
//   - fenced code blocks that differ in number, order or content
//   - link targets (markdown links, reference definitions, href/to/src
//     attributes, autolinks) that differ as a multiset
//   - heading levels in a different order, or heading {#ids} that differ
//   - JSX/HTML tag names or admonition markers that differ as a multiset
// Warnings (reported, not fatal):
//   - inline code spans that differ as a multiset
//   - a translation identical to its source, or much shorter or longer

const lib = require('./lib');

const PROTECTED_KEYS = [
  'id', 'slug', 'sidebar_position', 'sidebar_class_name', 'displayed_sidebar',
  'hide_title', 'hide_table_of_contents', 'hide_last_update', 'hide_translation_notice',
  'toc_min_heading_level', 'toc_max_heading_level', 'pagination_next', 'pagination_prev',
  'custom_edit_url', 'draft', 'unlisted', 'date', 'image', 'format', 'tags', 'authors',
  'softwareApplication', 'proficiencyLevel', 'dependencies', 'last_update',
];

function splitFrontMatter(text) {
  const m = /^---\n([\s\S]*?)\n---(?:\n|$)/.exec(text);
  if (!m) return { frontMatter: null, body: text };
  return { frontMatter: m[1], body: text.slice(m[0].length) };
}

// Top-level keys of a YAML front matter block, in order, with the rest of
// the key's line (its scalar value, or '' for a nested value).
function frontMatterKeys(block) {
  const keys = [];
  for (const line of block.split('\n')) {
    const m = /^([A-Za-z_][\w-]*):\s*(.*)$/.exec(line);
    if (m) keys.push({ key: m[1], value: m[2].trim() });
  }
  return keys;
}

// Lines outside fenced code, and the fenced blocks (opening line included).
function splitFences(body) {
  const prose = [];
  const fences = [];
  let open = null;
  let current = [];
  for (const line of body.split('\n')) {
    const m = /^\s*(`{3,}|~{3,})/.exec(line);
    if (!open && m) {
      open = m[1];
      current = [line];
      continue;
    }
    if (open) {
      current.push(line);
      if (m && m[1][0] === open[0] && m[1].length >= open.length && line.trim() === m[1]) {
        fences.push(current.join('\n'));
        open = null;
      }
      continue;
    }
    prose.push(line);
  }
  if (open) fences.push(current.join('\n'));
  return { prose: prose.join('\n'), fences };
}

function sortedMultiset(items) {
  return [...items].sort();
}

function linkTargets(prose) {
  const out = [];
  for (const m of prose.matchAll(/\]\(\s*(<[^>]*>|[^)\s]+)(?:\s+["'(][^)]*)?\)/g)) out.push(m[1]);
  for (const m of prose.matchAll(/^\s*\[[^\]]+\]:\s*(\S+)/gm)) out.push(m[1]);
  for (const m of prose.matchAll(/\b(?:href|to|src)\s*=\s*["']([^"']+)["']/g)) out.push(m[1]);
  for (const m of prose.matchAll(/<(https?:\/\/[^>\s]+)>/g)) out.push(m[1]);
  return sortedMultiset(out);
}

function headings(prose) {
  const out = [];
  for (const line of prose.split('\n')) {
    const m = /^(#{1,6})\s+(.*?)\s*$/.exec(line);
    if (!m) continue;
    const id = /\{#([^}]+)\}\s*$/.exec(m[2]);
    out.push({ level: m[1].length, id: id ? id[1].trim() : null });
  }
  return out;
}

function tagNames(prose) {
  const out = [];
  for (const m of prose.matchAll(/<([A-Za-z][\w.-]*)(?=[\s/>])/g)) out.push(m[1]);
  return sortedMultiset(out);
}

function admonitions(prose) {
  const out = [];
  for (const m of prose.matchAll(/^\s*(:::+\w*)/gm)) out.push(m[1]);
  return sortedMultiset(out);
}

function inlineCode(prose) {
  const out = [];
  for (const m of prose.matchAll(/`([^`\n]+)`/g)) out.push(m[1]);
  return sortedMultiset(out);
}

function importLines(body) {
  return sortedMultiset(body.split('\n').filter((l) => /^(import|export)\s/.test(l)).map((l) => l.trim()));
}

function diffMultiset(a, b) {
  const count = new Map();
  for (const x of a) count.set(x, (count.get(x) || 0) + 1);
  for (const x of b) count.set(x, (count.get(x) || 0) - 1);
  const missing = [];
  const extra = [];
  for (const [x, n] of count) {
    if (n > 0) missing.push(`${x}${n > 1 ? ` x${n}` : ''}`);
    if (n < 0) extra.push(`${x}${n < -1 ? ` x${-n}` : ''}`);
  }
  return { missing, extra };
}

function describeDiff(label, a, b) {
  const { missing, extra } = diffMultiset(a, b);
  if (missing.length === 0 && extra.length === 0) return null;
  const parts = [];
  if (missing.length) parts.push(`missing ${missing.slice(0, 5).join(', ')}${missing.length > 5 ? ` (+${missing.length - 5})` : ''}`);
  if (extra.length) parts.push(`extra ${extra.slice(0, 5).join(', ')}${extra.length > 5 ? ` (+${extra.length - 5})` : ''}`);
  return `${label}: ${parts.join('; ')}`;
}

// Compares a source text with its translation. Returns { errors, warnings }.
function compareTexts(sourceText, targetText) {
  const errors = [];
  const warnings = [];
  const source = lib.normalizeText(sourceText);
  const target = targetText;

  if (/\r/.test(targetText)) errors.push('CR line endings (write LF)');
  if (!target.endsWith('\n')) errors.push('no trailing newline');
  if (target.trim() === source.trim()) warnings.push('identical to the source (untranslated?)');

  const s = splitFrontMatter(source);
  const t = splitFrontMatter(target.replace(/\r/g, ''));
  if (Boolean(s.frontMatter) !== Boolean(t.frontMatter)) {
    errors.push(s.frontMatter ? 'front matter missing' : 'front matter added');
  } else if (s.frontMatter) {
    const sk = frontMatterKeys(s.frontMatter);
    const tk = frontMatterKeys(t.frontMatter);
    const keyDiff = describeDiff('front matter keys', sk.map((k) => k.key), tk.map((k) => k.key));
    if (keyDiff) errors.push(keyDiff);
    const tv = new Map(tk.map((k) => [k.key, k.value]));
    for (const { key, value } of sk) {
      if (PROTECTED_KEYS.includes(key) && tv.has(key) && tv.get(key) !== value) {
        errors.push(`front matter ${key}: "${value}" became "${tv.get(key)}"`);
      }
    }
  }

  const sf = splitFences(s.body);
  const tf = splitFences(t.body);
  if (sf.fences.length !== tf.fences.length) {
    errors.push(`code fences: ${sf.fences.length} in source, ${tf.fences.length} in translation`);
  } else {
    sf.fences.forEach((block, i) => {
      const norm = (b) => b.split('\n').map((l) => l.replace(/\s+$/, '')).join('\n');
      if (norm(block) !== norm(tf.fences[i])) {
        const first = block.split('\n')[0].trim();
        errors.push(`code fence ${i + 1} (${first || '```'}) differs`);
      }
    });
  }

  const imports = describeDiff('import/export lines', importLines(s.body), importLines(t.body));
  if (imports) errors.push(imports);

  const links = describeDiff('link targets', linkTargets(sf.prose), linkTargets(tf.prose));
  if (links) errors.push(links);

  const sh = headings(sf.prose);
  const th = headings(tf.prose);
  if (sh.map((h) => h.level).join(',') !== th.map((h) => h.level).join(',')) {
    errors.push(`heading levels: ${sh.map((h) => h.level).join(',') || 'none'} in source, ${th.map((h) => h.level).join(',') || 'none'} in translation`);
  }
  const ids = describeDiff('heading ids', sh.map((h) => h.id).filter(Boolean), th.map((h) => h.id).filter(Boolean));
  if (ids) errors.push(ids);

  const tags = describeDiff('JSX/HTML tags', tagNames(sf.prose), tagNames(tf.prose));
  if (tags) errors.push(tags);

  const adm = describeDiff('admonitions', admonitions(sf.prose), admonitions(tf.prose));
  if (adm) errors.push(adm);

  const code = describeDiff('inline code', inlineCode(sf.prose), inlineCode(tf.prose));
  if (code) warnings.push(code);

  const ratio = t.body.length / Math.max(1, s.body.length);
  if (ratio < 0.6 || ratio > 2.2) warnings.push(`length ratio ${ratio.toFixed(2)} (translation/source)`);

  return { errors, warnings };
}

// Verifies one source file's translation for a locale.
function verifyFile(locale, file) {
  const target = lib.targetPath(locale, file);
  if (!lib.exists(file)) return { file, target, errors: ['source file does not exist'], warnings: [] };
  if (!lib.exists(target)) return { file, target, errors: ['no translation'], warnings: [] };
  const raw = require('fs').readFileSync(require('path').join(lib.ROOT, target), 'utf8');
  const { errors, warnings } = compareTexts(lib.readText(file), raw);
  return { file, target, errors, warnings };
}

function translatedSources(locale) {
  const out = [];
  for (const key of lib.localeSources(locale)) {
    for (const file of lib.listSourceFiles(key)) {
      if (lib.exists(lib.targetPath(locale, file))) out.push(file);
    }
  }
  return out;
}

function main() {
  const { flags, files } = lib.parseArgs(process.argv.slice(2));
  if (!flags.locale) throw new Error('--locale <l> is required');
  const locale = flags.locale;
  lib.localeSources(locale);
  const list = files.length > 0 ? files : translatedSources(locale);
  const results = list.map((file) => verifyFile(locale, file));
  const failed = results.filter((r) => r.errors.length > 0);
  if (flags.json) {
    console.log(JSON.stringify(results, null, 2));
  } else {
    for (const r of results) {
      if (r.errors.length === 0 && r.warnings.length === 0 && flags.quiet) continue;
      const status = r.errors.length ? 'FAIL' : r.warnings.length ? 'WARN' : 'ok';
      console.log(`${status.padEnd(4)} ${r.file}`);
      for (const e of r.errors) console.log(`       error: ${e}`);
      for (const w of r.warnings) console.log(`       warning: ${w}`);
    }
    console.log(`${results.length - failed.length}/${results.length} passed`);
  }
  if (failed.length > 0) process.exit(1);
}

module.exports = { compareTexts, verifyFile, translatedSources, splitFrontMatter, splitFences };

if (require.main === module) {
  try {
    main();
  } catch (e) {
    console.error(`[i18n:verify] ${e.message}`);
    process.exit(2);
  }
}
