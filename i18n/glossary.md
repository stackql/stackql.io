# Translation glossary

Read by the `i18n-translate` skill before translating a page. The first section applies to every locale; each locale then has its own section with terminology and style.

## All locales

Never translate, transliterate or reformat:

- Product and project names: StackQL, `stackql`, `stackql-deploy`, StackQL Studios, StackQL Registry, Query Library, MCP, Model Context Protocol, OpenTelemetry, Docusaurus.
- Provider, service and resource names as they appear in StackQL (`aws`, `google`, `azure`, `github`, `aws.ec2.instances`), and the vendors' own product names (Amazon EC2, Google Cloud Storage, Microsoft Entra ID).
- SQL keywords and StackQL statements (`SELECT`, `INSERT`, `EXEC`, `SHOW`, `DESCRIBE`, `REGISTRY PULL`), CLI commands, subcommands, flags and environment variables (`stackql exec`, `--output`, `AWS_ACCESS_KEY_ID`), file and directory names, configuration keys, HTTP methods and status codes.
- Everything inside code fences, inline code, URLs, link targets, image paths, front matter keys and heading ids (`{#like-this}`).
- Author names, company names, version numbers, licence names.

Terms that are translated consistently (the locale section gives the rendering):

- provider, service, resource, method, operation
- query, statement, result set, row, column
- control plane, data plane
- agent, AI agent, tool (an MCP tool), prompt
- credentials, authentication, authorization
- deployment, resource lifecycle, infrastructure as code

## de

Audience: German-speaking engineers reading technical documentation. Formal address (Sie), present tense, short sentences. Keep English loanwords that German developer documentation uses as a matter of course; do not coin German compounds for established technical terms.

Terminology:

| English | German |
|---|---|
| provider | Provider (der) |
| service | Service (der) |
| resource | Ressource (die) |
| method | Methode (die) |
| operation | Operation (die) |
| query | Abfrage (die); "to query" = abfragen |
| statement | Anweisung (die) |
| result set | Ergebnismenge (die) |
| row / column | Zeile / Spalte |
| table | Tabelle (die) |
| view | View (die) |
| materialized view | materialisierte View |
| window function | Fensterfunktion (die) (not "Window-Funktion"; matches the sidebar category) |
| window frame | Fensterrahmen (der) |
| session | Sitzung (die); session variable = Sitzungsvariable |
| principal | Principal (der) |
| secret store | Secret-Store (der) |
| string (data type) | String (der); as a general noun "Zeichenkette" is also fine |
| integer | Ganzzahl (die) |
| whitespace | Whitespace (loanword in technical context) |
| pull (a provider) | laden; "pulled" = geladen |
| gating (MCP) | Freigabe (die) |
| control plane / data plane | Control Plane / Data Plane (unverändert) |
| agent, AI agent | Agent, KI-Agent |
| tool (MCP) | Tool (das) |
| prompt | Prompt (der) |
| credentials | Anmeldedaten (die) |
| authentication / authorization | Authentifizierung / Autorisierung |
| deployment | Deployment (das) |
| resource lifecycle | Ressourcenlebenszyklus (der) |
| infrastructure as code | Infrastructure as Code (unverändert) |
| cloud provider | Cloud-Anbieter (der) |
| command line | Kommandozeile (die) |
| shell | Shell (die) |
| server | Server (der) |
| output / output format | Ausgabe / Ausgabeformat |
| template / templating | Vorlage / Templating |
| quick start | Schnellstart (der) |
| getting started | Erste Schritte |
| installation | Installation (die) |
| release | Release (das) |
| repository | Repository (das) |
| registry | Registry (die) |
| documentation | Dokumentation (die) |
| tutorial | Tutorial (das) |
| announcement | Ankündigung (die) |
| feature | Funktion (die) |
| example | Beispiel (das) |
| note / tip / warning (admonition titles are translated by the theme) | Hinweis / Tipp / Warnung |

UI and navigation labels (navbar, footer, sidebar categories):

| English | German |
|---|---|
| Install | Installation |
| Installing StackQL | StackQL installieren |
| Getting Started | Erste Schritte |
| Command Line Usage | Kommandozeile |
| Language Specification | Sprachspezifikation |
| Functions | Funktionen |
| Developers | Entwickler |
| Providers | Provider |
| Quick Starts | Schnellstarts |
| AI Agents | KI-Agenten |
| MCP Server / MCP Tools / Embedded MCP | MCP-Server / MCP-Tools / Eingebettetes MCP |
| Blog | Blog |
| Product Announcements | Produktankündigungen |
| Provider Announcements | Provider-Ankündigungen |
| Tutorials | Tutorials |
| Documentation | Dokumentation |
| Contact us | Kontakt |
| Privacy Policy | Datenschutzerklärung |
| Terms of Service | Nutzungsbedingungen |
| Support | Support |
| AI Reference | KI-Referenz |
| More | Mehr |
| Welcome to StackQL | Willkommen bei StackQL |

Style:

- Headings in sentence case as in the English source; no title case.
- Compound nouns with a hyphen where German spelling requires it (Cloud-Anbieter, KI-Agent, MCP-Server).
- Numbers, dates and units as in the source; decimal comma is not applied inside code or tables of command output.
- The `description` front matter stays one sentence and under about 160 characters.
