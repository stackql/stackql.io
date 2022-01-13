import React, { useState, useEffect } from 'react';
import clsx from 'clsx';
import {
    CodeDiv,
    ContentDiv,
    Cards,
} from './components';
import Link from '@docusaurus/Link';
import styles from './featurescontent.module.css';
import '@fortawesome/fontawesome-free/css/all.css';
import useBaseUrl from '@docusaurus/useBaseUrl';
import AnimatedTerm from '../AnimatedTerm';
import DownloadLink from '../DownloadLink';
import MediaQuery from 'react-responsive'
import BrowserOnly from '@docusaurus/BrowserOnly';
import useIsBrowser from '@docusaurus/useIsBrowser';


import AOS from 'aos';
import 'aos/dist/aos.css';

const FeatureContent = props => {
    const { feature, idx } = props;
    return (
        <div className="row">
            {idx % 2 == 0 ? 
                <>
                <CodeDiv code={feature.code} />
                <ContentDiv title={feature.title} checks={feature.checks} isRight/>
                </>
            : 
                <>
                <ContentDiv title={feature.title} checks={feature.checks}/>            
                <CodeDiv code={feature.code} isRight/>
                </>
            }
        </div>
    );
};


//AOS.init({ 
//    duration: '2000', 
//    disable: 'mobile'
//    });

const FeaturesContent = props => {
    const { data } = props;
    const isBrowser = useIsBrowser();
    React.useEffect(() => {
        AOS.init({
          once: true,
          delay: 50,
          duration: 500,
          easing: 'ease-in-out',
        });
      }, []);    

    return (
        <>
        <div className="lgContainer">
            <div className="row">
                {/* <div className="col margin-horiz--xl"> */}
                <>
                <div className="col col--2"></div>
                <div className="col col--8">
                <div className='row'>
                    <div className={clsx('col', 'col--12', styles.title)}>
                        <span><span style={{color: '#00af91'}}>One Solution</span> for everything</span>
                    </div>
                </div>
                <div className='row'>
                    <div className={clsx('col', 'col--12', 'margin-bottom--lg', styles.subtitle)}>
                        What do you need to do?
                    </div>
                </div>


                    <div className="row">
                        <Cards data={data.cards} />
                    </div>
                    {
                        data.features.map((feature, idx) => (
                            <FeatureContent feature={feature} idx={idx} />
                        ))
                    }
                </div>
                <div className="col col--2"></div>
                </>
            </div>
        </div>
        </>
        );
    };
    
export default FeaturesContent;        