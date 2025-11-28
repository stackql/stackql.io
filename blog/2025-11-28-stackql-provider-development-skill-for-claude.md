---
slug: stackql-provider-development-skill-for-claude
title: StackQL Provider Development Skill for Claude
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-claude-featured-image.png"
description: A Claude Skill for developing StackQL providers, covering OpenAPI extensions, resource definitions, SQL verb mappings, and more.
keywords: [stackql, claude, ai, provider development, openapi, infrastructure-as-code, any-sdk]
tags: [stackql, claude, ai, provider development]
---

We've published a Claude Skill for [__StackQL__](https://github.com/stackql/stackql) provider development. It provides Claude with the context needed to help you build providers using the [__any-sdk__](https://github.com/stackql/any-sdk) library (interface used by StackQL to interact with the cloud providers).

## What's in the Skill

The skill covers the full provider development workflow:

- **Provider document structure** - The `provider.yaml` schema and service document layout
- **OpenAPI extensions** - All `x-stackQL-*` extensions including `x-stackQL-resources`, `x-stackQL-config`, `x-stackQL-objectKey`, and others
- **Resource and method definitions** - How to define resources, map methods to operations, and configure responses
- **SQL verb mappings** - Connecting REST operations to `SELECT`, `INSERT`, `UPDATE`, and `DELETE`
- **Authentication patterns** - API keys, service accounts, OAuth, and custom auth schemes
- **Pagination handling** - Request/response token configuration
- **Response processing** - JSONPath extraction, transformations, and schema handling

:::info What is a Claude Skill?
A Claude Skill is a markdown file that provides Claude with specialized knowledge for a particular domain or task. When you add a skill to your project, Claude can reference it during conversations to give more accurate, context-aware responses.

**To use this skill:**

1. Download the skill file ([__stackql-provider-development.md__](https://github.com/stackql/any-sdk/raw/refs/heads/main/.claude/skills/stackql-provider-development.md))
2. In Claude, go to your Project settings
3. Add the skill file to your Project Knowledge

Once added, Claude will automatically apply this knowledge when you're working on StackQL provider development.
:::

## Download

Grab the skill from the any-sdk repository:

**[Download stackql-provider-development.md](https://github.com/stackql/any-sdk/raw/refs/heads/main/.claude/skills/stackql-provider-development.md)**

## Usage

With the skill loaded, you can ask Claude things like:

- *"Create a provider definition for this API"*
- *"How do I map this endpoint to a SELECT operation?"*
- *"What's the correct objectKey syntax for this nested response?"*
- *"Help me configure pagination for this API"*

Claude will have the full context of StackQL's OpenAPI extensions and provider patterns to work from.

## Example

Here's a sample interaction after loading the skill:

```
You: I have an API endpoint GET /users that returns {"data": {"users": [...]}}. 
     How do I set up the resource?

Claude: For that response structure, you'd configure the method like this:

methods:
  list:
    operation:
      $ref: '#/paths/~1users/get'
    response:
      mediaType: application/json
      openAPIDocKey: '200'
      objectKey: $.data.users

The objectKey uses JSONPath to extract the users array from the nested response.
```

## Resources

- [StackQL Provider Registry](https://github.com/stackql/stackql-provider-registry)
- [any-sdk documentation](https://github.com/stackql/any-sdk)
- [StackQL documentation](https://stackql.io/docs)

⭐ Star us on [__GitHub__](https://github.com/stackql/stackql)
