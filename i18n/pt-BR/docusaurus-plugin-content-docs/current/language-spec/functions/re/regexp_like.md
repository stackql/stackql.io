---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função REGEXP_LIKE no StackQL: testar se uma string corresponde a uma expressão regular e retornar true ou false, para filtros de padrão em cláusulas WHERE."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Verifica se a string de origem corresponde ao padrão e retorna `true` ou `false`.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Referência de expressões regulares ]](/language-spec/functions/re/reference) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*source*__  
Uma string literal ou coluna de string na qual você deseja testar a existência de um padrão definido por uma expressão regular (`pattern`).  

__*pattern*__  
O padrão de expressão regular a ser procurado.  

## Valor(es) retornado(s) {#return-values}
Retorna `0` se `false` e `1` se `true`.  

* * *

## Exemplos {#examples}

### Determinar se uma string contém um padrão {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, 'vm[0-9]+') as name_starts_with_vm 
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

Para obter mais informações, consulte [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
