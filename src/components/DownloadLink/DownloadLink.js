import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import '@fortawesome/fontawesome-free/css/all.css';
import useIsBrowser from '@docusaurus/useIsBrowser';

function getOS() {
  let platform = 'Linux';
  try {
    platform = navigator.userAgentData.platform;
  } catch (ignore) {
    //browser does not support this, so catch error and continue
  }

  if (platform.match(/(.*?)(M|m)(A|a)(C|c)(.*?)/)) {
    return 'Mac OS';
  } else if (platform.match(/(.*?)(W|w)(I|i)(N|n)(.*?)/)) {
    return 'Windows';
  } else {
    return 'Linux';
  }
}

const DownloadLink = props => {
  const { styles } = props;
  const isBrowser = useIsBrowser();
  const os = isBrowser ? getOS() : 'Windows';
  let downloadUrl = '';
  let downloadIcon = '';
  if (os === 'Mac OS') {
    downloadUrl = 'https://releases.stackql.io/stackql/latest/infraql_darwin_multiarch.pkg';
    downloadIcon = 'fab fa-apple';
  } else if (os === 'Windows') {
    downloadUrl = 'https://releases.stackql.io/stackql/latest/infraql_windows_amd64.msi';
    downloadIcon = 'fab fa-windows';
  } else {
    downloadUrl = 'https://releases.stackql.io/stackql/latest/infraql_linux_amd64.zip';
    downloadIcon = 'fab fa-linux';
  }

  return(
    <Link
    className={clsx('button', styles)}
    to={downloadUrl}>
    <span><i class={downloadIcon}></i></span>&nbsp;&nbsp;Download for {os}
    </Link>
  );
};

export default DownloadLink;