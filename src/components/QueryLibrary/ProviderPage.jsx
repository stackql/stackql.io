import React from 'react';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import VerbBadge, {StatusBadge} from './VerbBadge';
import {providerMeta} from './providerMeta';
import styles from './styles.module.css';

export default function ProviderPage({provider, entries}) {
  const meta = providerMeta(provider.id);
  return (
    <Layout
      title={`${meta.title} queries - StackQL Query Library`}
      description={`Curated StackQL queries for ${meta.title}: ${entries
        .map((e) => e.title)
        .slice(0, 5)
        .join(', ')}.`}
    >
      <main className="container margin-vert--lg">
        <nav className="margin-bottom--sm" aria-label="breadcrumb">
          <Link to="/docs/query-library">Query Library</Link>
          {' / '}
          <span>{meta.title}</span>
        </nav>
        <div className={styles.providerCardHeader}>
          {provider.logo && (
            <img src={provider.logo} alt={`${meta.title} logo`} className={styles.providerLogo} />
          )}
          <h1 style={{marginBottom: 0}}>{meta.title} queries</h1>
        </div>
        <p className="margin-top--sm">{meta.description}</p>
        <table className={styles.entryTable}>
          <thead>
            <tr>
              <th>Query</th>
              <th>Description</th>
              <th>Verb</th>
              <th>Required params</th>
            </tr>
          </thead>
          <tbody>
            {entries.map((entry) => (
              <tr key={entry.id}>
                <td>
                  <Link to={`/docs/query-library/queries/${entry.id}`}>{entry.title}</Link>{' '}
                  <StatusBadge status={entry.status} />
                </td>
                <td>{entry.description}</td>
                <td>
                  <VerbBadge verb={entry.verb || (entry.mutation ? 'mutation' : 'select')} />
                </td>
                <td>
                  {(entry.required_params || []).length === 0
                    ? '-'
                    : entry.required_params.map((p) => (
                        <React.Fragment key={p}>
                          <code>{p}</code>{' '}
                        </React.Fragment>
                      ))}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <p className="margin-top--md">
          <em>draft</em> marks entries not yet verified against a live
          provider by the nightly execution job; templates are still
          schema-validated and parse-checked in CI.
        </p>
      </main>
    </Layout>
  );
}
