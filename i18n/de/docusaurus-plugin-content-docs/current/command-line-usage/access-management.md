---
title: Zugriffsverwaltung
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Zugriffsverwaltung
  - Authentifizierung
  - Autorisierung
  - jwt
  - mtls
  - Bearer-Token
description: "Optionale Zugriffsverwaltung für Aufrufer in StackQL: Aufrufer per JWT, Bearer-Token, Mutual TLS oder lokaler OS-Identität authentifizieren, in einem Verzeichnis Principals und Gruppen zuordnen und Anweisungen mit Policy-Regeln autorisieren."
image: "/img/stackql-featured-image.png"
---

Die Zugriffsverwaltung für Aufrufer authentifiziert den Aufrufer einer StackQL-Sitzung und autorisiert jede Anweisung anhand einer Policy.  Sie ist unabhängig von den Provider-Anmeldedaten, die weiterhin über `--auth`, Umgebungsvariablen oder [Provider-Sitzungen](/getting-started/provider-sessions) bereitgestellt werden.  Sie ist standardmäßig deaktiviert; Deployments, die sie nicht aktivieren, verhalten sich wie bisher.  Verfügbar in StackQL `v1.0` und höher.

Siehe auch:  
[[ Globale Flags ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## Zugriffsverwaltung aktivieren {#enabling-access-management}

Die Zugriffsverwaltung wird eingeschaltet, indem StackQL mit dem globalen Flag `--access.config` gestartet wird, das auf eine JSON-Konfigurationsdatei verweist.  Das Beispiel startet eine interaktive Shell im geschützten Modus; `exec` und `mcp` werden auf dieselbe Weise aktiviert.

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` erwartet ein JSON-Envelope mit einem einzigen Schlüssel `path`; ein bloßer Dateipfad wird ebenfalls akzeptiert.  Die Konfiguration wird einmal beim Start geladen; rotieren Sie Schlüssel oder Policy daher über einen kontrollierten Neustart.

Solange die Zugriffsverwaltung aktiviert ist, laufen nur `exec`, `shell`, `mcp` und `version`; andere Befehle, einschließlich `registry`, verweigern die Ausführung, statt den geschützten Pfad zu umgehen.  `srv` und der MCP-Servertyp `reverse_proxy` verweigern ebenfalls, weil die PostgreSQL-Wire-Abhängigkeit noch keine Authentifizierungsnachweise pro Verbindung bereitstellt.  Die SQL-Anweisungen `AUTH` und `AUTH REVOKE` verweigern im geschützten Modus; stellen Sie Provider-Anmeldedaten stattdessen über die Host-Konfiguration bereit.  Ein geschütztes `exec`, das fehlschlägt, gibt nach Ausgabe des Fehlers den Exit-Status `1` zurück.

## Authentifizierungsmodi {#authentication-modes}

Die Konfigurationsdatei wählt einen `type`: `jwt` (Standard, wenn nicht angegeben), `bearer`, `mtls` oder `local_os`.  Die Konfiguration ist striktes JSON: unbekannte Felder und Felder, die zu einem anderen Modus gehören, werden abgelehnt.  Alle Modi teilen dieselben `rules` und dasselbe [Principal-Verzeichnis](#principal-directory).

### JWT {#jwt}

Der Aufrufer legt ein signiertes Token vor, das offline gegen die in der Konfiguration fest hinterlegten Schlüssel geprüft wird.  Eine minimale Konfiguration nennt Issuer und Audience, die das Token tragen muss, führt einen öffentlichen Schlüssel auf und gewährt der Gruppe `readers` Lesezugriff:

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

Issuer, Audience und öffentliche Schlüssel sind in der Konfiguration fest hinterlegt; es gibt keine OIDC-Discovery.  Akzeptierte Algorithmen sind `EdDSA`, `RS256`, `RS384`, `RS512`, `PS256`, `PS384`, `PS512`, `ES256`, `ES384` und `ES512`; RSA-Schlüssel müssen mindestens 2048 Bit lang sein.  Jeder Schlüssel ist an seinen konfigurierten Algorithmus gebunden.  Tokens mit unbekannter Schlüssel-ID, ungültiger Signatur, fehlendem oder abgelaufenem Ablaufzeitpunkt, in der Zukunft liegendem `nbf` oder `iat` oder falschem Issuer oder falscher Audience werden abgelehnt.  Gruppen-Claims und URLs im Token werden weder für Vertrauen noch für die Verzeichnismitgliedschaft verwendet.

### Statisches Bearer-Token {#static-bearer-token}

Der Aufrufer legt ein opakes Token vor, und die Konfiguration führt den SHA-256-Digest jedes akzeptierten Tokens zusammen mit dem Subject auf, das es identifiziert.  Das eignet sich für eine kleine, feste Menge von Service-Identitäten, bei der der Betrieb eines Token-Issuers nicht gerechtfertigt ist:

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

Es wird nur der SHA-256-Digest jedes Tokens konfiguriert, und die Digests müssen eindeutig sein.  Eine Änderung der konfigurierten Digests erfordert einen Neustart.

### Mutual TLS {#mutual-tls}

Der Aufrufer wird über das Client-Zertifikat identifiziert, das er auf dem HTTPS-MCP-Transport vorlegt.  Die Konfiguration enthält die CA, die das Zertifikat ausgestellt haben muss, und gibt an, welcher Subject Alternative Name die Identität trägt:

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

Die Client-Zertifikatskette wird gegen `client_ca_pem` geprüft, und das Identitäts-Subject wird aus genau einem SAN der konfigurierten Art, `uri_san` oder `dns_san`, entnommen.  Es gibt keinen Rückgriff auf den Common Name.  Mutual TLS ist nur über den HTTPS-MCP-Transport verfügbar.

### Lokale OS-Identität {#local-os-identity}

Es werden keine Anmeldedaten ausgetauscht: Das Subject ist die Betriebssystemidentität, unter der der StackQL-Prozess läuft.  Die Konfiguration benötigt nur ein `issuer`-Label und die Regeln:

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

Das Identitäts-Subject ist die effektive Unix-UID oder Windows-SID des StackQL-Prozesses, einschließlich numerischer Container-UIDs ohne passwd-Eintrag.  Es werden keine Anmeldedaten vorgelegt, und `STACKQL_CALLER_TOKEN` wird ignoriert.  Dieser Modus ist rein lokal: Für HTTP-Aufrufer wird er verweigert.  Die Gruppenmitgliedschaft im Betriebssystem gewährt keinen Zugriff; nur die Verzeichnismitgliedschaft tut das.

### Sitzungsfrist {#session-deadline}

Abfragen haben standardmäßig keine aus der Authentifizierung abgeleitete Frist.  Anmeldedaten werden für jede neue Anweisung geprüft und die Verzeichnismitgliedschaft wird aktualisiert, sodass abgelaufene Anmeldedaten keine neue Arbeit starten können; der Ablauf unterbricht jedoch keine bereits autorisierte Anweisung.  Setzen Sie `"enforce_session_deadline": true`, um nach `max_session_seconds` (Standard `300`, Maximum `86400`) abzubrechen, verkürzt durch den Ablauf von JWT oder Zertifikat.  Tokens werden dadurch nicht erneuert.

## Principal-Verzeichnis {#principal-directory}

Identitäten werden Principals und Principals Gruppen zugeordnet, in vier Tabellen im konfigurierten SQLite- oder PostgreSQL-Backend:

| Tabelle | Inhalt |
|--|--|
| `stackql_access_principals` | Principal-ID und ein JSON-Objekt mit String-Attributen.  Ein `tenant`-Attribut gliedert den Preview-IaC-Zustand in Namespaces. |
| `stackql_access_identities` | Verifiziertes `(issuer, subject)`-Paar zu Principal-ID. |
| `stackql_access_groups` | Gruppen-IDs. |
| `stackql_access_memberships` | Bindungen von Principal zu Gruppe. |

Legen Sie die Tabellen, Identitäten und Mitgliedschaften über eine vertrauenswürdige Datenbank-Administrationsverbindung an, bevor Sie geschützte Abfragen bedienen:

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

Verwenden Sie den tatsächlichen Issuer und das tatsächliche Subject des Modus: Für die lokale OS-Identität könnte das `('this-host', '1000')` sein, für Mutual TLS `('client-certificates', 'spiffe://example/alice')`.  Wählen Sie das Backend explizit aus:

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

Das Verzeichnis erzeugt für jede Anweisung einen unveränderlichen Snapshot.  Ein Widerruf ist ein `DELETE FROM stackql_access_memberships` auf der Administrationsverbindung; die nächste Anweisung sieht den neuen Snapshot.  Unbekannte Identitäten, ein fehlendes Schema, fehlerhafte Daten und Ausfälle des Verzeichnisses verweigern sämtlich den Zugriff.  Gewöhnliches geschütztes SQL kann die Verzeichnistabellen nicht ansprechen.  Führen Sie Schreibvorgänge an Mitgliedschaften nur über die Administrationsverbindung aus, schützen Sie die Datenbankdatei und verwenden Sie getrennte Datenbankrechte für Administration und Laufzeit, wo das Backend sie unterstützt.

## Policy-Regeln {#policy-rules}

Jede Regel gewährt eine `action` auf einer `resource` an ein `subject` der Form `group:<group id>`.  Verwenden Sie die kompilierten Methoden- und Operationsbezeichner aus vertrauenswürdigen Provider-Dokumenten; ein Operations-Wildcard gewährt sowohl mutierende Methoden als auch Lesezugriffe.

Die Gruppe `readers` besitzt implizit die Safe-Read-SQL-Fähigkeit des Hosts.  Lesezugriffe durch Principals außerhalb dieser Gruppe erfordern `host/sql/read`, und andere Anweisungen erfordern `execute` auf `host/sql/<statement kind>`.  Provider-Operationen und die Nutzung von Anmeldedaten erfordern immer explizite Regeln, und eine Berechtigung auf eine Collection gewährt nicht deren zugrunde liegende Methoden.  Weitere Host-Prüfungen:

| Ressource | Aktion | Geltungsbereich |
|--|--|--|
| `host/sql/read` | `execute` | Leseanweisungen außerhalb der impliziten Reader-Gruppe |
| `host/sql/exec` | `execute` | Natives `EXEC`, einschließlich verschachteltem `EXEC` |
| `host/iac/run`, `host/iac/destroy` | `execute` | Die Preview-IaC-Relationen, trotz ihrer `SELECT`-Syntax |
| `host/function/<name>` | `execute` | Funktionen außerhalb der kleinen Allowlist bekanntermaßen reiner Funktionen |
| `host/backend` | `read` | Opake gespeicherte und externe Backend-Relationen |
| `host/credentials` | `reload` | Das MCP-Tool `reload_credentials` |
| `session/variables` | `read` | Dereferenzieren von [Sitzungsvariablen](/getting-started/session-variables) und Secrets |
| `session/config` | `write` | `SET` und `UNSET` |

Die Expansion gespeicherter Abfragen wird vor der Ausführung geprüft, und die Wiederverwendung von Plan- und Zeilen-Cache ist auf geschützten Pfaden deaktiviert.

## Einstiegspunkte {#entry-points}

| Authentifizierung | `exec`, `shell`, MCP stdio | HTTPS MCP |
|--|--|--|
| JWT | Umgebungsvariable `STACKQL_CALLER_TOKEN` | Request-Header `Authorization: Bearer` |
| Statisches Bearer-Token | Umgebungsvariable `STACKQL_CALLER_TOKEN` | Request-Header `Authorization: Bearer` |
| Lokales OS | Effektive Prozess-UID oder -SID; keine Anmeldedaten | Verweigert |
| Mutual TLS | Verweigert: keine verifizierte TLS-Verbindung | Verifizierte Client-Kette und ein konfigurierter SAN |

Übergeben Sie lokale Token-Anmeldedaten über die Prozessumgebung, niemals als SQL-Literal oder Kommandozeilenargument.  Über HTTPS ersetzt der Aufrufer-Verifizierer die gemeinsame `auth_token_env_var`-Bearer-Sperre, die unter [HTTP-Client-Authentifizierung](/command-line-usage/mcp#http-client-authentication) beschrieben ist; die Konfiguration von Server-TLS-Zertifikat und -Schlüssel ist verpflichtend, und eine Anfrage ohne Anmeldedaten fällt nie auf das Umgebungs-Token des Servers zurück.

## Den Aufrufer prüfen {#inspecting-the-caller}

In einer authentifizierten Sitzung gibt `whoami()` die Principal-ID zurück und `groups()` die Verzeichnisgruppen als JSON-Array:

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

Beide Funktionen erfordern eine mit `--access.config` gestartete Sitzung.

## Walkthroughs {#walkthroughs}

Das StackQL-Repository enthält [vier ausführbare Walkthroughs](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md), einen pro Authentifizierungsmodus, gesteuert von einem Python-Skript, das ein Demonstrationsverzeichnis anlegt und eine Abfrage verifiziert.  Sie benötigen kein Cloud-Konto und werden von der Testsuite des Projekts ausgeführt.
