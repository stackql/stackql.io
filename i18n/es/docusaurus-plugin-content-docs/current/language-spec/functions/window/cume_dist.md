---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana CUME_DIST en StackQL: la distribución acumulada de la fila actual en su partición, como número de fila dividido entre las filas de la partición."
image: "/img/stackql-featured-image.png"
---
Devuelve la distribución acumulada de un valor dentro de una partición. Se calcula como `row-number / partition-rows`, donde `row-number` es el valor devuelto por `ROW_NUMBER()` para la última fila homóloga del grupo y `partition-rows` es el número de filas de la partición.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaxis {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `CUME_DIST` se aplica a cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se calcula la distribución acumulada.

## Valor(es) devuelto(s) {#return-values}

Devuelve un número de punto flotante entre 0.0 y 1.0 que representa el valor de la distribución acumulada.

* * *

## Ejemplos {#examples}

### Calcular la distribución acumulada de los colaboradores {#calculate-cumulative-distribution-of-contributors}

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

`CUME_DIST` devuelve la proporción de filas con valores menores o iguales que el valor de la fila actual. Un valor de 0.75 significa que el 75% de las filas tiene valores menores o iguales que el de la fila actual.

:::

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
