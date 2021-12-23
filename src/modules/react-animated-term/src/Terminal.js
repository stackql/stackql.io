import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

const cursor = <span className="Terminal-cursor" />
// const prompt1 = <span className="Terminal-prompt">$ StackQL* {'>> '}</span>
const prompt2 = <span className="Terminal-prompt">$ {'>> '}</span>

const keywords = [
  'SELECT',
  'FROM',
  'GROUP',
  'BY',
  'WHERE',
  'AND',
  'OR',
  'LIKE',
  'IN',
  'IS',
  'NOT',
  'NULL',
  'TRUE',
  'FALSE',
];


function renderPrompt(){
    return prompt2;
}

function renderText(text){
// return a span for each word in the text
  return text.split(' ').map((word, index) => {
    const isKeyword = keywords.includes(word);
    return (
      <span
        key={index}
        className={isKeyword ? 'Terminal-keyword' : ''}
      >
        {word}{' '}
      </span>
    );
  });
}

  // return text;
//  return kewords.reduce((acc, keyword) => {
//    return acc.replace(keyword, <span className="Terminal-keyword">${keyword}</span>);
//  }, text);


const renderLines = (lines) => {
  return lines.map((line) => {
    return (
      <React.Fragment key={line.id}>
        {line.cmd ?  renderPrompt() : ''}
        {line.cmd ? renderText(line.text) : line.text}
        {line.current ? cursor : ''}
        <br />
      </React.Fragment>
    )
  })
}

const getWindowStyle = (white) => {
  return classNames({
    'Terminal-window': true,
    'Terminal-window-white': white,
  })
}

const getTerminalStyle = (code) => {
  return classNames({
    'Terminal-term': true,
    'Terminal-term-code': code,
  })
}

const getButtonStyle = (type) => {
  return classNames({
    'Terminal-btn': true,
    'Terminal-btn-close': type === 'close',
    'Terminal-btn-minimize': type === 'minimize',
    'Terminal-btn-maximize': type === 'maximize',
  })
}

const getBodyStyle = (code) => {
  return classNames({
    'Terminal-body': true,
    'Terminal-body-animated': !code,
  })
}

const getConsoleStyle = (code, white) => {
  return classNames({
    'Terminal-console': true,
    'Terminal-console-code': code,
    'Terminal-console-white': white,
  })
}

const Terminal = ({ children, white, height, code, onReplay, completed }) => {
  const btnClassName = white
    ? 'Terminal-control-btn Terminal-control-btn-white'
    : 'Terminal-control-btn'

  return (
    <div className={getWindowStyle(white)}>
      <div
        className={getTerminalStyle(code)}
        style={height ? { height } : null}
      >
        <div className="Terminal-header">
          <span className={getButtonStyle('close')} />
          <span className={getButtonStyle('minimize')} />
          <span className={getButtonStyle('maximize')} />
        </div>
        <div className={getBodyStyle(code)}>
          <div className={getConsoleStyle(code, white)}>
            {code ? (
              <code className="Terminal-code">{children}</code>
            ) : (
              <div>
                <div className="Terminal-code">{renderLines(children)}</div>
                {completed ? (
                  <a className={btnClassName} onClick={() => onReplay()}>
                    Replay
                  </a>
                ) : null}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

Terminal.propTypes = {
  children: PropTypes.oneOfType([PropTypes.array, PropTypes.string]),
  white: PropTypes.bool,
  height: PropTypes.number,
  code: PropTypes.bool,
  onReplay: PropTypes.func,
  completed: PropTypes.bool,
}

export default Terminal
