---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función ROUND en StackQL: redondear un argumento numérico a un número dado de decimales, cero de forma predeterminada."
image: "/img/stackql-featured-image.png"
---
Redondea un argumento numérico.  

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión de la categoría de tipos de datos numéricos exactos o numéricos aproximados.

__*precision*__  
El número de dígitos a la derecha del punto decimal al que se redondea *numeric_expression*.

> Si se omite el argumento *precision*, se asume que es 0.

## Valor(es) devuelto(s) {#return-values}
Devuelve un valor numérico.

* * *

## Ejemplos {#examples}

### Redondear un número de punto flotante a dos decimales {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
