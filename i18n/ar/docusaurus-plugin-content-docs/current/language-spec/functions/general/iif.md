---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة IIF في StackQL: إرجاع إحدى قيمتين بحسب صحة شرط، وهي تعبير if-then-else داخل السطر."
image: "/img/stackql-featured-image.png"
---
تعيد قيمة بناء على تقييم تعبير الإدخال.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> تكافئ الدالة `iif()` منطقيًا تعبير `CASE` بالشكل `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## الوسيطات {#arguments}

__*expression*__  
تعبير منطقي يراد تقييمه.

:::tip 

يمكن أن تكون الوسيطة *expression* دالة متداخلة مثل `JSON_EXTRACT`.

:::

__*result_if_true*__  
النتيجة التي تعاد إذا كان تقييم *expression* هو `true`.

__*result_if_false*__  
النتيجة التي تعاد إذا كان تقييم *expression* هو `false`.

## القيمة المرجعة {#return-values}
نوع بيانات الوسيطتين *result_if_true* أو *result_if_false*.

* * *

## أمثلة {#examples}

### تصنيف أحجام أقراص مثيلات compute engine {#categorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER) > 10, '> 10GB', '<= 10GB') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
