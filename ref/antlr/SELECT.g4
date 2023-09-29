grammar SELECT;

import common;

selectStatement
    : SELECT 
     (DISTINCT)?
    (star='*' | 
    fullFieldName (AS? alias)? | 
    functionCall (AS? alias)?)*
    (
    FROM resource (( OUTER )? JOIN resource ON expression)?
    (WHERE expression)?
    (
    GROUP BY
    groupByItem (',' groupByItem)*
    (WITH ROLLUP)?
    )?
    (HAVING havingExpr=expression)?
    )?
    (ORDER BY fullFieldName (',' fullFieldName)*)? (ASC|DESC)?
    (LIMIT number)?
    ';'
    ;
