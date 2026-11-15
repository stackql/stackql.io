---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - アクセス管理
  - whoami
  - プリンシパル
description: "StackQL の WHOAMI 関数：アクセス管理が有効な場合に、認証済みの呼び出し元のプリンシパル ID を返します。"
image: "/img/stackql-featured-image.png"
---
認証済みの呼び出し元のプリンシパル ID を返します。[アクセス管理](/command-line-usage/access-management)を有効にして開始したセッションが必要です。StackQL `v1.0` 以降で利用できます。

関連項目：  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ アクセス管理 ]](/command-line-usage/access-management)

* * * 

## 構文 {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## 引数 {#arguments}

なし。

## 戻り値 {#return-values}

現在の文に対して確立されたプリンシパル ID の文字列を返します。`--access.config` を指定せずに開始したセッションでは、この関数は失敗します。

* * *

## 例 {#examples}

### 確立された ID を確認する {#inspect-the-established-identity}

どちらの関数も引数を取らず、この文に対してサーバーが認証した呼び出し元を表します。`whoami()` はプリンシパル ID を、`groups()` は呼び出し元のディレクトリグループを返します。そのため、両方を一緒に実行すると、ほかの処理を実行する前に、トークンまたは証明書が想定どおりのプリンシパルに対応付けられていることをすばやく確認できます。

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
