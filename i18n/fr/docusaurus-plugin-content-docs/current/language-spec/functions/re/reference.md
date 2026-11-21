---
title: Référence des expressions régulières
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Référence des expressions régulières pour StackQL : syntaxe, classes de caractères, ancres, quantificateurs et groupes pour REGEXP_LIKE, REGEXP_REPLACE et REGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
Référence des expressions régulières utilisées dans StackQL.  

Voir aussi :  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## Syntaxe prise en charge {#supported-syntax}

La syntaxe d'expressions régulières suivante est prise en charge dans StackQL :    

| Expression | Description |
|------------|-------------|
| `X*` | zéro ou plusieurs occurrences de `X` |
| `X+` | une ou plusieurs occurrences de `X` | 
| `X?` | zéro ou une occurrence de `X` |
| `X{n}` | exactement `n` occurrences de `X` |
| `X{n,}` | `n` occurrences ou plus de `X` |
| `X{n,m}` | entre `n` et `m` occurrences de `X` |
| `(X)` | correspond à `X` |
| `X`&vert;`Y` | `X` ou `Y` |
| `^X` | `X` au début de la chaîne de caractères |
| `X$` | `X` à la fin de la chaîne |
| `.` | Correspond à n'importe quel caractère unique, y compris un saut de ligne |
| `\c` | Caractère `c`, où `c` est l'un de `\{}()[]`&vert;`*+?.` |
| `\c` | Échappements du langage C pour `c` dans `afnrtv`, par exemple `\t` ou `\n` |
| `[abc]` | N'importe quel caractère unique de l'ensemble `abc` |
| `[^abc]` | N'importe quel caractère unique absent de l'ensemble `abc` |
| `[a-z]` | N'importe quel caractère unique de la plage `a-z` |
| `[^a-z]` | N'importe quel caractère unique hors de la plage `a-z` |
| `\d`, `\w`, `\s` | Un chiffre, un caractère de mot (`[0-9A-Za-z_]`), un caractère d'espace blanc |
| `\D`, `\W`, `\S` | N'importe quel caractère qui n'est ni un chiffre, ni un caractère de mot, ni un caractère d'espace blanc |
| `\b` | Une limite de mot |  

&nbsp;  
&nbsp;  
Les motifs utilisent la syntaxe RE2, telle qu'implémentée par le paquet `regexp` de Go.  Les références arrière (`\1`) et les assertions avant et arrière (lookaround) ne sont pas prises en charge : un motif qui les utilise est rejeté avec une erreur.

Dans un littéral de chaîne StackQL, une barre oblique inverse introduit une séquence d'échappement (`'\n'` est un saut de ligne) ; une barre oblique inverse qui appartient à l'expression régulière s'écrit donc deux fois : `'\\d+'` correspond à un ou plusieurs chiffres et `'[0-9]+\\.[0-9]+'` correspond à un point littéral entre des chiffres.

Pour la syntaxe complète, consultez [Syntaxe RE2](https://github.com/google/re2/wiki/Syntax).
