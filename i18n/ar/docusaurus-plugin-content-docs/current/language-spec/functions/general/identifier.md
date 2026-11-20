---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - متغيرات الجلسة
  - identifier
description: "الدالة IDENTIFIER في StackQL: حل متغير جلسة يحمل اسم عمود إلى مرجع عمود، لتتمكن المتغيرات من تسمية الأعمدة في الإسقاطات وفي ORDER BY."
image: "/img/stackql-featured-image.png"
---
تحل قيمة سلسلة نصية، تكون في العادة متغير جلسة، إلى اسم عمود.  استخدمها في المواضع التي يُقرأ فيها `$variable` المجرد بدونها كقيمة عددية.  متاح في StackQL من الإصدار `v1.0` فصاعدًا.

راجع أيضًا:  
[[` SET `]](/language-spec/set) [[ متغيرات الجلسة ]](/getting-started/session-variables)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## الوسيطات {#arguments}

__*variable*__  
متغير جلسة، أو قيمة حرفية نصية، يحمل اسم عمود.  تقبل الدالة الاسم المؤهل مثل `t.name`.  تقبل الدالة وسيطة واحدة بالضبط ويجب أن تكون قيمة سلسلة نصية واحدة؛ وترفض الدالة الدمج مثل `'t.' || $column_name`، أو الوسيطة الثانية، أو القيمة الرقمية بالخطأ `IDENTIFIER requires one string value`.  وترفض القيمة الفارغة، أو القيمة التي تحتوي على جملة، بالخطأ `invalid identifier value`، ويفشل المتغير غير المعرف بوصفه غير معرف.  تجري جميع عمليات التحقق هذه قبل إرسال أي طلب إلى الموفر.

## القيمة المرجعة {#return-values}

يحل مرجع إلى العمود المسمى محل الدالة في الجملة بعد تحليلها، لذا فهي تعيد قيمة ذلك العمود.  يجري تحويل الأسماء إلى معرفات، ولا يتم إدخالها أبدًا في نص SQL.  يمكن استخدام مرجع العمود الناتج في أي موضع يمكن استخدام عمود فيه: قائمة التحديد، والعبارات `WHERE` و`GROUP BY` و`ORDER BY`، ووسيطات الدوال، وداخل تعبيرات CTE والاستعلامات الفرعية.

* * *

## أمثلة {#examples}

### إسقاط عمود يرد اسمه في متغير والترتيب حسبه {#project-and-order-by-a-column-named-in-a-variable}

يؤخذ العمود المراد إرجاعه والعمود المراد الترتيب حسبه كلاهما من `column_name`، وتعاد تسمية عمود الإخراج وفق `output_name`.  تلزم الدالة `IDENTIFIER` في قائمة التحديد وفي `ORDER BY` لأن `$column_name` المجرد في هذين الموضعين سيكون السلسلة النصية `'name'`، وليس العمود.  أما الاسم المستعار فيستخدم بدلًا من ذلك الشكل المحاط بعلامات اقتباس مزدوجة.  بعد حل المتغيرات، تصبح الجملة مماثلة لـ `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### التصفية حسب عمود يرد اسمه في متغير {#filter-on-a-column-named-in-a-variable}

يعمل المرجع بعد حله في الشرط كما يعمل في الإسقاط.  هنا يأتي كل من الحقل المراد التصفية حسبه والقيمة المراد مطابقتها من متغيرات: يصبح `IDENTIFIER($filter_column)` العمود `status`، بينما يُمرر `$filter_value` كقيمة حرفية نصية `'RUNNING'`.  الشكلان مختلفان عن قصد، لأن الأول يحدد اسم عمود والثاني يوفر قيمة.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### تأهيل العمود باسم مستعار للجدول {#qualify-the-column-with-a-table-alias}

تُحل القيمة التي تحتوي على نقطة إلى مرجع عمود مؤهل.  يعطى الجدول الاسم المستعار `w`، مع أخذ الاسم المستعار نفسه من متغير، ويصبح `IDENTIFIER($column_name)` هو `w.name`.  هذا هو الشكل الذي ينبغي استخدامه عندما يوجد اسم العمود نفسه على جانبي عملية الربط، وإلا كان المرجع موضع التباس.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### القيم المرفوضة {#rejected-values}

لأن القيمة تتحول إلى اسم وليس إلى نص استعلام، يتم رفض كل ما لا يصلح كاسم قبل تخطيط الجملة، ولا يصل أي طلب إلى الموفر.  تفشل الجملتان الأوليان أدناه بالخطأ `invalid identifier value` وتفشل الثالثة بالخطأ `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> يُحل `$variable` في العبارة `FROM` إلى علاقة دون `IDENTIFIER`، أما الشكل `"$variable"` المحاط بعلامات اقتباس مزدوجة فهو مخصص للأسماء المستعارة وأسماء أعمدة DDL وقوائم أعمدة `INSERT` وأهداف `UPDATE`.
