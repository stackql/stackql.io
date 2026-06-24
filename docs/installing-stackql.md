---
title: Installation
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
  - mcp
  - mcp-server
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
import { FaApple, FaWindows, FaDocker } from 'react-icons/fa';
import { FcLinux } from 'react-icons/fc';
import {
  BinaryDownloadLink,
  DropDownBinaryDownloadLink,
} from '../src/components';
import buttonStyles from '../src/components/BinaryDownloadLink/binarydownloadlink.module.css';
import clsx from 'clsx';

export const ClaudeIcon = ({ size = '1em' }) => (
  <svg xmlns="http://www.w3.org/2000/svg" width={size} height={size} viewBox="0 0 24 24" role="img" aria-hidden="true" style={{ flex: 'none' }}>
    <path fill="#D97757" d="m4.7144 15.9555 4.7174-2.6471.079-.2307-.079-.1275h-.2307l-.7893-.0486-2.6956-.0729-2.3375-.0971-2.2646-.1214-.5707-.1215-.5343-.7042.0546-.3522.4797-.3218.686.0608 1.5179.1032 2.2767.1578 1.6514.0972 2.4468.255h.3886l.0546-.1579-.1336-.0971-.1032-.0972L6.973 9.8356l-2.55-1.6879-1.3356-.9714-.7225-.4918-.3643-.4614-.1578-1.0078.6557-.7225.8803.0607.2246.0607.8925.686 1.9064 1.4754 2.4893 1.8336.3643.3035.1457-.1032.0182-.0728-.164-.2733-1.3539-2.4467-1.445-2.4893-.6435-1.032-.17-.6194c-.0607-.255-.1032-.4674-.1032-.7285L6.287.1335 6.6997 0l.9957.1336.419.3642.6192 1.4147 1.0018 2.2282 1.5543 3.0296.4553.8985.2429.8318.091.255h.1579v-.1457l.1275-1.706.2368-2.0947.2307-2.6957.0789-.7589.3764-.9107.7468-.4918.5828.2793.4797.686-.0668.4433-.2853 1.8517-.5586 2.9021-.3643 1.9429h.2125l.2429-.2429.9835-1.3053 1.6514-2.0643.7286-.8196.85-.9046.5464-.4311h1.0321l.759 1.1293-.34 1.1657-1.0625 1.3478-.8804 1.1414-1.2628 1.7-.7893 1.36.0729.1093.1882-.0183 2.8535-.607 1.5421-.2794 1.8396-.3157.8318.3886.091.3946-.3278.8075-1.967.4857-2.3072.4614-3.4364.8136-.0425.0304.0486.0607 1.5482.1457.6618.0364h1.621l3.0175.2247.7892.522.4736.6376-.079.4857-1.2142.6193-1.6393-.3886-3.825-.9107-1.3113-.3279h-.1822v.1093l1.0929 1.0686 2.0035 1.8092 2.5075 2.3314.1275.5768-.3218.4554-.34-.0486-2.2039-1.6575-.85-.7468-1.9246-1.621h-.1275v.17l.4432.6496 2.3436 3.5214.1214 1.0807-.17.3521-.6071.2125-.6679-.1214-1.3721-1.9246L14.38 17.959l-1.1414-1.9428-.1397.079-.674 7.2552-.3156.3703-.7286.2793-.6071-.4614-.3218-.7468.3218-1.4753.3886-1.9246.3157-1.53.2853-1.9004.17-.6314-.0121-.0425-.1397.0182-1.4328 1.9672-2.1796 2.9446-1.7243 1.8456-.4128.164-.7164-.3704.0667-.6618.4008-.5889 2.386-3.0357 1.4389-1.882.929-1.0868-.0062-.1579h-.0546l-6.3385 4.1164-1.1293.1457-.4857-.4554.0608-.7467.2307-.2429 1.9064-1.3114Z" />
  </svg>
);

export const tabLabel = (icon, text) => (
  <span style={{ display: 'inline-flex', alignItems: 'center', gap: '0.4em' }}>{icon}{text}</span>
);

Instructions for installing StackQL on various different platforms are provided here.

<Tabs
  defaultValue="macos"
  values={[
    { label: tabLabel(<FaApple />, 'macOS'), value: 'macos', },
    { label: tabLabel(<FcLinux />, 'Linux'), value: 'linux', },
    { label: tabLabel(<FaWindows style={{ color: '#00A4EF' }} />, 'Windows'), value: 'windows', },
    { label: tabLabel(<FaDocker style={{ color: '#2496ED' }} />, 'Docker'), value: 'docker', },
    { label: tabLabel(<ClaudeIcon />, 'Claude Desktop'), value: 'mcp', },
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
<TabItem value="mcp">

<Box sx={{ 
  border: '1px solid #e0e0e0', 
  borderRadius: '4px', 
  p: 3, 
  mb: 2
}}>

  ## Claude Desktop / MCP clients

  Prebuilt MCP Bundles (`.mcpb`) are attached to every StackQL release for one-click installation of the StackQL MCP server into Claude Desktop - no separate StackQL installation is required.  Bundles are available for macOS, Windows and Linux.
  
  <Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="Download macOS MCPB"
        to="https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb"
      />
      <DropDownBinaryDownloadLink 
        iconSize={20} 
        text="Download Linux MCPB (x64)"
        to="https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb"
        options={[
          { text: 'Download Linux MCPB (arm64)', to: 'https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb' },
        ]}
      />
      <BinaryDownloadLink 
        iconSize={20} 
        text="Download Windows MCPB"
        to="https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-windows-x64.mcpb"
      />      
    </div>
  </Box>

<br />
:::info

The StackQL MCP server is also listed on the [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) as `io.github.stackql/stackql-mcp`.

:::

:::tip

The bundle is one of several ways to run the StackQL MCP server.  See [__Installing the MCP server__](#installing-the-mcp-server) for the npx, Docker, Python, CI and manual options, or [__Using StackQL with Claude Desktop__](/docs/getting-started/claude-desktop#one-click-install-mcp-bundle) for the full Claude Desktop walkthrough.

:::

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

`stackql-deploy` is a stateless IaC framework built on StackQL. For full documentation see [__StackQL Deploy Docs__](https://stackql-deploy.io/).

__Linux / macOS__

Downloads and extracts the latest `stackql-deploy` binary in one step. The installer detects your OS and architecture (including Apple Silicon and Intel via a universal macOS binary) and fetches the correct release asset automatically.
```bash
curl -fsSL https://get-stackql-deploy.io/install.sh | sh
```

__Windows__

Downloads and extracts the latest `stackql-deploy` binary into the current directory.
The installer detects your platform and fetches the correct release asset automatically.
```powershell
irm https://get-stackql-deploy.io/install.ps1 | iex
```

__cargo__

If you have the Rust toolchain installed, this builds and installs the binary directly from [__crates.io__](https://crates.io/crates/stackql-deploy).
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

  ## Cloud Shells

  StackQL can be used directly from major cloud and data platforms' built-in cloud shells and web terminals. This provides a seamless experience as these environments are pre-authorized with the identity you're logged into, eliminating the need for separate authentication setup.

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
  <TabItem value="databricks">

  ### Databricks Web Terminal

  Databricks workspaces include a web terminal that runs as the logged-in user. Running StackQL there lets you query your workspace using your Databricks identity, with no separate authentication setup. For example queries and account-level auth, see our [Databricks Web Terminal guide](/blog/stackql-in-databricks-web-terminal).

  First, download the StackQL package:

  ```bash
  curl -L https://bit.ly/stackql-zip -O \
  && unzip stackql-zip
  ```

  Then run the StackQL Databricks shell script:

  ```bash
  sh stackql-databricks-shell.sh
  ```

  This starts a StackQL session using your Databricks workspace identity, so you can immediately query workspace-scoped resources such as `databricks_workspace.iam.vw_user_entitlements`. For account-level queries (provisioning, billing, account IAM), set the Databricks OAuth2 service principal variables as described in the guide.

  </TabItem>    
  </Tabs>

</Box>

</TabItem>
</Tabs>

## Installing the MCP server

The StackQL MCP server lets AI agents query and provision cloud resources using StackQL. It ships through several channels - they all run the same server, so pick the channel that matches your client and your trust requirements. The options below are listed roughly in order of trust and ease.

### Marketplaces and directories

The server is published to the Official MCP Registry as `io.github.stackql/stackql-mcp`; the package registries below carry the distributable artifacts, and downstream directories pick the listing up from there.  

<Tabs
  defaultValue="registries"
  values={[
    { label: 'Registries', value: 'registries', },
    { label: 'Directories', value: 'directories', },
  ]}
>
<TabItem value="registries">

| Registry | Type | Published via | Listing | Notes |
|---|---|---|---|---|
| Official MCP Registry | Canonical metadata registry | `mcp-publisher` CLI + `server.json` | [`modelcontextprotocol.io/search`](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)<br/>[`modelcontextprotocol.io/direct`](https://registry.modelcontextprotocol.io/v0/servers?search=io.github.stackql/stackql-mcp) | advertises all 7 package types (`mcpb x4`, `oci`, `npm`, `pypi`) |
| npm | Package registry | `npm publish` | [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) | `npx` launcher |
| PyPI | Package registry | `twine upload` | [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) | `uvx` / `pip` launcher |
| Docker Hub | Container registry | `docker buildx --push` | [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) | multi-arch `amd64` + `arm64` |
| GitHub Actions Marketplace | CI marketplace (`setup-stackql-mcp` action) | Public repo + release + marketplace listing | [`setup-stackql-mcp-server`](https://github.com/marketplace/actions/setup-stackql-mcp-server) | Verified publisher; [`stackql/setup-stackql-mcp`](https://github.com/stackql/setup-stackql-mcp) |

</TabItem>
<TabItem value="directories">

Directory and aggregator listings, in rough order of significance for discovery:

| Directory | Type | Listing |
|---|---|---|
| __Cursor Directory__ | IDE client directory | [cursor.directory](https://cursor.directory/plugins/stackql-mcp-server) |
| __mcp.so__ | Largest aggregator | [mcp.so](https://mcp.so/server/stackql/stackql) |
| __PulseMCP__ | Discovery, registry backer | [pulsemcp](https://www.pulsemcp.com/servers/stackql) |
| __Glama.ai MCP__ | Searchable marketplace | [glama.ai](https://glama.ai/mcp/servers/stackql/stackql) |
| __mcpmarket.com__ | Aggregator | [mcpmarket](https://mcpmarket.com/server/stackql) |
| __mcpservers.org__ | Awesome-list site | [mcpservers.org](https://mcpservers.org/servers/stackql-mcp-server) |

</TabItem>
</Tabs>

### Prebuilt `.mcpb` bundle

**When to use:** Claude Desktop, no separate StackQL install, and you want a signed one-click bundle.

A prebuilt MCP Bundle is attached to every StackQL release for each platform:

```
https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-<platform>.mcpb
```

where `<platform>` is one of `darwin-universal`, `windows-x64`, `linux-x64`, or `linux-arm64`. Download buttons for each platform are in the **Claude Desktop / MCP** tab above. Each bundle has a matching `.sha256` checksum on the [release page](https://github.com/stackql/stackql/releases/latest). Verify it, then install via **Settings -> Extensions** in Claude Desktop:

```bash
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

See [Using StackQL with Claude Desktop](/docs/getting-started/claude-desktop#one-click-install-mcp-bundle) for the full walkthrough.

### Manual `claude_desktop_config.json`

**When to use:** you already have the `stackql` binary on your PATH and use Claude Desktop or any other stdio MCP client.

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

All three arguments are load-bearing:

- `--mcp.server.type=stdio` selects the stdio transport that editor-embedded clients speak.
- `--approot` points the provider cache at a writable directory. MCP clients may launch the server with the working directory set to `/`, which is not writable.
- `--mcp.config '{"server": {"audit": {"disabled": true}}}'` disables the audit log, which otherwise defaults its directory to the (possibly non-writable) working directory.

Add provider credentials with an `"env"` block and tune the safety contract with `"mode"` - see [Server modes](/docs/command-line-usage/mcp#server-modes).

### `npx` (no install)

**When to use:** any Node environment, when you do not want a global StackQL install.

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

The [`@stackql/mcp-server`](https://www.npmjs.com/package/@stackql/mcp-server) launcher downloads the signed `stackql` binary on first run, verifies its SHA-256, and caches it for subsequent runs.

### `uvx` / `pip` (Python)

**When to use:** a Python environment - the same launcher packaged for Python, sharing the binary cache the npx launcher populates.

```json
{ "mcpServers": { "stackql": { "command": "uvx", "args": ["stackql-mcp-server"] } } }
```

Or install it into the current environment with `pip install stackql-mcp-server` (Python 3.9+). The package is [`stackql-mcp-server`](https://pypi.org/project/stackql-mcp-server/) on PyPI.

### Docker

**When to use:** a containerised or otherwise isolated runtime. The image is multi-arch (amd64 and arm64).

```bash
docker run -i --rm stackql/stackql-mcp
```

As a client configuration:

```json
{ "mcpServers": { "stackql": { "command": "docker", "args": ["run", "-i", "--rm", "stackql/stackql-mcp"] } } }
```

The image is [`stackql/stackql-mcp`](https://hub.docker.com/r/stackql/stackql-mcp) on Docker Hub.

### CI and agentic workflows (GitHub Actions)

**When to use:** run the server inside a GitHub Actions job so an agent can query - and, if you allow it, act on - your cloud as part of CI.

[`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) installs the binary and writes an MCP config (defaulting to `read_only` mode). Pass that config to [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) through `claude_args`:

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

See the [action README](https://github.com/stackql/setup-stackql-mcp) for more agentic recipes (cloud audits, cost estimates, and so on).

### Embedded MCP

**When to use:** you are building an agentic app and want to vendor the MCP server into your own binary - no `npx`, no separate install, no runtime dependency.

Native libraries spawn the signed `stackql` binary over stdio behind each language's official MCP SDK client, so your app gets the same governed SQL interface to every provider in the registry. Each library defaults to `read_only` - escalation to a writable mode is always explicit - and can either download-and-verify the binary on first run or vendor it into your build for a single self-contained executable. The full per-language API is in the [Embedded MCP reference](/docs/mcp/embedded).

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

The `stackql-mcp` crate spawns the binary behind an `rmcp` client. The default `sidecar` feature downloads and verifies the binary on first run; the `vendored` feature embeds it with `include_bytes!` for a single self-contained binary. MSRV Rust 1.88.

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
    <BinaryDownloadLink iconSize={20} text="View on GitHub" to="https://github.com/stackql/stackql-mcp-rs" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="View on crates.io" to="https://crates.io/crates/stackql-mcp" isOpenInNewTab={true} />
  </div>
</Box>

Full reference: [Embedded MCP: Rust](/docs/mcp/embedded/rust).

</TabItem>
<TabItem value="go">

The `stackql-mcp-go` library spawns the binary behind the official Go MCP SDK client. Vendor the binary with `go:embed` for a single self-contained binary, or let it download and verify on first run. Starts in `read_only` mode by default.

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
    <BinaryDownloadLink iconSize={20} text="View on GitHub" to="https://github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
    <BinaryDownloadLink iconSize={20} text="View on pkg.go.dev" to="https://pkg.go.dev/github.com/stackql/stackql-mcp-go" isOpenInNewTab={true} />
  </div>
</Box>

Full reference: [Embedded MCP: Go](/docs/mcp/embedded/go).

</TabItem>
<TabItem value="kotlin">

The `io.stackql:stackql-mcp` library spawns the binary behind the official Kotlin MCP SDK client, and a companion Gradle plugin wires the same launch into a build. Requires JDK 17 and Kotlin 2.x.

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
    <BinaryDownloadLink iconSize={20} text="View on GitHub" to="https://github.com/stackql/stackql-mcp-kotlin" isOpenInNewTab={true} />
  </div>
</Box>

Full reference: [Embedded MCP: Kotlin / JVM](/docs/mcp/embedded/kotlin).

</TabItem>
<TabItem value="dotnet">

The `StackQL.Mcp` package spawns the binary behind the official C# MCP SDK client. Sidecar by default, or vendor the bundle as a build resource for a self-contained executable. Requires .NET 8 or later.

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
    <BinaryDownloadLink iconSize={20} text="View on GitHub" to="https://github.com/stackql/stackql-mcp-dotnet" isOpenInNewTab={true} />
  </div>
</Box>

Full reference: [Embedded MCP: .NET / C#](/docs/mcp/embedded/dotnet).

</TabItem>
<TabItem value="gleam">

The `stackql_mcp` library targets the Erlang/BEAM runtime and exposes the server as an OTP child via `child_spec()`, so it can sit inside your own supervision tree. Starts in `read_only` mode by default.

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
    <BinaryDownloadLink iconSize={20} text="View on GitHub" to="https://github.com/stackql/stackql-mcp-gleam" isOpenInNewTab={true} />
  </div>
</Box>

Full reference: [Embedded MCP: Gleam](/docs/mcp/embedded/gleam).

</TabItem>
<TabItem value="swift">

The `stackql-mcp-swift` package spawns the binary behind the official Swift MCP SDK client. The `darwin-universal` binary is Developer ID signed and Apple-notarised, so you can bundle it inside a signed `.app` and keep the app's notarisation valid. Requires macOS 13+ and Swift 6.1 (Xcode 16.3+).

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
    <BinaryDownloadLink iconSize={20} text="View on GitHub" to="https://github.com/stackql/stackql-mcp-swift" isOpenInNewTab={true} />
  </div>
</Box>

Full reference: [Embedded MCP: Swift](/docs/mcp/embedded/swift).

</TabItem>
</Tabs>

### Trust model

The same security properties hold across every channel:

- The embedded `stackql` binary is Authenticode-signed (Windows) and Apple-notarised (macOS).
- Every `.mcpb` bundle ships with a published SHA-256 checksum on the release page.
- The `npx` and `uvx` launchers verify the downloaded binary's SHA-256 before first use.
- The [MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) entry attests the per-platform hashes.

## Using GitHub Actions

[StackQL GitHub Actions](https://github.com/stackql/stackql-actions-demo) are available for use in your GitHub Actions workflows. The following actions are available:

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

### `setup-stackql-mcp`

Install the StackQL MCP server in your GitHub Actions workflow.  The action installs the signed binary and writes an `mcpServers` config (defaulting to `read_only` mode) that an agentic step such as `anthropics/claude-code-action` consumes via `claude_args`.

<Box sx={{ mt: 2, mb: 1, display: 'flex', gap: 2 }}>
    <div className={clsx(buttonStyles.buttons)} style={{ display: 'flex', gap: '12px' }}>
      <BinaryDownloadLink 
        iconSize={20} 
        text="View on the GitHub Marketplace"
        to="https://github.com/marketplace/actions/setup-stackql-mcp-server"
        isOpenInNewTab={true}
        />
      <BinaryDownloadLink 
        iconSize={20} 
        text="View on GitHub"
        to="https://github.com/stackql/setup-stackql-mcp"
        isOpenInNewTab={true}
        />
    </div>
</Box>
<br/>

__Example usage__

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