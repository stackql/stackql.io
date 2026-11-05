---
title: Privacy Policy
description: How StackQL Studios handles personal data across the StackQL software, the MCP server, the ChatGPT and Claude plugins, the provider registry, the release downloads and the stackql.io website.
hide_table_of_contents: true
---

# Privacy Policy

*Last updated: 5 November 2026*

This policy explains what personal data StackQL Studios Pty Ltd ("StackQL Studios", "we", "us") collects, why, who receives it, how long it is kept and what controls you have. It covers:

- the StackQL software: the `stackql` binary and the StackQL MCP (Model Context Protocol) server, distributed as MCP Bundles (`.mcpb`), npm and PyPI packages, Docker images and GitHub releases;
- the StackQL plugin for the ChatGPT desktop app and the Codex CLI, and the StackQL plugin and connector for Claude Desktop and Claude Code;
- the services we operate: the provider registry, the release download host and the stackql.io website.

## Summary

| | What is collected | Why | Who receives it | How long it is kept | Your controls |
|--|--|--|--|--|--|
| StackQL software and plugins | Nothing is sent to StackQL Studios. Credentials and query data stay on your machine. | Not applicable | Only the provider APIs you query, directly from your machine | Only what you keep locally | Delete `~/.stackql` to remove local files |
| Provider registry and release downloads | Standard web server access logs: IP address, user agent, requested file, time | Operating the services, capacity planning and abuse prevention | Our hosting providers | Hosting provider's standard log retention, then deleted | None needed; no account exists |
| stackql.io website | Analytics with IP anonymisation (pages viewed, browser and device type, approximate location, a pseudonymous cookie identifier); search queries typed into the site search; hosting access logs | Understanding how the documentation is used and improving it | Google (analytics), Algolia (search), Netlify (hosting) | Analytics for no longer than 14 months; search and hosting logs per those providers' retention | Block cookies or analytics in your browser; the site works without them |
| Correspondence | Your name, email address and the content of messages you send us, or post in our GitHub, Discord or Slack spaces | Answering you and keeping records of the exchange | GitHub, Discord and Slack for posts made there, under their own terms | As long as needed to deal with the matter and keep a record of it | Email us to access, correct or delete what we hold |
<br/>
## Where StackQL runs

StackQL and its MCP server run locally: on your machine, your CI runner or your container. When installed as a plugin, the ChatGPT desktop app, Codex or Claude Desktop starts the same server as a local process on your computer and talks to it over standard input and output. StackQL Studios does not operate a hosted service that receives your queries, results or credentials. Requests go directly from the StackQL process you run to the cloud and SaaS provider APIs you target.

## Cloud provider credentials

- Credentials are read locally, from environment variables, from the `~/.stackql/.env` file that the plugins and launchers use, or from credential files you nominate in StackQL's `--auth` configuration.
- They are used solely to authenticate requests to the provider APIs you query (for example AWS, Azure, Google Cloud, GitHub).
- They never leave your machine except in requests to those providers. They are not transmitted to StackQL Studios or to any other third party, and are not written to StackQL's cache or to the registry.
- The MCP server's `reload_credentials` tool reports variable names and a resolution status; it does not return credential values to the AI assistant.

## Query data

- Queries you run, and the results returned by provider APIs, are processed locally and returned to your MCP client or AI assistant. They are not sent to StackQL Studios. What your AI assistant does with the results it receives is governed by that assistant's privacy policy.
- StackQL's audit log is disabled by default in the MCP server bundles and plugins. If you enable it, audit records are written locally under the application root (`~/.stackql` by default) and remain on your machine.

## Provider registry

To use a provider, StackQL pulls its definition from the StackQL provider registry (`registry.stackql.app`). These are public, OpenAPI-based provider definitions. Pulling a provider is a standard web request: no credentials or query data are sent, and the request appears in the registry host's access logs like any other web request (IP address, user agent, requested file and time). Pulled definitions are cached under your application root.

## Binary downloads

The plugins and the npm and PyPI launchers download the pinned `stackql` binary for your platform from `releases.stackql.io` the first time they run, verify its SHA-256 checksum and cache it under `~/.stackql`. That download, like any download from our release host or the website, is recorded in the host's access logs (IP address, user agent, requested file and time). Those logs are used to operate the service, plan capacity and prevent abuse, are kept for the hosting provider's standard retention period, and are not combined with any other data about you.

## Telemetry

The StackQL binary, the MCP server and the plugins do not send usage telemetry or analytics to StackQL Studios.

## The stackql.io website

- **Analytics.** The website uses Google Analytics with IP anonymisation to understand which pages are read and how visitors arrive. It records pages viewed, browser and device type, approximate location derived from the anonymised address, and a pseudonymous identifier stored in a cookie. We use it only to understand and improve the documentation. Google processes this data on our behalf; it is retained for no longer than 14 months and then deleted automatically. You can block the cookie or analytics scripts in your browser and the site will work without them.
- **Search.** Site search is provided by Algolia. The text you type into the search box is sent to Algolia to return results and is handled under Algolia's privacy policy.
- **Hosting.** The website is hosted by Netlify, whose servers keep standard access logs.
- **Downloads and the registry.** See the sections above.

No account or sign-up exists for the website, the software or the plugins.

## Correspondence and community spaces

If you email us, we keep your name, email address and the content of the exchange for as long as needed to deal with it and to keep a record of it. Issues, discussions and messages you post on GitHub, Discord or Slack are public or visible to the members of those spaces and are handled by those services under their own terms and privacy policies.

## Your rights

StackQL Studios is an Australian company and handles personal data under the Privacy Act 1988 (Cth) and the Australian Privacy Principles. You can ask us what personal data we hold about you, and ask us to correct or delete it, by emailing [privacy@stackql.io](mailto:privacy@stackql.io). We will respond within a reasonable period. If you are not satisfied with our response you can complain to the Office of the Australian Information Commissioner. If you are in the European Economic Area or the United Kingdom, you have the rights set out in the GDPR, including access, rectification, erasure and objection; our legal basis for the processing described on this page is our legitimate interest in operating and improving the software and the website.

## Children

The software, the plugins and the website are intended for use by professionals and are not directed at children. We do not knowingly collect personal data from children.

## Contact

Questions about this policy: [privacy@stackql.io](mailto:privacy@stackql.io), or by post to StackQL Studios Pty Ltd, Level 24, 570 Bourke Street, Melbourne, Victoria 3000, Australia.

## Changes

We may update this policy. Material changes are reflected by the "Last updated" date above.
