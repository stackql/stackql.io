import React from 'react';
import clsx from 'clsx';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
  DownloadCard,
} from '../components';
import Link from '@docusaurus/Link';
import MediaQuery from 'react-responsive'

import { downloadsPageData } from '../data/downloads';
const windows = downloadsPageData.downloadCards.windows;
const macos = downloadsPageData.downloadCards.macos;
const linux = downloadsPageData.downloadCards.linux;

export default function Downloads() {
  const {siteConfig} = useDocusaurusContext();
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
          ]}
        />
      </header>
     
      <main>
        <div className={clsx('padding-bottom--xl', 'lgContainer', 'divHalfBackgroundBottom')}>
          <div className={clsx('row')}>
            <div className={clsx('container')}>
              <div className={clsx('row')}>
              {[windows, macos, linux].map(card => (
                <div className={clsx('col', 'col--4', 'margin-bottom--md')}>
                <DownloadCard
                    key={card.title}
                    data={card}
                    liftUp
                  />
                  </div>
                ))}
              </div>          
            </div>
          </div>
        </div>  
      </main>
    </Layout>
  );
};
