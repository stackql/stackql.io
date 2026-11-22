---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Сравнение двух JSON-строк на эквивалентность с помощью SQL.
image: "/img/stackql-featured-image.png"
---
Сравнивает две JSON-строки и возвращает 1, если они эквивалентны, и 0 в противном случае.

См. также:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Типы данных ]](/language-spec/data-types)

* * * 

:::tip 

Функция `json_equal` - это пользовательская функция расширения StackQL. Она поддерживает сравнение JSON-объектов и JSON-массивов и, согласно спецификации JSON, считает эквивалентными объекты, ключи которых идут в разном порядке. Функция `json_equal` доступна в StackQL `0.5.707` и более поздних версиях и поддерживается только со встроенным SQL-бэкендом в памяти, который StackQL использует по умолчанию.

:::

## Синтаксис {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Аргументы {#arguments}

__*json1*__  
JSON-строка, представляющая объект или массив.

__*json2*__  
Другая JSON-строка, которую нужно сравнить с первой.

> Оба аргумента должны быть допустимыми JSON-строками.

## Возвращаемое значение {#return-values}
Целое число: 1, если JSON-строки эквивалентны, 0, если нет.

* * *

## Примеры {#examples}

### Сравнение JSON-объектов {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### Сравнение JSON-массивов {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

Подробнее см. [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
