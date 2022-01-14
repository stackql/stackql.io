import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import MediaQuery from 'react-responsive';
import AnimatedTerm from '../AnimatedTerm';
import DownloadLink from '../DownloadLink';

import styles from './hero.module.css';

const Hero = props => {
    const { data } = props;
    return (
      <header className={clsx('hero', 'hero--primary', styles.heroBanner)}>
        <div className="container">
          <div className="row">
            <div className={clsx('col', 'col--6', styles.heroColDiv)}>
                <h1 className={clsx('hero__title')}>
                    {data.title.map((text, i) => (
                    (data.title.length - 1 === i) ? <span>{text}</span> : <span>{text}<br /></span>
                    ))}
                </h1>
                <p className={clsx('hero__subtitle')}>{data.subtitle}</p>
                <MediaQuery minWidth={768}>
                  <div className="row">
                    <div className={styles.buttons}>
                      <DownloadLink styles={['button--info', 'button--lg']}/>
                    </div>
                    <div className={styles.allPlatformsDiv}>
                      <Link className={styles.allPlatformsLink} to="/downloads">Download for other platforms{' >'}</Link>
                    </div>
                  </div>
                </MediaQuery>
            </div>
            <div className={clsx('col', 'col--6')}>
                <AnimatedTerm data={data.animatedTerm} />
            </div>
          </div>
        </div>
      </header>
    );
};

export default Hero;