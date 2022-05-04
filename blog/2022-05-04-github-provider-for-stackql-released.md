---
slug: github-provider-for-stackql-released
title: GitHub Provider for StackQL Released
authors:	
  - jeffreyaven
draft: false
image: "/img/blog/stackql-provider-for-github-released.png"
description: The StackQL provider for GitHub is now available, you can use this provider to query public and protected resources in GitHub.
keywords: [github, stackql, stackql provider registry, multicloud, asset management, cloud security]
tags: [github, stackql, stackql provider registry, multicloud, asset management, cloud security]
---

<head>
<meta name="author" content="Jeffrey Aven" />
</head>

The __GitHub provider for StackQL__ is now generally available.  This can be used to query resources in GitHub Cloud or GitHub Enterprise, including orgs, teams, users, repositories, branches, pull requests, issues, workflows/actions and much more!  

### See available providers

You can see the versions of GitHub Provider (and other providers) available using:  

`stackql registry list`  

or from the StackQL Command Shell (`stackql shell`) using:  

`REGISTRY LIST;`  

this would return a list of all the providers that are currently available, for example:  

```
+----------+---------+
| provider | version |
+----------+---------+
| github   | v0.1.0  |
| google   | v0.1.0  |
| okta     | v0.1.0  |
+----------+---------+
```

### Pull the `github` provider

To pull `v0.1.0` of the `github` provider use:  

`stackql registry pull github v0.1.0`  

or  

`REGSITRY PULL github v0.1.0;`  

to see what providers are installed use:  

`SHOW PROVIDERS;`  

this would return something like...  

```
+--------+
|  name  |
+--------+
| github |
+--------+
```

### Explore the `github` provider and query public resources

The provider and public objects can be queried without authentication as shown here:  

```
AUTH_STR='{"github": { "type": "null_auth" }}'
stackql shell --auth="${AUTH_STR}"
```

you can now enumerate services, resources, attributes and methods in the `github` provider using the `SHOW` and `DESCRIBE` meta commands, for instance:  

`show services in github` from either the StackQL command shell or via `stackql exec` would return something like...  

```
+----------------------------+---------------------+------------------------------------------+
|             id             |        name         |                  title                   |
+----------------------------+---------------------+------------------------------------------+
| actions_enterprises:v0.1.0 | actions_enterprises | GitHub v3 REST API - actions_enterprises |
| billing:v0.1.0             | billing             | GitHub v3 REST API - billing             |
| repos:v0.1.0               | repos               | GitHub v3 REST API - repos               |
| ...                        | ...                 | ...                                      |
+----------------------------+---------------------+------------------------------------------+
```

:::tip

Use the  __`EXTENDED`__ operator with the `SHOW` or `DESCRIBE` commands to get additional information about services, resources, attributes and methods, e.g. `DESCRIBE EXTENDED github.repos.repos`

:::

`show resources in github.repos` would return something like...  

```
+--------------+---------------------------+
|     name     |            id             |
+--------------+---------------------------+
| branches     | github.repos.branches     |
| commits      | github.repos.commits      |
| deployments  | github.repos.deployments  |
| environments | github.repos.environments |
| forks        | github.repos.forks        |
| releases     | github.repos.releases     |
| repos        | github.repos.repos        |
| statistics   | github.repos.statistics   |
| statuses     | github.repos.statuses     |
| traffic      | github.repos.traffic      |
| ...          | ...                       |
+--------------+---------------------------+
```

to see fields in a resource (which can be queried or updated) use `DESCRIBE` for example  `DESCRIBE github.repos.commits` would return something like...  

```
+--------------+--------+
|     name     |  type  |
+--------------+--------+
| files        | array  |
| stats        | object |
| commit       | object |
| url          | string |
| html_url     | string |
| parents      | array  |
| node_id      | string |
| comments_url | string |
| committer    | object |
| sha          | string |
| author       | object |
+--------------+--------+
```

to see methods available in a resource use the `SHOW METHODS` command for example `SHOW METHODS IN github.repos.commits` would return something like...  

```
+-------------------------------------------+-------------------------+
|                MethodName                 |     RequiredParams      |
+-------------------------------------------+-------------------------+
| compare_commits                           | basehead, owner, repo   |
| get_commit                                | owner, ref, repo        |
| list_branches_for_head_commit             | commit_sha, owner, repo |
| list_commits                              | owner, repo             |
| list_pull_requests_associated_with_commit | commit_sha, owner, repo |
+-------------------------------------------+-------------------------+
```

:::tip

Methods beginning with `list` or `get` can usually be accessed via `SELECT` statements.  For example, 

```sql
SELECT github.repos.commits.sha 
FROM github.repos.commits 
WHERE owner='${owner}' AND repo='${repo}';
```

Other methods can be accessed using the `EXEC` command (for more information see [__`EXEC`__](/docs/language-spec/exec))


:::

### Query protected resources

Accessing protected resources requires authentication using a Personal Access token as shown here:  

```
export GITHUB_CREDS=$(echo -n 'yourgithubusername:ghp_YOURPERSONALACCESSTOKEN' | base64)
AUTH_STR='{ "github": { "type": "basic", "credentialsenvvar": "GITHUB_CREDS" } }'
stackql shell --auth="${AUTH_STR}"
```

Now you are able to access protected resources, for example:  

```sql
select id, name, private 
from github.repos_orgs.repos_orgs 
where org = 'stackql';
```

which would return something like...  

```
+-----------+-------------------------+---------+
|    id     |          name           | private |
+-----------+-------------------------+---------+
| 443987542 | stackql                 | false   |
| 441087132 | stackqlproviderregistry | false   |
| 409393414 | fullstackchronicles.io  | false   |
| 435085734 | stackql.io              | true    |
| 443979486 | releases.stackql.io     | true    |
| 447890554 | stackqldevel            | true    |
|       ... | ...                     | ...     |
+-----------+-------------------------+---------+
```

Welcome your feedback by getting in touch or raising issues at [__stackql/stackql-provider-registry__](https://github.com/stackql/stackql-provider-registry), ⭐️ us while you are there!