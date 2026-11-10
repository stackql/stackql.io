---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "NTH_VALUE-Fensterfunktion in StackQL: der Wert eines Ausdrucks für die N-te Zeile des Fensterrahmens."
image: "/img/stackql-featured-image.png"
---
Gibt den Wert des Ausdrucks zurück, der für die N-te Zeile im Fensterrahmen ausgewertet wird.

Diese eingebaute Fensterfunktion berechnet den Fensterrahmen für jede Zeile auf dieselbe Weise wie eine Aggregat-Fensterfunktion. Sie gibt den Wert des Ausdrucks zurück, der für die Zeile N des Fensterrahmens ausgewertet wird. Die Zeilen werden innerhalb des Fensterrahmens beginnend bei 1 nummeriert, in der Reihenfolge, die durch die `ORDER BY`-Klausel festgelegt ist, falls vorhanden, andernfalls in beliebiger Reihenfolge. Gibt es keine N-te Zeile in der Partition, wird `NULL` zurückgegeben.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## Syntax {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expr*__
Der Ausdruck, der für die N-te Zeile im Fensterrahmen ausgewertet wird.

__*N*__
Eine positive Ganzzahl, die angibt, welche Zeilennummer abgerufen wird (Zählung beginnt bei 1).

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `NTH_VALUE` wird innerhalb jeder Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Zeilen verarbeitet werden.

__*ROWS BETWEEN frame_start AND frame_end*__
Optional. Definiert den Fensterrahmen.

## Rückgabewert(e) {#return-values}

Gibt den Wert des Ausdrucks zurück, der für die N-te Zeile im Fensterrahmen ausgewertet wird, oder `NULL`, wenn die N-te Zeile nicht existiert.

* * *

## Beispiele {#examples}

### Den zweitplatzierten Mitwirkenden ermitteln {#get-the-second-highest-contributor}

```sql
-- Get the top 3 contributors by name for comparison
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
