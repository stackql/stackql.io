---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "DESCRIBE no StackQL: listar os campos de um recurso, ou os parâmetros e o tipo de retorno de um método específico, antes de consultá-lo ou modificá-lo."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Descreve um recurso ou um método específico de um recurso.

Veja também:
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy)

* * *

## Sintaxe {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

`DESC` é aceito como alias de `DESCRIBE` ao descrever um recurso; `DESCRIBE METHOD` exige a palavra-chave completa.

## Resposta {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Coluna | Descrição |
| --- | --- |
| `name` | Nome do campo |
| `type` | Tipo de dados (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Descrição do campo (se `EXTENDED` for informado) |

</TabItem>
<TabItem value="describe-method">

| Coluna | Descrição |
| --- | --- |
| `name` | Nome do campo |
| `type` | Tipo de dados (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional` ou `output` |
| `shape` | Subconjunto do JSON Schema para campos de objeto e de array; vazio para escalares. Inclui `properties`, `items`, `required`, `enum`, `default` e `description` aninhados e, opcionalmente, booleanos, se o provider os disponibilizar (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Descrição do campo (se `EXTENDED` for informado) |

</TabItem>
</Tabs>

* * *

## Exemplos {#examples}

### Instrução `DESCRIBE` básica {#basic-describe-statement}
Execute uma instrução `DESCRIBE` básica para listar os campos de um recurso a partir de uma sessão autenticada.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Instrução `DESCRIBE` estendida {#extended-describe-statement}
Execute uma instrução `DESCRIBE` estendida para listar os campos de um recurso e suas descrições a partir de uma sessão autenticada.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### Alias `DESC` {#desc-alias}
`DESC` é a forma abreviada de `DESCRIBE` para um recurso, com ou sem `EXTENDED`. Ele é tratado como uma instrução somente leitura em todos os lugares em que `DESCRIBE` é, incluindo a ferramenta `run_select_query` do servidor MCP nos modos `read_only` e `safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### Instrução `DESCRIBE METHOD` básica {#basic-describe-method-statement}
Inspecione um único método de um recurso. O resultado inclui os parâmetros de entrada e os campos da resposta, com a estrutura aninhada representada na coluna `shape`.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Instrução `DESCRIBE METHOD` estendida {#extended-describe-method-statement}
Execute uma instrução `DESCRIBE METHOD` estendida para incluir as descrições de cada campo junto com o formato.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Descobrir o payload de um `INSERT` {#discovering-an-insert-payload}
`DESCRIBE METHOD` é a forma canônica de descobrir quais campos um `INSERT` (ou qualquer método de mutação) aceita. A coluna `shape` traz a estrutura aninhada que agentes e pessoas podem usar para montar um payload em uma única consulta.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
