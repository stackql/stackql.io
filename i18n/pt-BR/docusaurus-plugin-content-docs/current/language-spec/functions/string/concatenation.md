---
title: Operador de concatenação
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "O operador de concatenação || no StackQL: unir dois ou mais valores de string em uma única string."
image: "/img/stackql-featured-image.png"
---
Retorna a string resultante da concatenação, ou união, de dois ou mais valores de string.  

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression1*__, __*character_expression2*__  
Uma string literal ou coluna de string.

## Valor(es) retornado(s) {#return-values}

Retorna uma string.

:::note

A partir do StackQL `v1.0`, o analisador sintático trata `||` como o operador de concatenação, com precedência maior que a dos operadores de comparação, de modo que `WHERE id = prefix || suffix` compara `id` com a string unida.  As versões anteriores tratavam o token `||` como o `OR` booleano.  Use `OR` para a disjunção booleana.  [Variáveis de sessão](/getting-started/session-variables) também podem ser concatenadas: `$prefix || '-' || $suffix`.

:::

* * *

## Exemplos {#examples}

### Concatenar dois campos {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

Para obter mais informações, consulte [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
