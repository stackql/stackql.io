import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

// Rendered by plugins/blog-landing at /blog. `sections` is the plugin's
// generated data: one entry per blog instance with its newest posts.

function formatDate(iso) {
  // UTC on both server and client so SSR and hydration agree
  return new Date(iso).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    timeZone: 'UTC',
  });
}

function SectionCard({ section }) {
  const { label, description, permalink, count, posts } = section;
  return (
    <div className={clsx('card', styles.card)}>
      <div className="card__header">
        <Heading as="h2" className={styles.sectionTitle}>
          <Link to={permalink}>{label}</Link>
        </Heading>
        <p className={styles.sectionDescription}>{description}</p>
      </div>
      <div className="card__body">
        <ul className={styles.postList}>
          {posts.map((post) => (
            <li key={post.permalink} className={styles.post}>
              <Link to={post.permalink} className={styles.postTitle}>
                {post.title}
              </Link>
              <time dateTime={post.date} className={styles.postDate}>
                {formatDate(post.date)}
              </time>
            </li>
          ))}
        </ul>
      </div>
      <div className="card__footer">
        <Link className="button button--outline button--primary button--block" to={permalink}>
          All {label.toLowerCase()} ({count})
        </Link>
      </div>
    </div>
  );
}

export default function BlogLanding({ sections }) {
  const title = 'Blog';
  const description =
    'Cloud operations, security and automation using SQL. StackQL product announcements, provider announcements and tutorials.';
  return (
    <Layout title={title} description={description}>
      <main className={clsx('container', 'margin-vert--lg', styles.main)}>
        <header className={styles.header}>
          <Heading as="h1">StackQL Blog</Heading>
          <p className={styles.lead}>{description}</p>
        </header>
        <div className="row">
          {sections.map((section) => (
            <section key={section.id} className={clsx('col', 'col--4', styles.column)}>
              <SectionCard section={section} />
            </section>
          ))}
        </div>
      </main>
    </Layout>
  );
}
