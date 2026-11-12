---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Mettre à jour ou insérer des valeurs dans un document JSON avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Met à jour des valeurs existantes ou insère de nouvelles valeurs dans un document JSON aux chemins indiqués.

Voir aussi :  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Syntaxe {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*json_column*__  
La colonne ou l'expression JSON à modifier.

__*path*__  
L'expression de chemin JSON indiquant où définir la valeur. Si le chemin existe, la valeur est mise à jour ; si le chemin n'existe pas, une nouvelle paire clé-valeur est ajoutée.

__*value_to_set*__  
La valeur à définir au chemin indiqué dans le document JSON.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie un document JSON avec la valeur définie au chemin indiqué. Si le chemin existe, la valeur existante est mise à jour ; sinon, une nouvelle paire clé-valeur est ajoutée.

:::note Comportement si une clé existe

| Fonction                                                     | Écraser si la clé existe déjà ? | Créer si la clé n'existe pas ? |
|--------------------------------------------------------------|---------------------------------|--------------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Non                             | Oui                            |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Oui                             | Non                            |
| [`json_set()`](/language-spec/functions/json/json_set)       | Oui                             | Oui                            |

:::

* * *

## Exemples {#examples}

### Mettre à jour une valeur existante dans un objet JSON {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET(json_data, '$.status', 'active') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne", "status":"active", "region":"US"} |
-- |---------------------------------------------------------|
```

Cette requête met à jour la clé 'status' avec la valeur 'active' dans le document JSON si la clé 'status' existe. Si 'status' n'existe pas dans le document, elle est ajoutée avec la valeur 'active'.

### Ajouter une nouvelle paire clé-valeur à un objet JSON {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with the current date to a JSON object
SELECT JSON_SET(json_data, '$.lastChecked', '2024-04-29') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne", "status":"pending", "lastChecked":"2024-04-29"} |
|-----------------------------------------------------------------------|
*/
```

Cette requête ajoute une nouvelle clé 'lastChecked' avec la valeur '2024-04-29' au document JSON si 'lastChecked' n'existe pas déjà. Si elle existe, la valeur existante est mise à jour avec '2024-04-29'.
