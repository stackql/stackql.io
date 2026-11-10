---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "TRIM-Funktion in StackQL: Leerzeichen oder einen angegebenen Satz von Zeichen von beiden Enden eines Strings entfernen."
image: "/img/stackql-featured-image.png"
---
Gibt einen String zurück, der durch Entfernen von Leerzeichen oder beliebigen angegebenen Zeichen von der linken und rechten Seite eines Eingabestrings gebildet wird.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Syntax {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*character_expression*__  
Ein String-Literal oder eine String-Spalte.

__*character*__  
Ein optionales Zeichen, das entfernt wird, wenn es am Anfang oder Ende von *character_expression* gefunden wird.

## Rückgabewert(e) {#return-values}
Gibt einen String zurück.

* * *

## Beispiele {#examples}

### Führende und nachgestellte Leerzeichen aus einer String-Spalte entfernen {#remove-leading-and-trailing-whitespace-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
