import {useEffect} from 'react';
import Head from '@docusaurus/Head';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';

// Rendered by plugins/provider-redirects at /providers/<slug>,
// /registry/<slug> and the bare /registry. A Docusaurus head redirect, the
// same mechanism the hand-written src/pages stubs used.
//
// The hop between the main site and a provider microsite should feel like
// one site, so this renders no body at all: the meta refresh fires with or
// without JavaScript, and location.replace() fires on hydration so the stub
// never lands in the browser history. The canonical tells crawlers that the
// microsite is the real page.
export default function ProviderRedirect({ target }) {
  const { siteConfig } = useDocusaurusContext();
  const { to, name } = target;
  const canonical = /^https?:\/\//.test(to) ? to : `${siteConfig.url}${to}`;

  useEffect(() => {
    if (to) {
      window.location.replace(to);
    }
  }, [to]);

  return (
    <Head>
      <title>{`${name} | ${siteConfig.title}`}</title>
      <meta httpEquiv="refresh" content={`0;URL='${to}'`} />
      <link rel="canonical" href={canonical} />
    </Head>
  );
}
