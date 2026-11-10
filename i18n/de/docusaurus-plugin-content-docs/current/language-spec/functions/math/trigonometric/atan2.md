---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Arkustangens zweier Zahlen (y/x) mit SQL in StackQL berechnen und einen Winkel im Bogenmaß zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Arkustangens zweier Zahlen, genauer den Winkel \( \theta \) im Bogenmaß, dessen Tangens das Verhältnis der beiden angegebenen Zahlen (y geteilt durch x) ist, und berücksichtigt dabei die Vorzeichen der Eingaben korrekt, um den richtigen Quadranten zu bestimmen.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*y*__  
Die y-Koordinate (Zähler) des Punkts.

__*x*__  
Die x-Koordinate (Nenner) des Punkts.

## Rückgabewert(e) {#return-values}
Gibt den Arkustangens von y/x im Bogenmaß zurück und berücksichtigt dabei den richtigen Quadranten des Winkels.

* * *

## Beispiele {#examples}

### Den Arkustangens des Punkts (1,1) berechnen {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

Diese Abfrage gibt π/4 oder ungefähr `0.785` zurück, da der Arkustangens des Punkts (1,1) einem Winkel von π/4 Radiant im ersten Quadranten entspricht.

### Den Arkustangens des Punkts (10, -10) berechnen {#calculate-the-arc-tangent-of-the-point-10--10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

Diese Abfrage berechnet den Arkustangens des Punkts (10, -10) und ergibt 3π/4 oder ungefähr `2.356`, da der Tangens dieses Winkels im zweiten Quadranten -1 ist.
