---
title: Встановлення
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "Встановіть StackQL на macOS, Linux, Windows, у Docker або хмарній оболонці й використовуйте сервер MCP StackQL у Claude Desktop, ChatGPT, Goose та інших клієнтах MCP або через npx, uvx, pip, Docker і GitHub Actions."
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

Тут наведено інструкції зі встановлення StackQL на різних платформах.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Хмарні оболонки', value: 'shells', },
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
  
  StackQL доступний на macOS через Homebrew та інсталятор `pkg`; один багатоархітектурний інсталятор підтримує обидві архітектури: ARM (M1/Apple Silicon) і Intel (x86_64).

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (підписаний і нотаризований двійковий файл)', value: 'curl', },
      { label: 'Завантаження пакета', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Для встановлення через Homebrew виконайте таку команду у вашому терміналі:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  Щоб встановити підписаний і нотаризований виконуваний двійковий файл `stackql` у поточному каталозі, виконайте таку команду у вашому терміналі:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL доступний як підписаний і нотаризований інтерактивний інсталятор `pkg` для macOS.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="Завантажити PKG для macOS"
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

  StackQL доступний для всіх архітектур Linux.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'Завантаження пакета', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  Щоб встановити виконуваний двійковий файл `stackql` для вашої архітектури Linux (amd64 або arm64) у поточному каталозі, виконайте таку команду у вашому терміналі:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  Або ж ви можете завантажити двійкові файли тут:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Завантажити ZIP для Linux (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Завантажити ZIP для Linux (arm64)"
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

  StackQL доступний на Windows через Chocolatey, скрипт встановлення PowerShell та інсталятор MSI. Нативні збірки AMD64 (x64) і ARM64 доступні як інсталятори MSI та архіви ZIP, що містять `stackql.exe`. Збірка AMD64 також працює на ARM64 через емуляцію. Усі виконувані файли підписано сертифікатом підписування коду Authenticode.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'Завантаження MSI/ZIP', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Для встановлення через Chocolatey виконайте таку команду у вашому терміналі PowerShell або `cmd`:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Щоб встановити виконуваний файл із підписом Authenticode у поточному каталозі, виконайте таку команду в терміналі PowerShell: 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  Завантажте нижче підписаний інсталятор або виконуваний файл `stackql` для Windows. Натискання кнопки завантаження за замовчуванням вибирає AMD64; скористайтеся її розкривним списком, щоб вибрати AMD64 або нативний ARM64.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Завантажити MSI для Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Завантажити MSI для Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Завантажити MSI для Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Завантажити ZIP для Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Завантажити ZIP для Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Завантажити ZIP для Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  Збірки StackQL публікуються в [Docker Hub](https://hub.docker.com/u/stackql).  

  __Docker Hub__  

  Щоб завантажити образ контейнера StackQL, виконайте таку команду:

  ```bash
  docker pull stackql/stackql
  ```

  Починаючи з `v1.0`, образ ґрунтується на Ubuntu 26.04 LTS, а варіант із попередньо встановленим Python 3.13 публікується із суфіксом тегу `-python3.13` (псевдонім `-python3`), наприклад:

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

`stackql-deploy` - це фреймворк IaC без збереження стану, побудований на StackQL. Повну документацію див. у [__StackQL Deploy Docs__](https://stackql-deploy.io/).

__Linux / macOS__

Завантажує й розпаковує останній двійковий файл `stackql-deploy` за один крок. Інсталятор визначає вашу ОС та архітектуру (зокрема Apple Silicon та Intel через універсальний двійковий файл macOS) і автоматично отримує правильний файл випуску.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Завантажує й розпаковує останній двійковий файл `stackql-deploy` у поточний каталог.
Інсталятор визначає вашу платформу й автоматично отримує правильний файл випуску.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Якщо у вас встановлено набір інструментів Rust, ця команда збирає й встановлює двійковий файл безпосередньо з [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## Хмарні оболонки

  StackQL можна використовувати безпосередньо із вбудованих хмарних оболонок і вебтерміналів основних хмарних платформ і платформ даних. Це забезпечує безперешкодну роботу, оскільки ці середовища вже авторизовано з ідентичністю, під якою ви ввійшли, тож окремо налаштовувати автентифікацію не потрібно.

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

  AWS CloudShell надає оболонку в браузері з попередньо встановленим і автентифікованим AWS CLI. Запуск StackQL в AWS CloudShell дає змогу виконувати запити до ресурсів AWS і керувати ними без додаткових кроків автентифікації. Докладні інструкції див. на сторінці швидкого старту [StackQL в AWS CloudShell](/quick-starts/aws/aws-cloud-shell).

  Спочатку завантажте пакет StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  Потім запустіть скрипт StackQL для AWS CloudShell:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  Цей скрипт запускає командну оболонку StackQL з вашими обліковими даними AWS CloudShell, тож ви можете одразу почати виконувати запити до ресурсів AWS.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell надає доступне з браузера середовище оболонки, у якому Azure CLI вже автентифіковано вашим обліковим записом Azure. Використання StackQL в Azure Cloud Shell дає змогу безперешкодно виконувати запити до ваших ресурсів Azure без додаткового налаштування. Усі подробиці див. у нашому [посібнику з Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell).  

  Спочатку завантажте пакет StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  Потім запустіть скрипт StackQL для Azure Cloud Shell:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  Цей скрипт автоматично налаштовує сеанс StackQL на використання ваших облікових даних Azure Cloud Shell, тож ви можете одразу виконувати запити до ваших ресурсів Azure.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell пропонує середовище для розробки та експлуатації з уже автентифікованим Google Cloud CLI. Запуск StackQL у Google Cloud Shell дає змогу виконувати запити до ресурсів GCP з вашою наявною автентифікацією. Докладніше див. у нашому [посібнику з Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell).  

  Спочатку завантажте пакет StackQL:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  Потім запустіть скрипт StackQL для Google Cloud Shell:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  Скрипт налаштовує StackQL на використання ваших облікових даних Google Cloud Shell, тож ви можете одразу почати виконувати запити до ваших ресурсів GCP за допомогою синтаксису SQL.

  </TabItem>
  <TabItem value="databricks">

  ### Вебтермінал Databricks

  Робочі області Databricks містять вебтермінал, що працює від імені користувача, який увійшов у систему. Запуск StackQL у ньому дає змогу виконувати запити до вашої робочої області з вашою ідентичністю Databricks, без окремого налаштування автентифікації. Приклади запитів і автентифікацію на рівні облікового запису див. у нашому [посібнику з вебтерміналу Databricks](/blog/tutorials/stackql-in-databricks-web-terminal).

  Спочатку завантажте пакет StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  Потім запустіть скрипт оболонки StackQL для Databricks:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  Так запускається сеанс StackQL з ідентичністю вашої робочої області Databricks, тож ви можете одразу виконувати запити до ресурсів на рівні робочої області, таких як `databricks_workspace.iam.vw_user_entitlements`. Для запитів на рівні облікового запису (створення ресурсів, виставлення рахунків, IAM облікового запису) встановіть змінні service principal OAuth2 для Databricks, як описано в посібнику.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Використання з клієнтами MCP {#using-with-mcp-clients}

Сервер MCP StackQL працює локально через stdio, тож облікові дані провайдерів залишаються на вашому комп'ютері, хоч би який клієнт ви використовували. Виберіть свій клієнт нижче: кожна вкладка спершу описує шлях через каталог або встановлення одним клацанням, а ручну конфігурацію наводить як резервний варіант. Усі канали розповсюдження, зокрема `npx`, Python, Docker і CI, див. у розділі [Встановлення сервера MCP](#installing-the-mcp-server).

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

  Рекомендований спосіб встановити сервер MCP StackQL у Claude Desktop - з каталогу [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Settings -> Connectors -> Add -> Browse Connectors**, знайдіть **StackQL**) - Claude Desktop автоматично вибирає правильний підписаний двійковий файл для вашої платформи.  Також до кожного випуску StackQL додано попередньо зібрані пакети MCP Bundle (`.mcpb`) для встановлення із завантаженого файлу - в обох випадках окремо встановлювати StackQL не потрібно.  Пакети доступні для macOS, Windows і Linux.

  Завантаження MCPB для Linux і Windows за замовчуванням використовують **amd64** (x64). Скористайтеся розкривними списками, щоб вибрати **amd64** або **arm64**.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Завантажити MCPB для macOS"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Завантажити MCPB для Linux"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Завантажити MCPB для Linux (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Завантажити MCPB для Linux (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Завантажити MCPB для Windows"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Завантажити MCPB для Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Завантажити MCPB для Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

Сервер MCP StackQL також зареєстровано в [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) як `io.github.stackql/stackql-mcp`.

:::

:::tip

Пакет - один із кількох способів запустити сервер MCP StackQL.  Повне покрокове керівництво для Claude Desktop див. на сторінці [__Використання StackQL із Claude Desktop__](/getting-started/claude-desktop), а варіанти з npx, Docker, Python, CI і ручним налаштуванням - у розділі [__Встановлення сервера MCP__](#installing-the-mcp-server).

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

  ### ChatGPT і Codex

  StackQL постачається як локальний плагін для настільного застосунку ChatGPT і Codex CLI. Плагін запускає сервер MCP StackQL через stdio на вашому комп'ютері, тож облікові дані провайдерів ніколи його не залишають. Потрібен Node.js 24.22.1 або новіший (22.23.4 чи 26.12.1 для відповідних гілок випусків); плагіну потрібні лише вбудовані модулі Node.js, він завантажує підписаний двійковий файл `stackql` під час першого використання та перевіряє його SHA-256.

  **Настільний застосунок ChatGPT (каталог плагінів):**  
  
  відкрийте каталог плагінів у настільному застосунку ChatGPT, знайдіть **StackQL** і натисніть **Install**, потім запустіть налаштування плагіна; skill `stackql-setup`, що входить до плагіна, допоможе налаштувати облікові дані й виконати перший запит. Сторінка в каталозі має позначку **Desktop only**: плагін встановлюється лише з настільного застосунку, а не з вебверсії чи мобільної версії ChatGPT, і його інструменти доступні в режимі Work, у threads, що виконуються на вашому комп'ютері. Якщо сторінки ще не видно у вашому каталозі, натомість встановіть плагін із маркетплейсу репозиторію.

  **Настільний застосунок ChatGPT (маркетплейс репозиторію):**  
  
  відкрийте **Settings -> Plugins -> Add -> Add from GitHub**, введіть `stackql/stackql`, потім встановіть **StackQL** з розділу **Personal** каталогу плагінів і перезапустіть застосунок.

  **Codex CLI:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  Перезапустіть настільний застосунок ChatGPT або Codex, а потім спробуйте: *Використай StackQL, щоб за потреби встановити провайдер GitHub, а потім перелічи перші п'ять сервісів GitHub.* Облікові дані провайдерів зберігаються у файлі `~/.stackql/.env` під стандартними іменами змінних середовища кожного провайдера (наприклад, `AWS_ACCESS_KEY_ID` і `AWS_SECRET_ACCESS_KEY`); після редагування файлу попросіть агента викликати інструмент `reload_credentials`.

  **Без плагіна:**   
  
  у розділі **Settings -> Plugins -> MCPs -> Connect to a custom MCP** виберіть **STDIO**, укажіть команду `npx` і додайте аргументи `-y` та `@stackql/mcp-server`. Передайте облікові дані провайдерів як змінні середовища в цій формі.

<br />
:::note

Вебверсія та мобільна версія ChatGPT не можуть запускати локальний сервер stdio, тому плагін доступний лише для настільного застосунку та Codex. Відкрита помилка настільного застосунку ChatGPT ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) може приховувати локальні інструменти MCP у сеансах **Chat**; на threads у режимі Work, threads Codex і Codex CLI вона не впливає.

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

  Goose запускає розширення stdio безпосередньо, тож вам потрібен лише засіб запуску `npx` (Node.js 24.22.1 або новіший, чи 22.23.4 або 26.12.1 для відповідних гілок випусків).

  **Goose Desktop:** натисніть кнопку нижче. Goose відкриється, покаже команду, яку збирається виконати, і попросить вас підтвердити її, а потім додасть StackQL як розширення.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Встановити в Goose Desktop</a>
  </Box>

  Кнопка - це глибоке посилання `goose://`; ви також можете вставити його в адресний рядок браузера або відкрити з термінала командою `open` (macOS), `start` (Windows) чи `xdg-open` (Linux):

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  або виконайте `goose configure`, виберіть **Add Extension -> Command-line Extension** і введіть ту саму команду. Коли з'явиться запит, укажіть облікові дані провайдерів як змінні середовища розширення.

<br />
:::note

Goose відмовився від власного каталогу розширень у застосунку на користь [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql), де StackQL зареєстровано як `io.github.stackql/stackql-mcp`. Щойно Goose випустить імпорт із реєстру, StackQL можна буде знайти там без жодних додаткових дій.

:::

</Box>

</TabItem>
</Tabs>

## Встановлення сервера MCP {#installing-the-mcp-server}

Сервер MCP StackQL дає змогу агентам ШІ виконувати запити до хмарних ресурсів і створювати їх за допомогою StackQL. Він постачається кількома каналами - усі вони запускають той самий сервер, тож виберіть канал, що відповідає вашому клієнту та вашим вимогам до довіри. Варіанти нижче наведено приблизно в порядку довіри та простоти.

### Маркетплейси й каталоги {#marketplaces-and-directories}

Сервер опубліковано в Official MCP Registry як `io.github.stackql/stackql-mcp`; реєстри пакетів нижче містять артефакти для розповсюдження, а похідні каталоги беруть дані для своїх сторінок звідти.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Реєстри', value: 'registries', },
    { label: 'Каталоги', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Реєстр | Тип | Спосіб публікації | Сторінка | Примітки |
|---|---|---|---|---|
| Official MCP Registry | Канонічний реєстр метаданих | CLI `mcp-publisher` + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | оголошує всі 8 пакетів (`mcpb x5`, `oci`, `npm`, `pypi`) |
| npm | Реєстр пакетів | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | засіб запуску `npx` |
| PyPI | Реєстр пакетів | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | засіб запуску `uvx` / `pip` |
| Docker Hub | Реєстр контейнерів | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | багатоархітектурний: `amd64` + `arm64` |
| GitHub Actions Marketplace | Маркетплейс CI (дія `setup-stackql-mcp`) | Публічний репозиторій + випуск + сторінка в маркетплейсі | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Перевірений видавець; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Сторінки в каталогах і агрегаторах, приблизно в порядку значущості для пошуку:

| Каталог | Тип | Сторінка |
|---|---|---|
| __Anthropic Connector Directory__ | Каталог у застосунку Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | Каталог у настільному застосунку ChatGPT (лише настільна версія) | знайдіть **StackQL** у настільному застосунку ChatGPT; див. [ChatGPT і Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | Каталог клієнтів IDE | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | Найбільший агрегатор | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Пошук серверів; підтримує реєстр | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Маркетплейс із пошуком | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Агрегатор | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Вебсайт у стилі awesome-list | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Попередньо зібраний пакет `.mcpb` {#prebuilt-mcpb-bundle}

**Коли використовувати:** Claude Desktop без доступу до Connector Directory або коли потрібно закріпити певну версію чи самостійно перевірити контрольні суми перед встановленням.  Для більшості користувачів Claude Desktop рекомендований спосіб встановлення - [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

До кожного випуску StackQL для кожної платформи додано попередньо зібраний пакет MCP Bundle:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

де `<platform>` - одне зі значень `darwin-universal`, `windows-x64` (AMD64), `windows-arm64`, `linux-x64` або `linux-arm64`. Кнопки завантаження для кожної платформи розміщено на вкладці **Claude Desktop** у розділі [Використання з клієнтами MCP](#using-with-mcp-clients). Для кожного пакета на [сторінці випуску](https://github.com/stackql/stackql/releases/latest) є відповідна контрольна сума `.sha256`. Перевірте її, а потім встановіть пакет через **Settings -> Extensions** у Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Повне покрокове керівництво див. на сторінці [Використання StackQL із Claude Desktop](/getting-started/claude-desktop#install-from-a-downloaded-bundle).

### Ручне налаштування `claude_desktop_config.json` {#manual-claude_desktop_configjson}

**Коли використовувати:** двійковий файл `stackql` уже є у вашому PATH, і ви використовуєте Claude Desktop або будь-який інший клієнт MCP, що працює через stdio.

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

Усі три аргументи обов'язкові:

- `--mcp.server.type=stdio` вибирає транспорт stdio, який використовують клієнти, вбудовані в редактори.
- `--approot` спрямовує кеш провайдерів у каталог, доступний для запису. Клієнти MCP можуть запускати сервер із робочим каталогом `/`, який недоступний для запису.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` вимикає журнал аудиту, каталогом якого інакше за замовчуванням є робочий каталог (можливо, недоступний для запису).

Додайте облікові дані провайдерів у блоці `"env"` і налаштуйте контракт безпеки за допомогою `"mode"` - див. [Режими сервера](/command-line-usage/mcp#server-modes).

### `npx` (без встановлення) {#npx-no-install}

**Коли використовувати:** будь-яке середовище Node, коли ви не хочете встановлювати StackQL глобально.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

Засіб запуску [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) завантажує підписаний двійковий файл `stackql` під час першого запуску, перевіряє його SHA-256 і кешує для подальших запусків.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**Коли використовувати:** середовище Python - той самий засіб запуску, запакований для Python, що використовує спільний кеш двійкових файлів, який наповнює засіб запуску npx.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

Або встановіть його в поточне середовище командою `pip install stackql-mcp-server` (Python 3.9+). На PyPI це пакет [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/).

### Docker {#docker}

**Коли використовувати:** контейнеризоване або іншим чином ізольоване середовище виконання. Образ багатоархітектурний (amd64 і arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

Як конфігурація клієнта:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

У Docker Hub це образ [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp).

### CI та агентні робочі процеси (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**Коли використовувати:** запуск сервера в завданні GitHub Actions, щоб агент міг виконувати запити до вашої хмари - і, якщо ви дозволите, виконувати в ній дії - у межах CI.

Дія [`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) встановлює двійковий файл і записує конфігурацію MCP (за замовчуванням у режимі `read_only`). Передайте цю конфігурацію в [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) через `claude_args`:

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

Більше агентних рецептів (аудит хмари, оцінювання витрат тощо) див. у [README дії](https://github.com/stackql/setup-stackql-mcp).

### Вбудований MCP {#embedded-mcp}

**Коли використовувати:** ви створюєте агентний застосунок і хочете включити сервер MCP у власний двійковий файл через вендоринг - без `npx`, без окремого встановлення, без залежності в середовищі виконання.

Нативні бібліотеки запускають підписаний двійковий файл `stackql` і працюють із ним через stdio за допомогою офіційного клієнта MCP SDK відповідної мови, тож ваш застосунок отримує той самий керований інтерфейс SQL до кожного провайдера в реєстрі. Кожна бібліотека за замовчуванням працює в режимі `read_only` - перехід до режиму із записом завжди явний - і може або завантажити й перевірити двійковий файл під час першого запуску, або включити його у вашу збірку через вендоринг, щоб отримати єдиний автономний виконуваний файл. Повний API для кожної мови наведено в [довіднику з вбудованого MCP](/mcp/embedded).

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

Crate `stackql-mcp` запускає двійковий файл і працює з ним через клієнт `rmcp`. Можливість `sidecar`, увімкнена за замовчуванням, завантажує й перевіряє двійковий файл під час першого запуску; можливість `vendored` вбудовує його за допомогою `include_bytes!`, щоб отримати єдиний автономний двійковий файл. MSRV: Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="Переглянути на GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Переглянути на crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Повний довідник: [Вбудований MCP: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

Бібліотека `stackql-mcp-go` запускає двійковий файл і працює з ним через офіційний клієнт MCP SDK для Go. Включіть двійковий файл через вендоринг за допомогою `go:embed`, щоб отримати єдиний автономний двійковий файл, або дозвольте бібліотеці завантажити й перевірити його під час першого запуску. За замовчуванням запускається в режимі `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Переглянути на GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Переглянути на pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Повний довідник: [Вбудований MCP: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

Бібліотека `io.stackql:stackql-mcp` запускає двійковий файл і працює з ним через офіційний клієнт MCP SDK для Kotlin, а супровідний плагін Gradle підключає той самий запуск до збірки. Потрібні JDK 17 і Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="Переглянути на GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Повний довідник: [Вбудований MCP: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

Пакет `StackQL.Mcp` запускає двійковий файл і працює з ним через офіційний клієнт MCP SDK для C#. За замовчуванням працює як sidecar, або ж включіть пакет двійкового файлу через вендоринг як ресурс збірки, щоб отримати автономний виконуваний файл. Потрібен .NET 8 або новіший.

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
    <BinaryDownloadLink iconSize={20} text="Переглянути на GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Повний довідник: [Вбудований MCP: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

Бібліотека `stackql_mcp` орієнтована на середовище виконання Erlang/BEAM і надає сервер як дочірній процес OTP через `child_spec()`, тож його можна розмістити у вашому власному дереві супервізорів. За замовчуванням запускається в режимі `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Переглянути на GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Повний довідник: [Вбудований MCP: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

Пакет `stackql-mcp-swift` запускає двійковий файл і працює з ним через офіційний клієнт MCP SDK для Swift. Двійковий файл `darwin-universal` підписано Developer ID і нотаризовано Apple, тож ви можете включити його в підписаний застосунок `.app` і зберегти чинність нотаризації застосунку. Потрібні macOS 13+ і Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="Переглянути на GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Повний довідник: [Вбудований MCP: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### Модель довіри {#trust-model}

Ті самі властивості безпеки діють для кожного каналу:

- Вбудований двійковий файл `stackql` підписано Authenticode (Windows) і нотаризовано Apple (macOS).
- Кожен пакет `.mcpb` постачається з опублікованою контрольною сумою SHA-256 на сторінці випуску.
- Засоби запуску `npx` і `uvx` перевіряють SHA-256 завантаженого двійкового файлу перед першим використанням.
- Запис у [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) засвідчує хеші для кожної платформи.

## Використання GitHub Actions {#using-github-actions}

[StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo) можна використовувати у ваших робочих процесах GitHub Actions. Доступні такі дії:

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

Розгортайте або тестуйте стеки інфраструктури за допомогою StackQL безпосередньо з ваших робочих процесів GitHub. Ця дія уможливлює робочі процеси інфраструктури як коду (IaC) із синтаксисом, подібним до SQL, і дає змогу визначати й розгортати хмарні ресурси кількох провайдерів та керувати ними в одному робочому процесі.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Переглянути в GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Переглянути на GitHub"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Приклад використання__

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

Налаштуйте StackQL у вашому робочому процесі GitHub Actions.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Переглянути в GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Переглянути на GitHub"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Приклад використання__

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

Встановіть сервер MCP StackQL у вашому робочому процесі GitHub Actions.  Дія встановлює підписаний двійковий файл і записує конфігурацію `mcpServers` (за замовчуванням у режимі `read_only`), яку агентний крок, наприклад `anthropics/claude-code-action`, отримує через `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Переглянути в GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Переглянути на GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Приклад використання__

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

Виконуйте команди StackQL у вашому робочому процесі GitHub Actions.  Запити можна передати безпосередньо або з файлу в репозиторії.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Переглянути в GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Переглянути на GitHub"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Приклад використання__

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

Виконуйте модульні тести у вашому робочому процесі GitHub Actions (для перевірок забезпечення гарантій, керування чи безпеки хмари).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Переглянути в GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Переглянути на GitHub"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Приклад використання__

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


## Інші бібліотеки {#other-libraries}

StackQL надає кілька способів інтеграції для використання в різних мовах програмування та середовищах. Ці бібліотеки розширюють функціональність StackQL і спрощують додавання запитів до хмарних ресурсів і керування ними у ваші наявні застосунки та робочі процеси.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### Пакет Python `pystackql` {#pystackql-python-package}

Обгортка Python для використання StackQL у ваших програмах на Python. Пакет `pystackql` доступний на [__PyPI__](https://pypi.org/project/pystackql/), документація до пакета `pystackql` доступна на [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). Щоб встановити пакет `pystackql`, виконайте таку команду:

```bash
pip install pystackql
```

У наведеному нижче прикладі пакет `pystackql` використовується разом із бібліотекою `pandas`, щоб виконувати запити StackQL і повертати результати в об'єкт `pandas.DataFrame`:

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
