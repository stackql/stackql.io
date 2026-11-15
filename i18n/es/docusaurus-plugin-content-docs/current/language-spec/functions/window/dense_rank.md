---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana DENSE_RANK en StackQL: el rango sin huecos de la fila actual en su partición, de modo que las filas empatadas comparten rango y el siguiente es consecutivo."
image: "/img/stackql-featured-image.png"
---
Devuelve el rango de la fila actual sin huecos. Es el número del grupo de filas homólogas de la fila actual dentro de su partición. Las filas con valores iguales en las columnas de ordenación reciben el mismo rango, y el siguiente valor de rango siempre se incrementa en 1 (sin huecos).

Las filas se numeran a partir de 1 en el orden definido por la cláusula `ORDER BY` de la definición de ventana. Si no hay cláusula `ORDER BY`, todas las filas se consideran homólogas y esta función siempre devuelve 1.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaxis {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `DENSE_RANK` se aplica a cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se asignan los rangos. Si no hay cláusula `ORDER BY`, todas las filas se consideran homólogas y reciben el rango 1.

## Valor(es) devuelto(s) {#return-values}

Devuelve un entero que representa el rango denso de la fila actual dentro de la partición, sin huecos para los valores empatados.

* * *

## Ejemplos {#examples}

### Clasificar colaboradores por contribuciones (sin huecos) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### Comparar `DENSE_RANK` con `RANK` y `ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Clasificar contribuciones agregadas de varios repositorios {#rank-aggregated-contributions-across-multiple-repositories}

```sql
-- Rank total contributions across multiple repos
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

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
