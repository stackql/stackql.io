---
title: Modes de sortie
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Modes de sortie de StackQL : table, json, jsonl, csv et otel, comment les sélectionner et ce que chacun émet pour une utilisation en shell, exec et serveur."
image: "/img/stackql-featured-image.png"
---
Voir aussi :  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

Les résultats renvoyés par les requêtes StackQL peuvent être présentés au format tabulaire (table), ainsi qu'aux formats json, jsonl (JSON délimité par des sauts de ligne), csv, text ou otel (enregistrements de journal OpenTelemetry). Les formats JSON, JSONL et CSV permettent d'échanger des données avec d'autres programmes ; le format `otel` achemine un jeu de résultats vers un backend OpenTelemetry sous la forme d'un instantané horodaté. Le format de sortie souhaité se configure avec l'option `--output` de StackQL.

### Format de sortie `table` {#table-output-format}
Les résultats peuvent être présentés sous forme de tableau en donnant la valeur `table` au paramètre `--output`, comme indiqué ci-dessous (c'est la valeur par défaut du shell interactif StackQL) :

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output table

|---------------------|-----------------|
|         ID          |      NAME       |
|---------------------|-----------------|
| 1257085253691867467 | demo-instance-1 |
|---------------------|-----------------|
| 2586731219281477960 | demo-instance-2 |
|---------------------|-----------------|
| 5584456226809282885 | demo-instance-3 |
|---------------------|-----------------|
```

### Sortie `json` {#json-output}
Les résultats peuvent être renvoyés au format JSON en donnant la valeur `json` au paramètre `--output`, comme indiqué ci-dessous ; ce format de sortie est utile lorsque les résultats d'une requête doivent être transmis à un processus ou à un script externe :

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Sortie `jsonl` {#jsonl-output}
Les résultats peuvent être renvoyés au format JSON délimité par des sauts de ligne (JSON Lines) en donnant la valeur `jsonl` au paramètre `--output`, comme indiqué ci-dessous (`ndjson` est accepté comme alias). Chaque ligne du jeu de résultats est écrite sous la forme d'un seul objet JSON sur sa propre ligne de texte, et les lignes sont vidées du tampon au fur et à mesure de leur écriture, ce qui rend ce format de sortie utile pour diffuser les résultats en continu vers des outils orientés ligne ou pour transmettre de grands jeux de résultats à d'autres programmes par un pipe :

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### Sortie `csv` {#csv-output}
Les résultats peuvent être renvoyés au format CSV en donnant la valeur `csv` au paramètre `--output`, comme indiqué ci-dessous ; ce format de sortie est utile pour analyser les résultats dans Excel ou pour fournir une interface de données à un autre système :

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output csv

id,name
1257085253691867467,demo-instance-1
2586731219281477960,demo-instance-2
5584456226809282885,demo-instance-3
```

#### Exemple de sortie CSV (avec un autre délimiteur) {#csv-output-example-with-alternative-delimiter}
Un autre délimiteur peut être spécifié pour le format de sortie `csv` avec l'option `-d`, comme indiqué ci-dessous :

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output csv -d "|"

id|name
1257085253691867467|demo-instance-1
2586731219281477960|demo-instance-2
5584456226809282885|demo-instance-3
```

### Sortie `otel` {#otel-output}
Les résultats peuvent être émis sous forme d'enregistrements de journal OpenTelemetry en donnant la valeur `otel` au paramètre `--output`. Chaque ligne du jeu de résultats est écrite sous la forme d'un objet OTLP/JSON `LogsData` sur sa propre ligne de texte, vidée du tampon au fur et à mesure de son écriture comme pour `jsonl`, et l'ensemble est suivi d'un enregistrement de fin pour l'instruction. Ce format de sortie transforme une requête en instantané de configuration cloud sous forme de télémétrie : exécutez une requête d'inventaire de façon planifiée, acheminez les enregistrements vers un backend compatible OTLP (ClickHouse, Datadog ou un OpenTelemetry Collector placé devant tout autre système), et répondez-y aux questions "à quoi ressemble le parc actuellement", "qu'est-ce qui a changé depuis la dernière exécution" et "qui a détenu quel droit d'accès, et depuis quand". Disponible à partir de StackQL `v0.11.660`.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

Un enregistrement de ligne, mis en forme pour la lecture (le fichier contient chaque enregistrement sur une seule ligne) :

```json
{"resourceLogs":[{"resource":{"attributes":[
  {"key":"service.name","value":{"stringValue":"stackql"}},
  {"key":"service.version","value":{"stringValue":"0.11.660"}},
  {"key":"cloud.provider","value":{"stringValue":"gcp"}},
  {"key":"cloud.account.id","value":{"stringValue":"stackql-demo"}},
  {"key":"cloud.availability_zone","value":{"stringValue":"australia-southeast1-a"}}
]},"scopeLogs":[{"scope":{"name":"github.com/stackql/stackql/internal/stackql/output","version":"1.0.0"},
"logRecords":[{
  "timeUnixNano":"1788739200000000000","observedTimeUnixNano":"1788739203412000000",
  "severityNumber":9,"severityText":"INFO",
  "body":{"stringValue":"{\"id\":\"1257085253691867467\",\"name\":\"demo-instance-1\"}"},
  "attributes":[
    {"key":"id","value":{"stringValue":"1257085253691867467"}},
    {"key":"name","value":{"stringValue":"demo-instance-1"}},
    {"key":"stackql.snapshot.id","value":{"stringValue":"1e0d4a6c-4b0f-4b4e-9f7d-2a6c9a1f3b21"}},
    {"key":"stackql.query","value":{"stringValue":"select id, name from google.compute.instances where project = 'stackql-demo' and zone = 'australia-southeast1-a'"}},
    {"key":"stackql.query.hash","value":{"stringValue":"9c1d2e..."}},
    {"key":"stackql.provider","value":{"stringValue":"google"}},
    {"key":"stackql.service","value":{"stringValue":"compute"}},
    {"key":"stackql.resource","value":{"stringValue":"instances"}},
    {"key":"stackql.row.index","value":{"intValue":"0"}},
    {"key":"stackql.row.fingerprint","value":{"stringValue":"sha256:7f3a..."}}
  ],
  "traceId":"4bf92f3577b34da6a3ce929d0e0e4736","spanId":"a1b2c3d4e5f60718"
}],"schemaUrl":"https://opentelemetry.io/schemas/1.44.0"}],"schemaUrl":"https://opentelemetry.io/schemas/1.44.0"}]}
```

#### Instantanés {#snapshots}

Une instruction correspond à un instantané. Chaque enregistrement de l'instruction porte le même `timeUnixNano` (l'heure de début de l'instruction), le même `stackql.snapshot.id` et le même `spanId`, de sorte que les requêtes à une date donnée, les comparaisons et les requêtes sur le dernier état fonctionnent même si les lignes arrivent page par page sur plusieurs secondes ; `observedTimeUnixNano` indique le moment où chaque enregistrement a réellement été écrit. Toutes les instructions d'une même invocation partagent un `traceId`, repris d'une variable d'environnement `TRACEPARENT` lorsqu'elle est définie (la convention pour les pipelines CI) et généré sinon.

Le dernier enregistrement d'une instruction est l'enregistrement de fin : corps `snapshot complete <provider.service.resource>`, avec `stackql.snapshot.complete=true`, `stackql.rows_returned` et `stackql.duration_ms`. Il indique au backend que l'instantané est complet, et une instruction qui ne renvoie aucune ligne l'émet tout de même, de sorte qu'une ressource disparue apparaît comme une dérive plutôt que comme un silence. Une instruction en échec produit un enregistrement `ERROR` portant `error.type` et `error.message` lorsque la présentation des erreurs est `record` ; par défaut, les erreurs sont envoyées sur stderr comme pour tous les autres formats de sortie.

#### Forme des enregistrements {#record-shape}

Le corps est la ligne complète en JSON compact, objets imbriqués intacts, et constitue la source de vérité pour le document complet. Les attributs portent chaque colonne sous son propre nom, typée lorsque la valeur est un scalaire (`stringValue`, `intValue`, `doubleValue`, `boolValue`) ; les colonnes imbriquées sont portées sous forme de chaînes de caractères JSON, ce qui est portable entre les backends dont le schéma d'attributs par défaut est à valeurs de type chaîne, et les colonnes nulles sont omises. Les attributs `stackql.*` réservés sont :

| Attribut | Enregistrement | Valeur |
|--|--|--|
|`stackql.snapshot.id`|tous|UUID généré à chaque exécution d'instruction|
|`stackql.query`|tous|Le texte de la requête soumise, tel quel|
|`stackql.query.hash`|tous|SHA-256 du texte de la requête ; une clé de série moins coûteuse que le SQL pour une tâche récurrente|
|`stackql.provider`, `stackql.service`, `stackql.resource`|tous|La table principale de l'instruction ; omis pour les jointures et les vues qui couvrent plusieurs tables|
|`stackql.row.index`|ligne|Position dans le jeu de résultats, à partir de zéro|
|`stackql.row.fingerprint`|ligne|`sha256:` du JSON canonique de la ligne, de sorte que la dérive se détecte par une comparaison par identité au lieu d'une comparaison sur chaque attribut|
|`stackql.snapshot.complete`|fin|`true`|
|`stackql.rows_returned`|fin|Nombre de lignes de l'instruction|
|`stackql.duration_ms`|fin|Durée réelle écoulée depuis le début de l'instruction|

<br />

Le jeu d'attributs émis est une interface versionnée : le scope d'instrumentation est `github.com/stackql/stackql/internal/stackql/output` en version `1.0.0`, et `schemaUrl` fixe les conventions stables à `https://opentelemetry.io/schemas/1.44.0`.

Aucune identité de ressource n'est déduite : l'empreinte est la valeur et le backend choisit la clé (`id`, `arn`, `selfLink` ou un tuple de colonnes). Pour suivre les droits d'accès comme des entités, éclatez les liaisons en SQL à raison d'une ligne par principal et par rôle ; la télémétrie suit le grain du résultat.

#### Attributs de ressource {#resource-attributes}

La ressource porte `service.name` (`stackql`, ou la valeur de `OTEL_SERVICE_NAME`) et `service.version`, les conventions cloud d'OpenTelemetry lorsqu'elles peuvent être déduites d'un `SELECT` sur une seule table (`cloud.provider` à partir du provider ; `cloud.account.id` à partir d'une égalité sur `project` ou `subscriptionId` dans la clause `WHERE` ; `cloud.region` à partir de `region` ; `cloud.availability_zone` à partir de `zone`), ainsi que chaque paire de la variable d'environnement standard `OTEL_RESOURCE_ATTRIBUTES`, de sorte qu'une étape de pipeline peut apposer `deployment.environment`, un nom de tâche ou des étiquettes de compte sans option supplémentaire :

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

#### Acheminer les enregistrements {#shipping-the-records}

Le fichier contient un `LogsData` par ligne, la forme que le récepteur `otlp_json_file` de l'OpenTelemetry Collector ingère sans processeur de transformation :

```yaml
receivers:
  otlp_json_file:
    include: [/var/lib/stackql/inventory/*.otlp.jsonl]
exporters:
  otlphttp:
    endpoint: https://otlp.example.com
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [otlphttp]
```

Chaque ligne est aussi, octet pour octet, le corps d'une requête OTLP/HTTP `POST /v1/logs`, c'est-à-dire ce qu'envoie l'exportateur push intégré.

#### Pousser vers un point de terminaison OTLP {#pushing-to-an-otlp-endpoint}

Les enregistrements peuvent être poussés directement vers un point de terminaison de journaux OTLP/HTTP, en plus d'être écrits dans le fichier ou sur stdout, en configurant un exportateur dans l'option `--otel.config`, une chaîne JSON ou YAML comme les autres options de configuration (disponible dans les versions de StackQL à partir de `v0.12.732`) :

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\'
```

| Clé | Rôle |
|--|--|
|`exporter.endpoint`|URL complète du point de terminaison de journaux, par exemple `http://localhost:4318/v1/logs` ; obligatoire pour activer l'exportateur|
|`exporter.headers`|En-têtes de requête HTTP envoyés avec chaque export, généralement un jeton d'ingestion ou une clé d'API|
|`exporter.timeout_ms`|Délai d'expiration de la requête HTTP en millisecondes, valeur par défaut `10000`|
|`exporter.batch_size`|Enregistrements par requête HTTP, valeur par défaut `512`|

<br />

Chaque instruction constitue un lot : ses enregistrements de ligne et son enregistrement de fin sont envoyés dans un seul `LogsData` OTLP/JSON (une seule enveloppe de ressource et de scope, une entrée dans `logRecords` par enregistrement), et un grand jeu de résultats est découpé en requêtes HTTP d'au plus `batch_size` enregistrements au fil de l'arrivée des lignes, la dernière requête HTTP étant envoyée après l'enregistrement de fin. Les réponses `429` et `5xx` ainsi que les échecs de connexion donnent lieu à de nouvelles tentatives, jusqu'à quatre tentatives au total, avec un délai d'attente qui double à partir de 500ms, en respectant un en-tête `Retry-After` exprimé en secondes (plafonné à 30 secondes) lorsque le serveur en envoie un ; les autres erreurs ne donnent lieu à aucune nouvelle tentative. Le fichier ou le flux est écrit dans tous les cas, de sorte qu'un push en échec laisse une copie locale et une ligne d'erreur sur stderr. L'exportateur parle OTLP/JSON sur HTTP ; un point de terminaison qui n'accepte que protobuf nécessite un Collector placé devant lui.

Aucune variable d'environnement `OTEL_EXPORTER_OTLP_*` n'est lue ; chaque réglage de l'exportateur provient de `--otel.config`. Une valeur `--otel.config` mal formée, ou un objet `exporter` sans `endpoint`, fait échouer le démarrage.

Cela cible le point d'ingestion OTLP de ClickStack (`http://<host>:4318/v1/logs`, en-tête `authorization: <ingestion API key>`), le récepteur OTLP d'un Datadog Agent ou le point d'ingestion OTLP de Datadog (`dd-api-key: <key>`), ainsi que tout récepteur `otlp` d'un OpenTelemetry Collector. Consultez [Recettes pour backends OTel](/getting-started/otel-recipes) pour les requêtes d'inventaire, de dérive et de droits d'accès côté réception.
