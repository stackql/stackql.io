---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função INSTR no StackQL: a posição, contada a partir de 1, da primeira ocorrência de uma string dentro de outra, ou 0 quando ela não é encontrada."
image: "/img/stackql-featured-image.png"
---
Retorna a posição de caractere da primeira ocorrência de uma string dentro de outra string; retorna 0 se a string procurada não for encontrada.

Veja também:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*string*__  
A string de origem na qual a busca é feita.

__*searchstring*__  
A substring procurada.

> Se *string* e *searchstring* forem ambos BLOBs, `instr(<string>, <searchstring>)` retorna o número de bytes anteriores à primeira ocorrência de *searchstring* mais um, ou 0 se *searchstring* não ocorrer em nenhum lugar de *string*. Se os dois argumentos não forem `NULL` e não forem BLOBs, ambos serão interpretados como strings. 

## Valor(es) retornado(s) {#return-values}
Retorna um inteiro.

> Se o valor de *string* ou de *searchstring* for `NULL`, o resultado será `NULL`.

* * *

## Exemplos {#examples}

### Procurar uma string em um campo {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurrence of the term 'worker' in the 'name' column
*/
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
