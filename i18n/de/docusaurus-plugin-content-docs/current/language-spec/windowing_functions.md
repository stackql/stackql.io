---
title: Fensterfunktionen
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Fensterfunktionen in StackQL: Ränge, laufende Summen, Offsets und Verteilungen über zusammengehörige Zeilen, mit PARTITION BY, ORDER BY und Frame-Klauseln."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Fensterfunktionen führen Berechnungen über eine Menge von Zeilen aus, die mit der aktuellen Zeile in Beziehung stehen. Im Gegensatz zu Aggregatfunktionen, die ein einzelnes Ergebnis für eine Gruppe von Zeilen zurückgeben, geben Fensterfunktionen einen Wert für jede Zeile zurück und berücksichtigen dabei ein "Fenster" zusammengehöriger Zeilen.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` WITH (CTEs) `]](/language-spec/with) [[ Referenz der Fensterfunktionen ]](/language-spec/functions/window/row_number)

* * *

## Syntax {#syntax}

Fensterfunktionen verwenden die `OVER`-Klausel, um die Fensterspezifikation zu definieren:

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### Fensterfunktion {#window-function}

Fensterfunktionen sind spezialisierte Funktionen, die Berechnungen über eine durch die Fensterspezifikation definierte Menge von Zeilen ausführen. Es kann sich entweder um dedizierte Fensterfunktionen (wie `ROW_NUMBER` oder `LAG`) oder um Aggregatfunktionen (wie `SUM` oder `COUNT`) handeln, die mit einer `OVER`-Klausel verwendet werden.

```sql
{ <aggregateFunction> | ROW_NUMBER() | RANK() | DENSE_RANK() | PERCENT_RANK() 
  | CUME_DIST() | NTILE(<int>) | LAG(<expr> [,<offset> [,<default>]]) 
  | LEAD(<expr> [,<offset> [,<default>]]) | FIRST_VALUE(<expr>) 
  | LAST_VALUE(<expr>) | NTH_VALUE(<expr>, <int>) }
```  

*windowFunction::=*

<RailroadDiagram 
type="windowFunction"
/>

### Fensterspezifikation {#window-specification}

Die Fensterspezifikation legt fest, wie Zeilen für die Berechnung der Fensterfunktion partitioniert, sortiert und in einen Frame gefasst werden. `PARTITION BY` teilt Zeilen in Gruppen, `ORDER BY` bestimmt die Reihenfolge innerhalb jeder Partition, und die optionale Frame-Klausel gibt an, welche Zeilen relativ zur aktuellen Zeile in die Berechnung einbezogen werden.

```sql
[ <windowName> ]
[ PARTITION BY <fieldList> ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ { ROWS | RANGE | GROUPS } 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW }
  | { ROWS | RANGE | GROUPS } BETWEEN 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW | <int> FOLLOWING }
    AND
    { <int> PRECEDING | CURRENT ROW | <int> FOLLOWING | UNBOUNDED FOLLOWING } ]
```

*windowSpec::=*

<RailroadDiagram 
type="windowSpec"
/>

## Verfügbare Fensterfunktionen {#available-window-functions}

| Kategorie | Funktionen |
|----------|-----------|
| **Ranking** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **Offset** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **Verteilung** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **Aggregat** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) mit der __`OVER`__-Klausel |

* * *

## Beispiele {#examples}

### Ranking mit `ROW_NUMBER`, `RANK` und `DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

Verwenden Sie Ranking-Funktionen, um Zeilen anhand von Spaltenwerten Ränge zuzuweisen.

```sql
-- Rank contributors by contribution count
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Laufende Summen und Prozentwerte {#running-totals-and-percentages}

Aggregatfunktionen wie `SUM` und `COUNT` können als Fensterfunktionen verwendet werden, um laufende Summen und Prozentwerte zu berechnen.

```sql
-- Running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Zeilen mit `LAG` und `LEAD` vergleichen {#comparing-rows-with-lag-and-lead}

Verwenden Sie `LAG` und `LEAD`, um auf Werte vorheriger oder nachfolgender Zeilen zuzugreifen.

```sql
-- Compare each release to previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Gruppen mit `NTILE` erstellen {#creating-groups-with-ntile}

Verwenden Sie `NTILE`, um Zeilen in eine festgelegte Anzahl von Gruppen aufzuteilen.

```sql
-- Group contributors into quartiles
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### `PARTITION BY` für gruppierte Analysen verwenden {#using-partition-by-for-grouped-analysis}

Verwenden Sie `PARTITION BY`, um Fensterberechnungen innerhalb von Gruppen durchzuführen.

```sql
-- Track cumulative issues by state
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Window-Frames verwenden {#using-window-frames}

Geben Sie eine Frame-Klausel an, um zu steuern, welche Zeilen in die Fensterberechnung einbezogen werden.

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg,
    SUM(commits_this_week) OVER (ORDER BY week) as cumulative_commits
FROM weekly_totals
ORDER BY week;
```

Eine ausführliche Dokumentation jeder Fensterfunktion finden Sie in der [Referenz der Fensterfunktionen](/language-spec/functions/window/row_number).

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
