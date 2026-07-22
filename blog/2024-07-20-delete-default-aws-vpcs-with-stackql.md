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

This example uses the [__`awscc`__](https://awscc-provider.stackql.io) (AWS Cloud Control) provider. Cloud Control resources are read in two steps: a `_list_only` view returns the resource identifiers in a region, and the resource view returns the full properties for a single resource selected by its `Identifier`.

Default VPCs in AWS regions have a CIDR block of __`172.31.0.0/16`__, before deleting anything, the program ensures it is not in use by listing the network interfaces in the region and checking whether any belong to the VPC:

```sql
SELECT id
FROM awscc.ec2.network_interfaces_list_only
WHERE region = '{region}'
```

If any resources are using the VPC (such as EC2, RDS, ELB/ALB, VPC attached Lambda functions, etc), it will skip these VPCs.  Once determined that the VPC is not in use, all of the resources associated with the VPC must be deleted before the VPC itself can be deleted; this includes:

- External Routes (awscc.ec2.routes)
- Internet Gateways (awscc.ec2.internet_gateways)
- NACLs (awscc.ec2.network_acls)
- Subnets (awscc.ec2.subnets)
- and then finally, the VPC (awscc.ec2.vpcs)

> The default route table and default security group are deleted automatically when deleting the VPC

This is done by discovering the resources using StackQL `SELECT` queries and deleting them using StackQL `DELETE` queries, like:

```
DELETE FROM awscc.ec2.network_acls
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
stackql.executeStmt("REGISTRY PULL awscc")

def ensure_one_or_zero(resource_list, resource_name, region):
    if len(resource_list) > 1:
        raise RuntimeError(f"ah snap! multiple default {resource_name} resources found in {region}")
    elif len(resource_list) == 0:
        print(f"/* no default {resource_name} found in {region} */\n")
        return False
    return True

def get_resource(region, resource, identifier):
    # fetch the full properties of a single Cloud Control resource by its identifier
    rows = stackql.execute(f"""
        SELECT *
        FROM awscc.ec2.{resource}
        WHERE region = '{region}'
        AND Identifier = '{identifier}'
    """)
    return rows[0] if len(rows) else None

def find_in_vpc(region, resource, id_col, vpc_id):
    # list all identifiers for a resource in a region, then keep those attached to the target VPC
    matches = []
    for row in stackql.execute(f"SELECT {id_col} FROM awscc.ec2.{resource}_list_only WHERE region = '{region}'"):
        detail = get_resource(region, resource, row[id_col])
        if detail and detail.get('vpc_id') == vpc_id:
            matches.append(detail)
    return matches

regions = [
    'us-east-1', 'us-east-2', 'us-west-1', 'us-west-2',
    'eu-central-1', 'eu-central-2', 'eu-west-1', 'eu-west-2', 'eu-west-3', 'eu-north-1', 'eu-south-1',
    'ap-east-1', 'ap-south-1', 'ap-south-2', 'ap-northeast-1', 'ap-northeast-2', 'ap-northeast-3',
    'ap-southeast-1', 'ap-southeast-2', 'ap-southeast-3', 'ap-southeast-4', 'af-south-1',
    'ca-central-1',  'me-south-1', 'me-central-1', 'sa-east-1'
]

for region in regions:
    # find the default VPC (CIDR 172.31.0.0/16, untagged) by listing VPCs then inspecting each
    default_vpcs = []
    for row in stackql.execute(f"SELECT vpc_id FROM awscc.ec2.vpcs_list_only WHERE region = '{region}'"):
        vpc = get_resource(region, 'vpcs', row['vpc_id'])
        if vpc and vpc.get('cidr_block') == '172.31.0.0/16' and not vpc.get('tags'):
            default_vpcs.append(vpc)
    if not ensure_one_or_zero(default_vpcs, 'VPC', region): continue
    vpc_id = default_vpcs[0]['vpc_id']

    # check if the VPC is in use
    network_interfaces = find_in_vpc(region, 'network_interfaces', 'id', vpc_id)
    if len(network_interfaces) > 0:
        print(f"/* skipping deletion of default VPC ({vpc_id}) in {region} because it is in use */\n")
        continue

    print(f"/* deleting resources for default VPC ({vpc_id}) in {region} */\n")

    # get the default route table for the VPC
    route_tables = find_in_vpc(region, 'route_tables', 'route_table_id', vpc_id)
    ensure_one_or_zero(route_tables, 'route table', region)
    route_table_id = route_tables[0]['route_table_id']

    # get the internet gateway from the default (0.0.0.0/0) route
    default_route = get_resource(region, 'routes', f'{route_table_id}|0.0.0.0/0')
    inet_gateway_id = default_route['gateway_id'] if default_route else None

    # delete the default route
    print(f"/* deleting default VPC routes in route table ({route_table_id}) in {region} */")
    print(f"""
DELETE FROM awscc.ec2.routes
WHERE data__Identifier = '{route_table_id}|0.0.0.0/0'
AND region = '{region}';
    """)

    # detach the internet gateway
    print(f"/* detaching default VPC internet gateway ({inet_gateway_id}) in {region} */")
    print(f"""
DELETE FROM awscc.ec2.vpc_gateway_attachments
WHERE data__Identifier = 'IGW|{vpc_id}'
AND region = '{region}';
    """)

    # delete the internet gateway
    print(f"/* deleting default VPC internet gateway ({inet_gateway_id}) in {region} */")
    print(f"""
DELETE FROM awscc.ec2.internet_gateways
WHERE data__Identifier = '{inet_gateway_id}'
AND region = '{region}';
    """)

    # delete the network ACL
    nacls = find_in_vpc(region, 'network_acls', 'id', vpc_id)
    ensure_one_or_zero(nacls, 'network acl', region)
    nacl_id = nacls[0]['id']
    print(f"/* deleting default VPC NACL ({nacl_id}) in {region} */")
    print(f"""
DELETE FROM awscc.ec2.network_acls
WHERE data__Identifier = '{nacl_id}'
AND region = '{region}';
    """)

    # delete the subnets
    subnets = find_in_vpc(region, 'subnets', 'subnet_id', vpc_id)
    for subnet in subnets:
        print(f"/* deleting default VPC subnet ({subnet['subnet_id']}) in {region} */")
        print(f"""
DELETE FROM awscc.ec2.subnets
WHERE data__Identifier = '{subnet['subnet_id']}'
AND region = '{region}';
        """)

    # delete the VPC
    print(f"/* deleting default VPC ({vpc_id}) in {region} */")
    print(f"""
DELETE FROM awscc.ec2.vpcs
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
