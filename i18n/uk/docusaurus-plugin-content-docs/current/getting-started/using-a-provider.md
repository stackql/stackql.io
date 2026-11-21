---
title: Використання провайдера
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Використання провайдера в StackQL: завантаження з реєстру, автентифікація через змінні середовища або облікові дані та запити до його сервісів і ресурсів."
image: "/img/stackql-featured-image.png"
---

Див. також:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

Запити StackQL можна виконувати до багатьох різних постачальників хмарних послуг, зокрема Google Cloud Platform, Amazon Web Services, Microsoft Azure, DigitalOcean та інших. Специфікації провайдерів StackQL завантажуються з [__StackQL Provider Registry__](/providers); це можна зробити за допомогою команди StackQL [__`REGISTRY PULL`__](/language-spec/registry) або утиліти командного рядка [__`stackql registry`__](/command-line-usage/registry).


## Встановлення провайдера {#installing-a-provider}

Щоб переглянути список доступних провайдерів, виконайте таку команду:  

```sql
REGISTRY LIST;
```

Щоб встановити провайдера, виконайте таку команду:

```sql
REGISTRY PULL okta;
```

Щоб переглянути провайдерів, встановлених у поточній системі, виконайте таку команду:

```sql
SHOW PROVIDERS;
```

## Автентифікація в провайдері {#authenticating-to-a-provider}

Автентифікація для доступу до постачальника хмарних послуг часто є першим кроком у роботі зі StackQL. Метакоманди, як-от [`SHOW`](/language-spec/show) і [`DESCRIBE`](/language-spec/describe), можна виконувати для провайдера без автентифікації, але команди [`SELECT`](/language-spec/select), [`INSERT`](/language-spec/insert) і [`EXEC`](/language-spec/exec) зазвичай потребують автентифікованого сеансу.  

Автентифікація виконується за допомогою змінних середовища, встановлених у системі (або завантажених як секрети в системі CI); кожен провайдер має власні унікальні змінні за замовчуванням для автентифікації, тому зверніться до документації свого провайдера в [документації StackQL Provider Registry](/providers).  Змінні також можна завантажити під час запуску з файлу у стилі dotenv за допомогою глобального прапорця [`--env.file`](/command-line-usage/global-flags).  

:::tip

Ви можете пройти автентифікацію в кількох провайдерах в одній інтерактивній оболонці або неінтерактивній пакетній процедурі, встановивши змінні автентифікації кожного провайдера, наприклад:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

Це особливо корисно, коли ви з'єднуєте дані кількох провайдерів в одному запиті.

:::
