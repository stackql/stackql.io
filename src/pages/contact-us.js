import React from 'react';
import clsx from 'clsx';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
  HubspotContactForm,
} from '../components';
import styles from '../components/Hero/hero.module.css';
import Link from '@docusaurus/Link';
import MediaQuery from 'react-responsive'

import { contactusPageData } from '../data/contact-us';  

export default function ContactUs() {
  const {siteConfig} = useDocusaurusContext();
  return (
   <Layout
   title={contactusPageData.title}
   description={contactusPageData.description}
   image={contactusPageData.image}
   keywords={contactusPageData.keywords}
    >
      <header>
        <SectionHeader
          title={contactusPageData.header.title}
          subtitle={contactusPageData.header.subtitle}
          align="center"
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
      <div className={clsx('padding-bottom--xl', 'lgContainer', 'divHalfBackgroundBottom')}>
          <div className={clsx('row')}>
            <div className={clsx('container')}>
              <div className={clsx('row')}>
                {/* form */}
                <div className={clsx('col', 'col--6', 'divtest')}>
                <div className={clsx('row')}>
                  Form Header
                </div>  
                <div className={clsx('row')}>
                  <HubspotContactForm 
                    region="na1"
                    portalId="21220110"
                    formId="85a06e5f-d7aa-46ec-9953-b26ff962eedb"
                  />
                </div>  
                </div>  
                {/* address and map */}
                <div className={clsx('col', 'col--6', 'divtest')}>
                </div>
              </div>
            </div>
          </div>
      </div>      
      </main>
    </Layout>
  );
}
