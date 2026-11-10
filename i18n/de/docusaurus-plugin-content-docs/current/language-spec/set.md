---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Sitzungsvariablen
  - set
  - Provider-Sitzungen
  - Secrets
description: "SET in StackQL: typisierte Sitzungsvariablen zuweisen sowie Provider-Aliase, Anmeldedatenfelder und Secrets für die aktuelle Sitzung über Ziele mit Dollar-Präfix konfigurieren."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Weist eine Sitzungsvariable zu oder konfiguriert Laufzeiteinstellungen für die aktuelle Sitzung.  Verfügbar in StackQL `v1.0` und höher.

Siehe auch:  
[[` UNSET `]](/language-spec/unset) [[ Sitzungsvariablen ]](/getting-started/session-variables) [[ Provider-Sitzungen ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## Syntax {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
Ein einfacher Bezeichner (`region`), ein Name in doppelten Anführungszeichen (`"providers.saved"`) oder ein Konfigurationspfad mit Dollar-Präfix in doppelten Anführungszeichen (`"$.auth.aws.password"`).

__*literal*__  
Eine Zeichenkette, eine Zahl, ein boolescher Wert oder `null`.  Zeichenketten stehen in einfachen Anführungszeichen.  Arrays und Objekte sind keine skalaren Werte; halten Sie ein JSON-Dokument als Zeichenkette.

__*DEFAULT*__  
Setzt das Ziel zurück, gleichbedeutend mit [`UNSET`](/language-spec/unset).  Ein explizites `null` ist ein gespeicherter Wert, kein Zurücksetzen.

__*reference*__  
Eine Referenz `"$name"` in doppelten Anführungszeichen auf eine vorhandene Variable, zum Beispiel `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Optional und der einzige Gültigkeitsbereich, den StackQL hat.  `SET SESSION name = ...` und `SET name = ...` sind dieselbe Anweisung: Jede Zuweisung gehört zur aktuellen Sitzung und zu nichts anderem.  Das Schlüsselwort existiert, damit Skripte genauso lesbar sind wie auf Datenbanken, bei denen sich `SET SESSION` und `SET GLOBAL` unterscheiden.  `SET GLOBAL` wird geparst, aber mit `configuration scope "global" is not supported; use SESSION` abgewiesen, weil es keinen sitzungsübergreifenden Zustand gibt.

Zuweisungen in einer `SET`-Liste werden der Reihe nach angewendet und sind keine Transaktion: Schlägt die zweite Zuweisung einer Liste fehl, bleibt die erste in Kraft.

## Ziele {#targets}

| Ziel | Wirkung |
|--|--|
| `name` oder `"name"` | Speichert eine skalare Sitzungsvariable, die in späteren Anweisungen als `$name` dereferenziert wird.  Punkte allein wählen keine Konfiguration aus: `"providers.saved"` ist eine Variable namens `providers.saved`.  Die ältere Schreibweise `"variables.name"` ist ein Alias für `name`. |
| `"$.auth.<provider>.<field>"` | Setzt ein Feld des Authentifizierungskontexts des Providers für diese Sitzung.  Die Felder sind die Schlüssel des [`--auth`](/command-line-usage/global-flags)-Objekts für diesen Provider, zum Beispiel `credentialsenvvar`, `password` oder `token_url`. |
| `"$.providers.<alias>"` | Bindet einen Provider-Alias mit eigenen Anmeldedaten, Mandantenbezeichnung, Parameterstandardwerten und Tuning.  Der Wert ist ein JSON-Dokument in einer Zeichenkette, siehe [Provider-Sitzungen](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Speichert ein Secret im Secret-Store der Sitzung.  Der Wert ist eine literale Zeichenkette, typischerweise ein JSON-`auth`-Dokument, auf das aus einem Provider-Alias als `auth_secret` verwiesen wird. |
| `"$.secret_store"` | Wählt einen persistenten Secret-Store des Betriebssystems aus, `{"backend":"os","namespace":"..."}`.  Erfordert das Operator-Flag `--allow-persistent-secrets`. |

Jede Form wirkt sich nur auf die aktuelle Sitzung aus.  Unabhängige Verbindungen zu einem [`srv`](/command-line-usage/srv)-Server sehen die Zuweisungen der jeweils anderen nicht, und jede MCP-Anfrage ist eine eigene Sitzung; führen Sie Konfiguration und Abfrage bei Verwendung des MCP-Servers daher in einem Anweisungs-Batch aus.

* * *

## Beispiele {#examples}

### Sitzungsvariablen setzen {#set-session-variables}

Jede Anweisung speichert einen Skalar unter dem angegebenen Namen, und der Wert behält seinen Typ: `region` ist eine Zeichenkette, `minimum_count` eine Ganzzahl und `enabled` ein boolescher Wert.  `null` wird als Wert gespeichert, sodass `$optional_value` später zu SQL-`NULL` aufgelöst wird, statt als undefiniert fehlzuschlagen.  Die letzte Anweisung weist zwei Variablen in einer Liste zu.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Den Sitzungsbereich explizit angeben {#make-the-session-scope-explicit}

Die ersten beiden Anweisungen sind identisch: `SESSION` benennt den einzigen Gültigkeitsbereich, den es gibt, und beide Zuweisungen gelten, bis die Variable entfernt wird oder die Sitzung endet.  Verwenden Sie das Schlüsselwort, wenn ein Skript genauso lesbar sein soll wie auf einer Datenbank, die zwischen Sitzungs- und globalen Einstellungen unterscheidet.  Die auskommentierte Anweisung dient dem Vergleich: Sie weist nichts zu und schlägt mit `configuration scope "global" is not supported; use SESSION` fehl, weil zwischen Sitzungen kein Zustand geteilt wird.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Eine Variable in einer Abfrage verwenden {#use-a-variable-in-a-query}

Dereferenzieren Sie eine Variable mit `$name`, ohne Anführungszeichen, an jeder Ausdrucksposition.  Werte behalten ihren Typ: Zeichenketten werden als SQL-Literale escaped, Zahlen, boolesche Werte und `null` werden durchgereicht.  Variablen in einer `WHERE`-Klausel werden aufgelöst, bevor die Anfrage geplant wird, und daher wie Literale an den Provider übergeben.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Eine Variable zurücksetzen {#reset-a-variable}

`DEFAULT` entfernt die Variable, genau wie [`UNSET`](/language-spec/unset), sodass ein späteres `$region` mit `variable "$region" is not defined` fehlschlägt, bis sie erneut gesetzt wird; die auskommentierte Abfrage zeigt diesen Fehler.  Das unterscheidet sich von der Zuweisung von `null`, die die Variable mit dem Wert `NULL` bestehen lässt.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Einen Wert aus einer anderen Variable kopieren {#copy-a-value-from-another-variable}

Die rechte Seite kann eine Referenz in doppelten Anführungszeichen auf eine vorhandene Variable sein; das kopiert den Wert, den sie zu diesem Zeitpunkt enthält.  Hier wird eine Provider-Bindung zunächst als gewöhnliche Variable namens `providers.saved` gespeichert (die Punkte machen sie nicht zur Konfiguration) und dann durch Kopieren auf den Pfad `$.providers.production` aktiviert.  Das anschließende Entfernen der gespeicherten Kopie wirkt sich nicht auf die aktive Bindung aus.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Ein Anmeldedatenfeld für diese Sitzung überschreiben {#override-a-credential-field-for-this-session}

Der Authentifizierungskontext eines Providers ist das Objekt, das beim Start über [`--auth`](/command-line-usage/global-flags) für ihn angegeben wurde.  Ein Pfad unter `$.auth` ersetzt ein Feld dieses Objekts für den Rest der Sitzung und lässt die anderen Felder unverändert.  Hier wird der GitHub-Provider auf eine andere Umgebungsvariable verwiesen, die ein schreibgeschütztes Token enthält; die Variable wird aus der Prozessumgebung von StackQL gelesen, und spätere Abfragen gegen `github.*` verwenden sie, bis das Feld entfernt wird oder die Sitzung endet.

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
```

### Einen Provider-Alias binden {#bind-a-provider-alias}

Ein Provider-Alias ist eine zweite, unabhängig konfigurierte Instanz eines Providers, die in Abfragen anstelle des Provider-Namens verwendet wird.  Der Wert ist ein JSON-Dokument in einer Zeichenkette: `provider` benennt den zugrunde liegenden Provider, `tenancy` bezeichnet das Konto, `auth` ist ein Anmeldedatenblock in derselben Form wie `--auth`, und `params` liefert Standardwerte für Anfrageparameter wie `region`.  Abfragen gegen `production.s3.buckets` laufen dann mit diesen Anmeldedaten, während `aws.s3.buckets` die Startkonfiguration behält.  Die vollständige Liste der Schlüssel finden Sie unter [Provider-Sitzungen](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
