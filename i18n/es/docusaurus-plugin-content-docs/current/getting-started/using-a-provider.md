---
title: Usar un provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Usar un provider en StackQL: descargarlo del registro, autenticarse con variables de entorno o credenciales y consultar sus servicios y recursos."
image: "/img/stackql-featured-image.png"
---

Consulte también:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

Las consultas de StackQL se pueden ejecutar en varios proveedores de nube, entre ellos Google Cloud Platform, Amazon Web Services, Microsoft Azure, DigitalOcean y otros. Las especificaciones de los providers de StackQL se descargan del [__StackQL Provider Registry__](/providers); esto se puede hacer con el comando [__`REGISTRY PULL`__](/language-spec/registry) de StackQL o con la utilidad de línea de comandos [__`stackql registry`__](/command-line-usage/registry).


## Instalar un provider {#installing-a-provider}

Para ver la lista de providers disponibles, ejecute el siguiente comando:  

```sql
REGISTRY LIST;
```

Para instalar un provider, ejecute el siguiente comando:

```sql
REGISTRY PULL okta;
```

Para ver los providers instalados en el sistema actual, ejecute el siguiente comando:

```sql
SHOW PROVIDERS;
```

## Autenticarse en un provider {#authenticating-to-a-provider}

Autenticarse en un proveedor de nube suele ser el primer paso de una rutina de StackQL. Los metacomandos como [`SHOW`](/language-spec/show) y [`DESCRIBE`](/language-spec/describe) se pueden ejecutar en un provider sin autenticarse, pero los comandos [`SELECT`](/language-spec/select), [`INSERT`](/language-spec/insert) y [`EXEC`](/language-spec/exec) requieren por lo general una sesión autenticada.  

La autenticación se realiza mediante variables de entorno definidas en el sistema (u obtenidas como secretos en un sistema de CI); cada provider tiene sus propias variables predeterminadas y exclusivas para la autenticación, así que consulte la documentación de su provider en la [documentación del StackQL Provider Registry](/providers). Las variables también se pueden cargar al inicio desde un archivo de tipo dotenv con la opción global [`--env.file`](/command-line-usage/global-flags).  

:::tip

Puede autenticarse en varios providers en el mismo shell interactivo o en la misma rutina por lotes no interactiva estableciendo las variables de autenticación de cada provider, por ejemplo:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

Esto resulta especialmente útil cuando realiza joins de datos entre varios providers en una sola consulta.

:::
