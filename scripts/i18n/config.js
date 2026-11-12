// Configuration of the i18n tooling (scripts/i18n): which locales are
// translated, from which source sets, where the string files live, and how
// the pre-commit hook and the headless translator behave. Config, not code.
// The site's locale list itself (URLs, labels) is i18nConfig in
// docusaurus.config.js; a locale must be in both to be built and translated.

module.exports = {
  defaultLocale: 'en',
  i18nDir: 'i18n',
  manifest: 'i18n/manifest.json',
  glossary: 'i18n/glossary.md',

  // Source sets: a site directory and where its translations live under
  // i18n/<locale>/ (the Docusaurus layout: <plugin name>[-<instance id>]).
  // Only files with one of `extensions` are translated; everything else in
  // a source directory (images, _category_.yml) is shared by every locale.
  sources: {
    docs: {
      dir: 'docs',
      target: 'docusaurus-plugin-content-docs/current',
    },
    'ai-content': {
      dir: 'ai-content',
      target: 'docusaurus-plugin-content-docs-ai/current',
    },
    'blog-product': {
      dir: 'blog/product',
      target: 'docusaurus-plugin-content-blog-product',
    },
    'blog-providers': {
      dir: 'blog/providers',
      target: 'docusaurus-plugin-content-blog-providers',
    },
    'blog-tutorials': {
      dir: 'blog/tutorials',
      target: 'docusaurus-plugin-content-blog-tutorials',
    },
    pages: {
      dir: 'src/pages',
      target: 'docusaurus-plugin-content-pages',
    },
  },
  extensions: ['.md', '.mdx'],

  // Per locale, the source sets that are translated. A source set not
  // listed for a locale is served in English at the localized URL and
  // marked noindex by the theme wrappers (src/theme/DocItem/Content,
  // src/theme/BlogPostPage, src/theme/BlogListPage). The AI Reference
  // (ai-content) is meant for answer engines, whose crawlers read English
  // and Chinese, so it is translated for zh-Hans only.
  // `sameAsEnglish` lists string keys whose English message is also the
  // correct rendering in that locale (product and vendor names), so
  // yarn i18n:strings does not report them as untranslated.
  locales: {
    de: {
      sources: ['docs'],
      sameAsEnglish: [
        'item.label.Blog',
        'item.label.Tutorials',
        'link.title.StackQL',
        'link.item.label.Blog',
        'link.item.label.Support',
        'sidebar.docsSidebar.category.MCP',
        'sidebar.docsSidebar.category.Hash',
        'sidebar.docsSidebar.category.AWS',
        'sidebar.docsSidebar.category.Microsoft Azure',
        'sidebar.docsSidebar.category.Google Cloud Platform',
        'sidebar.docsSidebar.category.GitHub',
      ],
    },
    fr: {
      sources: ['docs'],
      sameAsEnglish: [
        'item.label.Providers',
        'item.label.Blog',
        'link.title.StackQL',
        'link.item.label.Documentation',
        'link.item.label.Providers',
        'link.item.label.Blog',
        'link.item.label.Support',
        'sidebar.docsSidebar.category.MCP',
        'sidebar.docsSidebar.category.Session',
        'sidebar.docsSidebar.category.AWS',
        'sidebar.docsSidebar.category.Microsoft Azure',
        'sidebar.docsSidebar.category.Google Cloud Platform',
        'sidebar.docsSidebar.category.GitHub',
      ],
    },
    'zh-Hans': {
      sources: ['docs', 'ai-content'],
      sameAsEnglish: [
        'link.title.StackQL',
        'sidebar.docsSidebar.category.MCP',
        'sidebar.docsSidebar.category.AWS',
        'sidebar.docsSidebar.category.Microsoft Azure',
        'sidebar.docsSidebar.category.Google Cloud Platform',
        'sidebar.docsSidebar.category.GitHub',
      ],
    },
  },

  // String files written by `docusaurus write-translations --locale <l>`
  // (yarn i18n:strings), relative to i18n/<locale>/. Their `message` values
  // are translated in place; a key the command adds arrives in English.
  stringFiles: [
    'code.json',
    'docusaurus-theme-classic/navbar.json',
    'docusaurus-theme-classic/footer.json',
    'docusaurus-plugin-content-docs/current.json',
    'docusaurus-plugin-content-docs-ai/current.json',
    'docusaurus-plugin-content-blog-product/options.json',
    'docusaurus-plugin-content-blog-providers/options.json',
    'docusaurus-plugin-content-blog-tutorials/options.json',
  ],

  // Keys that `write-translations` generates but that must keep the site's
  // default (English, computed) value: a missing key falls back to it, a
  // present one freezes it (the footer copyright would keep the year of the
  // extraction). yarn i18n:strings removes them after every run.
  stringKeysUseDefault: {
    'docusaurus-theme-classic/footer.json': ['copyright', 'logo.alt'],
    'docusaurus-theme-classic/navbar.json': ['logo.alt'],
    'docusaurus-plugin-content-docs/current.json': ['version.label'],
    'docusaurus-plugin-content-docs-ai/current.json': ['version.label'],
  },

  // Directories under src/ that `docusaurus write-translations` must not
  // read. It globs every .js/.jsx under src/ with no node_modules exclusion,
  // and src/modules/react-animated-term ships its own node_modules (36k
  // files, some of which do not parse). They are moved aside for the
  // duration of the extraction and put back.
  extractorExcludes: ['src/modules/react-animated-term/node_modules'],

  // Pre-commit hook (.githooks/pre-commit -> scripts/i18n/precommit.js):
  //   warn       list the staged source pages whose translations will need
  //              updating, then let the commit through
  //   translate  run the headless translator on them, verify, and stage
  //              the translations and the manifest into the same commit;
  //              a failure is reported and the commit still goes through
  //   off        do nothing
  // The I18N_PRECOMMIT environment variable overrides this per commit.
  precommit: 'warn',

  // Headless translation (yarn i18n:translate, and the hook in `translate`
  // mode): one `claude -p "/<skill> <file> <locale>"` run per page, with the
  // subscription login Claude Code already has. Tool access is limited to
  // reading the repo, editing under i18n/, and the verify and diff scripts.
  // `command` is the executable and any leading arguments: on this Windows
  // machine Claude Code is installed in WSL, so the Windows side runs it
  // through `wsl -e` (exec without the WSL login shell, so the tool rules
  // with parentheses and globs arrive untouched; wsl.exe starts in the
  // /mnt/c/... equivalent of the repo directory, so the skill and the
  // scripts resolve as usual); from a WSL or POSIX shell the CLI is called
  // directly.
  // `subscriptionOnly`: the runs must draw on the Claude subscription that
  // CLI is logged in with (`claude auth login`), never on API credits. The
  // driver starts the CLI without ANTHROPIC_API_KEY and the other variables
  // that override the stored login, and translates nothing unless
  // `claude auth status` reports a claude.ai login with no API key in play.
  // false allows whatever credential the CLI resolves.
  // `settings`: a Claude Code settings file passed to every run (path from
  // the repo root). It turns off `includeGitInstructions`, the git status
  // snapshot the CLI takes at startup for its system prompt. A translation
  // does not need it, and from WSL it is the slow part: WSL git reads the
  // 37,000 tracked files of this Windows checkout through /mnt/c and had
  // not finished after five minutes, before the first model call.
  headless: {
    command: process.platform === 'win32' ? ['wsl', '-e', 'claude'] : ['claude'],
    subscriptionOnly: true,
    settings: 'scripts/i18n/headless-settings.json',
    skill: 'i18n-translate',
    concurrency: 2,
    timeoutMs: 15 * 60 * 1000,
  },
};
