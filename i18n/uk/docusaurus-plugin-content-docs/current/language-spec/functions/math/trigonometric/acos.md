---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення арккосинуса (оберненого косинуса) числа з поверненням кута в радіанах за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює арккосинус (обернений косинус) числа, тобто кут у радіанах, косинус якого дорівнює вказаному числу.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз у діапазоні від -1 до 1 включно, для якого потрібно обчислити арккосинус.

## Значення, що повертається {#return-values}
Повертає арккосинус вказаного значення в радіанах.

* * *

## Приклади {#examples}

### Обчислення арккосинуса 0.5 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

Цей запит повертає приблизно `1.047`, оскільки арккосинус 0.5 дорівнює π/3 радіана (60 градусів), тобто приблизно 1.047 радіана.

### Обчислення арккосинуса 1 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

Цей запит обчислює арккосинус 1 і повертає `0`, оскільки косинус 0 радіанів дорівнює 1.
