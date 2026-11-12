---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction ABS dans StackQL : la valeur absolue d'un argument numérique."
image: "/img/stackql-featured-image.png"
---
Renvoie la valeur absolue de l'argument numérique.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `abs(X)` renvoie `NULL` si `X` vaut `NULL`. `abs(X)` renvoie `0.0` si `X` est une chaîne de caractères ou un blob qui ne peut pas être converti en valeur numérique. Si `X` est l'entier -9223372036854775808, `abs(X)` lève une erreur de dépassement d'entier, car il n'existe pas de valeur positive équivalente en complément à deux sur 64 bits.

## Arguments {#arguments}

__*numeric_expression*__  
Une expression de la catégorie des types de données numériques exacts ou approchés.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une valeur numérique positive.

* * *

## Exemples {#examples}

### Renvoyer la valeur absolue d'un nombre {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_mathfunc.html](https://www.sqlite.org/lang_mathfunc.html)
