---
title: Available Providers
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';

{/* Provider Data Map - Single source of truth */}
export const PROVIDER_CATEGORIES = [
  {
    id: 'cloud-providers',
    name: 'Cloud Providers',
    providers: [
      { 
        name: 'Amazon Web Services',
        href: 'https://aws-provider.stackql.io/',
        icon: '/img/providers/aws/favicon.ico'
      },
      { 
        name: 'Microsoft Azure',
        href: 'https://azure-provider.stackql.io/',
        icon: '/img/providers/azure/favicon.ico'
      },
      { 
        name: 'Google Cloud Platform',
        href: 'https://google-provider.stackql.io/',
        icon: '/img/providers/google/favicon.ico'
      },
      { 
        name: 'Digital Ocean',
        href: 'https://digitalocean-provider.stackql.io/',
        icon: '/img/providers/digitalocean/favicon.png'
      },
      { 
        name: 'Linode',
        href: 'https://linode-provider.stackql.io/',
        icon: '/img/providers/linode/favicon.ico'
      },
      { 
        name: 'Azure Stack',
        href: 'https://azure-stack-provider.stackql.io/',
        icon: '/img/providers/azure/favicon.ico'
      },
      { 
        name: 'Azure ISV',
        href: 'https://azure-isv-provider.stackql.io/',
        icon: '/img/providers/azure/favicon.ico'
      },            
    ]
  },
  {
    id: 'data-analytics',
    name: 'Data & Analytics',
    providers: [
      { 
        name: 'Databricks Account',
        href: 'https://databricks-account-provider.stackql.io/',
        icon: '/img/providers/databricks/favicon.ico'
      },
      { 
        name: 'Databricks Workspace',
        href: 'https://databricks-workspace-provider.stackql.io/',
        icon: '/img/providers/databricks/favicon.ico'
      },      
      { 
        name: 'Snowflake',
        href: 'https://snowflake-provider.stackql.io/',
        icon: '/img/providers/snowflake/favicon.ico'
      },
      { 
        name: 'Confluent',
        href: 'https://confluent.stackql.io/providers/confluent/',
        icon: '/img/providers/confluent/favicon.ico'
      }
    ]
  },
  {
    id: 'identity-security',
    name: 'Identity & Security',
    providers: [
      { 
        name: 'Okta',
        href: 'https://okta-provider.stackql.io/',
        icon: '/img/providers/okta/favicon.png'
      },
      { 
        name: 'Google Admin',
        href: 'https://googleadmin-provider.stackql.io/',
        icon: '/img/providers/googleadmin/favicon.ico'
      }
    ]
  },
  {
    id: 'ai-machine-learning',
    name: 'AI & Machine Learning',
    providers: [
      { 
        name: 'OpenAI',
        href: 'https://openai.stackql.io/providers/openai/',
        icon: '/img/providers/openai/favicon.ico'
      },
      { 
        name: 'Anthropic',
        href: 'https://anthropic.stackql.io/providers/anthropic/',
        icon: '/img/providers/anthropic/favicon.png'
      }
    ]
  },
  {
    id: 'devops-development',
    name: 'DevOps & Development',
    providers: [
      { 
        name: 'GitHub',
        href: 'https://github.stackql.io/providers/github/',
        icon: '/img/providers/github/favicon.ico'
      },
      { 
        name: 'Kubernetes',
        href: 'https://k8s-provider.stackql.io/',
        icon: '/img/providers/k8s/favicon.png'
      },
      { 
        name: 'Firebase',
        href: 'https://firebase-provider.stackql.io/',
        icon: '/img/providers/firebase/favicon.png'
      },
      { 
        name: 'Vercel',
        href: 'https://vercel-provider.stackql.io/',
        icon: '/img/providers/vercel/favicon.ico'
      },
      { 
        name: 'Netlify',
        href: 'https://netlify-provider.stackql.io/',
        icon: '/img/providers/netlify/favicon.ico'
      },
      { 
        name: 'Deno Deploy',
        href: 'https://deno-provider.stackql.io/',
        icon: '/img/providers/deno/favicon.ico'
      }      
    ]
  },
  {
    id: 'monitoring-observability',
    name: 'Monitoring & Observability',
    providers: [
      { 
        name: 'DataDog',
        href: 'https://datadog-provider.stackql.io/',
        icon: '/img/providers/datadog/favicon.ico'
      },
      { 
        name: 'SumoLogic',
        href: 'https://sumologic-provider.stackql.io/',
        icon: '/img/providers/sumologic/favicon.png'
      },
      { 
        name: 'PagerDuty',
        href: 'https://pagerduty-provider.stackql.io/',
        icon: '/img/providers/pagerduty/icon.svg'
      }
    ]
  },
  {
    id: 'other-providers',
    name: 'Other Providers',
    providers: [
      { 
        name: 'Google Workspace',
        href: 'https://googleworkspace-provider.stackql.io/',
        icon: '/img/providers/googleworkspace/favicon.ico'
      },
      { 
        name: 'GoDaddy',
        href: 'https://godaddy-provider.stackql.io/',
        icon: '/img/providers/godaddy/favicon.png'
      },
      { 
        name: 'Azure Extras',
        href: 'https://azure-extras-provider.stackql.io/',
        icon: '/img/providers/azure/favicon.ico'
      },      
      { 
        name: 'Homebrew',
        href: 'https://homebrew-provider.stackql.io/',
        icon: '/img/providers/homebrew/favicon.ico'
      }
    ]
  }
];

{/* Custom TOC Component - Now generated from data */}
export const CustomTOC = () => {
  return (
    <div className="table-of-contents table-of-contents__left-border">
      <ul className="toc-headings">
        {PROVIDER_CATEGORIES.map(category => (
          <li key={category.id}>
            <a href={`#${category.id}`}>{category.name}</a>
            <ul>
              {category.providers.map(provider => (
                <li key={provider.name}>
                  <a href={provider.href}>{provider.name}</a>
                </li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
};

{/* Main content component using the data map */}
export const ProviderContent = () => {
  return (
    <>
      <blockquote>
        don't see yours here? <a 
          href="https://github.com/stackql/stackql-provider-registry/issues/new?template=feature_request.md&title=%5BFEATURE%5D%20New%20Provider" 
          target="_blank" 
          rel="noopener noreferrer">reach out</a>
      </blockquote>

      {PROVIDER_CATEGORIES.map(category => (
        <div key={category.id}>
          <h2 id={category.id}>{category.name}</h2>
          <DocCardList
            items={category.providers.map(provider => ({
              type: 'link',
              label: provider.name,
              href: provider.href,
              customProps: { icon: provider.icon }
            }))}
          />
        </div>
      ))}
    </>
  );
};

<div className="row">
  <div className="col col--9">
    <ProviderContent />
  </div>
  <div className="col col--3">
    <CustomTOC />
  </div>
</div>