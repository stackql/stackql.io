---
title: LAST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana LAST_VALUE en StackQL: el valor de una expresión para la última fila del marco de ventana."
image: "/img/stackql-featured-image.png"
---
Devuelve el valor de la expresión evaluada sobre la última fila del marco de ventana.

Esta función de ventana integrada calcula el marco de ventana de cada fila del mismo modo que una función de ventana de agregación. Para cada fila, devuelve el valor de la expresión evaluada sobre la última fila del marco de ventana.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Sintaxis {#syntax}

```sql
SELECT LAST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
La expresión que se evalúa sobre la última fila del marco de ventana.

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `LAST_VALUE` se aplica dentro de cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se procesan las filas.

__*ROWS BETWEEN frame_start AND frame_end*__
Opcional. Define el marco de ventana. Las opciones habituales incluyen:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` (valor predeterminado)
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

## Valor(es) devuelto(s) {#return-values}

Devuelve el valor de la expresión evaluada sobre la última fila del marco de ventana.

* * *

## Ejemplos {#examples}

### Buscar la versión más reciente como contexto {#find-the-latest-release-for-context}

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

De forma predeterminada, el marco de ventana se extiende desde el inicio de la partición hasta la fila actual, por lo que `LAST_VALUE` devolvería el valor de la fila actual. Para obtener el último valor real de la partición, use `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
