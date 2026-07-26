---
title: Short human title (unique across the library)
description: One or two sentences describing what the query answers and any scoping caveat. This drives search, the catalogue and page metadata.
format: md
verb: select
status: draft
providers: [aws]
services: [ec2]
tags: [aws, ec2, inventory]
keywords: [synonym one, synonym two]
intent_keywords:
  - phrase it exactly as a user would ask
  - second phrasing of the same ask
auth: [AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY]
params:
  - name: region
    type: identifier
    required: true
    description: Routing region
    example: us-east-1
outputs:
  - name: column_name
    type: string
    description: What the column contains
cost:
  fan_out: none
  expensive: false
related: []
---

One short paragraph expanding the description: what the query returns, when to use
it, and anything an operator should know before running it. Plain prose - keep
placeholders inside code spans or the SQL fence.

## Query

```sql
SELECT column_name FROM aws.ec2.some_resource WHERE region = '{{region}}';
```

## Notes

Optional operational notes: routing vs filter semantics, pagination behavior,
follow-up queries, failure modes. This section becomes the notes field in the
emitted JSON, so write it as compact prose.
