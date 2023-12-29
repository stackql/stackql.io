---
slug: builtin-parallel-query-execution-in-stackql
title: "Builtin Parallel Query Execution in StackQL"
hide_table_of_contents: false
authors:  
  - jeffreyaven
image: "/img/blog/stackql-blog-post-featured-image.png"
keywords: [stackql, inventory, cpsm, analytics]
tags: [stackql, inventory, cpsm, analytics]
---

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more. 

:::

An exciting new feature to cap off 2023! Parallel query execution in StackQL.  With the latest release of StackQL, parameters in `WHERE IN` clauses are fetched asyncronously.  

This query for example, queries lambda functions across <span style={{ color: 'red', fontWeight: 'bold' }}>17 AWS regions in under 1.5 sec</span>, technically these are 17 parallel queries to 17 different endpoints.    

```sql
SELECT region, function_name
FROM aws.lambda.functions
WHERE region IN ( 'us-east-1','us-east-2','us-west-1','us-west-2','ap-south-1','ap-northeast-3','ap-northeast-2','ap-southeast-1','ap-southeast-2','ap-northeast-1','ca-central-1','eu-central-1','eu-west-1','eu-west-2','eu-west-3','eu-north-1','sa-east-1'
)
```

You could do something similar for other hyperscalars, for example querying resources across projects in GCP asynchronously, or querying across resource groups in Azure asynchronously.  

This capability was previously available using the [__`pystackql`__](https://pystackql.readthedocs.io/en/latest/) package, as discussed in the [__Query Resources Across AWS Regions Asynchronously__](https://stackql.io/blog/query-resources-across-aws-regions-asynchronously) blog post, but is now available natively in the StackQL query optimizer.   

You just need to add __`--execution.concurrency.limit=-1`__ to your [__`stackql exec`__](/docs/command-line-usage/exec) or [__`stackql shell`__](/docs/command-line-usage/shell) commands or when starting a StackQL Server using [__`stackql srv`__](/docs/command-line-usage/srv).  More query optimizations coming!  Happy New Year! 🎉 🎉 🎉   

Let us know your thoughts! Visit us and give us a ⭐ on [__GitHub__](https://github.com/stackql/stackql)