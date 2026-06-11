---
title: How to authenticate StackQL to Google Cloud
description: StackQL authenticates to Google Cloud using a service account key supplied through the GOOGLE_CREDENTIALS environment variable, which holds the JSON key content.
keywords: [stackql, google cloud authentication, gcp, service account, google credentials]
proficiencyLevel: Beginner
faq:
  - question: Does GOOGLE_CREDENTIALS take a file path or the key content?
    answer: The key content. The documented pattern is export GOOGLE_CREDENTIALS=$(cat /path/to/key.json), which places the JSON document itself in the variable. Alternatively, the --auth flag can reference the key by file path using the credentialsfilepath setting.
  - question: What IAM role does the service account need?
    answer: The role implied by your queries. roles/viewer at project scope covers read-only inventory and audit queries; mutations such as INSERT and DELETE require the corresponding editor or service-specific admin roles.
  - question: Why do Google queries require a project (and often zone) in the WHERE clause?
    answer: Google Cloud APIs are scoped by project, and zonal services by zone. These are required parameters of the API call. For google.compute.instances, the aggregated_list method requires only project, while list requires project and zone - run SHOW METHODS IN google.compute.instances to see both.
---

# How to authenticate StackQL to Google Cloud

StackQL authenticates to Google Cloud with a service account key, supplied as JSON content in the `GOOGLE_CREDENTIALS` environment variable.

## Steps

1. Create a service account and grant it a role on the target project (`roles/viewer` suffices for read-only querying), then create and download a JSON key:

```bash
gcloud iam service-accounts keys create stackql-key.json \
  --iam-account=stackql-sa@my-project.iam.gserviceaccount.com
```

2. Export the key content:

```bash
export GOOGLE_CREDENTIALS=$(cat ./stackql-key.json)
```

An alternative is passing the key by path with the `--auth` flag, which is useful for server and MCP deployments:

```bash
stackql shell \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/stackql-key.json"}}'
```

3. Pull the Google provider (first use only):

```sql
REGISTRY PULL google;
```

4. Verify with a query:

```sql
SELECT name, status, machineType
FROM google.compute.instances
WHERE project = 'my-project';
```

This uses the `aggregated_list` access method, which requires only `project` and returns instances across all zones. The zonal `list` method requires both `project` and `zone`; `SHOW METHODS IN google.compute.instances` shows each method's required parameters.

## Where the credentials apply

The same variable (or `--auth` object) works across `stackql shell`, `stackql exec`, `stackql srv`, and `stackql mcp`. For Claude Desktop and similar MCP clients, set `GOOGLE_CREDENTIALS` in the `env` block of the server configuration. In CI, source the key from the platform's secret store - never commit it.

## Related concepts

- [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws) - the AWS equivalent
- [How to authenticate StackQL to Azure](/ai/how-tos/authenticate-stackql-to-azure) - the Azure equivalent
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - credentials in MCP deployments
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - why required parameters surface in WHERE clauses
- [Common StackQL errors](/ai/troubleshooting/common-stackql-errors) - including authentication failures
