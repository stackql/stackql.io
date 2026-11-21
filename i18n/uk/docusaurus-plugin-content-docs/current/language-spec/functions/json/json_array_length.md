---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція JSON_ARRAY_LENGTH у StackQL: кількість елементів у масиві JSON на верхньому рівні або за шляхом у значенні JSON."
image: "/img/stackql-featured-image.png"
---
Повертає кількість елементів у масиві JSON.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Типи даних ]](/language-spec/data-types)

* * * 

:::tip 

Скористайтеся інструкцією [**DESCRIBE**](/language-spec/describe), щоб знайти типи даних `array` або `object`, які можна використовувати з функціями JSON у StackQL. Скористайтеся функцією [**JSON_TYPE**](/language-spec/functions/json/json_type), щоб визначити типи даних вкладених об'єктів або елементів масиву.

:::

## Синтаксис {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*json_array_or_object*__  
Масив або об'єкт JSON.

> Якщо *json_array_or_object* - об'єкт JSON (не масив) і *json_path* не вказано, функція повертає `0`.

__*json_path*__  
Необов'язковий шлях JSON до масиву, що міститься в об'єкті.  Див. [jsonpath.com](https://jsonpath.com/).

> Якщо вказано *json_path*, функція `json_array_length` знаходить цей шлях у *json_array_or_object* і повертає довжину масиву або `0`, якщо *json_path* є об'єктом (не масивом).  Якщо *json_path* не вдається знайти в *json_array_or_object*, функція повертає `NULL`.

> Якщо *json_array_or_object* не є правильно сформованим JSON або *json_path* не є правильно сформованим шляхом JSON, функція видає помилку.

## Значення, що повертається {#return-values}
Повертає ціле число.

* * *

## Приклади {#examples}

### Отримання кількості дисків, підключених до екземплярів Google Compute Engine {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання кількості мережевих інтерфейсів, підключених до екземплярів Google Compute Engine {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання кількості ACL, пов'язаних із бакетами Google Storage {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Отримання кількості областей, пов'язаних з обліковим записом служби {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
