grammar INSERT;

import common;

insertStatement
    : INSERT
      awaitQueryHint? IGNORE? INTO? multipartIdentifier
      ('(' fieldList ')')
      (
        selectStatement
        |
        (
        VALUES '(' fieldList ')'    
        )
      )
      ';'
    ;