---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 STRFTIME 函式：依格式字串格式化日期時間值，所用的替代符號與 C 的 strftime 函式相同。"
image: "/img/stackql-featured-image.png"
---
依指定的格式傳回格式化的日期時間值。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 日期修飾詞 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 語法 {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*format*__  
*datetime_expression* 值所指定之日期時間值的所需格式。請參閱下方的格式表格。

__*datetime_expression*__  
要格式化的日期或時間值。

__*modifier*__  
要套用至 *datetime_expression* 的零個或多個修飾詞。如需修飾詞的詳細資訊，請參閱[**日期修飾詞**](/language-spec/functions/datetime/date_modifiers)

### 格式 {#formats}

| 格式 | 描述                    |
|--------|--------------------------------|
| %d     | 月中的日期：01-31        |
| %f     | 含小數的秒數：SS.SSS     |
| %H     | 小時：00-24                    |
| %j     | 一年中的第幾天：001-366       |
| %J     | 儒略日數              |
| %m     | 月份：01-12                   |
| %M     | 分鐘：00-59                  |
| %s     | 自 1970-01-01 起的秒數       |
| %S     | 秒：00-59                 |
| %w     | 星期幾 0-6，星期日為 0 |
| %W     | 一年中的第幾週：00-53        |
| %Y     | 年份：0000-9999                |
| %%     | 逸出百分比符號：%     |

## 傳回值 {#return-values}

傳回以所需日期時間格式表示的格式化字串。

* * *

## 範例 {#examples}

### 以美國格式傳回日期 {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 將日期時間轉換為 epoch 時間戳記 {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 計算兩個日期之間的天數 {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
