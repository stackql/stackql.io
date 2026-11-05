grammar UNSET;

import common;

unsetStatement
    : UNSET SESSION? (variableName | configurationPath) (',' (variableName | configurationPath))* ';'
    ;
