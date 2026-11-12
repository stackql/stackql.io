---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: Outil MCP qui recherche des modèles de requête StackQL sélectionnés, par intention en langage naturel, provider, service et tags
image: "/img/stackql-featured-image.png"
---

Recherche dans la [Query Library de StackQL](pathname:///docs/query-library/) les modèles correspondant à une intention exprimée en langage naturel. Consultez-la avant de composer du SQL à partir de zéro, puis passez l'`id` d'un résultat à [`query_library_get`](/mcp/query_library_get) pour inspecter le modèle et valider ses paramètres.

L'outil récupère uniquement le contenu de la Query Library. Il n'exécute pas de SQL, n'appelle pas d'API cloud et ne requiert pas d'identifiants de provider.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`intent`|Oui|Description de la tâche en langage naturel.|
|`provider`|Non|Filtrer par provider, par exemple `aws`.|
|`service`|Non|Filtrer par service, par exemple `s3`.|
|`tags`|Non|Tableau de tags. Une entrée doit correspondre à chaque tag fourni.|
|`include_mutations`|Non|Inclure les modèles de mutation. Valeur par défaut : `false` ; ignoré en mode `read_only`, où les entrées de mutation sont toujours exclues.|
|`limit`|Non|Nombre maximal de résultats. Valeur par défaut : `5` lorsqu'il est omis ou non positif ; plafonné à `20`.|
|`format`|Non|Rendu du texte : `markdown` ou `json`. S'il est omis, le paramètre de rendu du serveur est utilisé (`markdown` par défaut).|

* * *

## Sortie {#output}

La vue texte par défaut est un tableau classé de `id`, `title`, `mutation`, `required_params` et `score`, suivi de la citation de la source et, le cas échéant, de conseils en cas d'absence de correspondance. La charge utile structurée contient :

| Champ | Description |
|--|--|
|`hits`|Entrées classées avec `id`, `title`, `description` facultative, `providers`, `services` et `required_params`, ainsi que `mutation` et `score`. Poursuivez avec `query_library_get` pour obtenir les déclarations de paramètres complètes.|
|`miss`|`true` lorsqu'il n'y a aucun résultat ou que le meilleur score est inférieur au seuil de correspondance. Les correspondances les plus proches à faible score peuvent tout de même être renvoyées.|
|`guidance`|En cas d'absence de correspondance, oriente l'agent vers les instructions du serveur, les outils de découverte et `validate_select_query` pour rédiger une requête.|
|`source_tier`|Source du contenu : `primary`, `fallback` ou `snapshot`.|
|`source_url`|URL ou chemin embarqué du catalogue utilisé, pour la citation.|
|`stale`|Lorsque la valeur est `true`, le contenu a été servi depuis le cache ou l'instantané embarqué après un échec de récupération.|

Les entrées obsolètes sont exclues. Le serveur met le catalogue en cache et peut se rabattre de la Query Library hébergée sur sa source GitHub, puis sur un instantané embarqué.

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur, sous réserve de la liste d'autorisation d'outils configurée. Aucune approbation ni aucun identifiant de provider n'est nécessaire. La recherche de modèles de mutation n'autorise pas leur exécution.

* * *

## Exemple {#example}

Arguments de l'outil :

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

Utilisez un `id` issu de `hits`, par exemple `aws/s3/buckets-list`, avec [`query_library_get`](/mcp/query_library_get). Si `miss` vaut `true`, inspectez les modèles les plus proches pour vous guider ou utilisez [`list_methods`](/mcp/list_methods) et [`describe_method`](/mcp/describe_method) avant de rédiger du SQL.
