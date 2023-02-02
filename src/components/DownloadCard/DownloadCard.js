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
          {/* <code style={{backgroundColor: 'rgb(55 65 81)', color: 'white', paddingLeft: 10, paddingRight: 10}}>{data.terminalInstall}</code>&nbsp;&nbsp;{data.orText} */}
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