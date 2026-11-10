---
title: Datumsmodifikatoren
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Datums- und Zeitmodifikatoren in StackQL: verschieben DATE-, TIME-, DATETIME-, JULIANDAY- und STRFTIME-Ergebnisse um Tage, Monate oder Jahre, zum Periodenbeginn, Wochentag und mehr."
image: "/img/stackql-featured-image.png"
---

## Überblick {#overview}

Auf die Datums- oder Zeitwerte der Datetime-Funktionen in StackQL können null oder mehr Modifikatoren folgen, die das Datum und/oder die Uhrzeit verändern. Jeder Modifikator ist eine Transformation, die auf den Zeitwert links von ihm angewendet wird. Modifikatoren werden der Reihe nach von links nach rechts angewendet. Die verfügbaren Modifikatoren sind:

| Modifikator          | Beschreibung                                                                        |
|----------------------|-------------------------------------------------------------------------------------|
| NNN day(s)           | Addiert ±NNN Tage zu Datum und Uhrzeit                                              |
| NNN hour(s)          | Addiert ±NNN Stunden zu Datum und Uhrzeit                                           |
| NNN minute(s)        | Addiert ±NNN Minuten zu Datum und Uhrzeit                                           |
| NNN[.NNNN] second(s) | Addiert ±NNN Sekunden zu Datum und Uhrzeit bzw. zieht sie ab                        |
| NNN month(s)         | Addiert ±NNN Monate zu Datum und Uhrzeit                                            |
| NNN year(s)          | Addiert ±NNN Jahre zu Datum und Uhrzeit                                             |
| start of month       | Zurück zum Beginn des Monats                                                        |
| start of year        | Zurück zum Beginn des Jahres                                                        |
| start of day         | Zurück zum Beginn des Tages                                                         |
| weekday N            | Verschiebt ein Datum vorwärts auf das nächste Datum, dessen Wochentagsnummer N ist |
| localtime            | Gibt die lokale Zeit zurück                                                         |
| utc                  | Gibt die UTC-Zeit zurück                                                            |
| unixepoch            | Unix-Zeit                                                                           |

## Beispiele {#examples}

### Das aktuelle Datum und die aktuelle Uhrzeit mit dem Modifikator `now` zurückgeben {#return-the-current-date-and-time-using-the-now-modifier}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### UTC mit dem Modifikator `localtime` in lokale Zeit umwandeln {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Datetime in UTC umwandeln {#covert-datetime-to-utc}

```sql
SELECT name, timeCreated,
datetime('now', 'utc') as now_utc
FROM google.storage.buckets WHERE project = 'stackql';
```

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

### Einen Unix-Zeitstempel in einen Datetime-Wert umwandeln {#covert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Zum Umwandeln eines Datetime-Werts in einen Unix-Epoch-Zeitstempel siehe [**STRFTIME**](/language-spec/functions/datetime/strftime).  Zum Berechnen von Differenzen zwischen zwei Datetime-Objekten siehe [**STRFTIME**](/language-spec/functions/datetime/strftime) oder [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::
