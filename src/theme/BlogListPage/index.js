// Wraps @theme/BlogListPage to mark list pages noindex (src/theme/NoIndex):
// the paginated pages (/blog/<section>/page/N; page 1 is the section's
// landing page and stays indexable), and in a localized build any list
// whose posts are all English fallbacks (no translated source under
// i18n/<locale>/). See src/lib/i18n.js.
import React from 'react';
import BlogListPage from '@theme-original/BlogListPage';
import NoIndex from '@theme/NoIndex';
import {isLocalizedSource, useIsLocalizedBuild} from '@site/src/lib/i18n';

export default function BlogListPageWrapper(props) {
  const localizedBuild = useIsLocalizedBuild();
  const paginated = (props.metadata?.page ?? 1) > 1;
  const items = props.items || [];
  const untranslated =
    localizedBuild &&
    !items.some((item) => isLocalizedSource(item.content?.metadata?.source));
  return (
    <>
      {(paginated || untranslated) && <NoIndex />}
      <BlogListPage {...props} />
    </>
  );
}
