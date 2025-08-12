---
slug: anthropic-provider-for-stackql-available
title: Anthropic Provider for StackQL Available
hide_table_of_contents: true
authors:	
  - jeffreyaven
image: "/img/blog/stackql-provider-for-anthropic-released.png"
description: Query and interact with Anthropic Claude using SQL.
keywords: [stackql, anthropic, claude, llms, genai, iac, analytics]
tags: [stackql, anthropic, claude, llms, genai, iac, analytics]
---

The __`anthropic`__ provider for [__`stackql`__](https://github.com/stackql/stackql) is now available in the dev stackql provider registry. The `anthropic` provider for `stackql` includes services for interacting with Claude models via the Messages API. To get started download [`stackql`](/install), set the `ANTHROPIC_API_KEY` environment variable and use the dev registry as shown here:  

```bash
export DEV_REG="{ \"url\": \"https://registry-dev.stackql.app/providers\" }"
./stackql --registry="${DEV_REG}" shell
```

Then pull the `anthropic` provider using:

```sql
REGISTRY PULL anthropic;
```

Now you can run some queries. Here's a simple example using the high-level claude_35_chat interface:

```shell
stackql  >>select * from anthropic.messages.claude_35_chat;
|----------------------------|-----------|-------------|---------------|--------------|---------------|--------------------------------|
|           model            |   role    | stop_reason | stop_sequence | input_tokens | output_tokens |            content             |
|----------------------------|-----------|-------------|---------------|--------------|---------------|--------------------------------|
| claude-3-5-sonnet-20240620 | assistant | end_turn    | null          |           13 |            39 | StackQL is a SQL-like query    |
|                            |           |             |               |              |               | language and universal API     |
|                            |           |             |               |              |               | client that allows users to    |
|                            |           |             |               |              |               | query, analyze, and manage     |
|                            |           |             |               |              |               | cloud infrastructure and       |
|                            |           |             |               |              |               | services across multiple       |
|                            |           |             |               |              |               | providers using familiar SQL   |
|                            |           |             |               |              |               | syntax.                        |
|----------------------------|-----------|-------------|---------------|--------------|---------------|--------------------------------|
```

Or you can use the lower-level messages interface directly:

```shell
stackql  >>select * from anthropic.messages.message
stackql  >>where "anthropic-version" = '2023-06-01'
stackql  >>and data__model = 'claude-3-5-sonnet-20240620'
stackql  >>and data__max_tokens = 1024
stackql  >>and data__messages = '[{"role": "user", "content": "Hello, world"}]';
|--------------------------------|------------------------------|----------------------------|-----------|-------------|---------------|---------|----------------------------------------|
|            content             |              id              |           model            |   role    | stop_reason | stop_sequence |  type   |                 usage                  |     
|--------------------------------|------------------------------|----------------------------|-----------|-------------|---------------|---------|----------------------------------------|     
| [{"text":"Hello! How can I     | msg_01MLTLVY6XCTT2cNBeFeJzfj | claude-3-5-sonnet-20240620 | assistant | end_turn    | null          | message | {"input_tokens":10,"output_tokens":30} |     
| assist you today? Feel free    |                              |                            |           |             |               |         |                                        |     
| to ask me any questions or let |                              |                            |           |             |               |         |                                        |     
| me know if you need help with  |                              |                            |           |             |               |         |                                        |     
| anything.","type":"text"}]     |                              |                            |           |             |               |         |                                        |     
|--------------------------------|------------------------------|----------------------------|-----------|-------------|---------------|---------|----------------------------------------|
```

Like other language models, Claude's responses are stochastic, so you'll get slightly different responses each time you query.

Let us know what you think! ⭐ us on [__GitHub__](https://github.com/stackql/stackql).