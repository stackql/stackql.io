---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: أداة MCP تعيد عقد الإدخال والإخراج الكامل لأسلوب وصول واحد في StackQL
image: "/img/stackql-featured-image.png"
---

تعيد عقد الإدخال والإخراج الكامل لأسلوب وصول واحد - وهو EXTENDED دائمًا، أي أنه يشمل الإدخالات ومخطط نص الطلب وشكل الإخراج الكامل. من هنا يحصل الوكيل على أسماء الأعمدة: يحمل كل صف قيمة `param_type` هي `input_required` (شرط `WHERE` إلزامي يتطلب مطابقة تامة) أو `input_optional` أو `output` (حقل يمكن أن تشير إليه جملة `SELECT` أو عبارة `RETURNING`). لا توجد للمورد قائمة حقول واحدة، لأن كل أسلوب يعيد شكله الخاص، لذا استدع هذه الأداة للأسلوب الذي اخترته من [`list_methods`](/mcp/list_methods) قبل أول استعلام عليه.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`provider`|نعم|اسم الموفر.|
|`service`|نعم|الخدمة ضمن هذا الموفر.|
|`resource`|نعم|المورد ضمن `provider.service`.|
|`method`|نعم|اسم الأسلوب كما تعيده الأداة [`list_methods`](/mcp/list_methods).|

<br/>

:::info

لا يجوز أن تحتوي المعرفات على مسافات بيضاء أو فواصل منقوطة أو شرطات مائلة عكسية؛ ويتم رفض مثل هذا الإدخال قبل بناء أي جملة.

:::

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| KV | صف واحد لكل حقل إدخال وإخراج: الاسم والنوع و`param_type` (`input_required` أو `input_optional` أو `output`) والشكل والوصف |

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم. للقراءة فقط.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
