---
title: Служебные команды
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Служебные команды clear и sleep оболочки StackQL: назначение каждой из них и пример."
image: "/img/stackql-featured-image.png"
---

Служебные команды StackQL перечислены в таблице ниже:

| Служебная команда | Описание | Пример |
|--|--|--|
| `clear` | Очищает оболочку от результатов и команд, не требует завершающей точки с запятой | `clear` или `CLEAR` |
| `sleep n` | Приостанавливает выполнение на `n` миллисекунд | `sleep 5000` или `SLEEP 5000` |
