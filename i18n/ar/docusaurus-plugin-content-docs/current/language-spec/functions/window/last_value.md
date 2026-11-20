---
title: LAST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة LAST_VALUE في StackQL: قيمة تعبير للصف الأخير من إطار النافذة."
image: "/img/stackql-featured-image.png"
---
تعيد قيمة التعبير بعد تقييمه على الصف الأخير في إطار النافذة.

تحسب دالة النافذة المدمجة هذه إطار النافذة لكل صف على النحو نفسه الذي تحسبه به دالة نافذة تجميعية. وتعيد لكل صف قيمة التعبير بعد تقييمه على الصف الأخير في إطار النافذة.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## بناء الجملة {#syntax}

```sql
SELECT LAST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expr*__
التعبير المراد تقييمه على الصف الأخير في إطار النافذة.

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `LAST_VALUE` داخل كل قسم على حدة.

__*ORDER BY column*__
تحدد ترتيب معالجة الصفوف.

__*ROWS BETWEEN frame_start AND frame_end*__
اختياري. تحدد إطار النافذة. من الخيارات الشائعة:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

في حالة عدم وجود عبارة إطار، يكون الإطار `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: أي أنه ينتهي عند آخر صف يتعادل مع الصف الحالي في أعمدة `ORDER BY`، وليس عند الصف الحالي.

## القيمة المرجعة {#return-values}

تعيد قيمة التعبير بعد تقييمه على الصف الأخير في إطار النافذة.

* * *

## أمثلة {#examples}

### العثور على أحدث إصدار لتوفير السياق {#find-the-latest-release-for-context}

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

افتراضيًا، يمتد إطار النافذة من بداية القسم إلى آخر صف يتعادل مع الصف الحالي في أعمدة `ORDER BY`، لذا تعيد الدالة `LAST_VALUE` قيمة آخر صف متعادل (وهو الصف الحالي نفسه عندما تكون قيمته في `ORDER BY` فريدة)، وليس القيمة الأخيرة في القسم. للحصول على القيمة الأخيرة في القسم، استخدم `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
