grammar AUTH;

import common;

authStatement
    : AUTH (LOGIN (provider? INTERACTIVE?)| REVOKE)  ';'
    ;   