---
title: SQL für Cloud-Infrastruktur, SaaS-APIs und KI-Agenten
sidebar_label: Willkommen bei StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: StackQL ist eine Open-Source-Engine, mit der Menschen und KI-Agenten Cloud- und SaaS-Services per SQL abfragen, bereitstellen und betreiben, ohne State-Datei und ohne SDK pro Cloud.
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
  Die universelle Schnittstelle<br/>
  für die agentische Ära
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
        <h3><FaRobot size={22} className="homeCardIcon" />Agentisches Platform Engineering</h3>
      </div>
      <div className="card__body">
        Eine universelle Oberfläche, auf der Agenten und Menschen Änderungen Seite an Seite und in Maschinengeschwindigkeit verfassen. Mehrere Schreiber, Live-Zustand und richtlinienbegrenzte Ausführung als erstrangige Anliegen - das Fundament, das autonome Systeme für einen sicheren Betrieb benötigen.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC für die agentische Ära</h3>
      </div>
      <div className="card__body">
        Code ist weiterhin Absicht. Die Realität sind Daten. StackQL gibt Agenten und Betreibern eine deklarative Schnittstelle, um den Live-Zustand über Provider hinweg abzufragen, den Sollzustand idempotent durchzusetzen und Control Plane und Data Plane mit einer einzigen Grammatik zu durchlaufen - ohne State-Datei, ohne SDK pro Cloud.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Autonome Observability</h3>
      </div>
      <div className="card__body">
        Behandeln Sie Cloud-APIs als Ihre Datenschicht, nicht als Ihr Deployment-Ziel. Jeder Lesezugriff ist live, jede Änderung ist ein Ereignis, und Drift wird zu einem Signal, über das ein Agent schlussfolgern kann - nicht zu einem Alarm, dem ein Mensch hinterherlaufen muss.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Abfrage', value: 'iql', },
    { label: 'Ergebnisse', value: 'data', },
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

Ressourcen bei Ihrem Cloud-Anbieter bereitzustellen ist so einfach wie das Schreiben einer [`INSERT`](/language-spec/insert)-Anweisung...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

Mit StackQL entwickeln Sie auf Ihre Weise: deklarativ oder prozedural. Mit einer leicht zu erlernenden Grammatik und ohne zu verwaltende State-Datei können Sie schnell beginnen und StackQL austauschbar mit anderen Infrastructure-as-Code-Tools einsetzen, cloud-nativ oder nicht. StackQL bietet KI-Agenten eine universelle Schnittstelle für die Interaktion mit Cloud-Infrastruktur. Einsatzmöglichkeiten von StackQL sind unter anderem:

- **Integration von KI-Agenten** - Agenten das Bereitstellen und Abfragen von Cloud-Ressourcen ermöglichen
- **Control-Plane-Kommunikation** - Programmatische Bereitstellung von Infrastruktur für agentische Workflows
- **Data-Plane-Observability** - Überwachung und Analysen in Echtzeit für KI-gesteuerte Abläufe
- Deployment von Cloud-Infrastruktur (mit SQL)
- Inventarisierung und Reporting von Cloud-Assets (mit SQL)
- Cloud-Compliance und Nachweis von Kontrollen (mit SQL)
- Erkennung von Konfigurationsdrift (mit SQL)
- und mehr, nur durch Ihre Vorstellungskraft begrenzt!

## Für wen StackQL gedacht ist {#who-stackql-is-for}

StackQL richtet sich an Platform-Engineering- und SRE-Teams, die Cloud-Umgebungen betreiben, an Security- und FinOps-Teams, die Live-Antworten zu Inventar, Compliance und Kosten benötigen, an Data Engineers, die Cloud- und SaaS-APIs als abfragbare Quellen nutzen möchten, und an Entwickler, die KI-Agenten bauen, die Cloud- und SaaS-Ressourcen sicher abfragen und ändern müssen. Es läuft dort, wo die Arbeit stattfindet: als einzelne Binärdatei unter macOS, Linux und Windows, in Docker, in CI mit GitHub Actions, in den Shells von AWS, Azure und Google Cloud, eingebettet in Anwendungen, als PostgreSQL-kompatibler Server für BI-Tools und Notebooks und als MCP-Server für KI-Agenten.

## Lizenz und Support {#license-and-support}

StackQL ist Open Source unter der [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) und für jeden Zweck frei nutzbar. Community-Support gibt es über [GitHub Discussions](https://github.com/orgs/stackql/discussions) und [Discord](https://discord.com/invite/xVXZ9d5NxN); Fehler und Funktionswünsche gehören in die [GitHub Issues](https://github.com/stackql/stackql/issues). Für Anfragen zu kommerziellem Support [kontaktieren Sie StackQL Studios](/contact-us).

## Einstieg {#get-started}

- [Installation](/installing-stackql) - die StackQL-Binärdatei unter macOS, Linux oder Windows installieren oder sie aus Docker, einem Paketmanager oder einer Cloud-Shell ausführen
- [KI-Agenten](/ai-agents) - KI-Agenten über MCP anbinden, den Server einbetten sowie Abfragen und Referenzinhalte erkunden
- [Erste Schritte](/getting-started) - die Ressourcenhierarchie, Ihre ersten Abfragen, die Verwendung eines Providers, Ausgabemodi, Variablen und Templating
- [Verfügbare Provider](/providers) - die Cloud- und SaaS-Provider, die Sie abfragen und verwalten können, jeder mit eigener Referenzseite
- [Blog](/blog) - Produktankündigungen, Provider-Ankündigungen und Tutorials
- [Kommandozeile](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry` und die globalen Flags
- [MCP-Tools](/mcp) - der Server und die Tools des Model Context Protocol, die KI-Agenten dieselbe SQL-Schnittstelle geben
- [Sprachspezifikation](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, Funktionen und Datentypen
