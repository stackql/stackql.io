---
title: Developing StackQL Providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---

See also:  
[[ StackQL Provider Registry ]](/providers) [[ Using a Provider ]](/docs/getting-started/using-a-provider)

## Overview

The StackQL Provider Registry is a crucial component of the StackQL ecosystem. It maintains and manages the 'provider' interface documents which inform the StackQL application about how to interact with various providers like AWS, Azure, Google, etc. 

The registry workflow involves three key components:

1. **GitHub**: This is where the 'provider' interface documents, formatted as OpenAPI specifications in YAML, are versioned and maintained. Each provider has its document, detailing available methods and how to invoke them using SQL semantics.

2. **GitHub Actions**: Upon updates to the GitHub repository, GitHub Actions are triggered to validate, test, and package the interface documents into signed, compressed artifacts.

3. **Deno Deploy**: The packaged artifacts are then registered and published to the StackQL Provider Registry Artifact Repository via Deno Deploy. These artifacts are available to the StackQL application via the registry API, retrievable using `REGISTRY LIST` or `REGISTRY PULL` commands.

![Your context diagram here]

## Developing a Provider Locally

To develop a StackQL provider, you'll need a provider's OpenAPI or Swagger specification. These specifications could either be supplied by the provider or generated through scripts such as [__google-discovery-to-openapi__](https://github.com/stackql/google-discovery-to-openapi) or [__stackql-azure-openapi__](https://github.com/stackql/stackql-azure-openapi).

Once you have the OpenAPI specification, you can use the [__openapisaurus__](https://github.com/stackql/openapisaurus) utility project to generate a StackQL provider document.

## Testing Your Provider

To ensure that your provider works as expected, you can test it using the `dev` registry before deploying it. Here's how:

1. Export the `dev` registry URL as an environment variable:

```bash
export DEV_REG="{ \"url\": \"https://registry-dev.stackql.app/providers\" }"
./stackql --registry="${DEV_REG}" shell
```