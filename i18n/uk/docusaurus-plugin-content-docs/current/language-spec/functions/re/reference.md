---
title: Довідник із регулярних виразів
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Довідник із регулярних виразів для StackQL: синтаксис, класи символів, якорі, квантифікатори й групи для REGEXP_LIKE, REGEXP_REPLACE і REGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
Довідник із регулярних виразів, які використовуються в StackQL.  

Див. також:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## Підтримуваний синтаксис {#supported-syntax}

StackQL підтримує такий синтаксис регулярних виразів:    

| Вираз | Опис |
|------------|-------------|
| `X*` | нуль або більше входжень `X` |
| `X+` | одне або більше входжень `X` | 
| `X?` | нуль або одне входження `X` |
| `(X)` | збіг із `X` |
| `X`&vert;`Y` | `X` або `Y` |
| `^X` | `X` на початку рядка |
| `X$` | `X` у кінці рядка |
| `.` | Збіг із будь-яким одним символом |
| `\c` | Символ `c`, де `c` - один із символів `\{}()[]`&vert;`*+?.` |
| `\c` | Escape-послідовності мови C для `c` з набору `afnrtv`. Напр.: `\t` або `\n` |
| `[abc]` | Будь-який один символ із набору `abc` |
| `[^abc]` | Будь-який один символ, якого немає в наборі `abc` |
| `[a-z]` | Будь-який один символ із діапазону `a-z` |
| `[^a-z]` | Будь-який один символ поза діапазоном `a-z` |  

&nbsp;  
&nbsp;  
Докладніше див. [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
