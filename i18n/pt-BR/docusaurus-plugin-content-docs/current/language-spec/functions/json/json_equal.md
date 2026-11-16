---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Comparar duas strings JSON quanto à equivalência com SQL.
image: "/img/stackql-featured-image.png"
---
Compara duas strings JSON e retorna 1 se forem equivalentes, ou 0 caso contrário.

Veja também:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

:::tip 

A função `json_equal` é uma função de extensão personalizada do StackQL. Ela oferece suporte à comparação de objetos e arrays JSON e trata objetos com chaves em ordens diferentes como equivalentes, de acordo com a especificação JSON. A função `json_equal` está disponível a partir do StackQL `0.5.707` e só é compatível com o backend SQL padrão do StackQL, incorporado e em memória.

:::

## Sintaxe {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Argumentos {#arguments}

__*json1*__  
Uma string JSON que representa um objeto ou array.

__*json2*__  
Outra string JSON para comparar com a primeira.

> Os dois argumentos devem ser strings JSON válidas.

## Valor(es) retornado(s) {#return-values}
Um valor inteiro: 1 se as strings JSON forem equivalentes, 0 se não forem.

* * *

## Exemplos {#examples}

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

Para obter mais informações, consulte [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
