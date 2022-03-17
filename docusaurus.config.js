// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const TwitterSvg =
  '<svg style="fill: #1DA1F2; vertical-align: middle;" width="16" height="16" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M459.37 151.716c.325 4.548.325 9.097.325 13.645 0 138.72-105.583 298.558-298.558 298.558-59.452 0-114.68-17.219-161.137-47.106 8.447.974 16.568 1.299 25.34 1.299 49.055 0 94.213-16.568 130.274-44.832-46.132-.975-84.792-31.188-98.112-72.772 6.498.974 12.995 1.624 19.818 1.624 9.421 0 18.843-1.3 27.614-3.573-48.081-9.747-84.143-51.98-84.143-102.985v-1.299c13.969 7.797 30.214 12.67 47.431 13.319-28.264-18.843-46.781-51.005-46.781-87.391 0-19.492 5.197-37.36 14.294-52.954 51.655 63.675 129.3 105.258 216.365 109.807-1.624-7.797-2.599-15.918-2.599-24.04 0-57.828 46.782-104.934 104.934-104.934 30.213 0 57.502 12.67 76.67 33.137 23.715-4.548 46.456-13.32 66.599-25.34-7.798 24.366-24.366 44.833-46.132 57.827 21.117-2.273 41.584-8.122 60.426-16.243-14.292 20.791-32.161 39.308-52.628 54.253z"></path></svg>';

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'StackQL',
  tagline: 'Provision and Query Cloud and SaaS Resources using SQL',
  url: 'https://stackql.io',
  baseUrl: '/',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'img/favicon.ico',
  organizationName: 'stackql', 
  projectName: 'stackql.io', 

  plugins: [
    '@docusaurus/plugin-ideal-image',
    'docusaurus-plugin-hubspot',
    'docusaurus-plugin-smartlook',
  ],

  presets: [
    [
      '@docusaurus/preset-classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        gtag: {
          trackingID: 'G-M7GH68KJ3F',
          anonymizeIP: true,
        },        
        sitemap: {},
        pages: {},
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          path: 'docs',
		      sidebarCollapsible: true, 
          editUrl: 'https://github.com/stackql/stackql.io/edit/main/',
        },
        blog: {
          path: 'blog',
          blogTitle: 'StackQL Blog',
          blogDescription: 'Cloud operations, security and automation using SQL',
          postsPerPage: 5,
          blogSidebarTitle: 'All posts',
          blogSidebarCount: 'ALL',
          feedOptions: {
            type: 'all',
            title: 'StackQL Blog Feed',
            description: 'Cloud operations, security and automation using SQL',
            copyright: `Copyright © ${new Date().getFullYear()} StackQL Studios`,
          },
          showReadingTime: true,
        },
        theme: {
          customCss: require.resolve('./src/css/global.css'),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
    announcementBar: {
      id: 'support_us',
      content:
        `<b>If you like StackQL, give it a ⭐️ on <a target="_blank" rel="noopener noreferrer" href="https://github.com/stackql/stackql">GitHub</a> and follow us on <a target="_blank" rel="noopener noreferrer" href="https://twitter.com/stackql" >Twitter</a></b> ${TwitterSvg}`,
      backgroundColor: '#A9BCD0',
      textColor: '#1A4E82',
      isCloseable: true,
    },
    hubspot: {
      accountId: '21220110',
    },
    image: '/img/stackql-cover.png',        
    smartlook: {
      projectKey: '0b522f879953aea6bcb0f17f0e397f498d8eea32',
      useBetaNextGen: true,
    },    
    algolia: {
      appId: 'HYO8BM1W1Y',
      apiKey: 'a67a2d399f2e604202e82f8fd284b103',
      indexName: 'stackql',
      // Optional: see doc section below
      contextualSearch: false,
      // Optional: Specify domains where the navigation should occur through window.location instead on history.push. Useful when our Algolia config crawls multiple documentation sites and we want to navigate with window.location.href to them.
      // externalUrlRegex: 'external\\.com|domain\\.com',
      // Optional: Algolia search parameters
      // searchParameters: {},
      //... other Algolia params
    },    
    hideableSidebar: true,  
    navbar: {
      logo: {
        alt: 'StackQL',
        href: '/',
        src: 'img/logo-original.svg',
        srcDark: 'img/logo-white.svg',
      },
      items: [
        {
          href: '/features',
          label: 'Features',
          position: 'left',
        },
        {
          href: '/downloads',
          label: 'Downloads',
          position: 'left',
        },        
        {
          to: 'docs',
          activeBaseRegex: '^((?!/blog).)*$',
          label: 'Documentation',
          position: 'left',
        },
        {
          to: 'blog',
          label: 'Blog',
          activeBasePath: 'blog',
          position: 'left',
        },
        {
          href: '/contact-us',
          label: 'Contact us',
          position: 'left',
        },        
        {
          href: 'https://github.com/stackql',
          position: 'right',
          className: 'header-github-link',
          'aria-label': 'GitHub repository',
        },
      ],
    },
    footer: {
      style: 'dark',
      logo: {
        alt: 'StackQL',
        href: 'https://stackql.io/',
        src: 'img/logo-original.svg',
        srcDark: 'img/logo-white.svg',
      },
      links: [
        {
          title: 'StackQL',
          items: [
            {
              label: 'Home',
              to: '/',
            },
            {
              label: 'Features',
              to: '/features',
            },
            {
              label: 'Downloads',
              to: '/downloads',
            },
            {
              label: 'Contact us',
              to: '/contact-us',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'Documentation',
              to: '/docs',
            },
            {
              label: 'Blog',
              to: '/blog',
            },
          ],
        },
      ],
      copyright: `© ${new Date().getFullYear()} StackQL Studios (ABN 65 656 147 054)`,
    },
    colorMode: {
      // using user system preferences, instead of the hardcoded defaultMode
      respectPrefersColorScheme: true,
    },
    prism: {
      theme: lightCodeTheme,
      darkTheme: darkCodeTheme,
    },    		
	}),
};

module.exports = config;
