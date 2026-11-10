// Wraps @theme/DocItem/Content for the localized builds.
//
// A doc with no translated source under i18n/<locale>/ is served in English
// at its localized URL (Docusaurus falls back to the default locale). That
// is an English duplicate with the locale's <html lang>, so it gets a robots
// noindex (src/theme/NoIndex) and so stays out of the index and of the
// per-locale sitemap until a translation exists. A translated doc gets a
// short notice that it was translated with AI assistance, linking to the
// English page; `hide_translation_notice: true` in the front matter drops
// the notice.
import React from 'react';
import Content from '@theme-original/DocItem/Content';
import {useDoc} from '@docusaurus/plugin-content-docs/client';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import {useAlternatePageUtils} from '@docusaurus/theme-common/internal';
import Translate from '@docusaurus/Translate';
import Link from '@docusaurus/Link';
import Admonition from '@theme/Admonition';
import NoIndex from '@theme/NoIndex';
import {isLocalizedSource, useIsLocalizedBuild} from '@site/src/lib/i18n';

function TranslationNotice() {
  const alternatePageUtils = useAlternatePageUtils();
  const {
    i18n: {defaultLocale},
  } = useDocusaurusContext();
  const englishUrl = alternatePageUtils.createUrl({
    locale: defaultLocale,
    fullyQualified: false,
  });
  return (
    <Admonition
      type="note"
      title={
        <Translate
          id="i18n.translationNotice.title"
          description="Title of the notice shown at the top of a translated doc">
          Translated page
        </Translate>
      }>
      <p>
        <Translate
          id="i18n.translationNotice.body"
          description="Body of the notice shown at the top of a translated doc; {link} is a link to the English page"
          values={{
            link: (
              <Link to={englishUrl}>
                <Translate
                  id="i18n.translationNotice.link"
                  description="Link text inside the translated-page notice">
                  English version
                </Translate>
              </Link>
            ),
          }}>
          {
            'This page was translated from English with AI assistance. Where the two differ, the {link} is the reference.'
          }
        </Translate>
      </p>
    </Admonition>
  );
}

export default function DocItemContentWrapper(props) {
  const {metadata, frontMatter} = useDoc();
  const localizedBuild = useIsLocalizedBuild();
  const translated = localizedBuild && isLocalizedSource(metadata.source);
  const fallback = localizedBuild && !translated;
  const showNotice = translated && !frontMatter.hide_translation_notice;
  return (
    <>
      {fallback && <NoIndex />}
      {showNotice && <TranslationNotice />}
      <Content {...props} />
    </>
  );
}
