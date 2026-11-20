---
title: WITH (CTE)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "WITH في StackQL: تعريف تعبيرات الجداول الشائعة (CTE)، وهي مجموعات نتائج مؤقتة مسماة يقتصر نطاقها على جملة SELECT أو INSERT أو UPDATE أو DELETE واحدة."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تحدد تعبير جدول شائع (CTE) لاستخدامه في جملة `SELECT` أو `INSERT` أو `UPDATE` أو `DELETE` لاحقة. تعمل تعبيرات CTE كمجموعات نتائج مؤقتة مسماة لا توجد إلا طوال مدة الاستعلام.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` INSERT `]](/language-spec/insert) [[` UPDATE `]](/language-spec/update) [[` DELETE `]](/language-spec/delete)

* * *

## بناء الجملة {#syntax}

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

## الوسيطات {#arguments}

__*RECURSIVE*__  
كلمة أساسية اختيارية تتيح لتعبير CTE الإشارة إلى نفسه، مما يسمح بالاستعلامات العودية مثل اجتياز البيانات الهرمية.

__*cte_name*__  
الاسم المعين لتعبير CTE. يمكن الإشارة إلى هذا الاسم في الاستعلام الرئيسي كأنه جدول.

__*column_name*__  
اختياري. أسماء الأعمدة لمجموعة نتائج تعبير CTE. إذا لم تحددها، تؤخذ أسماء الأعمدة من جملة SELECT.

__*select_statement*__  
الاستعلام الذي يحدد مجموعة نتائج تعبير CTE.

## القيمة المرجعة {#return-values}

لا تعيد تعبيرات CTE أي قيم بشكل مباشر. بل تحدد مجموعات نتائج مؤقتة يمكن الإشارة إليها في الاستعلام الرئيسي.

* * *

## أمثلة {#examples}

### تعبير CTE أساسي لتنظيم الاستعلامات المعقدة {#basic-cte-for-organizing-complex-queries}

يمكن لتعبير CTE تبسيط الاستعلامات المعقدة بتقسيمها إلى مكونات منطقية مسماة.

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

### الجمع بين بيانات من مصادر متعددة باستخدام `UNION ALL` {#combining-data-from-multiple-sources-with-union-all}

تفيد تعبيرات CTE في الجمع بين البيانات من مصادر متعددة قبل إجراء التحليل.

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

### استخدام تعبيرات CTE مع دوال النافذة للترتيب {#using-ctes-with-window-functions-for-ranking}

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

### تعبيرات CTE متعددة في استعلام واحد {#multiple-ctes-in-a-single-query}

يمكنك تعريف عدة تعبيرات CTE مفصولة بفواصل.

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

## تعبيرات CTE العودية {#recursive-ctes}

تتيح تعبيرات CTE العودية لتعبير CTE الإشارة إلى نفسه، مما يتيح كتابة استعلامات تجتاز هياكل البيانات الهرمية أو الشبيهة بالرسوم البيانية.

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

تعبيرات CTE العودية فعالة في التعامل مع البيانات الهرمية مثل المخططات التنظيمية وأنظمة الملفات وأشجار التبعيات. أدرج دائمًا شرط إنهاء في الحالة العودية لمنع الحلقات اللانهائية.

:::

## أفضل الممارسات {#best-practices}

1. **استخدم تعبيرات CTE لتحسين قابلية القراءة**: قسّم الاستعلامات المعقدة إلى مكونات منطقية مسماة.
2. **اجمع بينها وبين دوال النافذة**: تعمل تعبيرات CTE جيدًا مع دوال النافذة في التحليلات المتقدمة.
3. **اربط عدة تعبيرات CTE في سلسلة**: أشر إلى تعبيرات CTE السابقة في التعبيرات اللاحقة لبناء منطق معقد خطوة بخطوة.
4. **ضع الأداء في الحسبان**: رغم أن تعبيرات CTE تحسن قابلية القراءة، فإن تعبيرات CTE الكبيرة جدًا قد تؤثر في الأداء. اختبر باستخدام أحجام بياناتك الفعلية.

لمزيد من المعلومات، راجع [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
