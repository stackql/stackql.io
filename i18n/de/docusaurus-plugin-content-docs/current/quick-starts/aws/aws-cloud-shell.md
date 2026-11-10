---
title: StackQL in AWS Cloud Shell
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
description: "StackQL als angemeldeter Benutzer in AWS CloudShell ausführen: Linux-Paket laden, stackql-Shell nutzen, IAM-Rolle annehmen, Batch-Abfragen mit exec ausführen."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# StackQL in AWS Cloud Shell verwenden

In dieser Anleitung zeigen wir die Verwendung von StackQL, einem leistungsfähigen Entwicklungstool, mit dem sich Cloud-Infrastruktur und -Ressourcen per SQL-Syntax abfragen und bereitstellen lassen, aus AWS Cloud Shell heraus - mit dem Benutzerkontext der Anmeldesitzung an der AWS Console oder durch Annehmen einer anderen IAM-Rolle.    

Getestet mit <span class="tutorial_tested_on">eingebettetes SQL-Backend</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>Was ist <b>AWS Cloud Shell</b>?</summary>

> AWS Cloud Shell ist ein browserbasierter, vorauthentifizierter Shell-Dienst von Amazon Web Services (AWS).  Er ermöglicht Entwicklern und IT-Fachleuten, ihre Cloud-Ressourcen direkt aus dem Webbrowser zu verwalten.  Mit AWS Cloud Shell können Benutzer schnell Befehle für AWS-Services ausführen, ohne die AWS CLI (Command Line Interface) auf ihren lokalen Rechnern installieren oder konfigurieren zu müssen.  Der Dienst stellt eine einsatzbereite Umgebung mit vorinstallierten gängigen Entwicklungs- und Verwaltungswerkzeugen bereit, etwa der AWS CLI, Python, Node.js und mehr, was die Interaktion mit AWS-Services und die Automatisierung von Aufgaben erleichtert.  AWS Cloud Shell ist direkt in die AWS Management Console integriert und bietet so ein nahtloses Erlebnis beim Ausführen administrativer Aufgaben, beim Erkunden von AWS-Services und beim Verwalten von Infrastruktur.  Da AWS Cloud Shell Benutzer automatisch über ihre Anmeldung an der AWS Console authentifiziert, vereinfacht es das Ausführen von Befehlen und Skripten im Kontext ihres AWS-Kontos und steigert so Produktivität und Effizienz im Cloud-Betrieb.

</details>

## Funktionsweise {#how-it-works}

Das StackQL-Linux-Paket enthält Skripte, um `stackql` direkt in den integrierten Cloud-Shell-Umgebungen der großen Cloud-Anbieter zu verwenden.  Für AWS ist das Skript __`stackql-aws-cloud-shell.sh`__.  Die Verwendung des Skripts __`stackql-aws-cloud-shell.sh`__ ist hier dargestellt:  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

Das Skript `stackql-aws-cloud-shell.sh` lädt automatisch den neuesten AWS-Provider für StackQL herunter.  Der Quellcode der enthaltenen Cloud-Shell-Skripte ist unter [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts) zu finden.  

## Das `stackql`-Linux-Paket aus AWS Cloud Shell herunterladen {#download-the-stackql-linux-package-from-aws-cloud-shell}

Laden Sie zunächst aus AWS Cloud Shell die neueste Linux-Binärdatei für StackQL mit den folgenden Befehlen herunter:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

Dadurch werden die `stackql`-Binärdatei und das Skript `stackql-aws-cloud-shell.sh` heruntergeladen und in das Home-Verzeichnis Ihrer AWS Cloud Shell-Umgebung entpackt.  Jetzt kann es losgehen!  

## Die `stackql shell` aus AWS Cloud Shell verwenden {#using-the-stackql-shell-from-aws-cloud-shell}

Eine authentifizierte StackQL-Kommandoshell starten Sie aus AWS Cloud Shell heraus ganz einfach mit:  

```shell
sh stackql-aws-cloud-shell.sh
```

Sie können nun interaktive SQL-basierte AWS-Abfragen mit dem StackQL-Provider ausführen, wie hier gezeigt:  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Ihr Browser unterstützt das Video-Tag nicht.
</video>

## Eine IAM-Rolle mit StackQL annehmen {#assuming-an-iam-role-using-stackql}

Das Annehmen einer IAM-Rolle in AWS ermöglicht es Ihnen, Aktionen auszuführen und auf Ressourcen zuzugreifen, die mit Ihren Standardbenutzerberechtigungen möglicherweise nicht zugänglich sind.  Das ist besonders nützlich, wenn Sie vorübergehend Zugriff auf Ressourcen benötigen oder Aktionen mit anderen Berechtigungen als denen Ihres primären Kontos ausführen möchten.  Mit StackQL können Sie nahtlos eine IAM-Rolle annehmen, um AWS-Ressourcen mit den durch diese Rolle gewährten Berechtigungen abzufragen und zu verwalten.

<details>
<summary>Das Annehmen von IAM-Rollen verstehen</summary>

> Das Annehmen einer IAM-Rolle ist eine sichere Methode, um vorübergehenden Zugriff auf AWS-Ressourcen zu gewähren.  Wenn Sie eine Rolle annehmen, stellt AWS STS Ihnen temporäre Sicherheitsanmeldedaten bereit, mit denen Sie auf die AWS-Services und -Ressourcen zugreifen können, für die die Rolle berechtigt ist.  Dieser Mechanismus eignet sich ideal für kontoübergreifenden Zugriff, vorübergehenden Zugriff für Benutzer oder die Einhaltung des Prinzips der geringsten Berechtigung.

> Rollen können über die AWS Management Console, die AWS CLI sowie direkt in StackQL angenommen werden. So ist sichergestellt, dass Benutzer und Anwendungen nur für die erforderliche Dauer über die notwendigen Berechtigungen verfügen, was die Sicherheitslage Ihrer AWS-Umgebung verbessert.

</details>

Um eine IAM-Rolle mit StackQL in AWS Cloud Shell anzunehmen, verwenden Sie die Option `--role-arn` des Skripts `stackql-aws-cloud-shell.sh`.  Diese Option erfordert den Amazon Resource Name (ARN) der Rolle, die Sie annehmen möchten.  Ist sie angegeben, nimmt StackQL die angegebene Rolle über den AWS Security Token Service (STS) an, bevor Abfragen oder Befehle ausgeführt werden.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

Durch das Annehmen einer Rolle können Sie mit StackQL auf AWS-Ressourcen zugreifen, auf die Ihr Standardbenutzer möglicherweise keinen Zugriff hat, und so Ihre Cloud-Umgebung flexibler und sicherer verwalten.  

Stellen Sie beim Ausführen von StackQL-Befehlen oder -Skripten mit einer angenommenen IAM-Rolle sicher, dass die Rolle über die erforderlichen Berechtigungen verfügt, um die gewünschten Aktionen auszuführen und auf die angegebenen Ressourcen zuzugreifen.  Dieser Ansatz ermöglicht eine granulare Zugriffskontrolle und entspricht den AWS Best Practices für Sicherheit und Governance.  

## `stackql exec` (Batch-Operationen) aus AWS Cloud Shell ausführen {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

StackQL-Abfragen können auch mit dem Befehl `exec` ausgeführt werden; das ist eine nützliche Option für:

- die Angabe alternativer Ausgabeformate wie `json` oder `csv`
- das Erzeugen von Ausgabedateien mit den Ergebnissen von StackQL-Abfragen
- die Verwendung von StackQL-Abfragen aus Eingabedateien (per Konvention `.iql`-Dateien)
- das Ausführen komplexer Operationen, einschließlich des Einlesens von Variablen oder der Vorverarbeitung von Abfragen (mit `json` oder `jsonnet`)

Beispiele für die Verwendung von `exec` in AWS Cloud Shell sind hier dargestellt:  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

Das obige Beispiel führt die angegebene StackQL-Abfrage aus und erzeugt eine `CSV`-Ausgabedatei, die aus AWS Cloud Shell heruntergeladen werden kann.  Zusätzlich können Sie mit dem Argument `--role-arn` eine IAM-Rolle angeben, um für Ihre Abfrage- oder Mutationsoperation eine andere Rolle anzunehmen; ein Beispiel ist hier dargestellt (in diesem Fall stammt die Abfrage aus einer Eingabedatei):  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::824532806693:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

Weitere Informationen zum Befehl `exec` finden Sie [__hier__](/command-line-usage/exec)
