---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "La commande stackql srv exécute StackQL en serveur du protocole filaire PostgreSQL : psql, les outils de BI et les pilotes de langage interrogent les API cloud et SaaS en SQL."
image: "/img/stackql-featured-image.png"
---

Commande servant à lancer StackQL en tant que service, auquel des clients du [protocole filaire Postgres](https://www.postgresql.org/docs/current/protocol.html) authentifiés par mTLS peuvent ensuite accéder. La commande `srv` peut aussi exécuter un serveur [Model Context Protocol (MCP)](/command-line-usage/mcp) aux côtés du serveur du protocole filaire PostgreSQL, ce qui offre un accès à deux protocoles, à la fois aux clients de base de données traditionnels et aux agents IA.

* * * 

### Syntaxe {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

Bien que le serveur StackQL utilise le protocole filaire Postgres, ce n'est pas un serveur Postgres et il n'en nécessite pas.  Le client utilise le protocole filaire pour communiquer avec le serveur, qui utilise StackQL pour exécuter des requêtes ou des opérations DML sur des fournisseurs cloud et SaaS distants et renvoyer les résultats au client.

:::

Les paramètres envoyés par le protocole de requête étendu (`Parse` / `Bind` / `Execute`, tel qu'utilisé par `psycopg` 3, `pgx` et JDBC) sont liés côté serveur sous forme de littéraux de chaîne de caractères.  À partir de `v0.12.742`, un marqueur `$n` situé dans un littéral de chaîne, un identifiant entre guillemets ou un commentaire est laissé tel quel, les barres obliques inverses dans les valeurs de paramètre sont échappées, et une valeur texte `NULL` reste la chaîne `NULL` (le `NULL` SQL est transmis séparément).

### Options du serveur {#server-options}

| Option | Description |
|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|Adresse sur laquelle le serveur écoute (généralement `0.0.0.0`)|
|<span class="nowrap">`--pgsrv.port`</span>|Port sur lequel le serveur écoute (par exemple `5444`)|
|<span class="nowrap">`--pgsrv.tls`</span>|Objet de configuration mTLS, consultez l'[exemple](#examples) ci-dessous|
|<span class="nowrap">`--pgsrv.loglevel`</span>|Niveau de journalisation (valeur par défaut `WARN`)|

<br/>

> consultez [Options globales](/command-line-usage/global-flags) pour des options supplémentaires

### Options du serveur MCP {#mcp-server-options}

Lorsque la commande `srv` est exécutée avec la prise en charge de MCP, les options supplémentaires suivantes sont disponibles :

| Option | Description |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|Type de serveur MCP : `http` (en mémoire) ou `reverse_proxy` (basé sur TCP)|
|<span class="nowrap">`--mcp.config`</span>|Objet de configuration JSON du serveur MCP (consultez la [documentation MCP](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|Encodage du journal d'audit MCP : `jsonl` (valeur par défaut) ou `otel` (enregistrements de journal OTLP/JSON) ; prévaut sur `server.audit.format` dans l'objet de configuration|
|<span class="nowrap">`--mcp.protocol.version`</span>|Révision du protocole MCP la plus récente annoncée : `auto` (valeur par défaut), `2026-07-28` (sans session uniquement) ou une révision plus ancienne telle que `2025-11-25` ; prévaut sur `server.protocol_version` dans l'objet de configuration|

<br/>

:::info[Rupture de compatibilité dans la v0.12.742]

À partir de `v0.12.742`, le transport HTTP MCP (`--mcp.server.type=http` ou `reverse_proxy`) refuse de démarrer, sauf si l'objet de configuration désigne un jeton bearer avec `server.auth_token_env_var` ou s'en dispense explicitement avec `"allow_unauthenticated": true`.  Consultez [Authentification des clients HTTP](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

Consultez la [documentation de la commande MCP](/command-line-usage/mcp) pour des informations détaillées sur la configuration et l'utilisation du serveur MCP, notamment les modes de déploiement, les options de configuration et l'intégration avec les assistants IA.

:::

:::info

Vous devez définir les variables d'environnement requises pour l'authentification des providers avant de démarrer le serveur, consultez [Utiliser un provider](/getting-started/using-a-provider) pour plus d'informations.

:::

Pour les serveurs de longue durée, l'option [`--env.file`](/command-line-usage/global-flags) vous permet de déposer des identifiants, ou d'en effectuer la rotation, dans un fichier de type dotenv sur disque avant le démarrage, plutôt que d'exporter des variables dans le shell :

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

Pour `stackql srv`, le fichier est lu une seule fois au démarrage ; le rechargement en cours de session passe par l'outil MCP [`reload_credentials`](/mcp/reload_credentials).

### Options {#flags}

| Option | Description |
|--|--|
|`-H,--help`|Affiche les informations d'aide|
|`-v,--verbose`|Exécute les requêtes en mode détaillé, avec une sortie supplémentaire envoyée vers stdout ; si l'option `-f` est sélectionnée,<br/>ces informations de journalisation supplémentaires sont écrites dans le fichier de sortie avec les résultats de la requête|

<br/>

> consultez [Options globales](/command-line-usage/global-flags) pour des options supplémentaires

* * *

### Exemples {#examples}

Lancer un processus serveur StackQL et s'y connecter avec un client en ligne de commande `psql`.  

Téléchargez d'abord le fichier de configuration `openssl.cnf` depuis [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).  

Générez ensuite les fichiers de certificat et de clé qu'utiliseront le serveur et le client pour l'authentification TLS mutuelle (mTLS).  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Définissez maintenant les variables d'environnement ; si le client et le serveur sont sur le même hôte, elles peuvent être partagées.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Configurez un objet d'authentification pour chacun des providers auxquels le serveur devra accéder, par exemple pour GitHub.  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

Démarrez maintenant le serveur dans un terminal.  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

Dans un autre terminal, connectez-vous au serveur avec le client en ligne de commande `psql`.  

```bash
psql 127.0.0.1
```

Exécutez maintenant une requête StackQL sur un provider authentifié.  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

127.0.0.1=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

127.0.0.1=>
```
