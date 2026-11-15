---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función JSON_OBJECT en StackQL: construir un objeto JSON a partir de argumentos de clave y valor, por ejemplo para crear cuerpos de solicitud de INSERT y UPDATE."
image: "/img/stackql-featured-image.png"
---
Devuelve un objeto construido a partir de los argumentos proporcionados.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

:::tip 

Use la función [**DESCRIBE**](/language-spec/describe) para localizar los tipos de datos `array` u `object` que se pueden usar con las funciones JSON de StackQL.

:::

:::note 

La función `json_object` se implementa como `jsonb_build_object` si usa stackql con un backend SQL `postgres`.

:::

## Sintaxis {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*key1, key2, ...*__  
Las etiquetas del objeto JSON.

__*value1, value2, ...*__  
Los valores correspondientes a las etiquetas del objeto JSON.

## Valor(es) devuelto(s) {#return-values}
Una cadena que representa el objeto JSON bien formado.

* * *

## Ejemplos {#examples}

### Devolver el tipo de datos de un campo anidado en un objeto o array JSON {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

Para obtener más información, consulte [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
