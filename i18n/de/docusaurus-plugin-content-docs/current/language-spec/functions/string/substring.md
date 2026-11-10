---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "SUBSTRING-Funktion in StackQL (Alias SUBSTR): einen Teil eines String- oder Binärwerts anhand von Startposition und Länge zurückgeben."
image: "/img/stackql-featured-image.png"
---
Gibt einen Teil eines Zeichen- oder Binärausdrucks zurück.  `SUBSTR` ist ein Alias für `SUBSTRING`.

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Datentypen ]](/language-spec/data-types)

* * * 

:::info

Um ein Feld aus einem JSON-Objekt zu extrahieren, verwenden Sie die Funktion [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## Syntax {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expression*__  
Ein ASCII-String oder ein BLOB.

> Wenn *expression* ein String ist, beziehen sich die Zeichenindizes auf tatsächliche UTF-8-Zeichen. Wenn *expression* ein BLOB ist, beziehen sich die Indizes auf Bytes.

__*start*__  
Eine Ganzzahl, die das erste Zeichen oder Byte der zurückzugebenden Teilsequenz des Eingabeausdrucks angibt.  Wenn *start* negativ ist, wird das erste Zeichen des Teilstrings von rechts statt von links gezählt.

> Das Zeichen ganz links in *expression* hat die Nummer 1.

__*length*__  
Ein optionaler Ganzzahlwert, der die Länge der nach dem Wert *start* zurückzugebenden Teilsequenz angibt.  Wenn *length* weggelassen wird, werden alle Zeichen bis zum Ende des durch *expression* angegebenen Strings oder BLOBs zurückgegeben.

> Wenn *length* negativ ist, wird der Absolutwert von *length* verwendet.

## Rückgabewert(e) {#return-values}

Gibt je nach Typ von *expression* einen String- oder BLOB-Wert zurück.

* * *

## Beispiele {#examples}

### Die ersten *n* Zeichen einer Spalte zurückgeben {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Die ersten *n* Zeichen einer Spalte abschneiden {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```
### Die letzten *n* Zeichen einer Spalte zurückgeben {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
