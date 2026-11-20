---
title: استخدام موفر
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "استخدام موفر في StackQL: سحبه من السجل، والمصادقة باستخدام متغيرات البيئة أو بيانات الاعتماد، والاستعلام عن خدماته وموارده."
image: "/img/stackql-featured-image.png"
---

راجع أيضًا:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

يمكن تشغيل استعلامات StackQL على عدد من مزودي الخدمات السحابية المختلفين، بما في ذلك Google Cloud Platform وAmazon Web Services وMicrosoft Azure وDigital Ocean وغيرها. يتم سحب مواصفات موفري StackQL من [__StackQL Provider Registry__](/providers)، ويمكن القيام بذلك باستخدام أمر StackQL [__`REGISTRY PULL`__](/language-spec/registry)، أو باستخدام أداة سطر الأوامر [__`stackql registry`__](/command-line-usage/registry).


## تثبيت موفر {#installing-a-provider}

للاطلاع على قائمة الموفرين المتاحين، شغّل الأمر التالي:  

```sql
REGISTRY LIST;
```

لتثبيت موفر، شغّل الأمر التالي:

```sql
REGISTRY PULL okta;
```

لعرض الموفرين المثبتين على النظام الحالي، شغّل الأمر التالي:

```sql
SHOW PROVIDERS;
```

## المصادقة لدى موفر {#authenticating-to-a-provider}

غالبًا ما تكون المصادقة لدى مزود الخدمات السحابية الخطوة الأولى في أي إجراء StackQL. يمكن تشغيل الأوامر الوصفية مثل [`SHOW`](/language-spec/show) و[`DESCRIBE`](/language-spec/describe) على موفر دون مصادقة، لكن أوامر [`SELECT`](/language-spec/select) و[`INSERT`](/language-spec/insert) و[`EXEC`](/language-spec/exec) تتطلب عادة جلسة تمت مصادقتها.  

تتم المصادقة باستخدام متغيرات البيئة المعينة على النظام (أو المحملة كأسرار في نظام CI)، ولكل موفر متغيراته الافتراضية الفريدة الخاصة به المستخدمة للمصادقة، لذا راجع وثائق الموفر الذي تستخدمه في [وثائق StackQL Provider Registry](/providers).  يمكن أيضًا تحميل المتغيرات من ملف بنمط dotenv عند بدء التشغيل باستخدام العلامة العامة [`--env.file`](/command-line-usage/global-flags).  

:::tip

يمكنك المصادقة لدى عدة موفرين في واجهة الأوامر التفاعلية نفسها أو في إجراء دفعي غير تفاعلي واحد عن طريق تعيين متغيرات المصادقة لكل موفر، على سبيل المثال:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

يفيد هذا بشكل خاص عند ربط البيانات بين عدة موفرين في استعلام واحد.

:::
