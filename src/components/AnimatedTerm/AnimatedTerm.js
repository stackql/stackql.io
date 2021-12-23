import React from 'react';
import styles from './animatedterm.module.css';
import Code from '../Code';

const AnimatedTerm = props => {
    const { data } = props;
    return (
    <div className={[styles.marvelDevice, styles.macbook].join(" ")}>
      <div className={styles.notch}>
          <div className={styles.camera}></div>
          <div className={styles.speaker}></div>
      </div>
      <div className={styles.topBar}></div>
      <div className={styles.sleep}></div>
      <div className={styles.bottomBar}></div>
      <div className={styles.volume}></div>
	  <div className={styles.screen}>
          <Code />
      </div>
    </div>
    );
};
    
export default AnimatedTerm;