---
slug: openai-provider-for-stackql-available
title: OpenAI Provider for StackQL Available
hide_table_of_contents: true
authors:	
  - jeffreyaven
image: "/img/blog/stackql-provider-for-openai-released.png"
description: Deploy, configure and query openai resources using SQL.
keywords: [stackql, openai, llms, genai, iac, analytics]
tags: [stackql, openai, llms, genai, iac, analytics]
---

The __`openai`__ provider for [__`stackql`__](https://github.com/stackql/stackql) is now available in the dev stackql provider registry. The `openai` provider for `stackql` includes services `assistants`, `audit_logs`, `batch`, `chat`, `completions`, `embeddings`, `files`, `images`, `models`, `moderations`, `projects`, `uploads`, `vector_stores`, and more. To get started download [`stackql`](/install), set the `OPENAI_API_KEY` environment variable and use the dev registry as shown here:  

```bash
export DEV_REG="{ \"url\": \"https://registry-dev.stackql.app/providers\" }"
./stackql --registry="${DEV_REG}" shell
```

Then pull the `openai` provider using:

```sql
REGISTRY PULL openai;
```

Now you can run some queries, here are a few simple examples:

```shell
stackql  >>select * from openai.models.models;
|------------|------------------------------------|--------|-----------------|
|  created   |                 id                 | object |    owned_by     |
|------------|------------------------------------|--------|-----------------|
| 1712361441 | gpt-4-turbo                        | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1712601677 | gpt-4-turbo-2024-04-09             | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1681940951 | tts-1                              | model  | openai-internal |
|------------|------------------------------------|--------|-----------------|
| 1699053241 | tts-1-1106                         | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1723515131 | chatgpt-4o-latest                  | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1698798177 | dall-e-2                           | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1677532384 | whisper-1                          | model  | openai-internal |
|------------|------------------------------------|--------|-----------------|
| 1706037777 | gpt-4-turbo-preview                | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1727460443 | gpt-4o-audio-preview               | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1692901427 | gpt-3.5-turbo-instruct             | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1727389042 | gpt-4o-audio-preview-2024-10-01    | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1706037612 | gpt-4-0125-preview                 | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1706048358 | gpt-3.5-turbo-0125                 | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1677610602 | gpt-3.5-turbo                      | model  | openai          |
|------------|------------------------------------|--------|-----------------|
| 1692634615 | babbage-002                        | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1692634301 | davinci-002                        | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1727131766 | gpt-4o-realtime-preview-2024-10-01 | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1698785189 | dall-e-3                           | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1727659998 | gpt-4o-realtime-preview            | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1722814719 | gpt-4o-2024-08-06                  | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1715367049 | gpt-4o                             | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1721172741 | gpt-4o-mini                        | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1715368132 | gpt-4o-2024-05-13                  | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1721172717 | gpt-4o-mini-2024-07-18             | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1699046015 | tts-1-hd                           | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1699053533 | tts-1-hd-1106                      | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1698957206 | gpt-4-1106-preview                 | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1671217299 | text-embedding-ada-002             | model  | openai-internal |
|------------|------------------------------------|--------|-----------------|
| 1683758102 | gpt-3.5-turbo-16k                  | model  | openai-internal |
|------------|------------------------------------|--------|-----------------|
| 1705948997 | text-embedding-3-small             | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1705953180 | text-embedding-3-large             | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1698959748 | gpt-3.5-turbo-1106                 | model  | system          |
|------------|------------------------------------|--------|-----------------|
| 1686588896 | gpt-4-0613                         | model  | openai          |
|------------|------------------------------------|--------|-----------------|
| 1687882411 | gpt-4                              | model  | openai          |
|------------|------------------------------------|--------|-----------------|
| 1694122472 | gpt-3.5-turbo-instruct-0914        | model  | system          |
|------------|------------------------------------|--------|-----------------|
```

The `openai` provider covers the control plane - models, files, fine-tuning jobs, batches, vector stores and more. Inference endpoints (chat completions, responses, embeddings, images and audio) are out of scope; use the vendor SDKs for invocation.

For example, poll your fine-tuning jobs and batches:

```sql
SELECT id, status, model, fine_tuned_model, trained_tokens
FROM openai.fine_tuning.jobs;

SELECT id, status, endpoint, request_counts
FROM openai.batches.batches
LIMIT 10;
```

Vector stores expose a full CRUD surface with file membership:

```sql
SELECT id, name, status, usage_bytes, file_counts
FROM openai.vector_stores.vector_stores;
```

More to come!  Let us know what you think! ⭐ us on [__GitHub__](https://github.com/stackql/stackql).
