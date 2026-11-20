---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة FIRST_VALUE في StackQL: قيمة تعبير للصف الأول من إطار النافذة."
image: "/img/stackql-featured-image.png"
---
تعيد قيمة التعبير بعد تقييمه على الصف الأول في إطار النافذة.

تحسب دالة النافذة المدمجة هذه إطار النافذة لكل صف على النحو نفسه الذي تحسبه به دالة نافذة تجميعية. وتعيد لكل صف قيمة التعبير بعد تقييمه على الصف الأول في إطار النافذة.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## بناء الجملة {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expr*__
التعبير المراد تقييمه على الصف الأول في إطار النافذة.

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `FIRST_VALUE` داخل كل قسم على حدة.

__*ORDER BY column*__
تحدد ترتيب معالجة الصفوف.

__*ROWS BETWEEN frame_start AND frame_end*__
اختياري. تحدد إطار النافذة. من الخيارات الشائعة:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

في حالة عدم وجود عبارة إطار، يكون الإطار `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: أي أنه ينتهي عند آخر صف يتعادل مع الصف الحالي في أعمدة `ORDER BY`، وليس عند الصف الحالي.

## القيمة المرجعة {#return-values}

تعيد قيمة التعبير بعد تقييمه على الصف الأول في إطار النافذة.

* * *

## أمثلة {#examples}

### العثور على الإصدار الأول لتوفير السياق {#find-the-first-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

افتراضيًا، يمتد إطار النافذة من بداية القسم إلى آخر صف يتعادل مع الصف الحالي في أعمدة `ORDER BY`. تقرأ الدالة `FIRST_VALUE` الصف الأول من الإطار، لذا فإنها تعيد القيمة نفسها مع الإطار الافتراضي ومع `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`؛ أما الإطار الصريح فله أهمية في حالة الدالتين `LAST_VALUE` و`NTH_VALUE`، اللتين تقرآن ما هو أبعد داخل الإطار.

:::

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
