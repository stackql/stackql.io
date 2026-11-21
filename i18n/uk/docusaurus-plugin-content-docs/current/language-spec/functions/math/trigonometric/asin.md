---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення арксинуса (оберненого синуса) числа з поверненням кута в радіанах за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює арксинус (обернений синус) числа, тобто кут у радіанах, синус якого дорівнює вказаному числу.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз у діапазоні від -1 до 1 включно, для якого потрібно обчислити арксинус.

## Значення, що повертається {#return-values}
Повертає арксинус вказаного значення в радіанах.

* * *

## Приклади {#examples}

### Обчислення арксинуса 0.5 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

Цей запит повертає π/6, або приблизно `0.5236`, оскільки арксинус 0.5 відповідає куту π/6 радіана.

### Обчислення арксинуса -1 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

Цей запит обчислює арксинус -1 і повертає -π/2, або приблизно `-1.5708`, оскільки синус -π/2 дорівнює -1.
