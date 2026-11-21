---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - керування доступом
  - whoami
  - principal
description: "Функція WHOAMI у StackQL: повертає ідентифікатор principal автентифікованої сторони, що викликає, коли ввімкнено керування доступом."
image: "/img/stackql-featured-image.png"
---
Повертає ідентифікатор principal автентифікованої сторони, що викликає.  Потрібен сеанс, у якому ввімкнено [керування доступом](/command-line-usage/access-management).  Доступно в StackQL `v1.0` і новіших версіях.

Див. також:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ Керування доступом ]](/command-line-usage/access-management)

* * * 

## Синтаксис {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## Аргументи {#arguments}

Немає.

## Значення, що повертається {#return-values}

Повертає рядкове значення ідентифікатора principal, встановленого для поточної інструкції.  У сеансі, запущеному без `--access.config`, функція завершується помилкою.

* * *

## Приклади {#examples}

### Перевірка встановленої ідентичності {#inspect-the-established-identity}

Обидві функції не приймають аргументів і описують сторону, що викликає, яку сервер автентифікував для цієї інструкції.  `whoami()` повертає ідентифікатор principal, а `groups()` - групи каталогу сторони, що викликає, тому їх спільне виконання дає змогу швидко перевірити, що токен або сертифікат відповідає очікуваному principal, перш ніж виконувати щось інше.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
