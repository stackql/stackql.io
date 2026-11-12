---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction ROUND dans StackQL : arrondir un argument numérique à un nombre de décimales donné, zéro par défaut."
image: "/img/stackql-featured-image.png"
---
Arrondit un argument numérique.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
Une expression de la catégorie des types de données numériques exacts ou approchés.

__*precision*__  
Le nombre de chiffres à droite du séparateur décimal auquel arrondir *numeric_expression*.

> Si l'argument *precision* est omis, sa valeur est supposée égale à 0.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une valeur numérique.

* * *

## Exemples {#examples}

### Arrondir un nombre à virgule flottante à deux décimales {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
