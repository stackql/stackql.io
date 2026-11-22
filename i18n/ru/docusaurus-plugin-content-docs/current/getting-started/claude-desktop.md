---
title: Использование StackQL с Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ИИ
  - infrastructure-as-code
  - мультиоблако
description: Используйте StackQL с Claude Desktop, чтобы выполнять запросы к облачным ресурсам и создавать их на естественном языке
image: "/img/stackql-featured-image.png"
---

StackQL интегрируется с Claude Desktop через Model Context Protocol (MCP), что позволяет выполнять запросы к облачным ресурсам и создавать их у нескольких поставщиков облачных услуг на естественном языке. Это мощное сочетание позволяет работать с вашей облачной инфраструктурой в форме диалога, а внутри при этом используется интерфейс StackQL на основе SQL.

Есть три варианта установки, они приведены здесь в рекомендуемом порядке: установка из Anthropic Connector Directory, установка из скачанного пакета или ручная регистрация локально установленного двоичного файла StackQL.

## Установка из Connector Directory (рекомендуется) {#install-from-the-connector-directory-recommended}

Проще всего использовать MCP-сервер StackQL с Claude Desktop, установив его из Anthropic Connector Directory. В Claude Desktop выберите **Settings -> Connectors -> Add -> Browse Connectors**, найдите **StackQL** и нажмите **Install**. Ничего скачивать не нужно, и отдельная установка StackQL не требуется.

![StackQL в Anthropic Connector Directory](/img/stackql-anthropic-directory-listing.png)

Страницу в каталоге также можно открыть напрямую: [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

Так MCP-сервер StackQL устанавливается для вашей платформы и работает внутри процесса: страница в каталоге предоставляет объединенный мультиплатформенный пакет с подписанными двоичными файлами для macOS universal, Windows x64, а также Linux x64 и arm64, и Claude Desktop автоматически выбирает подходящий для вашей машины. Для нативной Windows ARM64 установите отдельный пакет `windows-arm64` из [списка для скачивания ниже](#install-from-a-downloaded-bundle). Двоичные файлы для Windows подписаны Authenticode, а двоичный файл для macOS нотариально заверен Apple - это те же двоичные файлы, что и в автономном выпуске StackQL.

![Установка коннектора StackQL](/img/stackql-anthropic-directory-install.png)

Коннектор, установленный из каталога, наследует среду Claude Desktop, поэтому задайте переменные среды для ваших провайдеров на уровне операционной системы до запуска Claude Desktop - см. [Учетные данные для конкретных провайдеров](#provider-specific-credentials). Claude Desktop считывает свою среду при запуске: после изменения переменных необходимо полностью закрыть Claude Desktop (включая значок в области уведомлений в Windows) и запустить его снова. Если попросить агента выполнить [`reload_credentials`](/mcp/reload_credentials), он сообщит состояние учетных данных для каждого провайдера; это самый быстрый способ проверить, что сервер видит, а что нет.

Claude Desktop объявляет возможность elicitation протокола MCP, поэтому режим сервера по умолчанию (`safe`) будет запрашивать у вас подтверждение для каждого изменения данных - см. [Режимы сервера](/command-line-usage/mcp#server-modes).

## Установка из скачанного пакета {#install-from-a-downloaded-bundle}

Используйте этот вариант установки, чтобы закрепить определенную версию, выполнить установку без доступа к Connector Directory или самостоятельно проверить контрольные суммы перед установкой.

Предварительно собранный пакет MCP Bundle (`.mcpb`) устанавливается одним щелчком и содержит подписанный двоичный файл `stackql` и конфигурацию сервера. Отдельная установка StackQL не требуется.

Скачайте пакет для вашей платформы из последнего выпуска:

| Платформа | Пакет |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (нативная) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

Затем в Claude Desktop выберите **Settings -> Extensions -> Install Extension** и укажите скачанный файл. Встроенный двоичный файл подписан Authenticode (Windows) и нотариально заверен Apple (macOS); рядом с каждым пакетом на [странице выпуска](https://github.com/stackql/stackql/releases/latest) лежит контрольная сумма `.sha256`. Чтобы проверить пакет перед установкой:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Сервер также указан в [официальном реестре MCP](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) как `io.github.stackql/stackql-mcp`, и там закреплены эти контрольные суммы.

## Ручная настройка {#manual-configuration}

Если StackQL уже установлен, можно зарегистрировать MCP-сервер StackQL непосредственно в файле конфигурации Claude Desktop.

### Предварительные требования {#prerequisites}

Перед настройкой StackQL для работы с Claude Desktop убедитесь, что у вас есть:

1. **Установленный StackQL** - скачайте и установите StackQL с [stackql.io](/installing-stackql) или используйте менеджер пакетов
2. **Claude Desktop** - скачайте с [веб-сайта Anthropic](https://claude.ai/desktop)
3. **Учетные данные поставщиков облачных услуг** - API-ключи или учетные данные для поставщиков облачных услуг, с которыми вы хотите работать

Убедитесь, что StackQL есть в системной переменной PATH, выполнив команду:

```bash
stackql --version
```

### Настройка MCP-сервера {#setting-up-the-mcp-server}

Чтобы Claude Desktop мог взаимодействовать с StackQL, необходимо настроить MCP-сервер в файле конфигурации Claude Desktop.

#### Windows {#windows}

Отредактируйте файл конфигурации, расположенный по пути:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

Отредактируйте файл конфигурации, расположенный по пути:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

Отредактируйте файл конфигурации, расположенный по пути:
```
~/.config/Claude/claude_desktop_config.json
```

### Пример конфигурации {#configuration-example}

Добавьте следующую конфигурацию в файл `claude_desktop_config.json`:

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
Достаточно указать переменные среды только для тех поставщиков облачных услуг, которые вы планируете использовать. Удалите из конфигурации учетные данные неиспользуемых провайдеров.
:::

:::warning
Никогда не добавляйте файл `claude_desktop_config.json` с реальными учетными данными в систему управления версиями. Используйте безопасные методы управления учетными данными.
:::

Claude Desktop объявляет возможность elicitation протокола MCP, поэтому режим сервера по умолчанию (`safe`) будет запрашивать у вас подтверждение для каждого изменения данных. Переключитесь на `mode: read_only`, чтобы получить доступ только для инвентаризации, или на `mode: full_access`, чтобы пропускать запросы подтверждения - см. [Режимы сервера](/command-line-usage/mcp#server-modes).

### Использование файла учетных данных (`--env.file`) {#using-a-credentials-file---envfile}

Вместо блока `env` можно хранить учетные данные в файле в формате dotenv и передавать его с помощью флага [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) (в Windows рекомендуется именно этот вариант):

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

Минимальный файл учетных данных:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

Чем этот способ лучше блока `env`:

- Изменения в блоке `env` становятся видны дочернему процессу MCP только после полного перезапуска Claude Desktop (включая выход через значок в области уведомлений), а в Windows команда `setx` записывает значения в реестр, и они так и не попадают в работающий процесс Desktop.
- Файл `--env.file` можно создать или сменить в любой момент (ему даже не обязательно существовать при запуске сервера), а если попросить агента выполнить [`reload_credentials`](/mcp/reload_credentials), изменения будут применены без какого-либо перезапуска.

:::note

`--env.file` и `reload_credentials` доступны в выпусках StackQL после `v0.10.542`.

:::

### Учетные данные для конкретных провайдеров {#provider-specific-credentials}

Разные поставщики облачных услуг используют разные методы аутентификации; переменные среды, необходимые для нужного вам провайдера (или провайдеров), см. в документации провайдеров ([https://stackql.io/providers](/providers)).


### Перезапуск Claude Desktop {#restarting-claude-desktop}

После обновления файла конфигурации перезапустите Claude Desktop, чтобы загрузить новую конфигурацию MCP-сервера. MCP-сервер StackQL будет автоматически запускаться, когда вы начинаете новый диалог.

## Что можно делать {#what-you-can-do}

После настройки вы можете использовать естественный язык в Claude Desktop для следующих задач:

### Запросы к облачным ресурсам {#query-cloud-resources}

Попросите Claude получить информацию о вашей облачной инфраструктуре:

- "Покажи все мои вычислительные экземпляры GCP в регионе us-central1"
- "Перечисли все бакеты S3 в моей учетной записи AWS"
- "Какие виртуальные машины Azure запущены в моей подписке?"
- "Покажи все поды Kubernetes в пространстве имен по умолчанию"

### Создание ресурсов {#provision-resources}

Создавайте и развертывайте облачные ресурсы, давая указания в диалоге:

- "Создай бакет S3 с именем 'my-data-bucket' в us-west-2"
- "Разверни бакет GCP Cloud Storage с включенным управлением версиями"
- "Запусти небольшой экземпляр EC2 в us-east-1"
- "Создай группу ресурсов Azure в westus2"

### Анализ и аудит {#analyze-and-audit}

Выполняйте аудит безопасности и проверки соответствия требованиям:

- "Покажи все общедоступные бакеты S3"
- "Перечисли пользователей IAM, которые не входили в систему 90 дней"
- "Найди все экземпляры GCP без нужных меток"
- "Определи виртуальные машины Azure, для которых не включено резервное копирование"

### Операции в нескольких облаках {#cross-cloud-operations}

Выполняйте запросы к ресурсам нескольких поставщиков облачных услуг и сравнивайте их:

- "Покажи все вычислительные экземпляры в AWS, GCP и Azure"
- "Сравни стоимость хранения в AWS S3 и GCP Cloud Storage"
- "Перечисли все базы данных у всех поставщиков облачных услуг"

### Инфраструктура как код {#infrastructure-as-code}

Формируйте и выполняйте запросы на развертывание инфраструктуры:

- "Создай стек ресурсов, включающий VPC, подсеть и экземпляр EC2"
- "Разверни полноценный кластер GKE с пулами узлов"
- "Настрой развертывание в нескольких регионах AWS"

### Изменение ресурсов {#modify-resources}

Обновляйте существующие облачные ресурсы:

- "Добавь теги ко всем экземплярам EC2 без тегов"
- "Включи шифрование для бакета S3 'my-bucket'"
- "Обнови тип машины экземпляра GCP 'web-server-1'"
- "Измени ACL моего бакета Cloud Storage на частный"

### Удаление ресурсов {#delete-resources}

Удаляйте облачные ресурсы, которые больше не нужны:

- "Удали бакет S3 'temp-bucket'"
- "Удали все остановленные экземпляры EC2"
- "Очисти неиспользуемые диски GCP"

## Пример диалога {#example-conversation}

Вот пример того, как можно работать с Claude Desktop с помощью StackQL:

**Вы:** "Можешь показать все мои вычислительные экземпляры GCP?"

**Claude:** *Выполняет запрос StackQL и возвращает результаты в виде отформатированной таблицы с именами экземпляров, зонами, типами машин и состоянием*

**Вы:** "Создай новый бакет Cloud Storage с именем 'analytics-data-2025' в регионе us-central1 с единообразным доступом на уровне бакета"

**Claude:** *Выполняет инструкцию INSERT в StackQL, чтобы создать бакет, и подтверждает его создание*

**Вы:** "Теперь перечисли все мои бакеты Cloud Storage"

**Claude:** *Выполняет запрос SELECT и показывает все бакеты, включая только что созданный*

## Советы и рекомендации {#tips-and-best-practices}

1. **Формулируйте конкретно**: чем конкретнее просьба, тем лучше результат. По возможности указывайте имена ресурсов, регионы и конкретные атрибуты.

2. **Используйте естественный язык**: знать синтаксис SQL не обязательно - Claude преобразует ваши просьбы на естественном языке в запросы StackQL.

3. **Проверяйте перед выполнением**: для разрушительных операций (DELETE, UPDATE) Claude обычно показывает, что будет выполнено, прежде чем продолжить.

4. **Многошаговые операции**: в одном диалоге можно выполнять цепочку операций. Claude сохраняет контекст на протяжении всего сеанса.

5. **Обработка ошибок**: если запрос завершается ошибкой, Claude может помочь устранить неполадку и предложить исправления на основе сообщения об ошибке.

6. **Изучайте возможности**: спросите Claude "Что я могу делать с помощью StackQL?", чтобы получить предложения для настроенных у вас провайдеров.

## Устранение неполадок {#troubleshooting}

### MCP-сервер не запускается {#mcp-server-not-starting}

Если Claude Desktop не может подключиться к MCP-серверу StackQL:

1. Убедитесь, что StackQL есть в PATH
2. Проверьте, что файл `claude_desktop_config.json` содержит допустимый JSON
3. Убедитесь, что путь к файлу в конфигурации правильный для вашей операционной системы
4. Просмотрите журналы Claude Desktop на наличие сообщений об ошибках

### Ошибки аутентификации {#authentication-errors}

Если вы получаете ошибки аутентификации:

1. Убедитесь, что учетные данные поставщика облачных услуг правильные
2. Убедитесь, что файлы учетных данных существуют по указанным путям
3. Проверьте, что переменные среды в конфигурации правильно отформатированы - если попросить агента выполнить [`reload_credentials`](/mcp/reload_credentials), он сообщит состояние учетных данных для каждого провайдера; это самый быстрый способ увидеть, какой переменной не хватает
4. Убедитесь, что у ваших учетных данных есть необходимые разрешения

### Ошибки разрешений {#permission-errors}

Если операции завершаются ошибкой из-за недостаточных разрешений:

1. Убедитесь, что у вашего сервисного аккаунта или пользователя IAM есть необходимые разрешения
2. Проверьте политики IAM конкретного провайдера
3. Убедитесь, что в ваших облачных проектах включены API-сервисы

## Дополнительные материалы {#learn-more}

- [Справочник по команде MCP в StackQL](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Спецификация языка](/language-spec/select)
- [Документация Model Context Protocol](https://modelcontextprotocol.io)

## Вопросы безопасности {#security-considerations}

:::caution
- Храните учетные данные в безопасном месте и никогда не добавляйте их в систему управления версиями
- Применяйте принцип минимальных привилегий для сервисных аккаунтов
- Регулярно меняйте учетные данные и API-ключи
- Отслеживайте необычную активность в журналах MCP-сервера
- Рассмотрите использование средств управления учетными данными от поставщиков облачных услуг (например, Secret Manager, Parameter Store)
:::
