import React, { useEffect, useRef, useState } from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useBaseUrl from '@docusaurus/useBaseUrl';
import { MdDownload, MdArrowDropDown } from "react-icons/md";
import dropdownStyles from './dropdownbinarydownloadlink.module.css';

const DropDownBinaryDownloadLink = props => {
  const { styles, iconSize, to, text, options = [] } = props;
  const [open, setOpen] = useState(false);
  const containerRef = useRef(null);
  const buttonClasses = clsx('button', styles ? styles : 'button--outline button--primary');

  useEffect(() => {
    const handleClickOutside = event => {
      if (containerRef.current && !containerRef.current.contains(event.target)) {
        setOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return(
    <div ref={containerRef} className={dropdownStyles.container}>
      <div className={dropdownStyles.splitButton}>
        <Link
        className={clsx(buttonClasses, dropdownStyles.mainButton)}
        to={useBaseUrl(to)}>
        <div style={{display: "flex", justifyContent: "center"}}>
          <span>{text}&nbsp;&nbsp;</span><MdDownload size={iconSize}/>
        </div>
        </Link>
        <button
        type="button"
        aria-label="More download options"
        aria-haspopup="true"
        aria-expanded={open}
        className={clsx(buttonClasses, dropdownStyles.caretButton)}
        onClick={() => setOpen(!open)}>
          <MdArrowDropDown size={iconSize}/>
        </button>
      </div>
      {open && (
        <ul className={dropdownStyles.menu}>
          {options.map(option => (
            <li key={option.to}>
              <Link
              className={dropdownStyles.menuItem}
              to={option.to}
              onClick={() => setOpen(false)}>
                <span>{option.text}&nbsp;&nbsp;</span><MdDownload size={iconSize}/>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default DropDownBinaryDownloadLink;
