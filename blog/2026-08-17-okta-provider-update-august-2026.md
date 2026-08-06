---
slug: okta-provider-update-august-2026
title: Okta Provider Update - August 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-okta-provider-featured-image.png"
description: Update to the StackQL Okta provider adding Cross App Access APIs for securing AI agent connections, new Bot Protection, Disaster Recovery and Telephony services, expanded identity source and privileged access coverage, plus built-in pagination and LIMIT pushdown.
keywords: [stackql, okta, provider, cross app access, ai agents, identity, bot protection, disaster recovery, privileged access]
tags: [stackql, okta, provider, identity, ai-agents]
---

We've released an update to the [__StackQL Okta provider__](https://okta-provider.stackql.io/), regenerated from the latest Okta Admin Management API specification (2026.07.2). The `okta` provider now covers __57 services, 202 resources and over 730 operations__ - up from 55 services, 190 resources and 699 operations in the previous release.

## Securing AI Agents with Cross App Access

The headline addition in this release is API support for [__Cross App Access__](https://www.okta.com/blog/2025/06/introducing-cross-app-access-to-help-secure-ai-agents-in-the-enterprise/), Okta's open protocol (based on the OAuth Identity Assertion Authorization Grant) for securing agent-to-app and app-to-app connections in the enterprise. As organizations roll out AI agents and MCP-based integrations, Cross App Access replaces scattered per-app OAuth consent with centrally administered trust relationships between client apps (including AI agents) and the target apps they access.

The new `interclient_allowed_apps` and `interclient_target_apps` resources in the `apps` service let you audit and manage these trust mappings with SQL:

~~~sql
-- which client apps (agents) are allowed to access a target app
SELECT interclient_allowed_application
FROM okta.apps.interclient_allowed_apps
WHERE appId = '<targetAppId>' AND subdomain = 'my-org';
~~~

~~~sql
-- create a trust mapping allowing an agent to access a target app
INSERT INTO okta.apps.interclient_allowed_apps (
  id,
  appId,
  subdomain
)
SELECT
  '<clientAppId>',
  '<targetAppId>',
  'my-org';
~~~

Related non-human identity coverage also lands in this release: the `privileged_access` service adds the `okta_service_accounts` resource for managing Okta-managed service accounts (list, create, update and delete privileged non-human accounts).

## New Services

Four services are new in this release:

| Service | Description |
|---------|-------------|
| `bot_protection` | Configuration for Okta's bot detection and mitigation capability |
| `dr` | Enhanced Disaster Recovery - query failover status and initiate org failover and failback |
| `telephony_providers` | Custom telephony provider credentials for SMS and voice OTP delivery, including activation, testing and primary provider selection |
| `_well_known` | Org well-known metadata - app authenticator configuration, SSF transmitter metadata and associated domain customizations (apple-app-site-association, assetlinks.json and WebAuthn URIs) |

## Expanded Coverage in Existing Services

- `identity_sources` - full CRUD for custom identity source objects under Okta's Anything-as-a-Source model: new `groups`, `group_memberships` and `users` resources, plus bulk upsert and delete operations for group data and group memberships
- `devices` - new `device_os_accounts` resource to enumerate OS accounts on managed devices
- `directories` - Active Directory group attribute queries (submit a query and retrieve results) and external directory group membership updates
- `authenticators` - WebAuthn relying party ID domain verification (`verify_rp_id_domain`)

## Removed Endpoints

Okta has retired several endpoints since the last release, and they are removed from the provider accordingly:

- `device_access` - desktop MFA endpoints were removed from the management API specification
- `risk` - legacy risk provider and risk event endpoints were removed
- `sessions` - the deprecated current-session operations (create, get, refresh and close) were removed; session management by session id remains
- `org` - the deprecated Okta Support grant, extend and revoke operations (now 301 redirects) and the legacy org logo upload were removed

## Pagination and Predicate Pushdown

The provider is now generated with explicit `x-stackQL-config` in every service:

- __Pagination__ - Okta's RFC 5988 `Link` header pagination is declared on every service, so multi-page result sets are transparently exhausted by a single `SELECT`
- __LIMIT pushdown__ - a SQL `LIMIT` is pushed down to Okta's `limit` query parameter, reducing over-fetch on large collections
- __Request body translation__ - `INSERT` and `UPDATE` columns map directly to request body attributes (for example `profile` in the examples above), no `data__` prefixes required

## Example: Find Inactive Users

~~~sql
SELECT id,
  JSON_EXTRACT(profile, '$.login') AS login,
  status,
  lastLogin
FROM okta.users.users
WHERE subdomain = 'my-org'
AND status IN ('SUSPENDED', 'DEPROVISIONED', 'LOCKED_OUT');
~~~

## Get Started

Pull the latest provider from the public registry:

~~~bash
stackql registry pull okta;
~~~

Authenticate by exporting an Okta API token as `OKTA_API_TOKEN`, then explore:

~~~sql
SELECT id, label, status
FROM okta.apps.applications
WHERE subdomain = 'my-org';
~~~

Provider docs, including getting-started queries for each service, are at [okta-provider.stackql.io](https://okta-provider.stackql.io/). Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
