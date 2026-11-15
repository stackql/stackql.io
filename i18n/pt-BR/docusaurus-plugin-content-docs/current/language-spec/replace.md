---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "REPLACE no StackQL: substituir todos os campos de uma instância de recurso, o equivalente a um PUT HTTP, ao contrário da atualização parcial feita por UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Atualiza todos os campos de um recurso (equivalente a uma operação `put`).

Veja também:  
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Sintaxe {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Exemplos {#examples}

### Instrução `REPLACE` básica {#basic-replace-statement}
Execute uma instrução `REPLACE` para substituir todos os tópicos de um repositório do GitHub.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### Instrução `REPLACE` (bloqueante) {#replace-statement-blocking}
Execute uma instrução `REPLACE` para substituir uma regra de firewall do Compute Engine. Esta é uma invocação bloqueante (síncrona).

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Instrução `REPLACE` com funções JSON {#replace-statement-with-json-functions}
Execute uma instrução `REPLACE` cujo valor é construído com funções JSON. Uma função que não lê nenhuma coluna é avaliada antes do envio da solicitação. Um valor de `SET` não pode ler a linha que está sendo substituída; por isso, para alterar parte de um valor existente, leia-o primeiro com um `SELECT` e defina o valor modificado.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
