---
title: How to authenticate StackQL to Azure
description: StackQL authenticates to Azure using a Microsoft Entra ID service principal via the AZURE_TENANT_ID, AZURE_CLIENT_ID, and AZURE_CLIENT_SECRET environment variables.
keywords: [stackql, azure authentication, service principal, entra id, environment variables]
proficiencyLevel: Beginner
faq:
  - question: What Azure role does StackQL need?
    answer: The role required by your queries, scoped as narrowly as practical. The built-in Reader role at subscription scope is sufficient for inventory and audit queries; mutations require Contributor or a custom role on the target resource types.
  - question: Why do Azure queries need a subscriptionId in the WHERE clause?
    answer: Azure Resource Manager APIs are scoped by subscription (and often resource group), so these appear as required parameters of the underlying API call, not as authentication settings. SHOW METHODS IN azure.compute.virtual_machines lists the required parameters per method.
  - question: Does StackQL work in Azure Cloud Shell?
    answer: Yes. StackQL can run in Azure Cloud Shell like any Linux binary; supply service principal credentials via the environment variables described on this page.
---

# How to authenticate StackQL to Azure

StackQL authenticates to Azure with a Microsoft Entra ID (formerly Azure AD) service principal, supplied through three environment variables: `AZURE_TENANT_ID`, `AZURE_CLIENT_ID`, and `AZURE_CLIENT_SECRET`.

## Steps

1. Create a service principal and grant it a role on the target subscription (the `Reader` role suffices for read-only querying):

```bash
az ad sp create-for-rbac --name stackql-sp --role Reader \
  --scopes /subscriptions/<subscription-id>
```

2. Export the credentials it returns:

```bash
export AZURE_TENANT_ID=your-tenant-id
export AZURE_CLIENT_ID=your-client-id
export AZURE_CLIENT_SECRET=your-client-secret
```

3. Pull the Azure provider (first use only):

```sql
REGISTRY PULL azure;
```

4. Verify with a query:

```sql
SELECT name, location
FROM azure.compute.virtual_machines
WHERE subscriptionId = '00000000-1111-2222-3333-444444444444';
```

The `subscriptionId` predicate is a required routing parameter of the Azure API, not part of authentication. Methods scoped to a resource group additionally require `resourceGroupName`; run `SHOW METHODS IN azure.compute.virtual_machines` to see the required parameters for each access method (`list_all` needs only `subscriptionId`; `list` needs `resourceGroupName` as well; `get` needs `vmName`).

## Where the variables apply

The variables work identically across `stackql shell`, `stackql exec`, `stackql srv` (PostgreSQL wire server), and `stackql mcp`. For MCP clients such as Claude Desktop, place them in the `env` block of the server entry in `claude_desktop_config.json`. In CI, source them from the pipeline's secret store.

## Related concepts

- [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws) - the AWS equivalent
- [How to authenticate StackQL to Google Cloud](/ai/how-tos/authenticate-stackql-to-google-cloud) - the GCP equivalent
- [How to use StackQL with Claude](/ai/how-tos/use-stackql-with-claude) - passing Azure credentials to an MCP session
- [Control Plane vs Data Plane](/ai/canonical-definitions/control-plane-vs-data-plane) - what the Resource Manager APIs cover
- [Common StackQL errors](/ai/troubleshooting/common-stackql-errors) - including authentication failures
