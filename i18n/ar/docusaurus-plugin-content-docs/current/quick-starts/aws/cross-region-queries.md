---
title: الاستعلامات عبر المناطق
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
description: "الاستعلام عن موارد AWS وتجميعها عبر مناطق متعددة في جملة StackQL واحدة، باستخدام قوائم المناطق والتنفيذ غير المتزامن بدلًا من حلقات لكل منطقة."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# الاستعلامات غير المتزامنة عبر المناطق باستخدام StackQL

في هذا الدليل، سنوضح ميزة قوية في StackQL: القدرة على الاستعلام عن الموارد وتجميعها عبر مناطق متعددة في AWS.  يتضمن ذلك طلبات API عديدة إلى نقاط نهاية مختلفة، تُنفذ بشكل غير متزامن.  ويتم ذلك بسلاسة وبشفافية تامة بالنسبة إلى المستخدم.

> StackQL أداة تطوير قوية تتيح الاستعلام عن البنية التحتية والموارد السحابية ونشرها باستخدام بناء جملة SQL.

تم الاختبار باستخدام <span class="tutorial_tested_on">واجهة SQL الخلفية المضمنة</span> <span class="tutorial_tested_on">واجهة SQL الخلفية postgres</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## فهم مناطق AWS ومواردها {#understanding-aws-regions-and-resources}

تستضاف موارد AWS في مواقع متعددة حول العالم.  وتتكون هذه المواقع من المناطق ومناطق التوافر، وهي مصممة لتتيح للمستخدمين وضع المثيلات وتخزين البيانات في مواقع مختلفة.

## مشكلة مناطق AWS {#the-problem-with-aws-regions}

عند العمل مع AWS، من الضروري أن تدرك أن AWS Management Console تعرض الموارد ضمن نطاق منطقة واحدة، مما يتيح للمستخدمين التفاعل مع الموارد وإدارتها داخل منطقة محددة في كل مرة.  وبالمثل، تستخدم أوامر AWS Command Line Interface (CLI) ومجموعات تطوير البرامج (SDKs) المنطقة كنقطة دخول للعمليات، مما يستلزم تحديد منطقة لتنفيذ الأوامر أو تشغيل البرامج.

> يؤدي التنظيم القائم على المناطق في الخدمات السحابية إلى تعقيدات عندما تمتد الموارد عبر مناطق متعددة.  إضافة إلى ذلك، كثيرًا ما تستغل الاختراقات الأمنية، بما في ذلك عمليات استغلال تعدين العملات المشفرة، موارد في مناطق مختلفة، فتتجاوز المنطقة الرئيسية التي تستخدمها المؤسسات عادة.

## تحديد المناطق في استعلامات StackQL {#specifying-regions-in-stackql-queries}

باستخدام StackQL، يمكنك تحديد المناطق لاستهداف الموارد عبر مواقع جغرافية مختلفة.  ويفيد ذلك بشكل خاص في إدارة الموارد الموزعة حول العالم.

### مثال: سرد مثيلات EC2 عبر مناطق متعددة {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

يعيد هذا الاستعلام قائمة بمثيلات EC2 من المنطقتين `us-east-1` (فرجينيا الشمالية) و`eu-west-1` (أيرلندا)، مما يوضح كيف يمكن لـ StackQL استرداد البيانات عبر المناطق في استعلام واحد.  ويمكن استخدام ذلك مع الاستعلامات التجميعية أيضًا، مثل:

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## قوة الاستعلامات غير المتزامنة {#the-power-of-asynchronous-queries}

ينفذ StackQL هذه الاستعلامات عبر المناطق بشكل غير متزامن، فيرسل طلبات API متعددة ومتوازية إلى AWS.  ويضمن ذلك استرداد البيانات بكفاءة دون انتظار اكتمال طلب منطقة ما قبل الانتقال إلى المنطقة التالية.

## الخلاصة {#conclusion}

توفر الاستعلامات غير المتزامنة عبر المناطق في StackQL وسيلة قوية وفعالة لإدارة موارد AWS عبر مواقع جغرافية مختلفة.  ومن خلال الاستفادة من بناء جملة شبيه بـ SQL في هذه الاستعلامات، يبسط StackQL مهام الإدارة السحابية المعقدة، مما يجعله أداة قيمة للغاية للمطورين ومسؤولي الأنظمة السحابية الذين يسعون إلى تحسين بنيتهم التحتية على AWS.
