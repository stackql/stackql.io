---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "REPLACE-Funktion in StackQL: jedes Vorkommen eines Teilstrings innerhalb eines Strings durch einen anderen String ersetzen."
image: "/img/stackql-featured-image.png"
---
Gibt einen String zurück, der durch Ersetzen jedes Vorkommens eines Strings durch einen anderen String gebildet wird.

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*character_expression*__  
Ein String-Literal oder eine String-Spalte, in der die Ersetzung durchgeführt werden soll.

__*pattern*__  
Der Teilstring, der im ursprünglichen String gesucht werden soll.

> Wenn *pattern* ein leerer String ist oder in *character_expression* nicht gefunden wird, wird *character_expression* unverändert zurückgegeben. 

__*replacement*__  
Der Ersetzungsstring.

> Wenn *replacement* ursprünglich kein String ist, wird es vor der Verarbeitung in einen UTF-8-String umgewandelt.

> Für Vergleiche wird die Sortierreihenfolge `BINARY` verwendet.

## Rückgabewert(e) {#return-values}
Gibt einen String zurück.

* * *

## Beispiele {#examples}

### Ein String-Muster in einer Spalte ersetzen {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
