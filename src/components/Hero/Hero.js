import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import styles from './hero.module.css';
import '@fortawesome/fontawesome-free/css/all.css';
import useBaseUrl from '@docusaurus/useBaseUrl';
import AnimatedTerm from '../AnimatedTerm';

function getOS() {
  const macosPlatforms = ['Macintosh', 'MacIntel', 'MacPPC', 'Mac68K'];
  const windowsPlatforms = ['Win32', 'Win64', 'Windows', 'WinCE'];
  let platform = navigator.userAgentData.platform;

  if (macosPlatforms.indexOf(platform) !== -1) {
    return 'Mac OS';
  } else if (windowsPlatforms.indexOf(platform) !== -1) {
    return 'Windows';
  } else {
    return 'Linux';
  }
}

const DownloadLink = () => {
  const os = getOS();
  //set downloadurl and downloadicon values based on os
  let downloadUrl = '';
  let downloadIcon = '';
  if (os === 'Mac OS') {
    downloadUrl = '/downloads/macos';
    downloadIcon = 'fab fa-apple';
  } else if (os === 'Windows') {
    downloadUrl = '/downloads/windows';
    downloadIcon = 'fab fa-windows';
  } else {
    downloadUrl = '/downloads/linux';
    downloadIcon = 'fab fa-linux';
  }

  return(
    <Link
    className="button button--info button--lg"
    to={downloadUrl}>
    <span><i class={downloadIcon}></i></span>&nbsp;&nbsp;Download for {os}
    </Link>
  );
};


const Hero = props => {
    const { data } = props;
    return (
      <header className={clsx('hero hero--primary', styles.heroBanner)}>
        <div className="container">
          <div className="row">
            <div className="col col--6">
                <h1 className="hero__title">
                    {data.title.map((text, i) => (
                    (data.title.length - 1 === i) ? <span>{text}</span> : <span>{text}<br /></span>
                    ))}
                </h1>
                <p className="hero__subtitle">{data.subtitle}</p>
                <div className={styles.buttons}>
                      <DownloadLink />
                </div>
                <div className={styles.allPlatformsDiv}>
                <Link className={styles.allPlatformsLink} to="/downloads">Download for other platforms{' >'}</Link>
                </div>
            </div>
            <div className="col col--6">
                <AnimatedTerm data={data.animatedTerm} />
            </div>
          </div>
        </div>
      </header>
    );
};

export default Hero;