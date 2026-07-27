import React from 'react';
import Link from '@docusaurus/Link';
import VerbBadge, {StatusBadge} from './VerbBadge';
import providersSummary from '@site/static/docs/query-library/providers.json';
import styles from './styles.module.css';

function providerTitle(id) {
  const summary = providersSummary.find((p) => p.id === id);
  return summary ? summary.title : id;
}

const FAN_OUT_TEXT = {
  region: 'one call per region when swept across regions',
  project: 'one call per project when swept org-wide',
  account: 'one call per account',
  subscription: 'one call per subscription when swept tenant-wide',
};

function CodeList({items}) {
  return items.map((item, i) => (
    <React.Fragment key={item}>
      {i > 0 && ' '}
      <code>{item}</code>
    </React.Fragment>
  ));
}

export default function QueryPageHeader({frontMatter}) {
  const {
    providers = [],
    services = [],
    verb = 'select',
    status,
    auth = [],
    cost,
    last_verified: lastVerified,
  } = frontMatter;

  const showCostWarning = cost && (cost.fan_out !== 'none' || cost.expensive);

  return (
    <div className={styles.metaPanel}>
      <div className={styles.metaBadges}>
        <VerbBadge verb={verb} />
        <StatusBadge status={status} />
        {providers.map((p) => (
          <Link
            key={p}
            className="badge badge--secondary"
            to={`/docs/query-library/${p}`}
            style={{textDecoration: 'none'}}
          >
            {providerTitle(p)}
          </Link>
        ))}
        {lastVerified && (
          <span className={styles.lastVerified}>last verified {String(lastVerified)}</span>
        )}
      </div>
      <div className={styles.metaRow}>
        <span className={styles.metaLabel}>Services</span>
        <span className={styles.metaValue}>
          <CodeList items={services} />
        </span>
      </div>
      {auth.length > 0 && (
        <div className={styles.metaRow}>
          <span className={styles.metaLabel}>Credentials</span>
          <span className={styles.metaValue}>
            <CodeList items={auth} />
          </span>
        </div>
      )}
      {showCostWarning && (
        <div className={`alert alert--warning ${styles.costAlert}`} role="note">
          {cost.fan_out !== 'none' && (
            <>
              <strong>Fan-out: {cost.fan_out}</strong>
              {' - '}
              {FAN_OUT_TEXT[cost.fan_out] || 'iterates when run at scale'}
              {'. '}
            </>
          )}
          {cost.expensive && <>Expensive at scale. </>}
          {cost.notes}
        </div>
      )}
    </div>
  );
}
