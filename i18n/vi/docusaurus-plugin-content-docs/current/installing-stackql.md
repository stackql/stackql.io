---
title: Cài đặt
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "Cài đặt StackQL trên macOS, Linux, Windows, Docker hoặc shell đám mây, và dùng máy chủ MCP StackQL từ Claude Desktop, ChatGPT, Goose và các ứng dụng khách MCP khác, hoặc qua npx, uvx, pip, Docker và GitHub Actions."
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

Trang này cung cấp hướng dẫn cài đặt StackQL trên nhiều nền tảng khác nhau.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Shell đám mây', value: 'shells', },
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
  
  StackQL khả dụng trên macOS qua Homebrew và trình cài đặt `pkg`; cả kiến trúc ARM (M1/Apple Silicon) và Intel (x86_64) đều được hỗ trợ bằng một trình cài đặt đa kiến trúc duy nhất.

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (tệp nhị phân được ký và công chứng)', value: 'curl', },
      { label: 'Tải xuống gói', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Để cài đặt qua Homebrew, chạy lệnh sau trong terminal của bạn:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  Để cài đặt tệp thực thi `stackql` được ký và công chứng vào thư mục hiện tại, chạy lệnh sau trong terminal của bạn:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL khả dụng dưới dạng trình cài đặt `pkg` tương tác cho macOS, được ký và công chứng.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="Tải xuống PKG macOS"
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

  StackQL khả dụng cho mọi kiến trúc Linux.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'Tải xuống gói', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  Để cài đặt tệp thực thi `stackql` cho kiến trúc Linux của bạn (amd64 hoặc arm64) vào thư mục hiện tại, chạy lệnh sau trong terminal của bạn:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  Ngoài ra, bạn có thể tải xuống các tệp nhị phân tại đây:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Tải xuống ZIP Linux (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Tải xuống ZIP Linux (arm64)"
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

  StackQL khả dụng trên Windows qua Chocolatey, tập lệnh cài đặt PowerShell và trình cài đặt MSI. Các bản dựng gốc AMD64 (x64) và ARM64 được cung cấp dưới dạng trình cài đặt MSI và tệp nén ZIP chứa `stackql.exe`. Bản dựng AMD64 cũng chạy được trên ARM64 thông qua mô phỏng. Mọi tệp thực thi đều được ký bằng chứng chỉ ký mã Authenticode.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'Tải xuống MSI/ZIP', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Để cài đặt qua Chocolatey, chạy lệnh sau trong terminal PowerShell hoặc `cmd` của bạn:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Để cài đặt một tệp thực thi Authenticode vào thư mục hiện tại, chạy lệnh sau trong một terminal PowerShell: 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  Tải xuống trình cài đặt hoặc tệp thực thi `stackql` được ký cho Windows bên dưới. Khi bạn nhấp vào một nút tải xuống, AMD64 được chọn theo mặc định; dùng danh sách thả xuống của nút để chọn AMD64 hoặc ARM64 gốc.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Tải xuống MSI Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Tải xuống MSI Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Tải xuống MSI Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Tải xuống ZIP Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Tải xuống ZIP Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Tải xuống ZIP Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  Các bản dựng StackQL được phát hành trên [Docker Hub](https://hub.docker.com/u/stackql).  

  __Docker Hub__  

  Để tải xuống image container StackQL, chạy lệnh sau:

  ```bash
  docker pull stackql/stackql
  ```

  Từ `v1.0`, image dựa trên Ubuntu 26.04 LTS, và một biến thể đã cài đặt sẵn Python 3.13 được phát hành với hậu tố tag `-python3.13` (bí danh `-python3`), ví dụ:

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

`stackql-deploy` là một framework IaC phi trạng thái dựa trên StackQL. Để biết tài liệu đầy đủ, hãy xem [__Tài liệu StackQL Deploy__](https://stackql-deploy.io/).

__Linux / macOS__

Tải xuống và giải nén tệp nhị phân `stackql-deploy` mới nhất trong một bước. Trình cài đặt phát hiện hệ điều hành và kiến trúc của bạn (bao gồm Apple Silicon và Intel qua một tệp nhị phân macOS universal) và tự động tải tệp phát hành phù hợp.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Tải xuống và giải nén tệp nhị phân `stackql-deploy` mới nhất vào thư mục hiện tại.
Trình cài đặt phát hiện nền tảng của bạn và tự động tải tệp phát hành phù hợp.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Nếu bạn đã cài đặt toolchain Rust, lệnh này dựng và cài đặt tệp nhị phân trực tiếp từ [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## Shell đám mây

  Bạn có thể dùng StackQL trực tiếp từ shell đám mây và terminal web tích hợp sẵn của các nền tảng đám mây và nền tảng dữ liệu chính. Cách này mang lại trải nghiệm liền mạch vì các môi trường này đã được ủy quyền sẵn bằng danh tính mà bạn đang đăng nhập, nên bạn không cần cấu hình xác thực riêng.

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

  AWS CloudShell cung cấp một shell chạy trên trình duyệt, với AWS CLI đã được cài đặt và xác thực sẵn. Khi chạy StackQL trong AWS CloudShell, bạn có thể truy vấn và quản lý tài nguyên AWS mà không cần thêm bước xác thực nào. Để biết các bước chi tiết, hãy xem [hướng dẫn AWS CloudShell](/quick-starts/aws/aws-cloud-shell) của chúng tôi.

  Trước tiên, tải xuống gói StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  Sau đó, chạy tập lệnh StackQL cho AWS CloudShell:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  Tập lệnh này khởi động shell lệnh StackQL bằng thông tin xác thực AWS CloudShell của bạn, cho phép bạn bắt đầu truy vấn tài nguyên AWS ngay lập tức.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell cung cấp một môi trường shell truy cập được từ trình duyệt, với Azure CLI đã được xác thực sẵn bằng tài khoản Azure của bạn. Khi dùng StackQL trong Azure Cloud Shell, bạn có thể truy vấn tài nguyên Azure một cách liền mạch mà không cần cấu hình thêm. Để biết đầy đủ chi tiết, hãy xem [hướng dẫn Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell) của chúng tôi.  

  Trước tiên, tải xuống gói StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  Sau đó, chạy tập lệnh StackQL cho Azure Cloud Shell:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  Tập lệnh này tự động cấu hình một phiên StackQL để dùng thông tin xác thực Azure Cloud Shell của bạn, giúp bạn truy vấn tài nguyên Azure ngay lập tức.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell cung cấp một môi trường phát triển và vận hành với Google Cloud CLI đã được xác thực sẵn. Khi chạy StackQL trong Google Cloud Shell, bạn có thể truy vấn tài nguyên GCP bằng xác thực hiện có của mình. Tìm hiểu thêm trong [hướng dẫn Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell) của chúng tôi.  

  Trước tiên, tải xuống gói StackQL:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  Sau đó, chạy tập lệnh StackQL cho Google Cloud Shell:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  Tập lệnh này cấu hình StackQL để dùng thông tin xác thực Google Cloud Shell của bạn, cho phép bạn bắt đầu truy vấn tài nguyên GCP ngay lập tức bằng cú pháp SQL.

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web Terminal

  Không gian làm việc Databricks có một terminal web chạy dưới danh nghĩa người dùng đang đăng nhập. Khi chạy StackQL tại đó, bạn có thể truy vấn không gian làm việc bằng danh tính Databricks của mình mà không cần cấu hình xác thực riêng. Để biết các truy vấn ví dụ và cách xác thực ở cấp tài khoản, hãy xem [hướng dẫn Databricks Web Terminal](/blog/tutorials/stackql-in-databricks-web-terminal) của chúng tôi.

  Trước tiên, tải xuống gói StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  Sau đó, chạy tập lệnh shell StackQL cho Databricks:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  Lệnh này khởi động một phiên StackQL bằng danh tính không gian làm việc Databricks của bạn, nhờ đó bạn có thể truy vấn ngay các tài nguyên trong phạm vi không gian làm việc, chẳng hạn như `databricks_workspace.iam.vw_user_entitlements`. Đối với truy vấn ở cấp tài khoản (cấp phát, thanh toán, IAM tài khoản), hãy đặt các biến service principal OAuth2 của Databricks như mô tả trong hướng dẫn.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Sử dụng với ứng dụng khách MCP {#using-with-mcp-clients}

Máy chủ MCP StackQL chạy cục bộ qua stdio, nên thông tin xác thực provider luôn nằm trên máy của bạn, dù bạn dùng ứng dụng khách nào. Chọn ứng dụng khách của bạn bên dưới: mỗi tab trình bày trước cách cài đặt qua thư mục hoặc bằng một cú nhấp, sau đó là cấu hình thủ công làm fallback. Để biết mọi kênh phân phối, bao gồm `npx`, Python, Docker và CI, hãy xem [Cài đặt máy chủ MCP](#installing-the-mcp-server).

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

  Cách được khuyên dùng để cài đặt máy chủ MCP StackQL vào Claude Desktop là từ [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Settings -> Connectors -> Add -> Browse Connectors**, tìm **StackQL**) - Claude Desktop tự động chọn đúng tệp nhị phân được ký cho nền tảng của bạn.  Ngoài ra, các MCP Bundle (`.mcpb`) dựng sẵn được đính kèm vào mọi bản phát hành StackQL để cài đặt từ tệp đã tải xuống - dù theo cách nào, bạn cũng không cần cài đặt StackQL riêng.  Bundle khả dụng cho macOS, Windows và Linux.

  Bản tải xuống MCPB cho Linux và Windows mặc định là **amd64** (x64). Dùng danh sách thả xuống để chọn **amd64** hoặc **arm64**.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Tải xuống MCPB macOS"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Tải xuống MCPB Linux"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Tải xuống MCPB Linux (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Tải xuống MCPB Linux (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Tải xuống MCPB Windows"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Tải xuống MCPB Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Tải xuống MCPB Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

Máy chủ MCP StackQL cũng được liệt kê trên [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) với tên `io.github.stackql/stackql-mcp`.

:::

:::tip

Bundle là một trong nhiều cách để chạy máy chủ MCP StackQL.  Xem [__Sử dụng StackQL với Claude Desktop__](/getting-started/claude-desktop) để biết hướng dẫn đầy đủ cho Claude Desktop, hoặc [__Cài đặt máy chủ MCP__](#installing-the-mcp-server) để biết các tùy chọn npx, Docker, Python, CI và thủ công.

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

  ### ChatGPT và Codex

  StackQL được phát hành dưới dạng plugin cục bộ cho ứng dụng ChatGPT trên máy tính và Codex CLI. Plugin chạy máy chủ MCP StackQL qua stdio trên máy của bạn, nên thông tin xác thực provider không bao giờ rời khỏi máy. Yêu cầu Node.js 24.22.1 trở lên (22.23.4 hoặc 26.12.1 trên các dòng phát hành đó); plugin chỉ cần các mô-đun tích hợp sẵn của Node.js, tải xuống tệp nhị phân `stackql` được ký trong lần sử dụng đầu tiên và xác minh SHA-256 của tệp.

  **Ứng dụng ChatGPT trên máy tính (thư mục plugin):**  
  
  mở thư mục plugin trong ứng dụng ChatGPT trên máy tính, tìm **StackQL** và nhấp **Install**, sau đó chạy phần thiết lập của plugin; skill `stackql-setup` đi kèm sẽ hướng dẫn bạn qua thông tin xác thực và truy vấn đầu tiên. Mục này có huy hiệu **Desktop only**: plugin chỉ cài đặt được từ ứng dụng trên máy tính, không cài đặt được từ ChatGPT web hoặc di động, và các công cụ của plugin khả dụng trong chế độ Work, trong các thread chạy trên máy tính của bạn. Nếu mục này chưa hiển thị trong thư mục của bạn, hãy cài đặt từ marketplace của kho lưu trữ.

  **Ứng dụng ChatGPT trên máy tính (marketplace của kho lưu trữ):**  
  
  mở **Settings -> Plugins -> Add -> Add from GitHub**, nhập `stackql/stackql`, sau đó cài đặt **StackQL** từ phần **Personal** của thư mục plugin và khởi động lại ứng dụng.

  **Codex CLI:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  Khởi động lại ChatGPT trên máy tính hoặc Codex, sau đó thử: *Dùng StackQL để cài đặt provider GitHub nếu cần, sau đó liệt kê năm dịch vụ GitHub đầu tiên.* Thông tin xác thực provider được đặt trong `~/.stackql/.env` theo tên biến môi trường chuẩn của từng provider (ví dụ `AWS_ACCESS_KEY_ID` và `AWS_SECRET_ACCESS_KEY`); sau khi sửa tệp, hãy yêu cầu tác nhân gọi `reload_credentials`.

  **Không dùng plugin:**   
  
  trong **Settings -> Plugins -> MCPs -> Connect to a custom MCP**, chọn **STDIO**, đặt lệnh là `npx` và thêm các đối số `-y` và `@stackql/mcp-server`. Cung cấp thông tin xác thực provider dưới dạng biến môi trường trên biểu mẫu đó.

<br />
:::note

ChatGPT web và di động không thể chạy máy chủ stdio cục bộ, nên plugin chỉ dùng được trên máy tính và Codex. Một lỗi đang mở của ChatGPT trên máy tính ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) có thể ẩn các công cụ MCP cục bộ khỏi các phiên **Chat**; các thread ở chế độ Work, thread Codex và Codex CLI không bị ảnh hưởng.

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

  Goose chạy trực tiếp các tiện ích mở rộng stdio, nên bạn chỉ cần trình khởi chạy `npx` (Node.js 24.22.1 trở lên, hoặc 22.23.4 hoặc 26.12.1 trên các dòng phát hành đó).

  **Goose Desktop:** nhấp vào nút bên dưới. Goose mở ra, hiển thị lệnh sắp chạy và yêu cầu bạn xác nhận, sau đó thêm StackQL làm tiện ích mở rộng.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Cài đặt trong Goose Desktop</a>
  </Box>

  Nút này là một liên kết sâu `goose://`; bạn cũng có thể dán nó vào thanh địa chỉ của trình duyệt, hoặc mở nó từ terminal bằng `open` (macOS), `start` (Windows) hoặc `xdg-open` (Linux):

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  hoặc chạy `goose configure`, chọn **Add Extension -> Command-line Extension** và nhập cùng lệnh đó. Cung cấp thông tin xác thực provider dưới dạng biến môi trường của tiện ích mở rộng khi được yêu cầu.

<br />
:::note

Goose đã ngừng dùng thư mục tiện ích mở rộng trong ứng dụng để chuyển sang [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql), nơi StackQL được liệt kê với tên `io.github.stackql/stackql-mcp`. Khi Goose phát hành tính năng nhập từ registry, StackQL sẽ có thể được tìm thấy ở đó mà không cần thao tác gì thêm.

:::

</Box>

</TabItem>
</Tabs>

## Cài đặt máy chủ MCP {#installing-the-mcp-server}

Máy chủ MCP StackQL cho phép tác nhân AI truy vấn và cấp phát tài nguyên đám mây bằng StackQL. Máy chủ được phân phối qua nhiều kênh - tất cả đều chạy cùng một máy chủ, vì vậy hãy chọn kênh phù hợp với ứng dụng khách và yêu cầu tin cậy của bạn. Các tùy chọn bên dưới được sắp xếp gần đúng theo mức độ tin cậy và mức độ dễ dùng.

### Marketplace và thư mục {#marketplaces-and-directories}

Máy chủ được phát hành lên Official MCP Registry với tên `io.github.stackql/stackql-mcp`; các registry gói bên dưới chứa các artifact để phân phối, và các thư mục ở hạ nguồn lấy mục từ đó.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Registry', value: 'registries', },
    { label: 'Thư mục', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Registry | Loại | Phát hành qua | Mục | Ghi chú |
|---|---|---|---|---|
| Official MCP Registry | Registry siêu dữ liệu chuẩn | CLI `mcp-publisher` + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | công bố cả 8 gói (`mcpb x5`, `oci`, `npm`, `pypi`) |
| npm | Registry gói | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | trình khởi chạy `npx` |
| PyPI | Registry gói | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | trình khởi chạy `uvx` / `pip` |
| Docker Hub | Registry container | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | đa kiến trúc `amd64` + `arm64` |
| GitHub Actions Marketplace | Marketplace CI (action `setup-stackql-mcp`) | Kho lưu trữ công khai + bản phát hành + mục marketplace | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Nhà phát hành đã xác minh; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Các mục trên thư mục và trang tổng hợp, sắp xếp gần đúng theo mức độ quan trọng đối với khả năng được tìm thấy:

| Thư mục | Loại | Mục |
|---|---|---|
| __Anthropic Connector Directory__ | Thư mục trong ứng dụng Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | Thư mục trong ứng dụng ChatGPT trên máy tính (chỉ trên máy tính) | tìm **StackQL** trong ứng dụng ChatGPT trên máy tính; xem [ChatGPT và Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | Thư mục ứng dụng khách IDE | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | Trang tổng hợp lớn nhất | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Khám phá, đơn vị hỗ trợ registry | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Marketplace có thể tìm kiếm | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Trang tổng hợp | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Trang awesome-list | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Bundle `.mcpb` dựng sẵn {#prebuilt-mcpb-bundle}

**Khi nào nên dùng:** Claude Desktop không có quyền truy cập Connector Directory, hoặc khi bạn muốn ghim một phiên bản cụ thể hoặc tự xác minh checksum trước khi cài đặt.  Với hầu hết người dùng Claude Desktop, [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) là cách cài đặt được khuyên dùng.

Mỗi bản phát hành StackQL đều đính kèm một MCP Bundle dựng sẵn cho từng nền tảng:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

trong đó `<platform>` là một trong các giá trị `darwin-universal`, `windows-x64` (AMD64), `windows-arm64`, `linux-x64` hoặc `linux-arm64`. Các nút tải xuống cho từng nền tảng nằm trong tab **Claude Desktop** ở phần [Sử dụng với ứng dụng khách MCP](#using-with-mcp-clients). Mỗi bundle có một checksum `.sha256` tương ứng trên [trang phát hành](https://github.com/stackql/stackql/releases/latest). Xác minh checksum, sau đó cài đặt qua **Settings -> Extensions** trong Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Xem [Sử dụng StackQL với Claude Desktop](/getting-started/claude-desktop#install-from-a-downloaded-bundle) để biết hướng dẫn từng bước đầy đủ.

### Cấu hình `claude_desktop_config.json` thủ công {#manual-claude_desktop_configjson}

**Khi nào nên dùng:** bạn đã có tệp nhị phân `stackql` trong PATH và dùng Claude Desktop hoặc bất kỳ ứng dụng khách MCP stdio nào khác.

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

Cả ba đối số đều không thể thiếu:

- `--mcp.server.type=stdio` chọn phương thức truyền tải stdio mà các ứng dụng khách nhúng trong trình soạn thảo sử dụng.
- `--approot` trỏ cache provider đến một thư mục có thể ghi. Ứng dụng khách MCP có thể khởi chạy máy chủ với thư mục làm việc là `/`, vốn không thể ghi.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` tắt nhật ký kiểm tra; nếu không tắt, nhật ký này mặc định dùng thư mục làm việc (có thể không ghi được) làm thư mục của nó.

Thêm thông tin xác thực provider bằng khối `"env"` và điều chỉnh hợp đồng an toàn bằng `"mode"` - xem [Chế độ máy chủ](/command-line-usage/mcp#server-modes).

### `npx` (không cần cài đặt) {#npx-no-install}

**Khi nào nên dùng:** mọi môi trường Node, khi bạn không muốn cài đặt StackQL toàn cục.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

Trình khởi chạy [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) tải xuống tệp nhị phân `stackql` được ký trong lần chạy đầu tiên, xác minh SHA-256 của tệp và lưu tệp vào cache cho các lần chạy sau.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**Khi nào nên dùng:** môi trường Python - cùng trình khởi chạy đó được đóng gói cho Python, dùng chung cache tệp nhị phân mà trình khởi chạy npx tạo ra.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

Hoặc cài đặt vào môi trường hiện tại bằng `pip install stackql-mcp-server` (Python 3.9+). Gói này có tên [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) trên PyPI.

### Docker {#docker}

**Khi nào nên dùng:** runtime chạy trong container hoặc được cô lập theo cách khác. Image hỗ trợ đa kiến trúc (amd64 và arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

Dưới dạng cấu hình ứng dụng khách:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

Image này có tên [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) trên Docker Hub.

### CI và quy trình làm việc dựa trên tác nhân (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**Khi nào nên dùng:** chạy máy chủ bên trong một job GitHub Actions để tác nhân có thể truy vấn - và, nếu bạn cho phép, thao tác trên - môi trường đám mây của bạn như một phần của CI.

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) cài đặt tệp nhị phân và ghi một cấu hình MCP (mặc định ở chế độ `read_only`). Truyền cấu hình đó cho [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) qua `claude_args`:

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

Xem [README của action](https://github.com/stackql/setup-stackql-mcp) để biết thêm các công thức dựa trên tác nhân (kiểm tra đám mây, ước tính chi phí, v.v.).

### MCP nhúng {#embedded-mcp}

**Khi nào nên dùng:** bạn đang xây dựng một ứng dụng dựa trên tác nhân và muốn đưa máy chủ MCP vào tệp nhị phân của mình theo kiểu vendoring - không cần `npx`, không cần cài đặt riêng, không có phần phụ thuộc runtime.

Các thư viện gốc khởi chạy tệp nhị phân `stackql` được ký qua stdio, phía sau ứng dụng khách SDK MCP chính thức của từng ngôn ngữ, nhờ đó ứng dụng của bạn có cùng một giao diện SQL được quản trị đến mọi provider trong registry. Mỗi thư viện mặc định dùng `read_only` - việc nâng lên chế độ có quyền ghi luôn phải được chỉ định rõ ràng - và có thể tải xuống rồi xác minh tệp nhị phân trong lần chạy đầu tiên, hoặc đưa nó vào bản dựng của bạn theo kiểu vendoring để có một tệp thực thi độc lập duy nhất. API đầy đủ cho từng ngôn ngữ có trong [tài liệu tham khảo MCP nhúng](/mcp/embedded).

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

Crate `stackql-mcp` khởi chạy tệp nhị phân phía sau một ứng dụng khách `rmcp`. Tính năng mặc định `sidecar` tải xuống và xác minh tệp nhị phân trong lần chạy đầu tiên; tính năng `vendored` nhúng tệp nhị phân bằng `include_bytes!` để có một tệp nhị phân độc lập duy nhất. MSRV Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="Xem trên GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Xem trên crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Tài liệu tham khảo đầy đủ: [MCP nhúng: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

Thư viện `stackql-mcp-go` khởi chạy tệp nhị phân phía sau ứng dụng khách SDK MCP chính thức cho Go. Đưa tệp nhị phân vào theo kiểu vendoring bằng `go:embed` để có một tệp nhị phân độc lập duy nhất, hoặc để thư viện tải xuống và xác minh tệp trong lần chạy đầu tiên. Theo mặc định, thư viện khởi động ở chế độ `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Xem trên GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Xem trên pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Tài liệu tham khảo đầy đủ: [MCP nhúng: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

Thư viện `io.stackql:stackql-mcp` khởi chạy tệp nhị phân phía sau ứng dụng khách SDK MCP chính thức cho Kotlin, và một plugin Gradle đi kèm tích hợp cùng cách khởi chạy đó vào bản dựng. Yêu cầu JDK 17 và Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="Xem trên GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Tài liệu tham khảo đầy đủ: [MCP nhúng: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

Gói `StackQL.Mcp` khởi chạy tệp nhị phân phía sau ứng dụng khách SDK MCP chính thức cho C#. Theo mặc định dùng sidecar, hoặc đưa bundle vào theo kiểu vendoring dưới dạng tài nguyên bản dựng để có một tệp thực thi độc lập. Yêu cầu .NET 8 trở lên.

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
    <BinaryDownloadLink iconSize={20} text="Xem trên GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Tài liệu tham khảo đầy đủ: [MCP nhúng: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

Thư viện `stackql_mcp` nhắm đến runtime Erlang/BEAM và cung cấp máy chủ dưới dạng một tiến trình con OTP qua `child_spec()`, nhờ đó máy chủ có thể nằm trong supervision tree của riêng bạn. Theo mặc định, thư viện khởi động ở chế độ `read_only`.

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
    <BinaryDownloadLink iconSize={20} text="Xem trên GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Tài liệu tham khảo đầy đủ: [MCP nhúng: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

Gói `stackql-mcp-swift` khởi chạy tệp nhị phân phía sau ứng dụng khách SDK MCP chính thức cho Swift. Tệp nhị phân `darwin-universal` được ký bằng Developer ID và được Apple công chứng, nên bạn có thể đóng gói nó bên trong một `.app` được ký mà vẫn giữ hiệu lực công chứng của ứng dụng. Yêu cầu macOS 13+ và Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="Xem trên GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Tài liệu tham khảo đầy đủ: [MCP nhúng: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### Mô hình tin cậy {#trust-model}

Các thuộc tính bảo mật sau áp dụng như nhau cho mọi kênh:

- Tệp nhị phân `stackql` nhúng được ký bằng Authenticode (Windows) và được Apple công chứng (macOS).
- Mỗi bundle `.mcpb` đều đi kèm một checksum SHA-256 được đăng trên trang phát hành.
- Trình khởi chạy `npx` và `uvx` xác minh SHA-256 của tệp nhị phân đã tải xuống trước lần sử dụng đầu tiên.
- Mục trên [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) xác nhận hash của từng nền tảng.

## Sử dụng GitHub Actions {#using-github-actions}

Bạn có thể dùng [GitHub Actions StackQL](https://github.com/stackql/stackql-actions-demo) trong các workflow GitHub Actions của mình. Hiện có các action sau:

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

Triển khai hoặc kiểm thử các stack hạ tầng bằng StackQL trực tiếp từ các workflow GitHub của bạn. Action này hỗ trợ các workflow hạ tầng dưới dạng mã (IaC) bằng cú pháp giống SQL, giúp bạn định nghĩa, triển khai và quản lý tài nguyên đám mây trên nhiều provider trong một workflow duy nhất.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Xem trên GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Xem trên GitHub"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ví dụ sử dụng__

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

Thiết lập StackQL trong workflow GitHub Actions của bạn.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Xem trên GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Xem trên GitHub"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ví dụ sử dụng__

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

Cài đặt máy chủ MCP StackQL trong workflow GitHub Actions của bạn.  Action này cài đặt tệp nhị phân được ký và ghi một cấu hình `mcpServers` (mặc định ở chế độ `read_only`) mà một bước dựa trên tác nhân như `anthropics/claude-code-action` sử dụng qua `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Xem trên GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Xem trên GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ví dụ sử dụng__

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

Thực thi các lệnh StackQL trong workflow GitHub Actions của bạn.  Truy vấn có thể được viết trực tiếp hoặc lấy từ một tệp trong kho lưu trữ.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Xem trên GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Xem trên GitHub"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ví dụ sử dụng__

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

Thực hiện kiểm thử đơn vị trong workflow GitHub Actions của bạn (cho các bước kiểm tra đảm bảo, quản trị hoặc bảo mật đám mây).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Xem trên GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Xem trên GitHub"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ví dụ sử dụng__

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


## Các thư viện khác {#other-libraries}

StackQL cung cấp nhiều cách tích hợp để dùng trong các ngôn ngữ lập trình và môi trường khác nhau. Các thư viện này mở rộng các tính năng của StackQL, giúp bạn dễ dàng đưa việc truy vấn và quản lý tài nguyên đám mây vào các ứng dụng và workflow hiện có.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### Gói Python `pystackql` {#pystackql-python-package}

Wrapper Python để dùng StackQL trong các chương trình Python của bạn. Gói `pystackql` có trên [__PyPI__](https://pypi.org/project/pystackql/), tài liệu cho gói `pystackql` có trên [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). Để cài đặt gói `pystackql`, chạy lệnh sau:

```bash
pip install pystackql
```

Ví dụ sau cho thấy gói `pystackql` được dùng cùng `pandas` để chạy truy vấn StackQL và trả kết quả về một `pandas.DataFrame`:

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
