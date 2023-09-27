import { spinner, getSpinnerFrames, spinnerRepeatCount, termLineDelay } from './shared';

export const azureSelect = [
    {
        text: "SELECT name, location",
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: "FROM azure.compute.virtual_machines",
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: "WHERE resourceGroupName = 'my-azure-rg'",
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: "AND subscriptionId = 'my-azure-sub-id'",
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: ' ',
        cmd: false,
        repeat: true,
        repeatCount: spinnerRepeatCount,
        frames: spinner.map(getSpinnerFrames),
    },
    {
        text: 
`+----------------------------+                                                                                                                                                      
|      name       | location |                                                                                                                                                      
+-----------------|----------+                                                                                                                                                      
| aks-agentpool-0 | eastus2  |                                                                                                                                                      
|-----------------|----------|                                                                                                                                                      
| aks-agentpool-1 | eastus2  |                                                                                                                                                      
+-----------------|----------+`,
        cmd: false,
    },
]
  