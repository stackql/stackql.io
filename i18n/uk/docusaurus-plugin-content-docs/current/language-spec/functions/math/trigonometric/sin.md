---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення синуса кута, вказаного в радіанах, за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює синус вказаного кута в радіанах.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*radian_value*__  
Числовий вираз, що визначає кут у радіанах, для якого потрібно обчислити синус.

## Значення, що повертається {#return-values}
Повертає синус вказаного кута.

* * *

## Приклади {#examples}

### Обчислення синуса π/2 радіана {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

Цей запит повертає `1`, оскільки синус π/2 радіана дорівнює 1.

### Обчислення синуса π радіан {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

Цей запит обчислює синус π радіан і повертає приблизно `0`, оскільки синус π дорівнює 0.
