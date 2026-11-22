---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana LEAD en StackQL: evaluar una expresión sobre una fila posterior de la partición, con un desplazamiento y un valor predeterminado opcionales."
image: "/img/stackql-featured-image.png"
---
Devuelve el resultado de evaluar una expresión sobre una fila posterior de la partición.

La primera forma de `LEAD()` devuelve el resultado de evaluar la expresión sobre la fila siguiente de la partición. O bien, si no hay fila siguiente (porque la fila actual es la última), se devuelve `NULL`.

Si se proporciona el argumento offset, debe ser un entero no negativo. El valor devuelto es el resultado de evaluar la expresión sobre la fila situada *offset* filas después de la fila actual dentro de la partición. Si offset es 0, la expresión se evalúa sobre la fila actual. Si no hay ninguna fila situada *offset* filas después de la fila actual, se devuelve `NULL`.

Si además se proporciona un valor predeterminado, se devuelve este en lugar de `NULL` cuando la fila identificada por offset no existe.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## Sintaxis {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
La expresión que se evalúa sobre la fila posterior.

__*offset*__
Opcional. Un entero no negativo que especifica cuántas filas hacia adelante se debe mirar. Valor predeterminado: 1.

__*default*__
Opcional. El valor que se devuelve si la fila indicada por offset no existe. Valor predeterminado: `NULL`.

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `LEAD` se aplica dentro de cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se procesan las filas.

## Valor(es) devuelto(s) {#return-values}

Devuelve el valor de la expresión evaluada sobre la fila posterior especificada, o `NULL` (o el valor predeterminado) si esa fila no existe.

* * *

## Ejemplos {#examples}

### Mirar hacia adelante a la versión siguiente {#look-ahead-to-the-next-release}

```sql
-- Compare each release to the previous and next release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
