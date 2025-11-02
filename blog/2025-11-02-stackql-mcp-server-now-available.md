---
slug: stackql-mcp-server-now-available
title: StackQL MCP Server Now Available
hide_table_of_contents: false
authors:
  - kieranrimmer
image: "/img/blog/stackql-mcp-server-featured-image.png"
description: Enable AI agents to query and manage cloud infrastructure using the Model Context Protocol.
keywords: [stackql, mcp, model context protocol, ai agents, claude, chatgpt, infrastructure-as-code, cloud management, anthropic]
tags: [stackql, mcp, model context protocol, ai agents, claude, infrastructure-as-code]
---

__StackQL__ now supports the [__Model Context Protocol (MCP)__](https://modelcontextprotocol.io/). This integration enables AI agents and assistants to query and manage cloud infrastructure across multiple providers using natural language.

## What is the Model Context Protocol?

The Model Context Protocol is an open standard that enables AI applications to securely connect to external data sources and tools. By running StackQL as an MCP server, AI agents like Claude, ChatGPT, and other LLM-based assistants can interact with your cloud infrastructure using StackQL's powerful SQL-based query capabilities.

## Why MCP + StackQL?

Combining MCP with StackQL creates a powerful interface for AI-assisted infrastructure management:

- **Natural Language Infrastructure Queries**: Ask questions about your cloud resources in plain English and get structured data back
- **Multi-Cloud Support**: Access resources across AWS, Google Cloud, Azure, and 100+ other providers through a single interface
- **Secure and Standardized**: MCP provides a secure, standardized way for AI agents to interact with your infrastructure
- **SQL-Powered Analytics**: Leverage StackQL's full SQL capabilities including joins, aggregations, and complex queries through AI agents

## Deployment Options

StackQL's MCP server supports three flexible deployment modes to suit different architectural requirements:

### 1. Standalone MCP Server

Perfect for development and AI agent integration:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}'
```

### 2. Dual-Protocol Server (In-Memory)

Run both MCP and PostgreSQL wire protocol simultaneously with high-performance in-memory communication:

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}' \
  --pgsrv.port 5665
```

This mode is ideal when you need both AI agent access and traditional database client connectivity.

### 3. Reverse Proxy with TLS

For production environments requiring distributed deployments and encrypted connections:

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/cert.pem", "tls_key_file": "/path/to/key.pem", "transport": "http", "address": "127.0.0.1:9004"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

## Available MCP Tools

When running as an MCP server, StackQL exposes several tools that AI agents can invoke:

| Tool | Description |
|------|-------------|
| `greet` | Test connectivity with the MCP server |
| `list_providers` | List all available StackQL providers |
| `list_services` | List services for a specific provider |
| `list_resources` | List resources within a provider service |
| `list_methods` | List available methods for a resource |
| `query_v2` | Execute StackQL queries |

## Integration with Claude Desktop

To integrate StackQL with Claude Desktop, add this configuration to your MCP settings file (`~/Library/Application Support/Claude/claude_desktop_config.json` on macOS):

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=http",
        "--mcp.config",
        "{\"server\": {\"transport\": \"http\", \"address\": \"127.0.0.1:9912\"}}"
      ]
    }
  }
}
```

## Example Use Cases

Once configured, you can ask your AI assistant questions like:

- "Show me all my EC2 instances across all AWS regions"
- "List all Google Cloud Storage buckets with public access"
- "Find all Azure virtual machines that haven't been updated in 30 days"
- "Compare compute costs across AWS, Azure, and GCP"
- "Show me IAM policies that grant admin access in my Google Cloud projects"

The AI agent will use StackQL's MCP server to execute the appropriate queries and return structured results.

## Example Query Flow

Here's how an AI agent interacts with StackQL via MCP:

```bash
# AI agent lists available providers
Tool: list_providers
Response: ["google", "aws", "azure", "github", ...]

# AI agent explores a provider's services
Tool: list_services
Args: {"provider": "google"}
Response: ["compute", "storage", "cloudresourcemanager", ...]

# AI agent executes a query
Tool: query_v2
Args: {"sql": "SELECT name, status FROM google.compute.instances WHERE project = 'my-project' AND zone = 'us-east1-a'"}
Response: [{"name": "instance-1", "status": "RUNNING"}, ...]
```

## Getting Started

1. **Download StackQL** version 0.9.250 or later from [stackql.io/install](/install)

2. **Set up provider authentication**:
```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/credentials.json)
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
```

3. **Start the MCP server**:
```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}'
```

4. **Configure your AI assistant** to use the StackQL MCP server (see [MCP documentation](/docs/command-line-usage/mcp) for details)

## Documentation

For comprehensive documentation on configuring and using the MCP server, including:
- Detailed configuration options
- TLS/mTLS setup
- Architecture considerations
- Testing and troubleshooting

Visit the [MCP command documentation](/docs/command-line-usage/mcp).

## What's Next?

We're actively developing additional MCP capabilities and welcome your feedback. Future enhancements may include:

- Enhanced resource provisioning and lifecycle management through MCP
- Built-in prompt templates for common infrastructure queries
- Extended tool catalog for specialized operations
- Support for additional MCP transport protocols

## Try It Out!

The MCP server feature is available now in StackQL 0.9.250. We'd love to hear about your experiences integrating StackQL with AI agents. Share your use cases, provide feedback, or contribute to the project on [GitHub](https://github.com/stackql/stackql).

⭐ Star us on [GitHub](https://github.com/stackql/stackql) and join our community!
