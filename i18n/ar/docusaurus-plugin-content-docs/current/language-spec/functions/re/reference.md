---
title: مرجع التعبيرات النمطية
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "مرجع التعبيرات النمطية في StackQL: بناء الجملة وفئات الأحرف وعلامات الارتكاز ومحددات الكمية والمجموعات للدوال REGEXP_LIKE وREGEXP_REPLACE وREGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
مرجع للتعبيرات النمطية المستخدمة في StackQL.  

راجع أيضًا:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## بناء الجملة المدعوم {#supported-syntax}

يدعم StackQL بناء جملة التعبيرات النمطية التالي:    

| التعبير | الوصف |
|------------|-------------|
| `X*` | صفر أو أكثر من تكرارات `X` |
| `X+` | تكرار واحد أو أكثر لـ `X` | 
| `X?` | صفر أو تكرار واحد لـ `X` |
| `(X)` | مطابقة `X` |
| `X`&vert;`Y` | `X` أو `Y` |
| `^X` | `X` في بداية السلسلة النصية |
| `X$` | `X` في نهاية السلسلة النصية |
| `.` | مطابقة أي حرف واحد |
| `\c` | الحرف `c` حيث `c` أحد الأحرف `\{}()[]`&vert;`*+?.` |
| `\c` | تسلسلات الإلغاء في لغة C لـ `c` ضمن `afnrtv`. مثال: `\t` أو `\n` |
| `[abc]` | أي حرف واحد من المجموعة `abc` |
| `[^abc]` | أي حرف واحد ليس في المجموعة `abc` |
| `[a-z]` | أي حرف واحد ضمن النطاق `a-z` |
| `[^a-z]` | أي حرف واحد ليس ضمن النطاق `a-z` |  

&nbsp;  
&nbsp;  
لمزيد من المعلومات، راجع [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
