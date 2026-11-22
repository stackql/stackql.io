---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція REGEXP_SUBSTR у StackQL: видобуває з вхідного рядка підрядок, що відповідає регулярному виразу."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Повертає підрядок вхідного рядка, що відповідає вказаному шаблону.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Довідник із регулярних виразів ]](/language-spec/functions/re/reference) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*source*__  
Рядковий літерал або рядковий стовпець, з якого потрібно видобути підрядок за шаблоном, визначеним регулярним виразом (`pattern`).  

__*pattern*__  
Шаблон регулярного виразу, який потрібно знайти.  

## Значення, що повертається {#return-values}
Повертає рядкове значення.

* * *

## Приклади {#examples}

### Видобування підрядка за регулярним виразом {#extract-a-substring-based-upon-a-regular-expression}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Результати', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
select name,
json_extract(properties, '$.storageProfile.imageReference.offer') as os,
regexp_substr(
  json_extract(properties, '$.storageProfile.imageReference.exactVersion'), 
  '[0-9][0-9]\\.[0-9][0-9]') as os_version 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------|------------|
| name |      os       | os_version |
|------|---------------|------------|
| vm0  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm1  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm2  | UbuntuServer  |      20.04 |
|------|---------------|------------|
```

</TabItem>
</Tabs>

Докладніше див. [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
