---
title: دوال النافذة
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دوال النافذة في StackQL: الترتيب والمجاميع التراكمية والإزاحات والتوزيعات عبر الصفوف المرتبطة، مع عبارات PARTITION BY وORDER BY وعبارات الإطار."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تجري دوال النافذة عمليات حسابية على مجموعة من الصفوف المرتبطة بالصف الحالي. على عكس الدوال التجميعية التي تعيد نتيجة واحدة لمجموعة من الصفوف، تعيد دوال النافذة قيمة لكل صف مع مراعاة "نافذة" من الصفوف المرتبطة.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` WITH (CTE) `]](/language-spec/with) [[ مرجع دوال النافذة ]](/language-spec/functions/window/row_number)

* * *

## بناء الجملة {#syntax}

تستخدم دوال النافذة عبارة `OVER` لتحديد مواصفات النافذة:

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### دالة النافذة {#window-function}

دوال النافذة دوال متخصصة تجري عمليات حسابية على مجموعة من الصفوف تحددها مواصفات النافذة. يمكن أن تكون إما دوال نافذة مخصصة لهذا الغرض (مثل `ROW_NUMBER` أو `LAG`) أو دوال تجميعية (مثل `SUM` أو `COUNT`) تستخدم مع عبارة `OVER`.

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

### مواصفات النافذة {#window-specification}

تحدد مواصفات النافذة كيفية تقسيم الصفوف وترتيبها وتأطيرها لحساب دالة النافذة. تقسم عبارة `PARTITION BY` الصفوف إلى مجموعات، وتحدد عبارة `ORDER BY` التسلسل داخل كل قسم، وتحدد عبارة الإطار الاختيارية الصفوف التي تدخل في الحساب نسبة إلى الصف الحالي.

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

## دوال النافذة المتاحة {#available-window-functions}

| الفئة | الدوال |
|----------|-----------|
| **الترتيب** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number)، [__`RANK()`__](/language-spec/functions/window/rank)، [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank)، [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **الإزاحة** | [__`LAG()`__](/language-spec/functions/window/lag)، [__`LEAD()`__](/language-spec/functions/window/lead)، [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value)، [__`LAST_VALUE()`__](/language-spec/functions/window/last_value)، [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **التوزيع** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank)، [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **التجميع** | [__`SUM()`__](/language-spec/functions/aggregate/sum)، [__`COUNT()`__](/language-spec/functions/aggregate/count)، [__`AVG()`__](/language-spec/functions/aggregate/avg)، [__`MIN()`__](/language-spec/functions/aggregate/min)، [__`MAX()`__](/language-spec/functions/aggregate/max) مع عبارة __`OVER`__ |

* * *

## أمثلة {#examples}

### الترتيب باستخدام `ROW_NUMBER` و`RANK` و`DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

استخدم دوال الترتيب لتعيين ترتيب للصفوف حسب قيم الأعمدة.

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

### المجاميع التراكمية والنسب المئوية {#running-totals-and-percentages}

يمكن استخدام الدوال التجميعية مثل `SUM` و`COUNT` كدوال نافذة لحساب المجاميع التراكمية والنسب المئوية.

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

### مقارنة الصفوف باستخدام `LAG` و`LEAD` {#comparing-rows-with-lag-and-lead}

استخدم الدالتين `LAG` و`LEAD` للوصول إلى القيم من الصفوف السابقة أو اللاحقة.

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

### إنشاء مجموعات باستخدام `NTILE` {#creating-groups-with-ntile}

استخدم الدالة `NTILE` لتقسيم الصفوف إلى عدد محدد من المجموعات.

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

### استخدام `PARTITION BY` للتحليل حسب المجموعات {#using-partition-by-for-grouped-analysis}

استخدم عبارة `PARTITION BY` لإجراء حسابات النافذة داخل المجموعات.

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

### استخدام أطر النافذة {#using-window-frames}

حدّد عبارة إطار للتحكم في الصفوف التي يتضمنها حساب النافذة.

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

للاطلاع على الوثائق التفصيلية لكل دالة نافذة، راجع [مرجع دوال النافذة](/language-spec/functions/window/row_number).

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
