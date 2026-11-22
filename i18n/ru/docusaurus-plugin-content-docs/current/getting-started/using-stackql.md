---
title: Использование StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Использование StackQL: интерактивные запросы в оболочке, неинтерактивные через exec или в режиме сервера и обнаружение провайдеров, сервисов и ресурсов по ходу работы."
image: "/img/stackql-featured-image.png"
---
См. также:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

У StackQL есть несколько режимов использования: интерактивная оболочка StackQL, командная строка StackQL с инструкциями StackQL или выполнение одной или нескольких инструкций StackQL из входного файла (файла IQL).  Каждый из режимов подробнее описан ниже:

- Использование интерактивной оболочки StackQL ([`stackql shell`](/command-line-usage/shell))
- Использование утилиты командной строки StackQL ([`stackql exec`](/command-line-usage/exec))
- Использование входного файла (файла IQL) ([`stackql exec -i`](/command-line-usage/exec))

### Использование интерактивной оболочки StackQL {#using-the-stackql-interactive-shell}
Оболочка StackQL предоставляет инженерам интерактивную среду программирования для развертывания облачных ресурсов и работы с ними, а также для интерактивного анализа своей облачной среды (с помощью инструкций [`SELECT`](/language-spec/select)).  По функциональности оболочка похожа на оболочку MySQL и другие оболочки REPL (Read Evaluate Print Loop).  Оболочка принимает допустимые инструкции StackQL и возвращает результаты пользователю.  Инструкции могут занимать несколько строк и выполняются после ввода завершающей точки с запятой.

Оболочку StackQL можно запустить командой `shell`, как показано ниже (сведения об аутентификации в провайдерах см. в [StackQL Provider Registry](/providers)):

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
Оказавшись в оболочке, аутентифицированной у поставщика облачных услуг, вы можете начать выполнять запросы StackQL (запросы `SELECT` или операции DDL - инфраструктуры как кода).

По умолчанию оболочка StackQL возвращает результаты в виде таблицы, однако с помощью флага `--output` доступны форматы вывода `json`, `jsonl`, `csv`, `text` и `otel`, см. [Режимы вывода](/getting-started/output-modes).  

### Файл инициализации `.stackqlrc` {#the-stackqlrc-initialization-file}

Значения по умолчанию для параметров команд StackQL, например формат вывода, можно задать в файле инициализации `.stackqlrc`.  Если этот файл находится в текущем каталоге, который использует исполняемый файл StackQL, он загружается автоматически; кроме того, на него можно сослаться с помощью флага `--configfile`, как показано ниже:

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
Подробнее о параметрах команд см. на странице [Глобальные флаги](/command-line-usage/global-flags).

### Использование утилиты командной строки StackQL {#using-the-stackql-command-line-utility}
Команды StackQL можно выполнять неинтерактивно с помощью команды exec исполняемого файла StackQL.  В одном пакете можно выполнить несколько команд, если каждая отдельная команда завершается точкой с запятой (`;`).  Флаги команды, например флаг `--output`, можно указать в командной строке или в рассмотренном ранее файле инициализации `.stackqlrc`.  

Вывод исполняемого файла StackQL направляется в stdout используемой системы, и этот вывод можно разобрать в скрипте BASH или PowerShell.  

Здесь показан пример скрипта Windows PowerShell, содержащего команду StackQL:  

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
Приведенная выше команда PowerShell выбирает экземпляры Compute Engine и отображает вывод с помощью `Out-GridView`.  

### Использование входного файла (файла IQL) {#using-an-input-file-iql-file}
Входные файлы StackQL позволяют выполнить несколько команд, завершенных точками с запятой.  Это еще один вариант неинтерактивного или пакетного программирования инфраструктуры.  Входной файл указывается с помощью флага `-i` или `--infile`, как показано ниже:

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
