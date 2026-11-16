---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função NULLIF no StackQL: retornar NULL quando dois argumentos são iguais; caso contrário, retornar o primeiro argumento."
image: "/img/stackql-featured-image.png"
---
Aceita dois argumentos e retorna um valor `NULL` se eles forem iguais; caso contrário, retorna o primeiro argumento.  

> A função `NULLIF` equivale a `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> `NULLIF` é útil com funções de agregação como [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum) e [`COUNT`](/language-spec/functions/aggregate/count), quando o conjunto de resultados contém valores especiais que não são `NULL`, mas que você quer tratar como `NULL` para fins de agregação.

Veja também:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## Sintaxe {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Duas expressões de qualquer tipo de dados que resultam em um valor `NULL` ou não `NULL`.

> A função `NULLIF` procura em seus argumentos, da esquerda para a direita, um argumento que defina uma função de ordenação e usa essa função de ordenação em todas as comparações de strings.  Se nenhum dos argumentos definir uma função de ordenação, a ordenação `BINARY` é usada.

## Valor(es) retornado(s) {#return-values}

Retorna `NULL` se os dois argumentos *expression* resultarem no mesmo valor; caso contrário, retorna o primeiro valor. 

* * *

## Exemplos {#examples}

### Contar linhas em que um campo não é igual a um determinado valor {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name != 'controller-0';
*/
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
