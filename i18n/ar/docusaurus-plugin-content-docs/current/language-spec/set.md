---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - متغيرات الجلسة
  - set
  - جلسات الموفر
  - الأسرار
description: "SET في StackQL: تعيين متغيرات جلسة ذات أنواع محددة، وتكوين الأسماء المستعارة للموفرين وحقول بيانات الاعتماد والأسرار للجلسة الحالية عبر أهداف مسبوقة بعلامة الدولار."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تعين متغير جلسة، أو تحدد إعدادات وقت التشغيل للجلسة الحالية. متاح في StackQL من الإصدار `v1.0` فصاعدًا.

راجع أيضًا:  
[[` UNSET `]](/language-spec/unset) [[ متغيرات الجلسة ]](/getting-started/session-variables) [[ جلسات الموفر ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## بناء الجملة {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
معرف عادي (`region`)، أو اسم بين علامتي اقتباس مزدوجتين (`"providers.saved"`)، أو مسار تكوين مسبوق بعلامة الدولار بين علامتي اقتباس مزدوجتين (`"$.auth.aws.password"`).

__*literal*__  
سلسلة نصية أو رقم أو قيمة منطقية أو `null`. توضع السلاسل النصية بين علامتي اقتباس مفردتين. المصفوفات والكائنات ليست قيمًا عددية؛ احتفظ بمستند JSON كسلسلة نصية.

__*DEFAULT*__  
يعيد تعيين الهدف، وهو ما يعادل [`UNSET`](/language-spec/unset). القيمة `null` الصريحة قيمة مخزنة، وليست إعادة تعيين.

__*reference*__  
مرجع `"$name"` بين علامتي اقتباس مزدوجتين إلى متغير موجود، على سبيل المثال `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
اختياري، وهو النطاق الوحيد في StackQL. `SET SESSION name = ...` و`SET name = ...` هما الجملة نفسها: ينتمي كل تعيين إلى الجلسة الحالية ولا ينتمي إلى أي شيء آخر. توجد هذه الكلمة الأساسية لكي تُقرأ البرامج النصية كما تُقرأ في قواعد البيانات التي تختلف فيها `SET SESSION` عن `SET GLOBAL`. يتم تحليل `SET GLOBAL` لكنها تُرفض مع الخطأ `configuration scope "global" is not supported; use SESSION`، لأنه لا توجد حالة مشتركة بين الجلسات.

تُطبق التعيينات في قائمة `SET` واحدة بالترتيب ولا تشكل معاملة: إذا فشل التعيين الثاني في القائمة، يظل التعيين الأول ساريًا.

## الأهداف {#targets}

| الهدف | التأثير |
|--|--|
| `name` أو `"name"` | يخزن متغير جلسة عدديًا يتم إلغاء مرجعيته باستخدام `$name` في الجمل اللاحقة. النقاط وحدها لا تحدد التكوين: `"providers.saved"` متغير اسمه `providers.saved`. الكتابة الأقدم `"variables.name"` اسم مستعار لـ `name`. |
| `"$.auth.<provider>.<field>"` | يعين حقلًا واحدًا من سياق المصادقة الخاص بالموفر لهذه الجلسة. الحقول هي مفاتيح الكائن [`--auth`](/command-line-usage/global-flags) لذلك الموفر، على سبيل المثال `credentialsenvvar` أو `password` أو `token_url`. |
| `"$.providers.<alias>"` | يربط اسمًا مستعارًا لموفر ببيانات اعتماد خاصة به وتسمية tenancy وقيم افتراضية للمعلمات وإعدادات الأداء. القيمة مستند JSON داخل سلسلة نصية، راجع [جلسات الموفر](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | يخزن سرًا في مخزن الأسرار الخاص بالجلسة. القيمة قيمة حرفية نصية، وهي في العادة مستند `auth` بتنسيق JSON، ويشار إليها من الاسم المستعار للموفر بـ `auth_secret`. |
| `"$.secret_store"` | يحدد مخزن الأسرار الدائم الخاص بنظام التشغيل، `{"backend":"os","namespace":"..."}`. يتطلب العلامة `--allow-persistent-secrets` التي يحددها المشغل. |

يؤثر كل شكل في الجلسة الحالية فقط. لا ترى الاتصالات المستقلة بخادم [`srv`](/command-line-usage/srv) تعيينات الاتصالات الأخرى، وكل طلب MCP هو جلسة مستقلة بذاتها، لذا نفّذ التكوين والاستعلام في دفعة جمل واحدة عند استخدام خادم MCP.

* * *

## أمثلة {#examples}

### تعيين متغيرات الجلسة {#set-session-variables}

تخزن كل جملة قيمة عددية واحدة تحت الاسم المحدد، وتحتفظ القيمة بنوعها: `region` سلسلة نصية، و`minimum_count` عدد صحيح، و`enabled` قيمة منطقية. يتم تخزين `null` كقيمة، لذا يُحل `$optional_value` لاحقًا إلى `NULL` في SQL بدلًا من أن يفشل لكونه غير معرف. تعين الجملة الأخيرة متغيرين في قائمة واحدة.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### تحديد نطاق الجلسة صراحة {#make-the-session-scope-explicit}

الجملتان الأوليان متطابقتان: تسمي `SESSION` النطاق الوحيد الموجود، ويستمر كلا التعيينين حتى يتم إلغاء تعيين المتغير أو تنتهي الجلسة. استخدم هذه الكلمة الأساسية عندما ينبغي أن يُقرأ البرنامج النصي كما يُقرأ في قاعدة بيانات تميز بين إعدادات الجلسة والإعدادات العامة. تُعرض الجملة الموجودة داخل كتلة التعليق للمقارنة: فهي لا تعين شيئًا وتفشل مع الخطأ `configuration scope "global" is not supported; use SESSION`، لأنه لا تتم مشاركة أي حالة بين الجلسات.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### استخدام متغير في استعلام {#use-a-variable-in-a-query}

استخدم `$name` بدون علامات اقتباس لإلغاء مرجعية متغير في أي موضع من مواضع التعبيرات. تحتفظ القيم بنوعها: تُدرج السلاسل النصية كقيم حرفية في SQL مع إلغاء أحرفها الخاصة، وتمرر الأرقام والقيم المنطقية و`null` كما هي. يتم حل المتغيرات في عبارة `WHERE` قبل تخطيط الطلب، لذا يتم دفعها إلى الموفر مثل القيم الحرفية.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### إعادة تعيين متغير {#reset-a-variable}

تزيل `DEFAULT` المتغير، تمامًا كما تفعل [`UNSET`](/language-spec/unset)، لذا يفشل أي `$region` لاحق مع الخطأ `variable "$region" is not defined` حتى يتم تعيينه مرة أخرى؛ ويوضح الاستعلام الموجود داخل كتلة التعليق هذا الفشل. يختلف هذا عن تعيين `null`، الذي يترك المتغير في مكانه بقيمة `NULL`.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### نسخ قيمة من متغير آخر {#copy-a-value-from-another-variable}

يمكن أن يكون الطرف الأيمن مرجعًا بين علامتي اقتباس مزدوجتين إلى متغير موجود، وهو ما ينسخ القيمة التي يحملها ذلك المتغير في تلك اللحظة. هنا يتم أولًا تخزين ربط موفر كمتغير عادي اسمه `providers.saved` (النقاط لا تجعله تكوينًا)، ثم يتم تنشيطه بنسخه إلى المسار `$.providers.production`. لا تؤثر إزالة النسخة المحفوظة بعد ذلك في الربط النشط.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### تجاوز حقل بيانات اعتماد لهذه الجلسة {#override-a-credential-field-for-this-session}

سياق المصادقة الخاص بالموفر هو الكائن الذي يتم تمريره له عبر [`--auth`](/command-line-usage/global-flags) عند بدء التشغيل. يستبدل المسار الموجود تحت `$.auth` حقلًا واحدًا من ذلك الكائن لبقية الجلسة ويترك الحقول الأخرى كما كانت. هنا يتم توجيه موفر GitHub إلى متغير بيئة مختلف يحمل رمزًا مميزًا للقراءة فقط؛ تتم قراءة المتغير من بيئة عملية StackQL، وتستخدمه الاستعلامات اللاحقة على `github.*` حتى يتم إلغاء تعيين الحقل أو تنتهي الجلسة.

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
```

### ربط اسم مستعار لموفر {#bind-a-provider-alias}

الاسم المستعار للموفر هو مثيل ثان من الموفر يتم تكوينه بشكل مستقل، ويُستخدم بدلًا من اسم الموفر في الاستعلامات. القيمة مستند JSON داخل سلسلة نصية: يسمي `provider` الموفر الأساسي، ويعطي `tenancy` تسمية للحساب، و`auth` كتلة بيانات اعتماد بالشكل نفسه المستخدم في `--auth`، ويوفر `params` قيمًا افتراضية لمعلمات الطلب مثل `region`. بعد ذلك تعمل الاستعلامات على `production.s3.buckets` باستخدام بيانات الاعتماد هذه، بينما يحتفظ `aws.s3.buckets` بتكوين بدء التشغيل. توجد القائمة الكاملة للمفاتيح في [جلسات الموفر](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
