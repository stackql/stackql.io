---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: Outil MCP qui exécute un SELECT StackQL sur un provider
image: "/img/stackql-featured-image.png"
---

Exécute une instruction `SELECT` sur les providers configurés et renvoie les lignes de résultat. En lecture seule - les mutations et les opérations de cycle de vie ne sont pas acceptées ; utilisez pour cela [`run_mutation_query`](/mcp/run_mutation_query) ou [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).

Les lignes de résultat sont renvoyées au client mais sont délibérément exclues du journal d'audit.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT dans la spécification du langage ]](/language-spec/select)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`sql`|Oui|Une instruction `SELECT`. Les instructions de métadonnées (`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`) sont classées en lecture seule par le contrôle de politique et sont également acceptées ; `DESC` est reconnu dans les versions de StackQL à partir de `v0.12.732`.|
|`row_limit`|Non|Plafond du nombre de lignes renvoyées au client. `0` (valeur par défaut) signifie aucun plafond côté client.|
|`source`|Non|Identifiant de l'entrée de la Query Library lorsque le SQL provient de [`query_library_get`](/mcp/query_library_get). Attribution uniquement ; ne modifie ni l'exécution ni les approbations.|
|`format`|Non|Rendu du texte : `markdown` ou `json`. S'il est omis, le paramètre de rendu du serveur est utilisé.|

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| Tableau | `{rows: [...]}` - un objet par ligne, indexé par nom de colonne |

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur (`read_only`, `safe`, `delete_safe`, `full_access`) lorsque toutes les instructions de la charge utile sont en lecture seule. À partir de `v1.0`, `INSERT`, `UPDATE`, `REPLACE`, `DELETE` et `EXEC` sont refusés par cet outil dans tous les modes, y compris `full_access`, avec une erreur qui nomme [`run_mutation_query`](/mcp/run_mutation_query) ou [`run_lifecycle_operation`](/mcp/run_lifecycle_operation). Une charge utile contenant plusieurs instructions est classée selon son instruction la plus privilégiée, de sorte qu'une mutation placée derrière un `SELECT` initial est refusée de la même manière. L'outil déclare `destructiveHint: false` et `openWorldHint: true`.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
