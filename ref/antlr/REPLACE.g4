grammar REPLACE;

import common;

replaceStatement
    : REPLACE multipartIdentifier
      SET fullFieldAssignment (',' fullFieldAssignment)*
      WHERE expression (('AND' | 'OR') expression)*
      ';'
    ;

fullFieldAssignment
    : fullFieldName '=' fieldValue
    ;

fieldValue
    : /* define the rule for fieldValue here */
    ;
