---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion JSON_EXTRACT in StackQL: einen oder mehrere Werte per Pfad aus einem JSON-Objekt oder -Array lesen, das Hauptwerkzeug zum Verflachen verschachtelter API-Antwortfelder."
image: "/img/stackql-featured-image.png"
---
Gibt einen oder mehrere Werte aus einem wohlgeformten JSON-Objekt oder -Array zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Datentypen ]](/language-spec/data-types)

* * * 

:::tip 

Verwenden Sie die Funktion [**DESCRIBE**](/language-spec/describe), um die Datentypen `array` oder `object` zu finden, die mit den JSON-Funktionen von StackQL verwendet werden können. Verwenden Sie die Funktion [**JSON_TYPE**](/language-spec/functions/json/json_type), um die Datentypen verschachtelter Objekte oder Array-Elemente zu ermitteln.

:::

:::note 

Die Funktion `json_extract` ist als Funktion `json_extract_path_text` implementiert, wenn Sie stackql mit einem `postgres`-SQL-Backend verwenden

:::


## Syntax {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*json_array_or_object*__  
Ein JSON-Array oder -Objekt.

__*json_path*__  
Der JSON-Pfad zu einem Array oder Objekt, das in *json_array_or_object* enthalten ist.  Siehe [jsonpath.com](https://jsonpath.com/).

> Wenn ein einzelner *json_path* angegeben wird, wird ein skalares Ergebnis zurückgegeben, das dem Datentyp des aufgelösten Pfads entspricht.

> Wenn mehrere *json_path*-Argumente angegeben werden, ist das Ergebnis ein Array aus den Objekten, die durch die jeweiligen Pfade aufgelöst werden.

> Die Angabe von `'$'` als Argument *json_path* gibt das gesamte JSON-Objekt zurück, das der durch das Argument *json_array_or_object* dargestellten Spalte entspricht.


## Rückgabewert(e) {#return-values}
Eine Zeichenkette, eine Ganzzahl, ein Objekt, ein Array, ein boolescher Wert oder null, abhängig von den als Eingabe übergebenen *json_array_or_object* und *json_path*.

* * *

## Beispiele {#examples}

### Die jeder Google Compute Engine-Instanz zugeordneten Laufwerksobjekte zurückgeben {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Den Gerätenamen und die Größe des ersten an jede Google Compute Engine-Instanz angehängten Laufwerks zurückgeben {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Die Netzwerkstufe der ersten an jede Google Compute Engine-Instanz angehängten NIC zurückgeben {#return-the-network-tier-of-first-nic-attached-to-each-google-compute-engine-instances}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Eine Liste der Scopes zurückgeben, die dem ersten Dienstkonto jeder Google Compute Engine-Instanz zugeordnet sind {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
