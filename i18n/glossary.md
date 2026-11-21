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

## fr

Audience: French-speaking engineers reading technical documentation (international French: France, Belgium, Switzerland, Quebec, francophone Africa; no regional vocabulary). Formal address (vous), present tense, short sentences. Instructions use the imperative in the vous form ("Exécutez la commande suivante") and task headings use the infinitive ("Installer StackQL"). Keep the English terms that French developers use as a matter of course (the "Kept in English" row); do not coin French names for products or APIs. No inclusive writing with median points; no marketing tone.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | French | Not |
|---|---|---|
| provider (a StackQL provider) | provider (le; plural providers; lower case in prose) | fournisseur, fournisseur de services |
| cloud provider (the vendor) | fournisseur cloud (le) | provider cloud, fournisseur de cloud |
| service | service (le) | |
| resource | ressource (la) | |
| method | méthode (la) | |
| operation | opération (la) | |
| query (noun) / to query | requête (la) / interroger | |
| statement | instruction (l', f.) | déclaration, énoncé |
| result set | jeu de résultats (le) | ensemble de résultats |
| row / column | ligne / colonne | rangée |
| table | table (la) | tableau (except a markdown or HTML table: tableau) |
| view | vue (la) | |
| materialized view | vue matérialisée | |
| window function | fonction de fenêtrage | fonction fenêtre |
| window frame | cadre de fenêtre | |
| aggregate function / scalar function | fonction d'agrégation / fonction scalaire | fonction agrégat |
| function / expression / clause | fonction / expression / clause | |
| keyword / operator / predicate | mot-clé / opérateur / prédicat | |
| subquery / alias / join | sous-requête / alias / jointure | |
| data type / literal / identifier | type de données / littéral / identifiant | |
| string / integer / boolean | chaîne de caractères (chaîne after the first mention) / entier / booléen | string |
| whitespace | espaces blancs | |
| parameter / argument | paramètre / argument | |
| flag (CLI) / option | option / option ("global flags" = options globales) | drapeau, indicateur, flag |
| environment variable | variable d'environnement | |
| field / key / value | champ / clé / valeur | |
| schema | schéma (le) | |
| control plane / data plane | plan de contrôle / plan de données | |
| agent, AI agent | agent, agent IA (plural agents IA) | agent d'IA |
| tool (an MCP tool) | outil (l', m.) | |
| prompt (text sent to a model) | prompt (le) | invite |
| approval prompt, elicitation prompt (a dialog shown to the user) | demande d'approbation, demande d'élicitation | prompt d'approbation |
| elicitation (MCP) | élicitation (l', f.) | sollicitation |
| gating / gated (MCP) | contrôle (le) / soumis à contrôle; "gated by X" = contrôlé par X; "gated writes" = écritures soumises à approbation | verrouillage, filtrage |
| approval / approve | approbation / approuver | |
| server mode, safety mode | mode du serveur, mode de sécurité | |
| read-only | en lecture seule | |
| mutation / mutating statement | mutation / instruction de mutation | |
| lifecycle operation | opération de cycle de vie | |
| resource lifecycle | cycle de vie des ressources | |
| audit log | journal d'audit | |
| credentials | identifiants (les, m.) | informations d'identification, crédentiels |
| authentication / authorization | authentification / autorisation | |
| principal / service principal | principal / principal de service | |
| service account | compte de service | |
| token / API key / access key | jeton / clé d'API / clé d'accès | token |
| secret / secret store | secret / magasin de secrets | coffre |
| scope (OAuth) | scope (le) | étendue, portée |
| role / permission | rôle / permission | |
| session / session variable | session / variable de session | |
| deployment / to deploy | déploiement / déployer | |
| infrastructure as code | infrastructure as code (unchanged, IaC) | infrastructure en tant que code |
| registry (generic) / provider registry | registre / registre de providers | |
| pull (a provider) | télécharger; "pulled" = téléchargé | tirer, puller |
| repository | dépôt (le) | référentiel |
| release | version (la) as a noun, publier as a verb | release |
| command line | ligne de commande | |
| shell / interactive shell | shell / shell interactif | |
| server / client | serveur / client | |
| transport (MCP) | transport (le) | |
| stateless / stateful | sans état / avec état | |
| endpoint | point de terminaison | endpoint |
| request / response | requête / réponse (write "requête HTTP" or "appel d'API" where it could be read as a query) | |
| request body | corps de la requête | |
| payload | charge utile | |
| header (HTTP) | en-tête (l', m.) | |
| pagination | pagination | |
| output / output format / output mode | sortie / format de sortie / mode de sortie | |
| template / templating | modèle / templating | gabarit |
| dry run | exécution à blanc | |
| guardrail | garde-fou | |
| answer engine | moteur de réponse | |
| region / project / subscription / tenant | région / projet / abonnement / tenant | locataire |
| account | compte | |
| open source | open source | code source ouvert |
| binary / library | binaire / bibliothèque | librairie |
| embedded (Embedded MCP) | embarqué (MCP embarqué) | intégré, incorporé |
| installation / to install | installation / installer | |
| documentation | documentation | |
| tutorial | tutoriel | tutorial |
| quick start | démarrage rapide | |
| getting started | prise en main | premiers pas |
| announcement | annonce | |
| feature | fonctionnalité | caractéristique, feature |
| example | exemple | |
| syntax / usage | syntaxe / utilisation | usage |
| note / tip / warning (admonition titles are translated by the theme) | remarque / astuce / avertissement | |
| resource hierarchy | hiérarchie des ressources | |
| access management | gestion des accès | |
| provider session | session de provider | |
| SQL backend | backend SQL | |
| query engine / parser | moteur de requêtes / analyseur syntaxique | |
| wire protocol | protocole filaire (protocole filaire PostgreSQL) | |
| reverse proxy | proxy inverse | |
| local cache | cache local | |
| key-value pair | paire clé-valeur | |
| delimiter / separator | délimiteur / séparateur | |
| bucket (object storage) / bucket (NTILE) | bucket / groupe | compartiment, seau |
| throw an error / return an error | lever une erreur / renvoyer une erreur | jeter |
| well-formed / valid (JSON) | bien formé / valide | |
| state file | fichier d'état | |
| drift / infrastructure drift | dérive / dérive d'infrastructure | |
| cloud inventory, inventory | inventaire cloud, inventaire | |
| compliance | conformité | |
| provision (resources) | provisionner | approvisionner |
| operator (the person running StackQL) | exploitant (l', m.) | opérateur (that is a SQL operator) |
| caller | appelant (l', m.) | |
| filter (noun and verb) | filtre / filtrer | |
| concatenation | concaténation | |
| parse / plan (a statement) | analyser / planifier | parser |
| contract (I/O contract, safety contract) | contrat | |
| source of truth | source de vérité | |
| entitlement | droit d'accès | |
| snapshot / checksum / digest | instantané / somme de contrôle / empreinte | |
| allowlist | liste d'autorisation | liste blanche |
| opt in / explicit opt-in | activer explicitement / activation explicite | |
| hint (in an MCP error or tool annotation) | indication | |
| advertise (a capability) | annoncer | |
| artifact (build or release output) | artefact | |
| stack (of resources) | stack (la) | pile |
| pipeline | pipeline (le) | |
| notarized / signed | notarisé / signé | |
| assume a role (AWS) | assumer un rôle | endosser |
| common table expression (CTE) | expression de table commune (CTE) | |
| default / by default | valeur par défaut / par défaut | |
| array (JSON, data type) | tableau (tableau JSON) | |
| policy (IAM, access policy) / IAM binding | politique / liaison IAM | stratégie |
| bind, binding (alias, secret store) | lier, liaison | |
| assign, assignment (`SET`) | affecter, affectation | |
| scope (of a variable or a setting, not OAuth) | portée | |
| token (lexical) | unité lexicale | jeton (that is an auth token) |
| pin (a version, a key, an issuer) | épingler; the heading "Pinning the revision" is "Fixer la révision" | |
| directory (of principals, of plugins) / directory (filesystem) | annuaire / répertoire | |
| log record / completion record / row record (OTel output) | enregistrement de journal / enregistrement de fin / enregistrement de ligne | |
| exporter / receiver (OTel) / sink (audit) | exportateur / récepteur / destination | |
| estate / fingerprint | parc / empreinte | |
| tenancy (the alias label) | tenancy (la) | |
| preview (a feature in preview) | en préversion | |
| legacy | hérité | |
| batch / statement batch | lot / lot d'instructions | |
| wildcard | caractère générique | |
| query hint | indication de requête | indicateur de requête |
| substring / pattern (regex) | sous-chaîne / motif | |
| collating function / collating sequence | fonction de collation / séquence de collation | classement (that is ranking) |
| timestamp | horodatage | |
| current row / rank / running total (window functions) | ligne courante / rang / total cumulé | |
| installer | programme d'installation | |
| workspace | espace de travail | |
| framework | framework for a named framework (Framework de liaisons IAM); cadre in the generic sense | |
| StackQL Provider Registry (link text to `/providers`) | unchanged, it is a name | Registre de providers StackQL |
| Kept in English | backend, bundle (the `.mcpb`; the format name MCP Bundle too), sidecar, vendored, crate, worker, bearer (jeton bearer), dotenv, pull request, fork, issue, webhook, plugin, runtime, fan-out, proxy, cache, cloud, SaaS, wrapper, skill, build, workflow, tag, handshake | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | French |
|---|---|
| Install | Installation |
| Installing StackQL | Installer StackQL |
| Getting Started | Prise en main |
| Command Line Usage | Ligne de commande |
| Language Specification | Spécification du langage |
| Functions | Fonctions |
| Developers | Développeurs |
| Providers | Providers |
| Quick Starts | Démarrages rapides |
| AI Agents | Agents IA |
| MCP Server / MCP Tools / Embedded MCP | Serveur MCP / Outils MCP / MCP embarqué |
| Blog | Blog |
| Product Announcements | Annonces produit |
| Provider Announcements | Annonces providers |
| Tutorials | Tutoriels |
| Documentation | Documentation |
| Contact us | Nous contacter |
| Privacy Policy | Politique de confidentialité |
| Terms of Service | Conditions d'utilisation |
| Support | Support |
| AI Reference | Référence IA |
| More | Plus |
| Welcome to StackQL | Bienvenue dans StackQL |

Recurring headings, phrases and keywords (the same on every page):

| English | French |
|---|---|
| Syntax | Syntaxe |
| Arguments | Arguments |
| Return Value(s) | Valeur(s) renvoyée(s) |
| Example / Examples | Exemple / Exemples |
| Inputs / Output | Entrées / Sortie |
| Flags | Options |
| Overview | Vue d'ensemble |
| How it works | Fonctionnement |
| Modes | Modes |
| Gating | Contrôle |
| Install | Installation |
| Quick start | Démarrage rapide |
| Demo app | Application de démonstration |
| Repo link | Lien du dépôt |
| Vendoring / embedding | Vendoring / intégration |
| Prerequisites | Prérequis |
| Next steps | Étapes suivantes |
| Related | Voir aussi |
| See also: | Voir aussi : |
| See X. | Consultez X. |
| For more information, see X. | Pour plus d'informations, consultez X. |
| Optional. / Defaults to X. | Facultatif. / Valeur par défaut : X. |
| Returns X. (the sentence that opens a function or tool page) | Renvoie X. (never "Retourne") |
| Return the absolute value of a number (an example heading that names a task) | Renvoyer la valeur absolue d'un nombre (infinitive) |
| X function in StackQL: ... (description pattern) | Fonction X dans StackQL : ... (an infinitive after the colon: "renvoyer", "calculer") |
| X window function in StackQL: ... (description pattern) | Fonction de fenêtrage X dans StackQL : ... |
| X in StackQL: ... (description of a statement page) | X dans StackQL : ... (an infinitive after the colon) |
| ... using SQL in StackQL (description pattern) | ... avec SQL dans StackQL |
| Available in StackQL `vX` and later / releases from `vX` | Disponible à partir de StackQL `vX` |
| Available in StackQL releases after `vX` | Disponible dans les versions de StackQL postérieures à `vX` |
| None. (no arguments, no inputs) | Aucun. / Aucune. |
| Query / Results (tab labels), Output: | Requête / Résultats, Sortie : |
| Tested with (badge line on quick starts) | Testé avec |
| Best Practices / Troubleshooting / Further reading | Bonnes pratiques / Dépannage / Pour aller plus loin |
| MCP tool listing X / MCP tool that does X (description pattern) | Outil MCP qui liste X / Outil MCP qui fait X |
| MCP overview (link text) | Présentation de MCP |
| Argument / Required / Description (table headers) | Argument / Obligatoire / Description |
| Renderer / Shape (table headers) | Rendu / Forme |
| Table / One row per X (table cells) | Tableau / Une ligne par X |
| Yes / No (table cells) | Oui / Non |
| For example | Par exemple |
| Aggregate function / Scalar function | Fonction d'agrégation / Fonction scalaire |
| keyword `cloud inventory` | inventaire cloud |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Headings in sentence case: a capital on the first word and on proper nouns only, never title case ("Variables de session", not "Variables De Session"). No end punctuation. Keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- One space before and after `:`, and one space before `;`, `?` and `!`, as French typography requires ("Par exemple : ..."). Use an ordinary space; never a non-breaking space character (U+00A0, U+202F) in the source. No space is added inside code, URLs, front matter keys or admonition markers.
- Quotation marks are straight double quotes ("..."), the apostrophe is the straight one (`'`, U+0027). No guillemets and no typographic apostrophe, so the text stays greppable and the quoting rules below stay simple.
- No dash punctuation other than the keyboard hyphen. Where the source uses ` - ` or ` -- ` as a dash inside a sentence or between a term and its explanation, write ` - `. Never write an em dash, an en dash or a double hyphen in prose.
- Elision and gender follow the French noun even when the term is English: le provider, la stack, l'outil, le shell, le backend. An inline code span takes the gender of what it names (la commande `stackql exec`, le paramètre `--output`).
- No capital on common nouns after the first word of a sentence or label; names of languages, months and days are lower case. Product, provider and vendor names keep their own spelling.
- List items and table cells end the way the source does: no full stop added to fragments, a full stop where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English.
- Numbers, dates, versions and units as in the source; the decimal comma is not applied inside code or tables of command output.
- The `description` front matter stays one sentence and under about 170 characters (French runs 15 to 20 percent longer than English; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud inventory` -> inventaire cloud); keep the number and order of entries.
- Front matter must stay valid YAML, and French breaks it in two ways. A value that contains ` : ` (the French colon has a space on both sides) or ` #` must be in double quotes. A value in single quotes cannot hold an apostrophe: put it in double quotes instead (`title: "Vue d'ensemble"`), escaping any inner double quote as `\"`. A plain unquoted value may contain apostrophes (`title: Vue d'ensemble`) as long as it does not start with one.
- The same holds for strings in JSX and in exported data arrays: a string prop or JavaScript string in single quotes that gains an apostrophe goes in double quotes (`label="Prise en main"`, `description: "L'exemple suivant"`).
- Bold and italic markers stay where the source has them, with no space inside the markers. Punctuation that follows a bold term stays outside the markers when the source has it outside.
- Link text that is another page's title is that page's translated `title`, including the "See also" badges that wrap the title in inline code (`` [[` Window Functions `]] `` becomes `` [[` Fonctions de fenêtrage `]] ``); `yarn i18n:verify` warns about the changed inline code on those pages (`select.md`, `windowing_functions.md`, `otel-recipes.md`), which is expected. Link text that is a heading of the target page is that heading's translation.
- Accented capitals and the œ ligature are ordinary French spelling and are kept (Étapes, nœud).
- Example prompts addressed to an AI assistant use the informal imperative ("Montre-moi", "Crée"); the reader is still addressed as vous.
- French text is longer than English, so a `length ratio` warning from `yarn i18n:verify` is not expected; if one appears, check for an omission or an addition.

## es

Audience: Spanish-speaking engineers reading technical documentation, in Spain and in Latin America alike. Neutral international Spanish: where the two sides of the Atlantic use different words, take the one in the table below (archivo, equipo, encabezado), never a regional one (fichero, ordenador, computadora, cabecera). Formal address (usted), present tense, short sentences. Instructions use the imperative in the usted form ("Ejecute el siguiente comando", "Consulte", "Use") and task headings use the infinitive ("Instalar StackQL"). The reader is always one person: no vosotros and no ustedes forms. Keep the English terms that Spanish-speaking developers use as a matter of course (the "Kept in English" row); do not coin Spanish names for products or APIs. No inclusive-writing forms (`@`, `x`, `e` endings); no marketing tone.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Spanish | Not |
|---|---|---|
| provider (a StackQL provider) | provider (el; plural providers; lower case in prose) | proveedor, proveedor de servicios |
| cloud provider (the vendor) | proveedor de nube (el) | proveedor cloud, proveedor de la nube, provider de nube |
| vendor (the company behind an API or SDK, not only cloud) | proveedor (el); "vendor-published" = publicado por el proveedor | fabricante, vendedor |
| cloud (as an adjective: cloud resources, cloud infrastructure) | en la nube (recursos en la nube, infraestructura en la nube); "de nube" in a fixed term from this table | cloud as a Spanish adjective (recursos cloud) |
| service | servicio (el) | |
| resource | recurso (el) | |
| method | método (el) | |
| operation | operación (la) | |
| query (noun) / to query | consulta (la) / consultar | query, interrogar |
| statement | instrucción (la) | sentencia, declaración, enunciado |
| result set | conjunto de resultados | juego de resultados |
| row / column | fila / columna | renglón, registro (for a row) |
| table | tabla (la) | |
| view | vista (la) | |
| materialized view | vista materializada | |
| window function | función de ventana | función ventana, función analítica |
| window frame | marco de ventana | |
| aggregate function / scalar function | función de agregación / función escalar | función agregada, función de agregado |
| function / expression / clause | función / expresión / cláusula | |
| keyword / operator / predicate | palabra clave / operador / predicado | |
| subquery / alias / join | subconsulta / alias / join (el; `JOIN` where the source has code) | unión (that is `UNION`), combinación, juntura |
| data type / literal / identifier | tipo de datos / literal / identificador | |
| string / integer / boolean | cadena (cadena de caracteres where "cadena" alone would be unclear) / entero / booleano | string, hilera |
| whitespace | espacios en blanco | |
| parameter / argument | parámetro / argumento | |
| flag (CLI) / option | opción / opción ("global flags" = opciones globales) | bandera, indicador, marca, flag |
| environment variable | variable de entorno | variable de ambiente |
| field / key / value | campo / clave / valor | llave |
| schema | esquema (el) | |
| control plane / data plane | plano de control / plano de datos | |
| agent, AI agent | agente, agente de IA (plural agentes de IA) | agente IA, agente de inteligencia artificial |
| tool (an MCP tool) | herramienta (la) | |
| prompt (text sent to a model) | prompt (el) | indicación, instrucción, mensaje |
| approval prompt, elicitation prompt (a dialog shown to the user) | solicitud de aprobación, solicitud de elicitación | prompt de aprobación |
| elicitation (MCP) | elicitación (la) | obtención, solicitud |
| gating / gated (MCP) | control (el) / sujeto a control; "gated by X" = controlado por X; "gated writes" = escrituras sujetas a aprobación | bloqueo, filtrado, compuerta |
| approval / approve | aprobación / aprobar | |
| server mode, safety mode | modo del servidor, modo de seguridad | |
| read-only | de solo lectura | de sólo lectura (no accent on solo) |
| mutation / mutating statement | mutación / instrucción de mutación | |
| lifecycle operation | operación de ciclo de vida | |
| resource lifecycle | ciclo de vida de los recursos | |
| log / audit log | log (el; plural logs) / log de auditoría | registro (that is a registry or a record), bitácora |
| log record / completion record / row record (OTel output) | registro de log / registro de finalización / registro de fila | |
| credentials | credenciales (las) | |
| authentication / authorization | autenticación / autorización | autentificación |
| principal / service principal | principal (el) / principal de servicio | entidad de seguridad, entidad de servicio |
| service account | cuenta de servicio | |
| token / API key / access key | token (el; plural tokens) / clave de API / clave de acceso | ficha, testigo, llave |
| secret / secret store | secreto / almacén de secretos | bóveda |
| scope (OAuth) | scope (el) | ámbito (that is the scope of a variable), alcance |
| role / permission | rol / permiso | papel |
| session / session variable | sesión / variable de sesión | |
| deployment / to deploy | despliegue / desplegar | implementación, implementar, deploy |
| infrastructure as code | infraestructura como código (IaC) | infraestructura en forma de código |
| registry (generic) / provider registry | registro / registro de providers | |
| pull (a provider) | descargar; "pulled" = descargado | tirar, jalar, hacer pull |
| repository | repositorio (el) | |
| release | versión (la) as a noun, publicar as a verb | release, lanzamiento |
| command line | línea de comandos | línea de órdenes |
| shell / interactive shell | shell (el) / shell interactivo | la shell |
| server / client | servidor / cliente | |
| transport (MCP) | transporte (el) | |
| stateless / stateful | sin estado / con estado | |
| endpoint | endpoint (el; plural endpoints) | punto de conexión, punto final, extremo |
| request / response | solicitud / respuesta | petición, requerimiento |
| request body | cuerpo de la solicitud | |
| payload | carga útil | |
| header (HTTP) | encabezado (el) | cabecera |
| pagination | paginación | |
| output / output format / output mode | salida / formato de salida / modo de salida | |
| template / templating | plantilla / uso de plantillas | template |
| dry run | ejecución de prueba | simulacro, ejecución en seco |
| guardrail | barrera de protección | barandilla |
| answer engine | motor de respuestas | |
| region / project / subscription / tenant | región / proyecto / suscripción / tenant (el) | inquilino, arrendatario |
| account | cuenta | |
| open source | de código abierto | open source, de fuente abierta |
| binary / library | binario / biblioteca | librería |
| embedded (Embedded MCP) / to embed | integrado (MCP integrado) / integrar | embebido, incrustado, empotrado |
| installation / to install | instalación / instalar | |
| documentation | documentación | |
| tutorial | tutorial (el) | |
| quick start | inicio rápido | |
| getting started | primeros pasos | |
| announcement | anuncio | |
| feature | funcionalidad | característica, prestación, feature |
| example | ejemplo | |
| syntax / usage | sintaxis / uso | |
| note / tip / warning (admonition titles are translated by the theme) | nota / consejo / advertencia | |
| resource hierarchy | jerarquía de recursos | |
| access management | gestión de accesos | administración de acceso |
| provider session | sesión de provider | |
| SQL backend | backend SQL | |
| query engine / parser | motor de consultas / analizador sintáctico | parser, parseador |
| wire protocol | protocolo de red (protocolo de red de PostgreSQL) | protocolo de cable |
| reverse proxy | proxy inverso | |
| local cache | caché local (la caché) | el caché |
| key-value pair | par clave-valor | |
| delimiter / separator | delimitador / separador | |
| bucket (object storage) / bucket (NTILE) | bucket (el) / grupo | cubo, depósito |
| throw an error / return an error | generar un error / devolver un error | lanzar, arrojar, tirar |
| return (a value) | devolver | retornar, regresar |
| well-formed / valid (JSON) | bien formado / válido | |
| file / state file | archivo / archivo de estado | fichero |
| directory (of principals, of plugins, filesystem) / folder | directorio / carpeta | |
| computer, machine | equipo, máquina | ordenador, computadora |
| drift / infrastructure drift | desviación / desviación de la infraestructura | deriva, drift |
| cloud inventory, inventory | inventario de nube, inventario | |
| compliance | cumplimiento | conformidad |
| provision (resources) | aprovisionar | provisionar |
| operator (the person running StackQL) | operador (el); the context separates it from a SQL operator | |
| caller | llamador (el) | invocador, autor de la llamada |
| filter (noun and verb) | filtro / filtrar | |
| concatenation | concatenación | |
| parse / plan (a statement) | analizar / planificar | parsear |
| contract (I/O contract, safety contract) | contrato | |
| source of truth | fuente de verdad | |
| entitlement | derecho de acceso | |
| snapshot / checksum / digest | instantánea / suma de comprobación / hash (el) | resumen (that is a summary) |
| estate / fingerprint | parque (parque de recursos en la nube) / huella digital | patrimonio |
| allowlist | lista de permitidos | lista blanca |
| opt in / explicit opt-in | activar explícitamente / activación explícita | |
| enable / disable | habilitar / deshabilitar | |
| support (a feature, a format) / supported | admitir / admitido, compatible | soportar, soportado |
| hint (in an MCP error or tool annotation) | indicación | pista |
| advertise (a capability) | anunciar | |
| artifact (build or release output) | artefacto | |
| stack (of resources) | stack (el) | pila |
| pipeline | pipeline (el) | canalización, tubería |
| notarized / signed | notarizado / firmado | |
| assume a role (AWS) | asumir un rol | |
| common table expression (CTE) | expresión de tabla común (CTE) | |
| default / by default | valor predeterminado / de forma predeterminada | por defecto, por omisión |
| array (JSON, data type) | array (el; plural arrays) | matriz, arreglo, vector |
| policy (IAM, access policy) / IAM binding | política / vinculación de IAM | directiva, binding |
| bind, binding (alias, secret store) | vincular, vinculación | |
| assign, assignment (`SET`) | asignar, asignación | |
| scope (of a variable or a setting, not OAuth) | ámbito | alcance |
| token (lexical) | componente léxico | token (that is an auth token) |
| pin (a version, a key, an issuer) | fijar; the heading "Pinning the revision" is "Fijar la revisión" | anclar, pinear |
| exporter / receiver (OTel) / sink (audit) | exportador / receptor / destino | sumidero |
| tenancy (the alias label) | tenancy (la) | |
| preview (a feature in preview) | en versión preliminar | en vista previa, preview |
| legacy | heredado | legado, legacy |
| batch / statement batch | lote / lote de instrucciones | |
| wildcard | comodín (carácter comodín) | |
| query hint | indicación de consulta | sugerencia de consulta |
| substring / pattern (regex) / regular expression | subcadena / patrón / expresión regular | |
| collating function / collating sequence | función de intercalación / secuencia de intercalación | colación, cotejamiento |
| timestamp | marca de tiempo | sello de tiempo, timestamp |
| current row / rank / running total (window functions) | fila actual / rango / total acumulado | |
| installer | instalador | |
| workspace | espacio de trabajo | área de trabajo |
| monitoring / to monitor / monitor (noun) | supervisión / supervisar / monitor | monitorización, monitoreo, monitorear |
| run, execute / list / delete, remove / manage | ejecutar / enumerar / eliminar / gestionar | correr / listar / borrar / administrar |
| framework | framework (el) for a named framework (Framework de vinculaciones de IAM); marco in the generic sense | |
| StackQL Provider Registry (link text to `/providers`) | unchanged, it is a name | Registro de providers de StackQL |
| Kept in English | backend, bundle (the `.mcpb`; the format name MCP Bundle too), sidecar, vendored, crate, worker, bearer (token bearer), dotenv, pull request, fork, issue, webhook, plugin, runtime, fan-out, proxy, SaaS, wrapper, skill, build, workflow, tag, handshake, log, hash | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Spanish |
|---|---|
| Install | Instalación |
| Installing StackQL | Instalar StackQL |
| Getting Started | Primeros pasos |
| Command Line Usage | Línea de comandos |
| Language Specification | Especificación del lenguaje |
| Functions | Funciones |
| Developers | Desarrolladores |
| Providers | Providers |
| Quick Starts | Inicios rápidos |
| AI Agents | Agentes de IA |
| MCP Server / MCP Tools / Embedded MCP | Servidor MCP / Herramientas MCP / MCP integrado |
| Blog | Blog |
| Product Announcements | Anuncios de producto |
| Provider Announcements | Anuncios de providers |
| Tutorials | Tutoriales |
| Documentation | Documentación |
| Contact us | Contacto |
| Privacy Policy | Política de privacidad |
| Terms of Service | Términos del servicio |
| Support | Soporte |
| AI Reference | Referencia de IA |
| More | Más |
| Welcome to StackQL | Le damos la bienvenida a StackQL |

Recurring headings, phrases and keywords (the same on every page):

| English | Spanish |
|---|---|
| Syntax | Sintaxis |
| Arguments | Argumentos |
| Return Value(s) | Valor(es) devuelto(s) |
| Example / Examples | Ejemplo / Ejemplos |
| Inputs / Output | Entradas / Salida |
| Flags | Opciones |
| Overview | Descripción general |
| How it works | Funcionamiento |
| Modes | Modos |
| Gating | Control |
| Install | Instalación |
| Quick start | Inicio rápido |
| Demo app | Aplicación de demostración |
| Repo link | Enlace al repositorio |
| Vendoring / embedding | Vendoring / integración |
| Prerequisites | Requisitos previos |
| Next steps | Pasos siguientes |
| Related | Consulte también |
| See also: | Consulte también: |
| See X. | Consulte X. |
| For more information, see X. | Para obtener más información, consulte X. |
| Optional. / Defaults to X. | Opcional. / Valor predeterminado: X. |
| Returns X. (the sentence that opens a function or tool page) | Devuelve X. (never "Retorna" or "Regresa") |
| Return the absolute value of a number (an example heading that names a task) | Devolver el valor absoluto de un número (infinitive) |
| X function in StackQL: ... (description pattern) | Función X en StackQL: ... (a noun phrase or an infinitive after the colon, as in the source) |
| X window function in StackQL: ... (description pattern) | Función de ventana X en StackQL: ... |
| X in StackQL: ... (description of a statement page) | X en StackQL: ... |
| ... using SQL in StackQL (description pattern) | ... con SQL en StackQL |
| Available in StackQL `vX` and later / releases from `vX` | Disponible a partir de StackQL `vX` |
| Available in StackQL releases after `vX` | Disponible en las versiones de StackQL posteriores a `vX` |
| None. (no arguments, no inputs) | Ninguno. / Ninguna. |
| Query / Results (tab labels), Output: | Consulta / Resultados, Salida: |
| Tested with (badge line on quick starts) | Probado con |
| Best Practices / Troubleshooting / Further reading | Prácticas recomendadas / Solución de problemas / Lecturas adicionales |
| MCP tool listing X / MCP tool that does X (description pattern) | Herramienta MCP que enumera X / Herramienta MCP que hace X |
| MCP overview (link text) | Descripción general de MCP |
| Argument / Required / Description (table headers) | Argumento / Obligatorio / Descripción |
| Renderer / Shape (table headers) | Renderizador / Forma |
| Table / One row per X (table cells) | Tabla / Una fila por X |
| Yes / No (table cells) | Sí / No |
| For example | Por ejemplo |
| Aggregate function / Scalar function | Función de agregación / Función escalar |
| keyword `cloud inventory` | inventario de nube |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Headings in sentence case: a capital on the first word and on proper nouns only, never title case ("Variables de sesión", not "Variables De Sesión"). No end punctuation. Keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- Questions and exclamations open with `¿` and `¡` as Spanish spelling requires, including in headings, FAQ entries and front matter. Accented vowels, `ñ` and `ü` are ordinary Spanish spelling and are kept, on capitals too (Índice, Único).
- No space before `:`, `;`, `?` or `!`. The word after a colon is lower case unless it is a name or starts a quoted sentence.
- Quotation marks are straight double quotes ("..."). No angle quotes, no typographic quotes and no typographic apostrophe, so the text stays greppable.
- No dash punctuation other than the keyboard hyphen. Where the source uses ` - ` or ` -- ` as a dash inside a sentence or between a term and its explanation, write ` - `, or use commas or parentheses where Spanish would use a raya. Never write an em dash (the raya), an en dash or a double hyphen in prose.
- Gender and number follow Spanish usage even when the term is English: el provider, el stack, el shell, el backend, el bucket, el endpoint, el token, el prompt, el pipeline, el array, el join, el tenant, el log, el hash; la caché, la API, la CLI, la URL, la tenancy. An inline code span takes the gender of what it names (el comando `stackql exec`, la opción `--output`, el parámetro `region`). English nouns form the plural with -s (providers, tokens, endpoints).
- Contractions `al` and `del` apply before an English or code term as before any other masculine noun (del provider, al endpoint); not before a name that starts with its own article.
- No capital on common nouns after the first word of a sentence or label; names of languages, months and days are lower case. Product, provider and vendor names keep their own spelling.
- Prefer the simple verb to the padded one (use, not "haga uso de"; para, not "con el fin de"). Prefer the active voice or the impersonal `se` ("se devuelve una fila") to the passive with `ser`.
- List items and table cells end the way the source does: no full stop added to fragments, a full stop where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English.
- Numbers, dates, versions and units as in the source; the decimal comma is not applied inside code or tables of command output.
- The `description` front matter stays one sentence and under about 170 characters (Spanish runs 15 to 25 percent longer than English; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud inventory` -> inventario de nube); keep the number and order of entries.
- Front matter must stay valid YAML. Keep the quoting of the source value. A value that contains `: ` (a colon followed by a space) or ` #` must be in double quotes, as must a value that starts with a quote, bracket, backtick, `*`, `&`, `!`, `%` or `@`; escape any inner double quote as `\"`. A value that starts with `¿` or `¡` needs no quoting.
- Strings in JSX and in exported data arrays keep the quote style of the source; a translated string that gains a double quote goes in single quotes, and the other way around.
- Bold and italic markers stay where the source has them, with no space inside the markers. Punctuation that follows a bold term stays outside the markers when the source has it outside.
- Link text that is another page's title is that page's translated `title`, including the "See also" badges that wrap the title in inline code (`` [[` Window Functions `]] `` becomes `` [[` Funciones de ventana `]] ``); `yarn i18n:verify` warns about the changed inline code on those pages, which is expected. Link text that is a heading of the target page is that heading's translation.
- Example prompts addressed to an AI assistant use the informal imperative ("Muéstrame", "Crea", "Enumera"); the reader is still addressed as usted.
- Spanish text is longer than English, so a `length ratio` warning from `yarn i18n:verify` is not expected; if one appears, check for an omission or an addition.

## ja

Audience: Japanese-speaking engineers reading technical documentation. Polite style (です・ます調) in every prose sentence; never the plain style (だ・である調) in prose. No honorifics beyond that (no ご覧ください, いただけます, させていただきます): the register is that of vendor reference documentation, plain and declarative. Do not write あなた: leave the subject out where the source says "you", as Japanese does. Short sentences; split an English sentence that would need a long chain of の or a nested relative clause. Keep the English terms that Japanese developers use as they are; do not invent Japanese names for products or APIs.

Names that stay in Latin script, in addition to the "All locales" list: SQL, CLI, API, SDK, JSON, YAML, CSV, HTTP, OAuth, IaC, CI/CD, LLM, MCP, SaaS, OTel, macOS, Linux, Windows, Docker, Kubernetes, GitHub Actions, Homebrew, Chocolatey, Webhook, and every provider, service, resource and method name. Acronyms are not expanded or glossed unless the source does so.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Japanese | Not |
|---|---|---|
| provider (a StackQL provider) | プロバイダー | プロバイダ, 提供者, Provider |
| cloud provider (the vendor) | クラウドベンダー | クラウドプロバイダー, クラウド事業者 |
| vendor (the company behind an API or SDK) | ベンダー | 業者 |
| service | サービス | |
| resource | リソース | 資源 |
| method | メソッド | |
| operation | 操作 | オペレーション |
| query (noun) / to query | クエリ / クエリする, クエリを実行する | クエリー, 照会, 問い合わせ |
| statement | 文 (SQL 文, `SELECT` 文) | ステートメント, 命令, 宣言 |
| result set | 結果セット | |
| row / column | 行 / 列 | レコード (for a row), カラム |
| table | テーブル (an HTML or markdown table: 表) | |
| view | ビュー | |
| materialized view | マテリアライズドビュー | 実体化ビュー, マテリアライズド・ビュー |
| window function | ウィンドウ関数 | 分析関数 |
| window frame | ウィンドウフレーム | |
| aggregate function / scalar function | 集約関数 / スカラー関数 | 集計関数, 集合関数 |
| function / expression / clause | 関数 / 式 / 句 | |
| keyword / operator / predicate | キーワード / 演算子 / 述語 | オペレーター (for an operator) |
| subquery / alias / join | サブクエリ / エイリアス / 結合 (`JOIN` where the source has code) | 副問い合わせ, 別名 |
| data type / literal / identifier | データ型 / リテラル / 識別子 | |
| string / integer / boolean | 文字列 / 整数 / ブール値 | ストリング |
| whitespace | 空白文字 | ホワイトスペース |
| parameter / argument | パラメーター / 引数 | パラメータ |
| flag (CLI) / option | フラグ / オプション ("global flags" = グローバルフラグ) | |
| environment variable | 環境変数 | |
| field / key / value | フィールド / キー / 値 | |
| schema | スキーマ | |
| control plane / data plane | コントロールプレーン / データプレーン | 制御プレーン |
| agent, AI agent | エージェント, AI エージェント | 代理人 |
| tool (an MCP tool) | ツール | |
| prompt (text sent to a model) | プロンプト | |
| approval prompt, elicitation prompt (a dialog shown to the user) | 承認リクエスト, エリシテーションリクエスト | 承認プロンプト |
| elicitation (MCP) | エリシテーション（elicitation） at first mention on a page, then エリシテーション | 誘導, 引き出し |
| gating / gated (MCP) | ゲート制御 / ゲート制御の対象; "gated by X" = X によってゲート制御される; "gated writes" = 承認が必要な書き込み | ゲーティング |
| approval / approve | 承認 / 承認する | |
| server mode, safety mode | サーバーモード, 安全モード | セーフティモード |
| read-only | 読み取り専用 | リードオンリー |
| mutation / mutating statement | ミューテーション / ミューテーション文 | 変異, 突然変異 |
| lifecycle operation | ライフサイクル操作 | |
| resource lifecycle | リソースライフサイクル | |
| log / audit log | ログ / 監査ログ | |
| credentials | 認証情報 | クレデンシャル, 資格情報 |
| authentication / authorization | 認証 / 認可 | 承認 (that is "approval") |
| principal / service principal | プリンシパル / サービスプリンシパル | |
| service account | サービスアカウント | |
| account | アカウント | |
| token / API key / access key | トークン / API キー / アクセスキー | |
| secret / secret store | シークレット / シークレットストア | 秘密 |
| scope (OAuth, or of a variable or a setting) | スコープ | |
| role / permission | ロール / 権限 (役割 is right for what a component does, not for an IAM role) | 役割 (for an IAM role), パーミッション, アクセス許可 |
| session / session variable | セッション / セッション変数 | |
| deployment / to deploy | デプロイ / デプロイする | 展開, 配備 |
| infrastructure as code | Infrastructure as Code（IaC） | コードとしてのインフラ |
| registry (generic) / provider registry | レジストリ / プロバイダーレジストリ | |
| pull (a provider) | プルする; "pulled" = プル済み | 引く |
| repository | リポジトリ | レポジトリ |
| release | リリース | |
| command line | コマンドライン | |
| shell / interactive shell | シェル / 対話型シェル | インタラクティブシェル |
| server / client | サーバー / クライアント | サーバ |
| transport (MCP) | トランスポート | |
| stateless / stateful | ステートレス / ステートフル | |
| endpoint | エンドポイント | |
| request / response | リクエスト / レスポンス | 要求, 応答 |
| request body | リクエストボディ | |
| payload | ペイロード | |
| header (HTTP) | ヘッダー | ヘッダ |
| pagination | ページネーション | ページング |
| output / output format / output mode | 出力 / 出力形式 / 出力モード | |
| template / templating | テンプレート / テンプレート機能 | |
| dry run | ドライラン | |
| fan-out | ファンアウト | |
| guardrail | ガードレール | |
| answer engine | アンサーエンジン | 回答エンジン |
| region / project / subscription / tenant | リージョン / プロジェクト / サブスクリプション / テナント | 地域 |
| open source | オープンソース | |
| binary / library | バイナリ / ライブラリ | |
| embedded (Embedded MCP) / to embed | 組み込み (組み込み MCP) / 組み込む | 埋め込み, エンベデッド |
| installation / to install | インストール / インストールする | |
| documentation | ドキュメント | ドキュメンテーション |
| tutorial | チュートリアル | |
| quick start | クイックスタート | |
| getting started | はじめに | 入門, スタートガイド |
| announcement | お知らせ | 発表 |
| feature | 機能 | 特徴, フィーチャー |
| example | 例 | サンプル (except "sample app", "sample data") |
| syntax / usage | 構文 / 使用方法 | |
| note / tip / warning (admonition titles are translated by the theme) | 注記 / ヒント / 警告 | |
| resource hierarchy | リソース階層 | |
| access management | アクセス管理 | |
| provider session | プロバイダーセッション | |
| SQL backend | SQL バックエンド | |
| query engine / parser | クエリエンジン / パーサー | |
| wire protocol | ワイヤプロトコル (PostgreSQL ワイヤプロトコル) | 有線プロトコル |
| reverse proxy | リバースプロキシ | |
| local cache | ローカルキャッシュ | |
| key-value pair | キーと値のペア | |
| delimiter / separator | 区切り文字 | デリミタ |
| bucket (object storage) / bucket (NTILE) | バケット / バケット | |
| throw an error / return an error | エラーを発生させる / エラーを返す | 投げる |
| return (a value) | 返す | 戻す, リターンする |
| well-formed / valid (JSON) | 整形式の / 有効な | |
| file / state file | ファイル / ステートファイル | 状態ファイル |
| directory / folder | ディレクトリ / フォルダー | フォルダ |
| computer, machine | コンピューター, マシン | コンピュータ |
| user | ユーザー | ユーザ |
| drift / infrastructure drift | ドリフト / インフラストラクチャのドリフト | |
| infrastructure | インフラストラクチャ | インフラ (except in a fixed name) |
| cloud inventory, inventory | クラウドインベントリ, インベントリ | 棚卸し |
| compliance | コンプライアンス | |
| provision (resources) | プロビジョニングする | |
| operator (the person running StackQL) | 運用者 | オペレーター |
| caller | 呼び出し元 | |
| filter (noun and verb) | フィルター / フィルタリングする | フィルタ |
| concatenation | 連結 | |
| parse / plan (a statement) | 解析する / 計画を作成する | パースする |
| contract (I/O contract, safety contract) | コントラクト | 契約 |
| source of truth | 信頼できる唯一の情報源 | |
| entitlement | 権限付与 | |
| snapshot / checksum / digest | スナップショット / チェックサム / ダイジェスト | |
| allowlist | 許可リスト | ホワイトリスト |
| opt in / explicit opt-in | オプトインする / 明示的なオプトイン | |
| enable / disable | 有効にする / 無効にする | |
| support (a feature, a format) / supported | サポートする / サポートされている | |
| hint (in an MCP error or tool annotation) | ヒント | |
| advertise (a capability) | 公開する | 広告する |
| artifact (build or release output) | アーティファクト | 成果物 |
| stack (of resources) | スタック | |
| pipeline | パイプライン | |
| notarized / signed | 公証済み / 署名済み | |
| assume a role (AWS) | ロールを引き受ける | ロールを想定する |
| common table expression (CTE) | 共通テーブル式（CTE） | |
| default / by default | デフォルト / デフォルトで | 既定 |
| array (JSON, data type) | 配列 | アレイ |
| policy (IAM, access policy) / IAM binding | ポリシー / IAM バインディング | |
| bind, binding (alias, secret store) | バインドする, バインディング | |
| assign, assignment (`SET`) | 割り当てる, 割り当て | |
| token (lexical) | 字句 | トークン (that is an auth token) |
| pin (a version, a key, an issuer) | 固定する | ピン留めする |
| exporter / receiver (OTel) / sink (audit) | エクスポーター / レシーバー / シンク | |
| preview (a feature in preview) | プレビュー版 | |
| legacy | レガシー | |
| batch / statement batch | バッチ / 文のバッチ | |
| wildcard | ワイルドカード | |
| query hint | クエリヒント | |
| substring / pattern (regex) / regular expression | 部分文字列 / パターン / 正規表現 | |
| collating function / collating sequence | 照合関数 / 照合順序 | |
| timestamp | タイムスタンプ | |
| current row / rank / running total (window functions) | 現在の行 / 順位 / 累計 | |
| installer | インストーラー | インストーラ |
| workspace | ワークスペース | |
| monitoring / to monitor / monitor (noun) | 監視 / 監視する / モニター | モニタリング |
| run, execute / list / delete, remove / manage | 実行する / 一覧表示する / 削除する / 管理する | |
| framework | フレームワーク | |
| StackQL Provider Registry (link text to `/providers`) | unchanged, it is a name | StackQL プロバイダーレジストリ |
| interface | インターフェイス | インターフェース |
| identity (caller identity, IAM identity) | ID | アイデンティティ |
| dereference (a secret, a variable) | 参照解決する | 逆参照する |
| in-memory / sessionless | インメモリ / セッションレス | メモリ内 |
| relation / fingerprint / tenancy | リレーション / フィンガープリント / テナンシー | |
| public key / private key / issuer / audience (JWT) | 公開鍵 / 秘密鍵 / 発行者 / オーディエンス | 公開キー, 秘密キー |
| mutual TLS / revocation | 相互 TLS（mTLS） / 取り消し | |
| source / re-source (an env file) | 読み込む / 再読み込みする | |
| blocking / non-blocking (invocation) | ブロッキング / ノンブロッキング | |
| discovery | 検出 (the MCP tool group); ディスカバリ (OIDC discovery, a directory listing) | ディスカバリー |
| escalate / escalation (server mode) | 昇格する / 昇格 | |
| sidecar / vendor (verb) | サイドカー / ベンダリングする | |
| entry point | エントリポイント | エントリーポイント |
| container / browser / preprocessor | コンテナー / ブラウザー / プリプロセッサー | コンテナ, ブラウザ, プリプロセッサ |
| serialize / deserialize | シリアル化する / 逆シリアル化する | シリアライズする |
| agentic / observability | エージェント型 / オブザーバビリティ | 可観測性 |
| render / deprecated / fall back | レンダリングする / 非推奨 / フォールバックする | |
| SQL verb | SQL 動詞 | |
| attach (a disk, a policy) | アタッチする | |
| refresh (a materialized view) / procedure / prepared statement | リフレッシュする / プロシージャ / プリペアド文 | |
| datetime / modifier (date functions) / table-valued function | 日時 / 修飾子 / テーブル値関数 | |
| partition / peer / tied rows (window functions) | パーティション / ピア / 同順位の行 | |
| sine / cosine / tangent; hyperbolic and arc forms | サイン / コサイン / タンジェント; ハイパーボリックサイン, アークサイン（逆正弦） with the kanji gloss at first mention | 正弦, 正接, 双曲線正接 (except inside the gloss) |
| Kept in Latin script | Webhook, dotenv, bearer (Bearer トークン), Issue (GitHub), crate names, the format name MCP Bundle | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Japanese |
|---|---|
| Install | インストール |
| Installing StackQL | StackQL のインストール |
| Getting Started | はじめに |
| Command Line Usage | コマンドラインの使用方法 |
| Language Specification | 言語仕様 |
| Functions | 関数 |
| Developers | 開発者向け |
| Providers | プロバイダー |
| Quick Starts | クイックスタート |
| AI Agents | AI エージェント |
| MCP Server / MCP Tools / Embedded MCP | MCP サーバー / MCP ツール / 組み込み MCP |
| Blog | ブログ |
| Product Announcements | 製品のお知らせ |
| Provider Announcements | プロバイダーのお知らせ |
| Tutorials | チュートリアル |
| Documentation | ドキュメント |
| Contact us | お問い合わせ |
| Privacy Policy | プライバシーポリシー |
| Terms of Service | 利用規約 |
| Support | サポート |
| AI Reference | AI リファレンス |
| More | その他 |
| Welcome to StackQL | StackQL へようこそ |

Recurring headings, phrases and keywords (the same on every page):

| English | Japanese |
|---|---|
| Syntax | 構文 |
| Arguments | 引数 |
| Return Value(s) | 戻り値 |
| Example / Examples | 例 |
| Inputs / Output | 入力 / 出力 |
| Flags | フラグ |
| Overview | 概要 |
| How it works | 仕組み |
| Modes | モード |
| Gating | ゲート制御 |
| Install | インストール |
| Quick start | クイックスタート |
| Demo app | デモアプリ |
| Repo link | リポジトリへのリンク |
| Vendoring / embedding | ベンダリング / 組み込み |
| Prerequisites | 前提条件 |
| Next steps | 次のステップ |
| Related | 関連項目 |
| See also: | 関連項目： |
| See X. | X を参照してください。 |
| For more information, see X. | 詳細については、X を参照してください。 |
| Optional. / Defaults to X. | 省略可能。 / デフォルトは X です。 |
| Returns X. (the sentence that opens a function or tool page) | X を返します。 |
| Return the absolute value of a number (an example heading that names a task) | 数値の絶対値を返す (dictionary form, no です・ます) |
| X function in StackQL: ... (description pattern) | StackQL の X 関数：... |
| X window function in StackQL: ... (description pattern) | StackQL の X ウィンドウ関数：... |
| X in StackQL: ... (description of a statement page) | StackQL の X：... |
| ... using SQL in StackQL (description pattern) | StackQL で SQL を使って... |
| Available in StackQL `vX` and later / releases from `vX` | StackQL `vX` 以降で利用できます |
| Available in StackQL releases after `vX` | StackQL `vX` より後のリリースで利用できます |
| None. (no arguments, no inputs) | なし。 |
| Query / Results (tab labels), Output: | クエリ / 結果, 出力： |
| Tested with (badge line on quick starts) | 動作確認済み |
| Best Practices / Troubleshooting / Further reading | ベストプラクティス / トラブルシューティング / 参考資料 |
| MCP tool listing X / MCP tool that does X (description pattern) | X を一覧表示する MCP ツール / X を行う MCP ツール |
| MCP overview (link text) | MCP の概要 |
| Argument / Required / Description (table headers) | 引数 / 必須 / 説明 |
| Renderer / Shape (table headers) | レンダラー / 形状 |
| Table / One row per X (table cells) | テーブル / X ごとに 1 行 |
| Yes / No (table cells) | はい / いいえ |
| For example | たとえば |
| Aggregate function / Scalar function | 集約関数 / スカラー関数 |
| When to use, Use case / Example usage | 使用場面 / 使用例 |
| Conclusion / Get started (homepage heading) | まとめ / 利用の開始 |
| keyword `cloud inventory` | クラウドインベントリ |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Do not nest full-width parentheses. Where a first-mention gloss would land inside （） or in a table cell (承認が必要（エリシテーション）), leave it out there and put it on the first mention in prose.
- Every prose sentence ends in です・ます form (〜します, 〜です, 〜できます). Descriptions of steps use 〜します ("次のコマンドを実行します。"); 〜してください is for an instruction the reader must follow ("must", "make sure", "you need to"). Never 〜しましょう.
- Headings, page titles, sidebar labels, table headers and list fragments are noun phrases or the dictionary form, never です・ます: "StackQL のインストール", "プロバイダーの使用", "数値の絶対値を返す". No end punctuation. Keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- Punctuation in Japanese prose is full width: 、。（）：「」, and ？ and ！ only where the source has them. Use 「」 to quote a word or a UI label in prose and 『』 inside 「」. Never use ，．or “” in Japanese prose. Everything inside code fences and inline code keeps its half-width punctuation, and a sentence that is entirely Latin text or code keeps half-width punctuation too. Use 、 between the items of a short inline list in a sentence.
- Parentheses in a Japanese sentence are full width even when they hold English: Infrastructure as Code（IaC）.
- One half-width space between Japanese text and an adjacent Latin word, number, inline code span or link whose text is Latin: `stackql exec` を実行します, 3 つのモード, StackQL の. No space next to full-width punctuation: `stackql shell` を実行します。 and （`--output` を参照）.
- Katakana is full width and alphanumerics are half width, always: never half-width katakana (ｶﾀｶﾅ) or full-width letters and digits (ＳＱＬ, １).
- Loanwords follow the long-vowel convention of current Japanese vendor documentation: words from English -er, -or, -ar take ー (サーバー, ユーザー, プロバイダー, パラメーター, フォルダー, コンピューター, ヘッダー, フィルター, パーサー, エクスポーター); words from -ry, -ty and -gy do not (クエリ, リポジトリ, レジストリ, ディレクトリ, ライブラリ, バイナリ, セキュリティ, プロパティ). ポリシー is spelled with ー.
- Katakana compounds are written solid, with no ・ and no space: サービスアカウント, リソースライフサイクル, コントロールプレーン. A Latin name and a katakana word are separated by a space: MCP サーバー, API キー, Bearer トークン.
- No dash punctuation in prose: no ー used as a dash, no ——, no spaced hyphen. Where the source uses ` - ` or ` -- ` as a dash inside a sentence or between a term and its explanation, use 、 or ： or （）, or split the sentence. A ` - ` separator in a title between two names stays.
- Example headings and `description` values do not start with a verb in です・ます form; a description is one sentence that may end in です・ます or in a noun phrase, as the source structure allows.
- List items and table cells end the way the source does: no 。 added to fragments, 。 where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English, in 「」 only if the source quotes it.
- Numbers are half-width Arabic numerals with a space before a counter or unit word (3 つ, 5 分, 10 行); versions, dates and units as in the source. No kanji numerals except in fixed words (一覧, 一部, 唯一).
- Prefer hiragana for the function words that Japanese style guides write in kana: たとえば, ただし, および, または, すべて, できる, ください, とき, こと, もの, ほか. Use kanji for 例, 場合, 際, 必要.
- The `description` front matter stays one sentence and under about 100 characters (count Japanese characters and Latin words alike; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud inventory` -> クラウドインベントリ, `infrastructure as code` -> Infrastructure as Code); keep the number and order of entries.
- Bold and italic markers stay where the source has them, with no space inside the markers (`**読み取り専用**`, not `** 読み取り専用 **`).
- A closing `**` that follows full-width punctuation and is followed directly by a Japanese character does not close in CommonMark, and the asterisks show on the page (`**使用場面：**すべて` renders literally). Move the punctuation outside the markers (`**使用場面**：すべて`) or leave a space after the closing marker. The same holds for an opening marker between a Japanese character and full-width punctuation, and for `*` and `_` emphasis.
- Front matter must stay valid YAML: keep the quoting of the source value, and put a value in double quotes if the Japanese text contains a half-width `: ` or ` #`, or starts with a quote, bracket, backtick, `*`, `&`, `!`, `%`, `@` or 「. A full-width ： needs no quoting.
- Link text that is another page's title is that page's translated `title`, including the "See also" badges that wrap the title in inline code (`` [[` Window Functions `]] `` becomes `` [[` ウィンドウ関数 `]] ``); `yarn i18n:verify` warns about the changed inline code on those pages, which is expected. Link text that is a heading of the target page is that heading's translation.
- Example prompts addressed to an AI assistant use the casual request form (〜を表示して, 〜を一覧表示して, 〜を作成して); everything else stays です・ます.
- The homepage tagline "Configuration-as-Data. Infrastructure-as-Context." stays in English.
- Japanese text is shorter than English, so the `length ratio` warning from `yarn i18n:verify` is expected on prose-heavy pages and can be ignored.

## ko

Audience: Korean-speaking engineers reading technical documentation. Formal polite style (합니다체: 〜합니다, 〜입니다, 〜할 수 있습니다) in every prose sentence; an instruction the reader must follow uses 〜하세요 (참조하세요, 실행하세요), never 〜하십시오, 〜해 주세요 or 〜해요. Never the plain style (〜한다, 〜이다) in prose. No extra honorifics (드립니다, 주시기 바랍니다): the register is that of vendor reference documentation (Microsoft, AWS and Google Korean documentation), plain and declarative. Do not write 당신 or 여러분: leave the subject out where the source says "you", as Korean does, or use 사용자 where a subject is needed. Short sentences; split an English sentence that would need a long chain of relative clauses. Keep the English terms that Korean developers use as they are; do not invent Korean names for products or APIs.

Names that stay in Latin script, in addition to the "All locales" list: SQL, CLI, API, SDK, JSON, YAML, CSV, HTTP, OAuth, IaC, CI/CD, LLM, MCP, SaaS, OTel, macOS, Linux, Windows, Docker, Kubernetes, GitHub Actions, Homebrew, Chocolatey, Webhook, and every provider, service, resource and method name. Acronyms are not expanded or glossed unless the source does so.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Korean | Not |
|---|---|---|
| provider (a StackQL provider) | 프로바이더 | 공급자, 제공자, 공급 기업, Provider |
| cloud provider (the vendor) | 클라우드 제공업체 | 클라우드 프로바이더, 클라우드 공급자, 클라우드 벤더 |
| vendor (the company behind an API or SDK) | 공급업체 ("vendor lock-in" = 공급업체 종속) | 벤더, 업체 |
| service | 서비스 | |
| resource | 리소스 | 자원 |
| method | 메서드 | 메소드 |
| operation | 작업 (연산 only for arithmetic) | 오퍼레이션 |
| query (noun) / to query | 쿼리 / 쿼리하다, 쿼리를 실행하다 (조회하다 is right for "look up, retrieve") | 질의, 퀘리 |
| statement | 문 (SQL 문, `SELECT` 문) | 명령문, 구문 (that is "syntax"), 스테이트먼트 |
| result set | 결과 집합 | 결과셋, 결과 세트 |
| row / column | 행 / 열 | 로우, 컬럼, 칼럼 |
| table | 테이블 (an HTML or markdown table: 표) | |
| view | 뷰 | |
| materialized view | 구체화된 뷰 | 머티리얼라이즈드 뷰, 실체화된 뷰 |
| window function | 윈도우 함수 | 창 함수, 윈도 함수 |
| window frame | 윈도우 프레임 | |
| aggregate function / scalar function | 집계 함수 / 스칼라 함수 | 집합 함수 |
| function / expression / clause | 함수 / 식 / 절 | 표현식 |
| keyword / operator / predicate | 키워드 / 연산자 / 조건자 | 술어, 오퍼레이터 (for an operator) |
| subquery / alias / join | 하위 쿼리 / 별칭 / 조인 (`JOIN` where the source has code) | 서브쿼리, 에일리어스 |
| data type / literal / identifier | 데이터 형식 / 리터럴 / 식별자 | 자료형, 데이터 타입 |
| string / integer / boolean | 문자열 / 정수 / 부울 | 스트링, 불리언 |
| whitespace | 공백 문자 | 화이트스페이스 |
| parameter / argument | 매개 변수 / 인수 | 파라미터, 매개변수, 인자 |
| flag (CLI) / option | 플래그 / 옵션 ("global flags" = 전역 플래그) | 글로벌 플래그 |
| environment variable | 환경 변수 | 환경변수 |
| field / key / value | 필드 / 키 / 값 | |
| schema | 스키마 | |
| control plane / data plane | 컨트롤 플레인 / 데이터 플레인 | 제어 평면, 데이터 평면 |
| agent, AI agent | 에이전트, AI 에이전트 | 대리인 |
| tool (an MCP tool) | 도구 | 툴 |
| prompt (text sent to a model) | 프롬프트 | |
| approval prompt, elicitation prompt (a dialog shown to the user) | 승인 요청, 엘리시테이션 요청 | 승인 프롬프트 |
| elicitation (MCP) | 엘리시테이션(elicitation) at first mention on a page, then 엘리시테이션 | 유도, 이끌어내기 |
| gating / gated (MCP) | 게이트 제어 / 게이트 제어 대상; "gated by X" = X에 의해 게이트 제어됨; "gated writes" = 승인이 필요한 쓰기 | 게이팅 |
| approval / approve | 승인 / 승인하다 | |
| server mode, safety mode | 서버 모드, 안전 모드 | 세이프티 모드 |
| read-only | 읽기 전용 | |
| mutation / mutating statement / mutation query | 변경 작업 / 변경 문 / 변경 쿼리 | 뮤테이션, 돌연변이 |
| lifecycle operation | 수명 주기 작업 | 라이프사이클 작업 |
| resource lifecycle | 리소스 수명 주기 | 리소스 라이프사이클 |
| log / audit log | 로그 / 감사 로그 | |
| credentials | 자격 증명 | 크리덴셜, 인증 정보 |
| authentication / authorization | 인증 / 권한 부여 | 인가, 승인 (that is "approval") |
| principal / service principal | 보안 주체 / 서비스 주체 | 프린시펄 |
| service account | 서비스 계정 | 서비스 어카운트 |
| account | 계정 | |
| token / API key / access key | 토큰 / API 키 / 액세스 키 | 접근 키 |
| secret / secret store | 시크릿 / 시크릿 저장소 | 비밀, 비밀 저장소 |
| scope (OAuth, or of a variable or a setting) | 범위 | 스코프 |
| role / permission | 역할 / 권한 | 롤, 퍼미션 |
| session / session variable | 세션 / 세션 변수 | |
| deployment / to deploy | 배포 / 배포하다 | 디플로이, 전개 |
| infrastructure as code | 코드형 인프라(IaC) | 코드로서의 인프라 |
| registry (generic) / provider registry | 레지스트리 / 프로바이더 레지스트리 | |
| pull (a provider) | 가져오다; "pulled" = 가져온 | 풀하다, 당기다 |
| repository | 리포지토리 | 레포지토리, 저장소 |
| release | 릴리스 | 릴리즈 |
| command line | 명령줄 | 커맨드 라인, 커맨드라인 |
| shell / interactive shell | 셸 / 대화형 셸 | 쉘, 인터랙티브 셸 |
| server / client | 서버 / 클라이언트 | |
| transport (MCP) | 전송 방식 (stdio 전송 방식, HTTP 전송 방식) | 트랜스포트 |
| stateless / stateful | 상태 비저장 / 상태 저장 | 스테이트리스, 스테이트풀 |
| endpoint | 엔드포인트 | |
| request / response | 요청 / 응답 | 리퀘스트, 리스폰스 |
| request body | 요청 본문 | 요청 바디 |
| payload | 페이로드 | |
| header (HTTP) | 헤더 | |
| pagination | 페이지네이션 | 페이징, 페이지 매기기 |
| output / output format / output mode | 출력 / 출력 형식 / 출력 모드 | 아웃풋, 출력 포맷 |
| template / templating | 템플릿 / 템플릿 기능 | 템플리팅 |
| dry run | 드라이 런 | 시험 실행 |
| fan-out | 팬아웃 | |
| guardrail | 가드레일 | |
| answer engine | 답변 엔진 | 응답 엔진, 앤서 엔진 |
| region / project / subscription / tenant | 리전 / 프로젝트 / 구독 / 테넌트 | 지역 |
| open source | 오픈 소스 | 오픈소스 |
| binary / library | 바이너리 / 라이브러리 | |
| embedded (Embedded MCP) / to embed | 임베디드 (임베디드 MCP) / 내장하다 | 내장형 MCP, 임베드하다 |
| installation / to install | 설치 / 설치하다 | |
| documentation | 문서 | 도큐먼트, 설명서 |
| tutorial | 튜토리얼 | 자습서 |
| quick start | 빠른 시작 | 퀵 스타트 |
| getting started | 시작하기 | 입문 |
| announcement | 공지 | 발표 |
| feature | 기능 | 피처, 특징 |
| example | 예제 (a worked example, the "Examples" heading); 예 in "for example" (예를 들어) | 샘플 (except "sample app", "sample data") |
| syntax / usage | 구문 / 사용법 | |
| note / tip / warning (admonition titles are translated by the theme) | 참고 / 팁 / 경고 | |
| resource hierarchy | 리소스 계층 구조 | |
| access management | 액세스 관리 | 접근 관리 |
| provider session | 프로바이더 세션 | |
| SQL backend | SQL 백엔드 | |
| query engine / parser | 쿼리 엔진 / 파서 | |
| wire protocol | 와이어 프로토콜 (PostgreSQL 와이어 프로토콜) | 유선 프로토콜 |
| reverse proxy | 역방향 프록시 | 리버스 프록시 |
| local cache | 로컬 캐시 | |
| key-value pair | 키-값 쌍 | 키 값 페어 |
| delimiter / separator | 구분 기호 | 구분자, 딜리미터 |
| bucket (object storage) / bucket (NTILE) | 버킷 / 버킷 | |
| error / throw an error / return an error | 오류 / 오류를 발생시키다 / 오류를 반환하다 | 에러 (except in quoted output), 던지다 |
| return (a value) | 반환하다 | 리턴하다, 돌려주다 |
| well-formed / valid (JSON) | 올바른 형식의 / 유효한 | |
| file / state file | 파일 / 상태 파일 | 스테이트 파일 |
| directory / folder | 디렉터리 / 폴더 | 디렉토리 |
| computer, machine | 컴퓨터, 머신 | |
| user | 사용자 | 유저 |
| drift / infrastructure drift | 드리프트 / 인프라 드리프트 | 표류 |
| infrastructure | 인프라 | 인프라스트럭처, 인프라스트럭쳐 |
| cloud inventory, inventory | 클라우드 인벤토리, 인벤토리 | 재고 |
| compliance | 규정 준수 | 컴플라이언스 |
| provision (resources) | 프로비저닝하다 | |
| operator (the person running StackQL) | 운영자 | 오퍼레이터 |
| caller | 호출자 | |
| filter (noun and verb) | 필터 / 필터링하다 | |
| concatenation | 연결 | 연접 |
| parse / plan (a statement) | 구문 분석하다 / 실행 계획을 수립하다 | 파싱하다 |
| contract (I/O contract, safety contract) | 계약 | 컨트랙트 |
| source of truth | 단일 정보 출처 | 진실의 원천 |
| entitlement | 사용 권한 | |
| snapshot / checksum / digest | 스냅샷 / 체크섬 / 다이제스트 | |
| allowlist | 허용 목록 | 화이트리스트 |
| opt in / explicit opt-in | 옵트인하다 / 명시적 옵트인 | |
| enable / disable | 활성화하다 / 비활성화하다 | 사용하도록 설정하다 |
| support (a feature, a format) / supported | 지원하다 / 지원되는 | |
| hint (in an MCP error or tool annotation) | 힌트 | |
| advertise (a capability) | 알리다 | 광고하다 |
| artifact (build or release output) | 아티팩트 | 산출물 |
| stack (of resources) | 스택 | |
| pipeline | 파이프라인 | |
| notarized / signed | 공증된 / 서명된 | |
| assume a role (AWS) | 역할을 수임하다 | 역할을 가정하다, 역할을 맡다 |
| common table expression (CTE) | 공통 테이블 식(CTE) | 공통 테이블 표현식 |
| default / by default | 기본값 / 기본적으로 | 디폴트 |
| array (JSON, data type) | 배열 | 어레이 |
| policy (IAM, access policy) / IAM binding | 정책 / IAM 바인딩 | 폴리시 |
| bind, binding (alias, secret store) | 바인딩하다, 바인딩 | |
| assign, assignment (`SET`) | 할당하다, 할당 | |
| lexical structure / token (lexical) | 어휘 구조 / 토큰 | 렉시컬 구조 |
| pin (a version, a key, an issuer) | 고정하다 | 핀 고정하다 |
| exporter / receiver (OTel) / sink (audit) | 익스포터 / 리시버 / 싱크 | |
| preview (a feature in preview) | 미리 보기 | 프리뷰 |
| legacy | 레거시 | |
| batch / statement batch | 배치 / 문 배치 | |
| wildcard | 와일드카드 | |
| query hint | 쿼리 힌트 | |
| substring / pattern (regex) / regular expression | 부분 문자열 / 패턴 / 정규식 | 정규 표현식, 정규표현식 |
| collating function / collating sequence | 데이터 정렬 함수 / 데이터 정렬 순서 | 콜레이션 |
| timestamp | 타임스탬프 | |
| current row / rank / running total (window functions) | 현재 행 / 순위 / 누계 | 누적 합계 |
| installer | 설치 관리자 | 인스톨러 |
| workspace | 작업 영역 | 워크스페이스 |
| monitoring / to monitor / monitor (noun) | 모니터링 / 모니터링하다 / 모니터 | 감시 |
| run, execute / list / delete, remove / manage | 실행하다 / 나열하다 / 삭제하다 / 관리하다 | |
| framework | 프레임워크 | |
| StackQL Provider Registry (link text to `/providers`) | unchanged, it is a name | StackQL 프로바이더 레지스트리 |
| interface | 인터페이스 | |
| identity (caller identity, IAM identity) | ID (호출자 ID) | 아이덴티티, 신원 |
| dereference (a secret, a variable) | 역참조하다 | |
| in-memory / sessionless | 인메모리 / 세션 없는 | 메모리 내 |
| relation / fingerprint / tenancy | 관계 / 지문 / 테넌시 | |
| public key / private key / issuer / audience (JWT) | 공개 키 / 개인 키 / 발급자 / 대상 | 비밀 키 (for a private key), 공개키, 개인키 |
| mutual TLS / revocation | 상호 TLS(mTLS) / 해지 | |
| source / re-source (an env file) | 로드하다 / 다시 로드하다 | 소싱하다 |
| blocking / non-blocking (invocation) | 블로킹 / 논블로킹 | |
| discovery | 탐색 (the MCP tool group, "discovery workflow"); 디스커버리 (OIDC discovery) | 발견 |
| escalate / escalation (server mode) | 승격하다 / 승격 | |
| sidecar / vendor (verb) | 사이드카 / 벤더링하다 | |
| entry point | 진입점 | 엔트리 포인트 |
| container / browser / preprocessor | 컨테이너 / 브라우저 / 전처리기 | |
| serialize / deserialize | 직렬화하다 / 역직렬화하다 | 시리얼라이즈하다 |
| agentic / observability | 에이전트형 / 관측성 | 옵저버빌리티, 관찰 가능성 |
| render / deprecated / fall back | 렌더링하다 / 더 이상 사용되지 않는 / 대체하다 ("falls back to X" = X로 대체됩니다) | 폴백하다 |
| SQL verb | SQL 동사 | |
| attach (a disk, a policy) | 연결하다 | 어태치하다 |
| refresh (a materialized view) / procedure / prepared statement | 새로 고치다 / 프로시저 / 준비된 문 | 리프레시하다 |
| datetime / modifier (date functions) / table-valued function | 날짜/시간 / 한정자 / 테이블 반환 함수 | 수식어 |
| partition / peer / tied rows (window functions) | 파티션 / 피어 / 동순위 행 | |
| sine / cosine / tangent; hyperbolic and arc forms | 사인 / 코사인 / 탄젠트; 하이퍼볼릭 사인, 아크사인 | 정현, 여현, 정접 |
| object (JSON object, configuration object, StackQL object notation) | 객체 | 개체, 오브젝트 |
| built-in (function, procedure) | 기본 제공 (기본 제공 함수) | 빌트인, 내장 (that is "embedded") |
| subcommand / placeholder | 하위 명령 / 자리 표시자 | 서브커맨드, 플레이스홀더 |
| attribute, property (resource, OTel) | 속성 | 어트리뷰트, 프로퍼티 |
| configuration / config file | 구성 / 구성 파일 | 컨피그 |
| membership / envelope | 멤버십 / 엔벨로프 | 멤버 자격, 엔벌로프 |
| rotate (credentials, keys) | 교체하다 (log rotation: 로테이션) | |
| resolve (credentials, an alias, a variable) / resolve to a value | 확인하다 / 평가되다 | |
| record / issue (GitHub) / pull request | 레코드 / 이슈 / 끌어오기 요청 ("PR" stays PR) | |
| floating point / exact numeric / approximate numeric | 부동 소수점 / 정확한 수치 / 근사 수치 | 정확한 숫자, 근사 숫자 |
| ascending / descending / column headers (CSV) | 오름차순 / 내림차순 / 열 머리글 | |
| protocol revision / telemetry / trace ID | 프로토콜 리비전 / 텔레메트리 / 트레이스 ID | |
| semantics / dialect (SQL) / specification | 의미 체계 / 방언 / 사양 | |
| subject (JWT, certificate) / claim | 주체 / 클레임 | |
| publish (a provider, a tool) / side effects | 게시하다 / 부작용 | |
| AI assistant / skill (Claude Code) | AI 어시스턴트 / 스킬 | |
| self-contained executable / release asset | 자체 포함된 실행 파일 / 릴리스 자산 | |
| Availability Zone (AWS) / pod (Kubernetes) | 가용 영역 / 파드 | |
| Agentic Era (homepage) | 에이전트 시대 (에이전트형 stays for the adjective) | |
| Kept in Latin script | Webhook, dotenv, Bearer (Bearer 토큰), crate names, the format name MCP Bundle | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Korean |
|---|---|
| Install | 설치 |
| Installing StackQL | StackQL 설치 |
| Getting Started | 시작하기 |
| Command Line Usage | 명령줄 사용법 |
| Language Specification | 언어 사양 |
| Functions / Built-in Functions | 함수 / 기본 제공 함수 |
| Developers | 개발자 |
| Providers | 프로바이더 |
| Quick Starts | 빠른 시작 |
| AI Agents | AI 에이전트 |
| MCP Server / MCP Tools / Embedded MCP | MCP 서버 / MCP 도구 / 임베디드 MCP |
| Blog | 블로그 |
| Product Announcements | 제품 공지 |
| Provider Announcements | 프로바이더 공지 |
| Tutorials | 튜토리얼 |
| Documentation | 문서 |
| Contact us | 문의하기 |
| Privacy Policy | 개인정보 처리방침 |
| Terms of Service | 서비스 약관 |
| Support | 지원 |
| AI Reference | AI 참조 |
| More | 더 보기 |
| Welcome to StackQL | StackQL에 오신 것을 환영합니다 |

Recurring headings, phrases and keywords (the same on every page):

| English | Korean |
|---|---|
| Syntax | 구문 |
| Arguments | 인수 |
| Return Value(s) | 반환 값 |
| Example / Examples | 예제 |
| Inputs / Output | 입력 / 출력 |
| Flags | 플래그 |
| Overview | 개요 |
| How it works | 작동 방식 |
| Modes | 모드 |
| Gating | 게이트 제어 |
| Install | 설치 |
| Quick start | 빠른 시작 |
| Demo app | 데모 앱 |
| Repo link | 리포지토리 링크 |
| Vendoring / embedding | 벤더링 / 내장 |
| Prerequisites | 사전 요구 사항 |
| Next steps | 다음 단계 |
| Related | 관련 항목 |
| See also: | 참고 항목: |
| See X. | X를 참조하세요. (particle by pronunciation, see Style) |
| For more information, see X. | 자세한 내용은 X를 참조하세요. |
| For more information, see https://... (a bare URL as link text) | 자세한 내용은 [https://...](https://...) 문서를 참조하세요. (문서 carries the particle, never a URL) |
| Optional. / Defaults to X. | 선택 사항입니다. / 기본값은 X입니다. |
| Returns X. (the sentence that opens a function or tool page) | X를 반환합니다. |
| Return the absolute value of a number (an example heading that names a task) | 숫자의 절댓값 반환 (noun phrase, no 합니다) |
| X function in StackQL: ... (description pattern) | StackQL의 X 함수: ... |
| X window function in StackQL: ... (description pattern) | StackQL의 X 윈도우 함수: ... |
| X in StackQL: ... (description of a statement page) | StackQL의 X: ... |
| ... using SQL in StackQL (description pattern) | StackQL에서 SQL로 ... |
| Available in StackQL `vX` and later / releases from `vX` | StackQL `vX` 이상에서 사용할 수 있습니다 |
| Available in StackQL releases after `vX` | StackQL `vX` 이후 릴리스에서 사용할 수 있습니다 |
| None. (no arguments, no inputs) | 없음. |
| Query / Results (tab labels), Output: | 쿼리 / 결과, 출력: |
| Tested with (badge line on quick starts) | 테스트 환경 |
| Best Practices / Troubleshooting / Further reading | 모범 사례 / 문제 해결 / 추가 자료 |
| MCP tool listing X / MCP tool that does X (description pattern) | X를 나열하는 MCP 도구 / X를 수행하는 MCP 도구 |
| MCP overview (link text) | MCP 개요 |
| Argument / Required / Description (table headers) | 인수 / 필수 / 설명 |
| Renderer / Shape (table headers) | 렌더러 / 형태 |
| Table / One row per X (table cells) | 테이블 / X당 1행 |
| Yes / No (table cells) | 예 / 아니요 |
| For example | 예를 들어 |
| Aggregate function / Scalar function | 집계 함수 / 스칼라 함수 |
| When to use, Use case / Example usage | 사용 시기, 사용 사례 / 사용 예 |
| Conclusion / Get started (homepage heading) | 결론 / 시작하기 |
| keyword `cloud inventory` | 클라우드 인벤토리 |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Every prose sentence ends in 합니다체 (〜합니다, 〜입니다, 〜할 수 있습니다). Descriptions of steps use 〜합니다 ("다음 명령을 실행합니다."); 〜하세요 is for an instruction the reader must follow ("must", "make sure", "you need to") and for "see X" (참조하세요). Never 〜합시다.
- Headings, page titles, sidebar labels, table headers and list fragments are noun phrases, never 합니다체: "StackQL 설치", "프로바이더 사용", "숫자의 절댓값 반환". Prefer a noun ending (설치, 사용, 생성, 반환) to the 〜하기 form; keep 시작하기, which is the established label. No end punctuation. Keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- Particles (은/는, 이/가, 을/를, 와/과, 으로/로) follow the Korean pronunciation of the word they attach to, including Latin words, numbers and code: StackQL (스택큐엘) -> StackQL은, StackQL을, StackQL로; SQL -> SQL은, SQL을, SQL로; JSON -> JSON은, JSON을, JSON으로; Google -> Google은, Google로; AWS, API, CLI, MCP, GitHub, Azure, Docker, Kubernetes -> 는, 를, 로. After inline code, add the noun the code names (명령, 함수, 플래그, 매개 변수, 파일, 문, 열, 도구, 키, 변수) so the particle attaches to Korean: "`stackql exec` 명령을 실행합니다", "`--output` 플래그를 사용합니다", "`SELECT` 문은". Attach a particle straight to a code span only where no such noun fits, and then by pronunciation.
- Particles attach to the preceding word, code span or link with no space ("StackQL은", "[StackQL 설치](/installing-stackql)를 참조하세요"); everything else follows standard Korean word spacing (띄어쓰기). A Latin word and the Korean word after it are separated by a space unless the Korean is a particle or a suffix such as 의, 에서, 에, 와: "MCP 서버", "API 키", "AWS에서".
- Write the established compound nouns with spaces, as Microsoft Korean documentation does: 매개 변수, 환경 변수, 세션 변수, 서비스 계정, 액세스 키, 결과 집합, 데이터 형식, 수명 주기, 오픈 소스. 명령줄 and 진입점 are one word.
- Punctuation in Korean prose is half width: . , ? ! : ; ( ). No full-width punctuation (。、，：（）「」『』) and no 〈〉《》. Quote a word or a UI label with straight double quotes ("..."), and use straight single quotes inside them; never curly quotes (“ ” ‘ ’). Everything inside code fences and inline code keeps its punctuation unchanged.
- No dash punctuation in prose: no em dash, no en dash, no spaced hyphen. Where the source uses ` - ` or ` -- ` as a dash inside a sentence or between a term and its explanation, use a comma, a colon or parentheses, or split the sentence. A ` - ` separator in a title between two names stays.
- Numbers are Arabic numerals, and a counter or unit attaches to them without a space (3개, 5분, 10행, 2단계, 1행); versions, dates and units as in the source. "One row per X" is X당 1행.
- Loanwords follow the standard orthography used in Microsoft and Google Korean documentation: 셸 (not 쉘), 릴리스 (not 릴리즈), 메서드 (not 메소드), 메시지 (not 메세지), 디렉터리 (not 디렉토리), 리포지토리 (not 레포지토리), 애플리케이션 (not 어플리케이션), 컨텍스트 (not 콘텍스트), 아키텍처 (not 아키텍쳐), 템플릿 (not 탬플릿), 워크플로 (not 워크플로우), 윈도우 함수 (the established exception).
- Example headings and `description` values do not start with a 합니다체 verb; a description is one sentence that may end in 합니다체 or in a noun phrase, as the source structure allows.
- List items and table cells end the way the source does: no period added to fragments, a period where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English, in double quotes only if the source quotes it.
- The `description` front matter stays one sentence and under about 120 characters (count Korean syllables and Latin characters alike; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud inventory` -> 클라우드 인벤토리, `infrastructure as code` -> 코드형 인프라); keep the number and order of entries.
- Bold and italic markers stay where the source has them, with no space inside the markers (`**읽기 전용**`, not `** 읽기 전용 **`).
- Emphasis followed by a particle: a closing `**` whose preceding character is punctuation (a closing quote or parenthesis, a period, a colon, or the backtick that ends an inline code span) and whose next character is a Hangul syllable does not close in CommonMark, and the asterisks show on the page (`` **`SELECT`**는 `` and `**"읽기 전용"**을` render literally). Put a space and a noun after the marker (`` **`SELECT`** 문은 ``), or move the punctuation outside the markers. A closing `**` or `*` after a Hangul or Latin letter is fine (`**StackQL**은`). Underscore emphasis is stricter: a closing `__` or `_` directly followed by a Hangul character never closes, whatever precedes it (`__모든 리전__의` shows the underscores). Where the source uses `__bold__` and a particle follows in Korean, write `**bold**` instead (it renders the same) or put a space after the marker.
- Front matter must stay valid YAML: keep the quoting of the source value, and put a value in double quotes if the Korean text contains `: ` or ` #`, or starts with a quote, bracket, backtick, `*`, `&`, `!`, `%` or `@`. The description pattern "StackQL의 X 함수: ..." always needs the double quotes the source already has.
- Link text that is another page's title is that page's translated `title`, including the "See also" badges that wrap the title in inline code (`` [[` Window Functions `]] `` becomes `` [[` 윈도우 함수 `]] ``); `yarn i18n:verify` warns about the changed inline code on those pages, which is expected. Link text that is a heading of the target page is that heading's translation.
- Example prompts addressed to an AI assistant use the polite request form (〜를 보여 주세요, 〜를 나열해 주세요, 〜를 만들어 주세요), the one place 〜해 주세요 is used; everything else stays 합니다체 or 〜하세요.
- The homepage tagline "Configuration-as-Data. Infrastructure-as-Context." stays in English.
- Korean text is shorter than English in characters, so the `length ratio` warning from `yarn i18n:verify` is expected on prose-heavy pages and can be ignored.

## pt-BR

Audience: Brazilian engineers reading technical documentation. Brazilian Portuguese only, in the spelling of the 1990 orthographic agreement as used in Brazil (ideia, frequente, controle, projeto, seção, ação, atual, econômico): never a European Portuguese form (ficheiro, utilizador, ecrã, registo, equipa, gerir, descarregar, controlo, secção, contacto, "está a executar"). Address the reader as você, with the verb in the third person; present tense, short sentences. Instructions use the imperative in the você form ("Execute o comando a seguir", "Consulte", "Use", "Instale") and task headings use the infinitive ("Instalar o StackQL"). Keep the English terms that Brazilian developers use as a matter of course (the "Kept in English" row); do not coin Portuguese names for products or APIs. No inclusive-writing forms (`@`, `x`, `e` endings); no marketing tone.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Brazilian Portuguese | Not |
|---|---|---|
| provider (a StackQL provider) | provider (o; plural providers; lower case in prose) | provedor, provedor de serviços |
| cloud provider (the vendor) | provedor de nuvem (o) | provedor cloud, provedor da nuvem, provider de nuvem |
| vendor (the company behind an API or SDK, not only cloud) | fornecedor (o); "vendor-published" = publicado pelo fornecedor | fabricante, vendedor |
| cloud (as an adjective: cloud resources, cloud infrastructure) | na nuvem (recursos na nuvem, infraestrutura na nuvem); "de nuvem" in a fixed term from this table | cloud as a Portuguese adjective (recursos cloud) |
| service | serviço (o) | |
| resource | recurso (o) | |
| method | método (o) | |
| operation | operação (a) | |
| query (noun) / to query | consulta (a) / consultar | query, interrogar |
| statement | instrução (a) | declaração, sentença, comando (for a SQL statement) |
| result set | conjunto de resultados | |
| row / column | linha / coluna | registro, tupla (for a row) |
| table | tabela (a) | |
| view | view (a; plural views) | exibição, visão, visualização |
| materialized view | view materializada | exibição materializada |
| window function | função de janela | função janela, função analítica |
| window frame | quadro da janela | frame |
| aggregate function / scalar function | função de agregação / função escalar | função agregada, função de agregado |
| function / expression / clause | função / expressão / cláusula | |
| keyword / operator / predicate | palavra-chave / operador / predicado | |
| subquery / alias / join | subconsulta / alias (o; plural aliases) / join (o; `JOIN` where the source has code) | junção, união (that is `UNION`) |
| partition / peer, peer group / tie (window functions) | partição / linha equivalente, grupo de linhas equivalentes / empate ("linhas empatadas", "com lacunas") | linhas pares (par also means "even") |
| offset (`LAG`, `LEAD`) | deslocamento (the argument name `offset` stays as code) | |
| target (of `SET` and `UNSET`) | alvo ("Alvos" as the heading) | destino (that is an audit sink) |
| override (a credential, a default) / dereference | substituir, substituição ("credential override" = substituição de credencial) / desreferenciar, desreferência | |
| data type / literal / identifier | tipo de dados / literal / identificador | |
| string / integer / boolean | string (a; plural strings) / inteiro / booleano | cadeia de caracteres, cadeia |
| whitespace | espaço em branco | |
| parameter / argument | parâmetro / argumento | |
| flag (CLI) / option | opção / opção ("global flags" = opções globais) | sinalizador, bandeira, flag |
| environment variable | variável de ambiente | |
| field / key / value | campo / chave / valor | |
| schema | esquema (o) | |
| control plane / data plane | plano de controle / plano de dados | plano de controlo |
| agent, AI agent | agente, agente de IA (plural agentes de IA) | agente IA, agente de inteligência artificial |
| tool (an MCP tool) | ferramenta (a) | |
| prompt (text sent to a model) | prompt (o) | instrução, solicitação, mensagem |
| approval prompt, elicitation prompt (a dialog shown to the user) | solicitação de aprovação, solicitação de elicitação | prompt de aprovação |
| elicitation (MCP) | elicitação (a) | obtenção |
| gating / gated (MCP) | controle (o) / sujeito a controle; "gated by X" = controlado por X; "gated writes" = gravações sujeitas a aprovação | bloqueio, filtragem |
| approval / approve | aprovação / aprovar | |
| server mode, safety mode | modo do servidor, modo de segurança | |
| read-only | somente leitura (de somente leitura where an adjective needs it) | apenas leitura, só leitura |
| write (noun, a mutating call) | gravação | escrita |
| mutation / mutating statement | mutação / instrução de mutação | |
| lifecycle operation | operação de ciclo de vida | |
| resource lifecycle | ciclo de vida dos recursos | |
| log / audit log | log (o; plural logs) / log de auditoria | registro (that is a registry or a record) |
| log record / completion record / row record (OTel output) | registro de log / registro de conclusão / registro de linha | |
| credentials | credenciais (as) | |
| authentication / authorization | autenticação / autorização | |
| principal / service principal | principal (o; plural principals: "diretório de principals") / principal de serviço | entidade de segurança, entidade de serviço, principais (that reads as "main ones") |
| subject / issuer / audience / claim (JWT, certificate identity) | subject (o) / emissor / público / claim (a; plural claims) | |
| membership (group, directory) | associação ("associações a grupos") | assinatura (that is a subscription) |
| service account | conta de serviço | |
| token / API key / access key | token (o; plural tokens) / chave de API / chave de acesso | ficha |
| secret / secret store | segredo / repositório de segredos | cofre |
| scope (OAuth, and of a variable or a setting) | escopo (o) | âmbito, alcance |
| role / permission | role (a; plural roles) / permissão | função (that is a function), papel, perfil |
| assume a role (AWS) | assumir uma role | |
| session / session variable | sessão / variável de sessão | |
| deployment / to deploy | implantação / implantar | implementação, implementar, deploy, fazer deploy |
| infrastructure as code | infraestrutura como código (IaC) | |
| registry (generic) / provider registry | registro / registro de providers | registo |
| pull (a provider) | baixar; "pulled" = baixado | puxar, fazer pull |
| download / upload | baixar, download (o) / carregar, upload (o) | descarregar |
| repository | repositório (o) | |
| release | versão (a) as a noun, publicar as a verb | release, lançamento |
| command line | linha de comando | linha de comandos |
| shell / interactive shell | shell (o) / shell interativo | a shell |
| server / client | servidor / cliente | |
| transport (MCP) | transporte (o) | |
| stateless / stateful | sem estado / com estado | |
| endpoint | endpoint (o; plural endpoints) | ponto de extremidade, ponto final |
| request / response | solicitação / resposta | requisição, pedido |
| request body | corpo da solicitação | |
| payload | payload (o) | carga útil |
| header (HTTP) | cabeçalho (o) | header |
| pagination | paginação | |
| output / output format / output mode | saída / formato de saída / modo de saída | |
| template / templating | template (o) / uso de templates | modelo (that is an AI model) |
| model (AI) | modelo (o) | |
| dry run | execução de teste | simulação a seco, dry run |
| guardrail | barreira de proteção | guarda-corpo |
| answer engine | mecanismo de respostas | motor de respostas |
| region / project / subscription / tenant | região / projeto / assinatura / tenant (o) | inquilino, locatário, subscrição |
| account | conta | |
| open source | de código aberto | open source, de fonte aberta |
| binary / library | binário / biblioteca | livraria |
| embedded (Embedded MCP) / to embed | incorporado (MCP incorporado) / incorporar | embutido, embarcado, embedded |
| installation / to install | instalação / instalar | |
| documentation | documentação | |
| tutorial | tutorial (o) | |
| quick start | início rápido | quickstart |
| getting started | primeiros passos | |
| announcement | anúncio | |
| feature | funcionalidade | recurso (that is a resource), característica, feature |
| example | exemplo | |
| syntax / usage | sintaxe / uso | |
| note / tip / warning (admonition titles are translated by the theme) | nota / dica / aviso | |
| resource hierarchy | hierarquia de recursos | |
| access management | gerenciamento de acesso | gestão de acessos, administração de acesso |
| provider session | sessão de provider | |
| SQL backend | backend SQL | |
| query engine / parser | mecanismo de consulta / analisador sintático | motor de consulta, parser |
| wire protocol | protocolo de comunicação (protocolo de comunicação do PostgreSQL) | protocolo de fio |
| reverse proxy | proxy reverso | |
| local cache | cache local (o cache) | a cache |
| key-value pair | par chave-valor | |
| delimiter / separator | delimitador / separador | |
| bucket (object storage) / bucket (NTILE) | bucket (o) / grupo | balde, depósito |
| throw an error / return an error | gerar um erro / retornar um erro | lançar, disparar |
| return (a value) | retornar | devolver |
| well-formed / valid (JSON) | bem formado / válido | |
| file / state file | arquivo / arquivo de estado | ficheiro |
| directory (of principals, of plugins, filesystem) / folder | diretório / pasta | |
| computer, machine | computador, máquina | |
| screen / user / team | tela / usuário / equipe | ecrã / utilizador / equipa |
| app, application | aplicativo (an end-user app: aplicativo de desktop), aplicação (a server or web application) | |
| drift / infrastructure drift | desvio / desvio da infraestrutura | deriva, drift |
| cloud inventory, inventory | inventário de nuvem, inventário | |
| compliance | conformidade | compliance |
| provision (resources) | provisionar | aprovisionar |
| operator (the person running StackQL) | operador (o); the context separates it from a SQL operator | |
| caller | chamador (o) | invocador |
| filter (noun and verb) | filtro / filtrar | |
| concatenation | concatenação | |
| parse / plan (a statement) | analisar / planejar | parsear, planear |
| contract (I/O contract, safety contract) | contrato | |
| source of truth | fonte da verdade | |
| entitlement | direito de acesso | |
| snapshot / checksum / digest | snapshot (o) / soma de verificação / hash (o) | instantâneo, resumo (that is a summary) |
| estate / fingerprint | parque (parque de recursos na nuvem) / impressão digital | patrimônio |
| allowlist | lista de permissões | lista branca, whitelist |
| opt in / explicit opt-in | ativar explicitamente / ativação explícita | |
| enable / disable | habilitar / desabilitar | |
| support (a feature, a format) / supported | oferecer suporte a, ser compatível com / compatível, com suporte | suportar, suportado |
| hint (in an MCP error or tool annotation) | indicação | pista |
| advertise (a capability) | anunciar | |
| artifact (build or release output) | artefato | artefacto |
| stack (of resources) | stack (o) | pilha |
| pipeline | pipeline (o) | |
| notarized / signed | notarizado / assinado | |
| common table expression (CTE) | expressão de tabela comum (CTE) | |
| default / by default | valor padrão / por padrão | por default, por omissão, predefinido |
| array (JSON, data type) | array (o; plural arrays) | matriz, vetor, arranjo |
| policy (IAM, access policy) / IAM binding | política / vinculação do IAM | diretiva, binding |
| bind, binding (alias, secret store) | vincular, vinculação | |
| assign, assignment (`SET`) | atribuir, atribuição | |
| token (lexical) | token léxico | |
| pin (a version, a key, an issuer) | fixar; the heading "Pinning the revision" is "Fixar a revisão" | |
| exporter / receiver (OTel) / sink (audit) | exportador / receptor / destino | sumidouro |
| tenancy (the alias label) | tenancy (a) | |
| preview (a feature in preview) | em versão prévia | em pré-visualização, preview |
| legacy | legado | |
| batch / statement batch | lote / lote de instruções | |
| wildcard | curinga (caractere curinga) | coringa, wildcard |
| query hint | dica de consulta | |
| substring / pattern (regex) / regular expression | substring (a) / padrão / expressão regular | subcadeia |
| collating function / collating sequence | função de ordenação / sequência de ordenação | colação, agrupamento |
| timestamp | carimbo de data/hora | marca temporal, timestamp |
| current row / rank / running total (window functions) | linha atual / classificação / total acumulado | |
| installer | instalador | |
| workspace | espaço de trabalho | área de trabalho |
| monitoring / to monitor / monitor (noun) | monitoramento / monitorar / monitor | monitorização, monitorizar |
| run, execute / list / delete, remove / manage | executar / listar / excluir, remover / gerenciar | rodar / enumerar / deletar, apagar / gerir, administrar |
| framework | framework (o) | estrutura |
| capability (of an MCP client or server) | capacidade ("cliente MCP compatível com elicitação" for an elicitation-capable client) | recurso (that is a resource), funcionalidade |
| Query Library entry | item (o): "ID do item da Query Library" | entrada (that is an input; "Entradas" is the Inputs header) |
| job (CI, scheduled) | job (o; plural jobs): "job de CI agendado", "nome do job" | tarefa |
| container | contêiner | container |
| trace / span / trace id | trace (o) / span (o) / ID de trace | rastreamento |
| SQL engine / backing (SQL engine, server) | mecanismo SQL / subjacente | motor SQL |
| self-contained / prebuilt / launcher (`npx`, `uvx`) | autocontido / pré-compilado / inicializador | |
| to vendor (a binary, a dependency) | fazer o vendoring de; "vendored into your app" = incluído na sua aplicação por vendoring | |
| Cargo features (`sidecar`, `vendored`) | funcionalidades do Cargo | feature flags, opções |
| predicate pushdown | pushdown de predicados | |
| verbose mode | modo detalhado | modo verboso |
| floating point / Unix timestamp / Julian day | ponto flutuante / carimbo de data/hora Unix / dia juliano | |
| StackQL Provider Registry (link text to `/providers`) | unchanged, it is a name | Registro de providers do StackQL |
| Kept in English | backend, bundle (the `.mcpb`; the format name MCP Bundle too), sidecar, vendored, crate, worker, bearer (token bearer), dotenv, pull request, fork, issue, webhook, plugin, runtime, fan-out, proxy, SaaS, wrapper, skill, build, workflow, tag, handshake, log, hash, snapshot, cache, listener, loopback, harness, upstream, placeholder, fallback, staging, smoke test, toolchain, action (a GitHub action; plural actions), marketplace; third-party UI terms (Datadog facets, ChatGPT threads, GitHub stargazers and watchers) | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Brazilian Portuguese |
|---|---|
| Install | Instalação |
| Installing StackQL | Instalar o StackQL |
| Getting Started | Primeiros passos |
| Command Line Usage | Linha de comando |
| Language Specification | Especificação da linguagem |
| Functions | Funções |
| Developers | Desenvolvedores |
| Providers | Providers |
| Quick Starts | Guias de início rápido |
| AI Agents | Agentes de IA |
| MCP Server / MCP Tools / Embedded MCP | Servidor MCP / Ferramentas MCP / MCP incorporado |
| Blog | Blog |
| Product Announcements | Anúncios de produto |
| Provider Announcements | Anúncios de providers |
| Tutorials | Tutoriais |
| Documentation | Documentação |
| Contact us | Contato |
| Privacy Policy | Política de privacidade |
| Terms of Service | Termos de serviço |
| Support | Suporte |
| AI Reference | Referência de IA |
| More | Mais |
| Welcome to StackQL | Boas-vindas ao StackQL |

Recurring headings, phrases and keywords (the same on every page):

| English | Brazilian Portuguese |
|---|---|
| Syntax | Sintaxe |
| Arguments | Argumentos |
| Return Value(s) | Valor(es) retornado(s) |
| Example / Examples | Exemplo / Exemplos |
| Inputs / Output | Entradas / Saída |
| Flags | Opções |
| Overview | Visão geral |
| How it works | Como funciona |
| Modes | Modos |
| Gating | Controle |
| Install | Instalação |
| Quick start | Início rápido |
| Demo app | Aplicativo de demonstração |
| Repo link | Link do repositório |
| Vendoring / embedding | Vendoring / incorporação |
| Prerequisites | Pré-requisitos |
| Next steps | Próximos passos |
| Related | Veja também |
| See also: | Veja também: |
| See X. | Consulte X. |
| For more information, see X. | Para obter mais informações, consulte X. |
| Optional. / Defaults to X. | Opcional. / Valor padrão: X. |
| Returns X. (the sentence that opens a function or tool page) | Retorna X. (never "Devolve") |
| Return the absolute value of a number (an example heading that names a task) | Retornar o valor absoluto de um número (infinitive) |
| X function in StackQL: ... (description pattern) | Função X no StackQL: ... (a noun phrase or an infinitive after the colon, as in the source) |
| X window function in StackQL: ... (description pattern) | Função de janela X no StackQL: ... |
| X in StackQL: ... (description of a statement page) | X no StackQL: ... |
| ... using SQL in StackQL (description pattern) | ... com SQL no StackQL |
| Available in StackQL `vX` and later / releases from `vX` | Disponível a partir do StackQL `vX` |
| Available in StackQL releases after `vX` | Disponível nas versões do StackQL posteriores à `vX` |
| None. (no arguments, no inputs) | Nenhum. / Nenhuma. |
| Query / Results (tab labels), Output: | Consulta / Resultados, Saída: |
| Tested with (badge line on quick starts) | Testado com |
| Best Practices / Troubleshooting / Further reading | Práticas recomendadas / Solução de problemas / Leitura adicional |
| MCP tool listing X / MCP tool that does X (description pattern) | Ferramenta MCP que lista X / Ferramenta MCP que faz X |
| MCP overview (link text) | Visão geral do MCP |
| Argument / Required / Description (table headers) | Argumento / Obrigatório / Descrição |
| Renderer / Shape (table headers) | Renderizador / Formato |
| Table / One row per X (table cells) | Tabela / Uma linha por X |
| Yes / No (table cells) | Sim / Não |
| For example | Por exemplo |
| Aggregate function / Scalar function | Função de agregação / Função escalar |
| When to use, Use case / Example usage | Quando usar, Caso de uso / Exemplo de uso |
| Conclusion / Get started (homepage heading) | Conclusão / Comece agora |
| Returns : (a `<summary>` or bold label) | Retorna: |
| Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered: | Experimente este prompt em qualquer cliente MCP compatível com elicitação que tenha o servidor MCP do StackQL registrado: |
| Here's an example in Claude Desktop: | Veja um exemplo no Claude Desktop: |
| Server mode table cells: allow / refuse / needs approval; allowed / refused / (default) | permite / recusa / requer aprovação; permitido / recusado / (padrão) |
| Gated by the server mode. / Allowed in every server mode. Read-only. (the Gating section of an MCP tool page) | Controlado pelo modo do servidor. / Permitida em todos os modos do servidor. Somente leitura. |
| `embedded sql backend` / `postgres sql backend` (the "Tested with" badges on quick starts) | backend SQL incorporado / backend SQL postgres |
| Short "See also" badges that are not a page's exact title: StackQL Resource Hierarchy / Provider Sessions / Templating / Window Function Reference / X language spec | Hierarquia de recursos do StackQL / Sessões de provider / Uso de templates / Referência de funções de janela / X na especificação da linguagem |
| keyword `cloud inventory` | inventário de nuvem |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Headings in sentence case: a capital on the first word and on proper nouns only, never title case ("Variáveis de sessão", not "Variáveis De Sessão"). No end punctuation. Keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- Product and vendor names take the article Portuguese grammar asks for, contracted with the preposition as usual: o StackQL (do StackQL, no StackQL, ao StackQL, pelo StackQL), a AWS (da AWS, na AWS), o Azure, o Google Cloud, o GitHub, o Claude, o Docker, o PostgreSQL. A name that opens a title, a heading or a table cell can go without the article where English has none ("StackQL com o Claude Desktop" is fine; so is "Instalar o StackQL").
- Gender and number follow Portuguese usage even when the term is English: o provider, o stack, o shell, o backend, o bucket, o endpoint, o token, o prompt, o pipeline, o array, o join, o alias, o tenant, o log, o hash, o payload, o cache, o template, o snapshot, o plugin, o runtime, o workflow, o webhook, o build, o fork, o pull request; a API, a CLI, a URL, a string, a substring, a view, a role, a tag, a issue, a tenancy, a skill. An inline code span takes the gender of what it names (o comando `stackql exec`, a opção `--output`, o parâmetro `region`, a função `json_extract`). English nouns form the plural with -s (providers, tokens, endpoints).
- Contractions of `de`, `em`, `a` and `por` with the article apply before an English or code term as before any other noun (do provider, no endpoint, ao shell, pela API, da opção `--output`).
- Mesoclisis and enclisis of the European kind are not used; pronoun placement follows ordinary Brazilian written usage ("Ele se conecta ao servidor", "Isso permite que você...").
- Accented letters (á, â, ã, à, é, ê, í, ó, ô, õ, ú, ç) are ordinary Portuguese spelling and are kept, on capitals too (Índice, Área, É).
- No space before `:`, `;`, `?` or `!`. The word after a colon is lower case unless it is a name or starts a quoted sentence.
- Quotation marks are straight double quotes ("..."). No angle quotes, no typographic quotes and no typographic apostrophe, so the text stays greppable.
- No dash punctuation other than the keyboard hyphen. Where the source uses ` - ` or ` -- ` as a dash inside a sentence or between a term and its explanation, write ` - `, or use commas or parentheses. Never write an em dash, an en dash or a double hyphen in prose. Hyphens that belong to Portuguese spelling (palavra-chave, pré-requisito, chave-valor, and enclitic forms such as "conecte-se") are kept.
- No capital on common nouns after the first word of a sentence or label; names of languages, months and days are lower case. Product, provider and vendor names keep their own spelling.
- Prefer the simple verb to the padded one (use, not "faça uso de"; para, not "a fim de"). Prefer the active voice ("a consulta retorna uma linha") to the passive with `ser` ("uma linha é retornada pela consulta"). The gerund is ordinary Brazilian usage ("está executando").
- List items and table cells end the way the source does: no full stop added to fragments, a full stop where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English.
- Numbers, dates, versions and units as in the source; the decimal comma is not applied inside code or tables of command output.
- The `description` front matter stays one sentence and under about 170 characters (Portuguese runs 15 to 25 percent longer than English; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud inventory` -> inventário de nuvem); keep the number and order of entries.
- Front matter must stay valid YAML. Keep the quoting of the source value. A value that contains `: ` (a colon followed by a space) or ` #` must be in double quotes, as must a value that starts with a quote, bracket, backtick, `*`, `&`, `!`, `%` or `@`; escape any inner double quote as `\"`. The description pattern "Função X no StackQL: ..." always needs the double quotes the source already has.
- Strings in JSX and in exported data arrays keep the quote style of the source; a translated string that gains a double quote goes in single quotes, and the other way around. A single-quoted string cannot hold an apostrophe (rare in Portuguese, but "d'água" and the like): rephrase or switch the quotes.
- Bold and italic markers stay where the source has them, with no space inside the markers. Punctuation that follows a bold term stays outside the markers when the source has it outside.
- Link text that is another page's title is that page's translated `title`, including the "See also" badges that wrap the title in inline code (`` [[` Window Functions `]] `` becomes `` [[` Funções de janela `]] ``); `yarn i18n:verify` warns about the changed inline code on those pages, which is expected. Link text that is a heading of the target page is that heading's translation.
- Example prompts addressed to an AI assistant use the same você imperative as the rest of the page ("Mostre", "Crie", "Liste").
- The homepage tagline "Configuration-as-Data. Infrastructure-as-Context." stays in English.
- Portuguese text is longer than English, so a `length ratio` warning from `yarn i18n:verify` is not expected; if one appears, check for an omission or an addition. Short pages whose code fences carry padded result tables are the exception (see the es note in CLAUDE.md).

## id

Audience: Indonesian engineers reading technical documentation. Standard formal Indonesian (bahasa Indonesia baku) in the spelling of KBBI and EYD (Edisi V), the register of vendor documentation in Indonesian (Google Cloud, Microsoft Learn, AWS): never Malaysian Malay (fail, kemas kini, muat turun, pangkalan data, perisian, pelayan, rangkaian, boleh for "can", sila, cuba, semak, tetapan, padam) and never colloquial Indonesian (kamu, gak, nggak, aja, bikin, pakai for "use", gimana, udah, banget). Address the reader as Anda, always with a capital A, never kamu, kalian or Saudara; the possessive is "komputer Anda", never a clitic (-mu). Present tense, short sentences. Instructions use the plain imperative of the verb ("Jalankan perintah berikut", "Gunakan", "Lihat", "Instal"), without the particle -lah and without "silakan" or "harap"; task headings use the meN- verb ("Menginstal StackQL", "Membuat template"). Keep the English terms Indonesian developers use as a matter of course (the "Kept in English" row); do not coin Indonesian names for products or APIs. No marketing tone.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Indonesian | Not |
|---|---|---|
| provider (a StackQL provider) | provider (lower case in prose; no plural ending: "semua provider", "beberapa provider") | penyedia (that is the cloud vendor), providers, provider-provider |
| cloud provider (the vendor) | penyedia cloud | provider cloud, penyedia awan, penyedia layanan awan |
| vendor (the company behind an API or SDK, not only cloud) | vendor; "vendor-published" = dipublikasikan oleh vendor | pemasok, penjual |
| cloud (as an adjective: cloud resources, cloud infrastructure) | cloud, after the noun (sumber daya cloud, infrastruktur cloud) | awan, komputasi awan (except for "cloud computing" = komputasi cloud) |
| service | layanan | servis, jasa |
| resource | sumber daya | resource, sumberdaya |
| method | metode | method |
| operation | operasi | |
| query (noun) / to query | kueri / mengkueri; "run a query" = menjalankan kueri | query, permintaan (that is a request), mengquery, menanyakan |
| statement | pernyataan ("pernyataan `SELECT`") | statement, perintah (that is a command), kalimat |
| result set | kumpulan hasil | set hasil, himpunan hasil |
| row / column | baris / kolom | rekaman, record (for a row) |
| table | tabel | |
| view | tampilan ("tampilan `CREATE VIEW`" reads "tampilan yang dibuat dengan `CREATE VIEW`") | view, pandangan |
| materialized view | tampilan terwujud | tampilan termaterialisasi |
| window function | fungsi jendela | fungsi window, fungsi analitik |
| window frame | bingkai jendela | frame |
| aggregate function / scalar function | fungsi agregat / fungsi skalar | fungsi agregasi |
| function / expression / clause | fungsi / ekspresi / klausa | klausul |
| keyword / operator / predicate | kata kunci / operator / predikat | keyword |
| subquery / alias / join | subkueri / alias / join (`JOIN` where the source has code) | sub-kueri, sub kueri, penggabungan (that is concatenation) |
| partition / peer, peer group / tie (window functions) | partisi / baris setara, kelompok baris setara / seri ("baris yang seri", "dengan celah", "tanpa celah") | peer |
| offset (`LAG`, `LEAD`) | offset (the argument name `offset` stays as code) | pergeseran |
| target (of `SET` and `UNSET`) | target ("Target" as the heading) | tujuan (that is an audit sink) |
| override (a credential, a default) / dereference | mengganti, penggantian ("credential override" = penggantian kredensial) / dereferensi, mendereferensi | menimpa (that is overwrite), override |
| overwrite (a value, a key) | menimpa, ditimpa ("Timpa jika sudah ada?") | mengganti (that is override) |
| data type / literal / identifier | tipe data / literal / pengidentifikasi | jenis data, identifier |
| string / integer / boolean | string / bilangan bulat / boolean | untaian, integer (in prose) |
| whitespace | spasi kosong | spasi putih, whitespace |
| parameter / argument | parameter / argumen | |
| flag (CLI) / option | flag / opsi ("global flags" = flag global) | bendera, penanda |
| environment variable | variabel lingkungan | variabel environment, variabel env |
| field / key / value | field / kunci / nilai | bidang, kolom (that is a column) |
| schema | skema | |
| control plane / data plane | bidang kontrol / bidang data | control plane, data plane |
| agent, AI agent | agen, agen AI | agen kecerdasan buatan, agent |
| tool (an MCP tool) | alat ("alat MCP") | tool, perkakas |
| prompt (text sent to a model) | prompt | perintah (that is a command), petunjuk |
| approval prompt, elicitation prompt (a dialog shown to the user) | permintaan persetujuan, permintaan elisitasi | prompt persetujuan |
| elicitation (MCP) | elisitasi | penggalian |
| gating / gated (MCP) | pengendalian / dikendalikan; "gated by X" = dikendalikan oleh X; "gated writes" = operasi tulis yang memerlukan persetujuan | gating, pembatasan |
| approval / approve | persetujuan / menyetujui | |
| server mode, safety mode | mode server, mode keamanan | |
| read-only | hanya baca | baca-saja, read-only |
| write (noun, a mutating call) | operasi tulis | penulisan |
| mutation / mutating statement | mutasi / pernyataan mutasi | |
| lifecycle operation | operasi siklus hidup | operasi lifecycle |
| resource lifecycle | siklus hidup sumber daya | |
| log / audit log | log / log audit | catatan, logging (as a noun) |
| log record / completion record / row record (OTel output) | catatan log / catatan penyelesaian / catatan baris | rekaman |
| credentials | kredensial | kredensi, kredential |
| authentication / authorization | autentikasi / otorisasi | otentikasi |
| principal / service principal | prinsipal / prinsipal layanan | principal, entitas keamanan |
| subject / issuer / audience / claim (JWT, certificate identity) | subjek / penerbit / audiens / klaim | |
| membership (group, directory) | keanggotaan | |
| service account | akun layanan | akun servis |
| token / API key / access key | token / kunci API / kunci akses | API key |
| secret / secret store | rahasia / penyimpanan rahasia | secret, brankas |
| scope (OAuth, and of a variable or a setting) | cakupan | lingkup, scope |
| role / permission | peran / izin | role, ijin, hak (for a permission) |
| assume a role (AWS) | mengambil peran | mengasumsikan peran |
| session / session variable | sesi / variabel sesi | |
| deployment / to deploy | deployment / men-deploy, di-deploy | penyebaran, menyebarkan, deploy as a verb without the prefix, mendeploy |
| infrastructure as code | infrastruktur sebagai kode (IaC) | |
| registry (generic) / provider registry | registri / registri provider | registry, registrasi (that is a sign-up) |
| pull (a provider) | mengunduh; "pulled" = diunduh | menarik, mem-pull |
| download / upload | mengunduh, unduhan / mengunggah, unggahan | download as a verb, muat turun, muat naik |
| repository | repositori | repository |
| release | rilis (noun and verb: "merilis") | |
| command line | baris perintah | command line, garis perintah |
| shell / interactive shell | shell / shell interaktif | cangkang |
| server / client | server / klien | peladen, pelayan |
| transport (MCP) | transport | transportasi, angkutan |
| stateless / stateful | tanpa status / dengan status | stateless, stateful |
| endpoint | endpoint | titik akhir |
| request / response | permintaan / respons | request, tanggapan |
| request body | isi permintaan | badan permintaan |
| payload | payload | muatan |
| header (HTTP) | header | tajuk, kepala |
| pagination | paginasi | penomoran halaman |
| output / output format / output mode | output / format output / mode output | keluaran |
| template / templating | template / penggunaan template | templat, cetakan |
| model (AI) | model | |
| dry run | uji coba | dry run, simulasi kering |
| guardrail | batasan pengaman | guardrail, pagar pembatas |
| answer engine | mesin jawaban | answer engine |
| region / project / subscription / tenant | region / proyek / langganan / tenant | wilayah, project (in prose), penyewa |
| account | akun | |
| open source | sumber terbuka | open source, kode terbuka |
| binary / library | biner / library | binari, pustaka, perpustakaan |
| embedded (Embedded MCP) / to embed | tersemat (MCP tersemat) / menyematkan | tertanam, embedded |
| built-in | bawaan ("fungsi bawaan") | built-in, terintegrasi |
| installation / to install / installed | instalasi / menginstal / terinstal, diinstal | penginstalan, menginstall, menginstalasi |
| documentation | dokumentasi | |
| tutorial | tutorial | |
| quick start | panduan mulai cepat; "Quick start" as a heading = Mulai cepat | quickstart, panduan memulai cepat |
| getting started | memulai | |
| announcement | pengumuman | |
| feature | fitur | |
| example | contoh | |
| syntax / usage | sintaksis / penggunaan | sintaks, sintak |
| note / tip / warning (admonition titles are translated by the theme) | catatan / tips / peringatan | |
| resource hierarchy | hierarki sumber daya | hirarki |
| access management | manajemen akses | pengelolaan akses |
| provider session | sesi provider | |
| SQL backend | backend SQL | |
| query engine / parser | mesin kueri / parser | pengurai (for the component) |
| wire protocol | protokol komunikasi (protokol komunikasi PostgreSQL) | protokol kabel |
| reverse proxy | reverse proxy | proksi terbalik |
| local cache | cache lokal | tembolok |
| key-value pair | pasangan kunci-nilai | |
| delimiter / separator | pembatas / pemisah | delimiter |
| bucket (object storage) / bucket (NTILE) | bucket / kelompok | ember, wadah |
| throw an error / return an error | menghasilkan error / mengembalikan error | melempar error, galat, kesalahan (for a returned error object) |
| return (a value) | mengembalikan | menampilkan, memberikan, me-return |
| well-formed / valid (JSON) | berformat benar / valid | terbentuk dengan baik |
| file / state file | file / file status | berkas, fail |
| directory (of principals, of plugins, filesystem) / folder | direktori / folder | |
| computer, machine | komputer, mesin | |
| screen / user / team | layar / pengguna / tim | user (in prose) |
| app, application | aplikasi | apl, app |
| drift / infrastructure drift | penyimpangan / penyimpangan infrastruktur | drift, pergeseran |
| cloud inventory, inventory | inventaris cloud, inventaris | inventori |
| compliance | kepatuhan | compliance, komplians |
| provision (resources) | menyediakan, penyediaan | memprovisi, provisioning |
| operator (the person running StackQL) | operator; the context separates it from a SQL operator | |
| caller | pemanggil | |
| filter (noun and verb) | filter / memfilter | menyaring |
| concatenation | penggabungan | konkatenasi |
| parse / plan (a statement) | mengurai / merencanakan | mem-parse, parsing (as a verb) |
| contract (I/O contract, safety contract) | kontrak | |
| source of truth | sumber kebenaran | |
| entitlement | hak akses | |
| snapshot / checksum / digest | snapshot / checksum / hash | cuplikan, ringkasan (that is a summary) |
| estate / fingerprint | lingkungan (lingkungan cloud) / sidik jari | estate, aset |
| allowlist | daftar izin | whitelist, daftar putih |
| opt in / explicit opt-in | mengaktifkan secara eksplisit / aktivasi eksplisit | |
| enable / disable | mengaktifkan / menonaktifkan | menghidupkan, mematikan, non-aktifkan |
| support (a feature, a format) / supported | mendukung / didukung | men-support |
| hint (in an MCP error or tool annotation) | petunjuk | hint |
| advertise (a capability) | menyatakan | mengiklankan |
| artifact (build or release output) | artefak | artifak |
| stack (of resources) | stack | tumpukan |
| pipeline | pipeline | jalur pipa |
| notarized / signed | dinotarisasi / ditandatangani | |
| common table expression (CTE) | ekspresi tabel umum (CTE) | |
| default / by default | default / secara default | bawaan (that is built-in), standar |
| array (JSON, data type) | array | larik, deret |
| policy (IAM, access policy) / IAM binding | kebijakan / binding IAM | policy, pengikatan IAM |
| bind, binding (alias, secret store) | mengikat, pengikatan | |
| assign, assignment (`SET`) | menetapkan, penetapan | |
| token (lexical) | token leksikal | |
| pin (a version, a key, an issuer) | mengunci; the heading "Pinning the revision" is "Mengunci revisi" | menyematkan (that is embed), pin |
| exporter / receiver (OTel) / sink (audit) | exporter / receiver / tujuan | pengekspor, penerima |
| tenancy (the alias label) | tenancy | |
| preview (a feature in preview) | dalam pratinjau | preview, pratayang |
| legacy | lama | legacy, warisan |
| batch / statement batch | batch / batch pernyataan | kelompok |
| wildcard | karakter pengganti | wildcard, karakter liar |
| query hint | petunjuk kueri | |
| substring / pattern (regex) / regular expression | substring / pola / ekspresi reguler | sub-string, regex (in prose, except in names) |
| collating function / collating sequence | fungsi kolasi / urutan kolasi | |
| timestamp | stempel waktu | cap waktu, timestamp |
| current row / rank / running total (window functions) | baris saat ini / peringkat / total berjalan | |
| installer | penginstal | installer |
| workspace | ruang kerja | workspace |
| monitoring / to monitor / monitor (noun) | pemantauan / memantau / monitor | monitoring, memonitor |
| run, execute / list / delete, remove / manage | menjalankan / mencantumkan / menghapus / mengelola | mengeksekusi (except "eksekusi" for a statement's execution), me-list, mendelete |
| framework | framework | kerangka kerja |
| capability (of an MCP client or server) | kemampuan ("klien MCP yang mendukung elisitasi" for an elicitation-capable client) | kapabilitas, fitur |
| Query Library entry | entri ("ID entri Query Library") | item, input (that is an input) |
| job (CI, scheduled) | job ("job CI terjadwal", "nama job") | tugas, pekerjaan |
| container | container | kontainer, wadah |
| trace / span / trace id | trace / span / ID trace | jejak |
| SQL engine / backing (SQL engine, server) | mesin SQL / yang mendasari | |
| self-contained / prebuilt / launcher (`npx`, `uvx`) | mandiri / siap pakai / peluncur | prebuilt |
| to vendor (a binary, a dependency) | melakukan vendoring; "vendored into your app" = disertakan dalam aplikasi Anda melalui vendoring | |
| Cargo features (`sidecar`, `vendored`) | fitur Cargo | feature flag |
| predicate pushdown | pushdown predikat | |
| verbose mode | mode verbose | mode bertele-tele, mode rinci |
| floating point / Unix timestamp / Julian day | floating point / stempel waktu Unix / hari Julian | titik mengambang |
| update (noun and verb) / settings / configuration | pembaruan, memperbarui / pengaturan / konfigurasi | update, mengupdate, kemas kini, tetapan, setelan |
| database / software / network / password | database / perangkat lunak / jaringan / kata sandi | basis data, pangkalan data, perisian, rangkaian, kata laluan |
| link (noun) / page / website | tautan / halaman / situs web | link, pranala, website |
| custom | kustom | custom, tersuai |
| interface / object / instance | antarmuka / objek / instance | antar muka, interface, obyek, instans |
| script / browser / plugin | skrip / browser / plugin | script, peramban |
| build (noun) / to build | build / membangun | membuild, mem-build |
| path (file, JSON, module, root) | jalur ("jalur file", "jalur JSON", "ekspresi jalur JSON", "jalur root"); the `PATH` variable and argument names such as `path` stay as code | path (in prose) |
| nested | bersarang | bertingkat |
| real-time | waktu nyata ("dalam waktu nyata") | real-time |
| executable (noun) | file executable; self-contained executable = file executable mandiri; after a code span "`stackql` yang dapat dieksekusi" | |
| agentic | agentik ("workflow agentik", "rekayasa platform agentik") | |
| subcommand / restart / reset | subperintah / memulai ulang / mengatur ulang | |
| encoding / encoded | pengodean / dikodekan ("dikodekan URL") | encoding, enkode |
| privileges / least privilege | hak istimewa / hak istimewa minimum | privilese |
| dependency / relation | dependensi / relasi | |
| resolve (a variable, an alias, credentials) | me-resolve, di-resolve; "resolves to" = menjadi | memecahkan |
| refresh (a materialized view) | menyegarkan | me-refresh |
| blocking / non-blocking (invocation) | memblokir / tidak memblokir | |
| listing (directory, registry, marketplace) | entri | daftar (that is a list) |
| event / alert / notification | peristiwa / peringatan / notifikasi | event |
| engineer, data engineer | engineer, data engineer | insinyur |
| hosted / self-host | di-hosting / meng-host sendiri | dihosting |
| escape (a character, a string) | meng-escape, di-escape; "escape sequence" = urutan escape | |
| datetime value / modifier (date functions) | nilai datetime / pengubah | |
| regex: character class / quantifier / capture group / backreference / occurrence / to match | kelas karakter / kuantifier / grup tangkapan / referensi balik / kemunculan / mencocokkan, cocok dengan | |
| Unicode code point / character code | titik kode Unicode / kode karakter | |
| arc cosine, arc sine, arc tangent / hyperbolic | arkus kosinus, arkus sinus, arkus tangen / hiperbolik ("kosinus hiperbolik 1" in example headings, without "dari") | |
| StackQL Provider Registry (link text to `/providers`) | unchanged, it is a name | Registri provider StackQL |
| Kept in English | backend, bundle (the `.mcpb`; the format name MCP Bundle too), sidecar, vendored, crate, worker, bearer (token bearer), dotenv, pull request, fork, issue, webhook, plugin, runtime, fan-out, proxy, SaaS, wrapper, skill, build, workflow, tag, handshake, log, hash, snapshot, cache, listener, loopback, harness, upstream, placeholder, fallback, staging, smoke test, toolchain, action (a GitHub action), marketplace, endpoint, payload, header, flag, field, offset, stack, pipeline, framework, library, container, region, tenant, bucket, array, string, boolean, prompt, error; third-party UI terms (Datadog facets, ChatGPT threads, GitHub stargazers and watchers) | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Indonesian |
|---|---|
| Install | Instalasi |
| Installing StackQL | Menginstal StackQL |
| Getting Started | Memulai |
| Command Line Usage | Baris perintah |
| Language Specification | Spesifikasi bahasa |
| Built-in Functions | Fungsi bawaan |
| Functions | Fungsi |
| Developers | Pengembang |
| Providers | Provider |
| Quick Starts | Panduan mulai cepat |
| AI Agents | Agen AI |
| MCP Server / MCP Tools / Embedded MCP | Server MCP / Alat MCP / MCP tersemat |
| Embedded (sidebar category) | Tersemat |
| Blog | Blog |
| Product Announcements | Pengumuman produk |
| Provider Announcements | Pengumuman provider |
| Tutorials | Tutorial |
| Documentation | Dokumentasi |
| Contact us | Hubungi kami |
| Privacy Policy | Kebijakan privasi |
| Terms of Service | Ketentuan layanan |
| Support | Dukungan |
| AI Reference | Referensi AI |
| More | Lainnya |
| Welcome to StackQL | Selamat datang di StackQL |

Recurring headings, phrases and keywords (the same on every page):

| English | Indonesian |
|---|---|
| Syntax | Sintaksis |
| Arguments | Argumen |
| Return Value(s) | Nilai yang dikembalikan |
| Example / Examples | Contoh (one word for both) |
| Inputs / Output | Input / Output |
| Flags | Flag |
| Overview | Ringkasan |
| How it works | Cara kerja |
| Modes | Mode |
| Gating | Pengendalian |
| Install | Instalasi |
| Quick start | Mulai cepat |
| Demo app | Aplikasi demo |
| Repo link | Tautan repositori |
| Vendoring / embedding | Vendoring / penyematan |
| Prerequisites | Prasyarat |
| Next steps | Langkah berikutnya |
| Related | Terkait |
| See also: | Lihat juga: |
| See X. | Lihat X. |
| For more information, see X. | Untuk informasi selengkapnya, lihat X. |
| Optional. / Defaults to X. | Opsional. / Nilai default: X. |
| Returns X. (the sentence that opens a function or tool page) | Mengembalikan X. |
| Return the absolute value of a number (an example heading that names a task) | Mengembalikan nilai absolut suatu bilangan (meN- verb) |
| X function in StackQL: ... (description pattern) | Fungsi X di StackQL: ... (a noun phrase or a meN- verb after the colon, as in the source) |
| X window function in StackQL: ... (description pattern) | Fungsi jendela X di StackQL: ... |
| X in StackQL: ... (description of a statement page) | X di StackQL: ... |
| ... using SQL in StackQL (description pattern) | ... menggunakan SQL di StackQL |
| Available in StackQL `vX` and later / releases from `vX` | Tersedia mulai StackQL `vX` |
| Available in StackQL releases after `vX` | Tersedia di rilis StackQL setelah `vX` |
| None. (no arguments, no inputs) | Tidak ada. |
| Query / Results (tab labels), Output: | Kueri / Hasil, Output: |
| Tested with (badge line on quick starts) | Diuji dengan |
| Best Practices / Troubleshooting / Further reading | Praktik terbaik / Pemecahan masalah / Bacaan lebih lanjut |
| MCP tool listing X / MCP tool that does X (description pattern) | Alat MCP yang mencantumkan X / Alat MCP yang melakukan X |
| MCP overview (link text) | Ringkasan MCP |
| Argument / Required / Description (table headers) | Argumen / Wajib / Deskripsi |
| Renderer / Shape (table headers) | Perender / Bentuk |
| Table / One row per X (table cells) | Tabel / Satu baris per X |
| Yes / No (table cells) | Ya / Tidak |
| For example | Misalnya |
| Aggregate function / Scalar function | Fungsi agregat / Fungsi skalar |
| When to use, Use case / Example usage | Kapan digunakan, Kasus penggunaan / Contoh penggunaan |
| Conclusion / Get started (homepage heading) | Kesimpulan / Mulai sekarang |
| Returns : (a `<summary>` or bold label) | Mengembalikan: |
| Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered: | Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL: |
| Here's an example in Claude Desktop: | Berikut contohnya di Claude Desktop: |
| Server mode table cells: allow / refuse / needs approval; allowed / refused / (default) | izinkan / tolak / perlu persetujuan; diizinkan / ditolak / (default) |
| Gated by the server mode. / Allowed in every server mode. Read-only. (the Gating section of an MCP tool page) | Dikendalikan oleh mode server. / Diizinkan di semua mode server. Hanya baca. |
| `embedded sql backend` / `postgres sql backend` (the "Tested with" badges on quick starts) | backend SQL tersemat / backend SQL postgres |
| Short "See also" badges that are not a page's exact title: StackQL Resource Hierarchy / Provider Sessions / Templating / Window Function Reference / X language spec | Hierarki sumber daya StackQL / Sesi provider / Penggunaan template / Referensi fungsi jendela / X di spesifikasi bahasa |
| keyword `cloud inventory` | inventaris cloud |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Headings in sentence case: a capital on the first word and on proper nouns only, never title case ("Variabel sesi", not "Variabel Sesi"). No end punctuation. Keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- Word order is head first: the noun comes before what modifies it ("kunci API", "variabel lingkungan", "provider StackQL", "server MCP StackQL", "fungsi jendela `RANK`"). An inline code span that names a thing takes its class noun before it (perintah `stackql exec`, flag `--output`, parameter `region`, fungsi `json_extract`, pernyataan `SELECT`).
- Nouns are not pluralized. An English loanword never takes -s ("semua provider", "beberapa token", "tiga endpoint"), and words are not reduplicated to mark the plural where context or a quantifier (semua, beberapa, setiap, banyak) already does; never reduplicate an English word or a code span. Do not use "para" for things.
- Affixes: the preposition `di` and `ke` are written apart from the noun ("di server", "ke direktori"), the passive prefix `di-` is attached to the verb ("dijalankan", "diinstal"); "di mana" is two words. An Indonesian affix on an English word that is not absorbed into Indonesian takes a hyphen, per EYD ("men-deploy", "di-deploy"); never put an affix or the suffix -nya on inline code, a link or a bold term, rephrase instead ("nilai `x`", not "`x`-nya").
- No anglicisms in relative clauses: "yang", not "di mana" or "yang mana", introduces a relative clause ("tabel yang berisi...", not "tabel di mana..."). "daripada" only in comparisons.
- Formal word choice: "dapat" rather than "bisa" in prose, "tidak" (never "gak", "nggak", "enggak"), "untuk" (never "buat" for "for"), "menggunakan" (never "pakai"), "membuat" (never "bikin"), "saja" (never "aja"), "sudah" or "telah" (never "udah").
- KBBI spelling: praktik, aktivitas, analisis, objek, standar, risiko, hierarki, izin, silakan, sistem, kategori, efektif, otomatis, nonaktif, antarmuka, memengaruhi, mengubah, subkueri, lintas region; never praktek, aktifitas, analisa, obyek, standard, resiko, hirarki, ijin, silahkan, non-aktif, antar muka, mempengaruhi, merubah.
- "Anda" always with a capital A. "kita" (inclusive) where the source speaks as "we" in a walkthrough ("we will create a view" = "kita akan membuat tampilan"); "kami" where "we" is StackQL the project or company ("we recommend" = "kami menyarankan").
- The active and the di- passive are both natural in Indonesian technical writing; use whichever reads naturally, and keep the subject of the English sentence where the meaning depends on it (who returns what, who must approve).
- Lists of three or more items take a comma before "dan" or "atau" ("baris, kolom, dan tabel"), per EYD.
- English loanwords and English terms from the "Kept in English" row are written plain, never in italics and never in quotes: added italics change the page structure that `yarn i18n:verify` and the node-count check compare.
- No space before `:`, `;`, `?` or `!`. The word after a colon is lower case unless it is a name or starts a quoted sentence.
- Quotation marks are straight double quotes ("..."). No typographic quotes, no typographic apostrophe, no angle quotes, so the text stays greppable.
- No dash punctuation other than the keyboard hyphen. Where the source uses ` - ` or ` -- ` as a dash inside a sentence or between a term and its explanation, write ` - `, or use commas or parentheses. Never write an em dash, an en dash or a double hyphen in prose. Hyphens that belong to Indonesian spelling (kunci-nilai, reduplication such as "masing-masing", "men-deploy") are kept.
- No capital on common nouns after the first word of a sentence or label; names of languages, months and days are capitalized as in Indonesian (bahasa Inggris, Januari, Senin). Product, provider and vendor names keep their own spelling.
- Prefer the simple verb to the padded one ("gunakan", not "lakukan penggunaan"; "untuk", not "dalam rangka untuk"). Avoid "adalah" where the sentence reads without it, and do not open sentences with "Dimana" or "Yang mana".
- List items and table cells end the way the source does: no full stop added to fragments, a full stop where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English.
- Numbers, dates, versions and units as in the source; the Indonesian decimal comma is not applied inside code or tables of command output, and prose numbers follow the source.
- The `description` front matter stays one sentence and under about 170 characters (Indonesian runs 10 to 20 percent longer than English; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud inventory` -> inventaris cloud); keep the number and order of entries.
- Front matter must stay valid YAML. Keep the quoting of the source value. A value that contains `: ` (a colon followed by a space) or ` #` must be in double quotes, as must a value that starts with a quote, bracket, backtick, `*`, `&`, `!`, `%` or `@`; escape any inner double quote as `\"`. The description pattern "Fungsi X di StackQL: ..." always needs the double quotes the source already has.
- Strings in JSX and in exported data arrays keep the quote style of the source; a translated string that gains a double quote goes in single quotes, and the other way around. A single-quoted string cannot hold an apostrophe (rare in Indonesian, except in names such as Jum'at; rephrase or switch the quotes).
- Bold and italic markers stay where the source has them, with no space inside the markers. Punctuation that follows a bold term stays outside the markers when the source has it outside. A closing `**` must not be followed directly by a letter when it follows punctuation or inline code (it would not close in CommonMark), and a closing `__` or `_` must never be followed directly by a letter; leave a space or punctuation after the marker.
- Link text that is another page's title is that page's translated `title`, including the "See also" badges that wrap the title in inline code (`` [[` Window Functions `]] `` becomes `` [[` Fungsi jendela `]] ``); `yarn i18n:verify` warns about the changed inline code on those pages, which is expected. Link text that is a heading of the target page is that heading's translation.
- Example prompts addressed to an AI assistant use the same plain imperative as the rest of the page ("Tampilkan", "Buat", "Cantumkan").
- The homepage tagline "Configuration-as-Data. Infrastructure-as-Context." stays in English.
- Indonesian is a little longer than English, so a `length ratio` warning from `yarn i18n:verify` is not expected; if one appears, check for an omission or an addition. Short pages whose code fences carry padded result tables are the exception (see the es note in CLAUDE.md).

## vi

Audience: Vietnamese engineers reading technical documentation. Standard written Vietnamese in the register of vendor documentation in Vietnamese (Microsoft Learn, Google Cloud, AWS): neutral, precise, no regional or colloquial forms (không có "ko", "đc", "dc", "mình" for the reader, "nha", "nhé", "ạ"). Address the reader as "bạn" (lower case inside a sentence), never "quý vị", "anh/chị" or "các bạn"; the possessive is "máy tính của bạn". Present tense, short sentences. Instructions use the plain imperative ("Chạy lệnh sau", "Sử dụng", "Xem", "Cài đặt"); "hãy" only where a bare verb would read abruptly (opening an instruction paragraph, "Để biết thêm thông tin, hãy xem X."), never "vui lòng" in documentation prose. Task headings are verb phrases ("Cài đặt StackQL", "Tạo template"). Keep the English terms Vietnamese developers use as a matter of course (the "Kept in English" row); do not coin Vietnamese names for products or APIs. No marketing tone.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Vietnamese | Not |
|---|---|---|
| provider (a StackQL provider) | provider (lower case in prose; no plural ending: "các provider", "mọi provider") | nhà cung cấp (that is the cloud vendor), providers |
| cloud provider (the vendor) | nhà cung cấp đám mây | provider đám mây, nhà cung cấp cloud |
| vendor (the company behind an API or SDK, not only cloud) | nhà cung cấp; "vendor-published" = do nhà cung cấp phát hành | vendor (in prose), người bán |
| cloud (as an adjective: cloud resources, cloud infrastructure) | đám mây, after the noun (tài nguyên đám mây, hạ tầng đám mây) | cloud (in prose), mây |
| service | dịch vụ | service |
| resource | tài nguyên | resource |
| method | phương thức | method, phương pháp |
| operation | thao tác | hoạt động (that is activity), operation |
| query (noun) / to query | truy vấn (noun and verb); "run a query" = chạy truy vấn | query, câu hỏi |
| statement | câu lệnh ("câu lệnh `SELECT`") | lệnh (that is a command), phát biểu, statement |
| command (CLI) / subcommand | lệnh ("lệnh `stackql exec`") / lệnh con | câu lệnh (that is a statement) |
| result set | tập kết quả | bộ kết quả, tập hợp kết quả |
| row / column | hàng / cột | dòng (that is a line of text), bản ghi (for a row) |
| line (of text, of output) | dòng | hàng |
| table | bảng | |
| view | dạng xem ("dạng xem `CREATE VIEW`" reads "dạng xem được tạo bằng `CREATE VIEW`") | view (in prose), khung nhìn |
| materialized view | dạng xem vật chất hóa | dạng xem cụ thể hóa, materialized view (in prose) |
| window function | hàm cửa sổ | hàm window, hàm phân tích |
| window frame | khung cửa sổ | frame |
| aggregate function / scalar function | hàm tổng hợp / hàm vô hướng | hàm gộp, hàm kết tập |
| function / expression / clause | hàm / biểu thức / mệnh đề | chức năng (that is a feature) |
| keyword / operator / predicate | từ khóa / toán tử / vị từ | keyword |
| subquery / alias / join | truy vấn con / bí danh / phép nối (`JOIN` where the source has code) | subquery, alias (in prose) |
| partition / peer, peer group / tie (window functions) | phân vùng / hàng ngang hàng, nhóm hàng ngang hàng / đồng hạng ("các hàng đồng hạng", "có khoảng trống", "không có khoảng trống") | peer |
| offset (`LAG`, `LEAD`) | độ lệch (the argument name `offset` stays as code) | offset (in prose) |
| target (of `SET` and `UNSET`) | đích ("Đích" as the heading) | mục tiêu |
| override (a credential, a default) / overwrite (a value, a key) | ghi đè for both ("credential override" = ghi đè thông tin xác thực; "Ghi đè nếu đã tồn tại?") | override, thay đè |
| dereference | giải tham chiếu | |
| data type / literal / identifier | kiểu dữ liệu / literal ("literal chuỗi", "literal số") / định danh | loại dữ liệu, hằng (for a literal), identifier |
| string / integer / boolean | chuỗi / số nguyên / boolean | string (in prose), xâu |
| whitespace | khoảng trắng | ký tự trắng, whitespace |
| parameter / argument | tham số / đối số | |
| flag (CLI) / option | cờ / tùy chọn ("global flags" = cờ toàn cục; "cờ `--output`") | flag (in prose), lá cờ |
| environment variable | biến môi trường | biến env |
| field / key / value | trường / khóa / giá trị | field, key (in prose) |
| schema | lược đồ | schema (in prose) |
| control plane / data plane | mặt phẳng điều khiển / mặt phẳng dữ liệu | control plane, data plane |
| agent, AI agent | tác nhân, tác nhân AI | agent, đại lý |
| tool (an MCP tool) | công cụ ("công cụ MCP") | tool |
| prompt (text sent to a model) | lời nhắc | prompt (in prose), dấu nhắc (that is a shell prompt) |
| approval prompt (a dialog shown to the user) | yêu cầu phê duyệt | lời nhắc phê duyệt |
| elicitation (MCP) / elicitation prompt | elicitation (the MCP feature name, kept) / yêu cầu elicitation; "elicitation-capable client" = ứng dụng khách MCP hỗ trợ elicitation | gợi hỏi, khơi gợi |
| gating / gated (MCP) | kiểm soát / được kiểm soát; "gated by X" = được kiểm soát bởi X; "gated writes" = các thao tác ghi cần phê duyệt | gating, chặn cổng |
| approval / approve | phê duyệt | chấp thuận |
| server mode, safety mode | chế độ máy chủ, chế độ an toàn | |
| read-only | chỉ đọc | |
| write (noun, a mutating call) | thao tác ghi | việc viết |
| mutation / mutating statement | thao tác thay đổi dữ liệu / câu lệnh thay đổi dữ liệu | đột biến, mutation |
| lifecycle operation | thao tác vòng đời | thao tác lifecycle |
| resource lifecycle | vòng đời tài nguyên | |
| log / audit log | nhật ký / nhật ký kiểm tra | log (in prose), sổ ghi |
| log record / completion record / row record (OTel output) | bản ghi nhật ký / bản ghi hoàn tất / bản ghi hàng | |
| credentials | thông tin xác thực | chứng chỉ (that is a certificate), credentials |
| authentication / authorization | xác thực / ủy quyền | chứng thực, cấp quyền |
| principal / service principal | principal / service principal (kept, Azure and IAM terms) | chủ thể bảo mật |
| subject / issuer / audience / claim (JWT, certificate identity) | chủ thể / bên phát hành / đối tượng nhận / claim | |
| membership (group, directory) | tư cách thành viên | |
| service account | tài khoản dịch vụ | |
| token / API key / access key | token / khóa API / khóa truy cập | mã thông báo, API key |
| secret / secret store | bí mật / kho bí mật | secret (in prose), mật mã |
| scope (OAuth, and of a variable or a setting) | phạm vi | scope |
| role / permission | vai trò / quyền | role, sự cho phép |
| assume a role (AWS) | đảm nhận vai trò | giả định vai trò |
| session / session variable | phiên / biến phiên | session |
| deployment / to deploy | triển khai (noun and verb) | deploy (in prose), phát triển (that is develop) |
| infrastructure / infrastructure as code | hạ tầng / hạ tầng dưới dạng mã (IaC) | cơ sở hạ tầng, hạ tầng như mã |
| registry (generic) / provider registry | registry / registry provider | sổ đăng ký, kho đăng ký |
| pull (a provider) | tải xuống; "pulled" = đã tải xuống | kéo, pull (in prose) |
| download / upload | tải xuống / tải lên | download, tải về |
| repository | kho lưu trữ | repository, repo (in prose) |
| release | bản phát hành; to release = phát hành | release |
| command line | dòng lệnh | command line |
| shell / interactive shell | shell / shell tương tác | vỏ |
| server / client | máy chủ / ứng dụng khách ("máy chủ MCP", "ứng dụng khách MCP") | server, client (in prose), máy khách (that is a client machine) |
| transport (MCP) | phương thức truyền tải ("phương thức truyền tải stdio") | giao vận, transport |
| stateless / stateful | phi trạng thái / có trạng thái | không trạng thái, stateless |
| endpoint | điểm cuối | endpoint |
| request / response | yêu cầu / phản hồi | request, đáp ứng |
| request body | phần thân yêu cầu | body |
| payload | payload | tải trọng |
| header (HTTP) | header | tiêu đề (that is a heading or a title) |
| pagination | phân trang | |
| output / output format / output mode | đầu ra / định dạng đầu ra / chế độ đầu ra | output, kết xuất |
| input | đầu vào | input |
| template / templating | template / sử dụng template | mẫu (that is a regex pattern), khuôn mẫu |
| model (AI) | mô hình | model |
| dry run | chạy thử | dry run, chạy khô |
| guardrail | cơ chế bảo vệ | guardrail, lan can |
| answer engine | công cụ trả lời | answer engine |
| region / project / subscription / tenant | khu vực / dự án / gói đăng ký / tenant | vùng, region (in prose), project (in prose), người thuê |
| account | tài khoản | |
| open source | mã nguồn mở | open source |
| binary / library | tệp nhị phân / thư viện | binary (in prose), library |
| embedded (Embedded MCP) / to embed | nhúng (MCP nhúng) / nhúng | được nhúng sẵn, embedded |
| built-in | tích hợp sẵn ("hàm tích hợp sẵn") | dựng sẵn, built-in, có sẵn |
| installation / to install / installed | cài đặt (noun and verb) / đã cài đặt | install (in prose), lắp đặt |
| settings / configuration | thiết lập / cấu hình | cài đặt (that is installation), setting (in prose) |
| documentation | tài liệu | tư liệu |
| tutorial | hướng dẫn | tutorial (in prose) |
| quick start | hướng dẫn bắt đầu nhanh; "Quick start" as a heading = Bắt đầu nhanh | quickstart |
| getting started | bắt đầu | khởi đầu |
| announcement | thông báo | |
| feature | tính năng | chức năng (that is a function) |
| example | ví dụ | mẫu (that is a pattern or a template) |
| syntax / usage | cú pháp / cách dùng | |
| note / tip / warning (admonition titles are translated by the theme) | ghi chú / mẹo / cảnh báo | |
| resource hierarchy | phân cấp tài nguyên | hệ thống phân cấp tài nguyên |
| access management | quản lý truy cập | |
| provider session | phiên provider | |
| SQL backend | backend SQL | phụ trợ SQL |
| query engine / parser | công cụ truy vấn / trình phân tích cú pháp | bộ máy truy vấn, parser (in prose) |
| wire protocol | giao thức wire ("giao thức wire của PostgreSQL") | giao thức dây |
| reverse proxy | reverse proxy | proxy ngược |
| local cache | bộ nhớ đệm cục bộ | cache cục bộ |
| key-value pair | cặp khóa-giá trị | |
| delimiter / separator | dấu phân cách (both) | delimiter |
| bucket (object storage) / bucket (NTILE) | bucket / nhóm | xô, thùng |
| throw an error / return an error | báo lỗi / trả về lỗi | ném lỗi, quăng lỗi |
| return (a value) | trả về | hoàn trả, return |
| well-formed / valid (JSON) | đúng định dạng / hợp lệ | |
| file / state file | tệp / tệp trạng thái | file (in prose), tập tin |
| directory (of principals, of plugins, filesystem) / folder | thư mục (both) | directory |
| computer, machine | máy tính, máy | |
| screen / user / team | màn hình / người dùng / nhóm | user (in prose) |
| app, application | ứng dụng | app (in prose) |
| drift / infrastructure drift | sai lệch / sai lệch hạ tầng | drift, độ trôi |
| cloud inventory, inventory | kiểm kê đám mây, kiểm kê ("kiểm kê tài nguyên") | inventory, hàng tồn kho |
| compliance | tuân thủ | compliance |
| provision (resources) | cấp phát | cung cấp, provision |
| operator (the person running StackQL) | người vận hành | operator (in prose), toán tử (that is a SQL operator) |
| caller | bên gọi | người gọi |
| filter (noun and verb) | bộ lọc / lọc | |
| concatenation | nối chuỗi ("toán tử nối chuỗi") | ghép nối, phép nối (that is a join) |
| parse / plan (a statement) | phân tích cú pháp / lập kế hoạch | parse (in prose) |
| contract (I/O contract, safety contract) | hợp đồng ("hợp đồng I/O", "hợp đồng an toàn") | contract |
| source of truth | nguồn dữ liệu gốc | nguồn sự thật |
| entitlement | quyền sử dụng | |
| snapshot / checksum / digest | ảnh chụp nhanh / checksum / hash | snapshot (in prose), bản tóm tắt |
| estate / fingerprint | môi trường ("môi trường đám mây") / dấu vân tay | bất động sản, tài sản |
| allowlist | danh sách cho phép | whitelist, danh sách trắng |
| opt in / explicit opt-in | chủ động bật / bật một cách rõ ràng | |
| enable / disable | bật / tắt | kích hoạt, vô hiệu hóa (except when the source says "deactivate") |
| support (a feature, a format) / supported | hỗ trợ / được hỗ trợ | support |
| hint (in an MCP error or tool annotation) | gợi ý | hint |
| advertise (a capability) | công bố | quảng cáo |
| artifact (build or release output) | artifact | tạo phẩm, hiện vật |
| stack (of resources) | stack | ngăn xếp (that is the data structure) |
| pipeline | pipeline | đường ống |
| notarized / signed | được công chứng / được ký | |
| common table expression (CTE) | biểu thức bảng chung (CTE) | |
| default / by default | mặc định / theo mặc định | default |
| array (JSON, data type) | mảng | array (in prose) |
| policy (IAM, access policy) / IAM binding | chính sách / liên kết IAM | policy, ràng buộc IAM |
| bind, binding (alias, secret store) | liên kết | ràng buộc |
| assign, assignment (`SET`) | gán, phép gán | |
| token (lexical) | token từ vựng | |
| pin (a version, a key, an issuer) | ghim; the heading "Pinning the revision" is "Ghim phiên bản sửa đổi" | pin, cố định |
| exporter / receiver (OTel) / sink (audit) | exporter / receiver / đích | bộ xuất, bộ nhận |
| tenancy (the alias label) | tenancy | |
| preview (a feature in preview) | bản xem trước ("đang ở giai đoạn xem trước") | preview |
| legacy | cũ | legacy, di sản |
| batch / statement batch | lô / lô câu lệnh | batch (in prose), mẻ |
| wildcard | ký tự đại diện | wildcard |
| query hint | gợi ý truy vấn | |
| substring / pattern (regex) / regular expression | chuỗi con / mẫu / biểu thức chính quy | regex (in prose, except in names), xâu con |
| collating function / collating sequence | hàm đối chiếu / thứ tự đối chiếu | |
| timestamp | dấu thời gian | tem thời gian, timestamp (in prose) |
| current row / rank / running total (window functions) | hàng hiện tại / thứ hạng / tổng lũy kế | |
| installer | trình cài đặt | installer |
| workspace | không gian làm việc | workspace |
| monitoring / to monitor / monitor (noun) | giám sát (noun and verb) / trình giám sát | theo dõi, monitor (in prose) |
| run / execute | chạy / thực thi | |
| list (verb) / delete, remove / manage | liệt kê / xóa / quản lý | gỡ (except for uninstall) |
| framework | framework | khung, khuôn khổ |
| capability (of an MCP client or server) | khả năng ("ứng dụng khách MCP hỗ trợ elicitation" for an elicitation-capable client) | năng lực, capability |
| Query Library entry | mục ("ID mục Query Library") | bài, đầu vào |
| job (CI, scheduled) | job ("job CI định kỳ", "tên job") | công việc, tác vụ |
| task (generic) | tác vụ | |
| container | container | vùng chứa, thùng chứa |
| trace / span / trace id | trace / span / ID trace | dấu vết |
| SQL engine / backing (SQL engine, server) | công cụ SQL / bên dưới ("công cụ SQL bên dưới") | |
| self-contained / prebuilt / launcher (`npx`, `uvx`) | độc lập / dựng sẵn / trình khởi chạy | prebuilt |
| to vendor (a binary, a dependency) | vendoring ("đưa vào ứng dụng của bạn theo kiểu vendoring") | |
| Cargo features (`sidecar`, `vendored`) | tính năng Cargo | feature flag |
| predicate pushdown | đẩy vị từ xuống (predicate pushdown) | |
| verbose mode | chế độ chi tiết | chế độ dài dòng, verbose (in prose) |
| floating point / Unix timestamp / Julian day | dấu phẩy động / dấu thời gian Unix / ngày Julian | dấu chấm động |
| update (noun and verb) | bản cập nhật, cập nhật | update |
| database / software / network / password | cơ sở dữ liệu / phần mềm / mạng / mật khẩu | database (in prose), CSDL |
| link (noun) / page / website | liên kết / trang / trang web | link (in prose), đường link, website |
| custom | tùy chỉnh | custom |
| interface / object / instance | giao diện / đối tượng / instance ("instance EC2") | phiên bản (that is a version), thể hiện |
| version / revision | phiên bản / bản sửa đổi | |
| script / browser / plugin | tập lệnh / trình duyệt / plugin | script (in prose), kịch bản |
| build (noun) / to build | bản dựng / dựng | build (in prose), xây dựng (except for "build a solution") |
| path (file, JSON, module, root) | đường dẫn ("đường dẫn tệp", "đường dẫn JSON", "biểu thức đường dẫn JSON", "đường dẫn gốc"); the `PATH` variable and argument names such as `path` stay as code | path (in prose) |
| nested | lồng nhau | lồng ghép |
| real-time | thời gian thực ("theo thời gian thực") | real-time |
| executable (noun) | tệp thực thi; self-contained executable = tệp thực thi độc lập; after a code span "tệp thực thi `stackql`" | |
| agentic | dựa trên tác nhân ("quy trình làm việc dựa trên tác nhân", "kỹ thuật nền tảng dựa trên tác nhân") | agentic, mang tính tác nhân |
| restart / reset | khởi động lại / đặt lại | |
| encoding / encoded | mã hóa / được mã hóa ("được mã hóa URL") | encoding, encode |
| privileges / least privilege | đặc quyền / đặc quyền tối thiểu | |
| dependency / relation | phần phụ thuộc / quan hệ | dependency |
| resolve (a variable, an alias, credentials) | phân giải; "resolves to" = được phân giải thành | giải quyết |
| refresh (a materialized view) | làm mới | refresh |
| blocking / non-blocking (invocation) | chặn / không chặn | |
| listing (directory, registry, marketplace) | mục | danh sách (that is a list) |
| event / alert / notification | sự kiện / cảnh báo / thông báo | event |
| engineer, data engineer | kỹ sư, kỹ sư dữ liệu | |
| hosted / self-host | được lưu trữ / tự lưu trữ | hosting (as a verb), được host |
| escape (a character, a string) | thoát; "escape sequence" = chuỗi thoát; "escape character" = ký tự thoát | |
| datetime value / modifier (date functions) | giá trị datetime / bộ điều chỉnh | |
| regex: character class / quantifier / capture group / backreference / occurrence / to match | lớp ký tự / bộ định lượng / nhóm bắt / tham chiếu ngược / lần xuất hiện / khớp | |
| Unicode code point / character code | điểm mã Unicode / mã ký tự | |
| sine, cosine, tangent / arc sine, arc cosine, arc tangent / hyperbolic | sin, cosin, tang / arcsin, arccos, arctan / hyperbolic ("cosin hyperbolic của 1" in example headings) | sinus, cosinus, tiếp tuyến (that is a geometric tangent), hypebolic |
| logarithm / natural logarithm | logarit / logarit tự nhiên | lôgarit, logarit nepe |
| validate, validation (a query, parameters, a document, configuration) / verify (a signature, a binary, a checksum) | kiểm tra tính hợp lệ / xác minh | xác thực (that is authentication) |
| implement, implemented as / implementation (of a function, a language) | hiện thực, được hiện thực bằng / cách hiện thực ("Hàm `json_each` được hiện thực bằng hàm `json_array_elements_text`") | triển khai (that is deploy), thực hiện bằng, cài đặt |
| evaluate (an expression, a predicate, a policy) | đánh giá ("quy tắc đánh giá chính sách của AWS") | |
| unset (a variable, a field) | hủy thiết lập | hủy đặt, bỏ đặt |
| deprecated | đã lỗi thời | đã ngừng sử dụng, không dùng nữa |
| mutual TLS | TLS hai chiều (mTLS) | mutual TLS (in prose), TLS tương hỗ |
| grant (access, a permission) | trao quyền; "grant access" = cho phép truy cập | cấp quyền (that is authorization) |
| identity (caller, resource, OS) | danh tính ("danh tính hệ điều hành cục bộ") | định danh (that is an identifier) |
| metadata | siêu dữ liệu | metadata (in prose) |
| rotate (a key, a secret, credentials) | xoay vòng | luân chuyển, rotate |
| source / re-source (an env file, variables) | nạp / nạp lại; the heading "Credential (re)sourcing" is "Nạp (lại) thông tin xác thực" | |
| process (OS) / subprocess / spawn | tiến trình / tiến trình con / khởi chạy | quá trình for an OS process ("trong quá trình" for "during" is fine), quy trình (that is a workflow or procedure) |
| production / development (environment) | môi trường sản xuất / môi trường phát triển | môi trường production |
| render (a template, rendered SQL) / render (display text) | tạo SQL từ template, SQL đã tạo / hiển thị ("định dạng hiển thị văn bản") | kết xuất |
| call (noun: tool call, API call) / invocation (blocking, non-blocking) / function call | lệnh gọi ("lệnh gọi công cụ", "một lệnh gọi chặn (đồng bộ)") / lời gọi hàm | lời gọi (except "lời gọi hàm"), cuộc gọi |
| synchronous / asynchronous | đồng bộ / bất đồng bộ | không đồng bộ |
| comment (SQL) / commented-out statement | chú thích / câu lệnh nằm trong khối chú thích | bình luận, ghi chú (that is an admonition note) |
| sessionless | không có phiên ("chế độ không có phiên") | phi phiên |
| SQL dialect | phương ngữ SQL | biến thể SQL |
| archive (ZIP, tarball) | tệp nén | kho lưu trữ (that is a repository) |
| walkthrough | hướng dẫn từng bước | |
| canonical | chuẩn tắc | chính tắc |
| namespace | không gian tên | namespace (in prose) |
| host (a network host, an MCP host application) / host (a machine) | host / máy | máy chủ (that is a server) |
| terminal | terminal | thiết bị đầu cuối |
| cast (to a type) | ép kiểu | chuyển kiểu |
| lowercase / uppercase / case-insensitive / case-sensitive | chữ thường / chữ hoa / không phân biệt chữ hoa chữ thường / phân biệt chữ hoa chữ thường | |
| non-NULL | khác `NULL` | không NULL |
| table-valued function / qualified name | hàm có giá trị bảng / tên đủ điều kiện | |
| floating-point number / radians / degrees | số dấu phẩy động / radian / độ ("tính bằng radian" in prose, "theo radian" in descriptions before "bằng SQL trong StackQL") | |
| inverse (trigonometric) | nghịch đảo ("arccos (cosin nghịch đảo)") | ngược |
| natural language | ngôn ngữ tự nhiên | |
| contributor / contribution | người đóng góp / lượt đóng góp | |
| available (methods, entries) / available (on a platform) | hiện có / khả dụng | có sẵn (that is built-in) |
| side effect / breaking change / entry point | tác dụng phụ / thay đổi không tương thích ngược / điểm vào | |
| clone / commit / merge (git) | clone / commit / hợp nhất | sao chép (for clone), cam kết (for commit) |
| image (container, Docker) | image ("image Docker", "image container") | hình ảnh, ảnh |
| dropdown | danh sách thả xuống | dropdown |
| production keys, production account | khóa sản xuất, tài khoản sản xuất | |
| StackQL Provider Registry (link text to `/providers`) | unchanged, it is a name | Registry provider StackQL |
| Kept in English | backend, bundle (the `.mcpb`; the format name MCP Bundle too), sidecar, vendored, crate, worker, bearer (token bearer), dotenv, pull request, fork, issue, webhook, plugin, runtime, fan-out, proxy, SaaS, wrapper, skill, workflow, tag, handshake, hash, cache (except "bộ nhớ đệm cục bộ"), listener, loopback, harness, upstream, placeholder, fallback, staging, smoke test, toolchain, action (a GitHub action), marketplace, payload, header, stack, pipeline, framework, container, tenant, bucket, boolean, token, template, literal, instance, artifact, principal, service principal, claim, trace, span, job, exporter, receiver; third-party UI terms (Datadog facets, ChatGPT threads, GitHub stargazers and watchers) | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Vietnamese |
|---|---|
| Install | Cài đặt |
| Installing StackQL | Cài đặt StackQL |
| Getting Started | Bắt đầu |
| Command Line Usage | Dòng lệnh |
| Language Specification | Đặc tả ngôn ngữ |
| Built-in Functions | Hàm tích hợp sẵn |
| Functions | Hàm |
| Developers | Nhà phát triển |
| Providers | Provider |
| Quick Starts | Bắt đầu nhanh |
| AI Agents | Tác nhân AI |
| MCP Server / MCP Tools / Embedded MCP | Máy chủ MCP / Công cụ MCP / MCP nhúng |
| Embedded (sidebar category) | Nhúng |
| Blog | Blog |
| Product Announcements | Thông báo sản phẩm |
| Provider Announcements | Thông báo provider |
| Tutorials | Hướng dẫn |
| Documentation | Tài liệu |
| Contact us | Liên hệ |
| Privacy Policy | Chính sách quyền riêng tư |
| Terms of Service | Điều khoản dịch vụ |
| Support | Hỗ trợ |
| AI Reference | Tài liệu tham khảo AI |
| More | Thêm |
| Welcome to StackQL | Chào mừng đến với StackQL |

Recurring headings, phrases and keywords (the same on every page):

| English | Vietnamese |
|---|---|
| Syntax | Cú pháp |
| Arguments | Đối số |
| Return Value(s) | Giá trị trả về |
| Example / Examples | Ví dụ (one word for both) |
| Inputs / Output | Đầu vào / Đầu ra |
| Flags | Cờ |
| Overview | Tổng quan |
| How it works | Cách hoạt động |
| Modes | Chế độ |
| Gating | Kiểm soát |
| Install | Cài đặt |
| Quick start | Bắt đầu nhanh |
| Demo app | Ứng dụng minh họa |
| Repo link | Liên kết kho lưu trữ |
| Vendoring / embedding | Vendoring / nhúng |
| Prerequisites | Điều kiện tiên quyết |
| Next steps | Các bước tiếp theo |
| Related | Liên quan |
| See also: | Xem thêm: |
| See X. | Xem X. |
| For more information, see X. | Để biết thêm thông tin, hãy xem X. |
| Optional. / Defaults to X. | Không bắt buộc. / Mặc định: X. |
| Returns X. (the sentence that opens a function or tool page) | Trả về X. |
| Return the absolute value of a number (an example heading that names a task) | Trả về giá trị tuyệt đối của một số (a verb phrase) |
| X function in StackQL: ... (description pattern) | Hàm X trong StackQL: ... (a noun phrase or a verb phrase after the colon, as in the source) |
| X window function in StackQL: ... (description pattern) | Hàm cửa sổ X trong StackQL: ... |
| X in StackQL: ... (description of a statement page) | X trong StackQL: ... |
| ... using SQL in StackQL (description pattern) | ... bằng SQL trong StackQL |
| Available in StackQL `vX` and later / releases from `vX` | Có từ StackQL `vX` trở đi |
| Available in StackQL releases after `vX` | Có trong các bản phát hành StackQL sau `vX` |
| None. (no arguments, no inputs) | Không có. |
| Query / Results (tab labels), Output: | Truy vấn / Kết quả, Đầu ra: |
| Tested with (badge line on quick starts) | Đã kiểm thử với |
| Best Practices / Troubleshooting / Further reading | Phương pháp hay nhất / Khắc phục sự cố / Đọc thêm |
| MCP tool listing X / MCP tool that does X (description pattern) | Công cụ MCP liệt kê X / Công cụ MCP thực hiện X |
| MCP overview (link text) | Tổng quan về MCP |
| Argument / Required / Description (table headers) | Đối số / Bắt buộc / Mô tả |
| Renderer / Shape (table headers) | Trình hiển thị / Dạng |
| Table / One row per X (table cells) | Bảng / Một hàng cho mỗi X |
| Yes / No (table cells) | Có / Không |
| For example | Ví dụ |
| Aggregate function / Scalar function | Hàm tổng hợp / Hàm vô hướng |
| When to use, Use case / Example usage | Khi nào nên dùng, Trường hợp sử dụng / Ví dụ sử dụng |
| Conclusion / Get started (homepage heading) | Kết luận / Bắt đầu ngay |
| Returns : (a `<summary>` or bold label) | Trả về: |
| Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered: | Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL: |
| Here's an example in Claude Desktop: | Dưới đây là một ví dụ trong Claude Desktop: |
| Server mode table cells: allow / refuse / needs approval; allowed / refused / (default) | cho phép / từ chối / cần phê duyệt; được cho phép / bị từ chối / (mặc định) |
| Gated by the server mode. / Allowed in every server mode. Read-only. (the Gating section of an MCP tool page) | Được kiểm soát bởi chế độ máy chủ. / Được cho phép ở mọi chế độ máy chủ. Chỉ đọc. |
| `embedded sql backend` / `postgres sql backend` (the "Tested with" badges on quick starts) | backend SQL nhúng / backend SQL postgres |
| Short "See also" badges that are not a page's exact title: StackQL Resource Hierarchy / Provider Sessions / Templating / Window Function Reference / X language spec | Phân cấp tài nguyên StackQL / Phiên provider / Sử dụng template / Tài liệu tham khảo hàm cửa sổ / X trong đặc tả ngôn ngữ |
| keyword `cloud inventory` | kiểm kê đám mây |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Unicode NFC only: every Vietnamese letter is one precomposed character (ế is U+1EBF, not e + U+0302 + U+0301). Text pasted from some editors or typed with some input methods arrives decomposed (NFD); it renders the same but breaks search, grep and the glossary checks. Normalize with `text.normalize('NFC')` before writing if in doubt.
- Tone marks on `oa`, `oe` and `uy` follow the traditional placement used by Microsoft and Google in Vietnamese and by the Docusaurus Vietnamese theme strings: hóa, hòa, xóa, khóa, tòa, góa, khỏe, thủy, tùy, lũy, hủy, ủy, thúy; never hoá, hoà, xoá, khoá, khoẻ, thuỷ, tuỳ, luỹ, huỷ, uỷ. When a final consonant follows, the mark goes on the second vowel as usual (hoàn, toán, tuyến, khuyến, quyết).
- `i` and `y` as in the dictionary forms of vendor documentation: kỹ thuật, kỹ sư, lý do, quản lý, xử lý, vật lý, hệ thống, ký tự, ký hiệu, quy tắc, quy trình, Mỹ; never kĩ, lí do, quản lí, xử lí, kí tự, qui tắc, qui trình.
- Headings in sentence case: a capital on the first word and on proper nouns only ("Biến phiên", not "Biến Phiên"). No end punctuation. Keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- Word order is head first: the noun comes before what modifies it ("khóa API", "biến môi trường", "provider StackQL", "máy chủ MCP StackQL", "hàm cửa sổ `RANK`"). An inline code span that names a thing takes its class noun before it (lệnh `stackql exec`, cờ `--output`, tham số `region`, hàm `json_extract`, câu lệnh `SELECT`, tệp `stackql.yml`).
- Nouns are not inflected. An English loanword never takes -s ("các provider", "nhiều token", "ba endpoint"). Use "các" or "những" only where the plural matters and no number or quantifier (mọi, mỗi, một số, nhiều) already marks it; do not put "các" before every plural English noun.
- Classifiers where Vietnamese needs them, not more: "một hàng", "mỗi cột", "một câu lệnh"; do not add "cái" or "chiếc" to technical nouns.
- "we" as StackQL the project or company is "chúng tôi" ("we recommend" = "chúng tôi khuyên dùng"); "we" in a walkthrough that includes the reader is "chúng ta" ("we will create a view" = "chúng ta sẽ tạo một dạng xem").
- Passive voice sparingly: "được" for a neutral or positive passive ("được hỗ trợ", "được trả về"), "bị" only for something adverse ("bị từ chối", "bị lỗi"). Prefer an active sentence where the English subject is clear (who returns what, who must approve).
- Formal word choice: "có thể" (not "được" alone for ability), "không" (never "ko", "k"), "để" for purpose, "nếu" for "if", "khi" for "when", "sử dụng" or "dùng" for "use" (both fine), "tạo" for "create", "xóa" for "delete".
- Do not stack Sino-Vietnamese abstractions where a plain verb exists ("dùng", not "tiến hành sử dụng"; "để", not "nhằm mục đích để"). Avoid "thì", "là" and "mà" where the sentence reads without them.
- Lists of three or more items take no comma before "và" or "hoặc" ("hàng, cột và bảng").
- No space before `:`, `;`, `?` or `!`; one space after. The word after a colon is lower case unless it is a name or starts a full sentence.
- Quotation marks are straight double quotes ("..."). No typographic quotes, no typographic apostrophe, no guillemets, so the text stays greppable.
- No dash punctuation other than the keyboard hyphen. Where the source uses ` - ` or ` -- ` as a dash inside a sentence or between a term and its explanation, write ` - `, or use a colon, commas or parentheses. Never write an em dash, an en dash or a double hyphen in prose. Hyphens in compound terms ("khóa-giá trị") are kept.
- English loanwords and English terms from the "Kept in English" row are written plain, never in italics and never in quotes: added italics change the page structure that `yarn i18n:verify` and the node-count check compare.
- Names of languages, countries, months and days follow Vietnamese capitalization: "tiếng Anh", "tiếng Việt", "tháng 1", "thứ Hai". Product, provider and vendor names keep their own spelling.
- List items and table cells end the way the source does: no full stop added to fragments, a full stop where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English.
- Numbers, dates, versions and units as in the source; the Vietnamese decimal comma and dot thousands separator are not applied inside code or tables of command output, and prose numbers follow the source.
- The `description` front matter stays one sentence and under about 170 characters (Vietnamese runs 15 to 30 percent longer than English in characters; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud inventory` -> kiểm kê đám mây); keep the number and order of entries.
- Front matter must stay valid YAML. Keep the quoting of the source value. A value that contains `: ` (a colon followed by a space) or ` #` must be in double quotes, as must a value that starts with a quote, bracket, backtick, `*`, `&`, `!`, `%` or `@`; escape any inner double quote as `\"`. The description pattern "Hàm X trong StackQL: ..." always needs the double quotes the source already has.
- Strings in JSX and in exported data arrays keep the quote style of the source; a translated string that gains a double quote goes in single quotes, and the other way around. Vietnamese has no apostrophes in ordinary words, so single-quoted strings are safe.
- Bold and italic markers stay where the source has them, with no space inside the markers. Punctuation that follows a bold term stays outside the markers when the source has it outside. A closing `**` must not be followed directly by a letter when it follows punctuation or inline code (it would not close in CommonMark), and a closing `__` or `_` must never be followed directly by a letter (Vietnamese letters with diacritics count as letters); leave a space or punctuation after the marker.
- Link text that is another page's title is that page's translated `title`, including the "See also" badges that wrap the title in inline code (`` [[` Window Functions `]] `` becomes `` [[` Hàm cửa sổ `]] ``); `yarn i18n:verify` warns about the changed inline code on those pages, which is expected. Link text that is a heading of the target page is that heading's translation.
- Example prompts addressed to an AI assistant use the same plain imperative as the rest of the page ("Hiển thị", "Tạo", "Liệt kê").
- The homepage tagline "Configuration-as-Data. Infrastructure-as-Context." stays in English.
- Vietnamese is longer than English in characters, so a `length ratio` warning from `yarn i18n:verify` points at an omission. Short pages whose code fences carry padded result tables are the exception (see the es note in CLAUDE.md).

## ar

Audience: Arabic-speaking engineers in every Arab country reading technical documentation. Modern Standard Arabic in the register of vendor documentation in Arabic (Microsoft Learn, Google Cloud, AWS): neutral, precise, the same text for readers in the Gulf, the Levant, Egypt and the Maghreb; no dialect words (no مش، عشان، كده، إيش، شلون، بدّك، هيك). Address the reader in the second person singular as those vendors do ("يمكنك"، "لديك"، "جهاز الكمبيوتر الخاص بك"), with the masculine singular as the generic form; never "حضرتك" or the plural "أنتم". Present tense, short sentences. Instructions use the plain imperative ("شغّل الأمر التالي"، "استخدم"، "راجع"، "ثبّت"، "أنشئ")، not "قم بـ" + verbal noun, which is acceptable only where the verb has no natural imperative. Headings that name a task use the verbal noun (مصدر): "تثبيت StackQL"، "إنشاء قالب"، "إرجاع القيمة المطلقة لعدد". Keep the English terms Arabic developers write in Latin script (the "Kept in English" row); do not coin Arabic names for products or APIs. No marketing tone.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Arabic | Not |
|---|---|---|
| provider (a StackQL provider) | موفر (plural موفرون / موفري; "موفر StackQL"، "الموفر `aws`"، "Available Providers" = "الموفرون المتاحون") | مزود (that is the cloud vendor), الموفرات, provider (in prose), بروفايدر |
| cloud provider (the vendor) | مزود الخدمات السحابية; short form مزود السحابة (plural مزودو الخدمات السحابية) | موفر (that is a StackQL provider), بائع |
| vendor (the company behind an API or SDK, not only cloud) | الجهة المزودة; "vendor-published" = تنشره الجهة المزودة | البائع، vendor |
| cloud (as an adjective) | سحابي / سحابية after the noun ("الموارد السحابية"، "البنية التحتية السحابية") | كلاود |
| API | API (Latin, with a class noun where Arabic needs one: "واجهة API"، plural "واجهات API"; "REST API" = "واجهة REST API") | واجهة برمجة التطبيقات (except where the source spells it out) |
| service | خدمة (plural خدمات) | |
| resource | مورد (plural موارد) | مصدر (that is a source) |
| method | أسلوب (plural أساليب) | طريقة، دالة (that is a function), method |
| operation | عملية (plural عمليات) | |
| query (noun) / to query | استعلام (plural استعلامات) / الاستعلام عن; "run a query" = تشغيل استعلام | سؤال |
| statement | جملة (plural جمل; "جملة `SELECT`") | عبارة (that is a clause), أمر (that is a command) |
| command (CLI) / subcommand | أمر (plural أوامر; "الأمر `stackql exec`") / أمر فرعي | جملة (that is a statement) |
| clause | عبارة ("عبارة `WHERE`") | جملة (that is a statement), شرط (that is a condition) |
| result set | مجموعة النتائج | |
| row / column | صف / عمود | سطر (that is a line of text), سجل (for a row) |
| line (of text, of output) | سطر | صف |
| table | جدول | |
| view | طريقة عرض (plural طرق عرض; "طريقة العرض `CREATE VIEW`") | عرض alone, view (in prose) |
| materialized view | طريقة عرض مجسدة | طريقة عرض مادية |
| window function | دالة نافذة (plural دوال النافذة; "دالة النافذة `RANK`") | دالة window، دالة تحليلية |
| window frame | إطار النافذة | frame |
| aggregate function / scalar function | دالة تجميعية / دالة عددية | دالة تجميع، دالة قياسية |
| function / expression | دالة (plural دوال) / تعبير | وظيفة (that is a job or a feature), دالات |
| keyword / operator / predicate, condition | كلمة أساسية / عامل (plural عوامل) / شرط | كلمة مفتاحية، معامل (that is ambiguous), مؤثر |
| subquery / alias / join | استعلام فرعي / اسم مستعار / ربط ("عمليات الربط `JOIN`") | subquery، alias (in prose)، صلة |
| partition / peer, peer group / tie (window functions) | قسم (plural أقسام) / صف نظير، مجموعة الصفوف النظيرة / تعادل ("الصفوف المتعادلة"، "مع فجوات"، "بدون فجوات") | peer |
| offset (`LAG`, `LEAD`) | الإزاحة (the argument name `offset` stays as code) | offset (in prose) |
| target (of `SET` and `UNSET`) | الهدف ("الهدف" as the heading) | |
| override (a credential, a default) / overwrite (a value, a key) | تجاوز ("credential override" = تجاوز بيانات الاعتماد) / الكتابة فوق ("الكتابة فوق القيمة إذا كانت موجودة؟") | override |
| dereference | إلغاء المرجعية | |
| data type / literal / identifier | نوع البيانات / قيمة حرفية ("قيمة حرفية نصية"، "قيمة حرفية رقمية") / معرف | ثابت (for a literal), identifier |
| string / integer / boolean | سلسلة نصية / عدد صحيح / قيمة منطقية (boolean as an adjective: منطقي) | string (in prose)، نص (for a string value) |
| whitespace | مسافة بيضاء | فراغ |
| parameter / argument | معلمة (plural معلمات) / وسيطة (plural وسيطات) | باراميتر، معامل، بارامتر |
| flag (CLI) / option | علامة (plural علامات; "global flags" = العلامات العامة; "العلامة `--output`") / خيار | flag (in prose)، علم |
| environment variable | متغير البيئة (plural متغيرات البيئة) | متغير بيئي |
| field / key / value | حقل / مفتاح / قيمة | |
| schema | مخطط | schema (in prose)، سكيما |
| control plane / data plane | مستوى التحكم / مستوى البيانات | control plane، data plane |
| agent, AI agent | وكيل (plural وكلاء)، وكيل ذكاء اصطناعي ("AI Agents" = وكلاء الذكاء الاصطناعي) | عميل (that is a client), agent |
| tool (an MCP tool) | أداة (plural أدوات; "أداة MCP") | tool |
| prompt (text sent to a model) | موجه (plural موجهات) | مطالبة (that is a claim), prompt (in prose) |
| approval prompt (a dialog shown to the user) | طلب موافقة | موجه الموافقة |
| elicitation (MCP) / elicitation prompt | elicitation (the MCP feature name, kept) / طلب elicitation; "elicitation-capable client" = عميل MCP يدعم elicitation | استنباط، استخلاص |
| gating / gated (MCP) | التقييد / مقيد; "gated by X" = يخضع لـ X; "gated writes" = عمليات الكتابة التي تتطلب موافقة | gating، بوابة |
| approval / approve | الموافقة / الموافقة على | الاعتماد (that is credentials) |
| server mode, safety mode | وضع الخادم، وضع الأمان | |
| read-only | للقراءة فقط | |
| write (noun, a mutating call) | عملية كتابة | كتابة alone |
| mutation / mutating statement | عملية تعديل البيانات / جملة تعديل البيانات | طفرة، mutation |
| lifecycle operation | عملية دورة الحياة | عملية lifecycle |
| resource lifecycle | دورة حياة الموارد | |
| log / audit log | سجل (plural سجلات) / سجل التدقيق | log (in prose) |
| log record / completion record / row record (OTel output) | إدخال السجل / سجل الإكمال / سجل الصف | |
| credentials | بيانات الاعتماد | أوراق الاعتماد، شهادات (that is certificates), credentials |
| authentication / authorization | المصادقة / التخويل | التوثيق، الترخيص (that is licensing) |
| principal / service principal | كيان الأمان / كيان الخدمة | المدير، الرئيسي |
| subject / issuer / audience / claim (JWT, certificate identity) | الموضوع / جهة الإصدار / الجمهور / مطالبة | |
| membership (group, directory) | العضوية | |
| service account | حساب الخدمة | |
| token (credential) / API key / access key | رمز مميز (plural رموز مميزة) / مفتاح API / مفتاح الوصول | توكن، API key |
| secret / secret store | سر (plural أسرار) / مخزن الأسرار | secret (in prose)، كلمة سر (that is a password) |
| scope (OAuth, and of a variable or a setting) | النطاق | scope |
| role / permission | دور (plural أدوار) / إذن (plural أذونات) | صلاحية (except "privileges")، role |
| assume a role (AWS) | تولي دور | افتراض دور |
| session / session variable | جلسة / متغير الجلسة | session |
| deployment / to deploy | النشر / نشر | deploy (in prose)، تطوير (that is develop) |
| infrastructure / infrastructure as code | البنية التحتية / البنية التحتية كتعليمات برمجية (IaC) | البنية الأساسية، البنية التحتية ككود |
| registry (generic) / provider registry | السجل / سجل الموفرين (the names StackQL Registry and StackQL Provider Registry stay as they are) | المستودع (that is a repository) |
| pull (a provider) | سحب ("سحب موفر"; "pulled" = تم سحبه) | pull (in prose)، جلب |
| download / upload | تنزيل / تحميل | تحميل for download (in documentation تحميل is upload) |
| repository | مستودع | repo (in prose)، مخزن |
| release / version / revision | إصدار (all of release, version; "to release" = أصدر) / مراجعة | نسخة (for a version) |
| command line | سطر الأوامر | |
| shell / interactive shell | واجهة الأوامر / واجهة الأوامر التفاعلية | الصدفة، الغلاف، شل |
| server / client | خادم (plural خوادم) / عميل (plural عملاء; "خادم MCP"، "عميل MCP") | سيرفر، زبون |
| transport (MCP) | وسيلة النقل ("وسيلة النقل stdio") | transport |
| stateless / stateful | عديم الحالة / ذو حالة | |
| endpoint | نقطة نهاية (plural نقاط نهاية) | endpoint |
| request / response | طلب / استجابة | رد |
| request body | نص الطلب | جسم الطلب |
| payload | الحمولة | |
| header (HTTP) | رأس (plural رؤوس; "رأس HTTP") | عنوان (that is a heading or a title), ترويسة |
| pagination | التقسيم إلى صفحات | ترقيم الصفحات |
| output / output format / output mode | الإخراج / تنسيق الإخراج / وضع الإخراج | المخرجات، output |
| input | الإدخال | المدخلات |
| template / templating | قالب (plural قوالب) / استخدام القوالب | نموذج (that is an AI model), template |
| model (AI) | نموذج (plural نماذج) | موديل |
| dry run | تشغيل تجريبي | |
| guardrail | ضابط حماية (plural ضوابط الحماية) | حاجز حماية |
| answer engine | محرك إجابات | |
| region / project / subscription / tenant | منطقة / مشروع / اشتراك / مستأجر | إقليم |
| account | حساب | |
| open source | مفتوح المصدر | |
| binary / library | ملف ثنائي / مكتبة | binary (in prose) |
| embedded (Embedded MCP) / to embed | مضمن ("MCP المضمن"، "خوادم MCP المضمنة") / تضمين | مدمج (that is built-in) |
| built-in | مدمج ("الدوال المدمجة") | مضمن (that is embedded), built-in |
| installation / to install / installed | التثبيت / تثبيت / مثبت | تنصيب |
| settings / configuration | الإعدادات / التكوين ("ملف التكوين") | الضبط، config (in prose) |
| documentation | الوثائق | التوثيق، المستندات |
| tutorial | برنامج تعليمي (plural البرامج التعليمية) | درس |
| quick start | البدء السريع | |
| getting started | بدء الاستخدام | |
| announcement | إعلان | |
| feature | ميزة (plural ميزات) | خاصية |
| example | مثال (plural أمثلة) | |
| syntax / usage | بناء الجملة / الاستخدام | الصيغة |
| note / tip / warning (admonition titles are translated by the theme) | ملاحظة / تلميح / تحذير | |
| resource hierarchy | التسلسل الهرمي للموارد | |
| access management | إدارة الوصول | |
| provider session | جلسة الموفر | |
| SQL backend | واجهة SQL الخلفية | backend (in prose) |
| query engine / parser | محرك الاستعلامات / المحلل | parser (in prose) |
| wire protocol | بروتوكول الاتصال ("بروتوكول الاتصال الخاص بـ PostgreSQL") | بروتوكول السلك |
| proxy / reverse proxy | خادم وكيل / وكيل عكسي (never a bare وكيل for a proxy: وكيل is an agent) | بروكسي |
| cache / local cache | ذاكرة التخزين المؤقت / ذاكرة التخزين المؤقت المحلية | كاش |
| key-value pair | زوج مفتاح وقيمة (plural أزواج المفاتيح والقيم) | |
| delimiter / separator | فاصل (both) | محدد |
| bucket (object storage) / bucket (NTILE) | حاوية ("حاوية S3"، "حاوية Cloud Storage") / مجموعة | دلو، bucket |
| container | حاوية ("حاوية Docker") | |
| throw an error / return an error | يطرح خطأ / يعيد خطأ | يرمي خطأ |
| return (a value) / return value | يعيد (the verb, agreeing with its subject: "تعيد الدالة"، "يعيد الأمر") / القيمة المرجعة | يرجع، القيمة المعادة |
| well-formed / valid (JSON) | جيد التكوين / صالح | |
| file / state file | ملف / ملف الحالة | |
| directory (of principals, filesystem) / folder | دليل / مجلد | |
| computer, machine | كمبيوتر ("جهاز الكمبيوتر")، جهاز | حاسوب، حاسب |
| screen / user / team | شاشة / مستخدم / فريق | |
| app, application | تطبيق | |
| drift / infrastructure drift | الانحراف / انحراف البنية التحتية | drift |
| cloud inventory, inventory | جرد الموارد السحابية، جرد ("جرد الموارد") | inventory، مخزون |
| compliance | الامتثال | |
| provision (resources) | توفير ("توفير الموارد") | تجهيز |
| operator (the person running StackQL) | المشغل | عامل (that is a SQL operator) |
| caller | الجهة المستدعية | المتصل |
| filter (noun and verb) | مرشح / تصفية | فلتر |
| concatenation | الدمج ("عامل الدمج"، "دمج السلاسل النصية") | التسلسل، السلسلة |
| parse / plan (a statement) | تحليل / تخطيط | parse (in prose) |
| contract (I/O contract, safety contract) | عقد ("عقد الإدخال والإخراج"، "عقد الأمان") | contract |
| source of truth | المصدر الموثوق | مصدر الحقيقة |
| entitlement | الاستحقاق | |
| snapshot / checksum / digest / hash | لقطة / المجموع الاختباري / الملخص / تجزئة ("دوال التجزئة") | هاش |
| estate / fingerprint | البيئة ("البيئة السحابية") / البصمة | العقارات، الممتلكات |
| allowlist | قائمة السماح | القائمة البيضاء، whitelist |
| opt in / explicit opt-in | تفعيل اختياري / تفعيل صريح | |
| enable / disable | تمكين / تعطيل | تفعيل (except "opt in")، إيقاف |
| support (a feature, a format) / supported | دعم / مدعوم | |
| hint (in an MCP error or tool annotation) / query hint | تلميح / تلميح الاستعلام | hint |
| advertise (a capability) | الإعلان عن | |
| artifact (build or release output) | ملف ناتج (plural الملفات الناتجة; "release artifacts" = ملفات الإصدار) | أداة، artifact |
| stack (of resources) | مكدس | stack (in prose) |
| pipeline | خط أنابيب ("خط أنابيب CI/CD") | مسار (that is a path), pipeline |
| notarized / signed | موثق / موقع رقميًا | |
| common table expression (CTE) | تعبير جدول شائع (CTE) | |
| default / by default | الافتراضي / افتراضيًا | default |
| array (JSON, data type) | مصفوفة | صفيف، array (in prose) |
| policy (IAM, access policy) / IAM binding | سياسة (plural سياسات) / ربط IAM (plural روابط IAM) | نهج، policy |
| bind, binding (alias, secret store) | ربط | |
| assign, assignment (`SET`) | تعيين | إسناد |
| token (lexical) | وحدة معجمية | رمز مميز (that is a credential) |
| pin (a version, a key, an issuer) | قفل ("قفل الإصدار"; the heading "Pinning the revision" is "قفل المراجعة") | تثبيت (that is install), pin |
| exporter / receiver / collector (OTel) / sink (audit) | exporter / receiver / collector (OTel component kinds, kept) / الوجهة | مصدّر، مستقبل |
| tenancy (the alias label) | tenancy | |
| preview (a feature in preview) | معاينة ("في مرحلة المعاينة") | preview |
| legacy | قديم | موروث، legacy |
| batch / statement batch | دفعة / دفعة جمل | batch (in prose) |
| wildcard | حرف بدل | |
| substring / pattern (regex) / regular expression | سلسلة فرعية / نمط / تعبير نمطي (plural التعبيرات النمطية) | تعبير عادي، regex (in prose, except in names) |
| collating function / collating sequence | دالة الترتيب / تسلسل الترتيب | |
| timestamp | الطابع الزمني | الختم الزمني |
| current row / rank / running total (window functions) | الصف الحالي / الترتيب / المجموع التراكمي | |
| installer | برنامج التثبيت | المثبت |
| workspace | مساحة العمل | |
| monitoring / to monitor / monitor (noun) | المراقبة / مراقبة / أداة مراقبة | الرصد |
| run / execute | تشغيل / تنفيذ | |
| list (verb) / delete / remove / manage | سرد / حذف / إزالة / إدارة | عرض قائمة |
| framework | إطار عمل | فريموورك |
| capability (of an MCP client or server) | قدرة (plural قدرات) | إمكانية |
| Query Library entry | إدخال ("معرف إدخال Query Library") | مقالة |
| job (CI, scheduled) / task (generic) | مهمة (plural مهام; "مهمة CI مجدولة") / مهمة | وظيفة |
| trace / span / trace id | تتبع / span / معرف التتبع | أثر |
| SQL engine / backing (SQL engine, server) | محرك SQL / الأساسي ("محرك SQL الأساسي") | |
| self-contained / prebuilt / launcher (`npx`, `uvx`) | مستقل / مبني مسبقًا / أداة التشغيل | |
| to vendor (a binary, a dependency) / vendored | تضمين نسخة محلية / مضمن كنسخة محلية | vendoring (in prose) |
| Cargo features (`sidecar`, `vendored`) | ميزات Cargo | feature flag |
| predicate pushdown | دفع الشروط إلى المصدر (predicate pushdown) | |
| verbose mode | الوضع المفصل | |
| floating point / Unix timestamp / Julian day | الفاصلة العائمة / الطابع الزمني لـ Unix / اليوم اليولياني | النقطة العائمة |
| update (noun and verb) | تحديث | |
| database / software / network / password | قاعدة بيانات / برنامج، برمجيات / شبكة / كلمة المرور | داتابيز |
| link (noun) / page / website | رابط / صفحة / موقع ويب | لينك |
| custom | مخصص | |
| interface / object / instance | واجهة / كائن / مثيل ("مثيل EC2") | نسخة (for an instance) |
| script / browser / plugin | برنامج نصي / متصفح / مكون إضافي | سكريبت، بلجن |
| build (noun) / to build | البناء / بناء | |
| path (file, JSON, module, root) | مسار ("مسار الملف"، "مسار JSON"، "تعبير مسار JSON"، "المسار الجذر"); the `PATH` variable and argument names such as `path` stay as code | path (in prose) |
| nested | متداخل | |
| real-time | في الوقت الفعلي | الزمن الحقيقي |
| executable (noun) | ملف تنفيذي ("الملف التنفيذي `stackql`") | |
| agentic | قائم على الوكلاء ("سير عمل قائم على الوكلاء") | وكيلي |
| restart / reset | إعادة التشغيل / إعادة التعيين | |
| encoding / encoded | الترميز / مرمز ("مرمز بتنسيق URL") | التشفير (that is encryption) |
| encryption / encrypted | التشفير / مشفر | |
| privileges / least privilege | الامتيازات / الحد الأدنى من الامتيازات | |
| dependency / relation | تبعية (plural تبعيات) / علاقة | اعتمادية |
| resolve (a variable, an alias, credentials) | حل; "resolves to" = يُحل إلى | تحليل (that is parse) |
| refresh (a materialized view) | تحديث | |
| blocking / non-blocking (invocation) | حاجب / غير حاجب | |
| listing (directory, registry, marketplace) | إدراج | قائمة (that is a list) |
| event / alert / notification | حدث / تنبيه / إعلام | إشعار |
| engineer, data engineer | مهندس، مهندس بيانات | |
| hosted / self-host | مستضاف / استضافة ذاتية | |
| escape (a character, a string) | إلغاء; "escape sequence" = تسلسل الإلغاء; "escape character" = حرف الإلغاء | هروب |
| datetime value / modifier (date functions) | قيمة datetime / معدّل (plural معدّلات) | مغير |
| regex: character class / quantifier / capture group / backreference / occurrence / to match | فئة الأحرف / محدد الكمية / مجموعة الالتقاط / مرجع خلفي / تكرار / مطابقة | |
| Unicode code point / character code | نقطة رمز Unicode / رمز الحرف | |
| sine, cosine, tangent / inverse sine, inverse cosine, inverse tangent / hyperbolic | الجيب، جيب التمام، الظل / الجيب العكسي، جيب التمام العكسي، الظل العكسي / الزائدي ("جيب التمام الزائدي للعدد 1" in example headings) | قوس الجيب، ظل الزاوية |
| logarithm / natural logarithm | اللوغاريتم / اللوغاريتم الطبيعي | لوغارتم |
| validate, validation (a query, parameters, a document, configuration) / verify (a signature, a binary, a checksum) | التحقق من صحة / التحقق من | |
| implement, implemented as / implementation (of a function, a language) | تنفيذ، تُنفذ باستخدام / التنفيذ ("تُنفذ الدالة `json_each` باستخدام الدالة `json_array_elements_text`") | تطبيق (that is an application) |
| evaluate (an expression, a predicate, a policy) | تقييم | |
| unset (a variable, a field) | إلغاء التعيين | |
| deprecated | مهمل | متقادم |
| mutual TLS | TLS المتبادل (mTLS) | |
| grant (access, a permission) | منح ("منح حق الوصول") | |
| identity (caller, resource, OS) | الهوية | |
| metadata | بيانات التعريف | بيانات وصفية، metadata (in prose) |
| rotate (a key, a secret, credentials) | تدوير | |
| source / re-source (an env file, variables) | تحميل / إعادة تحميل; the heading "Credential (re)sourcing" is "(إعادة) تحميل بيانات الاعتماد" | |
| process (OS) / subprocess / spawn | عملية / عملية فرعية / تشغيل | |
| production / development (environment) | بيئة الإنتاج / بيئة التطوير | |
| render (a template, rendered SQL) / render (display text) | إنشاء SQL من القالب، SQL الناتج / عرض | تصيير |
| call (noun: tool call, API call), invocation / function call | استدعاء ("استدعاء أداة"، "استدعاء حاجب (متزامن)") / استدعاء دالة | مكالمة |
| synchronous / asynchronous | متزامن / غير متزامن | |
| comment (SQL) / commented-out statement | تعليق / جملة داخل كتلة تعليق | ملاحظة (that is an admonition note) |
| sessionless | بدون جلسة ("وضع بدون جلسات") | |
| SQL dialect | لهجة SQL | |
| archive (ZIP, tarball) | ملف مضغوط | أرشيف |
| walkthrough | شرح تفصيلي | |
| canonical | المعياري | القانوني |
| namespace | مساحة الاسم | |
| host (a network host, an MCP host application) / host (a machine) | مضيف / جهاز | خادم (that is a server) |
| terminal | الوحدة الطرفية | التيرمنال |
| cast (to a type) | تحويل النوع ("التحويل إلى `INTEGER`") | |
| lowercase / uppercase / case-insensitive / case-sensitive | أحرف صغيرة / أحرف كبيرة / غير حساس لحالة الأحرف / حساس لحالة الأحرف | |
| non-NULL | غير `NULL` | |
| table-valued function / qualified name | دالة ذات قيمة جدولية / الاسم المؤهل | |
| floating-point number / radians / degrees | عدد بفاصلة عائمة / راديان ("بالراديان") / درجات | |
| natural language | اللغة الطبيعية | |
| contributor / contribution | مساهم / مساهمة | |
| available (methods, entries, on a platform) | متاح | |
| side effect / breaking change / entry point | أثر جانبي / تغيير جذري / نقطة الدخول | |
| clone / commit / merge (git) | استنساخ / إيداع / دمج | تثبيت (for commit) |
| pull request / fork / issue (GitHub) | طلب سحب / نسخة متفرعة / مشكلة (issue) | |
| image (container, Docker) | صورة ("صورة Docker"، "صورة الحاوية") | |
| dropdown | قائمة منسدلة | |
| production keys, production account | مفاتيح الإنتاج، حساب الإنتاج | |
| webhook / workflow / runtime / wrapper / placeholder / fallback / handshake / listener / toolchain | خطاف ويب / سير العمل / وقت التشغيل / غلاف / عنصر نائب / بديل احتياطي / المصافحة / المستمع / سلسلة الأدوات | the English words in prose |
| tag (label) | وسم (plural وسوم) | علامة (that is a CLI flag) |
| skill (a Claude skill) | مهارة | |
| StackQL Provider Registry (link text to `/providers`) | unchanged, it is a name | سجل موفري StackQL |
| compile / compiled (application, identifiers) | تجميع / مجمع ("التطبيقات المجمعة"، "تطبيق Go مجمع") | مترجم (that reads as "translated") |
| scalar (a value or a field that is not an object) | قيمة مفردة، حقل مفرد | قيمة عددية (that is a number; a scalar function is still دالة عددية) |
| attribute (OTel, template, resource) | سمة (plural سمات; "سمات الموارد") | خاصية (that is a property) |
| property (of a resource or an object) | خاصية (plural خصائص) | |
| SDK | SDK (with حزمة SDK where Arabic needs a class noun; "حزمة SDK للموفرين") | مجموعة تطوير البرامج alone |
| document (provider document, service document, JSON document) | مستند (plural مستندات) | المستندات for documentation |
| diagram | رسم تخطيطي | مخطط (that is a schema) |
| mapping (YAML mapping, verb mapping, parameter mapping) | تعيين | |
| virtual machine / resource group (Azure) / label (Google Cloud) / organization (GCP, GitHub) | جهاز ظاهري (plural الأجهزة الظاهرية) / مجموعة موارد / تصنيف / مؤسسة | جهاز افتراضي, وسم (that is a tag) for a label |
| package / platform / tab (UI) / extension (OpenAPI `x-`, an app extension) | حزمة / منصة / علامة تبويب / ملحق | |
| action (GitHub action, IAM or policy action) | إجراء | |
| code, source code | التعليمات البرمجية، التعليمات البرمجية المصدر | الكود |
| console / initialization file / timeout / sandbox | وحدة التحكم / ملف التهيئة / مهلة / بيئة معزولة | |
| staging (rows in the SQL backend) / telemetry / ingestion / push (records to an endpoint) | التخزين المرحلي / بيانات القياس عن بعد / الاستيعاب / دفع | |
| projection / unnest / recursive (CTE, query) | الإسقاط / فك التداخل / عودي | |
| time zone / observability / severity / correlation key, correlation ID | المنطقة الزمنية / قابلية الملاحظة / مستوى الخطورة / مفتاح الارتباط، معرف الارتباط | |
| preprocessor / concurrently | المعالج المسبق / بالتوازي | متزامن for concurrently (that is synchronous) |
| fetch (a spec, results) | استرداد، الحصول على | جلب |
| LLM / connector (Claude) / catalog / deep link | نموذج اللغة الكبير (LLM) / موصل / كتالوج / رابط عميق | |
| dense rank / percentile rank / tile (`NTILE`) | الترتيب الكثيف / الترتيب المئيني / شريحة | |
| leading / trailing (spaces) / one-based | البادئة / اللاحقة / بترقيم يبدأ من 1 | |
| Kept in English | sidecar, crate, dotenv, bundle (the `.mcpb`; the format name MCP Bundle too), stdio, SaaS, bearer (in "bearer token" = رمز مميز من نوع bearer), span, exporter, receiver, collector, elicitation, tenancy, fan-out (in names; in prose التوزيع المتوازي); product, vendor and file-format names (JSON, YAML, CSV); third-party UI terms (Datadog facets, ChatGPT threads, GitHub stargazers and watchers) | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Arabic |
|---|---|
| Install | التثبيت |
| Installing StackQL | تثبيت StackQL |
| Getting Started | بدء الاستخدام |
| Command Line Usage | استخدام سطر الأوامر |
| Language Specification | مواصفات اللغة |
| Built-in Functions | الدوال المدمجة |
| Functions | الدوال |
| Developers | المطورون |
| Providers | الموفرون |
| Quick Starts | البدء السريع |
| AI Agents | وكلاء الذكاء الاصطناعي |
| MCP Server / MCP Tools / Embedded MCP | خادم MCP / أدوات MCP / MCP المضمن |
| Embedded (sidebar category) | مضمن |
| Blog | المدونة |
| Product Announcements | إعلانات المنتج |
| Provider Announcements | إعلانات الموفرين |
| Tutorials | البرامج التعليمية |
| Documentation | الوثائق |
| Contact us | اتصل بنا |
| Privacy Policy | سياسة الخصوصية |
| Terms of Service | شروط الخدمة |
| Support | الدعم |
| AI Reference | مرجع الذكاء الاصطناعي |
| More | المزيد |
| Welcome to StackQL | مرحبًا بك في StackQL |

Recurring headings, phrases and keywords (the same on every page):

| English | Arabic |
|---|---|
| Syntax | بناء الجملة |
| Arguments | الوسيطات |
| Return Value(s) (also Return Value, Return Values) | القيمة المرجعة (one form for all three) |
| Example / Examples | مثال / أمثلة (follow the number of the source) |
| Inputs / Output | الإدخالات / الإخراج |
| Flags | العلامات |
| Overview | نظرة عامة |
| How it works | آلية العمل |
| Modes | الأوضاع |
| Gating | التقييد |
| Install | التثبيت |
| Quick start | البدء السريع |
| Demo app | تطبيق توضيحي |
| Repo link | رابط المستودع |
| Vendoring / embedding | تضمين نسخة محلية / التضمين |
| Prerequisites | المتطلبات الأساسية |
| Next steps | الخطوات التالية |
| Related | محتوى ذو صلة |
| See also: | راجع أيضًا: |
| See X. | راجع X. |
| For more information, see X. | لمزيد من المعلومات، راجع X. |
| Optional. / Defaults to X. | اختياري. / القيمة الافتراضية: X. |
| Returns X. (the sentence that opens a function or tool page) | تعيد X. (feminine for a دالة or أداة, يعيد for a أمر) |
| Return the absolute value of a number (an example heading that names a task) | إرجاع القيمة المطلقة لعدد (a verbal noun) |
| X function in StackQL: ... (description pattern) | الدالة X في StackQL: ... |
| X window function in StackQL: ... (description pattern) | دالة النافذة X في StackQL: ... |
| X in StackQL: ... (description of a statement page) | X في StackQL: ... |
| ... using SQL in StackQL (description pattern) | ... باستخدام SQL في StackQL |
| Available in StackQL `vX` and later / releases from `vX` | متاح في StackQL من الإصدار `vX` فصاعدًا |
| Available in StackQL releases after `vX` | متاح في إصدارات StackQL بعد `vX` |
| None. (no arguments, no inputs) | لا يوجد. |
| Query / Results (tab labels), Output: | الاستعلام / النتائج، الإخراج: |
| Tested with (badge line on quick starts) | تم الاختبار باستخدام |
| Best Practices / Troubleshooting / Further reading | أفضل الممارسات / استكشاف الأخطاء وإصلاحها / قراءات إضافية |
| MCP tool listing X / MCP tool that does X (description pattern) | أداة MCP تسرد X / أداة MCP تقوم بـ X (or a verb that names the action: "أداة MCP تنفذ ...") |
| MCP overview (link text) | نظرة عامة على MCP |
| Argument / Required / Description (table headers) | الوسيطة / مطلوب / الوصف |
| Renderer / Shape (table headers) | أداة العرض / الشكل |
| Table / One row per X (table cells) | جدول / صف واحد لكل X |
| Yes / No (table cells) | نعم / لا |
| For example | على سبيل المثال |
| Aggregate function / Scalar function | دالة تجميعية / دالة عددية |
| When to use, Use case / Example usage | متى تستخدم، حالة الاستخدام / مثال على الاستخدام |
| Conclusion / Get started (homepage heading) | الخلاصة / ابدأ الآن |
| Returns : (a `<summary>` or bold label) | القيمة المرجعة: |
| Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered: | جرّب هذا الموجه مع أي عميل MCP يدعم elicitation ومسجل فيه خادم StackQL MCP: |
| Here's an example in Claude Desktop: | إليك مثالًا في Claude Desktop: |
| Server mode table cells: allow / refuse / needs approval; allowed / refused / (default) | سماح / رفض / يتطلب موافقة; مسموح / مرفوض / (افتراضي) |
| Gated by the server mode. / Allowed in every server mode. Read-only. (the Gating section of an MCP tool page) | يخضع لوضع الخادم. / مسموح في جميع أوضاع الخادم. للقراءة فقط. |
| `embedded sql backend` / `postgres sql backend` (the "Tested with" badges on quick starts) | واجهة SQL الخلفية المضمنة / واجهة SQL الخلفية postgres |
| Short "See also" badges that are not a page's exact title: StackQL Resource Hierarchy / Provider Sessions / Templating / Window Function Reference / X language spec | التسلسل الهرمي لموارد StackQL / جلسات الموفر / استخدام القوالب / مرجع دوال النافذة / X في مواصفات اللغة |
| keyword `cloud inventory` | جرد الموارد السحابية |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Arabic letters only from the Arabic block as Arabic uses them: ك (U+0643), ي (U+064A) and ى (U+0649), ه and ة as standard orthography requires. Never the Persian or Urdu forms ک (U+06A9), ی (U+06CC), ە or ۀ, which some keyboards produce and which break search. Hamza spelled by the standard rules (إ، أ، ؤ، ئ، ء; "إعداد"، "أداة"، "مسؤول"، "بيئة").
- Digits are Western (0 to 9) everywhere, as in the Arabic documentation of Microsoft, Google and AWS: in prose, tables, version numbers and counts. Never Arabic-Indic digits (U+0660 to U+0669) or Extended Arabic-Indic digits (U+06F0 to U+06F9), never the Arabic percent sign ٪ or the Arabic decimal separator ٫.
- Diacritics: none, with two exceptions. A shadda where a word would otherwise read as a different common word, mainly the imperative of form II verbs (شغّل، ثبّت، حدّد، فعّل، جرّب، غيّر) and معدّل; and tanwin fath on every word that ends in the alif of the indefinite accusative, written on the letter before the alif: adverbs (أيضًا، افتراضيًا، حاليًا، تلقائيًا، مثلًا، فصاعدًا، رقميًا، مسبقًا، دائمًا، أولًا) and nouns and adjectives alike ("تعيد عددًا صحيحًا"، "11 صفًا"، "إليك مثالًا"، "اسمًا مستعارًا"). A damma only on the first letter of a passive verb whose active reading would mislead (يُحل إلى، تُنفذ باستخدام). No fatha, kasra or sukun, and no tanwin elsewhere.
- Punctuation: the Arabic comma ، (U+060C), the Arabic semicolon ؛ (U+061B) and the Arabic question mark ؟ (U+061F) in Arabic prose; the full stop `.`, the colon `:`, the exclamation mark `!`, parentheses and `%` are the ASCII ones. No space before a punctuation mark, one space after. Inside inline code, code fences, front matter keys and quoted English output the punctuation is the source's.
- Quotation marks are straight double quotes ("..."). No guillemets (« »), no typographic quotes, no typographic apostrophe, so the text stays greppable.
- No dash punctuation other than the keyboard hyphen. Where the source uses ` - ` or ` -- ` as a dash inside a sentence or between a term and its explanation, write ` - `, or use a colon, the Arabic comma or parentheses. Never an em dash, an en dash or a double hyphen in prose.
- No invisible characters: no bidirectional control marks (U+200E LRM, U+200F RLM, U+061C ALM, U+202A to U+202E, U+2066 to U+2069), no zero-width joiner or non-joiner (U+200C, U+200D), no non-breaking space. The page is rendered right to left by the theme; text is typed in logical order and never reordered or marked to make it look right in an editor.
- No tatweel (ـ U+0640), except after a one-letter prefix written before a Latin word, inline code, a number or a link: "بـ `SELECT`"، "لـ AWS"، "كـ JSON" (tatweel, then one space). The conjunction و attaches directly to the next word, Latin or Arabic, and to inline code ("AWS وAzure وGoogle Cloud"، "و`DELETE`"). The definite article ال never attaches to a Latin word or inline code: use the class noun with the article ("الأمر `exec`"، "خادم MCP"، "موفر `aws`"), not "ال`exec`" or "الـ MCP".
- Word order is head first: the noun comes before what modifies it ("مفتاح API"، "متغير البيئة"، "موفر StackQL"، "خادم StackQL MCP"، "دالة النافذة `RANK`"). An inline code span that names a thing takes its class noun before it ("الأمر `stackql exec`"، "العلامة `--output`"، "المعلمة `region`"، "الدالة `json_extract`"، "جملة `SELECT`"، "الملف `stackql.yml`").
- Agreement: دالة، أداة، جملة and قيمة are feminine ("تعيد الدالة"، "الأداة متاحة"); أمر، استعلام، موفر، مورد and حقل are masculine ("يعيد الأمر"). Plurals of things take feminine singular agreement ("الموارد المتاحة"، "الأدوات التي تعيد"). Providers are the exception: this glossary treats موفر as a sound masculine plural, as Microsoft does for resource providers ("الموفرون المتاحون"، "يدعم الموفرون"، "قائمة الموفرين"), never "الموفرات". Counted nouns follow the usual rules with Western digits ("صف واحد"، "صفان"، "3 صفوف"، "11 صفًا"، "100 صف").
- A statement page opens with a feminine verb, the implied subject being جملة ("تنشئ"، "تحذف"، "تصف"), as a function page does for دالة ("تعيد").
- "we" as StackQL the project or company is first person plural ("نوصي بـ"، "ننشر"); "we" in a walkthrough that includes the reader is also first person plural ("سننشئ طريقة عرض").
- Prefer the active voice with a clear subject. Use "يتم" + verbal noun sparingly (not in every sentence); a passive verb ("تعاد القيمة"، "يُحل المتغير", with the damma only where the active reading would mislead) is fine where the agent is unknown.
- Lists of three or more items in prose join every item after the first with و ("الصفوف والأعمدة والجداول"); with long items write the Arabic comma before each و ("..."، و"...").
- Headings: no end punctuation; keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is. Latin names keep their own case.
- English loanwords and English terms from the "Kept in English" row are written plain, never in italics and never in quotes: added italics change the page structure that `yarn i18n:verify` and the node-count check compare.
- Names of languages and countries as Arabic writes them ("الإنجليزية"، "العربية"); months, when they occur, as يناير، فبراير، مارس. Product, provider and vendor names keep their own spelling in Latin script.
- List items and table cells end the way the source does: no full stop added to fragments, a full stop where the source item is a full sentence. Tables keep the source's column order (the browser shows the first column on the right).
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English.
- The `description` front matter stays one sentence and under about 170 characters.
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud inventory` -> جرد الموارد السحابية); keep the number and order of entries.
- Front matter must stay valid YAML. Keep the quoting of the source value. A value that contains `: ` (a colon followed by a space) or ` #` must be in double quotes, as must a value that starts with a quote, bracket, backtick, `*`, `&`, `!`, `%` or `@`; escape any inner double quote as `\"`. The description pattern "الدالة X في StackQL: ..." always needs the double quotes the source already has.
- Strings in JSX and in exported data arrays keep the quote style of the source; a translated string that gains a double quote goes in single quotes, and the other way around. Arabic has no apostrophes in ordinary words, so single-quoted strings are safe.
- Bold and italic markers stay where the source has them, with no space inside the markers. Arabic letters count as letters for the CommonMark rules, and a prefix letter (و، ب، ل، ك، ف) is a letter: an opening `**` or `*` must not have a letter directly before it when the character after it is punctuation or a backtick, and a closing `**` or `*` must not have a letter directly after it when the character before it is punctuation or a backtick (neither would be emphasis); `__` and `_` must never touch a letter on the outside. Leave a space, or move the prefix inside the markers ("و**مهم**" works because a letter follows the opening marker; "و**`SELECT`**" does not, write "و `SELECT`" or rephrase).
- Link text that is another page's title is that page's translated `title`, including the "See also" badges that wrap the title in inline code (`` [[` Window Functions `]] `` becomes `` [[` دوال النافذة `]] ``); `yarn i18n:verify` warns about the changed inline code on those pages, which is expected. Link text that is a heading of the target page is that heading's translation.
- Example prompts addressed to an AI assistant use the same plain imperative as the rest of the page ("اعرض"، "أنشئ"، "اسرد").
- The homepage tagline "Configuration-as-Data. Infrastructure-as-Context." stays in English.
- Arabic is about as long as English in characters, so a `length ratio` warning from `yarn i18n:verify` is worth a look for an omission, except on short pages whose code fences carry padded result tables.

## zh-Hant

Audience: engineers in Taiwan, Hong Kong and Macau reading technical documentation in Traditional Chinese. Traditional characters in the Taiwan standard forms (為, 裡, 著, 線, 啟, 戶, 群, 衛, 峰, 綠), never simplified characters and never the mainland vocabulary that a simplified-to-traditional conversion leaves behind (軟件, 數據, 默認, 信息, 網絡, 用戶, 文檔, 代碼, 字符串, 函數, 對象, 接口, 調用). The vocabulary is that of vendor documentation in Traditional Chinese (Microsoft Learn zh-tw, AWS and Google Cloud zh-TW), which readers in Hong Kong and Macau read as well. Translate from the English source; never convert or adapt the zh-Hans translation, whose vocabulary differs (row and column in particular: mainland 行 is a row, here 資料列 is a row and 資料行 a column). Plain technical-documentation register: declarative, short sentences, no marketing tone. Address the reader as 您, only where the source says "you"; prefer an impersonal sentence when it reads naturally. Keep the English terms that developers in Taiwan and Hong Kong use as they are; do not invent Chinese names for products or APIs.

Names that stay in English, in addition to the "All locales" list: SQL, CLI, API, SDK, JSON, YAML, CSV, HTTP, OAuth, IaC, CI/CD, LLM, MCP, macOS, Linux, Windows, Docker, Kubernetes, GitHub Actions, Homebrew, Chocolatey, and every provider, service, resource and method name. Acronyms are not expanded or glossed unless the source does so.

Terminology (use the rendering every time; do not alternate with the variants in the third column):

| English | Traditional Chinese | Not |
|---|---|---|
| provider (a StackQL provider) | Provider (capitalized, untranslated; no plural form) | 提供者, 提供程式, 供應商, 提供商 |
| cloud provider (the vendor) | 雲端服務供應商; short form 雲端供應商 | 雲服務商, 雲廠商, 雲提供商 |
| cloud (as a modifier) | 雲端 (雲端資源, 雲端 API) | 雲 alone as a modifier |
| service | 服務 | |
| resource | 資源 | |
| method | 方法 | |
| operation | 作業 (生命週期作業) | 操作 for an API operation |
| query (noun and verb) | 查詢 | |
| statement | 陳述式 (`SELECT` 陳述式) | 語句, 聲明, 敘述 |
| command (CLI) / subcommand | 命令 / 子命令 | 指令 |
| result set | 結果集 | |
| row / column | 資料列 / 資料行; 列 and 行 alone only in a table of output where the meaning is plain | 行 for a row, 列 for a column, 欄 |
| table | 資料表 (a markdown or HTML table: 表格) | 表 alone |
| view | 檢視 | 視圖 |
| materialized view | 具體化檢視 | 物化視圖, 實體化檢視 |
| window function | 視窗函式 | 窗口函數, 開窗函數 |
| window frame | 視窗框架 | 窗口幀 |
| partition (PARTITION BY) | 分割區 | 分區 |
| aggregate function / scalar function | 彙總函式 / 純量函式 | 聚合函數, 標量函數 |
| function (every SQL or programming function, including math, trigonometric and logarithmic ones) | 函式 (三角函式, 對數函式) | 函數 |
| expression / clause | 運算式 / 子句 | 表達式 |
| keyword / operator / predicate | 關鍵字 / 運算子 / 述詞 | 關鍵詞 (except front matter `keywords`), 操作符, 運算符, 謂詞 |
| subquery / alias / join | 子查詢 / 別名 / 聯結 | 連接, 連結 (that is a link) |
| data type / literal / identifier | 資料型別 / 常值 / 識別碼 | 數據類型, 資料類型, 字面量, 標識符 |
| string / substring / character | 字串 / 子字串 / 字元 | 字符串, 字符 |
| integer / floating point / boolean | 整數 / 浮點數 / 布林值 | 布爾值 |
| array / object / element / nested | 陣列 / 物件 / 元素 / 巢狀 | 數組, 對象, 嵌套 |
| whitespace | 空白字元 | 空白字符 |
| parameter / argument | 參數 / 引數 (the "Arguments" heading is 引數) | |
| variable / environment variable | 變數 / 環境變數 | 變量 |
| flag (CLI) / option | 旗標 / 選項 | 標誌, 標記 |
| field / key / value | 欄位 / 鍵 / 值 (a cryptographic or API key is 金鑰) | 字段, 密鑰 |
| key-value pair | 鍵值對 | |
| schema | schema (untranslated, lower case) | 模式, 架構, 結構描述 |
| data / database / information | 資料 / 資料庫 / 資訊 | 數據, 數據庫, 信息 |
| file / directory / folder | 檔案 / 目錄 / 資料夾 | 文件 (that is documentation) |
| default (noun and adjective) | 預設 | 默認, 缺省 |
| support (a feature) | 支援 | 支持 |
| run / execute | 執行 | 運行 |
| call / invoke / caller | 呼叫 / 叫用 / 呼叫端 | 調用, 調用方 |
| create / configure / settings | 建立 / 設定 / 設定 | 創建, 配置 |
| enable / disable | 啟用 / 停用 | 禁用 |
| integrate / optimize | 整合 / 最佳化 | 集成, 優化 |
| software / program / code / source code | 軟體 / 程式 / 程式碼 / 原始碼; 代碼 only for an identifier code (語言代碼, 地區代碼) | 軟件, 程序 (a stored procedure is 預存程序), 代碼 for program code, 源代碼 |
| status code / error code | 狀態碼 / 錯誤碼 | 狀態代碼 |
| port / connection / load balancer | 連接埠 / 連線 / 負載平衡器 | 端口, 連接 |
| script | 指令碼 | 腳本 |
| network / user / interface | 網路 / 使用者 / 介面 | 網絡, 用戶, 接口, 界面 |
| message / error message | 訊息 / 錯誤訊息 | 消息, 信息 |
| access (noun and verb) | 存取 | 訪問 |
| sign in / log in | 登入 | 登錄 |
| log (noun and verb) / audit log | 記錄 / 稽核記錄 | 日誌, 日志 |
| cache / local cache | 快取 / 本機快取 | 緩存, 本地 |
| memory / process / thread | 記憶體 / 處理程序 / 執行緒 | 內存, 進程, 線程 |
| asynchronous / synchronous | 非同步 / 同步 | 異步 |
| module / package / plugin / library | 模組 / 套件 / 外掛程式 / 程式庫 | 模塊, 包, 插件, 庫 alone |
| application, app | 應用程式 | 應用 alone |
| binary | 二進位檔 | 二進制 |
| instance (EC2, VM) | 執行個體 | 實例 |
| cluster / node / container / image (container) | 叢集 / 節點 / 容器 / 映像檔 | 集群, 鏡像 |
| virtual machine / disk / storage | 虛擬機器 / 磁碟 / 儲存體 | 虛擬機, 磁盤, 存儲 |
| bucket (object storage) / bucket (NTILE) | 儲存貯體 / 群組 | 存儲桶, 桶, 值區 |
| control plane / data plane | 控制平面 / 資料平面 | 控制面, 數據平面 |
| agent, AI agent | 代理程式, AI 代理程式 | 智能體, 智慧體, 代理 alone |
| agentic (as in agentic AI) | 代理式 | 智能體 |
| tool (an MCP tool) | 工具 | |
| prompt (for an LLM) | 提示詞 | 提示 alone (that is "tip") |
| elicitation (MCP) | 徵詢（elicitation） at first mention on a page, then 徵詢 | 引導, 啟發, 征詢 |
| gating / gated (MCP) | 管控 / 受管控; "gated by X" = 由 X 管控 | 門控, 把關 |
| approval / approve | 核准 / 核准 | 審批, 批准 |
| approval prompt, elicitation prompt (a dialog shown to the user, not an LLM prompt) | 核准要求, 徵詢要求 | 審批提示 |
| server mode, safety mode | 伺服器模式, 安全模式 | |
| read-only | 唯讀 | 只讀 |
| mutation / mutating statement | 變更 / 變更陳述式 | 突變 |
| lifecycle / resource lifecycle | 生命週期 / 資源生命週期 | 生命周期 |
| credentials | 憑證 | 認證, 憑據 |
| certificate (TLS) | 憑證 (TLS 憑證) | 證書 |
| authentication / authorization | 驗證 / 授權 (身分驗證 where the source stresses identity) | 認證, 鑑權, 身份 |
| principal / service principal | 主體 / 服務主體 | |
| account / service account | 帳戶 / 服務帳戶 | 賬號, 賬戶, 帳號 for a cloud account |
| token / API key / access key | 權杖 / API 金鑰 / 存取金鑰 (bearer 權杖) | 令牌, 密鑰 |
| secret / secret store | 祕密 / 祕密存放區 | 機密, 秘密 |
| scope (OAuth) | 範圍 | 作用域 |
| role / permission | 角色 / 權限 | |
| assume a role (AWS) | 擔任角色 | 代入角色 |
| identity provider (IdP, OIDC provider) | 身分提供者 | 身份提供商 |
| binding (IAM) / policy | 繫結 / 政策 | 綁定, 策略 |
| session / session variable / provider session | 工作階段 / 工作階段變數 / Provider 工作階段 | 會話, Session |
| deployment / deploy | 部署 | |
| provision (resources) | 佈建 | 預配, 置備, 供應 |
| infrastructure | 基礎架構 | 基礎設施, 基礎結構 |
| infrastructure as code | 基礎架構即程式碼 | 基礎設施即代碼 |
| agentic infrastructure | 代理式基礎架構; add （agentic infrastructure） at first mention on a page | 智能體基礎設施 |
| queryable infrastructure | 可查詢基礎架構; add （queryable infrastructure） at first mention on a page | |
| SQL for APIs (the pattern name) | SQL for APIs (untranslated); gloss once as （適用於 API 的 SQL） | |
| registry (generic) / provider registry | 登錄庫 / Provider 登錄庫 (the product name StackQL Registry stays English) | 註冊表, 註冊中心, 倉庫 |
| pull (a provider, an image) | 提取 | 拉取 |
| repository | 存放庫 | 倉庫, 儲存庫 |
| release | 發行版本 (noun), 發行 (verb) | 發布版本 |
| command line | 命令列 | 命令行 |
| shell / interactive shell | shell / 互動式 shell | 交互式 |
| server / client | 伺服器 / 用戶端 | 服務器, 服務端, 客戶端 |
| transport (MCP) | 傳輸方式 | |
| protocol / wire protocol | 協定 / 線路協定 (PostgreSQL 線路協定) | 協議, 有線協議 |
| stateless / stateful | 無狀態 / 具狀態 | 有狀態 |
| endpoint | 端點 | 終結點 |
| request / response / request body | 要求 / 回應 / 要求本文 | 請求, 響應, 請求體 |
| header (HTTP) | 標頭 (要求標頭 / 回應標頭) | 頭, 請求頭 |
| payload | 承載 | 負載, 酬載 |
| pagination | 分頁 | |
| output / output format / output mode | 輸出 / 輸出格式 / 輸出模式 | |
| template / templating | 範本 / 範本化 | 模板 |
| dry run | 試執行 | 試運行 |
| fan-out | 扇出 | |
| guardrail | 防護措施 | 護欄 |
| answer engine | 答案引擎 | |
| region / project / subscription / tenant | 區域 / 專案 / 訂用帳戶 / 租用戶 | 地域, 項目, 訂閱, 租戶 |
| open source | 開源 | 開放原始碼 |
| embed / embedded (Embedded MCP) | 內嵌 / 內嵌式 | 嵌入式, 嵌入 |
| installation / install | 安裝 | |
| documentation | 文件 | 文檔 |
| tutorial | 教學 | 教程 |
| quick start | 快速入門 | |
| getting started | 開始使用 | 入門 alone (too close to 快速入門) |
| announcement | 公告 | |
| feature | 功能 | 特性 |
| example | 範例 | 示例, 例子 |
| syntax / usage | 語法 / 用法 | |
| note / tip / warning (admonition titles are translated by the theme) | 注意 / 提示 / 警告 | |
| resource hierarchy | 資源階層 | 資源層次結構, 資源層級 |
| global (flags, scope) | 全域 (全域旗標) | 全局 |
| access management | 存取管理 | 訪問管理 |
| SQL backend | SQL 後端 | |
| query engine / parser / parse / plan (a statement) | 查詢引擎 / 剖析器 / 剖析 / 規劃 | 解析器, 解析 |
| reverse proxy / proxy | 反向代理 / Proxy | |
| delimiter, separator | 分隔符號 | 分隔符 |
| throw an error / return | 擲回錯誤 / 傳回 | 拋出, 返回 |
| match / replace (strings, regex) / pattern | 比對 / 取代 / 模式 | 匹配, 替換 |
| regular expression | 正規表示式 | 正則表達式, 規則運算式 |
| hash | 雜湊 | 哈希, 散列 |
| timestamp / time zone / Julian day / modifier | 時間戳記 / 時區 / 儒略日 / 修飾詞 | 時間戳, 修飾符 |
| well-formed / valid (JSON) | 格式正確 / 有效 | |
| state file | 狀態檔 | 狀態文件 |
| drift / infrastructure drift | 漂移 / 基礎架構漂移 | 偏移 |
| inventory / cloud inventory | 資源清單 / 雲端資源清單 | 清查 |
| compliance | 合規性 | 合規 alone |
| operator (the person running StackQL) | 維運人員 | 運維人員, 操作員 |
| filter (noun and verb) | 篩選 (篩選條件) | 過濾 |
| concatenation / concatenate | 串連 | 拼接 |
| relation / dereference | 關聯 / 解除參考 | 解引用 |
| contract (I/O contract, safety contract) | 契約 | 約定 |
| source of truth | 事實來源 | 權威來源 |
| entitlement | 權利 | 權益 |
| snapshot / checksum / digest | 快照 / 總和檢查碼 / 摘要 | 校驗和 |
| allowlist | 允許清單 | 白名單 |
| opt in / explicit opt-in | 選擇加入 / 明確選擇加入 | 選擇啟用 |
| hint (in an MCP error or tool annotation) | 提示資訊 | |
| advertise (a capability) | 宣告 | 通告 |
| bundle (the downloadable `.mcpb`) | 安裝套件; the format name MCP Bundle stays in English | 捆綁包 |
| artifact (build or release output) | 成品 | 製品, 構件, 工件 |
| stack (of resources) | 堆疊 | 堆棧 |
| pipeline | 管線 (CI/CD and data or telemetry) | 流水線, 管道 |
| notarized / signed | 經過公證 / 已簽署 | 已簽名 |
| common table expression (CTE) | 通用資料表運算式（CTE） | 公用表表達式 |
| monitor / metrics / telemetry / trace | 監控 / 指標 / 遙測 / 追蹤 | |
| runtime / metadata / dependency | 執行階段 / 中繼資料 / 相依性 | 執行時期, 元資料, 依賴 for a dependency |
| resolve (credentials, a variable, a binary) / validate | 解析 / 驗證 (as Microsoft, the same word as authenticate) | 解決 |
| render (a template) / escape (a character) / placeholder | 轉譯 / 逸出 / 預留位置 | 渲染, 跳脫, 佔位符 |
| build (noun and verb) / publish / commit (git) | 建置 / 發佈 / 提交 | 組建, 發布, 認可 |
| deprecated / legacy / ad hoc | 已淘汰 / 舊版 / 即席 | 已棄用 |
| desired state / reconciliation / persistent | 期望狀態 / 調和 / 持續性 | 所需狀態, 永續性 |
| observability / workload / understand / distribution | 可觀測性 / 工作負載 / 了解 / 分佈 | 可觀察性, 工作負荷, 瞭解, 分布 |
| builder (SDK API) / supervisor (OTP) / pin (a version, a sha256) | 建立器 / 監督器 / 釘選 (sha256 釘選值) | 建構器, 監督者 |
| zone (Google Cloud) / Availability Zone (AWS) | 可用區 / 可用區域 | |
| console / terminal / listener / handle / handshake | 主控台 / 終端機 / 接聽程式 / 控制代碼 / 交握 | 控制台, 監聽器 |
| cursor (database, pagination) / offset (LAG, LEAD) | 資料指標 / 位移 | 游標 for a database cursor |
| collation / grammar / peer (window functions) / frame clause | 定序 / 文法 (語法 is syntax) / 對等資料列 / 框架子句 | |
| exception / suffix / prefix | 例外狀況 / 尾碼 / 前置詞 | |
| token (an LLM token) / context window | token / 上下文視窗 | 權杖 (that is an auth token) |
| rate limit / personal access token | 速率限制 / 個人存取權杖 | |
| workflow / job (GitHub Actions) / tab (UI) / extension (Goose, browser) | 工作流程 / 作業 / 索引標籤 / 擴充功能 | |
| OTel exporter / receiver / instrumentation scope / flush / backoff | 匯出工具 / 接收器 / 檢測範圍 / 排清 / 退避 | |
| Kept in English | sidecar, vendored, crate, worker, bearer (bearer 權杖), dotenv, pull request, fork, issue, webhook, span, sink, connector (Claude's Connectors), facet | |

UI and navigation labels (navbar, footer, sidebar categories):

| English | Traditional Chinese |
|---|---|
| Install | 安裝 |
| Installing StackQL | 安裝 StackQL |
| Getting Started | 開始使用 |
| Command Line Usage | 命令列用法 |
| Language Specification | 語言規格 |
| Functions / Built-in Functions | 函式 / 內建函式 |
| Aggregate / Date and Time / General / Hash / Math / Logarithmic / Trigonometric / Regular Expressions / Session / String / Window Functions (function categories) | 彙總 / 日期和時間 / 一般 / 雜湊 / 數學 / 對數函式 / 三角函式 / 正規表示式 / 工作階段 / 字串 / 視窗函式 |
| Developers | 開發人員 |
| Providers | Provider |
| Quick Starts | 快速入門 |
| AI Agents | AI 代理程式 |
| MCP Server / MCP Tools / Embedded MCP / Embedded | MCP 伺服器 / MCP 工具 / 內嵌式 MCP / 內嵌式 |
| Blog | 部落格 |
| Product Announcements | 產品公告 |
| Provider Announcements | Provider 公告 |
| Tutorials | 教學 |
| Documentation | 文件 |
| Contact us | 聯絡我們 |
| Privacy Policy | 隱私權政策 |
| Terms of Service | 服務條款 |
| Support | 支援 |
| AI Reference | AI 參考 |
| More | 更多 |
| Welcome to StackQL | 歡迎使用 StackQL |

Recurring headings, phrases and keywords (the same on every page):

| English | Traditional Chinese |
|---|---|
| Syntax | 語法 |
| Arguments | 引數 |
| Return Value(s) | 傳回值 |
| Example, Examples | 範例 |
| Inputs / Output | 輸入 / 輸出 |
| Flags | 旗標 |
| Overview | 概觀 |
| How it works | 運作方式 |
| Modes | 模式 |
| Gating | 管控 |
| Install | 安裝 |
| Quick start | 快速入門 |
| Demo app | 示範應用程式 |
| Repo link | 存放庫連結 |
| Vendoring / embedding | Vendoring / 內嵌 |
| Prerequisites | 必要條件 |
| Next steps | 後續步驟 |
| Related | 相關內容 |
| See also: | 另請參閱： |
| For more information, see X. | 如需詳細資訊，請參閱 X。 |
| Optional. / Defaults to X. | 選用。 / 預設為 X。 |
| X function in StackQL: ... (description pattern) | StackQL 中的 X 函式：... |
| Argument / Required / Description (table headers) | 引數 / 必要 / 描述 |
| Yes / No (table cells) | 是 / 否 |
| For example | 例如 |
| Aggregate function / Scalar function | 彙總函式 / 純量函式 |
| keyword `cloud inventory` | 雲端資源清單 |
| keywords `stackql`, `infrastructure-as-code`, `configuration-as-data`, `mcp`, `model context protocol`, `cspm` | unchanged |

Style:

- Punctuation in Chinese prose is full width: ，。：；？！（）、 and 「」 for quotation (『』 for a quotation inside a quotation), as in Taiwan usage; never “” or ‘’. Use 、 between the items of a short inline list. Everything inside code fences and inline code keeps its half-width punctuation, and a sentence that is entirely Latin text or code keeps half-width punctuation too.
- Parentheses in a Chinese sentence are full width even when they hold English: 基礎架構即程式碼（IaC）.
- One half-width space between Chinese characters and an adjacent Latin word, number, inline code span or link whose text is Latin: 使用 `stackql exec` 執行查詢, 支援 3 種模式, 在 StackQL 中. No space next to full-width punctuation: 執行 `stackql shell`。 and （請參閱 `--output`）.
- No dash punctuation in prose: neither ——, nor a spaced hyphen. Where the source uses ` - ` as a dash inside a sentence, use ， or ： or split the sentence. A ` - ` separator in a title between two names stays.
- Headings: translate the text, no end punctuation, keep the `{#id}` suffix. A heading that is only a name, a command or a function (`COUNT`, `stackql exec`, AWS) stays as it is.
- List items and table cells end the way the source does: no 。 added to fragments, 。 where the source item is a full sentence.
- Quoted command output, log lines and error text stay in English. UI text of third-party consoles (AWS, Azure, Google Cloud, GitHub) stays in English, optionally followed by a Chinese gloss in （）only if the source explains it.
- Numbers are Arabic numerals; versions, dates and units as in the source.
- The `description` front matter stays one sentence and under about 80 characters (count Chinese characters and Latin words alike; trim filler rather than facts).
- `keywords` in front matter: names stay in English, generic terms are translated (`cloud` -> 雲端, `infrastructure as code` -> 基礎架構即程式碼); keep the number and order of entries.
- Bold and italic markers stay where the source has them. Put no space inside the markers (`**唯讀**`, not `** 唯讀 **`).
- A closing `**` that follows full-width punctuation or a closing 」 and is followed directly by a Chinese character does not close in CommonMark, and the asterisks show on the page (`**適用情境：**任何` renders literally). Move the punctuation outside the markers (`**適用情境**：任何`) or leave a space after the closing marker. The same holds for an opening marker between a Chinese character and full-width punctuation, and for `_` and `__`, which never open or close next to a Chinese character: where the source has `__bold__` or `_italic_` next to translated text, write `**bold**` or `*italic*`.
- "See X" is 請參閱 X, never 參見 or 請參見.
- Front matter must stay valid YAML: keep the quoting of the source value, and put a value in double quotes if the Chinese text contains a half-width `: ` or ` #`, or starts with a quote, bracket, backtick, `*`, `&`, `!`, `%` or `@`, or with 「. A full-width ： needs no quoting.
- Chinese text is shorter than English, so the `length ratio` warning from `yarn i18n:verify` is expected on prose-heavy pages and can be ignored.
