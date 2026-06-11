---
title: How to use StackQL with Claude
description: Register StackQL as an MCP server in Claude Desktop or Claude Code so Claude can query and manage cloud infrastructure across AWS, Azure, Google Cloud, GitHub, and other providers using natural language.
keywords: [stackql, claude, claude desktop, claude code, mcp, anthropic, cloud infrastructure]
proficiencyLevel: Beginner
faq:
  - question: Does Claude need to know StackQL syntax?
    answer: No special training is required. Claude discovers the provider schema at runtime through the MCP discovery tools (list_providers, list_methods, describe_resource) and writes standard SQL, which it already does fluently. You interact in natural language; Claude translates to StackQL queries.
  - question: Will Claude ask before changing my infrastructure?
    answer: Yes, under the default configuration. The StackQL MCP server defaults to safe mode, and Claude Desktop advertises the MCP elicitation capability, so every INSERT, UPDATE, DELETE, or lifecycle operation triggers an approval prompt showing the SQL before it runs. Set mode to read_only to prevent mutations entirely.
  - question: Where do my cloud credentials go?
    answer: In the env block of the stackql entry in the MCP configuration file, or in the environment the server process inherits. Credentials stay on your machine with the StackQL binary; they are not sent to the model.
---

# How to use StackQL with Claude

Claude connects to StackQL through the Model Context Protocol: register `stackql` as an MCP server in Claude Desktop (or Claude Code), and Claude can inventory, audit, and - with your approval - modify cloud resources across every provider StackQL supports, driven by natural language.

## Steps

1. **Install StackQL** and verify it is on your PATH:

```bash
stackql --version
```

2. **Edit the Claude Desktop configuration file**:

- Windows: `%APPDATA%\Claude\claude_desktop_config.json`
- macOS: `~/Library/Application Support/Claude/claude_desktop_config.json`
- Linux: `~/.config/Claude/claude_desktop_config.json`

3. **Add the server entry**, including credentials for the providers you use:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--mcp.config",
        "{\"server\": {\"transport\": \"stdio\", \"mode\": \"safe\"}}"
      ],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "STACKQL_GITHUB_USERNAME": "your-github-username",
        "STACKQL_GITHUB_PASSWORD": "your-github-personal-access-token"
      }
    }
  }
}
```

Include only the providers you need (`AZURE_TENANT_ID`/`AZURE_CLIENT_ID`/`AZURE_CLIENT_SECRET` for Azure, `GOOGLE_CREDENTIALS` for Google Cloud, `DATABRICKS_CLIENT_ID`/`DATABRICKS_CLIENT_SECRET` for Databricks). Never commit this file to version control.

4. **Restart Claude Desktop.** The server starts automatically when a conversation begins.

5. **Ask in natural language.** Examples that map directly to StackQL operations:

- "List all EC2 instances in us-east-1 with public IP addresses"
- "Which repositories in our GitHub org have branch protection disabled?"
- "Create an S3 bucket named analytics-data in us-west-2"

For reads, Claude discovers the schema and runs `SELECT` statements immediately. For the bucket creation, `safe` mode triggers an approval prompt showing the exact `INSERT` before anything executes.

## Controlling what Claude can do

The `mode` field in the config is the contract: `read_only` for inventory-only assistants, `safe` (default) for approval-gated changes, `delete_safe` to allow create/update but gate deletes, `full_access` only for trusted automation. Every call Claude makes is recorded in the server's JSONL audit log regardless of mode.

## Related concepts

- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - the general MCP setup, including HTTP transport
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - modes, elicitation, and audit in detail
- [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws) - per-provider credential reference
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the broader pattern
- [StackQL vs Custom MCP Servers](/ai/comparisons/stackql-vs-custom-mcp-servers) - why one generic server beats many bespoke ones
