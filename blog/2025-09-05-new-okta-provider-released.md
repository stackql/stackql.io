---
slug: new-okta-provider-released
title: New Okta Provider Released
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-okta-provider-featured-image.png"
description: Query and interact with Okta resources using SQL.
keywords: [stackql, okta, iac, analytics]
tags: [stackql, okta, iac, analytics]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

The latest `okta` provider for StackQL is available now (`v25.09.00341`), featuring a comprehensive reorganization of services and resources that better aligns with Okta's API structure. This update improves discoverability, logical grouping, and overall usability when working with Okta resources through StackQL.

## What's Changed

The updated Okta provider features a more granular and logical organization of services that mirrors Okta's API architecture more closely.  Here is a summary of the services included in the latest `okta` provider:

| Service | Description |
|---------|-------------|
| `agentpools` | Manages agent pools for on-premises integration and deployment |
| `api_tokens` | Handles API token creation, management, and access control |
| `apps` | Manages Okta application integration, configuration, and assignments |
| `attack_protection` | Configures security controls to prevent various attack vectors |
| `authenticators` | Manages authentication methods, factors, and settings |
| `authorizationservers` | Controls OAuth 2.0 and OIDC authorization servers and policies |
| `behaviors` | Configures end-user behavioral patterns for risk-based authentication |
| `brands` | Manages customization of Okta UI appearance and branding |
| `captchas` | Configures CAPTCHA settings to prevent automated attacks |
| `device_access` | Controls access policies based on device attributes |
| `device_assurances` | Manages device trust requirements and verification |
| `device_integrations` | Configures integrations with MDM and endpoint security tools |
| `device_posture_checks` | Verifies security posture of devices accessing resources |
| `devices` | Manages device enrollment, lifecycle, and inventory |
| `directories` | Handles directory service connections and synchronization |
| `domains` | Manages custom domain configuration for Okta tenant |
| `email_domains` | Controls email domains for user provisioning and validation |
| `email_servers` | Configures email server settings for notifications |
| `eventhooks` | Manages webhook subscriptions for Okta event notifications |
| `features` | Controls feature flags and preview feature management |
| `first_party_app_settings` | Configures settings for Okta-developed applications |
| `groups` | Manages group creation, membership, and rule configuration |
| `hook_keys` | Handles encryption keys for securing hook communications |
| `iam` | Provides core identity and access management functionality |
| `identity_sources` | Manages sources of identity data for user provisioning |
| `idps` | Configures external identity providers for federation |
| `inlinehooks` | Manages customization points within Okta workflows |
| `integrations` | Configures third-party service integrations |
| `logs` | Provides access to system logs and audit events |
| `logstreams` | Manages streaming of log data to external systems |
| `mappings` | Configures attribute mappings between systems |
| `meta` | Provides metadata about the Okta API and environment |
| `oauth2` | Manages OAuth 2.0 clients, scopes, and tokens |
| `okta_personal_settings` | Controls user-specific preferences and settings |
| `org` | Manages organization-level settings and configurations |
| `orgs` | Handles multi-org deployments and org relationships |
| `policies` | Configures authentication, password, and access policies |
| `principal_rate_limits` | Manages API rate limits for specific principals |
| `privileged_access` | Controls privileged access management settings |
| `push_providers` | Configures push notification delivery services |
| `rate_limit_settings` | Manages global API rate limit configurations |
| `realm_assignments` | Maps users and groups to authentication realms |
| `realms` | Configures authentication realms for different user populations |
| `risk` | Manages risk-based authentication settings and policies |
| `roles` | Controls administrative role assignments and permissions |
| `security` | Provides security settings and configurations |
| `security_events_providers` | Manages integrations with security event sources |
| `sessions` | Controls user session policies and management |
| `ssf` | Configures server-side functions for customization |
| `templates` | Manages templates for emails, notifications, and forms |
| `threats` | Controls threat intelligence settings and configurations |
| `trustedorigins` | Manages CORS and redirect configurations |
| `users` | Handles user lifecycle, profiles, and credentials |
| `webauthn_registration` | Manages WebAuthn/FIDO2 credential registration |
| `zones` | Configures network zones for policy application |

:::note

The improved organization including service and resource naming and mapping will introduce changes to queries against the previous provider version (`v23.03.00121`), you can pin the previous version in `stackql-deploy` or via `registry pull` in the interim while you make necessary query modifications.

:::

## Enhanced Documentation

The new [__Okta Provider Docs__](https://okta-provider.stackql.io/) provide comprehensive documentation on how to use the new `okta` provider including ready-to-use SQL examples for each resource and method. A standout feature is the copy-paste functionality for all SQL queries, making it incredibly easy to:

1. **Compose Infrastructure-as-Code workflows**: Each method documentation includes working SQL examples that can be directly copied into your deployment scripts or CI/CD pipelines. Simply click the copy button next to any example to get production-ready SQL code.

2. **Build analytics dashboards**: Create sophisticated cross-service queries by combining examples from different resources. The documentation's consistent query formatting makes it simple to join related data across multiple Okta services.

3. **Develop governance reports**: Copy baseline queries and customize them for your specific compliance needs. The pre-formatted SQL provides the perfect starting point for custom reporting.

## Getting Started

To start using the updated `okta` provider, simply pull the latest version from `stackql shell` or `stackql registry` command:

```sql
registry pull okta;
```

Then you can begin querying your Okta resources with SQL:

<Tabs
  defaultValue="user-group-assignments"
  values={[
    { label: 'Group assignments', value: 'user-group-assignments', },
    { label: 'User activity', value: 'user-activity', },
    { label: 'Network zones', value: 'network-zones', },
    { label: 'AuthZ servers and scopes', value: 'auth-servers-scopes', },
  ]
}>
<TabItem value="user-group-assignments">

```sql
-- Get all users and their group assignments
SELECT 
  u.id, 
  JSON_EXTRACT(u.profile, '$.email') as email,
  u.status, 
  JSON_EXTRACT(g.profile, '$.name') as group_name
FROM 
  okta.user.users u
JOIN 
  okta.group.users gu ON u.id = gu.user_id
JOIN 
  okta.group.groups g ON gu.group_id = g.id
WHERE subdomain = 'my-company';  
```

</TabItem>
<TabItem value="user-activity">

```sql
-- Report on users login and activity
SELECT
id,
activated,
created,
lastLogin,
lastUpdated,
passwordChanged,
JSON_EXTRACT(profile, '$.email') as email,
JSON_EXTRACT(profile, '$.firstName') as first_name,
JSON_EXTRACT(profile, '$.lastName') as last_name,
status,
statusChanged
FROM okta.users.users
WHERE subdomain = 'my-company';  
```

</TabItem>
<TabItem value="network-zones">

```sql
-- Inventory network zones for security audit
SELECT 
  name, 
  type, 
  status, 
  gateways, 
  proxies
FROM 
  okta.networkzone.zones
WHERE subdomain = 'my-company'; 
```

</TabItem>
<TabItem value="auth-servers-scopes">

```sql
-- Review authorization servers and scopes
SELECT 
  a.name as auth_server_name,
  s.name as scope_name,
  s.description
FROM 
  okta.authorizationserver.authorizationservers a
JOIN 
  okta.authorizationserver.scopes s ON a.id = s.server_id
WHERE subdomain = 'my-company'; 
```

</TabItem>
</Tabs>

## Use Cases for the Okta Provider

1. **Infrastructure as Code**: Manage your Okta resources alongside other cloud providers in a unified IaC approach, see [__`stackql-deploy`__](https://stackql-deploy.io/).

2. **Cost Optimization**: Identify unused resources and opportunities for cost savings.

3. **Security and Compliance**: Audit account roles, permissions, and access patterns to ensure compliance with security policies.

4. **Performance Monitoring**: Track query performance, warehouse utilization, and identify optimization opportunities.

5. **Cross-Provider Orchestration**: Build workflows that span Okta and other resource providers, enabling sophisticated data and infrastructure pipelines.

6. **Automated Reporting**: Create automated reports on Okta usage, performance, and costs.

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!