---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Die mathematische Konstante π (Pi) mit SQL in StackQL abrufen.
image: "/img/stackql-featured-image.png"
---
Gibt die mathematische Konstante π (Pi) zurück, das Verhältnis des Umfangs eines Kreises zu seinem Durchmesser.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT PI() AS pi_value;
```

## Argumente {#arguments}

Diese Funktion erfordert keine Argumente.

## Rückgabewert(e) {#return-values}
Gibt die Konstante π zurück, ungefähr gleich 3.14159.

* * *

## Beispiele {#examples}

### Den Wert von π abrufen {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

Diese Abfrage gibt den Wert von π aus, ungefähr `3.14159`, der für verschiedene mathematische Berechnungen mit Kreisen und anderen geometrischen Funktionen nützlich ist.
