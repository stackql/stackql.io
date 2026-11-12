---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "La commande stackql mcp exécute StackQL comme serveur Model Context Protocol pour agents IA : modes de déploiement et de sécurité, rechargement des identifiants, journal d'audit."
image: "/img/stackql-featured-image.png"
---

Commande utilisée pour lancer StackQL en tant que serveur Model Context Protocol (MCP), ce qui permet aux agents et assistants IA d'interagir avec l'infrastructure cloud à l'aide des capacités de requête de StackQL.

:::info[Qu'est-ce que MCP ?]

Le [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) est un protocole ouvert qui standardise la manière dont les applications d'IA interagissent avec les sources de données et les outils externes.  En exécutant StackQL en tant que serveur MCP, vous permettez aux agents IA (Claude, Cursor, Continue, agents personnalisés, etc.) d'interroger et de gérer l'infrastructure cloud de plusieurs providers en langage naturel.

:::

* * *

### Syntaxe {#syntax}

`stackql mcp [flags]` pour un serveur MCP autonome.

`stackql srv [flags]` pour exécuter le serveur MCP aux côtés du serveur de protocole filaire PostgreSQL dans le même processus.

* * *

### Modes de déploiement {#deployment-modes}

Le serveur MCP de StackQL peut être déployé dans trois configurations différentes pour répondre à diverses exigences d'architecture.  Les exemples ci-dessous activent explicitement le service sans authentification avec `"allow_unauthenticated": true`, ce qui convient au développement local ; pour tout ce qu'un autre processus ou un autre hôte peut atteindre, configurez plutôt un jeton bearer (voir [Authentification des clients HTTP](#http-client-authentication)).

#### 1. Serveur MCP autonome {#1-standalone-mcp-server}

Exécutez StackQL en tant que serveur MCP dédié sur un port donné.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**Cas d'utilisation :** lorsque vous avez uniquement besoin d'un accès par le protocole MCP et que la compatibilité avec le protocole filaire PostgreSQL n'est pas nécessaire.

#### 2. Serveur MCP + PostgreSQL (en mémoire) {#2-mcp--postgresql-server-in-memory}

Exécutez simultanément les serveurs MCP et PostgreSQL, avec une communication en mémoire entre eux.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**Cas d'utilisation :** lorsque vous avez besoin à la fois d'un accès par le protocole MCP pour les agents IA et du protocole filaire PostgreSQL pour les clients de base de données traditionnels, avec des performances maximales grâce à la communication en mémoire.

#### 3. Serveur MCP + PostgreSQL (proxy inverse) {#3-mcp--postgresql-server-reverse-proxy}

Exécutez les deux serveurs avec une communication TCP, ce qui prend en charge les déploiements distribués et le chiffrement TLS.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**Avec chiffrement TLS :**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS chiffre le transport mais n'authentifie pas le client ; cet exemple exige donc aussi le jeton bearer contenu dans `STACKQL_MCP_TOKEN`.

**Cas d'utilisation :** lorsque vous devez séparer les charges de travail MCP et PostgreSQL entre différents processus ou hôtes, ou lorsque vous avez besoin du chiffrement TLS pour le point de terminaison MCP.

* * *

### Options de configuration {#configuration-options}

#### Type de serveur MCP {#mcp-server-type}

| Type | Description |
|--|--|
|`http`|Mode serveur HTTP direct - les requêtes MCP sont traitées dans le processus par le moteur StackQL.|
|`reverse_proxy`|Mode proxy inverse - les requêtes MCP sont transmises à un serveur PostgreSQL sous-jacent via le DSN configuré.  Utilisé avec `stackql srv`.|
|`stdio`|Transport par entrée/sortie standard - le serveur lit les requêtes sur `stdin` et écrit les réponses sur `stdout`.  Utilisé par les clients MCP embarqués dans un éditeur (Claude Desktop, Cursor, etc.).|

#### Format de journal MCP {#mcp-log-format}

L'option `--mcp.log.format` sélectionne l'encodage du journal d'audit : `jsonl` (valeur par défaut) ou `otel` (enregistrements de journal OpenTelemetry OTLP/JSON).  Elle prévaut sur `server.audit.format` dans l'objet de configuration.  Consultez [Format de journal](#log-format).

#### Version du protocole MCP {#mcp-protocol-version}

L'option `--mcp.protocol.version` définit la révision du protocole la plus récente que le serveur annonce : `auto` (valeur par défaut, toutes les révisions prises en charge), `2026-07-28` (sans session uniquement) ou une révision plus ancienne telle que `2025-11-25` (cette révision et les précédentes).  Elle prévaut sur `server.protocol_version` dans l'objet de configuration.  Consultez [Fixer la révision](#pinning-the-revision).

#### Objet de configuration MCP {#mcp-configuration-object}

L'option `--mcp.config` accepte un objet JSON de la structure suivante.

##### Configuration du serveur {#server-configuration}

| Champ | Description | Obligatoire |
|--|--|--|
|`server.transport`|Protocole de transport - `http` ou `stdio`.|Oui|
|`server.address`|Adresse et port d'écoute du serveur MCP (par exemple `127.0.0.1:9912`).  Transport HTTP uniquement.|Pour `http`|
|`server.tls_cert_file`|Chemin du fichier de certificat TLS pour HTTPS.|Non|
|`server.tls_key_file`|Chemin du fichier de clé privée TLS pour HTTPS.|Non|
|`server.auth_token_env_var`|Nom d'une variable d'environnement contenant un jeton bearer prépartagé.  Lorsqu'il est défini, chaque requête HTTP doit porter `Authorization: Bearer <token>` ; le serveur refuse de démarrer si la variable est absente ou vide.  Consultez [Authentification des clients HTTP](#http-client-authentication).|Ce champ ou `allow_unauthenticated`, pour `http`|
|`server.allow_unauthenticated`|Activation explicite du service du transport HTTP sans authentification des clients.  Valeur par défaut : `false`.|Ce champ ou `auth_token_env_var`, pour `http`|
|`server.mode`|Contrat de sécurité qui contrôle les mutations et les opérations de cycle de vie.  L'une des valeurs `read_only`, `safe` (valeur par défaut), `delete_safe`, `full_access`.  Consultez [Modes du serveur](#server-modes).|Non|
|`server.read_only`|Alias booléen **hérité** de `mode: read_only`.  Lorsque les deux champs sont définis, `mode` l'emporte.|Non|
|`server.stateless`|Sert Streamable HTTP sans sessions (pas de `Mcp-Session-Id`), ce qui est la manière dont la révision du protocole `2026-07-28` est servie en HTTP.  Valeur par défaut : `false`.  Ignoré pour `stdio`.  Consultez [Prise en charge des révisions du protocole](#protocol-revision-support).|Non|
|`server.protocol_version`|Révision du protocole la plus récente annoncée : `auto` (valeur par défaut), `2026-07-28` ou une révision plus ancienne telle que `2025-11-25`.  `2026-07-28` implique `stateless` pour Streamable HTTP.  Consultez [Fixer la révision](#pinning-the-revision).|Non|
|`server.audit`|Configuration du sous-système d'audit, dont `audit.format` (`jsonl` ou `otel`).  Consultez [Journal d'audit](#audit-log).|Non|

##### Configuration du backend (mode proxy inverse uniquement) {#backend-configuration-reverse-proxy-mode-only}

| Champ | Description | Obligatoire |
|--|--|--|
|`backend.dsn`|Chaîne de connexion PostgreSQL du serveur StackQL backend.|Oui (pour `reverse_proxy`)|

<br />

:::info

Le DSN du backend doit inclure le paramètre `default_query_exec_mode=simple_protocol` pour une compatibilité optimale.

:::

##### Listes d'autorisation {#allowlists}

| Champ | Description |
|--|--|
|`enabled_tools`|Tableau des noms d'outils que le serveur est autorisé à publier.  S'il est omis ou vide, tous les outils intégrés sont enregistrés.  Sert à exposer un sous-ensemble restreint (par exemple un serveur d'inventaire en lecture seule).|
|`enabled_prompts`|Tableau de noms de prompts.  Même sémantique que `enabled_tools`, mais pour la surface des prompts.|

<br/>

Exemple - un serveur qui publie uniquement `server_info` et `list_providers` :

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### Prise en charge des révisions du protocole {#protocol-revision-support}

La révision du protocole `2026-07-28`, `server.stateless` et le format de journal d'audit `otel` sont disponibles dans les versions de StackQL à partir de `v0.11.660`.  `server.protocol_version` et l'option `--mcp.protocol.version` sont disponibles dans les versions de StackQL à partir de `v0.12.732`.

Le serveur parle toutes les révisions du Model Context Protocol prises en charge par le [SDK MCP Go](https://github.com/modelcontextprotocol/go-sdk) et négocie client par client, de sorte qu'un parc de clients hétérogènes fonctionne avec un seul serveur.

| Révision | Cycle de vie | Demande d'approbation (`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28` (annoncée)|Pas de handshake : `server/discover`, puis chaque requête porte `io.modelcontextprotocol/protocolVersion` et `io.modelcontextprotocol/clientCapabilities` dans `_meta` (plus les en-têtes `Mcp-Protocol-Version`, `Mcp-Method` et `Mcp-Name` en HTTP).|Résultat `input_required`, que le client relance avec `inputResponses` (SEP-2322).|
|`2025-11-25`, `2025-06-18`|Handshake `initialize` / `notifications/initialized`.|Requête `elicitation/create` à l'initiative du serveur.|
|`2025-03-26`, `2024-11-05`|Comme ci-dessus.|Comme ci-dessus.|

<br />

**stdio** sert toutes les révisions dans un seul processus : la première requête d'un client `2026-07-28` est servie sans handshake et le `initialize` d'un client plus ancien fonctionne toujours.  Rien n'est à configurer pour Claude Desktop, les lanceurs npm / PyPI ou l'image Docker.

**Streamable HTTP** utilise par défaut le modèle avec état, à une session par client (`Mcp-Session-Id`), que le SDK sert pour les révisions jusqu'à `2025-11-25`.  Une requête `2026-07-28` reçoit l'erreur JSON-RPC `-32022` (version de protocole non prise en charge ; un simple HTTP 400 dans les versions jusqu'à `v0.12.718`) dont `data.supported` liste les révisions à handshake, et `server/discover` annonce la même chose ; le client négocie donc une révision inférieure et les intégrations HTTP existantes conservent leurs sessions et leurs demandes d'approbation inchangées.  Définissez `server.stateless` (ou fixez la révision à `2026-07-28`, [ci-dessous](#pinning-the-revision)) pour servir `2026-07-28` nativement en HTTP :

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

Un serveur sans session n'émet aucun `Mcp-Session-Id`, répond à `GET` et `DELETE` par 405 (il n'y a pas de session à fermer), garde `tools/list`, `prompts/list` et `resources/list` invariants d'une connexion à l'autre, et fait passer l'aller-retour d'approbation par `input_required`.  Il accepte toujours le `initialize` d'un client plus ancien et lui sert les lectures, mais il ne peut pas conserver la capacité d'élicitation que ce client a déclarée à l'initialisation (chaque requête obtient une session éphémère) ; les clients plus anciens ne peuvent donc pas approuver les écritures soumises à approbation sur un serveur sans session.  Choisissez `stateless` pour les hôtes à la révision actuelle et la valeur par défaut pour un parc qui comprend encore des clients plus anciens.

Le serveur ne conserve aucun état entre les appels : le mode, l'audit et les identifiants des providers relèvent de la configuration au niveau du processus, de sorte que rien n'a besoin d'être déplacé derrière des handles explicites.

#### Fixer la révision {#pinning-the-revision}

Par défaut, le serveur annonce toutes les révisions prises en charge et négocie client par client.  L'option `--mcp.protocol.version`, ou `server.protocol_version` dans l'objet de configuration (l'option l'emporte), définit la révision la plus récente annoncée :

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| Valeur | Annoncé | Effet |
|--|--|--|
|`auto` (valeur par défaut) ou absente|Toutes les révisions prises en charge.|Négociation comme ci-dessus : la révision la plus élevée que les deux parties parlent.|
|`2026-07-28`|`2026-07-28` uniquement.|Sans session uniquement, et `stateless` est implicite pour Streamable HTTP.  Le `initialize` d'un client plus ancien reçoit la réponse `2025-11-25`, signal pour ce client de se déconnecter plutôt que de lire la réponse comme le nouveau cycle de vie.|
|`2025-11-25` (ou toute révision plus ancienne)|Cette révision et toutes les précédentes.|Cycle de vie à handshake uniquement : une requête `2026-07-28` reçoit l'erreur JSON-RPC `-32022` avec `data.supported` listant les révisions annoncées, afin que le client puisse renégocier.  Les clients à handshake plus anciens conservent leur propre révision.|

<br />

Toute autre valeur fait échouer la validation de la configuration au démarrage, et l'erreur indique les valeurs autorisées.

#### Limites de transport {#transport-limits}

Les corps des requêtes Streamable HTTP sont plafonnés à 4 MiB (HTTP 413 au-delà), un JSON imbriqué sur plus de 1000 niveaux est rejeté avant l'analyse, et une trame `stdio` est plafonnée à 8 MiB.  Aucune de ces limites n'est configurable ; toutes se situent bien au-dessus de toute instruction SQL que le serveur accepte.

* * *

### (Re)chargement des identifiants (`--env.file` + `reload_credentials`) {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` et l'outil `reload_credentials` sont disponibles dans les versions de StackQL postérieures à `v0.10.542`.

:::

Le bloc d'environnement d'un processus est figé à son lancement.  Cela pose deux problèmes pour les serveurs MCP :

- Un serveur MCP `stdio` lancé par un client de bureau (Claude Desktop, Cursor, etc.) sans les variables d'environnement d'identifiants ne peut jamais les voir - le client transmet son propre environnement au sous-processus.
- Les identifiants de courte durée renouvelés n'atteignent jamais une session de serveur de longue durée.

L'option `--env.file` comble ces deux lacunes en désignant un fichier de type dotenv qui sert de magasin d'identifiants modifiable :

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### Sémantique du chargement {#sourcing-semantics}

Le fichier est chargé dans l'environnement du processus au démarrage, pour chaque point d'entrée (`exec`, `shell`, `srv`, `registry`, `mcp`) :

- Lorsque l'option est vide ou absente, rien ne se passe - les identifiants sont résolus à la demande depuis l'environnement du processus au moment de la requête, exactement comme dans le comportement historique.
- Un fichier absent au chemin indiqué est une opération sans effet tolérée (le fichier peut être créé plus tard).
- Un fichier mal formé ou illisible est une erreur fatale au démarrage.
- Seules les clés dont la valeur n'est pas vide sont définies ; les variables d'environnement existantes du processus **sont** écrasées (le fichier est la source de vérité) ; rien n'est jamais supprimé.
- L'option est disponible en ligne de commande uniquement - elle n'est pas lue depuis `.stackqlrc`.

Le format du fichier est un `KEY=VALUE` par ligne.  Les commentaires `#`, les lignes vides, un préfixe `export ` facultatif, des guillemets simples ou doubles facultatifs autour de la valeur et les fins de ligne CRLF sont tous tolérés :

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

L'outil [`reload_credentials`](/mcp/reload_credentials) recharge le fichier `--env.file` dans l'environnement du processus en cours de session, puis indique l'état de résolution des identifiants par provider.  Les valeurs des secrets ne sont jamais renvoyées, journalisées ni auditées - uniquement les noms et les états.  Sans `--env.file` configuré, l'outil se réduit à une simple sonde d'état.

Lorsqu'une requête échoue à la résolution des identifiants, l'erreur MCP porte une indication invitant l'agent à appeler `reload_credentials` puis à réessayer ; les clients agentiques se rétablissent ainsi d'eux-mêmes : écrivez l'identifiant dans le fichier, demandez à l'agent de recharger, et la requête en échec réussit à la nouvelle tentative - aucun redémarrage du serveur n'est nécessaire.

`reload_credentials` n'est pas pris en charge en mode backend `reverse_proxy` - les requêtes s'exécutent dans le processus du serveur stackql distant ; l'outil renvoie donc une erreur invitant l'utilisateur à recharger sur le serveur sous-jacent.

#### Windows et Claude Desktop {#windows-and-claude-desktop}

`setx` écrit dans le registre, pas dans les processus en cours d'exécution, et Claude Desktop transmet son propre environnement (périmé) aux sous-processus MCP - les changements d'identifiants nécessitent donc normalement un redémarrage complet de Claude Desktop.  Avec `--env.file`, le fichier d'identifiants peut être créé ou renouvelé à tout moment et `reload_credentials` le prend en compte sans aucun redémarrage.  Consultez [Utiliser StackQL avec Claude Desktop](/getting-started/claude-desktop) pour un exemple de configuration complet.

* * *

### Authentification des clients HTTP {#http-client-authentication}

:::note

`server.auth_token_env_var` et `server.allow_unauthenticated` sont disponibles dans les versions de StackQL à partir de `v0.12.742`.

:::

Le transport HTTP exige l'authentification des clients.  `server.auth_token_env_var` nomme une variable d'environnement contenant un jeton bearer prépartagé ; chaque requête doit porter `Authorization: Bearer <token>`, faute de quoi elle reçoit la réponse `401`.  Le jeton est lu depuis l'environnement ; il n'apparaît donc jamais dans `--mcp.config` ni dans les arguments du processus.

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[Authentification du serveur requise]

Servir sans jeton relève d'une activation explicite : définissez `"allow_unauthenticated": true` dans `server`.  Sans aucune des deux clés, le serveur s'arrête au démarrage avec un message qui nomme les deux ; il s'arrête aussi si la variable du jeton est absente ou vide.  À partir de la version `v0.12.742`, vous devez ajouter soit `auth_token_env_var`, soit `"allow_unauthenticated": true` à `server` lorsque vous utilisez le transport `http`.

:::

TLS (`tls_cert_file` / `tls_key_file`) chiffre le transport mais n'authentifie pas le client ; associez-le à un jeton pour tout écouteur au-delà de l'interface de bouclage.  Les requêtes de navigateur cross-origin (un `Origin` non concordant, ou un `Sec-Fetch-Site` cross-site) sont refusées avec `403`, quelle que soit l'authentification.  Le `stackql_mcp_client` fourni n'envoie aucun en-tête `Authorization` ; un serveur qu'il pilote doit donc activer explicitement le service sans authentification.

À partir de `v1.0`, démarrer le serveur avec [`--access.config`](/command-line-usage/access-management) remplace le contrôle par jeton partagé par une authentification par appelant : chaque requête HTTPS est vérifiée avec le schéma configuré (JWT, bearer statique ou TLS mutuel) et autorisée au regard des règles de politique.  Le TLS du serveur est alors obligatoire, l'authentification `local_os` est refusée en HTTP, et une requête sans identifiants ne se rabat jamais sur le jeton d'environnement du serveur.  La gestion des accès n'est pas disponible avec le type de serveur `reverse_proxy`.

* * *

### Modes du serveur {#server-modes}

`server.mode` choisit l'un des quatre contrats de sécurité.  Tous les quatre autorisent `SELECT` et les lectures de métadonnées ; ils diffèrent dans leur traitement des mutations et des opérations de cycle de vie.

| Mode | `SELECT` / métadonnées | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC` (cycle de vie) |
|--|--|--|--|--|
|`read_only`|autorisé|refusé|refusé|refusé|
|`safe` (valeur par défaut)|autorisé|approbation requise|approbation requise|approbation requise|
|`delete_safe`|autorisé|autorisé|approbation requise|approbation requise|
|`full_access`|autorisé|autorisé|autorisé|autorisé|

<br />

**Refusé** renvoie immédiatement une erreur au client.

**Approbation requise** utilise le flux d'élicitation MCP, dont la forme dépend de la [révision du protocole](#protocol-revision-support) négociée :

- Si le client a annoncé la capacité d'élicitation (à l'initialisation, ou dans les capacités client de `_meta` par requête en `2026-07-28`), le serveur demande à l'utilisateur d'approuver l'action au moyen d'un court message (nom de l'outil, classe de requête, SQL).  En `2026-07-28`, la demande est renvoyée sous la forme d'un résultat `input_required` portant une requête d'entrée `elicitation/create` sous l'identifiant `stackql_approval`, et le client relance l'appel avec la réponse dans `inputResponses` ; sur les révisions antérieures, le serveur envoie lui-même la requête `elicitation/create` en cours d'appel.  L'utilisateur accepte, refuse ou annule.
- Si le client n'a **pas** annoncé l'élicitation, l'outil est refusé avec un message qui explique ce manque et oriente l'exploitant vers le mode `full_access`.

L'approbation est donnée par le client ; elle protège donc contre un agent qui agit sans son utilisateur, pas contre un appelant non fiable.  Associez le transport HTTP à l'[authentification des clients](#http-client-authentication) lorsque les appelants ne sont pas de confiance.

Le mode est global pour chaque serveur.  Il n'existe pas de dérogation par outil.

À partir de `v1.0`, [`run_select_query`](/mcp/run_select_query) refuse `INSERT`, `UPDATE`, `REPLACE`, `DELETE` et `EXEC` dans tous les modes, y compris `full_access` ; l'erreur nomme `run_mutation_query` ou `run_lifecycle_operation`.  Chaque outil porte aussi un `title` et les quatre indications de comportement MCP sous forme de booléens explicites (`readOnlyHint`, `destructiveHint`, `idempotentHint`, `openWorldHint`), dérivées de la même classification que celle appliquée par le contrôle de politique.  `openWorldHint` vaut `true` pour `list_registry`, `pull_provider`, `run_select_query`, `run_mutation_query` et `run_lifecycle_operation`, qui atteignent les points de terminaison du registre ou des providers, et `false` pour les outils qui ne lisent que l'état local.  Les indications sont consultatives, pour l'expérience utilisateur du client ; l'application reste du ressort du contrôle de politique.

:::info[Contrôle des charges utiles]

À partir de la version `v0.12.742`, une charge utile contenant plusieurs instructions est contrôlée par son instruction la plus privilégiée dans tous les modes ; `SELECT 1; DELETE ...` est donc traité comme un `DELETE` et l'enregistrement d'audit porte le `query_class` `mutation_delete`.  `validate_select_query` accepte exactement une instruction.

:::

#### Changement de comportement du mode par défaut {#default-mode-behaviour-change}

Les versions précédentes n'appliquaient la sécurité qu'au moyen d'une seule option `read_only: true/false` dont la valeur par défaut était "aucune application".  La valeur par défaut actuelle (`mode: safe`) soumet d'emblée les mutations à l'approbation de l'utilisateur.  Les exploitants qui utilisent un client compatible avec l'élicitation (Claude Desktop, Cursor, Continue, etc.) verront une demande d'approbation par mutation.  Les exploitants qui exécutent des automatisations, ou le `stackql_mcp_client` fourni (qui n'annonce pas l'élicitation), doivent activer explicitement `mode: full_access` pour exécuter des mutations et des opérations de cycle de vie.

La clé JSON / YAML héritée `read_only: true` est toujours acceptée par compatibilité ascendante et est traitée comme équivalente à `mode: read_only`.

#### Exemples {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### Journal d'audit {#audit-log}

Chaque appel d'outil écrit un enregistrement dans la destination d'audit configurée, en JSONL par défaut ou sous forme d'enregistrements de journal OpenTelemetry (voir [Format de journal](#log-format)).  L'audit est **activé par défaut**.  L'audit répond à la question "qu'a fait l'agent", pas à "qu'a vu l'agent" - les lignes de résultat des instructions `SELECT` ne sont volontairement pas enregistrées, quel que soit le format.

#### Ce qui est enregistré {#what-gets-recorded}

| Champ | Description |
|--|--|
|`timestamp`|Heure réelle au début de l'appel (RFC3339).|
|`tool`|Nom de l'outil (par exemple `run_select_query`).|
|`mode`|Mode du serveur en vigueur au moment de l'appel.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`.  Pour une charge utile contenant plusieurs instructions, la classe de la plus privilégiée.|
|`sql`|Chaîne SQL pour les outils de requête (`run_select_query`, `run_mutation_query`, `run_lifecycle_operation`, `validate_select_query`).|
|`args`|Champs de hiérarchie pour les outils de métadonnées (`list_*`, `describe_method`) ; SQL + `row_limit` pour les outils de requête.|
|`duration_ms`|Durée réelle du contrôle + du gestionnaire.|
|`error`|Message d'erreur si l'outil a échoué ou a été refusé.|

<br />

Les lignes de résultat des instructions `SELECT` sont délibérément exclues - elles peuvent être très volumineuses et contenir des données sensibles.

#### Destination fichier {#file-sink}

La seule destination fournie aujourd'hui est `file`.  Un objet JSON par ligne, fsync après chaque enregistrement, rotation de type lumberjack par taille / âge / nombre de sauvegardes.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

Deux façons d'indiquer l'emplacement :

- `audit.file.path` - un chemin de fichier complet (absolu, ou relatif au répertoire de travail courant, cwd).
- `audit.file.dir` - un répertoire ; la destination y choisit le nom de base `stackql_mcp_server_<RFC3339-utc-second>.log`.

Lorsqu'aucun des deux n'est défini dans `mcp.config`, le serveur MCP donne à `dir` le répertoire de travail courant comme valeur par défaut, afin que les exploitants existants observent le même comportement qu'auparavant.  Le paquet générique de destination sous-jacent refuse lui-même de choisir silencieusement un répertoire - la décision "où atterrissent les journaux" est toujours prise par l'appelant.

Le chemin absolu résolu est écrit sur stderr au démarrage sous la forme `sink file: /path/to/file.log`.

#### Format de journal {#log-format}

Le flux d'audit a deux encodages, écrits dans la même destination fichier :

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

L'option prévaut sur `server.audit.format` dans `mcp.config` (`"audit": {"format": "otel"}`).  `jsonl` est compatible à l'octet près avec les enregistrements décrits ci-dessus.  `otel` écrit un objet OTLP/JSON `LogsData` par ligne, la forme que le récepteur `otlp_json_file` de l'OpenTelemetry Collector ingère telle quelle (vérifié avec otelcol-contrib 0.160.0) ; le flux atteint donc n'importe quel pipeline OTLP sans processeur de transformation :

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

Chaque appel d'outil produit un enregistrement de journal ; un appel passé par le contrôle d'approbation produit un second enregistrement pour la décision d'élicitation.  Les enregistrements d'un même appel partagent `gen_ai.tool.call.id` et un identifiant de trace.  Un `traceparent` W3C fourni par l'appelant dans `params._meta` est respecté ; sinon, un identifiant de trace est généré par session MCP afin que les enregistrements d'une session d'agent puissent être corrélés.

L'ensemble d'attributs émis est une interface versionnée : le scope d'instrumentation est `github.com/stackql/stackql/pkg/mcp_server/audit` en version `1.0.0`, et `schemaUrl` fixe les conventions stables à `https://opentelemetry.io/schemas/1.44.0`.  Les attributs `gen_ai.*` et `mcp.*` suivent les conventions sémantiques [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) et [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md), qui sont au statut Development ; la version du scope est incrémentée lorsque l'ensemble change.

| Attribut | Enregistrement | Valeur |
|--|--|--|
|`service.name`, `service.version`|ressource|`stackql`, la version de StackQL|
|`gen_ai.operation.name`|appel d'outil|`execute_tool`|
|`mcp.method.name`|les deux|`tools/call` sur l'enregistrement d'appel d'outil, `elicitation/create` sur l'enregistrement de décision|
|`gen_ai.tool.name`|les deux|Nom de l'outil|
|`gen_ai.tool.call.id`|les deux|Généré par appel, partagé par les deux enregistrements|
|`mcp.protocol.version`|les deux|Révision négociée|
|`mcp.session.id`|les deux|Identifiant de session (absent pour les requêtes HTTP sans session)|
|`stackql.mode`|les deux|Mode du serveur|
|`stackql.decision`|les deux|La valeur `decision` du tableau ci-dessus|
|`stackql.query`|appel d'outil|SQL tel quel, pour les outils de requête|
|`stackql.query_class`|appel d'outil|La valeur `query_class`|
|`stackql.provider`|appel d'outil|Provider, pour les outils de métadonnées|
|`stackql.query.source`|appel d'outil|Identifiant d'entrée de la Query Library, lorsqu'il est fourni|
|`stackql.duration_ms`|appel d'outil|Durée réelle|
|`stackql.rows_returned`|appel d'outil|Nombre de lignes, pour les outils qui renvoient des lignes|
|`error.type`, `error.message`|appel d'outil|En cas d'échec : la décision de refus, ou `tool_error` après le contrôle|

<br />

Le corps est `execute_tool <tool>` / `elicitation <tool>` ; la gravité est `INFO`, ou `ERROR` lorsque l'appel a échoué ou a été refusé.  Le contrat de masquage est identique dans les deux formats : les valeurs de résultat ne sont jamais sérialisées, seule l'instruction qui les a produites l'est.

#### Modes de défaillance {#failure-modes}

Lorsque la destination renvoie une erreur, le comportement de la réponse dépend de `audit.failure_mode`.

| failure_mode | Effet |
|--|--|
|`strict` (valeur par défaut)|L'appel d'outil renvoie l'erreur d'audit au client même si l'outil sous-jacent a réussi.  C'est voulu : mieux vaut une réponse ambiguë pour le client qu'un `DELETE` non détecté.|
|`strict_mutations`|Les `SELECT` / lectures de métadonnées se poursuivent avec une note sur stderr ; les mutations et les opérations de cycle de vie font remonter l'erreur d'audit.|
|`best_effort`|Toujours journaliser sur stderr et poursuivre.|

#### Séquencement {#sequencing}

L'écriture d'audit a lieu **après** l'exécution de l'outil (ou son blocage par le contrôle) mais **avant** le retour de la réponse au client.  En mode strict, un échec d'écriture d'audit sur un DELETE réussi signifie que la ligne a disparu mais que le client reçoit une erreur - c'est voulu, afin qu'aucune mutation ne passe sans être auditée.

#### Désactiver l'audit {#disabling-audit}

Pour désactiver entièrement l'audit (le comportement antérieur à PR2) :

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### Outils MCP disponibles {#available-mcp-tools}

Lorsqu'il s'exécute en tant que serveur MCP, StackQL expose les outils suivants, sous réserve de la liste d'autorisation `enabled_tools` configurée. Chacun renvoie à la fois une vue texte rendue (pour le LLM) et une charge utile structurée et typée (pour les clients programmatiques). Le rendu texte par défaut est un tableau markdown pour les résultats uniformes de plusieurs lignes, ou un bloc KV markdown pour les résultats clairsemés / à un seul enregistrement. Les outils qui ont des arguments acceptent un argument facultatif `format` (`markdown` ou `json`) qui remplace ce rendu ; sinon, le paramètre de rendu du serveur est utilisé. Les entrées ci-dessous listent les arguments propres à chaque tâche ; `?` marque les entrées facultatives.

Cliquez sur le nom d'un outil pour accéder à sa page de référence complète, avec les entrées, le comportement de contrôle et un exemple de prompt.

| Outil | Rendu | Description | Entrées |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|Identité et runtime du serveur : version de stackql, moteur SQL sous-jacent, emplacement du registre de providers, mode, option de lecture seule.  Appelez-le une fois en début de session.|aucune|
|[`list_providers`](/mcp/list_providers)|Tableau|Providers déjà téléchargés dans le cache local - sommet de la hiérarchie.|aucune|
|[`list_services`](/mcp/list_services)|Tableau|Services d'un provider.|`provider`|
|[`list_resources`](/mcp/list_resources)|Tableau|Ressources d'un `provider`.`service`.|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|Tableau|Méthodes d'accès (opérations HTTP) d'une ressource, avec le verbe SQL auquel chacune correspond et ses paramètres obligatoires.  Appelez-le avant d'écrire toute requête - c'est ici que les paramètres `WHERE` obligatoires sont déduits.|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|Contrat d'E/S complet d'une méthode (toujours EXTENDED) : entrées obligatoires et facultatives, et champs de sortie qu'un `SELECT` peut référencer.|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|Tableau|Trouve des modèles de requête sélectionnés à partir d'une intention en langage naturel. En lecture seule ; les entrées de mutation nécessitent une activation explicite et sont exclues en mode `read_only`.|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|Récupère un modèle et ses déclarations de paramètres. Avec un `params` non vide, valide les valeurs et renvoie le SQL rendu ainsi que `next_tool`, sans l'exécuter.|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|Analyse et planifie un seul `SELECT` sans l'exécuter.  Renvoie `{valid, errors}`.  `SELECT` uniquement ; exactement une instruction.|`sql`|
|[`run_select_query`](/mcp/run_select_query)|Tableau|Exécute un `SELECT`.  Renvoie `{rows}`.  Lectures uniquement.|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|Exécute `INSERT`/`UPDATE`/`REPLACE`/`DELETE` sur le provider.  **Effets de bord réels.** Renvoie `{messages, timestamp}`.  Contrôlé par le [mode](#server-modes) du serveur.|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|Exécute une opération de cycle de vie `EXEC` de stackql.  Renvoie `{messages, timestamp}`.  Contrôlé par le [mode](#server-modes) du serveur.|`sql`|
|[`list_registry`](/mcp/list_registry)|Tableau|Providers (et leurs versions) disponibles dans le registre configuré.  À distinguer de `list_providers`, qui ne liste que les providers déjà téléchargés.|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|Installe un seul provider depuis le registre dans le cache local.  État du cache local uniquement - aucun effet sur le plan de contrôle ou le plan de données du cloud.|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|Tableau|Recharge les identifiants depuis le fichier dotenv `--env.file` dans l'environnement du processus et indique l'état de résolution par provider (`ok`, `unresolved`, `not_checked`).  Ne renvoie jamais les valeurs des secrets.  Autorisé dans tous les modes.|`provider?`|

Consultez `query_library_search` avant d'écrire du SQL à partir de zéro, puis utilisez `query_library_get` pour inspecter et rendre un modèle correspondant. Les outils de requête acceptent aussi un argument facultatif `source` contenant l'identifiant de l'entrée de la Query Library, à des fins d'attribution uniquement ; l'exécution reste soumise au mode du serveur et à la politique d'approbation habituels.

:::note

`describe_resource` a été retiré dans StackQL `v0.12.732` ; les clients MCP, les prompts ou les instructions d'agent qui l'appelaient doivent appeler à la place [`list_methods`](/mcp/list_methods) puis `describe_method`.  Une ressource n'a pas de liste de champs unique, car chaque méthode d'accès renvoie sa propre forme ; les noms de colonnes proviennent donc de [`describe_method`](/mcp/describe_method) pour la méthode vers laquelle une requête est routée.  L'instruction SQL [`DESCRIBE`](/language-spec/describe) est inchangée.

:::

### Prompts MCP disponibles {#available-mcp-prompts}

Un seul prompt statique est publié.

| Prompt | Description |
|--|--|
|`write_safe_select`|Conseils pour écrire des requêtes `SELECT` sûres sur les ressources stackql.  Explique comment utiliser `SHOW METHODS IN <provider>.<service>.<resource>` pour découvrir la bonne méthode de lecture et les paramètres `WHERE` obligatoires.|

* * *

### Options {#flags}

| Option | Description |
|--|--|
|`--mcp.server.type`|Type de serveur MCP : `http`, `stdio` ou `reverse_proxy` (ce dernier s'utilise avec `stackql srv`).|
|`--mcp.config`|Objet de configuration JSON du serveur MCP.  YAML est également accepté.|
|`--mcp.protocol.version`|Révision du protocole la plus récente annoncée : `auto` (valeur par défaut), `2026-07-28` ou une révision plus ancienne telle que `2025-11-25`.  Consultez [Fixer la révision](#pinning-the-revision).|
|`--env.file`|Fichier d'identifiants de type dotenv chargé au démarrage et rechargé à la demande par l'outil `reload_credentials`.  Consultez [(Re)chargement des identifiants](#credential-resourcing---envfile--reload_credentials).|
|`--pgsrv.port`|Port TCP du serveur de protocole filaire PostgreSQL (utilisé avec `stackql srv`).|
|`-H`, `--help`|Affiche les informations d'aide.|
|`-v`, `--verbose`|Exécute en mode verbeux avec une sortie supplémentaire.|

&nbsp;
&nbsp;
> consultez [Options globales](/command-line-usage/global-flags) pour des options supplémentaires

:::info

Vous devez définir les variables d'environnement requises pour l'authentification des providers avant de démarrer le serveur MCP, ou désigner un fichier d'identifiants de type dotenv avec [`--env.file`](#credential-resourcing---envfile--reload_credentials).  Pour plus d'informations, consultez [Utiliser un provider](/getting-started/using-a-provider).

:::

* * *

### Exemples {#examples-1}

#### Serveur MCP autonome de base {#basic-standalone-mcp-server}

Lancez un serveur MCP autonome avec l'authentification du provider et un jeton bearer pour ses clients :

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Serveur MCP avec serveur PostgreSQL (en mémoire) {#mcp-server-with-postgresql-server-in-memory}

Exécutez les serveurs MCP et PostgreSQL pour un accès par les deux protocoles :

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Serveur d'inventaire en lecture seule avec audit vers un chemin connu {#read-only-inventory-server-with-audit-to-a-known-path}

Utile pour un agent d'analyse d'inventaire qui ne doit jamais effectuer de mutation :

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Serveur d'automatisation en accès complet {#full-access-automation-server}

Pour les pipelines d'automatisation de confiance qui ont besoin de mutations et ne peuvent pas répondre aux demandes d'élicitation.  Le pipeline présente le jeton bearer contenu dans `STACKQL_MCP_TOKEN` :

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Serveur MCP sécurisé avec TLS {#secure-mcp-server-with-tls}

Lancez un serveur MCP avec chiffrement TLS en mode proxy inverse :

Générez d'abord les certificats TLS (auto-signés pour le développement) :

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

Démarrez ensuite le serveur :

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### Tester votre serveur MCP {#testing-your-mcp-server}

Le dépôt fournit un client MCP de développement (`stackql_mcp_client`) pour les scripts et les tests de régression.  Compilez-le avec :

```bash
python cicd/python/build.py --build-mcp-client
```

Cela produit `./build/stackql_mcp_client`.

Notez que `stackql_mcp_client` n'envoie aucun en-tête `Authorization`, de sorte que le serveur qu'il cible doit définir `"allow_unauthenticated": true` (comme le font les exemples ci-dessus) ; un serveur protégé par jeton lui répond par `401`.  Il n'annonce **pas** non plus la capacité d'élicitation MCP - il est destiné aux scripts non interactifs.  Face à un serveur `safe` ou `delete_safe`, il reçoit le refus "client does not support elicitation" sur les appels de mutation et de cycle de vie.  Pour des tests interactifs avec des demandes d'élicitation, utilisez un client MCP embarqué dans un éditeur (Claude Desktop, Cursor, Continue).

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation - succeeds only against a full_access server, or against
# delete_safe (for INSERT/UPDATE) or safe with an elicitation-capable client.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### Exemples de réponses {#example-responses}

`server_info` (sur un serveur en mode par défaut) :

```json
{
  "version": "0.10.444",
  "commit": "abc1234",
  "build_date": "2026-05-16T10:47:33Z",
  "platform": "linux/amd64",
  "transport": "http",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.io/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query` :

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query` (sur un serveur `full_access`) :

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

Exemple de ligne du journal d'audit pour la même mutation :

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### Intégration avec les assistants IA {#integration-with-ai-assistants}

Pour intégrer le serveur MCP de StackQL à un assistant IA, enregistrez `stackql` comme serveur MCP dans la configuration de l'assistant.  La plupart des clients MCP embarqués dans un éditeur exécutent le serveur sur `stdio` ; pour ceux-ci, utilisez `--mcp.server.type=stdio` et l'assistant lance directement le processus.  Les agents autonomes qui parlent HTTP peuvent se connecter à un processus `stackql mcp --mcp.server.type=http` de longue durée.

Pour Claude Desktop - y compris la fiche de l'Anthropic Connector Directory (la méthode d'installation recommandée), le MCP Bundle téléchargeable (`.mcpb`) et la configuration manuelle - consultez [Utiliser StackQL avec Claude Desktop](/getting-started/claude-desktop).

:::note

L'emplacement des fichiers de configuration varie selon le système d'exploitation et l'assistant IA.  Consultez la documentation de votre assistant IA pour connaître le chemin et le format corrects.

:::

* * *

### Considérations d'architecture {#architecture-considerations}

Pour choisir un mode de déploiement, tenez compte des points suivants :

1. **Autonome (`stackql mcp`)** : configuration la plus simple, idéale pour le développement et pour les clients embarqués dans un éditeur sur `stdio`.
2. **Double serveur en mémoire (`stackql srv` avec `mcp.server.type=http`)** : un seul processus sert à la fois le protocole MCP et le protocole filaire PostgreSQL.  Meilleures performances, convient à la plupart des déploiements en production.
3. **Proxy inverse (`stackql srv` avec `mcp.server.type=reverse_proxy`)** :
   - permet de séparer les charges de travail entre processus ou hôtes
   - prend en charge le chiffrement TLS pour des points de terminaison MCP sécurisés
   - permet la mise à l'échelle indépendante des interfaces MCP et PostgreSQL
   - offre de la souplesse pour l'intégration de CA d'entreprise (remarque : la prise en charge des CA d'entreprise est expérimentale)

Et séparément, en matière de sécurité :

- Commencez avec la valeur par défaut `mode: safe`.  Les clients compatibles avec l'élicitation (Claude Desktop, Cursor, Continue) demandent confirmation à l'utilisateur avant toute mutation.
- Fixez `mode: read_only` pour les agents d'inventaire ou d'analyse qui ne doivent jamais écrire.
- Fixez `mode: delete_safe` lorsque vous voulez une liberté de création et de mise à jour, tout en gardant un humain dans la boucle pour les opérations destructrices.
- Réservez `mode: full_access` aux pipelines d'automatisation de confiance, et uniquement en association avec un journal d'audit que quelqu'un examine.
- Définissez `server.auth_token_env_var` sur tout écouteur HTTP que d'autres processus ou hôtes peuvent atteindre ; `allow_unauthenticated` est destiné au développement local.  TLS seul n'authentifie pas les clients.

:::caution

Lorsque vous utilisez TLS avec des autorités de certification (CA) d'entreprise, une configuration supplémentaire peut être nécessaire.  Cette fonctionnalité est expérimentale et peut nécessiter des ajustements selon votre implémentation de CA.

:::

* * *

### Pour aller plus loin {#further-reading}

- La référence pour les développeurs du paquet est le [README de `pkg/mcp_server`](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) dans le dépôt stackql.
- La documentation technique destinée aux contributeurs se trouve dans [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) dans le dépôt stackql et comprend des exemples de réponses détaillés pour chaque outil.
