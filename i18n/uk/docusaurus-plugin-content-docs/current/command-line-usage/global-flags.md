---
title: Глобальні прапорці
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Глобальні прапорці двійкового файлу stackql: автентифікація, формат виводу, розташування реєстру, журналювання та налаштування бекенду SQL, доступні для кожної команди."
image: "/img/stackql-featured-image.png"
---

Команди StackQL приймають глобальні прапорці, але деякі з них діють лише в певному режимі. Вказуйте їх у командному рядку або, де це підтримується, у файлі ініціалізації StackQL ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)). Прапорці `--pgsrv.*` перелічено в розділі [Параметри сервера PostgreSQL](/command-line-usage/srv#server-options).

| Прапорець | Тип даних | Опис | За замовчуванням |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Конфігурація доступу сторін, що викликають, у вигляді обгортки JSON `{"path": "/path/to/access.json"}` (простий шлях до файлу також приймається). Вмикає [керування доступом](/command-line-usage/access-management): сторони, що викликають, автентифікуються за JWT, статичним токеном bearer, взаємним TLS або ідентичністю локальної ОС, а інструкції авторизуються за правилами політики. Локальні облікові дані у вигляді токенів зчитуються з `STACKQL_CALLER_TOKEN`. Доступно в StackQL `v1.0` і новіших версіях. | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Дозволяє сеансу вибрати постійне сховище секретів ОС (macOS Keychain, Linux Secret Service або Windows Credential Manager) за допомогою `SET "$.secret_store"`. Сховища у вигляді простих файлів відхиляються навіть із цим прапорцем. Див. [Сеанси провайдерів і секрети](/getting-started/provider-sessions#persistent-os-secret-stores). Доступно в StackQL `v1.0` і новіших версіях. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Дозволяє розіменування значень секретів і облікових даних у виразах SQL (`"$secrets.name"`, `"$auth.provider.field"`), унаслідок чого вони можуть потрапити в результати, журнали та збережені артефакти запитів. Не потрібен для звичайної автентифікації провайдерів. Див. [Значення секретів у SQL](/getting-started/provider-sessions#secret-values-in-sql). Доступно в StackQL `v1.0` і новіших версіях. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | Час очікування запиту до API в секундах, 0 - без обмеження часу очікування | `45` |
| <span class="nowrap">`--approot`</span> | string | Кореневий шлях для конфігурації та кешу застосунку | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Режим файлів конфігурації та кешу застосунку | `493` |
| <span class="nowrap">`--auth`</span> | string | Пари "ключ-значення" контексту `auth` у форматі json |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Початкова кількість ключів кешу | `100` |
| <span class="nowrap">`--configfile`</span> | string | Повний шлях до файлу конфігурації | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | Файл `cpuprofile`; якщо порожньо, не використовується | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | integer | Максимальна глибина залежностей потоку даних для одного запиту | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | integer | Максимальна кількість слабко зв'язних компонентів потоку даних для одного запиту | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | Рядок JSON / YAML для налаштування обробки службових запитів СКБД | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Роздільник для виводу csv. Лише один символ. Ігнорується для всіх форматів виводу, крім csv | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | Прапорець `dryrun`; виконується лише препроцесор, повертається вивід, сформований із шаблону | `false` |
| <span class="nowrap">`--env.file`</span> | string | Необов'язковий файл у стилі dotenv, який завантажується в середовище процесу під час запуску (для всіх команд); інструмент MCP [`reload_credentials`](/mcp/reload_credentials) повторно завантажує його на вимогу. Відсутність файлу допускається; ключі з порожніми значеннями пропускаються; наявні змінні перезаписуються. Не зчитується з `.stackqlrc`. Доступно у випусках StackQL після `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | integer | Обмеження паралельності під час виконання запитів | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Префікс псевдоніма експорту (простір імен або схема) | |
| <span class="nowrap">`--gc`</span> | string | Рядок JSON / YAML з конфігурацією GC | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Вимикає заголовки стовпців, діє лише з `--output csv` або `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Відображати відомості про запити HTTP у терміналі | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Хост проксі (залиште порожнім, щоб не використовувати проксі) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Пароль проксі | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Порт проксі; будь-яке число, що не перевищує 0, означає порт за замовчуванням для відповідної схеми (наприклад, `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Схема проксі, `http` або `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Користувач проксі | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Максимальна кількість результатів на один запит HTTP; будь-яке число, що не перевищує 0, означає відсутність обмеження | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Максимальна кількість сторінок результатів, що повертаються для одного ресурсу; будь-яке число, що не перевищує 0, означає відсутність обмеження | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Максимальна глибина непрямих запитів: подань і підзапитів | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Вхідний файл (файл IQL), з якого зчитуються запити | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Файл контексту (даних) для шаблонізації (файл `json` або `jsonnet`)| |
| <span class="nowrap">`--loglevel`</span> | string | Рівень журналювання, має бути одним із \{`panic`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`\} (`warning` приймається як псевдонім для `warn`) | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | Об'єкт конфігурації JSON / YAML для [сервера MCP](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` і `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | Кодування журналу аудиту MCP, `jsonl` або `otel` (записи журналу OTLP/JSON); перевизначає `server.audit.format` у `--mcp.config`. Див. [Формат журналу](/command-line-usage/mcp#log-format). Доступно в StackQL `v0.11.660` і новіших версіях. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Найновіша ревізія протоколу MCP, яку оголошує сервер: `auto`, `2026-07-28` (лише в режимі без сеансу) або старіша ревізія, наприклад `2025-11-25`; перевизначає `server.protocol_version` у `--mcp.config`. Див. [Закріплення ревізії](/command-line-usage/mcp#pinning-the-revision). Доступно в StackQL `v0.12.732` і новіших версіях. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | Тип сервера MCP, одне зі значень \{`http`, `stdio`, `reverse_proxy`\}. Див. [Тип сервера MCP](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL кешованих документів метаданих, у секундах | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | Рядок JSON / YAML, що описує простори імен для кешування, подань тощо | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Працювати офлайн, використовуючи кешовані дані | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Файл виводу, у який записуються результати | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Формат виводу, має бути одним із \{`json`, `jsonl`, `table`, `csv`, `text`, `otel`\} (`ndjson` приймається як псевдонім для `jsonl`). Формат `otel` видає записи журналу OpenTelemetry, див. [Режими виводу](/getting-started/output-modes#otel-output); доступно в StackQL `v0.11.660` і новіших версіях. Якщо в `--otel.config` налаштовано експортер, записи `otel` також надсилаються до цієї кінцевої точки | `table` |
| <span class="nowrap">`--otel.config`</span> | string | Рядок JSON / YAML, що налаштовує формат виводу `otel`; `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms` і `exporter.batch_size` керують надсиланням записів до кінцевої точки журналів OTLP/HTTP, див. [Надсилання до кінцевої точки OTLP](/getting-started/output-modes#pushing-to-an-otlp-endpoint). Доступно в StackQL `v0.12.732` і новіших версіях | `{}` |
| <span class="nowrap">`--preview`</span> | string | Рядок JSON, що вмикає можливості попередньої версії. `{"unstable": true}` відкриває відношення `stackql_unstable_<provider>.<service>.<resource>`, засновані на документах: `omnisdk` визначає та виконує їх безпосередньо з документів провайдерів, передаючи рядки потоком у міру їх отримання; решта ключів (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) налаштовують цей бекенд. Можливості попередньої версії нестабільні: поведінка, синтаксис і вивід можуть змінитися або перестати працювати між випусками без попередження, тому не покладайтеся на них у робочому середовищі. Починаючи з `v1.0`, те саме явне ввімкнення відкриває відношення `stackql_preview.iac.run` і `stackql_preview.iac.destroy`, які застосовують і знищують оголошений набір ресурсів через `omnisdk`, зберігаючи стан у кореневому каталозі застосунку. Див. [Можливості попередньої версії](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--provider`</span> | string | Початковий провайдер за замовчуванням. Наприклад, із `--provider github` ім'я `repos.repos` відповідає `github.repos.repos` без попередньої інструкції `USE`. Не виконує автентифікації для провайдера. | немає |
| <span class="nowrap">`--querycachesize`</span> | integer | Розмір кешу LRU для планів запитів, у кількості записів | `10000` |
| <span class="nowrap">`--registry`</span> | string | Рядок JSON / YAML, що налаштовує розташування реєстру провайдерів і перевірку підписів документів провайдерів | див. [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | Рядок JSON / YAML з конфігурацією сеансу | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | Рядок JSON / YAML з конфігурацією системи бекенду SQL | див. [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | Рядок JSON / YAML з конфігурацією сховища Txn | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Шлях до пакета CA; якщо не вказано, використовуються системні значення за замовчуванням |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Дозволити довіру до незахищених сертифікатів (не рекомендовано) |  |
| <span class="nowrap">`--upstream.errors`</span> | string | Політика JSON / YAML для явного допущення статусів відповідей HTTP під час читання з провайдерів. Див. [`--upstream.errors`](#--upstream-errors). | `{}` |
| <span class="nowrap">`--var`</span> | string | Зовнішні змінні у вигляді пар "ключ-значення" для підстановки в запити StackQL через файли даних `jsonnet` |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Докладний вивід | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Контекстна довідка для stackql | |
| <span class="nowrap">`--version`</span> | | Відображає версію програми StackQL | |

Прапорець `--colorscheme` застарілий і більше не змінює кольори оболонки. Прапорець `--usenonpreferredapis` приймається, але в поточному рушії не впливає на вибір API.

## `--registry` {#--registry}

Прапорець `--registry` налаштовує, звідки зчитуються документи провайдерів і чи перевіряються їхні підписи. Значення - це рядок JSON з такими ключами:

| Ключ | Опис | За замовчуванням |
| -- | -- | -- |
| `url` | URL реєстру. Якщо не вказано, використовується публічний реєстр провайдерів StackQL. | публічний реєстр |
| `localDocRoot` | Локальний каталог із документами провайдерів, які зчитуються замість завантаження з реєстру. Використовується під час [розробки провайдера](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | Префікс шляху в корені реєстру, за яким розташовані вихідні документи провайдерів. | `src` |
| `verifyConfig.nopVerify` | Значення `true` вимикає перевірку підписів. Встановіть `false`, щоб вимагати вбудований підпис ML-DSA-65 (FIPS 204) для кожного зчитаного документа провайдера та сервісу, зокрема для відношень попередньої версії, заснованих на документах. | `true` |
| `verifyConfig.publicKeys` | Об'єкт JSON, що зіставляє ідентифікатори ключів із відкритими ключами ML-DSA-65 у шістнадцятковому кодуванні, для приватних реєстрів. Він замінює вбудовані робочі ключі; явно вказаний порожній об'єкт є помилкою. | вбудовані ключі |

### Приклад: вимога підписаних документів провайдерів {#example-require-signed-provider-documents}

Вмикає перевірку для кожного документа провайдера та сервісу, зчитаного з публічного реєстру, за вбудованими робочими ключами:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Приклад: зчитування непідписаних документів із локального каталогу {#example-read-unsigned-documents-from-a-local-directory}

Спрямовує реєстр на каталог документів провайдерів, що перебувають у розробці. Ці документи не підписані, тому перевірка залишається вимкненою:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

Перевірка підписів доступна в StackQL `v1.0` і новіших версіях і за замовчуванням вимкнена. Підписи охоплюють точні байти YAML документа, тому будь-яке редагування, зокрема зміна форматування або символів нового рядка, робить їх недійсними; підпишіть документ повторно за допомогою інструмента реєстру `pqsig sign-doc`. Для непідписаних локальних документів у розробці потрібно залишати `nopVerify: true`. Колишні параметри сертифікатів і файли sidecar `.sig` більше не використовуються, а архіви, опубліковані лише з файлами sidecar, потрібно опублікувати повторно, перш ніж використовувати їх із перевіркою. Перевірка автентифікує документи під час їх зчитування; завантаження та розпакування архівів не охоплюються.

:::

## `--sqlBackend` {#--sqlbackend}

Прапорець `--sqlBackend` налаштовує зовнішній бекенд SQL для StackQL, що дає змогу використовувати зовнішній рушій бази даних (наприклад, PostgreSQL) замість вбудованого рушія SQL за замовчуванням. Це корисно, якщо потрібно використовувати зовнішню базу даних для обробки запитів, постійного зберігання або інтеграції з наявною інфраструктурою баз даних.

Значення - це рядок JSON з такими ключами:

| Ключ | Опис | Приклади значень |
| -- | -- | -- |
| `dbEngine` | Тип рушія бази даних | `postgres_tcp` |
| `sqlDialect` | Діалект SQL, який слід використовувати | `postgres` |
| `dsn` | Ім'я джерела даних (рядок підключення) для бази даних | Див. приклади нижче |

### Приклад: використання бекенду PostgreSQL {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Приклад: використання інтерфейсу PostgreSQL у Databricks {#example-using-databricks-postgresql-interface}

Databricks надає сумісний із PostgreSQL інтерфейс, який можна використовувати як бекенд SQL для StackQL. DSN використовує стандартний формат рядка підключення PostgreSQL з вашими обліковими даними Databricks:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Спеціальні символи в імені користувача або паролі (наприклад, `@`) мають бути закодовані для URL у DSN (наприклад, `@` стає `%40`).
:::

## `--upstream.errors` {#--upstream-errors}

За допомогою `http.ignoreStatusCodes` перелічіть статуси відповідей HTTP, які читання з провайдера має явно допускати. Записи є рядковими значеннями: використовуйте точний код, як-от `"503"`, або клас, як-от `"5xx"`. Значення за замовчуванням `{}` не додає жодних винятків до звичайної обробки відповідей upstream у рушії. Це налаштування не виконує повторних спроб для невдалих запитів і не застосовується до змінення даних або обміну даними автентифікації.

Проігнорована відповідь не додає рядків. Інші операції читання в запиті все одно можуть повертати рядки, тому успішний запит може повернути неповні результати. StackQL повідомляє про проігнорований статус HTTP як діагностичне повідомлення.

Наприклад, цей параметр використовує [тест з'єднання з імітованим GitHub](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104):

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

У тесті запит до API на отримання співавторів одного репозиторію повертає HTTP `500`. Запит однаково повертає рядки співавторів для інших репозиторіїв, а stderr містить `ignoring upstream response: 500 INTERNAL SERVER ERROR`. Якщо очікується лише одна помилка, віддавайте перевагу точному коду статусу.
