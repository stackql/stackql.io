---
title: 프로바이더 사용
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL에서 프로바이더 사용: 레지스트리에서 프로바이더를 가져오고, 환경 변수나 자격 증명으로 인증한 뒤, 해당 서비스와 리소스를 쿼리합니다."
image: "/img/stackql-featured-image.png"
---

참고 항목:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

StackQL 쿼리는 Google Cloud Platform, Amazon Web Services, Microsoft Azure, Digital Ocean 등 다양한 클라우드 제공업체를 대상으로 실행할 수 있습니다. StackQL 프로바이더 사양은 [__StackQL Provider Registry__](/providers)에서 가져옵니다. 이 작업은 [__`REGISTRY PULL`__](/language-spec/registry) StackQL 명령이나 [__`stackql registry`__](/command-line-usage/registry) 명령줄 유틸리티를 사용하여 수행할 수 있습니다.


## 프로바이더 설치 {#installing-a-provider}

사용 가능한 프로바이더 목록을 보려면 다음 명령을 실행하세요.  

```sql
REGISTRY LIST;
```

프로바이더를 설치하려면 다음 명령을 실행하세요.

```sql
REGISTRY PULL okta;
```

현재 시스템에 설치된 프로바이더를 보려면 다음 명령을 실행하세요.

```sql
SHOW PROVIDERS;
```

## 프로바이더 인증 {#authenticating-to-a-provider}

클라우드 제공업체에 인증하는 것은 StackQL 루틴의 첫 단계인 경우가 많습니다. [`SHOW`](/language-spec/show), [`DESCRIBE`](/language-spec/describe) 같은 메타 명령은 인증 없이 프로바이더에 대해 실행할 수 있지만, [`SELECT`](/language-spec/select), [`INSERT`](/language-spec/insert), [`EXEC`](/language-spec/exec) 명령에는 일반적으로 인증된 세션이 필요합니다.  

인증은 시스템에 설정된 환경 변수(또는 CI 시스템에서 시크릿으로 로드된 환경 변수)를 사용하여 수행됩니다. 각 프로바이더에는 인증에 사용하는 고유한 기본 변수가 있으므로, [StackQL Provider Registry 문서](/providers)에서 해당 프로바이더의 문서를 참조하세요. [`--env.file`](/command-line-usage/global-flags) 전역 플래그를 사용하여 시작 시 dotenv 스타일 파일에서 변수를 로드할 수도 있습니다.  

:::tip

각 프로바이더의 인증 변수를 설정하여 같은 대화형 셸이나 비대화형 배치 루틴에서 여러 프로바이더에 인증할 수 있습니다. 예를 들면 다음과 같습니다.  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

이 방법은 단일 쿼리에서 여러 프로바이더의 데이터를 조인할 때 특히 유용합니다.

:::
