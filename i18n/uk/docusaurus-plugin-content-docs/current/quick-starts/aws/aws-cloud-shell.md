---
title: StackQL в AWS CloudShell
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
  - cspm
  - aws
  - amazon web services
description: "Запуск StackQL в AWS CloudShell від імені користувача, що увійшов у систему: завантаження пакета Linux, оболонка stackql, прийняття ролі IAM і пакетні запити через exec."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Використання StackQL в AWS CloudShell

У цьому посібнику ми покажемо, як в AWS CloudShell використовувати StackQL, потужний інструмент розробника, що дає змогу виконувати запити до хмарної інфраструктури й ресурсів і розгортати їх за допомогою синтаксису SQL, у контексті користувача сеансу входу в AWS Console або з прийняттям іншої ролі IAM.    

Перевірено з <span class="tutorial_tested_on">вбудований бекенд SQL</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>Що таке <b>AWS CloudShell</b>?</summary>

> AWS CloudShell - це браузерний сервіс оболонки з попередньою автентифікацією від Amazon Web Services (AWS).  Він дає змогу розробникам та ІТ-фахівцям керувати хмарними ресурсами безпосередньо з веббраузера.  За допомогою AWS CloudShell користувачі можуть швидко виконувати команди, пов'язані із сервісами AWS, не встановлюючи й не налаштовуючи AWS CLI (Command Line Interface) на своїх локальних машинах.  Сервіс надає готове до використання середовище з попередньо встановленими типовими інструментами для розробки й адміністрування, такими як AWS CLI, Python, Node.js тощо, що спрощує взаємодію із сервісами AWS та автоматизацію завдань.  AWS CloudShell безпосередньо інтегровано з AWS Management Console, що забезпечує зручну роботу під час виконання адміністративних завдань, ознайомлення із сервісами AWS і керування інфраструктурою.  Оскільки AWS CloudShell автоматично автентифікує користувачів через їхній вхід у AWS Console, він спрощує виконання команд і скриптів у контексті їхнього облікового запису AWS, підвищуючи продуктивність та ефективність хмарних операцій.

</details>

## Як це працює {#how-it-works}

Пакет StackQL для linux містить скрипти для використання `stackql` безпосередньо у вбудованих середовищах хмарних оболонок основних постачальників хмарних послуг.  Для AWS це скрипт __`stackql-aws-cloud-shell.sh`__.  Використання скрипта __`stackql-aws-cloud-shell.sh`__ показано тут:  

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

Скрипт `stackql-aws-cloud-shell.sh` автоматично завантажує останню версію провайдера AWS для StackQL.  Вихідний код скриптів хмарної оболонки, що входять до пакета, можна знайти за адресою [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).  

## Завантаження пакета `stackql` для Linux в AWS CloudShell {#download-the-stackql-linux-package-from-aws-cloud-shell}

Щоб почати, завантажте в AWS CloudShell останній двійковий файл StackQL для Linux за допомогою таких команд:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

Ці команди завантажать і розпакують двійковий файл `stackql` разом зі скриптом `stackql-aws-cloud-shell.sh` у домашній каталог вашого середовища AWS CloudShell.  Тепер усе готово до роботи!  

## Використання команди `stackql shell` в AWS CloudShell {#using-the-stackql-shell-from-aws-cloud-shell}

Запустити автентифіковану командну оболонку StackQL в AWS CloudShell дуже просто:  

```shell
sh stackql-aws-cloud-shell.sh
```

Тепер ви можете виконувати інтерактивні запити SQL до AWS за допомогою провайдера StackQL, як показано тут:  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Ваш браузер не підтримує тег video.
</video>

## Прийняття ролі IAM за допомогою StackQL {#assuming-an-iam-role-using-stackql}

Прийняття ролі IAM в AWS дає змогу виконувати дії та отримувати доступ до ресурсів, які можуть бути недоступні з дозволами вашого користувача за замовчуванням.  Це особливо корисно, коли вам потрібен тимчасовий доступ до ресурсів або потрібно виконати дії з дозволами, відмінними від дозволів вашого основного облікового запису.  За допомогою StackQL ви можете легко прийняти роль IAM, щоб виконувати запити до ресурсів AWS і керувати ними з дозволами, наданими цією роллю.

<details>
<summary>Як працює прийняття ролі IAM</summary>

> Прийняття ролі IAM - це безпечний спосіб надати тимчасовий доступ до ресурсів AWS.  Коли ви приймаєте роль, AWS STS надає вам тимчасові облікові дані безпеки, які дають змогу отримувати доступ до сервісів і ресурсів AWS, на використання яких роль має дозвіл.  Цей механізм ідеально підходить для доступу між обліковими записами, тимчасового доступу для користувачів або дотримання принципу мінімальних привілеїв.

> Ролі можна приймати за допомогою AWS Management Console, AWS CLI або пакетів AWS SDK, а також безпосередньо в StackQL. Завдяки цьому користувачі й застосунки мають лише необхідні дозволи і лише на потрібний час, що покращує стан безпеки вашого середовища AWS.

</details>

Щоб прийняти роль IAM за допомогою StackQL в AWS CloudShell, ви можете використати параметр `--role-arn` скрипта `stackql-aws-cloud-shell.sh`.  Цей параметр потребує Amazon Resource Name (ARN) ролі, яку ви хочете прийняти.  Якщо його вказано, StackQL використає AWS Security Token Service (STS), щоб прийняти вказану роль перед виконанням будь-яких запитів або команд.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

Прийнявши роль, ви можете за допомогою StackQL працювати з ресурсами AWS, до яких ваш користувач за замовчуванням може не мати доступу, що забезпечує гнучкіший і безпечніший спосіб керування вашим хмарним середовищем.  

Виконуючи команди або скрипти StackQL із прийнятою роллю IAM, переконайтеся, що роль має необхідні дозволи для виконання потрібних дій і доступу до вказаних ресурсів.  Такий підхід сприяє детальному керуванню доступом і відповідає найкращим практикам AWS щодо безпеки та керування.  

## Запуск команди `stackql exec` (пакетні операції) в AWS CloudShell {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

Запити StackQL також можна виконувати за допомогою команди `exec`; це корисний варіант для:

- вказування альтернативних форматів виводу, як-от `json` або `csv`
- створення файлів виводу з результатами запитів StackQL
- використання запитів StackQL із вхідних файлів (за домовленістю - файлів `.iql`)
- виконання складних операцій, зокрема завантаження змінних або попередньої обробки запитів (за допомогою `json` або `jsonnet`)

Приклади використання команди `exec` в AWS CloudShell наведено тут:  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instance_type FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

Наведений вище приклад виконує вказаний запит StackQL і створює файл виводу `CSV`, який можна завантажити з AWS CloudShell.  Крім того, ви можете вказати роль IAM за допомогою аргументу `--role-arn`, щоб прийняти іншу роль для запиту або операції змінення даних; приклад показано тут (у цьому разі ми завантажили запит із вхідного файлу):  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::123456789012:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

Докладніше про команду `exec` можна дізнатися [__тут__](/command-line-usage/exec).
