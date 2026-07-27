import React from 'react';
import Content from '@theme-original/DocItem/Content';
import {useDoc} from '@docusaurus/plugin-content-docs/client';
import QueryPageHeader from '@site/src/components/QueryLibrary/QueryPageHeader';
import RelatedEntries from '@site/src/components/QueryLibrary/RelatedEntries';

// Presentation pass for query library pages: a front-matter-driven metadata
// panel above the document and related-entry links below it. Every other doc
// renders unchanged. The SQL fence keeps the standard theme copy button.
export default function ContentWrapper(props) {
  const {metadata, frontMatter} = useDoc();
  const isQueryPage = metadata?.permalink?.startsWith('/docs/query-library/queries/');
  if (!isQueryPage) {
    return <Content {...props} />;
  }
  return (
    <>
      <QueryPageHeader frontMatter={frontMatter} />
      <Content {...props} />
      <RelatedEntries related={frontMatter.related} />
    </>
  );
}
