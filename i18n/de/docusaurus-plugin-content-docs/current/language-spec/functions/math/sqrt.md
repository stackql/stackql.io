---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Die Quadratwurzel einer Zahl mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet die Quadratwurzel eines nicht negativen numerischen Ausdrucks.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein nicht negativer numerischer Ausdruck, dessen Quadratwurzel berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt die Quadratwurzel des angegebenen nicht negativen numerischen Werts zurück.

* * *

## Beispiele {#examples}

### Die Quadratwurzel einer Zahl berechnen {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
