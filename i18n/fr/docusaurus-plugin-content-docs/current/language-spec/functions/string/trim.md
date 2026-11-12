---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction TRIM dans StackQL : supprimer les espaces blancs, ou un ensemble de caractères donné, aux deux extrémités d'une chaîne de caractères."
image: "/img/stackql-featured-image.png"
---
Renvoie une chaîne de caractères formée en supprimant les espaces blancs, ou tout caractère indiqué, à gauche et à droite d'une chaîne d'entrée.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*character_expression*__  
Une chaîne littérale ou une colonne de type chaîne.

__*character*__  
Un caractère facultatif à supprimer s'il se trouve au début ou à la fin de *character_expression*.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une chaîne.

* * *

## Exemples {#examples}

### Supprimer les espaces blancs de début et de fin d'une colonne de type chaîne {#remove-leading-and-trailing-whitespace-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
