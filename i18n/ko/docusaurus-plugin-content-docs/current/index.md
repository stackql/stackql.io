---
title: 클라우드 인프라, SaaS API 및 AI 에이전트를 위한 SQL
sidebar_label: StackQL에 오신 것을 환영합니다
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL은 사람과 AI 에이전트가 SQL로 클라우드 및 SaaS 서비스를 쿼리, 프로비저닝, 운영할 수 있게 하는 오픈 소스 엔진으로, 상태 파일이나 클라우드별 SDK가 필요하지 않습니다.
image: "/img/stackql-featured-image.png"
slug: /
# SoftwareApplication node on the homepage, from the site-wide defaults in
# themeConfig.structuredData.softwareApplication (name, category, OS,
# license, download URL, free offer)
softwareApplication: true
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import DocsHero from '@site/src/components/DocsHero/DocsHero';
import { FaRobot, FaCode, FaSatelliteDish } from 'react-icons/fa';

<DocsHero
  lightSrc="/img/stackql-logo-bold-light-mode.png"
  darkSrc="/img/stackql-logo-bold-dark-mode.png"
  alt="StackQL"
  title={
  <>
  에이전트 시대를 위한<br/>
  범용 인터페이스
  </>
  }
  byline={
    <>
    Configuration-as-Data. Infrastructure-as-Context.
    </>
    }
/>

---

<div className="row margin-top--lg margin-bottom--lg">
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaRobot size={22} className="homeCardIcon" />에이전트형 플랫폼 엔지니어링</h3>
      </div>
      <div className="card__body">
        에이전트와 사람이 기계의 속도로 나란히 변경 사항을 작성하는 범용 인터페이스입니다. 여러 작성자, 실시간 상태, 정책으로 제한되는 실행을 최우선 관심사로 다룹니다. 이는 자율 시스템이 안전하게 운영되는 데 필요한 기반입니다.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />에이전트 시대를 위한 IaC</h3>
      </div>
      <div className="card__body">
        코드는 여전히 의도입니다. 현실은 데이터입니다. StackQL은 에이전트와 운영자에게 하나의 선언적 인터페이스를 제공합니다. 이 인터페이스로 여러 프로바이더에 걸친 실시간 상태를 쿼리하고, 원하는 상태를 멱등적으로 보장하며, 단일 문법으로 컨트롤 플레인과 데이터 플레인을 넘나들 수 있습니다. 상태 파일도, 클라우드별 SDK도 필요하지 않습니다.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />자율 관측성</h3>
      </div>
      <div className="card__body">
        클라우드 API를 배포 대상이 아니라 데이터 계층으로 다루세요. 모든 읽기는 실시간이고 모든 변경은 이벤트이며, 드리프트는 사람이 쫓아다녀야 하는 경보가 아니라 에이전트가 추론할 수 있는 신호가 됩니다.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: '쿼리', value: 'iql', },
    { label: '결과', value: 'data', },
  ]
}>
<TabItem value="iql">

```sql
SELECT state, COUNT(*) as num_instances 
FROM aws.ec2.instances 
WHERE region = 'us-west-1' 
GROUP BY state;
```

</TabItem>
<TabItem value="data">

```
|---------------------------------|
|      state      | num_instances |
|---------------------------------|
|     running     |     342       |
|---------------------------------|
|     pending     |       4       |
|---------------------------------|
|     stopped     |      38       |
|---------------------------------|
```

</TabItem>
</Tabs>

클라우드 제공업체에 리소스를 배포하는 것은 [`INSERT`](/language-spec/insert) 문을 작성하는 것만큼 쉽습니다...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

StackQL을 사용하면 선언적 방식이든 절차적 방식이든 원하는 방식으로 개발할 수 있습니다. 문법이 배우기 쉽고 관리할 상태 파일이 없으므로 빠르게 시작할 수 있으며, 클라우드 네이티브 여부와 관계없이 다른 코드형 인프라 도구와 StackQL을 서로 바꿔 가며 사용할 수 있습니다. StackQL은 AI 에이전트가 클라우드 인프라와 상호 작용할 수 있는 범용 인터페이스를 제공합니다. StackQL의 용도는 다음과 같습니다.

- **AI 에이전트 통합**: 에이전트가 클라우드 리소스를 프로비저닝하고 쿼리할 수 있도록 지원
- **컨트롤 플레인 통신**: 에이전트형 워크플로를 위한 프로그래밍 방식의 인프라 프로비저닝
- **데이터 플레인 관측성**: AI 기반 운영을 위한 실시간 모니터링 및 분석
- 클라우드 인프라 배포(SQL 사용)
- 클라우드 자산 인벤토리 및 보고(SQL 사용)
- 클라우드 규정 준수 및 제어 증명(SQL 사용)
- 구성 드리프트 감지(SQL 사용)
- 그 밖의 다양한 용도, 한계는 상상력뿐입니다!

## StackQL의 대상 사용자 {#who-stackql-is-for}

StackQL은 클라우드 환경을 운영하는 플랫폼 엔지니어링 팀과 SRE 팀, 실시간 인벤토리, 규정 준수 및 비용에 대한 답이 필요한 보안 팀과 FinOps 팀, 클라우드 및 SaaS API를 쿼리 가능한 소스로 사용하려는 데이터 엔지니어, 그리고 클라우드 및 SaaS 리소스를 안전하게 쿼리하고 변경해야 하는 AI 에이전트를 빌드하는 개발자를 위한 도구입니다. StackQL은 작업이 이루어지는 곳 어디에서나 실행됩니다. macOS, Linux, Windows에서 단일 바이너리로, Docker에서, GitHub Actions를 사용하는 CI에서, AWS, Azure, Google Cloud 셸에서, 애플리케이션에 내장된 형태로, BI 도구와 노트북을 위한 PostgreSQL 호환 서버로, 그리고 AI 에이전트를 위한 MCP 서버로 실행됩니다.

## 라이선스 및 지원 {#license-and-support}

StackQL은 [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE)에 따른 오픈 소스이며, 어떤 용도로든 무료로 사용할 수 있습니다. 커뮤니티 지원은 [GitHub Discussions](https://github.com/orgs/stackql/discussions) 및 [Discord](https://discord.com/invite/xVXZ9d5NxN)를 통해 제공되며, 버그와 기능 요청은 [GitHub Issues](https://github.com/stackql/stackql/issues)에 등록합니다. 상업적 지원 문의는 [StackQL Studios에 문의하세요](/contact-us).

## 시작하기 {#get-started}

- [설치](/installing-stackql): macOS, Linux 또는 Windows에 StackQL 바이너리 설치, 또는 Docker, 패키지 관리자, 클라우드 셸에서 실행
- [AI 에이전트](/ai-agents): MCP를 통한 AI 에이전트 연결, 서버 내장, 쿼리 및 참조 콘텐츠 탐색
- [시작하기](/getting-started): 리소스 계층 구조, 첫 번째 쿼리, 프로바이더 사용, 출력 모드, 변수 및 템플릿 기능
- [사용 가능한 프로바이더](/providers): 쿼리하고 관리할 수 있는 클라우드 및 SaaS 프로바이더(각각 자체 참조 사이트 제공)
- [블로그](/blog): 제품 공지, 프로바이더 공지 및 튜토리얼
- [명령줄 사용법](/command-line-usage): `exec`, `shell`, `srv`, `mcp`, `registry` 및 전역 플래그
- [MCP 도구](/mcp): AI 에이전트에 동일한 SQL 인터페이스를 제공하는 Model Context Protocol 서버 및 도구
- [언어 사양](/language-spec/select): `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, 함수 및 데이터 형식
