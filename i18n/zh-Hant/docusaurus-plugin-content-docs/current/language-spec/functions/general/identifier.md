---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 工作階段變數
  - identifier
description: "StackQL 中的 IDENTIFIER 函式：將存放資料行名稱的工作階段變數解析為資料行參考，讓變數能在投影與 ORDER BY 中指定資料行。"
image: "/img/stackql-featured-image.png"
---
將字串值（通常是工作階段變數）解析為資料行名稱。在單獨的 `$variable` 原本會被讀取為純量值的位置，請使用此函式。適用於 StackQL `v1.0` 及更新版本。

另請參閱：  
[[` SET `]](/language-spec/set) [[ 工作階段變數 ]](/getting-started/session-variables)

* * * 

## 語法 {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## 引數 {#arguments}

__*variable*__  
存放資料行名稱的工作階段變數或字串常值。可接受 `t.name` 這類限定名稱。只接受一個引數，且必須是單一字串值；`'t.' || $column_name` 這類串連、第二個引數或數值都會遭到拒絕，錯誤訊息為 `IDENTIFIER requires one string value`。空的值或包含陳述式的值會遭到拒絕，錯誤訊息為 `invalid identifier value`；未定義的變數則會以「未定義」錯誤失敗。這些檢查全都在向 Provider 發出任何要求之前進行。

## 傳回值 {#return-values}

在剖析後的陳述式中，此函式會被取代為所指名資料行的參考，因此會傳回該資料行的值。名稱會轉換為識別碼，絕不會直接插入 SQL 文字中。產生的資料行參考可用於任何能使用資料行的位置：選取清單、`WHERE`、`GROUP BY` 與 `ORDER BY` 子句、函式引數，以及 CTE 與子查詢內部。

* * *

## 範例 {#examples}

### 依變數指定的資料行進行投影與排序 {#project-and-order-by-a-column-named-in-a-variable}

要傳回的資料行與排序依據的資料行都取自 `column_name`，輸出資料行則依 `output_name` 重新命名。選取清單與 `ORDER BY` 中需要 `IDENTIFIER`，因為在這些位置單獨使用 `$column_name` 會得到字串 `'name'`，而不是資料行。別名則改用雙引號形式。變數解析之後，此陳述式等同於 `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`。

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### 依變數指定的資料行進行篩選 {#filter-on-a-column-named-in-a-variable}

解析後的參考可用於投影，也可用於述詞。此處要篩選的欄位與要比對的值都來自變數：`IDENTIFIER($filter_column)` 會成為資料行 `status`，而 `$filter_value` 則以字串常值 `'RUNNING'` 傳遞。這兩種形式刻意不同，因為前者指名資料行，後者提供值。

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### 以資料表別名限定資料行 {#qualify-the-column-with-a-table-alias}

含點號的值會解析為限定的資料行參考。資料表的別名為 `w`，別名本身取自變數，而 `IDENTIFIER($column_name)` 會成為 `w.name`。當聯結的兩側都有相同的資料行名稱，參考會因此模稜兩可時，應使用此形式。

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### 遭拒絕的值 {#rejected-values}

由於值會成為名稱而非查詢文字，任何無法作為名稱的內容都會在規劃陳述式之前遭到拒絕，且不會有任何要求送達 Provider。下方前兩個陳述式會以 `invalid identifier value` 失敗，第三個則以 `IDENTIFIER requires one string value` 失敗。

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> `FROM` 子句中的 `$variable` 不需 `IDENTIFIER` 即可解析為關聯；別名、DDL 資料行名稱、`INSERT` 資料行清單與 `UPDATE` 目標則使用加上雙引號的 `"$variable"`。
