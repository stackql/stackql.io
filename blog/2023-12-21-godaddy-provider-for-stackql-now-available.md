---
slug: godaddy-provider-for-stackql-now-available
title: "GoDaddy Provider for StackQL Now Available"
hide_table_of_contents: false
authors:  
  - jeffreyaven
image: "/img/blog/stackql-godaddy-provider-featured-image.png"
keywords: [stackql, godaddy, analytics, reporting]
tags: [stackql, godaddy, analytics, reporting]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Give us a ⭐ on [__GitHub__](https://github.com/stackql/stackql)

With the GoDaddy provider, users can leverage StackQL to interact with their GoDaddy resources directly through SQL queries. The addition of `godaddy` to the StackQL provider catalog further enabled a unified SQL-based experience for cloud services management.

## Key Features
- **Domain Management**: List, update, and monitor domains registered with GoDaddy domains, including registration, renewal, and transfer.
- **DNS Configuration**: Manage DNS settings for your domains using SQL commands, including querying and updating DNS records.
- **Security Certificates**: Query and manage SSL certificates.
- **Order Management**: Report on orders related to GoDaddy services.

## Getting Started
To begin using the GoDaddy provider, with `stackql` installed (see [here](/install)), create a [GoDaddy API token](https://developer.godaddy.com/keys), populate an environment variable named `GODADDY_API_KEY` with this value, using `stackql exec` or `stackql shell` pull the latest provider for GoDaddy using:

```
REGISTRY PULL godaddy;
```

start querying!

## Example Queries

Here are some sample queries to get you started with the `godaddy` provider.

### List Domains

Heres a simple extract of domains with status, expiry date, privacy, and auto-renewal status:

<Tabs
  defaultValue="query"
  values={[
    { label: 'Query', value: 'query', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="query">

```sql
SELECT 
 domain, 
 status, 
 expires, 
 privacy, 
 renewAuto 
FROM godaddy.domains.domains;
```
</TabItem>
<TabItem value="results">

```
|----------------------------------|-------------------------|--------------------------|---------|-----------|
|              domain              |         status          |         expires          | privacy | renewAuto |
|----------------------------------|-------------------------|--------------------------|---------|-----------|
| 1novel.idea                      | CANCELLED               | 2023-08-10T23:59:59.000Z | true    | false     |
|----------------------------------|-------------------------|--------------------------|---------|-----------|
| sunflower.market                 | ACTIVE                  | 2025-09-26T21:30:37.000Z | true    | true      |
|----------------------------------|-------------------------|--------------------------|---------|-----------|
| moonlightchange.org              | ACTIVE                  | 2025-09-26T16:32:09.000Z | true    | true      |
|----------------------------------|-------------------------|--------------------------|---------|-----------|
| moonlightchange.net.au           | ACTIVE                  | 2024-07-13T10:17:01.000Z | false   | true      |
|----------------------------------|-------------------------|--------------------------|---------|-----------|
| starlightdreams.biz              | CANCELLED_TRANSFER      | null                     | true    | true      |
|----------------------------------|-------------------------|--------------------------|---------|-----------|
| reactinsight.io                  | UPDATED_OWNERSHIP       | 2022-03-02T04:09:38.000Z | false   | true      |
|----------------------------------|-------------------------|--------------------------|---------|-----------|
| quickdatafix.com.au              | PENDING_HOLD_REDEMPTION | 2023-12-02T02:56:14.000Z | false   | false     |
|----------------------------------|-------------------------|--------------------------|---------|-----------|
```
</TabItem>
</Tabs>

### Domain Summary by Status

Heres a quick summary by status:

<Tabs
  defaultValue="query"
  values={[
    { label: 'Query', value: 'query', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="query">

```sql
SELECT status, count(*) as num_domains 
FROM godaddy.domains.domains
GROUP BY status;
```
</TabItem>
<TabItem value="results">

```
|-------------------------|-------------|                                                                                                                                                     
|         status          | num_domains |                                                                                                                                                     
|-------------------------|-------------|                                                                                                                                                     
| ACTIVE                  |          19 |                                                                                                                                                     
|-------------------------|-------------|                                                                                                                                                     
| CANCELLED               |          45 |                                                                                                                                                     
|-------------------------|-------------|                                                                                                                                                     
| CANCELLED_TRANSFER      |           1 |                                                                                                                                                     
|-------------------------|-------------|                                                                                                                                                     
| PENDING_HOLD_REDEMPTION |           1 |                                                                                                                                                     
|-------------------------|-------------|                                                                                                                                                     
| UPDATED_OWNERSHIP       |          34 |                                                                                                                                                     
|-------------------------|-------------|  
```
</TabItem>
</Tabs>

### Listing Nameservers for a Domain

Heres a query expanding nameservers for a given domains:

<Tabs
  defaultValue="query"
  values={[
    { label: 'Query', value: 'query', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="query">

```sql
SELECT 
 domain, 
 ns.value as nameserver 
FROM godaddy.domains.domains, json_each(nameServers) as ns
WHERE domain = 'chessenthusiastclubvictoria.org.au';
```
</TabItem>
<TabItem value="results">

```
|--------------------------------------|-------------------|
|              domain                  |    nameserver     |
|--------------------------------------|-------------------|
| chessenthusiastclubvictoria.org.au   | ns1.wordpress.com |
|--------------------------------------|-------------------|
| chessenthusiastclubvictoria.org.au   | ns2.wordpress.com |
|--------------------------------------|-------------------|
| chessenthusiastclubvictoria.org.au   | ns3.wordpress.com |
|--------------------------------------|-------------------|
```
</TabItem>
</Tabs>

### Get DNS Records for a Domain

Heres an example query to get the `CNAME` records for a domain, you could use this to get any other type of DNS records (`A`, `AAAA`, `MX`, `TXT`, etc.):

<Tabs
  defaultValue="query"
  values={[
    { label: 'Query', value: 'query', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="query">

```sql
select data, name, ttl, type  from godaddy.domains.records
where domain = 'zetadata.com.au' and type = 'CNAME';
```
</TabItem>
<TabItem value="results">

```
|-------------------------------------|----------------|------|-------|                                                                                                                       
|                data                 |      name      | ttl  | type  |                                                                                                                       
|-------------------------------------|----------------|------|-------|                                                                                                                       
| @                                   | www            | 3600 | CNAME |                                                                                                                       
|-------------------------------------|----------------|------|-------|                                                                                                                       
| _domainconnect.gd.domaincontrol.com | _domainconnect | 3600 | CNAME |                                                                                                                       
|-------------------------------------|----------------|------|-------| 
```
</TabItem>
</Tabs>

You can visit the [GoDaddy StackQL provider docs](https://godaddy.stackql.io/providers/godaddy) for a detailed view of all the features and services.

## Join the Conversation
We want your feedback to improve the StackQL experience continually. Visit our [forum](https://github.com/stackql/stackql/discussions) to discuss the new GoDaddy provider and share your thoughts.