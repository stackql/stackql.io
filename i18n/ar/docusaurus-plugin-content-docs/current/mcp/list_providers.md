---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: أداة MCP تسرد الموفرين السحابيين وموفري SaaS المتاحين لخادم StackQL MCP
image: "/img/stackql-featured-image.png"
---

تعيد الموفرين الذين تم سحبهم بالفعل إلى ذاكرة التخزين المؤقت المحلية لـ StackQL، وهم قمة [التسلسل الهرمي للموارد](/getting-started/resource-hierarchy) في StackQL. استخدمها كنقطة دخول عندما يحتاج الوكيل إلى اكتشاف ما يمكنه الاستعلام عنه.

للاطلاع على الموفرين المتاحين في *السجل* (بما في ذلك الموفرون الذين لم يتم سحبهم بعد)، راجع [`list_registry`](/mcp/list_registry).

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## الإدخالات {#inputs}

لا يوجد.

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| جدول (جدول markdown لنموذج LLM) | صف واحد لكل موفر |

<br />

يحمل كل صف اسم الموفر وبيانات تعريف الإصدار كما ينشرها السجل.

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم (`read_only` و`safe` و`delete_safe` و`full_access`). للقراءة فقط.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
Which cloud providers are available for you to query right now?
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
