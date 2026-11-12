---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Insérer de nouvelles valeurs dans un document JSON sans écraser les données existantes avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Insère de nouvelles valeurs dans un document JSON aux chemins indiqués, sans écraser les valeurs existantes.

Voir aussi :  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Syntaxe {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*json_column*__  
La colonne ou l'expression JSON à modifier.

__*path*__  
L'expression de chemin JSON où la nouvelle valeur doit être insérée. Si le chemin existe déjà, la valeur d'origine à ce chemin n'est pas remplacée.

__*new_value*__  
La valeur à insérer au chemin indiqué dans le document JSON.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le document JSON avec la nouvelle valeur insérée si le chemin indiqué n'existe pas déjà ; sinon, le document JSON est renvoyé inchangé.

:::note Comportement si une clé existe

| Fonction                                                     | Écraser si la clé existe déjà ? | Créer si la clé n'existe pas ? |
|--------------------------------------------------------------|---------------------------------|--------------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Non                             | Oui                            |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Oui                             | Non                            |
| [`json_set()`](/language-spec/functions/json/json_set)       | Oui                             | Oui                            |

:::

* * *

## Exemples {#examples}

### Insérer une nouvelle paire clé-valeur dans un objet JSON {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT(json_data, '$.region', 'Europe') as updated_object;

/* expected output...
|-------------------------------------------------------------|
|                     updated_json                            |
|-------------------------------------------------------------|
| {"name":"ResourceOne", "type":"Storage", "region":"Europe"} |
|-------------------------------------------------------------|
*/
```

Cette requête insère une nouvelle clé nommée 'region' avec la valeur 'Europe' dans le document JSON si la clé 'region' n'existe pas déjà. Si 'region' est déjà une clé du document, le document d'origine est renvoyé sans modification.
