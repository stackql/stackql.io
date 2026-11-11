---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "stackql exec 命令运行单条 StackQL 语句或由多条语句组成的脚本，并提供输出格式、身份验证和 Provider 注册表相关的标志。"
image: "/img/stackql-featured-image.png"
---

用于运行 StackQL 查询的命令，可以运行单条语句，也可以运行 StackQL 脚本（IQL 文件）中的一批语句。  

* * * 

### 语法 {#syntax}

`stackql exec [query] [flags]`

* * *

### 参数 {#arguments}

| 参数 | 说明 | 示例 |
|--|--|--|
|`query`|要执行的 StackQL 查询<br/>或要执行的命令 | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_注意，查询必须用单引号或双引号括起来_ |    


### 标志 {#flags}

| 标志 | 说明 |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|输出模式，有效值包括：`table`（默认）、`json`、`jsonl`（换行分隔的 JSON，也接受别名 `ndjson`）、`csv`、`text` 和 `otel`（OpenTelemetry 日志记录，参阅[输出模式](/getting-started/output-modes#otel-output)），示例：`--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|需要时从指定文件运行查询，指定后将忽略作为参数提供的任何查询，示例：`--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|通过 `json` 或 `jsonnet` 文件为命令提供源数据，示例：`--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|试运行标志；只运行预处理器并返回输出 |
|<span class="nowrap">`--var`</span>|外部变量（可以是环境变量或机密），由通过 `--iqldata` 标志指定的 jsonnet 文件读取，示例 `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|需要时用于输出结果的文件（默认行为是只将结果输出到控制台），示例 `--outfile myoutput.csv`|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|打印帮助信息|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|以详细模式运行查询，并将额外输出发送到 stdout，如果选择了 `-f` 选项，这些额外的日志信息将与查询结果一起写入输出文件|
<br/>
<br/>
> 更多选项参阅[全局标志](/command-line-usage/global-flags)


* * *

### 示例 {#examples}

以表格格式将查询结果输出到控制台：
```shell
stackql exec "SHOW services IN google"
```

以 json 格式将查询结果输出到控制台：
```shell
stackql exec "SHOW services IN google" -o json
```

以 json 格式将查询结果输出到文件：
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

从输入文件（IQL 文件）执行查询，并以 json 格式将结果输出到文件：
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### 退出状态 {#exit-status}

输入处理完成后，`exec` 以状态 `0` 退出；单条语句的错误会写入控制台，因此脚本应检查输出。从 `v1.0` 起，当 [`SET`](/language-spec/set) 或 [`UNSET`](/language-spec/unset) 配置语句失败时，当[访问管理](/command-line-usage/access-management)已启用且某条语句被拒绝时，或者当预览版 IaC 运行报告某个资源失败时，它以状态 `1` 退出。
