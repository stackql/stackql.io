---
title: جلسات الموفرين والأسرار
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - جلسات الموفرين
  - حسابات متعددة
  - بيانات الاعتماد
  - الأسرار
  - keychain
description: "جلسات الموفرين في StackQL: ربط أسماء مستعارة للموفرين ببيانات اعتماد وtenancy وقيم افتراضية للمعلمات خاصة بها، وتجاوز حقول بيانات الاعتماد لكل جلسة، وحفظ الأسرار في ذاكرة الجلسة أو مخزن أسرار نظام التشغيل."
image: "/img/stackql-featured-image.png"
---

راجع أيضًا:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ متغيرات الجلسة ]](/getting-started/session-variables) [[ العلامات العامة ]](/command-line-usage/global-flags)

يمكن للجلسة ربط موفر باسم مستعار له بيانات اعتماد وتسمية tenancy وقيم افتراضية للمعلمات وحدود للطلبات المتزامنة خاصة به، بحيث يمكن الاستعلام عن عدة حسابات أو مستأجرين للموفر نفسه في جلسة واحدة، بل وفي جملة واحدة.  يمكن أيضًا تجاوز حقول بيانات الاعتماد للجلسة، ويمكن الاحتفاظ بالأسرار في ذاكرة الجلسة أو، بتفعيل اختياري من المشغل، في مخزن الأسرار الخاص بنظام التشغيل.  يتم تكوين كل ذلك باستخدام [`SET`](/language-spec/set) و[`UNSET`](/language-spec/unset)، وهو متاح في StackQL من الإصدار `v1.0` فصاعدًا.

يقتصر نطاق كل ما هو موضح هنا على الجلسة الحالية.  لا تشترك الاتصالات المستقلة بخادم `stackql srv` في عمليات الربط أو الأسرار، وكل طلب MCP جلسة مستقلة بذاته، لذا نفّذ التكوين والاستعلام في دفعة جمل واحدة عند استخدام خادم MCP.

## الأسماء المستعارة للموفرين {#provider-aliases}

تربط الجملة `SET "$.providers.<alias>"` اسمًا مستعارًا بموفر.  القيمة مستند JSON محفوظ في سلسلة نصية:

| المفتاح | مطلوب | الوصف |
|--|--|--|
| `provider` | نعم | الموفر المعياري الذي يُحل إليه الاسم المستعار، على سبيل المثال `aws`. |
| `tenancy` | نعم | تسمية للتجميع مثل اسم حساب أو مستأجر.  لا يتحقق StackQL منها مقابل الموفر. |
| `auth` | أحد `auth` أو `auth_secret` | مستند مصادقة بتنسيق الاسم المستعار، وهو يختلف عن إدخال الموفر في الكائن [`--auth`](/command-line-usage/global-flags): بالنسبة إلى AWS تكون المفاتيح `access_key_id_env_var` و`secret_access_key_env_var`، لا `keyIDenvvar` و`credentialsenvvar`، ويفشل أي مفتاح لا يعرفه تنسيق الاسم المستعار بالخطأ `invalid session configuration field or type`.  لا يستعير الاسم المستعار بيانات اعتماد الموفر المعياري. |
| `auth_secret` | أحد `auth` أو `auth_secret` | اسم [سر جلسة](#session-secrets) يحتوي على مستند المصادقة. |
| `params` | لا | القيم الافتراضية لمعلمات الموفر المعلنة، بما في ذلك معلمات الخادم المعلنة.  يتجاوز الشرط الموجود في الاستعلام القيمة الافتراضية. |
| `tuning` | لا | الحدان `max_concurrent` و`max_per_host` لطلبات الاسم المستعار.  تشترك الأسماء المستعارة التي تشترك في الموفر وtenancy في عناصر التحكم هذه، ويجب أن تعلن إعدادات أداء متطابقة. |
<br/>
يربط المثال التالي حسابين في AWS تحت اسمين مستعارين منفصلين، يقرأ كل منهما مفاتيحه من متغيرات بيئة بأسماء مختلفة، ثم يستعلم عن الحسابين في جملة واحدة:

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

تُحل مراجع متغيرات البيئة من بيئة عملية StackQL؛ ولا توجد بيئة محلية خاصة بالجلسة.  تحتاج بيانات اعتماد AWS المؤقتة أيضًا إلى حقل الرمز المميز للجلسة أو مرجع متغير البيئة الخاص به.

يتصرف الاسم المستعار مثل موفره في جمل بيانات التعريف وجمل البيانات: تقبل الجمل `SHOW` و`DESCRIBE` و`SELECT` و`INSERT` و`UPDATE` و`DELETE` و`EXEC` جميعها الاسم المستعار بدلًا من اسم الموفر.  لا تشمل الأسماء المستعارة العلاقات المدمجة الأصلية ولا علاقات IaC التي في مرحلة المعاينة.  تعمل فروع `UNION ALL` التي تستخدم أسماء مستعارة مختلفة بشكل تسلسلي.

يأخذ كل استعلام لقطة منفصلة من بيانات اعتماد الاسم المستعار.  يؤثر تغيير الربط في الاستعلامات اللاحقة، لا في استعلام قيد التشغيل بالفعل.  تزيل الجملة `UNSET "$.providers.<alias>"` الربط.

## تجاوز حقول بيانات الاعتماد {#overriding-credential-fields}

تغير الجملة `SET "$.auth.<provider>.<field>"` حقلًا واحدًا في سياق مصادقة الموفر للجلسة، وأسماء الحقول هي مفاتيح إدخال الموفر في `--auth`:

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.password_var";
```

كما هو الحال مع الأسماء المستعارة، ينطبق التغيير على الاستعلامات التي تبدأ بعد `SET`.  تفشل الاستعلامات التي تستهدف موفرًا تم إلغاء تعيين مصادقته، أو لا يملك اسمه المستعار قيمة `auth` قابلة للاستخدام، قبل إرسال أي طلب.

## أسرار الجلسة {#session-secrets}

افتراضيًا، تملك كل جلسة مخزن أسرار في الذاكرة فقط، دون الحاجة إلى أي تكوين.  تخزن الجملة `SET "$.secrets.<name>"` قيمة حرفية نصية، وهي عادة مستند مصادقة بتنسيق JSON، ويشير إليها الاسم المستعار للموفر باستخدام `auth_secret`:

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

يؤثر تدوير السر في الاستعلامات اللاحقة؛ ويؤدي حذفه إلى فشل الاستخدامات اللاحقة.  تحصل الجلسة الجديدة على مخزن جديد فارغ، ويؤدي إنهاء الجلسة إلى تحريره.  تتخلص الجملة `UNSET "$.secret_store"` من ربط المخزن الحالي وتبدأ من جديد بذاكرة جلسة فارغة.

لا يؤدي الاستخدام العادي لبيانات الاعتماد، ولا جمل `SET` التي تخزن المتغيرات أو التكوين، إلى كتابة الأسرار في واجهة SQL الخلفية.  وضع البيانات الحساسة في متغير أو جدول عادي مسؤولية الجهة المستدعية.

## مخازن الأسرار الدائمة في نظام التشغيل {#persistent-os-secret-stores}

التخزين الدائم معطل افتراضيًا، ولا يمكن تمكينه إلا من قبل المشغل عند تشغيل StackQL بالعلامة `--allow-persistent-secrets`؛ ولا يمكن لـ SQL منحه.  عند تحديد هذه العلامة، تختار الجلسة مخزنا في نظام التشغيل باستخدام:

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

المخزن هو macOS Keychain أو Linux Secret Service (`secret-tool` مع خدمة جلسة تعمل) أو Windows Credential Manager.  الواجهة الخلفية الوحيدة المقبولة هي `os`: إذ يتم رفض المخازن القائمة على الملفات المسطحة ومسارات الملفات وأي بديل احتياطي ضمني يعتمد على ملف حتى عند تحديد العلامة، وعند عدم توفر الخدمة في نظام التشغيل تفشل العملية بدلًا من الرجوع إلى ملف.

تبقى القيم المكتوبة في مخزن نظام التشغيل بعد انتهاء جلسة SQL، وتحذف صراحة باستخدام `UNSET "$.secrets.<name>"`.  لا تؤدي إعادة تعيين ربط المخزن إلى حذف القيم المحفوظة بالفعل.  يخضع الوصول إلى المخزن لهوية نظام التشغيل الخاصة بعملية StackQL؛ ومساحة الاسم ليست آلية للتحكم في الوصول لكل مستخدم، لذا لا تتعامل مع هذا التفعيل الاختياري كخزنة متعددة المستخدمين.

## قيم الأسرار في SQL {#secret-values-in-sql}

لا يتم إلغاء مرجعية قيم الأسرار في SQL أبدًا ما لم يشغل المشغل StackQL بالعلامة `--allow-secret-dereference`.  مع هذه العلامة، تُحل مراجع مثل `"$secrets.production"` و`"$secrets.production.credentials"` و`"$auth.github.password"` و`"$providers.production.auth.credentials"` في التعبيرات:

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

عند التشغيل دون هذه العلامة، يتم رفض الجملة نفسها قبل تقييم أي شيء، كما توضح النسخة الموجودة داخل كتلة التعليق.  تمثل العلامة إقرارا بأن القيم التي تم إلغاء مرجعيتها صراحة يمكن أن تدخل إلى التخزين المرحلي في SQL والنتائج والسجلات والملفات الناتجة المحفوظة للاستعلامات.  لا تتطلبها المصادقة العادية للموفر.

## الأذونات {#permissions}

عند تمكين [إدارة الوصول](/command-line-usage/access-management)، تتطلب قراءة المتغيرات والأسرار الإذن `session/variables:read`، ويتطلب `SET` أو `UNSET` الإذن `session/config:write`.  يتم تخويل الاسم المستعار الذي تم حله باعتباره موفره المعياري قبل التنفيذ.  لم يتم تنفيذ الملكية والمنح على مستوى كل متغير وكل سر.
