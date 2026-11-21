---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Команда stackql shell запускає інтерактивну командну оболонку StackQL для виконання запитів і команд провайдерів до ваших облікових записів у хмарі та SaaS."
image: "/img/stackql-featured-image.png"
---

Команда для запуску інтерактивної командної оболонки StackQL.  Докладніше див. [Використання інтерактивної оболонки StackQL](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Синтаксис {#syntax}

`stackql shell [flags]`

* * *

### Прапорці {#flags}

| Прапорець | Опис |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Вивести довідку|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Виконувати запити в докладному режимі з додатковим виводом у stdout; якщо встановлено глобальний прапорець `-f` (`--outfile`), цю додаткову інформацію журналювання буде записано у файл виводу разом із результатами запитів|
<br/>
<br/>
> додаткові параметри див. на сторінці [Глобальні прапорці](/command-line-usage/global-flags)

* * *

### Приклади {#examples}

Запуск оболонки StackQL з автентифікацією за допомогою файлу ключа облікового запису служби.
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
