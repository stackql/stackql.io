// Wraps @theme/BlogListPage to mark the paginated pages
// (/blog/<section>/page/N) noindex. Page 1 is the section's landing page
// and stays indexable. See src/theme/NoIndex.
import React from 'react';
import BlogListPage from '@theme-original/BlogListPage';
import NoIndex from '@theme/NoIndex';

export default function BlogListPageWrapper(props) {
  const paginated = (props.metadata?.page ?? 1) > 1;
  return (
    <>
      {paginated && <NoIndex />}
      <BlogListPage {...props} />
    </>
  );
}
