---
title: StackQL mit Claude Desktop verwenden
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ki
  - infrastructure-as-code
  - multi-cloud
description: Verwenden Sie StackQL mit Claude Desktop, um Cloud-Ressourcen in natürlicher Sprache abzufragen und bereitzustellen
image: "/img/stackql-featured-image.png"
---

StackQL integriert sich über das Model Context Protocol (MCP) in Claude Desktop und ermöglicht es Ihnen, Cloud-Ressourcen über mehrere Cloud-Anbieter hinweg in natürlicher Sprache abzufragen und bereitzustellen. Diese leistungsstarke Kombination erlaubt es Ihnen, im Dialog mit Ihrer Cloud-Infrastruktur zu arbeiten, während im Hintergrund die SQL-basierte Schnittstelle von StackQL genutzt wird.

Es gibt drei Installationsoptionen, hier in der empfohlenen Reihenfolge: die Installation aus dem Anthropic Connector Directory, die Installation aus einem heruntergeladenen Bundle oder die manuelle Registrierung einer lokal installierten StackQL-Binärdatei.

## Installation aus dem Connector Directory (empfohlen) {#install-from-the-connector-directory-recommended}

Am einfachsten verwenden Sie den StackQL MCP-Server mit Claude Desktop, indem Sie ihn aus dem Anthropic Connector Directory installieren. In Claude Desktop: **Settings -> Connectors -> Add -> Browse Connectors**, suchen Sie nach **StackQL** und klicken Sie auf **Install**. Es muss nichts heruntergeladen werden, und eine separate StackQL-Installation ist nicht erforderlich.

![StackQL im Anthropic Connector Directory](/img/stackql-anthropic-directory-listing.png)

Sie können den Eintrag auch direkt unter [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) aufrufen.

Damit wird der StackQL MCP-Server In-Process für Ihre Plattform installiert: Der Eintrag liefert ein kombiniertes Multiplattform-Bundle mit signierten Binärdateien für macOS universal, Windows x64 sowie Linux x64 und arm64, und Claude Desktop wählt automatisch die passende für Ihren Rechner aus. Für natives Windows ARM64 installieren Sie das separate `windows-arm64`-Bundle aus den [Downloads unten](#install-from-a-downloaded-bundle). Die Windows-Binärdateien sind Authenticode-signiert und die macOS-Binärdatei ist von Apple notarisiert - dieselben Binärdateien wie im eigenständigen StackQL-Release.

![Installation des StackQL-Connectors](/img/stackql-anthropic-directory-install.png)

Ein aus dem Directory installierter Connector erbt die Umgebung von Claude Desktop; setzen Sie die Umgebungsvariablen für Ihre Provider daher auf Betriebssystemebene, bevor Sie Claude Desktop starten - siehe [Provider-spezifische Anmeldedaten](#provider-specific-credentials). Claude Desktop erfasst seine Umgebung beim Start: Nach dem Ändern von Variablen müssen Sie Claude Desktop vollständig beenden (unter Windows einschließlich des Tray-Symbols) und neu starten. Wenn Sie den Agenten bitten, [`reload_credentials`](/mcp/reload_credentials) auszuführen, meldet das den Status der Anmeldedaten pro Provider und ist der schnellste Weg, um zu prüfen, was der Server sehen kann und was nicht.

Claude Desktop meldet Unterstützung für MCP-Elicitation, daher fragt der Standard-Servermodus (`safe`) bei jeder Mutation nach Ihrer Freigabe - siehe [Servermodi](/command-line-usage/mcp#server-modes).

## Installation aus einem heruntergeladenen Bundle {#install-from-a-downloaded-bundle}

Verwenden Sie diese Installationsoption, um eine bestimmte Version festzulegen, ohne Zugriff auf das Connector Directory zu installieren oder Prüfsummen vor der Installation selbst zu verifizieren.

Das vorgefertigte MCP Bundle (`.mcpb`) ist ein mit einem Klick installierbares Paket, das die signierte `stackql`-Binärdatei und die Serverkonfiguration enthält. Eine separate StackQL-Installation ist nicht erforderlich.

Laden Sie das Bundle für Ihre Plattform aus dem aktuellen Release herunter:

| Plattform | Bundle |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (nativ) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

Anschließend in Claude Desktop: **Settings -> Extensions -> Install Extension** und wählen Sie die heruntergeladene Datei aus. Die eingebettete Binärdatei ist Authenticode-signiert (Windows) und von Apple notarisiert (macOS); zu jedem Bundle liegt auf der [Release-Seite](https://github.com/stackql/stackql/releases/latest) eine `.sha256`-Prüfsumme. So verifizieren Sie vor der Installation:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Der Server ist außerdem in der [offiziellen MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) als `io.github.stackql/stackql-mcp` eingetragen, wo diese Prüfsummen festgehalten sind.

## Manuelle Konfiguration {#manual-configuration}

Wenn StackQL bereits installiert ist, können Sie den StackQL MCP-Server direkt in der Konfigurationsdatei von Claude Desktop registrieren.

### Voraussetzungen {#prerequisites}

Bevor Sie StackQL mit Claude Desktop einrichten, stellen Sie Folgendes sicher:

1. **StackQL ist installiert** - Laden Sie StackQL von [stackql.io](https://stackql.io/downloads) herunter und installieren Sie es, oder verwenden Sie einen Paketmanager
2. **Claude Desktop** - Download von der [Website von Anthropic](https://claude.ai/desktop)
3. **Anmeldedaten für Cloud-Anbieter** - API-Schlüssel oder Anmeldedaten für die Cloud-Anbieter, mit denen Sie arbeiten möchten

Prüfen Sie, ob StackQL im PATH Ihres Systems liegt, indem Sie Folgendes ausführen:

```bash
stackql --version
```

### Einrichten des MCP-Servers {#setting-up-the-mcp-server}

Damit Claude Desktop mit StackQL kommunizieren kann, müssen Sie den MCP-Server in der Konfigurationsdatei von Claude Desktop konfigurieren.

#### Windows {#windows}

Bearbeiten Sie die Konfigurationsdatei unter:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

Bearbeiten Sie die Konfigurationsdatei unter:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

Bearbeiten Sie die Konfigurationsdatei unter:
```
~/.config/Claude/claude_desktop_config.json
```

### Konfigurationsbeispiel {#configuration-example}

Fügen Sie die folgende Konfiguration in Ihre Datei `claude_desktop_config.json` ein:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
Sie müssen nur die Umgebungsvariablen für die Cloud-Anbieter angeben, die Sie verwenden möchten. Entfernen Sie nicht verwendete Provider-Anmeldedaten aus der Konfiguration.
:::

:::warning
Committen Sie Ihre Datei `claude_desktop_config.json` niemals mit echten Anmeldedaten in die Versionsverwaltung. Verwenden Sie sichere Verfahren zur Verwaltung von Anmeldedaten.
:::

Claude Desktop meldet Unterstützung für MCP-Elicitation, daher fragt der Standard-Servermodus (`safe`) bei jeder Mutation nach Ihrer Freigabe. Wechseln Sie zu `mode: read_only` für reinen Inventarzugriff oder zu `mode: full_access`, um die Rückfragen zu überspringen - siehe [Servermodi](/command-line-usage/mcp#server-modes).

### Verwenden einer Anmeldedaten-Datei (`--env.file`) {#using-a-credentials-file---envfile}

Eine Alternative zum `env`-Block - und unter Windows die empfohlene - ist, die Anmeldedaten in einer Datei im dotenv-Stil zu halten und diese mit dem Flag [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) zu übergeben:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

Mit einer minimalen Anmeldedaten-Datei:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

Warum das dem `env`-Block überlegen ist:

- Änderungen am `env`-Block erfordern einen vollständigen Neustart von Claude Desktop (einschließlich Beenden über das Tray-Symbol), bevor der MCP-Unterprozess sie sieht, und unter Windows schreibt `setx` in die Registry - den laufenden Desktop-Prozess erreicht es nie.
- Die `--env.file`-Datei kann jederzeit erstellt oder rotiert werden - sie muss beim Start des Servers nicht einmal existieren - und wenn Sie den Agenten bitten, [`reload_credentials`](/mcp/reload_credentials) auszuführen, werden die Änderungen ohne Neustart übernommen.

:::note

`--env.file` und `reload_credentials` sind in StackQL-Releases nach `v0.10.542` verfügbar.

:::

### Provider-spezifische Anmeldedaten {#provider-specific-credentials}

Verschiedene Cloud-Anbieter erfordern unterschiedliche Authentifizierungsmethoden, siehe die Provider-Dokumentation ([https://stackql.io/providers](/providers)) für die Umgebungsvariablen, die für Ihre gewünschten Provider erforderlich sind.


### Claude Desktop neu starten {#restarting-claude-desktop}

Starten Sie Claude Desktop nach dem Aktualisieren der Konfigurationsdatei neu, um die neue MCP-Server-Konfiguration zu laden. Der StackQL MCP-Server wird automatisch gestartet, wenn Sie eine neue Unterhaltung beginnen.

## Was Sie tun können {#what-you-can-do}

Nach der Konfiguration können Sie in Claude Desktop natürliche Sprache für Folgendes verwenden:

### Cloud-Ressourcen abfragen {#query-cloud-resources}

Bitten Sie Claude, Informationen über Ihre Cloud-Infrastruktur abzurufen:

- "Zeige mir alle meine GCP-Compute-Instanzen in der Region us-central1"
- "Liste alle S3-Buckets in meinem AWS-Konto auf"
- "Welche virtuellen Azure-Maschinen laufen in meinem Abonnement?"
- "Zeige mir alle Kubernetes-Pods im Namespace default"

### Ressourcen bereitstellen {#provision-resources}

Erstellen und deployen Sie Cloud-Ressourcen mit Befehlen in Dialogform:

- "Erstelle einen S3-Bucket namens 'my-data-bucket' in us-west-2"
- "Stelle einen GCP Cloud Storage-Bucket mit aktivierter Versionierung bereit"
- "Starte eine kleine EC2-Instanz in us-east-1"
- "Erstelle eine Azure-Ressourcengruppe in westus2"

### Analysieren und prüfen {#analyze-and-audit}

Führen Sie Sicherheitsaudits und Compliance-Prüfungen durch:

- "Zeige mir alle öffentlich zugänglichen S3-Buckets"
- "Liste alle IAM-Benutzer auf, die sich seit 90 Tagen nicht angemeldet haben"
- "Finde alle GCP-Instanzen ohne korrekte Labels"
- "Identifiziere Azure-VMs ohne aktiviertes Backup"

### Cloud-übergreifende Operationen {#cross-cloud-operations}

Fragen Sie Ressourcen über mehrere Cloud-Anbieter hinweg ab und vergleichen Sie sie:

- "Zeige mir alle Compute-Instanzen in AWS, GCP und Azure"
- "Vergleiche die Speicherkosten von AWS S3 und GCP Cloud Storage"
- "Liste alle Datenbanken über alle Cloud-Anbieter hinweg auf"

### Infrastructure as Code {#infrastructure-as-code}

Generieren Sie Abfragen für Infrastruktur-Deployments und führen Sie sie aus:

- "Erstelle einen Stack aus Ressourcen mit einer VPC, einem Subnetz und einer EC2-Instanz"
- "Stelle einen vollständigen GKE-Cluster mit Node-Pools bereit"
- "Richte ein Multi-Region-Deployment in AWS ein"

### Ressourcen ändern {#modify-resources}

Aktualisieren Sie bestehende Cloud-Ressourcen:

- "Füge allen nicht getaggten EC2-Instanzen Tags hinzu"
- "Aktiviere die Verschlüsselung für den S3-Bucket 'my-bucket'"
- "Aktualisiere den Maschinentyp der GCP-Instanz 'web-server-1'"
- "Ändere die ACL meines Cloud Storage-Buckets auf privat"

### Ressourcen löschen {#delete-resources}

Entfernen Sie Cloud-Ressourcen, die nicht mehr benötigt werden:

- "Lösche den S3-Bucket 'temp-bucket'"
- "Entferne alle gestoppten EC2-Instanzen"
- "Bereinige nicht verwendete GCP-Disks"

## Beispielunterhaltung {#example-conversation}

Hier ein Beispiel, wie Sie mit Claude Desktop und StackQL interagieren könnten:

**Sie:** "Kannst du mir alle meine GCP-Compute-Instanzen zeigen?"

**Claude:** *Führt eine StackQL-Abfrage aus und gibt die Ergebnisse in einer formatierten Tabelle mit Instanznamen, Zonen, Maschinentypen und Status zurück*

**Sie:** "Erstelle einen neuen Cloud Storage-Bucket namens 'analytics-data-2025' in der Region us-central1 mit einheitlichem Zugriff auf Bucket-Ebene"

**Claude:** *Führt eine StackQL-INSERT-Anweisung aus, um den Bucket zu erstellen, und bestätigt die Erstellung*

**Sie:** "Liste jetzt alle meine Cloud Storage-Buckets auf"

**Claude:** *Führt eine SELECT-Abfrage aus und zeigt alle Buckets einschließlich des neu erstellten*

## Tipps und Best Practices {#tips-and-best-practices}

1. **Seien Sie konkret**: Je konkreter Ihre Anfrage, desto bessere Ergebnisse erhalten Sie. Geben Sie nach Möglichkeit Ressourcennamen, Regionen und konkrete Attribute an.

2. **Verwenden Sie natürliche Sprache**: Sie müssen keine SQL-Syntax kennen - Claude übersetzt Ihre Anfragen in natürlicher Sprache in StackQL-Abfragen.

3. **Prüfen Sie vor der Ausführung**: Bei destruktiven Operationen (DELETE, UPDATE) zeigt Claude Ihnen in der Regel, was ausgeführt wird, bevor es fortfährt.

4. **Mehrstufige Operationen**: Sie können Operationen in einer einzigen Unterhaltung verketten. Claude behält den Kontext über die gesamte Sitzung bei.

5. **Fehlerbehandlung**: Wenn eine Abfrage fehlschlägt, kann Claude bei der Fehlersuche helfen und anhand der Fehlermeldung Korrekturen vorschlagen.

6. **Erkunden Sie die Möglichkeiten**: Fragen Sie Claude "Was kann ich mit StackQL tun?", um Vorschläge speziell für Ihre konfigurierten Provider zu erhalten.

## Fehlerbehebung {#troubleshooting}

### MCP-Server startet nicht {#mcp-server-not-starting}

Wenn Claude Desktop keine Verbindung zum StackQL MCP-Server herstellen kann:

1. Prüfen Sie, ob StackQL in Ihrem PATH liegt
2. Prüfen Sie, ob Ihre Datei `claude_desktop_config.json` gültiges JSON ist
3. Stellen Sie sicher, dass der Dateipfad in der Konfiguration für Ihr Betriebssystem korrekt ist
4. Sehen Sie in den Logs von Claude Desktop nach Fehlermeldungen

### Authentifizierungsfehler {#authentication-errors}

Wenn Sie Authentifizierungsfehler erhalten:

1. Prüfen Sie, ob Ihre Anmeldedaten für den Cloud-Anbieter korrekt sind
2. Stellen Sie sicher, dass die Anmeldedaten-Dateien unter den angegebenen Pfaden existieren
3. Prüfen Sie, ob die Umgebungsvariablen in der Konfiguration richtig formatiert sind - wenn Sie den Agenten bitten, [`reload_credentials`](/mcp/reload_credentials) auszuführen, meldet das den Status der Anmeldedaten pro Provider und ist der schnellste Weg, um zu sehen, welche Variable fehlt
4. Bestätigen Sie, dass Ihre Anmeldedaten über die erforderlichen Berechtigungen verfügen

### Berechtigungsfehler {#permission-errors}

Wenn Operationen wegen unzureichender Berechtigungen fehlschlagen:

1. Prüfen Sie, ob Ihr Dienstkonto oder IAM-Benutzer über die erforderlichen Berechtigungen verfügt
2. Prüfen Sie die Provider-spezifischen IAM-Richtlinien
3. Stellen Sie sicher, dass die API-Dienste in Ihren Cloud-Projekten aktiviert sind

## Weitere Informationen {#learn-more}

- [StackQL MCP-Befehlsreferenz](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Sprachspezifikation](/language-spec/select)
- [Dokumentation zum Model Context Protocol](https://modelcontextprotocol.io)

## Sicherheitshinweise {#security-considerations}

:::caution
- Speichern Sie Anmeldedaten sicher und committen Sie sie niemals in die Versionsverwaltung
- Wenden Sie das Prinzip der geringsten Rechte auf Dienstkonten an
- Rotieren Sie Anmeldedaten und API-Schlüssel regelmäßig
- Überwachen Sie die Logs des MCP-Servers auf ungewöhnliche Aktivitäten
- Erwägen Sie den Einsatz der Tools der Cloud-Anbieter zur Verwaltung von Anmeldedaten (z. B. Secret Manager, Parameter Store)
:::
