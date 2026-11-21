---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція JSON_OBJECT у StackQL: побудова об'єкта JSON з аргументів ключів і значень, наприклад щоб сформувати тіла запитів для INSERT і UPDATE."
image: "/img/stackql-featured-image.png"
---
Повертає об'єкт, побудований із переданих аргументів.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Типи даних ]](/language-spec/data-types)

* * * 

:::tip 

Скористайтеся інструкцією [**DESCRIBE**](/language-spec/describe), щоб знайти типи даних `array` або `object`, які можна використовувати з функціями JSON у StackQL.

:::

:::note 

Якщо ви використовуєте stackql із бекендом SQL `postgres`, використовуйте функцію `jsonb_build_object` замість `json_object`: StackQL передає `json_object` у PostgreSQL без змін, а однойменна функція PostgreSQL приймає ключі та значення як текстові масиви.

:::

## Синтаксис {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*key1, key2, ...*__  
Мітки для об'єкта JSON.

__*value1, value2, ...*__  
Значення, що відповідають міткам в об'єкті JSON.

## Значення, що повертається {#return-values}
Рядкове значення, що містить правильно сформований об'єкт JSON.

* * *

## Приклади {#examples}

### Побудова масиву об'єктів JSON зі стовпця {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

Докладніше див. [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
