---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 TIME 函式：依時間值與選用的修飾詞，以 HH:MM:SS 字串傳回時間。"
image: "/img/stackql-featured-image.png"
---
傳回格式為 HH:MM:SS 的時間字串。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 日期修飾詞 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 語法 {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*datetime_expression*__  
日期時間值。

__*modifier*__  
要套用至 *datetime_expression* 的零個或多個修飾詞。如需修飾詞的詳細資訊，請參閱[**日期修飾詞**](/language-spec/functions/datetime/date_modifiers)

## 傳回值 {#return-values}

傳回一個字串，表示輸入的 *datetime_expression* 套用 *modifier* 後的時間部分。

* * *

## 範例 {#examples}

### 傳回日期時間值的時間部分 {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### 傳回日期時間值的當地時間部分 {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
