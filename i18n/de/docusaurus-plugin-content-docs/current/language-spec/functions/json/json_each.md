---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Tabellenwertfunktion JSON_EACH in StackQL: ein JSON-Array oder -Objekt in eine Zeile pro Element oder Member auflösen, zum Verknüpfen und Filtern verschachtelter API-Antworten."
image: "/img/stackql-featured-image.png"
---
Tabellenwertfunktion, die eine Tabelle zurückgibt, die aus einer Zeile pro Array-Element oder Objektmember besteht.

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Datentypen ]](/language-spec/data-types)

* * * 

:::tip 

Verwenden Sie die Funktion [**DESCRIBE**](/language-spec/describe), um `array`-Datentypen zu finden, die mit der Funktion `json_each` verwendet werden können.

:::

:::note 

Die Funktion `json_each` ist als Funktion `json_array_elements_text` implementiert, wenn Sie stackql mit einem `postgres`-SQL-Backend verwenden

:::

## Syntax {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## Argumente {#arguments}

__*json_array_or_object*__  
Ein JSON-Array oder -Objekt.

__*json_path*__  
Der JSON-Pfad zu einem Array oder Objekt, das in *json_array_or_object* enthalten ist.  Siehe [jsonpath.com](https://jsonpath.com/).

> Wenn *json_path* in *json_array_or_object* nicht vorhanden ist, gibt die Funktion `NULL` zurück.

> Die Funktion `json_type()` löst einen Fehler aus, wenn eines ihrer Argumente nicht wohlgeformt ist.

## Rückgabewert(e) {#return-values}
Eine Tabelle, die aus einer Zeile pro Array-Element oder Objektmember besteht.

* * *

## Beispiele {#examples}

### Ein Array von Zeichenketten in eine mit zugehörigen Feldern verknüpfte Tabelle entschachteln {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

zum Kontext:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

gibt zurück...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

Die folgende `stackql`-Abfrage verwendet die Funktion `json_each`, um das Array `members` in eine Tabelle zu entschachteln, die mit den Feldern `role` und optional `condition` verknüpft ist.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

würde zurückgeben...

```
|--------------------------------------|-------------------------------------------------------|                                                                                                     
|                 role                 |                     member                            |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com           |                                                                                                     
|--------------------------------------|---------------------------------------------- --------|                                                                                                     
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com    |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com        |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| ...                                  | ...                                                   |                                                                                                     
|--------------------------------------|-------------------------------------------------------|    
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
