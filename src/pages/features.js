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
      viewBox='0 0 21 31'
      svgPath='M9.808 0.487999L0.448 18.536H4.336L10.288 6.728L16.24 18.536H20.128L10.768 0.487999H9.808Z 
      M9.808 12.488L0.448 30.536H4.336L10.288 18.728L16.24 30.536H20.128L10.768 12.488H9.808Z'
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
