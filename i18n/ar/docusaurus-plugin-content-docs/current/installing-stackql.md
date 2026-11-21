---
title: التثبيت
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "ثبّت StackQL على macOS أو Linux أو Windows أو Docker أو واجهة أوامر سحابية، واستخدم خادم StackQL MCP من Claude Desktop وChatGPT وGoose وعملاء MCP آخرين أو عبر npx وuvx وpip وDocker وGitHub Actions."
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

تقدم هذه الصفحة إرشادات تثبيت StackQL على منصات مختلفة.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'واجهات الأوامر السحابية', value: 'shells', },
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
  
  StackQL متاح على macOS عبر Homebrew وبرنامج التثبيت `pkg`، ويدعم برنامج تثبيت واحد متعدد البنى بنيتي ARM (M1/Apple Silicon) وIntel (x86_64) معًا.

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (ملف ثنائي موقع رقميًا وموثق)', value: 'curl', },
      { label: 'تنزيل الحزمة', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  للتثبيت عبر Homebrew، شغّل الأمر التالي في الوحدة الطرفية:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  لتثبيت الملف التنفيذي `stackql` الموقع رقميًا والموثق في الدليل الحالي، شغّل الأمر التالي في الوحدة الطرفية:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL متاح على شكل برنامج تثبيت `pkg` تفاعلي موقع رقميًا وموثق لنظام macOS.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="تنزيل PKG لنظام macOS"
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

  StackQL متاح لجميع بنى Linux.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'تنزيل الحزمة', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  لتثبيت الملف التنفيذي `stackql` في الدليل الحالي لبنية Linux لديك (amd64 أو arm64)، شغّل الأمر التالي في الوحدة الطرفية:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  بدلًا من ذلك، يمكنك تنزيل الملفات الثنائية من هنا:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="تنزيل ZIP لنظام Linux (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="تنزيل ZIP لنظام Linux (arm64)"
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

  StackQL متاح على Windows عبر Chocolatey وبرنامج تثبيت نصي لـ PowerShell وبرنامج التثبيت MSI. تتوفر إصدارات بناء أصلية لـ AMD64 (x64) وARM64 على شكل برامج تثبيت MSI وملفات ZIP مضغوطة تحتوي على `stackql.exe`. يعمل إصدار بناء AMD64 أيضًا على ARM64 عبر المحاكاة. جميع الملفات التنفيذية موقعة رقميًا بشهادة توقيع تعليمات برمجية من نوع Authenticode.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'تنزيل MSI/ZIP', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  للتثبيت عبر Chocolatey، شغّل الأمر التالي في الوحدة الطرفية لـ PowerShell أو `cmd`:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  لتثبيت ملف تنفيذي موقع رقميًا بتقنية Authenticode في الدليل الحالي، شغّل ما يلي في وحدة طرفية لـ PowerShell:

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  نزّل برنامج التثبيت أو الملف التنفيذي `stackql` الموقع رقميًا لنظام Windows أدناه. يؤدي النقر على زر التنزيل إلى تحديد AMD64 افتراضيًا؛ استخدم القائمة المنسدلة الخاصة به لاختيار AMD64 أو ARM64 الأصلي.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="تنزيل MSI لنظام Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'تنزيل MSI لنظام Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'تنزيل MSI لنظام Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="تنزيل ZIP لنظام Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'تنزيل ZIP لنظام Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'تنزيل ZIP لنظام Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  ننشر إصدارات بناء StackQL على [Docker Hub](https://hub.docker.com/u/stackql).

  __Docker Hub__  

  لسحب صورة حاوية StackQL، شغّل الأمر التالي:

  ```bash
  docker pull stackql/stackql
  ```

  من الإصدار `v1.0` فصاعدًا، تعتمد الصورة على Ubuntu 26.04 LTS، وننشر صورة بديلة مثبت عليها Python 3.13 مسبقًا تحمل لاحقة الوسم `-python3.13` (والاسم المستعار `-python3`)، على سبيل المثال:

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

`stackql-deploy` هو إطار عمل IaC عديم الحالة مبني على StackQL. للاطلاع على الوثائق الكاملة، راجع [__وثائق StackQL Deploy__](https://stackql-deploy.io/).

__Linux / macOS__

يتولى الأمر التالي تنزيل أحدث ملف ثنائي `stackql-deploy` واستخراجه في خطوة واحدة. يكتشف برنامج التثبيت نظام التشغيل والبنية لديك (بما في ذلك Apple Silicon وIntel عبر ملف ثنائي universal لنظام macOS) ويحصل على ملف الإصدار الصحيح تلقائيًا.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

يتولى الأمر التالي تنزيل أحدث ملف ثنائي `stackql-deploy` واستخراجه في الدليل الحالي.
يكتشف برنامج التثبيت منصتك ويحصل على ملف الإصدار الصحيح تلقائيًا.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

إذا كانت سلسلة أدوات Rust مثبتة لديك، فإن هذا الأمر يبني الملف الثنائي ويثبته مباشرة من [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## واجهات الأوامر السحابية

  يمكنك استخدام StackQL مباشرة من واجهات الأوامر السحابية والوحدات الطرفية على الويب المدمجة في منصات السحابة والبيانات الرئيسية. يوفر ذلك تجربة سلسة لأن هذه البيئات مخولة مسبقًا بالهوية التي سجلت الدخول بها، مما يلغي الحاجة إلى إعداد مصادقة منفصل.

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

  يوفر AWS CloudShell واجهة أوامر تعمل في المتصفح، تم فيها مسبقًا تثبيت AWS CLI وإعداد المصادقة الخاصة به. يتيح لك تشغيل StackQL في AWS CloudShell الاستعلام عن موارد AWS وإدارتها دون خطوات مصادقة إضافية. للحصول على إرشادات مفصلة، راجع [برنامجنا التعليمي حول AWS CloudShell](/quick-starts/aws/aws-cloud-shell).

  أولًا، نزّل حزمة StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  ثم شغّل البرنامج النصي لـ StackQL في AWS CloudShell:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  يبدأ هذا البرنامج النصي واجهة أوامر StackQL باستخدام بيانات اعتماد AWS CloudShell الخاصة بك، مما يتيح لك البدء فورًا في الاستعلام عن موارد AWS.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  يوفر Azure Cloud Shell بيئة واجهة أوامر يمكن الوصول إليها من المتصفح، تمت فيها مسبقًا مصادقة Azure CLI بحساب Azure الخاص بك. يتيح استخدام StackQL في Azure Cloud Shell الاستعلام بسلاسة عن موارد Azure دون إعداد إضافي. للاطلاع على التفاصيل الكاملة، راجع [دليلنا حول Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell).

  أولًا، نزّل حزمة StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  ثم شغّل البرنامج النصي لـ StackQL في Azure Cloud Shell:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  يتولى هذا البرنامج النصي تلقائيًا تكوين جلسة StackQL لاستخدام بيانات اعتماد Azure Cloud Shell الخاصة بك، مما يتيح لك الاستعلام عن موارد Azure فورًا.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  يوفر Google Cloud Shell بيئة للتطوير والعمليات تمت فيها مسبقًا مصادقة Google Cloud CLI. يتيح لك تشغيل StackQL في Google Cloud Shell الاستعلام عن موارد GCP باستخدام المصادقة الحالية لديك. لمعرفة المزيد، راجع [دليلنا حول Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell).

  أولًا، نزّل حزمة StackQL:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  ثم شغّل البرنامج النصي لـ StackQL في Google Cloud Shell:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  يتولى البرنامج النصي إعداد StackQL لاستخدام بيانات اعتماد Google Cloud Shell الخاصة بك، مما يتيح لك البدء فورًا في الاستعلام عن موارد GCP باستخدام بناء جملة SQL.

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web Terminal

  تتضمن مساحات عمل Databricks وحدة طرفية على الويب تعمل بهوية المستخدم الذي سجل الدخول. يتيح لك تشغيل StackQL هناك الاستعلام عن مساحة العمل باستخدام هوية Databricks الخاصة بك، دون إعداد مصادقة منفصل. للاطلاع على أمثلة استعلامات والمصادقة على مستوى الحساب، راجع [دليلنا حول Databricks Web Terminal](/blog/tutorials/stackql-in-databricks-web-terminal).

  أولًا، نزّل حزمة StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  ثم شغّل البرنامج النصي لواجهة أوامر StackQL في Databricks:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  يبدأ ذلك جلسة StackQL باستخدام هوية مساحة عمل Databricks الخاصة بك، بحيث يمكنك الاستعلام فورًا عن الموارد التي يقتصر نطاقها على مساحة العمل مثل `databricks_workspace.iam.vw_user_entitlements`. للاستعلامات على مستوى الحساب (التوفير والفوترة وIAM الخاص بالحساب)، عيّن متغيرات كيان خدمة OAuth2 في Databricks كما هو موضح في الدليل.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## الاستخدام مع عملاء MCP {#using-with-mcp-clients}

يعمل خادم StackQL MCP محليًا عبر stdio، لذا تبقى بيانات اعتماد الموفرين على جهازك مهما كان العميل الذي تستخدمه. اختر عميلك أدناه: تعرض كل علامة تبويب طريقة التثبيت من الدليل أو بنقرة واحدة أولًا، ثم التكوين اليدوي كبديل احتياطي. لمعرفة جميع قنوات التوزيع، بما في ذلك `npx` وPython وDocker وCI، راجع [تثبيت خادم MCP](#installing-the-mcp-server).

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

  الطريقة الموصى بها لتثبيت خادم StackQL MCP في Claude Desktop هي التثبيت من [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Settings -> Connectors -> Add -> Browse Connectors**، ثم ابحث عن **StackQL**) - يحدد Claude Desktop الملف الثنائي الموقع رقميًا الصحيح لمنصتك تلقائيًا. بدلًا من ذلك، يحتوي كل إصدار من StackQL على ملفات MCP Bundle مبنية مسبقًا (`.mcpb`) للتثبيت من ملف تم تنزيله - ولا يلزم تثبيت StackQL بشكل منفصل في أي من الحالتين. تتوفر ملفات bundle لأنظمة macOS وWindows وLinux.

  تستخدم تنزيلات MCPB لنظامي Linux وWindows البنية **amd64** (x64) افتراضيًا. استخدم القوائم المنسدلة لتحديد **amd64** أو **arm64**.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="تنزيل MCPB لنظام macOS"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="تنزيل MCPB لنظام Linux"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'تنزيل MCPB لنظام Linux (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'تنزيل MCPB لنظام Linux (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="تنزيل MCPB لنظام Windows"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'تنزيل MCPB لنظام Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'تنزيل MCPB لنظام Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

خادم StackQL MCP مدرج أيضًا في [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) باسم `io.github.stackql/stackql-mcp`.

:::

:::tip

ملف bundle هو إحدى الطرق العديدة لتشغيل خادم StackQL MCP. راجع [__استخدام StackQL مع Claude Desktop__](/getting-started/claude-desktop) للاطلاع على الشرح التفصيلي الكامل لـ Claude Desktop، أو [__تثبيت خادم MCP__](#installing-the-mcp-server) للاطلاع على خيارات npx وDocker وPython وCI والتثبيت اليدوي.

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

  ### ChatGPT وCodex

  يتوفر StackQL كمكون إضافي محلي لتطبيق ChatGPT لسطح المكتب وCodex CLI. يشغل المكون الإضافي خادم StackQL MCP عبر stdio على جهازك، لذا لا تغادر بيانات اعتماد الموفرين جهازك أبدًا. يلزم Node.js 24.22.1 أو إصدار أحدث (22.23.4 أو 26.12.1 في خطوط الإصدار تلك)؛ ولا يحتاج المكون الإضافي إلا إلى الوحدات المدمجة في Node.js، ويتولى تنزيل الملف الثنائي `stackql` الموقع رقميًا عند أول استخدام والتحقق من قيمة SHA-256 الخاصة به.

  **تطبيق ChatGPT لسطح المكتب (دليل المكونات الإضافية):**
  
  افتح دليل المكونات الإضافية في تطبيق ChatGPT لسطح المكتب، وابحث عن **StackQL** وانقر على **Install**، ثم شغّل إعداد المكون الإضافي؛ ترشدك المهارة `stackql-setup` المرفقة خلال إعداد بيانات الاعتماد وأول استعلام. يحمل الإدراج شارة **Desktop only**: لا يمكن تثبيت المكون الإضافي إلا من تطبيق سطح المكتب، وليس من ChatGPT على الويب أو الأجهزة المحمولة، وتتوفر أدواته في وضع Work، داخل threads تعمل على جهاز الكمبيوتر الخاص بك. إذا لم يظهر الإدراج في دليلك بعد، فثبّته من سوق المستودع بدلًا من ذلك.

  **تطبيق ChatGPT لسطح المكتب (سوق المستودع):**
  
  افتح **Settings -> Plugins -> Add -> Add from GitHub**، وأدخل `stackql/stackql`، ثم ثبّت **StackQL** من القسم **Personal** في دليل المكونات الإضافية وأعد تشغيل التطبيق.

  **Codex CLI:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  أعد تشغيل ChatGPT لسطح المكتب أو Codex، ثم جرّب: *استخدم StackQL لتثبيت موفر GitHub إذا لزم الأمر، ثم اسرد أول خمس خدمات في GitHub.* توضع بيانات اعتماد الموفرين في `~/.stackql/.env` باستخدام أسماء متغيرات البيئة القياسية لكل موفر (على سبيل المثال `AWS_ACCESS_KEY_ID` و`AWS_SECRET_ACCESS_KEY`)؛ بعد تعديل الملف، اطلب من الوكيل استدعاء الأداة `reload_credentials`.

  **بدون المكون الإضافي:**
  
  ضمن **Settings -> Plugins -> MCPs -> Connect to a custom MCP**، اختر **STDIO**، وعيّن `npx` كأمر وأضف الوسيطتين `-y` و`@stackql/mcp-server`. قدّم بيانات اعتماد الموفرين كمتغيرات بيئة في نموذج الإدخال نفسه.

<br />
:::note

لا يستطيع ChatGPT على الويب والأجهزة المحمولة تشغيل خادم stdio محلي، لذا يقتصر المكون الإضافي على تطبيق سطح المكتب وCodex. قد يؤدي خطأ برمجي مفتوح في ChatGPT لسطح المكتب ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) إلى إخفاء أدوات MCP المحلية من جلسات **Chat**؛ ولا تتأثر threads في وضع Work وthreads في Codex وCodex CLI.

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

  يشغل Goose ملحقات stdio مباشرة، لذا فإن أداة التشغيل `npx` هي كل ما تحتاج إليه (Node.js 24.22.1 أو إصدار أحدث، أو 22.23.4 أو 26.12.1 في خطوط الإصدار تلك).

  **Goose Desktop:** انقر على الزر أدناه. يفتح Goose، ويعرض الأمر الذي سيشغله ويطلب منك التأكيد، ثم يضيف StackQL كملحق.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>التثبيت في Goose Desktop</a>
  </Box>

  الزر هو رابط عميق `goose://`؛ يمكنك أيضًا لصقه في شريط العناوين في متصفحك، أو فتحه من وحدة طرفية باستخدام `open` (macOS) أو `start` (Windows) أو `xdg-open` (Linux):

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  أو شغّل الأمر `goose configure`، واختر **Add Extension -> Command-line Extension**، وأدخل الأمر نفسه. قدّم بيانات اعتماد الموفرين كمتغيرات بيئة للملحق عندما يطلب Goose منك ذلك.

<br />
:::note

أوقف Goose دليل الملحقات داخل التطبيق لصالح [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)، حيث StackQL مدرج باسم `io.github.stackql/stackql-mcp`. بمجرد أن يطلق Goose ميزة الاستيراد من السجل، سيصبح بالإمكان العثور على StackQL هناك دون أي إجراء إضافي.

:::

</Box>

</TabItem>
</Tabs>

## تثبيت خادم MCP {#installing-the-mcp-server}

يتيح خادم StackQL MCP لوكلاء الذكاء الاصطناعي الاستعلام عن الموارد السحابية وتوفيرها باستخدام StackQL. يتوفر الخادم عبر عدة قنوات - وجميعها تشغل الخادم نفسه، لذا اختر القناة التي تناسب عميلك ومتطلبات الثقة لديك. الخيارات أدناه مرتبة تقريبًا حسب الثقة وسهولة الاستخدام.

### الأسواق والأدلة {#marketplaces-and-directories}

الخادم منشور في Official MCP Registry باسم `io.github.stackql/stackql-mcp`؛ وتحمل سجلات الحزم أدناه الملفات الناتجة القابلة للتوزيع، وتأخذ الأدلة التابعة الإدراج من هناك.

<Tabs
  defaultValue="registries"
  values={[
    { label: 'السجلات', value: 'registries', },
    { label: 'الأدلة', value: 'directories', },
  ]}
>
<TabItem value="registries">

| السجل | النوع | طريقة النشر | الإدراج | ملاحظات |
|---|---|---|---|---|
| Official MCP Registry | سجل بيانات التعريف المعياري | أداة CLI `mcp-publisher` + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | يعلن عن جميع الحزم وعددها 8 (`mcpb x5` و`oci` و`npm` و`pypi`) |
| npm | سجل حزم | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | أداة التشغيل `npx` |
| PyPI | سجل حزم | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | أداة التشغيل `uvx` / `pip` |
| Docker Hub | سجل حاويات | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | متعدد البنى `amd64` + `arm64` |
| GitHub Actions Marketplace | سوق CI (الإجراء `setup-stackql-mcp`) | مستودع عام + إصدار + إدراج في السوق | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | ناشر تم التحقق منه؛ [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

الإدراجات في الأدلة ومواقع التجميع، مرتبة تقريبًا حسب أهميتها في الاكتشاف:

| الدليل | النوع | الإدراج |
|---|---|---|
| __Anthropic Connector Directory__ | دليل داخل تطبيق Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | دليل داخل تطبيق ChatGPT لسطح المكتب (سطح المكتب فقط) | ابحث عن **StackQL** في تطبيق ChatGPT لسطح المكتب؛ راجع [ChatGPT وCodex](#using-with-mcp-clients) |
| __Cursor Directory__ | دليل عملاء IDE | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | أكبر موقع تجميع | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | الاكتشاف، داعم للسجل | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | سوق قابل للبحث | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | موقع تجميع | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | موقع awesome-list | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### ملف bundle `.mcpb` مبني مسبقًا {#prebuilt-mcpb-bundle}

**متى تستخدم:** في Claude Desktop دون إمكانية الوصول إلى Connector Directory، أو عندما تريد قفل إصدار محدد أو التحقق من المجاميع الاختبارية بنفسك قبل التثبيت. طريقة التثبيت الموصى بها لمعظم مستخدمي Claude Desktop هي [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

يحتوي كل إصدار من StackQL على ملف MCP Bundle مبني مسبقًا لكل منصة:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

حيث `<platform>` هي إحدى القيم `darwin-universal` أو `windows-x64` (AMD64) أو `windows-arm64` أو `linux-x64` أو `linux-arm64`. توجد أزرار التنزيل لكل منصة في علامة التبويب **Claude Desktop** ضمن [الاستخدام مع عملاء MCP](#using-with-mcp-clients). لكل ملف bundle مجموع اختباري `.sha256` مطابق في [صفحة الإصدار](https://github.com/stackql/stackql/releases/latest). تحقق منه، ثم ثبّته عبر **Settings -> Extensions** في Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

راجع [استخدام StackQL مع Claude Desktop](/getting-started/claude-desktop#install-from-a-downloaded-bundle) للاطلاع على الشرح التفصيلي الكامل.

### تكوين الملف `claude_desktop_config.json` يدويًا {#manual-claude_desktop_configjson}

**متى تستخدم:** عندما يكون لديك الملف الثنائي `stackql` في متغير PATH بالفعل وتستخدم Claude Desktop أو أي عميل MCP آخر يعمل عبر stdio.

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

الوسيطات الثلاث جميعها ضرورية:

- `--mcp.server.type=stdio` تحدد وسيلة النقل stdio التي يستخدمها العملاء المضمنون في المحررات.
- `--approot` توجه ذاكرة التخزين المؤقت للموفرين إلى دليل قابل للكتابة. قد يشغل عملاء MCP الخادم مع تعيين دليل العمل إلى `/`، وهو دليل غير قابل للكتابة.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` تعطل سجل التدقيق، الذي يستخدم بخلاف ذلك دليل العمل (الذي قد يكون غير قابل للكتابة) كدليل افتراضي له.

أضف بيانات اعتماد الموفرين باستخدام كتلة `"env"` وعدّل عقد الأمان باستخدام `"mode"` - راجع [أوضاع الخادم](/command-line-usage/mcp#server-modes).

### `npx` (بدون تثبيت) {#npx-no-install}

**متى تستخدم:** في أي بيئة Node، عندما لا تريد تثبيت StackQL على مستوى النظام.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

تتولى أداة التشغيل [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) تنزيل الملف الثنائي `stackql` الموقع رقميًا عند أول تشغيل، والتحقق من قيمة SHA-256 الخاصة به، وتخزينه مؤقتًا لعمليات التشغيل اللاحقة.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**متى تستخدم:** في بيئة Python - وهي أداة التشغيل نفسها مجهزة كحزمة لـ Python، وتشترك في ذاكرة التخزين المؤقت للملف الثنائي التي تملؤها أداة التشغيل npx.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

أو ثبّتها في البيئة الحالية باستخدام `pip install stackql-mcp-server` (Python 3.9+). الحزمة هي [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) على PyPI.

### Docker {#docker}

**متى تستخدم:** في وقت تشغيل داخل حاوية أو معزول بطريقة أخرى. الصورة متعددة البنى (amd64 وarm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

كتكوين للعميل:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

الصورة هي [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) على Docker Hub.

### CI وسير العمل القائم على الوكلاء (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**متى تستخدم:** لتشغيل الخادم داخل مهمة GitHub Actions حتى يتمكن وكيل من الاستعلام عن بيئتك السحابية - واتخاذ إجراءات عليها إذا سمحت بذلك - كجزء من CI.

يثبت الإجراء [`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) الملف الثنائي ويكتب تكوين MCP (بالوضع `read_only` افتراضيًا). مرّر هذا التكوين إلى [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) عبر `claude_args`:

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

راجع [ملف README الخاص بالإجراء](https://github.com/stackql/setup-stackql-mcp) للاطلاع على مزيد من الوصفات القائمة على الوكلاء (عمليات تدقيق السحابة وتقديرات التكلفة وما إلى ذلك).

### MCP المضمن {#embedded-mcp}

**متى تستخدم:** عندما تعمل على بناء تطبيق قائم على الوكلاء وتريد تضمين نسخة محلية من خادم MCP في ملفك الثنائي - بدون `npx`، وبدون تثبيت منفصل، وبدون تبعية وقت تشغيل.

تشغل المكتبات الأصلية الملف الثنائي `stackql` الموقع رقميًا عبر stdio خلف عميل MCP SDK الرسمي لكل لغة، بحيث يحصل تطبيقك على واجهة SQL نفسها الخاضعة للحوكمة لكل موفر في السجل. تستخدم كل مكتبة الوضع `read_only` افتراضيًا - ولا يتم الانتقال إلى وضع يسمح بالكتابة إلا بشكل صريح - ويمكنها إما تنزيل الملف الثنائي والتحقق منه عند أول تشغيل، وإما تضمين نسخة محلية منه في البناء للحصول على ملف تنفيذي واحد مستقل. تجد واجهة API الكاملة لكل لغة في [مرجع MCP المضمن](/mcp/embedded).

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

يشغل crate `stackql-mcp` الملف الثنائي خلف عميل `rmcp`. تتولى الميزة الافتراضية `sidecar` تنزيل الملف الثنائي والتحقق منه عند أول تشغيل؛ أما الميزة `vendored` فتضمنه باستخدام `include_bytes!` للحصول على ملف ثنائي واحد مستقل. MSRV: Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="عرض على GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="عرض على crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

المرجع الكامل: [MCP المضمن: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

تشغل المكتبة `stackql-mcp-go` الملف الثنائي خلف عميل Go MCP SDK الرسمي. ضمّن نسخة محلية من الملف الثنائي باستخدام `go:embed` للحصول على ملف ثنائي واحد مستقل، أو دعها تتولى تنزيله والتحقق منه عند أول تشغيل. تبدأ المكتبة بالوضع `read_only` افتراضيًا.

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
    <BinaryDownloadLink iconSize={20} text="عرض على GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="عرض على pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

المرجع الكامل: [MCP المضمن: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

تشغل المكتبة `io.stackql:stackql-mcp` الملف الثنائي خلف عميل Kotlin MCP SDK الرسمي، ويربط مكون Gradle إضافي مصاحب عملية التشغيل نفسها بالبناء. تتطلب JDK 17 وKotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="عرض على GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

المرجع الكامل: [MCP المضمن: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

تشغل الحزمة `StackQL.Mcp` الملف الثنائي خلف عميل C# MCP SDK الرسمي. تعمل بوضع sidecar افتراضيًا، أو يمكنك تضمين نسخة محلية من ملف bundle كمورد بناء للحصول على ملف تنفيذي مستقل. تتطلب .NET 8 أو أحدث.

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
    <BinaryDownloadLink iconSize={20} text="عرض على GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

المرجع الكامل: [MCP المضمن: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

تستهدف المكتبة `stackql_mcp` وقت تشغيل Erlang/BEAM وتعرض الخادم كعملية فرعية في OTP عبر `child_spec()`، بحيث يمكن وضعه داخل شجرة الإشراف (supervision tree) الخاصة بك. تبدأ المكتبة بالوضع `read_only` افتراضيًا.

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
    <BinaryDownloadLink iconSize={20} text="عرض على GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

المرجع الكامل: [MCP المضمن: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

تشغل الحزمة `stackql-mcp-swift` الملف الثنائي خلف عميل Swift MCP SDK الرسمي. الملف الثنائي `darwin-universal` موقع رقميًا بمعرف Developer ID وموثق من Apple، لذا يمكنك تجميعه داخل تطبيق `.app` موقع رقميًا مع الحفاظ على صلاحية توثيق التطبيق. تتطلب macOS 13+ وSwift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="عرض على GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

المرجع الكامل: [MCP المضمن: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### نموذج الثقة {#trust-model}

تنطبق خصائص الأمان نفسها على جميع القنوات:

- الملف الثنائي `stackql` المضمن موقع رقميًا بتقنية Authenticode (Windows) وموثق من Apple (macOS).
- يأتي كل ملف bundle `.mcpb` مع مجموع اختباري SHA-256 منشور في صفحة الإصدار.
- تتحقق أداتا التشغيل `npx` و`uvx` من قيمة SHA-256 للملف الثنائي الذي تم تنزيله قبل أول استخدام.
- يشهد إدخال [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) على قيم التجزئة لكل منصة.

## استخدام GitHub Actions {#using-github-actions}

تتوفر [إجراءات StackQL لـ GitHub Actions](https://github.com/stackql/stackql-actions-demo) للاستخدام في سير عمل GitHub Actions لديك. الإجراءات التالية متاحة:

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

انشر مكدسات البنية التحتية أو اختبرها باستخدام StackQL مباشرة من سير عمل GitHub لديك. يتيح هذا الإجراء سير عمل البنية التحتية كتعليمات برمجية (IaC) باستخدام بناء جملة شبيه بـ SQL، مما يتيح لك تعريف الموارد السحابية ونشرها وإدارتها عبر موفرين متعددين في سير عمل واحد.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="عرض على GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="عرض على GitHub"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__مثال على الاستخدام__

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

إعداد StackQL في سير عمل GitHub Actions لديك.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="عرض على GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="عرض على GitHub"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__مثال على الاستخدام__

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

ثبّت خادم StackQL MCP في سير عمل GitHub Actions لديك. يثبت الإجراء الملف الثنائي الموقع رقميًا ويكتب تكوين `mcpServers` (بالوضع `read_only` افتراضيًا) تستخدمه خطوة قائمة على الوكلاء مثل `anthropics/claude-code-action` عبر `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="عرض على GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="عرض على GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__مثال على الاستخدام__

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

نفّذ أوامر StackQL في سير عمل GitHub Actions لديك. يمكنك تقديم الاستعلامات مباشرة في التكوين أو من ملف في المستودع.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="عرض على GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="عرض على GitHub"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__مثال على الاستخدام__

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

شغّل اختبارات الوحدة في سير عمل GitHub Actions لديك (لعمليات التحقق الخاصة بالضمان أو الحوكمة أو أمان السحابة).

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="عرض على GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="عرض على GitHub"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__مثال على الاستخدام__

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


## مكتبات أخرى {#other-libraries}

يوفر StackQL عدة وسائل للتكامل للاستخدام في لغات برمجة وبيئات مختلفة. توسع هذه المكتبات إمكانات StackQL، مما يسهل دمج الاستعلام عن الموارد السحابية وإدارتها في تطبيقاتك الحالية وسير العمل لديك.

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### حزمة Python `pystackql` {#pystackql-python-package}

غلاف Python لاستخدام StackQL في برامج Python الخاصة بك. الحزمة `pystackql` متاحة على [__PyPI__](https://pypi.org/project/pystackql/)، ووثائق الحزمة `pystackql` متاحة عبر [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). لتثبيت الحزمة `pystackql`، شغّل الأمر التالي:

```bash
pip install pystackql
```

يوضح المثال التالي استخدام الحزمة `pystackql` مع المكتبة `pandas` لتشغيل استعلامات StackQL وإعادة النتائج إلى كائن `pandas.DataFrame`:

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
