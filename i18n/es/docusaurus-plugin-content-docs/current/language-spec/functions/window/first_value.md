---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana FIRST_VALUE en StackQL: el valor de una expresión para la primera fila del marco de ventana."
image: "/img/stackql-featured-image.png"
---
Devuelve el valor de la expresión evaluada sobre la primera fila del marco de ventana.

Esta función de ventana integrada calcula el marco de ventana de cada fila de la misma manera que una función de ventana de agregación. Devuelve, para cada fila, el valor de la expresión evaluada sobre la primera fila del marco de ventana.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Sintaxis {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
La expresión que se evalúa sobre la primera fila del marco de ventana.

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `FIRST_VALUE` se aplica dentro de cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se procesan las filas.

__*ROWS BETWEEN frame_start AND frame_end*__
Opcional. Define el marco de ventana. Entre las opciones habituales se incluyen:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Sin una cláusula de marco, el marco es `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: termina en la última fila empatada con la fila actual en las columnas de `ORDER BY`, no en la fila actual.

## Valor(es) devuelto(s) {#return-values}

Devuelve el valor de la expresión evaluada sobre la primera fila del marco de ventana.

* * *

## Ejemplos {#examples}

### Obtener la primera versión como contexto {#find-the-first-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

De forma predeterminada, el marco de ventana se extiende desde el inicio de la partición hasta la fila actual. Para incluir todas las filas de la partición, use `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
