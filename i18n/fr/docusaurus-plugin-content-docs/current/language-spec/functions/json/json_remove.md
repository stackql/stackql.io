---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Supprimer des éléments d'un document JSON en indiquant des chemins avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Supprime les éléments indiqués d'un document JSON à l'aide d'expressions de chemin. Si un chemin indiqué n'existe pas dans le document, il est ignoré. Le traitement séquentiel des chemins peut modifier la structure pour les suppressions suivantes.

Voir aussi :  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Syntaxe {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*json_document*__  
Le document JSON dont des éléments doivent être supprimés.

__*path1, path2, ...*__  
Une ou plusieurs expressions de chemin désignant les éléments du document JSON à supprimer.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une copie du document JSON sans les éléments indiqués. Si aucun chemin n'est fourni, renvoie le JSON d'entrée reformaté, sans les espaces blancs superflus.

* * *

## Exemples {#examples}

### Supprimer un élément d'un tableau JSON {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Sortie : `'[0,1,3,4]'`

### Supprimer plusieurs éléments d'un tableau JSON {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Sortie : `'[1,3,4]'`

### Supprimer le dernier élément à l'aide d'un index négatif {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Sortie : `'[1,2,3]'`

### Supprimer un chemin inexistant {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Sortie : `'{"x":25,"y":42}'`

### Supprimer une clé existante d'un objet JSON {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Sortie : `'{"x":25}'`

### Cas limite : suppression avec le chemin racine {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Sortie : `NULL`

Ces exemples montrent comment `json_remove()` permet de manipuler dynamiquement des données JSON, dans des scénarios variés allant de simples suppressions à la gestion de cas limites dans la modification de structures de données.
