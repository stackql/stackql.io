---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función con valores de tabla JSON_EACH en StackQL: expandir un array u objeto JSON en una fila por elemento o miembro, para aplicar joins y filtros a respuestas de API anidadas."
image: "/img/stackql-featured-image.png"
---
Función con valores de tabla que devuelve una tabla formada por una fila por cada elemento del array o miembro del objeto.

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

:::tip 

Use la instrucción [**DESCRIBE**](/language-spec/describe) para localizar los tipos de datos `array` que se pueden usar con la función `json_each`.

:::

:::note 

La función `json_each` se implementa como la función `json_array_elements_text` si usa stackql con un backend SQL `postgres`

:::

## Sintaxis {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## Argumentos {#arguments}

__*json_array_or_object*__  
Un array u objeto JSON.

__*json_path*__  
La ruta JSON a un array u objeto contenido en *json_array_or_object*.  Consulte [jsonpath.com](https://jsonpath.com/).

> Si *json_path* no existe en *json_array_or_object*, la función no devuelve ninguna fila.

> La función `json_each()` genera un error si *json_array_or_object* no es JSON bien formado.

## Valor(es) devuelto(s) {#return-values}
Una tabla formada por una fila por cada elemento del array o miembro del objeto.

* * *

## Ejemplos {#examples}

### Desanidar un array de cadenas en una tabla con un join a los campos asociados {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

como contexto:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

devuelve...

```
|-----------|--------|
|   name    |  type  |
|-----------|--------|
| condition | object |
|-----------|--------|
| members   | array  |
|-----------|--------|
| role      | string |
|-----------|--------|
```

La siguiente consulta de `stackql` usa la función `json_each` para desanidar el array `members` en una tabla con un join a los campos `role` y, opcionalmente, `condition`.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

devolvería...

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

Para obtener más información, consulte [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
