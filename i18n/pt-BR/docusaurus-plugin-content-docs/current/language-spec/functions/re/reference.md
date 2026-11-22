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
| `X{n}` | exatamente `n` ocorrências de `X` |
| `X{n,}` | `n` ou mais ocorrências de `X` |
| `X{n,m}` | entre `n` e `m` ocorrências de `X` |
| `(X)` | corresponde a `X` |
| `X`&vert;`Y` | `X` ou `Y` |
| `^X` | `X` no início da string |
| `X$` | `X` no final da string |
| `.` | Corresponde a qualquer caractere único, inclusive o caractere de nova linha |
| `\c` | Caractere `c`, em que `c` é um de `\{}()[]`&vert;`*+?.` |
| `\c` | Sequências de escape da linguagem C para `c` em `afnrtv`. Ex.: `\t` ou `\n` |
| `[abc]` | Qualquer caractere único do conjunto `abc` |
| `[^abc]` | Qualquer caractere único que não esteja no conjunto `abc` |
| `[a-z]` | Qualquer caractere único no intervalo `a-z` |
| `[^a-z]` | Qualquer caractere único que não esteja no intervalo `a-z` |
| `\d`, `\w`, `\s` | Um dígito, um caractere de palavra (`[0-9A-Za-z_]`), um caractere de espaço em branco |
| `\D`, `\W`, `\S` | Qualquer caractere que não seja um dígito, que não seja um caractere de palavra, que não seja um caractere de espaço em branco |
| `\b` | Um limite de palavra |  

&nbsp;  
&nbsp;  
Os padrões usam a sintaxe RE2, conforme implementada pelo pacote `regexp` do Go.  Não há suporte a referências retroativas (`\1`) nem a asserções de lookaround: um padrão que as usa é rejeitado com um erro.

Uma barra invertida em uma string literal do StackQL inicia uma sequência de escape (`'\n'` é o caractere de nova linha); portanto, uma barra invertida que pertence à expressão regular é escrita duas vezes: `'\\d+'` corresponde a um ou mais dígitos e `'[0-9]+\\.[0-9]+'` corresponde a um ponto literal entre dígitos.

Para ver a sintaxe completa, consulte [sintaxe do RE2](https://github.com/google/re2/wiki/Syntax).
