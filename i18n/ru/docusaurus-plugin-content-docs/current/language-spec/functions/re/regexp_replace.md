---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция REGEXP_REPLACE в StackQL: заменяет части строки, соответствующие регулярному выражению, строкой замены."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Заменяет совпадающую подстроку строкой замены.  

См. также:  
[[` SELECT `]](/language-spec/select) [[ Справочник по регулярным выражениям ]](/language-spec/functions/re/reference) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Аргументы {#arguments}

__*source*__  
Строковый литерал или строковый столбец, в котором совпадения с регулярным выражением `pattern` заменяются на `replacement`.  

__*pattern*__  
Шаблон регулярного выражения для поиска.  

__*replacement*__  
Строка замены.

> Строка замены вставляется как есть: ссылки на группы захвата, такие как `$1`, не раскрываются.  

## Возвращаемое значение {#return-values}
Возвращает строку.

* * *

## Примеры {#examples}

### Замена шаблона регулярного выражения в столбце {#replace-a-regular-expression-pattern-in-a-column}

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

Подробнее см. [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
