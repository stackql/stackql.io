import React from 'react';
// import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  Hero,
  HomepageFeatures,
} from '../components';
import Head from '@docusaurus/Head';
import { homePageData } from '../data/homepage';

export default function Home() {
  // const {siteConfig} = useDocusaurusContext();
  return (
   <Layout
      title={homePageData.title}
      description={homePageData.description}
      image={homePageData.image}
      keywords={homePageData.keywords}
    >
      <Head>
      <meta name="wot-verification" content="38f4ae79b091dc8046a0"/>
      </Head>
      <Hero data={homePageData.hero} />
      <main>
      <HomepageFeatures data={homePageData.features} />
      </main>
    </Layout>
  );
};