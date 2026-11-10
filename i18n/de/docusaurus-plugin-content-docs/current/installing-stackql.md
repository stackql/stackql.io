---
title: Installation
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "Installieren Sie StackQL auf macOS, Linux, Windows, Docker oder in einer Cloud-Shell und verwenden Sie den StackQL MCP-Server aus Claude Desktop, ChatGPT, Goose und anderen MCP-Clients oder über npx, uvx, pip, Docker und GitHub Actions."
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

Hier finden Sie Anleitungen zur Installation von StackQL auf verschiedenen Plattformen.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Cloud-Shells', value: 'shells', },
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
  
  StackQL ist auf macOS über Homebrew und den `pkg`-Installer verfügbar; sowohl ARM- (M1/Apple Silicon) als auch AMD-Architekturen werden mit einem einzigen Multi-Arch-Installer unterstützt.

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (signierte und notarisierte Binärdatei)', value: 'curl', },
      { label: 'Paket-Download', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Führen Sie zur Installation über Homebrew den folgenden Befehl in Ihrem Terminal aus:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  Um eine signierte und notarisierte ausführbare `stackql`-Binärdatei im aktuellen Verzeichnis zu installieren, führen Sie den folgenden Befehl in Ihrem Terminal aus:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL ist als signierter und notarisierter, interaktiver `pkg`-Installer für macOS verfügbar.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="macOS-PKG herunterladen"
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

  StackQL ist für alle Linux-Architekturen verfügbar.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'Paket-Download', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  Um eine ausführbare `stackql`-Binärdatei für Ihre Linux-Architektur (amd64 oder arm64) im aktuellen Verzeichnis zu installieren, führen Sie den folgenden Befehl in Ihrem Terminal aus:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  Alternativ können Sie die Binärdateien hier herunterladen:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Linux-ZIP (amd64) herunterladen"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Linux-ZIP (arm64) herunterladen"
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

  StackQL ist auf Windows über Chocolatey, ein PowerShell-Installationsskript und den MSI-Installer verfügbar. Native AMD64- (x64) und ARM64-Builds sind als MSI-Installer und als ZIP-Archive mit `stackql.exe` verfügbar. Der AMD64-Build läuft über Emulation auch auf ARM64. Alle ausführbaren Dateien sind mit einem Authenticode-Codesignaturzertifikat signiert.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'MSI/ZIP-Download', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Führen Sie zur Installation über Chocolatey den folgenden Befehl in Ihrem PowerShell- oder `cmd`-Terminal aus:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Um eine mit Authenticode signierte ausführbare Datei im aktuellen Verzeichnis zu installieren, führen Sie Folgendes in einem PowerShell-Terminal aus: 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  Laden Sie unten den signierten `stackql`-Installer oder die ausführbare Datei für Windows herunter. Ein Klick auf einen Download-Button wählt standardmäßig AMD64; über das zugehörige Dropdown-Menü wählen Sie AMD64 oder natives ARM64.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows-MSI herunterladen"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Download Windows MSI (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Download Windows MSI (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows-ZIP herunterladen"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Download Windows ZIP (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Download Windows ZIP (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  StackQL-Builds werden auf [DockerHub](https://hub.docker.com/u/stackql) veröffentlicht.  

  __Docker Hub__  

  Um das StackQL-Container-Image herunterzuladen, führen Sie den folgenden Befehl aus:

  ```bash
  docker pull stackql/stackql
  ```

  Ab `v1.0` basiert das Image auf Ubuntu 26.04 LTS, und eine Variante mit vorinstalliertem Python 3.13 wird mit dem Tag-Suffix `-python3.13` (Alias `-python3`) veröffentlicht, zum Beispiel:

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

`stackql-deploy` ist ein zustandsloses IaC-Framework auf Basis von StackQL. Die vollständige Dokumentation finden Sie in den [__StackQL Deploy Docs__](https://stackql-deploy.io/).

__Linux / macOS__

Lädt die neueste `stackql-deploy`-Binärdatei in einem Schritt herunter und entpackt sie. Der Installer erkennt Ihr Betriebssystem und Ihre Architektur (einschließlich Apple Silicon und Intel über eine universelle macOS-Binärdatei) und ruft automatisch das passende Release-Asset ab.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Lädt die neueste `stackql-deploy`-Binärdatei herunter und entpackt sie in das aktuelle Verzeichnis.
Der Installer erkennt Ihre Plattform und ruft automatisch das passende Release-Asset ab.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Wenn Sie die Rust-Toolchain installiert haben, wird die Binärdatei damit direkt von [__crates.io__](https://crates.io/crates/stackql-deploy) gebaut und installiert.
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

  ## Cloud-Shells

  StackQL kann direkt aus den integrierten Cloud-Shells und Web-Terminals der großen Cloud- und Datenplattformen verwendet werden. Das ermöglicht einen nahtlosen Ablauf, da diese Umgebungen bereits mit der Identität autorisiert sind, mit der Sie angemeldet sind, sodass keine separate Einrichtung der Authentifizierung nötig ist.

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

  AWS CloudShell stellt eine browserbasierte Shell mit vorinstallierter und authentifizierter AWS CLI bereit. Wenn Sie StackQL in AWS CloudShell ausführen, können Sie AWS-Ressourcen ohne zusätzliche Authentifizierungsschritte abfragen und verwalten. Eine ausführliche Anleitung finden Sie in unserem [AWS CloudShell-Tutorial](/quick-starts/aws/aws-cloud-shell).

  Laden Sie zunächst das StackQL-Paket herunter:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  Führen Sie dann das StackQL-Skript für AWS CloudShell aus:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  Dieses Skript startet eine StackQL-Befehlsshell mit Ihren AWS CloudShell-Anmeldedaten, sodass Sie sofort AWS-Ressourcen abfragen können.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell stellt eine über den Browser erreichbare Shell-Umgebung bereit, in der die Azure CLI bereits mit Ihrem Azure-Konto authentifiziert ist. Mit StackQL in Azure Cloud Shell fragen Sie Ihre Azure-Ressourcen nahtlos und ohne zusätzliche Einrichtung ab. Alle Einzelheiten finden Sie in unserer [Anleitung zu Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell).  

  Laden Sie zunächst das StackQL-Paket herunter:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  Führen Sie dann das StackQL-Skript für Azure Cloud Shell aus:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  Dieses Skript konfiguriert automatisch eine StackQL-Sitzung mit Ihren Azure Cloud Shell-Anmeldedaten, sodass Sie Ihre Azure-Ressourcen sofort abfragen können.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell bietet eine Entwicklungs- und Betriebsumgebung, in der die Google Cloud CLI bereits authentifiziert ist. Wenn Sie StackQL in Google Cloud Shell ausführen, fragen Sie GCP-Ressourcen mit Ihrer bestehenden Authentifizierung ab. Mehr dazu erfahren Sie in unserer [Anleitung zu Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell).  

  Laden Sie zunächst das StackQL-Paket herunter:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  Führen Sie dann das StackQL-Skript für Google Cloud Shell aus:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  Das Skript richtet StackQL so ein, dass Ihre Google Cloud Shell-Anmeldedaten verwendet werden, sodass Sie Ihre GCP-Ressourcen sofort mit SQL-Syntax abfragen können.

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web Terminal

  Databricks-Workspaces enthalten ein Web-Terminal, das unter dem angemeldeten Benutzer läuft. Wenn Sie StackQL dort ausführen, fragen Sie Ihren Workspace mit Ihrer Databricks-Identität ab, ohne separate Einrichtung der Authentifizierung. Beispielabfragen und die Authentifizierung auf Account-Ebene finden Sie in unserer [Anleitung zum Databricks Web Terminal](/blog/tutorials/stackql-in-databricks-web-terminal).

  Laden Sie zunächst das StackQL-Paket herunter:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  Führen Sie dann das StackQL-Shell-Skript für Databricks aus:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  Dadurch wird eine StackQL-Sitzung mit Ihrer Databricks-Workspace-Identität gestartet, sodass Sie sofort Ressourcen auf Workspace-Ebene wie `databricks_workspace.iam.vw_user_entitlements` abfragen können. Für Abfragen auf Account-Ebene (Provisionierung, Abrechnung, Account-IAM) setzen Sie die Variablen des Databricks-OAuth2-Service-Principals wie in der Anleitung beschrieben.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Verwendung mit MCP-Clients {#using-with-mcp-clients}

Der StackQL MCP-Server läuft lokal über stdio, sodass die Provider-Anmeldedaten unabhängig vom verwendeten Client auf Ihrem Rechner bleiben. Wählen Sie unten Ihren Client: Jeder Tab nennt zuerst den Weg über das Verzeichnis oder die Ein-Klick-Installation und als Ausweichlösung die manuelle Konfiguration. Alle Vertriebskanäle, einschließlich `npx`, Python, Docker und CI, finden Sie unter [MCP-Server installieren](#installing-the-mcp-server).

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

  Der empfohlene Weg, den StackQL MCP-Server in Claude Desktop zu installieren, führt über das [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Einstellungen -> Connectors -> Hinzufügen -> Connectors durchsuchen**, nach **StackQL** suchen) - Claude Desktop wählt automatisch die passende signierte Binärdatei für Ihre Plattform.  Alternativ sind vorgefertigte MCP-Bundles (`.mcpb`) an jedes StackQL-Release angehängt, um aus einer heruntergeladenen Datei zu installieren - in beiden Fällen ist keine separate StackQL-Installation erforderlich.  Bundles sind für macOS, Windows und Linux verfügbar.

  Die MCPB-Downloads für Linux und Windows verwenden standardmäßig **amd64** (x64). Über die Dropdown-Menüs wählen Sie **amd64** oder **arm64**.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="macOS-MCPB herunterladen"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Linux-MCPB herunterladen"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Download Linux MCPB (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Download Linux MCPB (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows-MCPB herunterladen"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Download Windows MCPB (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Download Windows MCPB (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

Der StackQL MCP-Server ist außerdem in der [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) als `io.github.stackql/stackql-mcp` gelistet.

:::

:::tip

Das Bundle ist eine von mehreren Möglichkeiten, den StackQL MCP-Server auszuführen.  Die vollständige Anleitung für Claude Desktop finden Sie unter [__StackQL mit Claude Desktop verwenden__](/getting-started/claude-desktop), die Optionen npx, Docker, Python, CI und manuelle Konfiguration unter [__MCP-Server installieren__](#installing-the-mcp-server).

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

  ### ChatGPT und Codex

  StackQL wird als lokales Plugin für die ChatGPT-Desktop-App und die Codex CLI ausgeliefert. Das Plugin führt den StackQL MCP-Server über stdio auf Ihrem Rechner aus, sodass Provider-Anmeldedaten diesen nie verlassen. Erforderlich ist Node.js 24.22.1 oder neuer (22.23.4 bzw. 26.12.1 auf diesen Release-Linien); das Plugin benötigt nur die in Node.js integrierten Module, lädt die signierte `stackql`-Binärdatei bei der ersten Verwendung herunter und prüft deren SHA-256.

  **ChatGPT-Desktop-App (Plugin-Verzeichnis):**  
  
  öffnen Sie das Plugin-Verzeichnis in der ChatGPT-Desktop-App, suchen Sie nach **StackQL** und klicken Sie auf **Installieren**, und führen Sie dann das Setup des Plugins aus; der mitgelieferte Skill `stackql-setup` führt durch die Anmeldedaten und eine erste Abfrage. Der Eintrag trägt das Badge **Desktop only**: Das Plugin lässt sich nur aus der Desktop-App installieren, nicht aus ChatGPT Web oder Mobile, und seine Tools stehen im Work-Modus zur Verfügung, in Threads, die auf Ihrem Computer laufen. Wenn der Eintrag in Ihrem Verzeichnis noch nicht sichtbar ist, installieren Sie stattdessen aus dem Repository-Marketplace.

  **ChatGPT-Desktop-App (Repository-Marketplace):**  
  
  öffnen Sie **Einstellungen -> Plugins -> Hinzufügen -> Von GitHub hinzufügen**, geben Sie `stackql/stackql` ein, installieren Sie dann **StackQL** aus dem Abschnitt **Persönlich** des Plugin-Verzeichnisses und starten Sie die App neu.

  **Codex CLI:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  Starten Sie ChatGPT Desktop oder Codex neu und probieren Sie dann: *Verwende StackQL, um bei Bedarf den GitHub-Provider zu installieren, und liste dann die ersten fünf GitHub-Services auf.* Provider-Anmeldedaten gehören in `~/.stackql/.env` unter den Standardnamen der Umgebungsvariablen des jeweiligen Providers (zum Beispiel `AWS_ACCESS_KEY_ID` und `AWS_SECRET_ACCESS_KEY`); bitten Sie den Agenten nach dem Bearbeiten der Datei, `reload_credentials` aufzurufen.

  **Ohne das Plugin:**   
  
  wählen Sie unter **Einstellungen -> Plugins -> MCPs -> Mit einem benutzerdefinierten MCP verbinden** die Option **STDIO**, setzen Sie den Befehl auf `npx` und fügen Sie die Argumente `-y` und `@stackql/mcp-server` hinzu. Geben Sie Provider-Anmeldedaten in diesem Formular als Umgebungsvariablen an.

<br />
:::note

ChatGPT Web und Mobile können keinen lokalen stdio-Server ausführen, daher gibt es das Plugin nur für Desktop und Codex. Ein offener Bug in ChatGPT Desktop ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) kann lokale MCP-Tools in **Chat**-Sitzungen ausblenden; Threads im Work-Modus, Codex-Threads und die Codex CLI sind nicht betroffen.

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

  Goose führt stdio-Erweiterungen direkt aus, daher genügt der `npx`-Launcher (Node.js 18 oder neuer).

  **Goose Desktop:** Klicken Sie auf den Button unten. Goose öffnet sich, zeigt den Befehl an, den es ausführen wird, bittet Sie um Bestätigung und fügt dann StackQL als Erweiterung hinzu.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>In Goose Desktop installieren</a>
  </Box>

  Der Button ist ein `goose://`-Deep-Link; Sie können ihn auch in die Adressleiste Ihres Browsers einfügen oder aus einem Terminal mit `open` (macOS), `start` (Windows) oder `xdg-open` (Linux) öffnen:

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  oder führen Sie `goose configure` aus, wählen Sie **Add Extension -> Command-line Extension** und geben Sie denselben Befehl ein. Geben Sie die Provider-Anmeldedaten als Umgebungsvariablen der Erweiterung an, wenn Sie dazu aufgefordert werden.

<br />
:::note

Goose hat sein In-App-Erweiterungsverzeichnis zugunsten der [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) eingestellt, in der StackQL als `io.github.stackql/stackql-mcp` gelistet ist. Sobald Goose seinen Registry-Import ausliefert, ist StackQL dort ohne weiteres Zutun auffindbar.

:::

</Box>

</TabItem>
</Tabs>

## MCP-Server installieren {#installing-the-mcp-server}

Mit dem StackQL MCP-Server können KI-Agenten Cloud-Ressourcen mit StackQL abfragen und bereitstellen. Er wird über mehrere Kanäle ausgeliefert - alle führen denselben Server aus, wählen Sie also den Kanal, der zu Ihrem Client und Ihren Anforderungen an die Vertrauenswürdigkeit passt. Die folgenden Optionen sind ungefähr nach Vertrauenswürdigkeit und Einfachheit geordnet.

### Marketplaces und Verzeichnisse {#marketplaces-and-directories}

Der Server ist in der Official MCP Registry als `io.github.stackql/stackql-mcp` veröffentlicht; die unten aufgeführten Paket-Registries enthalten die verteilbaren Artefakte, und nachgelagerte Verzeichnisse übernehmen den Eintrag von dort.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Registries', value: 'registries', },
    { label: 'Verzeichnisse', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Registry | Typ | Veröffentlicht über | Eintrag | Hinweise |
|---|---|---|---|---|
| Official MCP Registry | Kanonische Metadaten-Registry | `mcp-publisher` CLI + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | führt alle 8 Pakete auf (`mcpb x5`, `oci`, `npm`, `pypi`) |
| npm | Paket-Registry | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | `npx`-Launcher |
| PyPI | Paket-Registry | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | `uvx`-/`pip`-Launcher |
| Docker Hub | Container-Registry | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | Multi-Arch `amd64` + `arm64` |
| GitHub Actions Marketplace | CI-Marketplace (`setup-stackql-mcp`-Action) | Öffentliches Repository + Release + Marketplace-Eintrag | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Verifizierter Publisher; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Einträge in Verzeichnissen und Aggregatoren, ungefähr nach Bedeutung für die Auffindbarkeit geordnet:

| Verzeichnis | Typ | Eintrag |
|---|---|---|
| __Anthropic Connector Directory__ | In-App-Verzeichnis von Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | In-App-Verzeichnis von ChatGPT Desktop (nur Desktop) | in der ChatGPT-Desktop-App nach **StackQL** suchen; siehe [ChatGPT und Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | IDE-Client-Verzeichnis | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | Größter Aggregator | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Discovery, Unterstützer der Registry | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Durchsuchbarer Marketplace | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Aggregator | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Awesome-List-Website | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Vorgefertigtes `.mcpb`-Bundle {#prebuilt-mcpb-bundle}

**Wann verwenden:** Claude Desktop ohne Zugang zum Connector Directory, oder wenn Sie eine bestimmte Version festlegen oder Prüfsummen vor der Installation selbst prüfen möchten.  Für die meisten Claude Desktop-Benutzer ist das [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) die empfohlene Installationsmethode.

Für jede Plattform ist an jedes StackQL-Release ein vorgefertigtes MCP-Bundle angehängt:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

wobei `<platform>` einer der Werte `darwin-universal`, `windows-x64` (AMD64), `windows-arm64`, `linux-x64` oder `linux-arm64` ist. Download-Buttons für jede Plattform finden Sie im Tab **Claude Desktop** unter [Verwendung mit MCP-Clients](#using-with-mcp-clients). Zu jedem Bundle gibt es eine passende `.sha256`-Prüfsumme auf der [Release-Seite](https://github.com/stackql/stackql/releases/latest). Prüfen Sie diese und installieren Sie dann über **Einstellungen -> Erweiterungen** in Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Die vollständige Anleitung finden Sie unter [StackQL mit Claude Desktop verwenden](/getting-started/claude-desktop#install-from-a-downloaded-bundle).

### Manuelle `claude_desktop_config.json` {#manual-claude_desktop_configjson}

**Wann verwenden:** Sie haben die `stackql`-Binärdatei bereits in Ihrem PATH und verwenden Claude Desktop oder einen anderen stdio-MCP-Client.

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

Alle drei Argumente sind erforderlich:

- `--mcp.server.type=stdio` wählt den stdio-Transport, den in Editoren eingebettete Clients sprechen.
- `--approot` verweist den Provider-Cache auf ein beschreibbares Verzeichnis. MCP-Clients starten den Server unter Umständen mit `/` als Arbeitsverzeichnis, das nicht beschreibbar ist.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` deaktiviert das Audit-Log, das sein Verzeichnis sonst standardmäßig in das (möglicherweise nicht beschreibbare) Arbeitsverzeichnis legt.

Fügen Sie Provider-Anmeldedaten mit einem `"env"`-Block hinzu und passen Sie den Sicherheitsvertrag mit `"mode"` an - siehe [Server-Modi](/command-line-usage/mcp#server-modes).

### `npx` (ohne Installation) {#npx-no-install}

**Wann verwenden:** jede Node-Umgebung, wenn Sie keine globale StackQL-Installation möchten.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

Der Launcher [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) lädt die signierte `stackql`-Binärdatei beim ersten Start herunter, prüft deren SHA-256 und legt sie für spätere Starts im Cache ab.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**Wann verwenden:** eine Python-Umgebung - derselbe Launcher, für Python paketiert, der den Binärdatei-Cache des npx-Launchers mitbenutzt.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

Oder installieren Sie ihn mit `pip install stackql-mcp-server` (Python 3.9+) in die aktuelle Umgebung. Das Paket heißt [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) auf PyPI.

### Docker {#docker}

**Wann verwenden:** eine containerisierte oder anderweitig isolierte Laufzeitumgebung. Das Image ist Multi-Arch (amd64 und arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

Als Client-Konfiguration:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

Das Image heißt [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) auf Docker Hub.

### CI und agentische Workflows (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**Wann verwenden:** Sie führen den Server in einem GitHub Actions-Job aus, damit ein Agent Ihre Cloud im Rahmen von CI abfragen - und, wenn Sie es erlauben, verändern - kann.

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) installiert die Binärdatei und schreibt eine MCP-Konfiguration (standardmäßig im Modus `read_only`). Übergeben Sie diese Konfiguration über `claude_args` an [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action):

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

Weitere agentische Rezepte (Cloud-Audits, Kostenschätzungen und so weiter) finden Sie in der [README der Action](https://github.com/stackql/setup-stackql-mcp).

### Eingebettetes MCP {#embedded-mcp}

**Wann verwenden:** Sie entwickeln eine agentische App und möchten den MCP-Server in Ihre eigene Binärdatei einbetten - kein `npx`, keine separate Installation, keine Laufzeitabhängigkeit.

Native Bibliotheken starten die signierte `stackql`-Binärdatei über stdio hinter dem offiziellen MCP-SDK-Client der jeweiligen Sprache, sodass Ihre App dieselbe kontrollierte SQL-Schnittstelle zu jedem Provider in der Registry erhält. Jede Bibliothek verwendet standardmäßig `read_only` - der Wechsel in einen schreibenden Modus ist immer explizit - und kann die Binärdatei entweder beim ersten Start herunterladen und prüfen oder sie für eine einzelne eigenständige ausführbare Datei in Ihren Build einbetten. Die vollständige API je Sprache finden Sie in der [Referenz zum eingebetteten MCP](/mcp/embedded).

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

Das Crate `stackql-mcp` startet die Binärdatei hinter einem `rmcp`-Client. Das Standard-Feature `sidecar` lädt die Binärdatei beim ersten Start herunter und prüft sie; das Feature `vendored` bettet sie mit `include_bytes!` für eine einzelne eigenständige Binärdatei ein. MSRV Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="Auf GitHub ansehen" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Auf crates.io ansehen" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Vollständige Referenz: [Eingebettetes MCP: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

Die Bibliothek `stackql-mcp-go` startet die Binärdatei hinter dem offiziellen Go-MCP-SDK-Client. Betten Sie die Binärdatei mit `go:embed` für eine einzelne eigenständige Binärdatei ein, oder lassen Sie sie beim ersten Start herunterladen und prüfen. Startet standardmäßig im Modus `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Auf GitHub ansehen" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Auf pkg.go.dev ansehen" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Vollständige Referenz: [Eingebettetes MCP: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

Die Bibliothek `io.stackql:stackql-mcp` startet die Binärdatei hinter dem offiziellen Kotlin-MCP-SDK-Client, und ein begleitendes Gradle-Plugin bindet denselben Start in einen Build ein. Erfordert JDK 17 und Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="Auf GitHub ansehen" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Vollständige Referenz: [Eingebettetes MCP: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

Das Paket `StackQL.Mcp` startet die Binärdatei hinter dem offiziellen C#-MCP-SDK-Client. Standardmäßig als Sidecar, oder Sie betten das Bundle als Build-Ressource für eine eigenständige ausführbare Datei ein. Erfordert .NET 8 oder neuer.

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
    <BinaryDownloadLink iconSize={20} text="Auf GitHub ansehen" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Vollständige Referenz: [Eingebettetes MCP: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

Die Bibliothek `stackql_mcp` zielt auf die Erlang/BEAM-Laufzeitumgebung und stellt den Server über `child_spec()` als OTP-Child bereit, sodass er in Ihrem eigenen Supervision-Tree laufen kann. Startet standardmäßig im Modus `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Auf GitHub ansehen" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Vollständige Referenz: [Eingebettetes MCP: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

Das Paket `stackql-mcp-swift` startet die Binärdatei hinter dem offiziellen Swift-MCP-SDK-Client. Die `darwin-universal`-Binärdatei ist mit Developer ID signiert und von Apple notarisiert, sodass Sie sie in eine signierte `.app` einbetten können und die Notarisierung der App gültig bleibt. Erfordert macOS 13+ und Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="Auf GitHub ansehen" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Vollständige Referenz: [Eingebettetes MCP: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### Vertrauensmodell {#trust-model}

Für jeden Kanal gelten dieselben Sicherheitseigenschaften:

- Die eingebettete `stackql`-Binärdatei ist mit Authenticode signiert (Windows) und von Apple notarisiert (macOS).
- Jedes `.mcpb`-Bundle wird mit einer veröffentlichten SHA-256-Prüfsumme auf der Release-Seite ausgeliefert.
- Die Launcher `npx` und `uvx` prüfen den SHA-256 der heruntergeladenen Binärdatei vor der ersten Verwendung.
- Der Eintrag in der [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) bestätigt die Hashes je Plattform.

## GitHub Actions verwenden {#using-github-actions}

[StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo) stehen für Ihre GitHub Actions-Workflows zur Verfügung. Die folgenden Actions sind verfügbar:

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

Stellen Sie Infrastruktur-Stacks mit StackQL direkt aus Ihren GitHub-Workflows bereit oder testen Sie sie. Diese Action ermöglicht Infrastructure as Code (IaC)-Workflows mit SQL-ähnlicher Syntax, sodass Sie Cloud-Ressourcen über mehrere Provider hinweg in einem einzigen Workflow definieren, bereitstellen und verwalten können.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Im GitHub Marketplace ansehen"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Auf GitHub ansehen"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Anwendungsbeispiel__

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

Richten Sie StackQL in Ihrem GitHub Actions-Workflow ein.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Im GitHub Marketplace ansehen"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Auf GitHub ansehen"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Anwendungsbeispiel__

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

Installieren Sie den StackQL MCP-Server in Ihrem GitHub Actions-Workflow.  Die Action installiert die signierte Binärdatei und schreibt eine `mcpServers`-Konfiguration (standardmäßig im Modus `read_only`), die ein agentischer Schritt wie `anthropics/claude-code-action` über `claude_args` verwendet.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Im GitHub Marketplace ansehen"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Auf GitHub ansehen"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Anwendungsbeispiel__

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

Führen Sie StackQL-Befehle in Ihrem GitHub Actions-Workflow aus.  Abfragen können inline oder aus einer Datei im Repository übergeben werden.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Im GitHub Marketplace ansehen"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Auf GitHub ansehen"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Anwendungsbeispiel__

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

Führen Sie Unit-Tests in Ihrem GitHub Actions-Workflow aus (für Assurance-, Governance- oder Cloud-Sicherheitsprüfungen).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Im GitHub Marketplace ansehen"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Auf GitHub ansehen"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Anwendungsbeispiel__

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


## Weitere Bibliotheken {#other-libraries}

StackQL bietet mehrere Integrationsmöglichkeiten für die Verwendung in verschiedenen Programmiersprachen und Umgebungen. Diese Bibliotheken erweitern die Funktionalität von StackQL und erleichtern es, die Abfrage und Verwaltung von Cloud-Ressourcen in Ihre bestehenden Anwendungen und Workflows einzubinden.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### `pystackql`-Python-Paket {#pystackql-python-package}

Python-Wrapper zur Verwendung von StackQL in Ihren Python-Programmen. Das Paket `pystackql` ist auf [__PyPi__](https://pypi.org/project/pystackql/) verfügbar, die Dokumentation zum Paket `pystackql` finden Sie auf [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). Um das Paket `pystackql` zu installieren, führen Sie den folgenden Befehl aus:

```bash
pip install pystackql
```

Das folgende Beispiel zeigt das Paket `pystackql` zusammen mit `pandas`, um StackQL-Abfragen auszuführen und die Ergebnisse in einen `pandas.DataFrame` zu übernehmen:

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
