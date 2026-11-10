---
title: Regionsübergreifende Abfragen
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
  - cspm
  - aws
  - amazon web services
description: "AWS-Ressourcen über mehrere Regionen in einer StackQL-Anweisung abfragen und aggregieren, mit Regionslisten und asynchroner Ausführung statt Schleifen pro Region."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Asynchrone regionsübergreifende Abfragen mit StackQL

In dieser Anleitung zeigen wir eine leistungsfähige Funktion von StackQL: die Möglichkeit, Ressourcen über mehrere AWS-Regionen hinweg abzufragen und zu aggregieren.  Dabei werden zahlreiche API-Anfragen an verschiedene Endpunkte gestellt, die asynchron ausgeführt werden.  Für den Benutzer geschieht dies nahtlos und transparent.  

> StackQL ist ein leistungsfähiges Entwicklungstool, mit dem sich Cloud-Infrastruktur und -Ressourcen per SQL-Syntax abfragen und bereitstellen lassen.  

Getestet mit <span class="tutorial_tested_on">eingebettetes SQL-Backend</span> <span class="tutorial_tested_on">Postgres-SQL-Backend</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## AWS-Regionen und -Ressourcen verstehen {#understanding-aws-regions-and-resources}

AWS-Ressourcen werden an mehreren Standorten weltweit gehostet.  Diese Standorte setzen sich aus Regionen und Availability Zones zusammen und sind darauf ausgelegt, Benutzern das Platzieren von Instanzen und das Speichern von Daten an verschiedenen Orten zu ermöglichen.

## Das Problem mit AWS-Regionen... {#the-problem-with-aws-regions}

Bei der Arbeit mit AWS ist es wichtig zu verstehen, dass die AWS Management Console eine regional begrenzte Sicht auf Ressourcen bietet: Benutzer können jeweils nur mit den Ressourcen einer bestimmten Region arbeiten und diese verwalten.  Ebenso verwenden die Befehle der AWS Command Line Interface (CLI) und die Software Development Kits (SDKs) die Region als Einstiegspunkt für Operationen, sodass zum Ausführen von Befehlen oder Programmen eine Region angegeben werden muss.  

> Das regionale Modell von Cloud-Diensten bringt Komplexität mit sich, wenn sich Ressourcen über mehrere Regionen erstrecken.  Darüber hinaus nutzen Sicherheitsverletzungen, einschließlich Exploits zum Schürfen von Kryptowährungen, häufig Ressourcen in verschiedenen Gebieten, auch jenseits der von Organisationen üblicherweise verwendeten Hauptregion.

## Regionen in StackQL-Abfragen angeben {#specifying-regions-in-stackql-queries}

Mit StackQL können Sie Regionen angeben, um Ressourcen an verschiedenen geografischen Standorten anzusprechen.  Das ist besonders nützlich für die Verwaltung von Ressourcen, die weltweit verteilt sind.

### Beispiel: EC2-Instanzen über mehrere Regionen hinweg auflisten {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

Diese Abfrage gibt eine Liste der EC2-Instanzen aus den Regionen `us-east-1` (Nord-Virginia) und `eu-west-1` (Irland) zurück und zeigt, wie StackQL Daten über Regionen hinweg in einer einzigen Abfrage abrufen kann.  Dies lässt sich auch mit Aggregatabfragen verwenden, zum Beispiel:  

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## Die Stärke asynchroner Abfragen {#the-power-of-asynchronous-queries}

StackQL führt diese regionsübergreifenden Abfragen asynchron aus und stellt dabei mehrere parallele API-Anfragen an AWS.  Das gewährleistet einen effizienten Datenabruf, ohne dass auf den Abschluss der Anfrage einer Region gewartet werden muss, bevor die nächste an der Reihe ist.

## Fazit {#conclusion}

Asynchrone regionsübergreifende Abfragen in StackQL bieten eine robuste und effiziente Methode zur Verwaltung von AWS-Ressourcen an verschiedenen geografischen Standorten.  Durch die SQL-ähnliche Syntax dieser Abfragen vereinfacht StackQL komplexe Aufgaben der Cloud-Verwaltung und ist damit ein unverzichtbares Werkzeug für Entwickler und Cloud-Administratoren, die ihre AWS-Infrastruktur optimieren möchten.
