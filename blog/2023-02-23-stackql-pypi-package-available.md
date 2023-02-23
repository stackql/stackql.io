---
slug: stackql-pypi-package-available
title: StackQL PyPI Package is Now Available
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-pypi-package-available.png"
description: This how-to article demonstrates how to use stackql with Python using the pystackql package
keywords: [stackql, python]
tags: [stackql, python]
---

<details>
<summary>What is StackQL?</summary>

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more.

:::

</details>

[`pystackql`](https://github.com/stackql/pystackql) is now available in [PyPI](https://pypi.org/project/pystackql/), and documentation for the package is available at [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/).  `pystackql` can be used with `pandas`, `matplotlib`, `plotly`, `jupyter` and more to run queries and visualize results.  

The latest version of stackql for any platform can be installed using:  

```
pip install pystackql
```

here is a complete example...  

[![pystackql usage](/img/blog/pystackql.gif)](/img/blog/pystackql.gif)


<details>
<summary>the code used in this example is available here</summary>

```python
from pystackql import StackQL
import pandas as pd
provider_auth =  {
    "aws": {
        "credentialsenvvar": "AWS_SECRET_ACCESS_KEY",
        "keyIDenvvar": "AWS_ACCESS_KEY_ID",
        "type": "aws_signing_v4"
    }
}
regions = ["ap-southeast-2", "us-east-1"]
stackql = StackQL(auth=provider_auth)

query = """
SELECT '%s' as region, instanceType, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = '%s'
GROUP BY instanceType
UNION
SELECT  '%s' as region, instanceType, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = '%s'
GROUP BY instanceType
""" % (regions[0], regions[0], regions[1], regions[1])

res = stackql.execute(query)
df = pd.read_json(res)
print(df)

```
which returns a `pandas` DataFrame like the following:  

```
  instanceType  num_instances          region
0    t2.medium              2  ap-southeast-2
1     t2.micro              7  ap-southeast-2
2     t2.small              4  ap-southeast-2
3     t2.micro              6       us-east-1
```

</details>

> Source code for `pystackql` can be found at [__`stackql/pystackql`__](https://github.com/stackql/pystackql)  

Let us know what you think!
