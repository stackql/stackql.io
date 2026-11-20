---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - تجزئة
  - ملخص
description: "الدالة MD5 في StackQL: إرجاع ملخص MD5 لسلسلة نصية بالنظام السداسي العشري وبأحرف صغيرة، لإنشاء البصمات واكتشاف التغييرات وليس لأغراض الأمان."
image: "/img/stackql-featured-image.png"
---
تعيد ملخص MD5 لسلسلة نصية في صورة 32 حرفًا بالنظام السداسي العشري وبأحرف صغيرة.  متاح في StackQL من الإصدار `v1.0` فصاعدًا.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ الدمج ]](/language-spec/functions/string/concatenation)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expression*__  
سلسلة نصية، أو قيمة يجري تحويلها ضمنيًا إلى نص.  إذا كانت الوسيطة `NULL`، تعيد الدالة `NULL`.

## القيمة المرجعة {#return-values}

تعيد سلسلة نصية سداسية عشرية من 32 حرفًا بأحرف صغيرة.

> الغرض من الدالة `MD5` هو إنشاء البصمات واكتشاف التغييرات.  وهي لا تصلح كعنصر أساسي لكلمات المرور أو للأمان.  على واجهة SQL الخلفية PostgreSQL، تُستخدم الدالة `md5` الخاصة بقاعدة البيانات نفسها.

* * *

## أمثلة {#examples}

### إنشاء بصمة لقيمة مشتقة {#fingerprint-a-derived-value}

يتم استبدال المتغيرات كقيم ذات أنواع، ويتحول العدد الصحيح إلى نص يدمج مع اللاحقة، ثم يؤخذ الملخص من النتيجة بعد تحويلها إلى أحرف صغيرة.  تنتج الإدخالات نفسها دائمًا السلسلة السداسية العشرية نفسها المكونة من 32 حرفًا، لذا يمكن استخدام القيمة كمفتاح ثابت.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### اكتشاف تغييرات التكوين {#detect-configuration-changes}

تنتج تجزئة عمود JSON متسلسل قيمة قصيرة يمكن تخزينها ومقارنتها بين مرات التشغيل.  الحاوية التي يختلف ملخصها عن ملخص آخر تشغيل يكون تكوين IAM الخاص بها قد تغير، دون الحاجة إلى تخزين المستند بأكمله أو مقارنة الفروق فيه.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
