---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "REPLACE in StackQL: replace every field of a resource instance, equivalent to an HTTP PUT, as opposed to the partial update performed by UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Updates all fields in a resource (synonymous with a `put` operation).

See also:  
[[ StackQL Resource Hierarchy ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Syntax {#syntax}

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

## Examples {#examples}

### Basic `REPLACE` statement {#basic-replace-statement}
Run a `REPLACE` statement to replace all of the topics on a GitHub repository.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### `REPLACE` statement (blocking) {#replace-statement-blocking}
Run a `REPLACE` statement to replace a Compute Engine firewall rule. This is a blocking (synchronous) invocation.

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

### `REPLACE` statement with JSON functions {#replace-statement-with-json-functions}
Run a `REPLACE` statement whose value is built with JSON functions. A function that reads no columns is evaluated before the request is sent. A `SET` value cannot read the row being replaced, so to change part of an existing value, read it with a `SELECT` first and set the modified value.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```