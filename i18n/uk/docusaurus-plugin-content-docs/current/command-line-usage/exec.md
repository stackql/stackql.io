---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Команда stackql exec виконує одну інструкцію StackQL або скрипт з інструкціями і має прапорці для формату виводу, автентифікації та реєстру провайдерів."
image: "/img/stackql-featured-image.png"
---

Команда для виконання запиту StackQL - як окремої інструкції або як пакета інструкцій зі скрипту StackQL (файлу IQL).  

* * * 

### Синтаксис {#syntax}

`stackql exec [query] [flags]`

* * *

### Аргументи {#arguments}

| Аргумент | Опис | Приклад |
|--|--|--|
|`query`|Запит StackQL, який потрібно виконати,<br/>або команда, яку потрібно запустити | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-b'"`<br/>_Зверніть увагу, що запит має бути взятий в одинарні або подвійні лапки_ |    


### Прапорці {#flags}

| Прапорець | Опис |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Режим виводу, допустимі значення: `table` (за замовчуванням), `json`, `jsonl` (JSON, розділений символами нового рядка; `ndjson` приймається як псевдонім), `csv`, `text` і `otel` (записи журналу OpenTelemetry, див. [Режими виводу](/getting-started/output-modes#otel-output)), приклад: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Виконати запит із зазначеного файлу, якщо потрібно; якщо прапорець вказано, будь-який запит, переданий як аргумент, ігнорується, приклад: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Вхідні дані для команди з файлу `json` або `jsonnet`, приклад: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Прапорець пробного запуску; виконується лише препроцесор, і повертається його вивід |
|<span class="nowrap">`--var`</span>|Зовнішні змінні (це можуть бути змінні середовища або секрети), які завантажують файли jsonnet, вказані за допомогою прапорця `--iqldata`, приклад: `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|Файл, у який за потреби виводяться результати (за замовчуванням результати виводяться лише в консоль), приклад: `--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Вивести довідку|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Виконувати запити в докладному режимі з додатковим виводом у stdout; якщо вибрано параметр `-f`, ця додаткова інформація журналювання записується у файл виводу разом із результатами запиту|
<br/>
<br/>
> додаткові параметри див. на сторінці [Глобальні прапорці](/command-line-usage/global-flags)


* * *

### Приклади {#examples}

Виведення результатів запиту в консоль у форматі таблиці:
```shell
stackql exec "SHOW services IN google"
```

Виведення результатів запиту в консоль у форматі json:
```shell
stackql exec "SHOW services IN google" -o json
```

Виведення результатів запиту у файл у форматі json:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Виконання запиту з вхідного файлу (файлу IQL) з виведенням результатів у файл у форматі json:
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### Код завершення {#exit-status}

Команда `exec` завершується з кодом `0` після обробки вхідних даних; помилки окремих інструкцій виводяться в консоль, тому скрипти мають перевіряти вивід. Починаючи з `v1.0`, вона завершується з кодом `1`, якщо інструкція конфігурації [`SET`](/language-spec/set) або [`UNSET`](/language-spec/unset) завершується помилкою, якщо запуск IaC у попередній версії повідомляє про збій ресурсу, або, коли ввімкнено [керування доступом](/command-line-usage/access-management), якщо будь-яка інструкція завершується помилкою чи відхиляється.
