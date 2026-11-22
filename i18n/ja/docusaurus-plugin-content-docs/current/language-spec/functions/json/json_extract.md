---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の JSON_EXTRACT 関数：パスを指定して JSON オブジェクトまたは配列から 1 つ以上の値を取り出す、ネストした API レスポンスのフィールドを平坦化するための主要な手段。"
image: "/img/stackql-featured-image.png"
---
整形式の JSON オブジェクトまたは配列から 1 つ以上の値を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ データ型 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 文を使用すると、StackQL の JSON 関数で使用できる `array` または `object` データ型を確認できます。ネストしたオブジェクトや配列要素のデータ型を調べるには、[**JSON_TYPE**](/language-spec/functions/json/json_type) 関数を使用します。

:::

:::note 

`postgres` SQL バックエンドで stackql を使用している場合、`json_extract` 関数は `json_extract_path_text` 関数として実装されます。パスは `.` で分割され、先頭の `$` は取り除かれるため、`'$.a.b'` はキー `'a'` と `'b'` になります。`$[0]` などの配列添字はオブジェクトのキーとしてそのまま渡され、このバックエンドでは配列要素を選択しません。

:::


## 構文 {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*json_array_or_object*__  
JSON 配列またはオブジェクト。

__*json_path*__  
*json_array_or_object* に含まれる配列またはオブジェクトへの JSON パス。[jsonpath.com](https://jsonpath.com/) を参照してください。

> *json_path* を 1 つ指定した場合は、解決されたパスのデータ型に対応するスカラー値が返されます。

> 複数の *json_path* 引数を指定した場合、結果は各パスで解決されたオブジェクトで構成される配列になります。

> *json_path* 引数に `'$'` を指定すると、*json_array_or_object* 引数が表す列に対応する JSON オブジェクト全体が返されます。

> 注記：[`json-viewer`](https://aarrange.com/json-viewer) などのツールを使用して、JSON Path 式を作成およびテストできます。

## 戻り値 {#return-values}
入力として指定した *json_array_or_object* と *json_path* に応じて、文字列、整数、オブジェクト、配列、ブール値、または null。

* * *

## 例 {#examples}

### 各 Google Compute Engine インスタンスに関連付けられたディスクオブジェクトを返す {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 各 Google Compute Engine インスタンスにアタッチされた最初のディスクのデバイス名とサイズを返す {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 各 Google Compute Engine インスタンスにアタッチされた最初の NIC のネットワーク階層を返す {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 各 Google Compute Engine インスタンスに関連付けられた最初のサービスアカウントのスコープの一覧を返す {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

詳細については、[https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex) を参照してください。
