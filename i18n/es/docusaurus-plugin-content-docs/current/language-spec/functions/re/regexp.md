---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Usar expresiones regulares para buscar coincidencias de patrones en cadenas con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Comprueba si la cadena de origen coincide con el patrón de expresión regular proporcionado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*source_string*__  
La cadena en la que se busca el patrón.

__*pattern_expression*__  
El patrón de expresión regular que se compara con la cadena de origen.

## Valor(es) devuelto(s) {#return-values}
Devuelve `1` si la cadena de origen coincide con el patrón de expresión regular y `0` en caso contrario.

* * *

## Ejemplos {#examples}

### Comprobar si una cadena contiene números {#check-if-a-string-contains-numbers}

Este ejemplo comprueba si la cadena 'the year is 2021' contiene uno o más caracteres numéricos:

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

Esta consulta devuelve `1` porque la cadena contiene la subcadena '2021', que coincide con el patrón `[0-9]+`, el cual indica uno o más dígitos.

### Validar el formato de correo electrónico {#validate-email-format}

Para comprobar si una cadena tiene un formato de correo electrónico válido:

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

Esta consulta devuelve `1`, lo que indica que 'user@example.com' tiene el formato de una dirección de correo electrónico típica y coincide con el patrón de expresión regular especificado.
