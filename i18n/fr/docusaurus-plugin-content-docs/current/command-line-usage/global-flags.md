---
title: Options globales
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Options globales du binaire stackql, disponibles sur toutes les commandes : authentification, format de sortie, emplacement du registre, journalisation et backend SQL."
image: "/img/stackql-featured-image.png"
---

Les options globales sont acceptées par les commandes StackQL, mais certaines ne s'appliquent qu'à un mode particulier. Définissez-les sur la ligne de commande ou, lorsque c'est pris en charge, dans le fichier d'initialisation de StackQL ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)). Les options `--pgsrv.*` sont répertoriées dans les [options du serveur PostgreSQL](/command-line-usage/srv#server-options).

| Option | Type de données | Description | Valeur par défaut |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Configuration de l'accès des appelants, une enveloppe JSON `{"path": "/path/to/access.json"}` (un simple chemin de fichier est également accepté).  Active la [gestion des accès](/command-line-usage/access-management) : les appelants sont authentifiés par JWT, jeton bearer statique, TLS mutuel ou identité locale de l'OS, et les instructions sont autorisées au regard de règles de politique.  Les identifiants locaux de type jeton sont lus depuis `STACKQL_CALLER_TOKEN`.  Disponible à partir de StackQL `v1.0`. | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Autorise une session à sélectionner un magasin de secrets persistant du système d'exploitation (macOS Keychain, Linux Secret Service ou Windows Credential Manager) avec `SET "$.secret_store"`.  Les magasins sous forme de fichier plat sont refusés même avec cette option.  Consultez [Sessions de provider et secrets](/getting-started/provider-sessions#persistent-os-secret-stores).  Disponible à partir de StackQL `v1.0`. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Autorise le déréférencement des valeurs de secrets et d'identifiants dans les expressions SQL (`"$secrets.name"`, `"$auth.provider.field"`), avec pour conséquence qu'elles peuvent se retrouver dans les résultats, les journaux et les artefacts de requête persistés.  Inutile pour l'authentification normale des providers.  Consultez [Valeurs de secrets dans SQL](/getting-started/provider-sessions#secret-values-in-sql).  Disponible à partir de StackQL `v1.0`. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | Délai d'expiration des appels d'API en secondes, 0 pour aucun délai d'expiration | `45` |
| <span class="nowrap">`--approot`</span> | string | Chemin racine de la configuration et du cache de l'application | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Mode de fichier de la configuration et du cache de l'application | `493` |
| <span class="nowrap">`--auth`</span> | string | Paires clé-valeur du contexte `auth` au format json |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Nombre initial de clés du cache | `100` |
| <span class="nowrap">`--configfile`</span> | string | Chemin complet du fichier de configuration | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | Fichier `cpuprofile`, aucun si vide | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | Profondeur maximale des dépendances de flux de données pour une requête donnée | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | Nombre maximal de composantes faiblement connexes du flux de données pour une requête donnée | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | Chaîne JSON / YAML configurant le traitement des requêtes de maintenance du SGBD | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Délimiteur de la sortie csv. Un seul caractère.  Ignoré pour toute sortie autre que csv | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | Option `dryrun` ; exécute uniquement le préprocesseur, la sortie issue des modèles est renvoyée | `false` |
| <span class="nowrap">`--env.file`</span> | string | Fichier facultatif de type dotenv chargé dans l'environnement du processus au démarrage (toutes les commandes) ; l'outil MCP [`reload_credentials`](/mcp/reload_credentials) le recharge à la demande. Un fichier manquant est toléré ; les clés dont la valeur est vide sont ignorées ; les variables existantes sont écrasées. Cette option n'est pas lue depuis `.stackqlrc`. Disponible dans les versions de StackQL postérieures à `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | Limite de concurrence pour l'exécution des requêtes | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Préfixe d'alias d'export (espace de noms ou schéma) | |
| <span class="nowrap">`--gc`</span> | string | Chaîne JSON / YAML représentant la configuration du GC | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Désactive les en-têtes de colonnes, valide uniquement avec `--output csv` ou `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Affiche les informations des requêtes http dans le terminal | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Hôte du proxy (laisser vide pour ne pas utiliser de proxy) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Mot de passe du proxy | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Port du proxy, tout nombre inférieur ou égal à 0 donne le port par défaut du schéma concerné (par exemple `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Schéma du proxy, `http` ou `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Utilisateur du proxy | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Nombre maximal de résultats par requête http, tout nombre inférieur ou égal à 0 supprime la limite | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Nombre maximal de pages de résultats renvoyées par ressource, tout nombre inférieur ou égal à 0 supprime la limite | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Profondeur maximale des requêtes indirectes : vues et sous-requêtes | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Fichier d'entrée (fichier IQL) à partir duquel les requêtes sont lues | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Fichier de contexte (données) pour le templating (fichier `json` ou `jsonnet`)| |
| <span class="nowrap">`--loglevel`</span> | string | Niveau de journalisation, doit être l'un de \{`panic`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`\} (`warning` est accepté comme alias de `warn`) | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | Objet de configuration JSON / YAML du [serveur MCP](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` et `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | Encodage du journal d'audit MCP, `jsonl` ou `otel` (enregistrements de journal OTLP/JSON) ; prévaut sur `server.audit.format` dans `--mcp.config`.  Consultez [Format de journal](/command-line-usage/mcp#log-format).  Disponible à partir de StackQL `v0.11.660`. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Révision du protocole MCP la plus récente annoncée, `auto`, `2026-07-28` (sans session uniquement) ou une révision plus ancienne telle que `2025-11-25` ; prévaut sur `server.protocol_version` dans `--mcp.config`.  Consultez [Fixer la révision](/command-line-usage/mcp#pinning-the-revision).  Disponible à partir de StackQL `v0.12.732`. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | Type de serveur MCP, l'un de \{`http`, `stdio`, `reverse_proxy`\}.  Consultez [Type de serveur MCP](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL des documents de métadonnées mis en cache, en secondes | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | Chaîne JSON / YAML représentant les espaces de noms pour la mise en cache, les vues, etc. | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Fonctionne hors ligne, avec les données en cache | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Fichier de sortie dans lequel les résultats sont écrits | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Format de sortie, doit être l'un de \{`json`, `jsonl`, `table`, `csv`, `text`, `otel`\} (`ndjson` est accepté comme alias de `jsonl`).  `otel` émet des enregistrements de journal OpenTelemetry, consultez [Modes de sortie](/getting-started/output-modes#otel-output) ; disponible dans les versions de StackQL à partir de `v0.11.660`.  Lorsqu'un exportateur est configuré dans `--otel.config`, les enregistrements `otel` sont également poussés vers ce point de terminaison | `table` |
| <span class="nowrap">`--otel.config`</span> | string | Chaîne JSON / YAML configurant le format de sortie `otel` ; `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms` et `exporter.batch_size` poussent les enregistrements vers un point de terminaison de journaux OTLP/HTTP, consultez [Pousser vers un point de terminaison OTLP](/getting-started/output-modes#pushing-to-an-otlp-endpoint).  Disponible à partir de StackQL `v0.12.732` | `{}` |
| <span class="nowrap">`--preview`</span> | string | Chaîne JSON activant les fonctionnalités en préversion.  `{"unstable": true}` expose les relations `stackql_unstable_<provider>.<service>.<resource>` pilotées par les documents, qui sont résolues et exécutées par `omnisdk` directement à partir des documents de provider, les lignes étant diffusées au fur et à mesure de leur production ; les autres clés (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) ajustent ce backend.  Les fonctionnalités en préversion ne sont pas stables : le comportement, la syntaxe et la sortie peuvent changer ou cesser de fonctionner d'une version à l'autre sans préavis, ne vous y fiez donc pas en production.  À partir de `v1.0`, la même activation explicite expose les relations `stackql_preview.iac.run` et `stackql_preview.iac.destroy`, qui appliquent et détruisent un ensemble de ressources déclaré par l'intermédiaire de `omnisdk`, l'état étant conservé sous la racine de l'application.  Consultez [Fonctionnalités en préversion](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--provider`</span> | string | Provider par défaut initial. Par exemple, avec `--provider github`, `repos.repos` est résolu en `github.repos.repos` sans instruction `USE` préalable. Ne s'authentifie pas auprès du provider. | aucun |
| <span class="nowrap">`--querycachesize`</span> | integer | Taille, en nombre d'entrées, du cache LRU des plans de requête | `10000` |
| <span class="nowrap">`--registry`</span> | string | Chaîne JSON / YAML configurant l'emplacement du registre de providers et la vérification de signature des documents de provider | consultez [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | Chaîne JSON / YAML représentant la configuration de session | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | Chaîne JSON / YAML représentant la configuration système du backend SQL | consultez [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | Chaîne JSON / YAML représentant la configuration du magasin Txn | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Chemin du bundle de CA ; s'il n'est pas spécifié, les valeurs par défaut du système sont utilisées |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Autorise la confiance envers des certificats non sécurisés (non recommandé) |  |
| <span class="nowrap">`--upstream.errors`</span> | string | Politique JSON / YAML permettant de tolérer explicitement des statuts de réponse HTTP lors des lectures de provider. Consultez [`--upstream.errors`](#--upstream-errors). | `{}` |
| <span class="nowrap">`--var`</span> | string | Variables externes fournies sous forme de paires clé/valeur pour être utilisées dans les requêtes StackQL par l'intermédiaire de fichiers de données `jsonnet` |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Sortie détaillée | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Aide contextuelle pour stackql | |
| <span class="nowrap">`--version`</span> | | Affiche la version du programme StackQL | |

L'option `--colorscheme` est obsolète et ne modifie plus les couleurs du shell. L'option `--usenonpreferredapis` est acceptée, mais ne modifie pas la sélection des API dans le moteur actuel.

## `--registry` {#--registry}

L'option `--registry` configure l'emplacement depuis lequel les documents de provider sont lus et la vérification éventuelle de leurs signatures.  La valeur est une chaîne JSON avec les clés suivantes :

| Clé | Description | Valeur par défaut |
| -- | -- | -- |
| `url` | URL du registre.  Le registre public de providers StackQL est utilisé si elle est omise. | registre public |
| `localDocRoot` | Répertoire local contenant des documents de provider, lus sur place au lieu d'être téléchargés depuis le registre.  Utilisé lors du [développement d'un provider](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | Préfixe de chemin, sous la racine du registre, où se trouvent les documents source des providers. | `src` |
| `verifyConfig.nopVerify` | `true` désactive la vérification de signature.  Définissez `false` pour exiger une signature ML-DSA-65 (FIPS 204) embarquée à chaque lecture d'un document de provider ou de service, y compris pour les relations en préversion adossées aux documents. | `true` |
| `verifyConfig.publicKeys` | Objet JSON associant des ID de clé à des clés publiques ML-DSA-65 encodées en hexadécimal, pour les registres privés.  Il remplace les clés de production embarquées ; un objet vide explicite est une erreur. | clés embarquées |

### Exemple : exiger des documents de provider signés {#example-require-signed-provider-documents}

Active la vérification pour chaque document de provider et de service lu depuis le registre public, au regard des clés de production embarquées :

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Exemple : lire des documents non signés depuis un répertoire local {#example-read-unsigned-documents-from-a-local-directory}

Fait pointer le registre vers un répertoire de documents de provider en cours de développement.  Ces documents ne sont pas signés, la vérification reste donc désactivée :

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

La vérification de signature est disponible dans les versions de StackQL à partir de `v1.0` et est désactivée par défaut.  Les signatures couvrent les octets YAML exacts d'un document : toute modification, y compris un changement de mise en forme ou de saut de ligne, les invalide ; signez à nouveau avec l'outil `pqsig sign-doc` du registre.  Les documents de développement locaux non signés doivent conserver `nopVerify: true`.  Les anciennes options de certificat et les fichiers sidecar `.sig` ne sont plus utilisés, et les archives publiées uniquement avec des sidecars doivent être republiées avant toute utilisation vérifiée.  La vérification authentifie les documents au moment de leur lecture ; le téléchargement et l'extraction des archives ne sont pas couverts.

:::

## `--sqlBackend` {#--sqlbackend}

L'option `--sqlBackend` configure un backend SQL externe pour StackQL, ce qui vous permet d'utiliser un moteur de base de données externe (tel que PostgreSQL) à la place du moteur SQL embarqué par défaut. Cela permet de s'appuyer sur une base de données externe pour le traitement des requêtes, le stockage persistant ou l'intégration à une infrastructure de bases de données existante.

La valeur est une chaîne JSON avec les clés suivantes :

| Clé | Description | Exemples de valeurs |
| -- | -- | -- |
| `dbEngine` | Le type de moteur de base de données | `postgres_tcp` |
| `sqlDialect` | Le dialecte SQL à utiliser | `postgres` |
| `dsn` | Le nom de la source de données (chaîne de connexion) de la base de données | Consultez les exemples ci-dessous |

### Exemple : utiliser un backend PostgreSQL {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Exemple : utiliser l'interface PostgreSQL de Databricks {#example-using-databricks-postgresql-interface}

Databricks expose une interface compatible PostgreSQL qui peut servir de backend SQL à StackQL. Le DSN utilise le format standard des chaînes de connexion PostgreSQL avec vos identifiants Databricks :

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Les caractères spéciaux du nom d'utilisateur ou du mot de passe (tels que `@`) doivent être encodés pour URL dans le DSN (par exemple, `@` devient `%40`).
:::

## `--upstream.errors` {#--upstream-errors}

Utilisez `http.ignoreStatusCodes` pour lister les statuts de réponse HTTP qu'une lecture de provider doit tolérer explicitement. Les entrées sont des chaînes : utilisez un code exact tel que `"503"` ou une classe telle que `"5xx"`. La valeur par défaut `{}` n'ajoute aucune exception au traitement normal des réponses en amont par le moteur. Ce paramètre ne relance pas les requêtes HTTP ayant échoué et ne s'applique ni aux mutations ni aux échanges d'authentification.

Une réponse ignorée ne fournit aucune ligne. Les autres lectures de la requête peuvent tout de même renvoyer des lignes, si bien qu'une requête réussie peut renvoyer des résultats incomplets. StackQL signale le statut HTTP ignoré sous forme de diagnostic.

Par exemple, le [test de jointure GitHub simulé](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104) utilise cette option :

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

Dans ce test, l'appel d'API qui lit les collaborateurs d'un dépôt renvoie HTTP `500`. La requête renvoie tout de même les lignes de collaborateurs des autres dépôts, et stderr contient `ignoring upstream response: 500 INTERNAL SERVER ERROR`. Privilégiez un code de statut exact lorsqu'un seul échec est attendu.
