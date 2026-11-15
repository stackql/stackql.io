---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función SPLIT_PART en StackQL: dividir una cadena por un separador y devolver la parte de una posición (a partir de 1), útil para descomponer ID de recursos y ARN."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Divide la cadena de origen según un separador y devuelve la parte indicada (contando desde uno).  

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*source*__  
Una cadena literal o una columna de cadena que desea dividir.

__*sep*__  
Uno o varios caracteres que desea usar como separador.  

> Si `sep` se compone de varios caracteres, cada carácter se trata como separador.  

> Solo se admiten símbolos ASCII (de 1 byte) como separadores.  

__*part*__  
El elemento (contado desde uno) de la cadena dividida que se devuelve.  Los valores negativos se usan para contar hacia atrás desde el último elemento.  

## Valor(es) devuelto(s) {#return-values}
Devuelve una cadena.

* * *

## Ejemplos {#examples}

### Extraer elementos de una cadena separados por una barra diagonal {#extract-elements-from-a-string-seperated-by-a-forward-slash}


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

### Extraer el último elemento de una URL {#extract-the-last-element-from-a-url}


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

Para obtener más información, consulte [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)
