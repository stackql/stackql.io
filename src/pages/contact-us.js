import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
  HubspotContactForm,
} from '../components';
import styles from '../components/Hero/hero.module.css';
import Link from '@docusaurus/Link';
import MediaQuery from 'react-responsive';
import { 
  FaRegEnvelope, 
  FaRegBuilding,
  FaTwitter,
  FaPhone,
} from "react-icons/fa";

import { contactusPageData } from '../data/contact-us';  

const ContactForm = props => {
  const { margin } = props;
 
  return (
    <div className={clsx('padding-bottom--xl', 'lgContainer', 'divThreeQtrBackgroundBottom', margin ? 'margin-top--xl' : '')}>
    <div className={clsx('row')}>
      <div className={clsx('mobileContainer80')}>
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
  );
};

const SmViewportContactDetails = () => {
  return (
    <div className={clsx('container', 'margin-top--lg', 'text--center', 'addressDetails')}>
      {/* address */}
      <div className={clsx('row')}>
        <div className={clsx('col', 'col--12')}>
        {contactusPageData.body.address.line1}
        </div>
      </div>
      <div className={clsx('row')}>
        <div className={clsx('col', 'col--12')}>
        {contactusPageData.body.address.line2}
        </div>
      </div>
      <div className={clsx('row')}>
        <div className={clsx('col', 'col--12', 'emptyRow')}>
        </div>
      </div>
      {/* phone */}
      <div className={clsx('row')}>
        <div className={clsx('col', 'col--12')}>
          <Link className={clsx('contactLink')} to={contactusPageData.body.address.phoneLink}>   
            <div style={{display: "flex", justifyContent: "center"}}>
              <FaPhone size={25}/><span>&nbsp;&nbsp;{contactusPageData.body.address.phone}</span>
            </div>
          </Link>
        </div>
      </div>
      <div className={clsx('row')}>
        <div className={clsx('col', 'col--12', 'emptyRow')}>
        </div>
      </div>
      {/* email */}      
      <div className={clsx('row')}>
        <div className={clsx('col', 'col--12')}>
          <Link className={clsx('contactLink')} to={contactusPageData.body.address.emailLink}>
            <div style={{display: "flex", justifyContent: "center"}}>
              <FaRegEnvelope size={25}/><span>&nbsp;&nbsp;{contactusPageData.body.address.email}</span>
            </div>
          </Link>
        </div>
      </div>
    </div>
  );
};

const LgViewportContactDetails = () => {
  return (
    <div className={clsx('container', 'margin-bottom--xl', 'margin-top--xl')}>
    <div className={clsx('row')}>
    <div className={clsx('col', 'col--1')}></div>
    {/* email */}
    <div className={clsx('col', 'col--3')}>
    <Link className={clsx('contactLink')} to={contactusPageData.body.address.emailLink}>
      <div className={clsx('row', 'addressDetails')}>
        <div className={clsx('col', 'col--1', 'padding--none')}>
          <FaRegEnvelope size={30}/>
        </div>
        <div className={clsx('col', 'col--11')}>
          {contactusPageData.body.address.email}
        </div>  
      </div>        
      </Link>
    </div>
    {/* address */}
    <div className={clsx('col', 'col--4')}>
    <div className={clsx('row', 'addressDetails')}>
      <div className={clsx('col', 'col--1')}>
        <FaRegBuilding size={30}/>
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
    {/* twitter */}
    <div className={clsx('col', 'col--3')}>
    <Link className={clsx('contactLink')} to={contactusPageData.body.address.twitterLink}>
    <div className={clsx('row', 'addressDetails')}>
      <div className={clsx('col', 'col--1', 'padding--none')}>
        <FaTwitter size={30}/>
      </div>
      <div className={clsx('col', 'col--11')}>
        {contactusPageData.body.address.twitter}
      </div>  
    </div>
    </Link>                
    </div> 
    <div className={clsx('col', 'col--1')}></div>       
    </div>
  </div>
    );
};  

export default function ContactUs() {
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
                <DownloadLink iconSize={20} styles={['button--primary']}/>
              </div>
            </MediaQuery>,
            <MediaQuery minWidth={1224}>
              <div style={{width: "2em"}}></div>
            </MediaQuery>,
            <div className={styles.buttons}>
              <DocumentationLink iconSize={20} />
            </div>
          ]}
        />
      </header>
      <main>
        <section className={clsx('sectionContainer')}>
          <MediaQuery maxWidth={996}>
            {/* contact form */}
            <ContactForm margin />
            {/* contact details */}  
            <SmViewportContactDetails />
          </MediaQuery>

          <MediaQuery minWidth={997}>
            {/* contact details */}  
            <LgViewportContactDetails />
            {/* contact form */}
            <ContactForm />
          </MediaQuery>
        </section>
      </main>
    </Layout>
  );
}
