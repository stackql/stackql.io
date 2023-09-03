---
slug: query-and-create-github-releases-using-stackql
title: Query and Create GitHub Releases using StackQL
hide_table_of_contents: false
authors:  
  - jeffreyaven
image: "/img/blog/stackql-provider-for-github-released.png"
keywords: [stackql, github, github releases]
tags: [stackql, github, github releases]
---

[StackQL](https://github.com/stackql/stackql) and the [StackQL GitHub provider](https://registry.stackql.io/github) can be used to query objects in GitHub, including releases, tags, forks, commits, and much more.  This article shows how you can automate releases using StackQL.  

## Push tags

In my case, I merged a PR to the `main` branch for an updated GitHub action in a repo called `stackql-exec`, then I pushed a tag with an updated semver:

```
git tag v1.2.1
git push origin v1.2.1
```

## Pull the StackQL GitHub provider

From the StackQL shell or via the `exec` command, pull the latest GitHub provider for StackQL using:

```
REGISTRY PULL github;
```


## Generate a template for creating a release (optional)

StackQL allows you to create resource templates for creating resources such as ec2 instances, google cloud storage buckets, or github releases.  This is done using the `SHOW INSERT INTO` command as seen here:  

```
stackql exec --output text "SHOW INSERT INTO github.repos.releases"
```

The template will also generate an optional jsonnet variable block to define reusable or externally sourced variables (e.g., from environment variables).  

## Run your `INSERT` statement

In this case, I have just used literals to keep it simple...

```sql
INSERT INTO github.repos.releases(
  owner,
  repo,
  data__body,
  data__draft,
  data__generate_release_notes,
  data__name,
  data__prerelease,
  data__tag_name,
  data__target_commitish
)
SELECT
  'stackql',
  'stackql-exec',
  'Updated authentication and added support for external variables',
   false,
   false,
  'v1.2.1',
   false,
  'v1.2.1',
  'main'
;
```

## `SELECT FROM github.repos.releases` (optional)

You can inspect the releases created for your repo, including the one you just created, using a simple `SELECT` statement as shown here...

```sql
select 
name,
tag_name,
target_commitish,
created_at
from github.repos.releases where owner = 'stackql' and repo = 'stackql-exec';
```

You will see output like the following:

```
|--------------------------------|-------------|------------------|----------------------|                                              
|              name              |  tag_name   | target_commitish |      created_at      |                                              
|--------------------------------|-------------|------------------|----------------------|                                              
| v1.2.1                         | v1.2.1      | main             | 2023-09-03T05:44:40Z |                                              
|--------------------------------|-------------|------------------|----------------------|                                              
| v1.2.0                         | v1.2.0      | main             | 2023-09-03T05:20:42Z |                                              
|--------------------------------|-------------|------------------|----------------------|                                              
| Fix composite action issue     | v1.0.1      | main             | 2023-03-01T09:14:20Z |                                              
| with path                      |             |                  |                      |                                              
|--------------------------------|-------------|------------------|----------------------|                                              
| Production release with        | v1.0.0      | main             | 2023-02-25T23:07:32Z |                                              
| outputs                        |             |                  |                      |                                              
|--------------------------------|-------------|------------------|----------------------|                                              
| Initial release                | v1.0.0-beta | main             | 2023-02-05T12:50:01Z |                                              
|--------------------------------|-------------|------------------|----------------------|              
```

More information about the GitHub Provider for StackQL can be found [here](https://registry.stackql.io/github).  

Enjoy!
