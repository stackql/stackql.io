---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction SUBSTRING dans StackQL (alias SUBSTR) : renvoyer une partie d'une chaîne de caractères ou d'une valeur binaire selon une position de départ et une longueur."
image: "/img/stackql-featured-image.png"
---
Renvoie une partie d'une expression de type caractère ou binaire. `SUBSTR` est un alias de `SUBSTRING`.

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Types de données ]](/language-spec/data-types)

* * * 

:::info

Pour extraire un champ d'un objet JSON, utilisez la fonction [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## Syntaxe {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expression*__  
Une chaîne de caractères ASCII ou un BLOB.

> Si *expression* est une chaîne, les indices de caractères désignent de véritables caractères UTF-8. Si *expression* est un BLOB, les indices désignent des octets.

__*start*__  
Un entier représentant le premier caractère ou octet de la sous-séquence de l'expression d'entrée à renvoyer. Si *start* est négatif, le premier caractère de la sous-chaîne est déterminé en comptant à partir de la droite plutôt que de la gauche.

> Le caractère le plus à gauche de *expression* porte le numéro 1.

__*length*__  
Une valeur entière facultative représentant la longueur de la sous-séquence à renvoyer après la valeur *start*. Si *length* est omis, tous les caractères jusqu'à la fin de la chaîne ou du BLOB désigné par *expression* sont renvoyés.

> Si *length* est négatif, la valeur absolue de *length* est utilisée.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une valeur de type chaîne ou BLOB selon le type de *expression*.

* * *

## Exemples {#examples}

### Renvoyer les *n* premiers caractères d'une colonne {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Tronquer les *n* premiers caractères d'une colonne {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```
### Renvoyer les *n* derniers caractères d'une colonne {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
