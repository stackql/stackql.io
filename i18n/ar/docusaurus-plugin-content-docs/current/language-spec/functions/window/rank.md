---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة RANK في StackQL: ترتيب الصف الحالي ضمن قسمه مع فجوات، إذ تتشارك الصفوف المتعادلة الترتيب نفسه ويتم تخطي قيم الترتيب التالية."
image: "/img/stackql-featured-image.png"
---
تعيد ترتيب الصف الحالي مع فجوات. وهو قيمة `ROW_NUMBER` لأول صف نظير في كل مجموعة. تحصل الصفوف ذات القيم المتساوية في أعمدة الترتيب على الترتيب نفسه، وتزداد قيمة الترتيب التالية بعدد الصفوف المتعادلة (مما يترك فجوات).

إذا لم تكن هناك عبارة `ORDER BY`، فإن جميع الصفوف تُعد صفوفًا نظيرة، وتعيد هذه الدالة دائمًا 1.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## بناء الجملة {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `RANK` على كل قسم على حدة.

__*ORDER BY column*__
تحدد الترتيب الذي يتم به تعيين قيم الترتيب. إذا لم تكن هناك عبارة `ORDER BY`، فإن جميع الصفوف تُعد صفوفًا نظيرة وتحصل على الترتيب 1.

## القيمة المرجعة {#return-values}

تعيد عددًا صحيحًا يمثل ترتيب الصف الحالي ضمن القسم، مع فجوات للقيم المتعادلة.

* * *

## أمثلة {#examples}

### ترتيب المساهمين حسب المساهمات (مع فجوات) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### مقارنة `RANK` مع `DENSE_RANK` و`ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### ترتيب المساهمين ضمن كل مستودع باستخدام `PARTITION BY` {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
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

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
