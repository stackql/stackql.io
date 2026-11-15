---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "stackql exec 명령은 StackQL 문 하나 또는 문 스크립트를 실행하며, 출력 형식, 인증 및 프로바이더 레지스트리에 대한 플래그를 제공합니다."
image: "/img/stackql-featured-image.png"
---

StackQL 쿼리를 단일 문으로 실행하거나 StackQL 스크립트(IQL 파일)의 문 배치로 실행하는 데 사용하는 명령입니다.  

* * * 

### 구문 {#syntax}

`stackql exec [query] [flags]`

* * *

### 인수 {#arguments}

| 인수 | 설명 | 예제 |
|--|--|--|
|`query`|수행할 StackQL 쿼리<br/>또는 실행할 명령 | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_쿼리는 작은따옴표 또는 큰따옴표로 묶어야 합니다_ |    


### 플래그 {#flags}

| 플래그 | 설명 |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|출력 모드입니다. 유효한 값은 `table`(기본값) `json`, `jsonl`(줄 바꿈으로 구분된 JSON, `ndjson`도 별칭으로 허용됨), `csv`, `text`, `otel`(OpenTelemetry 로그 레코드, [출력 모드](/getting-started/output-modes#otel-output) 참조)입니다. 예: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|필요한 경우 지정한 파일에서 쿼리를 실행합니다. 이 플래그를 지정하면 인수로 제공한 쿼리는 무시됩니다. 예: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|`json` 또는 `jsonnet` 파일을 사용하는 명령의 소스 데이터입니다. 예: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|드라이 런 플래그입니다. 전처리기만 실행되고 그 출력이 반환됩니다 |
|<span class="nowrap">`--var`</span>|`--iqldata` 플래그로 지정한 jsonnet 파일에서 사용하는 외부 변수(환경 변수 또는 시크릿 사용 가능)입니다. 예: `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|필요한 경우 결과를 출력할 파일입니다(기본 동작은 결과를 콘솔에만 출력하는 것입니다). 예: `--outfile myoutput.csv`|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|도움말 정보를 출력합니다|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|추가 출력을 stdout으로 보내는 자세한 정보 표시 모드로 쿼리를 실행합니다. `-f` 옵션을 선택하면 이 추가 로깅 정보가 쿼리 결과와 함께 출력 파일에 기록됩니다|
<br/>
<br/>
> 추가 옵션은 [전역 플래그](/command-line-usage/global-flags)를 참조하세요.


* * *

### 예제 {#examples}

쿼리 결과를 테이블 형식으로 콘솔에 출력합니다.
```shell
stackql exec "SHOW services IN google"
```

쿼리 결과를 json 형식으로 콘솔에 출력합니다.
```shell
stackql exec "SHOW services IN google" -o json
```

쿼리 결과를 json 형식으로 파일에 출력합니다.
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

입력 파일(IQL 파일)에서 쿼리를 실행하고 결과를 json 형식으로 파일에 출력합니다.
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### 종료 상태 {#exit-status}

`exec` 명령은 입력 처리가 끝나면 상태 `0`으로 종료됩니다. 개별 문의 오류는 콘솔에 기록되므로 스크립트에서 출력을 검사해야 합니다. `v1.0`부터는 [`SET`](/language-spec/set) 또는 [`UNSET`](/language-spec/unset) 구성 문이 실패하거나, [액세스 관리](/command-line-usage/access-management)가 활성화된 상태에서 문이 거부되거나, 미리 보기 IaC 실행에서 실패한 리소스를 보고하면 상태 `1`로 종료됩니다.
