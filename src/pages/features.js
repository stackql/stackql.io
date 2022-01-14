import React from 'react';
import Layout from '@theme/Layout';
import MediaQuery from 'react-responsive'
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
  FeaturesContent,
} from '../components';

import styles from '../components/Hero/hero.module.css';

import { featuresPageData } from '../data/features';  

export default function Features() {
  return (
   <Layout
      title={`Features`}
      description="Description will go into a meta tag in <head />"
      // image
      // keywords
    >
      <header>
        <SectionHeader
          title={
            <span>
            Cloud <span style={{color: '#00af91'}}>Everything</span> as SQL
            </span>
          }
          subtitle="A new approach to querying and provisioning cloud services."
          align="center"
          label="FAMILIAR, FUNCTIONAL, FLEXIBLE"
          ctaGroup = {[
            <MediaQuery minWidth={1224}>
              <div className={styles.buttons}>
                <DownloadLink styles={['button--primary']}/>
              </div>
            </MediaQuery>,
            <MediaQuery minWidth={1224}>
              <div style={{width: "2em"}}></div>
            </MediaQuery>,
            <div className={styles.buttons}>
              <DocumentationLink />
            </div>
          ]}
        />
      </header>
      <main>
        <FeaturesContent data={featuresPageData}/>
      </main>
    </Layout>
  );
}
