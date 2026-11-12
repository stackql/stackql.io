---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: Outil MCP qui récupère un modèle de requête StackQL et valide les paramètres fournis pour renvoyer le SQL rendu sans l'exécuter
image: "/img/stackql-featured-image.png"
---

Récupère une entrée de la [Query Library](pathname:///docs/query-library/) à partir d'un `id` renvoyé par [`query_library_search`](/mcp/query_library_search).

Lorsque `params` est omis ou vide, il renvoie le modèle brut, les déclarations de paramètres et les notes. Avec un objet `params` non vide, le serveur valide les valeurs et les substitue dans le modèle. Il ne renvoie du SQL que si le rendu réussit. Le modèle de langage n'a pas besoin de substituer lui-même les valeurs.

Cet outil n'exécute pas de SQL et ne vérifie pas les identifiants cloud. Sa validation des paramètres est distincte de [`validate_select_query`](/mcp/validate_select_query), qui analyse et planifie un `SELECT` sur le provider installé.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`id`|Oui|ID de requête issu des résultats de recherche, par exemple `aws/s3/buckets-list`.|
|`params`|Non|Objet associant les noms des paramètres déclarés à des valeurs. Omettez-le ou passez `{}` pour inspecter le modèle sans rendu.|
|`format`|Non|Rendu du texte : `markdown` ou `json`. S'il est omis, le paramètre de rendu du serveur est utilisé (`markdown` par défaut).|

Les paramètres inconnus sont rejetés. Les paramètres obligatoires sans valeur fournie ni valeur par défaut sont signalés dans `missing_params`. Les valeurs fournies sont vérifiées par rapport aux types et aux contraintes déclarés ; les espaces réservés non résolus sont des erreurs.

* * *

## Sortie {#output}

La vue texte par défaut est un bloc KV avec l'identité, le statut de mutation, l'outil suivant, la validité, les liens vers la source et, le cas échéant, le modèle, le SQL rendu ou les erreurs. Utilisez la charge utile structurée ou `format: "json"` pour obtenir les métadonnées complètes :

| Champ | Description |
|--|--|
|`id`, `title`, `description`|Identité et résumé de l'entrée.|
|`mutation`, `verb`|Indique si l'entrée modifie des ressources et, lorsqu'il est déclaré, son type d'opération (`select`, `mutation` ou `lifecycle`).|
|`next_tool`|Outil d'exécution de l'entrée : `run_select_query`, `run_mutation_query` ou `run_lifecycle_operation`. La récupération ne l'appelle pas.|
|`params`|Déclarations de paramètres avec les noms, les types, le caractère obligatoire et, le cas échéant, les valeurs par défaut, descriptions, exemples, énumérations ou motifs.|
|`template`, `notes`|Modèle brut et notes de l'auteur, renvoyés lorsque `params` est omis ou vide.|
|`rendered`, `valid`|Indiquent si le rendu a été tenté et s'il a réussi. Les deux valent `false` lors de l'inspection du modèle brut.|
|`sql`|SQL rendu, présent uniquement après un rendu réussi.|
|`missing_params`, `unknown_params`, `errors`|Détails de validation lorsque le rendu échoue. N'exécutez pas le SQL tant que ces points ne sont pas résolus.|
|`auth`, `permissions`|Lorsqu'elles sont déclarées, les variables d'environnement d'identifiants et les permissions natives du provider nécessaires à l'exécution. La récupération elle-même ne requiert ni les unes ni les autres.|
|`cost`, `outputs`, `related`|Lorsqu'ils sont déclarés, les indications de fan-out et de coût, les descriptions des colonnes de résultat et les ID des entrées associées.|
|`doc_url`|Page de la Query Library lisible par un humain.|
|`source_tier`, `source_url`, `stale`|Provenance du contenu : `primary`, `fallback` ou `snapshot`, son URL ou son chemin embarqué, et si le contenu a été servi depuis le cache ou l'instantané après un échec de récupération.|

Un ID inconnu ou mal formé renvoie une erreur. Utilisez `query_library_search` pour trouver des ID valides.

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur, sous réserve de la liste d'autorisation d'outils configurée. La récupération ou le rendu d'un modèle de mutation ou de cycle de vie n'a aucun effet côté cloud et ne nécessite aucune approbation. L'exécution du SQL renvoyé est un appel distinct, régi par le [mode](/command-line-usage/mcp#server-modes) du serveur.

* * *

## Exemple {#example}

Inspectez d'abord une entrée :

```json
{
  "id": "aws/s3/buckets-list"
}
```

Fournissez ensuite sa région de routage obligatoire :

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

Après avoir vérifié `rendered` et `valid`, validez le `SELECT` renvoyé avec [`validate_select_query`](/mcp/validate_select_query). Exécutez-le avec l'outil indiqué dans `next_tool`, en passant le `sql` renvoyé et en définissant `source` sur l'`id` de l'entrée de la Query Library à des fins d'attribution. Les identifiants et les permissions du provider sont requis au moment de l'exécution.
