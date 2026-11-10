#!/usr/bin/env node
// postinstall: point git at the tracked hooks in .githooks (pre-commit runs
// scripts/i18n/precommit.js) and make them executable on POSIX, where the
// index may not carry the mode. Never fails an install: no git checkout
// (Netlify, a tarball) means nothing to do.

const fs = require('fs');
const path = require('path');
const { execFileSync } = require('child_process');

const root = path.resolve(__dirname, '..', '..');
const hooksDir = path.join(root, '.githooks');

try {
  if (!fs.existsSync(path.join(root, '.git')) || !fs.existsSync(hooksDir)) process.exit(0);
  execFileSync('git', ['config', 'core.hooksPath', '.githooks'], { cwd: root, stdio: 'ignore' });
  if (process.platform !== 'win32') {
    for (const name of fs.readdirSync(hooksDir)) fs.chmodSync(path.join(hooksDir, name), 0o755);
  }
} catch (e) {
  // not fatal
}
