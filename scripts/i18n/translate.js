#!/usr/bin/env node
// yarn i18n:translate --locale <l> [--all | --staged | source files...]
//                     [--concurrency n] [--dry-run]
//
// Headless translation: one `claude -p "/i18n-translate <file> <locale>"`
// run per page (the skill in .claude/skills/i18n-translate carries the
// rules), using the subscription login Claude Code already has; no API key.
// Default work list: the pages yarn i18n:status reports as new, changed or
// unverified. Each page is verified afterwards and, when it passes, marked
// in the manifest, so an interrupted run resumes where it stopped. Exit 1
// if any page failed.
//
// The interactive alternative is a Claude Code session that works through
// the same backlog with the same skill (and can fan pages out to parallel
// subagents); this script exists for the pre-commit hook and for unattended
// runs.

const { spawn } = require('child_process');
const lib = require('./lib');
const { config } = lib;
const { statusForLocale } = require('./status');
const { verifyFile } = require('./verify');
const { markFiles } = require('./mark');

function workList(locale, flags, files) {
  if (files.length > 0) return files;
  if (flags.all) return lib.localeSources(locale).flatMap((key) => lib.listSourceFiles(key));
  const status = statusForLocale(locale, { staged: Boolean(flags.staged) });
  return [...status.new, ...status.changed, ...status.unverified];
}

function runClaude(locale, file) {
  const { command, skill, timeoutMs } = config.headless;
  const [executable, ...leading] = Array.isArray(command) ? command : [command];
  // The run is bounded by timeoutMs (this CLI version has no turn limit flag).
  const args = [
    ...leading,
    '-p', `/${skill} ${file} ${locale}`,
    '--allowedTools', 'Read', 'Glob', 'Grep', `Edit(./${config.i18nDir}/**)`,
    'Bash(node scripts/i18n/verify.js*)', 'Bash(node scripts/i18n/diff.js*)',
    'Bash(yarn i18n:verify*)', 'Bash(yarn i18n:diff*)',
    '--output-format', 'json',
  ];
  // No shell: the executable is a real binary (wsl.exe on Windows, claude
  // elsewhere), so the arguments are passed through untouched. A tool call
  // the allow list does not cover is denied in -p mode rather than prompted.
  return new Promise((resolve) => {
    const child = spawn(executable, args, { cwd: lib.ROOT, stdio: ['ignore', 'pipe', 'pipe'] });
    let stdout = '';
    let stderr = '';
    child.stdout.on('data', (d) => { stdout += d; });
    child.stderr.on('data', (d) => { stderr += d; });
    const timer = setTimeout(() => child.kill(), timeoutMs);
    child.on('close', (code) => {
      clearTimeout(timer);
      let result = null;
      try { result = JSON.parse(stdout); } catch (e) { /* not JSON: reported below */ }
      resolve({ code, stdout, stderr, result });
    });
    child.on('error', (e) => {
      clearTimeout(timer);
      resolve({ code: -1, stdout, stderr: `${stderr}${e.message}`, result: null });
    });
  });
}

async function translateFiles(locale, files, { concurrency = config.headless.concurrency, dryRun = false, log = console.log } = {}) {
  const outcomes = [];
  if (dryRun) {
    for (const f of files) log(`would translate ${f} -> ${lib.targetPath(locale, f)}`);
    return { done: [], failed: [] };
  }
  const queue = [...files];
  const worker = async () => {
    while (queue.length > 0) {
      const file = queue.shift();
      log(`translating ${file} (${locale})`);
      const run = await runClaude(locale, file);
      const check = verifyFile(locale, file);
      if (run.code !== 0 || check.errors.length > 0) {
        const why = check.errors.length ? check.errors.join('; ') : (run.result && run.result.result) || run.stderr.trim() || `exit ${run.code}`;
        log(`FAIL ${file}: ${why.split('\n')[0]}`);
        outcomes.push({ file, ok: false, why });
        continue;
      }
      const { marked } = markFiles(locale, [file]);
      const cost = run.result && typeof run.result.total_cost_usd === 'number' ? ` (est. $${run.result.total_cost_usd.toFixed(2)})` : '';
      log(`ok   ${file}${marked.length ? ', marked' : ''}${cost}`);
      outcomes.push({ file, ok: true });
    }
  };
  await Promise.all(Array.from({ length: Math.max(1, Math.min(concurrency, files.length)) }, worker));
  return { done: outcomes.filter((o) => o.ok).map((o) => o.file), failed: outcomes.filter((o) => !o.ok) };
}

async function main() {
  const { flags, files } = lib.parseArgs(process.argv.slice(2));
  if (!flags.locale) throw new Error('--locale <l> is required');
  const locale = flags.locale;
  lib.localeSources(locale);
  const list = workList(locale, flags, files);
  if (list.length === 0) {
    console.log(`${locale}: nothing to translate`);
    return;
  }
  const { done, failed } = await translateFiles(locale, list, {
    concurrency: flags.concurrency ? Number(flags.concurrency) : undefined,
    dryRun: Boolean(flags['dry-run']),
  });
  if (!flags['dry-run']) console.log(`${locale}: ${done.length} translated, ${failed.length} failed`);
  if (failed.length > 0) process.exit(1);
}

module.exports = { translateFiles, workList };

if (require.main === module) {
  main().catch((e) => {
    console.error(`[i18n:translate] ${e.message}`);
    process.exit(2);
  });
}
