---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Команда stackql exec выполняет одну инструкцию StackQL или скрипт из инструкций, а ее флаги задают формат вывода, аутентификацию и реестр провайдеров."
image: "/img/stackql-featured-image.png"
---

Команда для выполнения запроса StackQL в виде одной инструкции или пакета инструкций из скрипта StackQL (файла IQL).

* * * 

### Синтаксис {#syntax}

`stackql exec [query] [flags]`

* * *

### Аргументы {#arguments}

| Аргумент | Описание | Пример |
|--|--|--|
|`query`|Выполняемый запрос StackQL<br/>или выполняемая команда | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-b'"`<br/>_Обратите внимание, что запрос должен быть заключен в одинарные или двойные кавычки_ |    


### Флаги {#flags}

| Флаг | Описание |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Режим вывода, допустимые значения: `table` (по умолчанию), `json`, `jsonl` (JSON с разделением символами новой строки, `ndjson` принимается как псевдоним), `csv`, `text` и `otel` (записи журнала OpenTelemetry, см. [Режимы вывода](/getting-started/output-modes#otel-output)), пример: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Выполнить запрос из указанного файла, если требуется; если флаг задан, любой запрос, переданный как аргумент, игнорируется, пример: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Исходные данные для команды из файла `json` или `jsonnet`, пример: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Флаг пробного запуска; выполняется только препроцессор, и возвращается вывод |
|<span class="nowrap">`--var`</span>|Внешние переменные (могут быть переменными среды или секретами), которые загружаются файлами jsonnet, заданными флагом `--iqldata`, пример: `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|Файл для вывода результатов, если требуется (по умолчанию результаты выводятся только в консоль), пример: `--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Вывести справку|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Выполнять запросы в подробном режиме с дополнительным выводом в stdout; если выбран флаг `-f`, эти дополнительные сведения журнала записываются в выходной файл вместе с результатами запроса|
<br/>
<br/>
> дополнительные параметры см. на странице [Глобальные флаги](/command-line-usage/global-flags)


* * *

### Примеры {#examples}

Вывод результатов запроса в консоль в формате таблицы:
```shell
stackql exec "SHOW services IN google"
```

Вывод результатов запроса в консоль в формате json:
```shell
stackql exec "SHOW services IN google" -o json
```

Вывод результатов запроса в файл в формате json:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Выполнение запроса из входного файла (файла IQL) с выводом результатов в файл в формате json:
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### Код завершения {#exit-status}

Команда `exec` завершается с кодом `0` после обработки входных данных; ошибки отдельных инструкций выводятся в консоль, поэтому скрипты должны проверять вывод. Начиная с `v1.0` она завершается с кодом `1` в следующих случаях: инструкция конфигурации [`SET`](/language-spec/set) или [`UNSET`](/language-spec/unset) завершается ошибкой; запуск IaC предварительной версии сообщает о сбое ресурса; включено [управление доступом](/command-line-usage/access-management), и любая инструкция завершается ошибкой или отклоняется.
