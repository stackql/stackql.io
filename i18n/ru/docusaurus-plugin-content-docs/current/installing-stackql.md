---
title: Установка
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "Установите StackQL в macOS, Linux, Windows, Docker или облачной оболочке и используйте MCP-сервер StackQL в Claude Desktop, ChatGPT, Goose и других MCP-клиентах или через npx, uvx, pip, Docker и GitHub Actions."
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

Здесь описана установка StackQL на различных платформах.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Облачные оболочки', value: 'shells', },
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
  
  StackQL доступен в macOS через Homebrew и установщик `pkg`; один мультиархитектурный установщик поддерживает обе архитектуры: ARM (M1/Apple Silicon) и Intel (x86_64).

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (подписанный и нотариально заверенный двоичный файл)', value: 'curl', },
      { label: 'Скачивание пакета', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Для установки через Homebrew выполните в терминале следующую команду:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  Чтобы установить подписанный и нотариально заверенный исполняемый двоичный файл `stackql` в текущий каталог, выполните в терминале следующую команду:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL доступен в виде подписанного и нотариально заверенного интерактивного установщика `pkg` для macOS.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="Скачать PKG для macOS"
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

  StackQL доступен для всех архитектур Linux.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'Скачивание пакета', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  Чтобы установить исполняемый двоичный файл `stackql` для вашей архитектуры Linux (amd64 или arm64) в текущий каталог, выполните в терминале следующую команду:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  Кроме того, двоичные файлы можно скачать здесь:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Скачать ZIP для Linux (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Скачать ZIP для Linux (arm64)"
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

  StackQL доступен в Windows через Chocolatey, скрипт установки PowerShell и установщик MSI. Нативные сборки для AMD64 (x64) и ARM64 доступны в виде установщиков MSI и архивов ZIP, содержащих `stackql.exe`. Сборка для AMD64 также работает на ARM64 с помощью эмуляции. Все исполняемые файлы подписаны сертификатом подписи кода Authenticode.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'Скачивание MSI/ZIP', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Для установки через Chocolatey выполните следующую команду в терминале PowerShell или `cmd`:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Чтобы установить исполняемый файл с подписью Authenticode в текущий каталог, выполните в терминале PowerShell следующую команду: 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  Скачайте ниже подписанный установщик или исполняемый файл `stackql` для Windows. При нажатии кнопки скачивания по умолчанию выбирается AMD64; чтобы выбрать AMD64 или нативную сборку для ARM64, используйте раскрывающийся список кнопки.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Скачать MSI для Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Скачать MSI для Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Скачать MSI для Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Скачать ZIP для Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Скачать ZIP для Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Скачать ZIP для Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  Сборки StackQL публикуются в [Docker Hub](https://hub.docker.com/u/stackql).  

  __Docker Hub__  

  Чтобы загрузить образ контейнера StackQL, выполните следующую команду:

  ```bash
  docker pull stackql/stackql
  ```

  Начиная с `v1.0`, образ основан на Ubuntu 26.04 LTS, а вариант с предустановленным Python 3.13 публикуется с суффиксом тега `-python3.13` (псевдоним `-python3`), например:

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

`stackql-deploy` - это фреймворк IaC без сохранения состояния, построенный на StackQL. Полную документацию см. в [__StackQL Deploy Docs__](https://stackql-deploy.io/).

__Linux / macOS__

Скачивает и распаковывает последнюю версию двоичного файла `stackql-deploy` за один шаг. Установщик определяет вашу ОС и архитектуру (включая Apple Silicon и Intel с помощью универсального двоичного файла macOS) и автоматически получает нужный файл выпуска.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Скачивает и распаковывает последнюю версию двоичного файла `stackql-deploy` в текущий каталог.
Установщик определяет вашу платформу и автоматически получает нужный файл выпуска.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Если у вас установлен набор инструментов Rust, эта команда собирает и устанавливает двоичный файл непосредственно с [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## Облачные оболочки

  StackQL можно использовать непосредственно из встроенных облачных оболочек и веб-терминалов основных облачных платформ и платформ данных. Это упрощает работу: эти среды уже авторизованы с удостоверением, под которым вы вошли в систему, поэтому отдельно настраивать аутентификацию не нужно.

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

  AWS CloudShell предоставляет оболочку в браузере с предустановленным AWS CLI, который уже прошел аутентификацию. Запуск StackQL в AWS CloudShell позволяет выполнять запросы к ресурсам AWS и управлять ими без дополнительных шагов аутентификации. Подробности см. в нашем [руководстве по AWS CloudShell](/quick-starts/aws/aws-cloud-shell).

  Сначала скачайте пакет StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  Затем запустите скрипт StackQL для AWS CloudShell:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  Этот скрипт запускает командную оболочку StackQL с вашими учетными данными AWS CloudShell, и вы можете сразу начать выполнять запросы к ресурсам AWS.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell предоставляет доступную из браузера среду оболочки, в которой Azure CLI уже прошел аутентификацию с вашей учетной записью Azure. Использование StackQL в Azure Cloud Shell позволяет беспрепятственно выполнять запросы к вашим ресурсам Azure без дополнительной настройки. Подробности см. в нашем [руководстве по Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell).  

  Сначала скачайте пакет StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  Затем запустите скрипт StackQL для Azure Cloud Shell:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  Этот скрипт автоматически настраивает сеанс StackQL на использование ваших учетных данных Azure Cloud Shell, и вы можете сразу выполнять запросы к вашим ресурсам Azure.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell предлагает среду для разработки и эксплуатации, в которой Google Cloud CLI уже прошел аутентификацию. Запуск StackQL в Google Cloud Shell позволяет выполнять запросы к ресурсам GCP с использованием вашей существующей аутентификации. Подробнее см. в нашем [руководстве по Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell).  

  Сначала скачайте пакет StackQL:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  Затем запустите скрипт StackQL для Google Cloud Shell:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  Скрипт настраивает StackQL на использование ваших учетных данных Google Cloud Shell, и вы можете сразу начать выполнять запросы к вашим ресурсам GCP с помощью синтаксиса SQL.

  </TabItem>
  <TabItem value="databricks">

  ### Веб-терминал Databricks

  Рабочие области Databricks включают веб-терминал, который работает от имени вошедшего в систему пользователя. Запуск StackQL в нем позволяет выполнять запросы к вашей рабочей области с вашим удостоверением Databricks, без отдельной настройки аутентификации. Примеры запросов и аутентификацию на уровне учетной записи см. в нашем [руководстве по веб-терминалу Databricks](/blog/tutorials/stackql-in-databricks-web-terminal).

  Сначала скачайте пакет StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  Затем запустите скрипт оболочки StackQL для Databricks:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  Так запускается сеанс StackQL с удостоверением вашей рабочей области Databricks, и вы можете сразу выполнять запросы к ресурсам уровня рабочей области, таким как `databricks_workspace.iam.vw_user_entitlements`. Для запросов на уровне учетной записи (создание ресурсов, выставление счетов, IAM учетной записи) задайте переменные субъекта-службы OAuth2 для Databricks, как описано в руководстве.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Использование с MCP-клиентами {#using-with-mcp-clients}

MCP-сервер StackQL работает локально через stdio, поэтому учетные данные провайдеров остаются на вашем компьютере, какой бы клиент вы ни использовали. Выберите свой клиент ниже: на каждой вкладке сначала описана установка из каталога или в один щелчок, а ручная конфигурация приведена как резервный вариант. Все каналы распространения, включая `npx`, Python, Docker и CI, см. в разделе [Установка MCP-сервера](#installing-the-mcp-server).

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

  Рекомендуемый способ установить MCP-сервер StackQL в Claude Desktop - из каталога [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Settings -> Connectors -> Add -> Browse Connectors**, найдите **StackQL**) - Claude Desktop автоматически выбирает нужный подписанный двоичный файл для вашей платформы.  Кроме того, к каждому выпуску StackQL прилагаются предварительно собранные пакеты MCP Bundle (`.mcpb`) для установки из скачанного файла - в обоих случаях отдельно устанавливать StackQL не требуется.  Пакеты доступны для macOS, Windows и Linux.

  По умолчанию для Linux и Windows скачиваются пакеты MCPB для **amd64** (x64). Чтобы выбрать **amd64** или **arm64**, используйте раскрывающиеся списки.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Скачать MCPB для macOS"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Скачать MCPB для Linux"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Скачать MCPB для Linux (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Скачать MCPB для Linux (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Скачать MCPB для Windows"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Скачать MCPB для Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Скачать MCPB для Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

MCP-сервер StackQL также зарегистрирован в [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) как `io.github.stackql/stackql-mcp`.

:::

:::tip

Пакет - один из нескольких способов запустить MCP-сервер StackQL.  Полное пошаговое руководство для Claude Desktop см. на странице [__Использование StackQL с Claude Desktop__](/getting-started/claude-desktop), а варианты с npx, Docker, Python, CI и ручной настройкой - в разделе [__Установка MCP-сервера__](#installing-the-mcp-server).

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

  ### ChatGPT и Codex

  StackQL поставляется как локальный плагин для настольного приложения ChatGPT и Codex CLI. Плагин запускает MCP-сервер StackQL через stdio на вашем компьютере, поэтому учетные данные провайдеров никогда его не покидают. Требуется Node.js версии 24.22.1 или более поздней (22.23.4 или 26.12.1 для соответствующих линеек выпусков); плагину нужны только встроенные модули Node.js, он скачивает подписанный двоичный файл `stackql` при первом использовании и проверяет его SHA-256.

  **Настольное приложение ChatGPT (каталог плагинов):**  
  
  откройте каталог плагинов в настольном приложении ChatGPT, найдите **StackQL** и нажмите **Install**, затем запустите настройку плагина; входящий в комплект skill `stackql-setup` поможет настроить учетные данные и выполнить первый запрос. Страница в каталоге отмечена значком **Desktop only**: плагин устанавливается только из настольного приложения, а не из веб-версии или мобильной версии ChatGPT, и его инструменты доступны в режиме Work, в threads, которые выполняются на вашем компьютере. Если страница еще не отображается в вашем каталоге, установите плагин из маркетплейса репозитория.

  **Настольное приложение ChatGPT (маркетплейс репозитория):**  
  
  откройте **Settings -> Plugins -> Add -> Add from GitHub**, введите `stackql/stackql`, затем установите **StackQL** из раздела **Personal** каталога плагинов и перезапустите приложение.

  **Codex CLI:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  Перезапустите настольное приложение ChatGPT или Codex, затем попробуйте: *Используй StackQL, чтобы при необходимости установить провайдер GitHub, а затем перечисли первые пять сервисов GitHub.* Учетные данные провайдеров указываются в файле `~/.stackql/.env` под стандартными именами переменных среды каждого провайдера (например, `AWS_ACCESS_KEY_ID` и `AWS_SECRET_ACCESS_KEY`); после изменения файла попросите агента вызвать инструмент `reload_credentials`.

  **Без плагина:**   
  
  в разделе **Settings -> Plugins -> MCPs -> Connect to a custom MCP** выберите **STDIO**, укажите команду `npx` и добавьте аргументы `-y` и `@stackql/mcp-server`. Передайте учетные данные провайдеров как переменные среды в этой форме.

<br />
:::note

Веб-версия и мобильная версия ChatGPT не могут запускать локальный сервер stdio, поэтому плагин доступен только для настольного приложения и Codex. Открытая ошибка настольного приложения ChatGPT ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) может скрывать локальные MCP-инструменты в сеансах **Chat**; на threads в режиме Work, threads Codex и Codex CLI она не влияет.

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

  Goose запускает расширения stdio напрямую, поэтому вам нужно только средство запуска `npx` (Node.js версии 24.22.1 или более поздней либо 22.23.4 или 26.12.1 для соответствующих линеек выпусков).

  **Goose Desktop:** нажмите кнопку ниже. Goose откроется, покажет команду, которую собирается выполнить, попросит вас подтвердить ее, а затем добавит StackQL как расширение.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Установить в Goose Desktop</a>
  </Box>

  Кнопка - это прямая ссылка `goose://`; ее также можно вставить в адресную строку браузера или открыть из терминала командой `open` (macOS), `start` (Windows) или `xdg-open` (Linux):

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  или выполните `goose configure`, выберите **Add Extension -> Command-line Extension** и введите ту же команду. Когда появится запрос, укажите учетные данные провайдеров как переменные среды расширения.

<br />
:::note

Goose отказался от собственного каталога расширений в приложении в пользу [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql), где StackQL зарегистрирован как `io.github.stackql/stackql-mcp`. Как только в Goose появится импорт из реестра, StackQL можно будет найти там без каких-либо дополнительных действий.

:::

</Box>

</TabItem>
</Tabs>

## Установка MCP-сервера {#installing-the-mcp-server}

MCP-сервер StackQL позволяет ИИ-агентам выполнять запросы к облачным ресурсам и создавать их с помощью StackQL. Он распространяется через несколько каналов - все они запускают один и тот же сервер, поэтому выберите канал, который подходит вашему клиенту и вашим требованиям к доверию. Варианты ниже перечислены примерно в порядке доверия и простоты.

### Маркетплейсы и каталоги {#marketplaces-and-directories}

Сервер опубликован в Official MCP Registry как `io.github.stackql/stackql-mcp`; реестры пакетов ниже содержат распространяемые артефакты, а производные каталоги берут оттуда сведения для своих страниц.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Реестры', value: 'registries', },
    { label: 'Каталоги', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Реестр | Тип | Способ публикации | Страница | Примечания |
|---|---|---|---|---|
| Official MCP Registry | Канонический реестр метаданных | CLI `mcp-publisher` + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | объявляет все 8 пакетов (`mcpb x5`, `oci`, `npm`, `pypi`) |
| npm | Реестр пакетов | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | средство запуска `npx` |
| PyPI | Реестр пакетов | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | средство запуска `uvx` / `pip` |
| Docker Hub | Реестр контейнеров | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | мультиархитектурный: `amd64` + `arm64` |
| GitHub Actions Marketplace | Маркетплейс CI (действие `setup-stackql-mcp`) | Публичный репозиторий + выпуск + страница в маркетплейсе | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Проверенный издатель; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Страницы в каталогах и агрегаторах, примерно в порядке значимости для обнаружения:

| Каталог | Тип | Страница |
|---|---|---|
| __Anthropic Connector Directory__ | Каталог в приложении Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | Каталог в настольном приложении ChatGPT (только настольная версия) | найдите **StackQL** в настольном приложении ChatGPT; см. [ChatGPT и Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | Каталог клиентов IDE | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | Крупнейший агрегатор | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Обнаружение серверов; поддерживает реестр | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Маркетплейс с поиском | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Агрегатор | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Веб-сайт в стиле awesome-list | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Предварительно собранный пакет `.mcpb` {#prebuilt-mcpb-bundle}

**Когда использовать:** Claude Desktop без доступа к Connector Directory или когда нужно закрепить определенную версию либо самостоятельно проверить контрольные суммы перед установкой.  Для большинства пользователей Claude Desktop рекомендуемый способ установки - [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

К каждому выпуску StackQL для каждой платформы прилагается предварительно собранный пакет MCP Bundle:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

где `<platform>` - одно из значений `darwin-universal`, `windows-x64` (AMD64), `windows-arm64`, `linux-x64` или `linux-arm64`. Кнопки скачивания для каждой платформы находятся на вкладке **Claude Desktop** в разделе [Использование с MCP-клиентами](#using-with-mcp-clients). Для каждого пакета на [странице выпуска](https://github.com/stackql/stackql/releases/latest) есть соответствующая контрольная сумма `.sha256`. Проверьте ее, а затем установите пакет через **Settings -> Extensions** в Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Полное пошаговое руководство см. на странице [Использование StackQL с Claude Desktop](/getting-started/claude-desktop#install-from-a-downloaded-bundle).

### Ручная настройка `claude_desktop_config.json` {#manual-claude_desktop_configjson}

**Когда использовать:** двоичный файл `stackql` уже есть в вашем PATH, и вы используете Claude Desktop или любой другой MCP-клиент, работающий через stdio.

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

Все три аргумента необходимы:

- `--mcp.server.type=stdio` выбирает транспорт stdio, который используют клиенты, встроенные в редакторы.
- `--approot` направляет кэш провайдеров в каталог, доступный для записи. MCP-клиенты могут запускать сервер с рабочим каталогом `/`, который недоступен для записи.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` отключает журнал аудита, который иначе по умолчанию использует в качестве своего каталога рабочий каталог (возможно, недоступный для записи).

Добавьте учетные данные провайдеров с помощью блока `"env"` и настройте контракт безопасности с помощью `"mode"` - см. [Режимы сервера](/command-line-usage/mcp#server-modes).

### `npx` (без установки) {#npx-no-install}

**Когда использовать:** любая среда Node, если вы не хотите устанавливать StackQL глобально.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

Средство запуска [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) скачивает подписанный двоичный файл `stackql` при первом запуске, проверяет его SHA-256 и кэширует для последующих запусков.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**Когда использовать:** среда Python - то же средство запуска в виде пакета для Python, использующее общий кэш двоичных файлов, который заполняет средство запуска npx.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

Или установите его в текущую среду командой `pip install stackql-mcp-server` (Python 3.9+). На PyPI это пакет [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/).

### Docker {#docker}

**Когда использовать:** контейнерная или иным образом изолированная среда выполнения. Образ мультиархитектурный (amd64 и arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

В виде конфигурации клиента:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

В Docker Hub это образ [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp).

### CI и агентные рабочие процессы (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**Когда использовать:** запуск сервера внутри задания GitHub Actions, чтобы агент мог выполнять запросы к вашему облаку - и, если вы это разрешите, выполнять в нем действия - в рамках CI.

Действие [`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) устанавливает двоичный файл и записывает конфигурацию MCP (по умолчанию в режиме `read_only`). Передайте эту конфигурацию в [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) через `claude_args`:

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

Другие агентные рецепты (аудит облака, оценка затрат и так далее) см. в [README действия](https://github.com/stackql/setup-stackql-mcp).

### Встроенный MCP {#embedded-mcp}

**Когда использовать:** вы создаете агентное приложение и хотите включить MCP-сервер в собственный двоичный файл через вендоринг - без `npx`, без отдельной установки, без зависимостей во время выполнения.

Нативные библиотеки запускают подписанный двоичный файл `stackql` и работают с ним через stdio с помощью официального клиента MCP SDK для соответствующего языка, поэтому ваше приложение получает тот же управляемый SQL-интерфейс к каждому провайдеру в реестре. Каждая библиотека по умолчанию работает в режиме `read_only` - повышение уровня доступа до режима с записью всегда выполняется явно - и может либо скачать и проверить двоичный файл при первом запуске, либо включить его в вашу сборку через вендоринг, чтобы получить единый автономный исполняемый файл. Полное описание API для каждого языка приведено в [справочнике по встроенному MCP](/mcp/embedded).

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

Crate `stackql-mcp` запускает двоичный файл и работает с ним через клиент `rmcp`. Возможность `sidecar`, включенная по умолчанию, скачивает и проверяет двоичный файл при первом запуске; возможность `vendored` встраивает его с помощью `include_bytes!`, чтобы получить единый автономный двоичный файл. MSRV: Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="Открыть на GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Открыть на crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Полный справочник: [Встроенный MCP: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

Библиотека `stackql-mcp-go` запускает двоичный файл и работает с ним через официальный клиент MCP SDK для Go. Включите двоичный файл через вендоринг с помощью `go:embed`, чтобы получить единый автономный двоичный файл, или позвольте библиотеке скачать и проверить его при первом запуске. По умолчанию запускается в режиме `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Открыть на GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Открыть на pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Полный справочник: [Встроенный MCP: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

Библиотека `io.stackql:stackql-mcp` запускает двоичный файл и работает с ним через официальный клиент MCP SDK для Kotlin, а сопутствующий плагин Gradle подключает тот же запуск к сборке. Требуются JDK 17 и Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="Открыть на GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Полный справочник: [Встроенный MCP: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

Пакет `StackQL.Mcp` запускает двоичный файл и работает с ним через официальный клиент MCP SDK для C#. По умолчанию работает как sidecar; также можно включить пакет через вендоринг как ресурс сборки, чтобы получить автономный исполняемый файл. Требуется .NET 8 или более поздняя версия.

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
    <BinaryDownloadLink iconSize={20} text="Открыть на GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Полный справочник: [Встроенный MCP: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

Библиотека `stackql_mcp` предназначена для среды выполнения Erlang/BEAM и предоставляет сервер как дочерний процесс OTP через `child_spec()`, поэтому его можно разместить в вашем собственном дереве супервизоров. По умолчанию запускается в режиме `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Открыть на GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Полный справочник: [Встроенный MCP: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

Пакет `stackql-mcp-swift` запускает двоичный файл и работает с ним через официальный клиент MCP SDK для Swift. Двоичный файл `darwin-universal` подписан Developer ID и нотариально заверен Apple, поэтому его можно включить в подписанное приложение `.app` и сохранить действительность нотариального заверения приложения. Требуются macOS 13+ и Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="Открыть на GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Полный справочник: [Встроенный MCP: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### Модель доверия {#trust-model}

Для всех каналов действуют одни и те же свойства безопасности:

- Встроенный двоичный файл `stackql` подписан Authenticode (Windows) и нотариально заверен Apple (macOS).
- Каждый пакет `.mcpb` поставляется с опубликованной на странице выпуска контрольной суммой SHA-256.
- Средства запуска `npx` и `uvx` проверяют SHA-256 скачанного двоичного файла перед первым использованием.
- Запись в [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) подтверждает хэши для каждой платформы.

## Использование GitHub Actions {#using-github-actions}

[StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo) можно использовать в ваших рабочих процессах GitHub Actions. Доступны следующие действия:

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

Развертывайте или тестируйте стеки инфраструктуры с помощью StackQL непосредственно из ваших рабочих процессов GitHub. Это действие позволяет строить рабочие процессы инфраструктуры как кода (IaC) с синтаксисом, похожим на SQL, чтобы определять, развертывать облачные ресурсы нескольких провайдеров и управлять ими в одном рабочем процессе.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Открыть в GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Открыть на GitHub"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Пример использования__

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

Настройте StackQL в вашем рабочем процессе GitHub Actions.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Открыть в GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Открыть на GitHub"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Пример использования__

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

Установите MCP-сервер StackQL в вашем рабочем процессе GitHub Actions.  Действие устанавливает подписанный двоичный файл и записывает конфигурацию `mcpServers` (по умолчанию в режиме `read_only`), которую агентный шаг, например `anthropics/claude-code-action`, получает через `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Открыть в GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Открыть на GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Пример использования__

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

Выполняйте команды StackQL в вашем рабочем процессе GitHub Actions.  Запросы можно указать прямо в рабочем процессе или в файле репозитория.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Открыть в GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Открыть на GitHub"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Пример использования__

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

Выполняйте модульные тесты в вашем рабочем процессе GitHub Actions (для проверок в рамках обеспечения гарантий, управления или безопасности облака).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Открыть в GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Открыть на GitHub"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Пример использования__

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


## Другие библиотеки {#other-libraries}

StackQL предоставляет несколько способов интеграции для разных языков программирования и сред. Эти библиотеки расширяют возможности StackQL и упрощают добавление запросов к облачным ресурсам и управления ими в ваши существующие приложения и рабочие процессы.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### Пакет Python `pystackql` {#pystackql-python-package}

Оболочка Python для использования StackQL в ваших программах на Python. Пакет `pystackql` доступен на [__PyPI__](https://pypi.org/project/pystackql/), документация по пакету `pystackql` доступна на [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). Чтобы установить пакет `pystackql`, выполните следующую команду:

```bash
pip install pystackql
```

В следующем примере пакет `pystackql` используется вместе с библиотекой `pandas`, чтобы выполнять запросы StackQL и возвращать результаты в объект `pandas.DataFrame`:

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
