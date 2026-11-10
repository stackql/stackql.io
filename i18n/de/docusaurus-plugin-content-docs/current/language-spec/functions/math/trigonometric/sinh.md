---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Sinus hyperbolicus einer Zahl mit SQL in StackQL berechnen und das Ergebnis zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Sinus hyperbolicus eines angegebenen Werts.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck, dessen Sinus hyperbolicus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Sinus hyperbolicus des angegebenen Werts zurück.

* * *

## Beispiele {#examples}

### Den Sinus hyperbolicus von 1 berechnen {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

Diese Abfrage gibt ungefähr `1.1752` zurück, da der Sinus hyperbolicus von 1 etwa 1.1752 beträgt.

### Den Sinus hyperbolicus von 0 berechnen {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

Diese Abfrage berechnet den Sinus hyperbolicus von 0 und ergibt `0`, da der Sinus hyperbolicus von 0 genau 0 ist.
