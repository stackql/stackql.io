---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Команда stackql registry выводит список провайдеров из реестра провайдеров StackQL, загружает их и управляет ими из командной строки."
image: "/img/stackql-featured-image.png"
---

Команда для работы с StackQL Provider Registry.  Расположение реестра и то, проверяются ли подписи документов провайдеров, задаются глобальным флагом [`--registry`](/command-line-usage/global-flags#--registry).  Команда `registry` недоступна, пока включено [управление доступом](/command-line-usage/access-management); вместо нее используйте `REGISTRY PULL` в защищенном сеансе `exec` или `shell`.

* * * 

### Синтаксис {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Аргументы {#arguments}

| Аргумент | Описание | Пример |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Выполняемая операция с реестром (`list` или `pull`) | `list` или `pull` |    
|<span class="nowrap">`provider`</span>|Провайдер, устанавливаемый с помощью подкоманды `pull`| `okta` |
|<span class="nowrap">`version`</span>|Необязательная версия провайдера, устанавливаемая с помощью подкоманды `pull` *(версия документа провайдера StackQL)* | `v23.03.00121` |


### Флаги {#flags}

| Флаг | Описание |
|--|--|
|<span class="nowrap">`-h, --help`</span>|Вывести справку|
<br/>
<br/>
> дополнительные параметры см. на странице [Глобальные флаги](/command-line-usage/global-flags)

* * *

### Примеры {#examples}

Вывод списка провайдеров, доступных в публичном реестре:
```shell
stackql registry list
```

Загрузка последней версии провайдера из публичного реестра:
```shell
stackql registry pull okta
```
