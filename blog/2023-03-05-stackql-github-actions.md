---
slug: stackql-github-actions
title: Stackql Github Actions
hide_table_of_contents: false
authors:	
  - yunchengyang
image: "/img/blog/cross-cloud-queries-with-stackql.png"
description: This is a demonstration of how to use stackql with github actions.
keywords: [stackql, devops, infrastructure, github actions, cloud security, CI/CD]
tags: [stackql, devops, infrastructure, github actions, cloud security, CI/CD]
---

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more.

:::

## StackQL GitHub Actions

StackQL GitHub Actions include three main actions:

### [`setup-stackql`](https://github.com/stackql/setup-stackql)

This action installs the `stackql` CLI on Actions runners. It is used if you want to perform custom operations using StackQL queries.

Using the `setup-stackql` action allows you to perform StackQL CLI operations in the subsequent steps of a GitHub Actions job. The `setup-stackql` action installs the `stackql` CLI on the runner, making it available to all subsequent steps. This means you can use StackQL queries and commands directly in your GitHub Action Workflow Steps. 

### [`stackql-exec`](https://github.com/stackql/stackql-exec)

This action executes a StackQL query within an Actions workflow. 

The `stackql-exec` action is a wrapper around `setup-stackql` that allows you to pass your StackQL query and authentication information as inputs. This makes it easy to execute StackQL queries directly within your GitHub Action Workflow Step.

### [`stackql-assert`](https://github.com/stackql/stackql-assert)

This action is used to test assertions against the results of a StackQL query. You can use this action to validate the state of a resource after an IaC or lifecycle operation has been performed or to validate the system (e.g., CSPM or compliance queries).

The `stackql-assert` action is also a wrapper around `setup-stackql` that allows you to pass your StackQL query and authentication information as inputs. This makes it easy to test assertions against the results of your StackQL queries directly within your GitHub Action Workflow Step.

## Use `setup-stackql` to run StackQL queries in your GitHub Workflow Job

To run `StackQL` query with `GitHub Actions`, you need to install the `stackql CLI` on the runner. This can be done using the `setup-stackql` action. Once installed, the `stackql CLI` is available to all subsequent steps in the job via `StackQL`.

```
- name: setup StackQL
  uses: stackql/setup-stackql@v1.1.0
  with:
    use_wrapper: true
```

### Run StackQL query

After using the `setup-stackql` action, you can run the `stackql CLI` as a shell command in subsequent steps of the job. For example, you can run a dry run of a StackQL query as a shell command like this:

```
- name: Prep Google Creds (bash)
      if: ${{ matrix.os != 'windows-latest' }}
      run: | ## use the secret to create json file
        sudo echo ${{ secrets.GOOGLE_CREDS }} | base64 -d > sa-key.json

    - name: Use Google Provider
      run: | 
        stackql exec -i ./examples/google-example.iql --auth='{ "google": { "type": "service_account",  "credentialsfilepath": "sa-key.json" }}'
```

You can check more example [here](https://github.com/stackql/setup-stackql/blob/main/.github/workflows/setup-stackql.yml).

 

## Use `stackql-exec` to run StackQL queries in a single step

**`stackql-exec`** action ****provides a simpler and more streamlined way to execute **`StackQL`**
 queries in your GitHub Workflow. You can create a step like this:

```yaml
- name: stop running instances using stackql-exec
  uses: stackql/stackql-exec@v1.0.1
  with:
    auth_obj_path: './stackql/auth.json'
    query_file_path: './stackql/scripts/stop-instances.iql'
```

The **`auth_obj_path`** parameter specifies the path to the authentication object that contains the credentials required to authenticate with the data source. The **`query_file_path`** parameter specifies the path to the **`StackQL`** query file that you want to execute.

Once you have specified the authentication object and query file, you can run the **`StackQL`** query by executing the **`stackql-exec`** action in a single step in your GitHub Workflow.

You can check more examples **[here](https://github.com/stackql/stackql-exec/blob/main/.github/workflows/stackql-exec.yml)**.

## Validating resource state with `stackql-assert`

**`stackql-assert`** is another useful action that **`StackQL`** provides, allowing you to test assertions against the results of a **`StackQL`** query. 

This action is particularly helpful for validating the state of a resource after an infrastructure as code (IaC) or lifecycle operation has been performed.

You can run an assert step like this example:

```yaml
- name: check terraform deployment using stackql-assert - should fail
  uses: stackql/stackql-assert@v1.0.2
  with:
    auth_obj_path: './stackql/auth.json'
    test_query_file_path: './stackql/scripts/check-terraform-instances.iql'
    expected_results_str: '[{"name":"terraform-test-1","name":"terraform-test-2"}]'
```
## Authentication

### `setup-stackql`

if you use basic token, you can setup authentication with the following example

```yaml
- name: Use GitHub Provider
      run: |
        stackql exec -i ./examples/github-example.iql --auth='{ "github": { "type": "basic", "credentialsenvvar": "STACKQL_GITHUB_CREDS" } }'
      env: 
        STACKQL_GITHUB_CREDS: ${{  secrets.STACKQL_GITHUB_CREDS }}
```

Meanwhile if you are using a credential file, you can create the file via this example bash command
```bash
    - name: Prep Google Creds (bash)
      run: | ## use the secret to create json file
        sudo echo ${{ secrets.GOOGLE_CREDS }} | base64 -d > sa-key.json

    - name: Use Google Provider
      run: | 
        stackql exec "SELECT name, status
        FROM google.compute.instances 
        WHERE project = 'stackql-demo' AND zone = 'australia-southeast1-a';" --auth='{ "google": { "type": "service_account",  "credentialsfilepath": "sa-key.json" }}'
```

### `stackql-exec` and `stackql-assert`
For `stackql-exec` and `stackql-assert`, you can use the same authentication method as `setup-stackql`, except the authentication object is passed via the `auth_obj_path` or `auth_str`parameter
- `auth_obj_path` parameter to specify the path to the authentication object that contains the credentials required to authenticate with the data source.
- `auth_str` parameter to specify the authentication object as a string.
Example Authentication Object file (auth.json):
```json
{
  "github": {
    "type": "basic",
    "credentialsenvvar": "STACKQL_GITHUB_CREDS"
  }
}
```
Example Usage of `auth_obj_path`:
```yaml
  - name: Prep Google Creds (bash)
  if: ${{ matrix.os != 'windows-latest' }}
  shell: bash
  run: | ## use the base64 encoded secret to create json file
    sudo echo ${{ secrets.GOOGLE_CREDS }} | base64 -d > sa-key.json
    
  - name: stop running instances using stackql-exec
    uses: stackql/stackql-exec@v1.0.1
    with:
      auth_obj_path: './stackql/auth.json'
      query_file_path: './stackql/scripts/stop-instances.iql'

  - name: check if we have 4 instances using stackql-assert
    uses: stackql/stackql-assert@v1.0.2
    with:
      auth_obj_path: './stackql/auth.json'
      test_query_file_path: './stackql/scripts/check-instances.iql'
      expected_rows: 4
```

Example Usage of `auth_str`:
```yaml
  - name: Prep Google Creds (bash)
    if: ${{ matrix.os != 'windows-latest' }}
    shell: bash
    run: | ## use the base64 encoded secret to create json file
      sudo echo ${{ secrets.GOOGLE_CREDS }} | base64 -d > sa-key.json
      
  - name: exec github example
    uses: ./
    with:
      auth_str: '{ "github": { "type": "basic", "credentialsenvvar": "STACKQL_GITHUB_CREDS" } }'
      query: "REGISTRY PULL github v23.01.00104;
              SHOW PROVIDERS;
              select total_private_repos
              from github.orgs.orgs
              where org = 'stackql';"
    env: 
      STACKQL_GITHUB_CREDS: ${{ secrets.STACKQL_GITHUB_CREDS }}

  - name: Use test query string and expected rows, with auth string
    uses: ./
    with:
      auth_str: '{ "google": { "type": "service_account",  "credentialsfilepath": "sa-key.json" }}'
      test_query_file_path: './.github/workflows/workflow_scripts/google-example.iql'
      expected_rows: 1
```
## Read More
check the GitHub Repo for more examples and documentation
- [setup-stackql](https://github.com/stackql/setup-stackql)
- [stackql-exec](https://github.com/stackql/stackql-exec)
- [stackql-assert](https://github.com/stackql/stackql-assert)