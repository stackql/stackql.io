---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Arkussinus (Umkehrfunktion des Sinus) einer Zahl mit SQL in StackQL berechnen und einen Winkel im Bogenmaß zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Arkussinus (Umkehrfunktion des Sinus) einer Zahl, also den Winkel im Bogenmaß, dessen Sinus die angegebene Zahl ist.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck im Bereich von -1 bis 1 (einschließlich), dessen Arkussinus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Arkussinus des angegebenen Werts im Bogenmaß zurück.

* * *

## Beispiele {#examples}

### Den Arkussinus von 0.5 berechnen {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

Diese Abfrage gibt π/6 oder ungefähr `0.5236` zurück, da der Arkussinus von 0.5 einem Winkel von π/6 Radiant entspricht.

### Den Arkussinus von -1 berechnen {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

Diese Abfrage berechnet den Arkussinus von -1 und ergibt -π/2 oder ungefähr `-1.5708`, da der Sinus von -π/2 gleich -1 ist.
