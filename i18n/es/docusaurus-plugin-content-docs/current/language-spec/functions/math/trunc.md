---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función TRUNC en StackQL: la parte entera de un número, con redondeo hacia cero y sin la parte fraccionaria."
image: "/img/stackql-featured-image.png"
---

Devuelve la parte entera de un número redondeando hacia cero y descartando cualquier componente fraccionario.

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión de la categoría de tipos de datos numéricos exactos o numéricos aproximados.

## Valor(es) devuelto(s) {#return-values}
Devuelve un valor numérico truncado a su componente entero.

* * *

## Ejemplos {#examples}

### Truncar a días completos el número de días desde la creación de los buckets de Google Cloud Storage {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
