---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función JSON_TYPE en StackQL: el tipo (object, array, text, integer, real, true, false o null) del elemento más externo de un valor JSON, o de una ruta dentro de él."
image: "/img/stackql-featured-image.png"
---
Devuelve el tipo de datos del elemento más externo de un valor JSON, o del elemento de la ruta opcional.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

:::tip 

Use la instrucción [**DESCRIBE**](/language-spec/describe) para localizar los tipos de datos `array` u `object` que se pueden usar con las funciones JSON de StackQL.

:::

:::note 

Si usa StackQL con un backend SQL `postgres`, use en su lugar la función `json_typeof`.

:::

## Sintaxis {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_array_or_object*__  
Un array u objeto JSON.

__*json_path*__  
La ruta JSON a un array u objeto contenido en *json_array_or_object*.  Consulte [jsonpath.com](https://jsonpath.com/).

> Si *json_path* no existe en *json_array_or_object*, la función devuelve `NULL`.

> La función `json_type()` genera un error si alguno de sus argumentos no está bien formado.

## Valor(es) devuelto(s) {#return-values}
Una cadena que representa el tipo de datos resuelto.

> Los tipos de datos incluyen `null`, `true`, `false`, `integer`, `real`, `text`, `array` u `object`.

* * *

## Ejemplos {#examples}

### Devolver el tipo de datos de un campo anidado en un objeto o array JSON {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

Para obtener más información, consulte [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
