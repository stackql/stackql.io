---
title: Використання StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Використання StackQL: виконання запитів в інтерактивній оболонці, неінтерактивно через exec або в режимі сервера та пошук провайдерів, сервісів і ресурсів під час роботи."
image: "/img/stackql-featured-image.png"
---
Див. також:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL має кілька режимів використання: інтерактивна оболонка StackQL, командний рядок StackQL з інструкціями StackQL або виконання однієї чи кількох інструкцій StackQL із вхідного файлу (файлу IQL).  Кожен із цих режимів докладніше описано нижче:

- Використання інтерактивної оболонки StackQL ([`stackql shell`](/command-line-usage/shell))
- Використання утиліти командного рядка StackQL ([`stackql exec`](/command-line-usage/exec))
- Використання вхідного файлу (файлу IQL) ([`stackql exec -i`](/command-line-usage/exec))

### Використання інтерактивної оболонки StackQL {#using-the-stackql-interactive-shell}
Оболонка StackQL надає інженерам інтерактивне середовище програмування для розгортання хмарних ресурсів і роботи з ними, а також дає змогу інтерактивно аналізувати (за допомогою інструкцій [`SELECT`](/language-spec/select)) їхнє хмарне середовище.  Функціонально оболонка подібна до оболонки MySQL або інших оболонок REPL (Read Evaluate Print Loop).  Оболонка приймає коректні інструкції StackQL і повертає результати користувачеві.  Інструкції можуть займати кілька рядків і виконуються після введення завершальної крапки з комою.

Оболонку StackQL можна запустити командою `shell`, як показано нижче (відомості про автентифікацію провайдерів див. у [StackQL Provider Registry](/providers)):

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
Після запуску оболонки з автентифікацією для постачальника хмарних послуг ви можете виконувати запити StackQL (запити `SELECT` або операції DDL, тобто інфраструктура як код).

За замовчуванням оболонка StackQL повертає результати в табличному форматі, проте за допомогою прапорця `--output` доступні також формати виводу `json`, `jsonl`, `csv` і `text`; див. [Режими виводу](/getting-started/output-modes).  

### Файл ініціалізації `.stackqlrc` {#the-stackqlrc-initialization-file}

Значення за замовчуванням для параметрів команд StackQL, як-от формат виводу, можна встановити у файлі ініціалізації `.stackqlrc`.  Якщо цей файл є в поточному каталозі, який використовує виконуваний файл StackQL, його буде завантажено автоматично; інакше на нього можна вказати за допомогою прапорця `--configfile`, як показано нижче:

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
Докладніше про параметри команд див. на сторінці [Глобальні прапорці](/command-line-usage/global-flags).

### Використання утиліти командного рядка StackQL {#using-the-stackql-command-line-utility}
Команди StackQL можна виконувати неінтерактивно за допомогою команди exec виконуваного файлу StackQL.  У пакеті можна виконати кілька команд, якщо кожну окрему команду завершено крапкою з комою (`;`).  Прапорці команд, як-от прапорець `--output`, можна вказати в командному рядку або у файлі ініціалізації `.stackqlrc`, описаному вище.  

Вивід виконуваного файлу StackQL надсилається в stdout системи, яку ви використовуєте; цей вивід можна проаналізувати у скрипті BASH або PowerShell.  

Ось приклад скрипту Windows PowerShell із командою StackQL:  

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
Наведена вище команда PowerShell демонструє вибірку екземплярів Compute Engine з відображенням виводу за допомогою `Out-GridView`.  

### Використання вхідного файлу (файлу IQL) {#using-an-input-file-iql-file}
Вхідні файли StackQL можна використовувати для виконання кількох команд, завершених крапкою з комою.  Це ще один варіант неінтерактивного або пакетного програмування інфраструктури.  Вхідний файл вказують за допомогою параметра `-i` або `--infile`, як показано нижче:

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
