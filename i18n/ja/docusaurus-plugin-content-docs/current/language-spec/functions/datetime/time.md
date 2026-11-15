---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の TIME 関数：時刻値と省略可能な修飾子から、時刻を HH:MM:SS 形式の文字列で返します。"
image: "/img/stackql-featured-image.png"
---
HH:MM:SS 形式の時刻文字列を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 日付修飾子 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 構文 {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*datetime_expression*__  
日時値。

__*modifier*__  
*datetime_expression* に適用する 0 個以上の修飾子。修飾子の詳細については、[**日付修飾子**](/language-spec/functions/datetime/date_modifiers) を参照してください。

## 戻り値 {#return-values}

入力 *datetime_expression* に *modifier* を適用した時刻部分を表す文字列を返します。

* * *

## 例 {#examples}

### 日時値の時刻部分を返す {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### 日時値のローカル時刻部分を返す {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

詳細については、[https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) を参照してください。
