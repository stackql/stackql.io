---
title: Authenticating to a Provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

See also:  
[[` exec `]](/docs/command-line-usage/exec) [[` shell `]](/docs/command-line-usage/shell) [[ StackQL Provider Registry ]](https://registry.stackql.io/)

Authenticating to a cloud provider is often the first step in an StackQL routine. Meta commands such as [`SHOW`](/docs/language-spec/show) and [`DESCRIBE`](/docs/language-spec/describe) can be run against a provider without authenticating, but [`SELECT`](/docs/language-spec/select), [`INSERT`](/docs/language-spec/insert) and [`EXEC`](/docs/language-spec/exec) commands generally require an authenticated session.

Authentication can be done in one of two ways, interactive (which requires a browser on the machine you are using) and non-interactive (using a service account key file or API credentials).  Interactive authentication is not available for all providers, see the specific provider documentation in the [StackQL Provider Registry](https://registry.stackql.io/) for more information.

## Examples

Here are some examples of various methods of provider authentication, it is important to see the supported options for your provider, see  [__StackQL Provider Registry__](https://registry.stackql.io/).  

:::tip

You can authenticate to multiple providers in the same interactive shell or non-interactive batch routine by supplying multiple authentication objects, for example:  

```
AUTH='{ "google": { "credentialsfilepath": "creds/stackql-demo.json",  "type": "service_account" }, "okta": { "credentialsenvvar": "OKTA_SECRET_KEY", "type": "api_key" }}'
```
This is especially useful when you are joining data between multiple providers in a single query.

:::

### Service Account Key File Authentication

```
AUTH='{ "google": { "type": "service_account",  "credentialsfilepath": "creds/sa-key.json" }}
stackql shell --auth="${AUTH}"
```

### Interactive Authentication

```
AUTH='{ "google": { "type": "interactive" }}
stackql shell --auth="${AUTH}"
```
:::info

Not supported for every provider, check the provider documentation at [StackQL Provider Registry](https://registry.stackql.io/).

:::

### API Key Authentication

```
OKTA_SECRET_KEY=yourapikey
AUTH='{ "okta": { "type": "api_key",  "credentialsenvvar": "OKTA_SECRET_KEY" }}'
stackql shell --auth="${AUTH}"
```