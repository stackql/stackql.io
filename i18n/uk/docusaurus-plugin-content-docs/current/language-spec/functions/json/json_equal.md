---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Порівняння двох рядків JSON на еквівалентність за допомогою SQL.
image: "/img/stackql-featured-image.png"
---
Порівнює два рядки JSON і повертає 1, якщо вони еквівалентні, і 0 в іншому разі.

Див. також:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Типи даних ]](/language-spec/data-types)

* * * 

:::tip 

Функція `json_equal` - це власна функція розширення StackQL. Вона підтримує порівняння об'єктів і масивів JSON і, згідно зі специфікацією JSON, вважає еквівалентними об'єкти з ключами в різному порядку. Функція `json_equal` доступна в StackQL версії `0.5.707` і новіших і підтримується лише з вбудованим бекендом SQL у пам'яті, який StackQL використовує за замовчуванням.

:::

## Синтаксис {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Аргументи {#arguments}

__*json1*__  
Рядок JSON, що містить об'єкт або масив.

__*json2*__  
Інший рядок JSON для порівняння з першим.

> Обидва аргументи мають бути допустимими рядками JSON.

## Значення, що повертається {#return-values}
Ціле число: 1, якщо рядки JSON еквівалентні, і 0, якщо ні.

* * *

## Приклади {#examples}

### Порівняння об'єктів JSON {#compare-json-objects}

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

### Порівняння масивів JSON {#compare-json-arrays}

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

Докладніше див. [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
