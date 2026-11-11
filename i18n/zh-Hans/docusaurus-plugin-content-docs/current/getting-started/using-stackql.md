---
title: 使用 StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "使用 StackQL：在 shell 中交互式运行查询，通过 exec 非交互式运行，或以服务器方式运行，并在使用过程中发现 Provider、服务和资源。"
image: "/img/stackql-featured-image.png"
---
另请参阅：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL 有多种使用模式，包括：StackQL 交互式 shell、使用 StackQL 语句的 StackQL 命令行，或执行输入文件（IQL 文件）中的一条或多条 StackQL 语句。下面将更详细地介绍每种模式：

- 使用 StackQL 交互式 shell（[`stackql shell`](/command-line-usage/shell)）
- 使用 StackQL 命令行工具（[`stackql exec`](/command-line-usage/exec)）
- 使用输入文件（IQL 文件）（[`stackql exec -i`](/command-line-usage/exec)）

### 使用 StackQL 交互式 shell {#using-the-stackql-interactive-shell}
StackQL shell 为工程师提供了一个交互式编程环境，用于部署云资源并与之交互，还可以对其云环境进行交互式分析（使用 [`SELECT`](/language-spec/select) 语句）。该 shell 在功能上类似于 MySQL shell 或其他 REPL（Read Evaluate Print Loop）shell。该 shell 接受有效的 StackQL 语句，并将结果返回给用户。语句可以跨多行，在输入分号终止符后执行。

可以使用 `shell` 命令调用 StackQL shell，如下所示（有关 Provider 身份验证的信息，请参阅 [StackQL Provider Registry](/providers)）：

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
进入已通过云服务商身份验证的 shell 后，你就可以开始运行 StackQL 查询（`SELECT` 查询或 DDL 操作，即基础设施即代码操作）。

默认情况下，StackQL shell 返回表格形式的结果，不过也可以通过 `--output` 标志使用 `json`、`jsonl`、`csv` 和 `text` 输出格式，参阅[输出模式](/getting-started/output-modes)。  

你可以使用 `--colorscheme` 标志更改 StackQL shell 的配色方案，如下所示：  

```shell
# Changing the Shell Color Scheme
./stackql shell --colorscheme light
```
### `.stackqlrc` 初始化文件 {#the-stackqlrc-initialization-file}

StackQL 命令选项的默认值（例如前面讨论的输出格式和配色方案）可以通过 `stackqlrc` 初始化文件设置。如果该文件存在于 StackQL 可执行文件所使用的当前目录中，则会被自动加载；也可以使用 `--configfile` 标志引用它，如下所示：

```shell
# Specifing a Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
有关命令选项的更多信息，请参阅“全局标志”主题。

### 使用 StackQL 命令行工具 {#using-the-stackql-command-line-utility}
可以使用 StackQL 可执行文件的 exec 命令以非交互方式执行 StackQL 命令。只要每条独立的命令都以分号（`;`）结尾，就可以在一个批次中执行多条命令。命令标志（例如 `--output` 标志）可以在命令行中指定，也可以通过前面讨论的 `.stackqlrc` 初始化文件指定。  

StackQL 可执行文件的输出会发送到所用系统的 stdout，可以在 BASH 或 PowerShell 脚本中解析该输出。  

下面是一个包含 StackQL 命令的 Windows PowerShell 脚本示例：  

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$keyfilepath = "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--keyfilepath ${keyfilepath} --output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
上面的 PowerShell 命令演示了如何查询 Compute Engine 实例并使用 `Out-GridView` 显示输出。  

### 使用输入文件（IQL 文件） {#using-an-input-file-iql-file}
StackQL 输入文件可用于执行多条以分号结尾的命令。这是进行非交互式或批量基础设施编程的另一种方式。输入文件通过 `-i` 或 `--infile` 选项指定，如下所示：

```shell
# create an iql file
echo "select id, name from compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--keyfilepath /mnt/c/tmp/stackql-demo.json \
--output json | jq '.[] | .name'
```
