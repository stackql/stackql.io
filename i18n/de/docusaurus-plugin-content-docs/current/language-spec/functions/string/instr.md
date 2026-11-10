---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "INSTR-Funktion in StackQL: die 1-basierte Position des ersten Vorkommens eines Strings in einem anderen String, oder 0, wenn er nicht gefunden wird."
image: "/img/stackql-featured-image.png"
---
Gibt die Zeichenposition des ersten Vorkommens eines Strings innerhalb eines anderen Strings zurück; wird der gesuchte String nicht gefunden, wird 0 zurückgegeben.

Siehe auch:  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*string*__  
Der Quellstring, der durchsucht wird.

__*searchstring*__  
Der Teilstring, nach dem gesucht wird.

> Wenn *string* und *searchstring* beide BLOBs sind, gibt `instr(<string>, <searchstring>)` die Anzahl der Bytes vor dem ersten Vorkommen von *searchstring* plus eins zurück, oder 0, wenn *searchstring* nirgends innerhalb von *string* vorkommt. Wenn beide Argumente nicht `NULL` und keine BLOBs sind, werden beide als Strings interpretiert. 

## Rückgabewert(e) {#return-values}
Gibt eine Ganzzahl zurück.

> Wenn der Wert von *string* oder der von *searchstring* `NULL` ist, ist das Ergebnis `NULL`.

* * *

## Beispiele {#examples}

### Ein Feld nach einem String durchsuchen {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurence of the term 'worker' in the 'name' column
*/
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
