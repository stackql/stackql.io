---
title: AWS CloudShell 中的 StackQL
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
  - cspm
  - aws
  - amazon web services
description: "以登入的使用者身分在 AWS CloudShell 中執行 StackQL：下載 Linux 套件、使用 stackql shell、擔任 IAM 角色，並以 exec 執行批次查詢。"
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# 在 AWS CloudShell 中使用 StackQL

本指南將示範如何在 AWS CloudShell 中使用 StackQL，並以登入 AWS Console 的工作階段使用者內容執行，或擔任另一個 IAM 角色。StackQL 是一款功能強大的開發工具，能以 SQL 語法查詢及部署雲端基礎架構與資源。

已測試於 <span class="tutorial_tested_on">內嵌式 SQL 後端</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>什麼是 <b>AWS CloudShell</b>？</summary>

> AWS CloudShell 是由 Amazon Web Services（AWS）提供、以瀏覽器為基礎且已預先驗證的 shell 服務。它可讓開發人員與 IT 專業人員直接從網頁瀏覽器管理其雲端資源。使用者透過 AWS CloudShell，無須在本機電腦上安裝或設定 AWS CLI（Command Line Interface），即可快速執行與 AWS 服務相關的命令。此服務提供立即可用的環境，並預先安裝了常見的開發與管理工具，例如 AWS CLI、Python、Node.js 等，讓與 AWS 服務互動及自動化工作變得更容易。AWS CloudShell 直接整合於 AWS Management Console，為執行管理工作、探索 AWS 服務及管理基礎架構提供順暢的體驗。由於 AWS CloudShell 會透過使用者的 AWS Console 登入自動驗證使用者，因此簡化了在其 AWS 帳戶內容中執行命令與指令碼的流程，提升雲端維運的生產力與效率。

</details>

## 運作方式 {#how-it-works}

StackQL 的 Linux 套件包含可在主要雲端服務供應商內建的雲端 shell 環境中直接使用 `stackql` 的指令碼。AWS 的指令碼為 **`stackql-aws-cloud-shell.sh`**。**`stackql-aws-cloud-shell.sh`** 指令碼的用法如下：

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

`stackql-aws-cloud-shell.sh` 指令碼會自動下載 StackQL 最新的 AWS Provider。隨附的雲端 shell 指令碼原始碼位於 [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts)。

## 從 AWS CloudShell 下載 `stackql` Linux 套件 {#download-the-stackql-linux-package-from-aws-cloud-shell}

首先，請在 AWS CloudShell 中使用下列命令下載 StackQL 最新的 Linux 二進位檔：

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

這會將 `stackql` 二進位檔連同 `stackql-aws-cloud-shell.sh` 指令碼下載並解壓縮到您 AWS CloudShell 環境的主目錄。現在您已準備就緒！

## 從 AWS CloudShell 使用 `stackql shell` {#using-the-stackql-shell-from-aws-cloud-shell}

在 AWS CloudShell 中啟動已驗證的 StackQL 命令 shell 非常簡單，只需執行：

```shell
sh stackql-aws-cloud-shell.sh
```

現在您可以開始使用 StackQL Provider，以互動方式執行以 SQL 為基礎的 AWS 查詢，如下所示：

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  您的瀏覽器不支援 video 標籤。
</video>

## 使用 StackQL 擔任 IAM 角色 {#assuming-an-iam-role-using-stackql}

在 AWS 中擔任 IAM 角色，可讓您執行預設使用者權限可能無法執行的動作，並存取預設使用者權限可能無法存取的資源。當您需要暫時存取資源，或需要以不同於主要帳戶的權限執行動作時，這特別有用。透過 StackQL，您可以順暢地擔任 IAM 角色，以該角色所授予的權限查詢及管理 AWS 資源。

<details>
<summary>了解 IAM 角色的擔任</summary>

> 擔任 IAM 角色是授予 AWS 資源暫時存取權的安全方式。當您擔任角色時，AWS STS 會為您提供暫時性安全憑證，讓您能夠存取該角色有權使用的 AWS 服務與資源。此機制非常適合跨帳戶存取、使用者的暫時存取，或遵循最低權限原則。

> 角色可以透過 AWS Management Console、AWS CLI 或 AWS SDK 擔任，也可以直接在 StackQL 中擔任。這可確保使用者與應用程式只在所需期間內擁有必要的權限，進而強化您 AWS 環境的安全性狀態。

</details>

若要在 AWS CloudShell 中使用 StackQL 擔任 IAM 角色，您可以搭配 `stackql-aws-cloud-shell.sh` 指令碼使用 `--role-arn` 選項。此選項需要您要擔任之角色的 Amazon Resource Name（ARN）。提供此選項時，StackQL 會在執行任何查詢或命令之前，使用 AWS Security Token Service（STS）擔任指定的角色。

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

透過擔任角色，您可以運用 StackQL 與預設使用者可能無權存取的 AWS 資源互動，以更靈活且安全的方式管理您的雲端環境。

使用所擔任的 IAM 角色執行 StackQL 命令或指令碼時，請確保該角色具備執行所需動作及存取指定資源的必要權限。此方法有助於實現精細的存取控制，並符合 AWS 在安全性與治理方面的最佳做法。

## 從 AWS CloudShell 執行 `stackql exec`（批次作業） {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

StackQL 查詢也可以使用 `exec` 命令執行；此選項在下列情況特別有用：

- 指定替代輸出格式，例如 `json` 或 `csv`
- 為 StackQL 查詢的結果產生輸出檔案
- 使用透過輸入檔案提供的 StackQL 查詢（依慣例為 `.iql` 檔案）
- 執行複雜的作業，包括載入變數或前置處理查詢（使用 `json` 或 `jsonnet`）

在 AWS CloudShell 中使用 `exec` 的範例如下：

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instance_type FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

上述範例會執行指定的 StackQL 查詢並產生 `CSV` 輸出檔案，該檔案可從 AWS CloudShell 下載。此外，您可以使用 `--role-arn` 引數提供 IAM 角色，以便在查詢或變更作業中擔任另一個角色；範例如下（在此範例中，查詢來自輸入檔案）：

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::123456789012:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

如需 `exec` 命令的詳細資訊，請參閱[**這裡**](/command-line-usage/exec)。
