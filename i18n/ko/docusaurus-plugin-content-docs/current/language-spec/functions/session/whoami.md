---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 액세스 관리
  - whoami
  - 보안 주체
description: "StackQL의 WHOAMI 함수: 액세스 관리가 활성화된 경우 인증된 호출자의 보안 주체 ID를 반환합니다."
image: "/img/stackql-featured-image.png"
---
인증된 호출자의 보안 주체 ID를 반환합니다.  [액세스 관리](/command-line-usage/access-management)를 활성화하여 시작한 세션이 필요합니다.  StackQL `v1.0` 이상에서 사용할 수 있습니다.

참고 항목:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ 액세스 관리 ]](/command-line-usage/access-management)

* * * 

## 구문 {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## 인수 {#arguments}

없음.

## 반환 값 {#return-values}

현재 문에 대해 설정된 보안 주체 ID 문자열을 반환합니다.  이 함수는 `--access.config` 플래그 없이 시작한 세션에서 실패합니다.

* * *

## 예제 {#examples}

### 설정된 ID 확인 {#inspect-the-established-identity}

두 함수 모두 인수를 받지 않으며, 서버가 이 문에 대해 인증한 호출자를 설명합니다.  `whoami()` 함수는 보안 주체 ID를, `groups()` 함수는 호출자의 디렉터리 그룹을 반환합니다. 따라서 두 함수를 함께 실행하면 다른 작업을 실행하기 전에 토큰이나 인증서가 예상한 보안 주체에 매핑되는지 빠르게 확인할 수 있습니다.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
