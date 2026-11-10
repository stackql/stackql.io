---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "IIF-Funktion in StackQL: gibt je nachdem, ob eine Bedingung wahr ist, einen von zwei Werten zurück, ein Inline-Ausdruck der Form if-then-else."
image: "/img/stackql-featured-image.png"
---
Gibt einen Wert auf Grundlage der Auswertung eines Eingabeausdrucks zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> Die Funktion `iif()` ist logisch äquivalent zum `CASE`-Ausdruck `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Argumente {#arguments}

__*expression*__  
Ein boolescher Ausdruck, der ausgewertet wird.

:::tip 

Das *expression*-Argument kann eine verschachtelte Funktion wie `JSON_EXTRACT` sein.

:::

__*result_if_true*__  
Das Ergebnis, das zurückgegeben wird, wenn *expression* zu `true` ausgewertet wird.

__*result_if_false*__  
Das Ergebnis, das zurückgegeben wird, wenn *expression* zu `false` ausgewertet wird.

## Rückgabewert(e) {#return-values}
Der Datentyp des *result_if_true*- oder *result_if_false*-Arguments.

* * *

## Beispiele {#examples}

### Datenträgergrößen von Compute-Engine-Instanzen kategorisieren {#catgorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(json_extract(disks, '$[0].diskSizeGb') > 9, "> 10GB", "<= 10GB") as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
