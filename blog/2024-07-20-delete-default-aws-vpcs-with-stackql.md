---
slug: delete-default-aws-vpcs-with-stackql
title: Delete Default AWS VPCs with StackQL
hide_table_of_contents: false
authors:	
  - kieranrimmer
image: "/img/blog/stackql-aws-provider-featured-image.png"
description: Use StackQL to delete default AWS VPCs across all regions to improve your cloud security posture.
keywords: [aws, amazon web services, stackql, cloud security]
tags: [aws, amazon web services, stackql, cloud security]
---

AWS creates default VPCs in each region for convenience. However, these default VPCs often contain noncompliant network ACLs and security group rules that do not align with best practices for AWS Config and Security Hub. Deleting these default VPCs is beneficial, especially for regions not used by your organization or architectures that do not utilize a VPC.  

This guide demonstrates how to use StackQL to enumerate and delete all default VPCs and their associated resources in all AWS regions.  

## What you need

All you need to do is to install the [`pystackql`](https://pypi.org/project/pystackql/) package using:

```bash
pip install pystackql
```

Deleting resources will require a privileged AWS IAM user. You can do this from a terminal with the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` environment variables set (and optionally `AWS_SESSION_TOKEN` set if you are using `sts assume-role`).  

You can also use the [StackQL Cloud Shell Scripts](https://stackql.io/blog/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-aws-cloud-shell), from within AWS Cloud Shell, to run authenticated StackQL queries using:

```bash
sh stackql-aws-cloud-shell.sh
```

## How it works

Default VPCs in AWS regions have a CIDR block of __`172.31.0.0/16`__, before deleting anything, the program ensures it is not in use by using:

```sql
SELECT
id
FROM aws.ec2.network_interfaces
WHERE region = '{region}'
AND vpc_id = '{vpc_id}'
```

If any resources are using the VPC (such as EC2, RDS, ELB/ALB, VPC attached Lambda functions, etc), it will skip these VPCs.  Once determined that the VPC is not in use, all of the resources associated with the VPC must be deleted before the VPC itself can be deleted; this includes:

- External Routes (aws.ec2.routes)
- Internet Gateways (aws.ec2.internet_gateways)
- NACLs (aws.ec2.network_acls)
- Subnets (aws.ec2.subnets)
- and then finally, the VPC (aws.ec2.vpcs)

> The default route table and default security group are deleted automatically when deleting the VPC

This is done by discovering the resources using StackQL `SELECT` queries and deleting them using StackQL `DELETE` queries, like:

```
DELETE FROM aws.ec2.network_acls
WHERE data__Identifier = '{nacl_id}'
AND region = '{region}'
```

## Complete code

The following Python program uses StackQL to list and delete default VPCs and their associated resources (subnets, route tables, internet gateways, security groups, and network ACLs) across all AWS regions.

<details>

<summary>Get the complete code here</summary>

```python
from pystackql import StackQL
stackql = StackQL()
stackql.executeStmt("REGISTRY PULL aws")

def ensure_one_or_zero(resource_list, resource_name, region):
    if len(resource_list) > 1:
        raise RuntimeError(f"ah snap! multiple default {resource_name} resources found in {region}")
    elif len(resource_list) == 0:
        print(f"/* no default {resource_name} found in {region} */\n")
        return False
    return True

regions = [
    'us-east-1', 'us-east-2', 'us-west-1', 'us-west-2',
    'eu-central-1', 'eu-central-2', 'eu-west-1', 'eu-west-2', 'eu-west-3', 'eu-north-1', 'eu-south-1',
    'ap-east-1', 'ap-south-1', 'ap-south-2', 'ap-northeast-1', 'ap-northeast-2', 'ap-northeast-3',
    'ap-southeast-1', 'ap-southeast-2', 'ap-southeast-3', 'ap-southeast-4', 'af-south-1',
    'ca-central-1',  'me-south-1', 'me-central-1', 'sa-east-1'
]

for region in regions:
    vpc_ids = stackql.execute(
        f"""
        SELECT
        vpc_id
        FROM aws.ec2.vpcs
        WHERE region = '{region}'
        AND cidr_block = '172.31.0.0/16'
        AND JSON_ARRAY_LENGTH(tags) = 0
        """
    )
    if not ensure_one_or_zero(vpc_ids, 'VPC', region): continue 
    vpc_id = vpc_ids[0]['vpc_id']
    
    # check if vpc is in use
    network_interfaces = stackql.execute(
        f"""
        SELECT
        id
        FROM aws.ec2.network_interfaces
        WHERE region = '{region}'
        AND vpc_id = '{vpc_id}'
        """
    )
    if len(network_interfaces) > 0:
        print(f"/* skipping deletion of default VPC ({vpc_id}) in {region} because it is in use */\n")
        continue
        
    print(f"/* deleting resources for default VPC ({vpc_id}) in {region} */\n")

    # get route table
    route_table_ids = stackql.execute(
        f"""
        SELECT
        route_table_id
        FROM aws.ec2.route_tables
        WHERE region = '{region}'
        AND vpc_id = '{vpc_id}'
        """
    )
    ensure_one_or_zero(route_table_ids, 'route table', region)
    route_table_id = route_table_ids[0]['route_table_id']

    # get inet gateway id
    inet_gateway_ids = stackql.execute(
        f"""
        SELECT gateway_id 
        FROM aws.ec2.routes 
        WHERE data__Identifier = '{route_table_id}|0.0.0.0/0' 
        AND region = '{region}'
        """
    )
    ensure_one_or_zero(inet_gateway_ids, 'internet gateway', region)
    inet_gateway_id = inet_gateway_ids[0]['gateway_id']

    # delete routes
    print(f"/* deleting default VPC routes in route table ({route_table_id}) in {region} */")
    print(f"""
DELETE FROM aws.ec2.routes
WHERE data__Identifier = '{route_table_id}|0.0.0.0/0'
AND region = '{region}';
    """)

    # detatch inet gateway
    print(f"/* detaching default VPC internet gateway ({inet_gateway_id}) in {region} */")
    print(f"""
DELETE FROM aws.ec2.vpc_gateway_attachments
WHERE data__Identifier = 'IGW|{vpc_id}'
AND region = '{region}';
    """)

    # delete inet gateway
    print(f"/* deleting default VPC internet gateway ({inet_gateway_id}) in {region} */")
    print(f"""
DELETE FROM aws.ec2.internet_gateways
WHERE data__Identifier = '{inet_gateway_id}'
AND region = '{region}';
    """)

    # delete nacl
    nacl_ids = stackql.execute(
        f"""
        SELECT
        id
        FROM aws.ec2.network_acls
        WHERE vpc_id = '{vpc_id}'
        AND region = '{region}'
        """
    )
    ensure_one_or_zero(nacl_ids, 'network acl', region)
    nacl_id = nacl_ids[0]['id']
    print(f"/* deleting default VPC NACL ({nacl_id}) in {region} */")
    print(f"""
DELETE FROM aws.ec2.network_acls
WHERE data__Identifier = '{nacl_id}'
AND region = '{region}';
    """)

    # delete subnets
    subnet_ids = stackql.execute(
        f"""
        SELECT
        subnet_id
        FROM aws.ec2.subnets
        WHERE vpc_id = '{vpc_id}'
        AND region = '{region}'
        """
    )
    for subnet_id in subnet_ids:
        print(f"/* deleting default VPC subnet ({subnet_id['subnet_id']}) in {region} */")
        print(f"""
DELETE FROM aws.ec2.subnets
WHERE data__Identifier = '{subnet_id['subnet_id']}'
AND region = '{region}';
        """)

    # delete vpc
    print(f"/* deleting default VPC ({vpc_id}) in {region} */")
    print(f"""
DELETE FROM aws.ec2.vpcs
WHERE data__Identifier = '{vpc_id}'
AND region = '{region}';
    """)
```
</details>

run the program using:

```bash
python3 delete-all-default-vpcs.py > delete_default_vpcs.iql
```
to generate a StackQL script you can run as a batch using `stackql exec` or as individual statements in the `stackql shell`.

## Conclusion

Deleting default VPCs can help improve your AWS security posture by removing potentially noncompliant network configurations. This program leverages StackQL to automate the process, ensuring consistency across all regions.

Let us know what you think! ⭐ us on [__GitHub__](https://github.com/stackql/stackql).
