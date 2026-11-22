---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Видалення елементів із документа JSON із зазначенням шляхів за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Видаляє вказані елементи з документа JSON за виразами шляхів. Якщо вказаного шляху в документі немає, функція його ігнорує. Послідовна обробка шляхів може впливати на структуру документа для подальших видалень.

Див. також:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*json_document*__  
Документ JSON, з якого потрібно видалити елементи.

__*path1, path2, ...*__  
Один або кілька виразів шляхів, що вказують на елементи документа JSON, які потрібно видалити.

## Значення, що повертається {#return-values}
Повертає копію документа JSON без указаних елементів. Якщо шляхи не вказано, повертає вхідний JSON, переформатований без зайвих пробільних символів.

* * *

## Приклади {#examples}

### Видалення елемента з масиву JSON {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Вивід: `'[0,1,3,4]'`

### Видалення кількох елементів із масиву JSON {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Вивід: `'[1,3,4]'`

### Видалення останнього й першого елементів за допомогою від'ємного індексу {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Вивід: `'[1,2,3]'`

### Видалення відсутнього шляху {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Вивід: `'{"x":25,"y":42}'`

### Видалення наявного ключа з об'єкта JSON {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Вивід: `'{"x":25}'`

### Граничний випадок: видалення за кореневим шляхом {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Вивід: `NULL`

Ці приклади показують, як за допомогою функції `json_remove()` динамічно обробляти дані JSON у різних сценаріях: від простих видалень до обробки граничних випадків під час змінення структур даних.
