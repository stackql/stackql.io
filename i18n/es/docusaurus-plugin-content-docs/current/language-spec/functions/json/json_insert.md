---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Insertar nuevos valores en un documento JSON sin sobrescribir los datos existentes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Inserta nuevos valores en un documento JSON en las rutas especificadas sin sobrescribir los valores existentes.

Consulte también:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Sintaxis {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_column*__  
La columna o expresión JSON que se va a modificar.

__*path*__  
La expresión de ruta JSON donde se debe insertar el nuevo valor. Si la ruta ya existe, el valor original de esa ruta no se reemplaza.

__*new_value*__  
El valor que se inserta en la ruta especificada del documento JSON.

## Valor(es) devuelto(s) {#return-values}
Devuelve el documento JSON con el nuevo valor insertado si la ruta especificada aún no existe; en caso contrario, el documento JSON se devuelve sin cambios.

:::note Comportamiento si una clave existe

| Función                                                      | ¿Sobrescribir si ya existe? | ¿Crear si no existe? |
|--------------------------------------------------------------|-----------------------------|----------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | No                          | Sí                   |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Sí                          | No                   |
| [`json_set()`](/language-spec/functions/json/json_set)       | Sí                          | Sí                   |

:::

* * *

## Ejemplos {#examples}

### Insertar un nuevo par clave-valor en un objeto JSON {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT('{"name":"ResourceOne","type":"Storage"}', '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

Esta consulta inserta una nueva clave llamada 'region' con el valor 'Europe' en el documento JSON si la clave 'region' aún no existe. Si 'region' ya es una clave del documento, se devuelve el documento original sin cambios.
