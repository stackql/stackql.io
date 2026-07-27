import React from 'react';
import Link from '@docusaurus/Link';
import catalogue from '@site/static/docs/query-library/index.json';
import styles from './styles.module.css';

// Related-entries footer for query pages, driven by the related front matter
// field. Titles and descriptions come from the generated catalogue.
export default function RelatedEntries({related}) {
  if (!related || related.length === 0) return null;
  const entries = related
    .map((id) => catalogue.entries.find((e) => e.id === id))
    .filter(Boolean);
  if (entries.length === 0) return null;
  return (
    <section className={styles.relatedSection}>
      <h2>Related queries</h2>
      <ul>
        {entries.map((entry) => (
          <li key={entry.id}>
            <Link to={`/docs/query-library/queries/${entry.id}`}>{entry.title}</Link>
            {' - '}
            {entry.description}
          </li>
        ))}
      </ul>
    </section>
  );
}
