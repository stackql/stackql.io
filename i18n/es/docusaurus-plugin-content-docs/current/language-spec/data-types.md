---
title: Tipos de datos
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Tipos de datos de StackQL: los tipos de las propiedades y los parámetros de recursos en consultas y resultados, y su correspondencia con los esquemas de API de los providers."
image: "/img/stackql-featured-image.png"
---

Los tipos de datos de StackQL se usan para representar los valores de los campos de entrada o de salida (propiedades o parámetros) usados en los recursos en la nube. Los tipos de datos de StackQL se enumeran en la tabla siguiente:

| Tipo de StackQL | Descripción | Ejemplo |
|--|--|--|
| `string` | secuencia de caracteres Unicode entre comillas simples | `'mybucket'` |
| `array` | colección ordenada de valores, entre corchetes `[ ]`, separados por una coma | `[0,1,2]` |
| `object` | conjunto no ordenado de cero o más pares nombre/valor insertados entre `{ }` y separados por una coma | `{"name": "mydb"}` |
| `boolean` | `true` o `false`, los valores booleanos no van entre comillas; de lo contrario, se tratan como valores de cadena | `true` |
| `integer` | número entero que puede ser positivo, negativo o cero | `2` |
| `double` | número de punto flotante de doble precisión, puede ser positivo o negativo | `0.95` |
<br/>
<br/>
> Los tipos de datos de StackQL se corresponden con los tipos de datos expuestos en las API de todos los principales proveedores de nube.

## Conversión de tipos {#type-conversion}

`CAST(expression AS type)` y `CONVERT(expression, type)` convierten un valor a otro tipo. Además de los tipos de conversión heredados de la gramática de MySQL (`CHAR`, `DATE`, `DATETIME`, `DECIMAL`, `SIGNED`, `UNSIGNED`, `TIME`, `JSON`), StackQL `v1.0` y las versiones posteriores aceptan `TEXT`, `INTEGER`, `INT`, `BIGINT`, `BOOLEAN` y `BOOL`. La conversión la realiza el backend SQL, por lo que su semántica es la de SQLite o PostgreSQL. Las conversiones se usan habitualmente para comparar una [variable de sesión](/getting-started/session-variables) tipada con una columna de cadena:

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

Los campos booleanos servidos por providers XML como AWS se devuelven como `true` y `false`; las versiones de StackQL anteriores a `v1.0` los devolvían como `0` y `1`.
