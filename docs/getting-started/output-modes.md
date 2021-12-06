---
title: Output Modes
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
[[` exec `]](/docs/command-line-usage/exec) [[` shell `]](/docs/command-line-usage/shell)

Results returned from StackQL queries can be formatted in tabular (table) format, as well as json or csv format.  JSON and csv formats can be used to interchange data with other programs.  The desired output format is configured using the `--output` StackQL flag.

### Table Output Format
Results can be formatted in a table format using the `table` value for the `--output` parameter as shown below (this is the default value for the StackQL interactive shell):

```shell
stackql exec "select id, name from compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output table

|---------------------|-----------------|
|         ID          |      NAME       |
|---------------------|-----------------|
| 1257085253691867467 | demo-instance-1 |
|---------------------|-----------------|
| 2586731219281477960 | demo-instance-2 |
|---------------------|-----------------|
| 5584456226809282885 | demo-instance-3 |
|---------------------|-----------------|
```

### Json Output
Results can be returned in JSON format using the `json` value for the `--output` parameter as shown below, this output format is useful if the results of a query need to be passed to an external process or script:

```shell
stackql exec "select id, name from compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Csv Output
Results can be returned in JSON format using the `csv` value for the `--output` parameter as shown below, this output format is useful for parsing results in Excel or providing a data interface to another system:

```shell
stackql exec "select id, name from compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output csv

id,name
1257085253691867467,demo-instance-1
2586731219281477960,demo-instance-2
5584456226809282885,demo-instance-3
```

#### Csv Output Example (with alternative delimiter)
An alternatate delimiter can be specified for the `csv` output format using the `-d` switch as shown below:

```shell
stackql exec "select id, name from compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output csv -d "|"

id|name
1257085253691867467|demo-instance-1
2586731219281477960|demo-instance-2
5584456226809282885|demo-instance-3
```