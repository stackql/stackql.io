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
| `(X)` | correspond à `X` |
| `X`&vert;`Y` | `X` ou `Y` |
| `^X` | `X` au début de la chaîne de caractères |
| `X$` | `X` à la fin de la chaîne |
| `.` | Correspond à n'importe quel caractère unique |
| `\c` | Caractère `c`, où `c` est l'un de `\{}()[]`&vert;`*+?.`
| `\c` | Échappements du langage C pour `c` dans `afnrtv`, par exemple `\t` ou `\n` |
| `[abc]` | N'importe quel caractère unique de l'ensemble `abc` |
| `[^abc]` | N'importe quel caractère unique absent de l'ensemble `abc` |
| `[a-z]` | N'importe quel caractère unique de la plage `a-z` |
| `[^a-z]` | N'importe quel caractère unique hors de la plage `a-z` |  

&nbsp;  
&nbsp;  
Pour plus d'informations, consultez [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
