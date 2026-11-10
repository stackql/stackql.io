// Wraps @theme/BlogPostPage for the localized builds: a post with no
// translated source under i18n/<locale>/ is the English post served at the
// localized URL, so it is marked noindex (src/theme/NoIndex) rather than
// indexed as a duplicate. See src/lib/i18n.js.
import React from 'react';
import BlogPostPage from '@theme-original/BlogPostPage';
import NoIndex from '@theme/NoIndex';
import {isLocalizedSource, useIsLocalizedBuild} from '@site/src/lib/i18n';

export default function BlogPostPageWrapper(props) {
  const localizedBuild = useIsLocalizedBuild();
  const fallback =
    localizedBuild && !isLocalizedSource(props.content?.metadata?.source);
  return (
    <>
      {fallback && <NoIndex />}
      <BlogPostPage {...props} />
    </>
  );
}
