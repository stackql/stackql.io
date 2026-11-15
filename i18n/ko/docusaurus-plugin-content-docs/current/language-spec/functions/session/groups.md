---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 액세스 관리
  - groups
  - 보안 주체 디렉터리
description: "StackQL의 GROUPS 함수: 액세스 관리가 활성화된 경우 인증된 호출자의 디렉터리 그룹을 JSON 배열로 반환합니다."
image: "/img/stackql-featured-image.png"
---
인증된 호출자의 디렉터리 그룹 멤버십을 JSON 배열로 반환합니다.  [액세스 관리](/command-line-usage/access-management)를 활성화하여 시작한 세션이 필요합니다.  StackQL `v1.0` 이상에서 사용할 수 있습니다.

참고 항목:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ 액세스 관리 ]](/command-line-usage/access-management)

* * * 

## 구문 {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## 인수 {#arguments}

없음.

## 반환 값 {#return-values}

현재 문에 대해 생성된 보안 주체 디렉터리 스냅샷의 그룹 ID를 JSON 배열로 반환합니다(예: `["readers"]`).  멤버십은 문마다 새로 고쳐지므로 해지는 다음 문에 반영됩니다.  이 함수는 `--access.config` 플래그 없이 시작한 세션에서 실패합니다.

* * *

## 예제 {#examples}

### 설정된 ID 확인 {#inspect-the-established-identity}

두 함수를 함께 사용하면 호출자의 보안 주체 ID와 현재 디렉터리 그룹을 확인할 수 있습니다.  멤버십은 문마다 새로 읽으므로 디렉터리를 변경한 후 이 쿼리를 실행하면 변경 내용이 세션에 표시되는지 확인할 수 있습니다.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
