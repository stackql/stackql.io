---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função LENGTH no StackQL: o número de caracteres em uma string, ou o número de bytes em um BLOB."
image: "/img/stackql-featured-image.png"
---
Retorna o número de caracteres de uma expressão de entrada do tipo string antes do primeiro caractere `NULL`, ou o número de bytes de uma expressão BLOB.  

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

Para obter o comprimento de um array JSON, use a função [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## Sintaxe {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Uma string ASCII ou um BLOB.

> Retorna `NULL` se *expression* for `NULL`.

> Se *expression* for numérica, a função retorna o comprimento de uma representação em string de *expression*.

## Valor(es) retornado(s) {#return-values}
Retorna um inteiro.

* * *

## Exemplos {#examples}

### Retornar o comprimento de uma coluna de string {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
