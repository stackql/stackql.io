---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: أداة MCP تسرد الموارد ضمن provider.service في StackQL
image: "/img/stackql-featured-image.png"
---

تعيد الموارد ضمن `provider.service`، وهي المستوى الثالث في [التسلسل الهرمي للموارد](/getting-started/resource-hierarchy) في StackQL. الموارد هي الأهداف التي تظهر في عبارة `FROM` في استعلام StackQL.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`provider`|نعم|اسم الموفر.|
|`service`|نعم|الخدمة ضمن ذلك الموفر.|

<br/>

:::info

لا يجوز أن تحتوي المعرفات على مسافات بيضاء أو فواصل منقوطة أو شرطات مائلة عكسية؛ ويتم رفض هذا الإدخال قبل إنشاء أي جملة.

:::

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| جدول | صف واحد لكل مورد ضمن `provider.service` |

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم. للقراءة فقط.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
List the resources in the google.storage service
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
