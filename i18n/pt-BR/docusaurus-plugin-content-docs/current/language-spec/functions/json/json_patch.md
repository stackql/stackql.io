---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Aplicar um JSON MergePatch a um documento JSON com SQL no StackQL, modificando ou mesclando objetos JSON.
image: "/img/stackql-featured-image.png"
---
Aplica um JSON MergePatch (RFC-7396) a um documento JSON, modificando-o de acordo com as operações especificadas. Esta função serve para adicionar, modificar ou excluir elementos em objetos JSON, mas trata arrays JSON como unidades atômicas.

Veja também:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaxe {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*target_json*__  
O documento JSON ao qual o patch será aplicado.

__*patch_json*__  
Um documento JSON que descreve as alterações a serem aplicadas de acordo com o algoritmo MergePatch.

## Valor(es) retornado(s) {#return-values}
Retorna o documento JSON modificado após a aplicação do patch.

* * *

## Exemplos {#examples}

### MergePatch simples {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Saída: `{"a":1,"b":2,"c":3,"d":4}`

### Substituir um elemento em um array {#replace-element-in-array}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Saída: `{"a":9,"b":2}`

### Remover um elemento usando null {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Saída: `{"a":9,"c":8}`

### MergePatch aninhado {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Saída: `{"a":{"x":1,"y":9},"b":3,"c":8}`

Essa funcionalidade permite atualizações flexíveis e dinâmicas de JSON armazenado em um banco de dados, especialmente úteis para aplicações que exigem modificações condicionais de estruturas de dados.
