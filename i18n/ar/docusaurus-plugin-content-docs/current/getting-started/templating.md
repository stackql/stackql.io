---
title: إنشاء قوالب البنية التحتية
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "إنشاء قوالب INSERT لأي مورد باستخدام SHOW INSERT في StackQL، كاملة أو بالمعلمات المطلوبة فقط، كنقطة بداية لتعريفات البنية التحتية."
image: "/img/stackql-featured-image.png"
---
راجع أيضًا:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ استخدام المتغيرات ]](/getting-started/variables)

يتم نشر الموارد السحابية باستخدام StackQL عبر جمل `INSERT`.  يمكن إنشاء قوالب `INSERT` لمختلف الموارد السحابية باستخدام الأمر `SHOW INSERT`.

### إنشاء قالب `INSERT` باستخدام الأمر `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

يظهر هنا مثال على إنشاء قالب:

```sql
SHOW INSERT INTO google.compute.addresses;
```
يمكن تشغيل هذا من خلال `shell` أو باستخدام الأمر `exec` كما هو موضح هنا:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__القيمة المرجعة:__*  

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

> يمكن بعد ذلك استخدام هذا القالب مع ملف بيانات `json` أو `jsonnet` لتوفير القيم في وقت التشغيل؛ لمزيد من المعلومات، راجع [استخدام المتغيرات](/getting-started/variables).

### إنشاء قالب `INSERT` بالمعلمات المطلوبة فقط {#generating-an-insert-template-with-only-required-parameters}

لإنشاء قالب `INSERT` في StackQL يحتوي على السمات الإلزامية فقط، استخدم تلميح الاستعلام `/*+ REQUIRED */` كما هو موضح هنا:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
يمكن تشغيل هذا من خلال `shell` أو باستخدام الأمر `exec` كما هو موضح هنا:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__القيمة المرجعة:__*  

```sql
INSERT INTO google.compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```
