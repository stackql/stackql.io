import React from 'react';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import {providerMeta} from './providerMeta';
import styles from './styles.module.css';

function ProviderCard({provider}) {
  const meta = providerMeta(provider.id);
  return (
    <article className="col col--4 margin-bottom--lg">
      <Link
        to={`/docs/query-library/${provider.id}`}
        className={`card padding--md ${styles.providerCard}`}
      >
        <div className={styles.providerCardHeader}>
          {provider.logo ? (
            <img src={provider.logo} alt={`${meta.title} logo`} className={styles.providerLogo} />
          ) : (
            <div className={styles.providerLogoFallback} aria-hidden="true">
              {meta.title.charAt(0)}
            </div>
          )}
          <h2 className={styles.providerCardTitle}>{meta.title}</h2>
          <span className={`badge badge--secondary ${styles.countBadge}`}>
            {provider.count} {provider.count === 1 ? 'query' : 'queries'}
          </span>
        </div>
        <p className={styles.providerCardBlurb}>{meta.description}</p>
      </Link>
    </article>
  );
}

export default function LandingPage({providers, total, buildId}) {
  return (
    <Layout
      title="StackQL Query Library"
      description="Curated, parameterized StackQL queries for cloud inventory, security and operations, browsable by provider and consumed by the stackql MCP server."
    >
      <main className="container margin-vert--lg">
        <h1>StackQL Query Library</h1>
        <p>
          A curated set of parameterized, known-good StackQL queries for common
          cloud inventory, security and operations asks. Every entry is
          published three ways: a rendered page for humans, a raw Markdown
          source (<code>.md</code>) and a structured JSON document
          (<code>.json</code>) consumed by the stackql MCP server&apos;s{' '}
          <code>query_library_search</code> and <code>query_library_get</code>{' '}
          tools, which validate parameters and render SQL server-side so the
          model never performs substitution itself.
        </p>
        <p>
          {total} queries across {providers.length} providers. Pick a provider
          to browse its entries.
        </p>
        <div className="row margin-top--md">
          {providers.map((p) => (
            <ProviderCard key={p.id} provider={p} />
          ))}
        </div>
        <h2 className="margin-top--lg">Machine catalogue</h2>
        <p>
          Tools consume the library from{' '}
          <a href="/docs/query-library/index.json">index.json</a> (searchable
          catalogue), <a href="/docs/query-library/manifest.json">manifest.json</a>{' '}
          (build metadata; <code>build_id</code> {buildId ? <code>{buildId}</code> : null} is
          a content hash of the library and the MCP cache key) and a Markdown
          catalogue at <a href="/docs/query-library/index.md">index.md</a>.
        </p>
        <p>
          Entries are mastered in the{' '}
          <a href="https://github.com/stackql/stackql.io/tree/main/query-library">
            stackql.io repository
          </a>{' '}
          and verified nightly against live providers - see the{' '}
          <a href="https://github.com/stackql/stackql.io/blob/main/query-library/CONTRIBUTING.md">
            contributing guide
          </a>{' '}
          to add one.
        </p>
      </main>
    </Layout>
  );
}
