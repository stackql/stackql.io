import { spinner, getSpinnerFrames, spinnerRepeatCount, termLineDelay } from './shared';
 
export const awsSelect1 = [
    {
        text: 'SELECT instanceType, COUNT(*)',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: 'FROM aws.ec2.instances',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: 'GROUP BY instanceType',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: "WHERE region = 'us-east-1'",
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
|  instancetype  | count |
+------------------------+
| m7g.16xlarge   |  30   |
| c7g.12xlarge   |  80   |
+------------------------+`,
        cmd: false,
    },
]

export const awsSelect2 = [
    {
        text: 'SELECT instanceState, COUNT(*)',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: 'FROM aws.ec2.instances',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: 'GROUP BY instanceState',
        cmd: true,
        delay: termLineDelay,
    },
    {
        text: "WHERE region = 'us-east-1'",
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
`+-----------------------+
| instancestate | count |
+-----------------------+
| running       |  13   |
| stopped       |  18   |
| stopping      |  14   |
+-----------------------+`,
        cmd: false,
    },
]
