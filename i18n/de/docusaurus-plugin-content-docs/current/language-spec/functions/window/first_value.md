---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "FIRST_VALUE-Fensterfunktion in StackQL: der Wert eines Ausdrucks für die erste Zeile des Fensterrahmens."
image: "/img/stackql-featured-image.png"
---
Gibt den Wert des Ausdrucks zurück, der für die erste Zeile im Fensterrahmen ausgewertet wird.

Diese eingebaute Fensterfunktion berechnet den Fensterrahmen für jede Zeile auf dieselbe Weise wie eine Aggregat-Fensterfunktion. Sie gibt für jede Zeile den Wert des Ausdrucks zurück, der für die erste Zeile im Fensterrahmen ausgewertet wird.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Syntax {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expr*__
Der Ausdruck, der für die erste Zeile im Fensterrahmen ausgewertet wird.

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `FIRST_VALUE` wird innerhalb jeder Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Zeilen verarbeitet werden.

__*ROWS BETWEEN frame_start AND frame_end*__
Optional. Definiert den Fensterrahmen. Gängige Optionen sind:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` (Standard)
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

## Rückgabewert(e) {#return-values}

Gibt den Wert des Ausdrucks zurück, der für die erste Zeile im Fensterrahmen ausgewertet wird.

* * *

## Beispiele {#examples}

### Das erste Release als Kontext ermitteln {#find-the-first-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Standardmäßig erstreckt sich der Fensterrahmen vom Anfang der Partition bis zur aktuellen Zeile. Um alle Zeilen der Partition einzubeziehen, verwenden Sie `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
