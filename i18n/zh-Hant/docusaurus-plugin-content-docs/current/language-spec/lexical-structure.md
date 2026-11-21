---
title: 語彙結構
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 的語彙結構：剖析器如何將陳述式、識別碼、引號、常值、註解和空白字元切分為語彙基元。"
image: "/img/stackql-featured-image.png"
---

另請參閱：  
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy) [[ 關鍵字 ]](/language-spec/keywords) [[ 資料型別 ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


StackQL 的語彙結構與文法以 MySQL 的語言實作（ANSI SQL-92 標準的改編版本）為藍本，並加入一些擴充。StackQL 陳述式可用來查詢雲端資源（使用 `SELECT` 文法），或使用標準資料操作語言（DML）陳述式（例如 `INSERT`、`UPDATE`、`REPLACE` 和 `DELETE`）建立、修改或刪除基礎架構。此外，非 DML 作業，例如啟動執行個體，或在 AWS S3 的儲存貯體中取得或放入物件，都實作為內建的 Provider 程序（類似 DBMS 中的預存程序）。內建的 Provider 程序使用 `EXEC` 陳述式叫用。

StackQL 的 SQL 領域特定語言支援宣告式以及程序式的基礎架構程式設計。

* * * 

## 語法 {#syntax}
StackQL 作業由一個或多個以分號（`;`）結尾的陳述式組成。陳述式包含關鍵字、識別碼、參數和值，例如，在 GCP 中建立 10GB 磁碟的 StackQL 陳述式如下：

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

在上述範例中，*`INSERT`*、*`INTO`* 和 *`SELECT`* 是關鍵字，*google.compute.disks* 是識別碼，*project*、*zone*、*name*、*sizeGb* 是參數，而 *'myProject'*、*'australia-southeast1-a'*、*'my10GbDisk'*、*10* 是值。

## 關鍵字 {#keywords}
關鍵字不區分大小寫，也就是說 `INSERT` 和 `insert` 會被視為相同，不過依照慣例通常以大寫表示。完整的關鍵字清單請參閱「關鍵字」一節。

## 識別碼 {#identifiers}
識別碼是代表雲端供應商資源階層中物件的符號。識別碼由多個部分組成，如需識別碼的詳細資訊，請參閱「StackQL 資源階層」主題。

## 參數和值 {#parameters-and-values}
參數由資源指定；可以對指定的雲端供應商資源使用 `DESCRIBE` 陳述式來找到這些參數。值是對應到參數的具型別純量或複雜物件。如需參數和值的詳細資訊，請參閱 `DESCRIBE` 和「資料型別」主題。

## 變數 {#variables}
以 $ 為前置字元的識別碼（例如 `$region`）會解除參考以 [`SET`](/language-spec/set) 指派的工作階段變數；在規劃陳述式之前，它會被取代為該變數的具型別值。以單引號括住的 `'$region'` 是字串常值。在 `FROM` 子句中，`$variable` 會解析為關聯名稱，而 `IDENTIFIER($variable)` 在運算式位置會解析為資料行名稱。以雙引號括住且以 $ 為前置字元的路徑（例如 `"$.auth.aws.password"`）是 `SET` 和 `UNSET` 所接受的執行階段設定目標。StackQL `v1.0` 及更新版本提供工作階段變數，請參閱[工作階段變數](/getting-started/session-variables)。

## 運算子 {#operators}
`||` 是字串串連運算子，其優先順序高於比較運算子。`OR` 是布林析取運算。`v1.0` 之前的 StackQL 發行版本會將 `||` 當作 `OR` 語彙基元處理。

## 註解 {#comments}
StackQL 支援在 StackQL 陳述式中使用多行註解和行內註解，註解範例如下：

```sql
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
