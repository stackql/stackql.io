---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция REGEXP_SUBSTR в StackQL: извлекает подстроку входной строки, соответствующую регулярному выражению."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Возвращает подстроку входной строки, соответствующую заданному шаблону.  

См. также:  
[[` SELECT `]](/language-spec/select) [[ Справочник по регулярным выражениям ]](/language-spec/functions/re/reference) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*source*__  
Строковый литерал или строковый столбец, из которого нужно извлечь подстроку по шаблону, заданному регулярным выражением (`pattern`).  

__*pattern*__  
Шаблон регулярного выражения для поиска.  

## Возвращаемое значение {#return-values}
Возвращает строку.

* * *

## Примеры {#examples}

### Извлечение подстроки по регулярному выражению {#extract-a-substring-based-upon-a-regular-expression}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Результаты', value: 'results', },
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

Подробнее см. [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
