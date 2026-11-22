---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Команда stackql srv запускає StackQL як сервер мережевого протоколу PostgreSQL, щоб psql, інструменти BI і драйвери мов могли виконувати запити SQL до API хмар і SaaS."
image: "/img/stackql-featured-image.png"
---

Команда для запуску StackQL як служби, до якої потім можуть звертатися клієнти [мережевого протоколу Postgres](https://www.postgresql.org/docs/current/protocol.html), яких можна автентифікувати за допомогою mTLS. Команда `srv` також може запускати сервер [Model Context Protocol (MCP)](/command-line-usage/mcp) поряд із сервером мережевого протоколу PostgreSQL, що забезпечує доступ за двома протоколами як для традиційних клієнтів баз даних, так і для агентів ШІ.

* * * 

### Синтаксис {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

Хоча сервер StackQL використовує мережевий протокол Postgres, він не є сервером Postgres і не потребує його.  Клієнт використовує мережевий протокол для зв'язку із сервером, який за допомогою StackQL виконує запити або операції DML щодо віддалених постачальників хмарних послуг і SaaS та повертає результати клієнтові.

:::

Параметри, надіслані через розширений протокол запитів (`Parse` / `Bind` / `Execute`, як у `psycopg` 3, `pgx` і JDBC), прив'язуються на боці сервера як рядкові літерали.  Починаючи з `v0.12.742`, заповнювач `$n` усередині рядкового літерала, ідентифікатора в лапках або коментаря залишається без змін, зворотні скісні риски в значеннях параметрів екрануються, а текстове значення `NULL` залишається рядком `NULL` (значення `NULL` у SQL передається окремо).

### Параметри сервера {#server-options}

| Параметр | Опис | За замовчуванням |
|--|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|Адреса, яку прослуховує сервер.|`0.0.0.0`|
|<span class="nowrap">`--pgsrv.port`</span>|Порт TCP, який прослуховує сервер.|`5466`|
|<span class="nowrap">`--pgsrv.tls`</span>|Об'єкт конфігурації TLS у форматі JSON. Див. [приклад mTLS](#examples) нижче.|немає|
|<span class="nowrap">`--pgsrv.debug.enable`</span>|Вмикає налагоджувальний вивід запитів для клієнтів PostgreSQL, наприклад `stackql srv --pgsrv.debug.enable=true`. Не вмикайте, якщо клієнти не повинні отримувати налагоджувальну інформацію.|`false`|
|<span class="nowrap">`--pgsrv.sundry`</span>|Розширене відображення JSON або YAML, що передається серверу мережевого протоколу PostgreSQL лише тоді, коли прапорець `--pgsrv.tls` не встановлено.|`{}`|
|<span class="nowrap">`--pgsrv.loglevel`</span>|Приймається, але не встановлює рівень журналювання в поточному рушії. Натомість використовуйте `--loglevel`.|`WARN`|

<br/>

> додаткові параметри див. на сторінці [Глобальні прапорці](/command-line-usage/global-flags)

### Параметри сервера MCP {#mcp-server-options}

Під час запуску команди `srv` з підтримкою MCP доступні такі додаткові параметри:

| Параметр | Опис |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|Тип сервера MCP: `http` (у пам'яті) або `reverse_proxy` (на основі TCP)|
|<span class="nowrap">`--mcp.config`</span>|Об'єкт конфігурації сервера MCP у форматі JSON (див. [документацію MCP](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|Кодування журналу аудиту MCP: `jsonl` (за замовчуванням) або `otel` (записи журналу OTLP/JSON); перевизначає `server.audit.format` в об'єкті конфігурації|
|<span class="nowrap">`--mcp.protocol.version`</span>|Найновіша ревізія протоколу MCP, яку оголошує сервер: `auto` (за замовчуванням), `2026-07-28` (лише в режимі без сеансу) або старіша ревізія, наприклад `2025-11-25`; перевизначає `server.protocol_version` в об'єкті конфігурації|

<br/>

:::info[Несумісна зміна у v0.12.742]

Починаючи з `v0.12.742`, транспорт HTTP для MCP (`--mcp.server.type=http` або `reverse_proxy`) не запускається, якщо в об'єкті конфігурації не вказано токен bearer через `server.auth_token_env_var` або автентифікацію не вимкнено явно за допомогою `"allow_unauthenticated": true`.  Див. [Автентифікація клієнтів HTTP](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

Докладні відомості про налаштування й використання сервера MCP, зокрема про режими розгортання, параметри конфігурації та інтеграцію з асистентами ШІ, див. у [документації до команди MCP](/command-line-usage/mcp).

:::

:::info

Перед запуском сервера потрібно встановити змінні середовища, необхідні для автентифікації провайдерів; докладніше див. [Використання провайдера](/getting-started/using-a-provider).

:::

Для серверів, що працюють тривалий час, прапорець [`--env.file`](/command-line-usage/global-flags) дає змогу перед запуском розмістити облікові дані у файлі в стилі dotenv на диску або виконати їх ротацію, замість того щоб експортувати змінні в оболонку:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

Для команди `stackql srv` файл зчитується один раз під час запуску; для повторного завантаження під час сеансу призначено інструмент MCP [`reload_credentials`](/mcp/reload_credentials).

### Прапорці {#flags}

| Прапорець | Опис |
|--|--|
|`-h,--help`|Вивести довідку|
|`-v,--verbose`|Виконувати запити в докладному режимі з додатковим виводом у stdout; якщо встановлено глобальний прапорець `-f` (`--outfile`),<br/>цю додаткову інформацію журналювання буде записано у файл виводу разом із результатами запитів|

<br/>

> додаткові параметри див. на сторінці [Глобальні прапорці](/command-line-usage/global-flags)

* * *

### Приклади {#examples}

Запуск процесу сервера StackQL і підключення за допомогою клієнта командного рядка `psql`.  

Спочатку завантажте файл конфігурації `openssl.cnf` з [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).  

Далі згенеруйте файли сертифікатів і ключів, які сервер і клієнт використовуватимуть для автентифікації за допомогою взаємного TLS (mTLS).  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Тепер встановіть змінні середовища; якщо клієнт і сервер працюють на одній машині, ці змінні можуть бути спільними.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Встановіть облікові дані для провайдерів, потрібних серверу.  Для GitHub провайдер зчитує ім'я користувача й персональний токен доступу з цих двох змінних:  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

Тепер запустіть сервер в одному терміналі.  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

В іншому терміналі підключіться до сервера за допомогою клієнта командного рядка `psql`.  

```bash
psql -h 127.0.0.1 -d stackql
```

Тепер виконайте запит StackQL до автентифікованого провайдера.  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

stackql=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

stackql=>
```
