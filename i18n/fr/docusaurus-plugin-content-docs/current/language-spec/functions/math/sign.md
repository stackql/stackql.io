---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction SIGN dans StackQL : renvoyer -1, 0 ou 1 selon qu'un argument numérique est négatif, nul ou positif."
image: "/img/stackql-featured-image.png"
---
Renvoie -1 si la valeur d'entrée est négative, 0 si la valeur d'entrée est 0 et 1 si la valeur d'entrée est positive.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
Une expression de la catégorie des types de données numériques exacts ou approchés.

> Si *numeric_expression* vaut `NULL`, ou est une `string` ou un BLOB qui ne peut pas être converti en nombre sans perte, la fonction renvoie `NULL`.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie `-1`, `0` ou `1`.

* * *

## Exemples {#examples}

### Déterminer le signe d'un nombre {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
