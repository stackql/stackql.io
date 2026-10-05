---
slug: setup-stackql-github-action
title: Using the setup-stackql Github Action
hide_table_of_contents: false
authors:	
  - yunchengyang
image: "/img/blog/setup-stackql-github-action.png"
description: This is a how-to article demonstrating the use of setup-stackql github action to run stackql with github actions
keywords: [stackql, devops, infrastructure, github actions, cloud security, CI/CD]
tags: [stackql, devops, infrastructure, github actions, cloud security, CI/CD]
---

<details>
<summary>What is StackQL?</summary>

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more.

:::

</details>

The `setup-stackql` action is now available in the [GitHub Marketplace](https://github.com/marketplace/actions/stackql-studio-setup-stackql).  `setup-stackql` downloads and installs the latest `stackql` binary in your Actions runner.  Different runner operating systems are fully tested and supported, including `ubuntu-latest`, `windows-latest` and `darwin-latest`.  

Embedding `stackql` in an actions workflow can be done for assurance, compliance, and provisioning.  An example workflow is shown here:  

```
name: 'Setup StackQL Example'

on:
  pull_request:

defaults:
  run:
    shell: bash

jobs:
  stackql-test-matrix:
    name: Stackql local run on ubuntu-latest with wrapper
    runs-on: 'ubuntu-latest'
    steps:
    - name: Checkout
      uses: actions/checkout@v3

    - name: Setup Stackql
      uses: ./
      with:
        use_wrapper: true

    - name: Validate Stackql Version
      run: |
        stackql --version

    - name: Prep Google Creds (bash)
      run: | ## use the secret to create json file
        sudo echo ${{ secrets.GOOGLE_CREDS }} | base64 -d > sa-key.json

    - name: Use Google Provider
      run: | 
        stackql exec "SELECT name, status
        FROM google.compute.instances 
        WHERE project = 'stackql-demo' AND zone = 'australia-southeast1-a';" --auth='{ "google": { "type": "service_account",  "credentialsfilepath": "sa-key.json" }}'

```

the log outputs showing...  

```
|-----------------------------------------------------|------------|
|                        name                         |   status   |
|-----------------------------------------------------|------------|
| stackql-demo-001                                    | TERMINATED |
|-----------------------------------------------------|------------|
| stackql-demo-002                                    | RUNNING    |
|-----------------------------------------------------|------------|
| stackql-demo-003                                    | RUNNING    |
|-----------------------------------------------------|------------|
| stackql-demo-004                                    | RUNNING    |
|-----------------------------------------------------|------------|
```

> additional output types available through the `--output` argument include `json` and `csv`

Source code for the action can be found at [__`stackql/setup-stackql`__](https://github.com/stackql/setup-stackql)  

much more to come, stay tuned!  
