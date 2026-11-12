---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction LTRIM dans StackQL : supprimer les espaces blancs, ou un ensemble de caractères donné, à gauche d'une chaîne de caractères."
image: "/img/stackql-featured-image.png"
---
Renvoie une chaîne de caractères formée en supprimant les espaces blancs, ou tout caractère indiqué, à gauche d'une chaîne d'entrée.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*character_expression*__  
Une chaîne littérale ou une colonne de type chaîne.

__*character*__  
Un caractère facultatif à supprimer s'il se trouve au début de *character_expression*.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une chaîne.

* * *

## Exemples {#examples}

### Supprimer les espaces blancs de début d'une colonne de type chaîne {#remove-leading-whitespace-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
