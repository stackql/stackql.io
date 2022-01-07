import React from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
  FeaturesContent,
} from '../components';
import styles from '../components/Hero/hero.module.css';
import Link from '@docusaurus/Link';
import MediaQuery from 'react-responsive'

const pageData = {
  features: [
    {
      title: 'Simplified Cloud Provisioning.',
      code: `-- Deploy new cloud resources
INSERT INTO google.compute.instances (
 name,
 zone,
 machineType,
 project)
SELECT
 'worker-1',
 'australia-southeast1-a',
 'f1-micro',
 'infraql-demo';
      `,
      checks: [
        'something',
        'something',
        'something',
      ],
    },
    {
      title: 'Easily Navigate Cloud APIs.',
      code: `-- Discover services and resources available in a provider
SHOW SERVICES IN google LIKE '%compute%';
SHOW RESOURCES IN google.compute LIKE '%instances%';
-- Show attributes of a resource
DESCRIBE google.compute.instances;
DESCRIBE EXTENDED google.compute.instances;
-- Show available methods for a resource
SHOW METHODS IN google.compute.instances;
-- Create a provisioning template
SHOW INSERT INTO google.compute.instances;
    `,
      checks: [
        'something',
        'something',
        'something',
      ],
    },    
    {
      title: 'Query Cloud and SaaS Assets.',
      code: `-- Query cloud resources
SELECT machineType, COUNT(*)
FROM google.compute.instances
GROUP BY machineType
WHERE zone = 'us-east1-a';
/*
    +------------------------+
    |  MACHINETYPE   | COUNT |
    +------------------------+
    | n1-standard-1  |   3   |
    | n1-megamem-96  |   8   |
    | c2-standard-60 |   4   |
    +------------------------+      
*/
      `,
      checks: [
        'something',
        'something',
        'something',
      ],
    },
    {
      title: 'Cloud Operations Made Easy.',
      code: `-- Perform operations on cloud resources
USE google;
EXEC compute.instances.start
 @instance = 'demo-instance-1',
 @project = 'infraql-demo',
 @zone = 'australia-southeast1-a';
✔ Instance started successfully      
      `,
      checks: [
        'something',
        'something',
        'something',
      ],
    },        
  ],
};  

export default function Features() {
  const {siteConfig} = useDocusaurusContext();
  return (
   <Layout
      title={`Features`}
      description="Description will go into a meta tag in <head />"
      // image
      // keywords
    >
      <header>
        <SectionHeader
          title={
            <span>
            Cloud <span style={{color: '#00af91'}}>Everything</span> as SQL
            </span>
          }
          subtitle="A new approach to querying and provisioning cloud services."
          align="center"
          label="FAMILIAR, FUNCTIONAL, FLEXIBLE"
          ctaGroup = {[
            <MediaQuery minWidth={1224}>
              <div className={styles.buttons}>
                <DownloadLink styles={['button--primary']}/>
              </div>
            </MediaQuery>,
            <MediaQuery minWidth={1224}>
              <div style={{width: "2em"}}></div>
            </MediaQuery>,
            <div className={styles.buttons}>
              <DocumentationLink />
            </div>
          ]}
        />
      </header>
      <main>
        <FeaturesContent data={pageData.features}/>
      </main>
    </Layout>
  );
}
