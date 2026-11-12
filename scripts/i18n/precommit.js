#!/usr/bin/env node
// Run by .githooks/pre-commit. Looks at the staged source pages (docs,
// ai-content, blog, pages) and, per configured locale, reports the ones
// whose translation will need creating or updating. In `translate` mode
// (scripts/i18n/config.js `precommit`, or I18N_PRECOMMIT=translate) it runs
// the headless translator on them and stages the translations and the
// manifest into the same commit, printing the tokens each translation used
// (`warn` mode calls no model, so it has no usage to report). Never blocks
// a commit: a translation that fails is reported and left for
// yarn i18n:status to show later. I18N_PRECOMMIT=off skips everything;
// `git commit --no-verify` skips the hook itself.

const lib = require('./lib');
const { config } = lib;
const { statusForLocale, pendingCount, printStatus } = require('./status');

async function main() {
  const mode = process.env.I18N_PRECOMMIT || config.precommit || 'warn';
  if (mode === 'off') return;
  const locales = lib.configuredLocales();
  if (locales.length === 0) return;

  const statuses = locales.map((locale) => statusForLocale(locale, { staged: true }));
  const pending = statuses.filter((s) => pendingCount(s) > 0);
  if (pending.length === 0) return;

  if (mode !== 'translate') {
    for (const s of pending) printStatus(s, { hook: true });
    console.log('[i18n] translations to update; run yarn i18n:status after committing (or set I18N_PRECOMMIT=translate)');
    return;
  }

  // Each page's line carries the tokens its run used; a locale with more
  // than one page gets a subtotal, and the commit as a whole a total.
  const { translateFiles, emptyUsage, addUsage, formatUsage } = require('./translate');
  const total = emptyUsage();
  let runs = 0;
  for (const s of pending) {
    const files = [...s.new, ...s.changed, ...s.unverified];
    if (files.length === 0) continue;
    console.log(`[i18n] translating ${files.length} staged page(s) into ${s.locale}`);
    const { done, failed, usage } = await translateFiles(s.locale, files, { log: (m) => console.log(`[i18n] ${m}`) });
    if (done.length > 0) {
      lib.git(['add', '--', config.manifest, ...done.map((f) => lib.targetPath(s.locale, f))]);
      console.log(`[i18n] staged ${done.length} ${s.locale} translation(s)`);
    }
    for (const f of failed) console.log(`[i18n] not translated: ${f.file} (${f.why.split('\n')[0]})`);
    const used = formatUsage(usage);
    if (used && files.length > 1) console.log(`[i18n] ${s.locale}: ${used}`);
    addUsage(total, usage);
    runs += files.length;
  }
  const used = formatUsage(total);
  if (used && runs > 1) console.log(`[i18n] total for ${runs} translation run(s): ${used}`);
}

main().catch((e) => {
  console.log(`[i18n] pre-commit check skipped: ${e.message}`);
});
