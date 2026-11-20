---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة LAG في StackQL: تقييم تعبير على صف سابق في القسم، مع إزاحة وقيمة افتراضية اختياريتين."
image: "/img/stackql-featured-image.png"
---
تعيد نتيجة تقييم تعبير على صف سابق في القسم.

يعيد الشكل الأول من الدالة `LAG()` نتيجة تقييم التعبير على الصف السابق في القسم. أو، إذا لم يوجد صف سابق (لأن الصف الحالي هو الأول)، يعيد `NULL`.

إذا تم تقديم وسيطة الإزاحة، فيجب أن تكون عددًا صحيحًا غير سالب. القيمة المرجعة هي نتيجة تقييم التعبير على الصف الذي يسبق الصف الحالي بعدد *offset* من الصفوف داخل القسم. إذا كانت الإزاحة 0، فيتم تقييم التعبير على الصف الحالي. إذا لم يوجد صف يسبق الصف الحالي بعدد *offset* من الصفوف، فإن القيمة المرجعة هي `NULL`.

إذا تم تقديم قيمة افتراضية أيضًا، فإن الدالة تعيدها بدلًا من `NULL` إذا لم يوجد الصف الذي تحدده الإزاحة.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## بناء الجملة {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expr*__
التعبير المراد تقييمه على الصف السابق.

__*offset*__
اختياري. عدد صحيح غير سالب يحدد عدد الصفوف التي يتم الرجوع بها إلى الوراء. القيمة الافتراضية: 1.

__*default*__
اختياري. القيمة المراد إرجاعها إذا لم يوجد صف الإزاحة. القيمة الافتراضية: `NULL`.

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `LAG` داخل كل قسم على حدة.

__*ORDER BY column*__
تحدد ترتيب معالجة الصفوف.

## القيمة المرجعة {#return-values}

تعيد قيمة التعبير بعد تقييمه على الصف السابق المحدد، أو `NULL` (أو القيمة الافتراضية) إذا لم يوجد هذا الصف.

* * *

## أمثلة {#examples}

### مقارنة كل إصدار بالإصدار السابق {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to the previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### تحليل الفجوات بين عمليات إنشاء المشكلات {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
