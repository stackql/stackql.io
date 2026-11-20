---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - إدارة الوصول
  - groups
  - دليل كيانات الأمان
description: "الدالة GROUPS في StackQL: إرجاع مجموعات الدليل للجهة المستدعية المصادق عليها كمصفوفة JSON عند تمكين إدارة الوصول."
image: "/img/stackql-featured-image.png"
---
تعيد عضويات مجموعات الدليل للجهة المستدعية المصادق عليها كمصفوفة JSON.  تتطلب جلسة بدأت مع تمكين [إدارة الوصول](/command-line-usage/access-management).  متاحة في StackQL من الإصدار `v1.0` فصاعدًا.

راجع أيضًا:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ إدارة الوصول ]](/command-line-usage/access-management)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## الوسيطات {#arguments}

لا يوجد.

## القيمة المرجعة {#return-values}

تعيد مصفوفة JSON بمعرفات المجموعات من لقطة دليل كيانات الأمان المأخوذة للجملة الحالية، على سبيل المثال `["readers"]`.  يتم تحديث العضوية لكل جملة، لذلك ينعكس أي إبطال في الجملة التالية.  تفشل الدالة في جلسة بدأت بدون `--access.config`.

* * *

## أمثلة {#examples}

### فحص الهوية التي تم تأسيسها {#inspect-the-established-identity}

تعرض الدالتان معًا معرف كيان الأمان للجهة المستدعية ومجموعات الدليل الحالية الخاصة بها.  ولأن قراءة العضوية تتم من جديد لكل جملة، فإن تشغيل هذا الاستعلام بعد تغيير في الدليل يؤكد أن التغيير مرئي للجلسة.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
