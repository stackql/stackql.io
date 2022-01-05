---
title: Creating Infrastructure Templates
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---
See also:  
[[` exec `]](/docs/command-line-usage/exec) [[` INSERT `]](/docs/language-spec/insert) [[` SHOW `]](/docs/language-spec/show) [[ Using Variables ]](/docs/getting-started/variables)

Cloud resources are deployed with StackQL using `INSERT` statements.  `INSERT` templates for different cloud resources using the `SHOW INSERT` command.

### Generating an `INSERT` template using the `SHOW INSERT` command

An example of generating a template is shown here:

```sql
SHOW INSERT INTO google.compute.addresses;
```
This can be run through the `shell` or using an `exec` command as shown here:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Returns :__*  

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

> This template can then be used along with a `json` or `jsonnet` data file to supply values at run time,for more information see [Using Variables](/docs/getting-started/variables).

### Generating an `INSERT` template with only required parameters

To generate an StackQL `INSERT` template with only mandatory attributes use the `/*+ REQUIRED */` query hint as shown here:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
This can be run through the `shell` or using an `exec` command as shown here:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into compute.instances' --output text -H
```

*__Returns :__*  

```sql
INSERT INTO compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```