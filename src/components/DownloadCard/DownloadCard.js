import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useBaseUrl from '@docusaurus/useBaseUrl';
import styles from './downloadcard.module.css';
import { 
  DiApple,
  DiWindows,
  DiLinux,
} from "react-icons/di";
import { 
  FaDownload,
} from "react-icons/fa";
import CodeBlock from '@theme/CodeBlock';


const PlatformIcon = props => {
  const { platform } = props;  
  switch(platform) {
    case 'windows':
      return (
        <DiWindows />
      );
    case 'macos':
      return (
        <DiApple />
      );
    case 'linux':
      return (
        <DiLinux />
      );
    default:
      return (
        <DiLinux />
      );
  }
}

const DownloadCard = props => {
  const { data, liftUp } = props;
  
  let terminalInstallCmd = '';
  
  if (data.terminalInstallLine2) {
    terminalInstallCmd = `${data.terminalInstallLine1}\n${data.terminalInstallLine2}`;
  } else {
    terminalInstallCmd = `${data.terminalInstallLine1}`;
  }
  
  return(
    <div className={clsx('card', 'card--full-height', styles.downloadCard, liftUp ? styles.downloadCardLift : '')}>
    <div className={clsx('card__header')}>
      <div className={clsx('avatar', 'avatar--vertical')}>
        <span className={clsx(styles.downloadIcon)}>
          <PlatformIcon platform={data.platform} />
        </span>
        <div className={clsx('avatar__intro')}>
          <span className={clsx(styles.downloadTitle)}>{data.title}</span>
          <p className={clsx(styles.downloadDesc)}>{data.description}</p>
          <details>
          <summary className={clsx(styles.summaryText)}>Install using <code>{data.detailsText}</code></summary>
          <CodeBlock
            className={clsx(styles.codeBlock)}
            showLineNumbers={false}>
            {`${terminalInstallCmd}`}
          </CodeBlock>
          </details>
        </div>
      </div>
    </div>
    {/*     
    <div className="card__body text--center">{data.description}</div>
    */}
    <div className={clsx('card__footer')}>
      <div className={clsx('button-group', 'button-group--block')}>
        {data.buttons.map(button => (
          <Link
          className={clsx('button', 'button--primary button--sm')}
          to={useBaseUrl(button.url)}>
            <span><FaDownload /></span>&nbsp;&nbsp;{button.text}
          </Link>
        ))}
      </div>
    </div>
  </div>
  );
};

export default DownloadCard;