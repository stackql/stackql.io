---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Eliminar elementos de un documento JSON especificando rutas con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Elimina los elementos especificados de un documento JSON mediante expresiones de ruta. Si una ruta especificada no existe en el documento, se ignora. El procesamiento secuencial de las rutas puede afectar a la estructura para las eliminaciones posteriores.

Consulte también:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaxis {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_document*__  
El documento JSON del que se van a eliminar elementos.

__*path1, path2, ...*__  
Una o varias expresiones de ruta que apuntan a los elementos del documento JSON que se van a eliminar.

## Valor(es) devuelto(s) {#return-values}
Devuelve una copia del documento JSON sin los elementos especificados. Si no se proporciona ninguna ruta, devuelve el JSON de entrada con un nuevo formato, sin los espacios en blanco sobrantes.

* * *

## Ejemplos {#examples}

### Eliminar un elemento de un array JSON {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Salida: `'[0,1,3,4]'`

### Eliminar varios elementos de un array JSON {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Salida: `'[1,3,4]'`

### Eliminar el último y el primer elemento con un índice negativo {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Salida: `'[1,2,3]'`

### Eliminar una ruta inexistente {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Salida: `'{"x":25,"y":42}'`

### Eliminar una clave existente de un objeto JSON {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Salida: `'{"x":25}'`

### Caso límite: eliminación con la ruta raíz {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Salida: `NULL`

Estos ejemplos ilustran cómo se puede usar `json_remove()` para manipular datos JSON de forma dinámica, en distintos escenarios que van desde eliminaciones simples hasta el tratamiento de casos límite en la modificación de estructuras de datos.
