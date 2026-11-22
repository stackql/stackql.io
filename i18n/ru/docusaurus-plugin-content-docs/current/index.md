---
title: SQL для облачной инфраструктуры, API SaaS-сервисов и ИИ-агентов
sidebar_label: Добро пожаловать в StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "StackQL - движок с открытым исходным кодом: люди и ИИ-агенты на SQL запрашивают, создают и эксплуатируют облачные сервисы и SaaS без файла состояния и SDK для каждого облака."
image: "/img/stackql-featured-image.png"
slug: /
# SoftwareApplication node on the homepage, from the site-wide defaults in
# themeConfig.structuredData.softwareApplication (name, category, OS,
# license, download URL, free offer)
softwareApplication: true
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import DocsHero from '@site/src/components/DocsHero/DocsHero';
import { FaRobot, FaCode, FaSatelliteDish } from 'react-icons/fa';

<DocsHero
  lightSrc="/img/stackql-logo-bold-light-mode.png"
  darkSrc="/img/stackql-logo-bold-dark-mode.png"
  alt="StackQL"
  title={
  <>
  Универсальный интерфейс<br/>
  для агентной эры
  </>
  }
  byline={
    <>
    Configuration-as-Data. Infrastructure-as-Context.
    </>
    }
/>

---

<div className="row margin-top--lg margin-bottom--lg">
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaRobot size={22} className="homeCardIcon" />Агентная платформенная инженерия</h3>
      </div>
      <div className="card__body">
        Универсальное пространство, в котором агенты и люди бок о бок вносят изменения с машинной скоростью. Несколько авторов изменений, актуальное состояние и выполнение в рамках политик как первоочередные задачи - основа, которая нужна автономным системам для безопасной работы.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC для агентной эры</h3>
      </div>
      <div className="card__body">
        Код по-прежнему выражает намерение. Реальность - это данные. StackQL дает агентам и операторам единый декларативный интерфейс, чтобы выполнять запросы к актуальному состоянию в разных провайдерах, идемпотентно задавать желаемое состояние и охватывать плоскость управления и плоскость данных единой грамматикой - без файла состояния и без SDK для каждого облака.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Автономная наблюдаемость</h3>
      </div>
      <div className="card__body">
        Рассматривайте облачные API как свой уровень данных, а не как цель развертывания. Каждое чтение выполняется в реальном времени, каждое изменение - это событие, а дрейф становится сигналом, который агент может анализировать, а не тревогой, на которую должен реагировать человек.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Запрос', value: 'iql', },
    { label: 'Результаты', value: 'data', },
  ]
}>
<TabItem value="iql">

```sql
SELECT state, COUNT(*) as num_instances 
FROM aws.ec2.instances 
WHERE region = 'us-west-1' 
GROUP BY state;
```

</TabItem>
<TabItem value="data">

```
|---------------------------------|
|      state      | num_instances |
|---------------------------------|
|     running     |     342       |
|---------------------------------|
|     pending     |       4       |
|---------------------------------|
|     stopped     |      38       |
|---------------------------------|
```

</TabItem>
</Tabs>

Развернуть ресурсы у вашего поставщика облачных услуг так же просто, как написать инструкцию [`INSERT`](/language-spec/insert)...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

С StackQL вы можете вести разработку так, как вам удобно: декларативно или процедурно. Грамматика проста в изучении, а файла состояния, которым нужно управлять, нет, поэтому вы можете быстро начать работу и использовать StackQL взаимозаменяемо с другими инструментами инфраструктуры как кода, облачно-нативными или любыми другими. StackQL предоставляет ИИ-агентам универсальный интерфейс для взаимодействия с облачной инфраструктурой. Сценарии использования StackQL:

- **Интеграция с ИИ-агентами** - Позволяет агентам создавать облачные ресурсы и выполнять к ним запросы
- **Взаимодействие с плоскостью управления** - Программное создание инфраструктуры для агентных рабочих процессов
- **Наблюдаемость плоскости данных** - Мониторинг и аналитика в реальном времени для операций под управлением ИИ
- Развертывание облачной инфраструктуры (с помощью SQL)
- Инвентаризация облачных активов и отчетность (с помощью SQL)
- Проверка соответствия облака требованиям и аттестация средств контроля (с помощью SQL)
- Обнаружение дрейфа конфигурации (с помощью SQL)
- и многое другое, ограниченное лишь вашим воображением!

## Для кого предназначен StackQL {#who-stackql-is-for}

StackQL предназначен для команд платформенной инженерии и SRE, которые эксплуатируют облачные среды, команд безопасности и FinOps, которым нужны актуальные ответы по инвентаризации, соответствию требованиям и затратам, инженеров данных, которым нужны API облачных сервисов и SaaS в качестве источников для запросов, и разработчиков ИИ-агентов, которые должны безопасно выполнять запросы к облачным ресурсам и ресурсам SaaS и изменять их. Он работает там, где идет работа: как один двоичный файл в macOS, Linux и Windows, в Docker, в CI с GitHub Actions, в оболочках AWS, Azure и Google Cloud, встроенным в приложения, как совместимый с PostgreSQL сервер для инструментов BI и блокнотов и как MCP-сервер для ИИ-агентов.

## Лицензия и поддержка {#license-and-support}

StackQL распространяется с открытым исходным кодом по лицензии [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) и бесплатен для использования в любых целях. Поддержка сообщества доступна в [GitHub Discussions](https://github.com/orgs/stackql/discussions) и [Discord](https://discord.com/invite/xVXZ9d5NxN); сообщения об ошибках и запросы новых возможностей направляйте в [GitHub Issues](https://github.com/stackql/stackql/issues). По вопросам коммерческой поддержки [свяжитесь со StackQL Studios](/contact-us).

## Начните работу {#get-started}

- [Установка](/installing-stackql) - установите двоичный файл StackQL в macOS, Linux или Windows либо запустите его из Docker, с помощью менеджера пакетов или из облачной оболочки
- [ИИ-агенты](/ai-agents) - подключите ИИ-агентов через MCP, встройте сервер и изучите запросы и справочные материалы
- [Начало работы](/getting-started) - иерархия ресурсов, ваши первые запросы, использование провайдера, режимы вывода, переменные и шаблонизация
- [Доступные провайдеры](/providers) - провайдеры облачных сервисов и SaaS, к которым можно выполнять запросы и которыми можно управлять, каждый со своим справочным сайтом
- [Блог](/blog) - новости продукта, новости провайдеров и руководства
- [Командная строка](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry` и глобальные флаги
- [MCP-инструменты](/mcp) - сервер и инструменты Model Context Protocol, которые дают ИИ-агентам тот же SQL-интерфейс
- [Спецификация языка](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, функции и типы данных
