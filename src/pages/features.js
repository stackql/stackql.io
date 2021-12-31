import React from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
} from '../components';
import styles from '../components/Hero/Hero.module.css';
import Link from '@docusaurus/Link';

export default function Features() {
  const {siteConfig} = useDocusaurusContext();
  // downloadFile();
  return (
   <Layout
      title={`Features`}
      description="Description will go into a meta tag in <head />"
      // image
      // keywords
    >
      <SectionHeader
          title="Cloud Everything as SQL"
          subtitle="A new approach to querying and provisioning cloud services."
          align="center"
        // disableGutter 
        label="label fred"
        // overline
        // fadeUp
        ctaGroup = {[
          <div className={styles.buttons}>
            <DownloadLink styles={['button--primary']}/>
          </div>,
          <div style={{width: "2em"}}></div>,
          <div className={styles.buttons}>
            <DocumentationLink />
          </div>,
        ]}
        // className
      />
      <main>
      {/*  <HomepageFeatures /> */}
      </main>
    </Layout>
  );
}
