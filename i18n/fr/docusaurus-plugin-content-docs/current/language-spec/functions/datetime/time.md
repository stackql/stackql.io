---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction TIME dans StackQL : renvoyer une heure sous forme de chaîne HH:MM:SS à partir d'une valeur temporelle et de modificateurs facultatifs."
image: "/img/stackql-featured-image.png"
---
Renvoie une heure sous forme de chaîne de caractères au format HH:MM:SS.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Modificateurs de date ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*datetime_expression*__  
La valeur de date et d'heure.

__*modifier*__  
Zéro ou plusieurs modificateurs à appliquer à *datetime_expression*.  Pour plus d'informations sur les modificateurs, consultez [**Modificateurs de date**](/language-spec/functions/datetime/date_modifiers)

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une chaîne représentant la composante heure de l'entrée *datetime_expression* après application de *modifier*.

* * *

## Exemples {#examples}

### Renvoyer la composante heure d'une valeur de date et d'heure {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Renvoyer la composante heure locale d'une valeur de date et d'heure {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
