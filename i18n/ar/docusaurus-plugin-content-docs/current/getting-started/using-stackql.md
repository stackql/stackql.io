---
title: استخدام StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "استخدام StackQL: تشغيل الاستعلامات تفاعليًا في واجهة الأوامر، أو بشكل غير تفاعلي باستخدام exec، أو كخادم، واكتشاف الموفرين والخدمات والموارد أثناء العمل."
image: "/img/stackql-featured-image.png"
---
راجع أيضًا:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

لدى StackQL عدة أوضاع للاستخدام، وهي: واجهة الأوامر التفاعلية في StackQL، أو سطر أوامر StackQL باستخدام جمل StackQL، أو تنفيذ جملة StackQL واحدة أو أكثر في ملف إدخال (ملف IQL).  يتم وصف كل وضع من هذه الأوضاع بمزيد من التفصيل أدناه:

- استخدام واجهة الأوامر التفاعلية في StackQL ([`stackql shell`](/command-line-usage/shell))
- استخدام أداة سطر الأوامر في StackQL ([`stackql exec`](/command-line-usage/exec))
- استخدام ملف إدخال (ملف IQL) ([`stackql exec -i`](/command-line-usage/exec))

### استخدام واجهة الأوامر التفاعلية في StackQL {#using-the-stackql-interactive-shell}
توفر واجهة أوامر StackQL بيئة برمجة تفاعلية للمهندسين لنشر الموارد السحابية والتفاعل معها، كما تتيح التحليل التفاعلي (باستخدام جمل [`SELECT`](/language-spec/select)) لبيئتهم السحابية.  تشبه واجهة الأوامر من الناحية الوظيفية واجهة أوامر MySQL أو غيرها من واجهات أوامر REPL (Read Evaluate Print Loop).  تقبل واجهة الأوامر جمل StackQL الصالحة، وتعيد النتائج إلى المستخدم.  يمكن أن تمتد الجمل على عدة أسطر، ويتم تنفيذها بمجرد إدخال الفاصلة المنقوطة التي تنهيها.

يمكن تشغيل واجهة أوامر StackQL باستخدام الأمر `shell` كما هو موضح أدناه (راجع [StackQL Provider Registry](/providers) للحصول على معلومات حول المصادقة لدى الموفرين):

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
بمجرد دخولك إلى واجهة أوامر تمت مصادقتها لدى مزود خدمات سحابية، يمكنك البدء في تشغيل استعلامات StackQL (استعلامات `SELECT` أو عمليات DDL، أي عمليات البنية التحتية كتعليمات برمجية).

افتراضيًا، تعيد واجهة أوامر StackQL النتائج في شكل جدولي، إلا أن تنسيقات الإخراج `json` و`jsonl` و`csv` و`text` متاحة باستخدام العلامة `--output`، راجع [أوضاع الإخراج](/getting-started/output-modes).  

### ملف التهيئة `.stackqlrc` {#the-stackqlrc-initialization-file}

يمكن تعيين القيم الافتراضية لخيارات أوامر StackQL، مثل تنسيق الإخراج، باستخدام ملف التهيئة `.stackqlrc`.  إذا كان هذا الملف موجودًا في الدليل الحالي الذي يستخدمه الملف التنفيذي لـ StackQL، فسيتم تحميله تلقائيًا، أو يمكن بدلًا من ذلك الإشارة إليه باستخدام العلامة `--configfile` كما هو موضح أدناه:

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
تتوفر مزيد من المعلومات حول خيارات الأوامر في [العلامات العامة](/command-line-usage/global-flags).

### استخدام أداة سطر الأوامر في StackQL {#using-the-stackql-command-line-utility}
يمكن تنفيذ أوامر StackQL بشكل غير تفاعلي باستخدام الأمر exec في الملف التنفيذي لـ StackQL.  يمكن تنفيذ أكثر من أمر في دفعة واحدة شريطة أن ينتهي كل أمر منفصل بفاصلة منقوطة (`;`).  يمكن تحديد علامات الأوامر، مثل العلامة `--output`، في سطر الأوامر أو باستخدام ملف التهيئة `.stackqlrc` الذي تمت مناقشته سابقًا.  

يتم إرسال الإخراج من الملف التنفيذي لـ StackQL إلى stdout على النظام المستخدم، ويمكن تحليل هذا الإخراج داخل برنامج نصي BASH أو PowerShell.  

يظهر هنا مثال على برنامج نصي Windows PowerShell يتضمن أمر StackQL:  

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$env:GOOGLE_CREDENTIALS = Get-Content -Raw "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from google.compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
يوضح أمر PowerShell أعلاه تحديد مثيلات Compute Engine وعرض الإخراج باستخدام `Out-GridView`.  

### استخدام ملف إدخال (ملف IQL) {#using-an-input-file-iql-file}
يمكن استخدام ملفات إدخال StackQL لتنفيذ عدة أوامر تنتهي بفواصل منقوطة.  وهذا خيار آخر لبرمجة البنية التحتية بشكل غير تفاعلي أو على دفعات.  يتم تحديد ملف الإدخال باستخدام الخيار `-i` أو `--infile` كما هو موضح أدناه:

```shell
# create an iql file
echo "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "/mnt/c/tmp/stackql-demo.json" } }' \
--output json | jq '.[] | .name'
```
