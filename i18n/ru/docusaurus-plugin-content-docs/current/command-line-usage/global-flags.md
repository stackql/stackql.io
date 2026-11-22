---
title: Глобальные флаги
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Глобальные флаги двоичного файла stackql для любой команды: аутентификация, формат вывода, расположение реестра, ведение журнала и настройки SQL-бэкенда."
image: "/img/stackql-featured-image.png"
---

Глобальные флаги принимаются командами StackQL, но некоторые из них действуют только в определенном режиме. Задавайте их в командной строке или, если это поддерживается, в файле инициализации StackQL ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)). Флаги `--pgsrv.*` перечислены в разделе [Параметры сервера PostgreSQL](/command-line-usage/srv#server-options).

| Флаг | Тип данных | Описание | По умолчанию |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Конфигурация доступа вызывающих сторон, JSON-конверт `{"path": "/path/to/access.json"}` (также принимается просто путь к файлу). Включает [управление доступом](/command-line-usage/access-management): вызывающие стороны аутентифицируются по JWT, статическому токену bearer, взаимному TLS или удостоверению локальной ОС, а инструкции авторизуются по правилам политики. Локальные учетные данные в виде токена считываются из `STACKQL_CALLER_TOKEN`. Доступно в StackQL `v1.0` и более поздних версиях. | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Разрешает сеансу выбрать постоянное хранилище секретов ОС (macOS Keychain, Linux Secret Service или Windows Credential Manager) с помощью инструкции `SET "$.secret_store"`. Хранилища в виде простых файлов отклоняются даже с этим флагом. См. [Сеансы провайдеров и секреты](/getting-started/provider-sessions#persistent-os-secret-stores). Доступно в StackQL `v1.0` и более поздних версиях. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Разрешает разыменование значений секретов и учетных данных в SQL-выражениях (`"$secrets.name"`, `"$auth.provider.field"`), из-за чего они могут попасть в результаты, журналы и сохраняемые артефакты запросов. Для обычной аутентификации провайдеров не требуется. См. [Значения секретов в SQL](/getting-started/provider-sessions#secret-values-in-sql). Доступно в StackQL `v1.0` и более поздних версиях. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | Тайм-аут запроса к API в секундах, 0 - без тайм-аута | `45` |
| <span class="nowrap">`--approot`</span> | string | Корневой путь конфигурации и кэша приложения | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Режим файлов конфигурации и кэша приложения | `493` |
| <span class="nowrap">`--auth`</span> | string | Пары "ключ-значение" контекста `auth` в формате json |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Начальное количество ключей кэша | `100` |
| <span class="nowrap">`--configfile`</span> | string | Полный путь к файлу конфигурации | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | Файл `cpuprofile`; если значение пустое, файл не создается | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | integer | Максимальная глубина зависимостей потока данных для одного запроса | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | integer | Максимальное число слабо связанных компонентов потока данных для одного запроса | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | Строка JSON / YAML для настройки обработки служебных запросов СУБД | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Разделитель для вывода csv. Только один символ. Игнорируется для любого вывода, кроме csv | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | Флаг `dryrun`; выполняется только препроцессор, возвращается вывод после шаблонизации | `false` |
| <span class="nowrap">`--env.file`</span> | string | Необязательный файл в стиле dotenv, который загружается в среду процесса при запуске (для всех команд); MCP-инструмент [`reload_credentials`](/mcp/reload_credentials) повторно загружает его по требованию. Отсутствие файла допускается; ключи с пустыми значениями пропускаются; существующие переменные перезаписываются. Значение флага не берется из `.stackqlrc`. Доступно в выпусках StackQL после `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | integer | Ограничение параллелизма при выполнении запросов | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Префикс псевдонима экспорта (пространство имен или схема) | |
| <span class="nowrap">`--gc`</span> | string | Строка JSON / YAML с конфигурацией GC | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Отключает заголовки столбцов; действует только с `--output csv` или `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Выводить сведения об HTTP-запросах в терминал | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Хост прокси (оставьте пустым, чтобы не использовать прокси) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Пароль прокси | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Порт прокси; при любом числе, меньшем или равном 0, используется порт по умолчанию для соответствующей схемы (например, `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Схема прокси, `http` или `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Пользователь прокси | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Максимальное число результатов на один HTTP-запрос; любое число, меньшее или равное 0, снимает ограничение | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Максимальное число страниц результатов, возвращаемых для одного ресурса; любое число, меньшее или равное 0, снимает ограничение | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Максимальная глубина косвенных запросов: представлений и вложенных запросов | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Входной файл (файл IQL), из которого считываются запросы | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Файл контекста (данных) для шаблонизации (файл `json` или `jsonnet`)| |
| <span class="nowrap">`--loglevel`</span> | string | Уровень ведения журнала, должен быть одним из \{`panic`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`\} (`warning` принимается как псевдоним для `warn`) | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | Объект конфигурации JSON / YAML для [MCP-сервера](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` и `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | Кодировка журнала аудита MCP, `jsonl` или `otel` (записи журнала OTLP/JSON); переопределяет `server.audit.format` в `--mcp.config`. См. [Формат журнала](/command-line-usage/mcp#log-format). Доступно в StackQL `v0.11.660` и более поздних версиях. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Самая новая объявляемая ревизия протокола MCP: `auto`, `2026-07-28` (только в режиме без сеанса) или более старая ревизия, например `2025-11-25`; переопределяет `server.protocol_version` в `--mcp.config`. См. [Закрепление ревизии](/command-line-usage/mcp#pinning-the-revision). Доступно в StackQL `v0.12.732` и более поздних версиях. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | Тип MCP-сервера, одно из значений \{`http`, `stdio`, `reverse_proxy`\}. См. [Тип MCP-сервера](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL кэшированных документов метаданных в секундах | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | Строка JSON / YAML с пространствами имен для кэширования, представлений и т. д. | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Работать в автономном режиме, используя кэшированные данные | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Выходной файл, в который записываются результаты | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Формат вывода, должен быть одним из \{`json`, `jsonl`, `table`, `csv`, `text`, `otel`\} (`ndjson` принимается как псевдоним для `jsonl`). Формат `otel` выдает записи журнала OpenTelemetry, см. [Режимы вывода](/getting-started/output-modes#otel-output); доступно в StackQL `v0.11.660` и более поздних версиях. Если в `--otel.config` настроен экспортер, записи `otel` также отправляются в эту конечную точку | `table` |
| <span class="nowrap">`--otel.config`</span> | string | Строка JSON / YAML, настраивающая формат вывода `otel`; ключи `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms` и `exporter.batch_size` задают отправку записей в конечную точку журналов OTLP/HTTP, см. [Отправка в конечную точку OTLP](/getting-started/output-modes#pushing-to-an-otlp-endpoint). Доступно в StackQL `v0.12.732` и более поздних версиях | `{}` |
| <span class="nowrap">`--preview`</span> | string | Строка JSON, включающая возможности предварительной версии. `{"unstable": true}` открывает доступ к управляемым документами отношениям `stackql_unstable_<provider>.<service>.<resource>`, которые определяются и выполняются с помощью `omnisdk` непосредственно по документам провайдеров, а строки передаются потоком по мере их получения; остальные ключи (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) настраивают этот бэкенд. Возможности предварительной версии нестабильны: поведение, синтаксис и вывод могут измениться или перестать работать между выпусками без предупреждения, поэтому не полагайтесь на них в рабочей среде. Начиная с `v1.0` то же явное включение открывает доступ к отношениям `stackql_preview.iac.run` и `stackql_preview.iac.destroy`, которые применяют и удаляют объявленный набор ресурсов через `omnisdk`, сохраняя состояние в корневом каталоге приложения. См. [Возможности предварительной версии](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--provider`</span> | string | Начальный провайдер по умолчанию. Например, с `--provider github` имя `repos.repos` соответствует `github.repos.repos` без предшествующей инструкции `USE`. Не выполняет аутентификацию у провайдера. | нет |
| <span class="nowrap">`--querycachesize`</span> | integer | Размер кэша LRU для планов запросов в количестве записей | `10000` |
| <span class="nowrap">`--registry`</span> | string | Строка JSON / YAML, задающая расположение реестра провайдеров и проверку подписей документов провайдеров | см. [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | Строка JSON / YAML с конфигурацией сеанса | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | Строка JSON / YAML с конфигурацией системы SQL-бэкенда | см. [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | Строка JSON / YAML с конфигурацией хранилища Txn | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Путь к пакету сертификатов CA; если не указан, используются системные значения по умолчанию |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Разрешить доверие к небезопасным сертификатам (не рекомендуется) |  |
| <span class="nowrap">`--upstream.errors`</span> | string | Политика JSON / YAML для явного допуска кодов состояния HTTP-ответов при чтении из провайдеров. См. [`--upstream.errors`](#--upstream-errors). | `{}` |
| <span class="nowrap">`--var`</span> | string | Внешние переменные в виде пар "ключ-значение" для подстановки в запросы StackQL через файлы данных `jsonnet` |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Подробный вывод | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Контекстная справка по stackql | |
| <span class="nowrap">`--version`</span> | | Выводит версию программы StackQL | |

Флаг `--colorscheme` устарел и больше не меняет цвета оболочки. Флаг `--usenonpreferredapis` принимается, но в текущем движке не влияет на выбор API.

## `--registry` {#--registry}

Флаг `--registry` задает, откуда считываются документы провайдеров и проверяются ли их подписи. Значение - строка JSON со следующими ключами:

| Ключ | Описание | По умолчанию |
| -- | -- | -- |
| `url` | URL-адрес реестра. Если не указан, используется общедоступный реестр провайдеров StackQL. | общедоступный реестр |
| `localDocRoot` | Локальный каталог с документами провайдеров, которые считываются вместо скачивания из реестра. Используется при [разработке провайдера](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | Префикс пути в корне реестра, по которому находятся исходные документы провайдеров. | `src` |
| `verifyConfig.nopVerify` | Значение `true` отключает проверку подписей. Задайте `false`, чтобы требовать встроенную подпись ML-DSA-65 (FIPS 204) в каждом считываемом документе провайдера и сервиса, включая отношения предварительной версии на основе документов. | `true` |
| `verifyConfig.publicKeys` | JSON-объект, сопоставляющий идентификаторы ключей с открытыми ключами ML-DSA-65 в шестнадцатеричной кодировке, для частных реестров. Он заменяет встроенные рабочие ключи; явно заданный пустой объект считается ошибкой. | встроенные ключи |

### Пример: требование подписанных документов провайдеров {#example-require-signed-provider-documents}

Включает проверку каждого документа провайдера и сервиса, считываемого из общедоступного реестра, по встроенным рабочим ключам:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Пример: чтение неподписанных документов из локального каталога {#example-read-unsigned-documents-from-a-local-directory}

Направляет реестр на каталог с документами провайдеров в разработке. Эти документы не подписаны, поэтому проверка остается отключенной:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

Проверка подписей доступна в StackQL `v1.0` и более поздних версиях и по умолчанию отключена. Подписи охватывают точные байты YAML документа, поэтому любое изменение, включая изменение форматирования или символов новой строки, делает их недействительными; подпишите документ заново с помощью инструмента реестра `pqsig sign-doc`. Для неподписанных локальных документов в разработке нужно оставить `nopVerify: true`. Прежние параметры сертификатов и файлы sidecar `.sig` больше не используются, а архивы, опубликованные только с файлами sidecar, нужно опубликовать заново, прежде чем использовать их с проверкой. Проверка выполняет аутентификацию документов при их чтении; скачивание и извлечение архивов ею не охватываются.

:::

## `--sqlBackend` {#--sqlbackend}

Флаг `--sqlBackend` настраивает внешний SQL-бэкенд для StackQL, что позволяет использовать внешний движок базы данных (например, PostgreSQL) вместо встроенного SQL-движка по умолчанию. Это полезно, если нужно использовать внешнюю базу данных для обработки запросов, постоянного хранения или интеграции с существующей инфраструктурой баз данных.

Значение - строка JSON со следующими ключами:

| Ключ | Описание | Примеры значений |
| -- | -- | -- |
| `dbEngine` | Тип движка базы данных | `postgres_tcp` |
| `sqlDialect` | Используемый диалект SQL | `postgres` |
| `dsn` | Имя источника данных (строка подключения) для базы данных | См. примеры ниже |

### Пример: использование бэкенда PostgreSQL {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Пример: использование интерфейса PostgreSQL в Databricks {#example-using-databricks-postgresql-interface}

Databricks предоставляет совместимый с PostgreSQL интерфейс, который можно использовать как SQL-бэкенд StackQL. DSN использует стандартный формат строки подключения PostgreSQL с вашими учетными данными Databricks:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Специальные символы в имени пользователя или пароле (например, `@`) в DSN нужно закодировать для URL (например, `@` превращается в `%40`).
:::

## `--upstream.errors` {#--upstream-errors}

Используйте ключ `http.ignoreStatusCodes`, чтобы перечислить коды состояния HTTP-ответов, которые чтение из провайдера должно явно допускать. Элементы - строки: укажите точный код, например `"503"`, или класс, например `"5xx"`. Значение по умолчанию `{}` не добавляет исключений к обычной обработке ответов upstream движком. Этот параметр не повторяет неудачные запросы и не применяется к изменениям данных и обменам при аутентификации.

Проигнорированный ответ не добавляет строк. Другие операции чтения в запросе по-прежнему могут возвращать строки, поэтому успешный запрос может вернуть неполные результаты. StackQL сообщает о проигнорированном коде состояния HTTP в виде диагностического сообщения.

Например, этот параметр используется в [тесте соединения с имитацией GitHub](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104):

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

В тесте запрос к API collaborators для одного репозитория возвращает HTTP `500`. SQL-запрос все равно возвращает строки collaborators для остальных репозиториев, а stderr содержит `ignoring upstream response: 500 INTERNAL SERVER ERROR`. Если ожидается только один сбой, лучше указывать точный код состояния.
