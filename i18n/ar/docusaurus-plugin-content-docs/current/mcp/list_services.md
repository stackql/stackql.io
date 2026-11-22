---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: أداة MCP تسرد الخدمات ضمن موفر StackQL
image: "/img/stackql-featured-image.png"
---

تعيد الخدمات التي يوفرها موفر واحد، وهي المستوى الثاني في [التسلسل الهرمي للموارد](/getting-started/resource-hierarchy) في StackQL. استدعها بعد [`list_providers`](/mcp/list_providers) لتضييق مساحة البحث قبل التعمق في الموارد.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`provider`|نعم|اسم الموفر (على سبيل المثال، `google` و`aws` و`azure` و`github`).|

<br/>

:::info

لا يجوز أن تحتوي المعرفات على مسافات بيضاء أو فواصل منقوطة أو شرطات مائلة عكسية؛ ويتم رفض هذا الإدخال قبل إنشاء أي جملة.

:::

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| جدول | صف واحد لكل خدمة ضمن الموفر المحدد |

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم. للقراءة فقط.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
List the services available in Google Cloud
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
