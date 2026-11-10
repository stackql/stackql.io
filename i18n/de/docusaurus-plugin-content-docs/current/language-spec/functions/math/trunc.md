---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion TRUNC in StackQL: der ganzzahlige Anteil einer Zahl, gerundet in Richtung null und ohne Nachkommaanteil."
image: "/img/stackql-featured-image.png"
---

Gibt den ganzzahligen Anteil einer Zahl zurück, indem in Richtung null gerundet und jeder Nachkommaanteil entfernt wird.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein Ausdruck aus der Kategorie der exakten oder näherungsweisen numerischen Datentypen.

## Rückgabewert(e) {#return-values}
Gibt einen auf seinen ganzzahligen Anteil abgeschnittenen numerischen Wert zurück.

* * *

## Beispiele {#examples}

### Die Anzahl der Tage seit der Erstellung von Google Cloud Storage-Buckets auf zwei Dezimalstellen abschneiden {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-two-decimal-places}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
