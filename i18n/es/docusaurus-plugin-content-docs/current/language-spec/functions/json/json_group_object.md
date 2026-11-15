---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Agregar pares clave-valor en un objeto JSON con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Agrega pares clave-valor en un objeto JSON, agrupando por una columna especificada. `JSON_GROUP_OBJECT()` recopila todos los pares clave-valor de cada grupo de las columnas especificadas y los combina en un único objeto JSON por cada grupo.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Argumentos {#arguments}

__*key_column*__  
La columna que proporciona las claves de los objetos JSON.

__*value_column*__  
La columna que proporciona los valores de los objetos JSON.

__*grouping_column*__  
La columna por la que se agrupan los resultados.

## Valor(es) devuelto(s) {#return-values}
Devuelve un objeto JSON que contiene todos los pares clave-valor agregados de las columnas especificadas, agrupados según `grouping_column`.

* * *

## Ejemplos {#examples}

### Agregar los tamaños de los repositorios por lenguaje de programación {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

Esta consulta devuelve un objeto JSON por cada lenguaje de programación, donde cada objeto contiene los nombres de los repositorios y sus tamaños correspondientes como pares clave-valor. Esta es una muestra de la salida esperada:

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

Esta salida es útil para visualizar la distribución de los tamaños de los repositorios entre los distintos lenguajes de programación de una organización, y proporciona datos claros y organizados agrupados por lenguaje.
