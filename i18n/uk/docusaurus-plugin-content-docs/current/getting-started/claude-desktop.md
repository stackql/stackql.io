---
title: Використання StackQL із Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ШІ
  - infrastructure-as-code
  - багатохмарність
description: Використовуйте StackQL із Claude Desktop, щоб виконувати запити до хмарних ресурсів і створювати їх природною мовою
image: "/img/stackql-featured-image.png"
---

StackQL інтегрується з Claude Desktop через Model Context Protocol (MCP), що дає змогу виконувати запити до хмарних ресурсів і створювати їх у кількох постачальників хмарних послуг природною мовою. Це потужне поєднання дає змогу взаємодіяти з вашою хмарною інфраструктурою у формі розмови, а за лаштунками працює інтерфейс StackQL на основі SQL.

Є три варіанти встановлення, наведені тут у рекомендованому порядку: встановлення з Anthropic Connector Directory, встановлення із завантаженого пакета або ручна реєстрація локально встановленого двійкового файлу StackQL.

## Встановлення з Connector Directory (рекомендовано) {#install-from-the-connector-directory-recommended}

Найпростіший спосіб використовувати сервер MCP StackQL із Claude Desktop - встановити його з Anthropic Connector Directory. У Claude Desktop виберіть **Settings -> Connectors -> Add -> Browse Connectors**, знайдіть **StackQL** і натисніть **Install**. Нічого завантажувати не потрібно, і окреме встановлення StackQL теж не потрібне.

![StackQL в Anthropic Connector Directory](/img/stackql-anthropic-directory-listing.png)

Сторінку в каталозі також можна переглянути безпосередньо за адресою [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

Так встановлюється внутрішньопроцесний сервер MCP StackQL для вашої платформи: сторінка в каталозі надає комбінований багатоплатформний пакет із підписаними двійковими файлами для macOS universal, Windows x64, а також Linux x64 і arm64, і Claude Desktop автоматично вибирає потрібний для вашої машини. Для нативної Windows ARM64 встановіть окремий пакет `windows-arm64` з [завантажень нижче](#install-from-a-downloaded-bundle). Двійкові файли для Windows підписано Authenticode, а двійковий файл для macOS нотаризовано Apple - це ті самі двійкові файли, що й в автономному випуску StackQL.

![Встановлення конектора StackQL](/img/stackql-anthropic-directory-install.png)

Конектор, встановлений із каталогу, успадковує середовище Claude Desktop, тому встановіть змінні середовища для ваших провайдерів на рівні операційної системи до запуску Claude Desktop - див. [Облікові дані для окремих провайдерів](#provider-specific-credentials). Claude Desktop фіксує своє середовище під час запуску: після змінення змінних потрібно повністю закрити Claude Desktop (зокрема значок у системному лотку у Windows) і запустити його знову. Якщо попросити агента запустити [`reload_credentials`](/mcp/reload_credentials), він повідомить стан облікових даних для кожного провайдера; це найшвидший спосіб перевірити, що сервер бачить, а чого ні.

Claude Desktop оголошує можливість elicitation протоколу MCP, тому режим сервера за замовчуванням (`safe`) запитуватиме у вас схвалення для кожного змінення даних - див. [Режими сервера](/command-line-usage/mcp#server-modes).

## Встановлення із завантаженого пакета {#install-from-a-downloaded-bundle}

Скористайтеся цим варіантом встановлення, щоб закріпити певну версію, встановити сервер без доступу до Connector Directory або самостійно перевірити контрольні суми перед встановленням.

Попередньо зібраний MCP Bundle (`.mcpb`) - це пакет, який встановлюється одним клацанням і містить підписаний двійковий файл `stackql` та конфігурацію сервера. Окреме встановлення StackQL не потрібне.

Завантажте пакет для вашої платформи з останнього випуску:

| Платформа | Пакет |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (нативна) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

Потім у Claude Desktop виберіть **Settings -> Extensions -> Install Extension** і вкажіть завантажений файл. Вбудований двійковий файл підписано Authenticode (Windows) і нотаризовано Apple (macOS); поруч із кожним пакетом на [сторінці випуску](https://github.com/stackql/stackql/releases/latest) є контрольна сума `.sha256`. Щоб перевірити пакет перед встановленням:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Сервер також є в [офіційному реєстрі MCP](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) під назвою `io.github.stackql/stackql-mcp`, і цей реєстр закріплює ці контрольні суми.

## Ручна конфігурація {#manual-configuration}

Якщо StackQL уже встановлено, можна зареєструвати сервер MCP StackQL безпосередньо у файлі конфігурації Claude Desktop.

### Передумови {#prerequisites}

Перш ніж налаштовувати StackQL для роботи з Claude Desktop, переконайтеся, що у вас є:

1. **Встановлений StackQL** - завантажте та встановіть StackQL з [stackql.io](/installing-stackql) або скористайтеся менеджером пакетів
2. **Claude Desktop** - завантажте з [вебсайту Anthropic](https://claude.ai/desktop)
3. **Облікові дані постачальників хмарних послуг** - ключі API або облікові дані для постачальників хмарних послуг, з якими ви плануєте працювати

Переконайтеся, що StackQL є у вашому системному PATH, виконавши команду:

```bash
stackql --version
```

### Налаштування сервера MCP {#setting-up-the-mcp-server}

Щоб Claude Desktop міг взаємодіяти з StackQL, потрібно налаштувати сервер MCP у файлі конфігурації Claude Desktop.

#### Windows {#windows}

Відредагуйте файл конфігурації, розташований за шляхом:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

Відредагуйте файл конфігурації, розташований за шляхом:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

Відредагуйте файл конфігурації, розташований за шляхом:
```
~/.config/Claude/claude_desktop_config.json
```

### Приклад конфігурації {#configuration-example}

Додайте таку конфігурацію до файлу `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
Потрібно додавати лише змінні середовища для постачальників хмарних послуг, які ви плануєте використовувати. Видаліть із конфігурації облікові дані провайдерів, які не використовуються.
:::

:::warning
Ніколи не додавайте файл `claude_desktop_config.json` зі справжніми обліковими даними до системи контролю версій. Дотримуйтеся безпечних практик керування обліковими даними.
:::

Claude Desktop оголошує можливість elicitation протоколу MCP, тому режим сервера за замовчуванням (`safe`) запитуватиме у вас схвалення для кожного змінення даних. Перейдіть на `mode: read_only`, щоб отримати доступ лише для інвентаризації, або на `mode: full_access`, щоб пропускати запити на схвалення - див. [Режими сервера](/command-line-usage/mcp#server-modes).

### Використання файлу облікових даних (`--env.file`) {#using-a-credentials-file---envfile}

Альтернативою блоку `env`, а у Windows і рекомендованим варіантом, є зберігання облікових даних у файлі у стилі dotenv, який передають за допомогою прапорця [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials):

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

З мінімальним файлом облікових даних:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

Чому це краще за блок `env`:

- Щоб дочірній процес MCP побачив зміни в блоці `env`, потрібно повністю перезапустити Claude Desktop (зокрема закрити значок у системному лотку), а у Windows `setx` записує в реєстр, і ці зміни ніколи не доходять до запущеного процесу Desktop.
- Файл `--env.file` можна створити або ротувати будь-коли (він навіть не мусить існувати під час запуску сервера), а якщо попросити агента запустити [`reload_credentials`](/mcp/reload_credentials), зміни буде підхоплено без жодного перезапуску.

:::note

`--env.file` і `reload_credentials` доступні у випусках StackQL після `v0.10.542`.

:::

### Облікові дані для окремих провайдерів {#provider-specific-credentials}

Різні постачальники хмарних послуг потребують різних способів автентифікації; змінні середовища, потрібні для вибраних провайдерів, див. у документації провайдерів ([https://stackql.io/providers](/providers)).


### Перезапуск Claude Desktop {#restarting-claude-desktop}

Після оновлення файлу конфігурації перезапустіть Claude Desktop, щоб завантажити нову конфігурацію сервера MCP. Сервер MCP StackQL запуститься автоматично, коли ви почнете нову розмову.

## Що можна робити {#what-you-can-do}

Після налаштування можна використовувати природну мову в Claude Desktop, щоб:

### Виконання запитів до хмарних ресурсів {#query-cloud-resources}

Попросіть Claude отримати інформацію про вашу хмарну інфраструктуру:

- "Покажи всі мої обчислювальні екземпляри GCP у регіоні us-central1"
- "Перелічи всі бакети S3 у моєму обліковому записі AWS"
- "Які віртуальні машини Azure працюють у моїй підписці?"
- "Покажи всі поди Kubernetes у просторі імен default"

### Створення ресурсів {#provision-resources}

Створюйте та розгортайте хмарні ресурси за допомогою розмовних команд:

- "Створи бакет S3 з назвою 'my-data-bucket' у us-west-2"
- "Розгорни бакет GCP Cloud Storage з увімкненим керуванням версіями"
- "Запусти невеликий екземпляр EC2 в us-east-1"
- "Створи групу ресурсів Azure у westus2"

### Аналіз і аудит {#analyze-and-audit}

Виконуйте аудити безпеки й перевірки відповідності вимогам:

- "Покажи всі загальнодоступні бакети S3"
- "Перелічи користувачів IAM, які не входили в систему 90 днів"
- "Знайди всі екземпляри GCP без належних міток"
- "Визнач віртуальні машини Azure без увімкненого резервного копіювання"

### Операції в кількох хмарах {#cross-cloud-operations}

Виконуйте запити до ресурсів і порівнюйте їх у кількох постачальників хмарних послуг:

- "Покажи всі обчислювальні екземпляри в AWS, GCP і Azure"
- "Порівняй вартість зберігання в AWS S3 і GCP Cloud Storage"
- "Перелічи всі бази даних у всіх постачальників хмарних послуг"

### Інфраструктура як код {#infrastructure-as-code}

Генеруйте та виконуйте запити для розгортання інфраструктури:

- "Створи стек ресурсів, що містить VPC, підмережу й екземпляр EC2"
- "Розгорни повний кластер GKE з пулами вузлів"
- "Налаштуй розгортання в кількох регіонах AWS"

### Змінення ресурсів {#modify-resources}

Оновлюйте наявні хмарні ресурси:

- "Додай теги до всіх екземплярів EC2 без тегів"
- "Увімкни шифрування для бакета S3 'my-bucket'"
- "Онови тип машини екземпляра GCP 'web-server-1'"
- "Зміни ACL мого бакета Cloud Storage на приватний"

### Видалення ресурсів {#delete-resources}

Видаляйте хмарні ресурси, коли вони більше не потрібні:

- "Видали бакет S3 'temp-bucket'"
- "Видали всі зупинені екземпляри EC2"
- "Прибери невикористовувані диски GCP"

## Приклад розмови {#example-conversation}

Ось приклад того, як можна взаємодіяти з Claude Desktop за допомогою StackQL:

**Ви:** "Можеш показати всі мої обчислювальні екземпляри GCP?"

**Claude:** *Виконує запит StackQL і повертає результати у відформатованій таблиці з назвами екземплярів, зонами, типами машин і станом*

**Ви:** "Створи новий бакет Cloud Storage з назвою 'analytics-data-2025' у регіоні us-central1 з уніфікованим доступом на рівні бакета"

**Claude:** *Виконує інструкцію INSERT у StackQL, щоб створити бакет, і підтверджує створення*

**Ви:** "Тепер перелічи всі мої бакети Cloud Storage"

**Claude:** *Виконує запит SELECT і показує всі бакети, зокрема щойно створений*

## Поради й найкращі практики {#tips-and-best-practices}

1. **Будьте конкретними**: що конкретніше прохання, то кращі результати ви отримаєте. За можливості вказуйте назви ресурсів, регіони й конкретні атрибути.

2. **Використовуйте природну мову**: знати синтаксис SQL не потрібно - Claude перетворює ваші прохання природною мовою на запити StackQL.

3. **Переглядайте перед виконанням**: для деструктивних операцій (DELETE, UPDATE) Claude зазвичай показує, що буде виконано, перш ніж продовжити.

4. **Багатокрокові операції**: в одній розмові можна об'єднувати операції в ланцюжок. Claude зберігає контекст протягом усього сеансу.

5. **Обробка помилок**: якщо запит завершується помилкою, Claude може допомогти усунути несправність і запропонувати виправлення на основі повідомлення про помилку.

6. **Досліджуйте можливості**: запитайте Claude "Що я можу робити зі StackQL?", щоб отримати пропозиції для ваших налаштованих провайдерів.

## Усунення несправностей {#troubleshooting}

### Сервер MCP не запускається {#mcp-server-not-starting}

Якщо Claude Desktop не може підключитися до сервера MCP StackQL:

1. Переконайтеся, що StackQL є у вашому PATH
2. Перевірте, що файл `claude_desktop_config.json` містить допустимий JSON
3. Переконайтеся, що шлях до файлу в конфігурації правильний для вашої операційної системи
4. Перегляньте журнали Claude Desktop на наявність повідомлень про помилки

### Помилки автентифікації {#authentication-errors}

Якщо ви отримуєте помилки автентифікації:

1. Переконайтеся, що облікові дані постачальника хмарних послуг правильні
2. Переконайтеся, що файли облікових даних існують за вказаними шляхами
3. Перевірте, що змінні середовища в конфігурації правильно відформатовано - якщо попросити агента запустити [`reload_credentials`](/mcp/reload_credentials), він повідомить стан облікових даних для кожного провайдера; це найшвидший спосіб з'ясувати, якої змінної бракує
4. Переконайтеся, що ваші облікові дані мають потрібні дозволи

### Помилки дозволів {#permission-errors}

Якщо операції завершуються помилкою через недостатні дозволи:

1. Переконайтеся, що ваш обліковий запис служби або користувач IAM має потрібні дозволи
2. Перевірте політики IAM конкретного провайдера
3. Переконайтеся, що сервіси API увімкнено у ваших хмарних проєктах

## Докладніше {#learn-more}

- [Довідник із команди MCP у StackQL](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Специфікація мови](/language-spec/select)
- [Документація Model Context Protocol](https://modelcontextprotocol.io)

## Міркування щодо безпеки {#security-considerations}

:::caution
- Зберігайте облікові дані в безпечному місці й ніколи не додавайте їх до системи контролю версій
- Застосовуйте принцип мінімальних привілеїв для облікових записів служб
- Регулярно виконуйте ротацію облікових даних і ключів API
- Відстежуйте в журналах сервера MCP незвичну активність
- Розгляньте можливість використання засобів керування обліковими даними постачальників хмарних послуг (наприклад, Secret Manager, Parameter Store)
:::
