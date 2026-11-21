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
| `X{n}` | exactamente `n` apariciones de `X` |
| `X{n,}` | `n` o más apariciones de `X` |
| `X{n,m}` | entre `n` y `m` apariciones de `X` |
| `(X)` | coincide con `X` |
| `X`&vert;`Y` | `X` o `Y` |
| `^X` | `X` al principio de la cadena |
| `X$` | `X` al final de la cadena |
| `.` | Coincide con cualquier carácter individual, incluido un salto de línea |
| `\c` | Carácter `c`, donde `c` es uno de `\{}()[]`&vert;`*+?.` |
| `\c` | Secuencias de escape del lenguaje C para `c` en `afnrtv`. Por ejemplo: `\t` o `\n` |
| `[abc]` | Cualquier carácter individual del conjunto `abc` |
| `[^abc]` | Cualquier carácter individual que no esté en el conjunto `abc` |
| `[a-z]` | Cualquier carácter individual del rango `a-z` |
| `[^a-z]` | Cualquier carácter individual que no esté en el rango `a-z` |
| `\d`, `\w`, `\s` | Un dígito, un carácter de palabra (`[0-9A-Za-z_]`), un carácter de espacio en blanco |
| `\D`, `\W`, `\S` | Cualquier carácter que no sea un dígito, ni un carácter de palabra, ni un carácter de espacio en blanco |
| `\b` | Un límite de palabra |  

&nbsp;  
&nbsp;  
Los patrones usan la sintaxis de RE2, tal como la implementa el paquete `regexp` de Go.  No se admiten referencias inversas (`\1`) ni aserciones de búsqueda alrededor (lookaround): un patrón que las usa se rechaza con un error.

Una barra invertida en un literal de cadena de StackQL inicia una secuencia de escape (`'\n'` es un salto de línea), de modo que una barra invertida que pertenece a la expresión regular se escribe dos veces: `'\\d+'` coincide con uno o más dígitos y `'[0-9]+\\.[0-9]+'` coincide con un punto literal entre dígitos.

Para ver la sintaxis completa, consulte [Sintaxis de RE2](https://github.com/google/re2/wiki/Syntax).
