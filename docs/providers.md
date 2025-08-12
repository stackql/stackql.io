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
        href: 'https://aws.stackql.io/providers/aws/',
        icon: '/img/providers/aws/favicon.ico'
      },
      { 
        name: 'Microsoft Azure',
        href: 'https://azure.stackql.io/providers/azure/',
        icon: '/img/providers/azure/favicon.ico'
      },
      { 
        name: 'Google Cloud Platform',
        href: 'https://googlecloud.stackql.io/providers/google/',
        icon: '/img/providers/google/favicon.ico'
      },
      { 
        name: 'Digital Ocean',
        href: 'https://digitalocean.stackql.io/providers/digitalocean/',
        icon: '/img/providers/digitalocean/favicon.png'
      },
      { 
        name: 'Linode',
        href: 'https://linode.stackql.io/providers/linode/',
        icon: '/img/providers/linode/favicon.ico'
      }
    ]
  },
  {
    id: 'data-analytics',
    name: 'Data & Analytics',
    providers: [
      { 
        name: 'Databricks',
        href: 'https://databricks-workspace.stackql.io/providers/databricks_workspace/',
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
        href: 'https://okta.stackql.io/providers/okta/',
        icon: '/img/providers/okta/favicon.png'
      },
      { 
        name: 'Google Admin',
        href: 'https://googleadmin.stackql.io/providers/googleadmin/',
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
        href: 'https://k8s.stackql.io/providers/k8s/',
        icon: '/img/providers/k8s/favicon.png'
      },
      { 
        name: 'Firebase',
        href: 'https://firebase.stackql.io/providers/firebase/',
        icon: '/img/providers/firebase/favicon.png'
      },
      { 
        name: 'Vercel',
        href: 'https://vercel.stackql.io/providers/vercel/',
        icon: '/img/providers/vercel/favicon.ico'
      },
      { 
        name: 'Netlify',
        href: 'https://netlify.stackql.io/providers/netlify/',
        icon: '/img/providers/netlify/favicon.ico'
      }
    ]
  },
  {
    id: 'monitoring-observability',
    name: 'Monitoring & Observability',
    providers: [
      { 
        name: 'DataDog',
        href: 'https://datadog.stackql.io/providers/datadog/',
        icon: '/img/providers/datadog/favicon.ico'
      },
      { 
        name: 'SumoLogic',
        href: 'https://sumologic.stackql.io/providers/sumologic/',
        icon: '/img/providers/sumologic/favicon.png'
      },
      { 
        name: 'PagerDuty',
        href: 'https://pagerduty.stackql.io/providers/pagerduty/',
        icon: '/img/providers/pagerduty/icon.svg'
      }
    ]
  },
  {
    id: 'other-providers',
    name: 'Other Providers',
    providers: [
      { 
        name: 'GoDaddy',
        href: 'https://godaddy.stackql.io/providers/godaddy/',
        icon: '/img/providers/godaddy/favicon.png'
      },
      { 
        name: 'Homebrew',
        href: 'https://homebrew.stackql.io/providers/homebrew/',
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