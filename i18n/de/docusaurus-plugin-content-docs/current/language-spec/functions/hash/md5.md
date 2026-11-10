---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - hash
  - digest
description: "MD5-Funktion in StackQL: gibt den hexadezimalen MD5-Digest eines Strings in Kleinbuchstaben zurück, für Fingerprinting und Änderungserkennung, nicht für Sicherheit."
image: "/img/stackql-featured-image.png"
---
Gibt den MD5-Digest eines Strings als 32 hexadezimale Zeichen in Kleinbuchstaben zurück.  Verfügbar in StackQL `v1.0` und höher.

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Verkettung ]](/language-spec/functions/string/concatenation)

* * * 

## Syntax {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expression*__  
Ein String oder ein in Text umgewandelter Wert.  Ein `NULL`-Argument gibt `NULL` zurück.

## Rückgabewert(e) {#return-values}

Gibt einen hexadezimalen String aus 32 Zeichen in Kleinbuchstaben zurück.

> `MD5` dient dem Fingerprinting und der Änderungserkennung.  Es ist kein Passwort- oder Sicherheitsprimitiv.  Auf dem PostgreSQL-Backend wird die datenbankeigene Funktion `md5` verwendet.

* * *

## Beispiele {#examples}

### Einen abgeleiteten Wert mit einem Fingerprint versehen {#fingerprint-a-derived-value}

Die Variablen werden als typisierte Werte eingesetzt, die Ganzzahl wird in Text umgewandelt und mit dem Suffix verkettet, und der Digest wird über das in Kleinbuchstaben umgewandelte Ergebnis gebildet.  Dieselben Eingaben erzeugen immer denselben hexadezimalen String aus 32 Zeichen, sodass der Wert als stabiler Schlüssel verwendet werden kann.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### Konfigurationsänderungen erkennen {#detect-configuration-changes}

Das Hashen einer serialisierten JSON-Spalte liefert einen kurzen Wert, der gespeichert und zwischen Läufen verglichen werden kann.  Ein Bucket, dessen Digest sich vom letzten Lauf unterscheidet, hat eine geänderte IAM-Konfiguration, ohne dass das gesamte Dokument gespeichert oder per Diff verglichen werden muss.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
