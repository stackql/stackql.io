---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Funktion DATE in StackQL: gibt ein Datum als Zeichenfolge im Format YYYY-MM-DD aus einem Zeitwert und optionalen Modifikatoren zurück."
image: "/img/stackql-featured-image.png"
---
Gibt eine Datumszeichenfolge im Format YYYY-MM-DD zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Datumsmodifikatoren ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*datetime_expression*__  
Der Datetime-Wert.

__*modifier*__  
Null oder mehr Modifikatoren, die auf die *datetime_expression* angewendet werden.  Weitere Informationen zu Modifikatoren finden Sie unter [**Datumsmodifikatoren**](/language-spec/functions/datetime/date_modifiers)

## Rückgabewert(e) {#return-values}

Gibt eine Zeichenfolge zurück, die den Datumsanteil der eingegebenen *datetime_expression* nach Anwendung des *modifier* darstellt.

* * *

## Beispiele {#examples}

### Den Datumsanteil eines Datetime-Werts zurückgeben {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Den lokalen Datumsanteil eines Datetime-Werts zurückgeben {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
