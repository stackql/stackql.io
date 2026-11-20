---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة NTH_VALUE في StackQL: قيمة تعبير للصف رقم N من إطار النافذة."
image: "/img/stackql-featured-image.png"
---
تعيد قيمة التعبير بعد تقييمه على الصف رقم N في إطار النافذة.

تحسب دالة النافذة المدمجة هذه إطار النافذة لكل صف على النحو نفسه الذي تحسبه به دالة نافذة تجميعية. وتعيد قيمة التعبير بعد تقييمه على الصف N من إطار النافذة. يبدأ ترقيم الصفوف داخل إطار النافذة من 1 بالترتيب الذي تحدده عبارة `ORDER BY` إن وجدت، أو بترتيب اعتباطي في غير ذلك. إذا لم يوجد صف رقم N في القسم، فإن الدالة تعيد `NULL`.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## بناء الجملة {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expr*__
التعبير المراد تقييمه على الصف رقم N في إطار النافذة.

__*N*__
عدد صحيح موجب يحدد رقم الصف المراد استرداده (يبدأ الترقيم من 1).

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `NTH_VALUE` داخل كل قسم على حدة.

__*ORDER BY column*__
تحدد ترتيب معالجة الصفوف.

__*ROWS BETWEEN frame_start AND frame_end*__
اختياري. تحدد إطار النافذة.

## القيمة المرجعة {#return-values}

تعيد قيمة التعبير بعد تقييمه على الصف رقم N في إطار النافذة، أو `NULL` إذا لم يوجد الصف رقم N.

* * *

## أمثلة {#examples}

### الحصول على أفضل ثلاثة مساهمين {#get-the-top-three-contributors}

```sql
-- Get the top 3 contributors by number of contributions
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
