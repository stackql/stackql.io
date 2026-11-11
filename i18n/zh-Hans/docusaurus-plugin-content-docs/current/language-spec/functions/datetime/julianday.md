---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 JULIANDAY 函数：时间值自公元前 4714 年 11 月 24 日正午以来的天数（含小数），可用于日期运算。"
image: "/img/stackql-featured-image.png"
---
返回儒略日，即自公元前 4714 年 11 月 24 日格林尼治正午以来的天数[（外推格里历）](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar)。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 日期修饰符 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 语法 {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*datetime_expression*__  
日期时间值。

__*modifier*__  
应用于 *datetime_expression* 的零个或多个修饰符。有关修饰符的更多信息，请参阅[**日期修饰符**](/language-spec/functions/datetime/date_modifiers)。

## 返回值 {#return-values}

返回[儒略日](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar)，表示应用 *modifier* 后输入 *datetime_expression* 的日期部分。

* * *

## 示例 {#examples}

### 计算两个日期之间的天数 {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

使用 [**ROUND**](/language-spec/functions/math/round) 将浮点数转换为最接近的整数。

:::

有关更多信息，请参阅 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)。
