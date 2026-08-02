---
slug: new-gemini-provider-available
title: New Google Gemini Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-gemini-provider-featured-image.png"
description: A new StackQL provider for the Google Gemini API - run Gemini inference, count tokens, generate embeddings, and manage models, tuned models, files, cached contents, corpora, file search stores and batches using SQL - available in the StackQL Provider Registry now.
keywords: [stackql, gemini, google, generative language, provider, inference, embeddings, tuned models, file search]
tags: [stackql, gemini, google, generative language, provider, inference, embeddings, tuned models, file search]
---

We've released a new StackQL provider for the Google Gemini API:

- [__`gemini`__](https://gemini-provider.stackql.io) - the Generative Language API surface (`generativelanguage.googleapis.com`): inference (content generation, embeddings, token counting, batches) plus the API's own management surface - models, tuned models and permissions, files, cached contents, corpora, file search stores and long-running operations (10 services, 32 resources, 75 operations)

Everything reachable with a `GEMINI_API_KEY` ships in one provider. Authentication is handled automatically, `LIMIT` is pushed down to the wire, and paginated lists are walked for you.

## Inference as a result set

Asking Gemini a question is a `SELECT`. The reply comes back as a row, with the text in the `candidates` array and the token spend in `usageMetadata`:

```sql
SELECT
  JSON_EXTRACT(candidates, '$[0].content.parts[0].text') AS reply,
  JSON_EXTRACT(usageMetadata, '$.totalTokenCount') AS total_tokens
FROM gemini.models.content
WHERE modelsId = 'gemini-2.5-flash'
AND contents = '[{"parts":[{"text":"Name the four Galilean moons of Jupiter, comma separated."}]}]';
```

Token counting works the same way via `gemini.models.token_counts` - free of charge, so a prompt can be priced before it is sent. Embeddings come back as rows too:

```sql
SELECT JSON_EXTRACT(embedding, '$.values[0]') AS first_dimension
FROM gemini.models.embeddings
WHERE modelsId = 'gemini-embedding-001'
AND content = '{"parts":[{"text":"stackql lets you query cloud APIs with SQL"}]}';
```

## Which model can do what

The provider ships a convenience view that fans each model's supported generation methods and limits out into flat columns:

```sql
SELECT name, display_name, input_token_limit, thinking, supports_generate_content
FROM gemini.models.vw_model_capabilities
ORDER BY name;
```

One row per model with token limits, default sampling parameters, and boolean flags for content generation, token counting, embedding, batching and caching support - useful for picking a model programmatically instead of reading release notes.

## The management surface as SQL

Files, cached contents, corpora, file search stores and tuned models are managed with the corresponding SQL verbs:

```sql
-- create
INSERT INTO gemini.corpora.corpora (displayName)
SELECT 'my-knowledge-base'
RETURNING name, displayName;

-- read
SELECT name, displayName, mimeType, sizeBytes, state
FROM gemini.files.files;

-- update
UPDATE gemini.cached_contents.cached_contents
SET ttl = '600s'
WHERE cachedContentsId = 'my-cache-id';

-- delete
DELETE FROM gemini.cached_contents.cached_contents
WHERE cachedContentsId = 'my-cache-id';
```

Batches are long-running operations and follow the async job pattern: `SELECT` polls them, `EXEC` runs the lifecycle ops:

```sql
SELECT name, done, JSON_EXTRACT(metadata, '$.state') AS state
FROM gemini.batches.batches;

EXEC gemini.batches.batches.cancel @batchesId = 'my-batch-id';
```

Tuned model permissions are a full CRUD surface as well, so sharing a tuned model is an `INSERT` and auditing who can see it is a `SELECT`.

## One credential, one boundary

The provider covers the Gemini API surface under a `GEMINI_API_KEY`. Billing, quota, API key management and IAM are GCP control-plane services (`cloudbilling`, `serviceusage`, `apikeys`, `iam`) that authenticate with OAuth/ADC - they live in the [__`google`__](https://google.stackql.io) provider. StackQL does cross-provider joins, so the two compose; worked examples are at [gemini-provider.stackql.io/google-control-plane](https://gemini-provider.stackql.io/google-control-plane).

## Authentication

Set a single environment variable:

```bash
export GEMINI_API_KEY=...
```

Keys are created in [Google AI Studio](https://aistudio.google.com/apikey). The key is only ever sent as a request header (`x-goog-api-key`) - never in the URL query string.

## Get started

Pull the provider from the public registry:

```bash
registry pull gemini
```

Provider docs are at [gemini-provider.stackql.io](https://gemini-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
