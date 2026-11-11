---
title: 安装
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "在 macOS、Linux、Windows、Docker 或云 shell 中安装 StackQL，并在 Claude Desktop、ChatGPT、Goose 等 MCP 客户端中，或通过 npx、uvx、pip、Docker 和 GitHub Actions 使用 StackQL MCP 服务器。"
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

本页提供在各种平台上安装 StackQL 的说明。

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: '云 shell', value: 'shells', },
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
  
  StackQL 在 macOS 上可通过 Homebrew 和 `pkg` 安装程序获取，单个多架构安装程序同时支持 ARM（M1/Apple Silicon）和 AMD 架构。

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl（已签名并经过公证的二进制文件）', value: 'curl', },
      { label: '软件包下载', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  要通过 Homebrew 安装，请在终端中运行以下命令：

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  要在当前目录中安装已签名并经过公证的 `stackql` 可执行二进制文件，请在终端中运行以下命令：

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL 提供适用于 macOS 的交互式 `pkg` 安装程序，该安装程序已签名并经过公证。

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="下载 macOS PKG"
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

  StackQL 适用于所有 Linux 架构。

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: '软件包下载', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  要在当前目录中安装适用于你的 Linux 架构（amd64 或 arm64）的 `stackql` 可执行二进制文件，请在终端中运行以下命令：

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  也可以在此处下载二进制文件：

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="下载 Linux ZIP（amd64）"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="下载 Linux ZIP（arm64）"
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

  StackQL 在 Windows 上可通过 Chocolatey、PowerShell 安装脚本和 MSI 安装程序获取。原生 AMD64（x64）和 ARM64 构建版本以 MSI 安装程序和包含 `stackql.exe` 的 ZIP 压缩包形式提供。AMD64 构建版本也可通过模拟在 ARM64 上运行。所有可执行文件均使用 Authenticode 代码签名证书签名。

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'MSI/ZIP 下载', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  要通过 Chocolatey 安装，请在 PowerShell 或 `cmd` 终端中运行以下命令：

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  要在当前目录中安装经 Authenticode 签名的可执行文件，请在 PowerShell 终端中运行以下命令： 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  在下方下载已签名的 Windows `stackql` 安装程序或可执行文件。点击下载按钮默认选择 AMD64；使用其下拉菜单可选择 AMD64 或原生 ARM64。

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="下载 Windows MSI"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: '下载 Windows MSI（amd64）', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: '下载 Windows MSI（arm64）', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="下载 Windows ZIP"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: '下载 Windows ZIP（amd64）', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: '下载 Windows ZIP（arm64）', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  StackQL 构建版本发布到 [DockerHub](https://hub.docker.com/u/stackql)。  

  __Docker Hub__  

  要拉取 StackQL 容器镜像，请运行以下命令：

  ```bash
  docker pull stackql/stackql
  ```

  从 `v1.0` 起，该镜像基于 Ubuntu 26.04 LTS，并以 `-python3.13` 标签后缀（别名 `-python3`）发布预装 Python 3.13 的变体，例如：

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

`stackql-deploy` 是基于 StackQL 构建的无状态 IaC 框架。完整文档请参阅 [__StackQL Deploy 文档__](https://stackql-deploy.io/)。

__Linux / macOS__

一步下载并解压最新的 `stackql-deploy` 二进制文件。安装程序会检测你的操作系统和架构（包括通过 macOS 通用二进制文件支持的 Apple Silicon 和 Intel），并自动获取正确的发布版本资产。
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

将最新的 `stackql-deploy` 二进制文件下载并解压到当前目录。
安装程序会检测你的平台，并自动获取正确的发布版本资产。
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

如果已安装 Rust 工具链，此命令将直接从 [__crates.io__](https://crates.io/crates/stackql-deploy) 构建并安装该二进制文件。
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

  ## 云 shell

  StackQL 可以直接在主流云平台和数据平台内置的云 shell 和 Web 终端中使用。这些环境已使用你登录的身份预先授权，无需单独设置身份验证，因此使用体验顺畅。

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

  ### AWS Cloud Shell

  AWS CloudShell 提供基于浏览器的 shell，其中已预装 AWS CLI 并完成身份验证。在 AWS CloudShell 中运行 StackQL，无需额外的身份验证步骤即可查询和管理 AWS 资源。详细说明请参阅 [AWS CloudShell 教程](/quick-starts/aws/aws-cloud-shell)。

  首先，下载 StackQL 软件包：

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  然后运行 StackQL AWS CloudShell 脚本：
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  该脚本使用你的 AWS CloudShell 凭据启动 StackQL 命令 shell，让你可以立即开始查询 AWS 资源。

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell 提供可通过浏览器访问的 shell 环境，其中的 Azure CLI 已使用你的 Azure 账号预先完成身份验证。在 Azure Cloud Shell 中使用 StackQL，无需额外设置即可顺畅地查询你的 Azure 资源。完整详情请参阅 [Azure Cloud Shell 指南](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell)。  

  首先，下载 StackQL 软件包：

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  然后运行 StackQL Azure Cloud Shell 脚本：

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  该脚本自动将 StackQL 会话配置为使用你的 Azure Cloud Shell 凭据，让你可以立即查询你的 Azure 资源。

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell 提供开发和运维环境，其中的 Google Cloud CLI 已完成身份验证。在 Google Cloud Shell 中运行 StackQL，可以使用现有的身份验证查询 GCP 资源。有关更多信息，请参阅 [Google Cloud Shell 指南](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell)。  

  首先，下载 StackQL 软件包：
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  然后运行 StackQL Google Cloud Shell 脚本：

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  该脚本将 StackQL 设置为使用你的 Google Cloud Shell 凭据，让你可以立即开始使用 SQL 语法查询你的 GCP 资源。

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web 终端

  Databricks 工作区包含以当前登录用户身份运行的 Web 终端。在其中运行 StackQL，可以使用你的 Databricks 身份查询工作区，无需单独设置身份验证。示例查询和账号级身份验证请参阅 [Databricks Web 终端指南](/blog/tutorials/stackql-in-databricks-web-terminal)。

  首先，下载 StackQL 软件包：

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  然后运行 StackQL Databricks shell 脚本：

  ```bash
  ./stackql-databricks-shell.sh
  ```

  这会使用你的 Databricks 工作区身份启动 StackQL 会话，因此可以立即查询工作区级资源，例如 `databricks_workspace.iam.vw_user_entitlements`。对于账号级查询（预配、计费、账号 IAM），请按指南中的说明设置 Databricks OAuth2 服务主体变量。

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## 与 MCP 客户端配合使用 {#using-with-mcp-clients}

StackQL MCP 服务器通过 stdio 在本地运行，因此无论使用哪个客户端，Provider 凭据都保留在你的机器上。在下方选择你的客户端：每个选项卡先给出目录或一键安装途径，再给出手动配置作为备选。所有分发渠道（包括 `npx`、Python、Docker 和 CI）请参阅[安装 MCP 服务器](#installing-the-mcp-server)。

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

  将 StackQL MCP 服务器安装到 Claude Desktop 的推荐方式是通过 [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql)（**Settings -> Connectors -> Add -> Browse Connectors**，搜索 **StackQL**），Claude Desktop 会自动为你的平台选择正确的已签名二进制文件。此外，每个 StackQL 发布版本都附带预构建的 MCP Bundle（`.mcpb`），可从下载的文件安装；两种方式都不需要单独安装 StackQL。安装包适用于 macOS、Windows 和 Linux。

  Linux 和 Windows 的 MCPB 下载默认为 **amd64**（x64）。使用下拉菜单可选择 **amd64** 或 **arm64**。
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="下载 macOS MCPB"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="下载 Linux MCPB"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: '下载 Linux MCPB（amd64）', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: '下载 Linux MCPB（arm64）', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="下载 Windows MCPB"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: '下载 Windows MCPB（amd64）', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: '下载 Windows MCPB（arm64）', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

StackQL MCP 服务器也已收录于 [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)，名称为 `io.github.stackql/stackql-mcp`。

:::

:::tip

安装包是运行 StackQL MCP 服务器的多种方式之一。完整的 Claude Desktop 操作指南请参阅[__在 Claude Desktop 中使用 StackQL__](/getting-started/claude-desktop)，npx、Docker、Python、CI 和手动配置等选项请参阅[__安装 MCP 服务器__](#installing-the-mcp-server)。

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

  StackQL 以本地插件的形式提供给 ChatGPT 桌面应用和 Codex CLI。该插件在你的机器上通过 stdio 运行 StackQL MCP 服务器，因此 Provider 凭据不会离开你的机器。需要 Node.js 24.22.1 或更高版本（在相应版本线上为 22.23.4 或 26.12.1）；该插件只需要 Node.js 内置模块，首次使用时下载已签名的 `stackql` 二进制文件并校验其 SHA-256。

  **ChatGPT 桌面应用（插件目录）：**  
  
  在 ChatGPT 桌面应用中打开插件目录，搜索 **StackQL** 并点击 **Install**，然后运行插件的设置流程；随附的 `stackql-setup` 技能会引导完成凭据配置和首次查询。该条目带有 **Desktop only** 徽章：插件只能从桌面应用安装，不能从 ChatGPT 网页版或移动版安装，其工具在 Work 模式下、在你的计算机上运行的线程中可用。如果该条目尚未出现在你的目录中，请改为从仓库市场安装。

  **ChatGPT 桌面应用（仓库市场）：**  
  
  打开 **Settings -> Plugins -> Add -> Add from GitHub**，输入 `stackql/stackql`，然后从插件目录的 **Personal** 部分安装 **StackQL** 并重启应用。

  **Codex CLI：**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  重启 ChatGPT 桌面应用或 Codex，然后试试：*使用 StackQL，在需要时先安装 GitHub Provider，然后列出前 5 个 GitHub 服务。* Provider 凭据放在 `~/.stackql/.env` 中，使用各 Provider 的标准环境变量名（例如 `AWS_ACCESS_KEY_ID` 和 `AWS_SECRET_ACCESS_KEY`）；编辑该文件后，让智能体调用 `reload_credentials`。

  **不使用插件：**   
  
  在 **Settings -> Plugins -> MCPs -> Connect to a custom MCP** 下选择 **STDIO**，将命令设置为 `npx`，并添加参数 `-y` 和 `@stackql/mcp-server`。在该表单中以环境变量的形式提供 Provider 凭据。

<br />
:::note

ChatGPT 网页版和移动版无法运行本地 stdio 服务器，因此该插件仅适用于桌面应用和 Codex。ChatGPT 桌面应用的一个未修复 bug（[openai/codex#38162](https://github.com/openai/codex/issues/38162)）可能导致本地 MCP 工具在 **Chat** 会话中不可见；Work 模式线程、Codex 线程和 Codex CLI 不受影响。

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

  Goose 直接运行 stdio 扩展，因此只需要 `npx` 启动器（Node.js 18 或更高版本）。

  **Goose Desktop：** 点击下方按钮。Goose 会打开，显示即将运行的命令并请你确认，然后将 StackQL 添加为扩展。

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>在 Goose Desktop 中安装</a>
  </Box>

  该按钮是一个 `goose://` 深层链接；也可以将其粘贴到浏览器的地址栏，或在终端中使用 `open`（macOS）、`start`（Windows）或 `xdg-open`（Linux）打开：

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI：**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  或者运行 `goose configure`，选择 **Add Extension -> Command-line Extension**，然后输入相同的命令。出现提示时，以该扩展的环境变量的形式提供 Provider 凭据。

<br />
:::note

Goose 已停用其应用内扩展目录，改用 [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)，StackQL 在其中以 `io.github.stackql/stackql-mcp` 的名称收录。Goose 推出注册表导入功能后，无需任何额外操作即可在其中发现 StackQL。

:::

</Box>

</TabItem>
</Tabs>

## 安装 MCP 服务器 {#installing-the-mcp-server}

StackQL MCP 服务器让 AI 智能体可以使用 StackQL 查询和预配云资源。它通过多个渠道分发，这些渠道运行的都是同一个服务器，因此请选择与你的客户端和信任要求相符的渠道。以下选项大致按可信度和易用性排序。

### 市场和目录 {#marketplaces-and-directories}

该服务器以 `io.github.stackql/stackql-mcp` 的名称发布到 Official MCP Registry；下方的软件包注册表承载可分发的制品，下游目录从那里获取该条目。  

<Tabs
  defaultValue="registries"
  values={[
    { label: '注册表', value: 'registries', },
    { label: '目录', value: 'directories', },
  ]}
>
<TabItem value="registries">

| 注册表 | 类型 | 发布方式 | 条目 | 备注 |
|---|---|---|---|---|
| Official MCP Registry | 规范元数据注册表 | `mcp-publisher` CLI + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | 公布全部 8 个软件包（`mcpb x5`、`oci`、`npm`、`pypi`） |
| npm | 软件包注册表 | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | `npx` 启动器 |
| PyPI | 软件包注册表 | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | `uvx` / `pip` 启动器 |
| Docker Hub | 容器注册表 | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | 多架构 `amd64` + `arm64` |
| GitHub Actions Marketplace | CI 市场（`setup-stackql-mcp` action） | 公开仓库 + 发布版本 + 市场条目 | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | 已验证的发布者；[`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

目录和聚合站点中的条目，大致按对发现的重要性排序：

| 目录 | 类型 | 条目 |
|---|---|---|
| __Anthropic Connector Directory__ | Claude Desktop 应用内目录 | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | ChatGPT 桌面应用内目录（仅限桌面应用） | 在 ChatGPT 桌面应用中搜索 **StackQL**；请参阅 [ChatGPT 和 Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | IDE 客户端目录 | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | 最大的聚合站点 | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | 发现平台、注册表支持方 | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | 可搜索的市场 | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | 聚合站点 | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Awesome 列表站点 | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### 预构建的 `.mcpb` 安装包 {#prebuilt-mcpb-bundle}

**适用场景：** Claude Desktop 无法访问 Connector Directory，或者希望固定到特定版本，或在安装前自行验证校验和。对于大多数 Claude Desktop 用户，推荐的安装方式是 [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql)。

每个 StackQL 发布版本都为每个平台附带一个预构建的 MCP Bundle：

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

其中 `<platform>` 为 `darwin-universal`、`windows-x64`（AMD64）、`windows-arm64`、`linux-x64` 或 `linux-arm64` 之一。各平台的下载按钮位于[与 MCP 客户端配合使用](#using-with-mcp-clients)下的 **Claude Desktop** 选项卡中。每个安装包在[发布页面](https://github.com/stackql/stackql/releases/latest)上都有对应的 `.sha256` 校验和。先验证校验和，然后在 Claude Desktop 中通过 **Settings -> Extensions** 安装：

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

完整操作指南请参阅[在 Claude Desktop 中使用 StackQL](/getting-started/claude-desktop#install-from-a-downloaded-bundle)。

### 手动配置 `claude_desktop_config.json` {#manual-claude_desktop_configjson}

**适用场景：** `stackql` 二进制文件已在你的 PATH 中，并且使用 Claude Desktop 或其他任何 stdio MCP 客户端。

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

这三个参数都不可或缺：

- `--mcp.server.type=stdio` 选择嵌入编辑器的客户端所使用的 stdio 传输方式。
- `--approot` 将 Provider 缓存指向一个可写目录。MCP 客户端启动服务器时可能将工作目录设置为 `/`，而该目录不可写。
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` 禁用审计日志，否则审计日志的目录默认为（可能不可写的）工作目录。

使用 `"env"` 块添加 Provider 凭据，使用 `"mode"` 调整安全契约，请参阅[服务器模式](/command-line-usage/mcp#server-modes)。

### `npx`（免安装） {#npx-no-install}

**适用场景：** 任何 Node 环境，且不希望全局安装 StackQL 时。

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

[`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) 启动器在首次运行时下载已签名的 `stackql` 二进制文件，校验其 SHA-256，并将其缓存以供后续运行使用。

### `uvx` / `pip`（Python） {#uvx--pip-python}

**适用场景：** Python 环境。这是为 Python 打包的同一启动器，与 npx 启动器共用其填充的二进制文件缓存。

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

或者使用 `pip install stackql-mcp-server` 将其安装到当前环境（Python 3.9+）。该软件包在 PyPI 上为 [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/)。

### Docker {#docker}

**适用场景：** 容器化或以其他方式隔离的运行时。该镜像为多架构镜像（amd64 和 arm64）。

```bash
docker run -i --rm stackql/stackql-mcp
```

作为客户端配置：

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

该镜像在 Docker Hub 上为 [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp)。

### CI 和智能体工作流（GitHub Actions） {#ci-and-agentic-workflows-github-actions}

**适用场景：** 在 GitHub Actions 作业中运行该服务器，让智能体可以在 CI 流程中查询你的云环境，并在你允许时对其执行操作。

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) 安装二进制文件并写入 MCP 配置（默认为 `read_only` 模式）。通过 `claude_args` 将该配置传递给 [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action)：

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

更多智能体使用方案（云审计、成本估算等）请参阅 [action README](https://github.com/stackql/setup-stackql-mcp)。

### 嵌入式 MCP {#embedded-mcp}

**适用场景：** 你正在构建智能体应用，并希望将 MCP 服务器 vendor 到自己的二进制文件中：无需 `npx`，无需单独安装，没有运行时依赖。

原生库在各语言官方 MCP SDK 客户端的背后通过 stdio 启动已签名的 `stackql` 二进制文件，因此你的应用可以通过同一个受治理的 SQL 接口访问注册表中的每个 Provider。每个库默认使用 `read_only`（升级到可写模式始终需要显式指定），并且既可以在首次运行时下载并校验二进制文件，也可以将其 vendor 到你的构建中，得到单个自包含的可执行文件。各语言的完整 API 见[嵌入式 MCP 参考](/mcp/embedded)。

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

`stackql-mcp` crate 在 `rmcp` 客户端的背后启动该二进制文件。默认的 `sidecar` 功能在首次运行时下载并校验二进制文件；`vendored` 功能使用 `include_bytes!` 将其嵌入，得到单个自包含的二进制文件。MSRV 为 Rust 1.88。

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
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上查看" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="在 crates.io 上查看" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

完整参考：[嵌入式 MCP：Rust](/mcp/embedded/rust)。

</TabItem>
<TabItem value="go">

`stackql-mcp-go` 库在官方 Go MCP SDK 客户端的背后启动该二进制文件。可以使用 `go:embed` 将二进制文件 vendor 进来，得到单个自包含的二进制文件，也可以让它在首次运行时下载并校验。默认以 `read_only` 模式启动。

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
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上查看" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="在 pkg.go.dev 上查看" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

完整参考：[嵌入式 MCP：Go](/mcp/embedded/go)。

</TabItem>
<TabItem value="kotlin">

`io.stackql:stackql-mcp` 库在官方 Kotlin MCP SDK 客户端的背后启动该二进制文件，配套的 Gradle 插件可将同样的启动过程接入构建。需要 JDK 17 和 Kotlin 2.x。

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
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上查看" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

完整参考：[嵌入式 MCP：Kotlin / JVM](/mcp/embedded/kotlin)。

</TabItem>
<TabItem value="dotnet">

`StackQL.Mcp` 软件包在官方 C# MCP SDK 客户端的背后启动该二进制文件。默认采用 sidecar 方式，也可以将安装包作为构建资源 vendor 进来，得到自包含的可执行文件。需要 .NET 8 或更高版本。

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
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上查看" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

完整参考：[嵌入式 MCP：.NET / C#](/mcp/embedded/dotnet)。

</TabItem>
<TabItem value="gleam">

`stackql_mcp` 库面向 Erlang/BEAM 运行时，通过 `child_spec()` 将服务器公开为 OTP 子进程，因此可以放入你自己的监督树中。默认以 `read_only` 模式启动。

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
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上查看" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

完整参考：[嵌入式 MCP：Gleam](/mcp/embedded/gleam)。

</TabItem>
<TabItem value="swift">

`stackql-mcp-swift` 软件包在官方 Swift MCP SDK 客户端的背后启动该二进制文件。`darwin-universal` 二进制文件已使用 Developer ID 签名并经过 Apple 公证，因此可以将其打包到已签名的 `.app` 中，并保持应用的公证有效。需要 macOS 13+ 和 Swift 6.1（Xcode 16.3+）。

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
    <BinaryDownloadLink iconSize={20} text="在 GitHub 上查看" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

完整参考：[嵌入式 MCP：Swift](/mcp/embedded/swift)。

</TabItem>
</Tabs>

### 信任模型 {#trust-model}

所有渠道都具备相同的安全属性：

- 嵌入的 `stackql` 二进制文件经过 Authenticode 签名（Windows）和 Apple 公证（macOS）。
- 每个 `.mcpb` 安装包都在发布页面上附有公开的 SHA-256 校验和。
- `npx` 和 `uvx` 启动器在首次使用前校验所下载二进制文件的 SHA-256。
- [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) 条目为各平台的哈希值提供证明。

## 使用 GitHub Actions {#using-github-actions}

[StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo) 可在你的 GitHub Actions 工作流中使用。可用的 action 如下：

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

直接在 GitHub 工作流中使用 StackQL 部署或测试基础设施堆栈。该 action 支持使用类 SQL 语法的基础设施即代码（IaC）工作流，让你可以在单个工作流中定义、部署和管理跨多个 Provider 的云资源。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上查看"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="在 GitHub 上查看"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__用法示例__

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

在 GitHub Actions 工作流中设置 StackQL。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上查看"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="在 GitHub 上查看"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__用法示例__

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

在 GitHub Actions 工作流中安装 StackQL MCP 服务器。该 action 安装已签名的二进制文件并写入 `mcpServers` 配置（默认为 `read_only` 模式），`anthropics/claude-code-action` 等智能体步骤通过 `claude_args` 使用该配置。

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上查看"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub 上查看"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__用法示例__

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

在 GitHub Actions 工作流中执行 StackQL 命令。查询可以内联提供，也可以来自仓库中的文件。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上查看"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="在 GitHub 上查看"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__用法示例__

```yaml
- name: exec github example
  uses: stackql/stackql-exec@v2
  with:
    query: |
      select total_private_repos
      from github.orgs.orgs
      where org = 'stackql'"
  env: 
    STACKQL_GITHUB_USERNAME: ${{  secrets.STACKQL_GITHUB_USERNAME }}
    STACKQL_GITHUB_PASSWORD: ${{  secrets.STACKQL_GITHUB_PASSWORD }}
```

</TabItem>
<TabItem value="assert">

### `stackql-assert` {#stackql-assert}

在 GitHub Actions 工作流中执行单元测试（用于保障、治理或云安全检查）。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="在 GitHub Marketplace 上查看"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="在 GitHub 上查看"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__用法示例__

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


## 其他库 {#other-libraries}

StackQL 提供多种集成方式，可在不同的编程语言和环境中使用。这些库扩展了 StackQL 的功能，便于将云资源查询和管理纳入现有的应用和工作流。  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### `pystackql` Python 软件包 {#pystackql-python-package}

用于在 Python 程序中使用 StackQL 的 Python 封装库。`pystackql` 软件包可在 [__PyPI__](https://pypi.org/project/pystackql/) 上获取，`pystackql` 软件包的文档可通过 [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/) 查阅。要安装 `pystackql` 软件包，请运行以下命令：

```bash
pip install pystackql
```

以下示例展示了 `pystackql` 软件包与 `pandas` 配合使用，运行 StackQL 查询并将结果返回到 `pandas.DataFrame`：

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
