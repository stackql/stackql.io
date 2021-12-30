import React from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  SectionHeader,
} from '../components';

export default function Features() {
  const {siteConfig} = useDocusaurusContext();
  // downloadFile();
  return (
   <Layout
      title={`Features`}
      description="Description will go into a meta tag in <head />"
      // image
      // keywords
    >
      <SectionHeader
          title="Cloud Everything as SQL"
          subtitle="A new approach to querying and provisioning cloud services."
          align="center"
          disableGutter
          titleVariant="h1"      
        // title
        // titleVariant
        // subtitleVariant
        // subtitle
        // subtitleColor
        // label
        // overline
        // fadeUp
        // align
        // ctaGroup
        // disableGutter
        // titleClasses
        // className
        // labelProps
        // titleProps
        // subtitleProps
      />
      <main>
      {/*  <HomepageFeatures /> */}
      </main>
    </Layout>
  );
}
