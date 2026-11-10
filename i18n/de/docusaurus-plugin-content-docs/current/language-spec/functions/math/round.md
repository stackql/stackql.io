---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion ROUND in StackQL: ein numerisches Argument auf eine angegebene Anzahl von Dezimalstellen runden, standardmäßig null."
image: "/img/stackql-featured-image.png"
---
Rundet ein numerisches Argument.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein Ausdruck aus der Kategorie der exakten oder näherungsweisen numerischen Datentypen.

__*precision*__  
Die Anzahl der Stellen rechts vom Dezimalpunkt, auf die *numeric_expression* gerundet wird.

> Wird das Argument *precision* weggelassen, wird 0 angenommen.

## Rückgabewert(e) {#return-values}
Gibt einen numerischen Wert zurück.

* * *

## Beispiele {#examples}

### Eine Gleitkommazahl auf zwei Dezimalstellen runden {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
