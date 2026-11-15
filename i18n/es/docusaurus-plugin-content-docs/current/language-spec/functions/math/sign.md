---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función SIGN en StackQL: devolver -1, 0 o 1 según si un argumento numérico es negativo, cero o positivo."
image: "/img/stackql-featured-image.png"
---
Devuelve -1 si el valor de entrada es negativo, 0 si el valor de entrada es 0 y 1 si el valor de entrada es positivo.  

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión de la categoría de tipos de datos numéricos exactos o numéricos aproximados.

> Si *numeric_expression* es `NULL` o es un `string` o un BLOB que no se puede convertir en un número sin pérdida, la función devuelve `NULL`.

## Valor(es) devuelto(s) {#return-values}

Devuelve `-1`, `0` o `1`.

* * *

## Ejemplos {#examples}

### Determinar el signo de un número {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
