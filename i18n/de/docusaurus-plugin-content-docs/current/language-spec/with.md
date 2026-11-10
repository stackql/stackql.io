---
title: WITH (CTEs)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "WITH in StackQL: Common Table Expressions (CTEs) definieren, temporäre benannte Ergebnismengen, die auf eine einzelne SELECT-, INSERT-, UPDATE- oder DELETE-Anweisung beschränkt sind."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Definiert eine Common Table Expression (CTE) zur Verwendung in einer nachfolgenden `SELECT`-, `INSERT`-, `UPDATE`- oder `DELETE`-Anweisung. CTEs wirken als temporäre benannte Ergebnismengen, die nur für die Dauer der Abfrage existieren.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` INSERT `]](/language-spec/insert) [[` UPDATE `]](/language-spec/update) [[` DELETE `]](/language-spec/delete)

* * *

## Syntax {#syntax}

*withClause::=*

<RailroadDiagram
type="withClause"
/>

*cteDefinition::=*

<RailroadDiagram
type="cteDefinition"
/>

&nbsp;
&nbsp;

```sql
WITH [RECURSIVE] cte_name [(column_name, ...)] AS (
    select_statement
)
[, cte_name [(column_name, ...)] AS (
    select_statement
)]
SELECT ... FROM cte_name ...;
```

## Argumente {#arguments}

__*RECURSIVE*__
Optionales Schlüsselwort, das es der CTE erlaubt, auf sich selbst zu verweisen, und damit rekursive Abfragen wie das Durchlaufen hierarchischer Daten ermöglicht.

__*cte_name*__
Der der CTE zugewiesene Name. Auf diesen Namen kann in der Hauptabfrage wie auf eine Tabelle verwiesen werden.

__*column_name*__
Optional. Spaltennamen für die Ergebnismenge der CTE. Werden sie nicht angegeben, werden die Spaltennamen aus der SELECT-Anweisung abgeleitet.

__*select_statement*__
Die Abfrage, die die Ergebnismenge der CTE definiert.

## Rückgabewert(e) {#return-values}

CTEs geben keine Werte direkt zurück. Sie definieren temporäre Ergebnismengen, auf die in der Hauptabfrage verwiesen werden kann.

* * *

## Beispiele {#examples}

### Einfache CTE zur Strukturierung komplexer Abfragen {#basic-cte-for-organizing-complex-queries}

Eine CTE kann komplexe Abfragen vereinfachen, indem sie diese in logische, benannte Komponenten zerlegt.

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

### Daten aus mehreren Quellen mit `UNION ALL` kombinieren {#combining-data-from-multiple-sources-with-union-all}

CTEs sind nützlich, um Daten aus mehreren Quellen zu kombinieren, bevor eine Analyse durchgeführt wird.

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

### CTEs mit Fensterfunktionen für Rangfolgen verwenden {#using-ctes-with-window-functions-for-ranking}

```sql
-- Rank contributors within each repo using CTE and window functions
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo,
        SUM(contributions) OVER (PARTITION BY repo) as repo_total_contributions
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

### Mehrere CTEs in einer Abfrage {#multiple-ctes-in-a-single-query}

Sie können mehrere CTEs durch Kommas getrennt definieren.

```sql
-- Analyze issues and releases together
WITH open_issues AS (
    SELECT number, title, created_at
    FROM github.issues.issues
    WHERE owner = 'stackql'
      AND repo = 'stackql'
      AND state = 'open'
),
recent_releases AS (
    SELECT tag_name, published_at
    FROM github.repos.releases
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    ORDER BY published_at DESC
    LIMIT 5
)
SELECT
    'Open Issues' as category,
    COUNT(*) as count
FROM open_issues
UNION ALL
SELECT
    'Recent Releases' as category,
    COUNT(*) as count
FROM recent_releases;
```

## Rekursive CTEs {#recursive-ctes}

Rekursive CTEs erlauben es einer CTE, auf sich selbst zu verweisen, und ermöglichen so Abfragen, die hierarchische oder graphartige Datenstrukturen durchlaufen.

```sql
WITH RECURSIVE cte_name (columns) AS (
    -- Base case (anchor member)
    SELECT ...
    UNION ALL
    -- Recursive case (references cte_name)
    SELECT ... FROM cte_name WHERE ...
)
SELECT * FROM cte_name;
```

:::tip

Rekursive CTEs sind leistungsfähig für hierarchische Daten wie Organigramme, Dateisysteme oder Abhängigkeitsbäume. Nehmen Sie im rekursiven Fall immer eine Abbruchbedingung auf, um Endlosschleifen zu vermeiden.

:::

## Best Practices {#best-practices}

1. **CTEs für Lesbarkeit verwenden**: Zerlegen Sie komplexe Abfragen in logische, benannte Komponenten.
2. **Mit Fensterfunktionen kombinieren**: CTEs eignen sich gut in Verbindung mit Fensterfunktionen für fortgeschrittene Analysen.
3. **Mehrere CTEs verketten**: Verweisen Sie in späteren CTEs auf frühere, um komplexe Logik Schritt für Schritt aufzubauen.
4. **Leistung berücksichtigen**: CTEs verbessern zwar die Lesbarkeit, sehr große CTEs können sich jedoch auf die Leistung auswirken. Testen Sie mit Ihren tatsächlichen Datenmengen.

Weitere Informationen finden Sie unter [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
