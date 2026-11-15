---
slug: new-ovh-provider-available
title: New OVHcloud Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-ovh-provider-featured-image.png"
description: A new StackQL provider for OVHcloud - query and manage Public Cloud projects, instances, Managed Kubernetes, managed databases, storage and networks, bare metal servers, VPS, domains and DNS, IP addresses, vRack, IAM and the rest of the OVHcloud API using SQL - available in the StackQL Provider Registry now.
keywords: [stackql, ovh, ovhcloud, public cloud, bare metal, dedicated servers, managed kubernetes, iam, dns, provider, infrastructure as code, cloud inventory, sql]
tags: [stackql, ovh, ovhcloud, provider, cloud, bare-metal]
---

We've released a new StackQL provider for OVHcloud:

- [__`ovh`__](https://ovh-provider.stackql.io) - the OVHcloud API, v1 and v2: Public Cloud (__`cloud`__, __`cloud_kube`__, __`cloud_database`__, __`cloud_network`__, __`cloud_storage`__, __`cloud_ai`__), bare metal and private cloud (__`dedicated_server`__, __`vps`__, __`dedicated_cloud`__), network (__`ip`__, __`vrack`__, __`ip_loadbalancing`__), __`domain`__, web hosting and email, the account (__`me`__) and __`iam`__ (98 services, 2,470 resources, 6,590 operations)

<!-- truncate -->

The provider covers the OVHcloud control plane as OVHcloud publishes it: 88 API families across API v1 and API v2, one StackQL service per family, with the two Public Cloud families split by product. That is Public Cloud projects and everything in them (instances, flavors and images, Managed Kubernetes, managed databases, object, block and file storage, private networks, gateways and load balancers, AI Training, Notebooks and Deploy), dedicated servers, VPS, Hosted Private Cloud, Nutanix, domains and DNS zones, IP blocks, vRack, load balancers, web hosting, Exchange and email, telephony, ordering, billing, the account itself and IAM policies, resources and resource groups. The surface is generated from the OpenAPI documents OVHcloud serves for each API family; operations OVHcloud has deprecated and replaced are left out in favour of their replacements.

## Connect

Authentication is an OVHcloud service account using the OAuth2 client credentials grant, read from `OVH_CLIENT_ID` and `OVH_CLIENT_SECRET`, with `OVH_ENDPOINT` selecting the region (`ovh-eu`, the default, `ovh-ca` or `ovh-us`) - the same variables the OVHcloud Terraform provider uses. The service account needs an IAM policy for what you query; a call it is not granted is answered with the IAM action it lacked. Public Cloud resources are scoped to a project; setting `OVH_CLOUD_PROJECT_SERVICE` resolves it for the session, and a `WHERE service_name = '...'` value takes precedence when given:

```bash
export OVH_ENDPOINT=ovh-eu
export OVH_CLIENT_ID=...
export OVH_CLIENT_SECRET=...
export OVH_CLOUD_PROJECT_SERVICE=0123456789abcdef0123456789abcdef   # optional
stackql shell
```

A pre-issued access token (`OVH_ACCESS_TOKEN`) works with the `--auth` flag; application key signing is not available in this version. Columns, parameters and body attributes are snake_case; the provider maps them to the API's camelCase names on the wire.

## The Public Cloud estate

Most OVHcloud collections return only identifiers, one call per item to see the detail. The provider asks for the full objects in the same request, so a list is one statement and one round trip. Every project in the account:

```sql
SELECT project_id, project_name, status, plan_code, creation_date
FROM ovh.cloud.projects
ORDER BY project_name;
```

The instances of the project named by `OVH_CLOUD_PROJECT_SERVICE`, then its Kubernetes clusters and managed databases:

```sql
SELECT id, name, region, status, flavor_id, created
FROM ovh.cloud.instances
ORDER BY region, name;

SELECT id, name, region, version, status, is_up_to_date
FROM ovh.cloud_kube.clusters;

SELECT id, description, engine, version, plan, flavor, node_number, status
FROM ovh.cloud_database.services;
```

## Quota headroom

Instance and core usage against quota for each region of a project, read out of the nested `instance` column:

```sql
SELECT region,
       JSON_EXTRACT(instance, '$.usedInstances') AS used_instances,
       JSON_EXTRACT(instance, '$.maxInstances') AS max_instances,
       JSON_EXTRACT(instance, '$.usedCores') AS used_cores,
       JSON_EXTRACT(instance, '$.maxCores') AS max_cores
FROM ovh.cloud.quotas
ORDER BY region;
```

Block storage by state and size, to find volumes attached to nothing:

```sql
SELECT name, region, size, type, status
FROM ovh.cloud_storage.volumes
WHERE JSON_ARRAY_LENGTH(attached_to) = 0;
```

## Bare metal, VPS and network

The dedicated servers of the account with their datacenter, operating system and power state, and the VPS fleet beside them:

```sql
SELECT name, datacenter, os, state, power_state, commercial_range
FROM ovh.dedicated_server.servers
ORDER BY datacenter, name;

SELECT name, display_name, state, zone, vcore, memory_limit, offer_type
FROM ovh.vps.vps;
```

IP blocks and what each is routed to, and the vRacks that tie the estate together:

```sql
SELECT ip, type, version, country,
       JSON_EXTRACT(routed_to, '$.serviceName') AS routed_to
FROM ovh.ip.ips;

SELECT name, description
FROM ovh.vrack.vracks;
```

## IAM as rows

The IAM policies of the account, the identities they apply to and the actions they allow. API v2 collections are paged; `x_pagination_size` sets the page size:

```sql
SELECT name, read_only, identities,
       JSON_EXTRACT(permissions, '$.allow') AS allowed_actions
FROM ovh.iam.policies
WHERE x_pagination_size = 500;
```

Every resource IAM knows about, by type:

```sql
SELECT type, COUNT(*) AS resources
FROM ovh.iam.resources
WHERE x_pagination_size = 1000
GROUP BY type
ORDER BY resources DESC;
```

## Domains and DNS

Domains with their renewal state and name servers, and the records of a zone:

```sql
SELECT domain, state, renewal_state, expiration_date, transfer_lock_status
FROM ovh.domain.domains;

SELECT id, sub_domain, field_type, target, ttl
FROM ovh.domain.zone_records
WHERE zone_name = 'example.com';
```

## Provisioning

Mutations use the same SQL grammar - `INSERT` creates, `UPDATE` changes, `DELETE` removes, and lifecycle actions are `EXEC` methods on the resource they act on. An SSH key, a private network, a project description and an instance reboot:

```sql
INSERT INTO ovh.cloud.sshkeys (name, public_key)
SELECT 'deploy-key', 'ssh-ed25519 AAAA... deploy@example.com'
RETURNING id, name, finger_print;

INSERT INTO ovh.cloud_network.network_privates (name, vlan_id)
SELECT 'backend', 42;

UPDATE ovh.cloud.projects
SET description = 'analytics sandbox'
WHERE service_name = '0123456789abcdef0123456789abcdef';

EXEC ovh.cloud.instances.reboot
  @instance_id = '<instance_id>',
  @type = 'soft';

DELETE FROM ovh.cloud.sshkeys
WHERE key_id = '<key_id>';
```

A DNS record is the same shape, with the zone refreshed afterwards to publish it:

```sql
INSERT INTO ovh.domain.zone_records (zone_name, field_type, sub_domain, target, ttl)
SELECT 'example.com', 'A', 'www', '203.0.113.10', 3600;

EXEC ovh.domain.zones.refresh
  @zone_name = 'example.com';
```

`INSERT` and `UPDATE` send typed JSON: numbers and booleans as written, JSON-shaped strings as arrays and objects.

## Several projects, several clouds

The same read across two projects in one result set, each naming its project:

```sql
SELECT 'project-a' AS project, name, region, status
FROM ovh.cloud.instances
WHERE service_name = '<project_a_id>'
UNION ALL
SELECT 'project-b' AS project, name, region, status
FROM ovh.cloud.instances
WHERE service_name = '<project_b_id>';
```

OVHcloud instances alongside DigitalOcean droplets, using the `digitalocean` provider in the same session:

```sql
SELECT 'ovh' AS cloud, name, region, status
FROM ovh.cloud.instances
UNION ALL
SELECT 'digitalocean' AS cloud, name, JSON_EXTRACT(region, '$.slug') AS region, status
FROM digitalocean.compute.droplets;
```

## Get started

Pull the provider and start querying:

```sql
REGISTRY PULL ovh;
```

The full reference is at [ovh-provider.stackql.io](https://ovh-provider.stackql.io), and the source is at [stackql-registry/stackql-provider-ovh](https://github.com/stackql-registry/stackql-provider-ovh). Questions and requests are welcome in the [StackQL Discussions](https://github.com/orgs/stackql/discussions).
