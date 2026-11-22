---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Usar expressões regulares para encontrar padrões em strings com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Verifica se a string de origem corresponde ao padrão de expressão regular fornecido.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*source_string*__  
A string na qual o padrão é procurado.

__*pattern_expression*__  
O padrão de expressão regular a ser comparado com a string de origem.

## Valor(es) retornado(s) {#return-values}
Retorna `1` se a string de origem corresponder ao padrão de expressão regular; caso contrário, `0`.

* * *

## Exemplos {#examples}

### Verificar se uma string contém números {#check-if-a-string-contains-numbers}

Este exemplo verifica se a string 'the year is 2021' contém um ou mais caracteres numéricos:

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

Esta consulta retorna `1` porque a string contém a substring '2021', que corresponde ao padrão `[0-9]+`, que indica um ou mais dígitos.

### Validar o formato de e-mail {#validate-email-format}

Para verificar se uma string está em um formato de e-mail válido:

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$' AS match_result;
```

Esta consulta retorna `1`, o que indica que 'user@example.com' tem o formato de um endereço de e-mail típico e corresponde ao padrão de expressão regular especificado.  O ponto antes do sufixo do domínio é escrito `\\.` porque uma string literal do StackQL consome uma barra invertida; assim, uma barra invertida que pertence à expressão regular é escrita duas vezes (consulte [Referência de expressões regulares](/language-spec/functions/re/reference)).
