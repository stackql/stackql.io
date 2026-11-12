#!/usr/bin/env node
// yarn i18n:translate --locale <l> [--all | --staged | source files...]
//                     [--concurrency n] [--dry-run]
//
// Headless translation: one `claude -p "/i18n-translate <file> <locale>"`
// run per page (the skill in .claude/skills/i18n-translate carries the
// rules), using the subscription login Claude Code already has; no API key,
// and none is accepted (see "Subscription only" below).
// Default work list: the pages yarn i18n:status reports as new, changed or
// unverified. Each page is verified afterwards and, when it passes, marked
// in the manifest, so an interrupted run resumes where it stopped. Every
// page's line ends with the tokens its run used (and the CLI's cost
// estimate), and the summary line carries the total. Exit 1 if any page
// failed.
//
// The interactive alternative is a Claude Code session that works through
// the same backlog with the same skill (and can fan pages out to parallel
// subagents); this script exists for the pre-commit hook and for unattended
// runs.

const { spawn, spawnSync } = require('child_process');
const lib = require('./lib');
const { config } = lib;
const { statusForLocale } = require('./status');
const { verifyFile } = require('./verify');
const { markFiles } = require('./mark');

// Subscription only (config.headless.subscriptionOnly, on by default): the
// runs draw on the Claude subscription the CLI is logged in with
// (`claude auth login`, which is `--claudeai` by default), never on
// pay-as-you-go API credits. Two things enforce it. The CLI is started
// without the variables that override its stored login: in -p mode an API
// key in the environment is used without asking. And before the first run
// the driver asks the CLI which login it would use (`claude auth status`,
// JSON, no model call) and translates nothing unless the answer is a
// claude.ai login on Anthropic's own API with no API key in play.
// The list is every credential source the CLI ranks above the stored login
// (code.claude.com/docs/en/authentication, "Authentication precedence")
// except CLAUDE_CODE_OAUTH_TOKEN: that is the one-year token from
// `claude setup-token`, which is bound to the subscription as well.
const AUTH_OVERRIDES = [
  'ANTHROPIC_API_KEY',
  'ANTHROPIC_AUTH_TOKEN',
  'ANTHROPIC_PROFILE',
  'ANTHROPIC_FEDERATION_RULE_ID',
  'CLAUDE_CODE_USE_BEDROCK',
  'CLAUDE_CODE_USE_VERTEX',
  'CLAUDE_CODE_USE_FOUNDRY',
];

function subscriptionOnly() {
  return config.headless.subscriptionOnly !== false;
}

function commandParts() {
  const { command } = config.headless;
  return Array.isArray(command) ? command : [command];
}

// The environment the CLI is started with. Names are matched without
// regard to case because Windows variable names are case-insensitive. On
// Windows the CLI runs inside WSL, which only sees the Windows variables
// listed in WSLENV; removing them here covers that route too.
function childEnv() {
  if (!subscriptionOnly()) return process.env;
  const env = { ...process.env };
  for (const name of Object.keys(env)) {
    if (AUTH_OVERRIDES.includes(name.toUpperCase())) delete env[name];
  }
  return env;
}

// null when a run would use a Claude subscription login, otherwise why not.
// `status` is the JSON of `claude auth status`: a subscription login reports
// authMethod "claude.ai" and apiProvider "firstParty"; `apiKeySource` is
// only present when an API key (environment, settings or key helper) would
// be used instead.
function subscriptionProblem(status) {
  const cli = commandParts().join(' ');
  if (!status) return `\`${cli} auth status\` gave no answer (is the CLI installed?)`;
  if (!status.loggedIn) return `the claude CLI is not logged in; run \`${cli} auth login\` with the subscription account`;
  if (status.apiProvider && status.apiProvider !== 'firstParty') return `the claude CLI is set to use ${status.apiProvider}, not the Claude subscription`;
  if (status.apiKeySource) return `an API key (${status.apiKeySource}) is configured and would be used instead of the Claude subscription`;
  if (status.authMethod !== 'claude.ai' && !tokenLogin(status)) return `the claude CLI login is "${status.authMethod}", not a Claude subscription; run \`${cli} auth login\``;
  return null;
}

// A `claude setup-token` token in CLAUDE_CODE_OAUTH_TOKEN reports
// "oauth_token". So does ANTHROPIC_AUTH_TOKEN, but that variable is not
// passed to the CLI, which leaves the subscription token as the only source.
function tokenLogin(status) {
  return status.authMethod === 'oauth_token' && Boolean(process.env.CLAUDE_CODE_OAUTH_TOKEN);
}

let loginCheck = null;

// Asked once per process; the answer does not change between pages.
function checkLogin() {
  if (loginCheck) return loginCheck;
  const [executable, ...leading] = commandParts();
  const run = spawnSync(executable, [...leading, 'auth', 'status'], {
    cwd: lib.ROOT, env: childEnv(), encoding: 'utf8', timeout: 60 * 1000,
  });
  let status = null;
  try { status = JSON.parse(run.stdout); } catch (e) { /* no JSON: reported as a problem */ }
  loginCheck = { status, problem: subscriptionProblem(status), announced: false };
  return loginCheck;
}

function workList(locale, flags, files) {
  if (files.length > 0) return files;
  if (flags.all) return lib.localeSources(locale).flatMap((key) => lib.listSourceFiles(key));
  const status = statusForLocale(locale, { staged: Boolean(flags.staged) });
  return [...status.new, ...status.changed, ...status.unverified];
}

function runClaude(locale, file) {
  const { skill, timeoutMs, settings } = config.headless;
  const [executable, ...leading] = commandParts();
  // The run is bounded by timeoutMs (this CLI version has no turn limit flag).
  // --settings goes before --allowedTools, which takes every value after it.
  const args = [
    ...leading,
    '-p', `/${skill} ${file} ${locale}`,
    ...(settings ? ['--settings', settings] : []),
    '--allowedTools', 'Read', 'Glob', 'Grep', `Edit(./${config.i18nDir}/**)`,
    'Bash(node scripts/i18n/verify.js*)', 'Bash(node scripts/i18n/diff.js*)',
    'Bash(yarn i18n:verify*)', 'Bash(yarn i18n:diff*)',
    '--output-format', 'json',
  ];
  // No shell: the executable is a real binary (wsl.exe on Windows, claude
  // elsewhere), so the arguments are passed through untouched. A tool call
  // the allow list does not cover is denied in -p mode rather than prompted.
  return new Promise((resolve) => {
    const child = spawn(executable, args, { cwd: lib.ROOT, env: childEnv(), stdio: ['ignore', 'pipe', 'pipe'] });
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

// Token usage of a headless run, read from the CLI's JSON result. A run
// calls more than one model (the main one plus a small one for side tasks),
// so the per-model `modelUsage` map is summed when present; the top-level
// `usage` block covers the main model only and is the fallback. `cost` is
// `total_cost_usd`, which the CLI computes client-side from API list
// prices. It is not a bill: on a subscription login the usage counts
// against the plan's limits and the figure only sizes the run.
function emptyUsage() {
  return { input: 0, output: 0, cacheRead: 0, cacheWrite: 0, cost: 0 };
}

function usageOf(result) {
  const usage = emptyUsage();
  if (!result || typeof result !== 'object') return usage;
  for (const m of Object.values(result.modelUsage || {})) {
    usage.input += m.inputTokens || 0;
    usage.output += m.outputTokens || 0;
    usage.cacheRead += m.cacheReadInputTokens || 0;
    usage.cacheWrite += m.cacheCreationInputTokens || 0;
  }
  // No per-model figures (an older CLI, or a map this code cannot read).
  if (usage.input + usage.output + usage.cacheRead + usage.cacheWrite === 0 && result.usage) {
    usage.input = result.usage.input_tokens || 0;
    usage.output = result.usage.output_tokens || 0;
    usage.cacheRead = result.usage.cache_read_input_tokens || 0;
    usage.cacheWrite = result.usage.cache_creation_input_tokens || 0;
  }
  if (typeof result.total_cost_usd === 'number') usage.cost = result.total_cost_usd;
  return usage;
}

function addUsage(total, usage) {
  for (const key of Object.keys(total)) total[key] += usage[key] || 0;
  return total;
}

// "48,512 tokens (1,234 in, 3,456 out, 35,100 cache read, 8,722 cache
// write), ~$0.21 at API prices", or '' when the run reported no usage (it
// never reached the API, or it was killed before printing its result).
function formatUsage(usage) {
  const total = usage.input + usage.output + usage.cacheRead + usage.cacheWrite;
  if (total === 0) return '';
  const n = (value) => value.toLocaleString('en-US');
  const parts = [`${n(usage.input)} in`, `${n(usage.output)} out`];
  if (usage.cacheRead) parts.push(`${n(usage.cacheRead)} cache read`);
  if (usage.cacheWrite) parts.push(`${n(usage.cacheWrite)} cache write`);
  const cost = usage.cost > 0 ? `, ~$${usage.cost.toFixed(2)} at API prices` : '';
  return `${n(total)} tokens (${parts.join(', ')})${cost}`;
}

async function translateFiles(locale, files, { concurrency = config.headless.concurrency, dryRun = false, log = console.log } = {}) {
  const outcomes = [];
  const total = emptyUsage();
  if (dryRun) {
    for (const f of files) log(`would translate ${f} -> ${lib.targetPath(locale, f)}`);
    return { done: [], failed: [], usage: total };
  }
  if (subscriptionOnly()) {
    const login = checkLogin();
    if (login.problem) {
      if (!login.announced) log(`not translating: ${login.problem}`);
      login.announced = true;
      return { done: [], failed: files.map((file) => ({ file, ok: false, why: login.problem, usage: emptyUsage() })), usage: total };
    }
    const plan = tokenLogin(login.status) ? 'subscription token (CLAUDE_CODE_OAUTH_TOKEN)' : `${login.status.subscriptionType || 'claude.ai'} subscription`;
    if (!login.announced) log(`claude login: ${plan}, no API key`);
    login.announced = true;
  }
  const queue = [...files];
  const worker = async () => {
    while (queue.length > 0) {
      const file = queue.shift();
      log(`translating ${file} (${locale})`);
      const run = await runClaude(locale, file);
      // A failed run has used tokens too, so it counts towards the total.
      const usage = usageOf(run.result);
      addUsage(total, usage);
      const used = formatUsage(usage);
      const check = verifyFile(locale, file);
      if (run.code !== 0 || check.errors.length > 0) {
        const why = check.errors.length ? check.errors.join('; ') : (run.result && run.result.result) || run.stderr.trim() || `exit ${run.code}`;
        log(`FAIL ${file}: ${why.split('\n')[0]}${used ? ` - ${used}` : ''}`);
        outcomes.push({ file, ok: false, why, usage });
        continue;
      }
      const { marked } = markFiles(locale, [file]);
      log(`ok   ${file}${marked.length ? ', marked' : ''}${used ? ` - ${used}` : ''}`);
      outcomes.push({ file, ok: true, usage });
    }
  };
  await Promise.all(Array.from({ length: Math.max(1, Math.min(concurrency, files.length)) }, worker));
  return {
    done: outcomes.filter((o) => o.ok).map((o) => o.file),
    failed: outcomes.filter((o) => !o.ok),
    usage: total,
  };
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
  const { done, failed, usage } = await translateFiles(locale, list, {
    concurrency: flags.concurrency ? Number(flags.concurrency) : undefined,
    dryRun: Boolean(flags['dry-run']),
  });
  if (!flags['dry-run']) {
    const used = formatUsage(usage);
    console.log(`${locale}: ${done.length} translated, ${failed.length} failed${used ? ` - ${used}` : ''}`);
  }
  if (failed.length > 0) process.exit(1);
}

module.exports = { translateFiles, workList, emptyUsage, usageOf, addUsage, formatUsage, checkLogin, subscriptionProblem };

if (require.main === module) {
  main().catch((e) => {
    console.error(`[i18n:translate] ${e.message}`);
    process.exit(2);
  });
}
