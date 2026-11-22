---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция REGEXP_LIKE в StackQL: проверяет соответствие строки регулярному выражению и возвращает 1 (истина) или 0 (ложь) для фильтров по шаблону в предложениях WHERE."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Проверяет, соответствует ли исходная строка шаблону, и возвращает `1` (истина) или `0` (ложь).  

См. также:  
[[` SELECT `]](/language-spec/select) [[ Справочник по регулярным выражениям ]](/language-spec/functions/re/reference) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*source*__  
Строковый литерал или строковый столбец, в котором нужно проверить наличие шаблона, заданного регулярным выражением (`pattern`).  

__*pattern*__  
Шаблон регулярного выражения для поиска.  

## Возвращаемое значение {#return-values}
Возвращает `1` (истина: исходная строка соответствует шаблону) или `0` (ложь).  

* * *

## Примеры {#examples}

### Определение наличия шаблона в строке {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, '^vm[0-9]+') as name_starts_with_vm 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------------|
| name | name_starts_with_vm |
|------|---------------------|
| vm0  |                   1 |
|------|---------------------|
| vm1  |                   1 |
|------|---------------------|
| test |                   0 |
|------|---------------------|
```

</TabItem>
</Tabs>

Подробнее см. [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
