---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція REGEXP_LIKE у StackQL: перевіряє, чи відповідає рядок регулярному виразу, і повертає 1 для true та 0 для false, для фільтрів за шаблоном у реченнях WHERE."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Перевіряє, чи відповідає вихідний рядок шаблону, і повертає `1` для true або `0` для false.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Довідник із регулярних виразів ]](/language-spec/functions/re/reference) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*source*__  
Рядковий літерал або рядковий стовпець, який потрібно перевірити на наявність шаблону, визначеного регулярним виразом (`pattern`).  

__*pattern*__  
Шаблон регулярного виразу, який потрібно знайти.  

## Значення, що повертається {#return-values}
Повертає `1` для true (вихідний рядок відповідає шаблону) або `0` для false.  

* * *

## Приклади {#examples}

### Перевірка наявності шаблону в рядку {#determine-if-a-string-contains-a-pattern}

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

Докладніше див. [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
