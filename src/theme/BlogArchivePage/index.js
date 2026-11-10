// Wraps @theme/BlogArchivePage (/blog/<section>/archive): a list page, kept
// out of the sitemap already; in a localized build it lists the English
// posts, so it is marked noindex there (src/theme/NoIndex).
import React from 'react';
import BlogArchivePage from '@theme-original/BlogArchivePage';
import NoIndex from '@theme/NoIndex';
import {useIsLocalizedBuild} from '@site/src/lib/i18n';

export default function BlogArchivePageWrapper(props) {
  const localizedBuild = useIsLocalizedBuild();
  return (
    <>
      {localizedBuild && <NoIndex />}
      <BlogArchivePage {...props} />
    </>
  );
}
