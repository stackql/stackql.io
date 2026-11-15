---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の NULLIF 関数：2 つの引数が等しい場合は NULL を返し、それ以外の場合は最初の引数を返します。"
image: "/img/stackql-featured-image.png"
---
2 つの引数を受け取り、両者が等しい場合は `NULL` 値を返し、それ以外の場合は最初の引数を返します。  

> `NULLIF` 関数は `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END` と同等です。

> `NULLIF` は、[`AVG`](/language-spec/functions/aggregate/avg)、[`MAX`](/language-spec/functions/aggregate/max)、[`MIN`](/language-spec/functions/aggregate/min)、[`SUM`](/language-spec/functions/aggregate/sum)、[`COUNT`](/language-spec/functions/aggregate/count) などの集約関数と組み合わせると便利です。結果セットに、`NULL` ではないものの集約の際には `NULL` として扱いたい特殊な値が含まれる場合に役立ちます。

関連項目：  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## 構文 {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expression*__  
`NULL` 値または `NULL` 以外の値に評価される、任意のデータ型の 2 つの式。

> `NULLIF` 関数は、照合関数を定義している引数を左から右へ検索し、その照合関数をすべての文字列比較に使用します。どちらの引数も照合関数を定義していない場合は、`BINARY` 照合が使用されます。

## 戻り値 {#return-values}

2 つの *expression* 引数が同じ値に評価される場合は `NULL` を返し、それ以外の場合は最初の値を返します。

* * *

## 例 {#examples}

### フィールドが指定した値と等しくない行を数える {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE name != 'controller-0';
*/
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif) を参照してください。
