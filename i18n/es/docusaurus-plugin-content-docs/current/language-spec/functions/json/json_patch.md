---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Aplicar un JSON MergePatch a un documento JSON con SQL en StackQL para modificar o fusionar objetos JSON.
image: "/img/stackql-featured-image.png"
---
Aplica un JSON MergePatch (RFC-7396) a un documento JSON y lo modifica según las operaciones especificadas. Esta función se usa para agregar, modificar o eliminar elementos dentro de objetos JSON, pero trata los arrays JSON como unidades atómicas.

Consulte también:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaxis {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*target_json*__  
El documento JSON al que se aplica el parche.

__*patch_json*__  
Un documento JSON que describe los cambios que se aplican según el algoritmo MergePatch.

## Valor(es) devuelto(s) {#return-values}
Devuelve el documento JSON modificado después de aplicar el parche.

* * *

## Ejemplos {#examples}

### MergePatch simple {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Salida: `{"a":1,"b":2,"c":3,"d":4}`

### Reemplazar un array por un valor {#replace-an-array-with-a-value}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Salida: `{"a":9,"b":2}`

### Eliminar un elemento con null {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Salida: `{"a":9,"c":8}`

### MergePatch anidado {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Salida: `{"a":{"x":1,"y":9},"b":3,"c":8}`

Esta funcionalidad permite actualizaciones flexibles y dinámicas de JSON almacenado en una base de datos, lo que resulta especialmente útil para aplicaciones que requieren modificaciones condicionales de estructuras de datos.
