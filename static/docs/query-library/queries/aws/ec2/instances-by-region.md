---
title: EC2 instances in a region
description: Lists EC2 instances in one region with type, state, addressing and network placement.
format: md
verb: select
status: draft
providers: [aws]
services: [ec2, ec2_native]
tags: [aws, ec2, compute, inventory]
keywords: [ec2 inventory, instance list, running instances]
intent_keywords:
  - list ec2 instances
  - what instances are running
  - ec2 instance inventory
auth: [AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY]
params:
  - name: region
    type: identifier
    required: true
    description: Region to list instances in
    example: us-east-1
outputs:
  - name: instanceId
    type: string
    description: Instance identifier
  - name: instanceType
    type: string
    description: Instance type (e.g. t3.micro)
  - name: instanceState
    type: object
    description: State object; the name key holds running, stopped, etc.
  - name: privateIpAddress
    type: string
    description: Primary private IPv4 address
  - name: ipAddress
    type: string
    description: Public IPv4 address, empty when none
  - name: launchTime
    type: string
    description: Launch timestamp
  - name: vpcId
    type: string
    description: VPC the instance is in
  - name: subnetId
    type: string
    description: Subnet the instance is in
cost:
  fan_out: region
  expensive: false
  notes: One describe call per region when swept account-wide
related: [aws/ec2/regions-enabled, aws/ec2/instance-stop]
---

Lists every EC2 instance in a single region with the fields that answer most
inventory asks: type, state, private and public addressing, and network
placement. For an account-wide inventory, run aws/ec2/regions-enabled first and
fan this query out over the enabled regions.

## Query

```sql
SELECT instanceId, instanceType, instanceState, privateIpAddress, ipAddress, launchTime, vpcId, subnetId FROM aws.ec2_native.instances WHERE region = '{{region}}';
```

## Notes

instanceState is an object; filter on its name key (running, stopped,
terminated) client-side or with JSON extraction. Terminated instances remain
visible for about an hour after termination. ipAddress is empty for instances
with no public IP.
