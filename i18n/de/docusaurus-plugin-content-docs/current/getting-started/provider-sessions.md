---
title: Provider-Sitzungen und Secrets
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Provider-Sitzungen
  - Multi-Account
  - Anmeldedaten
  - Secrets
  - Keychain
description: "Provider-Sitzungen in StackQL: Provider-Aliasse mit eigenen Anmeldedaten, Tenancy und Parameter-Standardwerten binden, Anmeldedatenfelder pro Sitzung überschreiben und Secrets im Sitzungsspeicher oder einem Secret-Speicher des Betriebssystems halten."
image: "/img/stackql-featured-image.png"
---

Siehe auch:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Sitzungsvariablen ]](/getting-started/session-variables) [[ Globale Flags ]](/command-line-usage/global-flags)

Eine Sitzung kann einen Provider unter einem Alias mit eigenen Anmeldedaten, eigenem Tenancy-Label, eigenen Parameter-Standardwerten und eigenen Limits für parallele Anfragen binden, sodass mehrere Konten oder Mandanten desselben Providers in einer Sitzung und in einer Anweisung abgefragt werden können.  Anmeldedatenfelder können außerdem für die Sitzung überschrieben werden, und Secrets können im Sitzungsspeicher oder, nach Freigabe durch den Betreiber, im Secret-Speicher des Betriebssystems gehalten werden.  All das wird mit [`SET`](/language-spec/set) und [`UNSET`](/language-spec/unset) konfiguriert und ist in StackQL `v1.0` und höher verfügbar.

Alles hier Beschriebene gilt nur für die aktuelle Sitzung.  Unabhängige Verbindungen zu einem `stackql srv`-Server teilen weder Bindungen noch Secrets, und jede MCP-Anfrage ist eine eigene Sitzung; senden Sie Konfiguration und Abfrage daher bei Verwendung des MCP-Servers in einem einzigen Anweisungs-Batch.

## Provider-Aliasse {#provider-aliases}

`SET "$.providers.<alias>"` bindet einen Alias an einen Provider.  Der Wert ist ein JSON-Dokument in einer Zeichenkette:

| Schlüssel | Erforderlich | Beschreibung |
|--|--|--|
| `provider` | Ja | Der kanonische Provider, auf den der Alias aufgelöst wird, zum Beispiel `aws`. |
| `tenancy` | Ja | Ein Gruppierungslabel wie ein Konto- oder Mandantenname.  StackQL prüft es nicht gegen den Provider. |
| `auth` | Eines von `auth` oder `auth_secret` | Ein Authentifizierungsdokument mit derselben Struktur wie der Eintrag des Providers im [`--auth`](/command-line-usage/global-flags)-Objekt.  Der Alias übernimmt nicht die Anmeldedaten des kanonischen Providers. |
| `auth_secret` | Eines von `auth` oder `auth_secret` | Der Name eines [Sitzungs-Secrets](#session-secrets), das das Authentifizierungsdokument enthält. |
| `params` | Nein | Standardwerte für deklarierte Provider-Parameter, einschließlich deklarierter Server-Parameter.  Ein Prädikat in der Abfrage überschreibt einen Standardwert. |
| `tuning` | Nein | Die Limits `max_concurrent` und `max_per_host` für die Anfragen des Alias.  Aliasse, die sich Provider und Tenancy teilen, teilen sich diese Steuerung und müssen identisches Tuning deklarieren. |
<br/>
Das folgende Beispiel bindet zwei AWS-Konten unter getrennten Aliassen, die ihre Schlüssel jeweils aus unterschiedlich benannten Umgebungsvariablen lesen, und fragt beide in einer Anweisung ab:

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

Verweise auf Umgebungsvariablen werden aus der Prozessumgebung von StackQL aufgelöst; es gibt keine sitzungslokale Umgebung.  Temporäre AWS-Anmeldedaten benötigen zusätzlich das Feld für das Sitzungstoken oder den entsprechenden Verweis auf eine Umgebungsvariable.

Ein Alias verhält sich bei Metadaten- und Datenanweisungen wie sein Provider: `SHOW`, `DESCRIBE`, `SELECT`, `INSERT`, `UPDATE`, `DELETE` und `EXEC` akzeptieren den Alias anstelle des Provider-Namens.  Aliasse erstrecken sich nicht auf native eingebaute Relationen oder auf die IaC-Relationen der Vorschau.  `UNION ALL`-Zweige, die unterschiedliche Aliasse verwenden, laufen nacheinander.

Jede Abfrage nimmt einen entkoppelten Snapshot der Anmeldedaten des Alias.  Das Ändern einer Bindung wirkt sich auf spätere Abfragen aus, nicht auf eine bereits laufende.  `UNSET "$.providers.<alias>"` entfernt die Bindung.

## Anmeldedatenfelder überschreiben {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` ändert ein Feld des Authentifizierungskontexts eines Providers für die Sitzung; die Feldnamen sind die Schlüssel des `--auth`-Eintrags des Providers:

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.credentialsenvvar";
```

Wie bei Aliassen gilt die Änderung für Abfragen, die nach dem `SET` gestartet werden.  Abfragen gegen einen Provider, dessen Authentifizierung zurückgesetzt wurde oder dessen Alias kein verwendbares `auth` hat, schlagen fehl, bevor eine Anfrage gesendet wird.

## Sitzungs-Secrets {#session-secrets}

Standardmäßig hat jede Sitzung einen reinen In-Memory-Secret-Speicher, ohne dass eine Konfiguration nötig ist.  `SET "$.secrets.<name>"` speichert eine literale Zeichenkette, typischerweise ein JSON-Authentifizierungsdokument, und ein Provider-Alias verweist mit `auth_secret` darauf:

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

Das Rotieren eines Secrets wirkt sich auf spätere Abfragen aus; das Löschen lässt spätere Verwendungen fehlschlagen.  Eine neue Sitzung erhält einen frischen, leeren Speicher, und das Beenden der Sitzung gibt ihn frei.  `UNSET "$.secret_store"` verwirft die aktuelle Speicherbindung und beginnt erneut mit leerem Sitzungsspeicher.

Die normale Verwendung von Anmeldedaten sowie `SET`-Anweisungen, die Variablen oder Konfiguration speichern, schreiben keine Secrets in das SQL-Backend.  Sensible Daten in eine gewöhnliche Variable oder Tabelle zu legen liegt in der Verantwortung des Aufrufers.

## Persistente Secret-Speicher des Betriebssystems {#persistent-os-secret-stores}

Persistente Speicherung ist standardmäßig deaktiviert und kann nur vom Betreiber aktiviert werden, indem er StackQL mit `--allow-persistent-secrets` startet; SQL kann sie nicht gewähren.  Mit gesetztem Flag wählt eine Sitzung einen Betriebssystemspeicher wie folgt aus:

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

Der Speicher ist der macOS Keychain, der Linux Secret Service (`secret-tool` mit einem funktionierenden Sitzungsdienst) oder der Windows Credential Manager.  Nur das Backend `os` wird akzeptiert: Flat-File-Speicher, Dateipfade und jeder implizite Datei-Fallback werden auch mit gesetztem Flag abgelehnt, und eine nicht verfügbare Betriebssystemfunktion schlägt fehl, statt auf eine Datei zurückzufallen.

In einen Betriebssystemspeicher geschriebene Werte überdauern die SQL-Sitzung und werden explizit mit `UNSET "$.secrets.<name>"` gelöscht.  Das Zurücksetzen der Speicherbindung löscht bereits persistierte Werte nicht.  Der Zugriff auf den Speicher richtet sich nach der Betriebssystemidentität des StackQL-Prozesses; ein Namespace ist keine Zugriffskontrolle pro Benutzer, behandeln Sie diese Freigabe daher nicht als Tresor für mehrere Benutzer.

## Secret-Werte in SQL {#secret-values-in-sql}

Secret-Werte werden nie in SQL dereferenziert, es sei denn, der Betreiber startet StackQL mit `--allow-secret-dereference`.  Mit diesem Flag werden Verweise wie `"$secrets.production"`, `"$secrets.production.credentials"`, `"$auth.github.password"` und `"$providers.production.auth.credentials"` in Ausdrücken aufgelöst:

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

Ohne das Flag gestartet, wird dieselbe Anweisung abgewiesen, bevor irgendetwas ausgewertet wird, wie die auskommentierte Kopie zeigt.  Das Flag ist eine Bestätigung, dass explizit dereferenzierte Werte in SQL-Staging, Ergebnisse, Logs und persistierte Abfrageartefakte gelangen können.  Die normale Provider-Authentifizierung benötigt es nicht.

## Berechtigungen {#permissions}

Bei aktivierter [Zugriffsverwaltung](/command-line-usage/access-management) erfordert das Lesen von Variablen und Secrets `session/variables:read`, und `SET` oder `UNSET` erfordert `session/config:write`.  Ein aufgelöster Alias wird vor der Ausführung als sein kanonischer Provider autorisiert.  Besitz und Berechtigungen pro Variable und pro Secret sind nicht implementiert.
