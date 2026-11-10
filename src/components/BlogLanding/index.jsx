import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import Heading from '@theme/Heading';
import Translate, {translate} from '@docusaurus/Translate';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import NoIndex from '@theme/NoIndex';
import {useIsLocalizedBuild} from '@site/src/lib/i18n';
import styles from './styles.module.css';

// Rendered by plugins/blog-landing at /blog (and /<locale>/blog). `sections`
// is the plugin's generated data: one entry per blog instance with its
// newest posts. The UI strings go through <Translate> so they are extracted
// into i18n/<locale>/code.json; the section titles come from each blog
// instance (localized through its options.json).

function formatDate(iso, locale) {
  // UTC on both server and client so SSR and hydration agree
  return new Date(iso).toLocaleDateString(locale, {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    timeZone: 'UTC',
  });
}

function SectionCard({ section, locale }) {
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
                {formatDate(post.date, locale)}
              </time>
            </li>
          ))}
        </ul>
      </div>
      <div className="card__footer">
        <Link className="button button--outline button--primary button--block" to={permalink}>
          {translate(
            {
              id: 'blogLanding.allPosts',
              message: 'All {label} ({count})',
              description:
                'Button under each section of the blog landing page; {label} is the section title, {count} its number of posts',
            },
            { label, count },
          )}
        </Link>
      </div>
    </div>
  );
}

export default function BlogLanding({ sections }) {
  const {
    i18n: { currentLocale },
  } = useDocusaurusContext();
  // The posts themselves are not translated: in a localized build the
  // landing page lists English posts and stays out of the index.
  const localizedBuild = useIsLocalizedBuild();
  const title = translate({
    id: 'blogLanding.title',
    message: 'Blog',
    description: 'Browser title of the blog landing page',
  });
  const description = translate({
    id: 'blogLanding.description',
    message:
      'Cloud operations, security and automation using SQL. StackQL product announcements, provider announcements and tutorials.',
    description: 'Lead paragraph and meta description of the blog landing page',
  });
  return (
    <Layout title={title} description={description}>
      {localizedBuild && <NoIndex />}
      <main className={clsx('container', 'margin-vert--lg', styles.main)}>
        <header className={styles.header}>
          <Heading as="h1">
            <Translate id="blogLanding.heading" description="Heading of the blog landing page">
              StackQL Blog
            </Translate>
          </Heading>
          <p className={styles.lead}>{description}</p>
        </header>
        <div className="row">
          {sections.map((section) => (
            <section key={section.id} className={clsx('col', 'col--4', styles.column)}>
              <SectionCard section={section} locale={currentLocale} />
            </section>
          ))}
        </div>
      </main>
    </Layout>
  );
}
