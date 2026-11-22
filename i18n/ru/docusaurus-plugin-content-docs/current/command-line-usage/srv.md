---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Команда stackql srv запускает StackQL как сервер сетевого протокола PostgreSQL, чтобы psql, инструменты BI и драйверы языков могли выполнять SQL-запросы к API облачных сервисов и SaaS."
image: "/img/stackql-featured-image.png"
---

Команда для запуска StackQL в качестве сервиса, к которому затем могут обращаться клиенты [сетевого протокола Postgres](https://www.postgresql.org/docs/current/protocol.html), прошедшие аутентификацию с помощью mTLS. Команда `srv` также может запускать сервер [Model Context Protocol (MCP)](/command-line-usage/mcp) параллельно с сервером сетевого протокола PostgreSQL, обеспечивая доступ по двум протоколам как для традиционных клиентов баз данных, так и для ИИ-агентов.

* * * 

### Синтаксис {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

Хотя сервер StackQL использует сетевой протокол Postgres, это не сервер Postgres, и сервер Postgres ему не нужен.  Сетевой протокол служит клиенту для связи с сервером, который с помощью StackQL выполняет запросы или операции DML к удаленным поставщикам облачных услуг и SaaS и возвращает результаты клиенту.

:::

Параметры, передаваемые по расширенному протоколу запросов (`Parse` / `Bind` / `Execute`, как в `psycopg` 3, `pgx` и JDBC), привязываются на стороне сервера как строковые литералы.  Начиная с `v0.12.742` заполнитель `$n` внутри строкового литерала, идентификатора в кавычках или комментария остается без изменений, обратные косые черты в значениях параметров экранируются, а текстовое значение `NULL` остается строкой `NULL` (SQL-значение `NULL` передается отдельно).

### Параметры сервера {#server-options}

| Параметр | Описание | По умолчанию |
|--|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|Адрес, который прослушивает сервер.|`0.0.0.0`|
|<span class="nowrap">`--pgsrv.port`</span>|Порт TCP, который прослушивает сервер.|`5466`|
|<span class="nowrap">`--pgsrv.tls`</span>|JSON-объект конфигурации TLS. См. [пример mTLS](#examples) ниже.|нет|
|<span class="nowrap">`--pgsrv.debug.enable`</span>|Включает отладочный вывод запросов для клиентов PostgreSQL, например `stackql srv --pgsrv.debug.enable=true`. Не включайте, если клиенты не должны получать отладочную информацию.|`false`|
|<span class="nowrap">`--pgsrv.sundry`</span>|Расширенный словарь JSON или YAML, который передается серверу сетевого протокола PostgreSQL, только если флаг `--pgsrv.tls` не задан.|`{}`|
|<span class="nowrap">`--pgsrv.loglevel`</span>|Принимается, но в текущей версии движка не задает уровень журналирования. Используйте вместо него `--loglevel`.|`WARN`|

<br/>

> дополнительные параметры см. на странице [Глобальные флаги](/command-line-usage/global-flags)

### Параметры MCP-сервера {#mcp-server-options}

При запуске команды `srv` с поддержкой MCP доступны следующие дополнительные параметры:

| Параметр | Описание |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|Тип MCP-сервера: `http` (в памяти) или `reverse_proxy` (на основе TCP)|
|<span class="nowrap">`--mcp.config`</span>|JSON-объект конфигурации MCP-сервера (см. [документацию по MCP](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|Кодировка журнала аудита MCP: `jsonl` (по умолчанию) или `otel` (записи журнала OTLP/JSON); переопределяет `server.audit.format` в объекте конфигурации|
|<span class="nowrap">`--mcp.protocol.version`</span>|Новейшая объявляемая ревизия протокола MCP: `auto` (по умолчанию), `2026-07-28` (только в режиме без сеанса) или более старая ревизия, например `2025-11-25`; переопределяет `server.protocol_version` в объекте конфигурации|

<br/>

:::info[Критическое изменение в v0.12.742]

Начиная с `v0.12.742` HTTP-транспорт MCP (`--mcp.server.type=http` или `reverse_proxy`) отказывается запускаться, если в объекте конфигурации не указан токен bearer через `server.auth_token_env_var` и нет явного отказа от аутентификации через `"allow_unauthenticated": true`.  См. [Аутентификация HTTP-клиентов](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

Подробные сведения о настройке и использовании MCP-сервера, включая режимы развертывания, параметры конфигурации и интеграцию с ИИ-ассистентами, см. в [документации по команде MCP](/command-line-usage/mcp).

:::

:::info

Перед запуском сервера задайте переменные среды, которые требуются для аутентификации провайдеров; подробнее см. [Использование провайдера](/getting-started/using-a-provider).

:::

Для долго работающих серверов флаг [`--env.file`](/command-line-usage/global-flags) позволяет перед запуском разместить или сменить учетные данные в файле в стиле dotenv на диске, а не экспортировать переменные в оболочку:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

Для `stackql srv` файл читается один раз при запуске; для повторной загрузки во время сеанса служит MCP-инструмент [`reload_credentials`](/mcp/reload_credentials).

### Флаги {#flags}

| Флаг | Описание |
|--|--|
|`-h,--help`|Вывести справку|
|`-v,--verbose`|Выполнять запросы в подробном режиме с дополнительным выводом в stdout; если задан глобальный флаг `-f` (`--outfile`),<br/>эти дополнительные сведения журнала записываются в выходной файл вместе с результатами запросов|

<br/>

> дополнительные параметры см. на странице [Глобальные флаги](/command-line-usage/global-flags)

* * *

### Примеры {#examples}

Запуск серверного процесса StackQL и подключение к нему с помощью клиента командной строки `psql`.  

Сначала скачайте файл конфигурации `openssl.cnf` из [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).  

Затем создайте файлы сертификатов и ключей, которые сервер и клиент будут использовать для аутентификации по взаимному TLS (mTLS).  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Теперь задайте переменные среды; если клиент и сервер находятся на одной машине, их можно использовать совместно.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Задайте учетные данные для провайдеров, которые нужны серверу.  Для GitHub провайдер читает имя пользователя и персональный токен доступа из этих двух переменных:  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

Теперь запустите сервер в одном терминале.  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

В другом терминале подключитесь к серверу с помощью клиента командной строки `psql`.  

```bash
psql -h 127.0.0.1 -d stackql
```

Теперь выполните запрос StackQL к провайдеру, прошедшему аутентификацию.  

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
