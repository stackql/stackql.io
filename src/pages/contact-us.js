import React from 'react';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import { contactusPageData } from '../data/contact-us';

// /contact-us was a meta-refresh stub to GitHub Discussions, which agents and
// crawlers read as an empty page. This is a real page: the community
// channels first (GitHub Discussions is the preferred one), then StackQL
// Studios' email, phone and postal address as text with mailto: and tel:
// links, so a contact route exists in page text and not only in JSON-LD.
// The details come from src/data/contact-us/index.js; the same address and
// phone are in themeConfig.structuredData.organization.

const community = [
  {
    name: 'GitHub Discussions',
    href: 'https://github.com/orgs/stackql/discussions',
    text: 'Questions, ideas and show-and-tell. The best place to start.',
  },
  {
    name: 'GitHub Issues',
    href: 'https://github.com/stackql/stackql/issues',
    text: 'Bug reports and feature requests for the StackQL engine.',
  },
  {
    name: 'Discord',
    href: 'https://discord.com/invite/xVXZ9d5NxN',
    text: 'Chat with the StackQL community and maintainers.',
  },
  {
    name: 'Slack',
    href: 'https://join.slack.com/t/stackqlcommunity/shared_invite/zt-1cbdq9s5v-CkY65IMAesCgFqjN6FU6hg',
    text: 'The StackQL community workspace.',
  },
];

export default function ContactUs() {
  const { address } = contactusPageData.body;
  return (
    <Layout
      title="Contact Us"
      description="How to reach StackQL Studios and the StackQL community: GitHub Discussions, Discord and Slack for support, and email, phone and postal address for everything else.">
      <main className="container margin-vert--lg">
        <div className="row">
          <div className="col col--8 col--offset-2">
            <h1>Contact us</h1>
            <p>
              StackQL is open source and community supported. For help using
              it, start a discussion on GitHub; for anything else, StackQL
              Studios is reachable by email, phone or post.
            </p>

            <h2>Community support</h2>
            <p>
              <Link
                className="button button--primary"
                href="https://github.com/orgs/stackql/discussions/new?category=general&title=Hey%20StackQL!">
                Start a discussion on GitHub
              </Link>
            </p>
            <ul>
              {community.map(({ name, href, text }) => (
                <li key={href}>
                  <Link href={href}>{name}</Link> - {text}
                </li>
              ))}
            </ul>

            <h2>StackQL Studios</h2>
            <p>
              For commercial support enquiries, partnerships, media and
              everything that is not a usage question, email us. Phone and
              post reach the Melbourne office.
            </p>
            <address style={{ fontStyle: 'normal' }}>
              <p>
                <strong>Email:</strong>{' '}
                <a href={address.emailLink}>{address.email}</a>
                <br />
                <strong>Phone:</strong>{' '}
                <a href={address.phoneLink}>{address.phone}</a>
              </p>
              <p>
                <strong>Address:</strong>
                <br />
                StackQL Studios Pty Ltd
                <br />
                {address.line1}
                <br />
                {address.line2}
              </p>
            </address>
          </div>
        </div>
      </main>
    </Layout>
  );
}
