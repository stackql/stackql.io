---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "NTILE-Fensterfunktion in StackQL: eine Partition so gleichmäßig wie möglich in N Gruppen aufteilen und die Gruppe jeder Zeile von 1 bis N nummerieren."
image: "/img/stackql-featured-image.png"
---
Teilt die Partition so gleichmäßig wie möglich in N Gruppen auf und weist jeder Gruppe eine Ganzzahl zwischen 1 und N zu, in der Reihenfolge, die durch die `ORDER BY`-Klausel festgelegt ist, andernfalls in beliebiger Reihenfolge. Falls erforderlich, kommen die größeren Gruppen zuerst.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Syntax {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*N*__
Eine positive Ganzzahl, die die Anzahl der Gruppen (Tiles) angibt, in die die Partition aufgeteilt wird.

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `NTILE` wird auf jede Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Zeilen den Tiles zugewiesen werden.

## Rückgabewert(e) {#return-values}

Gibt eine Ganzzahl zwischen 1 und N zurück, die angibt, zu welchem Tile (welcher Gruppe) die aktuelle Zeile gehört.

* * *

## Beispiele {#examples}

### Mitwirkende nach Beitragsumfang in Quartile einteilen {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Verwenden Sie `NTILE`, um Segmente oder Stufen in Ihren Daten zu bilden. Typische Anwendungsfälle sind die Einteilung von Daten in Quartile (N=4), Dezile (N=10) oder Perzentile (N=100).

:::

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
