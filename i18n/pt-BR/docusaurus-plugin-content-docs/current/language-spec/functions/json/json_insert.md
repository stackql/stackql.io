---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Inserir novos valores em um documento JSON sem sobrescrever os dados existentes com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Insere novos valores em um documento JSON nos caminhos especificados, sem sobrescrever os valores existentes.

Veja também:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Sintaxe {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_column*__  
A coluna ou expressão JSON a ser modificada.

__*path*__  
A expressão de caminho JSON em que o novo valor deve ser inserido. Se o caminho já existir, o valor original nesse caminho não é substituído.

__*new_value*__  
O valor a ser inserido no caminho especificado do documento JSON.

## Valor(es) retornado(s) {#return-values}
Retorna o documento JSON com o novo valor inserido se o caminho especificado ainda não existir; caso contrário, retorna o documento JSON sem alterações.

:::note Comportamento se uma chave existir

| Função                                                       | Sobrescreve se já existir? | Cria se não existir? |
|--------------------------------------------------------------|----------------------------|----------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Não                        | Sim                  |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Sim                        | Não                  |
| [`json_set()`](/language-spec/functions/json/json_set)       | Sim                        | Sim                  |

:::

* * *

## Exemplos {#examples}

### Inserir um novo par chave-valor em um objeto JSON {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT('{"name":"ResourceOne","type":"Storage"}', '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

Esta consulta insere uma nova chave chamada 'region' com o valor 'Europe' no documento JSON se a chave 'region' ainda não existir. Se 'region' já for uma chave do documento, a consulta retorna o documento original sem alterações.
