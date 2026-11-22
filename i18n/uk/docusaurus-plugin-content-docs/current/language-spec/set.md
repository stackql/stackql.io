---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - змінні сеансу
  - set
  - сеанси провайдерів
  - секрети
description: "SET у StackQL: присвоєння типізованих змінних сеансу та налаштування псевдонімів провайдерів, полів облікових даних і секретів поточного сеансу через цілі з префіксом долара."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Присвоює значення змінній сеансу або налаштовує середовище виконання для поточного сеансу.  Доступно в StackQL `v1.0` і новіших версіях.

Див. також:  
[[` UNSET `]](/language-spec/unset) [[ Змінні сеансу ]](/getting-started/session-variables) [[ Сеанси провайдерів ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## Синтаксис {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
Звичайний ідентифікатор (`region`), ім'я в подвійних лапках (`"providers.saved"`) або шлях конфігурації в подвійних лапках із префіксом долара (`"$.auth.aws.password"`).

__*literal*__  
Рядкове значення, число, логічне значення або `null`.  Рядки беруть в одинарні лапки.  Масиви й об'єкти не є скалярними значеннями; зберігайте документ JSON як рядок.

__*DEFAULT*__  
Скидає ціль, рівнозначно [`UNSET`](/language-spec/unset).  Явне значення `null` - це збережене значення, а не скидання.

__*reference*__  
Посилання `"$name"` у подвійних лапках на наявну змінну, наприклад `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Необов'язкове, і це єдина область, яка є в StackQL.  `SET SESSION name = ...` і `SET name = ...` - це та сама інструкція: кожне присвоєння належить до поточного сеансу й більше ні до чого.  Ключове слово існує для того, щоб скрипти читалися так само, як у базах даних, де `SET SESSION` і `SET GLOBAL` відрізняються.  `SET GLOBAL` проходить синтаксичний аналіз, але відхиляється з помилкою `configuration scope "global" is not supported; use SESSION`, оскільки спільного для сеансів стану немає.

Присвоєння в одному списку `SET` застосовуються по черзі й не утворюють транзакцію: якщо друге присвоєння в списку завершується помилкою, перше залишається в силі.

## Цілі {#targets}

| Ціль | Дія |
|--|--|
| `name` або `"name"` | Зберігає скалярну змінну сеансу, яку в подальших інструкціях розіменовують як `$name`.  Самі лише крапки не вибирають конфігурацію: `"providers.saved"` - це змінна з ім'ям `providers.saved`.  Старіший запис `"variables.name"` є псевдонімом для `name`. |
| `"$.auth.<provider>.<field>"` | Встановлює одне поле контексту автентифікації провайдера для цього сеансу.  Поля - це ключі об'єкта [`--auth`](/command-line-usage/global-flags) для цього провайдера, наприклад `credentialsenvvar`, `password` або `token_url`. |
| `"$.providers.<alias>"` | Прив'язує псевдонім провайдера з власними обліковими даними, міткою tenancy, значеннями параметрів за замовчуванням і тонким налаштуванням.  Значення - це документ JSON у рядку, див. [Сеанси провайдерів](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Зберігає секрет у сховищі секретів сеансу.  Значення - це рядковий літерал, зазвичай документ JSON `auth`, на який посилаються з псевдоніма провайдера як на `auth_secret`. |
| `"$.secret_store"` | Вибирає постійне сховище секретів ОС, `{"backend":"os","namespace":"..."}`.  Потребує прапорця оператора `--allow-persistent-secrets`. |

Кожна форма впливає лише на поточний сеанс.  Незалежні підключення до сервера [`srv`](/command-line-usage/srv) не бачать присвоєнь одне одного, а кожен запит до сервера MCP - це окремий сеанс, тому під час використання сервера MCP виконуйте налаштування й запит в одному пакеті інструкцій.

* * *

## Приклади {#examples}

### Присвоєння значень змінним сеансу {#set-session-variables}

Кожна інструкція зберігає одне скалярне значення під указаним ім'ям, і значення зберігає свій тип: `region` - рядок, `minimum_count` - ціле число, а `enabled` - логічне значення.  `null` зберігається як значення, тому `$optional_value` згодом відповідає `NULL` у SQL, а не завершується помилкою як невизначена змінна.  Остання інструкція присвоює значення двом змінним в одному списку.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Явне зазначення області сеансу {#make-the-session-scope-explicit}

Перші дві інструкції ідентичні: `SESSION` називає єдину наявну область, і обидва присвоєння діють, доки значення змінної не скасовано або сеанс не завершився.  Використовуйте це ключове слово, коли скрипт має читатися так само, як у базі даних, що розрізняє налаштування сеансу та глобальні налаштування.  Закоментовану інструкцію наведено для порівняння: вона нічого не присвоює й завершується помилкою `configuration scope "global" is not supported; use SESSION`, оскільки між сеансами немає спільного стану.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Використання змінної в запиті {#use-a-variable-in-a-query}

Щоб розіменувати змінну, використовуйте `$name` без лапок у будь-якій позиції виразу.  Значення зберігають свій тип: рядки екрануються як літерали SQL, а числа, логічні значення й `null` передаються без змін.  Змінні в реченні `WHERE` визначаються до планування запиту, тому їх передають провайдеру так само, як літерали.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Скидання змінної {#reset-a-variable}

`DEFAULT` видаляє змінну так само, як це робить [`UNSET`](/language-spec/unset), тому подальше звернення до `$region` завершується помилкою `variable "$region" is not defined`, доки змінній знову не присвоять значення; закоментований запит показує цю помилку.  Це відрізняється від присвоєння `null`, яке залишає змінну на місці зі значенням `NULL`.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Копіювання значення з іншої змінної {#copy-a-value-from-another-variable}

Права частина може бути посиланням у подвійних лапках на наявну змінну; так копіюється значення, яке ця змінна має в цей момент.  Тут прив'язку провайдера спочатку зберігають як звичайну змінну з ім'ям `providers.saved` (крапки не роблять її конфігурацією), а потім активують, копіюючи її за шляхом `$.providers.production`.  Подальше видалення збереженої копії не впливає на активну прив'язку.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Перевизначення поля облікових даних для цього сеансу {#override-a-credential-field-for-this-session}

Контекст автентифікації провайдера - це об'єкт, переданий для нього через [`--auth`](/command-line-usage/global-flags) під час запуску.  Шлях у `$.auth` замінює одне поле цього об'єкта до кінця сеансу й залишає інші поля без змін.  Тут провайдеру GitHub указують іншу змінну середовища, що містить токен лише для читання; змінну зчитують із середовища процесу StackQL, і подальші запити до `github.*` використовують її, доки значення поля не скасовано або сеанс не завершився.

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
```

### Прив'язка псевдоніма провайдера {#bind-a-provider-alias}

Псевдонім провайдера - це другий, незалежно налаштований екземпляр провайдера, який використовують у запитах замість імені провайдера.  Значення - це документ JSON у рядку: `provider` називає базовий провайдер, `tenancy` позначає обліковий запис, `auth` - блок облікових даних у форматі псевдоніма (для AWS це `access_key_id_env_var` і `secret_access_key_env_var`, а не ключі `keyIDenvvar` і `credentialsenvvar` з `--auth`), а `params` надає значення за замовчуванням для параметрів запитів до API, як-от `region`.  Після цього запити до `production.s3.buckets` виконуються з цими обліковими даними, а `aws.s3.buckets` зберігає конфігурацію запуску.  Повний список ключів наведено на сторінці [Сеанси провайдерів](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
