---
title: Terms of Service
description: Terms governing use of the StackQL software, the StackQL MCP server, the ChatGPT and Claude plugins, the provider registry and the stackql.io website, operated by StackQL Studios Pty Ltd.
hide_table_of_contents: true
---

# Terms of Service

*Last updated: 5 November 2026*

These terms are an agreement between you and StackQL Studios Pty Ltd ("StackQL Studios", "we", "us"), of Level 24, 570 Bourke Street, Melbourne, Victoria 3000, Australia. They apply when you use any of the following:

- the StackQL software: the `stackql` binary, the StackQL MCP (Model Context Protocol) server, and the launchers and bundles that distribute it (npm, PyPI, Docker, MCP Bundles and GitHub releases);
- the StackQL plugin for the ChatGPT desktop app and the Codex CLI, and the StackQL plugin and connector for Claude Desktop and Claude Code;
- the StackQL provider registry, the release download host and the documentation;
- the stackql.io website.

By installing or using any of these you accept these terms. If you do not accept them, do not use them.

## Open source licence

The StackQL software is open source and is licensed under the [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE). The MIT License governs your rights to use, copy, modify and redistribute the software and its source code. These terms add to the MIT License; where the two conflict about the software itself, the MIT License prevails. Provider definitions in the StackQL provider registry are published under the licence stated in the registry's repository.

No payment is collected through the software, the plugins or the website.

## How the software works

StackQL runs on your computer, your CI runner or your container. There is no hosted StackQL service. The software makes requests directly to the cloud and SaaS provider APIs you query, using credentials you supply. The MCP server and the plugins run the same software as a local process started by your MCP client or AI assistant. The [Privacy Policy](/privacy) describes what data is handled where.

## Your responsibilities

- **Credentials and accounts.** You supply the credentials that StackQL uses, and you are responsible for keeping them secure and for everything those credentials allow. StackQL Studios never receives them.
- **Authorisation.** Only use StackQL against accounts, tenancies and systems that you are authorised to access.
- **Operations you run.** StackQL executes the statements you, or an AI assistant acting on your instructions, ask it to run. A `SELECT` reads live data; `INSERT`, `UPDATE`, `DELETE` and `EXEC` create, change or remove resources in your accounts. The MCP server starts in safe mode, which asks for approval before any mutation, but you are responsible for reviewing and approving the operations that are carried out and for any charges or changes they cause in your provider accounts.
- **Third-party terms.** The provider APIs you query, and the AI assistants and MCP clients you use with StackQL, are subject to their own terms. You are responsible for complying with them.
- **Lawful use.** Do not use the software or the website for any unlawful purpose, to interfere with other users or with the services we operate, or to circumvent a provider's access controls or rate limits.
- **Keeping up to date.** Updates, including security fixes, are published through the StackQL releases and the distribution channels above. Keep your installation current.

## Third-party services

StackQL interacts with services that StackQL Studios does not operate, including cloud and SaaS provider APIs, the ChatGPT, Codex and Claude applications, package registries and GitHub. We are not a party to your agreements with those services and are not responsible for their availability, conduct or terms.

## Registry, downloads and availability

The provider registry, the release download host, the website and the documentation are provided as they are, and we may change, suspend or withdraw any of them at any time. We do not commit to any availability level. Provider definitions are maintained in the open and may change as the underlying provider APIs change.

## Intellectual property

The StackQL name and logo are marks of StackQL Studios. The MIT License grants no rights to use them, except to describe the software accurately. The documentation and website content are owned by StackQL Studios unless stated otherwise. Contributions to the StackQL repositories are accepted under the terms of the repository in which they are made.

## Disclaimer and limitation of liability

The software is provided "as is", without warranty of any kind, as stated in the MIT License. To the fullest extent permitted by law, StackQL Studios excludes all warranties and conditions not expressly stated in these terms and is not liable for any loss or damage arising from the use of, or inability to use, the software, the plugins, the registry or the website, including changes made to resources in your provider accounts and the resulting charges.

Nothing in these terms excludes, restricts or modifies any guarantee, right or remedy that cannot lawfully be excluded, including under the Australian Consumer Law. Where liability for breach of such a guarantee can be limited, it is limited, at our option, to resupplying the relevant service or paying the cost of having it resupplied.

## Changes to these terms

We may update these terms. Changes take effect when published on this page, and the "Last updated" date above will reflect them. Continued use after a change is acceptance of the updated terms. The MIT License applying to a copy of the software you already hold is not affected by changes to these terms.

## Termination

You may stop using the software, the plugins and the website at any time. We may suspend access to the services we operate for a breach of these terms. Your rights under the MIT License for software you already hold are not affected.

## Governing law

These terms are governed by the laws of Victoria, Australia. Any dispute is subject to the non-exclusive jurisdiction of the courts of Victoria.

## Contact

Questions about these terms: [info@stackql.io](mailto:info@stackql.io), or by post to the address above. Support channels are listed on the [Support](/support) page.
