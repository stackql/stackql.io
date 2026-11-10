---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion JSON_ARRAY_LENGTH in StackQL: die Anzahl der Elemente eines JSON-Arrays, auf oberster Ebene oder unter einem Pfad innerhalb eines JSON-Werts."
image: "/img/stackql-featured-image.png"
---
Gibt die Anzahl der Elemente in einem JSON-Array zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Datentypen ]](/language-spec/data-types)

* * * 

:::tip 

Verwenden Sie die Funktion [**DESCRIBE**](/language-spec/describe), um die Datentypen `array` oder `object` zu finden, die mit den JSON-Funktionen von StackQL verwendet werden können. Verwenden Sie die Funktion [**JSON_TYPE**](/language-spec/functions/json/json_type), um die Datentypen verschachtelter Objekte oder Array-Elemente zu ermitteln.

:::

## Syntax {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*json_array_or_object*__  
Ein JSON-Array oder -Objekt.

> Wenn *json_array_or_object* ein JSON-Objekt (kein Array) ist und *json_path* nicht angegeben ist, wird `0` zurückgegeben.

__*json_path*__  
Der optionale JSON-Pfad zu einem Array, das in einem Objekt enthalten ist.  Siehe [jsonpath.com](https://jsonpath.com/).

> Wenn *json_path* angegeben ist, sucht die Funktion `json_array_length` den Pfad in *json_array_or_object* und gibt die Länge des Arrays zurück, oder `0`, wenn *json_path* ein Objekt (kein Array) ist.  Wenn *json_path* in *json_array_or_object* nicht gefunden werden kann, wird `NULL` zurückgegeben.

> Wenn *json_array_or_object* oder *json_path* keine JSON-Objekte oder nicht wohlgeformt sind, wird ein Fehler ausgelöst.

## Rückgabewert(e) {#return-values}
Gibt eine Ganzzahl zurück.

* * *

## Beispiele {#examples}

### Die Anzahl der an Google Compute Engine-Instanzen angehängten Laufwerke zurückgeben {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Die Anzahl der an Google Compute Engine-Instanzen angehängten Netzwerkschnittstellen zurückgeben {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Die Anzahl der Google Storage-Buckets zugeordneten ACLs zurückgeben {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Die Anzahl der einem Dienstkonto zugeordneten Scopes zurückgeben {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
