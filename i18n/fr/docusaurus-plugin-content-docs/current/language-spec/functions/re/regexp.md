---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Utiliser des expressions régulières pour rechercher des motifs dans des chaînes de caractères avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Vérifie si la chaîne de caractères source correspond au motif d'expression régulière fourni.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*source_string*__  
La chaîne dans laquelle rechercher le motif.

__*pattern_expression*__  
Le motif d'expression régulière à comparer à la chaîne source.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une valeur booléenne : `TRUE` si la chaîne source correspond au motif d'expression régulière, sinon `FALSE`.

* * *

## Exemples {#examples}

### Vérifier si une chaîne contient des chiffres {#check-if-a-string-contains-numbers}

Cet exemple vérifie si la chaîne 'the year is 2021' contient un ou plusieurs caractères numériques :

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

Cette requête renvoie `TRUE`, car la chaîne contient la sous-chaîne '2021', qui correspond au motif `[0-9]+`, lequel désigne un ou plusieurs chiffres.

### Valider le format d'une adresse e-mail {#validate-email-format}

Pour vérifier si une chaîne a un format d'adresse e-mail valide :

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

Cette requête renvoie `TRUE`, ce qui indique que 'user@example.com' a le format d'une adresse e-mail classique et correspond au motif d'expression régulière indiqué.
