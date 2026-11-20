---
title: AWS CloudShell의 StackQL
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
  - cspm
  - aws
  - amazon web services
description: "로그인한 사용자로 AWS CloudShell에서 StackQL 실행: Linux 패키지 다운로드, stackql shell 사용, IAM 역할 수임, exec 명령으로 배치 쿼리 실행"
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# AWS CloudShell에서 StackQL 사용

이 가이드에서는 AWS CloudShell 내에서 StackQL을 사용하는 방법을 보여 줍니다. AWS Console 로그인 세션의 사용자 컨텍스트를 사용하거나 다른 IAM 역할을 수임하여 실행합니다. StackQL은 SQL 구문을 사용하여 클라우드 인프라와 리소스를 쿼리하고 배포할 수 있는 강력한 개발 도구입니다.    

테스트 환경 <span class="tutorial_tested_on">임베디드 SQL 백엔드</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary><b>AWS CloudShell</b>이란?</summary>

> AWS CloudShell은 Amazon Web Services(AWS)에서 제공하는 브라우저 기반의 사전 인증된 셸 서비스입니다. 개발자와 IT 전문가는 웹 브라우저에서 직접 클라우드 리소스를 관리할 수 있습니다. AWS CloudShell을 사용하면 로컬 컴퓨터에 AWS CLI(Command Line Interface)를 설치하거나 구성하지 않고도 AWS 서비스 관련 명령을 빠르게 실행할 수 있습니다. 이 서비스는 AWS CLI, Python, Node.js 등 일반적인 개발 및 관리 도구가 미리 설치된 즉시 사용 가능한 환경을 제공하므로, AWS 서비스와 상호 작용하고 작업을 자동화하기가 더 쉬워집니다. AWS CloudShell은 AWS Management Console과 직접 통합되어 있어 관리 작업 실행, AWS 서비스 탐색, 인프라 관리를 원활하게 수행할 수 있습니다. AWS CloudShell은 AWS Console 로그인을 통해 사용자를 자동으로 인증하므로, AWS 계정 컨텍스트에서 명령과 스크립트를 실행하는 과정이 간소화되어 클라우드 운영의 생산성과 효율성이 향상됩니다.

</details>

## 작동 방식 {#how-it-works}

StackQL Linux 패키지에는 주요 클라우드 제공업체의 기본 제공 클라우드 셸 환경에서 `stackql` 바이너리를 직접 사용할 수 있는 스크립트가 포함되어 있습니다. AWS의 경우 __`stackql-aws-cloud-shell.sh`__ 스크립트를 사용합니다. __`stackql-aws-cloud-shell.sh`__ 스크립트의 사용법은 다음과 같습니다.  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

`stackql-aws-cloud-shell.sh` 스크립트는 StackQL용 최신 AWS 프로바이더를 자동으로 다운로드합니다. 포함된 클라우드 셸 스크립트의 소스 코드는 [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts)에서 확인할 수 있습니다.  

## AWS CloudShell에서 `stackql` Linux 패키지 다운로드 {#download-the-stackql-linux-package-from-aws-cloud-shell}

먼저 AWS CloudShell에서 다음 명령을 사용하여 StackQL의 최신 Linux 바이너리를 다운로드합니다.  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

이 명령은 `stackql` 바이너리와 `stackql-aws-cloud-shell.sh` 스크립트를 AWS CloudShell 환경의 홈 디렉터리에 다운로드하고 압축을 풉니다. 이제 사용할 준비가 되었습니다!  

## AWS CloudShell에서 `stackql shell` 명령 사용 {#using-the-stackql-shell-from-aws-cloud-shell}

AWS CloudShell 내에서 인증된 StackQL 명령 셸을 시작하려면 다음 명령을 실행하기만 하면 됩니다.  

```shell
sh stackql-aws-cloud-shell.sh
```

이제 다음과 같이 StackQL 프로바이더를 사용하여 SQL 기반 AWS 쿼리를 대화형으로 실행할 수 있습니다.  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  브라우저가 video 태그를 지원하지 않습니다.
</video>

## StackQL을 사용한 IAM 역할 수임 {#assuming-an-iam-role-using-stackql}

AWS에서 IAM 역할을 수임하면 기본 사용자 권한으로는 수행할 수 없는 작업을 수행하고 리소스에 액세스할 수 있습니다. 이는 리소스에 임시로 액세스해야 하거나 기본 계정과 다른 권한으로 작업을 수행해야 할 때 특히 유용합니다. StackQL을 사용하면 IAM 역할을 원활하게 수임하여 해당 역할에 부여된 권한으로 AWS 리소스를 쿼리하고 관리할 수 있습니다.

<details>
<summary>IAM 역할 수임 이해</summary>

> IAM 역할 수임은 AWS 리소스에 대한 임시 액세스 권한을 안전하게 부여하는 방법입니다. 역할을 수임하면 AWS STS가 임시 보안 자격 증명을 제공하며, 이 자격 증명으로 해당 역할에 사용 권한이 있는 AWS 서비스와 리소스에 액세스할 수 있습니다. 이 메커니즘은 계정 간 액세스, 사용자의 임시 액세스 또는 최소 권한 원칙 준수에 적합합니다.

> 역할은 AWS Management Console, AWS CLI 또는 AWS SDK를 사용하거나 StackQL 내에서 직접 수임할 수 있습니다. 이를 통해 사용자와 애플리케이션은 필요한 기간 동안 필요한 권한만 갖게 되므로 AWS 환경의 보안 태세가 강화됩니다.

</details>

AWS CloudShell에서 StackQL을 사용하여 IAM 역할을 수임하려면 `stackql-aws-cloud-shell.sh` 스크립트에 `--role-arn` 옵션을 사용할 수 있습니다. 이 옵션에는 수임할 역할의 Amazon 리소스 이름(ARN)이 필요합니다. 이 옵션을 지정하면 StackQL은 쿼리나 명령을 실행하기 전에 AWS Security Token Service(STS)를 사용하여 지정된 역할을 수임합니다.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

역할을 수임하면 StackQL을 활용하여 기본 사용자가 액세스할 수 없는 AWS 리소스와 상호 작용할 수 있으므로, 클라우드 환경을 더 유연하고 안전하게 관리할 수 있습니다.  

수임한 IAM 역할로 StackQL 명령이나 스크립트를 실행할 때는 해당 역할에 원하는 작업을 수행하고 지정된 리소스에 액세스하는 데 필요한 권한이 있는지 확인하세요. 이 방식은 세분화된 액세스 제어를 용이하게 하며 보안 및 거버넌스에 대한 AWS 모범 사례에 부합합니다.  

## AWS CloudShell에서 `stackql exec` 명령 실행(배치 작업) {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

StackQL 쿼리는 `exec` 명령을 사용하여 실행할 수도 있습니다. 이 방법은 다음과 같은 경우에 유용합니다.

- `json` 또는 `csv` 같은 대체 출력 형식 지정
- StackQL 쿼리 결과에 대한 출력 파일 생성
- 입력 파일(관례상 `.iql` 파일)을 통해 제공되는 StackQL 쿼리 사용
- 변수 로드 또는 쿼리 전처리(`json` 또는 `jsonnet` 사용)를 포함한 복잡한 작업 수행

AWS CloudShell 내에서 `exec` 명령을 사용하는 예제는 다음과 같습니다.  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

위 예제는 지정된 StackQL 쿼리를 실행하고 출력 `CSV` 파일을 생성하며, 이 파일은 AWS CloudShell에서 다운로드할 수 있습니다. 또한 `--role-arn` 인수로 IAM 역할을 지정하여 쿼리나 변경 작업에 다른 역할을 수임할 수 있습니다. 다음은 그 예제입니다(이 예제에서는 입력 파일에서 쿼리를 가져옵니다).  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::123456789012:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

`exec` 명령에 대한 자세한 내용은 [__여기__](/command-line-usage/exec)를 참조하세요.
