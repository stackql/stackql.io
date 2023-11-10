---
slug: query-resources-across-aws-regions-asynchronously
title: "Query Resources Across AWS Regions Asynchronously"
hide_table_of_contents: false
authors:  
  - jeffreyaven
image: "/img/blog/stackql-aws-provider-featured-image.png"
keywords: [stackql, aws, inventory, cpsm, analytics]
tags: [stackql, aws, inventory, cpsm, analytics]
---

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more. 

:::

Most AWS services and resources are regionally scoped, meaning the UI, CLI, SDKs, and all other methods of querying the aws provider give you a regional view (`us-east-1` or `ap-southeast-2`, for instance).  Many customer AWS estates span multiple regions - for multinational organizations, for example, or organizations with numerous dispersed locations within the US.

Sure, you could write custom scripts wrapping the CLI or SDKs - which would require development effort (not reusable for other providers); or get an abstract view with tools like AWS Config or Systems Manager, which requires these services to be enabled and configured (not flexible and not extendible to other providers).  In either case:

1.  You can't write and run customized queries and generate custom reports - as you can do in SQL
2.  Any solutions you build will have to be rebuilt entirely for other providers

Using the latest ([__AWS provider for StackQL__](https://aws.stackql.io/providers/aws/) - which leverages the AWS Cloud Control API) and the [`executeQueriesAsync`](https://pystackql.readthedocs.io/en/latest/_modules/pystackql/stackql.html#StackQL.executeQueriesAsync) method in the [`pystackql`](https://pystackql.readthedocs.io/en/latest/index.html) Python package, I've put together an example here which runs a query to bring back attributes from all AWS Lambda functions deployed across 17 different AWS regions asynchronously.  Results can be returned as a list of Python dictionaries or a Pandas dataframe.  I am doing the former here, which took less than 10s.

```python
from pystackql import StackQL
from pprint import pprint
from asyncio import run
stackql = StackQL()
stackql.executeStmt("REGISTRY PULL aws") # not required if the aws provider is already installed

async def stackql_async_queries(queries):
    return await stackql.executeQueriesAsync(queries)

regions= ["us-east-1","us-east-2","us-west-1","us-west-2","ap-south-1","ap-northeast-3","ap-northeast-2","ap-southeast-1",
         "ap-southeast-2","ap-northeast-1","ca-central-1","eu-central-1","eu-west-1","eu-west-2","eu-west-3","eu-north-1",
          "sa-east-1"]

# list functions from all regions asynchronously
get_fns = [
    f"""
    SELECT *
    FROM aws.lambda.functions
    WHERE region = '{region}'
    """
    for region in regions
]

functions = run(stackql_async_queries(get_fns))

# get function details for all functions across all regions asynchronously
get_fn_details = [
    f"""
    SELECT 
    function_name,
    region,
    arn,
    description,
    architectures,
    memory_size,
    runtime
    FROM aws.lambda.function
    WHERE region = '{function['region']}'
    AND data__Identifier = '{function['function_name']}'
    """
    for function in functions
]

function_details = run(stackql_async_queries(get_fn_details))
pprint(function_details)
```

which returns...

```python
[{'architectures': '["x86_64"]',
  'arn': 'arn:aws:lambda:us-east-1:824532806693:function:stackql-helloworld-fn',
  'description': '',
  'function_name': 'stackql-helloworld-fn',
  'memory_size': '128',
  'region': 'us-east-1',
  'runtime': 'nodejs18.x'},
 {'architectures': '["x86_64"]',
  'arn': 'arn:aws:lambda:us-east-2:824532806693:function:stackql-helloworld-fn',
  'description': '',
  'function_name': 'stackql-helloworld-fn',
  'memory_size': '128',
  'region': 'us-east-2',
  'runtime': 'nodejs18.x'},
 {'architectures': '["x86_64"]',
  'arn': 'arn:aws:lambda:us-west-1:824532806693:function:stackql-helloworld-fn',
  'description': '',
  'function_name': 'stackql-helloworld-fn',
  'memory_size': '128',
  'region': 'us-west-1',
  'runtime': 'nodejs18.x'},
...
```

You could customize the StackQL query to run specific reports and visualize the results in a Jupyter notebook, for example:

- Functions by runtimes
- Function by memory size
- Functions by tags
- etc...

You could do something similar for other hyperscalars, for example, GCP, which scopes resources by projects, or Azure, which scopes resources by resource groups.

Let us know your thoughts! Visit us and give us a ⭐ on [__GitHub__](https://github.com/stackql/stackql)