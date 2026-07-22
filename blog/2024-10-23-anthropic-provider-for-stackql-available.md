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

Now you can run some queries. Here's an example running inference against Claude using the Messages API - the request parameters are supplied in the `WHERE` clause and the response is projected using `JSON_EXTRACT`:

```sql
SELECT
  id,
  model,
  stop_reason,
  JSON_EXTRACT(content, '$[0].text') AS assistant_message,
  JSON_EXTRACT(usage, '$.output_tokens') AS output_tokens
FROM anthropic.messages.messages
WHERE model = 'claude-sonnet-5'
AND max_tokens = 1024
AND messages = '[{"role": "user", "content": "What is StackQL?"}]';
|------------------------------|-----------------|-------------|-------------------------------------------------------------|---------------|
|              id              |      model      | stop_reason |                      assistant_message                      | output_tokens |
|------------------------------|-----------------|-------------|-------------------------------------------------------------|---------------|
| msg_01MLTLVY6XCTT2cNBeFeJzfj | claude-sonnet-5 | end_turn    | StackQL is a SQL-based framework that lets you query and    |            48 |
|                              |                 |             | manage cloud and SaaS resources using familiar SQL syntax.  |               |
|------------------------------|-----------------|-------------|-------------------------------------------------------------|---------------|
```

You can also discover which models are available and their capabilities using the `vw_model_capabilities` view:

```sql
SELECT id, display_name, thinking, adaptive, xhigh, max_input_tokens, max_tokens
FROM anthropic.models.vw_model_capabilities;
|-------------------|-----------------|----------|----------|-------|------------------|------------|
|        id         |  display_name   | thinking | adaptive | xhigh | max_input_tokens | max_tokens |
|-------------------|-----------------|----------|----------|-------|------------------|------------|
| claude-sonnet-5   | Claude Sonnet 5 | 1        | 1        | 1     |           200000 |      64000 |
| claude-opus-4-8   | Claude Opus 4.8 | 1        | 1        | 1     |           200000 |      32000 |
| claude-haiku-4-5  | Claude Haiku 4.5| 1        | 0        | 0     |           200000 |      64000 |
|-------------------|-----------------|----------|----------|-------|------------------|------------|
```

Like other language models, Claude's responses are stochastic, so you'll get slightly different responses each time you query.

Let us know what you think! ⭐ us on [__GitHub__](https://github.com/stackql/stackql).