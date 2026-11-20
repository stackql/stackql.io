---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 JULIANDAY 函式：時間值自西元前 4714 年 11 月 24 日正午起算的天數（含小數），適用於日期運算。"
image: "/img/stackql-featured-image.png"
---
傳回儒略日，也就是自西元前 4714 年 11 月 24 日格林威治正午起算的天數[（前推格里曆）](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar)。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 日期修飾詞 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 語法 {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*datetime_expression*__  
日期時間值。

__*modifier*__  
要套用至 *datetime_expression* 的零個或多個修飾詞。如需修飾詞的詳細資訊，請參閱 [**日期修飾詞**](/language-spec/functions/datetime/date_modifiers)。

## 傳回值 {#return-values}

傳回套用 *modifier* 後輸入 *datetime_expression* 的[儒略日](https://en.wikipedia.org/wiki/Julian_day)：以[前推格里曆](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar)計算，自西元前 4714 年 11 月 24 日格林威治正午起算的天數，包含表示當日時間的小數部分。

* * *

## 範例 {#examples}

### 計算兩個日期之間的天數 {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

使用 [**ROUND**](/language-spec/functions/math/round) 函式將浮點數轉換為最接近的整數。

:::

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)。
