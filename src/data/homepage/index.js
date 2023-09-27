import React from 'react';
import { googleSelect1, googleSelect2 } from './terminals/google';
import { awsSelect1, awsSelect2 } from './terminals/aws';
import { azureSelect } from './terminals/azure';

const getRandomTermLines = () => {
  const termLinesSets = [
    googleSelect1,
    googleSelect2,
    awsSelect1,
    awsSelect2,
    azureSelect,
  ];
  return termLinesSets[Math.floor(Math.random() * termLinesSets.length)];
};

export const homePageData = {
  title: 'Home',
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
  hero: {
    title: [
      'Every Cloud.',
      'Every Operation.',
      'One Language.',
    ],
    subtitle: 'Provision. Query. Secure.',
    animatedTerm: {
      // termLines: termLines,
      termLines: getRandomTermLines(),
    },
  },
  features: [
    {
      title: 'Query Multiple Clouds',
      icon: 'query',
      link: '/features',
      description: (
        <>
          Use a familiar SQL language to perform interactive queries against cloud providers for inventory, security posture management, compliance, cost optimisation and more
        </>
      ),
    },
    {
      title: 'Deploy Cloud Resources',
      icon: 'deploy',
      link: '/features',
      description: (
        <>
          Infrastructure-as-Code to deploy and configure cloud infrastructure and applications using a familiar language ... SQL.  Extensible to all cloud and SaaS providers.
        </>
      ),
    },
    {
      title: 'Manage Cloud Resources',
      icon: 'manage',
      link: '/features',
      description: (
        <>
          Manage complete life cycle of cloud and SaaS assets from deployment to termination, including seamlessly handling existing cloud stacks and SaaS applications.
        </>
      ),
    },
  ],
};