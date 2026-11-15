---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Actualizar o insertar valores en un documento JSON con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Actualiza valores existentes o inserta nuevos valores en un documento JSON en las rutas especificadas.

Consulte también:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Sintaxis {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_column*__  
La columna o expresión JSON que se va a modificar.

__*path*__  
La expresión de ruta JSON que indica dónde establecer el valor. Si la ruta existe, el valor se actualiza; si la ruta no existe, se agrega un nuevo par clave-valor.

__*value_to_set*__  
El valor que se establece en la ruta especificada del documento JSON.

## Valor(es) devuelto(s) {#return-values}
Devuelve un documento JSON con el valor establecido en la ruta especificada. Si la ruta existe, el valor existente se actualiza; en caso contrario, se agrega un nuevo par clave-valor.

:::note Comportamiento si una clave existe

| Función                                                      | ¿Sobrescribir si ya existe? | ¿Crear si no existe? |
|--------------------------------------------------------------|-----------------------------|----------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | No                          | Sí                   |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Sí                          | No                   |
| [`json_set()`](/language-spec/functions/json/json_set)       | Sí                          | Sí                   |

:::

* * *

## Ejemplos {#examples}

### Actualizar un valor existente en un objeto JSON {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET(json_data, '$.status', 'active') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne", "status":"active", "region":"US"} |
-- |---------------------------------------------------------|
```

Esta consulta actualiza la clave 'status' a 'active' en el documento JSON si la clave 'status' existe. Si 'status' no existe en el documento, se agrega con el valor 'active'.

### Agregar un nuevo par clave-valor a un objeto JSON {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with the current date to a JSON object
SELECT JSON_SET(json_data, '$.lastChecked', '2024-04-29') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne", "status":"pending", "lastChecked":"2024-04-29"} |
|-----------------------------------------------------------------------|
*/
```

Esta consulta agrega una nueva clave 'lastChecked' con el valor '2024-04-29' al documento JSON si 'lastChecked' aún no existe. Si ya existe, el valor existente se actualiza a '2024-04-29'.
