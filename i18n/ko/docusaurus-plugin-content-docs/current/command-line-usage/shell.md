---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "stackql shell 명령은 클라우드 및 SaaS 계정에 대해 쿼리와 프로바이더 명령을 실행하는 StackQL 대화형 명령 셸을 시작합니다."
image: "/img/stackql-featured-image.png"
---

StackQL 대화형 명령 셸을 시작하는 데 사용하는 명령입니다. 자세한 내용은 [StackQL 대화형 셸 사용](/getting-started/using-stackql#using-the-stackql-interactive-shell)을 참조하세요.

* * * 

### 구문 {#syntax}

`stackql shell [flags]`

* * *

### 플래그 {#flags}

| 플래그 | 설명 |
|--|--|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|도움말 정보 출력|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|상세 모드로 쿼리를 실행하며 추가 출력은 stdout으로 전송됩니다. `-f` 옵션을 선택한 경우 이 추가 로깅 정보는 쿼리 결과와 함께 출력 파일에 기록됩니다.|
<br/>
<br/>
> 추가 옵션은 [전역 플래그](/command-line-usage/global-flags)를 참조하세요.

* * *

### 예제 {#examples}

인증에 서비스 계정 키 파일을 사용하여 StackQL 셸을 시작합니다.
```shell
stackql shell --keyfilepath /tmp/stackql-demo.json
```
