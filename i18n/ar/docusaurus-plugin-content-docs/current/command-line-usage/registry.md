---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "يسرد الأمر stackql registry الموفرين ويسحبهم ويديرهم من سجل الموفرين الخاص بـ StackQL عبر سطر الأوامر."
image: "/img/stackql-featured-image.png"
---

الأمر المستخدم للتفاعل مع StackQL Provider Registry. يتم تكوين موقع السجل، وما إذا كان يتم التحقق من توقيعات مستندات الموفرين، باستخدام العلامة العامة [`--registry`](/command-line-usage/global-flags#--registry). لا يتوفر الأمر `registry` أثناء تمكين [إدارة الوصول](/command-line-usage/access-management)؛ استخدم `REGISTRY PULL` في جلسة `exec` أو `shell` محمية بدلًا من ذلك.

* * * 

### بناء الجملة {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### الوسيطات {#arguments}

| الوسيطة | الوصف | مثال |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|عملية السجل المراد تنفيذها (إما `list` أو `pull`) | `list` أو `pull` |    
|<span class="nowrap">`provider`</span>|الموفر المراد تثبيته باستخدام الأمر الفرعي `pull`| `okta` |
|<span class="nowrap">`version`</span>|إصدار اختياري للموفر المراد تثبيته باستخدام الأمر الفرعي `pull` *(إصدار مستند موفر StackQL)* | `v23.03.00121` |


### العلامات {#flags}

| العلامة | الوصف |
|--|--|
|<span class="nowrap">`-h, --help`</span>|طباعة معلومات المساعدة|
<br/>
<br/>
> راجع [العلامات العامة](/command-line-usage/global-flags) للاطلاع على خيارات إضافية

* * *

### أمثلة {#examples}

سرد الموفرين المتاحين في السجل العام:
```shell
stackql registry list
```

سحب أحدث إصدار من موفر من السجل العام:
```shell
stackql registry pull okta
```
