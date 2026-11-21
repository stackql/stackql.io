---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Funktion DATETIME in StackQL: berechnet einen Datetime-Wert aus einer Eingabe und null oder mehr Modifikatoren, zurückgegeben als YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Gibt einen berechneten Datetime-Wert aus einem Eingabe-Datetime-Wert und null oder mehr Modifikatoren zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Datumsmodifikatoren ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*datetime_expression*__  
Der Datetime-Wert.

__*modifier*__  
Null oder mehr Modifikatoren, die auf die *datetime_expression* angewendet werden.  Weitere Informationen zu Modifikatoren finden Sie unter [**Datumsmodifikatoren**](/language-spec/functions/datetime/date_modifiers)

## Rückgabewert(e) {#return-values}

Gibt eine Zeichenfolge zurück, die den Datetime-Wert der eingegebenen *datetime_expression* nach Anwendung des *modifier* darstellt.

* * *

## Beispiele {#examples}

### UTC mit dem Modifikator `localtime` in lokale Zeit umwandeln {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Eine lokale Zeit in UTC umwandeln {#convert-datetime-to-utc}

Der Modifikator `utc` behandelt seine Eingabe als lokale Zeit auf dem Rechner, auf dem StackQL läuft, und wandelt sie in UTC um.  Ein Wert, der bereits in UTC vorliegt, etwa `'now'` oder ein Zeitstempel mit dem Suffix `Z`, wird unverändert zurückgegeben.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

Auf einem Rechner mit der Zeitzone UTC+11 gibt diese Abfrage `2026-11-20 06:59:24` zurück.

### Ein Datum relativ zu einem anderen Datum berechnen {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Den nächsten angegebenen Wochentag nach einem bestimmten Datum ermitteln {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Den Monatsbeginn für ein bestimmtes Datum ermitteln {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Einen Unix-Zeitstempel in einen Datetime-Wert umwandeln {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Zum Umwandeln eines Datetime-Werts in einen Unix-Epoch-Zeitstempel siehe [**STRFTIME**](/language-spec/functions/datetime/strftime).  Zum Berechnen von Differenzen zwischen zwei Datetime-Objekten siehe [**STRFTIME**](/language-spec/functions/datetime/strftime) oder [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
