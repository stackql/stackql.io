---
title: SQL pour l'infrastructure cloud, les API SaaS et les agents IA
sidebar_label: Bienvenue dans StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Avec le moteur open source StackQL, humains et agents IA interrogent, provisionnent et exploitent des services cloud et SaaS en SQL, sans fichier d'état ni SDK par cloud.
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
  L'interface universelle<br/>
  pour l'ère agentique
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
        <h3><FaRobot size={22} className="homeCardIcon" />Ingénierie de plateforme agentique</h3>
      </div>
      <div className="card__body">
        Une surface universelle sur laquelle agents et humains rédigent des modifications côte à côte, à la vitesse des machines. Plusieurs auteurs, un état en temps réel et une exécution encadrée par des politiques comme préoccupations de premier ordre - le socle dont les systèmes autonomes ont besoin pour fonctionner en toute sécurité.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC pour l'ère agentique</h3>
      </div>
      <div className="card__body">
        Le code reste l'intention. La réalité, ce sont des données. StackQL donne aux agents et aux exploitants une interface déclarative unique pour interroger l'état en temps réel sur l'ensemble des providers, appliquer l'état souhaité de manière idempotente et parcourir les plans de contrôle et de données avec une seule grammaire - sans fichier d'état, sans SDK par cloud.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Observabilité autonome</h3>
      </div>
      <div className="card__body">
        Traitez les API cloud comme votre couche de données, pas comme votre cible de déploiement. Chaque lecture est en temps réel, chaque modification est un événement, et la dérive devient un signal sur lequel un agent peut raisonner - pas une alerte après laquelle un humain doit courir.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Requête', value: 'iql', },
    { label: 'Résultats', value: 'data', },
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

Déployer des ressources chez votre fournisseur cloud est aussi simple que d'écrire une instruction [`INSERT`](/language-spec/insert)...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

Avec StackQL, vous développez à votre façon : en déclaratif ou en procédural. Avec une grammaire facile à apprendre et aucun fichier d'état à gérer, vous pouvez démarrer rapidement et utiliser StackQL de manière interchangeable avec d'autres outils d'infrastructure as code, cloud native ou non. StackQL fournit aux agents IA une interface universelle pour interagir avec l'infrastructure cloud. Parmi les utilisations de StackQL :

- **Intégration d'agents IA** - Permettre aux agents de provisionner et d'interroger des ressources cloud
- **Communication avec le plan de contrôle** - Provisionnement programmatique de l'infrastructure pour les workflows agentiques
- **Observabilité du plan de données** - Surveillance et analyse en temps réel pour les opérations pilotées par l'IA
- Déploiement d'infrastructure cloud (en SQL)
- Inventaire des actifs cloud et production de rapports (en SQL)
- Conformité cloud et attestation des contrôles (en SQL)
- Détection de la dérive de configuration (en SQL)
- et plus encore, la seule limite est votre imagination !

## À qui s'adresse StackQL {#who-stackql-is-for}

StackQL s'adresse aux équipes d'ingénierie de plateforme et SRE qui exploitent des parcs cloud, aux équipes sécurité et FinOps qui ont besoin de réponses en temps réel sur l'inventaire, la conformité et les coûts, aux ingénieurs de données qui veulent des API cloud et SaaS comme sources interrogeables, et aux développeurs qui construisent des agents IA devant interroger et modifier des ressources cloud et SaaS en toute sécurité. Il s'exécute là où se fait le travail : sous forme de binaire unique sur macOS, Linux et Windows, dans Docker, en CI avec GitHub Actions, dans les shells cloud d'AWS, d'Azure et de Google Cloud, embarqué dans des applications, comme serveur compatible PostgreSQL pour les outils de BI et les notebooks, et comme serveur MCP pour les agents IA.

## Licence et support {#license-and-support}

StackQL est open source, publié sous la [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE), et son utilisation est gratuite, quelle qu'en soit la finalité. Le support communautaire passe par [GitHub Discussions](https://github.com/orgs/stackql/discussions) et [Discord](https://discord.com/invite/xVXZ9d5NxN) ; les bugs et les demandes de fonctionnalités vont dans [GitHub Issues](https://github.com/stackql/stackql/issues). Pour toute demande de support commercial, [contactez StackQL Studios](/contact-us).

## Pour commencer {#get-started}

- [Installation](/installing-stackql) - installer le binaire StackQL sur macOS, Linux ou Windows, ou l'exécuter depuis Docker, un gestionnaire de paquets ou un shell cloud
- [Agents IA](/ai-agents) - connecter des agents IA via MCP, embarquer le serveur et explorer les requêtes et le contenu de référence
- [Prise en main](/getting-started) - la hiérarchie des ressources, vos premières requêtes, l'utilisation d'un provider, les modes de sortie, les variables et le templating
- [Providers disponibles](/providers) - les providers cloud et SaaS que vous pouvez interroger et gérer, chacun avec son propre site de référence
- [Blog](/blog) - annonces produit, annonces providers et tutoriels
- [Ligne de commande](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry` et les options globales
- [Outils MCP](/mcp) - le serveur et les outils Model Context Protocol qui donnent aux agents IA la même interface SQL
- [Spécification du langage](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, les fonctions et les types de données
