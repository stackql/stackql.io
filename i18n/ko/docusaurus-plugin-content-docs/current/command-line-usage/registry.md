---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "stackql registry 명령은 명령줄에서 StackQL Provider Registry의 프로바이더를 나열하고, 가져오고, 관리합니다."
image: "/img/stackql-featured-image.png"
---

StackQL Provider Registry와 상호 작용하는 데 사용하는 명령입니다. 레지스트리 위치와 프로바이더 문서 서명의 검증 여부는 [`--registry`](/command-line-usage/global-flags#--registry) 전역 플래그로 구성합니다. [액세스 관리](/command-line-usage/access-management)가 활성화되어 있는 동안에는 `registry` 명령을 사용할 수 없습니다. 대신 보호된 `exec` 또는 `shell` 세션에서 `REGISTRY PULL` 문을 사용하세요.

* * * 

### 구문 {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### 인수 {#arguments}

| 인수 | 설명 | 예 |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|수행할 레지스트리 작업(`list` 또는 `pull`) | `list` 또는 `pull` |    
|<span class="nowrap">`provider`</span>|`pull` 하위 명령으로 설치할 프로바이더| `okta` |
|<span class="nowrap">`version`</span>|`pull` 하위 명령으로 설치할 선택적 프로바이더 버전 *(StackQL 프로바이더 문서 버전)* | `v23.03.00121` |


### 플래그 {#flags}

| 플래그 | 설명 |
|--|--|
|<span class="nowrap">`-H, --help`</span>|도움말 정보 출력|
<br/>
<br/>
> 추가 옵션은 [전역 플래그](/command-line-usage/global-flags)를 참조하세요.

* * *

### 예제 {#examples}

공개 레지스트리에서 사용 가능한 프로바이더를 나열합니다:
```shell
stackql registry list
```

공개 레지스트리에서 프로바이더의 최신 버전을 가져옵니다:
```shell
stackql registry pull okta
```
