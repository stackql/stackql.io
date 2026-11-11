---
title: 创建基础设施模板
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "在 StackQL 中使用 SHOW INSERT 为任意资源生成 INSERT 模板（包含全部参数或仅必需参数），作为基础设施定义的起点。"
image: "/img/stackql-featured-image.png"
---
另请参阅：  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ 使用变量 ]](/getting-started/variables)

在 StackQL 中，云资源通过 `INSERT` 语句部署。可以使用 `SHOW INSERT` 命令为不同的云资源生成 `INSERT` 模板。

### 使用 `SHOW INSERT` 命令生成 `INSERT` 模板 {#generating-an-insert-template-using-the-show-insert-command}

生成模板的示例如下：

```sql
SHOW INSERT INTO google.compute.addresses;
```
这可以通过 `shell` 运行，也可以使用 `exec` 命令运行，如下所示：

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__返回：__*  

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

> 之后可以将此模板与 `json` 或 `jsonnet` 数据文件配合使用，在运行时提供值，有关更多信息，请参阅[使用变量](/getting-started/variables)。

### 生成仅包含必需参数的 `INSERT` 模板 {#generating-an-insert-template-with-only-required-parameters}

要生成仅包含必需属性的 StackQL `INSERT` 模板，请使用 `/*+ REQUIRED */` 查询提示，如下所示：

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
这可以通过 `shell` 运行，也可以使用 `exec` 命令运行，如下所示：

```shell
./stackql exec 'show insert /*+ REQUIRED */ into compute.instances' --output text -H
```

*__返回：__*  

```sql
INSERT INTO google.compute.instances(
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
