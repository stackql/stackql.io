import React from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
Hero,
} from '../components';

const pageData = {
  hero: {
    title: [
      'Every Cloud.',
      'Every Operation.',
      'One Language.',
    ],
    subtitle: 'Provision. Query. Secure.',
    animatedTerm: '',
  },

};  

export default function Home() {
  const {siteConfig} = useDocusaurusContext();
  return (
    <Layout
      title={`Home`}
      description="Description will go into a meta tag in <head />"
      // image
      // keywords
    >
      <Hero data={pageData.hero} />
      <main>
      {/*  <HomepageFeatures /> */}
      </main>
    </Layout>
  );
}
