---
title: 설치
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "macOS, Linux, Windows, Docker 또는 클라우드 셸에 StackQL을 설치하고, Claude Desktop, ChatGPT, Goose 등의 MCP 클라이언트나 npx, uvx, pip, Docker, GitHub Actions로 MCP 서버를 사용합니다."
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

여기에서는 다양한 플랫폼에 StackQL을 설치하는 방법을 설명합니다.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: '클라우드 셸', value: 'shells', },
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
  
  macOS에서는 Homebrew와 `pkg` 설치 관리자로 StackQL을 사용할 수 있습니다. 다중 아키텍처 설치 관리자 하나로 ARM(M1/Apple Silicon)과 Intel(x86_64) 아키텍처를 모두 지원합니다.

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl(서명 및 공증된 바이너리)', value: 'curl', },
      { label: '패키지 다운로드', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Homebrew로 설치하려면 터미널에서 다음 명령을 실행합니다:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  서명 및 공증된 `stackql` 실행 바이너리를 현재 디렉터리에 설치하려면 터미널에서 다음 명령을 실행합니다:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL은 macOS용으로 서명 및 공증된 대화형 `pkg` 설치 관리자로 제공됩니다.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="macOS PKG 다운로드"
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

  StackQL은 모든 Linux 아키텍처에서 사용할 수 있습니다.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: '패키지 다운로드', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  사용 중인 Linux 아키텍처(amd64 또는 arm64)용 `stackql` 실행 바이너리를 현재 디렉터리에 설치하려면 터미널에서 다음 명령을 실행합니다:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  또는 여기에서 바이너리를 다운로드할 수 있습니다:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Linux ZIP(amd64) 다운로드"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Linux ZIP(arm64) 다운로드"
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

  Windows에서는 Chocolatey, PowerShell 설치 스크립트, MSI 설치 관리자로 StackQL을 사용할 수 있습니다. 네이티브 AMD64(x64) 및 ARM64 빌드는 MSI 설치 관리자와 `stackql.exe` 파일이 포함된 ZIP 아카이브로 제공됩니다. AMD64 빌드는 에뮬레이션을 통해 ARM64에서도 실행됩니다. 모든 실행 파일은 Authenticode 코드 서명 인증서로 서명되어 있습니다.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'MSI/ZIP 다운로드', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Chocolatey로 설치하려면 PowerShell 또는 `cmd` 터미널에서 다음 명령을 실행합니다:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Authenticode 서명 실행 파일을 현재 디렉터리에 설치하려면 PowerShell 터미널에서 다음을 실행합니다:

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  아래에서 서명된 Windows용 `stackql` 설치 관리자 또는 실행 파일을 다운로드하세요. 다운로드 버튼을 클릭하면 기본적으로 AMD64가 선택됩니다. AMD64 또는 네이티브 ARM64를 선택하려면 버튼의 드롭다운을 사용하세요.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows MSI 다운로드"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Windows MSI(amd64) 다운로드', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Windows MSI(arm64) 다운로드', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows ZIP 다운로드"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Windows ZIP(amd64) 다운로드', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Windows ZIP(arm64) 다운로드', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  StackQL 빌드는 [Docker Hub](https://hub.docker.com/u/stackql)에 게시됩니다.

  __Docker Hub__  

  StackQL 컨테이너 이미지를 가져오려면 다음 명령을 실행합니다:

  ```bash
  docker pull stackql/stackql
  ```

  `v1.0`부터 이미지는 Ubuntu 26.04 LTS를 기반으로 합니다. Python 3.13이 사전 설치된 변형은 `-python3.13` 태그 접미사(별칭 `-python3`)로 게시됩니다. 예를 들면 다음과 같습니다:

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

`stackql-deploy` 도구는 StackQL을 기반으로 구축된 상태 비저장 IaC 프레임워크입니다. 전체 문서는 [__StackQL Deploy 문서__](https://stackql-deploy.io/)를 참조하세요.

__Linux / macOS__

최신 `stackql-deploy` 바이너리를 한 번에 다운로드하고 압축을 풉니다. 설치 관리자는 OS와 아키텍처(유니버설 macOS 바이너리를 통한 Apple Silicon 및 Intel 포함)를 감지하고 올바른 릴리스 자산을 자동으로 가져옵니다.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

최신 `stackql-deploy` 바이너리를 현재 디렉터리에 다운로드하고 압축을 풉니다.
설치 관리자는 플랫폼을 감지하고 올바른 릴리스 자산을 자동으로 가져옵니다.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Rust 도구 체인이 설치되어 있으면 이 명령은 [__crates.io__](https://crates.io/crates/stackql-deploy)에서 바이너리를 직접 빌드하고 설치합니다.
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

  ## 클라우드 셸

  StackQL은 주요 클라우드 및 데이터 플랫폼에 내장된 클라우드 셸과 웹 터미널에서 직접 사용할 수 있습니다. 이러한 환경에는 로그인한 ID의 권한이 미리 부여되어 있습니다. 따라서 별도의 인증 설정이 필요 없어 원활하게 사용할 수 있습니다.

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

  AWS CloudShell은 AWS CLI가 사전 설치 및 인증된 브라우저 기반 셸을 제공합니다. AWS CloudShell에서 StackQL을 실행하면 추가 인증 단계 없이 AWS 리소스를 쿼리하고 관리할 수 있습니다. 자세한 지침은 [AWS CloudShell 튜토리얼](/quick-starts/aws/aws-cloud-shell)을 참조하세요.

  먼저 StackQL 패키지를 다운로드합니다:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  그런 다음 StackQL AWS CloudShell 스크립트를 실행합니다:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  이 스크립트는 AWS CloudShell 자격 증명을 사용하는 StackQL 명령 셸을 시작하므로 바로 AWS 리소스 쿼리를 시작할 수 있습니다.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell은 Azure CLI가 Azure 계정으로 사전 인증된 셸 환경을 브라우저에서 제공합니다. Azure Cloud Shell에서 StackQL을 사용하면 추가 설정 없이 Azure 리소스를 원활하게 쿼리할 수 있습니다. 자세한 내용은 [Azure Cloud Shell 가이드](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell)를 참조하세요.

  먼저 StackQL 패키지를 다운로드합니다:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  그런 다음 StackQL Azure Cloud Shell 스크립트를 실행합니다:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  이 스크립트는 Azure Cloud Shell 자격 증명을 사용하도록 StackQL 세션을 자동으로 구성하므로 바로 Azure 리소스를 쿼리할 수 있습니다.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell은 Google Cloud CLI가 이미 인증된 개발 및 운영 환경을 제공합니다. Google Cloud Shell에서 StackQL을 실행하면 기존 인증을 사용하여 GCP 리소스를 쿼리할 수 있습니다. 자세한 내용은 [Google Cloud Shell 가이드](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell)를 참조하세요.

  먼저 StackQL 패키지를 다운로드합니다:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  그런 다음 StackQL Google Cloud Shell 스크립트를 실행합니다:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  이 스크립트는 Google Cloud Shell 자격 증명을 사용하도록 StackQL을 설정하므로 SQL 구문으로 바로 GCP 리소스 쿼리를 시작할 수 있습니다.

  </TabItem>
  <TabItem value="databricks">

  ### Databricks 웹 터미널

  Databricks 작업 영역에는 로그인한 사용자로 실행되는 웹 터미널이 포함되어 있습니다. 이 터미널에서 StackQL을 실행하면 별도의 인증 설정 없이 Databricks ID로 작업 영역을 쿼리할 수 있습니다. 예제 쿼리와 계정 수준 인증은 [Databricks 웹 터미널 가이드](/blog/tutorials/stackql-in-databricks-web-terminal)를 참조하세요.

  먼저 StackQL 패키지를 다운로드합니다:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  그런 다음 StackQL Databricks 셸 스크립트를 실행합니다:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  이 스크립트는 Databricks 작업 영역 ID를 사용하는 StackQL 세션을 시작하므로 `databricks_workspace.iam.vw_user_entitlements` 같은 작업 영역 범위의 리소스를 바로 쿼리할 수 있습니다. 계정 수준 쿼리(프로비저닝, 청구, 계정 IAM)를 실행하려면 가이드의 설명에 따라 Databricks OAuth2 서비스 주체 변수를 설정하세요.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## MCP 클라이언트에서 사용 {#using-with-mcp-clients}

StackQL MCP 서버는 stdio를 통해 로컬에서 실행되므로, 어떤 클라이언트를 사용하든 프로바이더 자격 증명은 사용자의 머신에 그대로 남습니다. 아래에서 클라이언트를 선택하세요. 각 탭은 디렉터리 또는 원클릭 설치 방법을 먼저 안내하고, 대체 방법으로 수동 구성을 안내합니다. `npx`, Python, Docker, CI를 포함한 모든 배포 채널은 [MCP 서버 설치](#installing-the-mcp-server)를 참조하세요.

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

  Claude Desktop에 StackQL MCP 서버를 설치할 때 권장하는 방법은 [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql)(**Settings -> Connectors -> Add -> Browse Connectors**에서 **StackQL** 검색)를 이용하는 것입니다. Claude Desktop이 플랫폼에 맞는 서명된 바이너리를 자동으로 선택합니다. 또는 다운로드한 파일로 설치할 수 있도록 모든 StackQL 릴리스에 사전 빌드된 MCP Bundle(`.mcpb`)이 첨부되어 있습니다. 어느 방법이든 StackQL을 별도로 설치할 필요는 없습니다. 번들은 macOS, Windows, Linux용으로 제공됩니다.

  Linux 및 Windows MCPB 다운로드의 기본값은 **amd64**(x64)입니다. 드롭다운을 사용하여 **amd64** 또는 **arm64**를 선택하세요.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="macOS MCPB 다운로드"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Linux MCPB 다운로드"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Linux MCPB(amd64) 다운로드', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Linux MCPB(arm64) 다운로드', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Windows MCPB 다운로드"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Windows MCPB(amd64) 다운로드', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Windows MCPB(arm64) 다운로드', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

StackQL MCP 서버는 [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)에도 `io.github.stackql/stackql-mcp` 이름으로 등록되어 있습니다.

:::

:::tip

번들은 StackQL MCP 서버를 실행하는 여러 방법 중 하나입니다. Claude Desktop의 전체 안내는 [__Claude Desktop에서 StackQL 사용__](/getting-started/claude-desktop)을, npx, Docker, Python, CI 및 수동 옵션은 [__MCP 서버 설치__](#installing-the-mcp-server)를 참조하세요.

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

  ### ChatGPT 및 Codex

  StackQL은 ChatGPT 데스크톱 앱과 Codex CLI용 로컬 플러그인으로 제공됩니다. 플러그인은 사용자의 머신에서 stdio를 통해 StackQL MCP 서버를 실행하므로 프로바이더 자격 증명이 머신 밖으로 나가지 않습니다. Node.js 24.22.1 이상이 필요합니다(해당 릴리스 라인에서는 22.23.4 또는 26.12.1). 플러그인은 Node.js 기본 제공 모듈만 사용하며, 처음 사용할 때 서명된 `stackql` 바이너리를 다운로드하고 SHA-256을 검증합니다.

  **ChatGPT 데스크톱 앱(플러그인 디렉터리):**
  
  ChatGPT 데스크톱 앱에서 플러그인 디렉터리를 열고 **StackQL**을 검색한 다음 **Install**을 클릭하고 플러그인 설정을 실행하세요. 함께 제공되는 `stackql-setup` 스킬이 자격 증명 설정과 첫 번째 쿼리를 안내합니다. 등록 항목에는 **Desktop only** 배지가 붙어 있습니다. 플러그인은 ChatGPT 웹이나 모바일이 아닌 데스크톱 앱에서만 설치되며, 플러그인의 도구는 Work 모드, 즉 사용자의 컴퓨터에서 실행되는 스레드에서 사용할 수 있습니다. 디렉터리에 아직 등록 항목이 표시되지 않으면 대신 리포지토리 마켓플레이스에서 설치하세요.

  **ChatGPT 데스크톱 앱(리포지토리 마켓플레이스):**
  
  **Settings -> Plugins -> Add -> Add from GitHub** 메뉴를 열고 `stackql/stackql` 리포지토리를 입력한 다음, 플러그인 디렉터리의 **Personal** 섹션에서 **StackQL**을 설치하고 앱을 다시 시작하세요.

  **Codex CLI:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  ChatGPT 데스크톱 또는 Codex를 다시 시작한 후 다음과 같이 요청해 보세요: *StackQL을 사용해서 필요하면 GitHub 프로바이더를 설치하고, 처음 5개의 GitHub 서비스를 나열해 주세요.* 프로바이더 자격 증명은 각 프로바이더의 표준 환경 변수 이름(예: `AWS_ACCESS_KEY_ID` 및 `AWS_SECRET_ACCESS_KEY`)으로 `~/.stackql/.env` 파일에 넣습니다. 파일을 편집한 후에는 에이전트에게 `reload_credentials` 도구를 호출하도록 요청하세요.

  **플러그인 없이 사용:**
  
  **Settings -> Plugins -> MCPs -> Connect to a custom MCP**에서 **STDIO**를 선택하고, 명령을 `npx`로 설정한 다음 인수 `-y` 및 `@stackql/mcp-server`를 추가하세요. 프로바이더 자격 증명은 같은 양식에서 환경 변수로 제공하세요.

<br />
:::note

ChatGPT 웹과 모바일은 로컬 stdio 서버를 실행할 수 없으므로 플러그인은 데스크톱과 Codex에서만 사용할 수 있습니다. 아직 해결되지 않은 ChatGPT 데스크톱 버그([openai/codex#38162](https://github.com/openai/codex/issues/38162))로 인해 **Chat** 세션에서 로컬 MCP 도구가 표시되지 않을 수 있습니다. Work 모드 스레드, Codex 스레드, Codex CLI는 영향을 받지 않습니다.

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

  Goose는 stdio 확장을 직접 실행하므로 `npx` 런처만 있으면 됩니다(Node.js 24.22.1 이상, 해당 릴리스 라인에서는 22.23.4 또는 26.12.1).

  **Goose Desktop:** 아래 버튼을 클릭하세요. Goose가 열리고, 실행할 명령을 표시하여 확인을 요청한 다음 StackQL을 확장으로 추가합니다.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Goose Desktop에 설치</a>
  </Box>

  이 버튼은 `goose://` 딥 링크입니다. 링크를 브라우저 주소 표시줄에 붙여 넣거나, 터미널에서 `open`(macOS), `start`(Windows) 또는 `xdg-open`(Linux) 명령으로 열 수도 있습니다:

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  또는 `goose configure` 명령을 실행하고 **Add Extension -> Command-line Extension**을 선택한 다음 같은 명령을 입력하세요. 입력하라는 메시지가 표시되면 프로바이더 자격 증명을 확장의 환경 변수로 제공하세요.

<br />
:::note

Goose는 앱 내 확장 디렉터리를 폐지하고 [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)로 전환했습니다. StackQL은 이 레지스트리에 `io.github.stackql/stackql-mcp` 이름으로 등록되어 있습니다. Goose가 레지스트리 가져오기 기능을 제공하면 별도의 조치 없이 Goose에서 StackQL을 찾을 수 있습니다.

:::

</Box>

</TabItem>
</Tabs>

## MCP 서버 설치 {#installing-the-mcp-server}

StackQL MCP 서버를 사용하면 AI 에이전트가 StackQL로 클라우드 리소스를 쿼리하고 프로비저닝할 수 있습니다. 이 서버는 여러 채널로 배포됩니다. 모든 채널은 같은 서버를 실행하므로, 사용하는 클라이언트와 신뢰 요구 사항에 맞는 채널을 선택하세요. 아래 옵션은 신뢰도와 사용 편의성 순으로 대략 정렬되어 있습니다.

### 마켓플레이스 및 디렉터리 {#marketplaces-and-directories}

이 서버는 Official MCP Registry에 `io.github.stackql/stackql-mcp` 이름으로 게시되어 있습니다. 아래의 패키지 레지스트리는 배포 가능한 아티팩트를 제공하며, 다운스트림 디렉터리는 그곳에서 등록 정보를 가져옵니다.

<Tabs
  defaultValue="registries"
  values={[
    { label: '레지스트리', value: 'registries', },
    { label: '디렉터리', value: 'directories', },
  ]}
>
<TabItem value="registries">

| 레지스트리 | 유형 | 게시 방법 | 등록 페이지 | 참고 |
|---|---|---|---|---|
| Official MCP Registry | 정식 메타데이터 레지스트리 | `mcp-publisher` CLI + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | 8개 패키지(`mcpb x5`, `oci`, `npm`, `pypi`)를 모두 알림 |
| npm | 패키지 레지스트리 | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | `npx` 런처 |
| PyPI | 패키지 레지스트리 | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | `uvx` / `pip` 런처 |
| Docker Hub | 컨테이너 레지스트리 | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | 다중 아키텍처 `amd64` + `arm64` |
| GitHub Actions Marketplace | CI 마켓플레이스(`setup-stackql-mcp` 액션) | 공개 리포지토리 + 릴리스 + 마켓플레이스 등록 | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | 인증된 게시자, [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

디렉터리 및 애그리게이터 등록 목록입니다. 검색 노출 측면의 중요도 순으로 대략 정렬되어 있습니다:

| 디렉터리 | 유형 | 등록 페이지 |
|---|---|---|
| __Anthropic Connector Directory__ | Claude Desktop 앱 내 디렉터리 | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | ChatGPT 데스크톱 앱 내 디렉터리(데스크톱 전용) | ChatGPT 데스크톱 앱에서 **StackQL** 검색, [ChatGPT 및 Codex](#using-with-mcp-clients) 참조 |
| __Cursor Directory__ | IDE 클라이언트 디렉터리 | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | 최대 규모의 애그리게이터 | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | 검색 노출, 레지스트리 후원사 | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | 검색 가능한 마켓플레이스 | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | 애그리게이터 | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Awesome 목록 사이트 | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### 사전 빌드된 `.mcpb` 번들 {#prebuilt-mcpb-bundle}

**사용 시기:** Connector Directory에 액세스할 수 없는 Claude Desktop 환경이거나, 특정 버전을 고정하거나 설치 전에 체크섬을 직접 검증하려는 경우입니다. 대부분의 Claude Desktop 사용자에게는 [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql)가 권장 설치 방법입니다.

모든 StackQL 릴리스에는 플랫폼별로 사전 빌드된 MCP Bundle이 첨부되어 있습니다:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

여기서 `<platform>` 값은 `darwin-universal`, `windows-x64`(AMD64), `windows-arm64`, `linux-x64`, `linux-arm64` 중 하나입니다. 플랫폼별 다운로드 버튼은 [MCP 클라이언트에서 사용](#using-with-mcp-clients) 섹션의 **Claude Desktop** 탭에 있습니다. 각 번들에 해당하는 `.sha256` 체크섬은 [릴리스 페이지](https://github.com/stackql/stackql/releases/latest)에 있습니다. 체크섬을 검증한 다음 Claude Desktop의 **Settings -> Extensions**에서 설치하세요:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

전체 안내는 [Claude Desktop에서 StackQL 사용](/getting-started/claude-desktop#install-from-a-downloaded-bundle)을 참조하세요.

### `claude_desktop_config.json` 수동 구성 {#manual-claude_desktop_configjson}

**사용 시기:** `stackql` 바이너리가 이미 PATH에 있고 Claude Desktop 또는 다른 stdio MCP 클라이언트를 사용하는 경우입니다.

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

세 인수 모두 반드시 필요합니다:

- `--mcp.server.type=stdio` 인수는 편집기에 내장된 클라이언트가 사용하는 stdio 전송 방식을 선택합니다.
- `--approot` 인수는 프로바이더 캐시가 쓰기 가능한 디렉터리를 가리키도록 합니다. MCP 클라이언트는 작업 디렉터리를 쓰기 불가능한 `/` 디렉터리로 설정한 상태로 서버를 시작할 수 있습니다.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` 인수는 감사 로그를 비활성화합니다. 이 인수가 없으면 감사 로그 디렉터리의 기본값은 (쓰기 불가능할 수 있는) 작업 디렉터리가 됩니다.

`"env"` 블록으로 프로바이더 자격 증명을 추가하고 `"mode"` 키로 안전 계약을 조정하세요. 자세한 내용은 [서버 모드](/command-line-usage/mcp#server-modes)를 참조하세요.

### `npx`(설치 불필요) {#npx-no-install}

**사용 시기:** StackQL을 전역으로 설치하지 않으려는 경우, 모든 Node 환경에서 사용할 수 있습니다.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

[`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) 런처는 처음 실행할 때 서명된 `stackql` 바이너리를 다운로드하고 SHA-256을 검증한 다음, 이후 실행을 위해 캐시합니다.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**사용 시기:** Python 환경입니다. Python용으로 패키징된 같은 런처이며, npx 런처가 채우는 바이너리 캐시를 공유합니다.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

또는 `pip install stackql-mcp-server` 명령(Python 3.9+)으로 현재 환경에 설치할 수 있습니다. 패키지는 PyPI의 [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/)입니다.

### Docker {#docker}

**사용 시기:** 컨테이너화되었거나 그 밖의 방식으로 격리된 런타임입니다. 이미지는 다중 아키텍처(amd64 및 arm64)를 지원합니다.

```bash
docker run -i --rm stackql/stackql-mcp
```

클라이언트 구성으로는 다음과 같습니다:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

이미지는 Docker Hub의 [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp)입니다.

### CI 및 에이전트형 워크플로(GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**사용 시기:** CI의 일부로 에이전트가 클라우드를 쿼리하고, 허용하는 경우 클라우드에서 작업도 수행할 수 있도록 GitHub Actions 작업 안에서 서버를 실행합니다.

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) 액션은 바이너리를 설치하고 MCP 구성을 작성합니다(기본값은 `read_only` 모드). 이 구성을 `claude_args` 입력을 통해 [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) 액션에 전달합니다:

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

더 많은 에이전트형 레시피(클라우드 감사, 비용 추정 등)는 [액션 README](https://github.com/stackql/setup-stackql-mcp)를 참조하세요.

### 임베디드 MCP {#embedded-mcp}

**사용 시기:** 에이전트형 앱을 빌드하면서 MCP 서버를 자체 바이너리에 벤더링하려는 경우입니다. `npx`, 별도 설치, 런타임 종속성이 모두 필요 없습니다.

네이티브 라이브러리는 각 언어의 공식 MCP SDK 클라이언트 뒤에서 서명된 `stackql` 바이너리를 stdio로 실행합니다. 따라서 앱은 레지스트리의 모든 프로바이더에 대해 거버넌스가 적용된 동일한 SQL 인터페이스를 사용할 수 있습니다. 각 라이브러리의 기본값은 `read_only` 모드이며, 쓰기 가능한 모드로의 승격은 항상 명시적으로 이루어집니다. 또한 처음 실행할 때 바이너리를 다운로드하고 검증하거나, 바이너리를 빌드에 벤더링하여 자체 포함된 단일 실행 파일을 만들 수 있습니다. 언어별 전체 API는 [임베디드 MCP 참조](/mcp/embedded)에 있습니다.

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

`stackql-mcp` 크레이트는 `rmcp` 클라이언트 뒤에서 바이너리를 실행합니다. 기본 `sidecar` 기능은 처음 실행할 때 바이너리를 다운로드하고 검증합니다. `vendored` 기능은 `include_bytes!` 매크로로 바이너리를 내장하여 자체 포함된 단일 바이너리를 만듭니다. MSRV는 Rust 1.88입니다.

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
    <BinaryDownloadLink iconSize={20} text="GitHub에서 보기" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="crates.io에서 보기" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

전체 참조: [임베디드 MCP: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

`stackql-mcp-go` 라이브러리는 공식 Go MCP SDK 클라이언트 뒤에서 바이너리를 실행합니다. `go:embed` 지시문으로 바이너리를 벤더링하여 자체 포함된 단일 바이너리를 만들거나, 처음 실행할 때 바이너리를 다운로드하고 검증하도록 할 수 있습니다. 기본적으로 `read_only` 모드로 시작합니다.

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
    <BinaryDownloadLink iconSize={20} text="GitHub에서 보기" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="pkg.go.dev에서 보기" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

전체 참조: [임베디드 MCP: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

`io.stackql:stackql-mcp` 라이브러리는 공식 Kotlin MCP SDK 클라이언트 뒤에서 바이너리를 실행하며, 함께 제공되는 Gradle 플러그인은 같은 실행 과정을 빌드에 연결합니다. JDK 17 및 Kotlin 2.x가 필요합니다.

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
    <BinaryDownloadLink iconSize={20} text="GitHub에서 보기" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

전체 참조: [임베디드 MCP: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

`StackQL.Mcp` 패키지는 공식 C# MCP SDK 클라이언트 뒤에서 바이너리를 실행합니다. 기본적으로 사이드카 방식이며, 번들을 빌드 리소스로 벤더링하여 자체 포함된 실행 파일을 만들 수도 있습니다. .NET 8 이상이 필요합니다.

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
    <BinaryDownloadLink iconSize={20} text="GitHub에서 보기" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

전체 참조: [임베디드 MCP: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

`stackql_mcp` 라이브러리는 Erlang/BEAM 런타임을 대상으로 하며 `child_spec()` 함수를 통해 서버를 OTP 자식 프로세스로 노출하므로 자체 슈퍼비전 트리 안에 배치할 수 있습니다. 기본적으로 `read_only` 모드로 시작합니다.

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
    <BinaryDownloadLink iconSize={20} text="GitHub에서 보기" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

전체 참조: [임베디드 MCP: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

`stackql-mcp-swift` 패키지는 공식 Swift MCP SDK 클라이언트 뒤에서 바이너리를 실행합니다. `darwin-universal` 바이너리는 Developer ID로 서명되고 Apple의 공증을 받았으므로, 서명된 `.app` 번들 안에 포함해도 앱의 공증이 유효하게 유지됩니다. macOS 13+ 및 Swift 6.1(Xcode 16.3+)이 필요합니다.

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
    <BinaryDownloadLink iconSize={20} text="GitHub에서 보기" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

전체 참조: [임베디드 MCP: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### 신뢰 모델 {#trust-model}

모든 채널에 동일한 보안 속성이 적용됩니다:

- 내장된 `stackql` 바이너리는 Authenticode로 서명(Windows)되고 Apple의 공증(macOS)을 받았습니다.
- 모든 `.mcpb` 번들에는 릴리스 페이지에 게시된 SHA-256 체크섬이 함께 제공됩니다.
- `npx` 및 `uvx` 런처는 처음 사용하기 전에 다운로드한 바이너리의 SHA-256을 검증합니다.
- [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) 항목은 플랫폼별 해시를 증명합니다.

## GitHub Actions 사용 {#using-github-actions}

GitHub Actions 워크플로에서 [StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo)를 사용할 수 있습니다. 다음 액션을 사용할 수 있습니다:

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

GitHub 워크플로에서 직접 StackQL을 사용하여 인프라 스택을 배포하거나 테스트합니다. 이 액션은 SQL과 유사한 구문을 사용하는 코드형 인프라(IaC) 워크플로를 지원하므로, 단일 워크플로에서 여러 프로바이더에 걸쳐 클라우드 리소스를 정의, 배포, 관리할 수 있습니다.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace에서 보기"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="GitHub에서 보기"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__사용 예__

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

GitHub Actions 워크플로에서 StackQL을 설정합니다.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace에서 보기"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="GitHub에서 보기"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__사용 예__

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

GitHub Actions 워크플로에 StackQL MCP 서버를 설치합니다. 이 액션은 서명된 바이너리를 설치하고 `mcpServers` 구성(기본값은 `read_only` 모드)을 작성합니다. `anthropics/claude-code-action` 같은 에이전트형 단계는 `claude_args` 입력을 통해 이 구성을 사용합니다.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace에서 보기"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub에서 보기"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__사용 예__

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

GitHub Actions 워크플로에서 StackQL 명령을 실행합니다. 쿼리는 인라인으로 지정하거나 리포지토리의 파일에서 제공할 수 있습니다.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace에서 보기"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="GitHub에서 보기"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__사용 예__

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

GitHub Actions 워크플로에서 단위 테스트를 수행합니다(보증, 거버넌스 또는 클라우드 보안 점검용).

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="GitHub Marketplace에서 보기"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="GitHub에서 보기"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__사용 예__

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


## 기타 라이브러리 {#other-libraries}

StackQL은 다양한 프로그래밍 언어와 환경에서 사용할 수 있는 여러 통합 방법을 제공합니다. 이러한 라이브러리는 StackQL의 기능을 확장하여 클라우드 리소스 쿼리 및 관리를 기존 애플리케이션과 워크플로에 쉽게 통합할 수 있게 합니다.

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### `pystackql` Python 패키지 {#pystackql-python-package}

Python 프로그램에서 StackQL을 사용하기 위한 Python 래퍼입니다. `pystackql` 패키지는 [__PyPI__](https://pypi.org/project/pystackql/)에서 제공되며, `pystackql` 패키지에 대한 문서는 [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/)에서 볼 수 있습니다. `pystackql` 패키지를 설치하려면 다음 명령을 실행합니다:

```bash
pip install pystackql
```

다음 예제는 `pystackql` 패키지를 `pandas` 라이브러리와 함께 사용하여 StackQL 쿼리를 실행하고 결과를 `pandas.DataFrame` 객체로 반환하는 방법을 보여 줍니다:

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
