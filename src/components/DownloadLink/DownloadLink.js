import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import '@fortawesome/fontawesome-free/css/all.css';
import useBaseUrl from '@docusaurus/useBaseUrl';
import useIsBrowser from '@docusaurus/useIsBrowser';

function getOS() {
  const macosPlatforms = ['Macintosh', 'MacIntel', 'MacPPC', 'Mac68K'];
  const windowsPlatforms = ['Win32', 'Win64', 'Windows', 'WinCE'];
  let platform = navigator.userAgentData.platform;

  if (macosPlatforms.indexOf(platform) !== -1) {
    return 'Mac OS';
  } else if (windowsPlatforms.indexOf(platform) !== -1) {
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