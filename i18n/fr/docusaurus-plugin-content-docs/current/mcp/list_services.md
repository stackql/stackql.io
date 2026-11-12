---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: Outil MCP qui liste les services d'un provider StackQL
image: "/img/stackql-featured-image.png"
---

Renvoie les services exposés par un seul provider - le deuxième niveau de la [hiérarchie des ressources](/getting-started/resource-hierarchy) de StackQL.  Appelez cet outil après [`list_providers`](/mcp/list_providers) pour restreindre le périmètre avant d'explorer les ressources.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`provider`|Oui|Nom du provider (par exemple `google`, `aws`, `azure`, `github`).|

<br/>

:::info

Les identifiants ne peuvent pas contenir d'espaces blancs, de points-virgules ni de barres obliques inverses ; une telle entrée est refusée avant la construction de toute instruction.

:::

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| Tableau | Une ligne par service du provider indiqué |

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur.  En lecture seule.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
List the services available in Google Cloud
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
