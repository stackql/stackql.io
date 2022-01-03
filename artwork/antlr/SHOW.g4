grammar SHOW;

import common;

showStatement
    : SHOW 
    (
    EXTENDED? 
    (SERVICES | RESOURCES | METHODS ) 
    ((FROM | IN) multipartIdentifier 
    (LIKE pattern | WHERE expression)?)? | PROVIDERS | AUTH | INSERT INTO? multipartIdentifier
    ) ';'
    ;