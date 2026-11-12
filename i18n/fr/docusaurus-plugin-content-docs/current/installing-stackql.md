---
title: Installation
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
  - mcp
  - mcp-server
  - claude-desktop
  - chatgpt
  - goose
description: "Installez StackQL sur macOS, Linux, Windows, Docker ou un shell cloud et utilisez son serveur MCP avec Claude Desktop, ChatGPT, Goose et d'autres clients MCP, ou via npx, uvx, pip, Docker et GitHub Actions."
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

Les instructions d'installation de StackQL sur différentes plateformes sont fournies ici.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Shells cloud', value: 'shells', },
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
  
  StackQL est disponible sur macOS via Homebrew et le programme d'installation `pkg` ; les architectures ARM (M1/Apple Silicon) et AMD sont toutes deux prises en charge par un seul programme d'installation multi-architecture.

  <Tabs
    defaultValue="homebrew"
    values={[
      { label: 'Homebrew', value: 'homebrew', },
      { label: 'curl (binaire signé et notarisé)', value: 'curl', },
      { label: 'Téléchargement du paquet', value: 'package', },
    ]}
  >
  <TabItem value="homebrew">

  Pour installer StackQL via Homebrew, exécutez la commande suivante dans votre terminal :

  ```bash
  brew install stackql
  ```

  </TabItem>
  <TabItem value="curl">

  Pour installer un binaire exécutable `stackql` signé et notarisé dans le répertoire courant, exécutez la commande suivante dans votre terminal :

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  StackQL est disponible sous la forme d'un programme d'installation `pkg` interactif, signé et notarisé, pour macOS.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="Télécharger le PKG macOS"
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

  StackQL est disponible pour toutes les architectures Linux.

  <Tabs
    defaultValue="curl"
    values={[
      { label: 'curl', value: 'curl', },
      { label: 'Téléchargement du paquet', value: 'package', },
    ]}
  >
  <TabItem value="curl">

  Pour installer un binaire exécutable `stackql` dans le répertoire courant pour votre architecture Linux (amd64 ou arm64), exécutez la commande suivante dans votre terminal :

  ```bash
  curl -fsSL https://get-stackql.io/install | sh
  ```

  </TabItem>
  <TabItem value="package">

  Vous pouvez aussi télécharger les binaires ici :

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Télécharger le ZIP Linux (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Télécharger le ZIP Linux (arm64)"
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

  StackQL est disponible sur Windows via Chocolatey, un script d'installation PowerShell et le programme d'installation MSI. Des builds natifs AMD64 (x64) et ARM64 sont disponibles sous forme de programmes d'installation MSI et d'archives ZIP contenant `stackql.exe`. Le build AMD64 s'exécute aussi sur ARM64 par émulation. Tous les exécutables sont signés avec un certificat de signature de code Authenticode.

  <Tabs
    defaultValue="chocolatey"
    values={[
      { label: 'Chocolatey', value: 'chocolatey', },
      { label: 'Invoke-RestMethod', value: 'irm', },
      { label: 'Téléchargement MSI/ZIP', value: 'package', },
    ]}
  >
  <TabItem value="chocolatey">

  Pour installer StackQL via Chocolatey, exécutez la commande suivante dans votre terminal PowerShell ou `cmd` :

  ```powershell
  choco install stackql
  ```

  </TabItem>
  <TabItem value="irm">

  Pour installer un exécutable Authenticode dans le répertoire courant, exécutez la commande suivante dans un terminal PowerShell : 

  ```powershell
  Invoke-RestMethod https://get-stackql.io/install | Invoke-Expression
  # or
  irm https://get-stackql.io/install | iex
  ```

  </TabItem>
  <TabItem value="package">

  Téléchargez ci-dessous le programme d'installation ou l'exécutable `stackql` signé pour Windows. Un clic sur un bouton de téléchargement sélectionne AMD64 par défaut ; utilisez sa liste déroulante pour choisir AMD64 ou ARM64 natif.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Télécharger le MSI Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
        options={[
          { text: 'Télécharger le MSI Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi' },
          { text: 'Télécharger le MSI Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Télécharger le ZIP Windows"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
        options={[
          { text: 'Télécharger le ZIP Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip' },
          { text: 'Télécharger le ZIP Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip' },
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

  Les builds de StackQL sont publiés sur [DockerHub](https://hub.docker.com/u/stackql).  

  __Docker Hub__  

  Pour télécharger l'image de conteneur StackQL, exécutez la commande suivante :

  ```bash
  docker pull stackql/stackql
  ```

  À partir de `v1.0`, l'image est basée sur Ubuntu 26.04 LTS, et une variante avec Python 3.13 préinstallé est publiée avec le suffixe de tag `-python3.13` (alias `-python3`), par exemple :

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

`stackql-deploy` est un framework IaC sans état construit sur StackQL. Pour la documentation complète, consultez la [__documentation de StackQL Deploy__](https://stackql-deploy.io/).

__Linux / macOS__

Télécharge et extrait le dernier binaire `stackql-deploy` en une seule étape. Le programme d'installation détecte votre système d'exploitation et votre architecture (y compris Apple Silicon et Intel via un binaire macOS universel) et récupère automatiquement le bon fichier de version.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Télécharge et extrait le dernier binaire `stackql-deploy` dans le répertoire courant.
Le programme d'installation détecte votre plateforme et récupère automatiquement le bon fichier de version.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

Si la chaîne d'outils Rust est installée, cette commande compile et installe le binaire directement depuis [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## Shells cloud

  StackQL peut être utilisé directement depuis les shells cloud et les terminaux web intégrés des principales plateformes cloud et de données. L'expérience est fluide, car ces environnements sont préautorisés avec l'identité sous laquelle vous êtes connecté, ce qui évite toute configuration d'authentification distincte.

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

  AWS CloudShell fournit un shell dans le navigateur avec l'AWS CLI préinstallée et authentifiée. Exécuter StackQL dans AWS CloudShell vous permet d'interroger et de gérer des ressources AWS sans étape d'authentification supplémentaire. Pour des instructions détaillées, consultez notre [tutoriel AWS CloudShell](/quick-starts/aws/aws-cloud-shell).

  Commencez par télécharger le paquet StackQL :

  ```bash
  curl -fsSL https://get-stackql.io/install/aws | sh
  ```
  Exécutez ensuite le script StackQL pour AWS CloudShell :
  
  ```bash
  ./stackql-aws-cloud-shell.sh
  ```  
  
  Ce script démarre un shell de commandes StackQL avec vos identifiants AWS CloudShell, ce qui vous permet d'interroger immédiatement des ressources AWS.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell fournit un environnement shell accessible depuis le navigateur, où l'Azure CLI est préauthentifiée avec votre compte Azure. Utiliser StackQL dans Azure Cloud Shell permet d'interroger vos ressources Azure de manière fluide, sans configuration supplémentaire. Pour tous les détails, consultez notre [guide Azure Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell).  

  Commencez par télécharger le paquet StackQL :

  ```bash
  curl -fsSL https://get-stackql.io/install/azure | sh
  ```

  Exécutez ensuite le script StackQL pour Azure Cloud Shell :

  ```bash
  ./stackql-azure-cloud-shell.sh
  ```

  Ce script configure automatiquement une session StackQL pour utiliser vos identifiants Azure Cloud Shell, ce qui vous permet d'interroger immédiatement vos ressources Azure.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell offre un environnement de développement et d'exploitation avec la Google Cloud CLI déjà authentifiée. Exécuter StackQL dans Google Cloud Shell vous permet d'interroger des ressources GCP avec votre authentification existante. Pour en savoir plus, consultez notre [guide Google Cloud Shell](/blog/tutorials/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell).  

  Commencez par télécharger le paquet StackQL :
  
  ```bash
  curl -fsSL https://get-stackql.io/install/google | sh
  ```

  Exécutez ensuite le script StackQL pour Google Cloud Shell :

  ```bash
  ./stackql-google-cloud-shell.sh
  ```
  
  Le script configure StackQL pour utiliser vos identifiants Google Cloud Shell, ce qui vous permet d'interroger immédiatement vos ressources GCP avec la syntaxe SQL.

  </TabItem>
  <TabItem value="databricks">

  ### Terminal web Databricks

  Les espaces de travail Databricks incluent un terminal web qui s'exécute sous l'identité de l'utilisateur connecté. Y exécuter StackQL vous permet d'interroger votre espace de travail avec votre identité Databricks, sans configuration d'authentification distincte. Pour des exemples de requêtes et l'authentification au niveau du compte, consultez notre [guide du terminal web Databricks](/blog/tutorials/stackql-in-databricks-web-terminal).

  Commencez par télécharger le paquet StackQL :

  ```bash
  curl -fsSL https://get-stackql.io/install/databricks | sh
  ```

  Exécutez ensuite le script shell StackQL pour Databricks :

  ```bash
  ./stackql-databricks-shell.sh
  ```

  Cette commande démarre une session StackQL avec l'identité de votre espace de travail Databricks, ce qui vous permet d'interroger immédiatement des ressources propres à l'espace de travail, comme `databricks_workspace.iam.vw_user_entitlements`. Pour les requêtes au niveau du compte (provisionnement, facturation, IAM du compte), définissez les variables du principal de service OAuth2 Databricks comme décrit dans le guide.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Utiliser avec des clients MCP {#using-with-mcp-clients}

Le serveur MCP StackQL s'exécute localement via stdio : les identifiants des providers restent donc sur votre machine, quel que soit le client utilisé. Choisissez votre client ci-dessous : chaque onglet indique d'abord l'installation depuis l'annuaire ou en un clic, puis la configuration manuelle comme solution de repli. Pour tous les canaux de distribution, y compris `npx`, Python, Docker et la CI, consultez [Installer le serveur MCP](#installing-the-mcp-server).

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

  La méthode recommandée pour installer le serveur MCP StackQL dans Claude Desktop passe par l'[Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) (**Settings -> Connectors -> Add -> Browse Connectors**, recherchez **StackQL**) - Claude Desktop sélectionne automatiquement le binaire signé adapté à votre plateforme.  Sinon, des MCP Bundles précompilés (`.mcpb`) sont joints à chaque version de StackQL pour une installation depuis un fichier téléchargé - dans les deux cas, aucune installation séparée de StackQL n'est nécessaire.  Des bundles sont disponibles pour macOS, Windows et Linux.

  Les téléchargements MCPB pour Linux et Windows utilisent **amd64** (x64) par défaut. Utilisez les listes déroulantes pour sélectionner **amd64** ou **arm64**.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Télécharger le MCPB macOS"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Télécharger le MCPB Linux"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Télécharger le MCPB Linux (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-x64.mcpb' },
          { text: 'Télécharger le MCPB Linux (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <DropDownBinaryDownloadLink
        iconSize={20} 
        text="Télécharger le MCPB Windows"
        to="https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb"
        options={[
          { text: 'Télécharger le MCPB Windows (amd64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb' },
          { text: 'Télécharger le MCPB Windows (arm64)', to: 'https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb' },
        ]}
      />      
    </div>
  </Box>

<br />
:::info

Le serveur MCP StackQL est également référencé dans l'[__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) sous le nom `io.github.stackql/stackql-mcp`.

:::

:::tip

Le bundle est l'une des différentes façons d'exécuter le serveur MCP StackQL.  Consultez [__Utiliser StackQL avec Claude Desktop__](/getting-started/claude-desktop) pour le guide pas à pas complet de Claude Desktop, ou [__Installer le serveur MCP__](#installing-the-mcp-server) pour les options npx, Docker, Python, CI et manuelle.

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

  ### ChatGPT et Codex

  StackQL est distribué sous la forme d'un plugin local pour l'application de bureau ChatGPT et Codex CLI. Le plugin exécute le serveur MCP StackQL via stdio sur votre machine : les identifiants des providers ne la quittent donc jamais. Node.js 24.22.1 ou une version ultérieure est requis (22.23.4 ou 26.12.1 sur ces lignes de version) ; le plugin n'a besoin que des modules intégrés de Node.js, télécharge le binaire `stackql` signé à la première utilisation et vérifie son SHA-256.

  **Application de bureau ChatGPT (annuaire de plugins) :**  
  
  ouvrez l'annuaire de plugins dans l'application de bureau ChatGPT, recherchez **StackQL** et cliquez sur **Install**, puis lancez la configuration du plugin ; le skill `stackql-setup` fourni vous guide pour les identifiants et une première requête. La fiche porte un badge **Desktop only** : le plugin s'installe uniquement depuis l'application de bureau, pas depuis ChatGPT web ou mobile, et ses outils sont disponibles en mode Work, dans les fils qui s'exécutent sur votre ordinateur. Si la fiche n'est pas encore visible dans votre annuaire, installez plutôt le plugin depuis la marketplace du dépôt.

  **Application de bureau ChatGPT (marketplace du dépôt) :**  
  
  ouvrez **Settings -> Plugins -> Add -> Add from GitHub**, saisissez `stackql/stackql`, puis installez **StackQL** depuis la section **Personal** de l'annuaire de plugins et redémarrez l'application.

  **Codex CLI :**

  ```shell
  codex plugin marketplace add stackql/stackql --sparse .agents/plugins --sparse packaging/openai-plugin
  codex plugin add stackql@stackql
  ```

  Redémarrez l'application de bureau ChatGPT ou Codex, puis essayez : *Utilisez StackQL pour installer le provider GitHub si nécessaire, puis listez les cinq premiers services GitHub.* Les identifiants des providers se placent dans `~/.stackql/.env`, sous les noms de variables d'environnement standard de chaque provider (par exemple `AWS_ACCESS_KEY_ID` et `AWS_SECRET_ACCESS_KEY`) ; après avoir modifié le fichier, demandez à l'agent d'appeler `reload_credentials`.

  **Sans le plugin :**   
  
  sous **Settings -> Plugins -> MCPs -> Connect to a custom MCP**, choisissez **STDIO**, définissez la commande sur `npx` et ajoutez les arguments `-y` et `@stackql/mcp-server`. Fournissez les identifiants des providers sous forme de variables d'environnement dans ce formulaire.

<br />
:::note

ChatGPT web et mobile ne peuvent pas exécuter un serveur stdio local : le plugin est donc réservé à l'application de bureau et à Codex. Un bug ouvert de l'application de bureau ChatGPT ([openai/codex#38162](https://github.com/openai/codex/issues/38162)) peut masquer les outils MCP locaux dans les sessions **Chat** ; les fils en mode Work, les fils Codex et Codex CLI ne sont pas concernés.

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

  Goose exécute directement les extensions stdio : le lanceur `npx` suffit donc (Node.js 18 ou version ultérieure).

  **Goose Desktop :** cliquez sur le bouton ci-dessous. Goose s'ouvre, affiche la commande qu'il s'apprête à exécuter et vous demande de confirmer, puis ajoute StackQL comme extension.

  <Box sx={{ mt: 2, mb: 1 }}>
    <a className="button button--outline button--primary" href={gooseDeepLink}>Installer dans Goose Desktop</a>
  </Box>

  Le bouton est un lien profond `goose://` ; vous pouvez aussi le coller dans la barre d'adresse de votre navigateur, ou l'ouvrir depuis un terminal avec `open` (macOS), `start` (Windows) ou `xdg-open` (Linux) :

  ```
  goose://extension?cmd=npx&arg=-y&arg=%40stackql%2Fmcp-server&id=stackql&name=StackQL&description=Query%20and%20provision%20cloud%20and%20SaaS%20resources%20with%20SQL
  ```

  **Goose CLI :**

  ```shell
  goose session --with-extension "npx -y @stackql/mcp-server"
  ```

  ou exécutez `goose configure`, choisissez **Add Extension -> Command-line Extension** et saisissez la même commande. Fournissez les identifiants des providers comme variables d'environnement de l'extension lorsque cela vous est demandé.

<br />
:::note

Goose a retiré son annuaire d'extensions intégré à l'application au profit de l'[Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql), où StackQL est référencé sous le nom `io.github.stackql/stackql-mcp`. Dès que Goose proposera son import depuis le registre, StackQL y sera visible sans aucune autre action.

:::

</Box>

</TabItem>
</Tabs>

## Installer le serveur MCP {#installing-the-mcp-server}

Le serveur MCP StackQL permet aux agents IA d'interroger et de provisionner des ressources cloud avec StackQL. Il est distribué par plusieurs canaux - tous exécutent le même serveur, choisissez donc le canal qui correspond à votre client et à vos exigences de confiance. Les options ci-dessous sont classées approximativement par ordre de confiance et de facilité.

### Marketplaces et annuaires {#marketplaces-and-directories}

Le serveur est publié dans l'Official MCP Registry sous le nom `io.github.stackql/stackql-mcp` ; les registres de paquets ci-dessous hébergent les artefacts distribuables, et les annuaires en aval reprennent la fiche à partir de là.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Registres', value: 'registries', },
    { label: 'Annuaires', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Registre | Type | Publié via | Fiche | Remarques |
|---|---|---|---|---|
| Official MCP Registry | Registre de métadonnées canonique | CLI `mcp-publisher` + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | annonce les 8 paquets (`mcpb x5`, `oci`, `npm`, `pypi`) |
| npm | Registre de paquets | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | lanceur `npx` |
| PyPI | Registre de paquets | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | lanceur `uvx` / `pip` |
| Docker Hub | Registre de conteneurs | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | multi-architecture `amd64` + `arm64` |
| GitHub Actions Marketplace | Marketplace CI (action `setup-stackql-mcp`) | Dépôt public + version + fiche marketplace | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Éditeur vérifié ; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Fiches dans les annuaires et les agrégateurs, classées approximativement par ordre d'importance pour la découverte :

| Annuaire | Type | Fiche |
|---|---|---|
| __Anthropic Connector Directory__ | Annuaire intégré à l'application Claude Desktop | [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) |
| __OpenAI Plugin Directory__ | Annuaire intégré à l'application de bureau ChatGPT (bureau uniquement) | recherchez **StackQL** dans l'application de bureau ChatGPT ; consultez [ChatGPT et Codex](#using-with-mcp-clients) |
| __Cursor Directory__ | Annuaire d'un client IDE | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | Plus grand agrégateur | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Découverte, soutien du registre | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Marketplace avec recherche | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Agrégateur | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Site de type awesome-list | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Bundle `.mcpb` précompilé {#prebuilt-mcpb-bundle}

**Quand l'utiliser :** Claude Desktop sans accès au Connector Directory, ou lorsque vous voulez épingler une version précise ou vérifier vous-même les sommes de contrôle avant l'installation.  Pour la plupart des utilisateurs de Claude Desktop, l'[Anthropic Connector Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) est la méthode d'installation recommandée.

Un MCP Bundle précompilé est joint à chaque version de StackQL pour chaque plateforme :

```
https://releases.stackql.io/stackql/latest/stackql-mcp-<platform>.mcpb
```

où `<platform>` vaut `darwin-universal`, `windows-x64` (AMD64), `windows-arm64`, `linux-x64` ou `linux-arm64`. Les boutons de téléchargement de chaque plateforme se trouvent dans l'onglet **Claude Desktop** sous [Utiliser avec des clients MCP](#using-with-mcp-clients). Chaque bundle a une somme de contrôle `.sha256` correspondante sur la [page de la version](https://github.com/stackql/stackql/releases/latest). Vérifiez-la, puis installez le bundle via **Settings -> Extensions** dans Claude Desktop :

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Consultez [Utiliser StackQL avec Claude Desktop](/getting-started/claude-desktop#install-from-a-downloaded-bundle) pour le guide pas à pas complet.

### Configuration manuelle de `claude_desktop_config.json` {#manual-claude_desktop_configjson}

**Quand l'utiliser :** vous avez déjà le binaire `stackql` dans votre PATH et utilisez Claude Desktop ou tout autre client MCP stdio.

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

Les trois arguments sont indispensables :

- `--mcp.server.type=stdio` sélectionne le transport stdio qu'utilisent les clients intégrés aux éditeurs.
- `--approot` fait pointer le cache des providers vers un répertoire accessible en écriture. Les clients MCP peuvent lancer le serveur avec `/` comme répertoire de travail, qui n'est pas accessible en écriture.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` désactive le journal d'audit, dont le répertoire par défaut est sinon le répertoire de travail (éventuellement non accessible en écriture).

Ajoutez les identifiants des providers avec un bloc `"env"` et ajustez le contrat de sécurité avec `"mode"` - consultez [Modes du serveur](/command-line-usage/mcp#server-modes).

### `npx` (sans installation) {#npx-no-install}

**Quand l'utiliser :** tout environnement Node, lorsque vous ne voulez pas d'installation globale de StackQL.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

Le lanceur [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) télécharge le binaire `stackql` signé au premier lancement, vérifie son SHA-256 et le met en cache pour les lancements suivants.

### `uvx` / `pip` (Python) {#uvx--pip-python}

**Quand l'utiliser :** un environnement Python - le même lanceur, empaqueté pour Python, qui partage le cache de binaires alimenté par le lanceur npx.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

Ou installez-le dans l'environnement courant avec `pip install stackql-mcp-server` (Python 3.9+). Le paquet est [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) sur PyPI.

### Docker {#docker}

**Quand l'utiliser :** un runtime conteneurisé ou isolé d'une autre manière. L'image est multi-architecture (amd64 et arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

Sous forme de configuration client :

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

L'image est [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) sur Docker Hub.

### CI et workflows agentiques (GitHub Actions) {#ci-and-agentic-workflows-github-actions}

**Quand l'utiliser :** exécuter le serveur dans un job GitHub Actions pour qu'un agent puisse interroger votre cloud - et, si vous l'autorisez, agir dessus - dans le cadre de la CI.

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) installe le binaire et écrit une configuration MCP (en mode `read_only` par défaut). Transmettez cette configuration à [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) via `claude_args` :

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

Consultez le [README de l'action](https://github.com/stackql/setup-stackql-mcp) pour d'autres recettes agentiques (audits cloud, estimations de coûts, etc.).

### MCP embarqué {#embedded-mcp}

**Quand l'utiliser :** vous développez une application agentique et voulez embarquer le serveur MCP dans votre propre binaire - pas de `npx`, pas d'installation séparée, pas de dépendance à l'exécution.

Des bibliothèques natives lancent le binaire `stackql` signé via stdio derrière le client du SDK MCP officiel de chaque langage : votre application obtient ainsi la même interface SQL gouvernée vers chaque provider du registre. Chaque bibliothèque utilise `read_only` par défaut - le passage à un mode autorisant l'écriture est toujours explicite - et peut soit télécharger et vérifier le binaire au premier lancement, soit l'embarquer dans votre build pour obtenir un exécutable unique et autonome. L'API complète de chaque langage figure dans la [référence du MCP embarqué](/mcp/embedded).

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

La crate `stackql-mcp` lance le binaire derrière un client `rmcp`. La fonctionnalité `sidecar`, activée par défaut, télécharge et vérifie le binaire au premier lancement ; la fonctionnalité `vendored` l'embarque avec `include_bytes!` pour obtenir un binaire unique et autonome. MSRV Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="Voir sur GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Voir sur crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Référence complète : [MCP embarqué : Rust](/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

La bibliothèque `stackql-mcp-go` lance le binaire derrière le client du SDK MCP officiel pour Go. Embarquez le binaire avec `go:embed` pour obtenir un binaire unique et autonome, ou laissez la bibliothèque le télécharger et le vérifier au premier lancement. Démarre en mode `read_only` par défaut.

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
    <BinaryDownloadLink iconSize={20} text="Voir sur GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="Voir sur pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Référence complète : [MCP embarqué : Go](/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

La bibliothèque `io.stackql:stackql-mcp` lance le binaire derrière le client du SDK MCP officiel pour Kotlin, et un plugin Gradle associé intègre le même lancement à un build. Nécessite JDK 17 et Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="Voir sur GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Référence complète : [MCP embarqué : Kotlin / JVM](/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

Le paquet `StackQL.Mcp` lance le binaire derrière le client du SDK MCP officiel pour C#. Sidecar par défaut, ou embarquez le bundle comme ressource de build pour obtenir un exécutable autonome. Nécessite .NET 8 ou version ultérieure.

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
    <BinaryDownloadLink iconSize={20} text="Voir sur GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Référence complète : [MCP embarqué : .NET / C#](/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

La bibliothèque `stackql_mcp` cible le runtime Erlang/BEAM et expose le serveur comme processus enfant OTP via `child_spec()` : il peut ainsi s'insérer dans votre propre arbre de supervision. Démarre en mode `read_only` par défaut.

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
    <BinaryDownloadLink iconSize={20} text="Voir sur GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Référence complète : [MCP embarqué : Gleam](/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

Le paquet `stackql-mcp-swift` lance le binaire derrière le client du SDK MCP officiel pour Swift. Le binaire `darwin-universal` est signé avec un Developer ID et notarisé par Apple : vous pouvez donc l'inclure dans une `.app` signée et conserver la validité de la notarisation de l'application. Nécessite macOS 13+ et Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="Voir sur GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Référence complète : [MCP embarqué : Swift](/mcp/embedded/swift).

</TabItem>
</Tabs>

### Modèle de confiance {#trust-model}

Les mêmes propriétés de sécurité s'appliquent à tous les canaux :

- Le binaire `stackql` embarqué est signé Authenticode (Windows) et notarisé par Apple (macOS).
- Chaque bundle `.mcpb` est accompagné d'une somme de contrôle SHA-256 publiée sur la page de la version.
- Les lanceurs `npx` et `uvx` vérifient le SHA-256 du binaire téléchargé avant la première utilisation.
- L'entrée du [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) atteste les empreintes de chaque plateforme.

## Utiliser GitHub Actions {#using-github-actions}

Des [actions GitHub StackQL](https://github.com/stackql/stackql-actions-demo) sont disponibles pour vos workflows GitHub Actions. Les actions suivantes sont disponibles :

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

Déployez ou testez des stacks d'infrastructure avec StackQL directement depuis vos workflows GitHub. Cette action permet des workflows d'infrastructure as code (IaC) avec une syntaxe proche de SQL, pour définir, déployer et gérer des ressources cloud de plusieurs providers dans un seul workflow.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Voir sur GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Voir sur GitHub"
        to="https://github.com/stackql/stackql-deploy-action"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemple d'utilisation__

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

Configurez StackQL dans votre workflow GitHub Actions.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Voir sur GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Voir sur GitHub"
        to="https://github.com/stackql/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemple d'utilisation__

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

Installez le serveur MCP StackQL dans votre workflow GitHub Actions.  L'action installe le binaire signé et écrit une configuration `mcpServers` (en mode `read_only` par défaut) qu'une étape agentique telle que `anthropics/claude-code-action` consomme via `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Voir sur GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Voir sur GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemple d'utilisation__

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

Exécutez des commandes StackQL dans votre workflow GitHub Actions.  Les requêtes peuvent être fournies en ligne ou depuis un fichier du dépôt.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Voir sur GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Voir sur GitHub"
        to="https://github.com/stackql/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemple d'utilisation__

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

Exécutez des tests unitaires dans votre workflow GitHub Actions (pour des contrôles d'assurance, de gouvernance ou de sécurité cloud).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Voir sur GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink
        iconSize={20}
        text="Voir sur GitHub"
        to="https://github.com/stackql/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Exemple d'utilisation__

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


## Autres bibliothèques {#other-libraries}

StackQL propose plusieurs méthodes d'intégration qui permettent de l'utiliser dans différents langages de programmation et environnements. Ces bibliothèques étendent les fonctionnalités de StackQL et facilitent l'intégration de l'interrogation et de la gestion des ressources cloud dans vos applications et workflows existants.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### Paquet Python `pystackql` {#pystackql-python-package}

Wrapper Python pour utiliser StackQL dans vos programmes Python. Le paquet `pystackql` est disponible sur [__PyPi__](https://pypi.org/project/pystackql/) ; la documentation du paquet `pystackql` est disponible sur [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). Pour installer le paquet `pystackql`, exécutez la commande suivante :

```bash
pip install pystackql
```

L'exemple suivant montre le paquet `pystackql` utilisé avec `pandas` pour exécuter des requêtes StackQL et renvoyer les résultats dans un `pandas.DataFrame` :

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
