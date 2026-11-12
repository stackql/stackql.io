---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Appliquer un JSON MergePatch à un document JSON avec SQL dans StackQL, pour modifier ou fusionner des objets JSON.
image: "/img/stackql-featured-image.png"
---
Applique un JSON MergePatch (RFC-7396) à un document JSON et le modifie selon les opérations indiquées. Cette fonction sert à ajouter, modifier ou supprimer des éléments dans des objets JSON, mais traite les tableaux JSON comme des unités atomiques.

Voir aussi :  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Syntaxe {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*target_json*__  
Le document JSON auquel appliquer le patch.

__*patch_json*__  
Un document JSON décrivant les modifications à appliquer selon l'algorithme MergePatch.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le document JSON modifié une fois le patch appliqué.

* * *

## Exemples {#examples}

### MergePatch simple {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Sortie : `{"a":1,"b":2,"c":3,"d":4}`

### Remplacer un élément dans un tableau {#replace-element-in-array}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Sortie : `{"a":9,"b":2}`

### Supprimer un élément à l'aide de null {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Sortie : `{"a":9,"c":8}`

### MergePatch imbriqué {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Sortie : `{"a":{"x":1,"y":9},"b":3,"c":8}`

Cette fonctionnalité permet des mises à jour souples et dynamiques de données JSON stockées dans une base de données, ce qui est particulièrement utile pour les applications qui nécessitent des modifications conditionnelles de structures de données.
