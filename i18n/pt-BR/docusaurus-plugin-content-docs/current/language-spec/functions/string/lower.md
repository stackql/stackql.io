---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função LOWER no StackQL: converter uma string para minúsculas."
image: "/img/stackql-featured-image.png"
---
Retorna uma string de entrada em minúsculas.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

## Sintaxe {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> A função `lower()` funciona somente com caracteres ASCII.

## Argumentos {#arguments}

__*character_expression*__  
Uma string ASCII.

## Valor(es) retornado(s) {#return-values}
Retorna um valor de string.

* * *

## Exemplos {#examples}

### Retornar uma string em minúsculas {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
