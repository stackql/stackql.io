---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction JULIANDAY dans StackQL : le nombre fractionnaire de jours depuis midi le 24 novembre 4714 av. J.-C. pour une valeur temporelle, utile pour les calculs de dates."
image: "/img/stackql-featured-image.png"
---
Renvoie le jour julien - le nombre de jours écoulés depuis midi à Greenwich le 24 novembre 4714 av. J.-C. [(calendrier grégorien proleptique)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Modificateurs de date ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*datetime_expression*__  
La valeur de date et d'heure.

__*modifier*__  
Zéro ou plusieurs modificateurs à appliquer à *datetime_expression*.  Pour plus d'informations sur les modificateurs, consultez [**Modificateurs de date**](/language-spec/functions/datetime/date_modifiers)

## Valeur(s) renvoyée(s) {#return-values}

Renvoie le [jour julien](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar) représentant la composante date de l'entrée *datetime_expression* après application de *modifier*.

* * *

## Exemples {#examples}

### Calculer le nombre de jours entre deux dates {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Utilisez la fonction [**ROUND**](/language-spec/functions/math/round) pour convertir des nombres à virgule flottante en l'entier le plus proche.

:::

Pour plus d'informations, consultez [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
