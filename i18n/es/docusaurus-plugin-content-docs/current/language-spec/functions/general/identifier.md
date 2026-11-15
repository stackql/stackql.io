---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variables de sesión
  - identifier
description: "Función IDENTIFIER en StackQL: resolver como referencia de columna una variable de sesión que contiene su nombre, para nombrar columnas en proyecciones y ORDER BY."
image: "/img/stackql-featured-image.png"
---
Resuelve un valor de cadena, normalmente una variable de sesión, en un nombre de columna.  Úsela donde una `$variable` por sí sola se leería como un valor escalar.  Disponible a partir de StackQL `v1.0`.

Consulte también:  
[[` SET `]](/language-spec/set) [[ Variables de sesión ]](/getting-started/session-variables)

* * * 

## Sintaxis {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Argumentos {#arguments}

__*variable*__  
Una variable de sesión, o un literal de cadena, que contiene un nombre de columna.  Se acepta un nombre calificado como `t.name`.  Se acepta exactamente un argumento, que debe ser un único valor de cadena; una concatenación como `'t.' || $column_name`, un segundo argumento o un valor numérico se rechazan con `IDENTIFIER requires one string value`.  Un valor vacío, o un valor que contiene una instrucción, se rechaza con `invalid identifier value`, y una variable no definida falla por no estar definida.  Todas estas comprobaciones se realizan antes de enviar cualquier solicitud al provider.

## Valor(es) devuelto(s) {#return-values}

En la instrucción analizada, la función se sustituye por una referencia a la columna nombrada, por lo que devuelve el valor de esa columna.  Los nombres se convierten en identificadores y nunca se insertan en el texto SQL.  La referencia de columna resultante se puede usar en cualquier lugar donde se admita una columna: la lista de selección, las cláusulas `WHERE`, `GROUP BY` y `ORDER BY`, los argumentos de funciones y dentro de CTE y subconsultas.

* * *

## Ejemplos {#examples}

### Proyectar y ordenar por una columna nombrada en una variable {#project-and-order-by-a-column-named-in-a-variable}

La columna que se devuelve y la columna por la que se ordena se toman ambas de `column_name`, y la columna de salida se renombra a partir de `output_name`.  `IDENTIFIER` es necesaria en la lista de selección y en `ORDER BY` porque una `$column_name` por sí sola en esas posiciones sería la cadena `'name'`, no la columna.  El alias usa, en cambio, la forma entre comillas dobles.  Una vez resueltas las variables, la instrucción es la misma que `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Filtrar por una columna nombrada en una variable {#filter-on-a-column-named-in-a-variable}

La referencia resuelta funciona tanto en un predicado como en una proyección.  Aquí, el campo por el que se filtra y el valor que debe coincidir provienen ambos de variables: `IDENTIFIER($filter_column)` se convierte en la columna `status`, mientras que `$filter_value` se pasa como el literal de cadena `'RUNNING'`.  Las dos formas son deliberadamente distintas, porque la primera nombra una columna y la segunda proporciona un valor.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Calificar la columna con un alias de tabla {#qualify-the-column-with-a-table-alias}

Un valor con punto se resuelve en una referencia de columna calificada.  La tabla recibe el alias `w`, que a su vez se toma de una variable, e `IDENTIFIER($column_name)` se convierte en `w.name`.  Esta es la forma que se debe usar cuando el mismo nombre de columna existe en ambos lados de un join y la referencia sería ambigua de otro modo.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Valores rechazados {#rejected-values}

Como el valor se convierte en un nombre y no en texto de consulta, todo lo que no sea un nombre utilizable se rechaza antes de planificar la instrucción, y ninguna solicitud llega al provider.  Las dos primeras instrucciones siguientes fallan con `invalid identifier value` y la tercera con `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> Una `$variable` en una cláusula `FROM` se resuelve en una relación sin `IDENTIFIER`, y una `"$variable"` entre comillas dobles se usa para alias, nombres de columna en DDL, listas de columnas de `INSERT` y destinos de `UPDATE`.
