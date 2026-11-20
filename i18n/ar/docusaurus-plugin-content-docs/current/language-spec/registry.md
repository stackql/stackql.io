---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "REGISTRY في StackQL: سرد الموفرين والإصدارات في سجل الموفرين وسحب أحدث إصدار أو إصدار محدد إلى التثبيت الخاص بك."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

تسرد الموفرين المتاحين في سجل الموفرين الذي تم تكوينه، أو تسحب موفرًا إلى التثبيت المحلي.  

راجع أيضًا:  
[[ التسلسل الهرمي لموارد StackQL ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> من الإصدار `v1.0` فصاعدًا، يمكن اشتراط أن تحمل مستندات الموفرين توقيع ML-DSA-65 صالحًا عند قراءتها. التحقق معطل افتراضيًا، ويتم تمكينه باستخدام المفتاح `verifyConfig` للعلامة [`--registry`](/command-line-usage/global-flags#--registry).

* * * 

## بناء الجملة {#syntax}

*registryStatement::=*

<RailroadDiagram 
type="registry"
/>

&nbsp;  
&nbsp;  

```sql
REGISTRY { LIST [<provider>] | PULL <provider> [<version>]};
```

* * *

## أمثلة {#examples}

### سرد الموفرين المتاحين {#list-available-providers}
تسترد قائمة بأحدث إصدار من كل موفر متاح.  
```sql
REGISTRY LIST;
```
### سرد جميع إصدارات موفر محدد {#list-all-versions-for-a-specific-provider}
تسترد قائمة بجميع الإصدارات المتاحة لموفر محدد.    
```sql
REGISTRY LIST google;
```

### تثبيت أحدث إصدار من موفر {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### تثبيت إصدار محدد من موفر {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v23.01.00104;
```
