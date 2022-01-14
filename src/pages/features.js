import React from 'react';
import Layout from '@theme/Layout';
import {
  FeaturesContent,
  FeaturesHeader,
} from '../components';

import { featuresPageData } from '../data/features';  

export default function Features() {
  return (
   <Layout
      title={featuresPageData.title}
      description={featuresPageData.description}
      image={featuresPageData.image}
      keywords={featuresPageData.keywords}
    >
      <header>
        <div className="lgContainer">
          <FeaturesHeader data={featuresPageData.header} />
        </div>
      </header>
      <main>
        <FeaturesContent data={featuresPageData}/>
      </main>
    </Layout>
  );
}
