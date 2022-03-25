import React from 'react';
import clsx from 'clsx';
// import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  Downloads,
  SectionHeader,
  DocumentationLink,  
} from '../components';

import { downloadsPageData } from '../data/downloads';
import buttonStyles from '../components/Hero/hero.module.css';

export default function Home() {
  // const {siteConfig} = useDocusaurusContext();
  return (
    <Layout
    title={downloadsPageData.title}
    description={downloadsPageData.description}
    image={downloadsPageData.image}
    keywords={downloadsPageData.keywords}
     >
       <header>
         <SectionHeader
           title={downloadsPageData.header.title}
           subtitle={downloadsPageData.header.subtitle}
           align="center"
           ctaGroup = {[
             <div className={clsx(buttonStyles.buttons)}>
             <DocumentationLink />
             </div>
         ]}
         />
       </header>
      <main>
      <Downloads data={downloadsPageData.downloadCards} />
      </main>
    </Layout>
  );
};