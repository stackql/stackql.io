---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função JSON_ARRAY_LENGTH no StackQL: o número de elementos de um array JSON, no nível superior ou em um caminho dentro de um valor JSON."
image: "/img/stackql-featured-image.png"
---
Retorna o número de elementos de um array JSON.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

:::tip 

Use a instrução [**DESCRIBE**](/language-spec/describe) para localizar os tipos de dados `array` ou `object` que podem ser usados com as funções JSON do StackQL. Use a função [**JSON_TYPE**](/language-spec/functions/json/json_type) para resolver os tipos de dados de objetos aninhados ou de elementos de arrays.

:::

## Sintaxe {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_array_or_object*__  
Um array ou objeto JSON.

> Se *json_array_or_object* for um objeto JSON (e não um array) e *json_path* não for especificado, a função retorna `0`.

__*json_path*__  
O caminho JSON opcional para um array contido em um objeto.  Consulte [jsonpath.com](https://jsonpath.com/).

> Se *json_path* for informado, a função `json_array_length` localiza o caminho dentro de *json_array_or_object* e retorna o comprimento do array, ou `0` se *json_path* for um objeto (e não um array).  Se *json_path* não puder ser localizado dentro de *json_array_or_object*, a função retorna `NULL`.

> Se *json_array_or_object* ou *json_path* não forem objetos JSON ou não forem bem formados, a função gera um erro.

## Valor(es) retornado(s) {#return-values}
Retorna um inteiro.

* * *

## Exemplos {#examples}

### Retornar o número de discos anexados a instâncias do Google Compute Engine {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar o número de interfaces de rede anexadas a instâncias do Google Compute Engine {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar o número de ACLs associadas a buckets do Google Storage {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Retornar o número de escopos associados a uma conta de serviço {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
