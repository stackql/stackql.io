---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - керування доступом
  - групи
  - каталог principal
description: "Функція GROUPS у StackQL: повертає групи каталогу автентифікованої сторони, що викликає, як масив JSON, коли ввімкнено керування доступом."
image: "/img/stackql-featured-image.png"
---
Повертає членство автентифікованої сторони, що викликає, у групах каталогу як масив JSON.  Потрібен сеанс, у якому ввімкнено [керування доступом](/command-line-usage/access-management).  Доступно в StackQL `v1.0` і новіших версіях.

Див. також:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ Керування доступом ]](/command-line-usage/access-management)

* * * 

## Синтаксис {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## Аргументи {#arguments}

Немає.

## Значення, що повертається {#return-values}

Повертає масив JSON з ідентифікаторами груп зі знімка каталогу principal, створеного для поточної інструкції, наприклад `["readers"]`.  Членство оновлюється для кожної інструкції, тому відкликання членства видно вже в наступній інструкції.  У сеансі, запущеному без `--access.config`, функція завершується помилкою.

* * *

## Приклади {#examples}

### Перевірка встановленої ідентичності {#inspect-the-established-identity}

Разом ці дві функції показують ідентифікатор principal сторони, що викликає, і її поточні групи каталогу.  Оскільки членство зчитується заново для кожної інструкції, виконання цього запиту після зміни в каталозі підтверджує, що сеанс бачить цю зміну.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
