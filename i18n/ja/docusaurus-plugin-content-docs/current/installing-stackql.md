---
title: インストール
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "StackQL を macOS、Linux、Windows、Docker、クラウドシェルにインストールし、Claude Desktop、ChatGPT、Goose などの MCP クライアントや npx、uvx、pip、Docker、GitHub Actions から StackQL MCP サーバーを使用します。"
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

このページでは、さまざまなプラットフォームに StackQL をインストールする手順を説明します。

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'クラウドシェル', value: 'shells', },
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
  
  StackQL は macOS で Homebrew と `pkg` インストーラーから利用できます。ARM（M1/Apple Silicon）と AMD の両アーキテクチャが、単一のマルチアーキテクチャインストーラーでサポートされています。

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl（署名済みかつ公証済みのバイナリ）', value: 'curl', },
      { label: 'パッケージのダウンロード', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Homebrew でインストールするには、ターミナルで次のコマンドを実行します。

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  署名済みかつ公証済みの `stackql` 実行可能バイナリをカレントディレクトリにインストールするには、ターミナルで次のコマンドを実行します。

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL は、macOS 向けの署名済みかつ公証済みの対話型 `pkg` インストーラーとして利用できます。

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="macOS PKG をダウンロード"
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

  StackQL はすべての Linux アーキテクチャで利用できます。

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'パッケージのダウンロード', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  使用している Linux アーキテクチャ（amd64 または arm64）用の `stackql` 実行可能バイナリをカレントディレクトリにインストールするには、ターミナルで次のコマンドを実行します。

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  または、ここからバイナリをダウンロードできます。

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Linux ZIP（amd64）をダウンロード"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Linux ZIP（arm64）をダウンロード"
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

  StackQL は Windows で Chocolatey、PowerShell インストールスクリプト、MSI インストーラーから利用できます。ネイティブの AMD64（x64）ビルドと ARM64 ビルドが、MSI インストーラーと、`stackql.exe` を含む ZIP アーカイブとして提供されています。AMD64 ビルドはエミュレーションにより ARM64 でも動作します。すべての実行ファイルは Authenticode コード署名証明書で署名されています。

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'MSI/ZIP のダウンロード', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Chocolatey でインストールするには、PowerShell または `cmd` のターミナルで次のコマンドを実行します。

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Authenticode で署名された実行ファイルをカレントディレクトリにインストールするには、PowerShell ターミナルで次のコマンドを実行します。 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  署名済みの Windows 用 `stackql` インストーラーまたは実行ファイルを以下からダウンロードします。ダウンロードボタンをクリックすると、デフォルトで AMD64 が選択されます。AMD64 とネイティブ ARM64 のどちらかを選ぶには、ボタンのドロップダウンを使用します。

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows MSI をダウンロード"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Windows MSI（amd64）をダウンロード', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Windows MSI（arm64）をダウンロード', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows ZIP をダウンロード"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Windows ZIP（amd64）をダウンロード', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Windows ZIP（arm64）をダウンロード', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  StackQL のビルドは [Docker Hub](https://hub.docker.com/u/stackql) で公開されています。  

  __Docker Hub__  

  StackQL のコンテナーイメージをプルするには、次のコマンドを実行します。

  ```bash
  docker pull stackql/stackql
  ```

  `v1.0` 以降、イメージは Ubuntu 26.04 LTS をベースにしています。また、Python 3.13 がプリインストールされたバリアントが `-python3.13` タグサフィックス（エイリアス `-python3`）付きで公開されています。たとえば次のとおりです。

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

`stackql-deploy` は、StackQL 上に構築されたステートレスな IaC フレームワークです。完全なドキュメントについては、[__StackQL Deploy ドキュメント__](https://stackql-deploy.io/)を参照してください。

__Linux / macOS__

最新の `stackql-deploy` バイナリを 1 回の手順でダウンロードして展開します。インストーラーは OS とアーキテクチャ（ユニバーサル macOS バイナリによる Apple Silicon と Intel を含む）を検出し、適切なリリースアセットを自動的に取得します。
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

最新の `stackql-deploy` バイナリをカレントディレクトリにダウンロードして展開します。
インストーラーはプラットフォームを検出し、適切なリリースアセットを自動的に取得します。
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Rust ツールチェーンがインストールされている場合は、次のコマンドで [__crates.io__](https://crates.io/crates/stackql-deploy) から直接バイナリをビルドしてインストールします。
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

  ## クラウドシェル

  StackQL は、主要なクラウドプラットフォームとデータプラットフォームに組み込まれたクラウドシェルや Web ターミナルから直接使用できます。これらの環境はログイン中の ID で事前に認可されているため、個別に認証を設定する必要がなく、シームレスに使用できます。

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

  AWS CloudShell は、AWS CLI がプリインストールされ認証済みの、ブラウザーベースのシェルを提供します。AWS CloudShell で StackQL を実行すると、追加の認証手順なしで AWS リソースをクエリおよび管理できます。詳しい手順については、[AWS CloudShell チュートリアル](/quick-starts/aws/aws-cloud-shell)を参照してください。

  まず、StackQL パッケージをダウンロードします。

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  次に、StackQL AWS CloudShell スクリプトを実行します。
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  このスクリプトは AWS CloudShell の認証情報を使用して StackQL のコマンドシェルを起動するため、すぐに AWS リソースのクエリを開始できます。

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell は、Azure CLI が Azure アカウントで事前に認証された、ブラウザーからアクセスできるシェル環境を提供します。Azure Cloud Shell で StackQL を使用すると、追加の設定なしで Azure リソースをシームレスにクエリできます。詳細については、[Azure Cloud Shell ガイド](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell)を参照してください。  

  まず、StackQL パッケージをダウンロードします。

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  次に、StackQL Azure Cloud Shell スクリプトを実行します。

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  このスクリプトは Azure Cloud Shell の認証情報を使用するように StackQL セッションを自動的に構成するため、すぐに Azure リソースをクエリできます。

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell は、Google Cloud CLI が認証済みの状態で使える開発および運用環境を提供します。Google Cloud Shell で StackQL を実行すると、既存の認証を使用して GCP リソースをクエリできます。詳しくは、[Google Cloud Shell ガイド](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell)を参照してください。  

  まず、StackQL パッケージをダウンロードします。
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  次に、StackQL Google Cloud Shell スクリプトを実行します。

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  このスクリプトは Google Cloud Shell の認証情報を使用するように StackQL を設定するため、すぐに SQL 構文で GCP リソースのクエリを開始できます。

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web ターミナル

  Databricks ワークスペースには、ログイン中のユーザーとして動作する Web ターミナルが含まれています。そこで StackQL を実行すると、個別に認証を設定することなく、Databricks の ID でワークスペースをクエリできます。クエリの例とアカウントレベルの認証については、[Databricks Web ターミナルガイド](/blog/tutorials/stackql-in-databricks-web-terminal)を参照してください。

  まず、StackQL パッケージをダウンロードします。

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  次に、StackQL Databricks シェルスクリプトを実行します。

  ```bash
  ./stackql-databricks-shell.sh
  ```

  これにより Databricks ワークスペースの ID を使用して StackQL セッションが開始されるため、`databricks_workspace.iam.vw_user_entitlements` などのワークスペーススコープのリソースをすぐにクエリできます。アカウントレベルのクエリ（プロビジョニング、課金、アカウント IAM）を実行するには、ガイドの説明に従って Databricks OAuth2 サービスプリンシパルの変数を設定してください。

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## MCP クライアントでの使用 {#using-with-mcp-clients}

StackQL MCP サーバーは stdio 経由でローカルに実行されるため、どのクライアントを使用してもプロバイダーの認証情報はマシン上にとどまります。以下で使用するクライアントを選択してください。各タブでは、まずディレクトリまたはワンクリックでのインストール方法を示し、代替手段として手動での構成を示します。`npx`、Python、Docker、CI を含むすべての配布チャネルについては、[MCP サーバーのインストール](#installing-the-mcp-server)を参照してください。

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

  StackQL MCP サーバーを Claude Desktop にインストールする推奨の方法は、[Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) からのインストールです（**Settings -> Connectors -> Add -> Browse Connectors** を開き、**StackQL** を検索します）。Claude Desktop がプラットフォームに適した署名済みバイナリを自動的に選択します。また、ダウンロードしたファイルからインストールできるように、ビルド済みの MCP Bundle（`.mcpb`）がすべての StackQL リリースに添付されています。どちらの方法でも、StackQL を別途インストールする必要はありません。バンドルは macOS、Windows、Linux 向けに提供されています。

  Linux と Windows の MCPB ダウンロードのデフォルトは **amd64**（x64）です。**amd64** または **arm64** を選択するには、ドロップダウンを使用します。
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="macOS MCPB をダウンロード"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Linux MCPB をダウンロード"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Linux MCPB（amd64）をダウンロード', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Linux MCPB（arm64）をダウンロード', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows MCPB をダウンロード"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Windows MCPB（amd64）をダウンロード', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Windows MCPB（arm64）をダウンロード', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

StackQL MCP サーバーは、[__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) にも `io.github.stackql/stackql-mcp` として掲載されています。

:::

:::tip

バンドルは、StackQL MCP サーバーを実行するいくつかの方法の 1 つです。Claude Desktop の詳しい手順については [__Claude Desktop で StackQL を使用する__](/getting-started/claude-desktop)を、npx、Docker、Python、CI、手動の各オプションについては [__MCP サーバーのインストール__](#installing-the-mcp-server)を参照してください。

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

  ### ChatGPT と Codex

  StackQL は、ChatGPT デスクトップアプリと Codex CLI 向けのローカルプラグインとして提供されています。プラグインはマシン上で stdio 経由で StackQL MCP サーバーを実行するため、プロバイダーの認証情報がマシンの外に出ることはありません。Node.js 24.22.1 以降が必要です（該当するリリースラインでは 22.23.4 または 26.12.1）。プラグインに必要なのは Node.js の組み込みモジュールだけで、初回使用時に署名済みの `stackql` バイナリをダウンロードし、その SHA-256 を検証します。

  **ChatGPT デスクトップアプリ（プラグインディレクトリ）**：  
  
  ChatGPT デスクトップアプリでプラグインディレクトリを開き、**StackQL** を検索して **Install** をクリックしてから、プラグインのセットアップを実行します。同梱の `stackql-setup` スキルが、認証情報の設定と最初のクエリを順に案内します。掲載ページには **Desktop only** バッジが付いています。プラグインはデスクトップアプリからのみインストールでき、ChatGPT の Web 版やモバイル版からはインストールできません。また、そのツールは Work モードの、コンピューター上で実行されるスレッドで利用できます。ディレクトリにまだ掲載ページが表示されていない場合は、代わりにリポジトリマーケットプレイスからインストールしてください。

  **ChatGPT デスクトップアプリ（リポジトリマーケットプレイス）**：  
  
  **Settings -> Plugins -> Add -> Add from GitHub** を開き、`stackql/stackql` を入力してから、プラグインディレクトリの **Personal** セクションから **StackQL** をインストールし、アプリを再起動します。

  **Codex CLI**：

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  ChatGPT デスクトップまたは Codex を再起動し、次を試します：*StackQL を使って、必要なら GitHub プロバイダーをインストールしてから、最初の 5 つの GitHub サービスを一覧表示して。* プロバイダーの認証情報は、各プロバイダーの標準の環境変数名（たとえば `AWS_ACCESS_KEY_ID` と `AWS_SECRET_ACCESS_KEY`）を使用して `~/.stackql/.env` に記述します。ファイルを編集したら、`reload_credentials` を呼び出すようにエージェントに依頼します。

  **プラグインを使用しない場合**：   
  
  **Settings -> Plugins -> MCPs -> Connect to a custom MCP** で **STDIO** を選択し、コマンドを `npx` に設定して、引数 `-y` と `@stackql/mcp-server` を追加します。プロバイダーの認証情報は、そのフォームで環境変数として指定します。

<br />
:::note

ChatGPT の Web 版とモバイル版はローカルの stdio サーバーを実行できないため、プラグインはデスクトップアプリと Codex 専用です。ChatGPT デスクトップの未解決のバグ（[openai/codex#38162](https://github.com/openai/codex/issues/38162)）により、**Chat** セッションからローカルの MCP ツールが見えなくなることがあります。Work モードのスレッド、Codex のスレッド、Codex CLI は影響を受けません。

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

  Goose は stdio 拡張機能を直接実行するため、必要なのは `npx` ランチャーだけです（Node.js 18 以降）。

  **Goose Desktop**：下のボタンをクリックします。Goose が開き、実行しようとしているコマンドを表示して確認を求めたあと、StackQL を拡張機能として追加します。

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Goose Desktop にインストール</a>
  </Box>

  このボタンは `goose://` ディープリンクです。ブラウザーのアドレスバーに貼り付けるか、ターミナルから `open`（macOS）、`start`（Windows）、`xdg-open`（Linux）で開くこともできます。

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI**：

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  または、`goose configure` を実行し、**Add Extension -> Command-line Extension** を選択して、同じコマンドを入力します。入力を求められたら、プロバイダーの認証情報を拡張機能の環境変数として指定します。

<br />
:::note

Goose はアプリ内の拡張機能ディレクトリを廃止し、[Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) に移行しました。StackQL はそこに `io.github.stackql/stackql-mcp` として掲載されています。Goose がレジストリのインポート機能をリリースすれば、追加の操作なしでそこから StackQL を見つけられるようになります。

:::

</Box>

</TabItem>
</Tabs>

## MCP サーバーのインストール {#installing-the-mcp-server}

StackQL MCP サーバーを使用すると、AI エージェントが StackQL でクラウドリソースをクエリおよびプロビジョニングできます。サーバーは複数のチャネルで配布されています。どのチャネルでも同じサーバーが動作するため、クライアントと信頼性の要件に合ったチャネルを選択してください。以下のオプションは、おおよそ信頼性と手軽さの順に並んでいます。

### マーケットプレイスとディレクトリ {#marketplaces-and-directories}

サーバーは Official MCP Registry に `io.github.stackql/stackql-mcp` として公開されています。以下のパッケージレジストリが配布用のアーティファクトをホストし、下流のディレクトリはそこから掲載情報を取得します。  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'レジストリ', value: 'registries', },
    { label: 'ディレクトリ', value: 'directories', },
  ]}
>
<TabItem value="registries">

| レジストリ | 種類 | 公開方法 | 掲載ページ | 備考 |
|---|---|---|---|---|
| Official MCP Registry | 正規のメタデータレジストリ | `mcp-publisher` CLI + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | 8 つのパッケージすべてを公開（`mcpb x5`、`oci`、`npm`、`pypi`） |
| npm | パッケージレジストリ | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | `npx` ランチャー |
| PyPI | パッケージレジストリ | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | `uvx` / `pip` ランチャー |
| Docker Hub | コンテナーレジストリ | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | マルチアーキテクチャ `amd64` + `arm64` |
| GitHub Actions Marketplace | CI マーケットプレイス（`setup-stackql-mcp` アクション） | パブリックリポジトリ + リリース + マーケットプレイスの掲載ページ | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | 検証済みパブリッシャー、[`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

ディレクトリとアグリゲーターの掲載ページを、発見されやすさに対する重要度のおおよその順に示します。

| ディレクトリ | 種類 | 掲載ページ |
|---|---|---|
| __Anthropic Connector Directory__ | Claude Desktop のアプリ内ディレクトリ | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | ChatGPT デスクトップのアプリ内ディレクトリ（デスクトップのみ） | ChatGPT デスクトップアプリで **StackQL** を検索。[ChatGPT と Codex](#using-with-mcp-clients) を参照 |
| __Cursor Directory__ | IDE クライアントのディレクトリ | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | 最大のアグリゲーター | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | ディスカバリ、レジストリの支援元 | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | 検索可能なマーケットプレイス | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | アグリゲーター | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Awesome リスト形式のサイト | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### ビルド済みの `.mcpb` バンドル {#prebuilt-mcpb-bundle}

**使用場面**：Connector Directory にアクセスできない Claude Desktop の場合、または特定のバージョンに固定したい場合や、インストール前にチェックサムを自分で検証したい場合。ほとんどの Claude Desktop ユーザーには、[Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) が推奨のインストール方法です。

すべての StackQL リリースには、プラットフォームごとにビルド済みの MCP Bundle が添付されています。

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

`<platform>` は `darwin-universal`、`windows-x64`（AMD64）、`windows-arm64`、`linux-x64`、`linux-arm64` のいずれかです。各プラットフォームのダウンロードボタンは、[MCP クライアントでの使用](#using-with-mcp-clients)の **Claude Desktop** タブにあります。各バンドルには、対応する `.sha256` チェックサムが[リリースページ](https://github.com/stackql/stackql/releases/latest)にあります。チェックサムを検証してから、Claude Desktop の **Settings -> Extensions** からインストールします。

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

詳しい手順については、[Claude Desktop で StackQL を使用する](/getting-started/claude-desktop#install-from-a-downloaded-bundle)を参照してください。

### `claude_desktop_config.json` の手動設定 {#manual-claude_desktop_configjson}

**使用場面**：`stackql` バイナリがすでに PATH 上にあり、Claude Desktop またはその他の stdio MCP クライアントを使用している場合。

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

3 つの引数はいずれも不可欠です。

- `--mcp.server.type=stdio` は、エディターに組み込まれたクライアントが使用する stdio トランスポートを選択します。
- `--approot` は、プロバイダーキャッシュの場所を書き込み可能なディレクトリに指定します。MCP クライアントは、作業ディレクトリを `/` に設定した状態でサーバーを起動する場合があり、このディレクトリは書き込みできません。
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` は監査ログを無効にします。指定しない場合、監査ログのディレクトリはデフォルトで（書き込みできない可能性のある）作業ディレクトリになります。

プロバイダーの認証情報は `"env"` ブロックで追加し、安全性のコントラクトは `"mode"` で調整します。[サーバーモード](/command-line-usage/mcp#server-modes)を参照してください。

### `npx`（インストール不要） {#npx-no-install}

**使用場面**：任意の Node 環境で、StackQL をグローバルにインストールしたくない場合。

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

[`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) ランチャーは、初回実行時に署名済みの `stackql` バイナリをダウンロードし、その SHA-256 を検証して、以降の実行のためにキャッシュします。

### `uvx` / `pip`（Python） {#uvx--pip-python}

**使用場面**：Python 環境の場合。Python 向けにパッケージ化された同じランチャーで、npx ランチャーが作成するバイナリキャッシュを共有します。

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

または、`pip install stackql-mcp-server`（Python 3.9 以降）で現在の環境にインストールします。パッケージは PyPI の [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) です。

### Docker {#docker}

**使用場面**：コンテナー化された、またはその他の方法で分離されたランタイムの場合。イメージはマルチアーキテクチャ（amd64 と arm64）です。

```bash
docker run -i --rm stackql/stackql-mcp
```

クライアント構成として指定する場合は次のとおりです。

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

イメージは Docker Hub の [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) です。

### CI とエージェント型ワークフロー（GitHub Actions） {#ci-and-agentic-workflows-github-actions}

**使用場面**：GitHub Actions のジョブ内でサーバーを実行し、CI の一環としてエージェントがクラウドをクエリできるようにする場合（許可した場合はクラウドに対する操作も可能）。

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) はバイナリをインストールし、MCP 構成（デフォルトは `read_only` モード）を書き込みます。その構成を `claude_args` 経由で [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) に渡します。

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

その他のエージェント型のレシピ（クラウド監査、コスト見積もりなど）については、[アクションの README](https://github.com/stackql/setup-stackql-mcp) を参照してください。

### 組み込み MCP {#embedded-mcp}

**使用場面**：エージェント型アプリを構築していて、MCP サーバーを独自のバイナリにベンダリングしたい場合（`npx` 不要、個別のインストール不要、ランタイム依存関係なし）。

ネイティブライブラリは、各言語の公式 MCP SDK クライアントの背後で、署名済みの `stackql` バイナリを stdio 経由で起動します。そのため、アプリはレジストリ内のすべてのプロバイダーに対して、同じガバナンスの効いた SQL インターフェイスを利用できます。各ライブラリのデフォルトは `read_only` で、書き込み可能なモードへの昇格は常に明示的に行います。また、初回実行時にバイナリをダウンロードして検証することも、ビルドにベンダリングして単一の自己完結型実行ファイルにすることもできます。言語ごとの API の詳細は、[組み込み MCP リファレンス](/mcp/embedded)にあります。

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

`stackql-mcp` クレートは、`rmcp` クライアントの背後でバイナリを起動します。デフォルトの `sidecar` 機能は、初回実行時にバイナリをダウンロードして検証します。`vendored` 機能は `include_bytes!` でバイナリを組み込み、単一の自己完結型バイナリにします。MSRV は Rust 1.88 です。

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
    <BinaryDownloadLink iconSize={20} text="GitHub で表示" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="crates.io で表示" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

詳細なリファレンス：[組み込み MCP：Rust](/mcp/embedded/rust)。

</TabItem>
<TabItem value="go">

`stackql-mcp-go` ライブラリは、公式の Go MCP SDK クライアントの背後でバイナリを起動します。`go:embed` でバイナリをベンダリングして単一の自己完結型バイナリにすることも、初回実行時にダウンロードして検証させることもできます。デフォルトでは `read_only` モードで起動します。

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
    <BinaryDownloadLink iconSize={20} text="GitHub で表示" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="pkg.go.dev で表示" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

詳細なリファレンス：[組み込み MCP：Go](/mcp/embedded/go)。

</TabItem>
<TabItem value="kotlin">

`io.stackql:stackql-mcp` ライブラリは、公式の Kotlin MCP SDK クライアントの背後でバイナリを起動します。また、付属の Gradle プラグインが同じ起動処理をビルドに組み込みます。JDK 17 と Kotlin 2.x が必要です。

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
    <BinaryDownloadLink iconSize={20} text="GitHub で表示" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

詳細なリファレンス：[組み込み MCP：Kotlin / JVM](/mcp/embedded/kotlin)。

</TabItem>
<TabItem value="dotnet">

`StackQL.Mcp` パッケージは、公式の C# MCP SDK クライアントの背後でバイナリを起動します。デフォルトはサイドカー方式ですが、バンドルをビルドリソースとしてベンダリングし、自己完結型の実行ファイルにすることもできます。.NET 8 以降が必要です。

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
    <BinaryDownloadLink iconSize={20} text="GitHub で表示" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

詳細なリファレンス：[組み込み MCP：.NET / C#](/mcp/embedded/dotnet)。

</TabItem>
<TabItem value="gleam">

`stackql_mcp` ライブラリは Erlang/BEAM ランタイムを対象とし、`child_spec()` を介してサーバーを OTP の子プロセスとして公開するため、独自のスーパービジョンツリーに組み込めます。デフォルトでは `read_only` モードで起動します。

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
    <BinaryDownloadLink iconSize={20} text="GitHub で表示" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

詳細なリファレンス：[組み込み MCP：Gleam](/mcp/embedded/gleam)。

</TabItem>
<TabItem value="swift">

`stackql-mcp-swift` パッケージは、公式の Swift MCP SDK クライアントの背後でバイナリを起動します。`darwin-universal` バイナリは Developer ID で署名され、Apple の公証を受けているため、署名済みの `.app` 内にバンドルしても、アプリの公証は有効なままです。macOS 13 以降と Swift 6.1（Xcode 16.3 以降）が必要です。

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
    <BinaryDownloadLink iconSize={20} text="GitHub で表示" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

詳細なリファレンス：[組み込み MCP：Swift](/mcp/embedded/swift)。

</TabItem>
</Tabs>

### 信頼モデル {#trust-model}

どのチャネルでも同じセキュリティ特性が成り立ちます。

- 組み込まれた `stackql` バイナリは Authenticode 署名済み（Windows）、Apple 公証済み（macOS）です。
- すべての `.mcpb` バンドルには、リリースページで公開されている SHA-256 チェックサムがあります。
- `npx` と `uvx` のランチャーは、初回使用前にダウンロードしたバイナリの SHA-256 を検証します。
- [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) のエントリは、プラットフォームごとのハッシュを証明します。

## GitHub Actions の使用 {#using-github-actions}

[StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo) は、GitHub Actions のワークフローで使用できます。次のアクションを利用できます。

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

GitHub のワークフローから直接 StackQL を使用して、インフラストラクチャのスタックをデプロイまたはテストします。このアクションにより、SQL に似た構文を使用する Infrastructure as Code（IaC）ワークフローが可能になり、1 つのワークフローで複数のプロバイダーにまたがるクラウドリソースを定義、デプロイ、管理できます。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace で表示"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="GitHub で表示"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__使用例__

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

GitHub Actions のワークフローで StackQL をセットアップします。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace で表示"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="GitHub で表示"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__使用例__

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

GitHub Actions のワークフローに StackQL MCP サーバーをインストールします。このアクションは署名済みのバイナリをインストールし、`mcpServers` 構成（デフォルトは `read_only` モード）を書き込みます。この構成は、`anthropics/claude-code-action` などのエージェント型ステップが `claude_args` 経由で使用します。

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace で表示"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub で表示"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__使用例__

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

GitHub Actions のワークフローで StackQL コマンドを実行します。クエリはインラインで指定することも、リポジトリ内のファイルから指定することもできます。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace で表示"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="GitHub で表示"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__使用例__

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

GitHub Actions のワークフローで単体テストを実行します（保証、ガバナンス、クラウドセキュリティのチェック向け）。  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace で表示"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="GitHub で表示"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__使用例__

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


## その他のライブラリ {#other-libraries}

StackQL には、さまざまなプログラミング言語や環境で使用できる複数の統合方法が用意されています。これらのライブラリは StackQL の機能を拡張し、クラウドリソースのクエリと管理を既存のアプリケーションやワークフローに簡単に組み込めるようにします。  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### `pystackql` Python パッケージ {#pystackql-python-package}

Python プログラムで StackQL を使用するための Python ラッパーです。`pystackql` パッケージは [__PyPI__](https://pypi.org/project/pystackql/) で入手でき、`pystackql` パッケージのドキュメントは [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/) で参照できます。`pystackql` パッケージをインストールするには、次のコマンドを実行します。

```bash
pip install pystackql
```

次の例では、`pystackql` パッケージを `pandas` と組み合わせて使用し、StackQL クエリを実行して結果を `pandas.DataFrame` に返します。

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
