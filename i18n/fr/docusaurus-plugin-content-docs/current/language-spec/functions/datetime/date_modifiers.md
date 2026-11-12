---
title: Modificateurs de date
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Modificateurs de date et d'heure dans StackQL : décalage en jours, mois ou années, début de période, jour de semaine, etc., pour DATE, TIME, DATETIME, JULIANDAY et STRFTIME."
image: "/img/stackql-featured-image.png"
---

## Vue d'ensemble {#overview}

Dans les fonctions de date et d'heure de StackQL, les valeurs de date ou d'heure peuvent être suivies de zéro ou plusieurs modificateurs qui modifient la date et/ou l'heure. Chaque modificateur est une transformation appliquée à la valeur temporelle située à sa gauche. Les modificateurs sont appliqués dans l'ordre, de gauche à droite. Les modificateurs disponibles sont les suivants :

| Modificateur         | Description                                                                          |
|----------------------|--------------------------------------------------------------------------------------|
| NNN day(s)           | Ajoute ±NNN jours à une date et une heure                                            |
| NNN hour(s)          | Ajoute ±NNN heures à une date et une heure                                           |
| NNN minute(s)        | Ajoute ±NNN minutes à une date et une heure                                          |
| NNN[.NNNN] second(s) | Ajoute ou retranche ±NNN secondes à une date et une heure                            |
| NNN month(s)         | Ajoute ±NNN mois à une date et une heure                                             |
| NNN year(s)          | Ajoute ±NNN années à une date et une heure                                           |
| start of month       | Revient au début du mois                                                             |
| start of year        | Revient au début de l'année                                                          |
| start of day         | Revient au début de la journée                                                       |
| weekday N            | Avance une date jusqu'à la prochaine date dont le numéro de jour de la semaine est N |
| localtime            | Renvoie l'heure locale                                                               |
| utc                  | Renvoie l'heure UTC                                                                  |
| unixepoch            | Heure Unix                                                                           |

## Exemples {#examples}

### Renvoyer la date et l'heure actuelles avec le modificateur `now` {#return-the-current-date-and-time-using-the-now-modifier}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convertir une heure UTC en heure locale avec le modificateur `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convertir une date et heure en UTC {#covert-datetime-to-utc}

```sql
SELECT name, timeCreated,
datetime('now', 'utc') as now_utc
FROM google.storage.buckets WHERE project = 'stackql';
```

### Calculer une date par rapport à une autre date {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Obtenir le prochain jour de la semaine spécifié après une date donnée {#get-the-next-specified-weekday-after-a-given-date}

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

### Convertir un horodatage Unix en date et heure {#covert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Pour convertir une date et heure en horodatage epoch Unix, consultez [**STRFTIME**](/language-spec/functions/datetime/strftime).  Pour calculer la différence entre deux objets de date et d'heure, consultez [**STRFTIME**](/language-spec/functions/datetime/strftime) ou [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::
