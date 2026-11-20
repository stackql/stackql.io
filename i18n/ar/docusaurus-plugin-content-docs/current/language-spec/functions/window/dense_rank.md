---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة DENSE_RANK في StackQL: ترتيب الصف الحالي في قسمه بدون فجوات، فتشترك الصفوف المتعادلة في ترتيب واحد ويليه الترتيب التالي مباشرة."
image: "/img/stackql-featured-image.png"
---
تعيد ترتيب الصف الحالي بدون فجوات. وهو رقم مجموعة الصفوف النظيرة التي ينتمي إليها الصف الحالي داخل قسمه. تحصل الصفوف ذات القيم المتساوية في أعمدة الترتيب على الترتيب نفسه، وتزيد قيمة الترتيب التالية دائمًا بمقدار 1 (بدون فجوات).

يبدأ ترقيم الصفوف من 1 بالترتيب الذي تحدده عبارة `ORDER BY` في تعريف النافذة. إذا لم توجد عبارة `ORDER BY`، فتعامل جميع الصفوف كصفوف نظيرة وتعيد هذه الدالة القيمة 1 دائمًا.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## بناء الجملة {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `DENSE_RANK` على كل قسم على حدة.

__*ORDER BY column*__
تحدد الترتيب الذي تعين على أساسه قيم الترتيب. إذا لم توجد عبارة `ORDER BY`، فتعامل جميع الصفوف كصفوف نظيرة وتحصل على الترتيب 1.

## القيمة المرجعة {#return-values}

تعيد عددًا صحيحًا يمثل الترتيب الكثيف للصف الحالي داخل القسم، بدون فجوات للقيم المتعادلة.

* * *

## أمثلة {#examples}

### ترتيب المساهمين حسب المساهمات (بدون فجوات) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### مقارنة الدالة `DENSE_RANK` مع الدالتين `RANK` و`ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### ترتيب المساهمات المجمعة عبر مستودعات متعددة {#rank-aggregated-contributions-across-multiple-repositories}

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

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
