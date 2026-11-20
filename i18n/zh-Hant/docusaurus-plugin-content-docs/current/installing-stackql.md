---
title: 安裝
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "在 macOS、Linux、Windows、Docker 或雲端 shell 上安裝 StackQL，並從 Claude Desktop、ChatGPT、Goose 等 MCP 用戶端，或透過 npx、uvx、pip、Docker 和 GitHub Actions 使用 StackQL MCP 伺服器。"
image: "/img/stackql-featured-image.png"
slug: /installing-stackql
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import Link from '@docusaurus/Link';
import { Box, Typography, Button } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';
import ArticleIcon from '@mui/icons-material/Article';
import { FaApple, FaWindows, FaDocker } from 'react-icons/fa';
import { FcLinux } from 'react-icons/fc';
import { SiOpenai } from 'react-icons/si';
import {
  BinaryDownloadLink,
  DropDownBinaryDownloadLink,
} from '@site/src/components';
import buttonStyles from '@site/src/components/BinaryDownloadLink/binarydownloadlink.module.css';
import clsx from 'clsx';

export const ClaudeIcon = ({ size = '1em' }) => (
  <svg xmlns="http://www.w3.org/2000/svg" width={size} height={size} viewBox="0 0 24 24" role="img" aria-hidden="true" style={{ flex: 'none' }}>
    <path fill="#D97757" d="m4.7144 15.9555 4.7174-2.6471.079-.2307-.079-.1275h-.2307l-.7893-.0486-2.6956-.0729-2.3375-.0971-2.2646-.1214-.5707-.1215-.5343-.7042.0546-.3522.4797-.3218.686.0608 1.5179.1032 2.2767.1578 1.6514.0972 2.4468.255h.3886l.0546-.1579-.1336-.0971-.1032-.0972L6.973 9.8356l-2.55-1.6879-1.3356-.9714-.7225-.4918-.3643-.4614-.1578-1.0078.6557-.7225.8803.0607.2246.0607.8925.686 1.9064 1.4754 2.4893 1.8336.3643.3035.1457-.1032.0182-.0728-.164-.2733-1.3539-2.4467-1.445-2.4893-.6435-1.032-.17-.6194c-.0607-.255-.1032-.4674-.1032-.7285L6.287.1335 6.6997 0l.9957.1336.419.3642.6192 1.4147 1.0018 2.2282 1.5543 3.0296.4553.8985.2429.8318.091.255h.1579v-.1457l.1275-1.706.2368-2.0947.2307-2.6957.0789-.7589.3764-.9107.7468-.4918.5828.2793.4797.686-.0668.4433-.2853 1.8517-.5586 2.9021-.3643 1.9429h.2125l.2429-.2429.9835-1.3053 1.6514-2.0643.7286-.8196.85-.9046.5464-.4311h1.0321l.759 1.1293-.34 1.1657-1.0625 1.3478-.8804 1.1414-1.2628 1.7-.7893 1.36.0729.1093.1882-.0183 2.8535-.607 1.5421-.2794 1.8396-.3157.8318.3886.091.3946-.3278.8075-1.967.4857-2.3072.4614-3.4364.8136-.0425.0304.0486.0607 1.5482.1457.6618.0364h1.621l3.0175.2247.7892.522.4736.6376-.079.4857-1.2142.6193-1.6393-.3886-3.825-.9107-1.3113-.3279h-.1822v.1093l1.0929 1.0686 2.0035 1.8092 2.5075 2.3314.1275.5768-.3218.4554-.34-.0486-2.2039-1.6575-.85-.7468-1.9246-1.621h-.1275v.17l.4432.6496 2.3436 3.5214.1214 1.0807-.17.3521-.6071.2125-.6679-.1214-1.3721-1.9246L14.38 17.959l-1.1414-1.9428-.1397.079-.674 7.2552-.3156.3703-.7286.2793-.6071-.4614-.3218-.7468.3218-1.4753.3886-1.9246.3157-1.53.2853-1.9004.17-.6314-.0121-.0425-.1397.0182-1.4328 1.9672-2.1796 2.9446-1.7243 1.8456-.4128.164-.7164-.3704.0667-.6618.4008-.5889 2.386-3.0357 1.4389-1.882.929-1.0868-.0062-.1579h-.0546l-6.3385 4.1164-1.1293.1457-.4857-.4554.0608-.7467.2307-.2429 1.9064-1.3114Z" />
  </svg>
);

export const GooseIcon = ({ size = '1em' }) => (
  <svg xmlns="http://www.w3.org/2000/svg" width={size} height={size} viewBox="0 0 45 45" role="img" aria-hidden="true" style={{ flex: 'none' }}>
    <path fill="currentColor" d="M43.2098 39.2492L39.8118 36.4522C37.9646 34.9318 36.3923 33.1051 35.1631 31.0529C33.4647 28.2172 31.1178 25.8243 28.3164 24.071L26.9482 23.2744C26.4794 22.9485 26.1525 22.4405 26.106 21.8675C26.0762 21.4981 26.1647 21.1678 26.3712 20.8771C27.0837 19.873 30.9617 15.6995 31.6365 15.1411C32.5056 14.4228 33.4739 13.8253 34.3724 13.1412C34.5001 13.0438 34.6283 12.9469 34.7545 12.8481C34.7589 12.8437 34.7653 12.8403 34.7692 12.8363C35.0574 12.6088 35.3295 12.3671 35.5458 12.0803C36.5993 10.8604 36.5783 9.82938 36.5783 9.82938C36.5783 9.82938 36.5793 9.8284 36.5798 9.82791C36.4677 9.5534 36.0645 8.69902 35.2449 8.29825C35.9696 8.28406 36.7872 8.55564 37.1582 8.91873C37.6039 8.21947 37.8922 7.76585 38.3404 7.00347C38.4471 6.82241 38.5038 6.48771 38.2998 6.30029C38.2998 6.30029 38.2988 6.30029 38.2978 6.30029C38.2978 6.30029 38.2978 6.29931 38.2978 6.29833C38.2939 6.29442 38.289 6.29148 38.2851 6.28757C38.2812 6.28365 38.2787 6.27925 38.2743 6.27485C38.2743 6.27485 38.2734 6.27485 38.2724 6.27485C38.2724 6.27485 38.2724 6.27387 38.2724 6.27289C38.2641 6.26457 38.2548 6.25821 38.246 6.25038C38.2406 6.245 38.2362 6.23864 38.2313 6.23276C38.2254 6.22787 38.219 6.22347 38.2137 6.21808C38.2053 6.20977 38.2 6.19949 38.1911 6.19166C38.1911 6.19166 38.1897 6.19117 38.1892 6.19166C38.1892 6.19166 38.1892 6.19068 38.1892 6.1897C38.1853 6.18579 38.1804 6.18285 38.1765 6.17894C38.1726 6.17502 38.1696 6.17013 38.1657 6.16621C38.1657 6.16621 38.1642 6.16572 38.1637 6.16621C38.1637 6.16621 38.1637 6.16524 38.1637 6.16426C37.9763 5.9602 37.6416 6.01697 37.4606 6.12364C36.4692 6.70693 35.5047 7.33475 34.6351 7.88525C34.6351 7.88525 33.6046 7.86372 32.3842 8.91775C32.097 9.13452 31.8552 9.4066 31.6282 9.69433C31.6238 9.69873 31.6199 9.7046 31.6164 9.70901C31.5171 9.83476 31.4202 9.96297 31.3233 10.0912C30.6387 10.9901 30.0417 11.958 29.3234 12.8271C28.7656 13.5023 24.5915 17.3798 23.5874 18.0923C23.2967 18.2988 22.9669 18.3879 22.597 18.3575C22.0245 18.3115 21.5161 17.9842 21.1902 17.5154L20.3935 16.1472C18.6402 13.3448 16.2474 10.9989 13.4117 9.30041C11.3594 8.0712 9.5332 6.49847 8.01234 4.65172L5.2153 1.25377C5.0773 1.08642 4.81306 1.11382 4.71422 1.30662C4.39615 1.92612 3.78888 3.23411 3.32353 4.99327C3.31227 5.03487 3.32206 5.07891 3.34946 5.11218C3.92786 5.81095 5.26717 7.35089 6.8624 8.65351C6.97348 8.74403 6.88246 8.92313 6.74447 8.88496C5.36846 8.51013 4.03062 7.90825 3.02944 7.39542C2.94772 7.35383 2.84887 7.4057 2.83713 7.49672C2.68152 8.74354 2.64139 10.1127 2.80581 11.5562C2.8107 11.6032 2.84055 11.6438 2.8841 11.6629C4.02572 12.1596 5.85143 12.8804 7.76816 13.3389C7.90713 13.3722 7.90273 13.5723 7.76229 13.5992C6.26982 13.8801 4.70688 13.9427 3.43656 13.9261C3.3475 13.9251 3.28193 14.0093 3.30591 14.0949C3.56085 14.9958 3.90632 15.9118 4.3619 16.8342C4.55029 17.2487 4.75532 17.6553 4.97504 18.0532C5.01271 18.1212 5.08562 18.1618 5.16343 18.1584C6.21207 18.107 7.47946 17.9964 8.75369 17.7713C8.97095 17.7331 9.08741 18.0189 8.90391 18.1417C8.03877 18.7201 7.11588 19.2183 6.25758 19.6283C6.14259 19.6836 6.1054 19.8304 6.18125 19.9327C6.69114 20.6236 7.25191 21.2774 7.86114 21.8866C7.86114 21.8866 10.8608 24.9763 10.9425 25.2185C12.6928 23.4261 15.5701 21.4394 18.7474 19.7047C14.4921 23.1672 12.1198 25.7235 10.9258 27.1758L10.0935 28.3439C9.66091 28.9507 9.2856 29.5951 8.97193 30.2709C7.92231 32.5292 6.19201 37.0966 6.19201 37.0966C6.0594 37.4543 6.16755 37.8072 6.39753 38.0371C6.40292 38.0425 6.4083 38.0479 6.41417 38.0528C6.41955 38.0582 6.42445 38.0641 6.42983 38.0694C6.66031 38.2999 7.01263 38.4076 7.37033 38.275C7.37033 38.275 11.9368 36.5447 14.1961 35.495C14.8719 35.1814 15.5168 34.8056 16.1231 34.3735L17.4095 33.457C18.0907 32.9715 19.0234 33.0489 19.615 33.6405L22.5804 36.6058C23.1896 37.2151 23.8433 37.7758 24.5343 38.2857C24.637 38.3611 24.7833 38.3244 24.8386 38.2094C25.2492 37.3516 25.7473 36.4277 26.3252 35.5631C26.4481 35.3796 26.7343 35.4965 26.6957 35.7133C26.4701 36.988 26.3605 38.2554 26.3086 39.3035C26.3047 39.3809 26.3458 39.4543 26.4138 39.4919C26.8112 39.7121 27.2178 39.9172 27.6327 40.1051C28.5551 40.5606 29.4717 40.9066 30.3721 41.1611C30.4577 41.185 30.5419 41.1195 30.5409 41.0304C30.5237 39.7596 30.5864 38.1967 30.8677 36.7047C30.8942 36.5638 31.0943 36.5593 31.1281 36.6988C31.5861 38.616 32.3069 40.4417 32.8041 41.5829C32.8231 41.6264 32.8633 41.6558 32.9107 41.6612C34.3543 41.8256 35.7234 41.7855 36.9703 41.6298C37.0618 41.6186 37.1136 41.5197 37.0715 41.4375C36.5587 40.4364 35.9568 39.0975 35.582 37.7225C35.5443 37.584 35.7229 37.4935 35.8135 37.6046C37.1161 39.1998 38.656 40.5391 39.3548 41.1175C39.3881 41.1449 39.4326 41.1542 39.4737 41.1434C41.2333 40.6786 42.5413 40.0713 43.1604 39.7528C43.3532 39.6539 43.3806 39.3897 43.2132 39.2517L43.2098 39.2492Z" />
  </svg>
);

export const gooseDeepLink = 'goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL';

export const tabLabel = (icon, text) => (
  <span style={{ display: 'inline-flex', alignItems: 'center', gap: '0.4em' }}>{icon}{text}</span>
);

這裡提供在各種不同平台上安裝 StackQL 的說明。

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: '雲端 shell', value: 'shells', },
  ]}
>
<TabItem value="macos">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>
  
  ## macOS
  
  在 macOS 上可以透過 Homebrew 和 `pkg` 安裝程式取得 StackQL，單一多架構安裝程式同時支援 ARM（M1/Apple Silicon）和 AMD 架構。

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl（已簽署且經過公證的二進位檔）', value: 'curl', },
      { label: '套件下載', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  若要透過 Homebrew 安裝，請在終端機中執行下列命令：

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  若要在目前目錄中安裝已簽署且經過公證的 `stackql` 可執行二進位檔，請在終端機中執行下列命令：

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL 提供適用於 macOS、已簽署且經過公證的互動式 `pkg` 安裝程式。

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="下載 macOS PKG"
          to="https://releases.stackql.io/stackql/latest/stackql_darwin_multiarch.pkg"
          />
      </div>
  </Box>

  </TabItem>

  </Tabs>    

</Box>

</TabItem>
<TabItem value="linux">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>

  ## Linux

  StackQL 適用於所有 Linux 架構。

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: '套件下載', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  若要在目前目錄中安裝適用於您的 Linux 架構（amd64 或 arm64）的 `stackql` 可執行二進位檔，請在終端機中執行下列命令：

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  您也可以在這裡下載二進位檔：

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="下載 Linux ZIP（amd64）"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="下載 Linux ZIP（arm64）"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_arm64.zip"
      />      
    </div>
  </Box>

  </TabItem>  
  </Tabs>

</Box>

</TabItem>
<TabItem value="windows">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>

  ## Windows

  在 Windows 上可以透過 Chocolatey、PowerShell 安裝指令碼和 MSI 安裝程式取得 StackQL。原生 AMD64（x64）和 ARM64 建置以 MSI 安裝程式和包含 `stackql.exe` 的 ZIP 封存檔形式提供。AMD64 建置也可以透過模擬在 ARM64 上執行。所有可執行檔都以 Authenticode 程式碼簽署憑證簽署。

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'MSI/ZIP 下載', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  若要透過 Chocolatey 安裝，請在 PowerShell 或 `cmd` 終端機中執行下列命令：

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  若要在目前目錄中安裝 Authenticode 可執行檔，請在 PowerShell 終端機中執行下列命令： 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  請從下方下載已簽署的 Windows `stackql` 安裝程式或可執行檔。按一下下載按鈕預設會選取 AMD64；使用按鈕的下拉式選單可選擇 AMD64 或原生 ARM64。

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="下載 Windows MSI"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: '下載 Windows MSI（amd64）', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: '下載 Windows MSI（arm64）', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="下載 Windows ZIP"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: '下載 Windows ZIP（amd64）', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: '下載 Windows ZIP（arm64）', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
        ]}
      />      
    </div>
  </Box>

  </TabItem>

  </Tabs>  

</Box>

</TabItem>
<TabItem value="docker">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>

  ## Docker

  StackQL 建置會發佈到 [Docker Hub](https://hub.docker.com/u/stackql)。  

  __Docker Hub__  

  若要提取 StackQL 容器映像檔，請執行下列命令：

  ```bash
  docker pull stackql/stackql
  ```

  從 `v1.0` 起，映像檔以 Ubuntu 26.04 LTS 為基礎，另有預先安裝 Python 3.13 的變體，以 `-python3.13` 標籤尾碼（別名 `-python3`）發佈，例如：

  ```bash
  docker pull stackql/stackql:latest-python3.13
  ```
</Box>

</TabItem>
<TabItem value="deploy">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>

## `stackql-deploy` {#stackql-deploy}

`stackql-deploy` 是以 StackQL 為基礎建置的無狀態 IaC 架構。完整文件請參閱 [**StackQL Deploy 文件**](https://stackql-deploy.io/)。

__Linux / macOS__

一個步驟即可下載並解壓縮最新的 `stackql-deploy` 二進位檔。安裝程式會偵測您的作業系統和架構（包括透過通用 macOS 二進位檔支援的 Apple Silicon 和 Intel），並自動取得正確的發行版本資產。
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

將最新的 `stackql-deploy` 二進位檔下載並解壓縮到目前目錄。安裝程式會偵測您的平台，並自動取得正確的發行版本資產。
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

如果您已安裝 Rust 工具鏈，此命令會直接從 [__crates.io__](https://crates.io/crates/stackql-deploy) 建置並安裝二進位檔。
```bash
cargo install stackql-deploy
```

</Box>

</TabItem>
<TabItem value="shells">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>

  ## 雲端 shell

  StackQL 可以直接在主要雲端和資料平台內建的雲端 shell 與網頁終端機中使用。這些環境已預先以您登入的身分完成授權，因此能提供順暢的體驗，不需要另外設定驗證。

  <Tabs
    defaultValue="aws"
    values={[
      { label: 'AWS', value: 'aws', },
      { label: 'Azure', value: 'azure', },
      { label: 'Google', value: 'google', },
      { label: 'Databricks', value: 'databricks', },
    ]}
  >
  <TabItem value="aws">

  ### AWS CloudShell

  AWS CloudShell 提供以瀏覽器為基礎的 shell，已預先安裝 AWS CLI 並完成驗證。在 AWS CloudShell 中執行 StackQL，您不需要額外的驗證步驟，即可查詢和管理 AWS 資源。如需詳細說明，請參閱我們的 [AWS CloudShell 教學](/quick-starts/aws/aws-cloud-shell)。

  首先，下載 StackQL 套件：

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  然後執行 StackQL AWS CloudShell 指令碼：
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  此指令碼會使用您的 AWS CloudShell 憑證啟動 StackQL 命令 shell，讓您可以立即開始查詢 AWS 資源。

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell 提供可從瀏覽器存取的 shell 環境，其中的 Azure CLI 已使用您的 Azure 帳戶預先完成驗證。在 Azure Cloud Shell 中使用 StackQL，不需要額外設定即可順暢查詢您的 Azure 資源。如需完整詳細資訊，請參閱我們的 [Azure Cloud Shell 指南](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell)。  

  首先，下載 StackQL 套件：

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  然後執行 StackQL Azure Cloud Shell 指令碼：

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  此指令碼會自動設定 StackQL 工作階段以使用您的 Azure Cloud Shell 憑證，讓您可以立即存取並查詢 Azure 資源。

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell 提供開發與維運環境，其中的 Google Cloud CLI 已完成驗證。在 Google Cloud Shell 中執行 StackQL，您可以使用現有的驗證查詢 GCP 資源。如需詳細資訊，請參閱我們的 [Google Cloud Shell 指南](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell)。  

  首先，下載 StackQL 套件：
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  然後執行 StackQL Google Cloud Shell 指令碼：

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  此指令碼會將 StackQL 設定為使用您的 Google Cloud Shell 憑證，讓您可以立即開始使用 SQL 語法查詢 GCP 資源。

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web Terminal

  Databricks 工作區包含一個以登入使用者身分執行的網頁終端機。在其中執行 StackQL，您可以使用您的 Databricks 身分查詢工作區，不需要另外設定驗證。如需查詢範例和帳戶層級驗證，請參閱我們的 [Databricks Web Terminal 指南](/blog/tutorials/stackql-in-databricks-web-terminal)。

  首先，下載 StackQL 套件：

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  然後執行 StackQL Databricks shell 指令碼：

  ```bash
  ./stackql-databricks-shell.sh
  ```

  這會使用您的 Databricks 工作區身分啟動 StackQL 工作階段，讓您可以立即查詢工作區範圍的資源，例如 `databricks_workspace.iam.vw_user_entitlements`。若要執行帳戶層級查詢（佈建、計費、帳戶 IAM），請依照指南中的說明設定 Databricks OAuth2 服務主體變數。

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## 搭配 MCP 用戶端使用 {#using-with-mcp-clients}

StackQL MCP 伺服器透過 stdio 在本機執行，因此無論您使用哪個用戶端，Provider 憑證都會留在您的電腦上。請在下方選擇您的用戶端：每個索引標籤會先提供目錄或一鍵安裝方式，再提供手動設定作為備用方案。如需所有發佈通道（包括 `npx`、Python、Docker 和 CI），請參閱[安裝 MCP 伺服器](#installing-the-mcp-server)。

<Tabs
  defaultValue="claude"
  values={[
    { label: tabLabel(<ClaudeIcon />, 'Claude Desktop'), value: 'claude', },
    { label: tabLabel(<SiOpenai />, 'ChatGPT'), value: 'chatgpt', },
    { label: tabLabel(<GooseIcon />, 'Goose'), value: 'goose', },
  ]}
>
<TabItem value="claude">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>

  ### Claude Desktop

  將 StackQL MCP 伺服器安裝到 Claude Desktop 的建議方式是透過 [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql)（**Settings -> Connectors -> Add -> Browse Connectors**，搜尋 **StackQL**），Claude Desktop 會自動為您的平台選取正確的已簽署二進位檔。此外，每個 StackQL 發行版本都附有預先建置的 MCP Bundle（`.mcpb`），可從下載的檔案安裝。無論採用哪種方式，都不需要另外安裝 StackQL。安裝套件提供 macOS、Windows 和 Linux 版本。

  Linux 和 Windows 的 MCPB 下載預設為 **amd64**（x64）。使用下拉式選單可選取 **amd64** 或 **arm64**。
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="下載 macOS MCPB"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="下載 Linux MCPB"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: '下載 Linux MCPB（amd64）', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: '下載 Linux MCPB（arm64）', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="下載 Windows MCPB"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: '下載 Windows MCPB（amd64）', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: '下載 Windows MCPB（arm64）', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

StackQL MCP 伺服器也以 `io.github.stackql/stackql-mcp` 名稱列於 [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)。

:::

:::tip

安裝套件是執行 StackQL MCP 伺服器的數種方式之一。如需完整的 Claude Desktop 逐步解說，請參閱[**在 Claude Desktop 中使用 StackQL**](/getting-started/claude-desktop)；如需 npx、Docker、Python、CI 和手動選項，請參閱[**安裝 MCP 伺服器**](#installing-the-mcp-server)。

:::

</Box>

</TabItem>
<TabItem value="chatgpt">

<Box sx={{
  border: '1px solid #e0e0e0',
  borderRadius: '4px',
  p: 3,
  mb: 2
}}>

  ### ChatGPT 和 Codex

  StackQL 以本機外掛程式的形式提供給 ChatGPT 桌面應用程式和 Codex CLI。此外掛程式透過 stdio 在您的電腦上執行 StackQL MCP 伺服器，因此 Provider 憑證不會離開您的電腦。需要 Node.js 24.22.1 或更新版本（在對應的發行線上為 22.23.4 或 26.12.1）；此外掛程式只需要 Node.js 內建模組，會在首次使用時下載已簽署的 `stackql` 二進位檔並驗證其 SHA-256。

  **ChatGPT 桌面應用程式（外掛程式目錄）**：  
  
  在 ChatGPT 桌面應用程式中開啟外掛程式目錄，搜尋 **StackQL** 並按一下 **Install**，然後執行外掛程式的設定；隨附的 `stackql-setup` 技能會逐步帶您完成憑證設定和第一個查詢。此清單項目帶有 **Desktop only** 徽章：此外掛程式只能從桌面應用程式安裝，無法從 ChatGPT 網頁版或行動版安裝，且其工具僅在 Work mode 中、在您電腦上執行的對話串中提供。如果您的目錄中尚未顯示此清單項目，請改從存放庫市集安裝。

  **ChatGPT 桌面應用程式（存放庫市集）**：  
  
  開啟 **Settings -> Plugins -> Add -> Add from GitHub**，輸入 `stackql/stackql`，然後從外掛程式目錄的 **Personal** 區段安裝 **StackQL**，並重新啟動應用程式。

  **Codex CLI**：

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  重新啟動 ChatGPT 桌面版或 Codex，然後嘗試：*使用 StackQL 安裝 GitHub Provider（如有需要），然後列出前五個 GitHub 服務。* Provider 憑證放在 `~/.stackql/.env` 中，並使用各 Provider 的標準環境變數名稱（例如 `AWS_ACCESS_KEY_ID` 和 `AWS_SECRET_ACCESS_KEY`）；編輯該檔案後，請要求代理程式呼叫 `reload_credentials`。

  **不使用外掛程式**：   
  
  在 **Settings -> Plugins -> MCPs -> Connect to a custom MCP** 下，選擇 **STDIO**，將命令設為 `npx`，並新增引數 `-y` 和 `@stackql/mcp-server`。在該表單上以環境變數提供 Provider 憑證。

<br />
:::note

ChatGPT 網頁版和行動版無法執行本機 stdio 伺服器，因此此外掛程式僅適用於桌面版和 Codex。ChatGPT 桌面版一個尚未修正的錯誤（[openai/codex#38162](https://github.com/openai/codex/issues/38162)）可能會在 **Chat** 工作階段中隱藏本機 MCP 工具；Work mode 對話串、Codex 對話串和 Codex CLI 不受影響。

:::

</Box>

</TabItem>
<TabItem value="goose">

<Box sx={{
  border: '1px solid #e0e0e0',
  borderRadius: '4px',
  p: 3,
  mb: 2
}}>

  ### Goose

  Goose 會直接執行 stdio 擴充功能，因此您只需要 `npx` 啟動器（Node.js 18 或更新版本）。

  **Goose Desktop**：按一下下方按鈕。Goose 會開啟，顯示即將執行的命令並要求您確認，然後將 StackQL 新增為擴充功能。

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>在 Goose Desktop 中安裝</a>
  </Box>

  此按鈕是 `goose://` 深層連結；您也可以將它貼到瀏覽器的網址列，或在終端機中使用 `open`（macOS）、`start`（Windows）或 `xdg-open`（Linux）開啟：

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI**：

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  或執行 `goose configure`，選擇 **Add Extension -> Command-line Extension**，然後輸入相同的命令。出現提示時，以擴充功能的環境變數提供 Provider 憑證。

<br />
:::note

Goose 已停用應用程式內的擴充功能目錄，改用 [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)，StackQL 在其中以 `io.github.stackql/stackql-mcp` 列出。Goose 推出登錄庫匯入功能後，不需要任何額外動作，即可在其中找到 StackQL。

:::

</Box>

</TabItem>
</Tabs>

## 安裝 MCP 伺服器 {#installing-the-mcp-server}

StackQL MCP 伺服器可讓 AI 代理程式使用 StackQL 查詢和佈建雲端資源。它透過多種通道發佈，這些通道都執行相同的伺服器，因此請選擇符合您的用戶端和信任需求的通道。下列選項大致依信任程度和易用性排序。

### 市集和目錄 {#marketplaces-and-directories}

此伺服器以 `io.github.stackql/stackql-mcp` 發佈至 Official MCP Registry；下列套件登錄庫提供可散發的成品，下游目錄則從這些來源取得清單項目。  

<Tabs
  defaultValue="registries"
  values={[
    { label: '登錄庫', value: 'registries', },
    { label: '目錄', value: 'directories', },
  ]}
>
<TabItem value="registries">

| 登錄庫 | 類型 | 發佈方式 | 清單項目 | 備註 |
|---|---|---|---|---|
| Official MCP Registry | 標準中繼資料登錄庫 | `mcp-publisher` CLI + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | 宣告全部 8 個套件（`mcpb x5`、`oci`、`npm`、`pypi`） |
| npm | 套件登錄庫 | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | `npx` 啟動器 |
| PyPI | 套件登錄庫 | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | `uvx` / `pip` 啟動器 |
| Docker Hub | 容器登錄庫 | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | 多架構 `amd64` + `arm64` |
| GitHub Actions Marketplace | CI 市集（`setup-stackql-mcp` 動作） | 公開存放庫 + 發行版本 + 市集清單項目 | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | 已驗證的發行者；[`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

目錄和彙總網站清單項目，大致依對探索的重要性排序：

| 目錄 | 類型 | 清單項目 |
|---|---|---|
| __Anthropic Connector Directory__ | Claude Desktop 應用程式內目錄 | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | ChatGPT 桌面應用程式內目錄（僅限桌面版） | 在 ChatGPT 桌面應用程式中搜尋 **StackQL**；請參閱 [ChatGPT 和 Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | IDE 用戶端目錄 | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | 最大的彙總網站 | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | 探索服務、登錄庫贊助者 | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | 可搜尋的市集 | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | 彙總網站 | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Awesome-list 網站 | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### 預先建置的 `.mcpb` 安裝套件 {#prebuilt-mcpb-bundle}

**適用情境**：無法存取 Connector Directory 的 Claude Desktop，或您想要固定特定版本，或在安裝前自行驗證總和檢查碼時。對於大多數 Claude Desktop 使用者，建議的安裝方式是 [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql)。

每個 StackQL 發行版本都附有適用於各平台的預先建置 MCP Bundle：

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

其中 `<platform>` 為 `darwin-universal`、`windows-x64`（AMD64）、`windows-arm64`、`linux-x64` 或 `linux-arm64` 之一。各平台的下載按鈕位於[搭配 MCP 用戶端使用](#using-with-mcp-clients)下的 **Claude Desktop** 索引標籤中。每個安裝套件在[發行版本頁面](https://github.com/stackql/stackql/releases/latest)上都有對應的 `.sha256` 總和檢查碼。驗證後，再透過 Claude Desktop 中的 **Settings -> Extensions** 安裝：

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

如需完整的逐步解說，請參閱[在 Claude Desktop 中使用 StackQL](/getting-started/claude-desktop#install-from-a-downloaded-bundle)。

### 手動設定 `claude_desktop_config.json` {#manual-claude_desktop_configjson}

**適用情境**：您的 PATH 中已有 `stackql` 二進位檔，並使用 Claude Desktop 或任何其他 stdio MCP 用戶端。

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--approot", "/Users/you/.stackql",
        "--mcp.config", "{\"server\": {\"audit\": {\"disabled\": true}}}"
      ]
    }
  }
}
```

這三個引數缺一不可：

- `--mcp.server.type=stdio` 選取編輯器內嵌用戶端所使用的 stdio 傳輸方式。
- `--approot` 將 Provider 快取指向可寫入的目錄。MCP 用戶端可能會在工作目錄設為 `/` 的情況下啟動伺服器，而該目錄無法寫入。
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` 會停用稽核記錄；否則稽核記錄的目錄預設為（可能無法寫入的）工作目錄。

使用 `"env"` 區塊新增 Provider 憑證，並使用 `"mode"` 調整安全契約。請參閱[伺服器模式](/command-line-usage/mcp#server-modes)。

### `npx`（免安裝） {#npx-no-install}

**適用情境**：任何 Node 環境，且您不想全域安裝 StackQL 時。

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

[`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) 啟動器會在首次執行時下載已簽署的 `stackql` 二進位檔，驗證其 SHA-256，並將其快取以供後續執行使用。

### `uvx` / `pip` (Python) {#uvx--pip-python}

**適用情境**：Python 環境。這是為 Python 封裝的同一個啟動器，與 npx 啟動器共用其填入的二進位檔快取。

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

或使用 `pip install stackql-mcp-server` 將其安裝到目前的環境中（Python 3.9+）。此套件在 PyPI 上為 [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/)。

### Docker {#docker}

**適用情境**：容器化或其他隔離的執行環境。此映像檔支援多架構（amd64 和 arm64）。

```bash
docker run -i --rm stackql/stackql-mcp
```

作為用戶端設定：

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

此映像檔在 Docker Hub 上為 [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp)。

### CI 和代理式工作流程（GitHub Actions） {#ci-and-agentic-workflows-github-actions}

**適用情境**：在 GitHub Actions 作業中執行伺服器，讓代理程式能在 CI 中查詢您的雲端，並在您允許時對其採取動作。

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) 會安裝二進位檔並寫入 MCP 設定（預設為 `read_only` 模式）。透過 `claude_args` 將該設定傳遞給 [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action)：

```yaml
- id: stackql
  uses: stackql/setup-stackql-mcp@v1
  with:
    auth: '{"github":{"type":"null_auth"}}'

- uses: anthropics/claude-code-action@v1
  with:
    anthropic_api_key: ${{ secrets.ANTHROPIC_API_KEY }}
    prompt: |
      Using stackql, list the public repositories in the stackql org and
      summarise them as a markdown table.
    claude_args: |
      --mcp-config ${{ steps.stackql.outputs.mcp-config-file }}
      --allowedTools 'mcp__stackql__*'
```

如需更多代理式用法範例（雲端稽核、成本估算等），請參閱[此動作的 README](https://github.com/stackql/setup-stackql-mcp)。

### 內嵌式 MCP {#embedded-mcp}

**適用情境**：您正在建置代理式應用程式，並想將 MCP 伺服器 vendor 到您自己的二進位檔中：不需要 `npx`、不需要另外安裝，也沒有執行階段相依性。

原生程式庫在各語言官方 MCP SDK 用戶端背後，透過 stdio 啟動已簽署的 `stackql` 二進位檔，讓您的應用程式透過同一套受治理的 SQL 介面存取登錄庫中的每個 Provider。每個程式庫預設為 `read_only`（提升為可寫入模式一律需要明確指定），並且可以在首次執行時下載並驗證二進位檔，或將其 vendor 到您的建置中，以產生單一獨立的可執行檔。完整的各語言 API 請參閱[內嵌式 MCP 參考](/mcp/embedded)。

<Tabs
  defaultValue="rust"
  values={[
    { label: 'Rust', value: 'rust', },
    { label: 'Go', value: 'go', },
    { label: 'Kotlin / JVM', value: 'kotlin', },
    { label: '.NET', value: 'dotnet', },
    { label: 'Gleam', value: 'gleam', },
    { label: 'Swift', value: 'swift', },
  ]}
>
<TabItem value="rust">

`stackql-mcp` crate 會在 `rmcp` 用戶端背後啟動二進位檔。預設的 `sidecar` 功能會在首次執行時下載並驗證二進位檔；`vendored` 功能則使用 `include_bytes!` 將其內嵌，以產生單一獨立的二進位檔。MSRV 為 Rust 1.88。

```toml
# Cargo.toml
stackql-mcp = "0.1"
```

```rust
use stackql_mcp::{Mode, StackqlMcp};

let server = StackqlMcp::builder()
    .mode(Mode::ReadOnly)
    .start()
    .await?;
let tools = server.list_all_tools().await?;
```

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
  <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上檢視" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="在 crates.io 上檢視" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

完整參考：[內嵌式 MCP：Rust](/mcp/embedded/rust)。

</TabItem>
<TabItem value="go">

`stackql-mcp-go` 程式庫會在官方 Go MCP SDK 用戶端背後啟動二進位檔。使用 `go:embed` vendor 二進位檔可產生單一獨立的二進位檔，也可以讓它在首次執行時下載並驗證。預設以 `read_only` 模式啟動。

```bash
go get github.com/stackql/stackql-mcp-go
```

```go
import (
    "context"
    stackqlmcp "github.com/stackql/stackql-mcp-go/embed"
)

client, err := stackqlmcp.StartServer(ctx, stackqlmcp.Options{
    Binary: StackqlMCPBinary(),
})
defer client.Close()
```

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
  <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上檢視" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="在 pkg.go.dev 上檢視" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

完整參考：[內嵌式 MCP：Go](/mcp/embedded/go)。

</TabItem>
<TabItem value="kotlin">

`io.stackql:stackql-mcp` 程式庫會在官方 Kotlin MCP SDK 用戶端背後啟動二進位檔，另有一個配套的 Gradle 外掛程式，可將相同的啟動方式接入建置。需要 JDK 17 和 Kotlin 2.x。

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

```kotlin
import io.stackql.mcp.Mode
import io.stackql.mcp.StackqlMcp

val server = StackqlMcp.builder().mode(Mode.ReadOnly).start()
server.use {
    val tools = server.client.listTools().tools
}
```

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
  <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上檢視" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

完整參考：[內嵌式 MCP：Kotlin / JVM](/mcp/embedded/kotlin)。

</TabItem>
<TabItem value="dotnet">

`StackQL.Mcp` 套件會在官方 C# MCP SDK 用戶端背後啟動二進位檔。預設採用 sidecar 方式，也可以將安裝套件作為建置資源 vendor，以產生獨立的可執行檔。需要 .NET 8 或更新版本。

```bash
dotnet add package StackQL.Mcp
```

```csharp
using StackQL.Mcp;

await using var server = await StackqlMcp.CreateBuilder()
    .WithMode(StackqlMode.ReadOnly)
    .StartAsync();

var tools = await server.ListToolsAsync();
```

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
  <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上檢視" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

完整參考：[內嵌式 MCP：.NET / C#](/mcp/embedded/dotnet)。

</TabItem>
<TabItem value="gleam">

`stackql_mcp` 程式庫以 Erlang/BEAM 執行階段為目標，並透過 `child_spec()` 將伺服器公開為 OTP 子處理程序，因此可以放在您自己的監督樹中。預設以 `read_only` 模式啟動。

```bash
gleam add stackql_mcp
```

```gleam
import envoy
import stackql_mcp

let assert Ok(server) =
  stackql_mcp.start(
    config: stackql_mcp.default_config(),
    home: "/home/u", os: "linux", arch: "x86_64",
    getenv: envoy.get,
  )
let assert Ok(tools) = stackql_mcp.list_tools(server)
```

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
  <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上檢視" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

完整參考：[內嵌式 MCP：Gleam](/mcp/embedded/gleam)。

</TabItem>
<TabItem value="swift">

`stackql-mcp-swift` 套件會在官方 Swift MCP SDK 用戶端背後啟動二進位檔。`darwin-universal` 二進位檔已使用 Developer ID 簽署並經過 Apple 公證，因此您可以將它封裝在已簽署的 `.app` 中，同時維持應用程式的公證有效。需要 macOS 13+ 和 Swift 6.1（Xcode 16.3+）。

```swift
// Package.swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
await server.stop()
```

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
  <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上檢視" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

完整參考：[內嵌式 MCP：Swift](/mcp/embedded/swift)。

</TabItem>
</Tabs>

### 信任模型 {#trust-model}

所有通道都具備相同的安全屬性：

- 內嵌的 `stackql` 二進位檔已使用 Authenticode 簽署（Windows）並經過 Apple 公證（macOS）。
- 每個 `.mcpb` 安裝套件在發行版本頁面上都附有公開的 SHA-256 總和檢查碼。
- `npx` 和 `uvx` 啟動器會在首次使用前驗證所下載二進位檔的 SHA-256。
- [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) 項目會為各平台的雜湊提供證明。

## 使用 GitHub Actions {#using-github-actions}

[StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo) 可在您的 GitHub Actions 工作流程中使用。提供下列動作：

<Tabs
  defaultValue="deploy"
  values={[
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'setup-stackql', value: 'setup', },
    { label: 'setup-stackql-mcp', value: 'setup-mcp', },
    { label: 'stackql-exec', value: 'exec', },
    { label: 'stackql-assert', value: 'assert', },    
  ]}
>
<TabItem value="deploy">

### `stackql-deploy` {#stackql-deploy-1}

直接從您的 GitHub 工作流程使用 StackQL 部署或測試基礎架構堆疊。此動作支援使用類 SQL 語法的基礎架構即程式碼（IaC）工作流程，讓您能在單一工作流程中定義、部署和管理跨多個 Provider 的雲端資源。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上檢視"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="在 GitHub 上檢視"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

**範例用法**

```yaml
...
jobs:
  stackql-actions-test:
    name: StackQL Actions Test
    runs-on: ubuntu-latest
    env:
      GOOGLE_CREDENTIALS: ${{ secrets.GOOGLE_CREDENTIALS }} # add additional cloud provider creds here as needed

    steps:
      - name: Checkout
        uses: actions/checkout@v6

      - name: Deploy a Stack
        uses: stackql/stackql-deploy-action@v2
        with:
          command: 'build'
          stack_dir: 'examples/k8s-the-hard-way'
          stack_env: 'dev'
          env_vars: |
            GOOGLE_PROJECT=stackql-k8s-the-hard-way-demo
            GOOGLE_REGION=australia-southeast1
```

</TabItem>
<TabItem value="setup">

### `setup-stackql` {#setup-stackql}

在您的 GitHub Actions 工作流程中設定 StackQL。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上檢視"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="在 GitHub 上檢視"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

**範例用法**

```yaml
- name: setup StackQL
  uses: stackql/setup-stackql@v2
  with:
    use_wrapper: true

- name: Use GitHub Provider
  run: |
    stackql exec -i ./examples/github-example.iql
  env: 
    STACKQL_GITHUB_USERNAME: ${{  secrets.STACKQL_GITHUB_USERNAME }}
    STACKQL_GITHUB_PASSWORD: ${{  secrets.STACKQL_GITHUB_PASSWORD }}
```

</TabItem>
<TabItem value="setup-mcp">

### `setup-stackql-mcp` {#setup-stackql-mcp}

在您的 GitHub Actions 工作流程中安裝 StackQL MCP 伺服器。此動作會安裝已簽署的二進位檔，並寫入 `mcpServers` 設定（預設為 `read_only` 模式），供 `anthropics/claude-code-action` 等代理式步驟透過 `claude_args` 使用。

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上檢視"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub 上檢視"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

**範例用法**

```yaml
- id: stackql
  uses: stackql/setup-stackql-mcp@v1
  with:
    auth: '{"github":{"type":"null_auth"}}'

- uses: anthropics/claude-code-action@v1
  with:
    anthropic_api_key: ${{ secrets.ANTHROPIC_API_KEY }}
    prompt: |
      Using stackql, list the public repositories in the stackql org and
      summarise them as a markdown table.
    claude_args: |
      --mcp-config ${{ steps.stackql.outputs.mcp-config-file }}
      --allowedTools 'mcp__stackql__*'
```

</TabItem>
<TabItem value="exec">

### `stackql-exec` {#stackql-exec}

在您的 GitHub Actions 工作流程中執行 StackQL 命令。查詢可以內嵌提供，或來自存放庫中的檔案。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上檢視"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="在 GitHub 上檢視"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

**範例用法**

```yaml
- name: exec github example
  uses: stackql/stackql-exec@v2
  with:
    query: |
      select total_private_repos
      from github.orgs.orgs
      where org = 'stackql'
  env: 
    STACKQL_GITHUB_USERNAME: ${{  secrets.STACKQL_GITHUB_USERNAME }}
    STACKQL_GITHUB_PASSWORD: ${{  secrets.STACKQL_GITHUB_PASSWORD }}
```

</TabItem>
<TabItem value="assert">

### `stackql-assert` {#stackql-assert}

在您的 GitHub Actions 工作流程中執行單元測試（用於保證、治理或雲端安全性檢查）。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上檢視"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="在 GitHub 上檢視"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

**範例用法**

```yaml
- name: Use test query string and expected rows
  uses: stackql/stackql-assert@v2
  with:
    test_query: |
        SELECT name
        FROM google.compute.instances 
        WHERE project = 'stackql-demo' AND zone = 'australia-southeast1-a' AND name = 'stackql-demo-001';
    expected_rows: 1
  env: 
    GOOGLE_CREDENTIALS: ${{ secrets.GOOGLE_CREDENTIALS }}
```

</TabItem>  
</Tabs>


## 其他程式庫 {#other-libraries}

StackQL 提供數種整合方式，可在不同的程式語言和環境中使用。這些程式庫擴充了 StackQL 的功能，讓您能輕鬆將雲端資源的查詢與管理納入現有的應用程式和工作流程。  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### `pystackql` Python 套件 {#pystackql-python-package}

可讓您在 Python 程式中使用 StackQL 的 Python 包裝程式庫。`pystackql` 套件可從 [__PyPI__](https://pypi.org/project/pystackql/) 取得，`pystackql` 套件的文件可透過 [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/) 取得。若要安裝 `pystackql` 套件，請執行下列命令：

```bash
pip install pystackql
```

下列範例示範如何搭配 `pandas` 使用 `pystackql` 套件來執行 StackQL 查詢，並將結果傳回至 `pandas.DataFrame`：

```python
from pystackql import StackQL
import pandas as pd
region = "ap-southeast-2"
stackql = StackQL()

query = """
SELECT instance_type, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = '%s'
GROUP BY instance_type
""" % (region)

res = stackql.execute(query)
df = pd.read_json(res)
print(df)
```

</TabItem>
</Tabs>
