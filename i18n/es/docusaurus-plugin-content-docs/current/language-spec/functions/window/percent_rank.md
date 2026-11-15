---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana PERCENT_RANK en StackQL: el rango relativo de la fila actual dentro de su partición como un valor entre 0.0 y 1.0."
image: "/img/stackql-featured-image.png"
---
Devuelve un valor entre 0.0 y 1.0 que representa el rango relativo de la fila actual dentro de su partición.

A pesar del nombre, esta función devuelve un valor igual a `(rank - 1) / (partition-rows - 1)`, donde `rank` es el valor devuelto por la función de ventana integrada `RANK()` y `partition-rows` es el número total de filas de la partición. Si la partición contiene una sola fila, esta función devuelve 0.0.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## Sintaxis {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `PERCENT_RANK` se aplica a cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se calculan los rangos percentiles.

## Valor(es) devuelto(s) {#return-values}

Devuelve un número de punto flotante entre 0.0 y 1.0 que representa el rango percentil.

* * *

## Ejemplos {#examples}

### Calcular el rango percentil de los colaboradores {#calculate-percentile-ranking-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

El valor 0.0 de `PERCENT_RANK` indica la fila de menor rango, mientras que 1.0 indica la fila de mayor rango. Use esta función para identificar en qué punto de una distribución se sitúa un valor concreto.

:::

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
