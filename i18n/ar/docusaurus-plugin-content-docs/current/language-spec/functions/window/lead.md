---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة LEAD في StackQL: تقييم تعبير على صف لاحق في القسم، مع إزاحة وقيمة افتراضية اختياريتين."
image: "/img/stackql-featured-image.png"
---
تعيد نتيجة تقييم تعبير على صف لاحق في القسم.

يعيد الشكل الأول من الدالة `LEAD()` نتيجة تقييم التعبير على الصف التالي في القسم. أو، إذا لم يوجد صف تال (لأن الصف الحالي هو الأخير)، يعيد `NULL`.

إذا تم تقديم وسيطة الإزاحة، فيجب أن تكون عددًا صحيحًا غير سالب. القيمة المرجعة هي نتيجة تقييم التعبير على الصف الذي يلي الصف الحالي بعدد *offset* من الصفوف داخل القسم. إذا كانت الإزاحة 0، فيتم تقييم التعبير على الصف الحالي. إذا لم يوجد صف يلي الصف الحالي بعدد *offset* من الصفوف، فإن القيمة المرجعة هي `NULL`.

إذا تم تقديم قيمة افتراضية أيضًا، فإن الدالة تعيدها بدلًا من `NULL` إذا لم يوجد الصف الذي تحدده الإزاحة.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## بناء الجملة {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expr*__
التعبير المراد تقييمه على الصف اللاحق.

__*offset*__
اختياري. عدد صحيح غير سالب يحدد عدد الصفوف التي يتم التقدم بها إلى الأمام. القيمة الافتراضية: 1.

__*default*__
اختياري. القيمة المراد إرجاعها إذا لم يوجد صف الإزاحة. القيمة الافتراضية: `NULL`.

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `LEAD` داخل كل قسم على حدة.

__*ORDER BY column*__
تحدد ترتيب معالجة الصفوف.

## القيمة المرجعة {#return-values}

تعيد قيمة التعبير بعد تقييمه على الصف اللاحق المحدد، أو `NULL` (أو القيمة الافتراضية) إذا لم يوجد هذا الصف.

* * *

## أمثلة {#examples}

### النظر إلى الإصدار التالي {#look-ahead-to-the-next-release}

```sql
-- Compare each release to the previous and next release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
