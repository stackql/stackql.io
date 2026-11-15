---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の STRFTIME 関数：C の strftime 関数と同じ置換規則を使い、書式文字列で日時値を整形します。"
image: "/img/stackql-featured-image.png"
---
指定した書式に基づいて整形した日時値を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 日付修飾子 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 構文 {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*format*__  
*datetime_expression* の値で指定された日時値に適用する書式。下記の書式の表を参照してください。

__*datetime_expression*__  
書式設定する日付または時刻の値。

__*modifier*__  
*datetime_expression* に適用する 0 個以上の修飾子。修飾子の詳細については、[**日付修飾子**](/language-spec/functions/datetime/date_modifiers) を参照してください。

### 書式 {#formats}

| 書式 | 説明                    |
|--------|--------------------------------|
| %d     | 月内の日：01-31        |
| %f     | 小数部付きの秒：SS.SSS     |
| %H     | 時：00-24                    |
| %j     | 年内の日：001-366       |
| %J     | ユリウス日番号              |
| %m     | 月：01-12                   |
| %M     | 分：00-59                  |
| %s     | 1970-01-01 からの秒数       |
| %S     | 秒：00-59                 |
| %w     | 曜日 0-6（日曜日==0） |
| %W     | 年内の週：00-53        |
| %Y     | 年：0000-9999                |
| %%     | パーセント記号のエスケープ：%     |

## 戻り値 {#return-values}

指定した書式で日時を表す、整形済みの文字列を返します。

* * *

## 例 {#examples}

### 米国形式で日付を返す {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 日時をエポックタイムスタンプに変換する {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 2 つの日付の間の日数を計算する {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) を参照してください。
