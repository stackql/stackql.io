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
import MediaQuery from 'react-responsive'

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
        // disableGutter 
        // overline
        // fadeUp
        // className
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
      <main>
      {/*  <HomepageFeatures /> */}
      <div className="container">
        <div className="row">
          <div className="col">
            <div className="row">
              <div className="col col--4 divtest padding-top--md margin-bottom--md">fredalskdjlaksjdlakjsd;lkaj;sdlkjal;sdkja;lksdjlaksjdlkajsd;lkajsldkjalskdjlaksjdlkajsdlkjalskdjalskdjals;dkj
              </div>
              <div className="col col--4 divtest">
              </div>
              <div className="col col--4 divtest">
              </div>                    
            </div>          
          </div>
        </div>
      </div>  
      </main>
    </Layout>
  );
}
