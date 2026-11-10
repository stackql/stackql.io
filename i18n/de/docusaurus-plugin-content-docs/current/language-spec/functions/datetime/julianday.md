---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Funktion JULIANDAY in StackQL: die gebrochene Anzahl der Tage seit dem Mittag des 24. November 4714 v. Chr. für einen Zeitwert, nützlich für Datumsarithmetik."
image: "/img/stackql-featured-image.png"
---
Gibt das Julianische Datum zurück - die Anzahl der Tage seit dem Mittag in Greenwich am 24. November 4714 v. Chr. [(Proleptischer gregorianischer Kalender)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Datumsmodifikatoren ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*datetime_expression*__  
Der Datetime-Wert.

__*modifier*__  
Null oder mehr Modifikatoren, die auf die *datetime_expression* angewendet werden.  Weitere Informationen zu Modifikatoren finden Sie unter [**Datumsmodifikatoren**](/language-spec/functions/datetime/date_modifiers)

## Rückgabewert(e) {#return-values}

Gibt das [Julianische Datum](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar) zurück, das den Datumsanteil der eingegebenen *datetime_expression* nach Anwendung des *modifier* darstellt.

* * *

## Beispiele {#examples}

### Die Tage zwischen zwei Datumswerten berechnen {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Verwenden Sie [**ROUND**](/language-spec/functions/math/round), um Gleitkommazahlen in die nächste Ganzzahl umzuwandeln.

:::

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
