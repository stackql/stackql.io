---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Sinus eines im Bogenmaß angegebenen Winkels mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet den Sinus eines angegebenen Winkels im Bogenmaß.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*radian_value*__  
Ein numerischer Ausdruck, der den Winkel im Bogenmaß angibt, dessen Sinus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Sinus des angegebenen Winkels zurück.

* * *

## Beispiele {#examples}

### Den Sinus von π/2 Radiant berechnen {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

Diese Abfrage gibt `1` zurück, da der Sinus von π/2 Radiant 1 ist.

### Den Sinus von π Radiant berechnen {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

Diese Abfrage berechnet den Sinus von π Radiant und ergibt ungefähr `0`, da der Sinus von π gleich 0 ist.
