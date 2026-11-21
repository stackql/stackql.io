---
title: StackQL في AWS CloudShell
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
  - cspm
  - aws
  - amazon web services
description: "تشغيل StackQL في AWS CloudShell بهوية المستخدم الذي سجل الدخول: تنزيل حزمة Linux، واستخدام الأمر stackql shell، وتولي دور IAM، وتشغيل استعلامات على دفعات باستخدام exec."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# استخدام StackQL في AWS CloudShell

في هذا الدليل، سنوضح كيفية استخدام StackQL، وهو أداة تطوير قوية تتيح الاستعلام عن البنية التحتية والموارد السحابية ونشرها باستخدام بناء جملة SQL، من داخل AWS CloudShell، باستخدام سياق المستخدم الخاص بجلسة تسجيل الدخول إلى AWS Console أو بتولي دور IAM آخر.    

تم الاختبار باستخدام <span class="tutorial_tested_on">واجهة SQL الخلفية المضمنة</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>ما هي <b>AWS CloudShell</b>؟</summary>

> AWS CloudShell هي خدمة واجهة أوامر تعمل في المتصفح ومصادق عليها مسبقًا، تقدمها Amazon Web Services (AWS).  تتيح للمطورين ومحترفي تكنولوجيا المعلومات إدارة مواردهم السحابية مباشرة من متصفحات الويب الخاصة بهم.  باستخدام AWS CloudShell، يمكن للمستخدمين تشغيل الأوامر المتعلقة بخدمات AWS بسرعة دون تثبيت AWS CLI (Command Line Interface) أو تكوينه على أجهزتهم المحلية.  توفر هذه الخدمة بيئة جاهزة للاستخدام تتضمن أدوات التطوير والإدارة المعتادة مثبتة مسبقًا، مثل AWS CLI وPython وNode.js وغيرها، مما يسهل التفاعل مع خدمات AWS وأتمتة المهام.  تتكامل AWS CloudShell مباشرة مع AWS Management Console، وتوفر تجربة سلسة لتنفيذ المهام الإدارية واستكشاف خدمات AWS وإدارة البنية التحتية.  وبما أن AWS CloudShell تجري مصادقة المستخدمين تلقائيًا من خلال تسجيل دخولهم إلى AWS Console، فإنها تبسط عملية تشغيل الأوامر والبرامج النصية في سياق حساب AWS الخاص بهم، مما يعزز الإنتاجية والكفاءة في العمليات السحابية.

</details>

## آلية العمل {#how-it-works}

تتضمن حزمة linux الخاصة بـ StackQL برامج نصية لاستخدام `stackql` مباشرة داخل بيئات واجهة الأوامر السحابية المدمجة لدى كبار مزودي الخدمات السحابية.  بالنسبة إلى AWS، البرنامج النصي هو __`stackql-aws-cloud-shell.sh`__.  يوضح ما يلي استخدام البرنامج النصي __`stackql-aws-cloud-shell.sh`__:  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

يتولى البرنامج النصي `stackql-aws-cloud-shell.sh` تنزيل أحدث موفر AWS لـ StackQL تلقائيًا.  يمكنك العثور على التعليمات البرمجية المصدر للبرامج النصية المرفقة الخاصة بواجهة الأوامر السحابية في [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).  

## تنزيل حزمة `stackql` لنظام Linux من AWS CloudShell {#download-the-stackql-linux-package-from-aws-cloud-shell}

للبدء، نزّل من AWS CloudShell أحدث ملف ثنائي لـ StackQL لنظام Linux باستخدام الأوامر التالية:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

سيؤدي ذلك إلى تنزيل الملف الثنائي `stackql` واستخراجه مع البرنامج النصي `stackql-aws-cloud-shell.sh` إلى الدليل الرئيسي لبيئة AWS CloudShell الخاصة بك.  أنت الآن جاهز للبدء!  

## استخدام الأمر `stackql shell` من AWS CloudShell {#using-the-stackql-shell-from-aws-cloud-shell}

لتشغيل واجهة أوامر StackQL مصادق عليها من داخل AWS CloudShell، يكفي تشغيل ما يلي:  

```shell
sh stackql-aws-cloud-shell.sh
```

يمكنك الآن بدء تشغيل استعلامات AWS تفاعلية قائمة على SQL باستخدام موفر StackQL، كما هو موضح هنا:  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  لا يدعم متصفحك وسم video.
</video>

## تولي دور IAM باستخدام StackQL {#assuming-an-iam-role-using-stackql}

يتيح لك تولي دور IAM في AWS تنفيذ إجراءات والوصول إلى موارد قد لا تتمكن من تنفيذها أو الوصول إليها بأذونات المستخدم الافتراضية الخاصة بك.  ويفيد ذلك بشكل خاص عندما تحتاج إلى وصول مؤقت إلى الموارد أو إلى تنفيذ إجراءات بأذونات مختلفة عن أذونات حسابك الأساسي.  باستخدام StackQL، يمكنك تولي دور IAM بسلاسة للاستعلام عن موارد AWS وإدارتها وفق الأذونات التي يمنحها ذلك الدور.

<details>
<summary>فهم تولي أدوار IAM</summary>

> إن تولي دور IAM وسيلة آمنة لمنح وصول مؤقت إلى موارد AWS.  عند تولي دور، تزودك خدمة AWS STS ببيانات اعتماد أمان مؤقتة تتيح لك الوصول إلى خدمات AWS والموارد التي لدى الدور إذن باستخدامها.  هذه الآلية مثالية للوصول عبر الحسابات، أو الوصول المؤقت للمستخدمين، أو الالتزام بمبدأ الحد الأدنى من الامتيازات.

> يمكن تولي الأدوار باستخدام AWS Management Console أو AWS CLI أو AWS SDKs، ومباشرة داخل StackQL. يضمن ذلك ألا يحصل المستخدمون والتطبيقات إلا على الأذونات اللازمة للمدة المطلوبة، مما يعزز الوضع الأمني لبيئة AWS الخاصة بك.

</details>

لتولي دور IAM باستخدام StackQL في AWS CloudShell، يمكنك استخدام الخيار `--role-arn` مع البرنامج النصي `stackql-aws-cloud-shell.sh`.  يتطلب هذا الخيار اسم مورد Amazon (ARN) للدور الذي تريد توليه.  عند توفيره، سيستخدم StackQL خدمة AWS Security Token Service (STS) لتولي الدور المحدد قبل تنفيذ أي استعلامات أو أوامر.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

بتولي دور، يمكنك الاستفادة من StackQL للتفاعل مع موارد AWS التي قد لا يملك المستخدم الافتراضي الخاص بك حق الوصول إليها، مما يتيح لك إدارة بيئتك السحابية بمرونة وأمان أكبر.  

عند تنفيذ أوامر StackQL أو برامجه النصية باستخدام دور IAM تم توليه، تأكد من أن الدور يملك الأذونات اللازمة لتنفيذ الإجراءات المطلوبة والوصول إلى الموارد المحددة.  ييسر ذلك التحكم الدقيق في الوصول ويتوافق مع أفضل ممارسات AWS للأمان والحوكمة.  

## تشغيل الأمر `stackql exec` (عمليات الدفعات) من AWS CloudShell {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

يمكن أيضًا تنفيذ استعلامات StackQL باستخدام الأمر `exec`؛ وهذا خيار مفيد لما يلي:

- تحديد تنسيقات إخراج بديلة مثل `json` أو `csv`
- إنتاج ملفات إخراج لنتائج استعلامات StackQL
- استخدام استعلامات StackQL المقدمة من خلال ملفات إدخال (ملفات `.iql` حسب العرف)
- تنفيذ عمليات معقدة، بما في ذلك تحميل المتغيرات أو المعالجة المسبقة للاستعلامات (باستخدام `json` أو `jsonnet`)

فيما يلي أمثلة على استخدام الأمر `exec` داخل AWS CloudShell:  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instance_type FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

ينفذ المثال أعلاه استعلام StackQL المحدد وينتج ملف إخراج بتنسيق `CSV`، يمكن تنزيله من AWS CloudShell.  بالإضافة إلى ذلك، يمكنك توفير دور IAM باستخدام الوسيطة `--role-arn` لتولي دور آخر لعملية الاستعلام أو تعديل البيانات؛ يظهر مثال على ذلك هنا (في هذه الحالة، قرأنا الاستعلام من ملف إدخال):  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::123456789012:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

يمكنك العثور على مزيد من المعلومات حول الأمر `exec` [__هنا__](/command-line-usage/exec).
