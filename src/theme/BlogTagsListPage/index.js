// Wraps @theme/BlogTagsListPage (/blog/<section>/tags) to mark it noindex.
// See src/theme/NoIndex.
import React from 'react';
import BlogTagsListPage from '@theme-original/BlogTagsListPage';
import NoIndex from '@theme/NoIndex';

export default function BlogTagsListPageWrapper(props) {
  return (
    <>
      <NoIndex />
      <BlogTagsListPage {...props} />
    </>
  );
}
