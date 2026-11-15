---
title: Estructura léxica
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Estructura léxica de StackQL: cómo el analizador sintáctico divide en componentes léxicos instrucciones, identificadores, comillas, literales, comentarios y espacios en blanco."
image: "/img/stackql-featured-image.png"
---

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy) [[ Palabras clave ]](/language-spec/keywords) [[ Tipos de datos ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


La estructura léxica y la gramática de StackQL se basan en la implementación del lenguaje de MySQL (una adaptación del estándar ANSI SQL-92) con algunas extensiones.  Las instrucciones de StackQL se usan para consultar recursos en la nube (con la gramática de `SELECT`) o para crear, modificar o eliminar infraestructura con instrucciones estándar del lenguaje de manipulación de datos (DML), como `INSERT`, `UPDATE`, `REPLACE` y `DELETE`.  Además, las operaciones que no son DML, como iniciar una instancia u obtener o colocar un objeto en un bucket de AWS S3, por ejemplo, se implementan como procedimientos integrados del provider (similares a los procedimientos almacenados de un SGBD).  Los procedimientos integrados del provider se invocan con la instrucción `EXEC`.

El lenguaje SQL de dominio específico de StackQL permite programar la infraestructura de forma tanto declarativa como procedimental.

* * * 

## Sintaxis {#syntax}
Las operaciones de StackQL constan de una o más instrucciones terminadas en punto y coma (`;`).  Las instrucciones incluyen palabras clave, identificadores, parámetros y valores; un ejemplo de instrucción de StackQL para crear un disco de 10GB en GCP sería:

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

En el ejemplo anterior, _`INSERT`_, _`INTO`_ y _`SELECT`_ son palabras clave, _google.compute.disks_ es un identificador, _project_, _zone_, _name_, _sizeGb_ son parámetros y _'myProject'_, _'australia-southeast1-a'_, _'my10GbDisk'_, _10_ son valores.

## Palabras clave {#keywords}
Las palabras clave no distinguen entre mayúsculas y minúsculas, es decir, `INSERT` e `insert` se tratan de forma equivalente; sin embargo, por convención suelen escribirse en mayúsculas. Encontrará la lista completa de palabras clave en la sección Palabras clave.

## Identificadores {#identifiers}
Los identificadores son símbolos que representan objetos de la jerarquía de recursos de un proveedor de nube.  Los identificadores constan de varias partes; encontrará más información sobre los identificadores en el tema Jerarquía de recursos de StackQL.

## Parámetros y valores {#parameters-and-values}
Los parámetros los especifica el recurso; se pueden obtener con una instrucción `DESCRIBE` sobre un recurso determinado del proveedor de nube.  Los valores son objetos escalares o complejos con tipo que se corresponden con los parámetros.  Para obtener más información sobre los parámetros y los valores, consulte los temas `DESCRIBE` y Tipos de datos.

## Variables {#variables}
Un identificador con prefijo de dólar, como `$region`, desreferencia una variable de sesión asignada con [`SET`](/language-spec/set); se sustituye por el valor con tipo de la variable antes de planificar la instrucción.  Un `'$region'` entre comillas simples es un literal de cadena.  En una cláusula `FROM`, una `$variable` se resuelve como nombre de relación, e `IDENTIFIER($variable)` se resuelve como nombre de columna en las posiciones de expresión.  Una ruta entre comillas dobles y con prefijo de dólar, como `"$.auth.aws.password"`, es un destino de configuración de tiempo de ejecución que aceptan `SET` y `UNSET`.  Las variables de sesión están disponibles a partir de StackQL `v1.0`; consulte [Variables de sesión](/getting-started/session-variables).

## Operadores {#operators}
`||` es el operador de concatenación de cadenas y tiene mayor precedencia que los operadores de comparación.  `OR` es la disyunción booleana.  Las versiones de StackQL anteriores a `v1.0` trataban `||` como `OR` en el análisis léxico.

## Comentarios {#comments}
StackQL admite comentarios de varias líneas y comentarios en línea dentro de las instrucciones de StackQL; a continuación se muestran ejemplos de comentarios:

```shell
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
