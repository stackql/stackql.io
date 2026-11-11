# Translation glossary

Read by the `i18n-translate` skill before translating a page. The first section applies to every locale; each locale then has its own section with terminology and style.

## All locales

Never translate, transliterate or reformat:

- Product and project names: StackQL, `stackql`, `stackql-deploy`, StackQL Studios, StackQL Registry, Query Library, MCP, Model Context Protocol, OpenTelemetry, Docusaurus.
- Provider, service and resource names as they appear in StackQL (`aws`, `google`, `azure`, `github`, `aws.ec2.instances`), and the vendors' own product names (Amazon EC2, Google Cloud Storage, Microsoft Entra ID).
- SQL keywords and StackQL statements (`SELECT`, `INSERT`, `EXEC`, `SHOW`, `DESCRIBE`, `REGISTRY PULL`), CLI commands, subcommands, flags and environment variables (`stackql exec`, `--output`, `AWS_ACCESS_KEY_ID`), file and directory names, configuration keys, HTTP methods and status codes.
- Everything inside code fences, inline code, URLs, link targets, image paths, front matter keys and heading ids (`{#like-this}`).
- Author names, company names, version numbers, licence names.

Terms that are translated consistently (the locale section gives the rendering):

- provider, service, resource, method, operation
- query, statement, result set, row, column
- control plane, data plane
- agent, AI agent, tool (an MCP tool), prompt
- credentials, authentication, authorization
- deployment, resource lifecycle, infrastructure as code

## de

Audience: German-speaking engineers reading technical documentation. Formal address (Sie), present tense, short sentences. Keep English loanwords that German developer documentation uses as a matter of course; do not coin German compounds for established technical terms.

Terminology:

| English | German |
|---|---|
| provider | Provider (der) |
| service | Service (der) |
| resource | Ressource (die) |
| method | Methode (die) |
| operation | Operation (die) |
| query | Abfrage (die); "to query" = abfragen |
| statement | Anweisung (die) |
| result set | Ergebnismenge (die) |
| row / column | Zeile / Spalte |
| table | Tabelle (die) |
| view | View (die) |
| materialized view | materialisierte View |
| window function | Fensterfunktion (die) (not "Window-Funktion"; matches the sidebar category) |
| window frame | Fensterrahmen (der) |
| session | Sitzung (die); session variable = Sitzungsvariable |
| principal | Principal (der) |
| secret store | Secret-Store (der) |
| string (data type) | String (der); as a general noun "Zeichenkette" is also fine |
| integer | Ganzzahl (die) |
| whitespace | Whitespace (loanword in technical context) |
| pull (a provider) | laden; "pulled" = geladen |
| gating (MCP) | Freigabe (die) |
| control plane / data plane | Control Plane / Data Plane (unverändert) |
| agent, AI agent | Agent, KI-Agent |
| tool (MCP) | Tool (das) |
| prompt | Prompt (der) |
| credentials | Anmeldedaten (die) |
| authentication / authorization | Authentifizierung / Autorisierung |
| deployment | Deployment (das) |
| resource lifecycle | Ressourcenlebenszyklus (der) |
| infrastructure as code | Infrastructure as Code (unverändert) |
| cloud provider | Cloud-Anbieter (der) |
| command line | Kommandozeile (die) |
| shell | Shell (die) |
| server | Server (der) |
| output / output format | Ausgabe / Ausgabeformat |
| template / templating | Vorlage / Templating |
| quick start | Schnellstart (der) |
| getting started | Erste Schritte |
| installation | Installation (die) |
| release | Release (das) |
| repository | Repository (das) |
| registry | Registry (die) |
| documentation | Dokumentation (die) |
| tutorial | Tutorial (das) |
| announcement | Ankündigung (die) |
| feature | Funktion (die) |
| example | Beispiel (das) |
| note / tip / warning (admonition titles are translated by the theme) | Hinweis / Tipp / Warnung |

UI and navigation labels (navbar, footer, sidebar categories):

| English | German |
|---|---|
| Install | Installation |
| Installing StackQL | StackQL installieren |
| Getting Started | Erste Schritte |
| Command Line Usage | Kommandozeile |
| Language Specification | Sprachspezifikation |
| Functions | Funktionen |
| Developers | Entwickler |
| Providers | Provider |
| Quick Starts | Schnellstarts |
| AI Agents | KI-Agenten |
| MCP Server / MCP Tools / Embedded MCP | MCP-Server / MCP-Tools / Eingebettetes MCP |
| Blog | Blog |
| Product Announcements | Produktankündigungen |
| Provider Announcements | Provider-Ankündigungen |
| Tutorials | Tutorials |
| Documentation | Dokumentation |
| Contact us | Kontakt |
| Privacy Policy | Datenschutzerklärung |
| Terms of Service | Nutzungsbedingungen |
| Support | Support |
| AI Reference | KI-Referenz |
| More | Mehr |
| Welcome to StackQL | Willkommen bei StackQL |

Style:

- Headings in sentence case as in the English source; no title case.
- Compound nouns with a hyphen where German spelling requires it (Cloud-Anbieter, KI-Agent, MCP-Server).
- Numbers, dates and units as in the source; decimal comma is not applied inside code or tables of command output.
- The `description` front matter stays one sentence and under about 160 characters.

## zh-Hans

Audience: Chinese-speaking engineers reading technical documentation (mainland usage). Simplified characters only, never traditional forms. Plain technical-documentation register: declarative, short sentences, no marketing tone, no honorifics. Address the reader as 你 (not 您) and only where the source says "you"; prefer an impersonal sentence when it reads naturally. Keep the English terms that Chinese developers use as they are; do not invent Chinese names for products or APIs.

Names that stay in English, in addition to the "All locales" list: SQL, CLI, API, SDK, JSON, YAML, CSV, HTTP, OAuth, IaC, CI/CD, LLM, MCP, macOS, Linux, Windows, Docker, Kubernetes, GitHub Actions, Homebrew, Chocolatey, and every provider, service, resource and method name. Acronyms are not expanded or glossed unless the source does so.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Chinese | Not |
|---|---|---|
| provider (a StackQL provider) | Provider (capitalized, untranslated; no plural form) | 提供商, 提供程序, 提供者 |
| cloud provider (the vendor) | 云服务商 | 云提供商, 云厂商 |
| service | 服务 | |
| resource | 资源 | |
| method | 方法 | |
| operation | 操作 | |
| query (noun and verb) | 查询 | |
| statement | 语句 | 声明 |
| result set | 结果集 | |
| row / column | 行 / 列 | |
| table | 表 | 表格 (except an HTML or markdown table: 表格) |
| view | 视图 | |
| materialized view | 物化视图 | 实体化视图, 具体化视图 |
| window function | 窗口函数 | 开窗函数 |
| window frame | 窗口帧 | 窗口框架 |
| aggregate function | 聚合函数 | 聚集函数 |
| function / expression / clause | 函数 / 表达式 / 子句 | |
| keyword / operator / predicate | 关键字 / 运算符 / 谓词 | 关键词 (except front matter `keywords`), 操作符 |
| subquery / alias / join | 子查询 / 别名 / 连接 | |
| data type / literal / identifier | 数据类型 / 字面量 / 标识符 | |
| string / integer / boolean | 字符串 / 整数 / 布尔值 | |
| whitespace | 空白字符 | |
| parameter, argument | 参数 | |
| flag (CLI) / option | 标志 / 选项 | 旗标 |
| environment variable | 环境变量 | |
| field / key / value | 字段 / 键 / 值 | |
| schema | schema (untranslated, lower case) | 模式, 架构 |
| control plane / data plane | 控制平面 / 数据平面 | 控制面, 管控面 |
| agent, AI agent | 智能体, AI 智能体 | 代理, AI 代理 |
| tool (an MCP tool) | 工具 | |
| prompt | 提示词 | 提示 (that is "tip") |
| elicitation (MCP) | 征询（elicitation） at first mention on a page, then 征询 | 引导, 启发 |
| gating / gated (MCP) | 门控 / 受门控的; "gated by X" = 由 X 门控 | 管控, 把关 |
| approval / approve | 审批 / 批准 | |
| server mode, safety mode | 服务器模式, 安全模式 | |
| read-only | 只读 | |
| mutation / mutating statement | 变更 / 变更语句 | 突变 |
| lifecycle operation | 生命周期操作 | |
| resource lifecycle | 资源生命周期 | |
| audit log | 审计日志 | |
| credentials | 凭据 | 凭证, 证书 |
| authentication / authorization | 身份验证 / 授权 | 认证, 鉴权 |
| principal / service principal | 主体 / 服务主体 | |
| service account | 服务账号 | 服务帐户 |
| account | 账号 | 帐户, 账户 |
| token / API key / access key | 令牌 / API 密钥 / 访问密钥 | |
| secret / secret store | 机密 / 机密存储 | 秘密 |
| scope (OAuth) | 作用域 | |
| role / permission | 角色 / 权限 | |
| session / session variable | 会话 / 会话变量 | |
| deployment | 部署 | |
| infrastructure as code | 基础设施即代码 | |
| registry (generic) / provider registry | 注册表 / Provider 注册表 | 仓库 (that is "repository") |
| pull (a provider) | 拉取 | |
| repository | 仓库 | 存储库 |
| release | 发布版本 (noun), 发布 (verb) | |
| command line | 命令行 | |
| shell / interactive shell | shell / 交互式 shell | |
| server / client | 服务器 / 客户端 | 服务端 |
| transport (MCP) | 传输方式 | |
| stateless / stateful | 无状态 / 有状态 | |
| endpoint | 端点 | 终结点 |
| request / response | 请求 / 响应 | |
| payload | 负载 | |
| pagination | 分页 | |
| output / output format | 输出 / 输出格式 | |
| template / templating | 模板 / 模板化 | |
| dry run | 试运行 | |
| fan-out | 扇出 | |
| guardrail | 护栏 | |
| answer engine | 答案引擎 | |
| region / project / subscription / tenant | 区域 / 项目 / 订阅 / 租户 | 地域 |
| open source | 开源 | |
| binary / library | 二进制文件 / 库 | |
| embedded (Embedded MCP) | 嵌入式 | 内嵌 |
| installation | 安装 | |
| documentation | 文档 | |
| tutorial | 教程 | |
| quick start | 快速入门 | |
| getting started | 开始使用 | 入门 (too close to 快速入门) |
| announcement | 公告 | |
| feature | 功能 | 特性 |
| example | 示例 | 例子 |
| syntax / usage | 语法 / 用法 | |
| note / tip / warning (admonition titles are translated by the theme) | 注意 / 提示 / 警告 | |
| resource hierarchy | 资源层次结构 | 资源层级 |
| output mode | 输出模式 | |
| global flags | 全局标志 | |
| access management | 访问管理 | |
| provider session | Provider 会话 | |
| SQL backend | SQL 后端 | |
| query engine / parser | 查询引擎 / 解析器 | |
| wire protocol | 线协议 (PostgreSQL 线协议) | 有线协议 |
| reverse proxy | 反向代理 | |
| local cache | 本地缓存 | |
| request body | 请求体 | |
| key-value pair | 键值对 | |
| delimiter, separator | 分隔符 | |
| bucket (object storage) / bucket (NTILE) | 存储桶 / 桶 | |
| throw an error | 抛出错误 | |
| well-formed / valid (JSON) | 格式正确 / 有效 | |
| state file | 状态文件 | |
| drift / infrastructure drift | 漂移 / 基础设施漂移 | 偏移 |
| cloud inventory | 云资源清单 | |
| compliance | 合规 | |
| agentic infrastructure | 智能体基础设施; add （agentic infrastructure） at first mention on a page | 代理式基础设施 |
| queryable infrastructure | 可查询基础设施; add （queryable infrastructure） at first mention on a page | |
| SQL for APIs (the pattern name) | SQL for APIs (untranslated); gloss once as （面向 API 的 SQL） | |
| provision (resources) | 预配 | 置备, 供应 |
| operator (the person running StackQL) | 运维人员 | 操作员 |
| caller | 调用方 | |
| filter (noun and verb) | 过滤 (过滤条件) | 筛选 |
| concatenation | 拼接 | 连接 (that is "join") |
| parse / plan (a statement) | 解析 / 规划 | |
| relation / dereference | 关系 / 解引用 | |
| contract (I/O contract, safety contract) | 契约 | 约定 (that is "convention") |
| source of truth | 事实来源 | 权威来源 |
| inventory | 资源清单 | |
| entitlement | 权限分配 | 权益 |
| snapshot / checksum / digest | 快照 / 校验和 / 摘要 | |
| allowlist | 允许列表 | 白名单 |
| opt in / explicit opt-in | 选择启用 / 显式选择启用 | 选择加入 |
| approval prompt, elicitation prompt (a dialog shown to the user, not an LLM prompt) | 审批请求, 征询请求 | 审批提示 |
| hint (in an MCP error or tool annotation) | 提示信息 | |
| advertise (a capability) | 通告 | |
| header (HTTP) | 请求头 / 响应头; 标头 where the direction is not stated | |
| bundle (the downloadable `.mcpb`) | 安装包; the format name MCP Bundle stays in English | 捆绑包 |
| artifact (build or release output) | 制品 | 构件, 工件 |
| stack (of resources) | 堆栈 | 资源栈 |
| pipeline | 流水线 (CI/CD), 管道 (data or telemetry) | |
| notarized / signed | 经过公证 / 已签名 | |
| assume a role (AWS) | 担任角色 | 代入角色 |
| common table expression (CTE) | 公用表表达式（CTE） | |
| Kept in English | sidecar, vendored, crate, worker, bearer (bearer 令牌), dotenv, pull request, fork, issue, webhook | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Chinese |
|---|---|
| Install | 安装 |
| Installing StackQL | 安装 StackQL |
| Getting Started | 开始使用 |
| Command Line Usage | 命令行用法 |
| Language Specification | 语言规范 |
| Functions | 函数 |
| Developers | 开发者 |
| Providers | Provider |
| Quick Starts | 快速入门 |
| AI Agents | AI 智能体 |
| MCP Server / MCP Tools / Embedded MCP | MCP 服务器 / MCP 工具 / 嵌入式 MCP |
| Blog | 博客 |
| Product Announcements | 产品公告 |
| Provider Announcements | Provider 公告 |
| Tutorials | 教程 |
| Documentation | 文档 |
| Contact us | 联系我们 |
| Privacy Policy | 隐私政策 |
| Terms of Service | 服务条款 |
| Support | 支持 |
| AI Reference | AI 参考 |
| More | 更多 |
| Welcome to StackQL | 欢迎使用 StackQL |

Recurring headings, phrases and keywords (the same on every page):

| English | Chinese |
|---|---|
| Syntax | 语法 |
| Arguments | 参数 |
| Return Value(s) | 返回值 |
| Example, Examples | 示例 |
| Inputs / Output | 输入 / 输出 |
| Flags | 标志 |
| Overview | 概述 |
| How it works | 工作原理 |
| Modes | 模式 |
| Gating | 门控 |
| Install | 安装 |
| Quick start | 快速入门 |
| Demo app | 演示应用 |
| Repo link | 仓库链接 |
| Vendoring / embedding | Vendoring / 嵌入 |
| Prerequisites | 前提条件 |
| Next steps | 后续步骤 |
| Related | 相关内容 |
| See also: | 另请参阅： |
| For more information, see X. | 有关更多信息，请参阅 X。 |
| Optional. / Defaults to X. | 可选。 / 默认为 X。 |
| X function in StackQL: ... (description pattern) | StackQL 中的 X 函数：... |
| Argument / Required / Description (table headers) | 参数 / 必填 / 说明 |
| Yes / No (table cells) | 是 / 否 |
| For example | 例如 |
| Aggregate function / Scalar function | 聚合函数 / 标量函数 |
| keyword `cloud inventory` | 云资源清单 |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Punctuation in Chinese prose is full width: ，。：；？！（）and “” for quotation. Use 、 between the items of a short inline list. Everything inside code fences and inline code keeps its half-width punctuation, and a sentence that is entirely Latin text or code keeps half-width punctuation too.
- Parentheses in a Chinese sentence are full width even when they hold English: 基础设施即代码（IaC）.
- One half-width space between Chinese characters and an adjacent Latin word, number, inline code span or link whose text is Latin: 使用 `stackql exec` 运行查询, 支持 3 种模式, 在 StackQL 中. No space next to full-width punctuation: 运行 `stackql shell`。 and （见 `--output`）.
- No dash punctuation in prose: neither ——, nor a spaced hyphen. Where the source uses ` - ` as a dash inside a sentence, use ， or ： or split the sentence. A ` - ` separator in a title between two names stays.
- Headings: translate the text, no end punctuation, keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- List items and table cells end the way the source does: no 。 added to fragments, 。 where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English, optionally followed by a Chinese gloss in （）only if the source explains it.
- Numbers are Arabic numerals; versions, dates and units as in the source.
- The `description` front matter stays one sentence and under about 80 characters (count Chinese characters and Latin words alike; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud` -> 云, `infrastructure as code` -> 基础设施即代码); keep the number and order of entries.
- Bold and italic markers stay where the source has them. Put no space inside the markers (`**只读**`, not `** 只读 **`).
- A closing `**` that follows full-width punctuation and is followed directly by a Chinese character does not close in CommonMark, and the asterisks show on the page (`**适用场景：**任何` renders literally). Move the punctuation outside the markers (`**适用场景**：任何`) or leave a space after the closing marker. The same holds for an opening marker between a Chinese character and full-width punctuation.
- "See X" is 参阅 X (请参阅 in a full sentence), never 参见.
- Front matter must stay valid YAML: keep the quoting of the source value, and put a value in double quotes if the Chinese text contains a half-width `: ` or ` #`, or starts with a quote, bracket, backtick, `*`, `&`, `!`, `%` or `@`. A full-width ： needs no quoting.
- Chinese text is shorter than English, so the `length ratio` warning from `yarn i18n:verify` is expected on prose-heavy pages and can be ignored.
