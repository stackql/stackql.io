---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función REGEXP_REPLACE en StackQL: reemplazar las partes de una cadena que coinciden con una expresión regular por una cadena de reemplazo."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Reemplaza una subcadena coincidente por la cadena de reemplazo.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Referencia de expresiones regulares ]](/language-spec/functions/re/reference) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Argumentos {#arguments}

__*source*__  
Una cadena literal o una columna de cadena de la que desea extraer una subcadena según un patrón definido por una expresión regular (`pattern`).  

__*pattern*__  
El patrón de expresión regular que se busca.  

__*replacement*__  
La cadena de reemplazo.

> Admite referencias inversas a los grupos capturados `\1` a `\9` en la cadena de reemplazo.  

## Valor(es) devuelto(s) {#return-values}
Devuelve una cadena.

* * *

## Ejemplos {#examples}

### Reemplazar un patrón de expresión regular en una columna {#replace-a-regular-expression-pattern-in-a-column}

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
regexp_replace(json_extract(properties, '$.timeCreated'), 'T.*', '') as date_created
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------|
| name | date_created |
|------|--------------|
| vm0  | 2022-09-14   |
|------|--------------|
| vm1  | 2022-09-14   |
|------|--------------|
| vm2  | 2022-09-14   |
|------|--------------|
```

</TabItem>
</Tabs>

Para obtener más información, consulte [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
