---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція SUBSTRING у StackQL (псевдонім SUBSTR): повертає частину рядкового або двійкового значення за початковою позицією та довжиною."
image: "/img/stackql-featured-image.png"
---
Повертає частину символьного або двійкового виразу.  `SUBSTR` є псевдонімом для `SUBSTRING`.

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Типи даних ]](/language-spec/data-types)

* * * 

:::info

Щоб видобути поле з об'єкта JSON, скористайтеся функцією [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## Синтаксис {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*expression*__  
Рядок ASCII або значення BLOB.

> Якщо *expression* є рядком, то індекси символів відповідають фактичним символам UTF-8. Якщо *expression* є значенням BLOB, то індекси відповідають байтам.

__*start*__  
Ціле число, що позначає перший символ або байт підпослідовності вхідного виразу, яку потрібно повернути.  Якщо *start* від'ємне, то перший символ підрядка визначається відліком справа, а не зліва.

> Крайній лівий символ *expression* має номер 1.

__*length*__  
Необов'язкове ціле значення, що позначає довжину підпослідовності, яку потрібно повернути після значення *start*.  Якщо *length* не вказано, то повертаються всі символи до кінця рядка або значення BLOB, заданого аргументом *expression*.

> Якщо *length* від'ємне, то використовується абсолютне значення *length*.

## Значення, що повертається {#return-values}

Повертає рядок або значення BLOB залежно від типу *expression*.

* * *

## Приклади {#examples}

### Отримання перших *n* символів зі стовпця {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Відкидання перших *n* символів зі стовпця {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання останніх *n* символів зі стовпця {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
