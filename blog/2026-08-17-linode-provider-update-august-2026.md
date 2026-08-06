---
slug: linode-provider-update-august-2026
title: Linode Provider Update - August 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-linode-provider-featured-image.png"
description: Update to the StackQL Linode provider adding Image Share Groups, VM maintenance policies and Akamai Cloud Pulse alerting, plus automatic pagination, native insert columns and cleaner list results across every service.
keywords: [stackql, linode, akamai, provider, image share groups, cloud pulse, maintenance policies, compute, object storage]
tags: [stackql, linode, akamai, provider, cloud-pulse]
---

We've released an update to the [__StackQL Linode provider__](https://linode-provider.stackql.io/), regenerated from the latest Linode (Akamai) OpenAPI specification. The `linode` provider now covers __21 services, 142 resources and 449 operations__ - up from 20 services, 136 resources and 425 operations in the previous release - and picks up a set of engine-level improvements that make every query cleaner and every mutation simpler.

## New Service

| Service | Description |
|---------|-------------|
| `maintenance` | VM maintenance policies - query the policies available on your account (such as live migrate versus power off/on) that control how scheduled host maintenance is applied to your Linodes |

~~~sql
SELECT slug, label, type, is_default
FROM linode.maintenance.policies;
~~~

## Image Share Groups

The largest functional addition in this release is __Image Share Groups__ - Linode's new mechanism for sharing private images across accounts. Four new resources in the `images` service cover the full workflow:

- `sharegroups` - create and manage share groups
- `sharegroup_images` - add images to a group, update or revoke individual image shares
- `sharegroup_members` - manage group membership
- `sharegroup_tokens` - the consumer-side tokens used to access images shared with you

~~~sql
-- share groups on your account and what they expose
SELECT id, label, description, images_count, members_count
FROM linode.images.sharegroups;

-- add an image to a share group
INSERT INTO linode.images.sharegroup_images (sharegroupId, images)
SELECT 12345, '[{"id": "private/54321"}]';
~~~

## Expanded Coverage in Existing Services

- `linode` - `interface_history` tracks the network interface history of a Linode as instances migrate to the new Linode Interfaces model
- `monitor` (Akamai Cloud Pulse) - alert definitions, metric definitions, dashboards and token operations updated to the latest API paths and fully covered, so you can manage alerting on managed databases, NodeBalancers and object storage with SQL
- `object_storage` - per-quota reads (`quotas`) with usage data
- `account` - service availability by region

## Provider Improvements

Beyond the API surface, this release modernizes how the provider itself behaves.

__Automatic pagination__. Linode's `page`/`pages` envelope is now wired into the provider's pagination configuration, so collection queries transparently traverse every page:

~~~sql
-- 338 rows, fetched across 4 pages behind the scenes
SELECT COUNT(*) FROM linode.linode.kernels;
~~~

__Clean list results everywhere__. Previously, many list resources returned the raw response envelope as a single row. Every enveloped list method now unwraps the `data` array, so `SELECT` returns one row per object across all 21 services.

__Native insert columns__. `INSERT` and `UPDATE` columns are now the native Linode API body property names, replacing the `data__` prefix convention from earlier releases:

~~~sql
INSERT INTO linode.linode.instances (label, region, type, image, root_pass)
SELECT 'web-01', 'us-ord', 'g6-nanode-1', 'linode/debian12', 'S3cureP@ssw0rd!';
~~~

> This is a breaking change if you have existing `INSERT` statements using `data__` prefixed columns - drop the prefix and they will work as before.

__Clean method names and working lifecycle operations__. Method names no longer carry HTTP plumbing or repeat the resource name - selects are `get`/`list`, mutations are `create`/`update`/`delete`, and lifecycle operations are named for the action they perform. Stopping and starting a Linode is now:

~~~sql
EXEC linode.linode.instances.shutdown @linodeId = '12345678';

EXEC linode.linode.instances.boot @linodeId = '12345678';
~~~

The same pattern applies across the provider - `reboot`, `resize`, `rebuild`, `clone` and `migrate` on instances, `attach`/`detach`/`resize` on volumes, `suspend`/`resume`/`patch` on managed databases, `recycle` on LKE clusters, pools and nodes, and so on. These operations were not invocable in earlier releases; the full stop/start cycle is now exercised against the live API in the provider's smoke test.

__Richer schemas__. The generation pipeline now flattens `allOf`/`oneOf` compositions in the upstream spec, so resources with union response types (Linode interfaces, managed statistics and others) present complete, queryable column sets.

## Example: Regional Capacity Check

Which regions support both Kubernetes and GPU plans, and their current status:

~~~sql
SELECT id, country, status
FROM linode.regions.regions
WHERE capabilities LIKE '%Kubernetes%'
AND capabilities LIKE '%GPU Linodes%';
~~~

## Get Started

Pull the latest provider from the public registry:

~~~bash
stackql registry pull linode;
~~~

Authenticate with a Linode API token in the `LINODE_TOKEN` environment variable, then explore:

~~~sql
SELECT id, label, region, status, type
FROM linode.linode.instances;
~~~

Provider docs, including getting-started queries for every service, are at [linode-provider.stackql.io](https://linode-provider.stackql.io/). Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
