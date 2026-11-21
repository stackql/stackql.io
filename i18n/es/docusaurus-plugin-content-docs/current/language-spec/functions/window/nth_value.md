---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana NTH_VALUE en StackQL: el valor de una expresión para la enésima fila del marco de ventana."
image: "/img/stackql-featured-image.png"
---
Devuelve el valor de la expresión evaluada sobre la enésima fila del marco de ventana.

Esta función de ventana integrada calcula el marco de ventana de cada fila del mismo modo que una función de ventana de agregación. Devuelve el valor de la expresión evaluada sobre la fila N del marco de ventana. Las filas se numeran dentro del marco de ventana a partir de 1 en el orden definido por la cláusula `ORDER BY`, si está presente, o en un orden arbitrario en caso contrario. Si no hay una enésima fila en el marco de ventana, se devuelve `NULL`.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## Sintaxis {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
La expresión que se evalúa sobre la enésima fila del marco de ventana.

__*N*__
Un entero positivo que especifica el número de la fila que se obtiene (con índice basado en 1).

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `NTH_VALUE` se aplica dentro de cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se procesan las filas.

__*ROWS BETWEEN frame_start AND frame_end*__
Opcional. Define el marco de ventana.

## Valor(es) devuelto(s) {#return-values}

Devuelve el valor de la expresión evaluada sobre la enésima fila del marco de ventana, o `NULL` si la enésima fila no existe.

* * *

## Ejemplos {#examples}

### Obtener los tres principales colaboradores {#get-the-top-three-contributors}

```sql
-- Get the top 3 contributors by number of contributions
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
