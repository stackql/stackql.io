---
title: Einen Provider verwenden
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Einen Provider in StackQL verwenden: ihn aus der Registry laden, sich mit Umgebungsvariablen oder Anmeldedaten authentifizieren und seine Services und Ressourcen abfragen."
image: "/img/stackql-featured-image.png"
---

Siehe auch:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

StackQL-Abfragen können gegen eine Reihe verschiedener Cloud-Anbieter ausgeführt werden, darunter Google Cloud Platform, Amazon Web Services, Microsoft Azure, Digital Ocean und weitere. StackQL-Provider-Spezifikationen werden aus der [__StackQL Provider Registry__](/providers) geladen; dies geschieht mit dem StackQL-Befehl [__`REGISTRY PULL`__](/language-spec/registry) oder mit dem Kommandozeilenwerkzeug [__`stackql registry`__](/command-line-usage/registry).


## Einen Provider installieren {#installing-a-provider}

Um die Liste der verfügbaren Provider anzuzeigen, führen Sie den folgenden Befehl aus:  

```sql
REGISTRY LIST;
```

Um einen Provider zu installieren, führen Sie den folgenden Befehl aus:

```sql
REGISTRY PULL okta;
```

Um die auf dem aktuellen System installierten Provider anzuzeigen, führen Sie den folgenden Befehl aus:

```sql
SHOW PROVIDERS;
```

## Authentifizierung bei einem Provider {#authenticating-to-a-provider}

Die Authentifizierung bei einem Cloud-Anbieter ist oft der erste Schritt in einer StackQL-Routine. Metabefehle wie [`SHOW`](/language-spec/show) und [`DESCRIBE`](/language-spec/describe) können ohne Authentifizierung gegen einen Provider ausgeführt werden, aber [`SELECT`](/language-spec/select)-, [`INSERT`](/language-spec/insert)- und [`EXEC`](/language-spec/exec)-Befehle erfordern in der Regel eine authentifizierte Sitzung.  

Die Authentifizierung erfolgt über auf dem System gesetzte Umgebungsvariablen (oder als Secrets aus einem CI-System bezogen); jeder Provider hat seine eigenen standardmäßigen Variablen für die Authentifizierung, ziehen Sie daher die Dokumentation Ihres Providers in der [Dokumentation der StackQL Provider Registry](/providers) zurate.  Die Variablen können beim Start auch aus einer Datei im dotenv-Stil bezogen werden, mit dem globalen Flag [`--env.file`](/command-line-usage/global-flags).  

:::tip

Sie können sich in derselben interaktiven Shell oder nicht-interaktiven Batch-Routine bei mehreren Providern authentifizieren, indem Sie mehrere Authentifizierungsobjekte angeben, zum Beispiel:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

Dies ist besonders nützlich, wenn Sie Daten mehrerer Provider in einer einzigen Abfrage verknüpfen.

:::

