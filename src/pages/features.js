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
      <ScrollToTop smooth />
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
