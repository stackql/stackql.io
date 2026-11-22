---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função JSON_EXTRACT no StackQL: extrair um ou mais valores de um objeto ou array JSON por caminho, a principal ferramenta para achatar campos aninhados de respostas de API."
image: "/img/stackql-featured-image.png"
---
Retorna um ou mais valores de um objeto ou array JSON bem formado.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

:::tip 

Use a instrução [**DESCRIBE**](/language-spec/describe) para localizar os tipos de dados `array` ou `object` que podem ser usados com as funções JSON do StackQL. Use a função [**JSON_TYPE**](/language-spec/functions/json/json_type) para resolver os tipos de dados de objetos aninhados ou de elementos de arrays.

:::

:::note 

A função `json_extract` é implementada como a função `json_extract_path_text` se você usa o stackql com um backend SQL `postgres`.  O caminho é dividido em cada `.` e um `$` inicial é descartado, então `'$.a.b'` se torna as chaves `'a'` e `'b'`; um índice de array como `$[0]` é repassado como chave de objeto e não seleciona um elemento do array nesse backend.

:::


## Sintaxe {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_array_or_object*__  
Um array ou objeto JSON.

__*json_path*__  
O caminho JSON para um array ou objeto contido em um *json_array_or_object*.  Consulte [jsonpath.com](https://jsonpath.com/).

> Se um único *json_path* for informado, a função retorna um resultado escalar que corresponde ao tipo de dados do caminho resolvido.

> Se vários argumentos *json_path* forem informados, o resultado será um array composto pelos objetos resolvidos pelos respectivos caminhos.

> Especificar `'$'` no argumento *json_path* retorna o objeto JSON inteiro correspondente à coluna representada pelo argumento *json_array_or_object*.

> NOTA: você pode usar ferramentas como [`json-viewer`](https://aarrange.com/json-viewer) para desenvolver e testar expressões JSON Path

## Valor(es) retornado(s) {#return-values}
Uma string, um inteiro, um objeto, um array, um booleano ou null, dependendo do *json_array_or_object* e do *json_path* informados como entrada.

* * *

## Exemplos {#examples}

### Retornar os objetos de disco associados a cada instância do Google Compute Engine {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar o nome do dispositivo e o tamanho do primeiro disco anexado a cada instância do Google Compute Engine {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar o nível de rede da primeira NIC anexada a cada instância do Google Compute Engine {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar uma lista de escopos associados à primeira conta de serviço associada a cada instância do Google Compute Engine {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

Para obter mais informações, consulte [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
