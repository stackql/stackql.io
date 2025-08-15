import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useBaseUrl from '@docusaurus/useBaseUrl';
import { MdDownload, MdOpenInNew } from "react-icons/md";

const BinaryDownloadLink = props => {
  const { styles, iconSize, to, text, isOpenInNewTab=false } = props;
  return(
    <Link
    className={clsx('button', styles ? styles : 'button--outline button--primary')}
    to={useBaseUrl(to)}
    target={isOpenInNewTab ? '_blank' : '_self'}
    rel={isOpenInNewTab ? 'noopener noreferrer' : undefined}>
    <div style={{display: "flex", justifyContent: "center"}}>
      <span>{text}&nbsp;&nbsp;</span>{isOpenInNewTab  ? <MdOpenInNew size={iconSize}/> : <MdDownload size={iconSize}/>}
    </div>
    </Link>
  );
};

export default BinaryDownloadLink;