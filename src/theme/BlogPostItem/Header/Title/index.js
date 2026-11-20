// Wraps @theme/BlogPostItem/Header/Title: the English title of an
// untranslated post is laid out left to right in a right-to-left locale.
// See ../../Content for the body and the reasoning.
import React from 'react';
import Title from '@theme-original/BlogPostItem/Header/Title';
import {useBlogPost} from '@docusaurus/plugin-content-blog/client';
import {FallbackDirection, isLocalizedSource, useIsLocalizedBuild} from '@site/src/lib/i18n';

export default function BlogPostItemHeaderTitleWrapper(props) {
  const {metadata} = useBlogPost();
  const localizedBuild = useIsLocalizedBuild();
  if (!localizedBuild || isLocalizedSource(metadata?.source)) return <Title {...props} />;
  return (
    <FallbackDirection>
      <Title {...props} />
    </FallbackDirection>
  );
}
