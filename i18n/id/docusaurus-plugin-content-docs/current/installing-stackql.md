---
title: Instalasi
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "Instal StackQL di macOS, Linux, Windows, Docker, atau shell cloud, lalu gunakan server MCP StackQL dari Claude Desktop, ChatGPT, Goose, dan klien MCP lain, atau melalui npx, uvx, pip, Docker, dan GitHub Actions."
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

Halaman ini berisi petunjuk untuk menginstal StackQL di berbagai platform.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Shell cloud', value: 'shells', },
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
  
  StackQL tersedia di macOS melalui Homebrew dan penginstal `pkg`; arsitektur ARM (M1/Apple Silicon) dan Intel (x86_64) sama-sama didukung oleh satu penginstal multiarsitektur.

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (biner yang ditandatangani dan dinotarisasi)', value: 'curl', },
      { label: 'Unduhan paket', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Untuk menginstal melalui Homebrew, jalankan perintah berikut di terminal Anda:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  Untuk menginstal biner `stackql` yang dapat dieksekusi, ditandatangani, dan dinotarisasi di direktori saat ini, jalankan perintah berikut di terminal Anda:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL tersedia sebagai penginstal `pkg` interaktif untuk macOS yang ditandatangani dan dinotarisasi.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="Unduh PKG macOS"
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

  StackQL tersedia untuk semua arsitektur Linux.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'Unduhan paket', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  Untuk menginstal biner `stackql` yang dapat dieksekusi di direktori saat ini untuk arsitektur Linux Anda (amd64 atau arm64), jalankan perintah berikut di terminal Anda:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  Sebagai alternatif, Anda dapat mengunduh biner di sini:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Unduh ZIP Linux (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Unduh ZIP Linux (arm64)"
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

  StackQL tersedia di Windows melalui Chocolatey, skrip instalasi PowerShell, dan penginstal MSI. Build native AMD64 (x64) dan ARM64 tersedia sebagai penginstal MSI dan arsip ZIP yang berisi `stackql.exe`. Build AMD64 juga berjalan di ARM64 melalui emulasi. Semua file yang dapat dieksekusi ditandatangani dengan sertifikat penandatanganan kode Authenticode.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'Unduhan MSI/ZIP', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Untuk menginstal melalui Chocolatey, jalankan perintah berikut di terminal PowerShell atau `cmd` Anda:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Untuk menginstal file Authenticode yang dapat dieksekusi di direktori saat ini, jalankan perintah berikut di terminal PowerShell: 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  Unduh penginstal atau file `stackql` yang dapat dieksekusi untuk Windows di bawah ini; keduanya telah ditandatangani. Saat diklik, tombol unduh memilih AMD64 secara default; gunakan menu dropdown pada tombol tersebut untuk memilih AMD64 atau ARM64 native.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Unduh MSI Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Unduh MSI Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Unduh MSI Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Unduh ZIP Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Unduh ZIP Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Unduh ZIP Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  Build StackQL dipublikasikan ke [Docker Hub](https://hub.docker.com/u/stackql).  

  __Docker Hub__  

  Untuk mengunduh image container StackQL, jalankan perintah berikut:

  ```bash
  docker pull stackql/stackql
  ```

  Mulai `v1.0`, image ini berbasis Ubuntu 26.04 LTS, dan varian dengan Python 3.13 yang sudah terinstal dipublikasikan dengan akhiran tag `-python3.13` (alias `-python3`), misalnya:

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

`stackql-deploy` adalah framework IaC tanpa status yang dibangun di atas StackQL. Untuk dokumentasi lengkap, lihat [__Dokumentasi StackQL Deploy__](https://stackql-deploy.io/).

__Linux / macOS__

Mengunduh dan mengekstrak biner `stackql-deploy` terbaru dalam satu langkah. Penginstal mendeteksi sistem operasi dan arsitektur Anda (termasuk Apple Silicon dan Intel melalui biner macOS universal) dan mengambil aset rilis yang sesuai secara otomatis.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Mengunduh dan mengekstrak biner `stackql-deploy` terbaru ke direktori saat ini.
Penginstal mendeteksi platform Anda dan mengambil aset rilis yang sesuai secara otomatis.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Jika toolchain Rust sudah terinstal, perintah ini membangun dan menginstal biner langsung dari [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## Shell cloud

  StackQL dapat digunakan langsung dari shell cloud dan terminal web bawaan platform cloud dan data utama. Cara ini memberikan pengalaman yang mulus karena lingkungan tersebut sudah diotorisasi sebelumnya dengan identitas yang Anda gunakan untuk masuk, sehingga Anda tidak perlu menyiapkan autentikasi secara terpisah.

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

  AWS CloudShell menyediakan shell berbasis browser dengan AWS CLI yang sudah terinstal dan terautentikasi. Dengan menjalankan StackQL di AWS CloudShell, Anda dapat mengkueri dan mengelola sumber daya AWS tanpa langkah autentikasi tambahan. Untuk petunjuk terperinci, lihat panduan mulai cepat [StackQL di AWS CloudShell](/quick-starts/aws/aws-cloud-shell).

  Pertama, unduh paket StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  Lalu jalankan skrip StackQL untuk AWS CloudShell:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  Skrip ini memulai shell perintah StackQL menggunakan kredensial AWS CloudShell Anda, sehingga Anda dapat langsung mulai mengkueri sumber daya AWS.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell menyediakan lingkungan shell yang dapat diakses dari browser dengan Azure CLI yang sudah terautentikasi dengan akun Azure Anda. Dengan menggunakan StackQL di Azure Cloud Shell, Anda dapat mengkueri sumber daya Azure dengan mulus tanpa penyiapan tambahan. Untuk detail lengkap, lihat [panduan Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell) kami.  

  Pertama, unduh paket StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  Lalu jalankan skrip StackQL untuk Azure Cloud Shell:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  Skrip ini secara otomatis mengonfigurasi sesi StackQL agar menggunakan kredensial Azure Cloud Shell Anda, sehingga Anda dapat langsung mengkueri sumber daya Azure Anda.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell menawarkan lingkungan pengembangan dan operasi dengan Google Cloud CLI yang sudah terautentikasi. Dengan menjalankan StackQL di Google Cloud Shell, Anda dapat mengkueri sumber daya GCP menggunakan autentikasi yang sudah ada. Pelajari lebih lanjut di [panduan Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell) kami.  

  Pertama, unduh paket StackQL:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  Lalu jalankan skrip StackQL untuk Google Cloud Shell:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  Skrip ini menyiapkan StackQL agar menggunakan kredensial Google Cloud Shell Anda, sehingga Anda dapat langsung mulai mengkueri sumber daya GCP Anda dengan sintaksis SQL.

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web Terminal

  Ruang kerja Databricks menyertakan terminal web yang berjalan sebagai pengguna yang sedang masuk. Dengan menjalankan StackQL di sana, Anda dapat mengkueri ruang kerja menggunakan identitas Databricks Anda, tanpa penyiapan autentikasi terpisah. Untuk contoh kueri dan autentikasi tingkat akun, lihat [panduan Databricks Web Terminal](/blog/tutorials/stackql-in-databricks-web-terminal) kami.

  Pertama, unduh paket StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  Lalu jalankan skrip shell StackQL untuk Databricks:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  Perintah ini memulai sesi StackQL menggunakan identitas ruang kerja Databricks Anda, sehingga Anda dapat langsung mengkueri sumber daya dengan cakupan ruang kerja seperti `databricks_workspace.iam.vw_user_entitlements`. Untuk kueri tingkat akun (penyediaan, penagihan, IAM akun), atur variabel prinsipal layanan OAuth2 Databricks seperti yang dijelaskan dalam panduan tersebut.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Menggunakan dengan klien MCP {#using-with-mcp-clients}

Server MCP StackQL berjalan secara lokal melalui stdio, sehingga kredensial provider tetap berada di mesin Anda, apa pun klien yang Anda gunakan. Pilih klien Anda di bawah ini: setiap tab menampilkan cara melalui direktori atau satu klik terlebih dahulu, dan konfigurasi manual sebagai fallback. Untuk semua saluran distribusi, termasuk `npx`, Python, Docker, dan CI, lihat [Menginstal server MCP](#installing-the-mcp-server).

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

  Cara yang direkomendasikan untuk menginstal server MCP StackQL ke Claude Desktop adalah melalui [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Settings -> Connectors -> Add -> Browse Connectors**, cari **StackQL**) - Claude Desktop memilih biner yang ditandatangani dan sesuai untuk platform Anda secara otomatis.  Sebagai alternatif, MCP Bundle (`.mcpb`) siap pakai dilampirkan pada setiap rilis StackQL untuk instalasi dari file yang diunduh - dengan cara mana pun, Anda tidak perlu menginstal StackQL secara terpisah.  Bundle tersedia untuk macOS, Windows, dan Linux.

  Unduhan MCPB untuk Linux dan Windows secara default menggunakan **amd64** (x64). Gunakan menu dropdown untuk memilih **amd64** atau **arm64**.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Unduh MCPB macOS"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Unduh MCPB Linux"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Unduh MCPB Linux (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Unduh MCPB Linux (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Unduh MCPB Windows"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Unduh MCPB Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Unduh MCPB Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

Server MCP StackQL juga tercantum di [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) sebagai `io.github.stackql/stackql-mcp`.

:::

:::tip

Bundle ini adalah salah satu dari beberapa cara untuk menjalankan server MCP StackQL.  Lihat [__Menggunakan StackQL dengan Claude Desktop__](/getting-started/claude-desktop) untuk panduan lengkap Claude Desktop, atau [__Menginstal server MCP__](#installing-the-mcp-server) untuk opsi npx, Docker, Python, CI, dan manual.

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

  ### ChatGPT dan Codex

  StackQL tersedia sebagai plugin lokal untuk aplikasi desktop ChatGPT dan Codex CLI. Plugin ini menjalankan server MCP StackQL melalui stdio di mesin Anda, sehingga kredensial provider tidak pernah meninggalkan mesin tersebut. Diperlukan Node.js 24.22.1 atau yang lebih baru (22.23.4 atau 26.12.1 pada lini rilis tersebut); plugin ini hanya memerlukan modul bawaan Node.js, mengunduh biner `stackql` yang ditandatangani saat pertama kali digunakan, dan memverifikasi SHA-256 biner tersebut.

  **Aplikasi desktop ChatGPT (direktori plugin):**  
  
  buka direktori plugin di aplikasi desktop ChatGPT, cari **StackQL** dan klik **Install**, lalu jalankan penyiapan plugin; skill `stackql-setup` yang disertakan memandu Anda melalui kredensial dan kueri pertama. Entri ini memiliki lencana **Desktop only**: plugin hanya dapat diinstal dari aplikasi desktop, bukan dari ChatGPT web atau seluler, dan alat plugin ini tersedia dalam mode Work, di thread yang berjalan di komputer Anda. Jika entri ini belum terlihat di direktori Anda, instal dari marketplace repositori sebagai gantinya.

  **Aplikasi desktop ChatGPT (marketplace repositori):**  
  
  buka **Settings -> Plugins -> Add -> Add from GitHub**, masukkan `stackql/stackql`, lalu instal **StackQL** dari bagian **Personal** di direktori plugin dan mulai ulang aplikasi.

  **Codex CLI:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  Mulai ulang ChatGPT desktop atau Codex, lalu coba: *Gunakan StackQL untuk menginstal provider GitHub jika diperlukan, lalu cantumkan lima layanan GitHub pertama.* Kredensial provider disimpan di `~/.stackql/.env` dengan nama variabel lingkungan standar setiap provider (misalnya `AWS_ACCESS_KEY_ID` dan `AWS_SECRET_ACCESS_KEY`); setelah mengedit file tersebut, minta agen untuk memanggil `reload_credentials`.

  **Tanpa plugin:**   
  
  di **Settings -> Plugins -> MCPs -> Connect to a custom MCP**, pilih **STDIO**, atur perintah ke `npx`, lalu tambahkan argumen `-y` dan `@stackql/mcp-server`. Berikan kredensial provider sebagai variabel lingkungan di formulir tersebut.

<br />
:::note

ChatGPT web dan seluler tidak dapat menjalankan server stdio lokal, sehingga plugin ini hanya untuk desktop dan Codex. Bug ChatGPT desktop yang masih terbuka ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) dapat menyembunyikan alat MCP lokal dari sesi **Chat**; thread mode Work, thread Codex, dan Codex CLI tidak terpengaruh.

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

  Goose menjalankan ekstensi stdio secara langsung, sehingga Anda hanya memerlukan peluncur `npx` (Node.js 24.22.1 atau yang lebih baru, atau 22.23.4 atau 26.12.1 pada lini rilis tersebut).

  **Goose Desktop:** klik tombol di bawah ini. Goose akan terbuka, menampilkan perintah yang akan dijalankan dan meminta konfirmasi Anda, lalu menambahkan StackQL sebagai ekstensi.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Instal di Goose Desktop</a>
  </Box>

  Tombol ini adalah deep link `goose://`; Anda juga dapat menempelkannya ke bilah alamat browser, atau membukanya dari terminal dengan `open` (macOS), `start` (Windows), atau `xdg-open` (Linux):

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  atau jalankan `goose configure`, pilih **Add Extension -> Command-line Extension**, lalu masukkan perintah yang sama. Berikan kredensial provider sebagai variabel lingkungan ekstensi saat diminta.

<br />
:::note

Goose telah menghentikan direktori ekstensi di dalam aplikasinya dan beralih ke [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql), tempat StackQL tercantum sebagai `io.github.stackql/stackql-mcp`. Setelah Goose merilis fitur impor registri, StackQL akan dapat ditemukan di sana tanpa tindakan lebih lanjut.

:::

</Box>

</TabItem>
</Tabs>

## Menginstal server MCP {#installing-the-mcp-server}

Server MCP StackQL memungkinkan agen AI mengkueri dan menyediakan sumber daya cloud menggunakan StackQL. Server ini didistribusikan melalui beberapa saluran - semuanya menjalankan server yang sama, jadi pilih saluran yang sesuai dengan klien dan persyaratan kepercayaan Anda. Opsi di bawah ini diurutkan kira-kira berdasarkan tingkat kepercayaan dan kemudahan.

### Marketplace dan direktori {#marketplaces-and-directories}

Server ini dipublikasikan ke Official MCP Registry sebagai `io.github.stackql/stackql-mcp`; registri paket di bawah ini memuat artefak yang dapat didistribusikan, dan direktori hilir mengambil entri tersebut dari sana.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Registri', value: 'registries', },
    { label: 'Direktori', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Registri | Tipe | Dipublikasikan melalui | Entri | Catatan |
|---|---|---|---|---|
| Official MCP Registry | Registri metadata kanonis | CLI `mcp-publisher` + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | menyatakan semua 8 paket (`mcpb x5`, `oci`, `npm`, `pypi`) |
| npm | Registri paket | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | peluncur `npx` |
| PyPI | Registri paket | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | peluncur `uvx` / `pip` |
| Docker Hub | Registri container | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | multiarsitektur `amd64` + `arm64` |
| GitHub Actions Marketplace | Marketplace CI (action `setup-stackql-mcp`) | Repositori publik + rilis + entri marketplace | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Penerbit terverifikasi; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Entri di direktori dan agregator, diurutkan kira-kira berdasarkan tingkat kepentingannya bagi penemuan:

| Direktori | Tipe | Entri |
|---|---|---|
| __Anthropic Connector Directory__ | Direktori dalam aplikasi Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | Direktori dalam aplikasi ChatGPT desktop (hanya desktop) | cari **StackQL** di aplikasi desktop ChatGPT; lihat [ChatGPT dan Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | Direktori klien IDE | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | Agregator terbesar | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Penemuan, pendukung registri | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Marketplace yang dapat dicari | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Agregator | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Situs awesome-list | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Bundle `.mcpb` siap pakai {#prebuilt-mcpb-bundle}

**Kapan digunakan:** Claude Desktop tanpa akses ke Connector Directory, atau ketika Anda ingin mengunci versi tertentu atau memverifikasi checksum sendiri sebelum menginstal.  Bagi sebagian besar pengguna Claude Desktop, [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) adalah metode instalasi yang direkomendasikan.

MCP Bundle siap pakai untuk setiap platform dilampirkan pada setiap rilis StackQL:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

dengan `<platform>` salah satu dari `darwin-universal`, `windows-x64` (AMD64), `windows-arm64`, `linux-x64`, atau `linux-arm64`. Tombol unduh untuk setiap platform ada di tab **Claude Desktop** di bagian [Menggunakan dengan klien MCP](#using-with-mcp-clients). Setiap bundle memiliki checksum `.sha256` yang sesuai di [halaman rilis](https://github.com/stackql/stackql/releases/latest). Verifikasi checksum tersebut, lalu instal melalui **Settings -> Extensions** di Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Lihat [Menggunakan StackQL dengan Claude Desktop](/getting-started/claude-desktop#install-from-a-downloaded-bundle) untuk panduan lengkap.

### `claude_desktop_config.json` manual {#manual-claude_desktop_configjson}

**Kapan digunakan:** biner `stackql` sudah ada di PATH Anda dan Anda menggunakan Claude Desktop atau klien MCP stdio lainnya.

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

Ketiga argumen ini diperlukan:

- `--mcp.server.type=stdio` memilih transport stdio yang digunakan oleh klien yang tersemat di editor.
- `--approot` mengarahkan cache provider ke direktori yang dapat ditulis. Klien MCP dapat meluncurkan server dengan direktori kerja yang diatur ke `/`, yang tidak dapat ditulis.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` menonaktifkan log audit, yang jika tidak dinonaktifkan akan menggunakan direktori kerja (yang mungkin tidak dapat ditulis) sebagai direktori default.

Tambahkan kredensial provider dengan blok `"env"` dan sesuaikan kontrak keamanan dengan `"mode"` - lihat [Mode server](/command-line-usage/mcp#server-modes).

### `npx` (tanpa instalasi) {#npx-no-install}

**Kapan digunakan:** lingkungan Node apa pun, ketika Anda tidak ingin menginstal StackQL secara global.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

Peluncur [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) mengunduh biner `stackql` yang ditandatangani saat pertama kali dijalankan, memverifikasi SHA-256 biner tersebut, dan menyimpannya di cache untuk penggunaan berikutnya.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**Kapan digunakan:** lingkungan Python - peluncur yang sama yang dikemas untuk Python, dan berbagi cache biner yang diisi oleh peluncur npx.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

Atau instal ke lingkungan saat ini dengan `pip install stackql-mcp-server` (Python 3.9+). Nama paket di PyPI adalah [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/).

### Docker {#docker}

**Kapan digunakan:** runtime dalam container atau runtime terisolasi lainnya. Image ini multiarsitektur (amd64 dan arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

Sebagai konfigurasi klien:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

Nama image di Docker Hub adalah [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp).

### CI dan workflow agentik (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**Kapan digunakan:** jalankan server di dalam job GitHub Actions agar agen dapat mengkueri lingkungan cloud Anda - dan, jika Anda mengizinkan, mengambil tindakan di dalamnya - sebagai bagian dari CI.

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) menginstal biner dan menulis konfigurasi MCP (secara default dalam mode `read_only`). Teruskan konfigurasi tersebut ke [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) melalui `claude_args`:

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

Lihat [README action](https://github.com/stackql/setup-stackql-mcp) untuk resep agentik lainnya (audit cloud, estimasi biaya, dan sebagainya).

### MCP tersemat {#embedded-mcp}

**Kapan digunakan:** Anda sedang membangun aplikasi agentik dan ingin menyertakan server MCP ke dalam biner Anda sendiri melalui vendoring - tanpa `npx`, tanpa instalasi terpisah, tanpa dependensi runtime.

Library native menjalankan biner `stackql` yang ditandatangani melalui stdio di balik klien SDK MCP resmi setiap bahasa, sehingga aplikasi Anda mendapatkan antarmuka SQL terkendali yang sama ke setiap provider di registri. Setiap library secara default menggunakan `read_only` - eskalasi ke mode yang dapat menulis selalu eksplisit - dan dapat mengunduh lalu memverifikasi biner saat pertama kali dijalankan, atau menyertakannya ke dalam build Anda melalui vendoring untuk satu file executable mandiri. API lengkap per bahasa ada di [referensi MCP tersemat](/mcp/embedded).

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

Crate `stackql-mcp` menjalankan biner di balik klien `rmcp`. Fitur default `sidecar` mengunduh dan memverifikasi biner saat pertama kali dijalankan; fitur `vendored` menyematkannya dengan `include_bytes!` untuk satu biner mandiri. MSRV Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="Lihat di GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Lihat di crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Referensi lengkap: [MCP tersemat: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

Library `stackql-mcp-go` menjalankan biner di balik klien SDK MCP Go resmi. Sertakan biner melalui vendoring dengan `go:embed` untuk satu biner mandiri, atau biarkan library mengunduh dan memverifikasinya saat pertama kali dijalankan. Secara default dimulai dalam mode `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Lihat di GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Lihat di pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Referensi lengkap: [MCP tersemat: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

Library `io.stackql:stackql-mcp` menjalankan biner di balik klien SDK MCP Kotlin resmi, dan plugin Gradle pendamping menghubungkan peluncuran yang sama ke dalam build. Memerlukan JDK 17 dan Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="Lihat di GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Referensi lengkap: [MCP tersemat: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

Paket `StackQL.Mcp` menjalankan biner di balik klien SDK MCP C# resmi. Secara default berupa sidecar, atau sertakan bundle melalui vendoring sebagai sumber daya build untuk file executable mandiri. Memerlukan .NET 8 atau yang lebih baru.

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
    <BinaryDownloadLink iconSize={20} text="Lihat di GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Referensi lengkap: [MCP tersemat: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

Library `stackql_mcp` menargetkan runtime Erlang/BEAM dan mengekspos server sebagai child OTP melalui `child_spec()`, sehingga server dapat ditempatkan di dalam supervision tree Anda sendiri. Secara default dimulai dalam mode `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Lihat di GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Referensi lengkap: [MCP tersemat: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

Paket `stackql-mcp-swift` menjalankan biner di balik klien SDK MCP Swift resmi. Biner `darwin-universal` ditandatangani dengan Developer ID dan dinotarisasi oleh Apple, sehingga Anda dapat menyertakannya di dalam `.app` yang ditandatangani dan notarisasi aplikasi tetap valid. Memerlukan macOS 13+ dan Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="Lihat di GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Referensi lengkap: [MCP tersemat: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### Model kepercayaan {#trust-model}

Properti keamanan yang sama berlaku di setiap saluran:

- Biner `stackql` yang tersemat ditandatangani dengan Authenticode (Windows) dan dinotarisasi oleh Apple (macOS).
- Setiap bundle `.mcpb` disertai checksum SHA-256 yang dipublikasikan di halaman rilis.
- Peluncur `npx` dan `uvx` memverifikasi SHA-256 biner yang diunduh sebelum penggunaan pertama.
- Entri di [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) mengesahkan hash per platform.

## Menggunakan GitHub Actions {#using-github-actions}

[GitHub Actions StackQL](https://github.com/stackql/stackql-actions-demo) tersedia untuk digunakan dalam workflow GitHub Actions Anda. Action berikut tersedia:

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

Gunakan StackQL untuk men-deploy atau menguji stack infrastruktur langsung dari workflow GitHub Anda. Action ini memungkinkan workflow infrastruktur sebagai kode (IaC) dengan sintaksis mirip SQL, sehingga Anda dapat mendefinisikan, men-deploy, dan mengelola sumber daya cloud di beberapa provider dalam satu workflow.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Lihat di GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Lihat di GitHub"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Contoh penggunaan__

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

Siapkan StackQL di workflow GitHub Actions Anda.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Lihat di GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Lihat di GitHub"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Contoh penggunaan__

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

Instal server MCP StackQL di workflow GitHub Actions Anda.  Action ini menginstal biner yang ditandatangani dan menulis konfigurasi `mcpServers` (secara default dalam mode `read_only`) yang digunakan oleh langkah agentik seperti `anthropics/claude-code-action` melalui `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Lihat di GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Lihat di GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Contoh penggunaan__

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

Jalankan perintah StackQL di workflow GitHub Actions Anda.  Kueri dapat diberikan secara inline atau dari file di repositori.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Lihat di GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Lihat di GitHub"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Contoh penggunaan__

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

Jalankan pengujian unit di workflow GitHub Actions Anda (untuk pemeriksaan penjaminan, tata kelola, atau keamanan cloud).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Lihat di GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Lihat di GitHub"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Contoh penggunaan__

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


## Library lainnya {#other-libraries}

StackQL menyediakan beberapa metode integrasi untuk digunakan dalam berbagai bahasa pemrograman dan lingkungan. Library ini memperluas fungsionalitas StackQL, sehingga Anda dapat dengan mudah menambahkan kemampuan mengkueri dan mengelola sumber daya cloud ke aplikasi dan workflow yang sudah ada.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### Paket Python `pystackql` {#pystackql-python-package}

Wrapper Python untuk menggunakan StackQL dalam program Python Anda. Paket `pystackql` tersedia di [__PyPI__](https://pypi.org/project/pystackql/), dan dokumentasi paket `pystackql` tersedia di [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). Untuk menginstal paket `pystackql`, jalankan perintah berikut:

```bash
pip install pystackql
```

Contoh berikut menunjukkan paket `pystackql` yang digunakan bersama `pandas` untuk menjalankan kueri StackQL dan mengembalikan hasilnya ke `pandas.DataFrame`:

```python
from pystackql import StackQL
region = "ap-southeast-2"
stackql = StackQL(output='pandas')

query = """
SELECT instance_type, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = '%s'
GROUP BY instance_type
""" % (region)

df = stackql.execute(query)
print(df)
```

</TabItem>
</Tabs>
