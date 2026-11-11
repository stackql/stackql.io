#!/usr/bin/env node
// yarn seo:check [--target local|prod] [--base <url>] [--locale <l>[,<l>]]
//                [--only <section>[,<section>]] [--sample <n>] [--all]
//                [--verbose] [--json] [--concurrency <n>] [--timeout <ms>]
//
// Checks that the site publishes what search engines and answer engines
// need, in every locale: robots.txt, sitemaps, llms.txt and the markdown
// companions, the head tags and structured data of representative pages,
// and (on the hosting edge) redirects, headers and crawler access.
//
//   --target local   a build served by `yarn serve` (http://localhost:3000),
//                    the default. Checks what the build contains.
//   --target prod    https://stackql.io. Adds the checks that only the edge
//                    can answer: redirect rules, response headers, 404 pages
//                    and whether each crawler gets the same response.
//   --base <url>     another server with the files of a build (a different
//                    port, a deploy preview); add --edge when it is Netlify.
//
// Sections: robots, sitemaps, llms, pages, assets, switcher, crawlers, edge.
// The switcher section drives headless Chrome (the links are computed in the
// browser); it is skipped when no Chrome is found.
// Exit code 1 when a check fails, 2 when the target cannot be reached.
// Expectations are in ./config.js.

const cfg = require('./config');
const lib = require('./lib');
const checks = require('./checks');

const SECTIONS = ['robots', 'sitemaps', 'llms', 'pages', 'assets', 'switcher', 'crawlers', 'edge'];

function parseArgs(argv) {
  const flags = {};
  const booleans = new Set(['all', 'verbose', 'json', 'edge', 'help']);
  for (let i = 0; i < argv.length; i++) {
    const m = argv[i].match(/^--([a-z-]+)(?:=(.*))?$/);
    if (!m) throw new Error(`unexpected argument: ${argv[i]}`);
    if (booleans.has(m[1])) flags[m[1]] = true;
    else flags[m[1]] = m[2] !== undefined ? m[2] : argv[++i];
  }
  return flags;
}

function usage() {
  console.log([
    'usage: yarn seo:check [--target local|prod] [--base <url> [--edge]] [--locale <l>[,<l>]]',
    '                      [--only <section>[,<section>]] [--sample <n>] [--all] [--verbose] [--json]',
    `targets:  ${Object.entries(cfg.targets).map(([name, t]) => `${name} (${t.base})`).join(', ')}`,
    `locales:  ${Object.keys(cfg.locales).join(', ')}`,
    `sections: ${SECTIONS.join(', ')}`,
  ].join('\n'));
}

async function main() {
  const flags = parseArgs(process.argv.slice(2));
  if (flags.help) return usage();

  const targetName = flags.target || 'local';
  if (!cfg.targets[targetName]) throw new Error(`unknown target "${targetName}" (${Object.keys(cfg.targets).join(', ')})`);
  const target = { name: flags.base ? flags.base : targetName, ...cfg.targets[targetName] };
  if (flags.base) {
    target.base = flags.base.replace(/\/+$/, '');
    target.edge = Boolean(flags.edge);
  }
  const locales = flags.locale ? flags.locale.split(',') : Object.keys(cfg.locales);
  for (const l of locales) if (!cfg.locales[l]) throw new Error(`unknown locale "${l}" (${Object.keys(cfg.locales).join(', ')})`);
  const only = flags.only ? flags.only.split(',') : SECTIONS;
  for (const s of only) if (!SECTIONS.includes(s)) throw new Error(`unknown section "${s}" (${SECTIONS.join(', ')})`);
  const opts = { sample: flags.sample ? Number(flags.sample) : 20, all: Boolean(flags.all) };

  const client = lib.createClient({
    base: target.base,
    siteUrl: cfg.siteUrl,
    timeoutMs: flags.timeout ? Number(flags.timeout) : 20000,
    concurrency: flags.concurrency ? Number(flags.concurrency) : 8,
  });
  const report = lib.createReport();
  const say = flags.json ? () => {} : console.log;

  say(`SEO / AEO check: ${target.base} (${target.edge ? 'hosting edge' : 'static files only'}), locales ${locales.join(', ')}`);
  try {
    await client.get('/robots.txt');
  } catch (e) {
    console.error(`cannot reach ${target.base}: ${e.message}`);
    if (targetName === 'local' && !flags.base) console.error('build the site and serve it first: yarn build && yarn serve');
    process.exit(2);
  }

  const started = Date.now();
  for (const section of SECTIONS.filter((s) => only.includes(s))) {
    report.setSection(section);
    try {
      await checks[section]({ cfg, client, report, target, locales, opts });
    } catch (e) {
      report.fail(`the ${section} checks stopped: ${e.message}`);
    }
    const mine = report.results.filter((x) => x.section === section);
    const n = (level) => mine.filter((x) => x.level === level).length;
    const tally = [`${n('pass')} passed`, n('fail') && `${n('fail')} FAILED`, n('warn') && `${n('warn')} warnings`, n('skip') && `${n('skip')} skipped`].filter(Boolean).join(', ');
    say(`\n${section}: ${tally}`);
    for (const x of mine) {
      if (x.level === 'pass' && !flags.verbose) continue;
      const label = { pass: 'ok  ', fail: 'FAIL', warn: 'warn', info: 'info', skip: 'skip' }[x.level];
      say(`  ${label} ${x.message}`);
    }
  }

  const total = (level) => report.count(level);
  if (flags.json) {
    console.log(JSON.stringify({ target: target.base, edge: target.edge, locales, results: report.results }, null, 2));
  } else {
    say(`\n${total('pass')} passed, ${total('fail')} failed, ${total('warn')} warnings, ${total('skip')} skipped (${client.requestCount()} requests, ${((Date.now() - started) / 1000).toFixed(0)} s)`);
    if (!flags.verbose && total('pass')) say('run with --verbose to list the checks that passed');
  }
  if (total('fail') > 0) process.exit(1);
}

main().catch((e) => {
  console.error(`[seo:check] ${e.message}`);
  usage();
  process.exit(2);
});
