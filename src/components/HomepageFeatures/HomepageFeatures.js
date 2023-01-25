import React from 'react';
import clsx from 'clsx';
import { useColorMode } from '@docusaurus/theme-common';
import styles from './homepagefeatures.module.css';
import { 
  HiChevronDoubleRight, 
} from "react-icons/hi";
import { 
  FaRocket, 
  FaBinoculars,
  FaWrench,
  FaChartBar,
} from "react-icons/fa";

const FeatureIcon = props => {
  const { icon, iconSize, color } = props;  
  switch(icon) {
    case 'query':
      return (
        <FaBinoculars size={iconSize} color={color}/>
      );
    case 'deploy':
      return (
        <FaRocket size={iconSize} color={color}/>
      );
    case 'manage':
      return (
        <FaWrench size={iconSize} color={color}/>
      );
    default:
      return (
        <FaChartBar size={iconSize} color={color}/>
      );
  }
}

function Feature({icon, link, title, description}) {
  const {colorMode} = useColorMode();
  const lightThemeIconColor = 'grey';
  return (
    <div className={clsx('col', 'col--4', styles.featureDiv)}>
      <div className={clsx('padding-horiz--md')}>
        <h3 className={clsx(colorMode === 'dark' ? styles.featureHeadingDark : styles.featureHeading)}>
          <span><FeatureIcon color={colorMode === 'dark' ? 'white' : lightThemeIconColor} icon={icon} iconSize={25} /></span>&nbsp;&nbsp;{title}</h3>
        <p>{description}</p>
      </div>
      <div className={clsx('padding-horiz--md', styles.learnMore)}>
        <a href={link} className={clsx(colorMode === 'dark' ? styles.learnMoreLinkDark : styles.learnMoreLink)}>
          <div style={{display: "flex", justifyContent: "right"}}>
            <span>Learn more&nbsp;&nbsp;</span><HiChevronDoubleRight size={25}/>
          </div>
        </a>
      </div>
    </div>
  );
}

const HomepageFeatures = props => {
  const { data } = props;
  return (
    <section className={clsx(styles.features)}>
      <div className={clsx('container')}>
        <div className={clsx('row')}>
          {data.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
};

export default HomepageFeatures;