---
title: JSON_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Replace existing values in a JSON document using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Updates existing values in a JSON object based on their path. `JSON_REPLACE` does not add new keys to the document; it only replaces the values of existing keys.

See also:  
[[`SELECT`]](/docs/language-spec/select) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[` JSON_SET `]](/docs/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/docs/language-spec/functions/json/json_replace)

* * * 

## Syntax

```sql
SELECT JSON_REPLACE(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Arguments

__*json_column*__  
The JSON column or expression from which to replace values.

__*path*__  
The JSON path expression pointing to the location within the JSON document where the replacement should occur. The path must point to an existing key.

__*new_value*__  
The new value to replace at the specified path in the JSON document.

## Return Value(s)
Returns a modified JSON document with the specified value replaced. If the specified path does not exist, the original JSON document is returned unchanged.

:::note Behavior If a Key Exists

| Function                                                     | Overwrite if already exists? | Create if does not exist? |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/docs/language-spec/functions/json/json_insert) | No                           | Yes                       |
| [`json_replace()`](/docs/language-spec/functions/json/json_replace) | Yes                          | No                        |
| [`json_set()`](/docs/language-spec/functions/json/json_set)       | Yes                          | Yes                       |


* * *

## Examples

### Replace a Value in a JSON Object

```sql
SELECT sku as original,
JSON_REPLACE(sku, '$.name', 'Standard_EU') as updated
FROM azure.container_registry.registries 
WHERE registryName = 'exampleRegistryProd' 
AND subscriptionId = '12345678-abcd-1234-abcd-1234abcd5678' 
AND resourceGroupName = 'exampleResourceGroupProd';
/* expected output...
|---------------------------------------|------------------------------------------|
|               original                |                 updated                  |
|---------------------------------------|------------------------------------------|
| {"name":"Standard","tier":"Standard"} | {"name":"Standard_EU","tier":"Standard"} |
|---------------------------------------|------------------------------------------|
*/
```

### Replace Value in JSON Object

```sql
select
  properties beforechange,
  json_replace(json_remove(properties,
                                '$.allowPort25Out',
                                '$.auxiliarySku',
                                '$.provisioningState',
                                '$.resourceGuid',
                                '$.macAddress',
                                '$.vnetEncryptionSupported',
                                '$.enableIPForwarding',
                                '$.defaultOutboundAccess',
                                '$.primary',
                                '$.virtualMachine',
                                '$.hostedWorkloads',
                                '$.tapConfigurations',
                                '$.nicType',
                                '$.auxiliaryMode',
                                '$.ipConfigurations[0].id',
                                '$.ipConfigurations[0].etag',
                                '$.ipConfigurations[0].type',
                                '$.ipConfigurations[0].properties.provisioningState',
                                '$.ipConfigurations[0].properties.privateIPAddress',
                                '$.ipConfigurations[0].properties.privateIPAllocationMethod',
                                '$.ipConfigurations[0].properties.primary',
                                '$.ipConfigurations[0].properties.privateIPAddressVersion'
                              ),
                    '$.ipConfigurations[0].name',
                    'vmss-flex-vnet-nic01-defaultIpConfiguration'
                    ) afterchange
from
  azure.network.interfaces
where subscriptionId = '0123456789'
and resourceGroupName = 'vm-rg'
and networkInterfaceName = 'vm1450_z1'

/* Output
[{'afterchange': '
{
  "disableTcpStateTracking": false,
  "dnsSettings": {
    "appliedDnsServers": [],
    "dnsServers": [],
    "internalDomainNameSuffix": "02ylkvekgslelowidrppwubzmb.bx.internal.cloudapp.net"
  },
  "enableAcceleratedNetworking": false,
  "ipConfigurations": [
    {
      "name": "vmss-flex-vnet-nic01-defaultIpConfiguration",
      "properties": {
        "publicIPAddress": {
          "id": "/subscriptions/0123456789/resourceGroups/vm-rg/providers/Microsoft.Network/publicIPAddresses/vm1-ip",
          "properties": {
            "deleteOption": "Delete"
          }
        },
        "subnet": {
          "id": "/subscriptions/0123456789/resourceGroups/vm-rg/providers/Microsoft.Network/virtualNetworks/vm1-vnet/subnets/default"
        }
      }
    }
  ],
  "networkSecurityGroup": {
    "id": "/subscriptions/0123456789/resourceGroups/vm-rg/providers/Microsoft.Network/networkSecurityGroups/vm1-nsg"
  }
}',
'beforechange': '
{
  "allowPort25Out": false,
  "auxiliaryMode": "None",
  "auxiliarySku": "None",
  "disableTcpStateTracking": false,
  "dnsSettings": {
    "appliedDnsServers": [],
    "dnsServers": [],
    "internalDomainNameSuffix": "123ylkvekgslelowidrppwubzmb.bx.internal.cloudapp.net"
  },
  "enableAcceleratedNetworking": false,
  "enableIPForwarding": false,
  "hostedWorkloads": [],
  "ipConfigurations": [
    {
      "etag": "123-bc7e-4609-9328-709fe04c69c3",
      "id": "/subscriptions/0123456789/resourceGroups/vm-rg/providers/Microsoft.Network/networkInterfaces/vm1450_z1/ipConfigurations/ipconfig1",
      "name": "ipconfig1",
      "properties": {
        "primary": true,
        "privateIPAddress": "10.0.0.4",
        "privateIPAddressVersion": "IPv4",
        "privateIPAllocationMethod": "Dynamic",
        "provisioningState": "Succeeded",
        "publicIPAddress": {
          "id": "/subscriptions/0123456789/resourceGroups/vm-rg/providers/Microsoft.Network/publicIPAddresses/vm1-ip",
          "properties": {
            "deleteOption": "Delete"
          }
        },
        "subnet": {
          "id": "/subscriptions/0123456789/resourceGroups/vm-rg/providers/Microsoft.Network/virtualNetworks/vm1-vnet/subnets/default"
        }
      },
      "type": "Microsoft.Network/networkInterfaces/ipConfigurations"
    }
  ],
  "macAddress": "23-0D-3A-1B-FF-91",
  "networkSecurityGroup": {
    "id": "/subscriptions/0123456789/resourceGroups/vm-rg/providers/Microsoft.Network/networkSecurityGroups/vm1-nsg"
  },
  "nicType": "Standard",
  "primary": true,
  "provisioningState": "Succeeded",
  "resourceGuid": "566a1d71-923d-41e0-bbd5-d09bf64cbe7b",
  "tapConfigurations": [],
  "virtualMachine": {
    "id": "/subscriptions/0123456789/resourceGroups/vm-rg/providers/Microsoft.Compute/virtualMachines/vm1"
  },
  "vnetEncryptionSupported": false
}
'}]
*/
```
