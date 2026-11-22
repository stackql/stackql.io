---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Агрегування пар "ключ-значення" в об'єкт JSON за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Агрегує пари "ключ-значення" в об'єкт JSON із групуванням за вказаним стовпцем. Функція `JSON_GROUP_OBJECT()` збирає всі пари "ключ-значення" кожної групи з указаних стовпців і об'єднує їх в один об'єкт JSON для кожної групи.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Аргументи {#arguments}

__*key_column*__  
Стовпець, що містить ключі для об'єктів JSON.

__*value_column*__  
Стовпець, що містить значення для об'єктів JSON.

__*grouping_column*__  
Стовпець `GROUP BY`; це не аргумент функції `JSON_GROUP_OBJECT`, яка приймає рівно два аргументи.  Для кожної групи повертається один об'єкт JSON.

## Значення, що повертається {#return-values}
Повертає об'єкт JSON з усіма агрегованими парами "ключ-значення" з указаних стовпців, згрупованими відповідно до `grouping_column`.

* * *

## Приклади {#examples}

### Агрегування розмірів репозиторіїв за мовою програмування {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

Цей запит повертає об'єкт JSON для кожної мови програмування, у якому імена репозиторіїв і відповідні їм розміри подано як пари "ключ-значення". Ось приклад очікуваного виводу:

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

Такий вивід корисний для візуалізації розподілу розмірів репозиторіїв між різними мовами програмування в межах організації: він дає чіткі й упорядковані дані, згруповані за мовою.
