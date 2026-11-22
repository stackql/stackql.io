---
title: Оператор конкатенації
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Оператор конкатенації || у StackQL: об'єднує два або більше рядкових значень в одне рядкове значення."
image: "/img/stackql-featured-image.png"
---
Повертає рядкове значення, отримане внаслідок конкатенації, тобто об'єднання, двох або більше рядкових значень.  

Див. також:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*character_expression*__  
Рядковий літерал або рядковий стовпець.

## Значення, що повертається {#return-values}

Повертає рядкове значення.

:::note

Починаючи з StackQL `v1.0`, синтаксичний аналізатор розглядає `||` як оператор конкатенації з вищим пріоритетом, ніж оператори порівняння, тому `WHERE id = prefix || suffix` порівнює `id` з об'єднаним рядком.  Попередні випуски розпізнавали лексему `||` як логічний `OR`.  Для логічної диз'юнкції використовуйте `OR`.  [Змінні сеансу](/getting-started/session-variables) також можна конкатенувати: `$prefix || '-' || $suffix`.

:::

* * *

## Приклади {#examples}

### Конкатенація двох полів {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

Докладніше див. [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
