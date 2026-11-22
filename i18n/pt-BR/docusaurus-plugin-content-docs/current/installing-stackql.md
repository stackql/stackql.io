---
title: Instalação
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "Instale o StackQL no macOS, Linux, Windows, Docker ou em um shell na nuvem e use o servidor MCP do StackQL no Claude Desktop, ChatGPT, Goose e outros clientes MCP, ou via npx, uvx, pip, Docker e GitHub Actions."
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

Esta página contém as instruções para instalar o StackQL em diversas plataformas.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Shells na nuvem', value: 'shells', },
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
  
  O StackQL está disponível no macOS via Homebrew e pelo instalador `pkg`; um único instalador multiarquitetura oferece suporte tanto à arquitetura ARM (M1/Apple Silicon) quanto à Intel (x86_64).

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (binário assinado e notarizado)', value: 'curl', },
      { label: 'Download do pacote', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Para instalar via Homebrew, execute o comando a seguir no seu terminal:

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  Para instalar um binário executável `stackql` assinado e notarizado no diretório atual, execute o comando a seguir no seu terminal:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  O StackQL está disponível como um instalador `pkg` interativo, assinado e notarizado, para macOS.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="Baixar PKG para macOS"
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

  O StackQL está disponível para todas as arquiteturas Linux.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'Download do pacote', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  Para instalar um binário executável `stackql` no diretório atual para a sua arquitetura Linux (amd64 ou arm64), execute o comando a seguir no seu terminal:

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  Como alternativa, você pode baixar os binários aqui:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Baixar ZIP para Linux (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Baixar ZIP para Linux (arm64)"
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

  O StackQL está disponível no Windows via Chocolatey, pelo script de instalação do PowerShell e pelo instalador MSI. Builds nativos AMD64 (x64) e ARM64 estão disponíveis como instaladores MSI e arquivos ZIP que contêm `stackql.exe`. O build AMD64 também funciona em ARM64 por emulação. Todos os executáveis são assinados com um certificado de assinatura de código Authenticode.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'Download de MSI/ZIP', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Para instalar via Chocolatey, execute o comando a seguir no seu terminal PowerShell ou `cmd`:

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Para instalar um executável Authenticode no diretório atual, execute o seguinte em um terminal PowerShell: 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  Baixe abaixo o instalador ou o executável `stackql` assinado para Windows. Clicar em um botão de download seleciona AMD64 por padrão; use o menu suspenso do botão para escolher AMD64 ou ARM64 nativo.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Baixar MSI para Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Baixar MSI para Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Baixar MSI para Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Baixar ZIP para Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Baixar ZIP para Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Baixar ZIP para Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  Os builds do StackQL são publicados no [Docker Hub](https://hub.docker.com/u/stackql).  

  __Docker Hub__  

  Para baixar a imagem de contêiner do StackQL, execute o comando a seguir:

  ```bash
  docker pull stackql/stackql
  ```

  A partir da `v1.0`, a imagem é baseada no Ubuntu 26.04 LTS, e uma variante com o Python 3.13 pré-instalado é publicada com o sufixo de tag `-python3.13` (alias `-python3`), por exemplo:

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

O `stackql-deploy` é um framework de IaC sem estado construído sobre o StackQL. Para ver a documentação completa, consulte a [__documentação do StackQL Deploy__](https://stackql-deploy.io/).

__Linux / macOS__

Baixa e extrai o binário mais recente do `stackql-deploy` em uma única etapa. O instalador detecta o seu sistema operacional e a sua arquitetura (incluindo Apple Silicon e Intel, por meio de um binário universal para macOS) e obtém automaticamente o artefato correto da versão.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Baixa e extrai o binário mais recente do `stackql-deploy` no diretório atual.
O instalador detecta a sua plataforma e obtém automaticamente o artefato correto da versão.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Se você tiver o toolchain do Rust instalado, este comando compila e instala o binário diretamente do [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## Shells na nuvem

  Você pode usar o StackQL diretamente nos shells na nuvem e terminais web integrados das principais plataformas de nuvem e de dados. Isso oferece uma experiência sem atritos, pois esses ambientes já estão pré-autorizados com a identidade com a qual você fez login, o que dispensa uma configuração de autenticação separada.

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

  O AWS CloudShell oferece um shell no navegador com a AWS CLI pré-instalada e autenticada. Executar o StackQL no AWS CloudShell permite consultar e gerenciar recursos da AWS sem etapas adicionais de autenticação. Para obter instruções detalhadas, consulte o guia de início rápido [StackQL no AWS CloudShell](/quick-starts/aws/aws-cloud-shell).

  Primeiro, baixe o pacote do StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  Em seguida, execute o script do StackQL para o AWS CloudShell:
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  Esse script inicia um shell de comandos do StackQL com as suas credenciais do AWS CloudShell, para que você comece a consultar recursos da AWS imediatamente.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  O Azure Cloud Shell oferece um ambiente de shell acessível pelo navegador com a Azure CLI pré-autenticada na sua conta do Azure. Usar o StackQL no Azure Cloud Shell permite consultar os seus recursos do Azure sem atritos e sem configuração adicional. Para obter todos os detalhes, consulte nosso [guia do Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell).  

  Primeiro, baixe o pacote do StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  Em seguida, execute o script do StackQL para o Azure Cloud Shell:

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  Esse script configura automaticamente uma sessão do StackQL para usar as suas credenciais do Azure Cloud Shell, o que permite consultar imediatamente os seus recursos do Azure.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  O Google Cloud Shell oferece um ambiente de desenvolvimento e operações com a Google Cloud CLI já autenticada. Executar o StackQL no Google Cloud Shell permite consultar recursos do GCP com a sua autenticação existente. Saiba mais no nosso [guia do Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell).  

  Primeiro, baixe o pacote do StackQL:
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  Em seguida, execute o script do StackQL para o Google Cloud Shell:

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  O script configura o StackQL para usar as suas credenciais do Google Cloud Shell, para que você comece imediatamente a consultar os seus recursos do GCP com sintaxe SQL.

  </TabItem>
  <TabItem value="databricks">

  ### Databricks Web Terminal

  Os espaços de trabalho do Databricks incluem um terminal web que é executado como o usuário conectado. Executar o StackQL nele permite consultar o seu espaço de trabalho com a sua identidade do Databricks, sem uma configuração de autenticação separada. Para ver exemplos de consultas e a autenticação no nível da conta, consulte nosso [guia do Databricks Web Terminal](/blog/tutorials/stackql-in-databricks-web-terminal).

  Primeiro, baixe o pacote do StackQL:

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  Em seguida, execute o script de shell do StackQL para o Databricks:

  ```bash
  ./stackql-databricks-shell.sh
  ```

  Isso inicia uma sessão do StackQL com a identidade do seu espaço de trabalho do Databricks, para que você possa consultar imediatamente recursos no escopo do espaço de trabalho, como `databricks_workspace.iam.vw_user_entitlements`. Para consultas no nível da conta (provisionamento, faturamento, IAM da conta), defina as variáveis do principal de serviço OAuth2 do Databricks conforme descrito no guia.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Usar com clientes MCP {#using-with-mcp-clients}

O servidor MCP do StackQL é executado localmente via stdio, então as credenciais dos providers ficam na sua máquina, seja qual for o cliente que você usar. Escolha o seu cliente abaixo: cada aba mostra primeiro a opção pelo diretório ou com um clique e, como alternativa, a configuração manual. Para ver todos os canais de distribuição, incluindo `npx`, Python, Docker e CI, consulte [Instalar o servidor MCP](#installing-the-mcp-server).

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

  A forma recomendada de instalar o servidor MCP do StackQL no Claude Desktop é pelo [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Settings -> Connectors -> Add -> Browse Connectors**, pesquise **StackQL**) - o Claude Desktop seleciona automaticamente o binário assinado correto para a sua plataforma.  Como alternativa, cada versão do StackQL traz MCP Bundles (`.mcpb`) pré-compilados anexados, para instalação a partir de um arquivo baixado - em nenhum dos casos é necessária uma instalação separada do StackQL.  Os bundles estão disponíveis para macOS, Windows e Linux.

  Os downloads de MCPB para Linux e Windows usam **amd64** (x64) por padrão. Use os menus suspensos para selecionar **amd64** ou **arm64**.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Baixar MCPB para macOS"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Baixar MCPB para Linux"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Baixar MCPB para Linux (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Baixar MCPB para Linux (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Baixar MCPB para Windows"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Baixar MCPB para Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Baixar MCPB para Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

O servidor MCP do StackQL também está listado no [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) como `io.github.stackql/stackql-mcp`.

:::

:::tip

O bundle é uma das várias formas de executar o servidor MCP do StackQL.  Consulte [__Usar o StackQL com o Claude Desktop__](/getting-started/claude-desktop) para ver o passo a passo completo do Claude Desktop, ou [__Instalar o servidor MCP__](#installing-the-mcp-server) para as opções npx, Docker, Python, CI e manual.

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

  ### ChatGPT e Codex

  O StackQL é distribuído como um plugin local para o aplicativo de desktop do ChatGPT e a CLI do Codex. O plugin executa o servidor MCP do StackQL via stdio na sua máquina, então as credenciais dos providers nunca saem dela. É necessário o Node.js 24.22.1 ou posterior (22.23.4 ou 26.12.1 nessas linhas de versão); o plugin precisa apenas dos módulos integrados do Node.js, baixa o binário `stackql` assinado no primeiro uso e verifica o SHA-256 dele.

  **Aplicativo de desktop do ChatGPT (diretório de plugins):**  
  
  abra o diretório de plugins no aplicativo de desktop do ChatGPT, pesquise **StackQL**, clique em **Install** e depois execute a configuração do plugin; a skill `stackql-setup` incluída orienta você na configuração das credenciais e em uma primeira consulta. A listagem exibe o selo **Desktop only**: o plugin só é instalado pelo aplicativo de desktop, não pelo ChatGPT na web ou no celular, e as ferramentas dele ficam disponíveis no modo Work, em threads executadas no seu computador. Se a listagem ainda não aparecer no seu diretório, instale pelo marketplace do repositório.

  **Aplicativo de desktop do ChatGPT (marketplace do repositório):**  
  
  abra **Settings -> Plugins -> Add -> Add from GitHub**, digite `stackql/stackql`, depois instale o **StackQL** pela seção **Personal** do diretório de plugins e reinicie o aplicativo.

  **CLI do Codex:**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  Reinicie o aplicativo de desktop do ChatGPT ou o Codex e depois experimente: *Use o StackQL para instalar o provider do GitHub, se necessário, e depois liste os cinco primeiros serviços do GitHub.* As credenciais dos providers ficam em `~/.stackql/.env`, com os nomes padrão de variáveis de ambiente de cada provider (por exemplo, `AWS_ACCESS_KEY_ID` e `AWS_SECRET_ACCESS_KEY`); depois de editar o arquivo, peça ao agente para chamar `reload_credentials`.

  **Sem o plugin:**   
  
  em **Settings -> Plugins -> MCPs -> Connect to a custom MCP**, escolha **STDIO**, defina o comando como `npx` e adicione os argumentos `-y` e `@stackql/mcp-server`. Informe as credenciais dos providers como variáveis de ambiente nesse formulário.

<br />
:::note

O ChatGPT na web e no celular não consegue executar um servidor stdio local, por isso o plugin funciona apenas no desktop e no Codex. Um bug em aberto do aplicativo de desktop do ChatGPT ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) pode ocultar as ferramentas MCP locais nas sessões de **Chat**; as threads do modo Work, as threads do Codex e a CLI do Codex não são afetadas.

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

  O Goose executa extensões stdio diretamente, então o inicializador `npx` é tudo de que você precisa (Node.js 24.22.1 ou posterior, ou 22.23.4 ou 26.12.1 nessas linhas de versão).

  **Goose Desktop:** clique no botão abaixo. O Goose abre, mostra o comando que vai executar e pede a sua confirmação; depois, adiciona o StackQL como extensão.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Instalar no Goose Desktop</a>
  </Box>

  O botão é um deep link `goose://`; você também pode colá-lo na barra de endereços do navegador ou abri-lo em um terminal com `open` (macOS), `start` (Windows) ou `xdg-open` (Linux):

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **CLI do Goose:**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  ou execute `goose configure`, escolha **Add Extension -> Command-line Extension** e digite o mesmo comando. Informe as credenciais dos providers como variáveis de ambiente da extensão quando solicitado.

<br />
:::note

O Goose desativou o diretório de extensões dentro do aplicativo em favor do [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql), onde o StackQL está listado como `io.github.stackql/stackql-mcp`. Quando o Goose disponibilizar a importação do registro, o StackQL poderá ser encontrado lá sem nenhuma ação adicional.

:::

</Box>

</TabItem>
</Tabs>

## Instalar o servidor MCP {#installing-the-mcp-server}

O servidor MCP do StackQL permite que agentes de IA consultem e provisionem recursos na nuvem com o StackQL. Ele é distribuído por vários canais - todos executam o mesmo servidor, então escolha o canal que corresponde ao seu cliente e aos seus requisitos de confiança. As opções abaixo estão listadas aproximadamente em ordem de confiança e facilidade.

### Marketplaces e diretórios {#marketplaces-and-directories}

O servidor é publicado no Official MCP Registry como `io.github.stackql/stackql-mcp`; os registros de pacotes abaixo contêm os artefatos distribuíveis, e os diretórios derivados obtêm a listagem a partir dali.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Registros', value: 'registries', },
    { label: 'Diretórios', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Registro | Tipo | Publicado via | Listagem | Observações |
|---|---|---|---|---|
| Official MCP Registry | Registro canônico de metadados | CLI `mcp-publisher` + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | anuncia todos os 8 pacotes (`mcpb x5`, `oci`, `npm`, `pypi`) |
| npm | Registro de pacotes | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | inicializador `npx` |
| PyPI | Registro de pacotes | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | inicializador `uvx` / `pip` |
| Docker Hub | Registro de contêineres | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | multiarquitetura `amd64` + `arm64` |
| GitHub Actions Marketplace | Marketplace de CI (action `setup-stackql-mcp`) | Repositório público + versão + listagem no marketplace | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Publicador verificado; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Listagens em diretórios e agregadores, aproximadamente em ordem de importância para a descoberta:

| Diretório | Tipo | Listagem |
|---|---|---|
| __Anthropic Connector Directory__ | Diretório dentro do aplicativo Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | Diretório dentro do aplicativo de desktop do ChatGPT (somente desktop) | pesquise **StackQL** no aplicativo de desktop do ChatGPT; consulte [ChatGPT e Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | Diretório de um cliente IDE | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | Maior agregador | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Descoberta, apoiador do registro | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Marketplace pesquisável | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Agregador | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Site no estilo awesome-list | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Bundle `.mcpb` pré-compilado {#prebuilt-mcpb-bundle}

**Quando usar:** Claude Desktop sem acesso ao Connector Directory, ou quando você quer fixar uma versão específica ou verificar as somas de verificação por conta própria antes de instalar.  Para a maioria dos usuários do Claude Desktop, o [Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) é o método de instalação recomendado.

Cada versão do StackQL traz anexado um MCP Bundle pré-compilado para cada plataforma:

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

em que `<platform>` é um destes valores: `darwin-universal`, `windows-x64` (AMD64), `windows-arm64`, `linux-x64` ou `linux-arm64`. Os botões de download de cada plataforma estão na aba **Claude Desktop**, em [Usar com clientes MCP](#using-with-mcp-clients). Cada bundle tem uma soma de verificação `.sha256` correspondente na [página da versão](https://github.com/stackql/stackql/releases/latest). Verifique-a e depois instale via **Settings -> Extensions** no Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Consulte [Usar o StackQL com o Claude Desktop](/getting-started/claude-desktop#install-from-a-downloaded-bundle) para ver o passo a passo completo.

### `claude_desktop_config.json` manual {#manual-claude_desktop_configjson}

**Quando usar:** você já tem o binário `stackql` no PATH e usa o Claude Desktop ou qualquer outro cliente MCP stdio.

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

Os três argumentos são indispensáveis:

- `--mcp.server.type=stdio` seleciona o transporte stdio usado pelos clientes incorporados em editores.
- `--approot` aponta o cache de providers para um diretório com permissão de gravação. Os clientes MCP podem iniciar o servidor com o diretório de trabalho definido como `/`, que não tem permissão de gravação.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` desabilita o log de auditoria, que, caso contrário, usa como diretório padrão o diretório de trabalho (possivelmente sem permissão de gravação).

Adicione as credenciais dos providers com um bloco `"env"` e ajuste o contrato de segurança com `"mode"` - consulte [Modos do servidor](/command-line-usage/mcp#server-modes).

### `npx` (sem instalação) {#npx-no-install}

**Quando usar:** qualquer ambiente Node, quando você não quer uma instalação global do StackQL.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

O inicializador [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) baixa o binário `stackql` assinado na primeira execução, verifica o SHA-256 dele e o armazena em cache para as execuções seguintes.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**Quando usar:** um ambiente Python - o mesmo inicializador, empacotado para Python, que compartilha o cache de binários preenchido pelo inicializador npx.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

Ou instale-o no ambiente atual com `pip install stackql-mcp-server` (Python 3.9+). O pacote é [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) no PyPI.

### Docker {#docker}

**Quando usar:** um runtime em contêiner ou isolado de outra forma. A imagem é multiarquitetura (amd64 e arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

Como configuração de cliente:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

A imagem é [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) no Docker Hub.

### CI e workflows agênticos (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**Quando usar:** execute o servidor dentro de um job do GitHub Actions para que um agente possa consultar a sua nuvem - e, se você permitir, agir sobre ela - como parte da CI.

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) instala o binário e grava uma configuração de MCP (com o modo `read_only` por padrão). Passe essa configuração para [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) por meio de `claude_args`:

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

Consulte o [README da action](https://github.com/stackql/setup-stackql-mcp) para ver mais receitas agênticas (auditorias da nuvem, estimativas de custo etc.).

### MCP incorporado {#embedded-mcp}

**Quando usar:** você está criando uma aplicação agêntica e quer fazer o vendoring do servidor MCP no seu próprio binário - sem `npx`, sem instalação separada, sem dependência de runtime.

Bibliotecas nativas iniciam o binário `stackql` assinado via stdio por trás do cliente oficial do SDK MCP de cada linguagem, para que a sua aplicação tenha a mesma interface SQL governada para todos os providers do registro. Cada biblioteca usa `read_only` por padrão - a elevação para um modo com gravação é sempre explícita - e pode baixar e verificar o binário na primeira execução ou fazer o vendoring dele no seu build para gerar um único executável autocontido. A API completa de cada linguagem está na [referência de MCP incorporado](/mcp/embedded).

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

O crate `stackql-mcp` inicia o binário por trás de um cliente `rmcp`. A funcionalidade padrão `sidecar` baixa e verifica o binário na primeira execução; a funcionalidade `vendored` o incorpora com `include_bytes!` para gerar um único binário autocontido. MSRV Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="Ver no GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Ver no crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Referência completa: [MCP incorporado: Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

A biblioteca `stackql-mcp-go` inicia o binário por trás do cliente oficial do SDK MCP para Go. Faça o vendoring do binário com `go:embed` para gerar um único binário autocontido, ou deixe que a biblioteca o baixe e verifique na primeira execução. Inicia no modo `read_only` por padrão.

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
    <BinaryDownloadLink iconSize={20} text="Ver no GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Ver no pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Referência completa: [MCP incorporado: Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

A biblioteca `io.stackql:stackql-mcp` inicia o binário por trás do cliente oficial do SDK MCP para Kotlin, e um plugin complementar do Gradle integra a mesma inicialização a um build. Requer JDK 17 e Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="Ver no GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Referência completa: [MCP incorporado: Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

O pacote `StackQL.Mcp` inicia o binário por trás do cliente oficial do SDK MCP para C#. Usa sidecar por padrão, ou você pode fazer o vendoring do bundle como recurso de build para gerar um executável autocontido. Requer .NET 8 ou posterior.

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
    <BinaryDownloadLink iconSize={20} text="Ver no GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Referência completa: [MCP incorporado: .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

A biblioteca `stackql_mcp` tem como alvo o runtime Erlang/BEAM e expõe o servidor como um processo filho OTP via `child_spec()`, para que ele possa ficar dentro da sua própria árvore de supervisão. Inicia no modo `read_only` por padrão.

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
    <BinaryDownloadLink iconSize={20} text="Ver no GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Referência completa: [MCP incorporado: Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

O pacote `stackql-mcp-swift` inicia o binário por trás do cliente oficial do SDK MCP para Swift. O binário `darwin-universal` é assinado com Developer ID e notarizado pela Apple, então você pode empacotá-lo dentro de um `.app` assinado e manter válida a notarização do aplicativo. Requer macOS 13+ e Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="Ver no GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Referência completa: [MCP incorporado: Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### Modelo de confiança {#trust-model}

As mesmas propriedades de segurança valem para todos os canais:

- O binário `stackql` incorporado é assinado com Authenticode (Windows) e notarizado pela Apple (macOS).
- Cada bundle `.mcpb` é distribuído com uma soma de verificação SHA-256 publicada na página da versão.
- Os inicializadores `npx` e `uvx` verificam o SHA-256 do binário baixado antes do primeiro uso.
- A entrada no [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) atesta os hashes de cada plataforma.

## Usar o GitHub Actions {#using-github-actions}

As [GitHub Actions do StackQL](https://github.com/stackql/stackql-actions-demo) estão disponíveis para uso nos seus workflows do GitHub Actions. As seguintes actions estão disponíveis:

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

Implante ou teste stacks de infraestrutura com o StackQL diretamente nos seus workflows do GitHub. Essa action possibilita workflows de infraestrutura como código (IaC) com uma sintaxe semelhante à SQL, o que permite definir, implantar e gerenciar recursos na nuvem em vários providers em um único workflow.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver no GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Ver no GitHub"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemplo de uso__

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

Configure o StackQL no seu workflow do GitHub Actions.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver no GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Ver no GitHub"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemplo de uso__

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

Instale o servidor MCP do StackQL no seu workflow do GitHub Actions.  A action instala o binário assinado e grava uma configuração `mcpServers` (com o modo `read_only` por padrão) que uma etapa agêntica, como `anthropics/claude-code-action`, consome via `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver no GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver no GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemplo de uso__

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

Execute comandos do StackQL no seu workflow do GitHub Actions.  As consultas podem ser fornecidas inline ou a partir de um arquivo no repositório.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver no GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Ver no GitHub"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemplo de uso__

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

Execute testes unitários no seu workflow do GitHub Actions (para verificações de garantia, governança ou segurança na nuvem).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Ver no GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Ver no GitHub"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemplo de uso__

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


## Outras bibliotecas {#other-libraries}

O StackQL oferece vários métodos de integração que permitem usá-lo em diferentes linguagens de programação e ambientes. Essas bibliotecas estendem a funcionalidade do StackQL e facilitam a incorporação da consulta e do gerenciamento de recursos na nuvem nas suas aplicações e workflows existentes.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### Pacote Python `pystackql` {#pystackql-python-package}

Wrapper Python para usar o StackQL nos seus programas Python. O pacote `pystackql` está disponível no [__PyPI__](https://pypi.org/project/pystackql/), e a documentação do pacote `pystackql` está disponível no [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). Para instalar o pacote `pystackql`, execute o comando a seguir:

```bash
pip install pystackql
```

O exemplo a seguir mostra o pacote `pystackql` usado junto com o `pandas` para executar consultas do StackQL e retornar os resultados em um `pandas.DataFrame`:

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
