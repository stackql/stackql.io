---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة PERCENT_RANK في StackQL: الترتيب النسبي للصف الحالي ضمن قسمه كقيمة بين 0.0 و1.0."
image: "/img/stackql-featured-image.png"
---
تعيد قيمة بين 0.0 و1.0 تمثل الترتيب النسبي للصف الحالي ضمن قسمه.

على الرغم من اسمها، تعيد هذه الدالة قيمة تساوي `(rank - 1) / (partition-rows - 1)`، حيث يمثل `rank` القيمة التي تعيدها دالة النافذة المدمجة `RANK()`، ويمثل `partition-rows` العدد الإجمالي للصفوف في القسم. إذا كان القسم يحتوي على صف واحد فقط، تعيد هذه الدالة 0.0.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## بناء الجملة {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `PERCENT_RANK` على كل قسم على حدة.

__*ORDER BY column*__
تحدد الترتيب الذي تُحسب به قيم الترتيب المئيني.

## القيمة المرجعة {#return-values}

تعيد عددًا بفاصلة عائمة بين 0.0 و1.0 يمثل الترتيب المئيني.

* * *

## أمثلة {#examples}

### حساب الترتيب المئيني للمساهمين {#calculate-percentile-ranking-of-contributors}

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

تشير قيمة `PERCENT_RANK` البالغة 0.0 إلى الصف ذي الترتيب الأدنى، بينما تشير القيمة 1.0 إلى الصف ذي الترتيب الأعلى. استخدم هذه الدالة لتحديد موضع قيمة معينة ضمن توزيع.

:::

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
