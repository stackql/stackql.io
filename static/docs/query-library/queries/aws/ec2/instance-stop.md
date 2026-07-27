---
title: Stop an EC2 instance
description: Stops one EC2 instance by instance id via the native stop lifecycle operation.
format: md
verb: lifecycle
status: draft
providers: [aws]
services: [ec2]
tags: [aws, ec2, compute, lifecycle]
keywords: [stop instance, shut down vm, power off]
intent_keywords:
  - stop an ec2 instance
  - shut down an instance
  - power off ec2
auth: [AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY]
permissions: ["ec2:StopInstances"]
params:
  - name: region
    type: identifier
    required: true
    description: Region the instance is in
    example: us-east-1
  - name: instance_id
    type: string
    required: true
    pattern: "^i-[0-9a-f]{8,17}$"
    description: EC2 instance identifier
    example: i-0abcd1234ef567890
outputs:
  - name: currentState
    type: object
    description: State the instance is transitioning to (stopping)
  - name: previousState
    type: object
    description: State the instance was in before the call
cost:
  fan_out: none
  expensive: false
related: [aws/ec2/instances-by-region]
---

Stops a running EC2 instance. This is a lifecycle operation (EXEC), not a
mutation of resource configuration: it transitions instance state and returns
the state change. The instance retains its EBS volumes and can be started
again; instance-store data is lost on stop.

## Query

```sql
EXEC aws.ec2.instances.stop @InstanceId = '{{instance_id}}', @region = '{{region}}';
```

## Notes

The call is asynchronous: it returns when the instance enters the stopping
state, not when it is stopped. Poll aws/ec2/instances-by-region until
instanceState.name is stopped. The matching start operation is
aws.ec2.instances.start with the same arguments.
