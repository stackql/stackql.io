---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana LAG en StackQL: evaluar una expresión sobre una fila anterior de la partición, con un desplazamiento y un valor predeterminado opcionales."
image: "/img/stackql-featured-image.png"
---
Devuelve el resultado de evaluar una expresión sobre una fila anterior de la partición.

La primera forma de `LAG()` devuelve el resultado de evaluar la expresión sobre la fila anterior de la partición. O bien, si no hay fila anterior (porque la fila actual es la primera), se devuelve `NULL`.

Si se proporciona el argumento offset, debe ser un entero no negativo. El valor devuelto es el resultado de evaluar la expresión sobre la fila situada *offset* filas antes de la fila actual dentro de la partición. Si offset es 0, la expresión se evalúa sobre la fila actual. Si no hay ninguna fila situada *offset* filas antes de la fila actual, se devuelve `NULL`.

Si también se proporciona un valor predeterminado, se devuelve este en lugar de `NULL` cuando la fila identificada por offset no existe.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## Sintaxis {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
La expresión que se evalúa sobre la fila anterior.

__*offset*__
Opcional. Un entero no negativo que especifica cuántas filas hacia atrás se debe buscar. Valor predeterminado: 1.

__*default*__
Opcional. El valor que se devuelve si la fila indicada por el desplazamiento no existe. Valor predeterminado: `NULL`.

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `LAG` se aplica dentro de cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que se procesan las filas.

## Valor(es) devuelto(s) {#return-values}

Devuelve el valor de la expresión evaluada sobre la fila anterior especificada, o `NULL` (o el valor predeterminado) si esa fila no existe.

* * *

## Ejemplos {#examples}

### Comparar cada versión con la versión anterior {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Analizar los intervalos entre la creación de issues {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
