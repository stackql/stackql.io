---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: Outil MCP qui liste les ressources d'un provider.service StackQL
image: "/img/stackql-featured-image.png"
---

Renvoie les ressources d'un `provider.service` - le troisième niveau de la [hiérarchie des ressources](/getting-started/resource-hierarchy) de StackQL.  Les ressources sont les cibles qui apparaissent dans la clause `FROM` d'une requête StackQL.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`provider`|Oui|Nom du provider.|
|`service`|Oui|Service de ce provider.|

<br/>

:::info

Les identifiants ne peuvent pas contenir d'espaces blancs, de points-virgules ni de barres obliques inverses ; une telle entrée est refusée avant la construction de toute instruction.

:::

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| Tableau | Une ligne par ressource de `provider.service` |

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur.  En lecture seule.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
List the resources in the google.storage service
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
