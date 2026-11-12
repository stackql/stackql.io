---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction REPLACE dans StackQL : remplacer chaque occurrence d'une sous-chaîne dans une chaîne de caractères par une autre chaîne."
image: "/img/stackql-featured-image.png"
---
Renvoie une chaîne de caractères formée en substituant une portion de chaîne à chaque occurrence d'une autre chaîne.

Voir aussi :  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*character_expression*__  
Une chaîne littérale ou une colonne de type chaîne sur laquelle effectuer le remplacement.

__*pattern*__  
La sous-chaîne à rechercher dans la chaîne d'origine.

> Si *pattern* est une chaîne vide ou est introuvable dans *character_expression*, *character_expression* est renvoyé tel quel. 

__*replacement*__  
La chaîne de remplacement.

> Si *replacement* n'est pas une chaîne à l'origine, il est converti en chaîne UTF-8 avant le traitement.

> La séquence de collation `BINARY` est utilisée pour les comparaisons.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une chaîne.

* * *

## Exemples {#examples}

### Remplacer un motif de chaîne dans une colonne {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
