---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の SELECT：API パラメーターに対応する WHERE フィルター、結合、グループ化、並べ替え、JSON フィールドアクセスを使用して、SQL でクラウドと SaaS のリソースをクエリします。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

リソースの 1 つ以上のインスタンスを返します。  

関連項目：
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy) [[` WITH (CTEs) `]](/language-spec/with) [[` ウィンドウ関数 `]](/language-spec/windowing_functions)

* * * 

## 構文 {#syntax}

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
[ WITH [ RECURSIVE ] <cteName> [ ( <columnList> ) ] AS ( <selectStatement> ) [, ...] ]
SELECT { * | <fieldList> | <windowFunctionCall> [ AS <alias> ] }
FROM { <multipartIdentifier> | <joinStatement(s)> }
[ WHERE <expression> ]
[ GROUP BY <fieldList> ]
[ HAVING <expression> ]
[ WINDOW <windowName> AS ( <windowSpec> ) [, ...] ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ LIMIT <integer> ]
[ UNION <selectStatement> ];
```

`ON` 句のない `JOIN` は `CROSS JOIN` として扱われるため、代わりに `WHERE` 句で結合条件を指定できます。`v0.12.732` より前の StackQL リリースでは、条件のない `JOIN` の計画の作成に失敗します。

* * *

## 例 {#examples}

### 基本的な `SELECT` 文 {#basic-select-statement}
認証済みのセッションで基本的な `SELECT` 文を実行します。

```sql
-- Selecting all instances in a zone using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### `SELECT` 文で指定したフィールドを返す {#returning-specified-fields-using-a-select-statement}
列リストを指定した `SELECT` 文を実行して、Compute Engine インスタンスから指定したフィールドを返します。

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### `ORDER BY` 句を使用して列の昇順に並べた結果セットを返す {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Cloud Storage のバケットリソースからフィールドを返す `SELECT` 文を実行し、結果を昇順（古い順）に並べます。

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### `ORDER BY` 句を使用して列の降順に並べた結果セットを返す {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Cloud Storage のバケットリソースからフィールドを返す `SELECT` 文を実行し、結果を降順（新しい順）に並べます。

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### `ORDER BY` 句と `LIMIT` 句を使用して列の先頭の結果を取得する {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
最も新しく作成された Cloud Storage バケットを返す `SELECT` 文を実行します。

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### `COUNT` 関数を `GROUP BY` 句および `HAVING` 句とともに使用して、グループ化とフィルターに基づいてリソースの数を数える {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
`COUNT` 関数を含む `SELECT` 文を `GROUP BY` 句と `HAVING` 句とともに実行して、アジア以外の各ロケーションにあるバケットの数を返します。

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

`COUNT` 関数および StackQL がサポートするその他の集約関数の詳細については、[集約関数](/language-spec/functions/aggregate/count)を参照してください。

### Azure VM Scale Set からオンデマンド VM と対応するネットワークインターフェイスの一覧を取得する {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
[`SPLIT_PART`](/language-spec/functions/string/split_part) 関数と [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) 関数を使用する `SELECT` 文を実行して、オンデマンド（OD）VM とその NIC の一覧を取得します。なお、修飾子がない場合、`subscription_id` と `resource_group_name` は 3 つのリソースすべてに適用されます。

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

### `WITH` で共通テーブル式（CTE）を使用する {#using-common-table-expressions-ctes-with-with}

CTE を使用すると、メインクエリで参照できる一時的な名前付き結果セットを定義できます。

```sql
-- Combine and rank contributors across multiple repositories
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

CTE の詳細については、[WITH (CTEs)](/language-spec/with) を参照してください。

ウィンドウ関数の詳細については、[ウィンドウ関数](/language-spec/windowing_functions)を参照してください。
