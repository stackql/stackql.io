---
title: Оператор конкатенации
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оператор конкатенации || в StackQL: объединение двух или более строковых значений в одну строку."
image: "/img/stackql-featured-image.png"
---
Возвращает строку, полученную в результате конкатенации, то есть объединения, двух или более строковых значений.  

См. также:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*character_expression*__  
Строковый литерал или строковый столбец.

## Возвращаемое значение {#return-values}

Возвращает строку.

:::note

Начиная с StackQL `v1.0` синтаксический анализатор рассматривает `||` как оператор конкатенации, который связывает операнды сильнее, чем операторы сравнения, поэтому `WHERE id = prefix || suffix` сравнивает `id` с объединенной строкой.  Более ранние выпуски распознавали лексему `||` как логический оператор `OR`.  Для логической дизъюнкции используйте `OR`.  [Переменные сеанса](/getting-started/session-variables) тоже можно конкатенировать: `$prefix || '-' || $suffix`.

:::

* * *

## Примеры {#examples}

### Конкатенация двух полей {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

Подробнее см. [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
