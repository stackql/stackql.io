import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import styles from './featurescontent.module.css';
import '@fortawesome/fontawesome-free/css/all.css';
import useBaseUrl from '@docusaurus/useBaseUrl';
import AnimatedTerm from '../AnimatedTerm';
import DownloadLink from '../DownloadLink';
import MediaQuery from 'react-responsive'
import BrowserOnly from '@docusaurus/BrowserOnly';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { 
    twilight, 
    atomDark,
    materialDark,
    materialOceanic,
    nord,
    okaidia,
    tomorrow
} from 'react-syntax-highlighter/dist/esm/styles/prism';
import { 
    irBlack,
    obsidian,
    vs2015,
    nightOwl,
    stackoverflowDark

} from 'react-syntax-highlighter/dist/cjs/styles/hljs';
//import SyntaxHighlighter from 'react-syntax-highlighter';
//import { docco } from 'react-syntax-highlighter/dist/esm/styles/hljs';

/*
   {
      title: 'Every Cloud.',
      code: 'cloud',
      checks: [
        'something',
        'something',
        'something',
      ],
    },
*/
const CodeDiv = props => {
    const {code} = props;
    return (
        <div className="col col--6 padding-top--md margin-bottom--md">
            <SyntaxHighlighter language="sql" style={materialOceanic}>
                {code}
            </SyntaxHighlighter>
        </div>
    );
};

const ContentDiv = props => {
    const {title, checks} = props;
    return (
        <div className="col col--6 padding-top--md margin-bottom--md">
            <div className="row">
                <div className="col col--12">
                    <h1 className="margin-bottom--sm">{title}</h1>
                    <ul style={{listStyleType: 'none', paddingLeft: 0}}>
                        {
                            checks.map(check => (
                                <div className="row">
                                    <div className="col col--12">
                                        <li>
                                            <span style={{fontSize: '2rem', color: 'green', verticalAlign: 'middle'}}>
                                                <i class="fas fa-check-circle"></i>
                                            </span>
                                            <span style={{ marginLeft: '1rem' }}>{check}</span>
                                        </li>
                                    </div>                                                            
                                </div>
                            ))
                        }
                    </ul>
                </div>
            </div>  
        </div>
    );
};

const FeatureContent = props => {
    const { feature, idx } = props;
    return (
        <div className="row">
            {idx % 2 == 0 ? 
                <>
                <CodeDiv code={feature.code} />
                <ContentDiv title={feature.title} checks={feature.checks}/>
                </>
            : 
                <>
                <ContentDiv title={feature.title} checks={feature.checks}/>            
                <CodeDiv code={feature.code} />
                </>
            }
        </div>
    );
};

const FeaturesContent = props => {
    const { data } = props;
    return (
        <div className="container">
            <div className="row">
                <div className="col" style={{marginLeft: '3rem', marginRight: '3rem'}}>
                    {
                        data.map((feature, idx) => (
                            <FeatureContent feature={feature} idx={idx} />
                        ))
                    }
                </div>
            </div>
        </div>
        );
    };
    
export default FeaturesContent;        