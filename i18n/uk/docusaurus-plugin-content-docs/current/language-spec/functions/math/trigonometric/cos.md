---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення косинуса кута, вказаного в радіанах, за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює косинус вказаного кута в радіанах.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*radian_value*__  
Числовий вираз, що визначає кут у радіанах, для якого потрібно обчислити косинус.

## Значення, що повертається {#return-values}
Повертає косинус вказаного кута.

* * *

## Приклади {#examples}

### Обчислення косинуса π/3 радіана {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

Цей запит повертає `0.5000000000000001`, наближене значення з рухомою комою для косинуса π/3 радіана, який дорівнює 0.5.

### Обчислення косинуса π радіан {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

Цей запит обчислює косинус π радіан і повертає `-1`, оскільки косинус π дорівнює -1.
