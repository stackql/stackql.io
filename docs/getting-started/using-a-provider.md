---
title: Using a Provider
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
[[` exec `]](/docs/command-line-usage/exec) [[` shell `]](/docs/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

StackQL queries can be run against a number of different cloud providers, including Google Cloud Platform, Amazon Web Services, Microsoft Azure, Digital Ocean, and more. StackQL provider specifications are pulled from the [__StackQL Provider Registry__](/providers), this can be done using the [__`REGISTRY PULL`__](/docs/language-spec/registry) StackQL command, or by using the [__`stackql registry`__](/docs/command-line-usage/registry) command line utility.


## Installing a Provider

To see the list of available providers, run the following command:  

```sql
REGISTRY LIST;
```

To install a provider, run the following command:

```sql
REGISTRY PULL okta;
```

To see providers installed on the current system, run the following command:

```sql
SHOW PROVIDERS;
```

## Authenticating to a Provider

Authenticating to a cloud provider is often the first step in an StackQL routine. Meta commands such as [`SHOW`](/docs/language-spec/show) and [`DESCRIBE`](/docs/language-spec/describe) can be run against a provider without authenticating, but [`SELECT`](/docs/language-spec/select), [`INSERT`](/docs/language-spec/insert) and [`EXEC`](/docs/language-spec/exec) commands generally require an authenticated session.  

Authentication is performed using environment variables set on the system (or sourced as secrets in a CI system), each provider will have it's own default unique variables used for authentication, so consult the documentation for your provider using the [StackQL Provider Registry documentation](/providers).  

:::tip

You can authenticate to multiple providers in the same interactive shell or non-interactive batch routine by supplying multiple authentication objects, for example:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```
```
This is especially useful when you are joining data between multiple providers in a single query.

:::

