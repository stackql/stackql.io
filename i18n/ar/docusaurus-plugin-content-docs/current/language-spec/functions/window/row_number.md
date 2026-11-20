---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة ROW_NUMBER في StackQL: ترقيم الصفوف ضمن القسم بدءًا من 1 بالترتيب الذي تحدده عبارة ORDER BY الخاصة بالنافذة."
image: "/img/stackql-featured-image.png"
---
تعيد رقم الصف ضمن القسم الحالي. تُرقم الصفوف بدءًا من 1 بالترتيب الذي تحدده عبارة `ORDER BY` في تعريف النافذة، أو بترتيب عشوائي بخلاف ذلك.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## بناء الجملة {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `ROW_NUMBER` على كل قسم على حدة وتعيد تعيين رقم الصف لكل قسم.

__*ORDER BY column*__
تحدد الترتيب الذي يتم به تعيين أرقام الصفوف. مطلوب للحصول على نتائج ذات معنى.

## القيمة المرجعة {#return-values}

تعيد عددًا صحيحًا يمثل رقم الصف التسلسلي ضمن القسم، بدءًا من 1.

* * *

## أمثلة {#examples}

### ترتيب المساهمين ضمن مستودع {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### تتبع تسلسل المشكلات بمرور الوقت {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### مقارنة `ROW_NUMBER` مع `RANK` و`DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

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

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
