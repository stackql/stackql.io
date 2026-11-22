---
title: Использование провайдера
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Использование провайдера в StackQL: загрузка из реестра, аутентификация с помощью переменных среды или учетных данных и запросы к его сервисам и ресурсам."
image: "/img/stackql-featured-image.png"
---

См. также:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

Запросы StackQL можно выполнять к различным поставщикам облачных услуг, включая Google Cloud Platform, Amazon Web Services, Microsoft Azure, DigitalOcean и другие. Спецификации провайдеров StackQL загружаются из [__StackQL Provider Registry__](/providers) с помощью команды StackQL [__`REGISTRY PULL`__](/language-spec/registry) или утилиты командной строки [__`stackql registry`__](/command-line-usage/registry).


## Установка провайдера {#installing-a-provider}

Чтобы просмотреть список доступных провайдеров, выполните следующую команду:  

```sql
REGISTRY LIST;
```

Чтобы установить провайдер, выполните следующую команду:

```sql
REGISTRY PULL okta;
```

Чтобы просмотреть список провайдеров, установленных в текущей системе, выполните следующую команду:

```sql
SHOW PROVIDERS;
```

## Аутентификация в провайдере {#authenticating-to-a-provider}

Аутентификация у поставщика облачных услуг часто бывает первым шагом в работе с StackQL. Метакоманды, такие как [`SHOW`](/language-spec/show) и [`DESCRIBE`](/language-spec/describe), можно выполнять для провайдера без аутентификации, но команды [`SELECT`](/language-spec/select), [`INSERT`](/language-spec/insert) и [`EXEC`](/language-spec/exec) обычно требуют аутентифицированного сеанса.  

Аутентификация выполняется с помощью переменных среды, заданных в системе (или загруженных как секреты в системе CI). У каждого провайдера есть собственные уникальные переменные по умолчанию для аутентификации, поэтому найдите документацию своего провайдера в [документации StackQL Provider Registry](/providers).  Переменные также можно загрузить при запуске из файла в стиле dotenv с помощью глобального флага [`--env.file`](/command-line-usage/global-flags).  

:::tip

Вы можете пройти аутентификацию у нескольких провайдеров в одной интерактивной оболочке или в одной неинтерактивной пакетной процедуре, задав переменные аутентификации каждого провайдера, например:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

Это особенно полезно, когда вы соединяете данные нескольких провайдеров в одном запросе.

:::
