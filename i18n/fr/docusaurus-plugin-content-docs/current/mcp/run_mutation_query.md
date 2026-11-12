---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: Outil MCP qui exécute INSERT, UPDATE, REPLACE ou DELETE sur un provider via StackQL
image: "/img/stackql-featured-image.png"
---

Exécute `INSERT`, `UPDATE`, `REPLACE` ou `DELETE` sur le provider. **Effets de bord réels** - un appel réussi modifie l'état du cloud.

Contrôlé par le [mode](/command-line-usage/mcp#server-modes) du serveur :

| Mode | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|refusé|refusé|
|`safe` (valeur par défaut)|approbation requise (élicitation)|approbation requise|
|`delete_safe`|autorisé|approbation requise|
|`full_access`|autorisé|autorisé|

<br/>

:::info

Une charge utile contenant plusieurs instructions est contrôlée par son instruction la plus privilégiée

:::

Lorsqu'une mutation nécessite une approbation et que le client n'a pas annoncé la capacité d'élicitation MCP, l'appel est refusé.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT dans la spécification du langage ]](/language-spec/insert) [[ DELETE dans la spécification du langage ]](/language-spec/delete)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`sql`|Oui|Une instruction `INSERT`, `UPDATE`, `REPLACE` ou `DELETE`.|
|`source`|Non|Identifiant de l'entrée de la Query Library lorsque le SQL provient de [`query_library_get`](/mcp/query_library_get). Attribution uniquement ; ne modifie ni l'exécution ni les approbations.|
|`format`|Non|Rendu du texte : `markdown` ou `json`. S'il est omis, le paramètre de rendu du serveur est utilisé.|

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| KV | `{messages: [...], timestamp}` - confirmation d'envoi, pas le corps de la réponse de l'API |

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

Le client MCP affiche une demande d'approbation avant l'envoi de la suppression, sauf si le serveur s'exécute en mode `full_access`.

La séquence suivante montre le flux d'élicitation lorsque le serveur MCP StackQL s'exécute dans le mode `safe` par défaut - le client est invité à approuver le `DELETE` avant l'envoi de toute modification côté cloud. Vous pouvez reproduire cela sur votre propre serveur MCP StackQL avec l'outil officiel [MCP Inspector](https://github.com/modelcontextprotocol/inspector), à l'aide de `npx @modelcontextprotocol/inspector` :


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
