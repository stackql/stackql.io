---
title: Instalación
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "Instale StackQL en macOS, Linux, Windows, Docker o un shell en la nube y use el servidor MCP de StackQL desde Claude Desktop, ChatGPT, Goose y otros clientes MCP, o mediante npx, uvx, pip, Docker y GitHub Actions."
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

Aquí se proporcionan las instrucciones para instalar StackQL en distintas plataformas.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Shells en la nube', value: 'shells', },
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
  
  StackQL está disponible en macOS mediante Homebrew y el instalador `pkg`; se admiten tanto la arquitectura ARM (M1/Apple Silicon) como la AMD con un único instalador multiarquitectura.

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (binario firmado y notarizado)', value: 'curl', },
      { label: 'Descarga del paquete', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Para instalar mediante Homebrew, ejecute el siguiente comando en su terminal:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  Para instalar un binario ejecutable `stackql` firmado y notarizado en el directorio actual, ejecute el siguiente comando en su terminal:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL está disponible como instalador `pkg` interactivo, firmado y notarizado, para macOS.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="Descargar PKG para macOS"
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

  StackQL está disponible para todas las arquitecturas de Linux.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'Descarga del paquete', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  Para instalar un binario ejecutable `stackql` en el directorio actual para su arquitectura de Linux (amd64 o arm64), ejecute el siguiente comando en su terminal:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  Como alternativa, puede descargar los binarios aquí:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Descargar ZIP para Linux (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Descargar ZIP para Linux (arm64)"
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

  StackQL está disponible en Windows mediante Chocolatey, el script de instalación de PowerShell y el instalador MSI. Los builds nativos para AMD64 (x64) y ARM64 están disponibles como instaladores MSI y archivos ZIP que contienen `stackql.exe`. El build para AMD64 también se ejecuta en ARM64 mediante emulación. Todos los ejecutables están firmados con un certificado de firma de código Authenticode.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'Descarga de MSI/ZIP', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Para instalar mediante Chocolatey, ejecute el siguiente comando en su terminal de PowerShell o `cmd`:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Para instalar un ejecutable firmado con Authenticode en el directorio actual, ejecute lo siguiente en un terminal de PowerShell: 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  Descargue a continuación el instalador o el ejecutable firmado de `stackql` para Windows. Al hacer clic en un botón de descarga se selecciona AMD64 de forma predeterminada; use su menú desplegable para elegir AMD64 o ARM64 nativo.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Descargar MSI para Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Descargar MSI para Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Descargar MSI para Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Descargar ZIP para Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Descargar ZIP para Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Descargar ZIP para Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  Los builds de StackQL se publican en [DockerHub](https://hub.docker.com/u/stackql).  

  __Docker Hub__  

  Para descargar la imagen de contenedor de StackQL, ejecute el siguiente comando:

  ```bash
  docker pull stackql/stackql
  ```

  A partir de `v1.0`, la imagen se basa en Ubuntu 26.04 LTS y se publica una variante con Python 3.13 preinstalado con el sufijo de tag `-python3.13` (alias `-python3`), por ejemplo:

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

`stackql-deploy` es un marco de IaC sin estado construido sobre StackQL. Para ver la documentación completa, consulte la [__documentación de StackQL Deploy__](https://stackql-deploy.io/).

__Linux / macOS__

Descarga y extrae el binario más reciente de `stackql-deploy` en un solo paso. El instalador detecta su sistema operativo y su arquitectura (incluidos Apple Silicon e Intel mediante un binario universal para macOS) y obtiene automáticamente el archivo de versión correcto.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Descarga y extrae el binario más reciente de `stackql-deploy` en el directorio actual.
El instalador detecta su plataforma y obtiene automáticamente el archivo de versión correcto.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Si tiene instalada la cadena de herramientas de Rust, este comando compila e instala el binario directamente desde [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## Shells en la nube

  StackQL se puede usar directamente desde los shells en la nube y los terminales web incorporados de las principales plataformas en la nube y de datos. Esto ofrece una experiencia fluida, ya que estos entornos están preautorizados con la identidad con la que ha iniciado sesión, lo que elimina la necesidad de configurar la autenticación por separado.

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

  AWS CloudShell proporciona un shell basado en navegador con la AWS CLI preinstalada y autenticada. Ejecutar StackQL en AWS CloudShell le permite consultar y gestionar recursos de AWS sin pasos de autenticación adicionales. Para obtener instrucciones detalladas, consulte nuestro [tutorial de AWS CloudShell](/quick-starts/aws/aws-cloud-shell).

  Primero, descargue el paquete de StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  A continuación, ejecute el script de StackQL para AWS CloudShell:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  Este script inicia un shell de comandos de StackQL con sus credenciales de AWS CloudShell, lo que le permite empezar a consultar recursos de AWS de inmediato.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell proporciona un entorno de shell accesible desde el navegador con la Azure CLI autenticada previamente con su cuenta de Azure. Usar StackQL en Azure Cloud Shell permite consultar sus recursos de Azure de forma fluida y sin configuración adicional. Para conocer todos los detalles, consulte nuestra [guía de Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell).  

  Primero, descargue el paquete de StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  A continuación, ejecute el script de StackQL para Azure Cloud Shell:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  Este script configura automáticamente una sesión de StackQL para que use sus credenciales de Azure Cloud Shell, lo que permite acceder de inmediato a consultar sus recursos de Azure.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell ofrece un entorno de desarrollo y operaciones con la Google Cloud CLI ya autenticada. Ejecutar StackQL en Google Cloud Shell le permite consultar recursos de GCP con su autenticación existente. Obtenga más información en nuestra [guía de Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell).  

  Primero, descargue el paquete de StackQL:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  A continuación, ejecute el script de StackQL para Google Cloud Shell:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  El script configura StackQL para que use sus credenciales de Google Cloud Shell, lo que le permite empezar a consultar de inmediato sus recursos de GCP con sintaxis SQL.

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web Terminal

  Los espacios de trabajo de Databricks incluyen un terminal web que se ejecuta como el usuario que ha iniciado sesión. Ejecutar StackQL allí le permite consultar su espacio de trabajo con su identidad de Databricks, sin configurar la autenticación por separado. Para ver consultas de ejemplo y la autenticación a nivel de cuenta, consulte nuestra [guía de Databricks Web Terminal](/blog/tutorials/stackql-in-databricks-web-terminal).

  Primero, descargue el paquete de StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  A continuación, ejecute el script de shell de StackQL para Databricks:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  Esto inicia una sesión de StackQL con la identidad de su espacio de trabajo de Databricks, de modo que puede consultar de inmediato recursos con ámbito de espacio de trabajo, como `databricks_workspace.iam.vw_user_entitlements`. Para las consultas a nivel de cuenta (aprovisionamiento, facturación, IAM de la cuenta), establezca las variables del principal de servicio OAuth2 de Databricks como se describe en la guía.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Usar con clientes MCP {#using-with-mcp-clients}

El servidor MCP de StackQL se ejecuta localmente sobre stdio, por lo que las credenciales de los providers permanecen en su equipo sea cual sea el cliente que use. Elija su cliente a continuación: cada pestaña presenta primero la vía del directorio o de un solo clic y la configuración manual como alternativa. Para ver todos los canales de distribución, incluidos `npx`, Python, Docker y CI, consulte [Instalar el servidor MCP](#installing-the-mcp-server).

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

  La forma recomendada de instalar el servidor MCP de StackQL en Claude Desktop es desde el [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Settings -> Connectors -> Add -> Browse Connectors**, busque **StackQL**) - Claude Desktop selecciona automáticamente el binario firmado correcto para su plataforma.  Como alternativa, a cada versión de StackQL se adjuntan MCP Bundles precompilados (`.mcpb`) para instalar desde un archivo descargado - en ninguno de los dos casos se requiere una instalación de StackQL por separado.  Los bundles están disponibles para macOS, Windows y Linux.

  Las descargas de MCPB para Linux y Windows usan **amd64** (x64) de forma predeterminada. Use los menús desplegables para seleccionar **amd64** o **arm64**.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Descargar MCPB para macOS"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Descargar MCPB para Linux"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Descargar MCPB para Linux (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Descargar MCPB para Linux (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Descargar MCPB para Windows"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Descargar MCPB para Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Descargar MCPB para Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

El servidor MCP de StackQL también figura en el [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) como `io.github.stackql/stackql-mcp`.

:::

:::tip

El bundle es una de las varias formas de ejecutar el servidor MCP de StackQL.  Consulte [__Usar StackQL con Claude Desktop__](/getting-started/claude-desktop) para ver la guía paso a paso completa de Claude Desktop, o [__Instalar el servidor MCP__](#installing-the-mcp-server) para las opciones de npx, Docker, Python, CI y manual.

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

  ### ChatGPT y Codex

  StackQL se distribuye como plugin local para la aplicación de escritorio de ChatGPT y la CLI de Codex. El plugin ejecuta el servidor MCP de StackQL sobre stdio en su equipo, por lo que las credenciales de los providers nunca salen de él. Se requiere Node.js 24.22.1 o posterior (22.23.4 o 26.12.1 en esas líneas de versiones); el plugin solo necesita los módulos incorporados de Node.js, descarga el binario `stackql` firmado en el primer uso y verifica su SHA-256.

  **Aplicación de escritorio de ChatGPT (directorio de plugins):**  
  
  abra el directorio de plugins en la aplicación de escritorio de ChatGPT, busque **StackQL** y haga clic en **Install**, después ejecute la configuración del plugin; la skill `stackql-setup` incluida explica paso a paso las credenciales y una primera consulta. La entrada lleva una insignia **Desktop only**: el plugin solo se instala desde la aplicación de escritorio, no desde ChatGPT web ni móvil, y sus herramientas están disponibles en el modo Work, en los hilos que se ejecutan en su equipo. Si la entrada aún no aparece en su directorio, instale desde el marketplace del repositorio.

  **Aplicación de escritorio de ChatGPT (marketplace del repositorio):**  
  
  abra **Settings -> Plugins -> Add -> Add from GitHub**, escriba `stackql/stackql`, después instale **StackQL** desde la sección **Personal** del directorio de plugins y reinicie la aplicación.

  **CLI de Codex:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  Reinicie ChatGPT de escritorio o Codex y después pruebe: *Usa StackQL para instalar el provider de GitHub si hace falta y luego enumera los cinco primeros servicios de GitHub.* Las credenciales de los providers van en `~/.stackql/.env` con los nombres de variables de entorno estándar de cada provider (por ejemplo, `AWS_ACCESS_KEY_ID` y `AWS_SECRET_ACCESS_KEY`); después de editar el archivo, pida al agente que llame a `reload_credentials`.

  **Sin el plugin:**   
  
  en **Settings -> Plugins -> MCPs -> Connect to a custom MCP**, elija **STDIO**, establezca el comando en `npx` y agregue los argumentos `-y` y `@stackql/mcp-server`. Proporcione las credenciales de los providers como variables de entorno en ese formulario.

<br />
:::note

ChatGPT web y móvil no pueden ejecutar un servidor stdio local, por lo que el plugin es solo para escritorio y Codex. Un error abierto de ChatGPT de escritorio ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) puede ocultar las herramientas MCP locales en las sesiones de **Chat**; los hilos del modo Work, los hilos de Codex y la CLI de Codex no se ven afectados.

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

  Goose ejecuta extensiones stdio directamente, por lo que el lanzador `npx` es todo lo que necesita (Node.js 18 o posterior).

  **Goose Desktop:** haga clic en el botón siguiente. Goose se abre, muestra el comando que va a ejecutar y le pide que confirme; después agrega StackQL como extensión.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Instalar en Goose Desktop</a>
  </Box>

  El botón es un enlace profundo `goose://`; también puede pegarlo en la barra de direcciones de su navegador o abrirlo desde un terminal con `open` (macOS), `start` (Windows) o `xdg-open` (Linux):

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  o ejecute `goose configure`, elija **Add Extension -> Command-line Extension** y escriba el mismo comando. Proporcione las credenciales de los providers como variables de entorno de la extensión cuando se le soliciten.

<br />
:::note

Goose ha retirado su directorio de extensiones dentro de la aplicación en favor del [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql), donde StackQL figura como `io.github.stackql/stackql-mcp`. Cuando Goose publique su importación desde el registro, StackQL se podrá encontrar allí sin ninguna otra acción.

:::

</Box>

</TabItem>
</Tabs>

## Instalar el servidor MCP {#installing-the-mcp-server}

El servidor MCP de StackQL permite a los agentes de IA consultar y aprovisionar recursos en la nube con StackQL. Se distribuye por varios canales - todos ejecutan el mismo servidor, así que elija el canal que se ajuste a su cliente y a sus requisitos de confianza. Las opciones siguientes se enumeran aproximadamente por orden de confianza y facilidad.

### Marketplaces y directorios {#marketplaces-and-directories}

El servidor está publicado en el Official MCP Registry como `io.github.stackql/stackql-mcp`; los registros de paquetes siguientes contienen los artefactos distribuibles, y los directorios derivados toman la entrada de allí.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Registros', value: 'registries', },
    { label: 'Directorios', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Registro | Tipo | Publicado mediante | Entrada | Notas |
|---|---|---|---|---|
| Official MCP Registry | Registro canónico de metadatos | CLI `mcp-publisher` + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | anuncia los 8 paquetes (`mcpb x5`, `oci`, `npm`, `pypi`) |
| npm | Registro de paquetes | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | lanzador `npx` |
| PyPI | Registro de paquetes | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | lanzador `uvx` / `pip` |
| Docker Hub | Registro de contenedores | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | multiarquitectura `amd64` + `arm64` |
| GitHub Actions Marketplace | Marketplace de CI (acción `setup-stackql-mcp`) | Repositorio público + versión + entrada del marketplace | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Editor verificado; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Entradas en directorios y agregadores, aproximadamente por orden de importancia para el descubrimiento:

| Directorio | Tipo | Entrada |
|---|---|---|
| __Anthropic Connector Directory__ | Directorio dentro de la aplicación Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | Directorio dentro de la aplicación de escritorio de ChatGPT (solo escritorio) | busque **StackQL** en la aplicación de escritorio de ChatGPT; consulte [ChatGPT y Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | Directorio de un cliente IDE | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | El mayor agregador | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Descubrimiento, impulsor del registro | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Marketplace con búsqueda | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Agregador | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Sitio de tipo awesome-list | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Bundle `.mcpb` precompilado {#prebuilt-mcpb-bundle}

**Cuándo usarlo:** Claude Desktop sin acceso al Connector Directory, o cuando quiere fijar una versión específica o verificar usted mismo las sumas de comprobación antes de instalar.  Para la mayoría de los usuarios de Claude Desktop, el [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) es el método de instalación recomendado.

A cada versión de StackQL se adjunta un MCP Bundle precompilado para cada plataforma:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

donde `<platform>` es `darwin-universal`, `windows-x64` (AMD64), `windows-arm64`, `linux-x64` o `linux-arm64`. Los botones de descarga de cada plataforma están en la pestaña **Claude Desktop**, en [Usar con clientes MCP](#using-with-mcp-clients). Cada bundle tiene su suma de comprobación `.sha256` correspondiente en la [página de la versión](https://github.com/stackql/stackql/releases/latest). Verifíquela y después instale mediante **Settings -> Extensions** en Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Consulte [Usar StackQL con Claude Desktop](/getting-started/claude-desktop#install-from-a-downloaded-bundle) para ver la guía paso a paso completa.

### `claude_desktop_config.json` manual {#manual-claude_desktop_configjson}

**Cuándo usarlo:** ya tiene el binario `stackql` en su PATH y usa Claude Desktop o cualquier otro cliente MCP stdio.

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

Los tres argumentos son imprescindibles:

- `--mcp.server.type=stdio` selecciona el transporte stdio que usan los clientes integrados en editores.
- `--approot` dirige la caché de providers a un directorio con permiso de escritura. Los clientes MCP pueden iniciar el servidor con el directorio de trabajo establecido en `/`, donde no se puede escribir.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` deshabilita el log de auditoría, que de lo contrario usa como directorio predeterminado el directorio de trabajo (en el que quizá no se pueda escribir).

Agregue las credenciales de los providers con un bloque `"env"` y ajuste el contrato de seguridad con `"mode"` - consulte [Modos del servidor](/command-line-usage/mcp#server-modes).

### `npx` (sin instalación) {#npx-no-install}

**Cuándo usarlo:** cualquier entorno Node, cuando no quiere una instalación global de StackQL.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

El lanzador [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) descarga el binario `stackql` firmado en la primera ejecución, verifica su SHA-256 y lo guarda en caché para las ejecuciones posteriores.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**Cuándo usarlo:** un entorno Python - el mismo lanzador empaquetado para Python, que comparte la caché de binarios que llena el lanzador npx.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

O instálelo en el entorno actual con `pip install stackql-mcp-server` (Python 3.9+). El paquete es [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) en PyPI.

### Docker {#docker}

**Cuándo usarlo:** un runtime en contenedor o aislado de otra forma. La imagen es multiarquitectura (amd64 y arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

Como configuración de cliente:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

La imagen es [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) en Docker Hub.

### CI y workflows agénticos (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**Cuándo usarlo:** ejecute el servidor dentro de un job de GitHub Actions para que un agente pueda consultar su nube (y, si usted lo permite, actuar sobre ella) como parte de la CI.

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) instala el binario y escribe una configuración de MCP (con el modo `read_only` de forma predeterminada). Pase esa configuración a [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) mediante `claude_args`:

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

Consulte el [README de la acción](https://github.com/stackql/setup-stackql-mcp) para ver más recetas agénticas (auditorías de nube, estimaciones de costos, etc.).

### MCP integrado {#embedded-mcp}

**Cuándo usarlo:** está creando una aplicación agéntica y quiere incorporar el servidor MCP a su propio binario mediante vendoring - sin `npx`, sin instalación por separado, sin dependencia de runtime.

Las bibliotecas nativas inician el binario `stackql` firmado sobre stdio detrás del cliente del SDK oficial de MCP de cada lenguaje, de modo que su aplicación obtiene la misma interfaz SQL gobernada para todos los providers del registro. Cada biblioteca usa `read_only` de forma predeterminada (el paso a un modo con escritura siempre es explícito) y puede descargar y verificar el binario en la primera ejecución o incorporarlo a su build mediante vendoring para obtener un único ejecutable autónomo. La API completa por lenguaje está en la [referencia de MCP integrado](/mcp/embedded).

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

El crate `stackql-mcp` inicia el binario detrás de un cliente `rmcp`. La funcionalidad predeterminada `sidecar` descarga y verifica el binario en la primera ejecución; la funcionalidad `vendored` lo integra con `include_bytes!` para obtener un único binario autónomo. MSRV Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="Ver en GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Ver en crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Referencia completa: [MCP integrado: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

La biblioteca `stackql-mcp-go` inicia el binario detrás del cliente del SDK oficial de MCP para Go. Incorpore el binario mediante vendoring con `go:embed` para obtener un único binario autónomo, o deje que lo descargue y verifique en la primera ejecución. Se inicia en modo `read_only` de forma predeterminada.

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
    <BinaryDownloadLink iconSize={20} text="Ver en GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Ver en pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Referencia completa: [MCP integrado: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

La biblioteca `io.stackql:stackql-mcp` inicia el binario detrás del cliente del SDK oficial de MCP para Kotlin, y un plugin de Gradle complementario conecta ese mismo inicio a un build. Requiere JDK 17 y Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="Ver en GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Referencia completa: [MCP integrado: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

El paquete `StackQL.Mcp` inicia el binario detrás del cliente del SDK oficial de MCP para C#. Sidecar de forma predeterminada, o incorpore el bundle mediante vendoring como recurso de build para obtener un ejecutable autónomo. Requiere .NET 8 o posterior.

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
    <BinaryDownloadLink iconSize={20} text="Ver en GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Referencia completa: [MCP integrado: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

La biblioteca `stackql_mcp` está dirigida al runtime Erlang/BEAM y expone el servidor como un hijo de OTP mediante `child_spec()`, de modo que puede formar parte de su propio árbol de supervisión. Se inicia en modo `read_only` de forma predeterminada.

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
    <BinaryDownloadLink iconSize={20} text="Ver en GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Referencia completa: [MCP integrado: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

El paquete `stackql-mcp-swift` inicia el binario detrás del cliente del SDK oficial de MCP para Swift. El binario `darwin-universal` está firmado con Developer ID y notarizado por Apple, por lo que puede incluirlo dentro de una `.app` firmada y mantener válida la notarización de la aplicación. Requiere macOS 13+ y Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="Ver en GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Referencia completa: [MCP integrado: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### Modelo de confianza {#trust-model}

Las mismas propiedades de seguridad se cumplen en todos los canales:

- El binario `stackql` integrado está firmado con Authenticode (Windows) y notarizado por Apple (macOS).
- Cada bundle `.mcpb` se distribuye con una suma de comprobación SHA-256 publicada en la página de la versión.
- Los lanzadores `npx` y `uvx` verifican el SHA-256 del binario descargado antes del primer uso.
- La entrada del [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) certifica los hashes por plataforma.

## Usar GitHub Actions {#using-github-actions}

Las [GitHub Actions de StackQL](https://github.com/stackql/stackql-actions-demo) están disponibles para usarlas en sus workflows de GitHub Actions. Están disponibles las siguientes acciones:

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

Despliegue o pruebe stacks de infraestructura con StackQL directamente desde sus workflows de GitHub. Esta acción habilita workflows de infraestructura como código (IaC) con una sintaxis similar a SQL, lo que le permite definir, desplegar y gestionar recursos en la nube de varios providers en un único workflow.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver en GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Ver en GitHub"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ejemplo de uso__

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

Configure StackQL en su workflow de GitHub Actions.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver en GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Ver en GitHub"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ejemplo de uso__

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

Instale el servidor MCP de StackQL en su workflow de GitHub Actions.  La acción instala el binario firmado y escribe una configuración `mcpServers` (con el modo `read_only` de forma predeterminada) que un paso agéntico como `anthropics/claude-code-action` consume mediante `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver en GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver en GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ejemplo de uso__

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

Ejecute comandos de StackQL en su workflow de GitHub Actions.  Las consultas se pueden proporcionar en línea o desde un archivo del repositorio.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver en GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Ver en GitHub"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ejemplo de uso__

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

Realice pruebas unitarias en su workflow de GitHub Actions (para comprobaciones de aseguramiento, gobernanza o seguridad en la nube).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver en GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Ver en GitHub"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Ejemplo de uso__

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


## Otras bibliotecas {#other-libraries}

StackQL ofrece varios métodos de integración que permiten usarlo en distintos lenguajes de programación y entornos. Estas bibliotecas amplían la funcionalidad de StackQL y facilitan la incorporación de la consulta y la gestión de recursos en la nube a sus aplicaciones y workflows existentes.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### Paquete de Python `pystackql` {#pystackql-python-package}

Wrapper de Python para usar StackQL en sus programas de Python. El paquete `pystackql` está disponible en [__PyPi__](https://pypi.org/project/pystackql/); la documentación del paquete `pystackql` está disponible en [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). Para instalar el paquete `pystackql`, ejecute el siguiente comando:

```bash
pip install pystackql
```

El siguiente ejemplo muestra el paquete `pystackql` usado junto con `pandas` para ejecutar consultas de StackQL y devolver los resultados en un `pandas.DataFrame`:

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
