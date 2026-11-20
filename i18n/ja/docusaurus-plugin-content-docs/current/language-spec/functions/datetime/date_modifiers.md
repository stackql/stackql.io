---
title: 日付修飾子
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の日付と時刻の修飾子：DATE、TIME、DATETIME、JULIANDAY、STRFTIME の結果を日、月、年の単位でずらしたり、期間の開始や曜日などに移動したりします。"
image: "/img/stackql-featured-image.png"
---

## 概要 {#overview}

StackQL の日時関数に渡す日付または時刻の値の後には、日付や時刻を変更する 0 個以上の修飾子を続けることができます。各修飾子は、その左側にある時刻値に適用される変換です。修飾子は左から右の順に適用されます。使用できる修飾子は次のとおりです。

| 修飾子             | 説明                                                           |
|----------------------|-----------------------------------------------------------------------|
| NNN day(s)           | 日付と時刻に ±NNN 日を加算する                                      |
| NNN hour(s)          | 日付と時刻に ±NNN 時間を加算する                                     |
| NNN minute(s)        | 日付と時刻に ±NNN 分を加算する                                   |
| NNN[.NNNN] second(s) | 日付と時刻に ±NNN 秒を加算または減算する                              |
| NNN month(s)         | 日付と時刻に ±NNN か月を加算する                                    |
| NNN year(s)          | 日付と時刻に ±NNN 年を加算する                                      |
| start of month       | 月の初めまで戻す                                |
| start of year        | 年の初めまで戻す                                 |
| start of day         | 日の初めまで戻す                                  |
| weekday N            | 曜日番号が N となる次の日付まで日付を進める |
| localtime            | ローカル時刻を返す                                                     |
| utc                  | UTC 時刻を返す                                                       |
| unixepoch            | Unix 時間                                                             |

## 例 {#examples}

### 時刻値 `now` を使用して現在の日付と時刻を返す {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

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
