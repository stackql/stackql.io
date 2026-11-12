---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction STRFTIME dans StackQL : formater une valeur de date et d'heure avec une chaîne de format, en utilisant les mêmes substitutions que la fonction C strftime."
image: "/img/stackql-featured-image.png"
---
Renvoie une valeur de date et d'heure formatée selon un format spécifié.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Modificateurs de date ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*format*__  
Le format souhaité pour la valeur de date et d'heure indiquée par *datetime_expression*.  Consultez le tableau des formats ci-dessous.

__*datetime_expression*__  
La valeur de date ou d'heure à formater.

__*modifier*__  
Zéro ou plusieurs modificateurs à appliquer à *datetime_expression*.  Pour plus d'informations sur les modificateurs, consultez [**Modificateurs de date**](/language-spec/functions/datetime/date_modifiers)

### Formats {#formats}

| Format | Description                               |
|--------|-------------------------------------------|
| %d     | jour du mois : 01-31                      |
| %f     | secondes fractionnaires : SS.SSS          |
| %H     | heure : 00-24                             |
| %j     | jour de l'année : 001-366                 |
| %J     | numéro de jour julien                     |
| %m     | mois : 01-12                              |
| %M     | minute : 00-59                            |
| %s     | secondes depuis le 1970-01-01             |
| %S     | secondes : 00-59                          |
| %w     | jour de la semaine 0-6 avec dimanche==0   |
| %W     | semaine de l'année : 00-53                |
| %Y     | année : 0000-9999                         |
| %%     | échappement du symbole de pourcentage : % |

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une chaîne de caractères formatée représentant le format de date et d'heure souhaité.

* * *

## Exemples {#examples}

### Renvoyer une date au format américain {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Convertir une date et heure en horodatage epoch {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Calculer le nombre de jours entre deux dates {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
