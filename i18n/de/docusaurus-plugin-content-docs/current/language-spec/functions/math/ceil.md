---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion CEIL in StackQL: ein numerisches Argument auf die nächste Ganzzahl aufrunden."
image: "/img/stackql-featured-image.png"
---
Rundet ein numerisches Argument auf die nächste Ganzzahl auf.  

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein Ausdruck aus der Kategorie der exakten oder näherungsweisen numerischen Datentypen.

## Rückgabewert(e) {#return-values}
Gibt die kleinste Ganzzahl zurück, die größer oder gleich dem angegebenen numerischen Ausdruck ist.

* * *

## Beispiele {#examples}

### Eine Gleitkommazahl auf die nächste (größere) Ganzzahl aufrunden {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
