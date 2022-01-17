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
            <div className={clsx('row')}>
                <div className={clsx('col', 'col--6', styles.cardsContainer)}>
                <div className={clsx('row')}>
                    <div className={clsx('col', 'col--2')}></div>
                    <div className={clsx('col', 'col--5', styles.cardsLeftCol)}>
                        <div className={clsx('row', 'margin-bottom--md', 'margin-right--sm')}>
                            <Card data={data.cards[0]} liftUp />
                        </div>
                        <div className={clsx('row', 'margin-right--sm')}>
                            <Card data={data.cards[1]} liftUp />
                        </div>
                    </div>
                    <div className={clsx('col', 'col--5', styles.cardsRightCol)}>
                        <div className={clsx('row', 'margin-bottom--md', 'margin-right--sm')}>
                            <Card data={data.cards[2]} liftUp />
                        </div>
                        <div className={clsx('row', 'margin-right--sm')}>
                            <Card data={data.cards[3]} liftUp />
                        </div>                            
                    </div>
                    </div>
                </div>
                <div className={clsx('col', 'col--6')}>
                <div className={clsx('row')} style={{height: '20%'}}></div>
                <div className={clsx('row')}>
                <SectionHeader
                    title={data.title}
                    subtitle={data.subtitle}
                    align="left"
                    label={data.label}
                    ctaGroup = {[
                        <MediaQuery minWidth={1224}>
                        <div className={clsx(buttonStyles.buttons)}>
                            <DownloadLink styles={['button--primary']}/>
                        </div>
                        </MediaQuery>,
                        <MediaQuery minWidth={1224}>
                        <div style={{width: "2em"}}></div>
                        </MediaQuery>,
                        <div className={clsx(buttonStyles.buttons)}>
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