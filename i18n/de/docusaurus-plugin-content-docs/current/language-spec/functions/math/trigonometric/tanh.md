---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Tangens hyperbolicus einer Zahl mit SQL in StackQL berechnen und das Ergebnis zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Tangens hyperbolicus eines angegebenen Werts.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck, dessen Tangens hyperbolicus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Tangens hyperbolicus des angegebenen Werts zurück.

* * *

## Beispiele {#examples}

### Den Tangens hyperbolicus von 1 berechnen {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

Diese Abfrage gibt ungefähr `0.7616` zurück, da der Tangens hyperbolicus von 1 etwa 0.7616 beträgt.

### Den Tangens hyperbolicus von 0 berechnen {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

Diese Abfrage berechnet den Tangens hyperbolicus von 0 und ergibt `0`, da der Tangens hyperbolicus von 0 genau 0 ist.
