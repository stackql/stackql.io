---
title: 適用於雲端基礎架構、SaaS API 與 AI 代理程式的 SQL
sidebar_label: 歡迎使用 StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: StackQL 是開源引擎，讓人員與 AI 代理程式以 SQL 查詢、佈建及維運雲端與 SaaS 服務，無需狀態檔，也無需各雲端專屬的 SDK。
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
  代理式時代的<br/>
  通用介面
  </>
  }
  byline={
    <>
    設定即資料。基礎架構即上下文。
    </>
    }
/>

---

<div className="row margin-top--lg margin-bottom--lg">
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaRobot size={22} className="homeCardIcon" />代理式平台工程</h3>
      </div>
      <div className="card__body">
        讓代理程式與人員以機器速度並肩撰寫變更的通用介面。多個寫入者、即時狀態，以及受政策約束的執行，都是首要考量，這正是自主系統安全運作所需的基礎。
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />代理式時代的 IaC</h3>
      </div>
      <div className="card__body">
        程式碼依然是意圖。現實則是資料。StackQL 為代理程式與維運人員提供單一的宣告式介面，可跨 Provider 查詢即時狀態、以等冪方式確立期望狀態，並透過單一語法遍歷控制平面與資料平面，無需狀態檔，也無需各雲端專屬的 SDK。
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />自主可觀測性</h3>
      </div>
      <div className="card__body">
        將雲端 API 視為您的資料層，而不是部署目標。每次讀取都是即時的，每次變更都是一個事件，漂移則成為代理程式可以推理的訊號，而不是需要人員追查的警報。
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: '查詢', value: 'iql', },
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

使用您的雲端服務供應商部署資源，就像撰寫 [`INSERT`](/language-spec/insert) 陳述式一樣簡單：

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

使用 StackQL，您可以依自己的方式開發：宣告式或程序式。語法容易學習，也沒有需要管理的狀態檔，因此您可以快速上手，並將 StackQL 與其他基礎架構即程式碼工具（無論是否為雲端原生）交替使用。StackQL 為 AI 代理程式提供與雲端基礎架構互動的通用介面。StackQL 的用途包括：

- **AI 代理程式整合**：讓代理程式佈建及查詢雲端資源
- **控制平面通訊**：為代理式工作流程以程式設計方式佈建基礎架構
- **資料平面可觀測性**：為 AI 驅動的作業提供即時監控與分析
- 雲端基礎架構部署（使用 SQL）
- 雲端資產清單與報告（使用 SQL）
- 雲端合規性與控制項證明（使用 SQL）
- 設定漂移偵測（使用 SQL）
- 以及更多用途，只受限於您的想像力！

## StackQL 適合誰使用 {#who-stackql-is-for}

StackQL 適用於：維運雲端資產的平台工程與 SRE 團隊；需要即時資源清單、合規性與成本解答的資安與 FinOps 團隊；希望將雲端與 SaaS API 作為可查詢資料來源的資料工程師；以及建置 AI 代理程式、需要安全查詢及變更雲端與 SaaS 資源的開發人員。StackQL 可在任何工作所在之處執行：作為 macOS、Linux 與 Windows 上的單一二進位檔；在 Docker 中；在搭配 GitHub Actions 的 CI 中；在 AWS、Azure 與 Google Cloud 的 shell 中；內嵌於應用程式中；作為供 BI 工具與筆記本使用的 PostgreSQL 相容伺服器；以及作為供 AI 代理程式使用的 MCP 伺服器。

## 授權與支援 {#license-and-support}

StackQL 以 [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) 開源，可免費用於任何用途。社群支援透過 [GitHub Discussions](https://github.com/orgs/stackql/discussions) 與 [Discord](https://discord.com/invite/xVXZ9d5NxN) 提供；錯誤回報與功能要求請提交至 [GitHub Issues](https://github.com/stackql/stackql/issues)。如有商業支援方面的洽詢，請[聯絡 StackQL Studios](/contact-us)。

## 開始使用 {#get-started}

- [安裝](/installing-stackql)：在 macOS、Linux 或 Windows 上安裝 StackQL 二進位檔，或透過 Docker、套件管理員或雲端 shell 執行
- [AI 代理程式](/ai-agents)：透過 MCP 串接 AI 代理程式、內嵌伺服器，並探索查詢與參考內容
- [開始使用](/getting-started)：資源階層、您的第一個查詢、使用 Provider、輸出模式、變數與範本化
- [可用的 Provider](/providers)：您可以查詢與管理的雲端與 SaaS Provider，每個都有自己的參考網站
- [部落格](/blog)：產品公告、Provider 公告與教學
- [命令列用法](/command-line-usage)：`exec`、`shell`、`srv`、`mcp`、`registry` 與全域旗標
- [MCP 工具](/mcp)：為 AI 代理程式提供相同 SQL 介面的 Model Context Protocol 伺服器與工具
- [語言規格](/language-spec/select)：`SELECT`、`INSERT`、`UPDATE`、`DELETE`、`SHOW`、`DESCRIBE`、函式與資料型別
