---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の DATETIME 関数：入力値と 0 個以上の修飾子から日時値を計算し、YYYY-MM-DD HH:MM:SS 形式で返します。"
image: "/img/stackql-featured-image.png"
---
入力された日時と 0 個以上の修飾子から計算した日時値を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 日付修飾子 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 構文 {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*datetime_expression*__  
日時値。

__*modifier*__  
*datetime_expression* に適用する 0 個以上の修飾子。修飾子の詳細については、[**日付修飾子**](/language-spec/functions/datetime/date_modifiers) を参照してください。

## 戻り値 {#return-values}

入力 *datetime_expression* に *modifier* を適用した日時を表す文字列を返します。

* * *

## 例 {#examples}

### `localtime` 修飾子を使用して UTC をローカル時刻に変換する {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### ローカル時刻を UTC に変換する {#convert-datetime-to-utc}

`utc` 修飾子は、入力を StackQL を実行しているマシンのローカル時刻として扱い、UTC に変換します。`'now'` や末尾に `Z` が付いたタイムスタンプなど、すでに UTC である値は変更されずに返されます。

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

タイムゾーンが UTC+11 のマシンでは、このクエリは `2026-11-20 06:59:24` を返します。

### 別の日付を基準に日付を計算する {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### 指定した日付の後に来る指定曜日を取得する {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### 指定した日付の月初を取得する {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Unix タイムスタンプを日時に変換する {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

日時を Unix エポックタイムスタンプに変換するには、[**STRFTIME**](/language-spec/functions/datetime/strftime) を参照してください。2 つの日時オブジェクトの差を計算するには、[**STRFTIME**](/language-spec/functions/datetime/strftime) または [**JULIANDAY**](/language-spec/functions/datetime/julianday) を参照してください。

:::

詳細については、[https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) を参照してください。
