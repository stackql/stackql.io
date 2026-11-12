---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction LOWER dans StackQL : convertir une chaîne de caractères en minuscules."
image: "/img/stackql-featured-image.png"
---
Renvoie la chaîne de caractères d'entrée en minuscules.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ Types de données ]](/language-spec/data-types)

* * * 

## Syntaxe {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> La fonction `lower()` ne prend en charge que les caractères ASCII.

## Arguments {#arguments}

__*character_expression*__  
Une chaîne ASCII.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une valeur de type chaîne.

* * *

## Exemples {#examples}

### Renvoyer une chaîne en minuscules {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
