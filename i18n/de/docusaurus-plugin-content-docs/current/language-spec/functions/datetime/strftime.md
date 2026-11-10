---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Funktion STRFTIME in StackQL: formatiert einen Datetime-Wert mit einer Formatzeichenfolge und verwendet dieselben Ersetzungen wie die C-Funktion strftime."
image: "/img/stackql-featured-image.png"
---
Gibt einen formatierten Datetime-Wert auf Basis eines angegebenen Formats zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Datumsmodifikatoren ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*format*__  
Das gewünschte Format für den Datetime-Wert, der durch den Wert *datetime_expression* angegeben wird.  Siehe die Formattabelle unten.

__*datetime_expression*__  
Der zu formatierende Datums- oder Zeitwert.

__*modifier*__  
Null oder mehr Modifikatoren, die auf die *datetime_expression* angewendet werden.  Weitere Informationen zu Modifikatoren finden Sie unter [**Datumsmodifikatoren**](/language-spec/functions/datetime/date_modifiers)

### Formate {#formats}

| Format | Beschreibung                      |
|--------|-----------------------------------|
| %d     | Tag des Monats: 01-31             |
| %f     | Sekundenbruchteile: SS.SSS        |
| %H     | Stunde: 00-24                     |
| %j     | Tag des Jahres: 001-366           |
| %J     | Julianische Tageszahl             |
| %m     | Monat: 01-12                      |
| %M     | Minute: 00-59                     |
| %s     | Sekunden seit 1970-01-01          |
| %S     | Sekunden: 00-59                   |
| %w     | Wochentag 0-6 mit Sonntag==0      |
| %W     | Woche des Jahres: 00-53           |
| %Y     | Jahr: 0000-9999                   |
| %%     | maskiert ein Prozentzeichen: %    |

## Rückgabewert(e) {#return-values}

Gibt eine formatierte Zeichenfolge zurück, die das gewünschte Datetime-Format darstellt.

* * *

## Beispiele {#examples}

### Ein Datum im US-Format zurückgeben {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Einen Datetime-Wert in einen Epoch-Zeitstempel umwandeln {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Die Tage zwischen zwei Datumswerten berechnen {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
