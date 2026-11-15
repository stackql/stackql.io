---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "UPDATE in StackQL: change specified fields of a resource instance, equivalent to an HTTP PATCH, with the target identified by the WHERE clause."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Updates specified fields in a resource (synonymous with a `patch` operation).

See also:  
[[ StackQL Resource Hierarchy ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Syntax {#syntax}

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

## Examples {#examples}

### Basic `UPDATE` statement {#basic-update-statement}
Run an `UPDATE` statement to set the labels on a Google Cloud Storage bucket.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### `UPDATE` statement (blocking) {#update-statement-blocking}
Run an `UPDATE` statement to modify a Compute Engine firewall rule. This is a blocking (synchronous) invocation.

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

### `UPDATE` statement with JSON functions {#update-statement-with-json-functions}
Run an `UPDATE` statement whose value is built with JSON functions. A function that reads no columns is evaluated before the request is sent. A `SET` value cannot read the row being updated, so to change part of an existing value, read it with a `SELECT` first and set the modified value.

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
