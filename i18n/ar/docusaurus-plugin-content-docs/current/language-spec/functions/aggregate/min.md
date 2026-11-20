---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة التجميعية MIN في StackQL: أصغر قيمة في عمود أو مجموعة من الأعمدة."
image: "/img/stackql-featured-image.png"
---
تعيد القيمة الدنيا استنادًا إلى عمود إدخال أو مجموعة من الأعمدة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

### دالة تجميعية {#aggregate-function}

__*columnExpression*__  
عمود أو تعبير يتضمن عوامل (`+`، `-`، ...).

> تعيد الدالة `MIN` أصغر قيمة للعمود المحدد استنادًا إلى نوع بيانات العمود، فعلى سبيل المثال تعيد عملية `MIN` على عمود أعداد صحيحة أصغر عدد صحيح في ذلك العمود أو في مجموعة الأعمدة، بينما تعيد عملية `MIN` على عمود سلاسل نصية أو مجموعة أعمدة القيمة التي تأتي أولًا في الترتيب، مع مقارنة السلاسل النصية باستخدام دالة الترتيب `BINARY` (بايت ببايت، لذا تأتي الأحرف الكبيرة قبل الأحرف الصغيرة في الترتيب).

__*groupByColumn*__  
عمود أو أعمدة تجري عليها عمليات التلخيص أو التجميع.  تعيد عبارة `GROUP BY` صفًا واحدًا لكل مجموعة أعمدة.

> تتجاهل الدالة `MIN` قيم `NULL`.

> تعيد الدالة `MIN` القيمة `NULL` إذا كانت جميع القيم في المجموعة `NULL`.

### دالة عددية {#scalar-function}

__*scalarExpression*__  
قائمة من الأعمدة أو التعبيرات (2 أو أكثر) تحدد الدالة منها أصغر قيمة وتعيدها. 

> تعيد الدالة العددية `MIN` القيمة `NULL` إذا كانت أي وسيطة `NULL`. 

> تبحث الدالة `MIN` العددية أو متعددة الوسيطات في وسيطاتها من اليسار إلى اليمين عن وسيطة تحدد دالة ترتيب، وتستخدم دالة الترتيب هذه في جميع مقارنات السلاسل النصية. 

> إذا لم تحدد أي من وسيطات الدالة العددية `MIN` دالة ترتيب، تستخدم الدالة دالة الترتيب `BINARY`. 

> إذا قدمت وسيطة واحدة فقط، تستدعى الدالة التجميعية `MIN`.

## القيمة المرجعة {#return-values}

تعيد القيمة الدنيا استنادًا إلى نوع بيانات الإدخال (أو المجموعة).

* * *

## أمثلة {#examples}

### إرجاع القيمة الدنيا لعمود {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### إرجاع القيمة الدنيا لكل قيمة في عمود {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### إرجاع القيمة الدنيا من قائمة قيم باستخدام الدالة العددية `min` {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### استخدام `MIN` كدالة نافذة للعثور على القيم الدنيا داخل الأقسام {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) أو [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)
