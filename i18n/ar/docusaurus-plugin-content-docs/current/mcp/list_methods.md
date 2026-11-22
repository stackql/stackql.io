---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: أداة MCP تسرد أساليب الوصول لمورد StackQL
image: "/img/stackql-featured-image.png"
---

تعيد أساليب الوصول (عمليات HTTP) المرتبطة بمورد: أساليب القراءة وعمليات تعديل البيانات وعمليات دورة الحياة المتاحة. هذه هي الأداة التي يجب أن يستدعيها الوكيل **قبل كتابة أي استعلام**، لأن معلمات `WHERE` المطلوبة يتم استنتاجها من أسلوب القراءة المختار. اختر الأسلوب الذي يطابق فعل SQL الخاص به الغرض والذي يمكن توفير معلماته المطلوبة، ثم استدع [`describe_method`](/mcp/describe_method) لهذا الأسلوب للحصول على العقد الكامل، بما في ذلك حقول الإخراج.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`provider`|نعم|اسم الموفر.|
|`service`|نعم|الخدمة ضمن ذلك الموفر.|
|`resource`|نعم|المورد ضمن `provider.service`.|

<br/>

:::info

لا يجوز أن تحتوي المعرفات على مسافات بيضاء أو فواصل منقوطة أو شرطات مائلة عكسية؛ ويتم رفض هذا الإدخال قبل إنشاء أي جملة.

:::

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| جدول | صف واحد لكل أسلوب وصول، بما في ذلك فعل SQL الذي يقابله والمعلمات المطلوبة |

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم. للقراءة فقط.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
