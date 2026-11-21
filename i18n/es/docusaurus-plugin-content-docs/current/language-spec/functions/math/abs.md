---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función ABS en StackQL: el valor absoluto de un argumento numérico."
image: "/img/stackql-featured-image.png"
---
Devuelve el valor absoluto del argumento numérico.  

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `abs(X)` devuelve `NULL` si `X` es `NULL`. `abs(X)` devuelve `0.0` si `X` es una cadena o un blob que no se puede convertir en un valor numérico. Si `X` es el entero -9223372036854775808, `abs(X)` genera un error de desbordamiento de enteros, ya que no existe un valor positivo equivalente en complemento a dos de 64 bits.

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión de la categoría de tipos de datos numéricos exactos o numéricos aproximados.

## Valor(es) devuelto(s) {#return-values}

Devuelve un valor numérico no negativo.

* * *

## Ejemplos {#examples}

### Devolver el valor absoluto de un número {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#abs](https://www.sqlite.org/lang_corefunc.html#abs)
