---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の IIF 関数：条件が真かどうかに応じて 2 つの値のいずれかを返す、インラインの if-then-else 式です。"
image: "/img/stackql-featured-image.png"
---
入力式の評価結果に基づいて値を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> `iif()` 関数は、`CASE` 式 `CASE WHEN expression THEN result_if_true ELSE result_if_false END` と論理的に同等です。

## 引数 {#arguments}

__*expression*__  
評価するブール式。

:::tip 

*expression* 引数には、`JSON_EXTRACT` のような入れ子の関数を指定できます。

:::

__*result_if_true*__  
*expression* が `true` と評価された場合に返す結果。

__*result_if_false*__  
*expression* が `false` と評価された場合に返す結果。

## 戻り値 {#return-values}
*result_if_true* 引数または *result_if_false* 引数のデータ型。

* * *

## 例 {#examples}

### Compute Engine インスタンスのディスクサイズを分類する {#categorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER) > 10, '> 10GB', '<= 10GB') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif) を参照してください。
