---
title: SQL для хмарної інфраструктури, API SaaS і агентів ШІ
sidebar_label: Ласкаво просимо до StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "StackQL - рушій із відкритим кодом: люди й агенти ШІ мовою SQL виконують запити, створюють ресурси й керують хмарними сервісами та SaaS без файлу стану й SDK для кожної хмари."
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
  Універсальний інтерфейс<br/>
  для агентної ери
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
        <h3><FaRobot size={22} className="homeCardIcon" />Агентна платформна інженерія</h3>
      </div>
      <div className="card__body">
        Універсальний простір, у якому агенти й люди пліч-о-пліч вносять зміни з машинною швидкістю. Кілька авторів змін, актуальний стан і виконання в межах політик як першочергові аспекти - основа, потрібна автономним системам для безпечної роботи.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC для агентної ери</h3>
      </div>
      <div className="card__body">
        Код і далі залишається наміром. Реальність - це дані. StackQL надає агентам і операторам єдиний декларативний інтерфейс, щоб виконувати запити до актуального стану в різних провайдерах, ідемпотентно забезпечувати бажаний стан і охоплювати площину керування й площину даних єдиною граматикою - без файлу стану, без SDK для кожної хмари.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Автономна спостережуваність</h3>
      </div>
      <div className="card__body">
        Розглядайте хмарні API як свій шар даних, а не як ціль розгортання. Кожне читання відбувається в реальному часі, кожна зміна - це подія, а дрейф стає сигналом, який агент може аналізувати, а не тривогою, з якою має розбиратися людина.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Запит', value: 'iql', },
    { label: 'Результати', value: 'data', },
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

Розгорнути ресурси у вашого постачальника хмарних послуг так само просто, як написати інструкцію [`INSERT`](/language-spec/insert)...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

За допомогою StackQL ви можете розробляти так, як вам зручно: декларативно або процедурно. Граматика проста у вивченні, а файлу стану, яким треба керувати, немає, тож ви можете швидко почати роботу й використовувати StackQL нарівні з іншими інструментами інфраструктури як коду, хмарно-нативними чи будь-якими іншими. StackQL надає агентам ШІ універсальний інтерфейс для взаємодії з хмарною інфраструктурою. Серед сценаріїв використання StackQL:

- **Інтеграція агентів ШІ** - Дає агентам змогу створювати хмарні ресурси й виконувати запити до них
- **Взаємодія з площиною керування** - Програмне створення інфраструктури для агентних робочих процесів
- **Спостережуваність площини даних** - Моніторинг і аналітика в реальному часі для операцій, керованих ШІ
- Розгортання хмарної інфраструктури (за допомогою SQL)
- Інвентаризація хмарних активів і звітність (за допомогою SQL)
- Перевірка відповідності хмарних середовищ вимогам і атестація засобів контролю (за допомогою SQL)
- Виявлення дрейфу конфігурації (за допомогою SQL)
- і багато іншого - межі визначає лише ваша уява!

## Для кого StackQL {#who-stackql-is-for}

StackQL призначений для команд платформної інженерії та SRE, які експлуатують хмарні середовища, для команд безпеки та FinOps, яким потрібні актуальні відповіді щодо інвентаризації, відповідності вимогам і витрат, для інженерів даних, які хочуть використовувати API хмарних сервісів і SaaS як джерела для запитів, а також для розробників агентів ШІ, які мають безпечно виконувати запити до хмарних ресурсів і ресурсів SaaS та змінювати їх. Він працює там, де відбувається робота: як один двійковий файл у macOS, Linux і Windows, у Docker, у CI з GitHub Actions, в оболонках AWS, Azure і Google Cloud, вбудованим у застосунки, як сумісний із PostgreSQL сервер для інструментів BI і блокнотів та як сервер MCP для агентів ШІ.

## Ліцензія та підтримка {#license-and-support}

StackQL - програмне забезпечення з відкритим кодом за ліцензією [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE), безплатне для використання з будь-якою метою. Підтримку спільноти надають у [GitHub Discussions](https://github.com/orgs/stackql/discussions) і [Discord](https://discord.com/invite/xVXZ9d5NxN); про помилки та запити на нові можливості повідомляйте в [GitHub Issues](https://github.com/stackql/stackql/issues). Щодо комерційної підтримки [зверніться до StackQL Studios](/contact-us).

## Почніть роботу {#get-started}

- [Встановлення](/installing-stackql) - встановіть двійковий файл StackQL у macOS, Linux або Windows чи запустіть його з Docker, через менеджер пакетів або з хмарної оболонки
- [Агенти ШІ](/ai-agents) - підключіть агентів ШІ через MCP, вбудуйте сервер і ознайомтеся із запитами та довідковими матеріалами
- [Початок роботи](/getting-started) - ієрархія ресурсів, ваші перші запити, використання провайдера, режими виводу, змінні та шаблонізація
- [Доступні провайдери](/providers) - провайдери для хмарних сервісів і SaaS, до яких ви можете виконувати запити й якими можете керувати, кожен із власним довідковим сайтом
- [Блог](/blog) - оголошення про продукт, оголошення про провайдери та посібники
- [Командний рядок](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry` і глобальні прапорці
- [Інструменти MCP](/mcp) - сервер та інструменти Model Context Protocol, що надають агентам ШІ той самий інтерфейс SQL
- [Специфікація мови](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, функції та типи даних
