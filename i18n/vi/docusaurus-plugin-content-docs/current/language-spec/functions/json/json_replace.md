---
title: JSON_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Dùng SQL trong StackQL để thay thế các giá trị hiện có trong tài liệu JSON.
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Cập nhật các giá trị hiện có trong đối tượng JSON dựa trên đường dẫn của chúng. Hàm `JSON_REPLACE` không thêm khóa mới vào tài liệu; hàm chỉ thay thế giá trị của các khóa hiện có.

Xem thêm:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert)

* * * 

## Cú pháp {#syntax}

```sql
SELECT JSON_REPLACE(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*json_column*__  
Cột hoặc biểu thức JSON có giá trị cần thay thế.

__*path*__  
Biểu thức đường dẫn JSON trỏ đến vị trí trong tài liệu JSON nơi cần thực hiện thay thế. Đường dẫn phải trỏ đến một khóa hiện có.

__*new_value*__  
Giá trị mới dùng để thay thế tại đường dẫn được chỉ định trong tài liệu JSON.

## Giá trị trả về {#return-values}
Trả về tài liệu JSON đã sửa đổi, trong đó giá trị được chỉ định đã được thay thế. Nếu đường dẫn được chỉ định không tồn tại, hàm trả về tài liệu JSON ban đầu không thay đổi.

:::note Hành vi khi khóa đã tồn tại

| Hàm                                                          | Ghi đè nếu đã tồn tại?       | Tạo nếu chưa tồn tại?     |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Không                        | Có                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Có                           | Không                     |
| [`json_set()`](/language-spec/functions/json/json_set)       | Có                           | Có                        |

:::

* * *

## Ví dụ {#examples}

### Thay thế một giá trị trong đối tượng JSON {#replace-a-single-value-in-a-json-object}

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

### Thay thế nhiều giá trị trong đối tượng JSON {#replace-multiple-values-in-a-json-object}

<Tabs
  defaultValue="query"
  values={[
    { label: 'Truy vấn', value: 'query', },
    { label: 'Đầu ra (JSON)', value: 'output', },
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
