---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion FLOOR in StackQL: ein numerisches Argument auf die nächste Ganzzahl abrunden."
image: "/img/stackql-featured-image.png"
---
Rundet ein numerisches Argument auf die nächste Ganzzahl ab.  

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein Ausdruck aus der Kategorie der exakten oder näherungsweisen numerischen Datentypen.

## Rückgabewert(e) {#return-values}
Gibt die größte Ganzzahl zurück, die kleiner oder gleich dem angegebenen numerischen Ausdruck ist.

* * *

## Beispiele {#examples}

### Eine Gleitkommazahl auf die nächste (kleinere) Ganzzahl abrunden {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
