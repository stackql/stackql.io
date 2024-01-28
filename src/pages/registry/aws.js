import React from 'react';
import Head from '@docusaurus/Head';

export default function Registry() {
  return (
    <Head>
    <meta http-equiv="refresh" content="0;URL='https://registry.stackql.io/aws'" />
    </Head>
  );
};

// import React, { useEffect } from 'react';
// import { useLocation } from '@docusaurus/router';

// export default function Registry() {
//   const location = useLocation();

//   useEffect(() => {
//     // Construct the new URL by appending the path suffix to the external base URL
//     // The replace() function is used to remove '/registry' from the path
//     const newPathSuffix = location.pathname.replace('/registry', '');
//     const newUrl = `https://registry.stackql.io${newPathSuffix}`;
//     window.location.href = newUrl;
//   }, [location.pathname]); // This effect will re-run if the pathname changes

//   // Render nothing or a loading indicator as the page will redirect
//   return null;
// };
