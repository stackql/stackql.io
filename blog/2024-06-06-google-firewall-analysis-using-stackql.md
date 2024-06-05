---
slug: google-firewall-analysis-using-stackql
title: Google Firewall Analysis using StackQL
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-googleadmin-provider-featured-image.png"
description: Deploy, configure and query GCP resources using SQL.
keywords: [stackql, google, gcp, google cloud platform, iac, analytics]
tags: [stackql, google, gcp, google cloud platform, iac, analytics]
---

Analyzing firewall rules is crucial for maintaining security in your cloud infrastructure. Using StackQL, you can efficiently query and analyze Google Cloud firewall configurations to ensure that your security policies are correctly implemented and that there are no unexpected open ports or protocols that might pose a security risk. Below is a simple query that retrieves important details about the ingress firewall rules for a specific network in a Google Cloud project.

```sql
SELECT 
	name, 
	source_range,
	ip_protocol,
	allowed_ports,
	direction
FROM (
	SELECT 
	name,
	source_ranges.value as source_range,  
	JSON_EXTRACT(allowed.value, '$.IPProtocol') as ip_protocol,
	JSON_EXTRACT(allowed.value, '$.ports') as allowed_ports,
	direction
	FROM google.compute.firewalls, json_each(sourceRanges) as source_ranges, json_each(allowed) as allowed
	WHERE project = 'stackql-k8s-the-hard-way-demo' 
	AND network = 'https://www.googleapis.com/compute/v1/projects/stackql-k8s-the-hard-way-demo/global/networks/kubernetes-the-hard-way-dev-vpc'
) t 
WHERE 
source_range = '0.0.0.0/0' 
and direction = 'INGRESS';
```

This query provides a comprehensive list of all ingress firewall rules that apply to any IP address (`0.0.0.0/0`) within the specified Google Cloud project and network. The results include the firewall rule name, the source IP range, the protocol, the allowed ports, and the direction of the traffic, an example is shown below:

```
|-----------------------------------------------|--------------|-------------|---------------|-----------|                                                                                         
|                     name                      | source_range | ip_protocol | allowed_ports | direction |                                                                                         
|-----------------------------------------------|--------------|-------------|---------------|-----------|                                                                                         
| default-allow-icmp                            | 0.0.0.0/0    | icmp        | null          | INGRESS   |                                                                                         
|-----------------------------------------------|--------------|-------------|---------------|-----------|                                                                                         
| default-allow-rdp                             | 0.0.0.0/0    | tcp         | ["3389"]      | INGRESS   |                                                                                         
|-----------------------------------------------|--------------|-------------|---------------|-----------|                                                                                         
| default-allow-ssh                             | 0.0.0.0/0    | tcp         | ["22"]        | INGRESS   |                                                                                         
|-----------------------------------------------|--------------|-------------|---------------|-----------|                                                                                         
| kubernetes-the-hard-way-dev-allow-external-fw | 0.0.0.0/0    | tcp         | ["22"]        | INGRESS   |                                                                                         
|-----------------------------------------------|--------------|-------------|---------------|-----------|                                                                                         
| kubernetes-the-hard-way-dev-allow-external-fw | 0.0.0.0/0    | tcp         | ["6443"]      | INGRESS   |                                                                                         
|-----------------------------------------------|--------------|-------------|---------------|-----------|                                                                                         
| kubernetes-the-hard-way-dev-allow-external-fw | 0.0.0.0/0    | icmp        | null          | INGRESS   |                                                                                         
|-----------------------------------------------|--------------|-------------|---------------|-----------| 
```

You can use this query to help quickly identify potential security vulnerabilities. Regularly auditing these rules ensures that your cloud environment remains secure and that only the necessary ports and protocols are open to the internet.  

Give us your feedback! ⭐ us [__here__](https://github.com/stackql/stackql)!