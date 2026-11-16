---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Comparar dos cadenas JSON para comprobar su equivalencia con SQL.
image: "/img/stackql-featured-image.png"
---
Compara dos cadenas JSON y devuelve 1 si son equivalentes, o 0 en caso contrario.

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

:::tip 

La función `json_equal` es una función de extensión personalizada de StackQL. Admite la comparación de objetos y arrays JSON, y trata como equivalentes los objetos con claves en distinto orden, de acuerdo con la especificación de JSON. La función `json_equal` está disponible a partir de StackQL `0.5.707` y solo se admite con el backend SQL integrado en memoria predeterminado de StackQL.

:::

## Sintaxis {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Argumentos {#arguments}

__*json1*__  
Una cadena JSON que representa un objeto o un array.

__*json2*__  
Otra cadena JSON para comparar con la primera.

> Ambos argumentos deben ser cadenas JSON válidas.

## Valor(es) devuelto(s) {#return-values}
Un valor entero: 1 si las cadenas JSON son equivalentes, 0 si no lo son.

* * *

## Ejemplos {#examples}

### Comparar objetos JSON {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### Comparar arrays JSON {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

Para obtener más información, consulte [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
