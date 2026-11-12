---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - hachage
  - empreinte
description: "Fonction MD5 dans StackQL : renvoyer l'empreinte MD5 hexadécimale en minuscules d'une chaîne, pour la prise d'empreinte et la détection de changements, pas pour la sécurité."
image: "/img/stackql-featured-image.png"
---
Renvoie l'empreinte MD5 d'une chaîne de caractères sous la forme de 32 caractères hexadécimaux en minuscules.  Disponible à partir de StackQL `v1.0`.

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Concaténation ]](/language-spec/functions/string/concatenation)

* * * 

## Syntaxe {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expression*__  
Une chaîne, ou une valeur convertie en texte.  Un argument `NULL` renvoie `NULL`.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une chaîne hexadécimale de 32 caractères en minuscules.

> `MD5` est fournie pour la prise d'empreinte et la détection de changements.  Ce n'est pas une primitive de sécurité ni de gestion des mots de passe.  Sur le backend PostgreSQL, c'est la fonction `md5` propre à la base de données qui est utilisée.

* * *

## Exemples {#examples}

### Calculer l'empreinte d'une valeur dérivée {#fingerprint-a-derived-value}

Les variables sont substituées sous forme de valeurs typées, l'entier est converti en texte et joint au suffixe, et l'empreinte est calculée sur le résultat mis en minuscules.  Les mêmes entrées produisent toujours la même chaîne hexadécimale de 32 caractères, si bien que la valeur peut servir de clé stable.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### Détecter les changements de configuration {#detect-configuration-changes}

Le hachage d'une colonne JSON sérialisée donne une valeur courte à stocker et à comparer d'une exécution à l'autre.  Un bucket dont l'empreinte diffère de celle de la dernière exécution a une configuration IAM modifiée, sans qu'il soit nécessaire de stocker ou de comparer le document entier.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
