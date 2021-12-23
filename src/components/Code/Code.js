import React from 'react';
import Terminal from '../../modules/react-animated-term/lib'
import '../../modules/react-animated-term/dist/react-animated-term.css'

const spinner = ['⠋', '⠙', '⠹', '⠸', '⠼', '⠴', '⠦', '⠧', '⠇', '⠏']
const termLines = [
    {
        text: 'SELECT machineType, COUNT(*)',
        cmd: true,
        delay: 80,
    },
    {
        text: 'FROM google.compute.instances',
        cmd: true,
        delay: 80,
    },
    {
        text: 'GROUP BY machineType',
        cmd: true,
        delay: 80,
    },
    {
        text: "WHERE zone = 'us-east1-a'",
        cmd: true,
        delay: 80,
    },
    {
        text: '✔ Loaded app',
        cmd: false,
        repeat: true,
        repeatCount: 5,
        frames: spinner.map(function (spinner) {
        return {
            text: spinner + ' Running query',
            delay: 10
        }
    })
    },
    {
        text: `
        +------------------------+
        |  MACHINETYPE   | COUNT |
        +------------------------+
        | n1-standard-1  |   3   |
        | n1-megamem-96  |   8   |
        | c2-standard-60 |   4   |
        +------------------------+
        `,
        cmd: false,
    }
]



const Code = props => {
    const { data } = props;
    return (
        <Terminal
        lines={termLines}
        interval={80}
        white
      />

    );
};
        
export default Code;