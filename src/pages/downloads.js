import React from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
    SectionHeader,
} from '../components';
import { Button } from '@mui/material';

export default function Downloads() {
    return (
     <Layout
      title={`Home`}
      description="Description will go into a meta tag in <head />"
      // image
      // keywords
    >
      <SectionHeader
        label="Downloads"
        ctaGroup={[
            <Button variant="contained" color="primary" size="large">
              Contact us
            </Button>,
            <Button variant="contained" color="primary" size="large">
            Contact us 2
            </Button>            
          ]}
        title={
          <span>
            Welcome to the Revolution
          </span>
        }
        subtitle={<>A new approach to querying and provisioning cloud services.</>}
        titleVariant="h1"
        fadeUp
        align="center"
      />
      <main>
      {/*  <HomepageFeatures /> */}
      </main>
    </Layout>
  );
};