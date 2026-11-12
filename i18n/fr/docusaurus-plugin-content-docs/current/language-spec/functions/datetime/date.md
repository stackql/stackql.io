---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction DATE dans StackQL : renvoyer une date sous forme de chaîne YYYY-MM-DD à partir d'une valeur temporelle et de modificateurs facultatifs."
image: "/img/stackql-featured-image.png"
---
Renvoie une date sous forme de chaîne de caractères au format YYYY-MM-DD.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Modificateurs de date ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*datetime_expression*__  
La valeur de date et d'heure.

__*modifier*__  
Zéro ou plusieurs modificateurs à appliquer à *datetime_expression*.  Pour plus d'informations sur les modificateurs, consultez [**Modificateurs de date**](/language-spec/functions/datetime/date_modifiers)

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une chaîne représentant la composante date de l'entrée *datetime_expression* après application de *modifier*.

* * *

## Exemples {#examples}

### Renvoyer la composante date d'une valeur de date et d'heure {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Renvoyer la composante date locale d'une valeur de date et d'heure {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
