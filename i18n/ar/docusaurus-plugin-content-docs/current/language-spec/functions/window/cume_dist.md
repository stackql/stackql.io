---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة CUME_DIST في StackQL: التوزيع التراكمي للصف الحالي داخل قسمه، وهو رقم الصف مقسوم على عدد صفوف القسم."
image: "/img/stackql-featured-image.png"
---
تعيد التوزيع التراكمي لقيمة داخل قسم. تحسب هذه القيمة على أنها `row-number / partition-rows`، حيث `row-number` هي القيمة التي تعيدها الدالة `ROW_NUMBER()` لآخر صف نظير في المجموعة، و`partition-rows` هو عدد الصفوف في القسم.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## بناء الجملة {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `CUME_DIST` على كل قسم على حدة.

__*ORDER BY column*__
تحدد ترتيب حساب التوزيع التراكمي.

## القيمة المرجعة {#return-values}

تعيد عددًا بفاصلة عائمة بين 0.0 و1.0 يمثل قيمة التوزيع التراكمي.

* * *

## أمثلة {#examples}

### حساب التوزيع التراكمي للمساهمين {#calculate-cumulative-distribution-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

تعيد الدالة `CUME_DIST` نسبة الصفوف التي تكون قيمها أقل من قيمة الصف الحالي أو مساوية لها. تعني القيمة 0.75 أن 75% من الصفوف لها قيم أقل من قيمة الصف الحالي أو مساوية لها.

:::

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
