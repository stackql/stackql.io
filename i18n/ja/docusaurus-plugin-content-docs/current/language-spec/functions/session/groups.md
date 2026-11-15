---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - アクセス管理
  - groups
  - プリンシパルディレクトリ
description: "StackQL の GROUPS 関数：アクセス管理が有効な場合に、認証済みの呼び出し元のディレクトリグループを JSON 配列として返します。"
image: "/img/stackql-featured-image.png"
---
認証済みの呼び出し元が所属するディレクトリグループを JSON 配列として返します。[アクセス管理](/command-line-usage/access-management)を有効にして開始したセッションが必要です。StackQL `v1.0` 以降で利用できます。

関連項目：  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ アクセス管理 ]](/command-line-usage/access-management)

* * * 

## 構文 {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## 引数 {#arguments}

なし。

## 戻り値 {#return-values}

現在の文に対して取得したプリンシパルディレクトリのスナップショットから、グループ ID の JSON 配列（たとえば `["readers"]`）を返します。メンバーシップは文ごとに更新されるため、メンバーシップの取り消しは次の文で反映されます。`--access.config` を指定せずに開始したセッションでは、この関数は失敗します。

* * *

## 例 {#examples}

### 確立された ID を確認する {#inspect-the-established-identity}

2 つの関数を組み合わせると、呼び出し元のプリンシパル ID と現在のディレクトリグループがわかります。メンバーシップは文ごとに新たに読み取られるため、ディレクトリの変更後にこれを実行すると、その変更がセッションから見えることを確認できます。

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
