// <NoIndex /> adds a robots noindex to the current page's head. The site
// sets `robots: index,follow` globally (themeConfig.metadata in
// docusaurus.config.js); a page-level <Head> meta with the same name
// replaces it, so the built page carries exactly one robots tag.
//
// Used by the blog wrappers in this directory (tag pages and paginated
// list pages): thin list pages that are linked from every post, so Google
// crawls them, but that are not worth indexing. They were already left out
// of sitemap.xml (ignorePatterns); this tells Google the same thing when it
// arrives via a link, so they stop accumulating under "Crawled - currently
// not indexed" in Search Console. `follow` keeps the links to the posts
// themselves crawlable.
import React from 'react';
import Head from '@docusaurus/Head';

export default function NoIndex() {
  return (
    <Head>
      <meta name="robots" content="noindex, follow" />
    </Head>
  );
}
