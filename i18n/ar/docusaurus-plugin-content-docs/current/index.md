---
title: SQL للبنية التحتية السحابية وواجهات API لخدمات SaaS ووكلاء الذكاء الاصطناعي
sidebar_label: مرحبًا بك في StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: StackQL محرك مفتوح المصدر يتيح للبشر ووكلاء الذكاء الاصطناعي الاستعلام عن الخدمات السحابية وخدمات SaaS وتوفيرها وتشغيلها باستخدام SQL، دون ملف حالة أو حزمة SDK لكل سحابة.
image: "/img/stackql-featured-image.png"
slug: /
# SoftwareApplication node on the homepage, from the site-wide defaults in
# themeConfig.structuredData.softwareApplication (name, category, OS,
# license, download URL, free offer)
softwareApplication: true
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import DocsHero from '@site/src/components/DocsHero/DocsHero';
import { FaRobot, FaCode, FaSatelliteDish } from 'react-icons/fa';

<DocsHero
  lightSrc="/img/stackql-logo-bold-light-mode.png"
  darkSrc="/img/stackql-logo-bold-dark-mode.png"
  alt="StackQL"
  title={
  <>
  الواجهة الشاملة<br/>
  لعصر الوكلاء
  </>
  }
  byline={
    <>
    Configuration-as-Data. Infrastructure-as-Context.
    </>
    }
/>

---

<div className="row margin-top--lg margin-bottom--lg">
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaRobot size={22} className="homeCardIcon" />هندسة المنصات القائمة على الوكلاء</h3>
      </div>
      <div className="card__body">
        سطح شامل يتيح للوكلاء والبشر صياغة التغييرات جنبًا إلى جنب، بسرعة الآلة. تعدد جهات الكتابة والحالة المباشرة والتنفيذ المحكوم بالسياسات اعتبارات أساسية من الدرجة الأولى - الأساس الذي تحتاجه الأنظمة ذاتية التشغيل لتعمل بأمان.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC لعصر الوكلاء</h3>
      </div>
      <div className="card__body">
        لا تزال التعليمات البرمجية هي النية. الواقع هو البيانات. يمنح StackQL الوكلاء والمشغلين واجهة تعريفية واحدة للاستعلام عن الحالة المباشرة عبر الموفرين، وتأكيد الحالة المطلوبة بشكل متساوي القوة (idempotent)، والتنقل بين مستوى التحكم ومستوى البيانات من خلال قواعد لغة واحدة - دون ملف حالة، ودون حزمة SDK خاصة بكل سحابة.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />قابلية الملاحظة الذاتية</h3>
      </div>
      <div className="card__body">
        تعامل مع واجهات API السحابية على أنها طبقة البيانات الخاصة بك، لا على أنها هدف النشر. كل عملية قراءة مباشرة، وكل تغيير هو حدث، ويصبح الانحراف إشارة يمكن للوكيل التفكير فيها - لا إنذارًا يتعين على الإنسان ملاحقته.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'الاستعلام', value: 'iql', },
    { label: 'النتائج', value: 'data', },
  ]
}>
<TabItem value="iql">

```sql
SELECT state, COUNT(*) as num_instances 
FROM aws.ec2.instances 
WHERE region = 'us-west-1' 
GROUP BY state;
```

</TabItem>
<TabItem value="data">

```
|---------------------------------|
|      state      | num_instances |
|---------------------------------|
|     running     |     342       |
|---------------------------------|
|     pending     |       4       |
|---------------------------------|
|     stopped     |      38       |
|---------------------------------|
```

</TabItem>
</Tabs>

نشر الموارد باستخدام مزود الخدمات السحابية الخاص بك سهل بقدر سهولة كتابة جملة [`INSERT`](/language-spec/insert)...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

باستخدام StackQL، يمكنك التطوير كما تشاء: تعريفيًا أو إجرائيًا. بفضل قواعد لغة سهلة التعلم وعدم وجود ملف حالة لإدارته، يمكنك البدء بسرعة واستخدام StackQL بالتبادل مع أدوات البنية التحتية كتعليمات برمجية الأخرى، سواء كانت سحابية أصلية أم لا. يوفر StackQL واجهة شاملة لوكلاء الذكاء الاصطناعي للتفاعل مع البنية التحتية السحابية. تشمل استخدامات StackQL ما يلي:

- **تكامل وكلاء الذكاء الاصطناعي** - تمكين الوكلاء من توفير الموارد السحابية والاستعلام عنها
- **الاتصال بمستوى التحكم** - توفير البنية التحتية برمجيًا لسير العمل القائم على الوكلاء
- **قابلية ملاحظة مستوى البيانات** - المراقبة والتحليلات في الوقت الفعلي للعمليات المعتمدة على الذكاء الاصطناعي
- نشر البنية التحتية السحابية (باستخدام SQL)
- جرد الأصول السحابية وإعداد التقارير (باستخدام SQL)
- الامتثال السحابي والتصديق على الضوابط (باستخدام SQL)
- اكتشاف انحراف التكوين (باستخدام SQL)
- والمزيد، لا يحده إلا خيالك!

## لمن StackQL {#who-stackql-is-for}

StackQL مخصص لفرق هندسة المنصات وSRE التي تشغل البيئات السحابية، وفرق الأمان وFinOps التي تحتاج إلى إجابات مباشرة حول الجرد والامتثال والتكلفة، ومهندسي البيانات الذين يريدون استخدام واجهات API السحابية وواجهات API لخدمات SaaS كمصادر قابلة للاستعلام، والمطورين الذين ينشئون وكلاء ذكاء اصطناعي يجب أن يستعلموا عن الموارد السحابية وموارد SaaS ويغيروها بأمان. يعمل StackQL أينما يكون العمل: كملف ثنائي واحد على macOS وLinux وWindows، وفي Docker، وفي CI مع GitHub Actions، وفي واجهات الأوامر السحابية لـ AWS وAzure وGoogle Cloud، وبشكل مضمن في التطبيقات، وكخادم متوافق مع PostgreSQL لأدوات BI ودفاتر الملاحظات، وكخادم MCP لوكلاء الذكاء الاصطناعي.

## الترخيص والدعم {#license-and-support}

StackQL مفتوح المصدر بموجب [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) ومجاني الاستخدام لأي غرض. يتوفر دعم المجتمع عبر [GitHub Discussions](https://github.com/orgs/stackql/discussions) و[Discord](https://discord.com/invite/xVXZ9d5NxN)؛ أما الأخطاء وطلبات الميزات فمكانها [GitHub Issues](https://github.com/stackql/stackql/issues). لاستفسارات الدعم التجاري، [اتصل بـ StackQL Studios](/contact-us).

## ابدأ الآن {#get-started}

- [التثبيت](/installing-stackql) - تثبيت الملف الثنائي لـ StackQL على macOS أو Linux أو Windows، أو تشغيله من Docker أو من مدير حزم أو من واجهة أوامر سحابية
- [وكلاء الذكاء الاصطناعي](/ai-agents) - توصيل وكلاء الذكاء الاصطناعي عبر MCP، وتضمين الخادم، واستكشاف الاستعلامات والمحتوى المرجعي
- [بدء الاستخدام](/getting-started) - التسلسل الهرمي للموارد، واستعلاماتك الأولى، واستخدام موفر، وأوضاع الإخراج، والمتغيرات، واستخدام القوالب
- [الموفرون المتاحون](/providers) - الموفرون للخدمات السحابية وخدمات SaaS التي يمكنك الاستعلام عنها وإدارتها، ولكل موفر موقع ويب مرجعي خاص به
- [المدونة](/blog) - إعلانات المنتج وإعلانات الموفرين والبرامج التعليمية
- [استخدام سطر الأوامر](/command-line-usage) - `exec` و`shell` و`srv` و`mcp` و`registry` والعلامات العامة
- [أدوات MCP](/mcp) - خادم Model Context Protocol وأدواته التي تمنح وكلاء الذكاء الاصطناعي واجهة SQL نفسها
- [مواصفات اللغة](/language-spec/select) - `SELECT` و`INSERT` و`UPDATE` و`DELETE` و`SHOW` و`DESCRIBE` والدوال وأنواع البيانات
