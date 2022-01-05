import React from 'react';
import clsx from 'clsx';
import styles from './HomepageFeatures.module.css';

const FeatureList = [
  {
    title: 'Deploy Cloud Resources',
    icon: 'fas fa-code',
    link: '#',
    description: (
      <>
        Infrastructure-as-Code to deploy and configure cloud infrastructure and applications using a familiar language ... SQL.  Extensible to all cloud and SaaS providers.
      </>
    ),
  },
  {
    title: 'Query Cloud Assets',
    icon: 'fas fa-angle-double-right',
    link: '#',
    description: (
      <>
        Use a familiar SQL language to perform interactive queries against cloud providers for inventory, security posture management, compliance, cost optimisation and more
      </>
    ),
  },
  {
    title: 'Manage Cloud Resources',
    icon: 'fas fa-layer-group',
    link: '#',
    description: (
      <>
        Manage complete life cycle of cloud and SaaS assets from deployment to termination, including seamlessly handling existing cloud stacks and SaaS applications.
      </>
    ),
  },
];

function Feature({icon, link, title, description}) {
  return (
    <div className={clsx('col col--4', styles.featureDiv)}>
      <div className="padding-horiz--md">
        <h3><span className={clsx(icon, styles.featureIcon)}></span>{' '}{title}</h3>
        <p>{description}</p>
      </div>
      <div className={clsx("padding-horiz--md", styles.learnMore)}>
        <a href={link} className={clsx(styles.learnMoreLink)}>
          <span>Learn more <i class="fas fa-angle-double-right"></i></span> 
        </a>
      </div>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
