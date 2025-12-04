grammar WITH;

import common;

withClause
    : WITH RECURSIVE? cteDefinition (',' cteDefinition)*
    ;

cteDefinition
    : cteName ('(' columnList ')')? AS '(' selectStatement ')'
    ;

cteName: 'cteName';
columnList: 'columnList';
