---
slug: getting-files-changed-in-a-github-commit
title: Using StackQL to find changed files in a commmit using GitHub Actions
hide_table_of_contents: false
authors:	
  - yunchengyang
image: "/img/blog/setup-stackql-github-action.png"
description: This article demonstrates how to use stackql with github actions to get details about changed files in a commit.
keywords: [stackql, devops, infrastructure, github actions, cloud security, CI/CD]
tags: [stackql, devops, infrastructure, github actions, cloud security, CI/CD]
---

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more.

:::

> For more background on using StackQL with GitHub Actions see [StackQL GitHub Actions Tutorial](https://stackql.io/blog/stackql-github-actions)

```yaml
- name: setup StackQL
  uses: stackql/setup-stackql@v1.1.0
  with:
    use_wrapper: true

- name: get changed files
  env:
    STACKQL_GITHUB_USERNAME: ${{ secrets.STACKQL_GITHUB_USERNAME }}
    STACKQL_GITHUB_PASSWORD: ${{ secrets.STACKQL_GITHUB_PASSWORD }}
  shell: bash
  run: |
    ORG=$(echo "$GITHUB_REPOSITORY" | cut -d '/' -f1)
    REPO=$(echo "$GITHUB_REPOSITORY" | cut -d '/' -f2)
    QUERY="select filename FROM github.repos.commit_files where owner = '${ORG}' and ref = '${GITHUB_SHA}' and repo = '${REPO}'"
    echo "pulling github provider"
    stackql exec "REGISTRY PULL github"
    echo "running query: ${QUERY}"
    stackql --output json -f changed_files.txt exec "${QUERY}"
```

`changed_files.txt` looks like this...  

```json
[{"filename":"src/app.ts"},{"filename":"src/mod.ts"},...]
```

You could then do something with the changed files in a further step like:  

```yaml
- name: Do something with changed files
  run: |
    while IFS="" read -r filename || [ -n "$filename" ]
    do
      echo "processing ${filename}..."
      #
      # do something interesting here...
      #
    done < <(jq -r '.[] | .filename' changed_files.txt)
```

The `github.repos.commit_files` StackQL resource has other interesting fields which could be projected and used for actioning or reporting, these can be seen using:  

```
DESCRIBE EXTENDED github.repos.commit_files;
```

Fields available in this resource include:

- `status` - one of `added`, `removed`, `modified`, `renamed`, `copied`, `changed` or `unchanged`
- `filename` - filename which has changed
- `previous_filename` - previous filename if the filename had changed in the commit
- `additions` - the number of additions in each file
- `changes` - the number of changes to each file
- `deletions` - the number of deletions in each file  
- `patch` - `git diff` output for each file
- `blob_url` - the blob url for the file
- `raw_url` - the raw url for the file
- `contents_url` - the contents url for the file
- `sha` - The sha for each individual file

## Read More
### Check out a full demo on using StackQL with GitHub Actions
- [stackql-actions-demo](https://github.com/stackql/stackql-actions-demo)
### Check the GitHub Repos
- [setup-stackql](https://github.com/stackql/setup-stackql)
- [stackql-exec](https://github.com/stackql/stackql-exec)
- [stackql-assert](https://github.com/stackql/stackql-assert)