import React from 'react';

export const downloadsPageData = {
    title: 'Downloads',
    description: 'StackQL is an open-source infrastructure-as-code tool that enables you to deploy, configure, query and operate cloud and SaaS services using SQL.',
    image: '/img/stackql-cover.png',
    keywords: [
      'stackql',
      'stack',
      'cloud query',
      'infrastructure query',
      'infrastructre-as-code',
      'iac',
      'configuration-as-data',
      'infraql',
      'cspm',
      'query google cloud resources',
      'okta deployment',
    ],
    header: {
        title: `<span><span style="color:#00af91">Download</span> StackQL</span>`,
        subtitle: "A new approach to querying and provisioning cloud services.",
        label: "FAMILIAR, FUNCTIONAL, FLEXIBLE",
    },
    downloadCards: [
        {
          platform: 'windows',
          title: 'Microsoft Windows',
          description: 'x86 and x64',
          detailsText: 'chocolatey',
          terminalInstallLine1: 'choco install stackql',
          orText: 'or',
          buttons: [
            {
              url:  'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi',
              text: 'Download MSI',
              icon: 'fas fa-download',
            },
            {
              url:  'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip',
              text: 'Download ZIP',
              icon: 'fas fa-download',
            },    
          ],
        },
        {
          platform: 'macos',  
          title: 'MacOS',
          description: '64-bit AMD and ARM',
          detailsText: 'homebrew',
          terminalInstallLine1: 'brew install stackql',
          orText: 'or',
          buttons: [
            {
              url:  'https://releases.stackql.io/stackql/latest/stackql_darwin_multiarch.pkg',
              text: 'Download PKG (multiarch)',
              icon: 'fas fa-download',
            },
          ],
        },
        {
          platform: 'linux',  
          title: 'Linux',
          description: 'amd64 and arm64',
          detailsText: 'curl',
          amd64InstallLine1: 'curl -L https://bit.ly/stackql-zip -O \\',
          amd64InstallLine2: '&& unzip stackql-zip',
          arm64InstallLine1: 'curl -L https://bit.ly/stackql-arm64-zip -O \\',
          arm64InstallLine2: '&& unzip stackql-arm64-zip',
          orText: 'or',
          buttons: [
            {
              url:  'https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip',
              text: 'Download ZIP (amd64)',
              icon: 'fas fa-download',
            },
            {
              url:  'https://releases.stackql.io/stackql/latest/stackql_linux_arm64.zip',
              text: 'Download ZIP (arm64)',
              icon: 'fas fa-download',
            },            
          ]
        }
      ]
};