---
title: Types de données
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Types de données StackQL : les types des propriétés et paramètres de ressources dans les requêtes et les résultats, et leur correspondance avec les schémas d'API des providers."
image: "/img/stackql-featured-image.png"
---

Les types de données StackQL servent à représenter les valeurs des champs d'entrée ou de sortie (propriétés ou paramètres) utilisés dans les ressources cloud.  Les types de données StackQL sont répertoriés dans le tableau ci-dessous :

| Type StackQL | Description | Exemple |
|--|--|--|
| `string` | séquence de caractères Unicode placée entre guillemets simples | `'mybucket'` |
| `array` | collection ordonnée de valeurs, placées entre crochets `[ ]` et séparées par une virgule | `[0,1,2]` |
| `object` | ensemble non ordonné de zéro ou plusieurs paires nom/valeur placées entre `{ }` et séparées par une virgule | `{"name": "mydb"}` |
| `boolean` | `true` ou `false`, les valeurs booléennes ne sont pas placées entre guillemets, sans quoi elles sont traitées comme des chaînes de caractères | `true` |
| `integer` | un nombre entier qui peut être positif, négatif ou nul | `2` |
| `double` | nombre à virgule flottante en double précision, peut être positif ou négatif | `0.95` |
<br/>
<br/>
> Les types de données StackQL correspondent aux types de données exposés par les API de tous les principaux fournisseurs cloud.

## Conversion de type {#type-conversion}

`CAST(expression AS type)` et `CONVERT(expression, type)` convertissent une valeur en un autre type.  En plus des types de conversion hérités de la grammaire MySQL (`CHAR`, `DATE`, `DATETIME`, `DECIMAL`, `SIGNED`, `UNSIGNED`, `TIME`, `JSON`), StackQL `v1.0` et les versions ultérieures acceptent `TEXT`, `INTEGER`, `INT`, `BIGINT`, `BOOLEAN` et `BOOL`.  La conversion est effectuée par le backend SQL, sa sémantique est donc celle de SQLite ou de PostgreSQL.  Les conversions servent couramment à comparer une [variable de session](/getting-started/session-variables) typée avec une colonne de type chaîne :

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

Les champs booléens servis par des providers XML tels qu'AWS sont renvoyés sous la forme `true` et `false` ; les versions de StackQL antérieures à `v1.0` les renvoyaient sous la forme `0` et `1`.
