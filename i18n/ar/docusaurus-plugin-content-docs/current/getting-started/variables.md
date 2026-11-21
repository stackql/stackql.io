---
title: استخدام المتغيرات
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "استخدام المتغيرات في StackQL: تقديم القيم من ملفات بيانات Jsonnet أو JSON، والإشارة إليها في الجمل، وتحميل القيم الخارجية، والمعاينة بالتشغيل التجريبي."
image: "/img/stackql-featured-image.png"
---
راجع أيضًا:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

يمكن تقديم المتغيرات إلى برامج StackQL النصية في صورة بيانات تكوين. يمكن أن تكون بيانات تكوين StackQL بتنسيق json أو [jsonnet](https://jsonnet.org/ "Jsonnet").

يقدم Jsonnet ميزات إضافية مثل المنطق الشرطي والاستيراد والوراثة، بالإضافة إلى بنى الحلقات التكرارية.

يمكن تقديم البيانات من خلال ملفات `json` أو `jsonnet` يشار إليها عبر المعلمة `--iqldata` للأمر `stackql exec`، أو يمكن تضمينها في برنامج نصي `iql` باستخدام المزخرفين `<<<` و`>>>` كما هو موضح هنا:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## مثال على تكوين Jsonnet {#jsonnet-configuration-example}
يوضح المثال التالي استخدام Jsonnet لتقديم بيانات التكوين إلى برنامج نصي لـ StackQL. يتم تقديم البيانات باستخدام المعلمة `--iqldata` للأمر `exec` كما هو موضح أدناه:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### بيانات Jsonnet {#jsonnet-data}
باستخدام jsonnet، يمكنك إعادة استخدام المتغيرات واستخدام المصفوفات داخل بنى الحلقات التكرارية في تعليمات StackQL البرمجية الخاصة بك. يمثل jsonnet التالي محتويات الملف `vars.jsonnet` المشار إليه في المعلمة `--iqldata`.

```json
// variables
local project = 'stackql-demo';
local region = 'australia-southeast1';
local name = 'myapp';
local nw_name = name + '-vpc';
local nw_self_link = 'https://compute.googleapis.com/compute/v1/projects/' + project + '/global/networks/' + nw_name + '/';

{
  // global config
  global: {
    project: project,
  },

  // firewall rules
  firewalls: [
    {
      allowed: [{IPProtocol: 'tcp'}, {IPProtocol: 'udp'}, {IPProtocol: 'icmp'}], 
      direction: 'INGRESS', 
      name: name + '-allow-internal-fw', 
      network: nw_self_link,
      sourceRanges: ['10.240.0.0/24', '10.200.0.0/16']
    },
    {
      allowed: [{IPProtocol: 'tcp', ports: ['22']}, {IPProtocol: 'tcp', ports: ['6443']},{IPProtocol: 'icmp'}],
      direction: 'INGRESS', 
      name: name + '-allow-external-fw', 
      network: nw_self_link,
      sourceRanges: ['0.0.0.0/0']
    }
  ],
}
```

### الإشارة إلى بيانات Jsonnet في جملة StackQL {#referencing-jsonnet-data-in-a-stackql-statement}
يوضح مقتطف التعليمات البرمجية التالي كيفية الإشارة إلى قيم jsonnet من أمر StackQL.

```sql
--
-- create firewall rules
--
{{range .firewalls}}
INSERT /*+ AWAIT  */ INTO google.compute.firewalls
(
 project,
 data__name,
 data__network,
 data__direction,
 data__sourceRanges,
 data__allowed
) 
SELECT
'{{ $.global.project }}',
'{{.name}}',
'{{.network}}',
'{{.direction}}',
'{{.sourceRanges}}',
'{{.allowed}}';
{{end}}
```

### تحميل المتغيرات الخارجية باستخدام Jsonnet {#sourcing-external-variables-using-jsonnet}
توضح مقتطفات التعليمات البرمجية التالية كيفية تحميل متغيرات خارجية في ملف تكوين Jsonnet يستخدم لإنشاء استعلام StackQL من القالب.

#### استعلام StackQL (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

```sql
INSERT /*+ AWAIT  */ INTO google.compute.networks
(
 project,
 data__name,
 data__autoCreateSubnetworks,
 data__routingConfig
) 
SELECT
'{{ .global.project }}',
'{{ .network.name }}',
{{ .network.autoCreateSubnetworks }},
'{{ .network.routingConfig }}';
```

#### ملف بيانات Jsonnet (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

```json
// variables
local name = 'env-var-input-demo';
local project = std.extVar("project");

{
  // global config
  global: {
    project: project,
  },
  // network
  network: {
    autoCreateSubnetworks: false,
    name: name + '-vpc',
    routingConfig: {routingMode: 'REGIONAL'}
  },
}
```

#### التنفيذ من سطر الأوامر {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## مثال على تكوين Json {#json-configuration-example}
يمكن أيضًا استخدام json بسيط لتقديم بيانات التكوين إلى برنامج نصي لـ StackQL كما هو موضح أدناه:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### بيانات Json {#json-data}

تمثل بيانات json التالية محتويات الملف `vars.json` المشار إليه في المعلمة `--iqldata`.

```json
{
  "project": "stackql-demo",
  "firewalls": [
    {
      "allowed": [{"IPProtocol": "tcp"}, {"IPProtocol": "udp"}, {"IPProtocol": "icmp"}], 
      "direction": "INGRESS", 
      "name": "myapp-allow-internal-fw", 
      "network": "https://compute.googleapis.com/compute/v1/projects/stackql-demo/global/networks/myapp-vpc/",
      "sourceRanges": ["10.240.0.0/24", "10.200.0.0/16"]
    },
    {
      "allowed": [{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}],
      "direction": "INGRESS", 
      "name": "myapp-allow-external-fw", 
      "network": "https://compute.googleapis.com/compute/v1/projects/stackql-demo/global/networks/myapp-vpc/",
      "sourceRanges": ["0.0.0.0/0"]
    }
  ]
}
```

### الإشارة إلى بيانات Json في جملة StackQL {#referencing-json-data-in-a-stackql-statement}
يشار إلى البيانات الموضحة أعلاه في جملة `INSERT` في StackQL كما هو موضح هنا:

```sql
INSERT /*+ AWAIT  */ INTO google.compute.firewalls
(
 project,
 data__name,
 data__network,
 data__direction,
 data__sourceRanges,
 data__allowed
) 
SELECT
'{{ .project }}',
'{{ (index .firewalls 0).name }}',
'{{ (index .firewalls 0).network }}',
'{{ (index .firewalls 0).direction }}',
'{{ (index .firewalls 0).sourceRanges }}',
'{{ (index .firewalls 0).allowed }}';

INSERT /*+ AWAIT  */ INTO google.compute.firewalls
(
 project,
 data__name,
 data__network,
 data__direction,
 data__sourceRanges,
 data__allowed
) 
SELECT
'{{ .project }}',
'{{ (index .firewalls 1).name }}',
'{{ (index .firewalls 1).network }}',
'{{ (index .firewalls 1).direction }}',
'{{ (index .firewalls 1).sourceRanges }}',
'{{ (index .firewalls 1).allowed }}';
```

## خيار التشغيل التجريبي لمعاينة الأمر {#dryrun-option-to-preview-command}
يمكنك استخدام الخيار `--dryrun` لإخراج برنامج نصي لـ StackQL بعد معالجته مسبقًا باستخدام البرنامج النصي للإدخال وملف بيانات `json` أو `jsonnet` كما هو موضح أدناه:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
