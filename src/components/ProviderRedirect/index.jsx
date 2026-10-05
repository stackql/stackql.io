import React from 'react';
import Head from '@docusaurus/Head';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';

// Rendered by plugins/provider-redirects at /providers/<slug>,
// /registry/<slug> and the two bare landings. A Docusaurus head redirect,
// the same mechanism the hand-written src/pages stubs used: the meta
// refresh fires with or without JavaScript. The canonical tells crawlers
// that the microsite is the real page, and the body is a fallback link for
// anything that honours neither.
export default function ProviderRedirect({ target }) {
  const { siteConfig } = useDocusaurusContext();
  const { to, name } = target;
  const canonical = /^https?:\/\//.test(to) ? to : `${siteConfig.url}${to}`;
  return (
    <>
      <Head>
        <title>{`${name} | ${siteConfig.title}`}</title>
        <meta httpEquiv="refresh" content={`0;URL='${to}'`} />
        <link rel="canonical" href={canonical} />
      </Head>
      <main style={{ padding: '2rem', fontFamily: 'system-ui, sans-serif' }}>
        <p>
          Redirecting to <a href={to}>{name}</a>.
        </p>
      </main>
    </>
  );
}
