---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "SELECT en StackQL: consultar recursos en la nube y de SaaS con SQL, con filtros WHERE asignados a parámetros de API, joins, agrupación, ordenación y acceso a campos JSON."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Devuelve una o varias instancias de un recurso.  

Consulte también:
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy) [[` WITH (CTE) `]](/language-spec/with) [[` Funciones de ventana `]](/language-spec/windowing_functions)

* * * 

## Sintaxis {#syntax}

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

Un `JOIN` sin cláusula `ON` se trata como un `CROSS JOIN`, por lo que la condición del join puede indicarse en la cláusula `WHERE`. Las versiones de StackQL anteriores a `v0.12.732` no pueden planificar un `JOIN` sin condición.

* * *

## Ejemplos {#examples}

### Instrucción `SELECT` básica {#basic-select-statement}
Ejecute una instrucción `SELECT` básica en una sesión autenticada.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Devolver campos específicos con una instrucción `SELECT` {#returning-specified-fields-using-a-select-statement}
Ejecute una instrucción `SELECT` con una lista de columnas para devolver campos específicos de una instancia de Compute Engine.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver un conjunto de resultados ordenado por una columna en orden ascendente con la cláusula `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Ejecute una instrucción `SELECT` para devolver campos del recurso de buckets de Cloud Storage, ordenando los resultados en orden ascendente (los más antiguos primero). 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Devolver un conjunto de resultados ordenado por una columna en orden descendente con la cláusula `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Ejecute una instrucción `SELECT` para devolver campos del recurso de buckets de Cloud Storage, ordenando los resultados en orden descendente (los más recientes primero).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Obtener el primer resultado de una columna con las cláusulas `ORDER BY` y `LIMIT` {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
Ejecute una instrucción `SELECT` para devolver el bucket de Cloud Storage creado más recientemente.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Usar la función `COUNT` con las cláusulas `GROUP BY` y `HAVING` para contar recursos según una agrupación y un filtro {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
Ejecute una instrucción `SELECT` con una función `COUNT`, usando las cláusulas `GROUP BY` y `HAVING` para devolver el número de buckets de cada ubicación distinta de Asia.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT name, location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

Para obtener más información sobre la función `COUNT` y otras funciones de agregación admitidas por StackQL, consulte [Funciones de agregación](/language-spec/functions/aggregate/count).

### Obtener la lista de VM bajo demanda y la interfaz de red correspondiente desde Azure VM Scaleset {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
Ejecute una instrucción `SELECT` con las funciones [`SPLIT_PART`](/language-spec/functions/string/split_part) y [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) para obtener la lista de VM bajo demanda y sus NIC. Tenga en cuenta que, sin un calificador, subscription_id y resource_group_name se aplican a los tres recursos.

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

Para obtener más información sobre las funciones de ventana, consulte [Funciones de ventana](/language-spec/windowing_functions).

### Usar expresiones de tabla comunes (CTE) con `WITH` {#using-common-table-expressions-ctes-with-with}

Las CTE permiten definir conjuntos de resultados temporales con nombre a los que se puede hacer referencia en la consulta principal.

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

Para obtener más información sobre las CTE, consulte [WITH (CTE)](/language-spec/with).
