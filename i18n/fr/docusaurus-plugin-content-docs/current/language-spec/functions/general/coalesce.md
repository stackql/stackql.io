---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction COALESCE dans StackQL : renvoyer le premier argument non NULL parmi deux ou plus, généralement pour substituer une valeur par défaut à une valeur manquante."
image: "/img/stackql-featured-image.png"
---
Renvoie le premier argument qui n'est pas `NULL` dans une série de deux arguments ou plus ; peut servir à substituer une valeur par défaut à une valeur `NULL`.  

> La fonction `COALESCE` est équivalente à la fonction `NVL` d'Oracle ou à la fonction `IFNULL` de MySQL.

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Syntaxe {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expression*__  
Deux expressions ou plus, de n'importe quel type de données, dont l'évaluation donne une valeur `NULL` ou non `NULL`.

> Si tous les arguments *expression* sont `NULL`, la fonction `COALESCE` renvoie `NULL`.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie la première valeur non `NULL` de la série d'arguments *expression*.

* * *

## Exemples {#examples}

### Remplacer une valeur `NULL` par une valeur par défaut {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
