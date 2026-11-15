#!/usr/bin/env node
// Run by .githooks/pre-commit. Keeps the generated provider redirect block
// in netlify.toml in step with the provider catalog, so a new or renamed
// entry in src/configs/providers.json gets its /providers/<slug> and
// /registry/<name> 301s in the same commit (OVH was added to the catalog
// without them, so /registry/ovh answered 404).
//
// Runs only when the commit touches the catalog, the rules that derive the
// redirects (src/lib/providers.js, scripts/generate-provider-redirects.js)
// or netlify.toml itself (which catches a hand edit inside the block). It
// works on the staged copies, not the working tree: the block is generated
// from the staged providers.json and written into the staged netlify.toml,
// so unrelated unstaged edits to netlify.toml stay unstaged. The working
// tree copy of netlify.toml gets the block generated from the working tree
// catalog, so it does not show the change reverted after the commit.
//
// Never blocks a commit: a catalog that does not validate is reported (the
// site build will fail on it) and nothing is changed.
// PROVIDER_REDIRECTS_PRECOMMIT=off skips it; `git commit --no-verify` skips
// the hook itself. scripts/generate-provider-redirects.js does the same by
// hand on the working tree.

const fs = require('fs');
const path = require('path');
const { execFileSync } = require('child_process');

const ROOT = path.resolve(__dirname, '..');
const CATALOG = 'src/configs/providers.json';
const TOML = 'netlify.toml';
const TRIGGERS = [CATALOG, TOML, 'src/lib/providers.js', 'scripts/generate-provider-redirects.js'];

function git(args, input) {
  return execFileSync('git', args, {
    cwd: ROOT,
    encoding: 'utf8',
    input,
    maxBuffer: 64 * 1024 * 1024,
    stdio: ['pipe', 'pipe', 'pipe'],
  });
}

function main() {
  if (process.env.PROVIDER_REDIRECTS_PRECOMMIT === 'off') return;
  const staged = git(['diff', '--cached', '--name-only', '--diff-filter=ACMR'])
    .split('\n')
    .filter(Boolean);
  if (!staged.some((file) => TRIGGERS.includes(file))) return;

  // Both modules validate the working tree catalog when they load; the
  // staged copy is validated by buildCatalog().
  let buildCatalog, generate, replaceBlock, catalog;
  try {
    ({ buildCatalog } = require('../src/lib/providers'));
    ({ generate, replaceBlock } = require('./generate-provider-redirects'));
    catalog = buildCatalog(JSON.parse(git(['show', `:${CATALOG}`])));
  } catch (e) {
    console.log(`[redirects] ${CATALOG} does not validate, the site build will fail on it:`);
    console.log(`[redirects]   ${e.message.split('\n')[0]}`);
    console.log(`[redirects] ${TOML} left unchanged`);
    return;
  }

  // Index: the staged netlify.toml with the block from the staged catalog.
  const { block, count } = generate(catalog);
  const indexToml = git(['show', `:${TOML}`]);
  const nextIndex = replaceBlock(indexToml, block);
  if (nextIndex !== indexToml) {
    const mode = git(['ls-files', '--stage', '--', TOML]).split(' ')[0] || '100644';
    const sha = git(['hash-object', '-w', '--stdin', '--no-filters'], nextIndex).trim();
    git(['update-index', '--cacheinfo', `${mode},${sha},${TOML}`]);
    console.log(`[redirects] regenerated the provider redirects in ${TOML} (${count} rules) and staged them`);
  }

  // Working tree: the same block from the working tree catalog.
  const file = path.join(ROOT, TOML);
  const worktree = fs.readFileSync(file, 'utf8');
  const nextWorktree = replaceBlock(worktree, generate().block);
  if (nextWorktree !== worktree) fs.writeFileSync(file, nextWorktree);
}

try {
  main();
} catch (e) {
  console.log(`[redirects] pre-commit check skipped: ${e.message.split('\n')[0]}`);
}
