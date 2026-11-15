---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função TRUNC no StackQL: a parte inteira de um número, com arredondamento em direção a zero e descarte de qualquer fração."
image: "/img/stackql-featured-image.png"
---

Retorna a parte inteira de um número arredondando em direção a zero e removendo qualquer componente fracionário.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão da categoria de tipos de dados numéricos exatos ou numéricos aproximados.

## Valor(es) retornado(s) {#return-values}
Retorna um valor numérico truncado para o seu componente inteiro.

* * *

## Exemplos {#examples}

### Truncar para dias inteiros o número de dias desde a criação dos buckets do Google Cloud Storage {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
