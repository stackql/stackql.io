---
slug: introducing-model-driven-iac-with-stackql-deploy 
title: Introducing model-driven IaC with stackql-deploy
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-deploy-featured-image.png"
description: Inventory all resources across all regions in an AWS account.
keywords: [stackql, iac, infrastructure-as-code, dbt]
tags: [stackql, iac, infrastructure-as-code, dbt]
---

[__`stackql-deploy`__](https://pypi.org/project/stackql-deploy/) is a multi-cloud resource provisioning framework using [__`stackql`__](https://github.com/stackql/stackql). It is inspired by dbt (data build tool), which manages data transformation workflows in analytics engineering by treating SQL scripts as models that can be built, tested, and materialized incrementally. With StackQL, you can create a similar framework for cloud and SaaS provisioning. The goal is to treat infrastructure-as-code (IaC) queries as __*models*__ that can be deployed, managed, and interconnected.  

This ELT/model-based framework for IaC allows you to provision, test, update, and tear down multi-cloud stacks, similar to how dbt manages data transformation projects, with the benefits of version control, peer review, and automation. This approach enables you to deploy complex, dependent infrastructure components in a reliable and repeatable manner.  

## Features

StackQL simplifies the interaction with cloud resources by using SQL-like syntax, making it easier to define and execute complex cloud management operations. Resources are provisioned with [__`INSERT`__](/docs/language-spec/insert) statements, and tests are structured around [__`SELECT`__](/docs/language-spec/select) statements.

Features include:

- Dynamic state determination (eliminating the need for state files)
- Pre-flight and post-deploy assurance tests for resources
- Simple flow control with rollback capabilities
- Single code base for multiple target environments
- SQL-based definitions for resources and tests

## Installing `stackql-deploy`

To get started with `stackql-deploy`, run the following:

```bash
pip install stackql-deploy
```

`stackql-deploy` will automatically download the latest release of `stackql` using the [`pystackql`](https://pypi.org/project/pystackql/) Python package. You can then use the __`info`__ command to get runtime information:

```
$ stackql-deploy info
stackql-deploy version: 1.1.0
pystackql version     : 3.6.1
stackql version       : v0.5.612
stackql binary path   : /home/javen/.local/stackql
platform              : Linux x86_64 (Linux-5.15.133.1-microsoft-standard-WSL2-x86_64-with-glibc2.35), Python 3.10.12
```

## Project structure

A `stackql-deploy` project is a directory with declarative SQL definitions to provision, de-provision, or test resources in a stack. The key components and their definitions are listed here:

- `stackql_manifest.yml` : The manifest file for your project, defining resources and properties in your stack.
- `stackql_resources` directory : Contains StackQL queries to provision and de-provision resources in your stack.
- `stackql_tests` directory : Contains StackQL queries to test the desired state for resources in your stack.

## Getting started

Use the __`init`__ command to create a starter project directory:

```bash
stackql-deploy init activity_monitor
```

You will now have a directory named `activity_monitor` with `stackql_resources` and `stackql_tests` directories and a sample `stackql_manifest.yml` file, which will help you to get started.

## Usage

The general syntax for `stackql-deploy` is described here:

```bash
stackql-deploy [OPTIONS] COMMAND [ARGS]...
```

Commands include:

- `build`: Create or update resources based on the defined stack.
- `teardown`: Remove or decommission resources that were previously deployed.
- `test`: Execute test queries to verify the current state of resources against the expected state.
- `info`: Display the version information of the `stackql-deploy` tool and current configuration settings.
- `init`: Initialize a new project structure for StackQL deployments.

Optional global options (for all commands) include:

- `--custom-registry TEXT`: Specify a custom registry URL for StackQL. This URL will be used by all commands for registry interactions.
- `--download-dir TEXT`: Define a download directory for StackQL where all files will be stored.
- `--help`: Show the help message and exit.

Options for `build`, `test`, and `teardown` include:

- `--on-failure [rollback|ignore|error]`: Define the action to be taken if the operation fails. Options include rollback, ignore, or treat as an error.
- `--dry-run`: Perform a simulation of the operation without making any actual changes.
- `-e <TEXT TEXT>...`: Specify additional environment variables in key-value pairs.
- `--env-file TEXT`: Path to a file containing environment variables.
- `--log-level [DEBUG|INFO|WARNING|ERROR|CRITICAL]`: Set the logging level to control the verbosity of logs during execution.

## Example

Using the `activity_monitor` stack we created previously using the `init` command, we can start defining a stack and defining the associated queries; here is the manifest file:

```yaml
version: 1
name: activity_monitor
description: oss activity monitor stack
providers:
  - azure
globals:
  - name: subscription_id
    description: azure subscription id
    value: "{{ vars.AZURE_SUBSCRIPTION_ID }}"
  - name: location
    value: eastus
  - name: resource_group_name_base
    value: "activity-monitor"
resources:
  - name: monitor_resource_group
    description: azure resource group for activity monitor
    props:
      - name: resource_group_name
        description: azure resource group name
        value: "{{ globals.resource_group_name_base }}-{{ globals.stack_env }}"
	# more resources would go here...
```

> `globals.stack_env` is a variable referencing the user-specified environment label.

Our `stackql_resources` directory must contain a `.iql` file (StackQL query file) with the same name as each `resource` defined in the `resources` key in the manifest file. Here is an example for `stackql_resources/monitor_resource_group.iql`:

```sql
/*+ createorupdate */
INSERT INTO azure.resources.resource_groups(
  resourceGroupName,
  subscriptionId,
  data__location
)
SELECT
  '{{ resource_group_name }}',
  '{{ subscription_id }}',
  '{{ location }}'

/*+ delete */
DELETE FROM azure.resources.resource_groups 
WHERE resourceGroupName = '{{ resource_group_name }}' AND subscriptionId = '{{ subscription_id }}'
```

Similarly, our `stackql_tests` directory must contain a `.iql` file (StackQL query file) with the same name as each `resource` defined in the stack. Here is an example for `stackql_tests/monitor_resource_group.iql`:

```sql
/*+ preflight */
SELECT COUNT(*) as count FROM azure.resources.resource_groups
WHERE subscriptionId = '{{ subscription_id }}'
AND resourceGroupName = '{{ resource_group_name }}'

/*+ postdeploy, retries=2, retry_delay=2 */
SELECT COUNT(*) as count FROM azure.resources.resource_groups
WHERE subscriptionId = '{{ subscription_id }}'
AND resourceGroupName = '{{ resource_group_name }}'
AND location = '{{ location }}'
AND JSON_EXTRACT(properties, '$.provisioningState') = 'Succeeded'
```

Now we can __`build`__, __`test`__, and __`teardown`__ our example stack using these commands (starting with a __`dry-run`__, which will render the target queries without executing them):

```bash
# stackql-deploy build|test|teardown {stack_name} {stack_env} [{options}]
stackql-deploy build example_stack prd -e AZURE_SUBSCRIPTION_ID 00000000-0000-0000-0000-000000000000 --dry-run
stackql-deploy build example_stack prd -e AZURE_SUBSCRIPTION_ID 00000000-0000-0000-0000-000000000000
stackql-deploy test example_stack prd -e AZURE_SUBSCRIPTION_ID 00000000-0000-0000-0000-000000000000
stackql-deploy teardown example_stack prd -e AZURE_SUBSCRIPTION_ID 00000000-0000-0000-0000-000000000000
```

Give us your feedback! ⭐ us [__here__](https://github.com/stackql/stackql)!