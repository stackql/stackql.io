---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Comparer deux chaînes de caractères JSON pour vérifier leur équivalence à l'aide de SQL.
image: "/img/stackql-featured-image.png"
---
Compare deux chaînes de caractères JSON et renvoie true si elles sont équivalentes, false sinon.

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Types de données ]](/language-spec/data-types)

* * * 

:::tip 

La fonction `json_equal` est une fonction d'extension personnalisée de StackQL. Elle permet de comparer des objets et des tableaux JSON, en considérant comme équivalents, conformément à la spécification JSON, des objets dont les clés sont dans un ordre différent. La fonction `json_equal` est disponible dans les versions `0.5.707` et ultérieures de StackQL et n'est prise en charge qu'avec le backend SQL par défaut de StackQL, embarqué et en mémoire.

:::

## Syntaxe {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Arguments {#arguments}

__*json1*__  
Une chaîne JSON représentant un objet ou un tableau.

__*json2*__  
Une autre chaîne JSON à comparer à la première.

> Les deux arguments doivent être des chaînes JSON valides.

## Valeur(s) renvoyée(s) {#return-values}
Une valeur entière : 1 si les chaînes JSON sont équivalentes, 0 sinon.

* * *

## Exemples {#examples}

### Comparer des objets JSON {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### Comparer des tableaux JSON {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

Pour plus d'informations, consultez [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
