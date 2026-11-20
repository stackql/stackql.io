// Wraps @theme/MDXPage (the markdown pages under src/pages: /privacy,
// /terms, /support) for the localized builds: a page with no translated
// source under i18n/<locale>/docusaurus-plugin-content-pages/ is the English
// page served at the localized URL, so it is marked noindex
// (src/theme/NoIndex). In a right-to-left locale its content is laid out
// left to right and marked as English (FallbackDirection). See
// src/lib/i18n.js.
import React from 'react';
import MDXPage from '@theme-original/MDXPage';
import NoIndex from '@theme/NoIndex';
import {isLocalizedSource, useFallbackDirectionContent, useIsLocalizedBuild} from '@site/src/lib/i18n';

export default function MDXPageWrapper(props) {
  const localizedBuild = useIsLocalizedBuild();
  const fallback =
    localizedBuild && !isLocalizedSource(props.content?.metadata?.source);
  const content = useFallbackDirectionContent(props.content, fallback);
  return (
    <>
      {fallback && <NoIndex />}
      <MDXPage {...props} content={content} />
    </>
  );
}
