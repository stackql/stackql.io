import React from 'react';
import useThemeContext from '@theme/hooks/useThemeContext'; //docs: https://v2.docusaurus.io/docs/2.0.0-alpha.69/theme-classic#usethemecontext

const ImageSwitcher = ({lightImageSrc, darkImageSrc, alttext}) => {
  const { isDarkTheme } = useThemeContext();

  return (
    // <img src={isDarkTheme ? darkImageSrc : lightImageSrc} alt={alttext} />
    <img src={isDarkTheme ? darkImageSrc : lightImageSrc} alt={alttext} onClick={()=> window.open(lightImageSrc, "_blank")} />
  )
}

export default ImageSwitcher;