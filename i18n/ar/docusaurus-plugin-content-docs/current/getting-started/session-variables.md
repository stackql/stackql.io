---
title: متغيرات الجلسة
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - متغيرات الجلسة
  - set
  - unset
  - identifier
description: "متغيرات الجلسة في StackQL: تعيين قيم مفردة ذات أنواع باستخدام SET، وإلغاء مرجعيتها باستخدام $name في الشروط والتعبيرات، واستخدامها كأسماء للعلاقات والأعمدة باستخدام IDENTIFIER."
image: "/img/stackql-featured-image.png"
---

راجع أيضًا:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ جلسات الموفر ]](/getting-started/provider-sessions) [[ استخدام المتغيرات ]](/getting-started/variables)

متغيرات الجلسة قيم مفردة تحتفظ بها الجلسة الحالية وتشير إليها الجمل اللاحقة.  وهي متاحة في StackQL من الإصدار `v1.0` فصاعدًا، وتعمل في واجهة الأوامر التفاعلية، وفي البرامج النصية لـ `exec`، وعبر بروتوكول الاتصال الخاص بـ PostgreSQL، ومن خلال خادم MCP.

تختلف متغيرات الجلسة عن متغيرات القوالب الموضحة في [استخدام المتغيرات](/getting-started/variables).  يتم تمرير متغيرات القوالب إلى `stackql exec` عبر `--iqldata` و`--var`، ويتم إنشاء نص البرنامج النصي من القالب بقيمها قبل تحليله.  أما متغيرات الجلسة فتُحل داخل الجملة بعد تحليلها، وتحتفظ بنوعها، ويمكن تغييرها بين الجمل.

## تعيين المتغيرات وإزالتها {#setting-and-removing-variables}

تعين الجملة [`SET`](/language-spec/set) سلسلة نصية أو رقما أو قيمة منطقية أو `null`.  تزيل الجملة [`UNSET`](/language-spec/unset) متغيرا؛ والجملة `SET name = DEFAULT` مكافئة لها.  القيمة `null` الصريحة قيمة مخزنة، وليست إعادة تعيين.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

المصفوفات والكائنات ليست قيمًا مفردة.  للاحتفاظ بمستند JSON، خزّنه كسلسلة نصية:

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## إلغاء مرجعية متغير {#dereferencing-a-variable}

استخدم `$name`، دون علامات اقتباس، في أي موضع للتعبير.  تُكتب السلاسل النصية كقيم حرفية في SQL مع إلغاء أحرفها الخاصة؛ وتحتفظ الأرقام والقيم المنطقية و`null` بنوعها.  القيمة `'$name'` بين علامتي اقتباس مفردتين هي النص الحرفي `$name`، وليست إلغاء مرجعية.  يفشل المرجع إلى متغير غير موجود قبل إرسال أي طلب إلى الموفر.

يمكن استخدام القيم التي تم حلها في تعبيرات SQL العادية، بما في ذلك `CAST` و`CONVERT` والدمج باستخدام `||` واستدعاءات الدوال:

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

لا تقبل عمليات التعيين إلا القيم الحرفية المفردة أو المراجع المباشرة؛ طبّق التحويلات والتعبيرات الأخرى في الموضع الذي يستخدم فيه المتغير، لا في جملة `SET`.

## المتغيرات في الشروط {#variables-in-predicates}

يُحل المتغير في عبارة `WHERE` قبل تخطيط الجملة، لذا يتم توجيهه إلى واجهة API الخاصة بالموفر تمامًا كما توجه القيمة الحرفية.  لذلك يمكن توفير المعلمات المطلوبة من المتغيرات:

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

تتم مقارنة القيم ذات الأنواع وفق نوعها.  يؤدي الشرط `WHERE active = $enabled` مع `SET enabled = false` إلى التصفية حسب القيمة المنطقية، والشرط `WHERE id = CAST($some_id AS TEXT)` هو الشكل الذي يجب استخدامه عندما يكون عمود الموفر سلسلة نصية والمتغير رقما.

## المتغيرات كأسماء للكائنات {#variables-as-object-names}

يمكن للمتغيرات أن تسمي العلاقات والأعمدة إلى جانب القيم.  يتم تحويل الأسماء إلى معرفات في الجملة بعد تحليلها ولا تدرج أبدًا في نص SQL، لذا يتم رفض القيمة التي تحتوي على جملة، أو الفارغة، أو التي ليست سلسلة نصية قبل أي وصول إلى الموفر.

| الموضع | الشكل |
|--|--|
| عبارة `FROM` | `FROM $relation_name`، أو حسب المكونات، `FROM $provider.$service.$resource` |
| عمود في تعبير | `IDENTIFIER($column_name)`، راجع [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| اسم مستعار لعمود أو جدول | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| أسماء أعمدة DDL، وقوائم أعمدة `INSERT`، وأهداف `UPDATE` | الشكل `"$name"` بين علامتي اقتباس مزدوجتين |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

تحل عمليات الربط وتعبيرات CTE والاستعلامات الفرعية متغيرات العلاقات الخاصة بها بالشكل نفسه.  تتكون قيم العلاقات المفصولة بنقاط من أربعة مكونات كحد أقصى.  المتغيرات ليست أجزاء عشوائية من SQL: لا يمكن استبدال كلمة أساسية أو قائمة أعمدة مفصولة بفواصل أو عبارة `WHERE` كاملة بمتغير.

## النطاق والعزل {#scope-and-isolation}

تنتمي المتغيرات إلى الجلسة التي عينتها.  للاتصالات المنفصلة بخادم `stackql srv` متغيرات منفصلة، وكذلك الحال لاستدعاءات `exec` المنفصلة.  كل طلب MCP جلسة مستقلة بذاته، لذا يجب إرسال `SET` والاستعلام الذي يستخدمه في دفعة الجمل نفسها.  تبدأ الجلسات الجديدة دون أي متغيرات.

الجلسة هي النطاق الوحيد.  الجملتان `SET SESSION` و`UNSET SESSION` مقبولتان وتعنيان تمامًا ما تعنيه `SET` و`UNSET`؛ وتوجد الكلمة الأساسية من أجل البرامج النصية التي يجب أن تبدو بالشكل نفسه كما في قواعد البيانات التي تختلف فيها إعدادات الجلسة عن الإعدادات العامة.  يتم رفض `GLOBAL`، لأنه لا توجد حالة مشتركة بين الجلسات.

تحل الجمل المعدة مسبقًا المتغيرات عند تنفيذها، لا عند إعدادها، ولا يعاد استخدام الخطة التي تحتوي على قيم جلسة مرتبطة عبر عمليات التنفيذ.

عند تمكين [إدارة الوصول](/command-line-usage/access-management)، تتطلب قراءة متغير الإذن `session/variables:read`، ويتطلب تعيينه أو إعادة تعيينه الإذن `session/config:write`.  لا يمنح حل متغير إلى اسم كائن أي إذن على ذلك الكائن؛ إذ يتم تخويل العلاقة التي تم حلها قبل التنفيذ كالمعتاد.
