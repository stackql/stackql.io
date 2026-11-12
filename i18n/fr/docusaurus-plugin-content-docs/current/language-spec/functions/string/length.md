---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction LENGTH dans StackQL : le nombre de caractères d'une chaîne, ou le nombre d'octets d'un BLOB."
image: "/img/stackql-featured-image.png"
---
Renvoie le nombre de caractères d'une expression d'entrée de type chaîne de caractères avant le premier caractère `NULL`, ou le nombre d'octets d'une expression BLOB.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

Pour obtenir la longueur d'un tableau JSON, utilisez la fonction [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## Syntaxe {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expression*__  
Une chaîne ASCII ou un BLOB.

> Renvoie `NULL` si *expression* est `NULL`.

> Si *expression* est numérique, la longueur d'une représentation de *expression* sous forme de chaîne est renvoyée.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie un entier.

* * *

## Exemples {#examples}

### Renvoyer la longueur d'une colonne de type chaîne {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
