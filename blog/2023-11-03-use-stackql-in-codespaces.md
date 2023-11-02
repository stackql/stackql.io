---
slug: use-stackql-in-codespaces
title: "Use StackQL in Codespaces"
hide_table_of_contents: false
authors:  
  - jeffreyaven
image: "/img/blog/stackql-provider-for-github-released.png"
keywords: [stackql, analytics, github, codespaces]
tags: [stackql, analytics, github, codespaces]
---

GitHub Codespaces is a development environment completely hosted online, enabling seamless development without setting up local machines.  One of the great features of Codespaces is its compatibility with IPython, providing an interactive computing environment to test and prototype StackQL queries.

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more. 

:::

Codespaces and the power of IPython and notebooks provide a quick and easy way to use StackQL to analyze and report on your cloud estate and resource configuration.  No software necessary!

![stackql-codespaces-notebook](/img/blog/codespaces/codespaces-3.png)

## Setting Up StackQL in Codespaces
See the [__stackql-codespaces-notebook__](https://github.com/stackql/stackql-codespaces-notebook) repository as an example.

With the example `devcontainer.json` configuration file shown below, you can use the [__`stackql/stackql-codespaces-base`__](https://hub.docker.com/r/stackql/stackql-codespaces-base) image, which includes stackql and the [__`pystackql`__](https://pystackql.readthedocs.io/en/latest/) package (which provides the IPython magic extension used to run stackql queries and return Pandas dataframes for inspection or visualization).

```json
{
  "image": "stackql/stackql-codespaces-base",
  "containerEnv": {
    "STACKQL_GITHUB_PASSWORD": "${secrets:STACKQL_GITHUB_PASSWORD}",
    "STACKQL_GITHUB_USERNAME": "${secrets:STACKQL_GITHUB_USERNAME}"
  },
  "hostRequirements": {
    "cpus": 2
  },  
  "customizations": {
    "vscode": {
      "extensions": [
        "ms-toolsai.jupyter",
        "ms-python.python"
      ]
    }
  }
}
```

Note that the `devcontainer` configuration includes the essential extensions for Jupyter and Python; you can also optionally specify host requirements for the Codespaces machine.

### Provider Authentication

The environment variables required to authenticate to your specific provider or providers can be supplied using Codespaces secrets and passed securely to the Codespaces container using the `containerEnv` object in the example above.  For specifics about variables required for different providers, see the provider documentation for your provider in the [StackQL Provider Registry](https://registry.stackql.io/).


## Launching the Codespaces Environment
Given the `devcontainer` configuration shown above in the root of your repository at `.devcontainer/devcontainer.json`, you can launch codespaces from `<your_repo_slug>/codespaces` for example [https://github.com/stackql/stackql-codespaces-notebook/codespaces](https://github.com/stackql/stackql-codespaces-notebook/codespaces).  You can start a codespaces environment on any branch of your repo.


## Running queries and visualizing output
When the environment is provisioned (usually takes a minute or two), you can use the StackQL magic extension and the `%%stackql` magic decorator to seamlessly run stackql queries, including variable substitution, for example.

```python
%load_ext pystackql.magic
```
(loads the Stackql magic extension, making the `%%stackql` decorator available)

```python
region = 'us-east-1'
```
(set some notebook variables for reusability)

```sql
%%stackql
SELECT instanceType, COUNT(*) as num_instances 
FROM aws.ec2.instances
WHERE region = '$region'
GROUP BY instanceType
```
(run a query)

```python
_.plot(kind='pie', y='num_instances', labels=_['instanceType'], title='Instances by Type', autopct='%1.1f%%')
```
(visualize the results - using `matplotlib`, `plotly`, or any other visualization package)  

heres an example:  

![stackql-codespaces-notebook](/img/blog/codespaces/codespaces-6.png)

Using the `pystackql` package, you can also run asynchronous queries such as querying assets across AWS regions, Azure resource groups (or subscriptions) or Google projects in one statement, for example:

```python
# get multiple regions asynchronously
regions = ["ap-southeast-2", "us-east-1"]

queries = [
    f"""
    SELECT '{region}' as region, instanceType, COUNT(*) as num_instances
    FROM aws.ec2.instances
    WHERE region = '{region}'
    GROUP BY instanceType
    """
    for region in regions
]

instances_df = await stackql.executeQueriesAsync(queries)
```

Visit us and give us a ⭐ on [__GitHub__](https://github.com/stackql/stackql)