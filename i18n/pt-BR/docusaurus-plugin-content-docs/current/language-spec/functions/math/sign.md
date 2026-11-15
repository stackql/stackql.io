---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função SIGN no StackQL: retornar -1, 0 ou 1 conforme um argumento numérico seja negativo, zero ou positivo."
image: "/img/stackql-featured-image.png"
---
Retorna -1 se o valor de entrada for negativo, 0 se o valor de entrada for 0 e 1 se o valor de entrada for positivo.  

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão da categoria de tipos de dados numéricos exatos ou numéricos aproximados.

> Se *numeric_expression* for `NULL` ou for uma `string` ou um BLOB que não pode ser convertido sem perdas em um número, a função retornará `NULL`.

## Valor(es) retornado(s) {#return-values}

Retorna `-1`, `0` ou `1`.

* * *

## Exemplos {#examples}

### Determinar o sinal de um número {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
