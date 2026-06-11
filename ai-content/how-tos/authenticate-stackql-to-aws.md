---
title: How to authenticate StackQL to AWS
description: StackQL authenticates to AWS using the standard AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY environment variables, with session tokens supported for assumed roles and AWS CloudShell.
keywords: [stackql, aws authentication, aws access key, environment variables, cloudshell, iam]
proficiencyLevel: Beginner
faq:
  - question: What IAM permissions does StackQL need for AWS?
    answer: Only the permissions for the operations you run. For read-only inventory and audit queries, the AWS-managed ReadOnlyAccess policy (or a narrower custom policy) is sufficient. INSERT, UPDATE, DELETE, and EXEC operations require the corresponding write permissions on the target services.
  - question: Does StackQL support temporary AWS credentials?
    answer: Yes. Credentials issued by sts assume-role or provided automatically in AWS CloudShell work by setting the standard environment variables, including the session token, before starting StackQL.
  - question: Can I authenticate to AWS and other providers in the same session?
    answer: Yes. Each provider reads its own environment variables, so exporting AWS, GitHub, and Azure credentials together enables cross-provider queries and joins in a single shell or server session.
---

# How to authenticate StackQL to AWS

StackQL authenticates to AWS using the standard AWS credential environment variables. Set them before starting a StackQL shell, `exec` command, server, or MCP server; no configuration file is required.

## Steps

1. Create or identify an IAM principal (user or role) with the permissions your queries need. For read-only querying, the `ReadOnlyAccess` managed policy or a narrower equivalent is sufficient.

2. Export the credentials as environment variables:

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

For temporary credentials (an assumed role via `sts assume-role`, or an AWS CloudShell session, which sets these automatically), the session token is exported as well:

```bash
export AWS_SESSION_TOKEN=YOURSESSIONTOKEN
```

3. Pull the AWS provider (first use only):

```sql
REGISTRY PULL aws;
```

4. Verify with a query:

```sql
SELECT instance_id, instance_type
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

A successful result set confirms authentication. Note that AWS queries require a `region` predicate in the `WHERE` clause - this is a routing parameter, not a credential setting.

## Where the variables apply

The same environment variables work in every StackQL execution mode:

- **Interactive shell**: `stackql shell`
- **Batch**: `stackql exec "..."`
- **Server mode**: `stackql srv` (PostgreSQL wire protocol)
- **MCP server**: `stackql mcp`, or via the `env` block of an MCP client configuration such as `claude_desktop_config.json`

In CI systems, source the variables from the platform's secret store rather than hardcoding them. Metadata operations (`SHOW`, `DESCRIBE`, `REGISTRY`) work without credentials; only data operations (`SELECT`, `INSERT`, `UPDATE`, `DELETE`, `EXEC`) require an authenticated session.

## Related concepts

- [How to query AWS EC2 instances with StackQL](/ai/how-tos/query-aws-ec2-instances-with-stackql) - first queries after authenticating
- [How to query S3 buckets with StackQL](/ai/how-tos/query-s3-buckets-with-stackql) - the S3 query pattern
- [How to authenticate StackQL to Azure](/ai/how-tos/authenticate-stackql-to-azure) - the Azure equivalent
- [How to authenticate StackQL to Google Cloud](/ai/how-tos/authenticate-stackql-to-google-cloud) - the GCP equivalent
- [Common StackQL errors](/ai/troubleshooting/common-stackql-errors) - including authentication failures
