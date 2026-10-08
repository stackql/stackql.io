---
title: SQL for cloud infrastructure, SaaS APIs and AI agents
sidebar_label: Welcome to StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: StackQL is an open-source engine that lets humans and AI agents query, provision and operate cloud and SaaS services with SQL, with no state file and no per-cloud SDK.
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
  The Universal Interface<br/>
  for the Agentic Era
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
        <h3><FaRobot size={22} className="homeCardIcon" />Agentic Platform Engineering</h3>
      </div>
      <div className="card__body">
        A universal surface for agents and humans to author changes side by side, at machine speed. Multiple writers, live state, and policy-bounded execution as first-class concerns - the foundation autonomous systems need to operate safely.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC for the Agentic Era</h3>
      </div>
      <div className="card__body">
        Code is still intent. Reality is data. StackQL gives agents and operators one declarative interface to query live state across providers, assert desired state idempotently, and traverse control and data planes through a single grammar - no state file, no per-cloud SDK.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Autonomous Observability</h3>
      </div>
      <div className="card__body">
        Treat cloud APIs as your data layer, not your deployment target. Every read is live, every change is an event, and drift becomes a signal an agent can reason about - not an alarm a human has to chase.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Query', value: 'iql', },
    { label: 'Results', value: 'data', },
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

Deploying resources using your cloud provider is as easy as writing an [`INSERT`](/language-spec/insert) statement...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

Using StackQL you can develop your way: declarative or procedural. With an easy grammar to learn and no state file to manage, you can get started quickly and use StackQL interchangeably with other infrastructure as code tools, cloud native or otherwise. StackQL provides a universal interface for AI agents to interact with cloud infrastructure. Uses for StackQL include:

- **AI Agent Integration** - Enable agents to provision and query cloud resources
- **Control Plane Communication** - Programmatic infrastructure provisioning for agentic workflows
- **Data Plane Observability** - Real-time monitoring and analytics for AI-driven operations
- Cloud infrastructure deployment (using SQL)
- Cloud asset inventory and reporting (using SQL)
- Cloud compliance and control attestation (using SQL)
- Configuration drift detection (using SQL)
- and more, only limited by your imagination!

## Who StackQL is for

StackQL is for platform engineering and SRE teams that operate cloud estates, security and FinOps teams that need live inventory, compliance and cost answers, data engineers who want cloud and SaaS APIs as queryable sources, and developers building AI agents that must query and change cloud and SaaS resources safely. It runs wherever the work is: as a single binary on macOS, Linux and Windows, in Docker, in CI with GitHub Actions, in the AWS, Azure and Google Cloud shells, embedded in applications, as a PostgreSQL-compatible server for BI tools and notebooks, and as an MCP server for AI agents.

## License and support

StackQL is open source under the [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) and free to use, for any purpose. Community support is through [GitHub Discussions](https://github.com/orgs/stackql/discussions) and [Discord](https://discord.com/invite/xVXZ9d5NxN); bugs and feature requests go to [GitHub Issues](https://github.com/stackql/stackql/issues). For commercial support enquiries, [contact StackQL Studios](/contact-us).

## Get started

- [Installation](/installing-stackql) - install the StackQL binary on macOS, Linux or Windows, or run it from Docker, a package manager or a cloud shell
- [AI Agents](/ai-agents) - connect AI agents through MCP, embed the server, and explore queries and reference content
- [Getting started](/getting-started) - the resource hierarchy, your first queries, using a provider, output modes, variables and templating
- [Available providers](/providers) - the cloud and SaaS providers you can query and manage, each with its own reference site
- [Blog](/blog) - product announcements, provider announcements and tutorials
- [Command line usage](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry` and the global flags
- [MCP tools](/mcp) - the Model Context Protocol server and tools that give AI agents the same SQL interface
- [Language specification](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, functions and data types
