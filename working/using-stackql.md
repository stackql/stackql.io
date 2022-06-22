---
title: Using StackQL
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

StackQL has several usage modes, these include: the StackQL interactive shell, the StackQL command line using StackQL statements, or executing one or more StackQL statements in an input file (IQL file).  Each of the modes is described in more detail below:

- Using the StackQL Interactive Shell ([`stackql shell`](/docs/command-line-usage/shell))
- Using the StackQL Command Line Utility ([`stackql exec`](/docs/command-line-usage/exec))
- Using an Input File (IQL File) ([`stackql exec -i`](/docs/command-line-usage/exec))

### Using the StackQL Interactive Shell
The StackQL Shell provides an interactive programming environment for engineers to deploy and interact with cloud resources, as well as enabling interactive analysis (using [`SELECT`](/docs/language-spec/select) statements) of their cloud environment.  The shell is functionally similar to the MySQL shell or other REPL (Read Evaluate Print Loop) shells.  The shell accepts valid StackQL statements, and returns results to the user.  Statements can span multiple lines and are executed once a semicolon terminator is entered.

The StackQL shell can be invoked using the shell command as shown below:

```shell
# Launching the StackQL Interactive Shell
stackql shell
```
Once in the shell and authenticated to a cloud provider (see [`AUTH`](/docs/language-spec/auth)), you can begin running StackQL queries (`SELECT` queries or DDL - Infrastructure as Code - operations).

By default the StackQL shell will return tabular results, however this can be changed to return results in `json` or `csv` format using the `--output` flag, as shown below:

```shell
# Json Output Example
stackql.exe shell --output json
```
You can change the color scheme for the StackQL shell using the `--colorscheme` flag, as shown below:

```shell
# Changing the Shell Color Scheme
./stackql shell --colorscheme light
```
### The `.iqlrc` Initialization File

Defaults for StackQL command options, such as the output format and color scheme discussed previously, can be set using the iqlrc initialization file.  If this file exists in the current directory used by the StackQL executable it will be automatically loaded, or alternatively can be referenced using the `--configfile` flag as shown below:

```shell
# Specifing a Initialization (.iqlrc) File
./stackql shell --configfile /tmp/.iqlrc
```
More information about command options is available in the Global Flags topic.

### Using the StackQL Command Line Utility
StackQL commands can be executed non-interactively using the exec command of the StackQL executable.  More than one command can be executed in a batch provided each discrete command is terminated with a semicolon (`;`).  Command flags, such as the `--output` flag, can be specified at the command line or using the `.iqlrc` initialization file discussed previously.  

The output from the StackQL executable is sent to stdout on the system being used, this output can be parsed within a BASH or PowerShell script.  

An example of a Windows PowerShell script including an StackQL command is shown here:  

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
The above PowerShell command demonstrates selecting Compute Engine instances displaying the output using `Out-GridView`.  

### Using an Input File (IQL File)
StackQL input files can be used to execute several commands terminated by semicolons.  This is another option for non-interactive or batch infrastructure programming.  An input file is specified with the  `-i` or `--infile` option as shown below:

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