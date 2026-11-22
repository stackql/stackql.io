---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция CHAR в StackQL: символы по одному или нескольким кодам символов, чтобы вставлять в строки переводы строк, кавычки, табуляцию и другие управляющие символы."
image: "/img/stackql-featured-image.png"
---

Функция `CHAR()` возвращает строку из символов, соответствующих одному или нескольким кодам символов.

См. также:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
При использовании StackQL с бэкендом PostgreSQL эквивалентная функция - `CHR()`, которая принимает один код.
:::

## Синтаксис {#syntax}

```sql
SELECT CHAR(ascii_code [, ascii_code, ...]) FROM <multipartIdentifier>;
```

> Каждый аргумент добавляет к результату один символ, поэтому `CHAR(72, 105)` возвращает `Hi`.

## Аргументы {#arguments}

**_ascii_code_**  
Одно или несколько целых чисел, каждое из которых - код символа: код ASCII или любая кодовая точка Юникода.

## Возвращаемое значение {#return-values}

Возвращает строку, в которой на каждый аргумент *ascii_code* приходится один символ.

* * *

## Распространенные коды ASCII {#common-ascii-codes}

| Символ        | Описание                  | Код ASCII  | Пример использования            |
|---------------|---------------------------|------------|---------------------------------|
| `\n`          | Перевод строки            | 10         | `CHAR(10)`                      |
| `'`           | Одинарная кавычка         | 39         | `CHAR(39)`                      |
| `"`           | Двойная кавычка           | 34         | `CHAR(34)`                      |
| (пробел)      | Пробел                    | 32         | `CHAR(32)`                      |
| `:`           | Двоеточие                 | 58         | `CHAR(58)`                      |
| `;`           | Точка с запятой           | 59         | `CHAR(59)`                      |
| `\t`          | Горизонтальная табуляция  | 9          | `CHAR(9)`                       |
| `\r`          | Возврат каретки           | 13         | `CHAR(13)`                      |
| `\b`          | Возврат на один символ    | 8          | `CHAR(8)`                       |
| `\f`          | Перевод страницы          | 12         | `CHAR(12)`                      |

* * *

## Примеры {#examples}

### Конкатенация строк со специальными символами {#concatenate-strings-with-special-characters}

Чтобы конкатенировать строки, разделив их символом новой строки:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Результат** (для пользователя с отображаемым именем `Jane Doe` и идентификатором `1234567890123456`):

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
