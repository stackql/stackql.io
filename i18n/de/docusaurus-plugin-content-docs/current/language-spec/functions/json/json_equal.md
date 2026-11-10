---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Zwei JSON-Zeichenketten mit SQL auf Äquivalenz vergleichen.
image: "/img/stackql-featured-image.png"
---
Vergleicht zwei JSON-Zeichenketten und gibt true zurück, wenn sie äquivalent sind, andernfalls false.

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Datentypen ]](/language-spec/data-types)

* * * 

:::tip 

Die Funktion `json_equal` ist eine benutzerdefinierte StackQL-Erweiterungsfunktion. Sie unterstützt den Vergleich von JSON-Objekten und -Arrays und behandelt Objekte mit Schlüsseln in unterschiedlicher Reihenfolge gemäß der JSON-Spezifikation als äquivalent. Die Funktion `json_equal` ist in StackQL ab Version `0.5.707` verfügbar und wird nur mit dem standardmäßigen eingebetteten In-Memory-SQL-Backend von StackQL unterstützt.

:::

## Syntax {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Argumente {#arguments}

__*json1*__  
Eine JSON-Zeichenkette, die ein Objekt oder Array darstellt.

__*json2*__  
Eine weitere JSON-Zeichenkette, die mit der ersten verglichen wird.

> Beide Argumente müssen gültige JSON-Zeichenketten sein.

## Rückgabewert(e) {#return-values}
Ein ganzzahliger Wert: 1, wenn die JSON-Zeichenketten äquivalent sind, 0, wenn nicht.

* * *

## Beispiele {#examples}

### JSON-Objekte vergleichen {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### JSON-Arrays vergleichen {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

Weitere Informationen finden Sie unter [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
