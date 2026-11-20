---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "يفتح الأمر stackql shell واجهة أوامر StackQL التفاعلية لتشغيل الاستعلامات وأوامر الموفرين على حساباتك السحابية وحسابات SaaS."
image: "/img/stackql-featured-image.png"
---

الأمر المستخدم لتشغيل واجهة أوامر StackQL التفاعلية. لمزيد من المعلومات، راجع [استخدام واجهة الأوامر التفاعلية في StackQL](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### بناء الجملة {#syntax}

`stackql shell [flags]`

* * *

### العلامات {#flags}

| العلامة | الوصف |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|طباعة معلومات المساعدة|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|تشغيل الاستعلامات في الوضع المفصل مع إرسال إخراج إضافي إلى stdout، وإذا تم تعيين العلامة العامة `-f` (`--outfile`) فستتم كتابة معلومات التسجيل الإضافية هذه في ملف الإخراج مع نتائج الاستعلام|
<br/>
<br/>
> راجع [العلامات العامة](/command-line-usage/global-flags) للاطلاع على خيارات إضافية

* * *

### أمثلة {#examples}

تشغيل واجهة أوامر StackQL باستخدام ملف مفتاح حساب الخدمة للمصادقة.
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
