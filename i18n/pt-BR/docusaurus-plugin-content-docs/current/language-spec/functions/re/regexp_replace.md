---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função REGEXP_REPLACE no StackQL: substituir as partes de uma string que correspondem a uma expressão regular por uma string de substituição."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Substitui uma substring correspondente pela string de substituição.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Referência de expressões regulares ]](/language-spec/functions/re/reference) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Argumentos {#arguments}

__*source*__  
Uma string literal ou coluna de string na qual as correspondências da expressão regular `pattern` são substituídas por `replacement`.  

__*pattern*__  
O padrão de expressão regular a ser procurado.  

__*replacement*__  
A string de substituição.

> A string de substituição é inserida exatamente como foi escrita: referências a grupos de captura, como `$1`, não são expandidas.  

## Valor(es) retornado(s) {#return-values}
Retorna uma string.

* * *

## Exemplos {#examples}

### Substituir um padrão de expressão regular em uma coluna {#replace-a-regular-expression-pattern-in-a-column}

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

Para obter mais informações, consulte [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
