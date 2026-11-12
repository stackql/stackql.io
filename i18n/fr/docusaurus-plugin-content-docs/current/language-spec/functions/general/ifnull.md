---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction IFNULL dans StackQL : renvoyer le premier argument non NULL parmi exactement deux, une forme à deux arguments de COALESCE."
image: "/img/stackql-featured-image.png"
---
Accepte exactement deux arguments et renvoie le premier argument non `NULL` ; peut servir à substituer une valeur par défaut à une valeur `NULL`.  

> La fonction `IFNULL` est équivalente à la fonction [`COALESCE`](/language-spec/functions/general/coalesce) avec deux arguments seulement ; la fonction [`COALESCE`](/language-spec/functions/general/coalesce) peut avoir plus de deux arguments.

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Syntaxe {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expression*__  
Deux expressions de n'importe quel type de données, dont l'évaluation donne une valeur `NULL` ou non `NULL`.

> Si les deux arguments *expression* sont `NULL`, la fonction `IFNULL` renvoie `NULL`.


## Valeur(s) renvoyée(s) {#return-values}

Renvoie la première valeur non `NULL` des deux arguments *expression*.

* * *

## Exemples {#examples}

### Remplacer une valeur `NULL` par une valeur par défaut {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
