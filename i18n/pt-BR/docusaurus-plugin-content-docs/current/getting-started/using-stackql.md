---
title: Usar o StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Usar o StackQL: executar consultas de forma interativa no shell, de forma não interativa com exec ou como servidor, e descobrir providers, serviços e recursos durante o uso."
image: "/img/stackql-featured-image.png"
---
Veja também:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

O StackQL tem vários modos de uso, entre eles: o shell interativo do StackQL, a linha de comando do StackQL com instruções do StackQL ou a execução de uma ou mais instruções do StackQL a partir de um arquivo de entrada (arquivo IQL). As seções a seguir descrevem cada um dos modos em mais detalhes:

- Usar o shell interativo do StackQL ([`stackql shell`](/command-line-usage/shell))
- Usar o utilitário de linha de comando do StackQL ([`stackql exec`](/command-line-usage/exec))
- Usar um arquivo de entrada (arquivo IQL) ([`stackql exec -i`](/command-line-usage/exec))

### Usar o shell interativo do StackQL {#using-the-stackql-interactive-shell}
O shell do StackQL oferece aos engenheiros um ambiente de programação interativo para implantar recursos na nuvem e interagir com eles, além de permitir a análise interativa (com instruções [`SELECT`](/language-spec/select)) do seu ambiente na nuvem. O shell é funcionalmente semelhante ao shell do MySQL ou a outros shells REPL (Read Evaluate Print Loop). O shell aceita instruções válidas do StackQL e retorna os resultados ao usuário. As instruções podem ocupar várias linhas e são executadas quando você digita um ponto e vírgula como terminador.

O shell do StackQL pode ser iniciado com o comando `shell`, como mostrado a seguir (consulte [StackQL Provider Registry](/providers) para obter informações sobre a autenticação dos providers):

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
Em um shell autenticado em um provedor de nuvem, você pode começar a executar consultas do StackQL (consultas `SELECT` ou operações DDL, de infraestrutura como código).

Por padrão, o shell do StackQL retorna resultados em formato de tabela; no entanto, os formatos de saída `json`, `jsonl`, `csv` e `text` estão disponíveis com a opção `--output`. Consulte [Modos de saída](/getting-started/output-modes).

Você pode alterar o esquema de cores do shell do StackQL com a opção `--colorscheme`, como mostrado a seguir:

```shell
# Changing the Shell Color Scheme
./stackql shell --colorscheme light
```
### O arquivo de inicialização `.stackqlrc` {#the-stackqlrc-initialization-file}

Os valores padrão das opções de comando do StackQL, como o formato de saída e o esquema de cores mencionados anteriormente, podem ser definidos no arquivo de inicialização `stackqlrc`. Se esse arquivo existir no diretório atual usado pelo executável do StackQL, ele será carregado automaticamente; como alternativa, você pode referenciá-lo com a opção `--configfile`, como mostrado a seguir:

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
Mais informações sobre as opções de comando estão disponíveis no tópico Opções globais.

### Usar o utilitário de linha de comando do StackQL {#using-the-stackql-command-line-utility}
Os comandos do StackQL podem ser executados de forma não interativa com o comando exec do executável do StackQL. É possível executar mais de um comando em um lote, desde que cada comando seja terminado com um ponto e vírgula (`;`). As opções de comando, como a opção `--output`, podem ser especificadas na linha de comando ou no arquivo de inicialização `.stackqlrc` mencionado anteriormente.

A saída do executável do StackQL é enviada para stdout no sistema em uso, e essa saída pode ser analisada em um script BASH ou PowerShell.

Veja aqui um exemplo de script do Windows PowerShell que inclui um comando do StackQL:

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$env:GOOGLE_CREDENTIALS = Get-Content -Raw "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from google.compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
O comando do PowerShell acima seleciona instâncias do Compute Engine e exibe a saída com `Out-GridView`.

### Usar um arquivo de entrada (arquivo IQL) {#using-an-input-file-iql-file}
Os arquivos de entrada do StackQL podem ser usados para executar vários comandos terminados por ponto e vírgula. Essa é outra opção para a programação de infraestrutura não interativa ou em lote. Você especifica um arquivo de entrada com a opção `-i` ou `--infile`, como mostrado a seguir:

```shell
# create an iql file
echo "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "/mnt/c/tmp/stackql-demo.json" } }' \
--output json | jq '.[] | .name'
```
