/**
 * Site override of @theme/DocItem/Footer (theme-classic 3.10).
 *
 * Identical to the original except that a doc can opt out of the
 * "Last updated" row with `hide_last_update: true` in its front matter,
 * keeping the tags row and the "Edit this page" link. Used by the homepage
 * (docs/index.md), which is a landing page rather than an article and should
 * not surface a modification date in search results. The global
 * `showLastUpdateTime` setting stays on for every other doc.
 *
 * EditMetaRow itself is shared with the blog and MDX pages, so the opt-out
 * cannot live there (useDoc() is only available inside a doc).
 */
import React from 'react';
import clsx from 'clsx';
import {ThemeClassNames} from '@docusaurus/theme-common';
import {useDoc} from '@docusaurus/plugin-content-docs/client';
import TagsListInline from '@theme/TagsListInline';
import EditMetaRow from '@theme/EditMetaRow';

export default function DocItemFooter() {
  const {metadata, frontMatter} = useDoc();
  const {editUrl, tags} = metadata;
  const hideLastUpdate = frontMatter.hide_last_update === true;
  const lastUpdatedAt = hideLastUpdate ? undefined : metadata.lastUpdatedAt;
  const lastUpdatedBy = hideLastUpdate ? undefined : metadata.lastUpdatedBy;
  const canDisplayTagsRow = tags.length > 0;
  const canDisplayEditMetaRow = !!(editUrl || lastUpdatedAt || lastUpdatedBy);
  const canDisplayFooter = canDisplayTagsRow || canDisplayEditMetaRow;
  if (!canDisplayFooter) {
    return null;
  }
  return (
    <footer className={clsx(ThemeClassNames.docs.docFooter, 'docusaurus-mt-lg')}>
      {canDisplayTagsRow && (
        <div className={clsx('row margin-top--sm', ThemeClassNames.docs.docFooterTagsRow)}>
          <div className="col">
            <TagsListInline tags={tags} />
          </div>
        </div>
      )}
      {canDisplayEditMetaRow && (
        <EditMetaRow
          className={clsx('margin-top--sm', ThemeClassNames.docs.docFooterEditMetaRow)}
          editUrl={editUrl}
          lastUpdatedAt={lastUpdatedAt}
          lastUpdatedBy={lastUpdatedBy}
        />
      )}
    </footer>
  );
}
