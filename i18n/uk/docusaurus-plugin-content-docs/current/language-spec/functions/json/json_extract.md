---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція JSON_EXTRACT у StackQL: отримання одного або кількох значень з об'єкта чи масиву JSON за шляхом, основний інструмент для сплощення вкладених полів відповідей API."
image: "/img/stackql-featured-image.png"
---
Повертає одне або кілька значень із правильно сформованого об'єкта чи масиву JSON.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Типи даних ]](/language-spec/data-types)

* * * 

:::tip 

Скористайтеся інструкцією [**DESCRIBE**](/language-spec/describe), щоб знайти типи даних `array` або `object`, які можна використовувати з функціями JSON у StackQL. Скористайтеся функцією [**JSON_TYPE**](/language-spec/functions/json/json_type), щоб визначити типи даних вкладених об'єктів або елементів масиву.

:::

:::note 

Функцію `json_extract` реалізовано як функцію `json_extract_path_text`, якщо ви використовуєте stackql із бекендом SQL `postgres`.  Шлях розбивається за `.`, а початковий `$` відкидається, тому `'$.a.b'` перетворюється на ключі `'a'` і `'b'`; індекс масиву, як-от `$[0]`, передається як ключ об'єкта й не вибирає елемент масиву на цьому бекенді.

:::


## Синтаксис {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*json_array_or_object*__  
Масив або об'єкт JSON.

__*json_path*__  
Шлях JSON до масиву або об'єкта всередині *json_array_or_object*.  Див. [jsonpath.com](https://jsonpath.com/).

> Якщо вказано один *json_path*, функція повертає скалярний результат, тип даних якого відповідає визначеному шляху.

> Якщо вказано кілька аргументів *json_path*, результатом буде масив з об'єктів, визначених відповідними шляхами.

> Якщо вказати `'$'` як аргумент *json_path*, функція поверне весь об'єкт JSON, що відповідає стовпцю, вказаному в аргументі *json_array_or_object*.

> ПРИМІТКА: для розроблення й тестування виразів JSON Path можна використовувати такі інструменти, як [`json-viewer`](https://aarrange.com/json-viewer)

## Значення, що повертається {#return-values}
Рядкове значення, ціле число, об'єкт, масив, логічне значення або null залежно від переданих на вхід *json_array_or_object* і *json_path*.

* * *

## Приклади {#examples}

### Отримання об'єктів дисків, пов'язаних із кожним екземпляром Google Compute Engine {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання імені пристрою та розміру першого диска, підключеного до кожного екземпляра Google Compute Engine {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання мережевого рівня першого мережевого інтерфейсу (NIC), підключеного до кожного екземпляра Google Compute Engine {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання списку областей, пов'язаних із першим обліковим записом служби кожного екземпляра Google Compute Engine {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

Докладніше див. [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
