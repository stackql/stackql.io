// Helpers for the localized builds (CLAUDE.md, "Internationalization").
//
// Docusaurus builds one copy of the site per locale. A doc or blog post with
// a translated source under i18n/<locale>/ is rendered from it; any other
// page in a non-default locale is the English source served at the localized
// URL (the plugin falls back to the default locale). `metadata.source` tells
// the two apart: a translated page's source is aliased under @site/i18n/.
import React, {useMemo} from 'react';
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

// True when rendering a right-to-left locale (Arabic).
export function useIsRtlBuild() {
  const {
    i18n: {currentLocale, localeConfigs},
  } = useDocusaurusContext();
  return localeConfigs[currentLocale]?.direction === 'rtl';
}

// An untranslated page in a right-to-left locale is English text inside a
// right-to-left layout: without this its punctuation lands on the wrong end
// of each line (".Hello world") and the text is right aligned. The content
// (not the navbar, sidebar or footer, which belong to the locale) is laid
// out left to right and marked as English. The list and blockquote rules
// for .fallback-ltr are in src/css/global.css. A no-op in the
// left-to-right locales.
export function FallbackDirection({children}) {
  const rtl = useIsRtlBuild();
  const {
    i18n: {defaultLocale, localeConfigs},
  } = useDocusaurusContext();
  if (!rtl) return children;
  return (
    <div dir="ltr" lang={localeConfigs[defaultLocale].htmlLang} className="fallback-ltr">
      {children}
    </div>
  );
}

// For a page component that receives its MDX content as a component (the
// pages under src/pages): the same component wrapped in FallbackDirection,
// keeping the metadata, frontMatter and toc the theme reads from it.
export function useFallbackDirectionContent(Content, fallback) {
  const rtl = useIsRtlBuild();
  return useMemo(() => {
    if (!fallback || !rtl || !Content) return Content;
    function FallbackContent(props) {
      return (
        <FallbackDirection>
          <Content {...props} />
        </FallbackDirection>
      );
    }
    return Object.assign(FallbackContent, Content);
  }, [Content, fallback, rtl]);
}
