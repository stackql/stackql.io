---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 DATE 函式：根據時間值和選用的修飾詞，以 YYYY-MM-DD 字串傳回日期。"
image: "/img/stackql-featured-image.png"
---
傳回格式為 YYYY-MM-DD 的日期字串。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 日期修飾詞 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 語法 {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*datetime_expression*__  
日期時間值。

__*modifier*__  
要套用至 *datetime_expression* 的零個或多個修飾詞。如需修飾詞的詳細資訊，請參閱 [**日期修飾詞**](/language-spec/functions/datetime/date_modifiers)。

## 傳回值 {#return-values}

傳回一個字串，表示套用 *modifier* 後輸入 *datetime_expression* 的日期部分。

* * *

## 範例 {#examples}

### 傳回日期時間值的日期部分 {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### 傳回日期時間值的當地日期部分 {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)。
