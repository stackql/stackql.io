import React from 'react';
import clsx from 'clsx';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
  HubspotContactForm,
  Map,
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
                  <div className={clsx('col', 'col--12')}>
                  <h2>{contactusPageData.body.form.heading}</h2>
                  </div>
                </div>  
                <div className={clsx('row')}>
                  <div className={clsx('col', 'col--12', 'divWhiteBackground')}>
                    <HubspotContactForm 
                      region={contactusPageData.body.form.hubspot.region}
                      portalId={contactusPageData.body.form.hubspot.portalId}
                      formId={contactusPageData.body.form.hubspot.formId}
                    />
                  </div>
                </div>  
                </div>  
                {/* address and map */}
                <div className={clsx('col', 'col--6', 'divtest')}>
                <div className={clsx('row')}>
                  <div className={clsx('col', 'col--12')}>
                  <h2>{contactusPageData.body.address.heading}</h2>
                  </div>  
                </div>
                {/* address */}
                <div className={clsx('row')}>
                  <div className={clsx('col', 'col--1')}>
                    <span><i class="fas fa-building"></i></span>
                  </div>
                  <div className={clsx('col', 'col--11')}>
                    {contactusPageData.body.address.line1}
                  </div>  
                </div>
                <div className={clsx('row')}>
                  <div className={clsx('col', 'col--1')}>
                  </div>
                  <div className={clsx('col', 'col--11')}>
                    {contactusPageData.body.address.line2}
                  </div>  
                </div>
                {/* phone */}
                <div className={clsx('row')}>
                  <div className={clsx('col', 'col--1')}>
                    <span><i class="fas fa-phone"></i></span>
                  </div>
                  <div className={clsx('col', 'col--11')}>
                    {contactusPageData.body.address.phone}
                  </div>  
                </div>
                {/* email */}
                <div className={clsx('row')}>
                  <div className={clsx('col', 'col--1')}>
                    <span><i class="fas fa-envelope"></i></span>
                  </div>
                  <div className={clsx('col', 'col--11')}>
                    {contactusPageData.body.address.email}
                  </div>  
                </div>                
               {/* twitter */}
                <div className={clsx('row')}>
                  <div className={clsx('col', 'col--1')}>
                    <span><i class="fab fa-twitter"></i></span>
                  </div>
                  <div className={clsx('col', 'col--11')}>
                    {contactusPageData.body.address.twitter}
                  </div>  
                </div>
                {/* map */}
                <div className={clsx('row')}>
                  <div className={clsx('col', 'col--12')}>
                    <Map
                      mapsApiKey={contactusPageData.body.address.map.mapsApiKey}
                      lat={contactusPageData.body.address.map.lat}
                      long={contactusPageData.body.address.map.long}
                      defaultZoom={contactusPageData.body.address.map.defaultZoom}
                      markerTitle={contactusPageData.body.address.map.markerTitle}
                    />
                  </div>  
                </div>
                {/* end map */}
                </div>
              </div>
            </div>
          </div>
      </div>      
      </main>
    </Layout>
  );
}
