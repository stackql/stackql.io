---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の JULIANDAY 関数：時刻値について紀元前 4714 年 11 月 24 日正午からの日数を小数で返し、日付の計算に役立ちます。"
image: "/img/stackql-featured-image.png"
---
ユリウス日を返します。ユリウス日は、グリニッジにおける紀元前 4714 年 11 月 24 日[（先発グレゴリオ暦）](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar)の正午からの日数です。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 日付修飾子 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 構文 {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*datetime_expression*__  
日時値。

__*modifier*__  
*datetime_expression* に適用する 0 個以上の修飾子。修飾子の詳細については、[**日付修飾子**](/language-spec/functions/datetime/date_modifiers) を参照してください。

## 戻り値 {#return-values}

入力 *datetime_expression* に *modifier* を適用した値の[ユリウス日](https://en.wikipedia.org/wiki/Julian_day)を返します。ユリウス日は、[先発グレゴリオ暦](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar)の紀元前 4714 年 11 月 24 日のグリニッジ正午からの日数で、時刻を表す小数部を含みます。

* * *

## 例 {#examples}

### 2 つの日付の間の日数を計算する {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

浮動小数点数を最も近い整数に変換するには、[**ROUND**](/language-spec/functions/math/round) を使用します。

:::

詳細については、[https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) を参照してください。
