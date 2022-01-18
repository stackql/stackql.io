import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DocumentationLink,
  DownloadCard,
} from '../components';
import { useMediaQuery } from 'react-responsive';
import MediaQuery from 'react-responsive';

import buttonStyles from '../components/Hero/hero.module.css';

import { downloadsPageData } from '../data/downloads';
const windows = downloadsPageData.downloadCards.windows;
const macos = downloadsPageData.downloadCards.macos;
const linux = downloadsPageData.downloadCards.linux;

export default function Downloads() {

  const isNormalLayout = useMediaQuery({
    query: '(min-width: 997px)'
  });

  const isMobile = useMediaQuery({
    query: '(max-width: 480px)'
  });

  const isTablet = useMediaQuery({
    query: '(min-width: 481px) and (max-width: 996px)'
  });

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
        <div className={clsx('padding-bottom--xl', 'lgContainer', isNormalLayout ? 'divHalfBackgroundBottom' : '')}>
          <div className={clsx('row')}>
            <div className={clsx(isNormalLayout ? 'container' : isTablet ? 'tabletContainer' : 'mobileContainer')}>
            {/* desktop/large tablet view */}         
            <MediaQuery minWidth={997}>
              <div className={clsx('row')}>
              {[windows, macos, linux].map(card => (
                <div className={clsx('col', 'col--4')}>
                  <DownloadCard
                      key={card.title}
                      data={card}
                      liftUp
                    />
                </div>
                ))}
              </div> 
            </MediaQuery>

          {/* mobile/small tablet view */}                
            <MediaQuery maxWidth={996}>
            {[windows, macos, linux].map(card => (
              <div className={clsx('row')}>
                  <div className={clsx('col', 'col--12', 'margin-bottom--lg')}>
                    <DownloadCard
                        key={card.title}
                        data={card}
                        liftUp
                      />
                  </div>
              </div>
              ))}
            </MediaQuery>

            </div>
          </div>
        </div>
      </main>
    </Layout>
  );
};
