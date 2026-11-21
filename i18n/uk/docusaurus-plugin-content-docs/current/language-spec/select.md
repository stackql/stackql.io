---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "SELECT у StackQL: запити SQL до хмарних ресурсів і SaaS із фільтрами WHERE, що відповідають параметрам API, з'єднаннями, групуванням, сортуванням і доступом до полів JSON."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Повертає екземпляр або екземпляри ресурсу.  

Див. також:
[[ Ієрархія ресурсів StackQL ]](/getting-started/resource-hierarchy) [[` WITH (CTE) `]](/language-spec/with) [[` Віконні функції `]](/language-spec/windowing_functions)

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

`JOIN` без речення `ON` обробляється як `CROSS JOIN`, тому умову з'єднання можна натомість указати в реченні `WHERE`.  Випуски StackQL до `v0.12.732` не можуть спланувати `JOIN` без умови.

* * *

## Приклади {#examples}

### Базова інструкція `SELECT` {#basic-select-statement}
Виконайте базову інструкцію `SELECT` в автентифікованому сеансі.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Повернення вказаних полів за допомогою інструкції `SELECT` {#returning-specified-fields-using-a-select-statement}
Виконайте інструкцію `SELECT` зі списком стовпців, щоб повернути вказані поля екземпляра Compute Engine.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання набору результатів, упорядкованого за стовпцем за зростанням, за допомогою речення `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Виконайте інструкцію `SELECT`, щоб повернути поля ресурсу бакетів Cloud Storage з упорядкуванням результатів за зростанням (спочатку найстаріші).

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Отримання набору результатів, упорядкованого за стовпцем за спаданням, за допомогою речення `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Виконайте інструкцію `SELECT`, щоб повернути поля ресурсу бакетів Cloud Storage з упорядкуванням результатів за спаданням (спочатку найновіші).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Отримання першого результату за стовпцем за допомогою речень `ORDER BY` і `LIMIT` {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
Виконайте інструкцію `SELECT`, щоб повернути бакет Cloud Storage, створений найпізніше.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Використання функції `COUNT` з реченнями `GROUP BY` і `HAVING` для підрахунку ресурсів на основі групування та фільтра {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
Виконайте інструкцію `SELECT` з функцією `COUNT` і реченнями `GROUP BY` та `HAVING`, щоб повернути кількість бакетів у кожному розташуванні, крім Азії.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

Докладніше про функцію `COUNT` та інші агрегатні функції, які підтримує StackQL, див. [Агрегатні функції](/language-spec/functions/aggregate/count).

### Отримання списку віртуальних машин On Demand і відповідних мережевих інтерфейсів із масштабованого набору віртуальних машин Azure {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
Виконайте інструкцію `SELECT` з функціями [`SPLIT_PART`](/language-spec/functions/string/split_part) і [`JSON_EXTRACT`](/language-spec/functions/json/json_extract), щоб отримати список віртуальних машин On Demand (OD) та їхніх мережевих адаптерів (NIC). Зверніть увагу, що без кваліфікатора `subscription_id` і `resource_group_name` застосовуються до всіх трьох ресурсів.

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

### Використання загальних табличних виразів (CTE) з `WITH` {#using-common-table-expressions-ctes-with-with}

CTE дають змогу визначати тимчасові іменовані набори результатів, на які можна посилатися в основному запиті.

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

Докладніше про CTE див. [WITH (CTE)](/language-spec/with).

Докладніше про віконні функції див. [Віконні функції](/language-spec/windowing_functions).
