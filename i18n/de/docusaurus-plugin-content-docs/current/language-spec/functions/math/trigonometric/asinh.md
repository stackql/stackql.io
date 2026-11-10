---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Areasinus hyperbolicus (Umkehrfunktion des Sinus hyperbolicus) einer Zahl mit SQL in StackQL berechnen und einen Winkel im Bogenmaß zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Areasinus hyperbolicus (Umkehrfunktion des Sinus hyperbolicus) einer Zahl, also den Wert \( \theta \) im Bogenmaß, dessen Sinus hyperbolicus die angegebene Zahl ist.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck, dessen Areasinus hyperbolicus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Areasinus hyperbolicus des angegebenen Werts im Bogenmaß zurück.

* * *

## Beispiele {#examples}

### Den Areasinus hyperbolicus von 0.5 berechnen {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

Diese Abfrage gibt ungefähr `0.4812` zurück, da der Areasinus hyperbolicus von 0.5 etwa 0.4812 Radiant beträgt.

### Den Areasinus hyperbolicus von -1 berechnen {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

Diese Abfrage berechnet den Areasinus hyperbolicus von -1 und ergibt ungefähr `-0.8814`, da dies der Wert im Bogenmaß ist, dessen Sinus hyperbolicus -1 ist.
