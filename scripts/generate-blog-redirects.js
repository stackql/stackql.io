#!/usr/bin/env node
// Regenerates the "generated blog redirects" block in netlify.toml.
//
// The blog was a single instance at /blog/<slug> until it was split into
// three plugin instances (product, providers, tutorials) living at
// /blog/<section>/<slug>. Every pre-split post URL, and its .md companion,
// gets a 301 to its new home. Rules are derived from each post's front
// matter slug (the same value Docusaurus uses for the permalink), not from
// the filename.
//
// Usage: node scripts/generate-blog-redirects.js
//
// Posts written after the split are born in a section directory and never
// had a /blog/<slug> URL, so they do not need a rule. Rerun this only if a
// pre-split post's slug or section changes.

const fs = require('fs');
const path = require('path');
const matter = require('gray-matter');

const ROOT = path.resolve(__dirname, '..');
const BLOG_DIR = path.join(ROOT, 'blog');
const NETLIFY_TOML = path.join(ROOT, 'netlify.toml');
const SECTIONS = ['product', 'providers', 'tutorials'];

const BEGIN = '# BEGIN generated blog redirects - do not edit by hand, run scripts/generate-blog-redirects.js';
const END = '# END generated blog redirects';

// Slugs that were corrected during the split. Key is the current slug,
// value is the slug the post was published under (the URL that needs the
// redirect). Posts not listed here redirect from their current slug.
const LEGACY_SLUGS = {
  'five-minute-dashboards-with-stackql': 'five-minute-dashboards-with-stackql.md',
};

// Posts published after the split, keyed by current slug. They never had a
// /blog/<slug> URL, so no redirect is generated for them.
const POST_SPLIT_SLUGS = new Set([]);

function postsIn(section) {
  const dir = path.join(BLOG_DIR, section);
  return fs
    .readdirSync(dir)
    .filter((f) => /\.mdx?$/.test(f))
    .sort()
    .map((file) => {
      const { data } = matter(fs.readFileSync(path.join(dir, file), 'utf8'));
      const slug = typeof data.slug === 'string' ? data.slug.trim() : '';
      if (!slug) {
        throw new Error(`${section}/${file}: missing slug front matter`);
      }
      return { file, slug, section };
    });
}

function rule(from, to) {
  return ['[[redirects]]', `  from = "${from}"`, `  to = "${to}"`, '  status = 301', ''].join('\n');
}

function generate() {
  const seen = new Map();
  const lines = [BEGIN, ''];
  for (const section of SECTIONS) {
    for (const post of postsIn(section)) {
      if (POST_SPLIT_SLUGS.has(post.slug)) continue;
      const legacy = LEGACY_SLUGS[post.slug] || post.slug;
      if (seen.has(legacy)) {
        throw new Error(`duplicate slug "${legacy}" in ${seen.get(legacy)} and ${section}/${post.file}`);
      }
      seen.set(legacy, `${section}/${post.file}`);
      const to = `/blog/${section}/${post.slug}`;
      lines.push(rule(`/blog/${legacy}`, to));
      lines.push(rule(`/blog/${legacy}.md`, `${to}.md`));
    }
  }
  lines.push(END);
  return { block: lines.join('\n'), count: seen.size };
}

function main() {
  const toml = fs.readFileSync(NETLIFY_TOML, 'utf8');
  const start = toml.indexOf(BEGIN);
  const end = toml.indexOf(END);
  if (start === -1 || end === -1 || end < start) {
    throw new Error(`netlify.toml must contain the markers:\n${BEGIN}\n${END}`);
  }
  const { block, count } = generate();
  const next = toml.slice(0, start) + block + toml.slice(end + END.length);
  if (next !== toml) {
    fs.writeFileSync(NETLIFY_TOML, next);
    console.log(`netlify.toml updated: ${count} posts, ${count * 2} redirect rules`);
  } else {
    console.log(`netlify.toml unchanged: ${count} posts, ${count * 2} redirect rules`);
  }
}

main();
