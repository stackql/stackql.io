import React from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
  DownloadCard,
} from '../components';
import Link from '@docusaurus/Link';
import MediaQuery from 'react-responsive'

const windows = {
  icon: 'fab fa-windows',
  title: 'Microsoft Windows',
  description: 'x86 and x64',
  buttons: [
    {
      url:  'https://releases.stackql.io/stackql/latest/infraql_windows_amd64.msi',
      text: 'Download MSI',
      icon: 'fas fa-download',
    },
    {
      url:  'https://releases.stackql.io/stackql/latest/infraql_windows_amd64.zip',
      text: 'Download ZIP',
      icon: 'fas fa-download',
    },    
  ]
};

const macos = {
  icon: 'fab fa-apple',
  title: 'MacOS',
  description: '64-bit AMD and ARM',
  buttons: [
    {
      url:  'https://releases.stackql.io/stackql/latest/infraql_darwin_multiarch.pkg',
      text: 'Download PKG',
      icon: 'fas fa-download',
    },
  ]
};

const linux = {
  icon: 'fab fa-linux',
  title: 'Linux',
  description: 'x86 and x64',
  buttons: [
    {
      url:  'https://releases.stackql.io/stackql/latest/infraql_linux_amd64.zip',
      text: 'Download ZIP',
      icon: 'fas fa-download',
    },
  ]
};

export default function Downloads() {
  const {siteConfig} = useDocusaurusContext();
  // downloadFile();
  return (
   <Layout
      title={`Downloads`}
      description="Description will go into a meta tag in <head />"
      // image
      // keywords
    >
      <SectionHeader
        // disableGutter 
        // overline
        // fadeUp
        // className
        title={
          <span>
          <span style={{color: '#00af91'}}>Download</span> StackQL
          </span>
        }
        subtitle="A new approach to querying and provisioning cloud services."
        align="center"
        ctaGroup = {[
        ]}
      />
      <main>
      {/*  <HomepageFeatures /> */}
      <div className="container">
        <div className="row">
          <div className="col">
            <div className="row">
            {[windows, macos, linux].map(card => (
              <div className="col col--4 margin-bottom--md">
              <DownloadCard
                  key={card.title}
                  data={card}
                  liftUp
                />
                </div>
              ))}
            </div>          
          </div>
        </div>
      </div>  
      </main>
    </Layout>
  );
};
