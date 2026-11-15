---
title: Referencia de expresiones regulares
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Referencia de expresiones regulares para StackQL: sintaxis, clases de caracteres, anclas, cuantificadores y grupos para REGEXP_LIKE, REGEXP_REPLACE y REGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
Referencia de las expresiones regulares que se usan en StackQL.  

Consulte también:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## Sintaxis admitida {#supported-syntax}

StackQL admite la siguiente sintaxis de expresiones regulares:    

| Expresión | Descripción |
|------------|-------------|
| `X*` | cero o más apariciones de `X` |
| `X+` | una o más apariciones de `X` | 
| `X?` | cero o una aparición de `X` |
| `(X)` | coincide con `X` |
| `X`&vert;`Y` | `X` o `Y` |
| `^X` | `X` al principio de la cadena |
| `X$` | `X` al final de la cadena |
| `.` | Coincide con cualquier carácter individual |
| `\c` | Carácter `c`, donde `c` es uno de `\{}()[]`&vert;`*+?.`
| `\c` | Secuencias de escape del lenguaje C para `c` en `afnrtv`. Por ejemplo: `\t` o `\n` |
| `[abc]` | Cualquier carácter individual del conjunto `abc` |
| `[^abc]` | Cualquier carácter individual que no esté en el conjunto `abc` |
| `[a-z]` | Cualquier carácter individual del rango `a-z` |
| `[^a-z]` | Cualquier carácter individual que no esté en el rango `a-z` |  

&nbsp;  
&nbsp;  
Para obtener más información, consulte [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
