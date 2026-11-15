---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función CHAR en StackQL: los caracteres de uno o más códigos de carácter, para incluir saltos de línea, comillas, tabulaciones y otros caracteres de control en cadenas."
image: "/img/stackql-featured-image.png"
---

La función `CHAR()` devuelve el carácter correspondiente al código ASCII especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
Cuando se usa StackQL con un backend de PostgreSQL, la función equivalente es `CHR()`.
:::

## Sintaxis {#syntax}

```sql
SELECT CHAR(ascii_code) FROM <multipartIdentifier>;
```

> La función `CHAR()` devuelve el carácter asociado al código ASCII dado.

## Argumentos {#arguments}

**_ascii_code_**  
Un entero que representa el código ASCII del carácter deseado.

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena de un solo carácter correspondiente al código ASCII especificado.

* * *

## Códigos ASCII comunes {#common-ascii-codes}

| Carácter      | Descripción            | Código ASCII | Ejemplo de uso                  |
|---------------|------------------------|--------------|---------------------------------|
| `\n`          | Salto de línea         | 10           | `CHAR(10)`                      |
| `'`           | Comilla simple         | 39           | `CHAR(39)`                      |
| `"`           | Comilla doble          | 34           | `CHAR(34)`                      |
| (espacio)     | Espacio                | 32           | `CHAR(32)`                      |
| `:`           | Dos puntos             | 58           | `CHAR(58)`                      |
| `;`           | Punto y coma           | 59           | `CHAR(59)`                      |
| `\t`          | Tabulación horizontal  | 9            | `CHAR(9)`                       |
| `\r`          | Retorno de carro       | 13           | `CHAR(13)`                      |
| `\b`          | Retroceso              | 8            | `CHAR(8)`                       |
| `\f`          | Salto de página        | 12           | `CHAR(12)`                      |

* * *

## Ejemplos {#examples}

### Concatenar cadenas con caracteres especiales {#concatenate-strings-with-special-characters}

Para concatenar cadenas con un carácter de salto de línea entre ellas:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Resultado:**

```
He said "Hello" and left.
```

* * *
