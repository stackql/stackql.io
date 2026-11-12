---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: Outil MCP qui analyse et planifie un SELECT StackQL sans l'exécuter
image: "/img/stackql-featured-image.png"
---

Analyse et planifie une seule instruction `SELECT` sans l'exécuter. Renvoie `{valid, errors}`. Utile lorsqu'un agent veut vérifier que sa requête se compile et se lie à la méthode découverte avant de s'engager dans un véritable appel d'API.

:::info

`SELECT` uniquement, et exactement une instruction ; une charge utile contenant plus d'une instruction est refusée avec `validate_select_query accepts exactly one statement`.

:::

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`sql`|Oui|Une seule instruction `SELECT`.|

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| KV | `valid` (booléen) et `errors` (tableau de chaînes de caractères, vide lorsque la requête est valide) |

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur. Aucun appel d'API n'est effectué.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
