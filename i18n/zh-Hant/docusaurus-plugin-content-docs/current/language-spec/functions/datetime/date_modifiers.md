---
title: 日期修飾詞
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的日期和時間修飾詞：依天、月或年調整 DATE、TIME、DATETIME、JULIANDAY 和 STRFTIME 的結果，或移至期間開始、特定星期幾等。"
image: "/img/stackql-featured-image.png"
---

## 概觀 {#overview}

在 StackQL 日期時間函式中，日期或時間值後面可以接零個或多個修飾詞，用來變更日期和/或時間。每個修飾詞都是套用至其左側時間值的轉換。修飾詞會由左至右依序套用。可用的修飾詞如下：

| 修飾詞               | 描述                                                                  |
|----------------------|-----------------------------------------------------------------------|
| NNN day(s)           | 將日期和時間加上 ±NNN 天                                              |
| NNN hour(s)          | 將日期和時間加上 ±NNN 小時                                            |
| NNN minute(s)        | 將日期和時間加上 ±NNN 分鐘                                            |
| NNN[.NNNN] second(s) | 將日期和時間加上或減去 ±NNN 秒                                        |
| NNN month(s)         | 將日期和時間加上 ±NNN 個月                                            |
| NNN year(s)          | 將日期和時間加上 ±NNN 年                                              |
| start of month       | 往回移至該月的開始                                                    |
| start of year        | 往回移至該年的開始                                                    |
| start of day         | 往回移至該日的開始                                                    |
| weekday N            | 將日期往後推進至星期數字為 N 的下一個日期                             |
| localtime            | 傳回當地時間                                                          |
| utc                  | 傳回 UTC 時間                                                         |
| unixepoch            | Unix 時間                                                             |

## 範例 {#examples}

### 使用 `now` 時間值傳回目前的日期和時間 {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

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

### 取得指定日期之後的下一個指定星期幾 {#get-the-next-specified-weekday-after-a-given-date}

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
