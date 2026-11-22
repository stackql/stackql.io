---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення арктангенса (оберненого тангенса) числа з поверненням кута в радіанах за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює арктангенс (обернений тангенс) числа, тобто кут у радіанах, тангенс якого дорівнює вказаному числу.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз, для якого потрібно обчислити арктангенс.

## Значення, що повертається {#return-values}
Повертає арктангенс вказаного значення в радіанах.

* * *

## Приклади {#examples}

### Обчислення арктангенса 1 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

Цей запит повертає π/4, або приблизно `0.785`, оскільки арктангенс 1 дорівнює π/4 радіана.

### Обчислення арктангенса 0 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

Цей запит обчислює арктангенс 0 і повертає `0`, оскільки тангенс 0 радіанів дорівнює 0.
