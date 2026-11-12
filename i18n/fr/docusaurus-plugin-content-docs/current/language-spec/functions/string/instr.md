---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction INSTR dans StackQL : la position, comptée à partir de 1, de la première occurrence d'une chaîne de caractères dans une autre, ou 0 si elle est introuvable."
image: "/img/stackql-featured-image.png"
---
Renvoie la position en caractères de la première occurrence d'une chaîne de caractères dans une autre chaîne, et renvoie 0 si la chaîne recherchée est introuvable.

Voir aussi :  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*string*__  
La chaîne source dans laquelle s'effectue la recherche.

__*searchstring*__  
La sous-chaîne recherchée.

> Si *string* et *searchstring* sont tous deux des BLOB, `instr(<string>, <searchstring>)` renvoie le nombre d'octets précédant la première occurrence de *searchstring*, plus un, ou 0 si *searchstring* n'apparaît nulle part dans *string*. Si les deux arguments ne sont ni `NULL` ni des BLOB, ils sont tous deux interprétés comme des chaînes. 

## Valeur(s) renvoyée(s) {#return-values}
Renvoie un entier.

> Si l'une des valeurs *string* ou *searchstring* est `NULL`, le résultat est `NULL`.

* * *

## Exemples {#examples}

### Rechercher une chaîne dans un champ {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurence of the term 'worker' in the 'name' column
*/
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
