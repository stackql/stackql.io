---
slug: stackql-deploy-github-action-available
title: GitHub Action available for stackql-deploy
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/setup-stackql-github-action.png"
description: Use stackql-deploy in a GitHub Actions workflow to provision, update and test infrastructure.
keywords: [stackql, devops, infrastructure, github actions, cloud security, CI/CD, iac, infrastructure-as-code, dbt]
tags: [stackql, devops, infrastructure, github actions, cloud security, CI/CD, iac, infrastructure-as-code, dbt]
---

[__`stackql-deploy`__](https://pypi.org/project/stackql-deploy/) is now available in the [__GitHub Actions Marketplace__](https://github.com/marketplace/actions/stackql-deploy).  

:::tip

__`stackql-deploy`__ is a declarative, stateless (and __*state file-less*__) infrastructure-as-code and test framework, driven by [__`stackql`__](https://github.com/stackql/stackql) queries.  `stackql-deploy` is capable of provisioning, updating, de-provisioning and testing cloud and SaaS stacks across all cloud and SaaS providers.

:::

Given this [example](https://github.com/stackql/stackql-deploy-action/tree/main/examples/k8s-the-hard-way) `stackql-deploy` stack definition in a GitHub repo, you would simply add the following to your GitHub Actions workflow:

```yaml
...
jobs:
  stackql-actions-test:
    name: StackQL Actions Test
    runs-on: ubuntu-latest
    env:
      GOOGLE_CREDENTIALS: ${{ secrets.GOOGLE_CREDENTIALS }}
    
    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Deploy a Stack
        uses: stackql/setup-deploy@v1.0.1
        with:
          command: build
          stack-dir: examples/k8s-the-hard-way
          stack-env: dev
          env-vars: GOOGLE_PROJECT=stackql-k8s-the-hard-way-demo
```          
Let us know what you think! ⭐ us on [__GitHub__](https://github.com/stackql/stackql).
