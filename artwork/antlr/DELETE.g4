grammar DELETE;

import common;

deleteStatement
    : 
    DELETE queryHint? FROM multipartIdentifier
    WHERE expression (expression)?
    ';'
    ;
