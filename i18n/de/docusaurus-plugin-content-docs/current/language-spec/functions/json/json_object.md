---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion JSON_OBJECT in StackQL: erstellt ein JSON-Objekt aus Schlüssel- und Wertargumenten, zum Beispiel als Request-Body für INSERT und UPDATE."
image: "/img/stackql-featured-image.png"
---
Gibt ein Objekt zurück, das aus den übergebenen Argumenten erstellt wird.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Datentypen ]](/language-spec/data-types)

* * * 

:::tip 

Verwenden Sie die Funktion [**DESCRIBE**](/language-spec/describe), um die Datentypen `array` oder `object` zu finden, die mit den JSON-Funktionen von StackQL verwendet werden können.

:::

:::note 

Die Funktion `json_object` ist als `jsonb_build_object` implementiert, wenn Sie stackql mit einem `postgres`-SQL-Backend verwenden.

:::

## Syntax {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*key1, key2, ...*__  
Die Bezeichnungen für das JSON-Objekt.

__*value1, value2, ...*__  
Die Werte, die den Bezeichnungen im JSON-Objekt entsprechen.

## Rückgabewert(e) {#return-values}
Eine Zeichenkette, die das wohlgeformte JSON-Objekt darstellt.

* * *

## Beispiele {#examples}

### Den Datentyp eines in einem JSON-Objekt oder -Array verschachtelten Felds zurückgeben {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

Weitere Informationen finden Sie unter [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
