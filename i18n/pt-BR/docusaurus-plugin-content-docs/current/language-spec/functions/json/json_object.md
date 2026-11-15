---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função JSON_OBJECT no StackQL: criar um objeto JSON a partir de argumentos de chave e valor, por exemplo para montar corpos de solicitação para INSERT e UPDATE."
image: "/img/stackql-featured-image.png"
---
Retorna um objeto construído a partir dos argumentos informados.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

:::tip 

Use a instrução [**DESCRIBE**](/language-spec/describe) para localizar os tipos de dados `array` ou `object` que podem ser usados com as funções JSON do StackQL.

:::

:::note 

Se você usa o stackql com um backend SQL `postgres`, use `jsonb_build_object` em vez de `json_object`: o StackQL repassa `json_object` ao PostgreSQL sem alteração, e a função do PostgreSQL com esse nome recebe as chaves e os valores como arrays de texto.

:::

## Sintaxe {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*key1, key2, ...*__  
Os rótulos do objeto JSON.

__*value1, value2, ...*__  
Os valores correspondentes aos rótulos do objeto JSON.

## Valor(es) retornado(s) {#return-values}
Uma string que representa o objeto JSON bem formado.

* * *

## Exemplos {#examples}

### Criar um array de objetos JSON a partir de uma coluna {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

Para obter mais informações, consulte [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
