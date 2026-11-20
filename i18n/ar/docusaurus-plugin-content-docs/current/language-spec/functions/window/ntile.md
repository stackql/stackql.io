---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "دالة النافذة NTILE في StackQL: تقسيم قسم إلى N من المجموعات بأكبر قدر ممكن من التساوي وترقيم مجموعة كل صف من 1 إلى N."
image: "/img/stackql-featured-image.png"
---
تقسم القسم إلى N من المجموعات بأكبر قدر ممكن من التساوي وتعين لكل مجموعة عددًا صحيحًا بين 1 وN، بالترتيب الذي تحدده عبارة `ORDER BY`، أو بترتيب اعتباطي في غير ذلك. وعند الضرورة، تأتي المجموعات الأكبر أولًا.

راجع أيضًا:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## بناء الجملة {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*N*__
عدد صحيح موجب يحدد عدد المجموعات (الشرائح) التي يقسم إليها القسم.

__*PARTITION BY column*__
اختياري. تقسم مجموعة النتائج إلى أقسام. يتم تطبيق الدالة `NTILE` على كل قسم على حدة.

__*ORDER BY column*__
تحدد ترتيب تعيين الصفوف إلى الشرائح.

## القيمة المرجعة {#return-values}

تعيد عددًا صحيحًا بين 1 وN يشير إلى الشريحة (المجموعة) التي ينتمي إليها الصف الحالي.

* * *

## أمثلة {#examples}

### تجميع المساهمين في ربيعات حسب مستوى المساهمة {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

استخدم الدالة `NTILE` لإنشاء قطاعات أو مستويات في بياناتك. تشمل حالات الاستخدام الشائعة تقسيم البيانات إلى ربيعات (N=4) أو عشيرات (N=10) أو مئينات (N=100).

:::

لمزيد من المعلومات، راجع [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
