---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Areatangens hyperbolicus (Umkehrfunktion des Tangens hyperbolicus) einer Zahl mit SQL in StackQL berechnen und einen Winkel im Bogenmaß zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Areatangens hyperbolicus (Umkehrfunktion des Tangens hyperbolicus) einer Zahl, also den Wert \( \theta \) im Bogenmaß, dessen Tangens hyperbolicus die angegebene Zahl ist.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck im Bereich von -1 bis 1 (ausschließlich), dessen Areatangens hyperbolicus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Areatangens hyperbolicus des angegebenen Werts im Bogenmaß zurück.

* * *

## Beispiele {#examples}

### Den Areatangens hyperbolicus von 0.5 berechnen {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

Diese Abfrage gibt ungefähr `0.5493` zurück, da der Areatangens hyperbolicus von 0.5 etwa 0.5493 Radiant beträgt.

### Den Areatangens hyperbolicus von -0.5 berechnen {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

Diese Abfrage berechnet den Areatangens hyperbolicus von -0.5 und ergibt ungefähr `-0.5493`, da dies der Wert im Bogenmaß ist, dessen Tangens hyperbolicus -0.5 ist.
