---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "يشغل الأمر stackql exec جملة StackQL واحدة أو برنامجًا نصيًا من الجمل، مع علامات لتنسيق الإخراج والمصادقة وسجل الموفرين."
image: "/img/stackql-featured-image.png"
---

أمر يستخدم لتشغيل استعلام StackQL، إما كجملة واحدة أو كدفعة جمل من برنامج نصي بلغة StackQL (ملف IQL).  

* * * 

### بناء الجملة {#syntax}

`stackql exec [query] [flags]`

* * *

### الوسيطات {#arguments}

| الوسيطة | الوصف | مثال |
|--|--|--|
|`query`|استعلام StackQL المراد إجراؤه<br/>أو الأمر المراد تنفيذه | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-b'"`<br/>_لاحظ أنه يجب وضع الاستعلام بين علامتي اقتباس مفردتين أو مزدوجتين_ |    


### العلامات {#flags}

| العلامة | الوصف |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|وضع الإخراج، وتشمل القيم الصالحة: `table` (افتراضي) و`json` و`jsonl` (JSON مفصول بأسطر جديدة، ويُقبل `ndjson` كاسم مستعار) و`csv` و`text` و`otel` (إدخالات سجل OpenTelemetry، راجع [أوضاع الإخراج](/getting-started/output-modes#otel-output))، مثال: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|تشغيل الاستعلام من الملف المحدد عند الحاجة، وعند تحديد هذه العلامة يتم تجاهل أي استعلام مقدم كوسيطة، مثال: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|بيانات المصدر للأمر باستخدام ملف `json` أو `jsonnet`، مثال: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|علامة التشغيل التجريبي؛ يعمل المعالج المسبق فقط ويعاد الإخراج |
|<span class="nowrap">`--var`</span>|متغيرات خارجية (من متغيرات البيئة أو الأسرار) تحملها ملفات jsonnet المحددة باستخدام العلامة `--iqldata`، مثال `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|الملف الذي تكتب فيه النتائج عند الحاجة (السلوك الافتراضي هو إخراج النتائج إلى وحدة التحكم فقط)، مثال `--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|طباعة معلومات التعليمات|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|تشغيل الاستعلامات في الوضع المفصل مع إرسال إخراج إضافي إلى stdout، وإذا تم تحديد الخيار `-f` فستكتب معلومات التسجيل الإضافية هذه في ملف الإخراج مع نتائج الاستعلام|
<br/>
<br/>
> راجع [العلامات العامة](/command-line-usage/global-flags) للاطلاع على خيارات إضافية


* * *

### أمثلة {#examples}

إخراج نتائج الاستعلام إلى وحدة التحكم بتنسيق جدول:
```shell
stackql exec "SHOW services IN google"
```

إخراج نتائج الاستعلام إلى وحدة التحكم بتنسيق json:
```shell
stackql exec "SHOW services IN google" -o json
```

إخراج نتائج الاستعلام إلى ملف بتنسيق json:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

تنفيذ استعلام من ملف إدخال (ملف IQL) وإخراج النتائج إلى ملف بتنسيق json:
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### حالة الخروج {#exit-status}

يخرج الأمر `exec` بالحالة `0` بعد معالجة الإدخال؛ وتكتب الأخطاء الناتجة عن الجمل الفردية إلى وحدة التحكم، لذا يجب أن تفحص البرامج النصية الإخراج.  من الإصدار `v1.0`، يخرج الأمر بالحالة `1` عند فشل جملة تكوين [`SET`](/language-spec/set) أو [`UNSET`](/language-spec/unset)، أو عندما يبلغ تشغيل IaC في مرحلة المعاينة عن فشل مورد، أو عند فشل أي جملة أو رفضها في حال تمكين [إدارة الوصول](/command-line-usage/access-management).
