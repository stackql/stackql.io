---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "SHOW in StackQL: Provider, Services, Ressourcen und Methoden auflisten, INSERT-Vorlagen erzeugen sowie Version, Mitwirkende und Abhängigkeiten der laufenden Binärdatei einsehen."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Dient zum Auflisten der Elemente einer Sammlung, zum Beispiel aller in einem Provider verfügbaren Services oder aller in einem bestimmten Service verfügbaren Ressourcen. Stellt außerdem die Version, die Mitwirkenden und die Software-Abhängigkeiten der laufenden StackQL-Binärdatei bereit.

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy)

* * * 

## Syntax {#syntax}

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE <pattern> | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

`VERSION`, `CONTRIBUTORS` und `DEPENDENCIES` erfordern keine Provider- oder Authentifizierungskonfiguration. `DEPENDENCIES` unterstützt nur eine optionale `LIKE`-Klausel; `IN`, `FROM`, `WHERE` und `LIMIT` werden nicht unterstützt.

* * *

## Beispiele {#examples}

### Alle Provider auflisten {#list-all-providers}
Listet alle in der installierten StackQL-Version verfügbaren Cloud-Anbieter auf.
```sql
-- Returns all of the available cloud providers
SHOW PROVIDERS;
```
<details>
<summary><em><strong>Rückgabe:</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### Alle Services eines Cloud-Anbieters auflisten {#list-all-services-in-a-cloud-provider}
Listet alle in einem bestimmten Cloud-Anbieter verfügbaren Services auf; mit dem optionalen Schlüsselwort EXTENDED werden zusätzliche Informationen zurückgegeben.
```sql
-- Returns all services available in a cloud provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>Rückgabe:</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### Alle Ressourcen in einem Service eines Cloud-Anbieters auflisten {#list-all-resources-within-a-cloud-provider-service}
Listet alle in einem bestimmten Service eines Cloud-Anbieters verfügbaren Ressourcen auf. Das optionale Schlüsselwort `EXTENDED` gibt zusätzlich zum Ressourcennamen die Ressourcenbeschreibung zurück.
```sql
-- Returns all resources available in a cloud provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>Rückgabe:</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### Verfügbare Methoden einer Ressource in einem Service eines Cloud-Anbieters anzeigen {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
Listet alle für eine bestimmte Ressource verfügbaren Methoden auf.
```sql
-- Returns all methods available for a cloud provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>Rückgabe:</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### Eine `INSERT`-Vorlage mit dem Befehl `SHOW INSERT` erzeugen {#generating-an-insert-template-using-the-show-insert-command}

Mit dem Befehl `SHOW` lassen sich Vorlagen für `INSERT`-Anweisungen erzeugen, wie hier gezeigt:

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>Rückgabe:</strong></em></summary>

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

</details>

> Die zurückgegebenen Felder können auf die vom Provider benötigten beschränkt werden; Beispiele dazu finden Sie unter [__Infrastrukturvorlagen erstellen__](/getting-started/templating).

> Diese Vorlage kann anschließend zusammen mit einer `json`- oder `jsonnet`-Datendatei verwendet werden, um Werte zur Laufzeit bereitzustellen; weitere Informationen finden Sie unter [__Variablen verwenden__](/getting-started/variables).

### Die StackQL-Version anzeigen {#show-the-stackql-version}

Gibt die Version des laufenden StackQL-Prozesses zurück. Nützlich im Shell-, Exec- und Postgres-Wire-Servermodus, wenn Sie prüfen müssen, welcher Build die Abfragen beantwortet. Dieselbe Information wird auch über das MCP-Tool `server_info` bereitgestellt.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>Rückgabe:</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

Mit dem optionalen Schlüsselwort `EXTENDED` werden zusätzlich der Git-Commit, der Build-Zeitstempel und die Build-Plattform zurückgegeben:

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>Rückgabe:</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` erfordert keine Provider-, Registry- oder Authentifizierungskonfiguration und kann in jeder Sitzung gefahrlos ausgeführt werden.

### StackQL-Mitwirkende auflisten {#list-stackql-contributors}

Gibt die in der laufenden StackQL-Binärdatei eingebettete Rangliste der Mitwirkenden zurück. Der Snapshot würdigt menschliche GitHub-Mitwirkende, die das StackQL-Repository mit einem Stern markiert haben, sortiert nach Anzahl der Beiträge, höchste zuerst. Er ist für jeden Build festgelegt und fragt GitHub zur Laufzeit nicht ab.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| Spalte | Beschreibung |
| --- | --- |
| `contributor` | GitHub-Benutzername |
| `contributions` | Im Snapshot erfasste Anzahl der Beiträge; enthalten bei `EXTENDED` |

### Die Software-Abhängigkeiten der Binärdatei auflisten {#list-the-binarys-software-dependencies}

Gibt ein Offline-Inventar der in der laufenden ausführbaren StackQL-Datei enthaltenen Go-Module zurück. Dies stellt eine leichtgewichtige Software Bill of Materials (SBOM) für die Binärdatei bereit, die von Benutzern und Agenten zur Laufzeit abgefragt werden kann. Es umfasst direkte und transitive Go-Abhängigkeiten; installierte Provider-Definitionen, separate Wrapper, CI-Tools, reine Testmodule, Container-Images und Betriebssystempakete liegen außerhalb seines Umfangs.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

Die Standardspalten sind `name`, `version`, `license` und `description`. `EXTENDED` hängt die unten aufgeführten übrigen Spalten an, ohne zu ändern, welche Zeilen zurückgegeben werden.

| Spalte | Beschreibung |
| --- | --- |
| `name` | Effektiver Go-Modulpfad; bei einer Go-Modul-Ersetzung wird der Ersetzungspfad verwendet |
| `version` | Exakte Modulversion, einschließlich Prerelease- und Pseudo-Versionen |
| `license` | Erkannter Lizenzbezeichner; `UNKNOWN`, wenn nicht identifiziert oder mehrdeutig |
| `description` | Komponentenbeschreibung, sofern verfügbar |
| `source_url` | URL des Quell-Repositorys; enthalten bei `EXTENDED` |
| `license_url` | URL der Lizenzdatei, festgelegt auf das Modul-Tag oder die Quellrevision; enthalten bei `EXTENDED` |
| `purl` | Package URL, die Modul und Version identifiziert; enthalten bei `EXTENDED` |
| `checksum` | Go-Modul-Prüfsumme mit ihrem Präfix `h1:`, kein SHA-256-Digest einer ausführbaren Datei oder eines Archivs; enthalten bei `EXTENDED` |
| `original_name` | Ursprünglich angeforderter Modulpfad, wenn eine Ersetzung gilt; enthalten bei `EXTENDED` |
| `original_version` | Ursprünglich angeforderte Version, wenn eine Ersetzung gilt; enthalten bei `EXTENDED` |

Die Zeilen sind nach effektivem Modulnamen und dann nach Version sortiert. Bei Ersetzungen werden Version, Prüfsumme und Attribution des Ersatzmoduls verwendet. `original_name` und `original_version` sind `NULL` für Module ohne Ersetzung. Andere nicht verfügbare Metadaten sind ebenfalls `NULL`, mit der Ausnahme, dass nicht identifizierte Lizenzen `UNKNOWN` sind. Ersetzungen aus dem lokalen Dateisystem erhalten keine erfundene Version, keine erfundenen URLs und keine erfundene Prüfsumme.

Mit `LIKE` durchsuchen Sie den effektiven Modulnamen, den ursprünglichen Modulnamen oder die Beschreibung:

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

Der Abgleich unterscheidet nicht zwischen Groß- und Kleinschreibung. `%` steht für null oder mehr Zeichen und `_` für genau ein Zeichen. Die Lizenz- und Versionsspalten werden nicht durchsucht. Ein einfaches Anführungszeichen innerhalb eines SQL-String-Literals wird verdoppelt; nach der Dekodierung des SQL-Strings sind Backslashes literal und maskieren keine `LIKE`-Platzhalter.

Wenn keine Module übereinstimmen, enthält das Ergebnis null Zeilen. CSV behält die Spaltenüberschriften, PostgreSQL-Wire-Clients behalten die Spaltenmetadaten, und die JSON-Ausgabe ist `null`.

Das Inventar kombiniert zur Build-Zeit eingebettete Metadaten mit den Go-Build-Informationen der ausführbaren Datei. Abfragen benötigen keinen Netzwerkzugriff, keine Go-Installation, keinen Quell-Checkout und keinen Modul-Cache. Builds ohne generierte Metadaten melden weiterhin ihre gelinkten Module, wobei fehlende Attribution als `UNKNOWN` oder `NULL` angezeigt wird.

Die Ausgabe umfasst nur die Go-Abhängigkeiten der Binärdatei. Sie enthält keine Abhängigkeitsbeziehungen, exportiert kein SPDX- oder CycloneDX-Dokument und ist weder eine vollständige Deployment-SBOM noch eine Feststellung der Lizenzkonformität.

* * *
