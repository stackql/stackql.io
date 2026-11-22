---
title: 連結演算子
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の || 連結演算子：2 つ以上の文字列値を 1 つの文字列に結合します。"
image: "/img/stackql-featured-image.png"
---
2 つ以上の文字列値を連結（結合）した結果の文字列を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*character_expression1*__, __*character_expression2*__  
文字列リテラルまたは文字列型の列です。

## 戻り値 {#return-values}

文字列を返します。

:::note

StackQL `v1.0` 以降、パーサーは `||` を連結演算子として扱い、比較演算子より強く結合します。そのため、`WHERE id = prefix || suffix` は `id` を結合後の文字列と比較します。それより前のリリースでは、`||` をブール値の `OR` として字句解析していました。ブール値の論理和には `OR` を使用してください。[セッション変数](/getting-started/session-variables)も連結できます：`$prefix || '-' || $suffix`。

:::

* * *

## 例 {#examples}

### 2 つのフィールドを連結する {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

詳細については、[https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html) を参照してください。
