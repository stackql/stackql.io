---
slug: new-google-provider-available
title: Updated google provider for StackQL available
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-googleadmin-provider-featured-image.png"
description: Deploy, configure and query GCP resources using SQL.
keywords: [stackql, google, gcp, google cloud platform, iac, analytics]
tags: [stackql, google, gcp, google cloud platform, iac, analytics]
---

We have released the latest StackQL provider for Google, which includes:

- __14 new services__ (including `alloydb`, `apphub`, `biglake`, `bigquerydatapolicy`, `looker` and more)
- __231 new resources__
- __1,185 new methods__

More information is available [here](https://google.stackql.io/providers/google/).  Run the following to install or update the Google provider:

```sql
-- run from stackql shell
REGSITRY PULL google;
```

or

```bash
# from the command line
stackql registry pull google
```
Give us your feedback! ⭐ us [__here__](https://github.com/stackql/stackql)!