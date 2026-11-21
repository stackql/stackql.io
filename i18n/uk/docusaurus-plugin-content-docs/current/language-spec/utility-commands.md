---
title: Службові команди
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Службові команди оболонки StackQL, як-от clear і sleep: їхній синтаксис і коли їх використовувати в інтерактивних сеансах і скриптах."
image: "/img/stackql-featured-image.png"
---

Службові команди StackQL наведено в таблиці нижче:

| Службова команда | Опис | Приклад |
|--|--|--|
| `clear` | Очищає оболонку від результатів і команд, не потребує крапки з комою в кінці | `clear` або `CLEAR` |
| `sleep n` | Призупиняє виконання на `n` мілісекунд  | `sleep 5000` або `SLEEP 5000` |
