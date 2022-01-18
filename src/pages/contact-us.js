import React from 'react';
import clsx from 'clsx';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
  HubspotContactForm,
//  Map,
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
          disableGutter
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
      {/* contact details */}  
      <div className={clsx('container', 'margin-bottom--xl', 'margin-top--xl')}>
        <div className={clsx('row')}>
        <div className={clsx('col', 'col--1')}></div>
        {/* phone */}
        <div className={clsx('col', 'col--3')}>
        <Link className={clsx('contactLink')} to={contactusPageData.body.address.phoneLink}>
          <div className={clsx('row', 'addressDetails')}>
            <div className={clsx('col', 'col--1', 'padding--none')}>
              <span><i class="fas fa-phone"></i></span>
            </div>
            <div className={clsx('col', 'col--11')}>
              {contactusPageData.body.address.phone}
            </div>  
          </div>        
          </Link>
        </div>
        {/* address */}
        <div className={clsx('col', 'col--4')}>
        <div className={clsx('row', 'addressDetails')}>
          <div className={clsx('col', 'col--1')}>
            <span><i class="fas fa-building"></i></span>
          </div>
          <div className={clsx('col', 'col--11')}>
            {contactusPageData.body.address.line1}
          </div>  
        </div>
        <div className={clsx('row', 'addressDetails')}>
          <div className={clsx('col', 'col--1')}></div>
          <div className={clsx('col', 'col--11')}>
            {contactusPageData.body.address.line2}
          </div>  
        </div>        
        </div>
        {/* email and twitter */}
        <div className={clsx('col', 'col--3')}>
        <Link className={clsx('contactLink')} to={contactusPageData.body.address.emailLink}>
        <div className={clsx('row', 'addressDetails')}>
          <div className={clsx('col', 'col--1', 'padding--none')}>
            <span><i class="fas fa-envelope"></i></span>
          </div>
          <div className={clsx('col', 'col--11')}>
            {contactusPageData.body.address.email}
          </div>  
        </div>
        </Link>                
        {/*
        <div className={clsx('row', 'addressDetails')}>
          <div className={clsx('col', 'col--1')}>
            <span><i class="fab fa-twitter"></i></span>
          </div>
          <div className={clsx('col', 'col--11')}>
            {contactusPageData.body.address.twitter}
          </div>  
        </div>
        */}   
        </div> 
        <div className={clsx('col', 'col--1')}></div>       
        </div>
      </div>
      {/* contact form */}
      <div className={clsx('padding-bottom--xl', 'lgContainer', 'divHalfBackgroundBottom')}>
        <div className={clsx('row')}>
          <div className={clsx('container')}>
            <div className={clsx('row')}>
            <div className={clsx('col', 'col--3')}></div>
            {/* form */}            
            <div className={clsx('col', 'col--6', 'contactFormDiv')}>
            <div className={clsx('row')}>
              <div className={clsx('col', 'col--12', 'text--center', 'contactFormHeading')}>
              {contactusPageData.body.form.heading}
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
            {/* end form */}
            <div className={clsx('col', 'col--3')}></div>
            </div>
          </div>
        </div>
      </div>
      </main>
    </Layout>
  );
}
