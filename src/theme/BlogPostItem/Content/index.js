// Wraps @theme/BlogPostItem/Content (the body of a post, on its own page and
// in the list pages) for the right-to-left locales: a post with no
// translated source under i18n/<locale>/ is English text, so its body is laid
// out left to right and marked as English inside the right-to-left layout
// (FallbackDirection in src/lib/i18n.js). The title gets the same treatment
// (../Header/Title); the date, reading time and author line stay in the
// page's direction because the date and reading time are in the locale's
// language. Nothing changes in the left-to-right locales. The noindex for
// untranslated posts is set by the page wrappers (src/theme/BlogPostPage,
// src/theme/BlogListPage).
import React from 'react';
import Content from '@theme-original/BlogPostItem/Content';
import {useBlogPost} from '@docusaurus/plugin-content-blog/client';
import {FallbackDirection, isLocalizedSource, useIsLocalizedBuild} from '@site/src/lib/i18n';

export default function BlogPostItemContentWrapper(props) {
  const {metadata} = useBlogPost();
  const localizedBuild = useIsLocalizedBuild();
  if (!localizedBuild || isLocalizedSource(metadata?.source)) return <Content {...props} />;
  return (
    <FallbackDirection>
      <Content {...props} />
    </FallbackDirection>
  );
}
