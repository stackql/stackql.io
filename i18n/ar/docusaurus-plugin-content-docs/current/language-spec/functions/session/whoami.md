---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - إدارة الوصول
  - whoami
  - كيان الأمان
description: "الدالة WHOAMI في StackQL: إرجاع معرف كيان الأمان للجهة المستدعية المصادق عليها عند تمكين إدارة الوصول."
image: "/img/stackql-featured-image.png"
---
تعيد معرف كيان الأمان للجهة المستدعية المصادق عليها.  تتطلب جلسة بدأت مع تمكين [إدارة الوصول](/command-line-usage/access-management).  متاحة في StackQL من الإصدار `v1.0` فصاعدًا.

راجع أيضًا:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ إدارة الوصول ]](/command-line-usage/access-management)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## الوسيطات {#arguments}

لا يوجد.

## القيمة المرجعة {#return-values}

تعيد سلسلة نصية تمثل معرف كيان الأمان الذي تم تأسيسه للجملة الحالية.  تفشل الدالة في جلسة بدأت بدون `--access.config`.

* * *

## أمثلة {#examples}

### فحص الهوية التي تم تأسيسها {#inspect-the-established-identity}

لا تأخذ الدالتان أي وسيطات، وتصفان الجهة المستدعية التي صادق عليها الخادم لهذه الجملة.  تعيد الدالة `whoami()` معرف كيان الأمان وتعيد الدالة `groups()` مجموعات الدليل الخاصة بالجهة المستدعية، لذلك فإن تشغيلهما معًا وسيلة سريعة للتحقق من أن الرمز المميز أو الشهادة يقابل كيان الأمان المتوقع قبل تشغيل أي شيء آخر.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
