---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Atualizar ou inserir valores em um documento JSON com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Atualiza valores existentes ou insere novos valores em um documento JSON nos caminhos especificados.

Veja também:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Sintaxe {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_column*__  
A coluna ou expressão JSON a ser modificada.

__*path*__  
A expressão de caminho JSON que indica onde definir o valor. Se o caminho existir, a função atualiza o valor; se o caminho não existir, a função adiciona um novo par chave-valor.

__*value_to_set*__  
O valor a ser definido no caminho especificado do documento JSON.

## Valor(es) retornado(s) {#return-values}
Retorna um documento JSON com o valor definido no caminho especificado. Se o caminho existir, a função atualiza o valor existente; caso contrário, adiciona um novo par chave-valor.

:::note Comportamento se uma chave existir

| Função                                                       | Sobrescrever se já existir? | Criar se não existir? |
|--------------------------------------------------------------|-----------------------------|-----------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Não                         | Sim                   |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Sim                         | Não                   |
| [`json_set()`](/language-spec/functions/json/json_set)       | Sim                         | Sim                   |

:::

* * *

## Exemplos {#examples}

### Atualizar um valor existente em um objeto JSON {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending","region":"US"}', '$.status', 'active') as updated_json;

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne","status":"active","region":"US"}   |
-- |---------------------------------------------------------|
```

Esta consulta atualiza a chave 'status' para 'active' no documento JSON se a chave 'status' existir. Se 'status' não existir no documento, a consulta a adiciona com o valor 'active'.

### Adicionar um novo par chave-valor a um objeto JSON {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with a date value to a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending"}', '$.lastChecked', '2024-04-29') as updated_json;

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne","status":"pending","lastChecked":"2024-04-29"}   |
|-----------------------------------------------------------------------|
*/
```

Esta consulta adiciona uma nova chave 'lastChecked' com o valor '2024-04-29' ao documento JSON se 'lastChecked' ainda não existir. Se existir, a consulta atualiza o valor existente para '2024-04-29'.
