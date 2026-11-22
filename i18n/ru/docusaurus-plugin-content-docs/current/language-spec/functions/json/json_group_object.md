---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Агрегирование пар "ключ-значение" в JSON-объект с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Агрегирует пары "ключ-значение" в JSON-объект с группировкой по указанному столбцу. Функция `JSON_GROUP_OBJECT()` собирает все пары "ключ-значение" из указанных столбцов для каждой группы и объединяет их в один JSON-объект для каждой группы.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Аргументы {#arguments}

__*key_column*__  
Столбец, из которого берутся ключи JSON-объектов.

__*value_column*__  
Столбец, из которого берутся значения JSON-объектов.

__*grouping_column*__  
Столбец, по которому группируются результаты.

## Возвращаемое значение {#return-values}
Возвращает JSON-объект, содержащий все агрегированные пары "ключ-значение" из указанных столбцов, сгруппированные по столбцу `grouping_column`.

* * *

## Примеры {#examples}

### Агрегирование размеров репозиториев по языкам программирования {#aggregate-repository-sizes-by-programming-language}

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

Этот запрос возвращает JSON-объект для каждого языка программирования; каждый объект содержит имена репозиториев и соответствующие им размеры в виде пар "ключ-значение". Пример ожидаемого вывода:

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

Такой вывод удобен для наглядного представления распределения размеров репозиториев по языкам программирования в организации: данные четко упорядочены и сгруппированы по языку.
