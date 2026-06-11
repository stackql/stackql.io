---
title: How to query AWS EC2 instances with StackQL
description: Query EC2 instances as the SQL table aws.ec2.instances - filter by region, project attributes such as instance type and IP addresses, and aggregate across regions with GROUP BY.
keywords: [stackql, aws, ec2, query instances, sql, cloud inventory, multi-region]
proficiencyLevel: Beginner
faq:
  - question: Why is region required when querying EC2 instances?
    answer: The EC2 DescribeInstances API is regional, so region is a required parameter of the underlying call. StackQL surfaces required API parameters as required WHERE clause predicates. Supplying region IN (...) with multiple regions fans out as parallel per-region API calls aggregated into one result set.
  - question: What columns are available on aws.ec2.instances?
    answer: Columns include instance_id, instance_type, image_id, launch_time, availability_zone, private_ip_address, public_ip_address, private_dns_name, public_dns_name, subnet_id, vpc_id, key_name, plus structured columns such as state, security_groups, network_interfaces, and tag_set. Run DESCRIBE aws.ec2.instances for the full list.
  - question: Can I stop, start, or terminate instances with StackQL?
    answer: Yes. Terminate maps to DELETE, and lifecycle actions (start, stop, reboot, monitor) are EXEC methods on the same resource. SHOW METHODS IN aws.ec2.instances lists each operation, its SQL verb, and its required parameters.
---

# How to query AWS EC2 instances with StackQL

EC2 instances are exposed as the table `aws.ec2.instances`. Any field returned by the EC2 `DescribeInstances` API is a queryable column, and `region` is a required `WHERE` predicate because the API is regional.

## Prerequisites

- AWS credentials exported as environment variables - see [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws)
- The AWS provider installed: `REGISTRY PULL aws;`

## Basic query

List instances in one region with their type and addressing:

```sql
SELECT
  instance_id,
  instance_type,
  private_ip_address,
  public_ip_address,
  launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

## Discover the schema first

Two metadata commands make the resource self-describing - no AWS documentation required:

```sql
DESCRIBE aws.ec2.instances;
SHOW METHODS IN aws.ec2.instances;
```

`DESCRIBE` lists the available columns. `SHOW METHODS` lists the operations (the `describe` SELECT method, the `terminate` DELETE method, and EXEC lifecycle methods such as `start`, `stop`, and `reboot`) with the required parameters for each. These commands work without credentials.

## Multi-region aggregation

A `region IN (...)` predicate runs one API call per region in parallel and aggregates the results - a regional service queried like a global table:

```sql
SELECT region, instance_type, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1','ap-southeast-2')
GROUP BY region, instance_type;
```

This pattern is the basis of account-wide inventory and drift baselines: the result is the live answer, not a snapshot.

## Filtering on attributes

Standard SQL predicates apply to any column. For example, instances exposed to the internet:

```sql
SELECT instance_id, instance_type, public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1'
AND public_ip_address IS NOT NULL;
```

Structured columns such as `state` and `tag_set` return JSON, which can be unpacked with the JSON functions of the configured SQL backend.

## Related concepts

- [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws) - credential setup
- [How to query S3 buckets with StackQL](/ai/how-tos/query-s3-buckets-with-stackql) - the S3 pattern, which differs slightly
- [What is Infrastructure Drift?](/ai/canonical-definitions/what-is-infrastructure-drift) - using these queries as baselines
- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the model behind the table abstraction
- [Getting started with StackQL](/ai/tutorials/getting-started-with-stackql) - end-to-end setup
