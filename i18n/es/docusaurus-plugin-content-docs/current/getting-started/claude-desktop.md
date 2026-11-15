---
title: Usar StackQL con Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ia
  - infrastructure-as-code
  - multinube
description: Use StackQL con Claude Desktop para consultar y aprovisionar recursos en la nube en lenguaje natural
image: "/img/stackql-featured-image.png"
---

StackQL se integra con Claude Desktop mediante Model Context Protocol (MCP), lo que le permite consultar y aprovisionar recursos en la nube de varios proveedores de nube en lenguaje natural. Esta potente combinación le permite interactuar con su infraestructura en la nube de forma conversacional mientras aprovecha internamente la interfaz basada en SQL de StackQL.

Hay tres opciones de instalación, presentadas aquí en el orden recomendado: instalar desde el Connector Directory de Anthropic, instalar desde un bundle descargado o registrar manualmente un binario de StackQL instalado localmente.

## Instalar desde el Connector Directory (recomendado) {#install-from-the-connector-directory-recommended}

La forma más sencilla de usar el servidor MCP de StackQL con Claude Desktop es instalarlo desde el Connector Directory de Anthropic. En Claude Desktop: **Settings -> Connectors -> Add -> Browse Connectors**, busque **StackQL** y haga clic en **Install**. No hay nada que descargar y no se requiere una instalación independiente de StackQL.

![StackQL en el Connector Directory de Anthropic](/img/stackql-anthropic-directory-listing.png)

También puede ver la entrada directamente en [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

Esto instala el servidor MCP de StackQL en proceso para su plataforma: la entrada sirve un bundle multiplataforma combinado que contiene binarios firmados para macOS universal, Windows x64 y Linux x64 y arm64, y Claude Desktop selecciona automáticamente el adecuado para su equipo. Para Windows ARM64 nativo, instale el bundle `windows-arm64` independiente desde las [descargas que aparecen más abajo](#install-from-a-downloaded-bundle). Los binarios de Windows están firmados con Authenticode y el binario de macOS está notarizado por Apple - los mismos binarios que los de la versión independiente de StackQL.

![Instalación del conector de StackQL](/img/stackql-anthropic-directory-install.png)

Un conector instalado desde el directorio hereda el entorno de Claude Desktop, así que defina las variables de entorno de sus providers en el nivel del sistema operativo antes de iniciar Claude Desktop - consulte [Credenciales específicas de cada provider](#provider-specific-credentials). Claude Desktop captura su entorno al iniciarse: después de cambiar variables, debe cerrar Claude Desktop por completo (incluido el icono de la bandeja del sistema en Windows) y volver a iniciarlo. Pedir al agente que ejecute [`reload_credentials`](/mcp/reload_credentials) informa del estado de las credenciales de cada provider y es la forma más rápida de confirmar qué puede ver el servidor y qué no.

Claude Desktop anuncia la capacidad de elicitación de MCP, por lo que el modo predeterminado del servidor (`safe`) le solicitará aprobación en cada mutación - consulte [Modos del servidor](/command-line-usage/mcp#server-modes).

## Instalar desde un bundle descargado {#install-from-a-downloaded-bundle}

Use esta opción de instalación para fijar una versión específica, instalar sin acceso al Connector Directory o verificar usted mismo las sumas de comprobación antes de instalar.

El MCP Bundle precompilado (`.mcpb`) es un paquete instalable con un solo clic que contiene el binario `stackql` firmado y la configuración del servidor. No se requiere una instalación independiente de StackQL.

Descargue el bundle para su plataforma desde la versión más reciente:

| Plataforma | Bundle |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (nativo) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

Después, en Claude Desktop: **Settings -> Extensions -> Install Extension** y seleccione el archivo descargado. El binario integrado está firmado con Authenticode (Windows) y notarizado por Apple (macOS); cada bundle tiene junto a él una suma de comprobación `.sha256` en la [página de la versión](https://github.com/stackql/stackql/releases/latest). Para verificar antes de instalar:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

El servidor también figura en el [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) como `io.github.stackql/stackql-mcp`, que fija estas sumas de comprobación.

## Configuración manual {#manual-configuration}

Si ya tiene StackQL instalado, puede registrar el servidor MCP de StackQL directamente en el archivo de configuración de Claude Desktop.

### Requisitos previos {#prerequisites}

Antes de configurar StackQL con Claude Desktop, asegúrese de tener:

1. **StackQL instalado** - Descargue e instale StackQL desde [stackql.io](/installing-stackql) o use un gestor de paquetes
2. **Claude Desktop** - Descárguelo desde el [sitio web de Anthropic](https://claude.ai/desktop)
3. **Credenciales de proveedores de nube** - Claves de API o credenciales de los proveedores de nube con los que quiere trabajar

Compruebe que StackQL está en el PATH del sistema ejecutando:

```bash
stackql --version
```

### Configurar el servidor MCP {#setting-up-the-mcp-server}

Para que Claude Desktop pueda comunicarse con StackQL, debe configurar el servidor MCP en el archivo de configuración de Claude Desktop.

#### Windows {#windows}

Edite el archivo de configuración ubicado en:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

Edite el archivo de configuración ubicado en:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

Edite el archivo de configuración ubicado en:
```
~/.config/Claude/claude_desktop_config.json
```

### Ejemplo de configuración {#configuration-example}

Agregue la siguiente configuración a su archivo `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
Solo necesita incluir las variables de entorno de los proveedores de nube que piensa usar. Elimine de la configuración las credenciales de los providers que no use.
:::

:::warning
Nunca confirme en el control de versiones su archivo `claude_desktop_config.json` con credenciales reales. Use prácticas seguras de gestión de credenciales.
:::

Claude Desktop anuncia la capacidad de elicitación de MCP, por lo que el modo predeterminado del servidor (`safe`) le solicitará aprobación en cada mutación. Cambie a `mode: read_only` para un acceso de solo inventario o a `mode: full_access` para omitir las solicitudes de aprobación - consulte [Modos del servidor](/command-line-usage/mcp#server-modes).

### Usar un archivo de credenciales (`--env.file`) {#using-a-credentials-file---envfile}

Una alternativa al bloque `env` (y en Windows la recomendada) es mantener las credenciales en un archivo de estilo dotenv y pasarlo con la opción [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials):

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

Con un archivo de credenciales mínimo:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

Por qué esto es mejor que el bloque `env`:

- Los cambios en el bloque `env` requieren un reinicio completo de Claude Desktop (incluido cerrar el icono de la bandeja del sistema) antes de que el subproceso MCP los vea, y en Windows `setx` escribe en el registro - nunca llega al proceso de Desktop en ejecución.
- El archivo de `--env.file` se puede crear o rotar en cualquier momento (ni siquiera necesita existir cuando se inicia el servidor), y pedir al agente que ejecute [`reload_credentials`](/mcp/reload_credentials) recoge los cambios sin ningún reinicio.

:::note

`--env.file` y `reload_credentials` están disponibles en las versiones de StackQL posteriores a `v0.10.542`.

:::

### Credenciales específicas de cada provider {#provider-specific-credentials}

Cada proveedor de nube requiere métodos de autenticación distintos; consulte la documentación de los providers ([https://stackql.io/providers](/providers)) para conocer las variables de entorno necesarias para el provider o los providers que quiera usar.


### Reiniciar Claude Desktop {#restarting-claude-desktop}

Después de actualizar el archivo de configuración, reinicie Claude Desktop para cargar la nueva configuración del servidor MCP. El servidor MCP de StackQL se iniciará automáticamente cuando comience una conversación nueva.

## Qué puede hacer {#what-you-can-do}

Una vez configurado, puede usar lenguaje natural en Claude Desktop para:

### Consultar recursos en la nube {#query-cloud-resources}

Pida a Claude que obtenga información sobre su infraestructura en la nube:

- "Muéstrame todas mis instancias de cómputo de GCP en la región us-central1"
- "Enumera todos los buckets de S3 de mi cuenta de AWS"
- "¿Qué máquinas virtuales de Azure están en ejecución en mi suscripción?"
- "Muéstrame todos los pods de Kubernetes del espacio de nombres default"

### Aprovisionar recursos {#provision-resources}

Cree y despliegue recursos en la nube con comandos conversacionales:

- "Crea un bucket de S3 llamado 'my-data-bucket' en us-west-2"
- "Despliega un bucket de Cloud Storage de GCP con el control de versiones habilitado"
- "Lanza una instancia EC2 pequeña en us-east-1"
- "Crea un grupo de recursos de Azure en westus2"

### Analizar y auditar {#analyze-and-audit}

Realice auditorías de seguridad y comprobaciones de cumplimiento:

- "Muéstrame todos los buckets de S3 accesibles públicamente"
- "Enumera los usuarios de IAM que no han iniciado sesión en 90 días"
- "Busca todas las instancias de GCP sin las etiquetas adecuadas"
- "Identifica las máquinas virtuales de Azure sin copia de seguridad habilitada"

### Operaciones entre nubes {#cross-cloud-operations}

Consulte y compare recursos de varios proveedores de nube:

- "Muéstrame todas las instancias de cómputo de AWS, GCP y Azure"
- "Compara los costos de almacenamiento entre AWS S3 y GCP Cloud Storage"
- "Enumera todas las bases de datos de todos los proveedores de nube"

### Infraestructura como código {#infrastructure-as-code}

Genere y ejecute consultas de despliegue de infraestructura:

- "Crea un stack de recursos que incluya una VPC, una subred y una instancia EC2"
- "Despliega un clúster de GKE completo con grupos de nodos"
- "Configura un despliegue multirregión en AWS"

### Modificar recursos {#modify-resources}

Actualice recursos en la nube existentes:

- "Agrega etiquetas a todas las instancias EC2 sin etiquetar"
- "Habilita el cifrado del bucket de S3 'my-bucket'"
- "Actualiza el tipo de máquina de la instancia de GCP 'web-server-1'"
- "Cambia la ACL de mi bucket de Cloud Storage a privada"

### Eliminar recursos {#delete-resources}

Elimine recursos en la nube cuando ya no sean necesarios:

- "Elimina el bucket de S3 'temp-bucket'"
- "Elimina todas las instancias EC2 detenidas"
- "Limpia los discos de GCP sin usar"

## Conversación de ejemplo {#example-conversation}

Este es un ejemplo de cómo podría interactuar con Claude Desktop usando StackQL:

**Usted:** "¿Puedes mostrarme todas mis instancias de cómputo de GCP?"

**Claude:** *Ejecuta una consulta de StackQL y devuelve los resultados en una tabla con formato que muestra los nombres de las instancias, las zonas, los tipos de máquina y el estado*

**Usted:** "Crea un nuevo bucket de Cloud Storage llamado 'analytics-data-2025' en la región us-central1 con acceso uniforme a nivel de bucket"

**Claude:** *Ejecuta una instrucción INSERT de StackQL para crear el bucket y confirma la creación*

**Usted:** "Ahora enumera todos mis buckets de Cloud Storage"

**Claude:** *Ejecuta una consulta SELECT y muestra todos los buckets, incluido el recién creado*

## Consejos y prácticas recomendadas {#tips-and-best-practices}

1. **Sea específico**: cuanto más específica sea su solicitud, mejores resultados obtendrá. Incluya nombres de recursos, regiones y atributos específicos cuando sea posible.

2. **Use lenguaje natural**: no necesita conocer la sintaxis de SQL - Claude traduce sus solicitudes en lenguaje natural a consultas de StackQL.

3. **Revise antes de ejecutar**: en las operaciones destructivas (DELETE, UPDATE), Claude normalmente le mostrará lo que se va a ejecutar antes de continuar.

4. **Operaciones de varios pasos**: puede encadenar operaciones en una sola conversación. Claude mantiene el contexto durante toda la sesión.

5. **Gestión de errores**: si una consulta falla, Claude puede ayudar a solucionar el problema y sugerir correcciones a partir del mensaje de error.

6. **Explore las capacidades**: pregunte a Claude "¿Qué puedo hacer con StackQL?" para obtener sugerencias específicas para sus providers configurados.

## Solución de problemas {#troubleshooting}

### El servidor MCP no se inicia {#mcp-server-not-starting}

Si Claude Desktop no puede conectarse al servidor MCP de StackQL:

1. Compruebe que StackQL está en su PATH
2. Compruebe que su archivo `claude_desktop_config.json` es JSON válido
3. Asegúrese de que la ruta del archivo en la configuración es correcta para su sistema operativo
4. Revise los logs de Claude Desktop en busca de mensajes de error

### Errores de autenticación {#authentication-errors}

Si recibe errores de autenticación:

1. Compruebe que las credenciales de su proveedor de nube son correctas
2. Asegúrese de que los archivos de credenciales existen en las rutas especificadas
3. Compruebe que las variables de entorno tienen el formato correcto en la configuración - pedir al agente que ejecute [`reload_credentials`](/mcp/reload_credentials) informa del estado de las credenciales de cada provider y es la forma más rápida de ver qué variable falta
4. Confirme que sus credenciales tienen los permisos necesarios

### Errores de permisos {#permission-errors}

Si las operaciones fallan por permisos insuficientes:

1. Compruebe que su cuenta de servicio o usuario de IAM tiene los permisos necesarios
2. Revise las políticas de IAM específicas de cada proveedor de nube
3. Asegúrese de que los servicios de API están habilitados en sus proyectos en la nube

## Más información {#learn-more}

- [Referencia del comando MCP de StackQL](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Especificación del lenguaje](/language-spec/select)
- [Documentación de Model Context Protocol](https://modelcontextprotocol.io)

## Consideraciones de seguridad {#security-considerations}

:::caution
- Almacene las credenciales de forma segura y nunca las confirme en el control de versiones
- Use principios de acceso con privilegios mínimos para las cuentas de servicio
- Rote periódicamente las credenciales y las claves de API
- Supervise los logs del servidor MCP para detectar actividad inusual
- Considere usar las herramientas de gestión de credenciales de los proveedores de nube (p. ej., Secret Manager, Parameter Store)
:::
