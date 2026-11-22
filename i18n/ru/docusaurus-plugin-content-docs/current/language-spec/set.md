---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - переменные сеанса
  - set
  - сеансы провайдеров
  - секреты
description: "SET в StackQL: присваивание типизированных переменных сеанса и настройка псевдонимов провайдеров, полей учетных данных и секретов сеанса через цели с префиксом доллара."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Присваивает значение переменной сеанса или настраивает параметры среды выполнения для текущего сеанса. Доступно в StackQL `v1.0` и более поздних версиях.

См. также:  
[[` UNSET `]](/language-spec/unset) [[ Переменные сеанса ]](/getting-started/session-variables) [[ Сеансы провайдеров ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

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
Простой идентификатор (`region`), имя в двойных кавычках (`"providers.saved"`) или путь конфигурации в двойных кавычках с префиксом доллара (`"$.auth.aws.password"`).

__*literal*__  
Строка, число, логическое значение или `null`. Строки заключаются в одинарные кавычки. Массивы и объекты не являются скалярными значениями; храните JSON-документ в виде строки.

__*DEFAULT*__  
Сбрасывает цель, аналогично инструкции [`UNSET`](/language-spec/unset). Явное значение `null` - это сохраненное значение, а не сброс.

__*reference*__  
Ссылка `"$name"` в двойных кавычках на существующую переменную, например `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Необязательное ключевое слово; это единственная область действия, которая есть в StackQL. `SET SESSION name = ...` и `SET name = ...` - одна и та же инструкция: каждое присваивание относится к текущему сеансу и ни к чему больше. Ключевое слово существует, чтобы скрипты читались так же, как в базах данных, где `SET SESSION` и `SET GLOBAL` различаются. `SET GLOBAL` проходит синтаксический анализ, но отклоняется с ошибкой `configuration scope "global" is not supported; use SESSION`, так как состояния, общего для разных сеансов, нет.

Присваивания в одном списке `SET` применяются по порядку и не образуют транзакцию: если второе присваивание в списке завершается ошибкой, первое остается в силе.

## Цели {#targets}

| Цель | Действие |
|--|--|
| `name` или `"name"` | Сохраняет скалярную переменную сеанса, которая разыменовывается как `$name` в последующих инструкциях. Одни лишь точки не выбирают конфигурацию: `"providers.saved"` - это переменная с именем `providers.saved`. Прежнее написание `"variables.name"` - псевдоним для `name`. |
| `"$.auth.<provider>.<field>"` | Задает одно поле контекста аутентификации провайдера для этого сеанса. Поля - это ключи объекта [`--auth`](/command-line-usage/global-flags) для этого провайдера, например `credentialsenvvar`, `password` или `token_url`. |
| `"$.providers.<alias>"` | Привязывает псевдоним провайдера с собственными учетными данными, меткой tenancy, значениями параметров по умолчанию и настройками. Значение - это JSON-документ в строке, см. [Сеансы провайдеров](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Сохраняет секрет в хранилище секретов сеанса. Значение - строковый литерал, обычно JSON-документ `auth`, на который псевдоним провайдера ссылается как `auth_secret`. |
| `"$.secret_store"` | Выбирает постоянное хранилище секретов ОС, `{"backend":"os","namespace":"..."}`. Требуется флаг оператора `--allow-persistent-secrets`. |

Каждая форма влияет только на текущий сеанс. Независимые подключения к серверу [`srv`](/command-line-usage/srv) не видят присваиваний друг друга, а каждый запрос к MCP-серверу - это отдельный сеанс, поэтому при использовании MCP-сервера настраивайте сеанс и выполняйте запросы в одном пакете инструкций.

* * *

## Примеры {#examples}

### Задание переменных сеанса {#set-session-variables}

Каждая инструкция сохраняет одно скалярное значение под заданным именем, и значение не теряет свой тип: `region` - строка, `minimum_count` - целое число, а `enabled` - логическое значение. `null` хранится как значение, поэтому `$optional_value` позже соответствует значению SQL `NULL`, а не завершается ошибкой как неопределенная переменная. Последняя инструкция присваивает две переменные в одном списке.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Явное указание области сеанса {#make-the-session-scope-explicit}

Первые две инструкции идентичны: `SESSION` называет единственную существующую область, и оба присваивания действуют, пока значение переменной не будет сброшено или сеанс не завершится. Используйте это ключевое слово, когда скрипт должен читаться так же, как в базе данных, которая различает настройки сеанса и глобальные настройки. Закомментированная инструкция показана для сравнения: она ничего не присваивает и завершается ошибкой `configuration scope "global" is not supported; use SESSION`, так как между сеансами нет общего состояния.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Использование переменной в запросе {#use-a-variable-in-a-query}

Разыменуйте переменную с помощью `$name` без кавычек в любой позиции выражения. Значения сохраняют свой тип: строки экранируются как SQL-литералы, а числа, логические значения и `null` передаются без изменений. Переменные в предложении `WHERE` определяются до планирования запроса, поэтому передаются провайдеру так же, как литералы.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Сброс переменной {#reset-a-variable}

`DEFAULT` удаляет переменную точно так же, как это делает инструкция [`UNSET`](/language-spec/unset), поэтому последующая ссылка `$region` завершается ошибкой `variable "$region" is not defined`, пока переменной снова не будет присвоено значение; закомментированный запрос показывает эту ошибку. Это отличается от присваивания `null`, при котором переменная остается на месте со значением `NULL`.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Копирование значения из другой переменной {#copy-a-value-from-another-variable}

Правая часть может быть ссылкой в двойных кавычках на существующую переменную; при этом копируется значение, которое переменная содержит в этот момент. Здесь привязка провайдера сначала сохраняется как обычная переменная с именем `providers.saved` (точки не делают ее конфигурацией), а затем активируется копированием в путь `$.providers.production`. Последующее удаление сохраненной копии не влияет на активную привязку.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Переопределение поля учетных данных для этого сеанса {#override-a-credential-field-for-this-session}

Контекст аутентификации провайдера - это объект, переданный для него через флаг [`--auth`](/command-line-usage/global-flags) при запуске. Путь внутри `$.auth` заменяет одно поле этого объекта до конца сеанса и оставляет остальные поля без изменений. Здесь провайдер GitHub перенаправляется на другую переменную среды, содержащую токен только для чтения; переменная считывается из среды процесса StackQL, и последующие запросы к `github.*` используют ее, пока значение поля не будет сброшено или сеанс не завершится.

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
```

### Привязка псевдонима провайдера {#bind-a-provider-alias}

Псевдоним провайдера - это второй, независимо настроенный экземпляр провайдера, который используется в запросах вместо имени провайдера. Значение - это JSON-документ в строке: `provider` задает базовый провайдер, `tenancy` - метку учетной записи, `auth` - блок учетных данных в формате псевдонима (для AWS - `access_key_id_env_var` и `secret_access_key_env_var`, а не ключи `keyIDenvvar` и `credentialsenvvar`, как во флаге `--auth`), а `params` - значения по умолчанию для параметров запросов, например `region`. Запросы к `production.s3.buckets` затем выполняются с этими учетными данными, а `aws.s3.buckets` сохраняет конфигурацию, заданную при запуске. Полный список ключей приведен на странице [Сеансы провайдеров](/getting-started/provider-sessions#provider-aliases).

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
