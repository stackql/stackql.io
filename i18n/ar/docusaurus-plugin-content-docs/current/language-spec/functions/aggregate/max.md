---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة التجميعية MAX في StackQL: أكبر قيمة في عمود أو مجموعة من الأعمدة."
image: "/img/stackql-featured-image.png"
---
تعيد القيمة القصوى استنادًا إلى عمود إدخال أو مجموعة من الأعمدة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

### دالة تجميعية {#aggregate-function}

__*columnExpression*__  
عمود أو تعبير يتضمن عوامل (`+`، `-`، ...).

> تعيد الدالة `MAX` أكبر قيمة للعمود المحدد استنادًا إلى نوع بيانات العمود، فعلى سبيل المثال تعيد عملية `MAX` على عمود أعداد صحيحة أكبر عدد صحيح في ذلك العمود أو في مجموعة الأعمدة، بينما تعيد عملية `MAX` على عمود سلاسل نصية أو مجموعة أعمدة القيمة التي تأتي أخيرًا في الترتيب، مع مقارنة السلاسل النصية باستخدام دالة الترتيب `BINARY` (بايت ببايت، لذا تأتي الأحرف الكبيرة قبل الأحرف الصغيرة في الترتيب).

__*groupByColumn*__  
عمود أو أعمدة تجري عليها عمليات التلخيص أو التجميع.  تعيد عبارة `GROUP BY` صفًا واحدًا لكل مجموعة أعمدة.

> تتجاهل الدالة `MAX` قيم `NULL`.

> تعيد الدالة `MAX` القيمة `NULL` إذا كانت جميع القيم في المجموعة `NULL`.

### دالة عددية {#scalar-function}

__*scalarExpression*__  
قائمة من الأعمدة أو التعبيرات (2 أو أكثر) تحدد الدالة منها أكبر قيمة وتعيدها. 

> تعيد الدالة العددية `MAX` القيمة `NULL` إذا كانت أي وسيطة `NULL`. 

> تبحث الدالة `MAX` العددية أو متعددة الوسيطات في وسيطاتها من اليسار إلى اليمين عن وسيطة تحدد دالة ترتيب، وتستخدم دالة الترتيب هذه في جميع مقارنات السلاسل النصية. 

> إذا لم تحدد أي من وسيطات الدالة العددية `MAX` دالة ترتيب، تستخدم الدالة دالة الترتيب `BINARY`. 

> إذا قدمت وسيطة واحدة فقط، تستدعى الدالة التجميعية `MAX`.

## القيمة المرجعة {#return-values}

تعيد القيمة القصوى استنادًا إلى نوع بيانات الإدخال (أو المجموعة).

* * *

## أمثلة {#examples}

### إرجاع القيمة القصوى لعمود {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### إرجاع القيمة القصوى لكل قيمة في عمود {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### إرجاع القيمة القصوى من قائمة قيم باستخدام الدالة العددية `max` {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### استخدام `MAX` كدالة نافذة للعثور على القيم القصوى داخل الأقسام {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

```sql
-- Find the most recent issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MAX(created_at) OVER (PARTITION BY state) as latest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at DESC;
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) أو [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)
