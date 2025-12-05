---
title: Using StackQL with Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ai
  - infrastructure-as-code
  - multi-cloud
description: Use StackQL with Claude Desktop to query and provision cloud resources using natural language
image: "/img/stackql-featured-image.png"
---

StackQL integrates with Claude Desktop via the Model Context Protocol (MCP), enabling you to query and provision cloud resources across multiple cloud providers using natural language. This powerful combination allows you to interact with your cloud infrastructure conversationally while leveraging StackQL's SQL-based interface under the hood.

## Prerequisites

Before setting up StackQL with Claude Desktop, ensure you have:

1. **StackQL installed** - Download and install StackQL from [stackql.io](https://stackql.io/downloads) or use a package manager
2. **Claude Desktop** - Download from [Anthropic's website](https://claude.ai/desktop)
3. **Cloud provider credentials** - API keys or credentials for the cloud providers you want to work with

Verify StackQL is in your system PATH by running:

```bash
stackql --version
```

## Configuration

### Setting Up the MCP Server

To enable Claude Desktop to communicate with StackQL, you need to configure the MCP server in your Claude Desktop configuration file.

#### Windows

Edit the configuration file located at:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS

Edit the configuration file located at:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux

Edit the configuration file located at:
```
~/.config/Claude/claude_desktop_config.json
```

### Configuration Example

Add the following configuration to your `claude_desktop_config.json` file:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
You only need to include environment variables for the cloud providers you plan to use. Remove any unused provider credentials from the configuration.
:::

:::warning
Never commit your `claude_desktop_config.json` file with actual credentials to version control. Use secure credential management practices.
:::

### Provider-Specific Credentials

Different cloud providers require different authentication methods. Below are examples for common providers:

#### Google Cloud Platform (GCP)

```json
"GOOGLE_CREDENTIALS": "/path/to/service-account-key.json"
```

Or use Application Default Credentials:
```json
"GOOGLE_APPLICATION_CREDENTIALS": "/path/to/service-account-key.json"
```

#### Amazon Web Services (AWS)

```json
"AWS_ACCESS_KEY_ID": "your-access-key-id",
"AWS_SECRET_ACCESS_KEY": "your-secret-access-key",
"AWS_REGION": "us-east-1"
```

#### Microsoft Azure

```json
"AZURE_CLIENT_ID": "your-client-id",
"AZURE_CLIENT_SECRET": "your-client-secret",
"AZURE_TENANT_ID": "your-tenant-id",
"AZURE_SUBSCRIPTION_ID": "your-subscription-id"
```

#### Databricks

```json
"DATABRICKS_CLIENT_ID": "your-client-id",
"DATABRICKS_CLIENT_SECRET": "your-client-secret"
```

For more information on provider authentication, see the [StackQL Provider Registry](/providers).

## Restarting Claude Desktop

After updating the configuration file, restart Claude Desktop to load the new MCP server configuration. The StackQL MCP server will be automatically started when you begin a new conversation.

## What You Can Do

Once configured, you can use natural language in Claude Desktop to:

### Query Cloud Resources

Ask Claude to retrieve information about your cloud infrastructure:

- "Show me all my GCP compute instances in the us-central1 region"
- "List all S3 buckets in my AWS account"
- "What Azure virtual machines are running in my subscription?"
- "Show me all Kubernetes pods in the default namespace"

### Provision Resources

Create and deploy cloud resources using conversational commands:

- "Create an S3 bucket named 'my-data-bucket' in us-west-2"
- "Deploy a GCP Cloud Storage bucket with versioning enabled"
- "Launch a small EC2 instance in us-east-1"
- "Create an Azure resource group in westus2"

### Analyze and Audit

Perform security audits and compliance checks:

- "Show me all publicly accessible S3 buckets"
- "List IAM users who haven't logged in for 90 days"
- "Find all GCP instances without proper labels"
- "Identify Azure VMs without backup enabled"

### Cross-Cloud Operations

Query and compare resources across multiple cloud providers:

- "Show me all compute instances across AWS, GCP, and Azure"
- "Compare storage costs between AWS S3 and GCP Cloud Storage"
- "List all databases across all cloud providers"

### Infrastructure as Code

Generate and execute infrastructure deployment queries:

- "Create a stack of resources including a VPC, subnet, and EC2 instance"
- "Deploy a complete GKE cluster with node pools"
- "Set up a multi-region deployment in AWS"

### Modify Resources

Update existing cloud resources:

- "Add tags to all untagged EC2 instances"
- "Enable encryption for S3 bucket 'my-bucket'"
- "Update the machine type of GCP instance 'web-server-1'"
- "Change the ACL on my Cloud Storage bucket to private"

### Delete Resources

Remove cloud resources when no longer needed:

- "Delete the S3 bucket 'temp-bucket'"
- "Remove all stopped EC2 instances"
- "Clean up unused GCP disks"

## Example Conversation

Here's an example of how you might interact with Claude Desktop using StackQL:

**You:** "Can you show me all my GCP compute instances?"

**Claude:** *Executes a StackQL query and returns results in a formatted table showing instance names, zones, machine types, and status*

**You:** "Create a new Cloud Storage bucket named 'analytics-data-2025' in the us-central1 region with uniform bucket-level access"

**Claude:** *Executes a StackQL INSERT statement to create the bucket and confirms the creation*

**You:** "Now list all my Cloud Storage buckets"

**Claude:** *Executes a SELECT query and shows all buckets including the newly created one*

## Tips and Best Practices

1. **Be Specific**: The more specific your request, the better results you'll get. Include resource names, regions, and specific attributes when possible.

2. **Use Natural Language**: You don't need to know SQL syntax - Claude translates your natural language requests into StackQL queries.

3. **Review Before Executing**: For destructive operations (DELETE, UPDATE), Claude will typically show you what will be executed before proceeding.

4. **Multi-Step Operations**: You can chain operations together in a single conversation. Claude maintains context throughout the session.

5. **Error Handling**: If a query fails, Claude can help troubleshoot and suggest corrections based on the error message.

6. **Explore Capabilities**: Ask Claude "What can I do with StackQL?" to get suggestions specific to your configured providers.

## Troubleshooting

### MCP Server Not Starting

If Claude Desktop cannot connect to the StackQL MCP server:

1. Verify StackQL is in your PATH
2. Check that your `claude_desktop_config.json` file is valid JSON
3. Ensure the file path in the configuration is correct for your operating system
4. Review the Claude Desktop logs for error messages

### Authentication Errors

If you receive authentication errors:

1. Verify your cloud provider credentials are correct
2. Ensure credential files exist at the specified paths
3. Check that environment variables are properly formatted in the configuration
4. Confirm your credentials have the necessary permissions

### Permission Errors

If operations fail due to insufficient permissions:

1. Verify your service account or IAM user has the required permissions
2. Check provider-specific IAM policies
3. Ensure API services are enabled in your cloud projects

## Learn More

- [StackQL MCP Command Reference](/docs/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Language Specification](/docs/language-spec/select)
- [Model Context Protocol Documentation](https://modelcontextprotocol.io)

## Security Considerations

:::caution
- Store credentials securely and never commit them to version control
- Use least-privilege access principles for service accounts
- Regularly rotate credentials and API keys
- Monitor MCP server logs for unusual activity
- Consider using cloud provider credential management tools (e.g., Secret Manager, Parameter Store)
:::
