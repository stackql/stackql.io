---
slug: markdownkv-output-format-available-in-pystackql
title: Markdown-KV Output Format Available in pystackql
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-ai-featured-image.png"
description: New output format in pystackql optimizes cloud infrastructure data for LLM processing based on research showing accuracy improvement over CSV.
keywords: [stackql, pystackql, markdown-kv, llm, ai, infrastructure-as-code, cloud management, data formats]
tags: [stackql, pystackql, llm, ai, infrastructure-as-code]
---

[__pystackql__](https://github.com/stackql/pystackql) now includes a `markdownkv` output format optimized for LLM processing of control plane and data plane data from cloud providers.

## Background

Recent research from [__ImprovingAgents.com__](https://www.improvingagents.com/blog/best-input-data-format-for-llms) tested 11 data formats to determine which ones LLMs parse most accurately. Using 1,000 synthetic employee records and 1,000 randomized queries, they measured how well different formats preserved data integrity through LLM processing.

The results:

| Format | Accuracy | 95% CI |
|--------|----------|--------|
| Markdown-KV | 60.7% | 57.6%–63.7% |
| JSON | 52.3% | 49.2%–55.4% |
| Markdown Tables | 51.9% | 48.8%–55.0% |
| JSONL | 45.0% | 41.9%–48.1% |
| CSV | 44.3% | 41.2%–47.4% |

<br/>
<br/>

Markdown-KV showed a 37% improvement over CSV and 16 percentage points over JSON. The tradeoff: it uses approximately 2.7x more tokens than CSV.

## What is Markdown-KV?

Markdown-KV uses hierarchical markdown headers with code blocks for key-value pairs:

```markdown
# Query Results

## Record 1

id: i-1234567890abcdef0
name: prod-web-01
region: us-east-1
instance_type: t3.large
state: running


## Record 2

id: i-0987654321fedcba0
name: staging-web-01
region: us-west-2
instance_type: t3.medium
state: stopped
```

The format combines clear hierarchy, explicit key-value pairs, and readability for both humans and LLMs.

## Usage

```python
from pystackql import StackQL

stackql = StackQL()

# Query with Markdown-KV output
result = stackql.execute(
    """
    SELECT instanceId, instanceType, state, availabilityZone 
    FROM aws.ec2.instances 
    WHERE region = 'us-east-1'
    """,
    output='markdownkv'
)

# Use with LLMs
response = llm_client.complete(
    f"Identify instances that should be stopped:\n\n{result}"
)
```

Works in server mode too:

```python
stackql = StackQL(server_mode=True)

result = stackql.execute(
    "SELECT name, region, encryption FROM google.storage.buckets WHERE project = 'my-project'",
    output='markdownkv'
)
```

## When to Use It

Markdown-KV is useful when:

- Feeding infrastructure data to LLMs for analysis, security reviews, or recommendations
- Building RAG pipelines that need to accurately retrieve and reason about infrastructure
- Accuracy matters more than token efficiency (infrastructure decisions typically do)
- Query results are focused datasets (most StackQL queries are)

The token cost is a real tradeoff, but infrastructure queries typically return targeted result sets, not massive datasets. When you're asking an LLM to analyze your production environment, accuracy matters.

## Getting Started

Update `pystackql`:

```bash
pip install --upgrade pystackql
```

Add `output='markdownkv'` to your execute calls or in the `StackQL` object instantiation:

```python
result = stackql.execute(query, output='markdownkv')
```

## Resources

- [ImprovingAgents.com research on LLM data formats](https://www.improvingagents.com/blog/best-input-data-format-for-llms)
- [pystackql v3.8.2 changelog](https://github.com/stackql/pystackql/releases/tag/v3.8.2)
- [pystackql documentation](https://github.com/stackql/pystackql)
- [StackQL documentation](https://stackql.io/docs)

The Markdown-KV output format is available in `pystackql` v3.8.2 and later.  

⭐ Star us on [__GitHub__](https://github.com/stackql/stackql) and join our community!