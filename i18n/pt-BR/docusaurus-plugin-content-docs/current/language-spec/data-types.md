---
title: Tipos de dados
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Tipos de dados do StackQL: os tipos usados para propriedades e parâmetros de recursos em consultas e resultados, e como eles correspondem aos esquemas de API dos providers."
image: "/img/stackql-featured-image.png"
---

Os tipos de dados do StackQL são usados para representar valores de campos de entrada ou saída (propriedades ou parâmetros) usados em recursos na nuvem. A tabela a seguir lista os tipos de dados do StackQL:

| Tipo do StackQL | Descrição | Exemplo |
|--|--|--|
| `string` | sequência de caracteres Unicode entre aspas simples | `'mybucket'` |
| `array` | coleção ordenada de valores, entre colchetes `[ ]`, separados por vírgula | `[0,1,2]` |
| `object` | conjunto não ordenado de zero ou mais pares nome/valor inseridos entre `{ }` e separados por vírgula | `{"name": "mydb"}` |
| `boolean` | `true` ou `false`, sem aspas (um valor entre aspas, como `'true'`, é tratado como string) | `true` |
| `integer` | um número inteiro que pode ser positivo, negativo ou zero | `2` |
| `double` | número de ponto flutuante de precisão dupla, pode ser positivo ou negativo | `0.95` |
<br/>
<br/>
> Os tipos de dados do StackQL correspondem aos tipos de dados expostos nas APIs de todos os principais provedores de nuvem.

## Conversão de tipos {#type-conversion}

`CAST(expression AS type)` e `CONVERT(expression, type)` convertem um valor em outro tipo. Além dos tipos de conversão herdados da gramática do MySQL (`CHAR`, `DATE`, `DATETIME`, `DECIMAL`, `SIGNED`, `UNSIGNED`, `TIME`, `JSON`), o StackQL `v1.0` e versões posteriores aceitam `TEXT`, `INTEGER`, `INT`, `BIGINT`, `BOOLEAN` e `BOOL`. O backend SQL faz a conversão, portanto a semântica é a do SQLite ou do PostgreSQL. As conversões são usadas com frequência para comparar uma [variável de sessão](/getting-started/session-variables) tipada com uma coluna de string:

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

Os campos booleanos servidos por providers XML, como o da AWS, são informados como `true` e `false`; as versões do StackQL anteriores à `v1.0` os informavam como `0` e `1`.
