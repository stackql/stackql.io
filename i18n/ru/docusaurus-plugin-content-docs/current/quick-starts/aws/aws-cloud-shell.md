---
title: StackQL в AWS CloudShell
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
  - cspm
  - aws
  - amazon web services
description: "Запуск StackQL в AWS CloudShell от имени вошедшего пользователя: скачивание пакета для Linux, оболочка stackql, принятие роли IAM и пакетные запросы через exec."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Использование StackQL в AWS CloudShell

В этом руководстве мы покажем, как использовать в AWS CloudShell StackQL - мощный инструмент разработчика, который позволяет выполнять запросы к облачной инфраструктуре и ресурсам и развертывать их с помощью синтаксиса SQL, в контексте пользователя сеанса входа в AWS Console или с принятием другой роли IAM.    

Проверено с <span class="tutorial_tested_on">встроенный SQL-бэкенд</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>Что такое <b>AWS CloudShell</b>?</summary>

> AWS CloudShell - это браузерный сервис оболочки с предварительной аутентификацией, предоставляемый Amazon Web Services (AWS).  Он позволяет разработчикам и ИТ-специалистам управлять облачными ресурсами непосредственно из веб-браузера.  С помощью AWS CloudShell пользователи могут быстро выполнять команды, связанные с сервисами AWS, не устанавливая и не настраивая AWS CLI (Command Line Interface) на своих локальных машинах.  Этот сервис предоставляет готовую к использованию среду с предустановленными типичными инструментами для разработки и управления, такими как AWS CLI, Python, Node.js и другими, что упрощает взаимодействие с сервисами AWS и автоматизацию задач.  AWS CloudShell напрямую интегрирован с AWS Management Console и обеспечивает удобную работу при выполнении административных задач, изучении сервисов AWS и управлении инфраструктурой.  Поскольку AWS CloudShell автоматически аутентифицирует пользователей через их вход в AWS Console, он упрощает выполнение команд и скриптов в контексте их учетной записи AWS, повышая производительность и эффективность работы в облаке.

</details>

## Как это работает {#how-it-works}

Пакет StackQL для linux содержит скрипты для использования `stackql` непосредственно во встроенных средах облачных оболочек крупных поставщиков облачных услуг.  Для AWS это скрипт __`stackql-aws-cloud-shell.sh`__.  Использование скрипта __`stackql-aws-cloud-shell.sh`__ показано здесь:  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

Скрипт `stackql-aws-cloud-shell.sh` автоматически загружает последнюю версию провайдера AWS для StackQL.  Исходный код входящих в пакет скриптов облачной оболочки можно найти по адресу [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).  

## Скачивание пакета `stackql` для Linux в AWS CloudShell {#download-the-stackql-linux-package-from-aws-cloud-shell}

Чтобы начать, скачайте в AWS CloudShell последний двоичный файл StackQL для Linux с помощью следующих команд:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

Эти команды скачают и распакуют двоичный файл `stackql` вместе со скриптом `stackql-aws-cloud-shell.sh` в домашний каталог вашей среды AWS CloudShell.  Теперь можно приступать к работе!  

## Использование команды `stackql shell` в AWS CloudShell {#using-the-stackql-shell-from-aws-cloud-shell}

Запустить аутентифицированную командную оболочку StackQL в AWS CloudShell очень просто:  

```shell
sh stackql-aws-cloud-shell.sh
```

Теперь вы можете выполнять интерактивные SQL-запросы к AWS с помощью провайдера StackQL, как показано здесь:  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Ваш браузер не поддерживает тег video.
</video>

## Принятие роли IAM с помощью StackQL {#assuming-an-iam-role-using-stackql}

Принятие роли IAM в AWS позволяет выполнять действия и получать доступ к ресурсам, которые могут быть недоступны с разрешениями вашего пользователя по умолчанию.  Это особенно полезно, когда вам нужен временный доступ к ресурсам или нужно выполнить действия с разрешениями, отличными от разрешений вашей основной учетной записи.  С помощью StackQL вы можете легко принять роль IAM, чтобы выполнять запросы к ресурсам AWS и управлять ими с разрешениями, предоставленными этой ролью.

<details>
<summary>Как работает принятие роли IAM</summary>

> Принятие роли IAM - это безопасный способ предоставить временный доступ к ресурсам AWS.  Когда вы принимаете роль, AWS STS предоставляет вам временные учетные данные безопасности, которые позволяют получать доступ к сервисам и ресурсам AWS, на использование которых у роли есть разрешение.  Этот механизм идеально подходит для доступа между учетными записями, временного доступа для пользователей или соблюдения принципа минимальных привилегий.

> Роли можно принимать с помощью AWS Management Console, AWS CLI или пакетов AWS SDK, а также непосредственно в StackQL. Благодаря этому пользователи и приложения получают только необходимые разрешения и только на нужное время, что повышает уровень безопасности вашей среды AWS.

</details>

Чтобы принять роль IAM с помощью StackQL в AWS CloudShell, можно использовать параметр `--role-arn` скрипта `stackql-aws-cloud-shell.sh`.  Для этого параметра требуется Amazon Resource Name (ARN) роли, которую вы хотите принять.  Если параметр указан, StackQL использует AWS Security Token Service (STS), чтобы принять указанную роль перед выполнением любых запросов или команд.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

Приняв роль, вы можете с помощью StackQL работать с ресурсами AWS, к которым у вашего пользователя по умолчанию может не быть доступа, что дает более гибкий и безопасный способ управления вашей облачной средой.  

Выполняя команды или скрипты StackQL с принятой ролью IAM, убедитесь, что у роли есть необходимые разрешения для выполнения нужных действий и доступа к указанным ресурсам.  Такой подход упрощает детальное управление доступом и соответствует рекомендациям AWS по безопасности и управлению.  

## Выполнение команды `stackql exec` (пакетные операции) в AWS CloudShell {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

Запросы StackQL можно выполнять и с помощью команды `exec`; это полезный вариант для:

- указания альтернативных форматов вывода, таких как `json` или `csv`
- создания файлов вывода с результатами запросов StackQL
- использования запросов StackQL из входных файлов (по соглашению - файлов `.iql`)
- выполнения сложных операций, включая загрузку переменных или предварительную обработку запросов (с помощью `json` или `jsonnet`)

Примеры использования команды `exec` в AWS CloudShell приведены здесь:  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instance_type FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

Приведенный выше пример выполняет указанный запрос StackQL и создает файл вывода `CSV`, который можно скачать из AWS CloudShell.  Кроме того, вы можете указать роль IAM с помощью аргумента `--role-arn`, чтобы принять другую роль для запроса или операции изменения данных; пример показан здесь (в этом случае мы загрузили запрос из входного файла):  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::123456789012:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

Подробнее о команде `exec` см. [__здесь__](/command-line-usage/exec).
