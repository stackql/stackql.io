import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';

import {
  FeaturesContent,
  FeaturesHeader,
} from '../components';
import ScrollToTop from 'react-scroll-to-top';

import { featuresPageData } from '../data/features';  

export default function Features() {
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
        <div className={clsx('margin-top--lg', 'padding-bottom--xl', 'lgContainer', 'divQtrBackgroundBottom')}>
          <FeaturesHeader data={featuresPageData.header} />
        </div>
      </header>
      <main>
        <FeaturesContent data={featuresPageData.content}/>
      </main>
    </Layout>
  );
}
