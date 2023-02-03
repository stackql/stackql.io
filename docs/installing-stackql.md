---
title: Installation
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
slug: /installing-stackql
---

Instructions for installing StackQL on various different platforms are provided here.  

## MacOS

StackQL is available on MacOS via Homebrew and the `pkg` Installer, both ARM (M1/Apple Silicon) and AMD archtectures are supported with a single multi-arch installer.  

### Homebrew

To install via Homebrew, run the following command in your terminal:  

```bash
brew tap stackql-ops/stackql
brew install stackql-ops/stackql/stackql
```

### `pkg` Installer

StackQL is available as a signed and notarized, interactive `pkg` installer for MacOS which can be downloaded at [__latest/stackql_darwin_multiarch.pkg__](https://storage.googleapis.com/stackql-public-releases/latest/stackql_darwin_multiarch.pkg).  Double click the `pkg` file and following the prompts.  

![StackQL MacOS pkg Installer](/img/mac-pkg-installer-screenshot.png)

## Windows

StackQL is available on Windows via Chocolatey and the `msi` Installer, x64 and x86 architectures are supported.  

### Chocolatey

To install via Chocolatey, run the following command in your PowerShell or `cmd` terminal:  

```powershell
choco install stackql --version=0.3.265
```

### `msi` Installer

To perform an interactive installation on Windows, you can use the signed `msi` installer which can be downloaded at [__latest/stackql_windows_amd64.msi__](https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi).  Double click the `msi` file and following the prompts.  

![StackQL Windows msi Installer](/img/stackql-msi-installer.png)

### `zip` Archive

Alternatively, the Windows `stackql` binary can be downloaded from [__latest/stackql_windows_amd64.zip__](https://releases.stackql.io/stackql/latest/stackql_windows_amd64.zip) and placed in your PATH.  The `stackql` binary is available for x64 and x86 architectures.


## Linux

StackQL is available for all Linux distributions, simply download the StackQL Linux archive from [__latest/stackql_linux_amd64.zip__](https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip), extract the `stackql` binary and place it in your PATH. StackQL is available for amd based Linux architectures.  

Alternatively, you could use `curl` as shown here:  

```bash
curl -L https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip -O \
&& unzip stackql_linux_amd64.zip \
&& ./stackql --version
```

## Docker

StackQL builds are published to [__DockerHub__](https://hub.docker.com/u/stackql).  To pull the StackQL container image, run the following command:  

```bash
docker pull stackql/stackql
```

## 🚧 GitHub Actions

Coming soon!




