---
title: Opérateur de concaténation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "L'opérateur de concaténation || dans StackQL : réunir deux valeurs de chaîne de caractères ou plus en une seule chaîne."
image: "/img/stackql-featured-image.png"
---
Renvoie la chaîne de caractères résultant de la concaténation, ou réunion, de deux valeurs de chaîne ou plus.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*character_expression*__  
Une chaîne littérale ou une colonne de type chaîne.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une chaîne.

:::note

À partir de StackQL `v1.0`, l'analyseur syntaxique traite `||` comme l'opérateur de concaténation, avec une priorité plus élevée que celle des opérateurs de comparaison : `WHERE id = prefix || suffix` compare donc `id` à la chaîne concaténée. Les versions antérieures interprétaient `||` comme l'opérateur booléen `OR`. Utilisez `OR` pour la disjonction booléenne. Les [variables de session](/getting-started/session-variables) peuvent elles aussi être concaténées : `$prefix || '-' || $suffix`.

:::

* * *

## Exemples {#examples}

### Concaténer deux champs {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

Pour plus d'informations, consultez [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
