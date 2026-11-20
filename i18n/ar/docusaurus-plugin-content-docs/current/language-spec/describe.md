---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "DESCRIBE في StackQL: سرد حقول مورد، أو معلمات أسلوب محدد ونوع قيمته المرجعة، قبل الاستعلام عنه أو تعديله."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

تصف موردًا أو أسلوبًا محددًا لمورد.

راجع أيضًا:
[[ التسلسل الهرمي لموارد StackQL ]](/getting-started/resource-hierarchy)

* * *

## بناء الجملة {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

يقبل StackQL الكلمة الأساسية `DESC` كاسم مستعار لـ `DESCRIBE` عند وصف مورد؛ أما جملة `DESCRIBE METHOD` فتتطلب الكلمة الأساسية كاملة.

## الاستجابة {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| العمود | الوصف |
| --- | --- |
| `name` | اسم الحقل |
| `type` | نوع البيانات (`string`، `integer`، `boolean`، `number`، `object`، `array`) |
| `description` | وصف الحقل (عند تحديد `EXTENDED`) |

</TabItem>
<TabItem value="describe-method">

| العمود | الوصف |
| --- | --- |
| `name` | اسم الحقل |
| `type` | نوع البيانات (`string`، `integer`، `boolean`، `number`، `object`، `array`) |
| `param_type` | `input_required` أو `input_optional` أو `output` |
| `shape` | مجموعة فرعية من JSON Schema لحقول الكائنات والمصفوفات؛ فارغ للحقول المفردة. يتضمن `properties` و`items` و`required` و`enum` و`default` و`description` المتداخلة، ويتضمن اختياريًا قيمًا منطقية إذا أتاحها الموفر (`readOnly`، `writeOnly`، `deprecated`) |
| `description` | وصف الحقل (عند تحديد `EXTENDED`) |

</TabItem>
</Tabs>

* * *

## أمثلة {#examples}

### جملة `DESCRIBE` أساسية {#basic-describe-statement}
شغّل جملة `DESCRIBE` أساسية لسرد الحقول في مورد من جلسة مصادق عليها.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### جملة `DESCRIBE` موسعة {#extended-describe-statement}
شغّل جملة `DESCRIBE` موسعة لسرد الحقول في مورد وأوصافها من جلسة مصادق عليها.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### الاسم المستعار `DESC` {#desc-alias}
الكلمة الأساسية `DESC` اختصار لـ `DESCRIBE` عند وصف مورد، مع `EXTENDED` أو بدونها.  ويعاملها StackQL كجملة للقراءة فقط في كل موضع يعامل فيه `DESCRIBE` كذلك، بما في ذلك الأداة `run_select_query` في خادم MCP في الوضعين `read_only` و`safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### جملة `DESCRIBE METHOD` أساسية {#basic-describe-method-statement}
افحص أسلوبًا واحدًا لمورد. تتضمن النتيجة معلمات الإدخال وحقول الاستجابة، مع عرض البنية المتداخلة في العمود `shape`.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### جملة `DESCRIBE METHOD` موسعة {#extended-describe-method-statement}
شغّل جملة `DESCRIBE METHOD` موسعة لتضمين وصف لكل حقل إلى جانب الشكل.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### اكتشاف حمولة `INSERT` {#discovering-an-insert-payload}
جملة `DESCRIBE METHOD` هي الوسيلة المعيارية لاكتشاف الحقول التي تقبلها جملة `INSERT` (أو أي أسلوب لتعديل البيانات). يحمل العمود `shape` بنية متداخلة يمكن للوكلاء والبشر استخدامها لبناء حمولة في استعلام واحد.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
