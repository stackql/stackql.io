---
title: SQL para infraestrutura na nuvem, APIs SaaS e agentes de IA
sidebar_label: Boas-vindas ao StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: O StackQL é um mecanismo de código aberto com que pessoas e agentes de IA consultam, provisionam e operam serviços na nuvem e SaaS com SQL, sem arquivo de estado nem SDK por nuvem.
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
  A interface universal<br/>
  para a era agêntica
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
        <h3><FaRobot size={22} className="homeCardIcon" />Engenharia de plataforma agêntica</h3>
      </div>
      <div className="card__body">
        Uma superfície universal para agentes e pessoas criarem mudanças lado a lado, na velocidade das máquinas. Vários autores, estado em tempo real e execução limitada por políticas como preocupações de primeira classe - a base de que os sistemas autônomos precisam para operar com segurança.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC para a era agêntica</h3>
      </div>
      <div className="card__body">
        O código continua sendo intenção. A realidade são dados. O StackQL oferece a agentes e operadores uma única interface declarativa para consultar o estado em tempo real em todos os providers, garantir o estado desejado de forma idempotente e percorrer os planos de controle e de dados com uma única gramática - sem arquivo de estado, sem SDK por nuvem.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Observabilidade autônoma</h3>
      </div>
      <div className="card__body">
        Trate as APIs na nuvem como sua camada de dados, não como seu destino de implantação. Cada leitura é em tempo real, cada mudança é um evento e o desvio se torna um sinal sobre o qual um agente pode raciocinar - não um alarme que uma pessoa precisa perseguir.
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

Implantar recursos no seu provedor de nuvem é tão fácil quanto escrever uma instrução [`INSERT`](/language-spec/insert)...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

Com o StackQL, você desenvolve do seu jeito: de forma declarativa ou procedural. Com uma gramática fácil de aprender e nenhum arquivo de estado para gerenciar, você começa rapidamente e pode usar o StackQL de forma intercambiável com outras ferramentas de infraestrutura como código, nativas da nuvem ou não. O StackQL oferece uma interface universal para que agentes de IA interajam com a infraestrutura na nuvem. Entre os usos do StackQL estão:

- **Integração com agentes de IA** - Permita que agentes provisionem e consultem recursos na nuvem
- **Comunicação com o plano de controle** - Provisionamento programático de infraestrutura para workflows agênticos
- **Observabilidade do plano de dados** - Monitoramento e análise em tempo real para operações orientadas por IA
- Implantação de infraestrutura na nuvem (com SQL)
- Inventário de ativos na nuvem e relatórios (com SQL)
- Conformidade na nuvem e atestação de controles (com SQL)
- Detecção de desvio de configuração (com SQL)
- e muito mais, com a sua imaginação como único limite!

## Para quem é o StackQL {#who-stackql-is-for}

O StackQL é para equipes de engenharia de plataforma e SRE que operam parques de recursos na nuvem, equipes de segurança e FinOps que precisam de respostas em tempo real sobre inventário, conformidade e custos, engenheiros de dados que querem usar as APIs na nuvem e de SaaS como fontes consultáveis e desenvolvedores que criam agentes de IA que precisam consultar e alterar recursos na nuvem e de SaaS com segurança. Ele é executado onde o trabalho acontece: como um único binário no macOS, Linux e Windows, no Docker, em CI com GitHub Actions, nos shells da AWS, do Azure e do Google Cloud, incorporado em aplicações, como servidor compatível com PostgreSQL para ferramentas de BI e notebooks e como servidor MCP para agentes de IA.

## Licença e suporte {#license-and-support}

O StackQL é de código aberto sob a [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) e de uso gratuito, para qualquer finalidade. O suporte da comunidade é feito pelo [GitHub Discussions](https://github.com/orgs/stackql/discussions) e pelo [Discord](https://discord.com/invite/xVXZ9d5NxN); bugs e solicitações de funcionalidades vão para o [GitHub Issues](https://github.com/stackql/stackql/issues). Para consultas sobre suporte comercial, [entre em contato com a StackQL Studios](/contact-us).

## Comece agora {#get-started}

- [Instalação](/installing-stackql) - instale o binário do StackQL no macOS, Linux ou Windows, ou execute-o a partir do Docker, de um gerenciador de pacotes ou de um shell na nuvem
- [Agentes de IA](/ai-agents) - conecte agentes de IA por meio do MCP, incorpore o servidor e explore consultas e conteúdo de referência
- [Primeiros passos](/getting-started) - a hierarquia de recursos, suas primeiras consultas, o uso de um provider, os modos de saída, as variáveis e o uso de templates
- [Providers disponíveis](/providers) - os providers para serviços na nuvem e SaaS que você pode consultar e gerenciar, cada um com seu próprio site de referência
- [Blog](/blog) - anúncios de produto, anúncios de providers e tutoriais
- [Linha de comando](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry` e as opções globais
- [Ferramentas MCP](/mcp) - o servidor e as ferramentas do Model Context Protocol que oferecem aos agentes de IA a mesma interface SQL
- [Especificação da linguagem](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, funções e tipos de dados
