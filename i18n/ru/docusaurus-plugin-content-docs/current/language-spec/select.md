---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "SELECT в StackQL: SQL-запросы к облачным ресурсам и ресурсам SaaS с фильтрами WHERE, сопоставляемыми с параметрами API, соединениями, группировкой, сортировкой и полями JSON."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Возвращает экземпляр или экземпляры ресурса.  

См. также:
[[ Иерархия ресурсов StackQL ]](/getting-started/resource-hierarchy) [[` WITH (CTE) `]](/language-spec/with) [[` Оконные функции `]](/language-spec/windowing_functions)

* * * 

## Синтаксис {#syntax}

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
[ WITH [ RECURSIVE ] <cteName> [ ( <columnList> ) ] AS ( <selectStatement> ) [, ...] ]
SELECT { * | <fieldList> | <windowFunctionCall> [ AS <alias> ] }
FROM { <multipartIdentifier> | <joinStatement(s)> }
[ WHERE <expression> ]
[ GROUP BY <fieldList> ]
[ HAVING <expression> ]
[ WINDOW <windowName> AS ( <windowSpec> ) [, ...] ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ LIMIT <integer> ]
[ UNION <selectStatement> ];
```

Соединение `JOIN` без предложения `ON` обрабатывается как `CROSS JOIN`, поэтому условие соединения можно задать в предложении `WHERE`. Выпуски StackQL до `v0.12.732` не могут спланировать соединение `JOIN` без условия.

* * *

## Примеры {#examples}

### Базовая инструкция `SELECT` {#basic-select-statement}
Выполните базовую инструкцию `SELECT` в аутентифицированном сеансе.

```sql
-- Selecting all instances in a zone using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Возврат указанных полей с помощью инструкции `SELECT` {#returning-specified-fields-using-a-select-statement}
Выполните инструкцию `SELECT` со списком столбцов, чтобы вернуть указанные поля экземпляра Compute Engine.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение результирующего набора, упорядоченного по столбцу по возрастанию, с помощью предложения `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Выполните инструкцию `SELECT`, чтобы вернуть поля ресурса бакетов Cloud Storage с упорядочением результатов по возрастанию (сначала самые старые). 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Получение результирующего набора, упорядоченного по столбцу по убыванию, с помощью предложения `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Выполните инструкцию `SELECT`, чтобы вернуть поля ресурса бакетов Cloud Storage с упорядочением результатов по убыванию (сначала самые новые).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Получение первого результата по столбцу с помощью предложений `ORDER BY` и `LIMIT` {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
Выполните инструкцию `SELECT`, чтобы вернуть последний созданный бакет Cloud Storage.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Использование функции `COUNT` с предложениями `GROUP BY` и `HAVING` для подсчета ресурсов с группировкой и фильтром {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
Выполните инструкцию `SELECT` с функцией `COUNT`, используя предложения `GROUP BY` и `HAVING`, чтобы вернуть количество бакетов в каждом местоположении, кроме Азии.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

Подробнее о функции `COUNT` и других агрегатных функциях, которые поддерживает StackQL, см. [Агрегатные функции](/language-spec/functions/aggregate/count).

### Получение списка виртуальных машин On Demand и соответствующих сетевых интерфейсов из масштабируемого набора виртуальных машин Azure {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
Выполните инструкцию `SELECT` с функциями [`SPLIT_PART`](/language-spec/functions/string/split_part) и [`JSON_EXTRACT`](/language-spec/functions/json/json_extract), чтобы получить список виртуальных машин On Demand (OD) и их сетевых интерфейсов (NIC). Обратите внимание, что без квалификатора параметры `subscription_id` и `resource_group_name` применяются ко всем трем ресурсам.

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

### Использование обобщенных табличных выражений (CTE) с предложением `WITH` {#using-common-table-expressions-ctes-with-with}

CTE позволяют определять временные именованные результирующие наборы, на которые можно ссылаться в основном запросе.

```sql
-- Combine and rank contributors across multiple repositories
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Подробнее о CTE см. [WITH (CTE)](/language-spec/with).

Подробнее об оконных функциях см. [Оконные функции](/language-spec/windowing_functions).
