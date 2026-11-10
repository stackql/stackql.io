---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Funktion TIME in StackQL: gibt eine Uhrzeit als Zeichenfolge im Format HH:MM:SS aus einem Zeitwert und optionalen Modifikatoren zurück."
image: "/img/stackql-featured-image.png"
---
Gibt eine Zeitzeichenfolge im Format HH:MM:SS zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Datumsmodifikatoren ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*datetime_expression*__  
Der Datetime-Wert.

__*modifier*__  
Null oder mehr Modifikatoren, die auf die *datetime_expression* angewendet werden.  Weitere Informationen zu Modifikatoren finden Sie unter [**Datumsmodifikatoren**](/language-spec/functions/datetime/date_modifiers)

## Rückgabewert(e) {#return-values}

Gibt eine Zeichenfolge zurück, die den Zeitanteil der eingegebenen *datetime_expression* nach Anwendung des *modifier* darstellt.

* * *

## Beispiele {#examples}

### Den Zeitanteil eines Datetime-Werts zurückgeben {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Den lokalen Zeitanteil eines Datetime-Werts zurückgeben {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
