---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Отримання математичної константи π (пі) за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Повертає математичну константу π (пі), тобто відношення довжини кола до його діаметра.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT PI() AS pi_value;
```

## Аргументи {#arguments}

Ця функція не потребує аргументів.

## Значення, що повертається {#return-values}
Повертає константу π, що приблизно дорівнює 3.14159.

* * *

## Приклади {#examples}

### Отримання значення π {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

Цей запит виводить значення π, що приблизно дорівнює `3.14159` і корисне в різних математичних обчисленнях, пов'язаних із колами та іншими геометричними функціями.
