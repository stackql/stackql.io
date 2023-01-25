import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useBaseUrl from '@docusaurus/useBaseUrl';
import { MdLibraryBooks } from "react-icons/md";

const DocumentationLink = props => {
  const { styles, iconSize } = props;
  const docs = useBaseUrl('/docs');
  return(
    <Link
    className={clsx('button', styles ? styles : 'button--outline button--primary')}
    to={docs}>
    <div style={{display: "flex", justifyContent: "center"}}>
      <MdLibraryBooks size={iconSize}/><span>&nbsp;&nbsp;Read the Docs</span>
    </div>
    </Link>
  );
};

export default DocumentationLink;