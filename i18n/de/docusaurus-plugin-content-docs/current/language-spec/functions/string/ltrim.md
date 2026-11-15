---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "LTRIM-Funktion in StackQL: Leerzeichen oder beliebige Zeichen aus einer angegebenen Zeichenmenge von der linken Seite eines Strings entfernen."
image: "/img/stackql-featured-image.png"
---
Gibt einen String zurück, der durch Entfernen von Leerzeichen oder beliebigen Zeichen aus einer angegebenen Zeichenmenge von der linken Seite eines Eingabestrings gebildet wird.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Syntax {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*character_expression*__  
Ein String-Literal oder eine String-Spalte.

__*character*__  
Ein optionaler String aus Zeichen: Jedes darin enthaltene Zeichen wird am Anfang von *character_expression* entfernt.  Ohne Angabe werden Leerzeichen entfernt (keine Tabulatoren oder Zeilenumbrüche).

## Rückgabewert(e) {#return-values}
Gibt einen String zurück.

* * *

## Beispiele {#examples}

### Führende Leerzeichen aus einer String-Spalte entfernen {#remove-leading-spaces-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
