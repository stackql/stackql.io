grammar REGISTRY;

import common;

registryStatement
    : REGISTRY ( LIST provider? | (PULL provider version? ) )  ';'
    ;