import React from 'react';
import clsx from 'clsx';
import MediaQuery from 'react-responsive'
import {
  SectionHeader,
  DownloadLink,
  DocumentationLink,
} from '../../components';
import {
    Card,
} from './components';

import buttonStyles from '../Hero/hero.module.css';
import styles from './featuresheader.module.css';

const FeaturesHeader = props => {
    const { data } = props;
    return (    
        <>
        <div className={clsx('col', 'col--12')}>
            <div className="row">
                <div className={clsx('col', 'col--6', styles.cardsContainer)}>
                    <div className={clsx('col', 'col--2')}></div>
                    <div className={clsx('col', 'col--5', styles.cardsLeftCol)}>
                        <div className="row margin-bottom--md margin-right--sm">
                            <Card data={data.cards[0]} liftUp />
                        </div>
                        <div className="row margin-right--sm">
                            <Card data={data.cards[1]} liftUp />
                        </div>
                    </div>
                    <div className={clsx('col', 'col--5', styles.cardsRightCol)}>
                        <div className="row margin-bottom--md margin-right--sm">
                            <Card data={data.cards[2]} liftUp />
                        </div>
                        <div className="row margin-right--sm">
                            <Card data={data.cards[3]} liftUp />
                        </div>                            
                    </div>
                </div>
                <div className={clsx('col', 'col--6')}>
                <div className={clsx('row')} style={{height: '20%'}}></div>
                <div className={clsx('row')}>
                <SectionHeader
                //title= {<span>Cloud <span style={{color: '#00af91'}}>Everything</span> as SQL</span>}
                title={data.title}
                subtitle={data.subtitle}
                align="left"
                label={data.label}
                ctaGroup = {[
                    <MediaQuery minWidth={1224}>
                    <div className={buttonStyles.buttons}>
                        <DownloadLink styles={['button--primary']}/>
                    </div>
                    </MediaQuery>,
                    <MediaQuery minWidth={1224}>
                    <div style={{width: "2em"}}></div>
                    </MediaQuery>,
                    <div className={buttonStyles.buttons}>
                    <DocumentationLink />
                    </div>
                ]}
                />
                </div>
                </div>
            </div>
        </div>
        </>
    );
};

export default FeaturesHeader;