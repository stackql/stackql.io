---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Einen Winkel mit SQL in StackQL vom Bogenmaß in Grad umrechnen.
image: "/img/stackql-featured-image.png"
---
Rechnet einen angegebenen Winkel vom Bogenmaß in Grad um.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*angle_in_radians*__  
Ein numerischer Ausdruck, der einen Winkel im Bogenmaß darstellt.

## Rückgabewert(e) {#return-values}
Gibt den entsprechenden Winkel in Grad zurück.

* * *

## Beispiele {#examples}

### π Radiant in Grad umrechnen {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

Diese Abfrage gibt ungefähr `180` zurück, da die Umrechnung von π Radiant (ungefähr `3.14159`) etwa 180 Grad ergibt.

### π/2 Radiant in Grad umrechnen {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

Diese Abfrage berechnet die Umrechnung von π/2 Radiant (ungefähr `1.5708`) in Grad und ergibt etwa `90`, da π/2 Radiant einem Winkel von 90 Grad entspricht.
