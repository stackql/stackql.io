grammar CREATEVIEW;

import common;

createViewStatement
    : CREATE ( (OR REPLACE) | (MATERIALIZED) ) VIEW viewName AS selectStatement  
    ( ( OUTER )? JOIN selectStatement | UNION selectStatement )? ';'
    ;

