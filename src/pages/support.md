---
title: Support
description: How to get help with StackQL, the StackQL MCP server and the ChatGPT and Claude plugins, including bug reports, questions, security reports and what to include.
hide_table_of_contents: true
---

# Support

StackQL, its MCP server and the ChatGPT and Claude plugins are open source and supported by their maintainers at StackQL Studios and by the community. There is no paid support tier. Use the channel that matches what you need.

## Channels

| Need | Channel |
|--|--|
| Bug report or feature request for the engine, the MCP server or a plugin | [GitHub Issues](https://github.com/stackql/stackql/issues) on `stackql/stackql` |
| Question, how-to or general discussion | [GitHub Discussions](https://github.com/orgs/stackql/discussions) |
| Real-time chat with the community | [Discord](https://discord.com/invite/xVXZ9d5NxN) or [Slack](https://join.slack.com/t/stackqlcommunity/shared_invite/zt-1cbdq9s5v-CkY65IMAesCgFqjN6FU6hg) |
| Problem with a provider's definitions (missing resource, wrong column, failed request) | [GitHub Issues](https://github.com/stackql/stackql/issues), naming the provider in the title |
| Anything that should not be public, including plugin directory matters | [info@stackql.io](mailto:info@stackql.io) |
| Security vulnerability | See [Security reports](#security-reports) below. Do not open a public issue. |
<br/>
Postal address and telephone are on the [Contact us](/contact-us) page.

## What to include

A report that contains the following can usually be acted on without a round trip:

- the StackQL version, from `stackql --version`, or the `server_info` tool output when using the MCP server;
- your operating system and architecture, and how StackQL was installed (binary, npm, PyPI, Docker, MCP Bundle, ChatGPT plugin, Claude plugin);
- the client in use, for example Claude Desktop, the ChatGPT desktop app, Codex, Cursor or the command line;
- the statement or tool call, the error text and, where relevant, the provider and resource involved;
- any log output. Remove credentials, tokens and account identifiers before posting. The `reload_credentials` tool never prints credential values, and nothing else in StackQL should either; if it does, treat that as a security report.

## Response expectations

Issues and discussions are monitored by the maintainers and answered on a best-effort basis; there is no guaranteed response time. Bugs that affect the MCP server or a plugin listing are prioritised. Fixes ship in StackQL releases and reach the plugins, bundles and launchers through their normal release process, so update your installation when a fix is announced on the issue.

## Security reports

Please do not open a public issue for a vulnerability. Report it privately through [GitHub private vulnerability reporting](https://github.com/stackql/stackql/security/advisories/new) or, if you cannot use that, email [info@stackql.io](mailto:info@stackql.io) with "SECURITY" in the subject line. We aim to acknowledge a report within a few business days. The full policy is in the repository's [SECURITY.md](https://github.com/stackql/stackql/blob/main/SECURITY.md).

## Before you ask

- [Installing StackQL](/installing-stackql) covers every installation route, including the [MCP clients](/installing-stackql#using-with-mcp-clients) section for Claude Desktop, ChatGPT, Codex and Goose.
- [Using StackQL with Claude Desktop](/getting-started/claude-desktop) is the full Claude Desktop walkthrough.
- [The MCP server](/command-line-usage/mcp) documents the tools, server modes and the safe-mode approval gate.
- [Common StackQL errors](/ai/troubleshooting/common-stackql-errors) lists error messages and their resolutions.
- Provider credentials for the plugins go in `~/.stackql/.env` using each provider's standard environment variable names; after editing the file, ask the assistant to call `reload_credentials`.
- A known ChatGPT desktop issue ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) can hide local MCP tools from Chat sessions. Threads that run on your computer, Codex threads and the Codex CLI are unaffected.

## Removing StackQL

The plugins and bundles are removed from the client that installed them (the plugin or connector settings of the ChatGPT desktop app, Codex or Claude Desktop). Removing a plugin does not delete the `~/.stackql` directory, which holds your credential file, cached provider definitions and the downloaded binary; delete it yourself if you no longer need it.
