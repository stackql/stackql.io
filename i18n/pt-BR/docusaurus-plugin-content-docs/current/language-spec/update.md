---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "UPDATE no StackQL: alterar campos especificados de uma instância de recurso, o equivalente a um HTTP PATCH, com o destino identificado pela cláusula WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Atualiza campos especificados em um recurso (sinônimo de uma operação `patch`).

Veja também:  
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Sintaxe {#syntax}

*updateStatement::=*

<RailroadDiagram 
type="update"
/>

&nbsp;  
&nbsp;

```sql
UPDATE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Exemplos {#examples}

### Instrução `UPDATE` básica {#basic-update-statement}
Execute uma instrução `UPDATE` para definir os rótulos de um bucket do Google Cloud Storage.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### Instrução `UPDATE` (bloqueante) {#update-statement-blocking}
Execute uma instrução `UPDATE` para modificar uma regra de firewall do Compute Engine. Esta é uma invocação bloqueante (síncrona).

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Instrução `UPDATE` com funções JSON {#update-statement-with-json-functions}
Execute uma instrução `UPDATE` cujo valor é construído com funções JSON. Uma função que não lê nenhuma coluna é avaliada antes do envio da solicitação. Um valor de `SET` não pode ler a linha que está sendo atualizada; por isso, para alterar parte de um valor existente, leia-o primeiro com um `SELECT` e defina o valor modificado.

```sql
-- Set the protocols and ports a firewall rule allows
UPDATE google.compute.firewalls
SET data__allowed = json_array(
  json_object('IPProtocol', 'tcp', 'ports', json_array('22', '443')),
  json_object('IPProtocol', 'icmp')
)
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```
