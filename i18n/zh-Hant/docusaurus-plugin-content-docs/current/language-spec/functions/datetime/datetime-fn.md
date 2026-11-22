---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 DATETIME 函式：根據輸入值和零個或多個修飾詞計算日期時間值，以 YYYY-MM-DD HH:MM:SS 格式傳回。"
image: "/img/stackql-featured-image.png"
---
根據輸入日期時間和零個或多個修飾詞，傳回計算後的日期時間值。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 日期修飾詞 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 語法 {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*datetime_expression*__  
日期時間值。

__*modifier*__  
要套用至 *datetime_expression* 的零個或多個修飾詞。如需修飾詞的詳細資訊，請參閱 [**日期修飾詞**](/language-spec/functions/datetime/date_modifiers)。

## 傳回值 {#return-values}

傳回一個字串，表示套用 *modifier* 後輸入 *datetime_expression* 的日期時間。

* * *

## 範例 {#examples}

### 使用 `localtime` 修飾詞將 UTC 轉換為當地時間 {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### 將當地時間轉換為 UTC {#convert-datetime-to-utc}

`utc` 修飾詞會將其輸入視為執行 StackQL 之電腦上的當地時間，並將其轉換為 UTC。已是 UTC 的值（例如 `'now'` 或帶有 `Z` 尾碼的時間戳記）會原封不動地傳回。

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

在時區為 UTC+11 的電腦上，此查詢會傳回 `2026-11-20 06:59:24`。

### 計算相對於另一個日期的日期 {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### 取得指定日期當天或之後的指定星期幾 {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### 取得指定日期所在月份的開始 {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### 將 Unix 時間戳記轉換為日期時間 {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

若要將日期時間轉換為 Unix epoch 時間戳記，請參閱 [**STRFTIME**](/language-spec/functions/datetime/strftime)。若要計算兩個日期時間物件之間的差異，請參閱 [**STRFTIME**](/language-spec/functions/datetime/strftime) 或 [**JULIANDAY**](/language-spec/functions/datetime/julianday)。

:::

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)。
