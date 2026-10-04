---
slug: azure-provider-update-july-2026
title: Azure Provider Update - July 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-azure-provider-featured-image.png"
description: Major update to the StackQL Azure provider - 268 services covering the ARM control plane and Azure data planes in one provider, with expanded AI coverage including Azure AI Foundry, AI Language, Document Intelligence, Content Safety and more, available in the StackQL Provider Registry now.
keywords: [stackql, azure, provider, arm, azure ai, ai foundry, key vault, cosmos db, data plane, cloud inventory, finops]
tags: [stackql, azure, provider, arm, azure ai, ai foundry, key vault, cosmos db, data plane, cloud inventory, finops]
---

We've released a major update to the StackQL Azure provider family:

- [__`azure`__](https://azure-provider.stackql.io) - core Microsoft Azure services: __268 services, 3,473 resources and 13,559 operations__ (up from 202 services, a 33% increase in service coverage)
- [__`azure_extras`__](https://azure-extras-provider.stackql.io) - domain-specific and specialized Microsoft services (44 services)
- [__`azure_isv`__](https://azure-isv-provider.stackql.io) - Azure Native ISV and partner services: Databricks, Datadog, Confluent, Elastic, MongoDB Atlas, Oracle Database@Azure and more (27 services)
- [__`azure_stack`__](https://azure-stack-provider.stackql.io) - the Azure Stack / Azure Local family (4 services)

Service, resource and method names are consistently snake_cased, service titles carry the official Azure product names, and related services have been consolidated - `SHOW SERVICES IN azure` now reads like the Azure portal, not like an SDK package index.

## Control plane and data plane in one provider

Most Azure tooling stops at ARM. This provider exposes Azure __data plane__ APIs alongside the ARM control plane as first-class services, so the same SQL surface that manages a resource can work with what's inside it.

Enumerate Key Vaults in a subscription (control plane), then list the secrets in one of them (data plane):

~~~sql
SELECT name, location
FROM azure.key_vault.vaults
WHERE subscription_id = '<subscription_id>';

SELECT id, content_type, attributes
FROM azure.key_vault_secrets.secrets
WHERE vault_name = 'my-vault';
~~~

The same pattern extends across the platform - Storage blobs, queues and file shares, Cosmos DB tables, App Configuration key-values, Event Grid publishing, Container Registry repositories, Azure Monitor log queries and ingestion, Azure Maps, Azure AI Search documents, Service Fabric cluster operations, Batch jobs, and the Synapse and Purview workspace APIs are all present as data plane services next to their management planes.

## The AI surface

The biggest expansion in this release is AI - 20 services covering Azure AI Foundry and the Azure AI services portfolio:

- __Azure AI Foundry__: `ai_projects`, `ai_agents`, `ai_inference`, `ai_evaluation`
- __Language__: `ai_language` (conversational language understanding, question answering and their authoring surfaces in a single service), `ai_text_analytics`, `ai_translation_text`, `ai_translation_document`
- __Vision__: `ai_vision_image_analysis`, `ai_vision_face`
- __Documents__: `ai_document_intelligence`, `ai_form_recognizer`
- __Speech__: `ai_transcription`, `ai_voice_live`
- __Safety and content__: `ai_content_safety`, `ai_content_understanding`
- Plus `ai_anomaly_detector`, `ai_personalizer`, `ai_discovery` and the `cognitive_services` management plane

Inventory every Azure AI services account in a subscription, with kind and provisioning state:

~~~sql
SELECT name, kind, location, provisioning_state
FROM azure.cognitive_services.accounts
WHERE subscription_id = '<subscription_id>';
~~~

ARM's nested `properties` envelope is flattened at query time, so attributes like `provisioning_state` are ordinary columns - no JSON extraction needed for the common case.

## More new and expanded coverage

- __Communication__ - the full Azure Communication Services surface: email, SMS, chat, calling automation, phone numbers, rooms, job router, advanced messaging and identity (9 services)
- __Databases__ - Azure NetApp Files, Azure Cache for Redis, Azure Managed Redis, Azure DocumentDB (MongoDB compatibility), Azure HorizonDB, MySQL and PostgreSQL flexible servers, Azure Data Explorer (Kusto)
- __Compute and containers__ - Compute Fleet, Compute Schedule, AKS (`container_service`), Kubernetes Fleet Manager, deployment safeguards, Container Registry tasks and data plane, Azure Red Hat OpenShift, Azure VMware Solution
- __Observability__ - Azure Monitor log query, metrics query and logs ingestion data planes, Azure Monitor workspaces (managed Prometheus), health models
- __Governance__ - Microsoft Purview catalog, data map, scanning, sharing and workflow APIs; a consolidated `resource` service spanning deployments, policy, locks, template specs and subscriptions
- __Maps__ - geocoding, routing, rendering, geolocation, timezone and weather (6 services)
- __Hybrid__ - Azure Arc-enabled servers, Kubernetes, VMware vSphere and System Center VMM, Arc gateway, Azure Local

## Authentication

The provider uses Azure's standard credential chain - an `az login` session works as-is, or set service principal credentials:

~~~bash
export AZURE_TENANT_ID=<tenant_id>
export AZURE_CLIENT_ID=<client_id>
export AZURE_CLIENT_SECRET=<client_secret>
~~~

## Get started

Pull the providers from the public registry:

~~~bash
registry pull azure
registry pull azure_extras
registry pull azure_isv
registry pull azure_stack
~~~

Then explore - it's just SQL:

~~~sql
SELECT name, location, provisioning_state, vm_id
FROM azure.compute.virtual_machines
WHERE subscription_id = '<subscription_id>';
~~~

Provider docs are at [azure-provider.stackql.io](https://azure-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
