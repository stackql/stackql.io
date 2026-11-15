---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の DATE 関数：時刻値と省略可能な修飾子から、日付を YYYY-MM-DD 形式の文字列で返します。"
image: "/img/stackql-featured-image.png"
---
YYYY-MM-DD 形式の日付文字列を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 日付修飾子 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 構文 {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*datetime_expression*__  
日時の値。

__*modifier*__  
*datetime_expression* に適用する 0 個以上の修飾子。修飾子の詳細については、[**日付修飾子**](/language-spec/functions/datetime/date_modifiers)を参照してください。

## 戻り値 {#return-values}

*modifier* を適用した入力 *datetime_expression* の日付部分を表す文字列を返します。

* * *

## 例 {#examples}

### 日時の値の日付部分を返す {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### 日時の値のローカル日付部分を返す {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

詳細については、[https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) を参照してください。
