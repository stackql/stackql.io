---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função UPPER no StackQL: converter uma string em maiúsculas."
image: "/img/stackql-featured-image.png"
---
Retorna uma string de entrada em maiúsculas.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

## Sintaxe {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> A função `upper()` funciona somente com caracteres ASCII.

## Argumentos {#arguments}

__*character_expression*__  
Uma string ASCII.

## Valor(es) retornado(s) {#return-values}
Retorna um valor de string.

* * *

## Exemplos {#examples}

### Retornar uma string em maiúsculas {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
