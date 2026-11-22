---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення тангенса кута, вказаного в радіанах, за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює тангенс вказаного кута в радіанах.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*radian_value*__  
Числовий вираз, що визначає кут у радіанах, для якого потрібно обчислити тангенс.

## Значення, що повертається {#return-values}
Повертає тангенс вказаного кута.

* * *

## Приклади {#examples}

### Обчислення тангенса π/4 радіана {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

Цей запит повертає `1`, оскільки тангенс π/4 радіана дорівнює 1.

### Обчислення тангенса π радіан {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

Цей запит обчислює тангенс π радіан і повертає приблизно `0`, оскільки тангенс π дуже близький до 0.
