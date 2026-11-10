---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion JSON_TYPE in StackQL: der Typ (object, array, text, integer, real, true, false oder null) des äußersten Elements eines JSON-Werts oder Pfads darin."
image: "/img/stackql-featured-image.png"
---
Gibt den Datentyp des äußersten Elements eines eingegebenen JSON-Objekts oder -Arrays und Pfads zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Datentypen ]](/language-spec/data-types)

* * * 

:::tip 

Verwenden Sie die Funktion [**DESCRIBE**](/language-spec/describe), um die Datentypen `array` oder `object` zu finden, die mit den JSON-Funktionen von StackQL verwendet werden können.

:::

:::note 

Die Funktion `json_type` ist als Funktion `json_typeof` implementiert, wenn Sie stackql mit einem `postgres`-SQL-Backend verwenden

:::

## Syntax {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*json_array_or_object*__  
Ein JSON-Array oder -Objekt.

__*json_path*__  
Der JSON-Pfad zu einem Array oder Objekt innerhalb von *json_array_or_object*.  Siehe [jsonpath.com](https://jsonpath.com/).

> Wenn *json_path* in *json_array_or_object* nicht vorhanden ist, gibt die Funktion `NULL` zurück.

> Die Funktion `json_type()` löst einen Fehler aus, wenn eines ihrer Argumente nicht wohlgeformt ist.

## Rückgabewert(e) {#return-values}
Eine Zeichenkette, die den ermittelten Datentyp darstellt.

> Zu den Datentypen gehören `null`, `true`, `false`, `integer`, `real`, `text`, `array` oder `object`.

* * *

## Beispiele {#examples}

### Den Datentyp eines in einem JSON-Objekt oder -Array verschachtelten Felds zurückgeben {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
