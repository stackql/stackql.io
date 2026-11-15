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
