---
title: Installation
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
slug: /installing-stackql
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import Link from '@docusaurus/Link';
import { Box, Typography, Button } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';
import ArticleIcon from '@mui/icons-material/Article';
import {
  BinaryDownloadLink,
} from '../src/components';
import buttonStyles from '../src/components/BinaryDownloadLink/binarydownloadlink.module.css';
import clsx from 'clsx';

Instructions for installing StackQL on various different platforms are provided here.

<Tabs
  defaultValue="macos"
  values={[
    { label: 'macOS', value: 'macos', },
    { label: 'Linux', value: 'linux', },
    { label: 'Windows', value: 'windows', },
    { label: 'Docker', value: 'docker', },
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'Cloud Shells', value: 'shells', },
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
  
  StackQL is available on macOS via Homebrew and the `pkg` Installer, both ARM (M1/Apple Silicon) and AMD architectures are supported with a single multi-arch installer.

  __Homebrew__

  To install via Homebrew, run the following command in your terminal:

  ```bash
  brew install stackql
  ```

  __Package download__

  StackQL is available as a signed and notarized, interactive `pkg` installer for MacOS.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
      <div className={clsx(buttonStyles.buttons)}>
        <BinaryDownloadLink 
          iconSize={20} 
          text="Download macOS PKG"
          to="https://storage.googleapis.com/stackql-public-releases/latest/stackql_darwin_multiarch.pkg"
          />
      </div>
  </Box>

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

  StackQL is available for all Linux architectures.

  __using curl__

  <Tabs
    defaultValue="amd64"
    values={[
      { label: 'amd64', value: 'amd64', },
      { label: 'arm64', value: 'arm64', },
    ]}
  >
  <TabItem value="amd64">

  ```bash
  curl -L https://bit.ly/stackql-zip -O \
  && unzip stackql-zip
  ```

  </TabItem>
  <TabItem value="arm64">

  ```bash
  curl -L https://bit.ly/stackql-arm64 -O \
  && unzip stackql-arm64
  ```

  </TabItem>  
  </Tabs>

  __Package download__  
  
  Alternatively, you can download the binaries here:

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Download Linux ZIP (amd64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Download Linux ZIP (arm64)"
        to="https://releases.stackql.io/stackql/latest/stackql_linux_arm64.zip"
      />      
    </div>
  </Box>

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
  
  StackQL is available on Windows via Chocolatey and the `msi` Installer, x64 and x86 architectures are supported.

  __Chocolatey__

  To install via Chocolatey, run the following command in your PowerShell or `cmd` terminal:

  ```powershell
  choco install stackql
  ```

  __Package download__  

  Alternatively, the Windows `stackql` binary can be downloaded here.

  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Download Windows MSI"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi"
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Download Windows ZIP"
        to="https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip"
      />      
    </div>
  </Box>  

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

  StackQL builds are published to [DockerHub](https://hub.docker.com/u/stackql).  

  __Docker Hub__  

  To pull the StackQL container image, run the following command:

  ```bash
  docker pull stackql/stackql
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

  ## `stackql-deploy`

  StackQL Deploy is a Python package that enables infrastructure deployment using StackQL. It provides a simplified interface for managing cloud resources across multiple providers with SQL-like syntax.  For more detailed information see [__StackQL Deploy Docs__](https://stackql-deploy.io/).  

  __PyPi__  

  The package is available on PyPI and can be installed using `pip`.

  <Tabs
    defaultValue="direct"
    values={[
      { label: 'Direct Install', value: 'direct', },
      { label: 'Virtual Environment', value: 'venv', },
    ]}
  >
  <TabItem value="direct">

  ```bash
  pip install stackql-deploy
  ```

  </TabItem>
  <TabItem value="venv">

  ```bash
  python -m venv stackql-env
  source stackql-env/bin/activate  # On Windows, use: stackql-env\Scripts\activate
  pip install stackql-deploy
  ```

  </TabItem>  
  </Tabs>

</Box>

</TabItem>
<TabItem value="shells">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>

  ## Cloud Shells

  StackQL can be used directly from major cloud providers' built-in cloud shells. This provides a seamless experience as these cloud shells are pre-authorized with your credentials for the cloud provider account you're logged into, eliminating the need for separate authentication setup.

  <Tabs
    defaultValue="aws"
    values={[
      { label: 'AWS', value: 'aws', },
      { label: 'Azure', value: 'azure', },
      { label: 'Google', value: 'google', },
    ]}
  >
  <TabItem value="aws">

  ### AWS Cloud Shell

  AWS CloudShell provides a browser-based shell with AWS CLI pre-installed and authenticated. Running StackQL in AWS CloudShell allows you to query and manage AWS resources without additional authentication steps. For detailed instructions, see our [AWS CloudShell tutorial](/docs/tutorials/aws/aws-cloud-shell).

  First, download the StackQL package:

  ```bash
  curl -L https://bit.ly/stackql-zip -O \
  && unzip stackql-zip
  ```
  Then run the StackQL AWS CloudShell script:
  
  ```bash
  sh stackql-aws-cloud-shell.sh
  ```  
  
  This script starts a StackQL command shell using your AWS CloudShell credentials, allowing you to immediately start querying AWS resources.

  </TabItem>
  <TabItem value="azure">

  ### Azure Cloud Shell

  Azure Cloud Shell provides a browser-accessible shell environment with Azure CLI pre-authenticated with your Azure account. Using StackQL in Azure Cloud Shell enables seamless querying of your Azure resources without additional setup. For complete details, check our [Azure Cloud Shell guide](https://docs.stackql.io/blog/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-azure-cloud-shell).  

  First, download the StackQL package:

  ```bash
  curl -L https://bit.ly/stackql-zip -O \
  && unzip stackql-zip
  ```

  Then run the StackQL Azure Cloud Shell script:

  ```bash
  sh stackql-azure-cloud-shell.sh
  ```

  This script automatically configures a StackQL session to use your Azure Cloud Shell credentials, enabling immediate access to query your Azure resources.

  </TabItem>
  <TabItem value="google">

  ### Google Cloud Shell

  Google Cloud Shell offers a development and operations environment with Google Cloud CLI already authenticated. Running StackQL in Google Cloud Shell lets you query GCP resources using your existing authentication. Learn more in our [Google Cloud Shell guide](https://docs.stackql.io/blog/using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp#using-stackql-in-the-google-cloud-shell).  

  First, download the StackQL package:
  
  ```bash
  curl -L https://bit.ly/stackql-zip -O \
  && unzip stackql-zip
  ```

  Then run the StackQL Google Cloud Shell script:

  ```bash
  sh stackql-google-cloud-shell.sh
  ```
  
  The script sets up StackQL to use your Google Cloud Shell credentials, allowing you to immediately start querying your GCP resources using SQL syntax.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Using GitHub Actions

[StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo) are available for use in your GitHub Actions workflows. The following actions are available:

<Tabs
  defaultValue="deploy"
  values={[
    { label: 'stackql-deploy', value: 'deploy', },
    { label: 'setup-stackql', value: 'setup', },
    { label: 'stackql-exec', value: 'exec', },
    { label: 'stackql-assert', value: 'assert', },    
  ]}
>
<TabItem value="deploy">

### `stackql-deploy`

Deploy or test infrastructure stacks using StackQL directly from your GitHub workflows. This action enables Infrastructure as Code (IaC) workflows using SQL-like syntax, allowing you to define, deploy, and manage cloud resources across multiple providers in a single workflow.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="View on the GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-deploy"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Example usage__

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
        uses: actions/checkout@v4

      - name: Deploy a Stack
        uses: stackql/stackql-deploy-action@v1.0.2
        with:
          command: 'build'
          stack_dir: 'examples/k8s-the-hard-way'
          stack_env: 'dev'
          env_vars: 'GOOGLE_PROJECT=stackql-k8s-the-hard-way-demo'
```

</TabItem>
<TabItem value="setup">

### `setup-stackql`

Setup StackQL in your GitHub Actions workflow.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="View on the GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Example usage__

```yaml
- name: setup StackQL
  uses: stackql/setup-stackql@v2.2.3
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
<TabItem value="exec">

### `stackql-exec`

Execute StackQL commands in your GitHub Actions workflow.  Queries can be supplied in line or from a file in the repo.  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="View on the GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-exec"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Example usage__

```yaml
- name: exec github example
  uses: stackql/stackql-exec@v2.2.3
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

### `stackql-assert`

Perform unit tests in your GitHub Actions workflow (for assurance, governance or cloud security checks).  

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="View on the GitHub Marketplace"
        to="https://github.com/marketplace/actions/stackql-assert"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Example usage__

```yaml
- name: Use test query string and expected rows
  uses: stackql/stackql-assert@v2.2.3
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


## Other Libraries

StackQL provides several integration methods that allow you for use in different programming languages and environments. These libraries extend StackQL's functionality, making it easy to incorporate cloud resource querying and management into your existing applications and workflows.  

<Tabs
  defaultValue="pystackql"
  values={[
    { label: 'pystackql', value: 'pystackql', },
  ]}
>
<TabItem value="pystackql">

### `pystackql` Python Package

Python wrapper to use StackQL in your Python programs. The `pystackql` package is available on [__PyPi__](https://pypi.org/project/pystackql/), documentation for the `pystackql` package is available via [__Read the Docs__](https://pystackql.readthedocs.io/en/latest/). To install the `pystackql` package, run the following command:

```bash
pip install pystackql
```

The following example shows the `pystackql` package used along with `pandas` to run StackQL queries and return the results to a `pandas.DataFrame`:

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