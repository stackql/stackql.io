---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Remover elementos de um documento JSON especificando caminhos com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Remove os elementos especificados de um documento JSON usando expressões de caminho. Se um caminho especificado não existir no documento, ele é ignorado. O processamento sequencial dos caminhos pode afetar a estrutura para as remoções seguintes.

Veja também:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaxe {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_document*__  
O documento JSON do qual os elementos serão removidos.

__*path1, path2, ...*__  
Uma ou mais expressões de caminho que apontam para os elementos do documento JSON a serem removidos.

## Valor(es) retornado(s) {#return-values}
Retorna uma cópia do documento JSON sem os elementos especificados. Se nenhum caminho for informado, retorna o JSON de entrada reformatado, sem o espaço em branco excedente.

* * *

## Exemplos {#examples}

### Remover um elemento de um array JSON {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Saída: `'[0,1,3,4]'`

### Remover vários elementos de um array JSON {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Saída: `'[1,3,4]'`

### Remover o último elemento usando um índice negativo {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Saída: `'[1,2,3]'`

### Remover um caminho inexistente {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Saída: `'{"x":25,"y":42}'`

### Remover uma chave existente de um objeto JSON {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Saída: `'{"x":25}'`

### Caso limite: remoção com o caminho raiz {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Saída: `NULL`

Estes exemplos mostram como usar `json_remove()` para manipular dados JSON dinamicamente, em vários cenários, de exclusões simples ao tratamento de casos limite em modificações de estruturas de dados.
