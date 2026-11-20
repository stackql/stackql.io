---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة CHAR في StackQL: الأحرف المقابلة لرمز حرف واحد أو أكثر، لإدراج أحرف السطر الجديد وعلامات الاقتباس وعلامات الجدولة وأحرف التحكم الأخرى في السلاسل النصية."
image: "/img/stackql-featured-image.png"
---

تعيد الدالة `CHAR()` سلسلة نصية مكونة من الأحرف المقابلة لرمز حرف واحد أو أكثر.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
عند استخدام StackQL مع واجهة SQL الخلفية PostgreSQL، تكون الدالة المكافئة هي `CHR()`، التي تأخذ رمز حرف واحد.
:::

## بناء الجملة {#syntax}

```sql
SELECT CHAR(ascii_code [, ascii_code, ...]) FROM <multipartIdentifier>;
```

> تضيف كل وسيطة حرفًا واحدًا إلى النتيجة، لذلك تعيد `CHAR(72, 105)` القيمة `Hi`.

## الوسيطات {#arguments}

**_ascii_code_**  
عدد صحيح واحد أو أكثر، كل منها رمز حرف: رمز ASCII، أو أي نقطة رمز Unicode.

## القيمة المرجعة {#return-values}

تعيد سلسلة نصية تحتوي على حرف واحد لكل وسيطة *ascii_code*.

* * *

## رموز ASCII الشائعة {#common-ascii-codes}

| الحرف         | الوصف                 | رمز ASCII  | مثال على الاستخدام              |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | سطر جديد              | 10         | `CHAR(10)`                      |
| `'`           | علامة اقتباس مفردة    | 39         | `CHAR(39)`                      |
| `"`           | علامة اقتباس مزدوجة   | 34         | `CHAR(34)`                      |
| (مسافة)       | مسافة                 | 32         | `CHAR(32)`                      |
| `:`           | نقطتان                | 58         | `CHAR(58)`                      |
| `;`           | فاصلة منقوطة          | 59         | `CHAR(59)`                      |
| `\t`          | علامة جدولة أفقية     | 9          | `CHAR(9)`                       |
| `\r`          | إرجاع العربة          | 13         | `CHAR(13)`                      |
| `\b`          | مسافة للخلف           | 8          | `CHAR(8)`                       |
| `\f`          | تغذية الصفحة          | 12         | `CHAR(12)`                      |

* * *

## أمثلة {#examples}

### دمج السلاسل النصية مع أحرف خاصة {#concatenate-strings-with-special-characters}

لدمج سلاسل نصية مع حرف سطر جديد بينها:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**النتيجة** (لمستخدم اسم العرض الخاص به `Jane Doe` ومعرفه `1234567890123456`):

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
