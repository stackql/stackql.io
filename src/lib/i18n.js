// Helpers for the localized builds (CLAUDE.md, "Internationalization").
//
// Docusaurus builds one copy of the site per locale. A doc or blog post with
// a translated source under i18n/<locale>/ is rendered from it; any other
// page in a non-default locale is the English source served at the localized
// URL (the plugin falls back to the default locale). `metadata.source` tells
// the two apart: a translated page's source is aliased under @site/i18n/.
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';

export function isLocalizedSource(source) {
  return typeof source === 'string' && source.startsWith('@site/i18n/');
}

// True when rendering a locale other than the default one.
export function useIsLocalizedBuild() {
  const {
    i18n: {currentLocale, defaultLocale},
  } = useDocusaurusContext();
  return currentLocale !== defaultLocale;
}
