---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função CHAR no StackQL: os caracteres de um ou mais códigos de caractere, para inserir quebras de linha, aspas, tabulações e outros caracteres de controle em strings."
image: "/img/stackql-featured-image.png"
---

A função `CHAR()` retorna uma string formada pelos caracteres de um ou mais códigos de caractere.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
Ao usar o StackQL com um backend PostgreSQL, a função equivalente é `CHR()`, que recebe um único código.
:::

## Sintaxe {#syntax}

```sql
SELECT CHAR(ascii_code [, ascii_code, ...]) FROM <multipartIdentifier>;
```

> Cada argumento adiciona um caractere ao resultado; assim, `CHAR(72, 105)` retorna `Hi`.

## Argumentos {#arguments}

**_ascii_code_**  
Um ou mais inteiros, cada um o código de um caractere: um código ASCII ou qualquer ponto de código Unicode.

## Valor(es) retornado(s) {#return-values}

Retorna uma string com um caractere para cada argumento *ascii_code*.

* * *

## Códigos ASCII comuns {#common-ascii-codes}

| Caractere     | Descrição             | Código ASCII | Exemplo de uso                  |
|---------------|-----------------------|--------------|---------------------------------|
| `\n`          | Nova linha            | 10           | `CHAR(10)`                      |
| `'`           | Aspas simples         | 39           | `CHAR(39)`                      |
| `"`           | Aspas duplas          | 34           | `CHAR(34)`                      |
| (espaço)      | Espaço                | 32           | `CHAR(32)`                      |
| `:`           | Dois-pontos           | 58           | `CHAR(58)`                      |
| `;`           | Ponto e vírgula       | 59           | `CHAR(59)`                      |
| `\t`          | Tabulação horizontal  | 9            | `CHAR(9)`                       |
| `\r`          | Retorno de carro      | 13           | `CHAR(13)`                      |
| `\b`          | Retrocesso            | 8            | `CHAR(8)`                       |
| `\f`          | Avanço de página      | 12           | `CHAR(12)`                      |

* * *

## Exemplos {#examples}

### Concatenar strings com caracteres especiais {#concatenate-strings-with-special-characters}

Para concatenar strings com um caractere de nova linha entre elas:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Resultado** (para um usuário com o nome de exibição `Jane Doe` e o ID `1234567890123456`):

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
