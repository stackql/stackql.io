---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función REGEXP_LIKE en StackQL: comprobar si una cadena coincide con una expresión regular y devolver 1 para true y 0 para false, para filtros de patrones en cláusulas WHERE."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Comprueba si la cadena de origen coincide con el patrón y devuelve `1` para true o `0` para false.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Referencia de expresiones regulares ]](/language-spec/functions/re/reference) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*source*__  
Una cadena literal o una columna de cadena en la que desea comprobar la existencia de un patrón definido por una expresión regular (`pattern`).  

__*pattern*__  
El patrón de expresión regular que se busca.  

## Valor(es) devuelto(s) {#return-values}
Devuelve `1` para true (la cadena de origen coincide con el patrón) o `0` para false.  

* * *

## Ejemplos {#examples}

### Determinar si una cadena contiene un patrón {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, '^vm[0-9]+') as name_starts_with_vm 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------------|
| name | name_starts_with_vm |
|------|---------------------|
| vm0  |                   1 |
|------|---------------------|
| vm1  |                   1 |
|------|---------------------|
| test |                   0 |
|------|---------------------|
```

</TabItem>
</Tabs>

Para obtener más información, consulte [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
