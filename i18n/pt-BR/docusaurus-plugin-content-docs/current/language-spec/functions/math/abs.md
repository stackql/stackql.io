---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função ABS no StackQL: o valor absoluto de um argumento numérico."
image: "/img/stackql-featured-image.png"
---
Retorna o valor absoluto do argumento numérico.  

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `abs(X)` retorna `NULL` se `X` for `NULL`. `abs(X)` retorna `0.0` se `X` for uma string ou um blob que não pode ser convertido em um valor numérico. Se `X` for o inteiro -9223372036854775808, `abs(X)` gera um erro de estouro de inteiro, pois não existe um valor positivo equivalente em complemento de dois de 64 bits.

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão da categoria de tipos de dados numéricos exatos ou numéricos aproximados.

## Valor(es) retornado(s) {#return-values}

Retorna um valor numérico não negativo.

* * *

## Exemplos {#examples}

### Retornar o valor absoluto de um número {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#abs](https://www.sqlite.org/lang_corefunc.html#abs)
