---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana RANK en StackQL: el rango con huecos de la fila actual en su partición; las filas empatadas comparten rango y se omiten los rangos siguientes."
image: "/img/stackql-featured-image.png"
---
Devuelve el rango de la fila actual con huecos. Es el `ROW_NUMBER` de la primera fila homóloga de cada grupo. Las filas con valores iguales en las columnas de ordenación reciben el mismo rango, y el siguiente valor de rango se incrementa en el número de filas empatadas (lo que deja huecos).

Si no hay cláusula `ORDER BY`, todas las filas se consideran homólogas y esta función siempre devuelve 1.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Sintaxis {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `RANK` se aplica a cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se asignan los rangos. Si no hay cláusula `ORDER BY`, todas las filas se consideran homólogas y reciben el rango 1.

## Valor(es) devuelto(s) {#return-values}

Devuelve un entero que representa el rango de la fila actual dentro de la partición, con huecos para los valores empatados.

* * *

## Ejemplos {#examples}

### Clasificar colaboradores por contribuciones (con huecos) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Comparar `RANK` con `DENSE_RANK` y `ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### Clasificar colaboradores dentro de cada repositorio con `PARTITION BY` {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
