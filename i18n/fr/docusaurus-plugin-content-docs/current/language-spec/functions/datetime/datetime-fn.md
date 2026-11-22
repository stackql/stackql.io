---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction DATETIME dans StackQL : calculer une valeur de date et d'heure à partir d'une entrée et de zéro ou plusieurs modificateurs, renvoyée au format YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Renvoie une valeur de date et d'heure calculée à partir d'une date et heure en entrée et de zéro ou plusieurs modificateurs.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Modificateurs de date ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*datetime_expression*__  
La valeur de date et d'heure.

__*modifier*__  
Zéro ou plusieurs modificateurs à appliquer à *datetime_expression*.  Pour plus d'informations sur les modificateurs, consultez [**Modificateurs de date**](/language-spec/functions/datetime/date_modifiers)

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une chaîne de caractères représentant la date et l'heure de l'entrée *datetime_expression* après application de *modifier*.

* * *

## Exemples {#examples}

### Convertir une heure UTC en heure locale avec le modificateur `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convertir une heure locale en UTC {#convert-datetime-to-utc}

Le modificateur `utc` traite son entrée comme une heure locale de la machine qui exécute StackQL et la convertit en UTC.  Une valeur déjà exprimée en UTC, comme `'now'` ou un horodatage avec le suffixe `Z`, est renvoyée inchangée.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

Sur une machine dont le fuseau horaire est UTC+11, cette requête renvoie `2026-11-20 06:59:24`.

### Calculer une date par rapport à une autre date {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Obtenir le jour de la semaine spécifié à une date donnée ou après {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Obtenir le début du mois pour une date donnée {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convertir un horodatage Unix en date et heure {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Pour convertir une date et heure en horodatage epoch Unix, consultez [**STRFTIME**](/language-spec/functions/datetime/strftime).  Pour calculer la différence entre deux objets de date et d'heure, consultez [**STRFTIME**](/language-spec/functions/datetime/strftime) ou [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

Pour plus d'informations, consultez [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
