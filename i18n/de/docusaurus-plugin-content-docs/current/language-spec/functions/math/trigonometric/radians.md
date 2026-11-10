---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Einen Winkel mit SQL in StackQL von Grad ins Bogenmaß umrechnen.
image: "/img/stackql-featured-image.png"
---
Rechnet einen angegebenen Winkel von Grad ins Bogenmaß um.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*angle_in_degrees*__  
Ein numerischer Ausdruck, der einen Winkel in Grad darstellt.

## Rückgabewert(e) {#return-values}
Gibt den entsprechenden Winkel im Bogenmaß zurück.

* * *

## Beispiele {#examples}

### 180 Grad ins Bogenmaß umrechnen {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

Diese Abfrage gibt π (ungefähr `3.14159`) zurück, da die Umrechnung von 180 Grad ins Bogenmaß π Radiant ergibt.

### 90 Grad ins Bogenmaß umrechnen {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

Diese Abfrage berechnet die Umrechnung von 90 Grad ins Bogenmaß und ergibt π/2 oder ungefähr `1.5708`, da 90 Grad die Hälfte von 180 Grad sind.
