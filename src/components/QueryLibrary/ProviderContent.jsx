import React from 'react';
import Link from '@docusaurus/Link';
import catalogue from '@site/static/docs/query-library/index.json';
import providersSummary from '@site/static/docs/query-library/providers.json';
import VerbBadge, {StatusBadge} from './VerbBadge';
import styles from './styles.module.css';

// Provider page body, mounted by the generated query-library/<provider>.mdx
// stubs inside the docs layout (which supplies the sidebar and breadcrumbs).
export default function ProviderContent({provider}) {
  const summary =
    providersSummary.find((p) => p.id === provider) || {
      id: provider,
      title: provider,
      description: '',
      logo: null,
    };
  const entries = catalogue.entries.filter((e) => e.id.split('/')[0] === provider);
  return (
    <>
      <div className={styles.providerCardHeader}>
        {summary.logo && (
          <img
            src={summary.logo}
            alt={`${summary.title} logo`}
            className={styles.providerLogo}
          />
        )}
        <h1 style={{marginBottom: 0}}>{summary.title} queries</h1>
      </div>
      <p className="margin-top--sm">{summary.description}</p>
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
        <em>draft</em> marks entries not yet verified against a live provider
        by the nightly execution job; templates are still schema-validated and
        parse-checked in CI.
      </p>
    </>
  );
}
