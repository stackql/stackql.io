---
title: クラウドインフラストラクチャ、SaaS API、AI エージェントのための SQL
sidebar_label: StackQL へようこそ
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL は、人間と AI エージェントが SQL でクラウドと SaaS のサービスをクエリ、プロビジョニング、運用できるオープンソースのエンジンで、ステートファイルもクラウドごとの SDK も不要です。
image: "/img/stackql-featured-image.png"
slug: /
# SoftwareApplication node on the homepage, from the site-wide defaults in
# themeConfig.structuredData.softwareApplication (name, category, OS,
# license, download URL, free offer)
softwareApplication: true
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import DocsHero from '@site/src/components/DocsHero/DocsHero';
import { FaRobot, FaCode, FaSatelliteDish } from 'react-icons/fa';

<DocsHero
  lightSrc="/img/stackql-logo-bold-light-mode.png"
  darkSrc="/img/stackql-logo-bold-dark-mode.png"
  alt="StackQL"
  title={
  <>
  エージェント時代の<br/>
  ユニバーサルインターフェイス
  </>
  }
  byline={
    <>
    Configuration-as-Data. Infrastructure-as-Context.
    </>
    }
/>

---

<div className="row margin-top--lg margin-bottom--lg">
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaRobot size={22} className="homeCardIcon" />エージェント型プラットフォームエンジニアリング</h3>
      </div>
      <div className="card__body">
        エージェントと人間が、マシンの速度で並行して変更を作成するためのユニバーサルなインターフェイスです。複数の書き込み主体、リアルタイムの状態、ポリシーの範囲内での実行を、第一級の関心事として扱います。これは、自律システムが安全に動作するために必要な基盤です。
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />エージェント時代の IaC</h3>
      </div>
      <div className="card__body">
        コードは今も意図を表します。現実はデータです。StackQL は、エージェントと運用者に 1 つの宣言型インターフェイスを提供します。これにより、プロバイダーをまたいでリアルタイムの状態をクエリし、望ましい状態を冪等に適用し、単一の文法でコントロールプレーンとデータプレーンを横断できます。ステートファイルもクラウドごとの SDK も不要です。
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />自律型オブザーバビリティ</h3>
      </div>
      <div className="card__body">
        クラウド API を、デプロイ先ではなくデータレイヤーとして扱います。すべての読み取りはリアルタイムで、すべての変更はイベントです。ドリフトは、人間が追いかけなければならないアラームではなく、エージェントが推論できるシグナルになります。
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'クエリ', value: 'iql', },
    { label: '結果', value: 'data', },
  ]
}>
<TabItem value="iql">

```sql
SELECT state, COUNT(*) as num_instances 
FROM aws.ec2.instances 
WHERE region = 'us-west-1' 
GROUP BY state;
```

</TabItem>
<TabItem value="data">

```
|---------------------------------|
|      state      | num_instances |
|---------------------------------|
|     running     |     342       |
|---------------------------------|
|     pending     |       4       |
|---------------------------------|
|     stopped     |      38       |
|---------------------------------|
```

</TabItem>
</Tabs>

クラウドベンダーへのリソースのデプロイは、[`INSERT`](/language-spec/insert) 文を書くのと同じくらい簡単です。

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

StackQL を使用すると、宣言型でも手続き型でも、自分のスタイルで開発できます。文法は習得しやすく、管理すべきステートファイルもありません。そのため、すぐに使い始めることができ、クラウドネイティブかどうかを問わず、ほかの Infrastructure as Code ツールと StackQL を相互に置き換えて使用できます。StackQL は、AI エージェントがクラウドインフラストラクチャと対話するためのユニバーサルなインターフェイスを提供します。StackQL の用途には、次のようなものがあります。

- **AI エージェントとの統合**：エージェントによるクラウドリソースのプロビジョニングとクエリを可能にする
- **コントロールプレーンとの通信**：エージェント型ワークフローのための、プログラムによるインフラストラクチャのプロビジョニング
- **データプレーンのオブザーバビリティ**：AI 主導の運用のためのリアルタイムの監視と分析
- クラウドインフラストラクチャのデプロイ（SQL を使用）
- クラウド資産のインベントリとレポート作成（SQL を使用）
- クラウドのコンプライアンスと統制の証明（SQL を使用）
- 構成ドリフトの検出（SQL を使用）
- ほかにも、想像力次第でさまざまな用途があります！

## StackQL の対象ユーザー {#who-stackql-is-for}

StackQL の対象は、クラウド環境を運用するプラットフォームエンジニアリングチームと SRE チーム、リアルタイムのインベントリ、コンプライアンス、コストに関する答えを必要とするセキュリティチームと FinOps チーム、クラウドと SaaS の API をクエリ可能なデータソースとして使いたいデータエンジニア、そしてクラウドと SaaS のリソースを安全にクエリおよび変更する必要がある AI エージェントを構築する開発者です。StackQL は、作業が行われる場所であればどこでも動作します。macOS、Linux、Windows 上の単一のバイナリとして、Docker 内で、GitHub Actions を使用した CI 内で、AWS、Azure、Google Cloud のシェル内で、アプリケーションに組み込んで、BI ツールやノートブック向けの PostgreSQL 互換サーバーとして、そして AI エージェント向けの MCP サーバーとして実行できます。

## ライセンスとサポート {#license-and-support}

StackQL は [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) のもとで公開されているオープンソースで、目的を問わず無料で使用できます。コミュニティサポートは [GitHub Discussions](https://github.com/orgs/stackql/discussions) と [Discord](https://discord.com/invite/xVXZ9d5NxN) で提供しています。バグ報告と機能リクエストは [GitHub Issues](https://github.com/stackql/stackql/issues) で受け付けています。商用サポートについては、[StackQL Studios に問い合わせてください](/contact-us)。

## 利用の開始 {#get-started}

- [インストール](/installing-stackql)：macOS、Linux、Windows への StackQL バイナリのインストール、または Docker、パッケージマネージャー、クラウドシェルからの実行
- [AI エージェント](/ai-agents)：MCP を介した AI エージェントの接続、サーバーの組み込み、クエリとリファレンスコンテンツの参照
- [はじめに](/getting-started)：リソース階層、最初のクエリ、プロバイダーの使用、出力モード、変数、テンプレート機能
- [利用可能なプロバイダー](/providers)：クエリおよび管理できるクラウドと SaaS のプロバイダーと、それぞれのリファレンスサイト
- [ブログ](/blog)：製品のお知らせ、プロバイダーのお知らせ、チュートリアル
- [コマンドラインの使用方法](/command-line-usage)：`exec`、`shell`、`srv`、`mcp`、`registry`、グローバルフラグ
- [MCP ツール](/mcp)：AI エージェントに同じ SQL インターフェイスを提供する Model Context Protocol サーバーとツール
- [言語仕様](/language-spec/select)：`SELECT`、`INSERT`、`UPDATE`、`DELETE`、`SHOW`、`DESCRIBE`、関数、データ型
