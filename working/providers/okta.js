import React from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
Hero,
} from '../../components';

const spinner = ['⠋', '⠙', '⠹', '⠸', '⠼', '⠴', '⠦', '⠧', '⠇', '⠏']
const termLines = [
    {
        text: 'SELECT machineType, COUNT(*)',
        cmd: true,
        delay: 80,
    },
    {
        text: 'FROM google.compute.instances',
        cmd: true,
        delay: 80,
    },
    {
        text: 'GROUP BY machineType',
        cmd: true,
        delay: 80,
    },
    {
        text: "WHERE zone = 'us-east1-a'",
        cmd: true,
        delay: 80,
    },
    {
        text: ' ',
        cmd: false,
        repeat: true,
        repeatCount: 3,
        frames: spinner.map(function (spinner) {
        return {
            text: spinner + ' Running query',
            delay: 10
        }
    })
    },
    {
        text: 
`+------------------------+
|  MACHINETYPE   | COUNT |
+------------------------+
| n1-standard-1  |   3   |
| n1-megamem-96  |   8   |
| c2-standard-60 |   4   |
+------------------------+`,
        cmd: false,
    }
]

const pageData = {
  hero: {
    title: [
      'Every Cloud.',
      'Every Operation.',
      'One Language.',
    ],
    subtitle: 'Provision. Query. Secure.',
    animatedTerm: {
      termLines: termLines,
    },
  },

};  

/*
const downloadFile = () => {
   const downloadLink = document.createElement('a');
   downloadLink.href = 'https://storage.googleapis.com/infraql-downloads/infraql.msi';
   downloadLink.download = 'macos';
   document.body.appendChild(downloadLink);
   downloadLink.click();
   document.body.removeChild(downloadLink);
 };
*/

export default function Home() {
  const {siteConfig} = useDocusaurusContext();
  // downloadFile();
  return (
   <Layout
      title={`Home`}
      description="Description will go into a meta tag in <head />"
      // image
      // keywords
    >
      <Hero data={pageData.hero} />
      <main>
      {/*  <HomepageFeatures /> */}
      </main>
    </Layout>
  );
}
