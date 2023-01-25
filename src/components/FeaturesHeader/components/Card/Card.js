import React from 'react';
import clsx from 'clsx';
import styles from './card.module.css';
import AnchorLink from 'react-anchor-link-smooth-scroll'
import { 
    FaPowerOff, 
    FaLayerGroup,
    FaRegMap, 
    FaSearch,
  } from "react-icons/fa";
import { 
HiChevronDoubleRight, 
} from "react-icons/hi";

const FeatureIcon = props => {
    const { title, className } = props;  
    switch(title) {
      case 'Provision':
        return (
          <FaLayerGroup className={className}/>
        );
      case 'Navigate':
        return (
          <FaRegMap className={className}/>
        );
      case 'Query':
        return (
          <FaSearch className={className}/>
        );
      case 'Operate':
        return (
          <FaPowerOff className={className}/>
        );        
      default:
        return (
          <FaSearch className={className}/>
        );
    }
}

const Card = props => {
    const { data, liftUp } = props;
    return (
        <div className={clsx('card', 'card--full-height', styles.featureCard, liftUp ? styles.featureCardLift : '')}>
            <div className={clsx('row')} style={{ padding: '8px', height: '42px' }}>
                <div className={clsx('col', 'col--2')}>
                    <div className={clsx(styles.featureIconContainer)}>
                        <FeatureIcon title={data.title} className={clsx(data.icon, styles.featureIcon)}/> 
                    </div>
                </div>
                <div className={clsx('col', 'col--10')}></div>
            </div>
            <div className={clsx('row')} style={{ padding: '8px' }}>
                <div className={clsx('col', 'col--12', 'padding-horiz--md', styles.featureTitle)}>
                    {data.title}
                </div>
            </div>
            <div className={clsx('row')} style={{ padding: '8px' }}>
                <div className={clsx('col', 'col--12', 'padding-horiz--md', styles.featureText)}>
                    {data.text}        
                </div> 
            </div>   
            <div className={clsx('row')} style={{ padding: '8px' }}>
                <div className={clsx('col', 'padding-horiz--md', styles.learnMore)}>
                <AnchorLink style={{display: "flex", justifyContent: "right"}} className={clsx(styles.learnMoreLink)} href={data.link}>
                    <span>Learn more&nbsp;&nbsp;</span><HiChevronDoubleRight size={25}/>
                </AnchorLink>
                </div>
            </div>
        </div>
    );
};

export default Card;