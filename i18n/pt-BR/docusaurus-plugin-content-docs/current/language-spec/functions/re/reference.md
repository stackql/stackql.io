---
title: Referência de expressões regulares
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Referência de expressões regulares para o StackQL: sintaxe, classes de caracteres, âncoras, quantificadores e grupos para REGEXP_LIKE, REGEXP_REPLACE e REGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
Referência das expressões regulares usadas no StackQL.  

Veja também:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## Sintaxe compatível {#supported-syntax}

O StackQL oferece suporte à seguinte sintaxe de expressões regulares:    

| Expressão | Descrição |
|------------|-------------|
| `X*` | zero ou mais ocorrências de `X` |
| `X+` | uma ou mais ocorrências de `X` | 
| `X?` | zero ou uma ocorrência de `X` |
| `(X)` | corresponde a `X` |
| `X`&vert;`Y` | `X` ou `Y` |
| `^X` | `X` no início da string |
| `X$` | `X` no final da string |
| `.` | Corresponde a qualquer caractere único |
| `\c` | Caractere `c`, em que `c` é um de `\{}()[]`&vert;`*+?.` |
| `\c` | Sequências de escape da linguagem C para `c` em `afnrtv`. Ex.: `\t` ou `\n` |
| `[abc]` | Qualquer caractere único do conjunto `abc` |
| `[^abc]` | Qualquer caractere único que não esteja no conjunto `abc` |
| `[a-z]` | Qualquer caractere único no intervalo `a-z` |
| `[^a-z]` | Qualquer caractere único que não esteja no intervalo `a-z` |  

&nbsp;  
&nbsp;  
Para obter mais informações, consulte [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
