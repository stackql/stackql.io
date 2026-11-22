---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Agregar pares chave-valor em um objeto JSON com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Agrega pares chave-valor em um objeto JSON, agrupando por uma coluna especificada. `JSON_GROUP_OBJECT()` coleta todos os pares chave-valor de cada grupo nas colunas especificadas e os combina em um único objeto JSON para cada grupo.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Argumentos {#arguments}

__*key_column*__  
A coluna que fornece as chaves dos objetos JSON.

__*value_column*__  
A coluna que fornece os valores dos objetos JSON.

__*grouping_column*__  
A coluna do `GROUP BY`; ela não é um argumento de `JSON_GROUP_OBJECT`, que recebe exatamente dois.  A função retorna um objeto JSON por grupo.

## Valor(es) retornado(s) {#return-values}
Retorna um objeto JSON que contém todos os pares chave-valor agregados das colunas especificadas, agrupados de acordo com a `grouping_column`.

* * *

## Exemplos {#examples}

### Agregar tamanhos de repositórios por linguagem de programação {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

Esta consulta retorna um objeto JSON para cada linguagem de programação, em que cada objeto contém os nomes dos repositórios e os tamanhos correspondentes como pares chave-valor. Veja uma amostra da saída esperada:

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

Essa saída é útil para visualizar a distribuição dos tamanhos dos repositórios entre as diferentes linguagens de programação de uma organização, com dados claros e organizados, agrupados por linguagem.
