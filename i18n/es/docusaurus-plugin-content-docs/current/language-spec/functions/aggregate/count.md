---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de agregación COUNT en StackQL: el número de filas de un recurso o de valores distintos de NULL de una columna dentro de una agrupación."
image: "/img/stackql-featured-image.png"
---
Devuelve el número total de registros de un recurso o un recuento de las instancias distintas de `NULL` de una columna en una agrupación.  

Consulte también:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  o __*__  
Una columna o expresión.  Si se especifica __*__, se cuentan todos los registros del recurso o de la agrupación.  

> Al menos un valor de *columnExpression* debe ser distinto de `NULL`; de lo contrario, se devuelve `NULL`.

__*groupByColumn*__  
Una o varias columnas sobre las que se realizan operaciones de resumen o de agregación.  La cláusula `GROUP BY` devuelve una fila por cada agrupación de columnas.

## Valor(es) devuelto(s) {#return-values}

Devuelve un entero que representa el recuento.

* * *

## Ejemplos {#examples}

### Devolver el número de filas de todo un recurso {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver el número de filas agrupadas por una expresión de columna {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Usar `COUNT` como función de ventana para calcular recuentos acumulados {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Para obtener más información, consulte [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
