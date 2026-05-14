grammar SHOW;

import common;

showStatement
    : SHOW EXTENDED?
        (
              PROVIDERS
            | (SERVICES | RESOURCES | METHODS) ((FROM | IN) multipartIdentifier (LIKE pattern | WHERE expression)?)?
            | INSERT INTO? multipartIdentifier
            | VERSION
        ) ';'
    ;
