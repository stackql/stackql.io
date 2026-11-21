---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція REGEXP_REPLACE у StackQL: замінює частини рядка, що відповідають регулярному виразу, рядком заміни."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Замінює підрядок, що відповідає шаблону, рядком заміни.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Довідник із регулярних виразів ]](/language-spec/functions/re/reference) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Аргументи {#arguments}

__*source*__  
Рядковий літерал або рядковий стовпець, у якому збіги з регулярним виразом `pattern` замінюються на `replacement`.  

__*pattern*__  
Шаблон регулярного виразу, який потрібно знайти.  

__*replacement*__  
Рядок заміни.

> Підтримує зворотні посилання на групи захоплення від `\1` до `\9` у рядку заміни.  

## Значення, що повертається {#return-values}
Повертає рядкове значення.

* * *

## Приклади {#examples}

### Заміна шаблону регулярного виразу в стовпці {#replace-a-regular-expression-pattern-in-a-column}

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
regexp_replace(json_extract(properties, '$.timeCreated'), 'T.*', '') as date_created
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------|
| name | date_created |
|------|--------------|
| vm0  | 2022-09-14   |
|------|--------------|
| vm1  | 2022-09-14   |
|------|--------------|
| vm2  | 2022-09-14   |
|------|--------------|
```

</TabItem>
</Tabs>

Докладніше див. [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
