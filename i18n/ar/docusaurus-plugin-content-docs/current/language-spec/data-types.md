---
title: أنواع البيانات
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "أنواع البيانات في StackQL: الأنواع المستخدمة لخصائص الموارد ومعلماتها في الاستعلامات والنتائج، وكيف تتوافق مع مخططات واجهات API لدى الموفرين."
image: "/img/stackql-featured-image.png"
---

تمثل أنواع بيانات StackQL قيم حقول الإدخال أو الإخراج (الخصائص أو المعلمات) المستخدمة في الموارد السحابية.  يسرد الجدول التالي أنواع بيانات StackQL:

| نوع StackQL | الوصف | مثال |
|--|--|--|
| `string` | سلسلة من أحرف Unicode محاطة بعلامتي اقتباس مفردتين | `'mybucket'` |
| `array` | مجموعة مرتبة من القيم، محاطة بالقوسين `[ ]` ومفصولة بفاصلة | `[0,1,2]` |
| `object` | مجموعة غير مرتبة من صفر أو أكثر من أزواج الاسم والقيمة، توضع بين `{ }` وتفصل بينها فاصلة | `{"name": "mydb"}` |
| `boolean` | `true` أو `false`، دون علامات اقتباس (يعامل StackQL القيمة المحاطة بعلامات اقتباس، مثل `'true'`، كسلسلة نصية) | `true` |
| `integer` | عدد صحيح يمكن أن يكون موجبًا أو سالبًا أو صفرًا | `2` |
| `double` | عدد بفاصلة عائمة مزدوج الدقة، يمكن أن يكون موجبًا أو سالبًا | `0.95` |
<br/>
<br/>
> تتوافق أنواع بيانات StackQL مع أنواع البيانات التي تعرضها واجهات API لدى جميع مزودي الخدمات السحابية الرئيسيين.

## تحويل النوع {#type-conversion}

يحول كل من `CAST(expression AS type)` و`CONVERT(expression, type)` قيمة إلى نوع آخر.  بالإضافة إلى أنواع التحويل الموروثة من القواعد النحوية لـ MySQL (`CHAR`، `DATE`، `DATETIME`، `DECIMAL`، `SIGNED`، `UNSIGNED`، `TIME`، `JSON`)، يقبل StackQL من الإصدار `v1.0` فصاعدًا الأنواع `TEXT` و`INTEGER` و`INT` و`BIGINT` و`BOOLEAN` و`BOOL`.  تنفذ واجهة SQL الخلفية عملية التحويل، لذا تتبع دلالاتها دلالات SQLite أو PostgreSQL.  يشيع استخدام التحويلات لمقارنة [متغير جلسة](/getting-started/session-variables) له نوع محدد بعمود من نوع سلسلة نصية:

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

تظهر الحقول المنطقية التي تأتي من موفرين يستخدمون XML، مثل AWS، بالقيمتين `true` و`false`؛ وكانت إصدارات StackQL قبل `v1.0` تعرضها بالقيمتين `0` و`1`.
