---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "SELECT no StackQL: consultar recursos na nuvem e de SaaS com SQL, com filtros WHERE mapeados para parâmetros de API, joins, agrupamento, ordenação e acesso a campos JSON."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Retorna uma ou mais instâncias de um recurso.  

Veja também:
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy) [[` WITH (CTEs) `]](/language-spec/with) [[` Funções de janela `]](/language-spec/windowing_functions)

* * * 

## Sintaxe {#syntax}

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

Um `JOIN` sem cláusula `ON` é tratado como um `CROSS JOIN`, então a condição do join pode ser informada na cláusula `WHERE`.  As versões do StackQL anteriores à `v0.12.732` não conseguem planejar um `JOIN` sem condição.

* * *

## Exemplos {#examples}

### Instrução `SELECT` básica {#basic-select-statement}
Execute uma instrução `SELECT` básica em uma sessão autenticada.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Retornar campos específicos com uma instrução `SELECT` {#returning-specified-fields-using-a-select-statement}
Execute uma instrução `SELECT` com uma lista de colunas para retornar campos específicos de uma instância do Compute Engine.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar um conjunto de resultados ordenado por uma coluna em ordem crescente com a cláusula `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Execute uma instrução `SELECT` para retornar campos do recurso de buckets do Cloud Storage, ordenando os resultados em ordem crescente (os mais antigos primeiro). 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Retornar um conjunto de resultados ordenado por uma coluna em ordem decrescente com a cláusula `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Execute uma instrução `SELECT` para retornar campos do recurso de buckets do Cloud Storage, ordenando os resultados em ordem decrescente (os mais recentes primeiro).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Obter o primeiro resultado de uma coluna com as cláusulas `ORDER BY` e `LIMIT` {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
Execute uma instrução `SELECT` para retornar o bucket do Cloud Storage criado mais recentemente.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Usar a função `COUNT` com as cláusulas `GROUP BY` e `HAVING` para contar recursos com base em um agrupamento e um filtro {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
Execute uma instrução `SELECT` com a função `COUNT`, usando as cláusulas `GROUP BY` e `HAVING` para retornar o número de buckets em cada local, exceto a Ásia.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT name, location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

Para obter mais informações sobre a função `COUNT` e outras funções de agregação compatíveis com o StackQL, consulte [Funções de agregação](/language-spec/functions/aggregate/count).

### Obter a lista de VMs sob demanda e as interfaces de rede correspondentes de um VM Scale Set do Azure {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
Execute uma instrução `SELECT` com as funções [`SPLIT_PART`](/language-spec/functions/string/split_part) e [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) para obter a lista de VMs OD e suas NICs. Observe que, sem um qualificador, subscription_id e resource_group_name são aplicados aos três recursos.

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

### Usar expressões de tabela comuns (CTEs) com `WITH` {#using-common-table-expressions-ctes-with-with}

As CTEs permitem definir conjuntos de resultados temporários nomeados, que podem ser referenciados na consulta principal.

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

Para obter mais informações sobre CTEs, consulte [WITH (CTEs)](/language-spec/with).

Para obter mais informações sobre funções de janela, consulte [Funções de janela](/language-spec/windowing_functions).
