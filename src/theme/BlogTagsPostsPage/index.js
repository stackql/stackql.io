// Wraps @theme/BlogTagsPostsPage (/blog/<section>/tags/<tag> and its
// /page/N pages) to mark it noindex. See src/theme/NoIndex.
import React from 'react';
import BlogTagsPostsPage from '@theme-original/BlogTagsPostsPage';
import NoIndex from '@theme/NoIndex';

export default function BlogTagsPostsPageWrapper(props) {
  return (
    <>
      <NoIndex />
      <BlogTagsPostsPage {...props} />
    </>
  );
}
