---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função JSON_TYPE no StackQL: o tipo (object, array, text, integer, real, true, false ou null) do elemento mais externo de um valor JSON, ou de um caminho dentro dele."
image: "/img/stackql-featured-image.png"
---
Retorna o tipo de dados do elemento mais externo de um objeto ou array JSON de entrada e de um caminho.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

:::tip 

Use a instrução [**DESCRIBE**](/language-spec/describe) para localizar os tipos de dados `array` ou `object` que podem ser usados com as funções JSON do StackQL.

:::

:::note 

A função `json_type` é implementada como a função `json_typeof` se você usar o stackql com um backend SQL `postgres`

:::

## Sintaxe {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_array_or_object*__  
Um array ou objeto JSON.

__*json_path*__  
O caminho JSON para um array ou objeto contido em *json_array_or_object*.  Consulte [jsonpath.com](https://jsonpath.com/).

> Se *json_path* não existir em *json_array_or_object*, a função retorna `NULL`.

> A função `json_type()` gera um erro se algum dos seus argumentos não estiver bem formado.

## Valor(es) retornado(s) {#return-values}
Uma string que representa o tipo de dados resolvido.

> Os tipos de dados incluem `null`, `true`, `false`, `integer`, `real`, `text`, `array` ou `object`.

* * *

## Exemplos {#examples}

### Retornar o tipo de dados de um campo aninhado em um objeto ou array JSON {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

Para obter mais informações, consulte [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
