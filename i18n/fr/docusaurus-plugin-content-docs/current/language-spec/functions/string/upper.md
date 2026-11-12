---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction UPPER dans StackQL : convertir une chaîne de caractères en majuscules."
image: "/img/stackql-featured-image.png"
---
Renvoie la chaîne de caractères d'entrée en majuscules.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ Types de données ]](/language-spec/data-types)

* * * 

## Syntaxe {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> La fonction `upper()` ne prend en charge que les caractères ASCII.

## Arguments {#arguments}

__*character_expression*__  
Une chaîne ASCII.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une valeur de type chaîne.

* * *

## Exemples {#examples}

### Renvoyer une chaîne en majuscules {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
