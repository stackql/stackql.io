---
slug: /ai-agents
title: Agents IA
hide_title: false
keywords:
  - stackql
  - agents IA
  - mcp
  - model context protocol
  - agentique
description: Connecter des agents IA aux services cloud et SaaS avec StackQL via des serveurs MCP, des outils, des intégrations embarquées et des requêtes SQL.
image: "/img/stackql-featured-image.png"
---

StackQL donne aux agents IA la même interface SQL que celle utilisée par les humains pour interroger, provisionner et exploiter des services cloud et SaaS. Un agent découvre ce qu'un provider expose, valide une requête avant son exécution, lit l'état en temps réel avec `SELECT` et le modifie avec `INSERT`, `UPDATE` et `DELETE`, sans fichier d'état ni SDK par cloud.

## Comment les agents se connectent {#how-agents-connect}

- **MCP sur stdio** pour les clients d'éditeur et de bureau tels que Claude Desktop, Cursor et Continue. Le client lance lui-même `stackql mcp --mcp.server.type=stdio`. Consultez [Serveur MCP](/command-line-usage/mcp).
- **MCP sur HTTP** pour les agents autonomes et les automatisations qui se connectent à un processus `stackql mcp` ou `stackql srv` de longue durée, avec authentification par jeton bearer et TLS facultatif.
- **MCP embarqué** pour les applications compilées : le serveur s'exécute dans le même processus en Go, Rust, Swift, Kotlin, .NET et Gleam. Consultez [MCP embarqué](/mcp/embedded).
- **Protocole filaire PostgreSQL** via [`stackql srv`](/command-line-usage/srv) pour les frameworks d'agents, les notebooks et les outils de BI qui parlent déjà Postgres.

## Démarrage rapide avec Claude Desktop {#quick-start-with-claude-desktop}

Installez le serveur depuis l'Anthropic Connector Directory (recommandé) ou le MCP Bundle téléchargeable, ou enregistrez-le à la main dans `claude_desktop_config.json` :

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio", "--tls.allowInsecure"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

N'incluez que les identifiants des providers que vous prévoyez d'utiliser. La procédure complète, y compris les téléchargements du bundle et l'utilisation d'un fichier d'identifiants, se trouve dans [Utiliser StackQL avec Claude Desktop](/getting-started/claude-desktop).

## Ce qu'un agent peut faire {#what-an-agent-can-do}

Le serveur MCP expose un petit ensemble d'outils typés. Chaque outil renvoie une vue rendue pour le modèle et une charge utile structurée pour les clients programmatiques.

- **Découvrir** : `list_providers`, `list_services`, `list_resources`, `list_methods` et `describe_method` parcourent la hiérarchie des providers et renvoient les entrées et les champs de sortie de chaque opération, de sorte que les paramètres `WHERE` obligatoires sont connus avant l'écriture d'une requête.
- **Valider et lire** : `validate_select_query` analyse et planifie un `SELECT` sans l'exécuter ; `run_select_query` l'exécute.
- **Modifier** : `run_mutation_query` exécute `INSERT`, `UPDATE`, `REPLACE` et `DELETE` ; `run_lifecycle_operation` exécute les opérations de cycle de vie `EXEC`. Les deux sont contrôlés par le mode du serveur décrit ci-dessous.
- **Administration** : `server_info`, `list_registry`, `pull_provider` et `reload_credentials`.

La référence complète, avec les entrées, le comportement de contrôle et un exemple de prompt par outil, se trouve dans [Outils MCP](/mcp).

## Garde-fous {#guardrails}

Le mode du serveur fixe le contrat de sécurité pour toute la session :

| Mode | `SELECT` / métadonnées | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | autorisé | refusé | refusé |
| `safe` (par défaut) | autorisé | approbation requise | approbation requise |
| `delete_safe` | autorisé | autorisé | approbation requise |
| `full_access` | autorisé | autorisé | autorisé |

"Approbation requise" repose sur l'élicitation MCP : les clients qui la prennent en charge (Claude Desktop, Cursor, Continue) montrent à l'utilisateur le nom de l'outil, la classe de requête et le SQL, puis attendent une décision avant toute exécution ; les clients qui ne l'annoncent pas sont refusés. Chaque appel d'outil est écrit dans un journal d'audit, les écouteurs HTTP exigent un jeton bearer, et les identifiants proviennent de l'environnement ou d'un fichier dotenv sans que les valeurs secrètes soient jamais renvoyées au client. Les détails se trouvent dans [Modes du serveur](/command-line-usage/mcp#server-modes), [Authentification des clients HTTP](/command-line-usage/mcp#http-client-authentication) et [Journal d'audit](/command-line-usage/mcp#audit-log).

## Étapes suivantes {#next-steps}

- [Serveur MCP](/command-line-usage/mcp) - modes de déploiement, configuration, modes du serveur, journal d'audit et options.
- [Outils MCP](/mcp) - la référence complète des outils avec les entrées, le contrôle et des exemples de prompts.
- [MCP embarqué](/mcp/embedded) - exécuter le serveur dans votre propre application Go, Rust, Swift, Kotlin, .NET ou Gleam.
- [Query Library](pathname:///docs/query-library/) - du SQL prêt à exécuter pour les questions courantes d'exploitation cloud, par provider.
- [Référence IA](/ai) - concepts, guides pratiques, comparaisons et dépannage, rédigés pour les agents et les personnes qui les construisent.
