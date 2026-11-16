---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função com valor de tabela JSON_EACH no StackQL: expandir um array ou objeto JSON em uma linha por elemento ou membro, para fazer join e filtrar respostas de API aninhadas."
image: "/img/stackql-featured-image.png"
---
Função com valor de tabela que retorna uma tabela com uma linha para cada elemento do array ou membro do objeto.

Veja também:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

:::tip 

Use a instrução [**DESCRIBE**](/language-spec/describe) para localizar os tipos de dados `array` que podem ser usados com a função `json_each`.

:::

:::note 

A função `json_each` é implementada como a função `json_array_elements_text` se você usa o stackql com um backend SQL `postgres`

:::

## Sintaxe {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## Argumentos {#arguments}

__*json_array_or_object*__  
Um array ou objeto JSON.

__*json_path*__  
O caminho JSON para um array ou objeto contido em um *json_array_or_object*.  Consulte [jsonpath.com](https://jsonpath.com/).

> Se *json_path* não existir em *json_array_or_object*, a função não retorna nenhuma linha.

> A função `json_each()` gera um erro se *json_array_or_object* não for um JSON bem formado.

## Valor(es) retornado(s) {#return-values}
Uma tabela com uma linha para cada elemento do array ou membro do objeto.

* * *

## Exemplos {#examples}

### Expandir um array de strings em uma tabela combinada com os campos associados {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

para contexto:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

retorna...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

A consulta `stackql` a seguir usa a função `json_each` para expandir o array `members` em uma tabela combinada com os campos `role` e, opcionalmente, `condition`.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

retornaria...

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

Para obter mais informações, consulte [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
