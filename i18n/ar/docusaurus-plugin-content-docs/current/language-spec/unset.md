---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - متغيرات الجلسة
  - unset
  - جلسات الموفرين
  - الأسرار
description: "UNSET في StackQL: إزالة متغيرات الجلسة، أو مسح الأسماء المستعارة للموفرين وحقول بيانات الاعتماد والأسرار التي تم تكوينها باستخدام SET للجلسة الحالية."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تزيل متغير جلسة، أو تمسح تكوين وقت التشغيل الذي تم تعيينه باستخدام [`SET`](/language-spec/set) للجلسة الحالية. متاح في StackQL من الإصدار `v1.0` فصاعدًا.

راجع أيضًا:  
[[` SET `]](/language-spec/set) [[ متغيرات الجلسة ]](/getting-started/session-variables) [[ جلسات الموفر ]](/getting-started/provider-sessions)

* * * 

## بناء الجملة {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
أي هدف تقبله الجملة `SET`: اسم متغير عادي أو محاط بعلامتي اقتباس مزدوجتين، أو مسار تكوين محاط بعلامتي اقتباس مزدوجتين ويبدأ بعلامة الدولار.

__*SESSION*__  
اختياري، وهو النطاق الوحيد الموجود: `UNSET SESSION name` و`UNSET name` هما الجملة نفسها، لأن كل متغير وكل إعداد تكوين ينتمي إلى الجلسة الحالية. يتم رفض `UNSET GLOBAL` مع الرسالة `configuration scope "global" is not supported; use SESSION`.

`UNSET name` مكافئة لـ `SET name = DEFAULT`. تزال الأهداف الموجودة في قائمة واحدة بالترتيب، وإزالة اسم غير معين ليست خطأ. إذا ألغيت مرجعية متغير بعد إلغاء تعيينه، تفشل العملية قبل إرسال أي طلب إلى الموفر.

| الهدف | التأثير |
|--|--|
| `name` | تزيل متغير الجلسة. |
| `"$.auth.<provider>.<field>"` | تحذف هذا الحقل من سياق المصادقة الخاص بجلسة الموفر. لا تتم استعادة القيمة التي كانت سارية قبل التجاوز. |
| `"$.providers.<alias>"` | تزيل ربط الاسم المستعار للموفر. |
| `"$.secrets.<name>"` | تحذف السر من مخزن الجلسة. في مخزن دائم لنظام التشغيل، يؤدي ذلك إلى حذف القيمة المخزنة. |
| `"$.secret_store"` | تلغي ربط المخزن الحالي وتبدأ بذاكرة جلسة فارغة. لا تحذف القيم المحفوظة مسبقًا في مخزن نظام التشغيل. |

* * *

## أمثلة {#examples}

### إزالة متغير {#remove-a-variable}

تتم إزالة المتغير من الجلسة، لذا يفشل الاستعلام الموجود داخل كتلة التعليق مع الخطأ `variable "$region" is not defined`، ولا يعمل بقيمة قديمة أو فارغة. عيّن الاسم مرة أخرى لإعادة استخدامه.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### إزالة عدة أهداف {#remove-several-targets}

يمكن لجملة واحدة إزالة متغيرات ومسارات تكوين معًا، وتتم معالجة الأهداف بالترتيب. هنا تتم إزالة متغير عادي ومتغير يحتوي اسمه على نقاط وتجاوز لبيانات الاعتماد في قائمة واحدة.

```sql
UNSET region, "providers.saved", "$.auth.github.password_var";
```

### تحديد نطاق الجلسة صراحة {#make-the-session-scope-explicit}

تؤدي هاتان الجملتان الشيء نفسه: تسمي الكلمة الأساسية `SESSION` النطاق الوحيد الموجود. يناسب الشكل الصريح البرامج النصية التي تعمل أيضًا على قواعد بيانات تميز بين حالة الجلسة والحالة العامة. تفشل الجملة الموجودة داخل كتلة التعليق لأنه لا توجد حالة مشتركة.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### مسح تجاوز بيانات الاعتماد {#clear-a-credential-override}

يتم حذف الحقل الذي تم تعيينه سابقًا باستخدام `SET "$.auth.github.password_var"` من سياق المصادقة الخاص بجلسة موفر GitHub، وتبقى الحقول الأخرى في هذا السياق كما هي. لا تتم استعادة القيمة التي كانت سارية قبل التجاوز، لذا عيّن الحقل مرة أخرى إذا كان الموفر لا يزال يحتاج إليه.

```sql
UNSET "$.auth.github.password_var";
```

### إزالة اسم مستعار لموفر وسره {#remove-a-provider-alias-and-its-secret}

تم ربط الاسم المستعار `production` باستخدام `SET "$.providers.production"`، وهو يشير، من خلال الحقل `auth_secret`، إلى سر مخزن في `$.secrets.production`. تؤدي إزالة الاثنين في جملة واحدة إلى إسقاط الربط، فلا يعود `production.s3.buckets` علاقة صالحة، وإلى حذف السر من مخزن الجلسة. الكلمة الأساسية `SESSION` اختيارية هنا، كما في كل مكان.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
