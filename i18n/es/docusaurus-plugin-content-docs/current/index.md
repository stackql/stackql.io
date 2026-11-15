---
title: SQL para infraestructura en la nube, API de SaaS y agentes de IA
sidebar_label: Le damos la bienvenida a StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: StackQL es un motor de código abierto con el que personas y agentes de IA consultan, aprovisionan y operan servicios en la nube y de SaaS con SQL, sin archivo de estado ni SDK por nube.
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
  La interfaz universal<br/>
  para la era agéntica
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
        <h3><FaRobot size={22} className="homeCardIcon" />Ingeniería de plataformas agéntica</h3>
      </div>
      <div className="card__body">
        Una superficie universal en la que agentes y personas crean cambios codo con codo, a la velocidad de las máquinas. Varios autores, estado en tiempo real y ejecución limitada por políticas como aspectos de primer orden - la base que necesitan los sistemas autónomos para operar de forma segura.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC para la era agéntica</h3>
      </div>
      <div className="card__body">
        El código sigue siendo la intención. La realidad son datos. StackQL ofrece a agentes y operadores una única interfaz declarativa para consultar el estado en tiempo real de todos sus providers, establecer el estado deseado de forma idempotente y recorrer los planos de control y de datos con una sola gramática - sin archivo de estado y sin un SDK por nube.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Observabilidad autónoma</h3>
      </div>
      <div className="card__body">
        Trate las API en la nube como su capa de datos, no como su destino de despliegue. Cada lectura es en tiempo real, cada cambio es un evento y la desviación se convierte en una señal sobre la que un agente puede razonar - no en una alarma que una persona tiene que perseguir.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Consulta', value: 'iql', },
    { label: 'Resultados', value: 'data', },
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

Desplegar recursos en su proveedor de nube es tan fácil como escribir una instrucción [`INSERT`](/language-spec/insert)...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

Con StackQL puede desarrollar a su manera: de forma declarativa o procedimental. Con una gramática fácil de aprender y sin archivo de estado que gestionar, puede empezar rápidamente y usar StackQL indistintamente con otras herramientas de infraestructura como código, nativas de la nube o no. StackQL proporciona una interfaz universal para que los agentes de IA interactúen con la infraestructura en la nube. Entre los usos de StackQL se incluyen:

- **Integración con agentes de IA** - Permita que los agentes aprovisionen y consulten recursos en la nube
- **Comunicación con el plano de control** - Aprovisionamiento programático de infraestructura para workflows agénticos
- **Observabilidad del plano de datos** - Supervisión y análisis en tiempo real para operaciones impulsadas por IA
- Despliegue de infraestructura en la nube (con SQL)
- Inventario de activos en la nube e informes (con SQL)
- Cumplimiento en la nube y certificación de controles (con SQL)
- Detección de desviaciones de configuración (con SQL)
- ¡y más, con su imaginación como único límite!

## Para quién es StackQL {#who-stackql-is-for}

StackQL está pensado para los equipos de ingeniería de plataformas y SRE que operan parques de recursos en la nube, los equipos de seguridad y FinOps que necesitan respuestas en tiempo real sobre inventario, cumplimiento y costos, los ingenieros de datos que quieren usar las API en la nube y de SaaS como fuentes consultables, y los desarrolladores que crean agentes de IA que deben consultar y modificar recursos en la nube y de SaaS de forma segura. Se ejecuta allí donde se hace el trabajo: como un único binario en macOS, Linux y Windows, en Docker, en CI con GitHub Actions, en los shells de AWS, Azure y Google Cloud, integrado en aplicaciones, como servidor compatible con PostgreSQL para herramientas de BI y notebooks, y como servidor MCP para agentes de IA.

## Licencia y soporte {#license-and-support}

StackQL es de código abierto bajo la [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) y su uso es gratuito, para cualquier fin. El soporte de la comunidad se ofrece a través de [GitHub Discussions](https://github.com/orgs/stackql/discussions) y [Discord](https://discord.com/invite/xVXZ9d5NxN); los errores y las solicitudes de funcionalidades se envían a [GitHub Issues](https://github.com/stackql/stackql/issues). Para consultas sobre soporte comercial, [póngase en contacto con StackQL Studios](/contact-us).

## Por dónde empezar {#get-started}

- [Instalación](/installing-stackql) - instale el binario de StackQL en macOS, Linux o Windows, o ejecútelo desde Docker, un gestor de paquetes o un shell en la nube
- [Agentes de IA](/ai-agents) - conecte agentes de IA mediante MCP, integre el servidor y explore consultas y contenido de referencia
- [Primeros pasos](/getting-started) - la jerarquía de recursos, sus primeras consultas, el uso de un provider, los modos de salida, las variables y el uso de plantillas
- [Providers disponibles](/providers) - los providers para servicios en la nube y de SaaS que puede consultar y gestionar, cada uno con su propio sitio de referencia
- [Blog](/blog) - anuncios de producto, anuncios de providers y tutoriales
- [Línea de comandos](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry` y las opciones globales
- [Herramientas MCP](/mcp) - el servidor y las herramientas de Model Context Protocol que ofrecen a los agentes de IA la misma interfaz SQL
- [Especificación del lenguaje](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, funciones y tipos de datos
