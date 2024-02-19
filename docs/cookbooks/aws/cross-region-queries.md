---
title: Cross Region Queries
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
  - cspm
  - aws
  - amazon web services
description: Query and Deploy AWS Cloud Infrastructure and Resources using SQL
image: "/img/cookbooks/aws/stackql-aws-provider-featured-image.png"
---

# Asynchronous Cross Region Queries with StackQL

In this guide, we will demonstrate a powerful capability within StackQL: the ability to query and aggregate resources across multiple regions in AWS.  This involves numerous API requests to different endpoints, which are performed asynchronously.  This is seamless and transparent to the user.  

> StackQL is a powerful dev tool that enables querying and deploying cloud infrastructure and resources using SQL syntax.  

Tested with <span class="cookbook_tested_on">embedded sql backend</span> <span class="cookbook_tested_on">postgres sql backend</span> <span class="cookbook_tested_on">macos</span> <span class="cookbook_tested_on">linux</span> <span class="cookbook_tested_on">powershell</span>

## Understanding AWS Regions and Resources

AWS resources are hosted in multiple locations worldwide.  These locations are composed of Regions and Availability Zones, designed to allow users to place instances and store data in various locations.

## The problem with AWS Regions...

When working with AWS, it's crucial to understand that the AWS Management Console provides a regionally scoped view of resources, allowing users to interact with and manage resources within a specific region at a time.  Similarly, AWS Command Line Interface (CLI) commands and Software Development Kits (SDKs) use the region as an entry point for operations, necessitating the specification of a region to execute commands or run programs.  

> The regional framework of cloud services introduces complexities when resources span multiple regions.  Additionally, security breaches, including cryptocurrency mining exploits, frequently utilize resources in various areas, extending beyond the primary region typically used by organizations.

## Specifying Regions in StackQL Queries

Using StackQL, you can specify regions to target resources across different geographic locations.  This is particularly useful for managing resources that are distributed across the globe.

### Example: Listing EC2 Instances Across Multiple Regions

```sql
SELECT instanceId, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

This query returns a list of EC2 instances from the `us-east-1` (N. Virginia) and `eu-west-1` (Ireland) regions, showcasing how StackQL can fetch data across regions in a single query.  This could be used with aggregate queries as well, such as:  

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## The Power of Asynchronous Queries

StackQL executes these cross-region queries asynchronously, making multiple parallel API requests to AWS.  This ensures efficient data retrieval without waiting for one region's request to complete before moving on to the next.

## Conclusion

Asynchronous cross-region queries in StackQL provide a robust and efficient method for managing AWS resources across different geographical locations.  By leveraging SQL-like syntax for these queries, StackQL simplifies complex cloud management tasks, making it an invaluable tool for developers and cloud administrators aiming to optimize their AWS infrastructure.