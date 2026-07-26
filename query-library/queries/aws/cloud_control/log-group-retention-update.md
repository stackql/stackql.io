---
title: Set CloudWatch log group retention
description: Updates RetentionInDays on a log group via Cloud Control JSON Patch semantics.
format: md
verb: mutation
status: stable
providers: [aws]
services: [cloud_control, logs]
tags: [aws, logs, cloud_control, mutation]
keywords: [log retention, patch document, cloud control update]
intent_keywords:
  - set log group retention
  - change cloudwatch retention days
auth: [AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY]
params:
  - name: region
    type: identifier
    required: true
    description: Routing region
    example: ap-southeast-1
  - name: log_group_name
    type: string
    required: true
    description: Log group name (the data__Identifier key)
    example: my-log-group
  - name: retention_days
    type: number
    required: true
    description: Retention period in days
    example: 180
outputs:
  - name: OperationStatus
    type: string
    description: Async progress event status
  - name: RequestToken
    type: string
    description: Token to poll aws.cloud_control.resource_requests
cost:
  fan_out: none
  expensive: false
---

Sets the retention period on a CloudWatch log group through the generic Cloud
Control resource, using JSON Patch semantics against the resource's current
state. This is the canonical example of a Cloud Control UPDATE in StackQL:
the same pattern works for any AWS::* resource type that supports update.

## Query

```sql
UPDATE aws.cloud_control.resources SET data__PatchDocument = string('[{"op":"replace","path":"/RetentionInDays","value":{{retention_days}}}]') WHERE region = '{{region}}' AND data__TypeName = 'AWS::Logs::LogGroup' AND data__Identifier = '{{log_group_name}}';
```

## Notes

Cloud Control mutations are asynchronous: the response is a progress event.
Check completion via aws.cloud_control.resource_requests or by re-reading the
resource. The PatchDocument is an RFC 6902 JSON Patch array against current
state, wrapped in string(...).
