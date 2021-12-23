import React from 'react';
import Terminal from '../../modules/react-animated-term/lib'
import '../../modules/react-animated-term/dist/react-animated-term.css'

const spinner = ['⠋', '⠙', '⠹', '⠸', '⠼', '⠴', '⠦', '⠧', '⠇', '⠏']
const termLines = [
  {
    text: 'SELECT * FROM something',
    prompt1: true,
    cmd: true,
    delay: 80
  },
  {
    text: 'WHERE something=something;',
    prompt2: true,
    cmd: true,
    delay: 80
  },
  {
    text: '✔ Loaded app',
    result: true,
    repeat: true,
    repeatCount: 5,
    frames: spinner.map(function (spinner) {
      return {
        text: spinner + ' Loading app',
        delay: 40
      }
    })
  },
  {
    text: 'HERES YOUR RESULT',
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