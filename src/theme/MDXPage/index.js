// Wraps @theme/MDXPage (the markdown pages under src/pages: /privacy,
// /terms, /support) for the localized builds: a page with no translated
// source under i18n/<locale>/docusaurus-plugin-content-pages/ is the English
// page served at the localized URL, so it is marked noindex
// (src/theme/NoIndex). See src/lib/i18n.js.
import React from 'react';
import MDXPage from '@theme-original/MDXPage';
import NoIndex from '@theme/NoIndex';
import {isLocalizedSource, useIsLocalizedBuild} from '@site/src/lib/i18n';

export default function MDXPageWrapper(props) {
  const localizedBuild = useIsLocalizedBuild();
  const fallback =
    localizedBuild && !isLocalizedSource(props.content?.metadata?.source);
  return (
    <>
      {fallback && <NoIndex />}
      <MDXPage {...props} />
    </>
  );
}
