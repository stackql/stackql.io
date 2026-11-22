---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция SPLIT_PART в StackQL: разбиение строки по разделителю и получение части по позиции с отсчетом от единицы, удобно для разбора идентификаторов ресурсов и ARN."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Разбивает исходную строку по разделителю и возвращает указанную часть (с отсчетом от единицы).  

См. также:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*source*__  
Строковый литерал или строковый столбец, который нужно разбить.

__*sep*__  
Символ или символы, которые нужно использовать в качестве разделителя.  

> Если `sep` состоит из нескольких символов, разделителем является вся строка: `SPLIT_PART('a||b||c', '||', 3)` возвращает `c`.  

> В разделителе можно использовать любые символы, включая символы вне набора ASCII, например `€`.  

__*part*__  
Элемент разбитой строки (с отсчетом от единицы), который нужно вернуть.  Отрицательные значения используются для отсчета в обратном направлении от последнего элемента.  

## Возвращаемое значение {#return-values}
Возвращает строку.

* * *

## Примеры {#examples}

### Извлечение элементов строки, разделенных косой чертой {#extract-elements-from-a-string-separated-by-a-forward-slash}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Результаты', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT name,  
 split_part(id, '/', 3) as subscription,
 split_part(id, '/', 5) as resource_group,
 json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines 
 WHERE resource_group_name = 'stackql-ops-cicd-dev-01' 
 AND subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------------------------------|-------------------------|-----------------|
| name |             subscription             |     resource_group      |     vm_size     |
|------|--------------------------------------|-------------------------|-----------------|
| test | 273769f6-545f-45b2-8ab8-2f14ec5768dc | stackql-ops-cicd-dev-01 | Standard_D2s_v3 |
|------|--------------------------------------|-------------------------|-----------------|
```

</TabItem>
</Tabs>

### Извлечение последнего элемента из URL-адреса {#extract-the-last-element-from-a-url}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Результаты', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT
name,
SPLIT_PART(network, '/', -1) as network,
SPLIT_PART(region, '/', -1) as region
FROM google.compute.vpn_gateways WHERE project = 'stackql-demo';
```

</TabItem>
<TabItem value="results">

```
+------------------------+-----------------------+----------------------+
| name                   | network               | region               |
+------------------------+-----------------------+----------------------+
| gcp-aws-vpn-gateway    | stackql-shrd-vpc-nw   | australia-southeast1 |
+------------------------+-----------------------+----------------------+
```

</TabItem>
</Tabs>

Подробнее см. [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)
