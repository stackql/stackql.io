---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction JSON_TYPE dans StackQL : type (object, array, text, integer, real, true, false ou null) de l'élément le plus externe d'une valeur JSON ou d'un chemin de celle-ci."
image: "/img/stackql-featured-image.png"
---
Renvoie le type de données de l'élément le plus externe d'un objet ou d'un tableau JSON fourni en entrée et d'un chemin.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Types de données ]](/language-spec/data-types)

* * * 

:::tip 

Utilisez la fonction [**DESCRIBE**](/language-spec/describe) pour repérer les types de données `array` ou `object` utilisables avec les fonctions JSON de StackQL.

:::

:::note 

La fonction `json_type` est implémentée en tant que fonction `json_typeof` si vous utilisez stackql avec un backend SQL `postgres`

:::

## Syntaxe {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*json_array_or_object*__  
Un tableau ou un objet JSON.

__*json_path*__  
Le chemin JSON vers un tableau ou un objet contenu dans *json_array_or_object*.  Consultez [jsonpath.com](https://jsonpath.com/).

> Si *json_path* n'existe pas dans *json_array_or_object*, la fonction renvoie `NULL`.

> La fonction `json_type()` lève une erreur si l'un de ses arguments n'est pas bien formé.

## Valeur(s) renvoyée(s) {#return-values}
Une chaîne de caractères représentant le type de données résolu.

> Les types de données comprennent `null`, `true`, `false`, `integer`, `real`, `text`, `array` ou `object`.

* * *

## Exemples {#examples}

### Renvoyer le type de données d'un champ imbriqué dans un objet ou un tableau JSON {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

Pour plus d'informations, consultez [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
