---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Команда stackql registry перелічує провайдери з реєстру провайдерів StackQL, завантажує їх і керує ними з командного рядка."
image: "/img/stackql-featured-image.png"
---

Команда для взаємодії з StackQL Provider Registry.  Розташування реєстру і те, чи перевіряються підписи документів провайдерів, налаштовуються глобальним прапорцем [`--registry`](/command-line-usage/global-flags#--registry).  Команда `registry` недоступна, поки ввімкнено [керування доступом](/command-line-usage/access-management); замість неї використовуйте `REGISTRY PULL` у захищеному сеансі `exec` або `shell`.

* * * 

### Синтаксис {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Аргументи {#arguments}

| Аргумент | Опис | Приклад |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Операція з реєстром, яку потрібно виконати (`list` або `pull`) | `list` або `pull` |    
|<span class="nowrap">`provider`</span>|Провайдер, який потрібно встановити за допомогою підкоманди `pull`| `okta` |
|<span class="nowrap">`version`</span>|Необов'язкова версія провайдера, яку потрібно встановити за допомогою підкоманди `pull` *(версія документа провайдера StackQL)* | `v23.03.00121` |


### Прапорці {#flags}

| Прапорець | Опис |
|--|--|
|<span class="nowrap">`-h, --help`</span>|Вивести довідку|
<br/>
<br/>
> додаткові параметри див. на сторінці [Глобальні прапорці](/command-line-usage/global-flags)

* * *

### Приклади {#examples}

Виведення списку провайдерів, доступних у публічному реєстрі:
```shell
stackql registry list
```

Завантаження останньої версії провайдера з публічного реєстру:
```shell
stackql registry pull okta
```
