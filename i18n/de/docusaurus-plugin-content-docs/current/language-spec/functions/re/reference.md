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
| `X{n}` | genau `n` Vorkommen von `X` |
| `X{n,}` | `n` oder mehr Vorkommen von `X` |
| `X{n,m}` | zwischen `n` und `m` Vorkommen von `X` |
| `(X)` | entspricht `X` |
| `X`&vert;`Y` | `X` oder `Y` |
| `^X` | `X` am Anfang des Strings |
| `X$` | `X` am Ende des Strings |
| `.` | Entspricht einem beliebigen einzelnen Zeichen, einschließlich eines Zeilenumbruchs |
| `\c` | Zeichen `c`, wobei `c` eines von `\{}()[]`&vert;`*+?.` ist |
| `\c` | Escape-Sequenzen der Sprache C für `c` in `afnrtv`, z. B. `\t` oder `\n` |
| `[abc]` | Ein beliebiges einzelnes Zeichen aus der Menge `abc` |
| `[^abc]` | Ein beliebiges einzelnes Zeichen, das nicht in der Menge `abc` enthalten ist |
| `[a-z]` | Ein beliebiges einzelnes Zeichen im Bereich `a-z` |
| `[^a-z]` | Ein beliebiges einzelnes Zeichen außerhalb des Bereichs `a-z` |
| `\d`, `\w`, `\s` | Eine Ziffer, ein Wortzeichen (`[0-9A-Za-z_]`), ein Whitespace-Zeichen |
| `\D`, `\W`, `\S` | Ein beliebiges Zeichen, das keine Ziffer, kein Wortzeichen und kein Whitespace-Zeichen ist |
| `\b` | Eine Wortgrenze |  

&nbsp;  
&nbsp;  
Muster verwenden die RE2-Syntax, wie sie das Go-Paket `regexp` implementiert.  Rückverweise (`\1`) und Lookaround-Assertionen werden nicht unterstützt: Ein Muster, das sie verwendet, wird mit einem Fehler abgelehnt.

Ein Backslash in einem StackQL-String-Literal leitet eine Escape-Sequenz ein (`'\n'` ist ein Zeilenumbruch), daher wird ein Backslash, der zum regulären Ausdruck gehört, doppelt geschrieben: `'\\d+'` entspricht einer oder mehreren Ziffern und `'[0-9]+\\.[0-9]+'` entspricht einem literalen Punkt zwischen Ziffern.

Die vollständige Syntax finden Sie unter [RE2-Syntax](https://github.com/google/re2/wiki/Syntax).
