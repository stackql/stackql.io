---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função SPLIT_PART no StackQL: dividir uma string por um separador e retornar a parte em uma posição contada a partir de 1, útil para decompor IDs de recursos e ARNs."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Divide a string de origem com base em um separador e retorna a parte indicada (contando a partir de um).  

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*source*__  
Uma string literal ou coluna de string que você deseja dividir.

__*sep*__  
Um ou mais caracteres que você deseja usar como separador.  

> Se `sep` for composto de vários caracteres, a string inteira é o separador: `SPLIT_PART('a||b||c', '||', 3)` retorna `c`.  

> Um separador pode conter quaisquer caracteres, inclusive caracteres não ASCII, como `€`.  

__*part*__  
O elemento (contado a partir de 1) da string dividida a ser retornado.  Valores negativos contam de trás para frente a partir do último elemento.  

## Valor(es) retornado(s) {#return-values}
Retorna uma string.

* * *

## Exemplos {#examples}

### Extrair elementos de uma string separada por barras {#extract-elements-from-a-string-separated-by-a-forward-slash}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Resultados', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT name,  
 split_part(id, '/', 3) as subscription,
 split_part(id, '/', 5) as resource_group,
 json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines 
 WHERE resource_group_name = 'stackql-ops-cicd-dev-01' 
 AND subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------------------------------|-------------------------|-----------------|
| name |             subscription             |     resource_group      |     vm_size     |
|------|--------------------------------------|-------------------------|-----------------|
| test | 273769f6-545f-45b2-8ab8-2f14ec5768dc | stackql-ops-cicd-dev-01 | Standard_D2s_v3 |
|------|--------------------------------------|-------------------------|-----------------|
```

</TabItem>
</Tabs>

### Extrair o último elemento de uma URL {#extract-the-last-element-from-a-url}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Resultados', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT
name,
SPLIT_PART(network, '/', -1) as network,
SPLIT_PART(region, '/', -1) as region
FROM google.compute.vpn_gateways WHERE project = 'stackql-demo';
```

</TabItem>
<TabItem value="results">

```
+------------------------+-----------------------+----------------------+
| name                   | network               | region               |
+------------------------+-----------------------+----------------------+
| gcp-aws-vpn-gateway    | stackql-shrd-vpc-nw   | australia-southeast1 |
+------------------------+-----------------------+----------------------+
```

</TabItem>
</Tabs>

Para obter mais informações, consulte [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)
