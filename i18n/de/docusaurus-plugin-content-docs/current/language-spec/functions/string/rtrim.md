---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "RTRIM-Funktion in StackQL: Leerzeichen oder einen angegebenen Satz von Zeichen von der rechten Seite eines Strings entfernen."
image: "/img/stackql-featured-image.png"
---
Gibt einen String zurück, der durch Entfernen von Leerzeichen oder beliebigen angegebenen Zeichen von der rechten Seite eines Eingabestrings gebildet wird.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Syntax {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*character_expression*__  
Ein String-Literal oder eine String-Spalte.

__*character*__  
Ein optionales Zeichen, das entfernt wird, wenn es am Ende von *character_expression* gefunden wird.

## Rückgabewert(e) {#return-values}
Gibt einen String zurück.

* * *

## Beispiele {#examples}

### Nachgestellte Leerzeichen aus einer String-Spalte entfernen {#remove-trailing-whitespace-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim)
