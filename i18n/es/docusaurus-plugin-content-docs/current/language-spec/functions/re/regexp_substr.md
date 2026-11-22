---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función REGEXP_SUBSTR en StackQL: extraer la subcadena de una entrada que coincide con una expresión regular."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Devuelve una subcadena de una cadena de entrada que coincide con un patrón especificado.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Referencia de expresiones regulares ]](/language-spec/functions/re/reference) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*source*__  
Una cadena literal o una columna de cadena de la que desea extraer una subcadena según un patrón definido por una expresión regular (`pattern`).  

__*pattern*__  
El patrón de expresión regular que se busca.  

## Valor(es) devuelto(s) {#return-values}
Devuelve una cadena.

* * *

## Ejemplos {#examples}

### Extraer una subcadena según una expresión regular {#extract-a-substring-based-upon-a-regular-expression}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Resultados', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
select name,
json_extract(properties, '$.storageProfile.imageReference.offer') as os,
regexp_substr(
  json_extract(properties, '$.storageProfile.imageReference.exactVersion'), 
  '[0-9][0-9]\\.[0-9][0-9]') as os_version 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------|------------|
| name |      os       | os_version |
|------|---------------|------------|
| vm0  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm1  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm2  | UbuntuServer  |      20.04 |
|------|---------------|------------|
```

</TabItem>
</Tabs>

Para obtener más información, consulte [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
