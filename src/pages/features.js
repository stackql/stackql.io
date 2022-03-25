import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';

import {
  FeaturesContent,
  FeaturesHeader,
} from '../components';
import ScrollToTop from 'react-scroll-to-top';
import { useMediaQuery } from 'react-responsive';
import MediaQuery from 'react-responsive';

import { featuresPageData } from '../data/features';  

export default function Features() {

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
      title={featuresPageData.title}
      description={featuresPageData.description}
      image={featuresPageData.image}
      keywords={featuresPageData.keywords}
    >
      <ScrollToTop 
      smooth 
      className={clsx('scrollToTop')}
      viewBox='0 0 20 17'
      svgPath='M15.28 16.8L10.096 4.8L4.984 16.8H0.784L8.032 0H11.872L19.144 16.8H15.28Z'
      color='#0F4C81'
      />
      <header>
        <section className={clsx('sectionContainer')}>
          <div className={clsx('margin-top--lg', 'padding-bottom--xl', 'lgContainer', isNormalLayout ? 'divQtrBackgroundBottom' : '')}>
            <FeaturesHeader data={featuresPageData.header} />
          </div>
        </section>
      </header>
      <main>
        <section className={clsx('sectionContainer')}>
          <FeaturesContent data={featuresPageData.content}/>
        </section>
      </main>
    </Layout>
  );
}
