---
title: Variables de sesión
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variables de sesión
  - set
  - unset
  - identifier
description: "Variables de sesión en StackQL: asignar valores escalares tipados con SET, usarlos con $name en predicados y expresiones y como nombres de relación y columna con IDENTIFIER."
image: "/img/stackql-featured-image.png"
---

Consulte también:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Sesiones de provider ]](/getting-started/provider-sessions) [[ Usar variables ]](/getting-started/variables)

Las variables de sesión son valores escalares que conserva la sesión actual y a los que se hace referencia desde instrucciones posteriores. Están disponibles a partir de StackQL `v1.0` y funcionan en el shell interactivo, en los scripts de `exec`, a través del protocolo de red de PostgreSQL y a través del servidor MCP.

Las variables de sesión son distintas de las variables de plantilla descritas en [Usar variables](/getting-started/variables). Las variables de plantilla se suministran a `stackql exec` mediante `--iqldata` y `--var` y se renderizan en el texto del script antes de que este se analice. Las variables de sesión se resuelven dentro de la instrucción analizada, conservan su tipo y se pueden cambiar entre instrucciones.

## Definir y eliminar variables {#setting-and-removing-variables}

[`SET`](/language-spec/set) asigna una cadena, un número, un booleano o `null`. [`UNSET`](/language-spec/unset) elimina una variable; `SET name = DEFAULT` es equivalente. Un `null` explícito es un valor almacenado, no un restablecimiento.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

Los arrays y los objetos no son valores escalares. Para guardar un documento JSON, almacénelo como una cadena:

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## Desreferenciar una variable {#dereferencing-a-variable}

Use `$name`, sin comillas, en cualquier posición de expresión. Las cadenas se escapan como literales de SQL; los números, los booleanos y `null` conservan su tipo. Un `'$name'` entre comillas simples es el texto literal `$name`, no una desreferencia. Una referencia a una variable que no existe falla antes de que se haga ninguna solicitud al provider.

Los valores resueltos se pueden usar en expresiones SQL ordinarias, incluidos `CAST`, `CONVERT`, la concatenación con `||` y las llamadas a funciones:

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

Las asignaciones solo aceptan literales escalares o referencias directas; aplique las conversiones y otras expresiones donde se usa la variable, no en la instrucción `SET`.

## Variables en predicados {#variables-in-predicates}

Una variable en una cláusula `WHERE` se resuelve antes de que se planifique la instrucción, por lo que se enruta a la API del provider exactamente igual que un literal. Por lo tanto, los parámetros obligatorios se pueden suministrar desde variables:

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

Los valores tipados se comparan con su tipo. `WHERE active = $enabled` con `SET enabled = false` filtra por el booleano, y `WHERE id = CAST($some_id AS TEXT)` es la forma que se debe usar cuando la columna del provider es una cadena y la variable es un número.

## Variables como nombres de objetos {#variables-as-object-names}

Las variables pueden nombrar relaciones y columnas además de valores. Los nombres se convierten en identificadores en la instrucción analizada y nunca se insertan en el texto SQL, de modo que un valor que contiene una instrucción, está vacío o no es una cadena se rechaza antes de cualquier acceso al provider.

| Posición | Forma |
|--|--|
| Cláusula `FROM` | `FROM $relation_name`, o por componente, `FROM $provider.$service.$resource` |
| Columna en una expresión | `IDENTIFIER($column_name)`, consulte [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| Alias de columna o de tabla | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| Nombres de columna en DDL, listas de columnas de `INSERT`, destinos de `UPDATE` | la forma `"$name"` entre comillas dobles |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

Los joins, las CTE y las subconsultas resuelven sus variables de relación de la misma manera. Los valores de relación con puntos tienen hasta cuatro componentes. Las variables no son fragmentos de SQL arbitrarios: no se puede sustituir una palabra clave, una lista de columnas separadas por comas ni una cláusula `WHERE` completa.

## Ámbito y aislamiento {#scope-and-isolation}

Las variables pertenecen a la sesión que las definió. Las conexiones independientes a un servidor `stackql srv` tienen variables independientes, al igual que las invocaciones independientes de `exec`. Cada solicitud MCP es su propia sesión, por lo que un `SET` y la consulta que lo usa deben enviarse en el mismo lote de instrucciones. Las sesiones nuevas empiezan sin variables.

La sesión es el único ámbito. `SET SESSION` y `UNSET SESSION` se aceptan y significan exactamente lo mismo que `SET` y `UNSET`; la palabra clave existe para los scripts que deben leerse igual que en las bases de datos donde la configuración de sesión y la global son distintas. `GLOBAL` se rechaza, porque no hay ningún estado compartido entre sesiones.

Las instrucciones preparadas resuelven las variables cuando se ejecutan, no cuando se preparan, y un plan que contiene valores de sesión vinculados no se reutiliza entre ejecuciones.

Con la [gestión de accesos](/command-line-usage/access-management) habilitada, leer una variable requiere `session/variables:read` y definir o restablecer una requiere `session/config:write`. Resolver una variable a un nombre de objeto no concede ningún permiso sobre ese objeto; la relación resuelta se autoriza antes de la ejecución, como de costumbre.
