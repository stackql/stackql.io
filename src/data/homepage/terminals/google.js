import { spinner, getSpinnerFrames, spinnerRepeatCount, termLineDelay } from './shared';

export const googleSelect1 = [
    {
        text: 'SELECT machineType, COUNT(*)',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: 'FROM google.compute.instances',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: 'GROUP BY machineType',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: "WHERE project = 'my-gcp-project'",
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: "HAVING COUNT(*) > 10",
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
`+------------------------+
|  machinetype   | count |
+------------------------+
| n1-standard-1  |   13  |
| n1-megamem-96  |   28  |
| c2-standard-60 |   34  |
+------------------------+`,
        cmd: false,
    },
]

export const googleSelect2 = [
    {
        text: 'SELECT status, COUNT(*)',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: 'FROM google.compute.instances',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: 'GROUP BY status',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: "WHERE project = 'my-gcp-project'",
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
`+------------------------+
|    status     | count  |
+------------------------+
| PROVISIONING  |   31   |
| RUNNING       |   82   |
| TERMINATED    |   43   |
+------------------------+`,
        cmd: false,
    },
]