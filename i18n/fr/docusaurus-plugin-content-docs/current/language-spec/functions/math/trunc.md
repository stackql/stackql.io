---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction TRUNC dans StackQL : la partie entière d'un nombre, arrondie vers zéro en écartant toute partie fractionnaire."
image: "/img/stackql-featured-image.png"
---

Renvoie la partie entière d'un nombre en arrondissant vers zéro, ce qui supprime toute composante fractionnaire.

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
Une expression de la catégorie des types de données numériques exacts ou approchés.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une valeur numérique tronquée à sa composante entière.

* * *

## Exemples {#examples}

### Tronquer à deux décimales le nombre de jours écoulés depuis la création de buckets Google Cloud Storage {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-two-decimal-places}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
