---
slug: using-the-jsonnet-map-function
title: Using the Jsonnet Map Function
authors:	
  - jeffreyaven
image: "/img/blog/infraql-jsonnet-blog.png"
description: This article demonstrates the use of the map and format functions in the Jsonnet standard library.
keywords: [jsonnet, map, stackql, infracoding, IaC, infrastructure as code]
tags: [jsonnet, map, stackql, infracoding, IaC, infrastructure as code]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

<head>
<meta name="author" content="Jeffrey Aven" />
</head>

[Jsonnet](https://jsonnet.org/) is a fantastic configuration language as discussed in [Using Jsonnet to Configure Multiple Environments](https://cloudywithachanceofbigdata.com/using-jsonnet-to-configure-multiple-environments/).  Going slightly beyond the basics, this article is an introduction to anonymous functions and the `map` and `format` methods in the Jsonnet standard library.  

Similar to `map` methods in various other functional programming languages or data processing frameworks, `map` in Jsonnet evaluates a named or anonymous function for each element within an array.  `map` is a higher order function, meaning it is a function that calls another function.  Its signature is here:  

```
std.map(func, arr)
```

the `func` argument could be a named function or an unnamed (or anonymous function).  `arr` is an input array which could include embedded dictionaries or other lists as well.  

In this example I am templating some config for a NAT gateway in GCP for use in an StackQL routine, where I have a list of external IP's that need to be formatted in the Google `selfLink` format.  Perfect use for the `map` method as well as the `format` command similar to the `printf` or equivalent commands found in various other languages.  The easiest way to use this is similar to the way you would invoke this in Python:  

```
"%s/%s/%s" % [string1, string2, string3]
```

Putting it all together in the following practical example, you can see the input Jsonnet in the __Jsonnet__ tab and the templated or rendered output in the __Json__ tab.  `x` represents an element of the `extIps` array, then the function returns the fully qualified `selfLink` url.  

<Tabs
  defaultValue="jsonnet"
  values={[
    { label: 'Jsonnet', value: 'jsonnet', },
    { label: 'Json', value: 'output', },
  ]
}>
<TabItem value="jsonnet">

```jsx
{
 local project_id = 'myproject-123',
 local region = 'australia-southeast1',
 local self_link_prefix = 'https://compute.googleapis.com/compute/v1/projects/',
 local extIps = [{name: 'syd-extip1', region: region},{name: 'syd-extip2', region: region}],

 nats: [
  {
   name: 'nat-config', 
   natIpAllocateOption: 'MANUAL_ONLY', 
   natIps: std.map((function(x) "%s/%s/regions/%s/addresses/%s" % [self_link_prefix, project_id, x.region, x.name]), extIps), 
   sourceSubnetworkIpRangesToNat: 'ALL_SUBNETWORKS_ALL_IP_RANGES'
  },	
 ],
}
```

</TabItem>
<TabItem value="output">

```json
{
 "nats": [
  {
   "name": "nat-config",
   "natIpAllocateOption": "MANUAL_ONLY",
   "natIps": [
    "https://compute.googleapis.com/compute/v1/projects/myproject-123/regions/australia-southeast1/addresses/syd-extip1",
    "https://compute.googleapis.com/compute/v1/projects/myproject-123/regions/australia-southeast1/addresses/syd-extip2"
   ],
   "sourceSubnetworkIpRangesToNat": "ALL_SUBNETWORKS_ALL_IP_RANGES"
  }
 ]
}
```

</TabItem>
</Tabs>


more to come...  
