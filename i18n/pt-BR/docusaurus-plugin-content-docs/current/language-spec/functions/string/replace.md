---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função REPLACE no StackQL: substituir cada ocorrência de uma substring dentro de uma string por outra string."
image: "/img/stackql-featured-image.png"
---
Retorna uma cópia de uma string em que cada ocorrência de uma substring é substituída por outra string.

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Uma string literal ou coluna de string na qual você deseja fazer a substituição.

__*pattern*__  
A substring a ser encontrada na string original.

> Se *pattern* for uma string vazia ou não for encontrado em *character_expression*, a função retorna *character_expression* sem alterações. 

__*replacement*__  
A string de substituição.

> Se *replacement* não for inicialmente uma string, ele é convertido em uma string UTF-8 antes do processamento.

> As comparações usam a sequência de ordenação `BINARY`.

## Valor(es) retornado(s) {#return-values}
Retorna uma string.

* * *

## Exemplos {#examples}

### Substituir um padrão de string em uma coluna {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
