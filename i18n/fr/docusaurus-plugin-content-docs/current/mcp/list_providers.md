---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: Outil MCP qui liste les providers (cloud et SaaS) dont dispose le serveur MCP StackQL
image: "/img/stackql-featured-image.png"
---

Renvoie les providers déjà téléchargés dans le cache local de StackQL - le sommet de la [hiérarchie des ressources](/getting-started/resource-hierarchy) de StackQL.  Utilisez cet outil comme point d'entrée lorsqu'un agent doit découvrir ce qu'il peut interroger.

Pour les providers disponibles dans le *registre* (y compris ceux qui ne sont pas encore téléchargés), consultez [`list_registry`](/mcp/list_registry).

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## Entrées {#inputs}

Aucune.

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| Tableau (tableau markdown pour le LLM) | Une ligne par provider |

<br />

Chaque ligne contient le nom du provider et les métadonnées de version telles que publiées par le registre.

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur (`read_only`, `safe`, `delete_safe`, `full_access`).  En lecture seule.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
Which cloud providers are available for you to query right now?
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
