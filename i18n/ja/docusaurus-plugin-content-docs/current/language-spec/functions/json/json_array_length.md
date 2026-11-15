---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の JSON_ARRAY_LENGTH 関数：JSON 値のトップレベルまたはその中のパスにある JSON 配列の要素数を返します。"
image: "/img/stackql-featured-image.png"
---
JSON 配列の要素数を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ データ型 ]](/language-spec/data-types)

* * * 

:::tip 

StackQL の JSON 関数で使用できる `array` または `object` データ型を見つけるには、[**DESCRIBE**](/language-spec/describe) 文を使用します。ネストされたオブジェクトや配列要素のデータ型を判別するには、[**JSON_TYPE**](/language-spec/functions/json/json_type) 関数を使用します。

:::

## 構文 {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*json_array_or_object*__  
JSON 配列またはオブジェクト。

> *json_array_or_object* が（配列ではなく）JSON オブジェクトで、*json_path* が指定されていない場合は、`0` が返されます。

__*json_path*__  
オブジェクト内に含まれる配列への JSON パス（省略可能）。[jsonpath.com](https://jsonpath.com/) を参照してください。

> *json_path* が指定された場合、`json_array_length` 関数は *json_array_or_object* 内でそのパスを特定し、配列の長さを返します。*json_path* が（配列ではなく）オブジェクトの場合は `0` を返します。*json_path* が *json_array_or_object* 内で見つからない場合は、`NULL` が返されます。

> *json_array_or_object* または *json_path* が JSON オブジェクトでない場合、または整形式でない場合は、エラーが発生します。

## 戻り値 {#return-values}
整数を返します。

* * *

## 例 {#examples}

### Google Compute Engine インスタンスにアタッチされたディスクの数を返す {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Google Compute Engine インスタンスにアタッチされたネットワークインターフェイスの数を返す {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Google Storage バケットに関連付けられた ACL の数を返す {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### サービスアカウントに関連付けられたスコープの数を返す {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen) を参照してください。
