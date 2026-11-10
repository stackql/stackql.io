---
title: Regex-Referenz
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Referenz für reguläre Ausdrücke in StackQL: Syntax, Zeichenklassen, Anker, Quantoren und Gruppen für REGEXP_LIKE, REGEXP_REPLACE und REGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
Referenz für reguläre Ausdrücke, die in StackQL verwendet werden.  

Siehe auch:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## Unterstützte Syntax {#supported-syntax}

Die folgende Syntax für reguläre Ausdrücke wird in StackQL unterstützt:    

| Ausdruck | Beschreibung |
|------------|-------------|
| `X*` | null oder mehr Vorkommen von `X` |
| `X+` | ein oder mehr Vorkommen von `X` | 
| `X?` | null oder ein Vorkommen von `X` |
| `(X)` | entspricht `X` |
| `X`&vert;`Y` | `X` oder `Y` |
| `^X` | `X` am Anfang des Strings |
| `X$` | `X` am Ende des Strings |
| `.` | Entspricht einem beliebigen einzelnen Zeichen |
| `\c` | Zeichen `c`, wobei `c` eines von `\{}()[]`&vert;`*+?.` ist
| `\c` | Escape-Sequenzen der Sprache C für `c` in `afnrtv`, z. B. `\t` oder `\n` |
| `[abc]` | Ein beliebiges einzelnes Zeichen aus der Menge `abc` |
| `[^abc]` | Ein beliebiges einzelnes Zeichen, das nicht in der Menge `abc` enthalten ist |
| `[a-z]` | Ein beliebiges einzelnes Zeichen im Bereich `a-z` |
| `[^a-z]` | Ein beliebiges einzelnes Zeichen außerhalb des Bereichs `a-z` |  

&nbsp;  
&nbsp;  
Weitere Informationen finden Sie unter [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
