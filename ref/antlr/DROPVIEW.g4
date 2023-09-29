grammar DROPVIEW;

import common;

dropStatement
    : DROP ( MATERIALIZED )? VIEW viewName ';'
    ;