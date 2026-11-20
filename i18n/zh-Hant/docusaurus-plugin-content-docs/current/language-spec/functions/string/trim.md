---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 TRIM 函式：從字串兩端移除空格，或移除指定字元集中的任何字元。"
image: "/img/stackql-featured-image.png"
---
傳回從輸入字串兩端移除空格（或指定字元集中的任何字元）後所形成的字串。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## 語法 {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*character_expression*__  
字串常值或字串資料行。

__*character*__  
選用的字元字串：其中的每個字元都會從 *character_expression* 的開頭和結尾移除。若省略，則移除空格（不包括定位字元或換行字元）。

## 傳回值 {#return-values}
傳回字串。

* * *

## 範例 {#examples}

### 移除字串資料行的前後空格 {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)。
