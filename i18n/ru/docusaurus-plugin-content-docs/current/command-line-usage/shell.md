---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Команда stackql shell запускает интерактивную оболочку StackQL для выполнения запросов и команд провайдеров в ваших облачных учетных записях и учетных записях SaaS."
image: "/img/stackql-featured-image.png"
---

Команда для запуска интерактивной командной оболочки StackQL.  Подробнее см. [Использование интерактивной оболочки StackQL](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Синтаксис {#syntax}

`stackql shell [flags]`

* * *

### Флаги {#flags}

| Флаг | Описание |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Вывести справку|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Выполнять запросы в подробном режиме с дополнительным выводом в stdout; если задан глобальный флаг `-f` (`--outfile`), эти дополнительные сведения журнала записываются в выходной файл вместе с результатами запросов|
<br/>
<br/>
> дополнительные параметры см. на странице [Глобальные флаги](/command-line-usage/global-flags)

* * *

### Примеры {#examples}

Запуск оболочки StackQL с аутентификацией с помощью файла ключа сервисного аккаунта.
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
