---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana ROW_NUMBER en StackQL: numerar las filas de una partición a partir de 1 en el orden indicado por la cláusula ORDER BY de la ventana."
image: "/img/stackql-featured-image.png"
---
Devuelve el número de la fila dentro de la partición actual. Las filas se numeran a partir de 1 en el orden definido por la cláusula `ORDER BY` de la definición de la ventana, o en un orden arbitrario en caso contrario.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Sintaxis {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `ROW_NUMBER` se aplica a cada partición por separado y reinicia el número de fila en cada partición.

__*ORDER BY column*__
Especifica el orden en que se asignan los números de fila. Es necesario para obtener resultados significativos.

## Valor(es) devuelto(s) {#return-values}

Devuelve un entero que representa el número de fila secuencial dentro de la partición, a partir de 1.

* * *

## Ejemplos {#examples}

### Clasificar colaboradores dentro de un repositorio {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Seguir la secuencia de issues a lo largo del tiempo {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Comparar `ROW_NUMBER` con `RANK` y `DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

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

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
