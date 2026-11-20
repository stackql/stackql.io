---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: تجميع أزواج المفاتيح والقيم في كائن JSON باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تجمع أزواج المفاتيح والقيم في كائن JSON، مع التجميع حسب عمود محدد. تجمع الدالة `JSON_GROUP_OBJECT()` كل أزواج المفاتيح والقيم لكل مجموعة في أعمدة محددة وتدمجها في كائن JSON واحد لكل مجموعة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## الوسيطات {#arguments}

__*key_column*__  
العمود الذي يوفر المفاتيح لكائنات JSON.

__*value_column*__  
العمود الذي يوفر القيم لكائنات JSON.

__*grouping_column*__  
العمود الذي يتم تجميع النتائج حسبه.

## القيمة المرجعة {#return-values}
تعيد كائن JSON يحتوي على جميع أزواج المفاتيح والقيم المجمعة من الأعمدة المحددة، مجمعة وفقًا للعمود `grouping_column`.

* * *

## أمثلة {#examples}

### تجميع أحجام المستودعات حسب لغة البرمجة {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

يعيد هذا الاستعلام كائن JSON لكل لغة برمجة، حيث يحتوي كل كائن على أسماء المستودعات وأحجامها المقابلة في صورة أزواج مفاتيح وقيم. إليك عينة من الإخراج المتوقع:

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

يفيد هذا الإخراج في تصور توزيع أحجام المستودعات عبر لغات البرمجة المختلفة داخل مؤسسة، إذ يوفر بيانات واضحة ومنظمة مجمعة حسب اللغة.
