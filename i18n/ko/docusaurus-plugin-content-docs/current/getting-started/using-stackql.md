---
title: StackQL 사용
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL 사용: 셸에서 대화형으로, exec로 비대화형으로, 또는 서버로 쿼리를 실행하고, 진행하면서 프로바이더, 서비스, 리소스를 탐색합니다."
image: "/img/stackql-featured-image.png"
---
참고 항목:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL에는 여러 사용 모드가 있습니다. StackQL 대화형 셸, StackQL 문을 사용하는 StackQL 명령줄, 입력 파일(IQL 파일)에 있는 하나 이상의 StackQL 문 실행이 여기에 해당합니다. 각 모드는 아래에서 자세히 설명합니다.

- StackQL 대화형 셸 사용([`stackql shell`](/command-line-usage/shell))
- StackQL 명령줄 유틸리티 사용([`stackql exec`](/command-line-usage/exec))
- 입력 파일(IQL 파일) 사용([`stackql exec -i`](/command-line-usage/exec))

### StackQL 대화형 셸 사용 {#using-the-stackql-interactive-shell}
StackQL 셸은 엔지니어가 클라우드 리소스를 배포하고 상호 작용할 수 있는 대화형 프로그래밍 환경을 제공하며, 클라우드 환경을 대화형으로 분석([`SELECT`](/language-spec/select) 문 사용)할 수 있게 합니다. 셸은 기능적으로 MySQL 셸이나 기타 REPL(Read Evaluate Print Loop) 셸과 비슷합니다. 셸은 유효한 StackQL 문을 받아 사용자에게 결과를 반환합니다. 문은 여러 줄에 걸쳐 작성할 수 있으며, 세미콜론 종결자를 입력하면 실행됩니다.

StackQL 셸은 아래와 같이 `shell` 명령을 사용하여 호출할 수 있습니다(프로바이더 인증에 대한 정보는 [StackQL Provider Registry](/providers)를 참조하세요).

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
클라우드 제공업체에 인증된 셸에 들어가면 StackQL 쿼리(`SELECT` 쿼리 또는 코드형 인프라에 해당하는 DDL 작업)를 실행할 수 있습니다.

기본적으로 StackQL 셸은 표 형식의 결과를 반환하지만, `--output` 플래그를 사용하여 `json`, `jsonl`, `csv`, `text`, `otel` 출력 형식을 사용할 수도 있습니다. [출력 모드](/getting-started/output-modes)를 참조하세요.  

### `.stackqlrc` 초기화 파일 {#the-stackqlrc-initialization-file}

출력 형식 같은 StackQL 명령 옵션의 기본값은 `.stackqlrc` 초기화 파일을 사용하여 설정할 수 있습니다. 이 파일이 StackQL 실행 파일이 사용하는 현재 디렉터리에 있으면 자동으로 로드됩니다. 또는 아래와 같이 `--configfile` 플래그를 사용하여 이 파일을 참조할 수도 있습니다.

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
명령 옵션에 대한 자세한 내용은 [전역 플래그](/command-line-usage/global-flags)에서 확인할 수 있습니다.

### StackQL 명령줄 유틸리티 사용 {#using-the-stackql-command-line-utility}
StackQL 명령은 StackQL 실행 파일의 exec 명령을 사용하여 비대화형으로 실행할 수 있습니다. 각 개별 명령이 세미콜론(`;`)으로 끝나면 여러 명령을 하나의 배치로 실행할 수 있습니다. `--output` 플래그 같은 명령 플래그는 명령줄에서 지정하거나 앞에서 설명한 `.stackqlrc` 초기화 파일을 사용하여 지정할 수 있습니다.  

StackQL 실행 파일의 출력은 사용 중인 시스템의 stdout으로 전송되며, 이 출력은 BASH 또는 PowerShell 스크립트에서 구문 분석할 수 있습니다.  

다음은 StackQL 명령을 포함하는 Windows PowerShell 스크립트의 예입니다.  

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$env:GOOGLE_CREDENTIALS = Get-Content -Raw "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from google.compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
위 PowerShell 명령은 Compute Engine 인스턴스를 선택하고 `Out-GridView` cmdlet을 사용하여 출력을 표시하는 방법을 보여 줍니다.  

### 입력 파일(IQL 파일) 사용 {#using-an-input-file-iql-file}
StackQL 입력 파일을 사용하면 세미콜론으로 끝나는 여러 명령을 실행할 수 있습니다. 이는 비대화형 또는 배치 인프라 프로그래밍을 위한 또 다른 옵션입니다. 입력 파일은 아래와 같이 `-i` 또는 `--infile` 옵션으로 지정합니다.

```shell
# create an iql file
echo "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "/mnt/c/tmp/stackql-demo.json" } }' \
--output json | jq '.[] | .name'
```
