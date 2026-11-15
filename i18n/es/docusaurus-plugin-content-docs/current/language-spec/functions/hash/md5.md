---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - hash
  - digest
description: "Función MD5 en StackQL: devolver el hash MD5 hexadecimal en minúsculas de una cadena, para huellas digitales y detección de cambios, no para seguridad."
image: "/img/stackql-featured-image.png"
---
Devuelve el hash MD5 de una cadena como 32 caracteres hexadecimales en minúsculas.  Disponible a partir de StackQL `v1.0`.

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Concatenación ]](/language-spec/functions/string/concatenation)

* * * 

## Sintaxis {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Una cadena, o un valor convertido a texto.  Un argumento `NULL` devuelve `NULL`.

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena hexadecimal de 32 caracteres en minúsculas.

> `MD5` se proporciona para generar huellas digitales y detectar cambios.  No es una primitiva de contraseñas ni de seguridad.  En el backend PostgreSQL se usa la propia función `md5` de la base de datos.

* * *

## Ejemplos {#examples}

### Obtener la huella digital de un valor derivado {#fingerprint-a-derived-value}

Las variables se sustituyen como valores con tipo, el entero se convierte a texto y se une al sufijo, y el hash se calcula sobre el resultado en minúsculas.  Las mismas entradas siempre producen la misma cadena hexadecimal de 32 caracteres, por lo que el valor se puede usar como clave estable.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### Detectar cambios de configuración {#detect-configuration-changes}

Calcular el hash de una columna JSON serializada proporciona un valor corto para almacenar y comparar entre ejecuciones.  Un bucket cuyo hash difiere del de la última ejecución tiene una configuración de IAM modificada, sin necesidad de almacenar ni comparar el documento completo.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
