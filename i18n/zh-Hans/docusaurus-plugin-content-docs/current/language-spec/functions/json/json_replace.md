---
title: JSON_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 替换 JSON 文档中的现有值。
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

根据路径更新 JSON 对象中的现有值。`JSON_REPLACE` 不会向文档添加新键，只会替换现有键的值。

另请参阅：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 语法 {#syntax}

```sql
SELECT JSON_REPLACE(json_column, path, new_value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*json_column*__  
要替换其中的值的 JSON 列或表达式。

__*path*__  
JSON 路径表达式，指向 JSON 文档中要进行替换的位置。该路径必须指向现有键。

__*new_value*__  
要在 JSON 文档的指定路径处替换成的新值。

## 返回值 {#return-values}
返回替换了指定值的已修改 JSON 文档。如果指定路径不存在，则原样返回原始 JSON 文档。

:::note 键已存在时的行为

| 函数                                                         | 已存在时是否覆盖？           | 不存在时是否创建？        |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | 否                           | 是                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | 是                           | 否                        |
| [`json_set()`](/language-spec/functions/json/json_set)       | 是                           | 是                        |

:::

* * *

## 示例 {#examples}

### 替换 JSON 对象中的单个值 {#replace-a-single-value-in-a-json-object}

```sql
SELECT sku as original,
JSON_REPLACE(sku, '$.name', 'Standard_EU') as updated
FROM azure.container_registry.registries 
WHERE registry_name = 'exampleRegistryProd' 
AND subscription_id = '12345678-abcd-1234-abcd-1234abcd5678' 
AND resource_group_name = 'exampleResourceGroupProd';
/* expected output...
|---------------------------------------|------------------------------------------|
|               original                |                 updated                  |
|---------------------------------------|------------------------------------------|
| {"name":"Standard","tier":"Standard"} | {"name":"Standard_EU","tier":"Standard"} |
|---------------------------------------|------------------------------------------|
*/
```

### 替换 JSON 对象中的多个值 {#replace-multiple-values-in-a-json-object}

<Tabs
  defaultValue="query"
  values={[
    { label: '查询', value: 'query', },
    { label: '输出（JSON）', value: 'output', },
  ]
}>
<TabItem value="query">

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
  azure.network.network_interfaces
where subscription_id = '0123456789'
and resource_group_name = 'vm-rg'
and network_interface_name = 'vm1450_z1'
```

</TabItem><TabItem value="output">

```json
[{"afterchange": 
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
},
"beforechange": 
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
}]
```

</TabItem></Tabs>
