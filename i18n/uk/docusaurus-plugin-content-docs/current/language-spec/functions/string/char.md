---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція CHAR у StackQL: символи для одного або кількох кодів символів, щоб вставляти в рядки символи нового рядка, лапки, табуляції й інші керувальні символи."
image: "/img/stackql-featured-image.png"
---

Функція `CHAR()` повертає рядкове значення, складене із символів для одного або кількох кодів символів.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
Якщо StackQL використовується з бекендом PostgreSQL, еквівалентною функцією є `CHR()`, яка приймає один код.
:::

## Синтаксис {#syntax}

```sql
SELECT CHAR(ascii_code [, ascii_code, ...]) FROM <multipartIdentifier>;
```

> Кожен аргумент додає до результату один символ, тому `CHAR(72, 105)` повертає `Hi`.

## Аргументи {#arguments}

**_ascii_code_**  
Одне або кілька цілих чисел, кожне з яких є кодом символу: кодом ASCII або будь-якою кодовою точкою Unicode.

## Значення, що повертається {#return-values}

Повертає рядкове значення з одним символом для кожного аргументу *ascii_code*.

* * *

## Поширені коди ASCII {#common-ascii-codes}

| Символ        | Опис                  | Код ASCII  | Приклад використання            |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | Новий рядок           | 10         | `CHAR(10)`                      |
| `'`           | Одинарна лапка        | 39         | `CHAR(39)`                      |
| `"`           | Подвійна лапка        | 34         | `CHAR(34)`                      |
| (пробіл)      | Пробіл                | 32         | `CHAR(32)`                      |
| `:`           | Двокрапка             | 58         | `CHAR(58)`                      |
| `;`           | Крапка з комою        | 59         | `CHAR(59)`                      |
| `\t`          | Горизонтальна табуляція | 9        | `CHAR(9)`                       |
| `\r`          | Повернення каретки    | 13         | `CHAR(13)`                      |
| `\b`          | Повернення на один символ | 8      | `CHAR(8)`                       |
| `\f`          | Переведення сторінки  | 12         | `CHAR(12)`                      |

* * *

## Приклади {#examples}

### Конкатенація рядків зі спеціальними символами {#concatenate-strings-with-special-characters}

Щоб конкатенувати рядки із символом нового рядка між ними:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Результат** (для користувача з відображуваним іменем `Jane Doe` та ідентифікатором `1234567890123456`):

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
