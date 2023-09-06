import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useIsBrowser from '@docusaurus/useIsBrowser';
import { 
  DiApple,
  DiChrome,
  DiWindows,
  DiLinux,
} from "react-icons/di";

const PlatformIcon = props => {
    const { icon, iconSize } = props;  
  switch(icon) {
    case 'Mac OS':
      return (
        <DiApple size={iconSize}/>
      );
    case 'Windows':
      return (
        <DiWindows size={iconSize}/>
      );
    case 'Chrome OS':
      return (
        <DiChrome size={iconSize}/>
      );
    default:
      return (
        <DiLinux size={iconSize}/>
      );
  }
}

function getOS() {
  let platform = 'Linux';
  try {
    platform = navigator.userAgentData.platform;
    if (platform.match(/(.*?)(M|m)(A|a)(C|c)(.*?)/)) {
      return 'Mac OS';
    } else if (platform.match(/(.*?)(W|w)(I|i)(N|n)(.*?)/)) {
      return 'Windows';
    } else if (platform === 'Chrome OS'){
      return 'Chrome OS';
    } else {
      return 'Linux';
    }    
  } catch (ignore) {
    //browser does not support this, so catch error and continue
    return 'Linux';
  }
}

const DownloadLink = props => {
  const { styles, iconSize } = props;
  const isBrowser = useIsBrowser();
  const os = isBrowser ? getOS() : 'Windows';
  let downloadUrl = '';
  if (os === 'Mac OS') {
    downloadUrl = 'https://releases.stackql.io/stackql/latest/stackql_darwin_multiarch.pkg';
  } else if (os === 'Windows') {
    downloadUrl = 'https://releases.stackql.io/stackql/latest/stackql_windows_amd64.msi';
  } else if (os === 'Chrome OS') {
    downloadUrl = 'https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip';
  } else {
    downloadUrl = 'https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip';
  }

  return(
    <Link
    className={clsx('button', styles)}
    to={downloadUrl}>
    <div style={{display: "flex", justifyContent: "center"}}>
      <PlatformIcon iconSize={iconSize} icon={os}/><span>&nbsp;&nbsp;Download for {os}</span>
    </div>    
    </Link>
  );
};

export default DownloadLink;